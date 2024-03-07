package com.model;
import javax.persistence.*;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;
    private String address;
    private String phone_number;

//    @OneToOne(mappedBy = "bank", cascade = CascadeType.ALL)
//    private Admin admin;

	public Bank() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Bank(Long id, String name, String email, String address, String phone_number/*, Admin admin*/) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone_number = phone_number;
		//this.admin = admin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

//	public Admin getAdmin() {
//		return admin;
//	}
//
//	public void setAdmin(Admin admin) {
//		this.admin = admin;
//	}

//	@Override
//	public String toString() {
//		return "Bank [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", phone_number="
//				+ phone_number + ", admin=" + admin + "]";
//	}

	@Override
	public String toString() {
		return "Bank [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", phone_number="
				+ phone_number + "]";
	}

    
}
