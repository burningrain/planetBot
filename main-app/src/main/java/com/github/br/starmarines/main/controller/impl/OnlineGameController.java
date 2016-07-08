package com.github.br.starmarines.main.controller.impl;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.model.logic.OnlineGameLogic;
import com.github.br.starmarines.main.model.logic.StrategyLogic;
import com.github.br.starmarines.main.view.widgets.IWidget;

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
