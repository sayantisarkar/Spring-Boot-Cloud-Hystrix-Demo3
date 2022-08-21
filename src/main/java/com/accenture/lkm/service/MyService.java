package com.accenture.lkm.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MyService {
  @HystrixCommand(fallbackMethod = "cstFallbackDoSomething",commandKey="msdKeyCompute")
  public void compute(int input) {
      System.out.println("compute: input: " + input);
      //in case of exception fallbackMethod is called
      System.out.println("compute: 10/"+input+", output: " + 10 / input);
  }
  
  public void cstFallbackDoSomething(int input,Throwable th) {
	  System.out.println("***************************************************************");
	  System.out.println("***cstFallbackDoSomething, The input number is: " + input+"***");
      System.out.println(th.getMessage());
      System.out.println("***************************************************************");
  }
  
}