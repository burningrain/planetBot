package starwors.model.lx.logic.strategies.bigbases;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.logic.strategies.IStrategy;
import starwors.model.lx.logic.strategies.tasks.CPlanetTask;
import starwors.model.lx.logic.strategies.tasks.DPlanetTask;
import starwors.model.lx.logic.strategies.tasks.ITask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
