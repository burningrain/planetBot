package com.github.br.starmarines.gameui.impl;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.ui.api.IPane;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

@Provides(specifications = PaneController.class)
@Instantiate
@Component(publicFactory = false)
public class PaneController extends AbstractOrderController<Pane, Node, IPane> implements IUiOrderComponent<Pane> {

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
	public void set(IPane uiComponentImpl) {
		bind(uiComponentImpl);
	}

	@Unbind
	public void unset(IPane uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected String generateUID(IPane uiComponentImpl) {
		return uiComponentImpl.toString();
	}

}
