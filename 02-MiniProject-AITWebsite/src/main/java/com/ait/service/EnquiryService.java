package com.ait.service;

import java.util.List;

import com.ait.binding.DashboardResponse;
import com.ait.binding.EnquiryForm;
import com.ait.binding.EnquirySearchCriteria;
import com.ait.entity.StudentEnqEntity;

public interface EnquiryService {
	
	public List<String> getCourses();
	
	public List<String> getEnqStatuses();
	
	public boolean saveEnquiry(EnquiryForm form);
	
	public DashboardResponse getDashboardData(Integer userId);
	
	public List<StudentEnqEntity> getEnquiries();
	
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySearchCriteria criteria,Integer userId);

	public StudentEnqEntity getEnquiries(Integer id);
	
	//public StudentEnqEntity updateEnquiry(Integer enquiryId,EnquiryForm form);

}
