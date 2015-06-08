package com.br.model.logic;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

@Component
@Instantiate
@Provides
public class Logic {
	
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
	
	@Requires
	private OnlineGameLogic onlineGameLogic;
	
	@Requires
	private ReplayLogic replayLogic;
	
	@Requires
	private StrategyLogic strategyLogic;
	

	public OnlineGameLogic getOnlineGameLogic() {
		return onlineGameLogic;
	}

	public ReplayLogic getReplayLogic() {
		return replayLogic;
	}	
	
	public StrategyLogic getStrategyLogic() {
		return strategyLogic;
	}
	

}
