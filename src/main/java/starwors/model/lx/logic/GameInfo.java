package starwors.model.lx.logic;


import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.utils.PlanetUtils;
import java.util.*;

// TODO а так ли нужен синглетон?
public class GameInfo {

    public int step = 0;
    private GameType currentGameType;
    private Set<String> players;
    private Map<String, Integer> unitsMap;

    public static enum GameType {
        BASE_IN_CENTER, BIG_BASES, UNKNOWN
    }

    public GameInfo(){
        players = new TreeSet<String>();
        unitsMap = new HashMap<String, Integer>();
    }

    public void clear(){
        this.step = 0;
        players.clear();
        unitsMap.clear();
    }

    public void fill(final Collection<Planet> galaxy) {
        if (currentGameType == null) {
            initCurrentType(galaxy);

            for(String player : players){
                unitsMap.put(player, 0);
            }
        }else{
            this.step++;
            clearUnitsMap();
            updateUnitsMap(galaxy);
        }

    }

    private void clearUnitsMap(){
        for(Map.Entry<String, Integer> entry : unitsMap.entrySet()){
            unitsMap.put(entry.getKey(), 0);
        }
    }


    private void updateUnitsMap(Collection<Planet> galaxy){
        for(Planet planet : galaxy){
            if(!PlanetUtils.isFreePlanet(planet)){
                int units = unitsMap.get(planet.getOwner());
                unitsMap.put(planet.getOwner(), units + planet.getUnits());
            }
        }
    }


    private void initCurrentType(Collection<Planet> galaxy) {
        int bigBaseCount = 0;
        int smallBaseCount = 0;
        if (currentGameType == null || players.size() == 0) {
            for (Planet planet : galaxy) {
                players.add(planet.getOwner());

                if (PlanetType.TYPE_D.equals(planet.getType())) {
                    bigBaseCount++;
                } else if(PlanetType.TYPE_A.equals(planet.getType())){
                    smallBaseCount++;
                }
            }

            players.remove("");

            if(bigBaseCount == 1){
                currentGameType = GameType.BASE_IN_CENTER;
            } else if(smallBaseCount == 1){
                currentGameType = GameType.BIG_BASES;
            } else{
                currentGameType = GameType.UNKNOWN;
            }
        }
    }



    public Set<String> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public Map<String, Integer> getUnitsMap() {
        return Collections.unmodifiableMap(unitsMap);
    }

    public GameType getCurrentGameType() {
        return currentGameType;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

}
