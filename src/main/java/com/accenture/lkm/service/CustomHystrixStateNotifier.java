package com.accenture.lkm.service;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;

public class CustomHystrixStateNotifier extends HystrixEventNotifier {
	  @Override
	  public void markEvent(HystrixEventType eventType, HystrixCommandKey commandKey){
	      System.out.printf("HystrixStateNotifier: EventType=%s, MethodTriggeredFrom/Key=%s%n", eventType, commandKey);
	  }
	}
