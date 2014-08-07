package com.epam.starwors.logic;


import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;
import com.epam.starwors.logic.strategies.IStrategy;
import com.epam.starwors.logic.strategies.IStrategyGenerator;
import com.epam.starwors.logic.strategies.StrategyGeneratorFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class MainAnalyzer {
	
	private final StrategyGeneratorFactory generatorFactory = new StrategyGeneratorFactory();
	

    

    public List<Move> step(Collection<Planet> galaxy) {
        long begin = System.currentTimeMillis();
        try{
            Game.init(galaxy);
            if(Game.getCurrentType() != null){
                Game.STEP++;
                IStrategyGenerator generator = generatorFactory.getGenerator(Game.getCurrentType());
                IStrategy strategy = generator.generate();

                return strategy.execute(galaxy);
            }

            return new LinkedList<Move>();

        } finally{
            long end = System.currentTimeMillis();
            System.out.println("step " + Game.STEP + ": " + (end - begin)/1000 + " sec");
        }

    }
}
