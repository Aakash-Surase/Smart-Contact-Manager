package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;
import com.smart.entities.contact;

public interface ContactRepository extends JpaRepository<contact, Integer>{

	//pagination method
	
	@Query("from contact as c where c.user.id =:userId")
	// pageable object has current page and contact per page =5
	
	public Page<contact> findContactsByUser(@Param("userId")int userId,Pageable pePageable );

	
	// search logic fatching data from database
	
	public List<contact>findByCnameContainingAndUser(String cname,User user);
		 
	

}

