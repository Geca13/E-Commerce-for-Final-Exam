package com.example.aaa.product.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.aaa.product.entity.Category;
import com.example.aaa.product.entity.Manufacturer;
import com.example.aaa.product.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	// sto pravime ako imaat isto iminja???
	
	Boolean existsByProductName(String productName);
	
    Product findByProductName(String productName);
	
    
    Boolean existsByKeyword(String keyword);
	
 //   Product findByKeyword(String keyword);
    
    
    
    
 
    
    

	@Query("SELECT DISTINCT product FROM Product product WHERE product.keyword LIKE :keyword%")
	@Transactional(readOnly = true)
	List<Product> findByKeyword(String keyword);
	
//	@Query("SELECT DISTINCT product FROM Product product WHERE product.country.countryName LIKE :productOrigin%")
//	@Transactional(readOnly = true)
//	List<Product>byOrigin(String countryName);

	
	
	
	
	Page<Product> findByCategory(Category category,Pageable pageable );
	
	List<Product> findByManufacturer(Manufacturer manufacturer);
	
	
	
	

}
