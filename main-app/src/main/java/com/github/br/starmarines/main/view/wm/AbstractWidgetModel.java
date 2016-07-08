package com.github.br.starmarines.main.view.wm;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.view.model.CommonVM;
import com.github.br.starmarines.main.view.widgets.IWidget;

//TODO допилить с generics нормально
public abstract class AbstractWidgetModel {

	private IWidget<? extends AbstractWidgetModel, ? extends IController> widget;

	public abstract void setModelListeners(IModel model);

	public abstract void setViewModelListeners(CommonVM model);

	public void setWidget(IWidget<? extends AbstractWidgetModel, ? extends IController> listener) {
		widget = listener;
	}

	public void unsetWidget(IWidget<? extends AbstractWidgetModel, ? extends IController> listener) {
		widget = null;
	}

	protected void updateWidget() {
		widget.update();
	}

}
