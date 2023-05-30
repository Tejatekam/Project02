package com.ait.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ait.binding.LoginForm;
import com.ait.binding.SignUpForm;
import com.ait.binding.UnlockForm;
import com.ait.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String handleSignUp(@ModelAttribute("user") SignUpForm form, Model model) {
		boolean status = userService.signUp(form);
		if (status) {
			model.addAttribute("successMsg", "Account created,Check Your Email");
		} else {
			model.addAttribute("errorMsg", "Choose Unique Emil");
		}
		return "signUp";

	}

	@GetMapping("/signup")
	public String signUpPage(Model model) {

		model.addAttribute("user", new SignUpForm());
		return "signUp";
	}

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {

		UnlockForm unlockFormObj = new UnlockForm();
		unlockFormObj.setEmail(email);

		model.addAttribute("unlock", unlockFormObj);
		return "unlock";
	}
    @PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm unlock, Model model) {

		if (unlock.getNewPassword().equals(unlock.getConfirmPassword())) {

			boolean status = userService.unlockAccount(unlock);
			if (status) {
				model.addAttribute("succMsg", "Your account Unlocked");
			} else {
				model.addAttribute("errorMsg", "Temporary password in incorrect,Check ypur email");
			}
		} else {
			model.addAttribute("errorMsg", "New Pwd and Confirm Pwd are not matched");
		}

		return "unlock";

	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute("loginForm") LoginForm form,Model model) {
		
		String status = userService.login(form);
		if(status.contains("Success")) {
			return "redirect:/dashboard";
		}
		model.addAttribute("errorMsg", status);
		return "login";
	}

	@GetMapping("/forgotPwd")
	public String forgotPwdPage() {
		return "forgotPwd";
	}
	@PostMapping("/forgotPwd")
	public String forgotPwd(@RequestParam("email") String email,Model model) {
		System.out.println(email);
		
		boolean status = userService.forgotPwd(email);
		
		if(status) {
			model.addAttribute("successMsg","Pwd sent to your email");
		}else {
			model.addAttribute("errorMsg","Invalid Email");
		}
		
		return "forgotPwd";
		
		
	}

}
