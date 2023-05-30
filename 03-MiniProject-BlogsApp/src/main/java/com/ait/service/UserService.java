package com.ait.service;

import com.ait.dto.LoginForm;
import com.ait.entity.UserEntity;
import com.ait.util.ServiceMsg;

public interface UserService {
	public ServiceMsg register(UserEntity user);

	public ServiceMsg login(LoginForm loginData);
	
	public boolean checkUser();
	
	public void logout();

}
