package com.example.aaa.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.example.aaa.bank.entity.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store", uniqueConstraints = {@UniqueConstraint(columnNames = "accountNumber")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String storeName;
	
	private String email;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private Bank bank;
	
	private String accountNumber;
	
	private Double balance;
	
	@Lob
	@Column
	private String image;

}
