package starwors.model.lx.logic.strategies;

import starwors.model.lx.logic.strategies.bigbases.StrategyControlPlanetCD;

import java.util.Map;
import java.util.TreeMap;

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
