package com.example.aaa.bank.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String cardholderName;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private CardBrend brend;
	
	private String cardNumber;
	
	@Enumerated
        private Month month;
	
	@Enumerated
        private Year year;
    
        private String cvv;
    
        private Double balance;
    
        private String contact;
     
}
