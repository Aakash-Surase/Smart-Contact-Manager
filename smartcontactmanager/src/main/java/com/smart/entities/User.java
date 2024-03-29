package com.smart.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	private String name;
	@Column(unique=true)
	private String email;
	private String password;
	private String role;
	private boolean enabled;
	private String imageurl;



	public User(int id, String name, String email, String password, String role, boolean enabled, String imageurl,
			String about, List<contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageurl = imageurl;
		this.about = about;
		this.contacts = contacts;
	}



	@Column(length= 500)
	private String about;
	
	
	//mapping data
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, mappedBy= "user")
	private List<contact> contacts=new ArrayList<contact>();
	
	
	public List<contact> getContacts() {
		return contacts;
	}



	public void setContacts(List<contact> contacts) {
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



	public String getImageurl() {
		return imageurl;
	}



	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
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
				+ ", enabled=" + enabled + ", imageurl=" + imageurl + ", about=" + about + ", contacts=" + contacts
				+ ", getContacts()=" + getContacts() + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getEmail()=" + getEmail() + ", getPassword()=" + getPassword() + ", getRole()=" + getRole()
				+ ", isEnabled()=" + isEnabled() + ", getImageurl()=" + getImageurl() + ", getAbout()=" + getAbout()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}



	
	
	
	
	
}
