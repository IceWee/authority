package bing.system.web.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.GlobalConstants;
import bing.constant.LogPrefixes;
import bing.constant.MessageKeys;
import bing.domain.CrudGroups;
import bing.domain.GenericPage;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.service.SysUserService;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.GenericController;

@Controller
@RequestMapping("/")
public class SysUserController extends GenericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	private static final String LOG_PREFIX = LogPrefixes.USER;
	private static final String PREFIX = "system/user";
	private static final String AJAX_LIST = "ajax/system/users";

	private static final String LIST = PREFIX + "/list";
	private static final String ADD = PREFIX + "/add";
	private static final String SAVE = PREFIX + "/save";
	private static final String EDIT = PREFIX + "/edit";
	private static final String UPDATE = PREFIX + "/update";
	private static final String DELETE = PREFIX + "/delete";

	@Autowired
	private SysUserService sysUserService;

	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<GenericPage<SysUserVO>> users(SysUserCondition condition) {
		RestResponse<GenericPage<SysUserVO>> response = new RestResponse<>();
		GenericPage<SysUserVO> page = sysUserService.listByPage(condition);
		response.setData(page);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = AJAX_LIST + "/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<RoleUserVO> getRoleUsers(@PathVariable Integer roleId) {
		RestResponse<RoleUserVO> response = new RestResponse<>();
		RoleUserVO data = sysUserService.getRoleUsers(roleId);
		response.setData(data);
		return response;
	}

	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, new SysUser());
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated(CrudGroups.Create.class) SysUser entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return ADD;
		}
		try {
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
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		return EDIT;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysUser entity, BindingResult bindingResult, Model model) {
		model.addAttribute(GlobalConstants.REQUEST_ATTRIBUTE_BEAN, entity);
		if (hasErrors(bindingResult, model)) {
			return EDIT;
		}
		try {
			sysUserService.update(entity);
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
			sysUserService.deleteById(id, username);
		} catch (Exception e) {
			LOGGER.error("{}删除异常：\n{}", LOG_PREFIX, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(MessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

}
