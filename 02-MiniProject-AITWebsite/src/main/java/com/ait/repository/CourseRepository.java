package com.ait.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.entity.CourseEntity;



public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

}
