package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.service.EmailService;

@Controller
public class ForgotController {
	Random random=new Random(1000);
	
	@Autowired
	private EmailService emailService;
	@Autowired 
    private UserRepository userRepository;
	@Autowired 
    private BCryptPasswordEncoder bcrypt;

	
	//emial id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forget_email_form";
	}
	
	@PostMapping("/send-otp")
	//fetching email from html page via request param
	public String sendOTP(@RequestParam("email")String email,HttpSession session)
	{
		System.out.println("email"+email);
		
		//generating 4 digits otp
		
		
		int otp=random.nextInt(9999);
		System.out.println("OTP"+otp);
		
		//write code for send OTP to email
		
		String subject="OTP from SCM";
		String message=""
				+"<div style='border:1px solid #e2e2e2; padding:20px'>"
				+ "<h1>"
				+"OTP is "
				+"<b>"+otp
				+"</n>"
				+"</h1>"
				+ "</div>";
		
				
				
		String to=email;
		
		
		
		boolean flag= this.emailService.sendEmail(subject,message,to);
		
		if(flag)
		{
			session.setAttribute("myotp",otp);
			session.setAttribute("email",email);
			return "verify_otp";
		}else
		{
			session.setAttribute("message","Check your Email ID");
			
			return "forget_email_form";
		}
			
	
	}
	
	// verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp")int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		if(myOtp==otp) {
			
			User user=this.userRepository.getUserByUserName(email);
			if(user==null)
			{
				//send error message if User not exit with this email
				session.setAttribute("message","User Does Not Exits with this Email");
				
				return "forget_email_form";

			}else {
				//send change password form if user exist
				
				
				
				
			}
			
			return "password_change_form";
			
		}else
		{
			session.setAttribute("message","You have enterd wrong OTP..!");
			return"verify_otp";
		}
	}
	 //change password
		
        @PostMapping("/change-password")
		public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session)
		{
			String email = (String)session.getAttribute("email");
			User user=this.userRepository.getUserByUserName(email);
            user.setPassword(this.bcrypt.encode(newpassword));
            this.userRepository.save(user);

            return "redirect:/signin?change=password Changed successfully...";
		}
		
	
		
	
	 
	}
