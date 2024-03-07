package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AccountResponse;
import com.dto.UserResponse;
import com.service.DashboardService;
import com.util.LoggedinUser;

@RestController
@CrossOrigin
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/user")
	public ResponseEntity<UserResponse> getUserDetails() {
		String accountNumber = LoggedinUser.getAccountNumber();
		UserResponse userResponse = dashboardService.getUserDetails(accountNumber);
		return ResponseEntity.ok(userResponse);
	}

	@GetMapping("/account")
	@CrossOrigin
	public ResponseEntity<AccountResponse> getAccountDetails() {
		String accountNumber = LoggedinUser.getAccountNumber();
		AccountResponse accountResponse = dashboardService.getAccountDetails(accountNumber);
		return ResponseEntity.ok(accountResponse);
	}

}