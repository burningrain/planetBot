package com.epam.starwors.logic.strategies;

import java.util.HashMap;
import java.util.Map;

import com.epam.starwors.logic.Game;

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
