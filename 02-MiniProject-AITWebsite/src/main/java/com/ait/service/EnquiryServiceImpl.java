package com.ait.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.binding.DashboardResponse;
import com.ait.binding.EnquiryForm;
import com.ait.binding.EnquirySearchCriteria;
import com.ait.entity.CourseEntity;
import com.ait.entity.EnqStatusEntity;
import com.ait.entity.StudentEnqEntity;
import com.ait.entity.UserDetailsEntity;
import com.ait.repository.CourseRepository;
import com.ait.repository.EnqStatusRepository;
import com.ait.repository.StudentEnqRepository;
import com.ait.repository.UserDetailsRepository;

@Service
public class EnquiryServiceImpl implements EnquiryService {
	
	@Autowired
	private StudentEnqRepository enqRepo;
	
	@Autowired
	private CourseRepository coursesRepo;
	
	@Autowired
	private EnqStatusRepository statusRepo;
	
	@Autowired
	private UserDetailsRepository userDtlsRepo;
	
	@Autowired
	private HttpSession session;
	
	
	@Override
	public DashboardResponse getDashboardData(Integer userId) {
	
		DashboardResponse response  = new DashboardResponse();
		
		Optional<UserDetailsEntity> findById =
				userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			UserDetailsEntity userEntity = findById.get();
			
			List<StudentEnqEntity> enquiries =
					userEntity.getEnquiries();
			
			Integer totalCnt = enquiries.size();
			
			
			
			Integer enrolledCnt = enquiries.stream()
					                       .filter(e -> e.getEnqStatus().equals("Enrolled"))
                                           .collect(Collectors.toList()).size();
			
			Integer lostCnt = enquiries.stream()
					                   .filter(e -> e.getEnqStatus().equals("Lost"))
                                       .collect(Collectors.toList()).size();
			
			response.setTotalEnquiriesCnt(totalCnt);
			response.setEnrolledCnt(enrolledCnt);
			response.setLostCnt(lostCnt);
		}
		
		
		return response;
	}


	@Override
	public List<String> getCourses() {
		List<CourseEntity> findAll = coursesRepo.findAll();
		
		List<String> names = new ArrayList<>();
		
		for(CourseEntity entity : findAll) {
			names.add(entity.getCourseName());
		}
		
		return names;
	}

	@Override
	public List<String> getEnqStatuses() {
		
		List<EnqStatusEntity> findAll = statusRepo.findAll();
		
		List<String> statusList = new ArrayList<>();
		
		for(EnqStatusEntity entity : findAll)
		{
			statusList.add(entity.getStatusName());
		}
		return statusList;
	}

	
	


	@Override
	public boolean saveEnquiry(EnquiryForm form) {
        StudentEnqEntity enqEntity = new  StudentEnqEntity();
        BeanUtils.copyProperties(form, enqEntity);
        
        Integer userId = (Integer)session.getAttribute("userId");
        
        UserDetailsEntity userEntity = userDtlsRepo.findById(userId).get();
       
        enqEntity.setUser(userEntity);
        
        
        enqRepo.save(enqEntity);
        
		return true;
	}
	
	public List<StudentEnqEntity> getEnquiries(){
		
		Integer userId = (Integer)session.getAttribute("userId");
		Optional<UserDetailsEntity> findById = userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			UserDetailsEntity userEntity = findById.get();
			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();
			
			
			return enquiries;
			}
		return null;
		
	}
	
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria,Integer userId){
		
		
         Optional<UserDetailsEntity> findById = userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			UserDetailsEntity userEntity = findById.get();
			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();
			
			if(null != criteria.getCourseName()&& !"".equals(criteria.getCourseName())) 
			{ 
			enquiries = enquiries.stream()
			.filter(e -> e.getCourseName().equals(criteria.getCourseName()))
			.collect(Collectors.toList());	
			}
			
			if(null != criteria.getEnqStatus()&& !"".equals(criteria.getEnqStatus())) 
			{ 
			enquiries = enquiries.stream()
					.filter(e -> e.getEnqStatus().equals(criteria.getEnqStatus()))
					.collect(Collectors.toList());
		    }
			
			if(null != criteria.getClassMode()&& !"".equals(criteria.getClassMode())) 
			{ 
			enquiries = enquiries.stream()
					.filter(e -> e.getClassMode().equals(criteria.getClassMode()))
					.collect(Collectors.toList());
		    }
			return enquiries;
			}
		return null;
		
	}


	
	public StudentEnqEntity getEnquiries(Integer enquiryId) {
		Optional<StudentEnqEntity> entity = enqRepo.findById(enquiryId);
		return entity.get();
	}


	/*@Override
	public StudentEnqEntity updateEnquiry(Integer enquiryId, EnquiryForm form) {
		StudentEnqEntity enqEntity = new  StudentEnqEntity();
        BeanUtils.copyProperties(form, enqEntity);
        
        Integer userId = (Integer)session.getAttribute("userId");
        
        UserDetailsEntity userEntity = userDtlsRepo.findById(userId).get();
        enqEntity.setUser(userEntity); 
		Optional<StudentEnqEntity> enq = enqRepo.findById(enquiryId);
		
		if (enq.isPresent()) {
			StudentEnqEntity enquiry = enq.get();
			enquiry.setStudentName(form.getStudentName());
			enquiry.setStudentPhno(form.getStudentPhno());
			enquiry.setClassMode(form.getClassMode());
			enquiry.setCourseName(form.getCourseName());
			enquiry.setEnqStatus(form.getEnqStatus());
			
			enqRepo.save(enquiry);
			return  enq.get();
		}
	
		return null;
	}*/

}
