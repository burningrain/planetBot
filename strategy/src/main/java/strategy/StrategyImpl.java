package strategy;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.strategy.IStrategy;



@Component(service = IStrategy.class)
public class StrategyImpl implements IStrategy {

	@Override
	public String getTitle() {		
		return "8";
	}

	@Override
	public Collection<Move> step(Collection<Planet> galaxy) {
		// TODO Auto-generated method stub
		return null;
	}

}
