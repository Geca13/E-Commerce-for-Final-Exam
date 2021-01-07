package com.example.aaa.product.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.aaa.product.entity.Category;
import com.example.aaa.product.entity.Comment;
import com.example.aaa.product.entity.Manufacturer;
import com.example.aaa.product.entity.Product;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.CategoryRepository;
import com.example.aaa.product.repository.CommentRepository;
import com.example.aaa.product.repository.ManufacturerRepository;
import com.example.aaa.product.repository.ProductRepository;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.product.service.ProductServiceImpl;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.users.entity.Country;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.AddressRepository;
import com.example.aaa.users.repository.CountryRepository;
import com.example.aaa.users.repository.UsersRepository;
import com.example.aaa.users.service.UsersDetails;


@Controller
public class ProductController {
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UsersRepository userRepository;
	
	@Autowired
	ShoppingCartRepository cartRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductServiceImpl productServiceImpl;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ManufacturerRepository manufacturerRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	CountryRepository countryRepository;
	
	  @GetMapping("/showNewProductForm")
	  public String showNewProductForm (Model model) {
		
		Product product = new Product();
		 List<Store> stores = storeRepository.findAll();
		 List<Category> categories = categoryRepository.findAll();
	     List<Country> countries = countryRepository.findAll();
		
		  model.addAttribute("product", product);
		  model.addAttribute("stores", stores);
	      model.addAttribute("categories", categories);
		  model.addAttribute("countries", countries);
		
		       return "add_product";
	
}
	
	  @PostMapping("/saveProduct")
	  public  String createProduct(Product product, MultipartFile file){
		
		productServiceImpl.save(product, file);
			
		       return "redirect:/product";
	}
	
	  @GetMapping("/")
	  public String index(Model model, @ModelAttribute ("product")Product product,@AuthenticationPrincipal UsersDetails userD,Pageable pageable) {
		String userEmail = userD.getUsername();
        Users user = userRepository.findByEmail(userEmail);
           model.addAttribute("user", user);
        
         List<Product> last10= productRepository.findAll();
     //   List<Product> last10 = all.subList(all.size()-8,all.size());
           model.addAttribute("last10", last10);
        
               return "index";
    }
	
	  @GetMapping("/product")
	  public String viewProductPage(Model model , @ModelAttribute("product")Product product) {
		
		 findPages(1,"productName", "asc", model);
		 
		       return "all_products";
	}
	
	   @GetMapping("pagess/{pageNumberss}")
	   public String findPages(@PathVariable("pageNumberss") Integer pageNumberss,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		Integer pageSize = 5;
		
		Page<Product>pagess = productServiceImpl.findPaginated(pageNumberss, pageSize, sortField, sortDir);
		
		List<Product> listProducts = pagess.getContent();
		  model.addAttribute("currentPage",pageNumberss);
		  model.addAttribute("totalPages", pagess.getTotalPages());
	  	  model.addAttribute("totalItems", pagess.getTotalElements());
		  model.addAttribute("sortField", sortField);
		  model.addAttribute("sortDir", sortDir);
		  model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		  model.addAttribute("listProducts", listProducts);
		
		       return "all_products";
		}
	
	  @GetMapping("/products")
	  public String viewProductPageInGrid(Model model,
			@Param("search")String search,
			@Param("pid")Integer pid,
			@Param("mid")Integer mid,
			@Param("cid")Integer cid,
			@Param("sid")Integer sid) {
		
		
		  gridDetails(model,  1,search,pid,mid,cid,sid);
		
		       return "productGrid";
	}
	
	   @GetMapping("/pag/{pagNum}")
	   public String gridDetails( Model model ,@PathVariable("pagNum") Integer pagNum,
			@Param("search")String search,
			@Param("pid")Integer pid,
			@Param("mid")Integer mid,
			@Param("cid")Integer cid,
			@Param("sid")Integer sid) {
		
        
        List<Product> listProducts = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        List<Manufacturer> brends = manufacturerRepository.findAll();
        List<Country> countries = countryRepository.findAll();
        List<Store> stores = storeRepository.findAll();
   
	    Integer pageSize = 12;
	    
	    Page<Product>pag = productServiceImpl.grid(pagNum, pageSize,search,pid,mid,cid,sid);
	    
	    List<Product> listProductss = pag.getContent();
	    
	    for (Product product2 : listProductss) {
	    	if(product2.getAvailableQty()> 0) {
	    		
	   		listProducts.add(product2);
	     	}
	     }
	 
		  model.addAttribute("listProducts", listProducts);
	   	  model.addAttribute("currentPage",pagNum);
		  model.addAttribute("totalPages", pag.getTotalPages());
		  model.addAttribute("totalItems", pag.getTotalElements());
		  model.addAttribute("search", search);
		  model.addAttribute("categories", categories);
		  model.addAttribute("pid", pid);
		  model.addAttribute("brends", brends);
		  model.addAttribute("mid", mid);
		  model.addAttribute("countries", countries);
		  model.addAttribute("cid", cid);
		  model.addAttribute("stores", stores);
		  model.addAttribute("sid", sid);
		
		       return "productGrid";
	}
	
