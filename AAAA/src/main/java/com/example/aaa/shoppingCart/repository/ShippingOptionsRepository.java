package com.example.aaa.shoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.shoppingCart.entity.ShippingOption;

@Repository
public interface ShippingOptionsRepository extends JpaRepository<ShippingOption, Integer> {

}
