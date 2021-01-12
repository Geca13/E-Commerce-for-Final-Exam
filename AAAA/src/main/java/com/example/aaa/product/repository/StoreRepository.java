package com.example.aaa.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.aaa.product.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
	
     Boolean existsByStoreName(String storeName);
	
     Store findByStoreName(String storeName);
     
     Boolean existsByAccountNumber(String accountNumber);
 	
     Store findByAccountNumber(String accountNumber);

}
