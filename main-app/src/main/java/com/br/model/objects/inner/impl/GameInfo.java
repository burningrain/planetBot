package com.br.model.objects.inner.impl;


import java.util.*;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.game.api.galaxy.Planet;
import com.br.game.api.galaxy.PlanetType;
import com.br.model.objects.IModelListener;
import com.br.model.objects.inner.IGameInfo;
import com.br.starwors.lx.logic.utils.PlanetUtils;

// TODO а так ли нужен синглетон?
@Component
@Instantiate
@Provides
public class GameInfo implements IGameInfo {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	
	public static enum GameType {
        BIG_BASE_IN_CENTER, SMALL_BASE_IN_CENTER, UNKNOWN
    }
    
    private GameType currentGameType = null;
    private Set<String> players = new TreeSet<String>();
    private Map<String, Integer> unitsMap = new HashMap<String, Integer>(); 
    private int stepsCount = 0;
    private Collection<Planet> galaxy;   	
	
    private List<IModelListener<IGameInfo>> listeners = new LinkedList<IModelListener<IGameInfo>>();
    
    public void clear(){
    	currentGameType = GameType.UNKNOWN;
        this.stepsCount = 0;
        players.clear();
        unitsMap.clear();
        
        this.updateListeners();
    }

    public void updateGalaxy(final Collection<Planet> galaxy) {
    	this.galaxy = galaxy;
    	
        if (currentGameType == null) {
            initCurrentType(galaxy);

            for(String player : players){
                unitsMap.put(player, 0);
            }
        }else{
            this.stepsCount++;
            clearUnitsMap();
            updateUnitsMap(galaxy);
        }
        this.updateListeners();
    }

 
    @Override
	public Set<String> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

   
    @Override
	public Map<String, Integer> getUnitsMap() {
        return Collections.unmodifiableMap(unitsMap);
    }

    
    @Override
	public GameType getCurrentGameType() {
        return currentGameType;
    }

    
    @Override
	public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }
    
  
    @Override
	public Collection<Planet> getGalaxy() {
		return Collections.unmodifiableCollection(galaxy);
	}

	private void initCurrentType(Collection<Planet> galaxy) {
	    int bigBaseCount = 0;
	    int smallBaseCount = 0;
	    if (players.size() == 0) {
	        for (Planet planet : galaxy) {
	            players.add(planet.getOwner());
	
	            if (PlanetType.TYPE_D.equals(planet.getType())) {
	                bigBaseCount++;
	            } else if(PlanetType.TYPE_A.equals(planet.getType())){
	                smallBaseCount++;
	            }
	        }
	
	        players.remove("");
	
	        if(bigBaseCount == 1){
	            currentGameType = GameType.BIG_BASE_IN_CENTER;
	        } else if(smallBaseCount == 1){
	            currentGameType = GameType.SMALL_BASE_IN_CENTER;
	        } else{
	            currentGameType = GameType.UNKNOWN;
	        }
	    }
	}

	private void updateUnitsMap(Collection<Planet> galaxy){
	    for(Planet planet : galaxy){
	        if(!PlanetUtils.isFreePlanet(planet)){
	            int units = unitsMap.get(planet.getOwner());
	            unitsMap.put(planet.getOwner(), units + planet.getUnits());
	        }
	    }
	}

	private void clearUnitsMap(){
	    for(Map.Entry<String, Integer> entry : unitsMap.entrySet()){
	        unitsMap.put(entry.getKey(), 0);
	    }
	}
	

	@Override
	public void addListener(IModelListener<IGameInfo> listener) {
		listeners.add(listener);
		
	}

	@Override
	public void removeListener(IModelListener<IGameInfo> listener) {
		listeners.remove(listener);
		
	}

	@Override
	public void updateListeners() {
		for(IModelListener<IGameInfo> listener : listeners){
			listener.update(this);
		}		
	}

}
