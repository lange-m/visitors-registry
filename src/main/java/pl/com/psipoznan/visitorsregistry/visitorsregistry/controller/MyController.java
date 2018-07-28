package pl.com.psipoznan.visitorsregistry.visitorsregistry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {


	@RequestMapping("/home")
	public ModelAndView home() {
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/")
	public String root() {
		return "index";
	}
	
	@GetMapping("/secured")
    public String securedIndex() {
        return "secured/index";
    }
	
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	
	@GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }
}
