package com.ait.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ait.binding.DashboardResponse;
import com.ait.binding.EnquiryForm;
import com.ait.binding.EnquirySearchCriteria;
import com.ait.entity.StudentEnqEntity;
import com.ait.repository.StudentEnqRepository;
import com.ait.service.EnquiryService;

@Controller
public class EnquiryController {

	@Autowired
	private StudentEnqRepository repo;

	@Autowired
	private EnquiryService enqService;

	@Autowired
	private HttpSession session;

	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "index";
	}

	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {

		Integer userId = (Integer) session.getAttribute("userId");

		DashboardResponse dashboardData = enqService.getDashboardData(userId);

		model.addAttribute("dashboardData", dashboardData);

		return "dashboard";
	}

	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {

		initForm(model);

		return "addEnquiry";
	}

	private void initForm(Model model) {

		// get courses for dropdown
		List<String> courses = enqService.getCourses();

		// get enq status for dropdown
		List<String> statuses = enqService.getEnqStatuses();

		// create binding class object
		EnquiryForm formObj = new EnquiryForm();

		// set data in model object
		model.addAttribute("courseName", courses);
		model.addAttribute("enqStatus", statuses);
		model.addAttribute("formObj", formObj);

	}

	@PostMapping("/addEnq")
	public String addEnquiry(@ModelAttribute("formObj") EnquiryForm formObj, Model model) {

		System.out.println(formObj);
		boolean status = enqService.saveEnquiry(formObj);
		if (status) {
			model.addAttribute("successMsg", "Enquiry Added");
		} else {
			model.addAttribute("errorMsg", "Enquiry denied");
		}
		return "addEnquiry";
	}

	@GetMapping("/enquiries")
	public String viewEnquiriesPage(Model model) {
		initForm(model);
		model.addAttribute("searchForm", new EnquirySearchCriteria());
		List<StudentEnqEntity> enquiries = enqService.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "viewEnquiry";
	}

	@GetMapping("/filter-enquiries")
	public String getFilteredEnqs(@RequestParam String cname, @RequestParam String status, @RequestParam String mode,
			Model model) {

		EnquirySearchCriteria criteria = new EnquirySearchCriteria();
		criteria.setCourseName(cname);
		criteria.setClassMode(mode);
		criteria.setEnqStatus(status);

		System.out.println(criteria);

		Integer userId = (Integer) session.getAttribute("userId");

		List<StudentEnqEntity> filteredEnqs = enqService.getFilteredEnqs(criteria, userId);
		model.addAttribute("enquiries", filteredEnqs);

		return "filter-enquiries-page";

	}

	@GetMapping("/edit/{id}")
	public String editEnq(@PathVariable("id") Integer id) {

		StudentEnqEntity entity = enqService.getEnquiries(id);
		session.setAttribute("entity", entity);
		session.setAttribute("enquiryId", id);
		return "redirect:/enqEdit";
	}

	@GetMapping("/enqEdit")
	public String edit(Model model) {

		List<String> courses = enqService.getCourses();

		List<String> statuses = enqService.getEnqStatuses();

		model.addAttribute("courseName", courses);
		model.addAttribute("enqStatus", statuses);

		EnquiryForm formObj = new EnquiryForm();
		if (session.getAttribute("entity") != null) {
			StudentEnqEntity entity = (StudentEnqEntity) session.getAttribute("entity");
			BeanUtils.copyProperties(entity, formObj);
			session.removeAttribute("entity");
		}
		model.addAttribute("formObj", formObj);

		return "addEnquiry";

	}
	




}
