package com.shoppingcart.ui.shoppingcartUi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.shoppingcart.ui.shoppingcartUi.authentication.AuthHeaderFilter;

@EnableZuulProxy
@SpringBootApplication
@ComponentScan
public class ShoppingcartUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartUiApplication.class, args);
	}
	
	@Bean
	AuthHeaderFilter authHeaderFilter() {
	    return new AuthHeaderFilter();
	}

}
