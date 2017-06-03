package bing.system.web.controller;

import java.util.Date;
import java.util.List;

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
import bing.system.condition.SysMenuCondition;
import bing.system.constant.SystemMessageKeys;
import bing.system.domain.MenuTreeNode;
import bing.system.model.SysMenu;
import bing.system.model.SysOperateLog;
import bing.system.model.SysResource;
import bing.system.model.SysUser;
import bing.system.service.SysMenuService;
import bing.system.service.SysOperateLogService;
import bing.system.service.SysResourceService;
import bing.system.vo.SysMenuVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysMenuController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysMenuController.class);

	private static final String MODULE_NAME = "菜单管理";

	private static final String LOG_PREFIX = LogPrefixes.MENU;
	private static final String PREFIX = "system/menu";
	private static final String AJAX_MENU_LIST = "ajax/system/menu/list";
	private static final String AJAX_MENU_TREE = "ajax/system/menu/tree";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	private static final String REQUEST_ATTRIBUTE_PARENT_MENU = "parentMenu";
	private static final String REQUEST_ATTRIBUTE_PARENT_MENU_ID = "parentId";
	private static final String REQUEST_ATTRIBUTE_RESOURCE = "resource";

	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysResourceService sysResourceService;

	@Autowired
	private SysOperateLogService sysOperateLogService;

	@ResponseBody
	@RequestMapping(value = AJAX_MENU_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysMenuVO>> menus(SysMenuCondition condition) {
		RestResponse<GenericPage<SysMenuVO>> response = new RestResponse<>();
		GenericPage<SysMenuVO> page = sysMenuService.listByPage(condition);
		response.setData(page);
		return response;
	}

	/**
	 * 菜单树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_MENU_TREE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<MenuTreeNode>> menuTree() {
		RestResponse<List<MenuTreeNode>> response = new RestResponse<>();
		List<MenuTreeNode> menus = sysMenuService.getMenuTree();
		response.setData(menus);
		return response;
	}

	/**
	 * 菜单树(排除自己)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_MENU_TREE + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<MenuTreeNode>> menuTreeExclude(@PathVariable(required = true) Integer id) {
		RestResponse<List<MenuTreeNode>> response = new RestResponse<>();
		List<MenuTreeNode> menus = sysMenuService.getMenuTree(id);
		response.setData(menus);
		return response;
	}

	@RequestMapping(LIST)
	public String list(@RequestParam(name = "parentId", required = false) Integer parentId, Model model) {
		model.addAttribute(REQUEST_ATTRIBUTE_PARENT_MENU_ID, parentId);
		return LIST;
	}

	@RequestMapping(ADD)
	public String add(@RequestParam(name = "parentId", required = true) Integer parentId, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysMenu());
		SysMenu parentMenu = sysMenuService.getById(parentId);
		if (parentMenu == null) {
			setError(SystemMessageKeys.PARENT_MENU_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(REQUEST_ATTRIBUTE_PARENT_MENU, parentMenu);
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated SysMenu entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		model.addAttribute(REQUEST_ATTRIBUTE_PARENT_MENU_ID, entity.getParentId());
		if (hasErrors(bindingResult, model)) {
			prepareParentMenu(entity.getParentId(), model);
			return ADD;
		}
		try {
			Date now = new Date();
			entity.setCreateUser(currentUser.getName());
			entity.setCreateDate(now);
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(now);
			sysMenuService.save(entity);
			String operateContent = "添加了菜单[" + entity + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}保存异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			prepareParentMenu(entity.getParentId(), model);
			return ADD;
		}
		setMessage(MessageKeys.SAVE_SUCCESS, model);
		return LIST;
	}

	@RequestMapping(EDIT)
	public String edit(@RequestParam(value = "id", required = true) Integer id, Model model) {
		SysMenu entity = sysMenuService.getById(id);
		if (entity == null) {
			setError(MessageKeys.ENTITY_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		prepareParentMenu(entity.getParentId(), model);
		prepareResource(entity.getResourceId(), model);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysMenu entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			prepareParentMenu(entity.getParentId(), model);
			prepareResource(entity.getResourceId(), model);
			return EDIT;
		}
		try {
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(new Date());
			sysMenuService.update(entity);
			String operateContent = "修改了菜单信息[" + entity + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}更新异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			prepareParentMenu(entity.getParentId(), model);
			return EDIT;
		}
		setMessage(MessageKeys.UPDATE_SUCCESS, model);
		prepareResource(entity.getResourceId(), model);
		return LIST;
	}

	@RequestMapping(DELETE)
	public String delete(@RequestParam(value = "id", required = true) Integer id, @RequestParam(name = "parentId", required = false) Integer parentId, Model model,
			@CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(REQUEST_ATTRIBUTE_PARENT_MENU_ID, parentId);
		try {
			String username = currentUser.getUsername();
			sysMenuService.deleteById(id, username);
			String operateContent = "删除了菜单[" + id + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_DELETE, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

	/**
	 * 准备上级菜单对象，用于前端页面显示
	 * 
	 * @param parentMenuId
	 * @param model
	 */
	private void prepareParentMenu(Integer parentMenuId, Model model) {
		SysMenu parentMenu = sysMenuService.getById(parentMenuId);
		model.addAttribute(REQUEST_ATTRIBUTE_PARENT_MENU, parentMenu);
	}

	/**
	 * 准备资源对象，用户前端页面显示
	 * 
	 * @param resourceId
	 * @param model
	 */
	private void prepareResource(Integer resourceId, Model model) {
		SysResource resource = sysResourceService.getById(resourceId);
		if (resource == null) {
			resource = new SysResource();
		}
		model.addAttribute(REQUEST_ATTRIBUTE_RESOURCE, resource);
	}

}
