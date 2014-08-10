package starwors.model.lx.bot;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.logic.MainAnalyzer;

import java.util.Collection;

public class Logic {
	
    public static String botName;

    public static Planet myBasePlanet;
    public static Planet enemyBasePlanet;
    public static final String FREE_PLANET = "";    
    
    
    private MainAnalyzer analyzer = new MainAnalyzer();  
    
	
	public Logic(String botName) {
		this.botName = botName;
	}

	
	public Collection<Move> step(Collection<Planet> galaxy) {
		
//		for (Planet planet : galaxy) {
//			System.out.print(planet);
//		}
		Collection<Move> moves = analyzer.step(galaxy);

		
//		for (Move move : moves) {
//			System.out.println(move);
//		}

		return moves;
	}

}
