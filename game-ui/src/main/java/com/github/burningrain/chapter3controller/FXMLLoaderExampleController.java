package com.github.burningrain.chapter3controller;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

@Provides
@Instantiate
@Component(publicFactory=false)
public class FXMLLoaderExampleController {
	
	@Requires(policy=BindingPolicy.STATIC)
	SimpleOsgiServiceImpl simpleService;

	@FXML
	private TextField address;
	@FXML
	private WebView webView;

	@FXML
	private Button loadButton;	
	
	private String name;
	
	@Validate
	private void v(){
		name = "11111";
		System.out.println("FXMLLoaderExampleController start");
	}

	@FXML
	public void actionHandler() {
		System.out.println("Contr: " + this);
		simpleService.doSomething();		
		webView.getEngine().load(address.getText());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
