package com.github.br.starmarines.main.controller.impl;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.logic.GameEngineLogic;
import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.view.widgets.IWidget;

public class GameEngineController implements IController<GameEngineController> {
	
	private GameEngineLogic gameEngineLogic;
	
	
	public void start() {
		gameEngineLogic.startGame();		
	}

	public void stop() {
		gameEngineLogic.stopGame();		
	}	
	

	@Override
	public void setLogic(Logic logic) {
		gameEngineLogic = logic.getGameEngineLogic();		
	}

	@Override
	public void setWidget(IWidget<?, GameEngineController> widget) {
		widget.setController(this);		
	}

}
