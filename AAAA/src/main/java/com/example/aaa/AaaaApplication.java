package com.example.aaa;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.aaa.bank.entity.Bank;
import com.example.aaa.bank.entity.CardBrend;
import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;
import com.example.aaa.bank.entity.Year;
import com.example.aaa.bank.repository.BankRepository;
import com.example.aaa.bank.repository.CardBrendRepository;
import com.example.aaa.bank.repository.CreditCardRepository;
import com.example.aaa.bank.repository.MonthRepository;
import com.example.aaa.bank.repository.YearRepository;
import com.example.aaa.product.entity.Category;
import com.example.aaa.product.entity.Store;
import com.example.aaa.product.repository.CategoryRepository;
import com.example.aaa.product.repository.StoreRepository;
import com.example.aaa.shoppingCart.entity.ShippingOption;
import com.example.aaa.shoppingCart.repository.ShippingOptionsRepository;
import com.example.aaa.users.entity.Country;
import com.example.aaa.users.entity.Role;
import com.example.aaa.users.entity.RoleName;
import com.example.aaa.users.repository.CountryRepository;
import com.example.aaa.users.repository.RoleRepository;




@SpringBootApplication
public class AaaaApplication {

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	CardBrendRepository cbRepository;
	@Autowired
	CreditCardRepository ccRepository;
	@Autowired
	MonthRepository monthRepository;
	@Autowired
	YearRepository yearRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ShippingOptionsRepository soRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	BankRepository bankRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(AaaaApplication.class, args);
		
		
	}
		@PostConstruct
		public void init() {
			
			try {
				roleRepository.save(new Role(1, RoleName.ROLE_ADMIN));
				roleRepository.save(new Role(2, RoleName.ROLE_USER));
				roleRepository.save(new Role(3, RoleName.ROLE_SELLER));
		
				cbRepository.save(new CardBrend(1,"VISA"));
				cbRepository.save(new CardBrend(2,"MasterCard"));
				cbRepository.save(new CardBrend(3,"Maestro"));
				cbRepository.save(new CardBrend(4,"AMEX"));
				cbRepository.save(new CardBrend(5,"Diners"));
				
				bankRepository.save(new Bank(1,"NLB Tutunska banka"));
				bankRepository.save(new Bank(2,"Stopanska Banka"));
				bankRepository.save(new Bank(3,"Komercijalna Banka"));
				
				storeRepository.save(new Store(1,"Vero","geca1234567890@yahoo.com",bankRepository.findById(1).get(),"1234567890", 0.00));
				storeRepository.save(new Store(2,"Ramstore","karafiloski84@gmail.com",bankRepository.findById(2).get(),"2345678901", 0.00));
				storeRepository.save(new Store(3,"KAM","rashelagk@yahoo.com",bankRepository.findById(3).get(),"3456789012", 0.00));
				storeRepository.save(new Store(4,"TINEX","geca1234567890@yahoo.com",bankRepository.findById(1).get(),"4567890123", 0.00));
				
				countryRepository.save(new Country(1, "Macedonia"));
				countryRepository.save(new Country(2, "Norway"));
				countryRepository.save(new Country(3, "Germany"));
				countryRepository.save(new Country(4, "China"));
				countryRepository.save(new Country(5, "Singapur"));
				countryRepository.save(new Country(6, "UK"));
				countryRepository.save(new Country(7, "Denmark"));
				countryRepository.save(new Country(8, "USA"));
				countryRepository.save(new Country(9, "Switzerland"));
				countryRepository.save(new Country(10, "Sweden"));
				countryRepository.save(new Country(11, "Serbia"));
				countryRepository.save(new Country(12, "France"));
				countryRepository.save(new Country(13, "Argentina"));
				
				ccRepository.save(new CreditCard(1,"Marjan Karafiloski",cbRepository.findById(1).get(),"1234567890123456", "January","2021","123",100000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(2,"Stole Lazarov",cbRepository.findById(2).get(),"2345678901234567", "February","2022","234",200000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(3,"Dimitar Josifov",cbRepository.findById(3).get(),"3456789012345678", "March","2023","345",300000.00,"karafiloski84@gmail.com"));
				
				categoryRepository.save(new Category(1,"Dairy"));
				categoryRepository.save(new Category(2,"Candy"));
				categoryRepository.save(new Category(3,"Snacks"));
				categoryRepository.save(new Category(4,"Fresh Vegetable"));
				categoryRepository.save(new Category(5,"Fresh Fruits"));
				categoryRepository.save(new Category(6,"Alcohol and Tobbacco"));
				categoryRepository.save(new Category(7,"Health and Beauty"));
				categoryRepository.save(new Category(8,"Personal Care"));
				categoryRepository.save(new Category(9,"Home And Garden"));
				categoryRepository.save(new Category(10,"Seasonings"));
				categoryRepository.save(new Category(11,"Fresh Meats"));
				categoryRepository.save(new Category(12,"Frozen Meats"));
				categoryRepository.save(new Category(13,"Fish"));
				categoryRepository.save(new Category(14,"Canned products"));
				
				soRepository.save(new ShippingOption(1,"Macedonian Post", "5-7 days",0.00));
				soRepository.save(new ShippingOption(2,"Kargo Express", "2-3 days",15.00));
				soRepository.save(new ShippingOption(3,"FeDex", "0-1 days",35.35));
				
		
			} catch (Exception e) {
				System.err.println(e);
			}
		
		
	}

}
