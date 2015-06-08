package com.br.view.wm;

import com.br.controller.IController;
import com.br.model.objects.IModel;
import com.br.view.model.CommonVM;
import com.br.view.widgets.IWidget;

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
