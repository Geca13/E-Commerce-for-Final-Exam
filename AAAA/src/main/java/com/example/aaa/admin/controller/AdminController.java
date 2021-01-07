package com.example.aaa.admin.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.aaa.admin.entity.Payment;
import com.example.aaa.admin.repository.PaymentRepository;
import com.example.aaa.admin.service.AdminService;
import com.example.aaa.bank.entity.Company;
import com.example.aaa.bank.repository.CompanyRepository;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.shoppingCart.entity.Order;
import com.example.aaa.shoppingCart.entity.OrderProducts;
import com.example.aaa.shoppingCart.repository.OrderProductsRepository;
import com.example.aaa.shoppingCart.repository.OrderRepository;
import com.example.aaa.shoppingCart.service.OrderService;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;

@Controller
public class AdminController {
	
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderProductsRepository orderPRepository;
	@Autowired
	UsersRepository userRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	OrderService orderService;
	@Autowired
	AdminService adminService;
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	CompanyRepository companyRepository;
	
	@GetMapping("/administration")
	public String administrationPage(Model model,@AuthenticationPrincipal UsersDetails userD) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
		   model.addAttribute("user", user);

		List<Order> orders = orderRepository.findAll();
		  Integer totalOrders = orders.size();
		   model.addAttribute("orders", orders);
		   model.addAttribute("totalOrders", totalOrders);
		List<OrderProducts> orderProducts = orderPRepository.findAll();
		  Integer totalItemsSold = 0;
		  Double ordersTotal = 0.00;
		  
		  for (OrderProducts orderProducts2 : orderProducts) {
			 totalItemsSold = totalItemsSold + orderProducts2.getQty();
			 ordersTotal = ordersTotal + orderProducts2.getItemTotal();
		}
		  Double profit = ordersTotal * 0.1;
		  model.addAttribute("ordersTotal", ordersTotal);  
		  model.addAttribute("totalItemsSold", totalItemsSold);
		  model.addAttribute("profit", profit);
		
		List<Store> stores = storeRepository.findAll();
		  Double totalStoresBalance = 0.00;
		  for (Store store : stores) {
			  totalStoresBalance = totalStoresBalance +  store.getBalance();
		}
		  model.addAttribute("stores", stores);
		  model.addAttribute("totalStoresBalance", totalStoresBalance);
		
		  List<Payment> payments = paymentRepository.findAll();
		  Double payedToStores = 0.00;
		  
		  for (Payment payment : payments) {
			  payedToStores = payedToStores + payment.getSum();
		  
			  
		}
		  
		  Integer payToStoresTransactions = payments.size();
		  model.addAttribute("payToStoresTransactions", payToStoresTransactions);
		  model.addAttribute("payedToStores", payedToStores);
		  
		  Company company = companyRepository.findByAccountNumber("0123456789");
		  Double balance = company.getAccountBalance();
		  model.addAttribute("balance", balance);
		
		return "adminPage";
		
	    }
	
	 @GetMapping("/orders")
	 public String getOrdersPage(Model model) {
		
		 orderPages(1, model);
		 
		return "orders";
	}
	 
	 @GetMapping("/pG/{pN}")
	 public String orderPages(@PathVariable("pN")Integer pN, Model model) {
		 
		 Integer pS = 2;
		 
		 Page<Order> or = orderService.orders(pN, pS);
		 List<Order> orders = or.getContent();
		 
		   
		    model.addAttribute("orders", orders);
		    model.addAttribute("currentPage",pN);
			model.addAttribute("totalPages", or.getTotalPages());
			model.addAttribute("totalItems", or.getTotalElements());
		 return "orders";
	 }
	 
	 @GetMapping("/payments")
	 public String getPaymentsPage(Model model) {
		
		 paymentPages(1,"localDateTime", "asc", model);
		 
		return "payments";
	}
	 
	 @GetMapping("pagesss/{pageNumbersss}")
	 public String paymentPages(@PathVariable("pageNumbersss") Integer pageNumbersss,
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				Model model) {
		 
		 Integer pageSize = 5;
		 
		 Page<Payment> payment = adminService.payments(pageNumbersss, pageSize, sortField, sortDir);
		 List<Payment> payments = payment.getContent();
		 
		    model.addAttribute("currentPage",pageNumbersss);
			model.addAttribute("totalPages", payment.getTotalPages());
			model.addAttribute("totalItems", payment.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("payments", payments);
		 return "payments";
	 }
	 
	 
	 
	 
}
