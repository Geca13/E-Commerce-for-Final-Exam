package com.example.aaa.admin.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	@Autowired
	private JavaMailSender sender;
	
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
			  totalStoresBalance = totalStoresBalance + store.getBalance();
			  storeRepository.save(store);
		}
		  model.addAttribute("stores", stores);
		  model.addAttribute("totalStoresBalance", totalStoresBalance);
		
		  List<Payment> payments = paymentRepository.findAll();
		  Double payedToStores = 0.00;
		  
		  for (Payment payment : payments) {
			  payedToStores = payedToStores + payment.getSum();
			       model.addAttribute("payedToStores", payedToStores);
			  }
		  
		  Integer payToStoresTransactions = payments.size();
		  model.addAttribute("payToStoresTransactions", payToStoresTransactions);
		  
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
		 
		 Integer pS = 12;
		 
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
	 
	 @GetMapping("/users/profile/{id}")
	 public String seeUserProfile(Model model,@PathVariable("id")Integer id) {
		 Double totalSpent=0.00;
		 Users user = userRepository.findById(id).get();
		 List<Order>orders = orderRepository.findByUser(user);
		  for (Order order : orders) {
			  totalSpent = totalSpent+order.getTotal();
			  model.addAttribute("totalSpent", totalSpent);
		}
				model.addAttribute("orders", orders);
				model.addAttribute("user", user);
		 
		 return "adminSeeUserProfile";
	 }
	 
	 @GetMapping("/pay/{id}")
	 public String payToStoresManually(@PathVariable("id")Integer id,Model model) {
			 Payment payment = new Payment();
			 Company comp = companyRepository.findByAccountNumber("0123456789");
			 Store store = storeRepository.findById(id).get();
			 Company company = companyRepository.findByAccountNumber(store.getAccountNumber());
			   company.setAccountBalance(company.getAccountBalance() + store.getBalance());
			   comp.setAccountBalance(comp.getAccountBalance() - store.getBalance());
			 
			     payment.setCompany(company);
				 payment.setSum(store.getBalance());
				 payment.setLocalDateTime(LocalDateTime.now());
				 paymentRepository.save(payment);
				 
			 String email = payment.getCompany().getEmail();
			 Double amount = payment.getSum();
			 LocalDateTime time = payment.getLocalDateTime();
			 Double balance = payment.getCompany().getAccountBalance();
				 
				 try {
					sendPaymentEmailToStore(email, amount,time,balance);
				} catch (UnsupportedEncodingException | MessagingException ex) {
					model.addAttribute("error", ex.getMessage());
					model.addAttribute("error", "Something bad happened while sending the email.");
				}
			   store.setBalance(0.00);
			         companyRepository.save(company);
			         companyRepository.save(comp);
			         storeRepository.save(store);
			 
			             return "redirect:/administration";
			 }
		 
	 private void sendPaymentEmailToStore(String email, Double amount,LocalDateTime time, Double balance) throws UnsupportedEncodingException, MessagingException {
				
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				
			   helper.setFrom("Geca@geca.com", "Java imitacija");
			   helper.setTo(email);
				
			String subject = "Payment ";
			String content =  "You have received a Payment from Dzikle's Express !!! on " + time + 
						" . The amount that was payed  was  " + amount +"$" + ". The new balance on your account now is: "+ balance + "$" ;
				
			   helper.setSubject(subject);
			   helper.setText(content, true);
				  sender.send(message);
	 }	 
}

