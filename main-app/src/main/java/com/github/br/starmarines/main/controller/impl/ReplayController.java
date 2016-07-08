package com.github.br.starmarines.main.controller.impl;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.model.logic.ReplayLogic;
import com.github.br.starmarines.main.view.widgets.IWidget;

public class ReplayController implements IController<ReplayController> {
	
	private ReplayLogic replayLogic;

	@Override
	public void setLogic(Logic logic) {
		replayLogic = logic.getReplayLogic();		
	}

	@Override
	public void setWidget(IWidget<?, ReplayController> widget) {
		widget.setController(this);			
	}
	
	
	public void start(){
		replayLogic.start();
	}

}
