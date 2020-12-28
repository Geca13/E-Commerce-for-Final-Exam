package com.example.aaa.bank.controller;



import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.aaa.bank.entity.CardBrend;
import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;
import com.example.aaa.bank.entity.Year;
import com.example.aaa.bank.repository.CardBrendRepository;
import com.example.aaa.bank.repository.CreditCardRepository;
import com.example.aaa.bank.repository.MonthRepository;
import com.example.aaa.bank.repository.YearRepository;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.shoppingCart.entity.CartProducts;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UserNotFoundException;
import com.example.aaa.users.service.UsersDetails;

import lombok.val;

  

@Controller
public class BankController {
	@Autowired
	UsersRepository userRepository;
	@Autowired
	CreditCardRepository ccRepository;
	@Autowired
	CardBrendRepository cbRepository;
	@Autowired
	MonthRepository monthRepository;
	@Autowired
	YearRepository yearRepository;
	@Autowired
	ShoppingCartRepository scRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	private JavaMailSender sender;
	
	
	
	@GetMapping("/payment/{id}")
	public String paymentForm(Model model,@AuthenticationPrincipal UsersDetails userD, @PathVariable("id")Integer id,@Param(value = "cardNumber") String cardNumber) {
		
		List<CardBrend> cardBrend = cbRepository.findAll();
		CreditCard card = ccRepository.findByCardNumber(cardNumber);
		ShoppingCart cart = scRepository.findById(id).get();
	    Users user = userRepository.findById(id).get();
	    List<CartProducts> products = cart.getProducts();
		model.addAttribute("card", card);
		model.addAttribute("cardBrend", cardBrend);
		model.addAttribute("cart", cart);
		model.addAttribute("user", user);
		model.addAttribute("products", products);
		
		
		return "paymentForm";
		}
	@PostMapping("/payment/{id}")	
	public String completePayment(@ModelAttribute("card") CreditCard card,@PathVariable("id") Integer id, @Param(value = "cardNumber")
	String cardNumber,@Param(value = "cardholderName") String cardholderName, @Param(value = "cvv")
	String cvv, @Param(value="month")String month,@Param(value="month")String year , Model model, Errors errors)   {
		
		
	
		
        ShoppingCart cart1 = scRepository.findById(id).get();
        
        
        
        	
        	CreditCard cc = ccRepository.findByCardNumber(cardNumber);
        	if(ccRepository.existsByCardNumber(cardNumber)) {
        		
        	
		
		if(!cc.getCardholderName().equals(cardholderName)) {
			
				return"redirect:/payment/"+ cart1.getId()+"?errorName";			
		 }
		
		 if(!cc.getCvv().equals(cvv)) {
			 
			 return"redirect:/payment/"+ cart1.getId()+"?errorCvv";
		 }
		 
		 if(!cc.getMonth().equals( month)) {
			 
			 return"redirect:/payment/"+ cart1.getId()+"?errorMonth";
		 }
		 if(!cc.getYear().equals(year)) {
			 
			 return"redirect:/payment/"+ cart1.getId()+"?errorYear";
		 }
		
		 if(cc.getBalance()< cart1.getTotal()) {
			 
			 return"redirect:/payment/"+ cart1.getId()+"?errorBalance";
			}
			cc.setBalance(cc.getBalance()-cart1.getTotal());
			
			ccRepository.save(cc);
			
			for (CartProducts product : cart1.getProducts()) {
				Store store = product.getProduct().getStore();
				
				store.setBalance(store.getBalance()+(product.getItemTotal() * 0.9));
				storeRepository.save(store);
			}
			
			String email = cc.getContact();
			
			Double amount = cart1.getTotal();
			
			try {
				
				
				sendPaymentInfo(email , cc, amount);
				
			}catch (UnsupportedEncodingException |MessagingException ex) {
				model.addAttribute("error", ex.getMessage());
				model.addAttribute("error", "Something bad happened while sending the email.");
			}
			
			
			return "redirect:/newOrder";
			
			
		}else {
			
			return"redirect:/payment/"+ cart1.getId()+"?errorNumber";
		
		
		
		}
	}

	private void sendPaymentInfo(String email, CreditCard cc, Double amount) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("Geca@geca.com", "Java imitacija");
		helper.setTo(email);
		
		String subject = "Payment ";
		String content =  "Payment has been made from one of your Credit card with number  " + cc.getCardNumber()+ 
				". The amount that was payed  was  " + amount +"$" + ". The new balance on your card now is: "+ cc.getBalance() + "$" ;
		
		helper.setSubject(subject);
		helper.setText(content, true);
		sender.send(message);
		
		
		
	}
	

}
