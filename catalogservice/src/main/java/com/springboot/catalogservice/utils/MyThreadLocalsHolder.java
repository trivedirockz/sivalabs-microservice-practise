package com.springboot.catalogservice.utils;

public class MyThreadLocalsHolder {
	private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<String>();
	
	public static void setCorrelationId(String correlationId) {
		CORRELATION_ID.set(correlationId);
	}
	
	public static String getCorrelationId() {
		return CORRELATION_ID.get();
	}
}
