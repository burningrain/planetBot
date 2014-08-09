package main.starwors.logic;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import main.starwors.galaxy.Planet;
import main.starwors.galaxy.PlanetType;

public final class Game {

    public static int STEP = 0;

	private static Type currentType;
	private static Set<String> players = new TreeSet<String>();	
	private static int bigBaseCount;	

	public static enum Type {
		BASE_IN_CENTER, BIG_BASES
	};

	public static void init(Collection<Planet> galaxy) {
		if (currentType == null) {
			initCurrentType(galaxy);
		}
	}

	private static void initCurrentType(Collection<Planet> galaxy) {
		if (currentType == null || players.size() == 0) {			
			for (Planet planet : galaxy) {
				players.add(planet.getOwner());
				
				if (PlanetType.TYPE_D.equals(planet.getType())) {
					bigBaseCount++;
				}
			}

			players.remove("");
			
			if(bigBaseCount != 0){
				currentType = (bigBaseCount >= 1) ? Type.BIG_BASES : Type.BASE_IN_CENTER;				
			}
			
		}
	}

	public static Set<String> getPlayers() {
		return players;
	}
	
	public static Type getCurrentType() {
		return currentType;
	}
	
	public static int getBigBaseCount() {
		return bigBaseCount;
	}

}
