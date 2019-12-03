package com.springboot.inventoryservice.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.inventoryservice.entities.InventoryItem;
import com.springboot.inventoryservice.repositories.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	@Autowired
	public InventoryService(InventoryRepository inventoryRepository) {
		super();
		this.inventoryRepository = inventoryRepository;
	}


	public Optional<InventoryItem> findByProductCode(String productCode) {
		Optional<InventoryItem> inventoryItem = null;
		inventoryItem = inventoryRepository.findByProductCode(productCode);
		return inventoryItem;
	}
	
	public List<InventoryItem> getInventory() {
        return inventoryRepository.findAll();
    }

}
