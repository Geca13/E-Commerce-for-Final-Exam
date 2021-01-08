package com.example.aaa.product.service;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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

	public void save(Store store, MultipartFile file) throws AccountNumberAllreadyExistException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(fileName.contains("..")) {
        	System.out.println("not a valid file");
        }
        
        try {
			store.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(bankRepository.existsByBankName(store.getBank().getBankName())) {
			Bank bank = bankRepository.findByBankName(store.getBank().getBankName());
			
			store.setBank(bank);
		}
		
		if(storeRepository.existsByAccountNumber(store.getAccountNumber()) == true) {
			
			throw new AccountNumberAllreadyExistException("The account number must be unique and this number is allready used from another store...");
		}
		 store.setBalance(0.00);
		
		storeRepository.save(store);
		
	}
}
