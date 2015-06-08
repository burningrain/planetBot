package com.br.view.widgets;

import com.br.controller.IController;
import com.br.view.wm.AbstractWidgetModel;

public interface IWidget<WM extends AbstractWidgetModel, C extends IController> {
	
	void setModel(WM wm);
	WM getModel();
	void setController(C controller);
	
	void update(); 

}
