package bing.system.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CrudGroups;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysUserCondition;
import bing.system.exception.UserExceptionCodes;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.service.SysRoleService;
import bing.system.service.SysUserService;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;
import bing.system.vo.UserRoleVO;
import bing.util.EasyPOIUtils;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysUserController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	private static final String LOG_PREFIX = LogPrefixes.USER;
	private static final String PREFIX = "system/user";
	private static final String AJAX_USER_LIST = "ajax/system/user/list";
	private static final String AJAX_USER_SAVE = "ajax/system/user/save";
	private static final String AJAX_USER_UPDATE = "ajax/system/user/update";
	private static final String AJAX_USER_PASSWORD = "ajax/system/user/password";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";
	private static final String PASSWORD = PREFIX + "/password"; // 修改密码
	private static final String MINE = PREFIX + "/mine"; // 我的信息
	private static final String LOCK = PREFIX + "/lock"; // 锁定用户
	private static final String UNLOCK = PREFIX + "/unlock"; // 解除锁定用户
	private static final String EXPORT = PREFIX + "/export"; // 导出

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;

	@ModelAttribute("roleList")
	protected List<SysRole> roleList() {
		return sysRoleService.listAll();
	}

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_USER_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysUserVO>> users(SysUserCondition condition) {
		RestResponse<GenericPage<SysUserVO>> response = new RestResponse<>();
		GenericPage<SysUserVO> page = sysUserService.listByPage(condition);
		response.setData(page);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_USER_LIST + "/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<RoleUserVO> getRoleUsers(@PathVariable Integer roleId) {
		RestResponse<RoleUserVO> response = new RestResponse<>();
		RoleUserVO data = sysUserService.getRoleUsers(roleId);
		response.setData(data);
		return response;
	}

	/**
	 * 保存角色用户关联关系
	 * 
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_USER_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveRoleUsers(Integer roleId, Integer[] userIds, @CurrentLoggedUser SysUser currentUser) {
		RestResponse<Object> response = new RestResponse<>();
		String username = currentUser.getUsername();
		sysUserService.saveRoleUsers(roleId, userIds, username);
		return response;
	}

	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysUser());
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated(CrudGroups.Create.class) SysUser entity, BindingResult bindingResult, Model model,
			@CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return ADD;
		}
		try {
			Date now = new Date();
			entity.setCreateUser(currentUser.getName());
			entity.setCreateDate(now);
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(now);
			sysUserService.save(entity);
		} catch (Exception e) {
			LOGGER.error("{}保存异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			return ADD;
		}
		setMessage(MessageKeys.SAVE_SUCCESS, model);
		return LIST;
	}

	@RequestMapping(EDIT)
	public String edit(@RequestParam(value = "id", required = true) Integer id, Model model) {
		SysUser entity = sysUserService.getById(id);
		if (entity == null) {
			setError(MessageKeys.ENTITY_NOT_EXIST, model);
			return LIST;
		}
		UserRoleVO userRoleVO = sysRoleService.getUserRoles(id);
		List<SysRole> roles = userRoleVO.getSelectedRoles();
		List<Integer> roleIdList = roles.stream().map(role -> role.getId()).collect(Collectors.toList());
		Integer[] roleIds = new Integer[roleIdList.size()];
		roleIdList.toArray(roleIds);
		entity.setRoleIds(roleIds);
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysUser entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return EDIT;
		}
		try {
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(new Date());
			sysUserService.updateWithRole(entity);
		} catch (Exception e) {
			LOGGER.error("{}更新异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			return EDIT;
		}
		setMessage(MessageKeys.UPDATE_SUCCESS, model);
		return LIST;
	}

	@RequestMapping(DELETE)
	public String delete(@RequestParam(value = "id", required = true) Integer id, Model model, @CurrentLoggedUser SysUser currentUser) {
		try {
			String username = currentUser.getName();
			sysUserService.deleteById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

	/**
	 * 当前登录用户修改密码
	 * 
	 * @return
	 */
	@RequestMapping(value = PASSWORD, method = RequestMethod.GET)
	public String changePassword() {
		return PASSWORD;
	}

	/**
	 * 保存修改密码
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_USER_PASSWORD + "/{userId}", method = RequestMethod.PUT)
	public RestResponse<Object> savePassword(@PathVariable Integer userId, String oldPassword, String newPassword) {
		sysUserService.changePassword(userId, oldPassword, newPassword);
		return new RestResponse<>();
	}

	/**
	 * 我的信息
	 * 
	 * @return
	 */
	@RequestMapping(MINE)
	public String mine(Model model, @CurrentLoggedUser SysUser currentUser) {
		SysUser entity = sysUserService.getById(currentUser.getId());
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		return MINE;
	}

	/**
	 * 保存我的信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_USER_UPDATE + "/{userId}", method = RequestMethod.PUT)
	public RestResponse<Object> updateMine(@PathVariable Integer userId, SysUser mine) {
		if (StringUtils.isBlank(mine.getName())) {
			throw new BusinessException(UserExceptionCodes.NAME_IS_NULL);
		}
		sysUserService.update(mine);
		return new RestResponse<>();
	}

	/**
	 * 锁定用户
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(LOCK)
	public String lock(@RequestParam(value = "id", required = true) Integer id, Model model, @CurrentLoggedUser SysUser currentUser) {
		try {
			String username = currentUser.getName();
			sysUserService.lockById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}锁定异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.LOCK_SUCCESS, model);
		return LIST;
	}

	/**
	 * 解除锁定用户
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(UNLOCK)
	public String unlock(@RequestParam(value = "id", required = true) Integer id, Model model, @CurrentLoggedUser SysUser currentUser) {
		try {
			String username = currentUser.getName();
			sysUserService.unlockById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}解除锁定异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.UNLOCK_SUCCESS, model);
		return LIST;
	}

	/**
	 * 数据导出
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = EXPORT)
	public ResponseEntity<byte[]> export(SysUserCondition condition) {
		try {
			condition.setPageNumber(1);
			condition.setPageSize(Integer.MAX_VALUE);
			GenericPage<SysUserVO> page = sysUserService.listByPage(condition);
			HttpHeaders headers = new HttpHeaders();
			List<SysUserVO> rows = page.getRows();
			Workbook workbook = EasyPOIUtils.exportExcel(SysUserVO.class, rows);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			byte[] bytes = baos.toByteArray();
			final String filename = "users" + System.currentTimeMillis() + ".xls";
			headers.setContentDispositionFormData("attachment", filename);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
		} catch (IOException e) {
			LOGGER.error("{}导出Excel异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			throw new RuntimeException(e);
		}

	}

}
