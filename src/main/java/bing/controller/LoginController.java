package bing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bing.service.MessageSourceService;
import bing.util.NumberUtils;

@Controller
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private MessageSourceService messageSourceService;
	
	@RequestMapping(value = "",method = RequestMethod.GET)
	public String index() {
		LOGGER.info("欢迎访问  {}", messageSourceService.getMessage("system.name"));
        return "login";
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login() {
        return "login";
    }
	
}
