package ru.greenatom.atsdb.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import ru.greenatom.atsdb.model.dto.request.LoginRequest;
import ru.greenatom.atsdb.model.dto.request.RegisterRequest;
import ru.greenatom.atsdb.model.dto.response.CookieResponse;
import ru.greenatom.atsdb.model.service.UserService;
import ru.greenatom.atsdb.security.service.AuthService;

@Controller
@RequestMapping("${app.path.authentication.main}")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	@GetMapping("${app.path.authentication.login}")
    public String login(Model model) {
        
		log.info("#login Login page");

		authService.authLinks(model);
		
		LoginRequest user = new LoginRequest("", "");
		model.addAttribute("user", user);
		
		return "login";
    }

    @PostMapping("${app.path.authentication.signin}")
	public String authenticateUser(@Valid @ModelAttribute("user") LoginRequest request, 
									BindingResult result, 
									Model model, 
									HttpServletResponse httpServletResponse) throws Exception {
		log.info("#authenticateUser username = " + request.username() + ", password = " + request.password());

		if (result.hasErrors()) {
            log.error("#authenticateUser Validation has error");
			model.addAttribute("user", request);
            return "login";
        }

		CookieResponse response = authService.authenticate(request);
		httpServletResponse.addCookie(response.accessCookie());
		httpServletResponse.addCookie(response.refreshCookie());
		return "redirect:/ats/mainMenu";
	}

	@GetMapping("/register")
    public String registrationForm(Model model){
        
		authService.authLinks(model);
		
		RegisterRequest user = new RegisterRequest();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") RegisterRequest userdto,
                               BindingResult result,
                               Model model) throws Exception {
        log.info(userdto.toString());
		
        if (result.hasErrors()) {
            log.error("Validation has error");
			model.addAttribute("user", userdto);
            return "register";
        }

		if(userService.registration(userdto)) {
			return "redirect:/auth/register?success";
		} else {
			model.addAttribute("errorMessage", "Registration error");
			return "register";
		}
    }
	
	@PostMapping("${app.path.authentication.refresh-token}")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("#refreshToken");
		try {
			response.addCookie(authService.refreshToken(request));
			log.info("#refreshToken: Success refreshing.");
		} catch (OAuth2AuthenticationException e) {
			log.warn("#refreshToken: refrashing failed: " + e.getError().getErrorCode());
		}
	}
}
