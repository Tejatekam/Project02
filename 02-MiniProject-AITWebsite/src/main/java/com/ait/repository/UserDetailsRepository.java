package com.ait.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.entity.UserDetailsEntity;

public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer>{

	public UserDetailsEntity findByEmail(String email);
	
	public UserDetailsEntity findByEmailAndPwd(String email,String pwd);
}
