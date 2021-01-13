package com.example.aaa.shoppingCart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.aaa.shoppingCart.entity.Order;
import com.example.aaa.shoppingCart.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;

	
     public Page<Order> orders(Integer pN, Integer pS ) {
		
	 Pageable pageable = PageRequest.of(pN - 1, pS, Sort.by("date").descending());
	      
		    return orderRepository.findAll(pageable);
	
       }
}
