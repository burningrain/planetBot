package com.github.br.starmarines.main.view.model.objects.impl;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.model.objects.IModelListener;
import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.view.model.objects.AbstractVM;
import com.github.br.starmarines.main.view.model.objects.IColorVM;
import com.github.br.starmarines.main.view.widgets.chart.ChartWidget;

public class ColorVM extends AbstractVM<IColorVM> implements IColorVM {	
	
	private Map<String, Color> playersColors = new ConcurrentHashMap<String, Color>();
	
	@Override
	public Map<String, Color> getPlayersColors() {
		return Collections.unmodifiableMap(playersColors);
	}
	
	@Override
	public void setModelListeners(IModel model) {
		model.getGameInfo().addListener(gameInfoListener);		
	}	
	
	public void setPlayerColor(String player, Color color){
		playersColors.put(player, color);	
	}

	private IModelListener<IGameInfo> gameInfoListener = new IModelListener<IGameInfo>() {

		@Override
		public void update(IGameInfo model) {			
			if(playersColors.isEmpty()){
				Set<String> players = model.getPlayers();
				fillPlayersColor(players);
				
				model.removeListener(gameInfoListener); // FIXME 
			} else{
				playersColors.clear();
			}				
			
			updateListeners();			
		}
	};
	
	private void fillPlayersColor(Set<String> players){
		final Random random = new Random();
        for(String player : players){
            playersColors.put(player, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

	
	

}
