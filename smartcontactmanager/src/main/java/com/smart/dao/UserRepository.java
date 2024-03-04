package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository <User, Integer>{

	org.springframework.boot.autoconfigure.security.SecurityProperties.User save(
			org.springframework.boot.autoconfigure.security.SecurityProperties.User user);
	
	
	@Query("select u from User u where u.email=:email")
	public User getUserByUserName(@Param("email") String email);

}
