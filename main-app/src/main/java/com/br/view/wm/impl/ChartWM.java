package com.br.view.wm.impl;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.model.objects.IModel;
import com.br.model.objects.IModelListener;
import com.br.model.objects.inner.IGameInfo;
import com.br.view.model.CommonVM;
import com.br.view.model.objects.IColorVM;
import com.br.view.model.objects.IObjectVMListener;
import com.br.view.widgets.chart.ChartWidget;
import com.br.view.wm.AbstractWidgetModel;

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
