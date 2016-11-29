package com.github.burningrain.chapter3controller;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;


@Provides
@Instantiate
@Component
public class SimpleOsgiServiceImpl {	
	
	public void doSomething(){
		System.out.println("doSomething");
	}
	
	@Validate
	public void check() {
		System.out.println("Check");
	}

}
