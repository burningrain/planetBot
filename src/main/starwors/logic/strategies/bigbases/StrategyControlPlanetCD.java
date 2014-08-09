package main.starwors.logic.strategies.bigbases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import main.starwors.galaxy.Move;
import main.starwors.galaxy.Planet;
import main.starwors.logic.strategies.IStrategy;
import main.starwors.logic.strategies.tasks.CPlanetTask;
import main.starwors.logic.strategies.tasks.DPlanetTask;
import main.starwors.logic.strategies.tasks.ITask;

public class StrategyControlPlanetCD implements IStrategy {
	
	private List<ITask> tasks;
	
	public StrategyControlPlanetCD(){
		tasks = new ArrayList<ITask>();
		tasks.add(new DPlanetTask());
        tasks.add(new CPlanetTask());
	}
	

	@Override
	public List<Move> execute(Collection<Planet> galaxy) {
		List<Move> moves = new LinkedList<Move>();

        for (Planet planet : galaxy) {
        	for(ITask task : tasks){
        		if(task.canUse(planet)){
        			moves.addAll(task.execute(planet));        			
        		}
        	}
        }
       return moves;
	}

}
