package ru.greenatom.atsdb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.log4j.Log4j2;

@RequestMapping(value = "${app.path.access}")
@Controller
@Log4j2
public class MyController {
    
	@GetMapping("/admin")
	public String homeAdmin(Model model) {
		log.info("homeAdmin");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("message", "Hello mr. " + auth.getName() + ". Authorities: " + auth.getAuthorities());
        return "fragments/admin :: admin";
	}
	
	@GetMapping("/user")
	public String homeUser(Model model) {
		log.info("homeUser");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("message", "Hello mr. " + auth.getName() + ". Authorities: " + auth.getAuthorities());
        return "fragments/user :: user";
	}

	@GetMapping("/test")
    public String test(Model model){
        log.info("Test page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("message", "Hello mr. " + auth.getName() + ". Authorities: " + auth.getAuthorities());
        //log.info(auth.getAuthorities());
        return "fragments/test :: test";
    }
}
