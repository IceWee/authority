package bing.system.web.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.GenericPage;
import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.model.SysUser;
import bing.system.service.SysResourceService;
import bing.system.vo.SysResourceCategoryVO;
import bing.system.vo.SysResourceVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysResourceController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysResourceController.class);

	private static final String LOG_PREFIX = LogPrefixes.RESOURCE;
	private static final String PREFIX = "system/resource";
	private static final String AJAX_LIST = "ajax/system/resources";
	private static final String AJAX_CATEGORY_TREE = "ajax/system/category/tree";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	@Autowired
	private SysResourceService sysResourceService;

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysResourceVO>> resouces(SysResourceCondition condition) {
		RestResponse<GenericPage<SysResourceVO>> response = new RestResponse<>();
		GenericPage<SysResourceVO> page = sysResourceService.listByPage(condition);
		response.setData(page);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_CATEGORY_TREE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<List<SysResourceCategoryVO>> categoryTree() {
		RestResponse<List<SysResourceCategoryVO>> response = new RestResponse<>();
		List<SysResourceCategoryVO> categories = sysResourceService.getCategoryTree();
		response.setData(categories);
		return response;
	}

	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysResource());
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated SysResource entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return ADD;
		}
		try {
			sysResourceService.save(entity);
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
		SysResource entity = sysResourceService.getById(id);
		if (entity == null) {
			setError(MessageKeys.ENTITY_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysResource entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return EDIT;
		}
		try {
			sysResourceService.update(entity);
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
			sysResourceService.deleteById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

}
