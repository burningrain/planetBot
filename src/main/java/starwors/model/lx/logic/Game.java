package starwors.model.lx.logic;

import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.utils.PlanetUtils;

import java.util.*;

public final class Game {

    public static int STEP = 0;

	private static Type currentType;
	private static Set<String> players = new TreeSet<String>();	
	private static int bigBaseCount;
    private static Map<String, Integer> unitsMap = new HashMap<String,  Integer>();

	public static enum Type {
		BASE_IN_CENTER, BIG_BASES
	};

	public static void init(Collection<Planet> galaxy) {
		if (currentType == null) {
			initCurrentType(galaxy);

            for(String player : players){
                unitsMap.put(player, 0);
            }
		}else{
            clearUnitsMap();
            updateUnitsMap(galaxy);
        }

	}

    public static Map<String, Integer> getUnitsMap() {
        return unitsMap;
    }

    private static void clearUnitsMap(){
        for(Map.Entry<String, Integer> entry : unitsMap.entrySet()){
            unitsMap.put(entry.getKey(), 0);
        }
    }


    private static void updateUnitsMap(Collection<Planet> galaxy){
        for(Planet planet : galaxy){
            if(!PlanetUtils.isFreePlanet(planet)){
                int units = unitsMap.get(planet.getOwner());
                unitsMap.put(planet.getOwner(), units + planet.getUnits());
            }
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
				currentType = (bigBaseCount > 1) ? Type.BIG_BASES : Type.BASE_IN_CENTER;
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
