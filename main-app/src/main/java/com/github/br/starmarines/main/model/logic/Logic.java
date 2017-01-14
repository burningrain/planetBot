package com.github.br.starmarines.main.model.logic;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.map.service.api.MapService;

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
	
	@Requires
	private GameEngineLogic gameEngineLogic;
	
	@Requires(optional=true)
	private MapService mapLogic;
	

	public OnlineGameLogic getOnlineGameLogic() {
		return onlineGameLogic;
	}

	public ReplayLogic getReplayLogic() {
		return replayLogic;
	}	
	
	public StrategyLogic getStrategyLogic() {
		return strategyLogic;
	}

	public GameEngineLogic getGameEngineLogic() {
		return gameEngineLogic;
	}	
	
	public MapService getMapService() {
		return mapLogic;
	}	

}
