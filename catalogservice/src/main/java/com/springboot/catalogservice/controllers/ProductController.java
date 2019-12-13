package com.springboot.catalogservice.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springboot.catalogservice.entities.Product;
import com.springboot.catalogservice.exceptions.ProductNotFoundException;
import com.springboot.catalogservice.models.ProductInventoryResponse;
import com.springboot.catalogservice.services.InventoryServiceClient;
import com.springboot.catalogservice.services.ProductService;
import com.springboot.catalogservice.utils.MyThreadLocalsHolder;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

	private final ProductService productService;
	private final RestTemplate restTemplate;
	private final InventoryServiceClient inventoryServClient;

	@Autowired
	public ProductController(ProductService productService, RestTemplate restTemplate, InventoryServiceClient inventoryServClient) {
		this.productService = productService;
		this.restTemplate = restTemplate;
		this.inventoryServClient = inventoryServClient;
	}
	
	@GetMapping("")
	public List<Product> allProducts() {
		return productService.findAllProducts();
	}
	
	@GetMapping("/{code}")
	public Product productByCode(@PathVariable String code) {
		/*
		 * return productService.findProductByCode(code) .orElseThrow(() -> new
		 * ProductNotFoundException("Product with code [" + code + "] doesn't exist"));
		 */
		Optional<Product> product = productService.findProductByCode(code);
		if (product.isPresent()) {
			log.info("Fetching inventory level for product_code: "+code);
			ResponseEntity<ProductInventoryResponse> itemResponseEntity = restTemplate.getForEntity(
					"http://inventory-service/api/inventory/{code}", ProductInventoryResponse.class, code);
			if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
				Integer quantity = itemResponseEntity.getBody().getAvailableQuantity();
				log.info("Available quantity: " + quantity);
				product.get().setInStock(quantity > 0);
			} else {
				log.error("Unable to get inventory level for product_code: " + code + ", StatusCode: "
						+ itemResponseEntity.getStatusCode());
			}
        } else {
        	throw new ProductNotFoundException("Product with code [" + code + "] doesn't exist");
        }
		
		return product.get();
	}
	
	@GetMapping("/circuitBreaker/{code}")
	public Product productByCodeWithCB(@PathVariable String code) {
		log.info("Fetching Product Details by code with circuit breaker for the product : "+code);
		Optional<Product> product = productService.findProductByCode(code);
		if (product.isPresent()) {
			log.info("Fetching inventory level for product_code: "+code);
			String correlationId = UUID.randomUUID().toString();
			MyThreadLocalsHolder.setCorrelationId(correlationId);
			log.info("Before CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
			Optional<ProductInventoryResponse> response = inventoryServClient.getProductInventoryByCode(code);
			log.info("After CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
			if (response.isPresent() && response.get().getAvailableQuantity() > 0) {
				log.info("Available quantity : "+response.get().getAvailableQuantity());
				product.get().setInStock(true);
			}
        } else {
        	throw new ProductNotFoundException("Product with code [" + code + "] doesn't exist");
        }
		
		return product.get();
	}
	
}
