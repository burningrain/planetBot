package com.br.model.logic;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.model.services.remote.IStrategyService;

@Component
@Instantiate
@Provides
public class StrategyLogic {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void validate(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " start");
	}
	
	@Invalidate
	private void invalidate(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " stop");
	}
	
	@Requires
	private IStrategyService strategyService;
	
//	public void step(Collection<Planet> galaxy){
//		strategyService.step(galaxy);
//	}
	
	public void setCurrentStrategy(String title){
		strategyService.setCurrentStrategy(title);
	}

}
