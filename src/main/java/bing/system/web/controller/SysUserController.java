package bing.system.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.domain.GenericPage;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.service.SysUserService;
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
	private static final String AJAX_LIST = "ajax/system/users";
	private static final String AJAX_SAVE = "ajax/system/users";

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
	public String add() {
		return ADD;
	}

	@RequestMapping(value = SAVE, method = RequestMethod.POST)
	public String save(@RequestBody @Valid SysUser sysUser, BindingResult bindingResult, Model model) {
		if (hasErrors(bindingResult, model)) {
			return ADD;
		}
		sysUserService.saveUser(sysUser);
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
		model.addAttribute("sysUser", sysUser);
		return EDIT;
	}

	/**
	 * ajax保存或更新用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_SAVE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveOrUpdate(@RequestBody @Valid SysUser sysUser, BindingResult bindingResult) {
		RestResponse<Object> response = new RestResponse<>();
		if (bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String message = errors.get(0).getDefaultMessage();
			LOGGER.warn("保存用户时，数据未通过校验，详细信息：{}", message);
			response.setMessage(message);
			return response;
		}
		sysUserService.saveUser(sysUser);
		return response;
	}

}
