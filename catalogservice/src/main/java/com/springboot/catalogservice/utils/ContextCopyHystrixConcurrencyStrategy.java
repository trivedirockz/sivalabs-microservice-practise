package com.springboot.catalogservice.utils;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ContextCopyHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

	private static boolean isInitialized = false;

	   public static synchronized void init()
	   {
	          if(!isInitialized)
	          {
	          log.info("CircuitBreakerAspect.init() registering ConcurrencyStrategy!!");
	           HystrixPlugins.getInstance().registerConcurrencyStrategy(new ContextCopyHystrixConcurrencyStrategy());
	           isInitialized = true;
	          }
	   }
	
	@Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new MyCallable(callable, MyThreadLocalsHolder.getCorrelationId());
    }
 
    public static class MyCallable<T> implements Callable<T> {
 
        private final Callable<T> actual;
        private final String correlationId;
 
        public MyCallable(Callable<T> callable, String correlationId) {
            this.actual = callable;
            this.correlationId = correlationId;
        }
 
        @Override
        public T call() throws Exception {
            MyThreadLocalsHolder.setCorrelationId(correlationId);
            try {
                return actual.call();
            } finally {
                MyThreadLocalsHolder.setCorrelationId(null);
            }
        }
    }
}
