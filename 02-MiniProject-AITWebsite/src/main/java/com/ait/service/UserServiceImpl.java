package com.ait.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.binding.LoginForm;
import com.ait.binding.SignUpForm;
import com.ait.binding.UnlockForm;
import com.ait.entity.UserDetailsEntity;
import com.ait.repository.UserDetailsRepository;
import com.ait.utility.EmailUtils;
import com.ait.utility.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailsRepository userdtlsRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;

	@Override
	public boolean signUp(SignUpForm form) {
		
		UserDetailsEntity user= userdtlsRepo.findByEmail(form.getEmail());
		
		if(user==null) {
			return false;
		}
		//TODO: Copy data from binding object to entity object
		UserDetailsEntity entity = new UserDetailsEntity();
		BeanUtils.copyProperties(form, entity);
		
		// TODO: generate random password and set to object
		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);
		
		// TODO: set account status as LOCKED
		entity.setAccStatus("LOCKED");
		
		// TODO: Insert record
		userdtlsRepo.save(entity);
		
		// TODO: send email to unlock the account
		String to = form.getEmail();
		String subject = "Unlock Your Account || Ashok IT";
		StringBuffer body = new StringBuffer("");
		body.append("<h1>Use below temporary password to unlock your account</h1> ");
		body.append("Temporary Pwd : " + tempPwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:8081/unlock?email="+to+"\">Click here to unlock your account</a>");
		
		emailUtils.sendEmail(to, subject, body.toString());
		
		return true;
	}
	
	@Override
	public boolean unlockAccount(UnlockForm form) {
		
		UserDetailsEntity entity = userdtlsRepo.findByEmail(form.getEmail());
		
		if(entity.getPwd().equals(form.getTempPassword())) {
			entity.setPwd(form.getNewPassword());
			entity.setAccStatus("UnLocked");
			userdtlsRepo.save(entity);
			return true;
		}
	
		return false;
	}
	
	@Override
	public String login(LoginForm form) {
		
		UserDetailsEntity entity = userdtlsRepo.findByEmailAndPwd(form.getEmail(),form.getPwd());
		if(entity==null) {
			return "Invalid Credentials";
		}
		if(entity.getAccStatus().equals("Locked")) {
			return "Your account Locked";
		}
		
		//create session and store user data in session
		session.setAttribute("userId", entity.getUserId());
		return "Success";
	}

	

	@Override
	public boolean forgotPwd(String email) {
		
		//check record presence in database with given email
		UserDetailsEntity entity = userdtlsRepo.findByEmail(email);
		
		//if record not available send error message
		if(entity == null) {
			return false;
		}
		
		//if record available send Password to email and send success message
		String Subject = "Recover Password";
		String body = "Your Pwd :: " + entity.getPwd();
		
		emailUtils.sendEmail(email, Subject,body);
		
		return true;
	}

}
