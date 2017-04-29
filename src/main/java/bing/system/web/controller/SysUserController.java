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

import bing.constants.RestResponseCodes;
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
	private static final String EDIT = "system/user/edit";

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
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> getUsers(SysUserCondition sysUserCondition) {
		RestResponse<Object> response = new RestResponse<>();
		GenericPage<SysUser> page = sysUserService.listByPage(sysUserCondition);
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

	/**
	 * 跳转到编辑用户页面
	 * 
	 * @return
	 */
	@RequestMapping(EDIT)
	public String edit(@RequestParam(value = "id", required = true) Integer id, Model model) {
		SysUser sysUser = sysUserService.get(id);
		model.addAttribute("sysUser", sysUser);
		return EDIT;
	}

	/**
	 * ajax保存或更新用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> saveOrUpdate(@RequestBody @Valid SysUser sysUser, BindingResult bindingResult) {
		RestResponse<Object> response = new RestResponse<>();
		if (bindingResult.hasErrors() && !bindingResult.getAllErrors().isEmpty()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String message = errors.get(0).getDefaultMessage();
			LOGGER.warn("保存用户时，数据未通过校验，详细信息：{}", message);
			response.setCode(RestResponseCodes.PARAM_INVALID);
			response.setMessage(message);
			return response;
		}
		sysUserService.saveOrUpdate(sysUser);
		return response;
	}

}
