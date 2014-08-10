package starwors.model.lx.logic.strategies;

public interface IStrategyGenerator {
	
	void updateStrategy(IStrategy strategy);
	
	IStrategy generate();

}
