package com.github.burningrain.chapter3controller;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;


@Provides
@Instantiate
@Component
public class SimpleOsgiService {	
	
	public void doSomething(){
		System.out.println("doSomething");
	}

}
