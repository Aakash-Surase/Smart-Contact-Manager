package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;
	

	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home- Smart Contact manager");
		return"home";
		
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About- Smart Contact manager");
		return"about";
		
	}
	

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - Smart Contact manager");
		model.addAttribute("user",new User());
		return "signup";
		
	}
	
	//handler for custom login page
	@RequestMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","LOgin Page");
		return "login";
	}
	
	//handler for register after signup
	@SuppressWarnings("unused")
	@RequestMapping("/do")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model, HttpSession session)
	{
		try {
			
			

			if(!agreement)
			{
				System.out.println("you dont have agreed the terms and conditon");
				throw new Exception("you dont have agreed the terms and conditon");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		
			
			
			System.out.println("Agreement"+agreement);
			System.out.println("USER"+user);
			
			User result = this.userRepository.save(user);
			
			//sucess meassage
			
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Register","alert-Success"));
			return "signup";
			
			
		}catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went Wrong"+ e.getMessage(),"alert-danger"));
			return "signup";

		}
		
		
	}
	
	}
