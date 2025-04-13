package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Name field is required !!")		//NotBlank comes under Validation dependency
	private String name;
	@Column(unique = true) //this will prevent saving same emailid in more than one column 
	private String email;
	private String password;
	private String role;	// for Security authentication	: take value as ROLE_USER or ROLE_ADMIN, in this project mostly ROLE_USER is made only, not ROLE_ADMIN
	private boolean enabled;	//this to show if user is active, it will value as true/false
	private String imageUrl;
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)	
	private List<Contact> contacts	=	new ArrayList<>();
	public List<Contact> getContacts() {
		return contacts;
	}
	//@OneTo Many :	It will show relation between User and Contact. i.e. One User can have Many Contacts.
	//	Cascade(ALL) means if you will delete one User, all contacts in it will be deleted.It can be modified here
	//	fetch(LAZY) means Contacts will only show when called, (EAGER) will fetch all contacts whenever User is called.
	//Relationship laga hai, to generally 3 tables banenge db me,(user, contact, user_contacts(isme user_id, aur contacts_c_id as a foreign key rahega))
	//	par hum nahi chahte ke teesra table bane, "user" se he sari mapping ho jaye jo Contacts me bani hai, aur yahi contacts table me as a user_id bhi bani hai
	//	to humne mappedBy lagakar dB se user_contacts ko delete kar diya
	// orphanRemoval : kyuki linking aisi hai ke one user can have many contacts, User parent entity hai aur Contact child entity,
		//jab bhi child entity unlink ho jayegi apne parent se tab usko remove kar diya jayega
	
	
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	

}
