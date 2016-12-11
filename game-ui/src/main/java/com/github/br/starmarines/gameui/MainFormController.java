package com.github.br.starmarines.gameui;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javafx.application.Platform;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.ToolBar;

import com.github.br.starmarines.ui.api.IMenu;

public class MainFormController {

	private MenuBar menubar;

	private ToolBar toolbar;

	private ConcurrentMap<String, IMenu> menus = new ConcurrentHashMap<String, IMenu>();

	private void addMenu(IMenu menu) {
		if (menubar != null) {
			if (menus.containsKey(menu.toString()))
				throw new RuntimeException(String.format(
						"Элемент меню '%s' уже существует",
						menu.toString()));
			menus.put(menu.toString(), menu);
			Platform.runLater(() -> {
				menu.getNode().getProperties()
						.put("menuUID", menu.toString());
				menubar.getMenus().add(menu.getNode());
			});
		}
	}

	private void deleteMenu(IMenu menu) {
		if (menubar != null) {
			menus.remove(menu.toString());
			Platform.runLater(() -> {
				Optional<Menu> menuElement = menubar
						.getMenus()
						.stream()
						.filter(m -> menu.toString().equals(
								m.getProperties().get("menuUID")))
								.findAny();
				if (menuElement.isPresent()) {
					menubar.getMenus().remove(menuElement.get());
				} else {
					throw new RuntimeException(String.format(
							"Элемент меню '%s' не найден!",
							menu.toString()));
				}
			});
		}
	}

}
