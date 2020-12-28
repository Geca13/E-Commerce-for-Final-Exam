package com.example.aaa.users.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import com.example.aaa.product.entity.Manufacturer;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import com.example.aaa.shoppingCart.repository.ShoppingCartRepository;
import com.example.aaa.users.entity.Role;
import com.example.aaa.users.entity.RoleName;
import com.example.aaa.users.entity.Users;
import com.example.aaa.users.repository.RoleRepository;
import com.example.aaa.users.repository.UsersRepository;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	ShoppingCartRepository cartRepository;

	private Pattern pattern;
	private Matcher matcher;
	
	private static  final String PASSWORD_REGEX = 
	        ("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})");
	
	
	public UsersServiceImpl() {
		
		pattern = Pattern.compile(PASSWORD_REGEX );
	}
	
	public boolean validate(final String password) {
		matcher =pattern.matcher(password);
		return matcher.matches();
	}

	
	
	
	


	@Override
	public Users save(Users userDto) throws InvalidPasswordException {
		
		
		UsersServiceImpl validator = new UsersServiceImpl();
		
		ShoppingCart cart = new ShoppingCart();
		cartRepository.save(cart);
	
		Users user = new Users();
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		    if(validator.validate(userDto.getPassword()) == false) {
		    	
		    	throw new InvalidPasswordException("Your chosen password doesnt fit our creteria , it must contain at least 1 number, UpperCase and LowerCase letters and 1 special character");
		    }
		 
		user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAge(userDto.getAge());
        user.setCart(cart);
        Role role = roleRepository.findByRole(RoleName.ROLE_ADMIN);
        user.setRoles( Collections.singleton(role));
		            
		LocalDate date = LocalDate.now();
		user.setDate(date);
		
		return usersRepository.save(user);
	}
	
	



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = usersRepository.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("You are not signUped with that email");
		}
		
		return new UsersDetails(user);
	}
	
	



	@Override
	public Page<Users> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		
		return usersRepository.findAll(pageable);
	}
	
	public void forgotPassword(String token, String email) throws UserNotFoundException {
		
		Users user = usersRepository.findByEmail(email);
		if(user != null) {
			user.setToken(token);
			usersRepository.save(user);
		} else {
			
			throw new UserNotFoundException("We dont have user with "+ email + " email, in our database ");
		}
	}
	
	public Users getToken(String token) {
		return usersRepository.findByToken(token);
	}
	
	public void updatePassword(Users user, String newPassword) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = encoder.encode(newPassword);
		user.setPassword(encodePassword);
		user.setToken(null);
		
		usersRepository.save(user);
	}
}
