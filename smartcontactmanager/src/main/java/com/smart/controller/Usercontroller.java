package com.smart.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Optional;
import com.smart.dao.ContactRepository;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.MyOrder;
import com.smart.entities.contact;
import com.smart.helper.Message;

import com.razorpay.*;
@Controller
@RequestMapping("/user")
public class Usercontroller {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private MyOrderRepository myOrderRepository;
	
	
	
	@ModelAttribute
	
	public void addcommonData(Model model, Principal principal){
		//Implement for all common data to response

		String userName=principal.getName();
		System.out.println("USERNAME"+userName);
		
		
		//getting teh data from database by email
		com.smart.entities.User user = userRepository.getUserByUserName(userName);
		System.out.println("USER"+user);
		
		
		model.addAttribute("user",user);//sending data to user dashboard file by using user object
		

	
	}
    //dash-board Home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		
		 
		model.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	
	// for opening the form
	
	@GetMapping("/add_contact")
	public String openContactform(Model model)
	{
		
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new contact());
		return"normal/add_contact_form";
	}

	
	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute contact contact,@RequestParam("profileImage")MultipartFile  file,
			Principal principal,HttpSession session) {
		
		
		try {
			
			
		String name=principal.getName();
		com.smart.entities.User user= this.userRepository.getUserByUserName(name);
		
		
		
		//uploading image file
		if(file.isEmpty())
		{
			//if file is empty try our message
			System.out.println("File is empty");
			contact.setImage("contacts.png");
		}
		else {
			//uploading file to folder and update name to contact
			contact.setImage(file.getOriginalFilename());
			
			java.io.File saveFile=new ClassPathResource("static/img").getFile();
			
			java.nio.file.Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is uploaded");
		}
		
		contact.setUser(user);
		
		
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		
		System.out.println("DATA"+ contact);
		System.out.println("Added to database");
		
		//message success
			
			session.setAttribute("message",new Message("your contact is added..","success"));
			
			}catch( Exception e) {
				System.out.println("ERROR"+e.getMessage());
				e.printStackTrace();
				//message error
				session.setAttribute("message",new Message("Some went wrong","danger"));
	
			}
			return"normal/add_contact_form";
			
	}
	
	// show contact handler
	
	// PER PAGE =5[N]
	//CURRENT PAGE=0[PAGE]
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m,Principal principal) {
		
		m.addAttribute("title","show user contacts");
		
		// sending contact list from database
		String userName= principal.getName();
		
		com.smart.entities.User user = this.userRepository.getUserByUserName(userName);
		// pageable object has current page and contact per page =5

		Pageable pageable = PageRequest.of(page,3);
		Page<contact> contacts= this.contactRepository.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contact",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		return"normal/show_contacts";
		
	}
	
	//showing perticular contact detils after clikung on mail id
	
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid,Model model, Principal principal)
	{
		
		java.util.Optional<contact> contactOptional = this.contactRepository.findById(cid);
		contact contacts=contactOptional.get(); 
		
		//no other person can see data using heat and fire in url we r using security
		
		String userName=principal.getName();
		com.smart.entities.User user=this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contacts.getUser().getId())
		{
			model.addAttribute("contacts",contacts);
			model.addAttribute("title",contacts.getCname());
		}
		
		
				return "normal/contact_detail";
	}
	
	//delete contact handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer ciD,Model model,HttpSession session,Principal principal)
	{
		
		System.out.println("ciD"+ciD);
		
		
		contact contacts= this.contactRepository.findById(ciD).get();
		
		com.smart.entities.User user= this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contacts);
		
		this.userRepository.save(user);
		
		//cheking who is deleting not anyone can delete id only authorized can delete
		
		//contacts.setUser(null);
		
		//this.contactRepository.delete(contacts);
		
		System.out.println("DELETED");
		session.setAttribute("message",new Message ("contact delete succesfully", "success"));
		
		
		return"redirect:/user/show_contacts/0";
	}
	
	//open new update form handler
	
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer ciD, Model m)
	{
		m.addAttribute("title","update contact");
		
		contact contacts = this.contactRepository.findById(ciD).get();
		m.addAttribute("contact", contacts);

		
		return"normal/update_form";
	}
	
	// update contact handler
	
	@RequestMapping(value="/process-update",method= RequestMethod.POST)
	public String updateHandler(@ModelAttribute contact contacts,@RequestParam("profileImage")MultipartFile file,Model m,
			HttpSession session,Principal principal )
	{
	   try {
		   // old contact details
		   contact oldcontactDetail=this.contactRepository.findById(contacts.getCid()).get();
		   //image
		   if(!file.isEmpty())
		   {
			   //update file here image
			  // delete old photo
			   File deleteFile=new ClassPathResource("static/img").getFile();

			   File file1=new File(deleteFile,oldcontactDetail.getImage());
			   
			   file1.delete();
			   
			   // update new photo
				
				java.io.File saveFile=new ClassPathResource("static/img").getFile();
				
				java.nio.file.Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				contacts.setImage(file.getOriginalFilename());
		   }
		   else
		   {
			   contacts.setImage(oldcontactDetail.getImage());
		   }
		   
		   com.smart.entities.User user = this.userRepository.getUserByUserName(principal.getName());
		   
		   contacts.setUser(user);
		   
		   this.contactRepository.save(contacts);
		   
		   
		   session.setAttribute("message",new Message("YOur contact is updated","success"));
	  
	   
	   }catch(Exception e) {
		 e.printStackTrace(); 
	   }
	   System.out.println("contact name "+contacts.getCname());
	   System.out.println("contact ID "+contacts.getCid());

	   
		return"redirect:/user/"+contacts.getCid()+"/contact";
	}
	
	//profile handler
	
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		
		model.addAttribute("title", "Profile Page");
		return"normal/profile";
	}
	
	//open setting handler
	
	@GetMapping("/settings")
	public String openSettings(){
		
		return"normal/settings";
	}
	
	//chamge password handler
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session)
	{
		System.out.println("oldpassword"+oldPassword);
		System.out.println("newpassword"+newPassword);
		
		String userName= principal.getName();
		com.smart.entities.User currentUser= this.userRepository.getUserByUserName(userName);
        System.out.println(currentUser.getPassword());
        
        if(this.bCryptPasswordEncoder.matches(oldPassword,currentUser.getPassword()))
        {
        	//change the password
        	currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message",new Message("Your password successfully changes","success"));
        }
        else{
        
        	//error
            session.setAttribute("message",new Message("PLease enter your old password correct","danger"));
            return "redirect:/user/settings";
        }
		
		return "redirect:/user/index";
	}
	
	//creating order for payment
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data,Principal principal) throws Exception
	{
	    System.out.println(data);
	    
	    int amt=Integer.parseInt(data.get("amount").toString());
	    
	   var client= new RazorpayClient("rzp_test_7qy3twTOdcYuMb","ZqTK2f4VwmEEXbtjI8Tej0rH");
	   
	   JSONObject ob=new JSONObject();
	   ob.put("amount", amt*100);
	   ob.put("currency","INR");
	   ob.put("receipt", "txn_235425");
	   
	   //cresting new order
	   
	   Order order = client.orders.create(ob);
	   System.out.println(order);
	   // saving order in database 
	   
	   
	   MyOrder myOrder=new MyOrder();
	   
	   myOrder.setAmount(order.get("amount")+"");
	   myOrder.setOrderId(order.get("id"));  
	   myOrder.setPaymentId(null);  
	   myOrder.setStatus("created");
	   myOrder.setUser(this.userRepository.getUserByUserName(principal.getName()));
	  myOrder.setRecepit(order.get("receipt"));
	  
	  this.myOrderRepository.save(myOrder);
	   
	   // if u want u can save to ur database to know ourself
	   
	   
	   
	   
		return order.toString();
		
	}
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String,Object>data)
	{
		MyOrder myorder=this.myOrderRepository.findByOrderId(data.get("order_id").toString());
		
		myorder.setPaymentId(data.get("payment_id").toString());
		myorder.setStatus(data.get("status").toString());
		this.myOrderRepository.save(myorder);

		System.out.println(data);
		return ResponseEntity.ok(Map.of("msg","updated successfully"));
	}
}





