package com.ait.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ait.entity.CourseEntity;
import com.ait.entity.EnqStatusEntity;
import com.ait.repository.CourseRepository;
import com.ait.repository.EnqStatusRepository;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private EnqStatusRepository statusRepo;
	
	public void run(ApplicationArguments args) throws Exception{
		
		courseRepo.deleteAll();
		
		CourseEntity c1 = new CourseEntity();
		c1.setCourseName("Java");
		
		CourseEntity c2 = new CourseEntity();
		c2.setCourseName("Python");
		
		CourseEntity c3 = new CourseEntity();
		c3.setCourseName("Devops");
		
		CourseEntity c4 = new CourseEntity();
		c4.setCourseName("AWS");
		
		courseRepo.saveAll(Arrays.asList(c1,c2,c3,c4));
		
		statusRepo.deleteAll();
		
		EnqStatusEntity e1 = new EnqStatusEntity();
		e1.setStatusName("New");
		
		EnqStatusEntity e2 = new EnqStatusEntity();
		e2.setStatusName("Enrolled");
		
		EnqStatusEntity e3 = new EnqStatusEntity();
		e3.setStatusName("Lost");
		
		statusRepo.saveAll(Arrays.asList(e1,e2,e3));
	
	}
	
	

}
