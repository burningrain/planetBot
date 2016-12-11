package com.github.br.starmarines.gameui.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.ui.api.IMenu;

@Provides(specifications = MenuController.class)
@Instantiate
@Component(publicFactory = false)
public class MenuController extends AbstractOrderController<MenuBar, IMenu> {

	private MenuBar menubar = new MenuBar();

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public MenuBar getNode() {
		return menubar;
	}

	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void a(IMenu uiComponentImpl) {
		bind(uiComponentImpl);
	}

	@Unbind
	public void b(IMenu uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected void addToUI(Collection<IMenu> allUiComponents) {
		ArrayList<IMenu> list = new ArrayList<>(allUiComponents);
		Collections.sort(list, (a1, a2) -> {
			return a1.getOrder() - a2.getOrder();
		});
		menubar.getMenus().clear();
		List<Menu> menus = list.stream().map(a -> {
			Menu menu = a.getNode();
			menu.getProperties().put("menuUID", a.toString());
			return menu;
		}).collect(Collectors.toList());		
		menubar.getMenus().addAll(menus);
	}

	@Override
	protected void deleteFromUI(IMenu uiComponentImpl) {
		Optional<Menu> menuElement = menubar
				.getMenus()
				.stream()
				.filter(m -> uiComponentImpl.toString().equals(
						m.getProperties().get("menuUID"))).findAny();
		if (menuElement.isPresent()) {
			menubar.getMenus().remove(menuElement.get());
		} else {
			throw new RuntimeException(String.format(
					"Элемент меню '%s' не найден!", uiComponentImpl.toString()));
		}

	}

}
