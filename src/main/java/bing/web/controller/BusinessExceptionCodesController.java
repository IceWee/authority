package bing.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bing.exception.BusinessExceptionCodes;
import bing.system.exception.MenuExceptionCodes;
import bing.system.exception.ResourceExceptionCodes;
import bing.system.exception.RoleExceptionCodes;
import bing.system.exception.UserExceptionCodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@Api(tags = "BusinessExceptionCodes", description = "业务异常代码表")
@RestController
public class BusinessExceptionCodesController {

	@ApiOperation(value = "基础业务异常代码", notes = "基础业务异常代码")
	@RequestMapping(value = "/businessExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = BusinessExceptionCodes.class, message = "")
	public BusinessExceptionCodes businessExceptionCodes() {
		return BusinessExceptionCodes.singleton();
	}

	@ApiOperation(value = "菜单业务异常代码", notes = "菜单业务异常代码")
	@RequestMapping(value = "/menuExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = MenuExceptionCodes.class, message = "")
	public MenuExceptionCodes menuExceptionCodes() {
		return MenuExceptionCodes.singleton();
	}

	@ApiOperation(value = "资源业务异常代码", notes = "资源业务异常代码")
	@RequestMapping(value = "/resourceExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = ResourceExceptionCodes.class, message = "")
	public ResourceExceptionCodes resourceExceptionCodes() {
		return ResourceExceptionCodes.singleton();
	}

	@ApiOperation(value = "角色业务异常代码", notes = "角色业务异常代码")
	@RequestMapping(value = "/roleExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = RoleExceptionCodes.class, message = "")
	public RoleExceptionCodes roleExceptionCodes() {
		return RoleExceptionCodes.singleton();
	}

	@ApiOperation(value = "用户业务异常代码", notes = "用户业务异常代码")
	@RequestMapping(value = "/userExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = UserExceptionCodes.class, message = "")
	public UserExceptionCodes userExceptionCodes() {
		return UserExceptionCodes.singleton();
	}

}