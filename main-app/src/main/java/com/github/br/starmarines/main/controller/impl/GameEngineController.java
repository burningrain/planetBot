package com.github.br.starmarines.main.controller.impl;

import java.util.List;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.logic.GameEngineLogic;
import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.map.service.api.MapService;

public class GameEngineController implements IController<GameEngineController> {
	
	private GameEngineLogic gameEngineLogic;
	private MapService mapService;
	
	
	public void start(Galaxy galaxy) {
		gameEngineLogic.startGame(galaxy);		
	}

	public void stop() {
		gameEngineLogic.stopGame();		
	}	
	
	public List<String> getAllTitles(){
		return mapService.getAllTitles();		
	}
	
	public Galaxy getMap(String title){
		return mapService.getMap(title);
	}
	

	@Override
	public void setLogic(Logic logic) {
		gameEngineLogic = logic.getGameEngineLogic();	
		mapService = logic.getMapService();
	}

	@Override
	public void setWidget(IWidget<?, GameEngineController> widget) {
		widget.setController(this);		
	}

}
