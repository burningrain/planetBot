package main.starwors.logic.strategies;

import java.util.Map;
import java.util.TreeMap;

import main.starwors.logic.strategies.bigbases.StrategyControlPlanetCD;

public class BigBasesStrategyGenerator implements IStrategyGenerator {
	
	public enum Type{
		ControlCD
	}
	
	private Map<Type, IStrategy> strategies = new TreeMap<Type, IStrategy>();
	
	private IStrategy currentStrategy;
	
	
	public BigBasesStrategyGenerator(){
		strategies.put(Type.ControlCD, new StrategyControlPlanetCD());
		
		currentStrategy = strategies.get(Type.ControlCD);
	}	
	
	public IStrategy generate(){
		return currentStrategy;		
	}

	@Override
	public void updateStrategy(IStrategy strategy) {
		currentStrategy = strategy;		
	}

}
