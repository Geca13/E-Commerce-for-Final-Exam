package com.example.aaa.product.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.example.aaa.bank.entity.Bank;
import com.example.aaa.bank.repository.BankRepository;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.service.AccountNumberAllreadyExistException;
import com.example.aaa.product.service.StoreServices;

@Controller
public class StoreController {
	
	@Autowired
	StoreServices storeServices;
	@Autowired
	BankRepository bankRepository;
	
	@GetMapping("/showNewStoreForm")
	public String showNewStoreForm (Model model) {
		
		Store store = new Store();
		List<Bank> banks = bankRepository.findAll();
		model.addAttribute("store", store);
		model.addAttribute("banks", banks);
		
		return "create_store";
	}
	
	@PostMapping("/saveStore")
	public  String createStore(Store store,Model model, MultipartFile file){
		
		try {
			storeServices.save(store,file);
		} catch (AccountNumberAllreadyExistException e) {
			model.addAttribute("error", e.getMessage());
			return "create_store";
		}
			return "redirect:/administration";
		}
	
}