	    @GetMapping("/showUpdateProductForm/{id}")
		public String showUpdateProductForm (Model model,@PathVariable	("id") Integer id) {
			
			Product product = productRepository.findById(id).get();
			 List<Store> stores = storeRepository.findAll();
			 List<Category> categories = categoryRepository.findAll();
			 List<Country> countries = countryRepository.findAll();
			
			   model.addAttribute("product", product);
			   model.addAttribute("stores", stores);
			   model.addAttribute("categories", categories);
			   model.addAttribute("countries", countries);
			
			   return "updateProduct";
	}
		
		@PostMapping("/showUpdateProductForm/{id}")
		public String completeUpdateProductForm (@PathVariable	("id") Integer id, @ModelAttribute("product") Product product) {
			
			Product product1 = productRepository.findById(id).get();
		      product1.setProductName(product.getProductName());
			  product1.setKeyword(product.getKeyword());
			  product1.setProductPrice(product.getProductPrice());
			  product1.setAvailableQty(product.getAvailableQty());
			  product1.setCategory(product.getCategory());
			  product1.setCountry(product.getCountry());
			  product1.setManufacturer(product.getManufacturer());
			  product1.setStore(product.getStore());
			
			productRepository.save(product1);
			  
		      return "redirect:/";
	}
		
	   @GetMapping("/products/productDetails/{id}")
	   public String profileDetailsComments(@PathVariable("id")Integer id, Model model) {
		    Product product = productRepository.findById(id).get();
			
	          profileDetails(id, model,1 );
			
			    return "productDetails";
	}
		
		
	@GetMapping("/products/productDetails/{id}/pg/{pagNo}")
	public String profileDetails(@PathVariable("id")Integer id, Model model,@PathVariable("pagNo") Integer pagNo) {
		Product product = productRepository.findById(id).get();
		Comment comment1 = new Comment();
		
	    Integer pageSize = 5;
		 Page<Comment>pg = productServiceImpl.comments(pagNo, pageSize,product);
		 List<Comment> comment = pg.getContent();
			     
		    model.addAttribute("comment1", comment1);
			model.addAttribute("product", product);
			model.addAttribute("comment", comment);
			
			model.addAttribute("currentPage",pagNo);
			model.addAttribute("totalPages", pg.getTotalPages());
			model.addAttribute("totalItems", pg.getTotalElements());
			    return "productDetails";
	}
		
		@PostMapping("/addComment/{id}")
		public String postComment(@PathVariable("id")Integer id,@AuthenticationPrincipal UsersDetails userD, @ModelAttribute("comment")Comment comment) {
			Product product = productRepository.findById(id).get();
			String userEmail = userD.getUsername();
	        Users user = userRepository.findByEmail(userEmail);
	        Comment comment1 = new Comment();
	        comment1.setProduct(product);
	        comment1.setUser(user);
	        comment1.setUserComment(comment.getUserComment());
	        comment1.setTime(LocalDateTime.now());
	          if(comment.getUserComment().isEmpty())
	        return "redirect:/products/productDetails/"+product.getId()+"?emptyPost";
	          commentRepository.save(comment1);
	        
	        return "redirect:/products/productDetails/"+product.getId()+"?post";
		}
		
		@GetMapping("/deleteProduct/{id}")
		public String deleteProduct(@PathVariable("id")Integer id) {
			
			Product product = productRepository.findById(id).get();
			product.setCategory(null);
			product.setManufacturer(null);
			product.setStore(null);
			product.setCountry(null);
			productRepository.save(product);
			List<Comment> comments = commentRepository.findAll();
			for (Comment comment : comments) {
				if( comment.getProduct()== product) {
					commentRepository.delete(comment);
				}
			}
			   productRepository.deleteById(id);
			 return "redirect:/product";
		
		}		
		
}
