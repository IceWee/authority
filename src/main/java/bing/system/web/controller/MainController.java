package bing.system.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.constant.GlobalConstants;
import bing.domain.CurrentLoggedUser;
import bing.security.SecurityConstants;
import bing.system.domain.MenuTreeNode;
import bing.system.model.SysUser;
import bing.system.service.SysMenuService;
import bing.web.controller.GenericController;

@Controller
public class MainController extends GenericController {

	@Autowired
	private SysMenuService sysMenuService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/index", "/"})
	public String main(HttpSession session, @CurrentLoggedUser SysUser currentUser) {
		if (currentUser != null) {
			session.setAttribute(GlobalConstants.SESSION_ATTRIBUTE_CURRENT_USER, currentUser);
		} else {
			return "redirect:/login";
		}
		List<MenuTreeNode> menus = (List<MenuTreeNode>) session.getAttribute(GlobalConstants.SESSION_ATTRIBUTE_MENUS);
		if (menus == null) {
			menus = sysMenuService.listMenuByUserId(currentUser.getId());
			session.setAttribute(GlobalConstants.SESSION_ATTRIBUTE_MENUS, menus);
		}
		return "index";
	}

	@RequestMapping(value = SecurityConstants.URI_ACCESS_DENIED)
	public String accessDenied() {
		return "accessDenied";
	}

}
