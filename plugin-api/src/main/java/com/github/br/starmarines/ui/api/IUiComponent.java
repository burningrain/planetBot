package com.github.br.starmarines.ui.api;

public interface IUiComponent<C> {
	
	/**
	 * Внимание, повторный вызов внезапно может вернуть новый объект!
	 * @return fx-component
	 */
	C getNode();
	
}
