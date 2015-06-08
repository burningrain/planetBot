package com.br.model.objects.remote.impl;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.game.api.IStrategy;
import com.br.game.api.galaxy.Move;
import com.br.game.api.galaxy.Planet;
import com.br.model.objects.IModelListener;
import com.br.model.objects.remote.IStrategies;

@Component
@Instantiate
@Provides
public class StrategiesImpl implements IStrategies {
	
	
	private IStrategy currentStrategy = null;	
	
	
	@Requires(optional = true)
	private LogService logService;
	
	private Map<String, IStrategy> strategies = new LinkedHashMap<>();	
	
	private List<IModelListener<IStrategies>> listeners = new LinkedList<>();
	
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	
	
	
	@Bind(aggregate = true, optional = true)
	private void bindStrategy(IStrategy strategy) {
		logService.log(LogService.LOG_INFO, "add strategy - " + strategy.getTitle());		
		strategies.put(strategy.getTitle(), strategy);			
		updateListeners();		
	}

	@Unbind
	private void unbindStrategy(IStrategy strategy) {
		logService.log(LogService.LOG_INFO, "remove strategy - " + strategy.getTitle());
		if(currentStrategy.getTitle().equals(strategy.getTitle())){
			currentStrategy = null;
		}
		strategies.remove(strategy.getTitle());
		updateListeners();	
	}
	
	
	@Override
	public Set<String> getStrategies() {
		return strategies.keySet();
	}
	
	
	
	public void setCurrentStrategy(String title){
		currentStrategy = strategies.get(title);
	}
	
	
	public Collection<Move> step(Collection<Planet> galaxy) {
		return currentStrategy.step(galaxy);
		
//		Attack attack = new Attack(new AttackCriteria() {
//			@Override
//			public int giveUnits(Planet from, Planet to) {
//				int units = from.getUnits();
//				if(PlanetType.TYPE_D.equals(from.getType())){
//					if(!PlanetUtils.isMyPlanet(to)){
//						return Double.valueOf(Math.ceil(units)).intValue();
//					} else{
//						return Double.valueOf(Math.ceil(units)/5).intValue();
//					}
//
//				} else if(PlanetType.TYPE_C.equals(from.getType())){
//					if(!PlanetUtils.isMyPlanet(to)){
//						return Double.valueOf(Math.ceil(units)).intValue();
//					} else{
//						return Double.valueOf(Math.ceil(units)/4).intValue();
//					}
//
//				} else{
//					if(!PlanetUtils.isMyPlanet(to)){
//						return Double.valueOf(Math.ceil(units)/1.3).intValue();
//					} else{
//						return Double.valueOf(Math.ceil(units)/5).intValue();
//					}
//
//				}
//			}
//		});
//
//
//
//		try{
//			List<Move> moves = attack.attack(getTargetPlanet(PlanetCloner.clonePlanets(galaxy)));
//			return moves;
//
//		} catch (Exception e){
//			return new LinkedList<Move>();
//		}
	}

//	private Planet getTargetPlanet(Collection<Planet> galaxy) {
//		List<Planet> myPlanets = new LinkedList<Planet>();
//		for (Planet planet : galaxy) {
//			if (PlanetUtils.isMyPlanet(planet)) {
//				myPlanets.add(planet);
//			}
//		}
//
//		
//		for (Planet myPlanet : myPlanets) {
//			for (Planet neighbour : myPlanet.getNeighbours()) {
//				if (!PlanetUtils.isMyPlanet(neighbour)) {
//					return neighbour;
//				}
//			}
//		}
//		return null;
//	}	
	
	
	@Override
	public void addListener(IModelListener<IStrategies> listener) {		
		listeners.add(listener);		
	}


	
	@Override
	public void removeListener(IModelListener<IStrategies> listener) {
		listeners.remove(listener);
		
	}

	
	@Override
	public void updateListeners() {
		for(IModelListener<IStrategies> listener : listeners){
			listener.update(this);
		}		
	}

}
