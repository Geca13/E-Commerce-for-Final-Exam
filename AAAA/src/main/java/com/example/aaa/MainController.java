package com.example.aaa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.service.EmailAllreadyExistExceptionMessage;
import com.example.aaa.users.service.InvalidPasswordException;
import com.example.aaa.users.service.UsersService;

@Controller
@RequestMapping("/signUpForm")
public class MainController {
	
	private UsersService usersService;

	public MainController(UsersService usersService) {
		super();
		this.usersService = usersService;
	}
	
	@ModelAttribute("user")
	public Users userDto() {
		return new Users();
	}
	
	
	@GetMapping
	public String showSignUpForm(Model model) {
		
	    return "signUpForm";
	}
	
	@PostMapping
	public String registerUser(@ModelAttribute("user") Users userDto, Model model) {
		
		try {
			usersService.save(userDto);
		} catch (InvalidPasswordException | EmailAllreadyExistExceptionMessage e) {
			model.addAttribute("error", e.getMessage());
			return "signUpForm";
		}
		
		return  "redirect:/signUpForm?success" ;
	}
	
	
}


