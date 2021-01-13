package com.example.aaa.shoppingCart.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.aaa.bank.repository.CreditCardRepository;
import com.example.aaa.product.entity.Product;
import com.example.aaa.product.repository.ProductRepository;
import com.example.aaa.shoppingCart.entity.CartProducts;
import com.example.aaa.shoppingCart.entity.Order;
import com.example.aaa.shoppingCart.entity.OrderProducts;
import com.example.aaa.shoppingCart.entity.ShippingOption;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import com.example.aaa.shoppingCart.repository.CartProductsRepository;
import com.example.aaa.shoppingCart.repository.OrderProductsRepository;
import com.example.aaa.shoppingCart.repository.OrderRepository;
import com.example.aaa.shoppingCart.repository.ShippingOptionsRepository;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.users.entity.Address;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.AddressRepository;
import com.example.aaa.users.repository.CountryRepository;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;

@Controller
public class OrderController {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	UsersRepository userRepository;
	@Autowired
	OrderProductsRepository orderPRepository;
	@Autowired
	CartProductsRepository cartPRepository;
	@Autowired
	ShoppingCartRepository cartRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	CreditCardRepository cardRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	ShippingOptionsRepository soRepository;
	@Autowired
	private JavaMailSender sender;
	
	@Value("${destination.email}")
	String destinationEmail;
	
	
	@GetMapping("/confirmAndPay")
	public String confirmAndPayment(@AuthenticationPrincipal UsersDetails userD,Model model) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        ShoppingCart cart = user.getCart();
        
        List<ShippingOption> options = soRepository.findAll();
        List<Address> listaddress = new ArrayList<>();
           
        List<Address> address1 = addressRepository.findAll();
        for (Address address2 : address1) {
			if(address2.getUser().getId() == user.getId()) {
				listaddress.add(address2);
			}
		}
        if(listaddress.isEmpty()) {
     	   model.addAttribute("messageAddress", "It looks like you are going to make your first order, please add a shipping address first");
     	 }
         model.addAttribute("cart", cart);
         model.addAttribute("user", user);
		 model.addAttribute("options", options);
		 model.addAttribute("listaddress", listaddress);
	   	 
