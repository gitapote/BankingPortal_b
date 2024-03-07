package com.service;

import java.util.List;

import com.model.Admin;

public interface AdminService {
	  Admin createAdmin(Admin admin);
	    Admin getAdminById(Long adminId);
	    List<Admin> getAllAdmins();
	    Admin deleteAdmin(Long adminId);
	    Admin updateAdmin(Admin admin);

}
