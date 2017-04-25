package bing.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bing.conditions.SysUserCondition;
import bing.domain.RestResponse;

@Controller
public class SysUserController {

	@RequestMapping("/user/list")
	public String list(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "name", required = false) String name) {
		return "user/list";
	}

	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<Object> getUsers(SysUserCondition sysUserCondition) {
		RestResponse<Object> response = new RestResponse<>();
		return response;
	}

}
