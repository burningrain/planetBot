package com.github.br.starmarines.gameui;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Unbind;

import javafx.application.Platform;

import com.github.br.starmarines.ui.api.IUiOrderComponent;

public abstract class AbstractOrderController<FXCOMPONENT, IUI extends IUiOrderComponent>
		implements IUiOrderComponent<FXCOMPONENT> {

	private ConcurrentMap<String, IUI> iuiMap = new ConcurrentHashMap<>();

	public void bind(IUI uiComponentImpl) {
		if (getNode() != null) {
			if (iuiMap.containsKey(uiComponentImpl.toString()))
				throw new RuntimeException(String.format(
						"Элемент %s '%s' уже существует", uiComponentImpl
								.getClass().getSimpleName(), uiComponentImpl
								.toString()));
			iuiMap.put(uiComponentImpl.toString(), uiComponentImpl);
			Platform.runLater(() -> {
				addToUI(iuiMap.values());
			});
		}
	}

	protected abstract void addToUI(Collection<IUI> allUiComponents);

	public void unbind(IUI uiComponentImpl) {
		IUI node = iuiMap.get(uiComponentImpl.toString());
		if (node != null) {
			iuiMap.remove(uiComponentImpl.toString());
			Platform.runLater(() -> {
				deleteFromUI(uiComponentImpl);
			});
		} else {
			throw new RuntimeException(String.format("Компонент интерфейса '%s' не был зарегистрирован!",
					uiComponentImpl.getClass().getName()));
		}
	}

	protected abstract void deleteFromUI(IUI uiComponentImpl);

}
