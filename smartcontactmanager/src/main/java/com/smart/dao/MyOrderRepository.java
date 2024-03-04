package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.MyOrder;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {

	  //we are putting data in this repo and by his obeject we can insert and delete data
      public MyOrder findByOrderId(String orderId);
}
