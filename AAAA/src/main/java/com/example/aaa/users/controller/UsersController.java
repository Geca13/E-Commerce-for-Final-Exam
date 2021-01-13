package com.example.aaa.users.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.aaa.product.entity.Category;
import com.example.aaa.product.entity.Product;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.CategoryRepository;
import com.example.aaa.product.repository.ManufacturerRepository;
import com.example.aaa.product.repository.ProductRepository;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.shoppingCart.entity.Order;
import com.example.aaa.shoppingCart.entity.OrderProducts;
import com.example.aaa.shoppingCart.repository.OrderRepository;
import com.example.aaa.users.entity.Address;
import com.example.aaa.users.entity.Country;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.AddressRepository;
import com.example.aaa.users.repository.CountryRepository;
import com.example.aaa.users.repository.RoleRepository;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;
import com.example.aaa.users.service.UsersServiceImpl;


@Controller
public class UsersController {
	
	@Autowired
	UsersRepository userRepository;
	@Autowired
	UsersServiceImpl usersServiceImpl;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ManufacturerRepository manufacturerRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	StoreRepository storeRepository;
	
	
	@GetMapping("/login")
	public String login(Model model , Product product) {
		
		 List<Product> all= productRepository.findAll();
	     List<Product> last10 = all.subList(all.size()-8,all.size());
	        model.addAttribute("last10", last10);
	           
	     List<Category> categories = categoryRepository.findAll();
	        model.addAttribute("categories", categories);
	        
	     Integer category = categories.size();
	        model.addAttribute("category", category);
	         
	     List<Store> stores = storeRepository.findAll();
	        model.addAttribute("stores", stores);
	           
		      return "login";
	}
	
	@GetMapping("/users")
	public String viewProductPage(Model model,@Param("search")String search) {
		
		 findPage(1,"firstName", "asc", model, search);
		 
		     return "all_users";
	}
	
	@GetMapping("/pages/{pageNumber}")
	public String findPage(@PathVariable("pageNumber") Integer pageNumber,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,Model model,
			@Param("search") String search) {
		
		Integer pageSize = 2;
		
		Page<Users>pages = usersServiceImpl.findPagina(pageNumber, pageSize, sortField, sortDir, search);
		
		List<Users> listUsers = pages.getContent();
		
		   model.addAttribute("currentPage",pageNumber);
		   model.addAttribute("totalPages", pages.getTotalPages());
		   model.addAttribute("totalItems", pages.getTotalElements());
		   model.addAttribute("sortField", sortField);
		   model.addAttribute("sortDir", sortDir);
		   model.addAttribute("search", search);
		   model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		   model.addAttribute("listUsers", listUsers);
		
		     return "all_users";
		}
	
	@GetMapping("/profile/changePassword")
	public String changePassword(@AuthenticationPrincipal UsersDetails userD, Model model ) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
	
		   model.addAttribute("user", user);
		
	         return "changePassword";
    }
	
	@PostMapping("/profile/changePassword")
	public String passwordForm( @AuthenticationPrincipal UsersDetails userD,@ModelAttribute("user")Users user, Model model)  {
		
		UsersServiceImpl validator = new UsersServiceImpl();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String userEmail = userD.getUsername();
	    Users user1 = userRepository.findByEmail(userEmail);
		
		  user1.setPassword(encoder.encode(user.getPassword()));
		   if(validator.validate(user.getPassword()) == false) {
			
	    	 return "redirect:/profile/changePassword?perror";
	       }
		userRepository.save(user1);
		
		     return "redirect:/profile?change" ;
	}
	
	@GetMapping("/profile")
	public String profile( @AuthenticationPrincipal UsersDetails userD, Model model) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
        
        List<Order> orders = new ArrayList<>();
        List<Order> ords = orderRepository.findAll();
        for (Order order : ords) {
        	if(order.getUser().getId()==user.getId()) {
        		orders.add(order);
        		
        		List<OrderProducts> products = order.getProducts();
        		for (OrderProducts product : products) {
        			
					model.addAttribute("product", product);
				}
        		model.addAttribute("products", products);
        	}
			
		}
                model.addAttribute("user", user);
		        model.addAttribute("orders", orders);
		
				
		         return "profile" ;
	}
	
	@GetMapping("/profile/newAddress")
	public String setAddress(@AuthenticationPrincipal UsersDetails userD, Model model,@ModelAttribute("address") Address address) {
		
	      String userEmail = userD.getUsername();
          Users user = userRepository.findByEmail(userEmail);
		  List<Country> listCountry = countryRepository.findAll();
            model.addAttribute("user", user);
		    model.addAttribute("listCountry", listCountry);
		
				   return "new_Address";
	}
	
	
	@PostMapping("/profile/newAddress")
	public String processCreationForm(@AuthenticationPrincipal UsersDetails userD, Address address) {
		
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
		
		Address  address1 = new Address();
		  address1.setStreetName(address.getStreetName());
		  address1.setStreetNumber(address.getStreetNumber());
		  address1.setCity(address.getCity());
		  address1.setZipCode(address.getZipCode());
		  address1.setCountry(address.getCountry());
		  address1.setUser(user);
		
		addressRepository.save(address1);
		
	             	return "redirect:/confirmAndPay";
	}
	
}
