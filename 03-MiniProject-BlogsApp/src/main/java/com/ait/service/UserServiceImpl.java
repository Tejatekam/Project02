package com.ait.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ait.dao.UserDao;
import com.ait.dto.LoginForm;
import com.ait.entity.UserEntity;
import com.ait.util.ServiceMsg;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private HttpSession session;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public ServiceMsg register(UserEntity user) {
		if (user.getFname().isBlank() || user.getLname().isBlank() || user.getEmail().isBlank()
				|| user.getPwd().isBlank()) {
			return ServiceMsg.EMPTY_FIELD;
		}
		UserEntity u = this.userDao.findByEmail(user.getEmail());
		if (u != null)
			return ServiceMsg.ACCOUNT_EXISTS;
		
		user.setPwd(encoder.encode(user.getPwd()));
		this.userDao.save(user);
		
		return ServiceMsg.REG_SUCCESS;
	}

	@Override
	public ServiceMsg login(LoginForm loginData) {
		if (loginData.getEmail().isBlank() || loginData.getPwd().isBlank()) {
			return ServiceMsg.EMPTY_FIELD;
		}
		
		UserEntity user = this.userDao.findByEmail(loginData.getEmail());
		
		if (user == null)
			return ServiceMsg.NOUSER_FOUND;
		else if (!encoder.matches(loginData.getPwd(), user.getPwd())) {
			return ServiceMsg.INCORRECT_PWD;
		}
			
		this.session.setAttribute("user", user);
		return ServiceMsg.LOGIN_SUCCESS;
	}

	@Override
	public boolean checkUser() {
		return session.getAttribute("user") != null;
	}

	@Override
	public void logout() {
		this.session.invalidate();
	}

}
