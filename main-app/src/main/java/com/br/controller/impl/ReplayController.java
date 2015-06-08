package com.br.controller.impl;

import com.br.controller.IController;
import com.br.model.logic.Logic;
import com.br.model.logic.ReplayLogic;
import com.br.view.widgets.IWidget;

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
