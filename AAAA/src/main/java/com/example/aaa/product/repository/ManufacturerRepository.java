package com.example.aaa.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.aaa.product.entity.Manufacturer;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
	
    Boolean existsByManufacturerName(String manufacturerName);
	
    Manufacturer findByManufacturerName(String manufacturerName);
    
    @Query(value="select * from Manufacturer m where p.manufacturer_name like :word ", nativeQuery = true)
    List<Manufacturer> findByWord(@Param("word") String word);

}
