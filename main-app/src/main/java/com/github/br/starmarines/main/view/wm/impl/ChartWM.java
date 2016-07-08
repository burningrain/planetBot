package com.github.br.starmarines.main.view.wm.impl;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.model.objects.IModelListener;
import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.view.model.CommonVM;
import com.github.br.starmarines.main.view.model.objects.IColorVM;
import com.github.br.starmarines.main.view.model.objects.IObjectVMListener;
import com.github.br.starmarines.main.view.widgets.chart.ChartWidget;
import com.github.br.starmarines.main.view.wm.AbstractWidgetModel;

public class ChartWM extends AbstractWidgetModel {
	private Logger logger = LoggerFactory.getLogger(ChartWM.class); 
	
	private Set<String> players;
	private Map<String, Integer> units;
	
	private int step;	
	
	private Map<String, Color> playersColors;	
	

	private IModelListener<IGameInfo> gameInfoListener = new IModelListener<IGameInfo>() {
		
		@Override
		public void update(IGameInfo model) {
			players = model.getPlayers();		
			units = model.getUnitsMap();			
			step = model.getStepsCount();
			
			updateWidget();
		}
	};
	
	private IObjectVMListener<IColorVM> colorVMListener = new IObjectVMListener<IColorVM>() {
		
		@Override
		public void update(IColorVM vm) {			
			playersColors = vm.getPlayersColors();
			logger.debug("get players = {}", playersColors);
		}
	};
	
	@Override
	public void setModelListeners(IModel model) {
		model.getGameInfo().addListener(gameInfoListener);		
	}	
	
	@Override
	public void setViewModelListeners(CommonVM model) {		
		model.getColorVM().addListener(colorVMListener);		
	}
	
	

	public Set<String> getPlayers() {
		return players;
	}

	public Map<String, Integer> getUnits() {
		return units;
	}
	
	public Map<String, Color> getPlayersColors() {
		return playersColors;
	}
	
	public int getStep() {
		return step;
	}


}
