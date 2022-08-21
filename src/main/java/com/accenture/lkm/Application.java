package com.accenture.lkm;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.accenture.lkm.service.CustomHystrixStateNotifier;
import com.accenture.lkm.service.MyService;
import com.netflix.hystrix.strategy.HystrixPlugins;

@SpringBootApplication
@EnableCircuitBreaker
public class Application {

    public static void main(String[] args) throws Exception {
       HystrixPlugins.getInstance().registerEventNotifier(new CustomHystrixStateNotifier());
       ConfigurableApplicationContext actx= SpringApplication.run(Application.class, args);
       MyService myService = actx.getBean(MyService.class);
       
       testMethod2(myService);
       actx.close();
    }
    
   
    // 5 request are sent, each request after 1 second interval. 
    // Last 2 calls are done to check the sleepWindowInMilliseconds.
    // Out of 5 requests sent, 3 are failing. 
    // This meets up the configured, volume,time and error thresholds.
    // Hence due to configured properties circuit will go to full open state
    // within 6 seconds for just 5 calls, out which 60% are failing
    // i.e. just for 3 failed calls with in 6 seconds circuit will trip open.
    // Un comment line number 55 after  System.out.println("Call 3");.
    // Observe that circuit never trips open as number of error calls are reduced
    // and thresholds are not met.
    public static void testMethod2(MyService myService) throws Exception{
    	 System.out.println("\n\n-- Testing Error: compute1() 5 times --");
         int num = 0;
         System.out.println("Call 1");
         myService.compute(num);
         getCircuitHealthStatus();
         TimeUnit.MILLISECONDS.sleep(1000);
         System.out.println("\n\n");
         
         System.out.println("Call 2");
         myService.compute(num);
         getCircuitHealthStatus();
         TimeUnit.MILLISECONDS.sleep(1000);
         System.out.println("\n\n");
         
         System.out.println("Call 3");        
         //num=2;
         myService.compute(num);
         getCircuitHealthStatus();
         TimeUnit.MILLISECONDS.sleep(1000);
         System.out.println("\n\n");
         
         System.out.println("Call 4");
         num=2;
         myService.compute(num);
         getCircuitHealthStatus();
         TimeUnit.MILLISECONDS.sleep(1000);
         System.out.println("\n\n");
         
         System.out.println("Call 5");
         num=2;
         myService.compute(num);
         getCircuitHealthStatus();
         TimeUnit.MILLISECONDS.sleep(1000);
         System.out.println("\n\n");
         
         System.out.println("Call6: As circuit is open, so sending this "
         		+ "request will print the circuit status to be short circuited");
         num=2;
         myService.compute(num);
         System.out.println("\n\n");
         
         
         TimeUnit.SECONDS.sleep(2);
         System.out.println("\n\n-- Normal final call After 2 seconds, "
         		+ "for this call fall back will be executed as  sleepWindowInMilliseconds is 3 seconds--");
         getCircuitHealthStatus();
         myService.compute(2);
         
         
         TimeUnit.SECONDS.sleep(2);
         System.out.println("\n\n-- Normal final call After 4 seconds, "
        		 + "for this call fall back will not be executed as  sleepWindowInMilliseconds is 3 seconds--");
         myService.compute(2);
         getCircuitHealthStatus();
    }
    
  //Following method is used to print the circuit status on console
    static public void getCircuitHealthStatus(){
    	RestTemplate restTemplate= new RestTemplate();
    	System.out.println("Circuit Status:"+restTemplate.getForObject("http://localhost:7093/health", Object.class));
    }
    
    
}
//https://www.logicbig.com/tutorials/spring-framework/spring-cloud/spring-circuit-breaker-hystrix-basics.html