	   	        return "shippmentAndPayment";
	}
	
	@PostMapping("/confirmAndPay")
	public String proccessPayment(@AuthenticationPrincipal UsersDetails userD, ShoppingCart cc) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        ShoppingCart cart = user.getCart();
       
          cart.setAddress(cc.getAddress());
          cart.setOption(cc.getOption());
        
          cartRepository.save(cart);
        
		return "redirect:/payment/"+ cart.getId();
	}
	
	
	@GetMapping("/newOrder")
	public String createOrder(@AuthenticationPrincipal UsersDetails userD, @ModelAttribute("order") Order order,@Param(value = "cardNumber") String cardNumber) {
		
		Order order1 = new Order();
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        ShoppingCart cart = user.getCart();
        
            order1.setDate(LocalDate.now());
            order1.setUser(user);
            order1.setShipping("by "+cart.getOption().getTransporter() +  " in "  + cart.getOption().getDays());
            order1.setSubtotal(cart.getTotal());
            order1.setTotal(cart.getTotal()+cart.getOption().getPrice());
            order1.setAddress(cart.getAddress().getStreetName() + " " + cart.getAddress().getStreetNumber()+ " " + cart.getAddress().getCity()+ " " + cart.getAddress().getZipCode()+ " " + cart.getAddress().getCountry().getCountryName());
       
        List<OrderProducts> products = new ArrayList<>();
        List<CartProducts> cartProducts = user.getCart().getProducts();
            
            for (CartProducts cartProducts2 : cartProducts) {
		     if(cartProducts2.getProduct().getAvailableQty() < cartProducts2.getQty()) {
			    	 return "redirect:/profile/cart?beated";
			      }
		    cartProducts2.getProduct().setAvailableQty(cartProducts2.getProduct().getAvailableQty() - cartProducts2.getQty());
			  
			productRepository.save(cartProducts2.getProduct());
			
			if(cartProducts2.getProduct().getAvailableQty()==0) {
				try {
					sendProductIsOutOfStockEmailToStore(cartProducts2.getProduct());
				} catch (UnsupportedEncodingException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
            for (CartProducts cartProducts2 : cartProducts) {
        	OrderProducts product = new OrderProducts();
        	product.setPid(cartProducts2.getProduct().getId());
        	product.setName(cartProducts2.getProduct().getProductName());
        	product.setQty(cartProducts2.getQty());
        	product.setPrice(cartProducts2.getProduct().getProductPrice());
        	product.setItemTotal(cartProducts2.getItemTotal());
        	product.setStore(cartProducts2.getProduct().getStore());
        	products.add(product);
        	orderPRepository.save(product);
        	
        	}
        
            order1.setShippingPrice(cart.getOption().getPrice());
            order1.setProducts(products);
            orderRepository.save(order1);
        
            for (CartProducts cartProducts2 : cartProducts) {
			cartProducts2.setProduct(null);
        	cartPRepository.save(cartProducts2);
        	}
            cartProducts.clear();
       
        List<CartProducts> cartProduc=  cartPRepository.findAll();
       
             for (CartProducts cartProducts2 : cartProduc) {
    	       if(cartProducts2.getProduct()==null) {
    		   cartPRepository.delete(cartProducts2); 
    	   }
		}
            cart.setTotal(null);
            cart.setAddress(null);
        
         cartRepository.save(cart);
         userRepository.save(user);
          
         try {
			sendOrderToAdmin(destinationEmail, order1);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return"redirect:/order/"+order1.getId();
	}
	
	private void sendOrderToAdmin(String destinationEmail2, Order order1) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("Geca@geca.com", "Java imitacija");
		
		for (OrderProducts order : order1.getProducts()) {
			
			helper.setTo(order.getStore().getEmail());
		
			
		String subject = "New Order!!! ";
		String content =  "New order with Id :" + order1.getId() + " was completed ..."
				+ "<p> Customer with shipping address: </p>"
				+"<p>"+ order1.getAddress() + "</p>"
				+ "<p> ordered product with </p>" 
				//+ "<p><b>"+order1.getProducts() + "</b></p>"
				+ "<p><b> id= "+order.getPid() + ", "+order.getName()+",and qty= "+order.getQty() + "</b></p>"
				+"<p> that needs to be shipped </p>"
				+"<p>"+ order1.getShipping() + "</p>";
		   
		helper.setSubject(subject);
		helper.setText(content, true);
		sender.send(message);
		
			}
		}
	
    private void sendProductIsOutOfStockEmailToStore( Product product) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		  helper.setFrom("Geca@geca.com", "Java imitacija");
		  helper.setTo(product.getStore().getEmail());
		
		String subject = "Product is out of Stock";
		String content =  "On the last order the product "
				+"<p>"+ product.getProductName() +"</p>"
						+ "<p>manufactured from:  </p>"
				+"<p>"+ product.getManufacturer().getManufacturerName() + "</p>"
				+ "<p>became unavailable , we sold all the quantity you submitted. </p>" 
				+ "<p>Please contact us if you want to sell more quantities from it on our platform.</p>" 
				+ "<p>If you do want to keep selling the product please provide the quantity you want to sell and the product id:</p>" + product.getId()
		        + "<p>Thank you and have a nice day.</p>" ;
		   
		  helper.setSubject(subject);
		  helper.setText(content, true);
		        sender.send(message);
		
			}
		
     @GetMapping("/order/{id}")
	 public String getOrder(Model model, @PathVariable("id")Integer id,@AuthenticationPrincipal UsersDetails userD) {
		
		Order order = orderRepository.findById(id).get();
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        List<OrderProducts> products = new ArrayList<>();
        List<OrderProducts> pro = orderPRepository.findAll();
        for (OrderProducts orderProducts : pro) {
		    if(order.getProducts().contains(orderProducts ) ) {
				products.add(orderProducts);
			}
        }
		model.addAttribute("products", products);
		model.addAttribute("order", order);
		model.addAttribute("user", user);
		
	           return "confirmOrder";
		}
	 	
 }
