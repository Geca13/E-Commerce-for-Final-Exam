package com.example.aaa.shoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.aaa.shoppingCart.entity.CartProducts;

public interface CartProductsRepository extends JpaRepository<CartProducts, Integer> {

}
