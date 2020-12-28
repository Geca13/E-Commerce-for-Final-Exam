package com.example.aaa.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aaa.bank.entity.Bank;
import com.example.aaa.bank.repository.BankRepository;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.StoreRepository;

@Service
public class StoreServices {
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	BankRepository bankRepository;

	public void save(Store store) throws AccountNumberAllreadyExistException {
		
		if(bankRepository.existsByBankName(store.getBank().getBankName())) {
			Bank bank = bankRepository.findByBankName(store.getBank().getBankName());
			
			store.setBank(bank);
		}else {
			Bank bank = new Bank();
			bankRepository.save(bank);
			store.setBank(bank);
		}
		
		
		if(storeRepository.existsByAccountNumber(store.getAccountNumber())) {
			
			throw new AccountNumberAllreadyExistException("The account number must be unique and this number is allready used for another store...");
		}
		
		storeRepository.save(store);
		
	}
}
