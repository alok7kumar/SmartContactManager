package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String name;
	private String secondName;
	private String work;
	private String email;
	private String phone;
	private String image;
	private String dscription;
	
	@ManyToOne
	@JsonIgnore			//see SearchController for reason behind this usage
	private User user;
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDscription() {
		return dscription;
	}
	public void setDscription(String dscription) {
		this.dscription = dscription;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	//Only made to solve Delete Bug 2, see in Controller -> delete operation
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cId==((Contact)obj).getcId(); //this.cId matlab current cId == obj ko typecast kiya gaya hai Contact se taaki usme se cId se nikal sake
	}				// cuurnt id aur paremeterised obj ki id jo typecast se nikli hai dono equal huye, tab object ko equal kahenge
	
	

}
