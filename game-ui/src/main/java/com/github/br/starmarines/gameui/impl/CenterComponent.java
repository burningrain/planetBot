package com.github.br.starmarines.gameui.impl;


import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.ui.api.ICenterPane;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.osgi.service.component.annotations.*;

@Component(service = CenterComponent.class)
public class CenterComponent extends AbstractOrderComponent<Pane, Node, ICenterPane> implements IUiOrderComponent<Pane> {

	@Activate
	public void validate() {
		StackPane pane = new StackPane();
		init(pane, pane.getChildren());
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, unbind = "unset")
	public void set(ICenterPane uiComponentImpl) {
		bind(uiComponentImpl);
	}

	public void unset(ICenterPane uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected String getConstUID(ICenterPane uiComponentImpl) {
		return uiComponentImpl.toString();
	}

}
