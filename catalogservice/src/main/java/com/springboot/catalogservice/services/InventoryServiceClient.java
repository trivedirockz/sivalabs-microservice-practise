package com.springboot.catalogservice.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.catalogservice.models.ProductInventoryResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryServiceClient {
	private final RestTemplate restTemplate;
	
	@Autowired
	public InventoryServiceClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@HystrixCommand(fallbackMethod="getDefaultProductInventoryResponse")
	public Optional<ProductInventoryResponse> getProductInventoryByCode(String pCode) {
		ResponseEntity<ProductInventoryResponse> itemResponseEntity = restTemplate
				.getForEntity("http://inventory-service/api/inventory/{code}", ProductInventoryResponse.class, pCode);
		if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
			return Optional.ofNullable(itemResponseEntity.getBody());
		} else {
			log.error("Inventory not found for product : "+pCode);
			return Optional.empty();
		}
	}
	
	Optional<ProductInventoryResponse> getDefaultProductInventoryResponse(String pCode) {
		log.info("Returning a default product inventory response for the product : "+pCode);
		ProductInventoryResponse piResponse = new ProductInventoryResponse();
		piResponse.setAvailableQuantity(50);
		piResponse.setProductCode(pCode);
		return Optional.ofNullable(piResponse);
	}
}
