package main.starwors.logic.strategies;

public interface IStrategyGenerator {
	
	void updateStrategy(IStrategy strategy);
	
	IStrategy generate();

}
