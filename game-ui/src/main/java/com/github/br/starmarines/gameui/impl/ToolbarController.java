package com.github.br.starmarines.gameui.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.ui.api.IToolBar;

@Provides(specifications = ToolbarController.class)
@Instantiate
@Component(publicFactory = false)
public class ToolbarController extends AbstractOrderController<ToolBar, IToolBar> {
	
	private ToolBar toolbar = new ToolBar();

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public ToolBar getNode() {
		return toolbar;
	}
	
	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void a(IToolBar uiComponentImpl){
		bind(uiComponentImpl);
	}
	
	@Unbind
	public void b(IToolBar uiComponentImpl){
		unbind(uiComponentImpl);
	}
	
	
    // TODO не дублировать, а перенести это говно в абстрактный контроллер. 
	// Тулбар и подобное создавать рефлексией, а тут передавать просто из наследника ObservableList
	@Override
	protected void addToUI(Collection<IToolBar> allUiComponents) {
		ArrayList<IToolBar> list = new ArrayList<>(allUiComponents);
		Collections.sort(list, (a1, a2) -> {
			return a1.getOrder() - a2.getOrder();
		});		
		List<Node> buttons = list.stream().map(a -> {
			Node btn = a.getNode();
			btn.getProperties().put("toolbarUID", a.toString());
			return btn;
		}).collect(Collectors.toList());		
		toolbar.getItems().addAll(buttons);	
	}

	@Override
	protected void deleteFromUI(IToolBar uiComponentImpl) {
		Optional<Node> btn = toolbar
				.getItems()
				.stream()
				.filter(m -> uiComponentImpl.toString().equals(
						m.getProperties().get("toolbarUID"))).findAny();
		if (btn.isPresent()) {
			toolbar.getItems().remove(btn.get());
		} else {
			throw new RuntimeException(String.format(
					"Элемент тулбара '%s' не найден!", uiComponentImpl.toString()));
		}
		
	}

}
