package com.github.br.starmarines.gameui.impl;

import com.github.br.starmarines.ui.api.IUiComponent;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;

import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.ui.api.IToolBar;
import com.github.br.starmarines.ui.api.IUiOrderComponent;
import org.osgi.service.component.annotations.*;

@Component(service = ToolbarComponent.class)
public class ToolbarComponent extends AbstractOrderComponent<ToolBar, Node, IToolBar> implements
		IUiOrderComponent<ToolBar> {

	@Activate
	public void validate() {
		ToolBar toolbar = new ToolBar();
		init(toolbar, toolbar.getItems());
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Reference(unbind = "unset", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void set(IToolBar uiComponentImpl) {
		bind(uiComponentImpl);
	}

	public void unset(IToolBar uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected String getConstUID(IToolBar uiComponentImpl) {
		return uiComponentImpl.toString();
	}

}
