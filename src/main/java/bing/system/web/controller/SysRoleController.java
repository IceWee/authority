package bing.system.web.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericPage;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.service.SysRoleService;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysRoleVO;
import bing.system.vo.UserRoleVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysRoleController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

	private static final String LOG_PREFIX = LogPrefixes.ROLE;
	private static final String PREFIX = "system/role";
	private static final String AJAX_ROLE_LIST = "ajax/system/role/list";
	private static final String AJAX_ROLE_SAVE = "ajax/system/role/save";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	@Autowired
	private SysRoleService sysRoleService;

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_ROLE_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysRoleVO>> roles(SysRoleCondition condition) {
		RestResponse<GenericPage<SysRoleVO>> response = new RestResponse<>();
		GenericPage<SysRoleVO> page = sysRoleService.listByPage(condition);
		response.setData(page);
		return response;
	}

	/**
	 * 获取用户角色列表
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_ROLE_LIST + "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<UserRoleVO> getUserRoles(@PathVariable Integer userId) {
		RestResponse<UserRoleVO> response = new RestResponse<>();
		UserRoleVO data = sysRoleService.getUserRoles(userId);
		response.setData(data);
		return response;
	}

	/**
	 * 保存用户角色列表
	 * 
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_ROLE_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<RoleUserVO> saveUserRoles(Integer userId, Integer[] roleIds, @CurrentLoggedUser SysUser currentUser) {
		String username = currentUser.getUsername();
		sysRoleService.saveUserRoles(userId, roleIds, username);
		return new RestResponse<>();
	}

	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysRole());
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated SysRole entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
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
			sysRoleService.save(entity);
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
		SysRole entity = sysRoleService.getById(id);
		if (entity == null) {
			setError(MessageKeys.ENTITY_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysRole entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return EDIT;
		}
		try {
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(new Date());
			sysRoleService.update(entity);
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
			String username = currentUser.getUsername();
			sysRoleService.deleteById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

}
