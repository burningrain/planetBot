package com.epam.starwors.bot;

import java.util.Collection;

import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;
import com.epam.starwors.logic.MainAnalyzer;

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
