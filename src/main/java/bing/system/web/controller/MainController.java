package bing.system.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.constant.GlobalConstants;
import bing.domain.CurrentLoggedUser;
import bing.domain.GenericTreeNode;
import bing.system.model.SysUser;
import bing.system.service.SysMenuService;
import bing.web.controller.GenericController;

@Controller
public class MainController extends GenericController {

	@Autowired
	private SysMenuService sysMenuService;

	@RequestMapping(value = { "/main", "/" })
	public String main(HttpSession session, Model model, @CurrentLoggedUser SysUser currentUser) {
		if (currentUser != null) {
			session.setAttribute(GlobalConstants.SESSION_ATTRIBUTE_CURRENT_USER, currentUser);
		} else {
			return "redirect:/login";
		}
		List<GenericTreeNode> menus = sysMenuService.listMenuByUserId(currentUser.getId());
		model.addAttribute("menus", menus);
		return "main";
	}

	@RequestMapping(value = "/accessDenied")
	public String accessDenied() {
		return "accessDenied";
	}

}
