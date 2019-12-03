package com.springboot.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.inventoryservice.entities.InventoryItem;
import com.springboot.inventoryservice.exceptions.InventoryNotFoundException;
import com.springboot.inventoryservice.services.InventoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/inventory")
public class InventoryController {
	
	private final InventoryService inventoryService;

	@Autowired
	public InventoryController(InventoryService inventoryService) {
		super();
		this.inventoryService = inventoryService;
	} 
	
	@GetMapping("/{productCode}")
    public InventoryItem findInventoryByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Finding inventory for product code :"+productCode);
        return inventoryService.findByProductCode(productCode)
        		.orElseThrow(() -> new InventoryNotFoundException("InventoryItem with product code [" + productCode + "] doesn't exist"));
    }
	
	@GetMapping("/getInventoryForAll")
	public List<InventoryItem> getInventory() {
        log.info("Finding inventory for all products ");
        return inventoryService.getInventory();
    }
	
}
