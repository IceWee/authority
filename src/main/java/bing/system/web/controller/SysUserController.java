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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.constant.LogPrefixes;
import bing.domain.CrudGroups;
import bing.domain.GenericPage;
import bing.system.condition.SysUserCondition;
import bing.system.constant.UserMessageKeys;
import bing.system.model.SysUser;
import bing.system.service.SysUserService;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;
import bing.web.controller.AbstractController;

@Controller
@RequestMapping("/")
public class SysUserController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	private static final String LIST = "system/user/list";
	private static final String ADD = "system/user/add";
	private static final String SAVE = "system/user/save";
	private static final String EDIT = "system/user/edit";
	private static final String UPDATE = "system/user/update";
	private static final String DELETE = "system/user/delete";
	private static final String AJAX_LIST = "ajax/system/users";

	private static final String ATTR_SYS_USER = "sysUser";

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 跳转到用户列表页面
	 * 
	 * @return
	 */
	@RequestMapping(LIST)
	public String list() {
		return LIST;
	}

	/**
	 * ajax获取用户列表数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> users(SysUserCondition sysUserCondition) {
		RestResponse<Object> response = new RestResponse<>();
		GenericPage<SysUser> page = sysUserService.listUserByPage(sysUserCondition);
		response.setData(page);
		return response;
	}

	/**
	 * 跳转到新增用户页面
	 * 
	 * @return
	 */
	@RequestMapping(ADD)
	public String add(Model model) {
		model.addAttribute("sysUser", new SysUser());
		return ADD;
	}

	/**
	 * 新增用户
	 * 
	 * @param sysUser
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@Validated(CrudGroups.Create.class) SysUser sysUser, BindingResult bindingResult, Model model) {
		if (hasErrors(bindingResult, model)) {
			model.addAttribute(ATTR_SYS_USER, sysUser);
			return ADD;
		}
		try {
			sysUserService.saveUser(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("{}保存用户异常：\n{}", LogPrefixes.USER, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			return ADD;
		}
		setMessage(UserMessageKeys.SAVE_SUCCESS, model);
		return LIST;
	}

	/**
	 * 跳转到编辑用户页面
	 * 
	 * @return
	 */
	@RequestMapping(EDIT)
	public String edit(@RequestParam(value = "id", required = true) Integer id, Model model) {
		SysUser sysUser = sysUserService.getUserById(id);
		if (sysUser == null) {
			setError(UserMessageKeys.USER_NOT_EXIST, model);
			return LIST;
		}
		model.addAttribute(ATTR_SYS_USER, sysUser);
		return EDIT;
	}

	/**
	 * 更新用户
	 * 
	 * @param sysUser
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	public String update(@Validated SysUser sysUser, BindingResult bindingResult, Model model) {
		if (hasErrors(bindingResult, model)) {
			model.addAttribute(ATTR_SYS_USER, sysUser);
			return EDIT;
		}
		try {
			sysUserService.updateUser(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("{}更新用户异常：\n{}", LogPrefixes.USER, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
			return EDIT;
		}
		setMessage(UserMessageKeys.UPDATE_SUCCESS, model);
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
			sysUserService.deleteUserById(id, username);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("{}删除用户异常：\n{}", LogPrefixes.USER, ExceptionUtils.parseStackTrace(e));
			setError(e, model);
		}
		setMessage(UserMessageKeys.DELETE_SUCCESS, model);
		return LIST;
	}

}
