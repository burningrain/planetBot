package com.br.controller;

import com.br.model.logic.Logic;
import com.br.view.widgets.IWidget;

public interface IController<C extends IController> {
	
	void setLogic(Logic logic);	
	void setWidget(IWidget<?, C> widget);

}
