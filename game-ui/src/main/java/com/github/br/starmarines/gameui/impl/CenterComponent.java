package com.github.br.starmarines.gameui.impl;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.ui.api.ICenterPane;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

@Provides(specifications = CenterComponent.class)
@Instantiate
@Component(publicFactory = false)
public class CenterComponent extends AbstractOrderComponent<Pane, Node, ICenterPane> implements IUiOrderComponent<Pane> {

	@Validate
	public void validate() {
		StackPane pane = new StackPane();
		init(pane, pane.getChildren());
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void set(ICenterPane uiComponentImpl) {
		bind(uiComponentImpl);
	}

	@Unbind
	public void unset(ICenterPane uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected String getConstUID(ICenterPane uiComponentImpl) {
		return uiComponentImpl.toString();
	}

}
