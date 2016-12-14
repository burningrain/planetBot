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

@Provides(specifications = ToolbarController.class)
@Instantiate
@Component(publicFactory = false)
public class ToolbarController extends AbstractOrderController<ToolBar, Node, IToolBar> {
	
	private ToolBar toolbar = new ToolBar();
	
	@Validate
	public void validate(){
		init(toolbar, toolbar.getItems());
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public ToolBar getNode() {
		return toolbar;
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
	protected void putUID(Node e, String uid) {
		e.getProperties().put("toolbarUID", uid);		
	}

	@Override
	protected String getUID(Node e) {		
		return (String) e.getProperties().get("toolbarUID");
	}	

}
