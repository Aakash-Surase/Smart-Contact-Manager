package com.smart.service;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;




import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message,String to) {
		
		boolean f=false;
		String from="aakashsurase2001@gmail.com";
		
		
		//variable for g-mail
		
		String host="smtp.gmail.com";
		
		//get the system properties
		
		Properties properties= System.getProperties();
		System.out.println("properties"+properties);
		
		//host set
		
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port",465);
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//step1 to get session object
		Session session=Session.getInstance(properties,new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				
			
		return new PasswordAuthentication("aakashsurase2001@gmail.com","hxkv mrsq ruay sgdo");
	   }
	});
	
	session.setDebug(true);
	
	//step 2 compose the message
	
	MimeMessage m=new MimeMessage(session);
	
	  try {
		  //from email
		  m.setFrom(from);
	 
		  m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
		  //adding subject to message
		  m.setSubject(subject);
		  
		  //adding text to message
		  
		 // m.setText(message);
		  m.setContent(message,"text/html");
		  
		  //send
		  //step 3 send the message using transport class
		  
		  Transport.send(m);
		  
		  System.out.println("Send success....");
		  f=true;
	  }catch (Exception e) {
		  e.printStackTrace();
	  }
	  return f;
	  
		  
	}

}
