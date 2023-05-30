package com.ait.binding;

import lombok.Data;

@Data
public class UnlockForm {
	
	private String  email;
	
	private String tempPassword;
	
	private String newPassword;
	
	private String confirmPassword;

}
