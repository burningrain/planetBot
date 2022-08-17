package com.github.br.starmarines.main.view.model.objects;

import java.awt.Color;
import java.util.Map;

public interface IColorVM extends IObjectVM<IColorVM> {

	public Map<Short, Color> getPlayersColors();

}