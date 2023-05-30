package com.ait.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.entity.Comment;

public interface CommentDao extends JpaRepository<Comment, Integer>{

}
