package com.example.aaa.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.product.entity.Product;

@Repository
public interface ProductRepo extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE CONCAT(p.productName, ' ', p.productPrice, ' ', p.keyword) LIKE %?1%")
	//@Transactional(readOnly = true)
	Page<Product> findBySearch(String search,Pageable pageable );
}
