package com.github.br.starmarines.main.controller;

import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.view.widgets.IWidget;

public interface IController<C extends IController> {
	
	void setLogic(Logic logic);	
	void setWidget(IWidget<?, C> widget);

}
