package com.br.model.objects;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.model.objects.inner.IGameInfo;
import com.br.model.objects.inner.IStepInfo;
import com.br.model.objects.remote.IStrategies;

@Component
@Instantiate
@Provides
public class Model implements IModel {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " stop");
	}
	
	@Requires(policy = BindingPolicy.STATIC)
	private IGameInfo gameInfo;
	
	@Requires(policy = BindingPolicy.STATIC)
	private IStepInfo step;
	
	@Requires(policy = BindingPolicy.STATIC)
	private IStrategies strategies;
	

	
	public IGameInfo getGameInfo() {
		return gameInfo;
	}

	
	public IStepInfo getStep() {
		return step;
	}
	
	
	public IStrategies getStrategies() {
		return strategies;
	}

}
