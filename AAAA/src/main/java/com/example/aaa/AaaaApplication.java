package com.example.aaa;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.aaa.bank.entity.Bank;
import com.example.aaa.bank.entity.CardBrend;
import com.example.aaa.bank.entity.Company;
import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;
import com.example.aaa.bank.entity.Year;
import com.example.aaa.bank.repository.BankRepository;
import com.example.aaa.bank.repository.CardBrendRepository;
import com.example.aaa.bank.repository.CompanyRepository;
import com.example.aaa.bank.repository.CreditCardRepository;
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
                                companyRepository.save(new Company(2,"2345678901","Veropulous",300000.00,"geca1234567890@yahoo.com",bankRepository.findById(1).get()));
				companyRepository.save(new Company(3,"1234567890","Ramstore Ltd",400000.00,"karafiloski84@gmail.com",bankRepository.findById(2).get()));
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
				
				ccRepository.save(new CreditCard(1,"Marjan Karafiloski",cbRepository.findById(1).get(),"1234567890123456", Month.JANUARY, Year.PRVA,"123",100000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(2,"Stole Lazarov",cbRepository.findById(2).get(),"2345678901234567", Month.FEBRUARY,Year.VTORA,"234",200000.00,"karafiloski84@gmail.com"));
				ccRepository.save(new CreditCard(3,"Dimitar Josifov",cbRepository.findById(3).get(),"3456789012345678", Month.MARCH,Year.TRETA,"345",300000.00,"karafiloski84@gmail.com"));
				
				soRepository.save(new ShippingOption(1,"Macedonian Post", "5-7 days",0.00));
				soRepository.save(new ShippingOption(2,"Kargo Express", "2-3 days",15.00));
				soRepository.save(new ShippingOption(3,"FeDex", "0-1 days",35.35));
				
			} catch (Exception e) {
				System.err.println(e);
			}
		
		
	}

}
