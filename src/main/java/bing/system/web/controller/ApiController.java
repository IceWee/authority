package bing.system.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bing.web.api.RestResponse;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

	@RequestMapping(value = "/v1/authenticate", method = RequestMethod.POST)
	public RestResponse<Object> authentication() {
		return new RestResponse<Object>();
	}

	@RequestMapping(value = "/v1/users", method = RequestMethod.GET)
	public RestResponse<Object> users() {
		return new RestResponse<Object>();
	}

}
