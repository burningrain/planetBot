package starwors.model.lx.logic;


import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.logic.strategies.IStrategy;
import starwors.model.lx.logic.strategies.IStrategyGenerator;
import starwors.model.lx.logic.strategies.StrategyGeneratorFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class MainAnalyzer {

    private final StrategyGeneratorFactory generatorFactory = new StrategyGeneratorFactory();


    public List<Move> step(Collection<Planet> galaxy) {
        long begin = System.currentTimeMillis();
        try {
            Game.init(galaxy);
            if (Game.getCurrentType() != null) {
                Game.STEP++;
                IStrategyGenerator generator = generatorFactory.getGenerator(Game.getCurrentType());
                IStrategy strategy = generator.generate();

                return strategy.execute(galaxy);
            }
            return null;
        } catch (Exception e) {
            //TODO сообщить пользователю об ошибке
            return new LinkedList<Move>();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("step " + Game.STEP + ": " + (end - begin) / 1000 + " sec");
        }

    }
}
