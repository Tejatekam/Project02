package com.ait.service;

import com.ait.binding.LoginForm;
import com.ait.binding.SignUpForm;
import com.ait.binding.UnlockForm;

public interface UserService {
	
	
	
	public boolean signUp(SignUpForm form);
	
	public boolean unlockAccount(UnlockForm form);
	
	public String login(LoginForm form);
	
	public boolean forgotPwd(String email);

}
