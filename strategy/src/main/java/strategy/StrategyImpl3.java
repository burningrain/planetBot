package strategy;

import java.util.*;

import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.strategy.IStrategy;



@Component(service = IStrategy.class)
public class StrategyImpl3 implements IStrategy {

	@Override
	public String getTitle() {		
		return "Unpredictable2";
	}
	
	private Planet basePlanet;
	
	private static Random random = new Random();
	
	private static int getRandom(int min, int max){
		return random.nextInt(max - min + 1) + min;
	}
	
	

	@Override
	public Collection<Move> step(Collection<Planet> galaxy) {
		return Collections.emptyList(); //TODO переделать все это
	}
	
	
	

}
