package starwors.model.lx.logic.strategies;

import starwors.model.lx.logic.Game;

import java.util.HashMap;
import java.util.Map;

public final class StrategyGeneratorFactory {
	
	private Map<Game.Type, IStrategyGenerator> generators;
	
	public StrategyGeneratorFactory(){
		generators = new HashMap<Game.Type, IStrategyGenerator>();
		generators.put(Game.Type.BIG_BASES, new BigBasesStrategyGenerator());
		generators.put(Game.Type.BASE_IN_CENTER, new BaseInCenterStrategyGenerator());
	}	
	
	
	public IStrategyGenerator getGenerator(Game.Type type){
		return generators.get(type);
	}
	
	

}
