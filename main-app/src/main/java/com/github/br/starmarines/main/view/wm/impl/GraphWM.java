package com.github.br.starmarines.main.view.wm.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.model.objects.IModelListener;
import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.model.objects.inner.impl.GameInfo.GameType;
import com.github.br.starmarines.main.model.services.inner.beans.Response;
import com.github.br.starmarines.main.view.model.CommonVM;
import com.github.br.starmarines.main.view.model.objects.IColorVM;
import com.github.br.starmarines.main.view.model.objects.IObjectVMListener;
import com.github.br.starmarines.main.view.wm.AbstractWidgetModel;

public class GraphWM extends AbstractWidgetModel {
	
	// IGameInfo
	private Set<String> players;
	private Map<String, Integer> units;
	private int stepsCount;
	private Collection<Planet> galaxy;
	private GameType gameType;
	
	// IStepInfo
	private Response currentStep;
	private Collection<Action> currentActions;
	
	// from VM
	private Map<Short, Color> playersColors;

	// self
	private Collection<Action> lastActions;
	private Response lastStep;
	
	
	
	
	private IModelListener<IGameInfo> gameInfoListener = new IModelListener<IGameInfo>() {
		
		@Override
		public void update(IGameInfo model) {
			players = model.getPlayers();		
			units = model.getUnitsMap();
			stepsCount = model.getStepsCount();
			galaxy = model.getGalaxy();
			gameType = model.getCurrentGameType();					
			
			updateWidget();			
		}
	};
	
	private IModelListener<IStepInfo> stepInfoListener = new IModelListener<IStepInfo>() {
		
		@Override
		public void update(IStepInfo model) {
			lastActions = currentActions;
			lastStep = currentStep;
			currentStep = model.getCurrentStep();		
			currentActions = model.getCurrentActions();
			
			updateWidget();			
		}
	};
	
	private IObjectVMListener<IColorVM> colorVMListener = new IObjectVMListener<IColorVM>() {
		
		@Override
		public void update(IColorVM vm) {
			playersColors = vm.getPlayersColors();			
		}
	};
	
	@Override
	public void setModelListeners(IModel model) {
		model.getGameInfo().addListener(gameInfoListener);
		model.getStep().addListener(stepInfoListener);
	}	
	
	@Override
	public void setViewModelListeners(CommonVM model) {
		model.getColorVM().addListener(colorVMListener);			
	}
	
	
	

	public Collection<Action> getLastActions() {
		return lastActions;
	}

	public Set<String> getPlayers() {
		return players;
	}

	public Map<String, Integer> getUnits() {
		return units;
	}

	public int getStepsCount() {
		return stepsCount;
	}

	public Collection<Planet> getGalaxy() {
		return galaxy;
	}

	public GameType getGameType() {
		return gameType;
	}

	public Response getCurrentStep() {
		return currentStep;
	}

	public Response getLastStep() {
		return lastStep;
	}

	public Collection<Action> getCurrentActions() {
		return currentActions;
	}

	public Map<Short, Color> getPlayersColors() {
		return playersColors;
	}	
	
	

}
