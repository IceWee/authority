package bing.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bing.exception.BusinessExceptionCodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@Api(tags = "BusinessExceptionCodes", description = "业务异常代码表")
@RestController
public class BusinessExceptionCodesController {

	@ApiOperation(value = "业务异常代码", notes = "业务异常代码")
	@RequestMapping(value = "/businessExceptionCodes", method = RequestMethod.GET)
	@ApiResponse(code = 200, response = BusinessExceptionCodes.class, message = "")
	public BusinessExceptionCodes businessExceptionCodes() {
		return BusinessExceptionCodes.singleton();
	}

}
