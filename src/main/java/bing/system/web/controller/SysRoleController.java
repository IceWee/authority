package bing.system.web.controller;

import java.util.List;
import java.util.Optional;

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
import bing.domain.GenericPage;
import bing.domain.GenericTreeNode;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.service.SysResourceService;
import bing.system.service.SysRoleResourceService;
import bing.system.service.SysRoleService;
import bing.system.service.SysUserService;
import bing.system.vo.SysRoleVO;
import bing.system.vo.UserRoleVO;
import bing.util.ExceptionUtils;
import bing.util.StringUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysRoleController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

	private static final String LOG_PREFIX = LogPrefixes.ROLE;
	private static final String PREFIX = "system/role";
	private static final String AJAX_LIST = "ajax/system/roles";
	private static final String AJAX_ROLE_USERS = "ajax/system/role/users";
	private static final String AJAX_ROLE_RESOURCES = "ajax/system/role/resources";
	private static final String AJAX_RESOURCE_TREE = "ajax/system/role/resources/tree";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysResourceService sysResourceService;

	@Autowired
	private SysRoleResourceService sysRoleResourceService;

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	@RequestMapping(value = AJAX_LIST + "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<UserRoleVO> getUserRoles(@PathVariable Integer userId) {
		RestResponse<UserRoleVO> response = new RestResponse<>();
		UserRoleVO data = sysRoleService.getUserRoles(userId);
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
	@RequestMapping(value = AJAX_ROLE_USERS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveRoleUsers(Integer roleId, Integer[] userIds) {
		RestResponse<Object> response = new RestResponse<>();
		Optional<SysUser> optional = getCurrentUser();
		String username = StringUtils.EMPTY;
		if (optional.isPresent()) {
			username = optional.get().getUsername();
		}
		sysUserService.saveRoleUsers(roleId, userIds, username);
		return response;
	}

	/**
	 * 保存角色资源关联关系
	 * 
	 * @param roleId
	 * @param resourceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_ROLE_RESOURCES, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveRoleResources(Integer roleId, Integer[] resourceIds) {
		RestResponse<Object> response = new RestResponse<>();
		Optional<SysUser> optional = getCurrentUser();
		String username = StringUtils.EMPTY;
		if (optional.isPresent()) {
			username = optional.get().getUsername();
		}
		sysRoleResourceService.saveRoleResources(roleId, resourceIds, username);
		return response;
	}

	/**
	 * 资源树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_RESOURCE_TREE + "/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<GenericTreeNode>> resourceTree(@PathVariable Integer roleId) {
		RestResponse<List<GenericTreeNode>> response = new RestResponse<>();
		List<GenericTreeNode> resources = sysResourceService.getResourceTree(roleId);
		response.setData(resources);
		return response;
	}

	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysRole());
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated SysRole entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return ADD;
		}
		try {
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
	public String update(@Validated SysRole entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return EDIT;
		}
		try {
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
	public String delete(@RequestParam(value = "id", required = true) Integer id, Model model) {
		try {
			Optional<SysUser> optional = getCurrentUser();
			String username = StringUtils.EMPTY;
			if (optional.isPresent()) {
				username = optional.get().getUsername();
			}
			sysRoleService.deleteById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

}
