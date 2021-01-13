package com.example.aaa.shoppingCart.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.aaa.product.entity.Product;
import com.example.aaa.product.repository.ProductRepository;
import com.example.aaa.shoppingCart.entity.CartProducts;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import com.example.aaa.shoppingCart.repository.CartProductsRepository;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.shoppingCart.service.ShoppingCartService;
import com.example.aaa.users.entity.Address;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.AddressRepository;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;

@Controller
public class ShoppingCartController {
	
	@Autowired
	ShoppingCartService shoppingCartService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	UsersRepository userRepository;
	@Autowired
	ShoppingCartRepository cartRepository;
	@Autowired
	CartProductsRepository cartPrRepository;
	
	@GetMapping("/profile/cart")
	public String showShoppingCartS( @AuthenticationPrincipal UsersDetails userD, Model model) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
	    ShoppingCart cart = user.getCart();
	    Integer totalQty = 0;
        List<CartProducts> products = cart.getProducts();
           for (CartProducts cartProducts : products) {
			totalQty = totalQty + cartProducts.getQty();
			model.addAttribute("totalQty", totalQty);
		}
        Integer totalProductsInCart = products.size();
        Double total = cart.getTotal();
    
         model.addAttribute("totalProductsInCart", totalProductsInCart);
         model.addAttribute("user", user);
		 model.addAttribute("cart", cart);
		 model.addAttribute("products", products);
		 model.addAttribute("total", total);
		
			    return "confirmShoppingCart";
	}

	@GetMapping("/shoppingCart/{id}")
	public String addProductToShoppingCart(@AuthenticationPrincipal UsersDetails userD , @Param (value="prod") Integer prod,
			 @PathVariable("id")  Integer id ,Address address) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        ShoppingCart cart1 = user.getCart();
        Product p = productRepository.findById(id).get();
		List<CartProducts>cartProducts = cart1.getProducts();
		   for (CartProducts cartProducts2 : cartProducts) {
			   if(cartProducts2.getProduct()== p) {
		    	  return "redirect:/profile/cart?allreaddyAdded";
		      }
		 }
		       
	     CartProducts cp = new CartProducts();
		   cp.setProduct(p);
		   cp.setQty(1);
		    cartPrRepository.save(cp);
		   cartProducts.add(cp);
           cart1.setProducts(cartProducts);
           user.setCart(cart1);
      
         Double itemTotal =  0.00; 
        
           for (CartProducts cartProducts2 : cartProducts) {
			itemTotal = cartProducts2.getQty() * cartProducts2.getProduct().getProductPrice();
		  }
        
            cp.setItemTotal(itemTotal);    
             cartPrRepository.save(cp);
    
          Double total = 0.00;
        	for (CartProducts product : cartProducts) {
			 total = total + product.getItemTotal();
		    }
		
	         cart1.setTotal(total);  
              cartRepository.save(cart1);
              userRepository.save(user);
        
                  return "redirect:/products?productAdded";
      }
	
    @GetMapping("/removeProduct/{id}")
	public String removeProductFromShoppingCart(@AuthenticationPrincipal UsersDetails userD ,
			 @PathVariable("id")  Integer id ) {
		
		CartProducts p = cartPrRepository.findById(id).get();
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        ShoppingCart cart = user.getCart();
        List<CartProducts>cartProducts = cart.getProducts();
          if(cartProducts.contains(p)) {
        	  cart.setTotal(cart.getTotal()-(p.getProduct().getProductPrice()*p.getQty()));
         }
        	  p.setProduct(null);
        	  cartProducts.remove(p);
        	  if(cartProducts.isEmpty()) {
       		     cart.setTotal(null);
        	  
        	       cartPrRepository.delete(p);
         }
        cartRepository.save(cart);
        userRepository.save(user);
        if(cartProducts.isEmpty()) {
        	return "redirect:/";
        }
      
         return "redirect:/profile/cart";
	}
    
    @GetMapping("/addQty/{id}")
    public String increaseQuantity(@AuthenticationPrincipal UsersDetails userD, @PathVariable("id") Integer id) {
    	
    	String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        CartProducts product = cartPrRepository.findById(id).get();
        ShoppingCart cart = user.getCart();
        
           product.setQty(product.getQty()+1);
            if(product.getProduct().getAvailableQty() < product.getQty()) {
            	return "redirect:/profile/cart?noMoreQty";
            }
        
        cartPrRepository.save(product);
        product.setItemTotal(product.getItemTotal()+product.getProduct().getProductPrice());
        cartPrRepository.save(product);
        cart.setTotal(cart.getTotal() + product.getProduct().getProductPrice());
        cartRepository.save(cart);
    	        return "redirect:/profile/cart";
    }
	
    @GetMapping("/removeQty/{id}")
    public String decreaceQuantity(@AuthenticationPrincipal UsersDetails userD, @PathVariable("id") Integer id) {
    	
    	String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        CartProducts product = cartPrRepository.findById(id).get();
        ShoppingCart cart = user.getCart();
        
          product.setQty(product.getQty()-1);
            if(product.getQty()==0) {
        	   return "redirect:/removeProduct/{id}";
        }
        cartPrRepository.save(product);
        product.setItemTotal(product.getItemTotal()-product.getProduct().getProductPrice());
        cartPrRepository.save(product);
        
        
        cart.setTotal(cart.getTotal() - product.getProduct().getProductPrice());
      
       cartRepository.save(cart);
    	return "redirect:/profile/cart";
    }
	
}
