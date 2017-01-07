package com.github.br.starmarines.gameui.impl;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.ui.api.IToolBar;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

@Provides(specifications = ToolbarController.class)
@Instantiate
@Component(publicFactory = false)
public class ToolbarController extends
		AbstractOrderController<ToolBar, Node, IToolBar> implements
		IUiOrderComponent<ToolBar> {

	@Validate
	public void validate() {
		ToolBar toolbar = new ToolBar();
		init(toolbar, toolbar.getItems());
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void set(IToolBar uiComponentImpl) {
		bind(uiComponentImpl);
	}

	@Unbind
	public void unset(IToolBar uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected String generateUID(IToolBar uiComponentImpl) {		
		return uiComponentImpl.toString();
	}

}
