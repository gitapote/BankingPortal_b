package com.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.model.User;

public interface UserService {
	public User registerUser(User user);

	User getUserByAccountNumber(String account_no);

	public void saveUser(User user);

	User updateUser(User user);
	
	 List<User> getAllUsers();
	 
	 List<User> getAllUsers(Authentication authentication);
}
