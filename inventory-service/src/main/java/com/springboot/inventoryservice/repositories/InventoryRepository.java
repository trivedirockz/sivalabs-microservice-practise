package com.springboot.inventoryservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.inventoryservice.entities.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long>{
	Optional<InventoryItem> findByProductCode(String productCode);
}
