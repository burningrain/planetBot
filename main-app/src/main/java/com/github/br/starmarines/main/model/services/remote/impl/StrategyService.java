package com.github.br.starmarines.main.model.services.remote.impl;

import java.util.Collection;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.main.model.objects.remote.IStrategies;
import com.github.br.starmarines.main.model.services.remote.IStrategyService;


@Component
@Instantiate
@Provides
public class StrategyService implements IStrategyService {
	
	@Requires(policy = BindingPolicy.STATIC)
	private IStrategies strategies;
	
	@Requires(optional = true)
	private LogService logService;
	
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	

	@Override
	public Collection<Move> step(Collection<Planet> galaxy) {		
		return ((IStrategyService) strategies).step(galaxy);
	}


	@Override
	public void setCurrentStrategy(String title) {
		strategies.setCurrentStrategy(title);		
	}

}
