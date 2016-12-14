package com.github.br.starmarines.gameui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

public abstract class AbstractOrderController<FXCOMPONENT, E, IUI extends IUiOrderComponent<?>>
		implements IUiOrderComponent<FXCOMPONENT> {

	private ConcurrentMap<String, IUI> iuiMap = new ConcurrentHashMap<>();
	private FXCOMPONENT component;
	private ObservableList<E> children;

	public void init(FXCOMPONENT component, ObservableList<E> children) {
		this.component = component;
		this.children = children;
	}

	public void bind(IUI uiComponentImpl) {
		if (getNode() != null) {
			if (iuiMap.containsKey(uiComponentImpl.toString()))
				throw new RuntimeException(String.format(
						"Элемент %s '%s' уже существует", uiComponentImpl
								.getClass().getSimpleName(), uiComponentImpl
								.toString()));
			iuiMap.put(uiComponentImpl.toString(), uiComponentImpl);
			Platform.runLater(() -> {
				addToUI(iuiMap.values(), children);
			});
		}
	}

	protected void addToUI(Collection<IUI> allUiComponents,
			ObservableList<E> children) {
		ArrayList<IUI> list = new ArrayList<>(allUiComponents);
		Collections.sort(list, (a1, a2) -> {
			return a1.getOrder() - a2.getOrder();
		});
		children.clear();
		List<E> elements = list.stream().map(a -> {
			E element = (E) a.getNode();
			putUID(element, a.toString());
			return element;
		}).collect(Collectors.toList());
		children.addAll(elements);
	}

	public void unbind(IUI uiComponentImpl) {
		IUI node = iuiMap.get(uiComponentImpl.toString());
		if (node != null) {
			iuiMap.remove(uiComponentImpl.toString());
			Platform.runLater(() -> {
				deleteFromUI(uiComponentImpl, children);
			});
		} else {
			throw new RuntimeException(String.format(
					"Компонент интерфейса '%s' не был зарегистрирован!",
					uiComponentImpl.getClass().getName()));
		}
	}

	protected void deleteFromUI(IUI uiComponentImpl, ObservableList<E> children) {
		Optional<E> menuElement = children.stream()
				.filter(m -> uiComponentImpl.toString().equals(getUID(m)))
				.findAny();
		if (menuElement.isPresent()) {
			children.remove(menuElement.get());
		} else {
			throw new RuntimeException(String.format(
					"Элемент интерфейса '%s' не найден!",
					uiComponentImpl.toString()));
		}

	}

	protected abstract void putUID(E e, String uid);
	protected abstract String getUID(E e);

}
