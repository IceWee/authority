package bing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bing.service.MessageSourceService;

@Controller
public class LoginController {
	
	@Autowired
	private MessageSourceService messageSourceService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView("login");
		model.addObject("msg", messageSourceService.getMessage("login.label.username"));
		if (error != null) {
			model.addObject("msg", messageSourceService.getMessage("login.tips.invalid"));
		}
		if (logout != null) {
			model.addObject("msg", messageSourceService.getMessage("logout.tips.success"));
		}
		return model;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)  
    public String logout(HttpServletRequest request, HttpServletResponse response) {  
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();  
        if (auth != null){      
            new SecurityContextLogoutHandler().logout(request, response, auth);  
        }  
        return "redirect:/login?logout";  
    }  

}
