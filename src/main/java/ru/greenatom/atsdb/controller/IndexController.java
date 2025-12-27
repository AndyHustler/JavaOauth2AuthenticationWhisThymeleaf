package ru.greenatom.atsdb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.greenatom.atsdb.html.NavBarLink;
import ru.greenatom.atsdb.security.config.AuthentinticotionUrlConfig;

@Log4j2
@Controller
@RequiredArgsConstructor
public class IndexController {
    
    private final AuthentinticotionUrlConfig url; 

    @GetMapping("${app.path.access}/mainMenu")
    public String home(Model model){

        log.info("#home user = " + SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("mainLink", NavBarLink.mainMenu());

        List<NavBarLink> links = new ArrayList<NavBarLink>();
        links.add(new NavBarLink("/ats/test","Test"));
        links.add(new NavBarLink("/ats/user","User page"));
        links.add(new NavBarLink("/ats/admin","Admin page"));
        model.addAttribute("links", links);

        model.addAttribute("loginUrl", url.getLoginUrl());
        model.addAttribute("refreshUrl", url.getRefreshUrl());

        model.addAttribute("message", "Ресурс доступен только для авторизованных пользователей!");

        return "index";
    }

    @GetMapping("/")
    public String redirectToAuth() {
        return "redirect:" + url.getLoginUrl();
    }
}
