package com.springboot.catalogservice.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.catalogservice.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	Optional<Product> findByCode(String code);
}
