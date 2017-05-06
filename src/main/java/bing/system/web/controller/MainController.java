package bing.system.web.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import bing.constant.GlobalConstants;
import bing.domain.GenericTreeNode;
import bing.system.model.SysUser;
import bing.system.service.SysMenuService;
import bing.util.JsonUtils;
import bing.web.controller.GenericController;

@Controller
public class MainController extends GenericController {

	@Autowired
	private SysMenuService sysMenuService;

	@RequestMapping(value = {"/main", "/"})
	public String main(HttpSession session, Model model) {
		Optional<SysUser> optional = getCurrentUser();
		if (optional.isPresent()) {
			session.setAttribute(GlobalConstants.SESSION_ATTRIBUTE_CURRENT_USER, optional.get());
		} else {
			return "redirect:/login";
		}
		SysUser sysUser = optional.get();
		List<GenericTreeNode> menus = sysMenuService.listMenuByUserId(sysUser.getId());
		System.out.println(JsonUtils.toString(menus));
		model.addAttribute("menus", menus);
		return "main";
	}

}
