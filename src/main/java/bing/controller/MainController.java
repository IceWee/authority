package bing.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.constants.SystemConstants;
import bing.model.SysUser;

@Controller
public class MainController extends AbstractController {

	@RequestMapping(value = { "/main", "/" })
	public String main(HttpSession session) {
		Optional<SysUser> optional = getCurrentUser();
		if (optional.isPresent()) {
			session.setAttribute(SystemConstants.SESSION_ATTRIBUTE_CURRENT_USER, optional.get());
		} else {
			return "redirect:/login";
		}
		return "main";
	}

}
