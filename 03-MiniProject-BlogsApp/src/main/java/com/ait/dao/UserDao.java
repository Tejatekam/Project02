package com.ait.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.entity.UserEntity;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
	public UserEntity findByEmail(String email);
}
