package bing.system.web.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.constant.GlobalConstants;
import bing.system.model.SysUser;
import bing.web.controller.GenericController;

@Controller
public class MainController extends GenericController {

	@RequestMapping(value = {"/main", "/"})
	public String main(HttpSession session) {
		Optional<SysUser> optional = getCurrentUser();
		if (optional.isPresent()) {
			session.setAttribute(GlobalConstants.SESSION_ATTRIBUTE_CURRENT_USER, optional.get());
		} else {
			return "redirect:/login";
		}
		return "main";
	}

}
