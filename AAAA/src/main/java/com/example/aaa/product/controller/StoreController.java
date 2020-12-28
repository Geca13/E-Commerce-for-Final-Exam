package com.example.aaa.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.aaa.product.entity.Product;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.service.AccountNumberAllreadyExistException;
import com.example.aaa.product.service.StoreServices;

@Controller
public class StoreController {
	
	@Autowired
	StoreServices storeServices;
	
	@GetMapping("/showNewStoreForm")
	public String showNewStoreForm (Model model) {
		
		Store store = new Store();
		
		model.addAttribute("store", store);
		
		return "create_store";
	
}
	
	@PostMapping("/saveStore")
	public  String createStore(Store store,Model model){
		
			
		try {
			storeServices.save(store);
		} catch (AccountNumberAllreadyExistException e) {
			model.addAttribute("error", e.getMessage());
			
		}
			
		return "redirect:/product";
		}
	

}
