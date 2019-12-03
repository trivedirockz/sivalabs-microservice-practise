package com.springboot.inventoryservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotFoundException extends RuntimeException {

	public InventoryNotFoundException() {
	}
	
	public InventoryNotFoundException(String message) {
        super(message);
    }
 
    public InventoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public InventoryNotFoundException(Throwable cause) {
        super(cause);
    }
}
