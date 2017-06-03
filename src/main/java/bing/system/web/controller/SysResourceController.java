package bing.system.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericPage;
import bing.domain.LabelValueBean;
import bing.system.condition.SysResourceCondition;
import bing.system.constant.ResourceTypeEnum;
import bing.system.constant.SystemMessageKeys;
import bing.system.domain.ResourceTreeNode;
import bing.system.model.SysOperateLog;
import bing.system.model.SysResource;
import bing.system.model.SysResourceCategory;
import bing.system.model.SysUser;
import bing.system.service.SysOperateLogService;
import bing.system.service.SysResourceService;
import bing.system.service.SysRoleResourceService;
import bing.system.vo.SysResourceVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.api.RestResponseCodes;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysResourceController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysResourceController.class);

	private static final String MODULE_NAME = "资源管理";

	private static final String LOG_PREFIX = LogPrefixes.RESOURCE;
	private static final String PREFIX = "system/resource";
	private static final String AJAX_RESOURCE_TREE = "ajax/system/resource/tree";
	private static final String AJAX_RESOURCE_LIST = "ajax/system/resource/list";
	private static final String AJAX_RESOURCE_SAVE = "ajax/system/resource/save";

	private static final String AJAX_CATEGORY_TREE = "ajax/system/category/tree";
	private static final String AJAX_CATEGORY_SAVE = "ajax/system/category/save";
	private static final String AJAX_CATEGORY_UPDATE = "ajax/system/category/update";
	private static final String AJAX_CATEGORY_DELETE = "ajax/system/category/delete";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	private static final String REQUEST_ATTRIBUTE_CATEGORY = "category";
	private static final String REQUEST_ATTRIBUTE_CATEGORY_ID = "categoryId";

	@Autowired
	private SysResourceService sysResourceService;

	@Autowired
	private SysRoleResourceService sysRoleResourceService;

	@Autowired
	private SysOperateLogService sysOperateLogService;

	@ModelAttribute("typeList")
	protected List<LabelValueBean> typeList() {
		List<LabelValueBean> typeList = new ArrayList<>();
		typeList.add(new LabelValueBean("一般功能", String.valueOf(ResourceTypeEnum.FUNCTION.ordinal())));
		typeList.add(new LabelValueBean("数据接口", String.valueOf(ResourceTypeEnum.DATA_API.ordinal())));
		typeList.add(new LabelValueBean("其他", String.valueOf(ResourceTypeEnum.OTHER.ordinal())));
		return typeList;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_RESOURCE_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysResourceVO>> resouces(SysResourceCondition condition) {
		RestResponse<GenericPage<SysResourceVO>> response = new RestResponse<>();
		GenericPage<SysResourceVO> page = sysResourceService.listByPage(condition);
		response.setData(page);
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
	@RequestMapping(value = AJAX_RESOURCE_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveRoleResources(Integer roleId, Integer[] resourceIds, @CurrentLoggedUser SysUser currentUser) {
		RestResponse<Object> response = new RestResponse<>();
		String username = currentUser.getUsername();
		sysRoleResourceService.saveRoleResources(roleId, resourceIds, username);
		String operateContent = "配置了角色与资源关系，角色ID[" + roleId + "]，资源ID列表[" + Arrays.toString(resourceIds) + "]";
		sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
		return response;
	}

	/**
	 * 资源分类树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_CATEGORY_TREE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<ResourceTreeNode>> categoryTree() {
		RestResponse<List<ResourceTreeNode>> response = new RestResponse<>();
		List<ResourceTreeNode> categories = sysResourceService.getCategoryTree();
		response.setData(categories);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_CATEGORY_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> categorySave(@RequestBody @Validated SysResourceCategory entity, BindingResult bindingResult, @CurrentLoggedUser SysUser currentUser) {
		RestResponse<Object> response = new RestResponse<>();
		if (hasErrors(bindingResult)) {
			response.setCode(RestResponseCodes.VALIDATE_FAILED);
			response.setMessage(getError(bindingResult));
			return response;
		}
		sysResourceService.saveCategory(entity);
		String operateContent = "添加了资源分类[" + entity + "]";
		sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_CATEGORY_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> categoryUpdate(@RequestBody @Validated SysResourceCategory entity, BindingResult bindingResult, @CurrentLoggedUser SysUser currentUser) {
		RestResponse<Object> response = new RestResponse<>();
		if (hasErrors(bindingResult)) {
			response.setCode(RestResponseCodes.VALIDATE_FAILED);
			response.setMessage(getError(bindingResult));
			return response;
		}
		sysResourceService.updateCategory(entity);
		String operateContent = "修改了资源分类信息[" + entity + "]";
		sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_CATEGORY_DELETE + "/{categoryId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> categoryDelete(@PathVariable Integer categoryId, @CurrentLoggedUser SysUser currentUser) {
		RestResponse<Object> response = new RestResponse<>();
		String username = currentUser.getUsername();
		sysResourceService.deleteCategoryById(categoryId, username);
		String operateContent = "删除了资源分类[" + categoryId + "]";
		sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_DELETE, currentUser.getId(), currentUser.getName(), operateContent));
		return response;
	}

	/**
	 * 资源树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_RESOURCE_TREE + "/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<ResourceTreeNode>> resourceTreeByRole(@PathVariable Integer roleId) {
		RestResponse<List<ResourceTreeNode>> response = new RestResponse<>();
		List<ResourceTreeNode> resources = sysResourceService.getResourceTree(roleId);
		response.setData(resources);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_RESOURCE_TREE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<ResourceTreeNode>> resourceTree() {
		RestResponse<List<ResourceTreeNode>> response = new RestResponse<>();
		List<ResourceTreeNode> resources = sysResourceService.getResourceTree();
		response.setData(resources);
		return response;
	}

	@RequestMapping(LIST)
	public String list(@RequestParam(name = "categoryId", required = false) Integer categoryId, Model model) {
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY_ID, categoryId);
		return LIST;
	}

	@RequestMapping(ADD)
	public String add(@RequestParam(value = "categoryId", required = true) Integer categoryId, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysResource());
		SysResourceCategory category = sysResourceService.getCategoryById(categoryId);
		if (category == null) {
			setError(SystemMessageKeys.RESOURCE_CATEGORY_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY, category);
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated SysResource entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		Integer categoryId = entity.getCategoryId();
		if (hasErrors(bindingResult, model)) {
			prepareCategory(categoryId, model);
			return ADD;
		}
		try {
			Date now = new Date();
			entity.setCreateUser(currentUser.getName());
			entity.setCreateDate(now);
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(now);
			sysResourceService.save(entity);
			String operateContent = "添加了资源[" + entity + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_ADD, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}保存异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			prepareCategory(categoryId, model);
			return ADD;
		}
		setMessage(MessageKeys.SAVE_SUCCESS, model);
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY_ID, categoryId);
		return LIST;
	}

	@RequestMapping(EDIT)
	public String edit(@RequestParam(value = "categoryId", required = false) Integer categoryId, @RequestParam(value = "id", required = true) Integer id, Model model) {
		SysResource entity = sysResourceService.getById(id);
		if (entity == null) {
			setError(MessageKeys.ENTITY_NOT_EXIST, model);
			model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY_ID, categoryId);
			return LIST;
		}
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		prepareCategory(entity.getCategoryId(), model);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysResource entity, BindingResult bindingResult, Model model, @CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		Integer categoryId = entity.getCategoryId();
		if (hasErrors(bindingResult, model)) {
			prepareCategory(categoryId, model);
			return EDIT;
		}
		try {
			entity.setUpdateUser(currentUser.getName());
			entity.setUpdateDate(new Date());
			sysResourceService.update(entity);
			String operateContent = "修改了资源信息[" + entity + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_MODIFY, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}更新异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			prepareCategory(categoryId, model);
			return EDIT;
		}
		setMessage(MessageKeys.UPDATE_SUCCESS, model);
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY_ID, categoryId);
		return LIST;
	}

	@RequestMapping(DELETE)
	public String delete(@RequestParam(value = "id", required = true) Integer id, @RequestParam(value = "categoryId", required = false) Integer categoryId, Model model,
			@CurrentLoggedUser SysUser currentUser) {
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY_ID, categoryId);
		try {
			String username = currentUser.getUsername();
			sysResourceService.deleteById(id, username);
			String operateContent = "删除了资源[" + id + "]";
			sysOperateLogService.log(new SysOperateLog(MODULE_NAME, SysOperateLog.OPERATE_DELETE, currentUser.getId(), currentUser.getName(), operateContent));
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

	/**
	 * 准备资源分类对象，用于前端页面显示
	 * 
	 * @param categoryId
	 * @param model
	 */
	private void prepareCategory(Integer categoryId, Model model) {
		SysResourceCategory category = sysResourceService.getCategoryById(categoryId);
		model.addAttribute(REQUEST_ATTRIBUTE_CATEGORY, category);
	}

}
