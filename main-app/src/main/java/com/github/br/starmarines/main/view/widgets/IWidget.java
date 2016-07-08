package com.github.br.starmarines.main.view.widgets;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.view.wm.AbstractWidgetModel;

public interface IWidget<WM extends AbstractWidgetModel, C extends IController> {
	
	void setModel(WM wm);
	WM getModel();
	void setController(C controller);
	
	void update(); 

}
