package com.smart.config;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smart.entities.User;


public class CustomUserDetails implements UserDetails {

	//by this we access the user details role and all
	private User user;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	
	//here we giving what athority user can get
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		SimpleGrantedAuthority simpleGrantedAuthority= new  SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {
		return user.getEmail();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

}
