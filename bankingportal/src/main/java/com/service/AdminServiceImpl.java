package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AdminRepository;
import com.model.Admin;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository adminRepo;

	@Override
	public Admin createAdmin(Admin admin) {
		// TODO Auto-generated method stub
		return adminRepo.save(admin);
	}

	@Override
	public Admin getAdminById(Long adminId) {
		return adminRepo.findById(adminId).orElse(null);
	}

	@Override
	public List<Admin> getAllAdmins() {
		return adminRepo.findAll();
	}

	@Override
	public Admin deleteAdmin(Long adminId) {
		 Optional<Admin> optionalAccount = adminRepo.findById(adminId);
		    
		    // Check if the account exists
		    if (optionalAccount.isPresent()) {
		        // Get the account object from Optional
		        Admin exAcc = optionalAccount.get();
		        
		        // Delete the account
		        adminRepo.deleteById(null);
		        
		        // Return the deleted account
		        return exAcc;
		    } else {
		        // Handle case where account doesn't exist
		        return null; // or throw an exception, depending on your requirements
		    }

	}

	@Override
	public Admin updateAdmin(Admin admin) {
		if (adminRepo.existsById(admin.getId())) {
			return adminRepo.save(admin);
		} else {
			// Handle case where account doesn't exist
			return null;
		}
	}

}
