package com.example.aaa.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.bank.entity.Bank;
import com.example.aaa.product.entity.Store;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
	
	Boolean existsByBankName(String bankName);
	
    Bank findByBankName(String bankName);
    
    

}
