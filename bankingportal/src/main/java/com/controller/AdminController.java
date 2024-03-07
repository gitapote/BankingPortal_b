package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dao.UserRepository;
import com.dto.AdminLoginRequest;
import com.model.Admin;
import com.model.User;
import com.security.JwtTokenUtil;
import com.service.AdminService;
import com.service.OTPService;
import com.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

//    @Autowired
//    private AdminService adminService;

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepository;

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserDetailsService userDetailsService;
	private final AdminService adminService;
	private final OTPService otpService;

	public AdminController(AdminService adminService, AuthenticationManager authenticationManager,
			JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, OTPService otpService) {
		this.adminService = adminService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.otpService = otpService;
	}

	@PostMapping("/add")
	public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin) {
		Admin savedAdmin = adminService.createAdmin(admin);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
	}

	@GetMapping("/getOne/{id}")
	public ResponseEntity<Admin> findAdminById(@PathVariable("id") Long id) {
		Admin admin = adminService.getAdminById(id);
		if (admin != null) {
			return ResponseEntity.ok(admin);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

//    @GetMapping("/getAll")
//    public ResponseEntity<List<Admin>> findAllAdmins() {
//        List<Admin> admins = adminService.getAllAdmins();
//        return ResponseEntity.ok(admins);
//    }

	@PutMapping("/update")
	public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
		Admin updatedAdmin = adminService.updateAdmin(admin);
		if (updatedAdmin != null) {
			return ResponseEntity.ok(updatedAdmin);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
		adminService.deleteAdmin(id);
		return null;

	}

	@GetMapping("/getAll")
	public ResponseEntity<List<User>> findAllUserinAdmins() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

//	@GetMapping("/{accountNumber}")
//	public ResponseEntity<User> getUserByAccountNumberinAdmins(@PathVariable("accountNumber") String accountNumber) {
//		User user = userService.getUserByAccountNumber(accountNumber);
//		if (user != null) {
//			return ResponseEntity.ok(user);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}

//	 @GetMapping("/getAll")
//	    @PreAuthorize("hasRole('ROLE_ADMIN')")
//	    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) throws AccessDeniedException {
//	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//	        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//	        if (isAdmin) {
//	            return ResponseEntity.ok(userRepository.findAll());
//	        } else {
//	            throw new AccessDeniedException("Insufficient permissions");
//	        }
//	    }

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AdminLoginRequest loginRequest) {
		try {
			// Authenticate the user with the account number and password
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getAccountNumber(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			// Invalid credentials, return 401 Unauthorized
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account number or password");
		}

		// If authentication successful, generate JWT token
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getAccountNumber());
		System.out.println(userDetails);
		String token = jwtTokenUtil.generateToken(userDetails);

		Map<String, String> result = new HashMap<>();
		result.put("token", token);
		// Return the JWT token in the response
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
