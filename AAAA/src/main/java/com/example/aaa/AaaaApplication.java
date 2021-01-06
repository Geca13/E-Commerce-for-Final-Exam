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
	@Autowired
	CompanyRepository companyRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(AaaaApplication.class, args);
		
		
	}
		@PostConstruct
		public void init() {
			
			try {
				
				monthRepository.save(new Month(1,"January"));
				monthRepository.save(new Month(2,"February"));
				monthRepository.save(new Month(3,"March"));
				monthRepository.save(new Month(4,"April"));
				monthRepository.save(new Month(5,"May"));
				monthRepository.save(new Month(6,"June"));
				monthRepository.save(new Month(7,"July"));
				monthRepository.save(new Month(8,"August"));
				monthRepository.save(new Month(9,"September"));
				monthRepository.save(new Month(10,"October"));
				monthRepository.save(new Month(11,"November"));
				monthRepository.save(new Month(12,"December"));
				
				yearRepository.save(new Year(1,"2021"));
				yearRepository.save(new Year(2,"2022"));
				yearRepository.save(new Year(3,"2023"));
				yearRepository.save(new Year(4,"2024"));
				yearRepository.save(new Year(5,"2025"));
				yearRepository.save(new Year(6,"2026"));
				yearRepository.save(new Year(7,"2027"));
				yearRepository.save(new Year(8,"2028"));
				yearRepository.save(new Year(9,"2029"));
				yearRepository.save(new Year(10,"2030"));
				
				roleRepository.save(new Role(1, RoleName.ROLE_ADMIN));
				roleRepository.save(new Role(2, RoleName.ROLE_USER));
				
		        cbRepository.save(new CardBrend(1,"VISA"));
				cbRepository.save(new CardBrend(2,"MasterCard"));
				cbRepository.save(new CardBrend(3,"Maestro"));
				cbRepository.save(new CardBrend(4,"AMEX"));
				cbRepository.save(new CardBrend(5,"Diners"));
				
				bankRepository.save(new Bank(1,"NLB Tutunska banka"));
				bankRepository.save(new Bank(2,"Stopanska Banka"));
				bankRepository.save(new Bank(3,"Komercijalna Banka"));
				
				companyRepository.save(new Company(1,"0123456789","Dzikle",300000.00,"karafiloski84@gmail.com",bankRepository.findById(1).get()));
                companyRepository.save(new Company(2,"1234567890","Veropulous",300000.00,"geca1234567890@yahoo.com",bankRepository.findById(1).get()));
				companyRepository.save(new Company(3,"2345678901","Ramstore Ltd",400000.00,"karafiloski84@gmail.com",bankRepository.findById(2).get()));
				companyRepository.save(new Company(4,"3456789012","KAM Doo",500000.00,"rashelagk@yahoo.com",bankRepository.findById(3).get()));
				companyRepository.save(new Company(5,"4567890123","TINEX - Skopje",600000.00,"geca1234567890@yahoo.com",bankRepository.findById(1).get()));
				
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
				
				ccRepository.save(new CreditCard(1,"Marjan Karafiloski",cbRepository.findById(1).get(),"1234567890123456", monthRepository.findById(1).get(),yearRepository.findById(1).get(),"123",100000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(2,"Stole Lazarov",cbRepository.findById(2).get(),"2345678901234567", monthRepository.findById(2).get(),yearRepository.findById(2).get(),"234",200000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(3,"Dimitar Josifov",cbRepository.findById(3).get(),"3456789012345678", monthRepository.findById(3).get(),yearRepository.findById(3).get(),"345",300000.00,"karafiloski84@gmail.com"));
				
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
				categoryRepository.save(new Category(15,"Soft-Drinks"));
				
				soRepository.save(new ShippingOption(1,"Macedonian Post", "5-7 days",0.00));
				soRepository.save(new ShippingOption(2,"Kargo Express", "2-3 days",15.00));
				soRepository.save(new ShippingOption(3,"FeDex", "0-1 days",35.35));
				
			} catch (Exception e) {
				System.err.println(e);
			}
		
		
	}

}
