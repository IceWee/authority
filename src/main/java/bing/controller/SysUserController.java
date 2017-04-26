package bing.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.conditions.SysUserCondition;
import bing.constants.RestResponseCodes;
import bing.domain.GenericPage;
import bing.domain.RestResponse;
import bing.model.SysUser;
import bing.service.SysUserService;

@Controller
public class SysUserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 跳转到用户列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/user/list")
	public String list() {
		return "user/list";
	}

	/**
	 * ajax获取用户列表数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> getUsers(SysUserCondition sysUserCondition) {
		RestResponse<Object> response = new RestResponse<>();
		GenericPage<SysUser> page = sysUserService.listByPage(sysUserCondition);
		response.setData(page);
		return response;
	}

	/**
	 * 跳转到新增/编辑用户页面
	 * 
	 * @return
	 */
	@RequestMapping("/user/detail")
	public String detail(@RequestParam(value = "id", required = false) Integer id, Model model) {
		SysUser sysUser;
		if (id != null) {
			sysUser = sysUserService.get(id);
		} else {
			sysUser = new SysUser();
		}
		model.addAttribute("sysUser", sysUser);
		return "user/detail";
	}

	/**
	 * ajax保存或更新用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveOrUpdate(@RequestBody @Validated SysUser dto, BindingResult bindingResult) {
		RestResponse<Object> response = new RestResponse<>();
		if (bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String message = errors.get(0).getDefaultMessage();
			LOGGER.warn("保存用户时，未通过数据校验，详细信息：{}", message);
			response.setCode(RestResponseCodes.PARAM_INVALID);
			response.setMessage(message);
			return response;
		}
		return response;
	}

}
