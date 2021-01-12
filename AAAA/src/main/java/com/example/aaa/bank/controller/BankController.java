package com.example.aaa.bank.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.aaa.admin.entity.Payment;
import com.example.aaa.admin.repository.PaymentRepository;
import com.example.aaa.bank.entity.CardBrend;
import com.example.aaa.bank.entity.Company;
import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;
import com.example.aaa.bank.entity.Year;
import com.example.aaa.bank.repository.CardBrendRepository;
import com.example.aaa.bank.repository.CompanyRepository;
import com.example.aaa.bank.repository.CreditCardRepository;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.shoppingCart.entity.CartProducts;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;

@Controller
public class BankController {
	@Autowired
	UsersRepository userRepository;
	@Autowired
	CreditCardRepository ccRepository;
	@Autowired
	CardBrendRepository cbRepository;
	@Autowired
	ShoppingCartRepository scRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	PaymentRepository paymentRepository;
	
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
	public String completePayment(@PathVariable("id") Integer id, @Param(value = "cardNumber")
	String cardNumber,@Param(value = "cardholderName") String cardholderName, @Param(value = "cvv")
	String cvv, @Param(value="month")Month month,@Param(value="year")Year year , Model model)   {
		
		 Company company = companyRepository.findByAccountNumber("0123456789");
		 ShoppingCart cart1 = scRepository.findById(id).get();
         CreditCard card = ccRepository.findByCardNumber(cardNumber);
            model.addAttribute("card", card);
        	if(ccRepository.existsByCardNumber(cardNumber)) {
        		
        	   if(!card.getCardholderName().equals(cardholderName)) {
			         return"redirect:/payment/"+ cart1.getId()+"?errorName";			
		       }
		       if(!card.getCvv().equals(cvv)) {
			         return"redirect:/payment/"+ cart1.getId()+"?errorCvv";
		       }
		       if(card.getMonth() !=month) {
			         return"redirect:/payment/"+ cart1.getId()+"?errorMonth";
		       }
		       if(card.getYear() != year) {
			         return"redirect:/payment/"+ cart1.getId()+"?errorYear";
			   }
		       if(card.getBalance()< cart1.getTotal()) {
			         return"redirect:/payment/"+ cart1.getId()+"?errorBalance";
			   }
		       Double orderTotal = cart1.getTotal()+cart1.getOption().getPrice();
			   card.setBalance(card.getBalance()-orderTotal);
			   company.setAccountBalance(company.getAccountBalance() + orderTotal);
			     companyRepository.save(company);
			     ccRepository.save(card);
			
			for (CartProducts product : cart1.getProducts()) {
				Store store = product.getProduct().getStore();
				
				store.setBalance(store.getBalance()+(product.getItemTotal() * 0.9));
				storeRepository.save(store);
				
			   }
			
		 String email = card.getContact();
		 Double amount = orderTotal;
			
			try {
				
				sendPaymentInfo(email , card, amount);
				
			}catch (UnsupportedEncodingException |MessagingException ex) {
				model.addAttribute("error", ex.getMessage());
				model.addAttribute("error", "Something bad happened while sending the email.");
			}
			         payToStoresAutomatic(model);
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
		String content =  "Payment has been made from one of your " + cc.getBrend().getBrend() + "'s ending with " + cc.getCardNumber().substring(12) + 
				". The amount that was payed  was  " + amount +"$" + ". The new balance on your card now is: "+ cc.getBalance() + "$" ;
		
		  helper.setSubject(subject);
		  helper.setText(content, true);
		 sender.send(message);
		 
		}
	
	public void payToStoresAutomatic(Model model) {
		Payment payment = new Payment();
		List<Store> stores = storeRepository.findAll();
		  for (Store store : stores) {
			 if (store.getBalance() > 20000.00 ) {
			   Company company = companyRepository.findByAccountNumber(store.getAccountNumber());
				 company.setAccountBalance(company.getAccountBalance() + store.getBalance());
			   Company comp = companyRepository.findByAccountNumber("0123456789");
				 comp.setAccountBalance(comp.getAccountBalance()-store.getBalance());
				 
				 payment.setCompany(company);
				 payment.setSum(store.getBalance());
				 payment.setLocalDateTime(LocalDateTime.now());
				 
				   paymentRepository.save(payment);
				   companyRepository.save(company);
				   companyRepository.save(comp);
				   
				String email = payment.getCompany().getEmail();
				Double amount = payment.getSum();
			    LocalDateTime time = payment.getLocalDateTime();
				Double balance = payment.getCompany().getAccountBalance();  
				
				try {
					sendAutomaticPaymentConfirmationToStore(email, amount, time, balance);
				} catch (UnsupportedEncodingException | MessagingException ex) {
					model.addAttribute("error", ex.getMessage());
					model.addAttribute("error", "Something bad happened while sending the email.");
				}
				
				String email2 = comp.getEmail();
				Double balance2 = comp.getAccountBalance();
				
				try {
					sendAutomaticPaymentConfirmationToAdmin(email2, payment, balance2);
				} catch (UnsupportedEncodingException | MessagingException ex) {
					model.addAttribute("error", ex.getMessage());
					model.addAttribute("error", "Something bad happened while sending the email.");
				}
				   store.setBalance(0.00);
				   storeRepository.save(store);

			}
		}
	}
	
     private void sendAutomaticPaymentConfirmationToStore(String email, Double amount,LocalDateTime time, Double balance) throws UnsupportedEncodingException, MessagingException {
		
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
     
     private void sendAutomaticPaymentConfirmationToAdmin(String email, Payment payment, Double balance) throws UnsupportedEncodingException, MessagingException {
 		
 		MimeMessage message = sender.createMimeMessage();
 		MimeMessageHelper helper = new MimeMessageHelper(message);
 		
 		  helper.setFrom("Geca@geca.com", "Java imitacija");
 		  helper.setTo(email);
 		
 		String subject = "Payment was made to "+payment.getCompany().getCompanyName();
 		String content =  "On " + payment.getLocalDateTime() + 
 					" . a payment of " + payment.getSum() +"$" + " was made to " + payment.getCompany().getCompanyName() + " , and the new balance in your account is "+ balance + "$" ;
 		
 		  helper.setSubject(subject);
 		  helper.setText(content, true);
 		 sender.send(message);
      }
	
	}
