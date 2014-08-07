package com.epam.starwors.logic.strategies.bigbases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.epam.starwors.bot.Logic;
import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;
import com.epam.starwors.galaxy.PlanetType;
import com.epam.starwors.logic.strategies.IStrategy;
import com.epam.starwors.logic.strategies.tasks.CPlanetTask;
import com.epam.starwors.logic.strategies.tasks.DPlanetTask;
import com.epam.starwors.logic.strategies.tasks.ITask;

import static com.epam.starwors.logic.utils.PlanetUtils.*;

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
