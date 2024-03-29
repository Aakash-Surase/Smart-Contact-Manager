package com.smart.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="orders")
public class MyOrder {
  
	
	
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private Long myOrderId;
	 
	 private  String orderId;
	 
	 private String amount;
	 private String recepit;
	 private String status;
	 
	 //one user can do many payment
	 @ManyToOne
	 //to send data we create getter setter methods

	 private User user;
	 private String paymentId;
	public Long getMyOrderId() {
		return myOrderId;
	}
	public void setMyOrderId(Long myOrderId) {
		this.myOrderId = myOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRecepit() {
		return recepit;
	}
	public void setRecepit(String recepit) {
		this.recepit = recepit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	 
	 
	 
	 
}
