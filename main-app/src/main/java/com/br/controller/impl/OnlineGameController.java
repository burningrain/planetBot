package com.br.controller.impl;

import com.br.controller.IController;
import com.br.model.logic.Logic;
import com.br.model.logic.OnlineGameLogic;
import com.br.view.widgets.IWidget;
import com.br.model.logic.StrategyLogic;

public class OnlineGameController implements IController<OnlineGameController> {
	
	private OnlineGameLogic onlineGameLogic;
	private StrategyLogic strategyLogic;

	@Override
	public void setLogic(Logic logic) {
		this.onlineGameLogic = logic.getOnlineGameLogic();
		this.strategyLogic = logic.getStrategyLogic();
	}

	@Override
	public void setWidget(IWidget<?, OnlineGameController> widget) {
		widget.setController(this);		
	}
	
	public void start() {
		onlineGameLogic.start();
	}
	
	public void stop() {
		onlineGameLogic.stop();
	}
	
	public void setCurrentStrategy(String title){
		strategyLogic.setCurrentStrategy(title);
	}

}
