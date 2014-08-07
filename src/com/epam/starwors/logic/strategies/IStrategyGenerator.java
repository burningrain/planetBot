package com.epam.starwors.logic.strategies;

public interface IStrategyGenerator {
	
	void updateStrategy(IStrategy strategy);
	
	IStrategy generate();

}
