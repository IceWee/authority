package bing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.annotation.CurrentUser;
import bing.constants.SystemConstants;
import bing.model.SysUser;

@Controller
public class MainController {

	@RequestMapping(value = { "/main", "/" })
	public String main(@CurrentUser SysUser user, HttpSession session) {
		session.setAttribute(SystemConstants.SESSION_ATTRIBUTE_CURRENT_USER, user);
		return "main";
	}

}
