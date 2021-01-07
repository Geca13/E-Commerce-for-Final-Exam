package com.example.aaa.product.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
import com.example.aaa.users.entity.Country;
import com.example.aaa.users.repository.CountryRepository;


@Service
public class ProductServiceImpl  {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ManufacturerRepository manufacturerRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	StoreRepository storeRepository;

	
	public Page<Product> findPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
		
	    Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
		Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		
		          return productRepository.findAll(pageable);
	}
	
     public Page<Product> grid(Integer pageNumber, Integer pageSize,String search, Integer pid, Integer mid,Integer cid,Integer sid) {
		
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		       if(search != null ) {
		          return	productRepository.findBySearch(search, pageable);
		    }else if(pid != null ) {
			      return	productRepository.findAllProductByCategoryId(pid, pageable);
			}else if(mid != null ) {
			      return	productRepository.findAllProductByManufacturerId(mid, pageable);
			}else if(cid != null ) {
			      return	productRepository.findAllProductByCountryId(cid, pageable);
			}else if(sid != null ) {
			      return	productRepository.findAllProductByStoreId(sid, pageable);
			}
		          return productRepository.findAll(pageable);
	
	}
	
	
	 public void save(Product product, MultipartFile file) {
	    	 
	         String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		        if(fileName.contains("..")) {
		        	System.out.println("not a valid file");
		        }
		        try {
					product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		        
		        
		        if(storeRepository.existsByStoreName(product.getStore().getStoreName())) {
		        	Store store1 = storeRepository.findByStoreName(product.getStore().getStoreName());
		        	product.setStore(store1);
		        }
		
		        if(categoryRepository.existsByProductCategory(product.getCategory().getProductCategory())) {
    			
    			Category cat = categoryRepository.findByProductCategory(product.getCategory().getProductCategory());
    			
    			product.setCategory(cat);
    		    }
		
	  
	        	if(manufacturerRepository.existsByManufacturerName(product.getManufacturer().getManufacturerName())) {
	    			
	    	      	Manufacturer man = manufacturerRepository.findByManufacturerName(product.getManufacturer().getManufacturerName());
	    			
	    			product.setManufacturer(man);
	    			product.setCountry(man.getCountry());
	    		}
	        
	                product.setTime(LocalDateTime.now());
    		             productRepository.save(product);
		}
	 
	 public void update(Product product) {
    	 
            if(storeRepository.existsByStoreName(product.getStore().getStoreName())) {
	        	Store store1 = storeRepository.findByStoreName(product.getStore().getStoreName());
	        	product.setStore(store1);
	        }
	
	        if(categoryRepository.existsByProductCategory(product.getCategory().getProductCategory())) {
			
			Category cat = categoryRepository.findByProductCategory(product.getCategory().getProductCategory());
			
			product.setCategory(cat);
		    }
	
        	if(countryRepository.existsByCountryName(product.getCountry().getCountryName())) {
        		Country country = countryRepository.findByCountryName(product.getCountry().getCountryName());
        		
        		product.setCountry(country);
        	}
        	
        	if(manufacturerRepository.existsByManufacturerName(product.getManufacturer().getManufacturerName())) {
    			
    	      	Manufacturer man = manufacturerRepository.findByManufacturerName(product.getManufacturer().getManufacturerName());
    			
    			product.setManufacturer(man);
    		}
        	
		        productRepository.save(product);
	   }
	
        public Page<Comment> comments(Integer pageNumber, Integer pageSize, Product product) {
		
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("time").descending());
		
		                 return commentRepository.findAllByProductId(product.getId(),pageable );
	
       }
        
        public void newCategory(Category category,MultipartFile file) {
        	
        	
        	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        if(fileName.contains("..")) {
	        	System.out.println("not a valid file");
	        }
	        try {
				category.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				
				e.printStackTrace();
			}  
	        
	        categoryRepository.save(category);
        	
        }
        
        public void newManufacturer(Manufacturer manufacturer) {
        	
        	if(countryRepository.existsByCountryName(manufacturer.getCountry().getCountryName())) {
        		Country country = countryRepository.findByCountryName(manufacturer.getCountry().getCountryName());
        		
        		manufacturer.setCountry(country);
        	}
        	
        	manufacturerRepository.save(manufacturer);
        	
        }

}
