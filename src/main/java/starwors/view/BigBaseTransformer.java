package starwors.view;

import org.apache.commons.collections15.Transformer;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.Game;
import starwors.model.lx.logic.utils.PlanetCloner;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;


public class BigBaseTransformer implements Transformer<Planet, Point2D> {

    private Collection<Planet> galaxy;
    private Map<String, Point2D> planetsCoords;

    private Point2D centerPoint = new Point(SwingView.START_WIDTH/2, SwingView.START_HEIGHT/2);

    //private Map<String, Point2D> aPlanetMap = new HashMap<String, Point2D>();
    private Set<Planet> visitedPlanets;
    private List<Planet> levelList = new LinkedList<Planet>();
    private int level = 0;
    private Map<Integer, List<Planet>> planetsLevels;


    public BigBaseTransformer(Collection<Planet> galaxy){
        this.galaxy = galaxy;
        visitedPlanets = new HashSet<Planet>(galaxy.size());
        planetsLevels = new LinkedHashMap<Integer, List<Planet>>(galaxy.size());
        planetsCoords = new HashMap<String, Point2D>(galaxy.size());

        initPlanetsLocation();
        fillPlanetsCoords();
    }

    @Override
    public Point2D transform(Planet planet) {
        return planetsCoords.get(planet.getId());
    }

    private void fillPlanetsCoords() {
        int magnitude = 0;
        for (Map.Entry<Integer, List<Planet>> entry : planetsLevels.entrySet()) {
            List<Planet> planets = entry.getValue();
            double angular = 360.0 / planets.size();

            double offset = 0;
            if (entry.getKey() == 1) {
                offset = angular / 2;
            }

            for (int i = 1; i < planets.size() + 1; i++) {
                planetsCoords.put(
                        planets.get(i - 1).getId(),
                        new Point(Double.valueOf(centerPoint.getX() + magnitude * Math.cos((offset + i * angular) * Math.PI / 180)).intValue(),
                                Double.valueOf(centerPoint.getY() + magnitude * Math.sin((offset + i * angular) * Math.PI / 180)).intValue()));
            }

            magnitude += 70;
        }
    }


    private void initPlanetsLocation(){
        Planet center = getCenterPlanet(galaxy);

        List<Planet> list0 = new ArrayList<Planet>(1);
        list0.add(center);
        visitedPlanets.add(center);
        planetsLevels.put(level, list0);
        planetsCoords.put(center.getId(), centerPoint);

        planetsLevels.put(++level, new LinkedList(PlanetCloner.clonePlanets(center.getNeighbours()))); //FIXME
        makeThree(getNeighbours(center));

        for(Map.Entry<Integer, List<Planet>> entry : planetsLevels.entrySet()){
            Collections.sort(entry.getValue(), new Comparator<Planet>() {
                @Override
                public int compare(Planet o1, Planet o2) {
                    return Integer.valueOf(o1.getId()) - Integer.valueOf(o2.getId());
                }
            });
        }
    }

    private void makeThree(List<Planet> neighbours) {
        if (neighbours.size() != 0) {

            List<Planet> tempLevelStates = new LinkedList<Planet>();
            Iterator<Planet> iterator = neighbours.iterator();
            while (iterator.hasNext()) {
                Planet neighbour = iterator.next();
                tempLevelStates.addAll(getNeighbours(neighbour));
                iterator.remove();

            }
            levelList = tempLevelStates;
            if (!levelList.isEmpty()) {
                planetsLevels.put(++level, new LinkedList(PlanetCloner.clonePlanets(levelList)));
                makeThree(levelList);
            }
        }
    }

    private List<Planet> getNeighbours(Planet planet) {
        visitedPlanets.add(planet);

        List<Planet> neighbours = planet.getNeighbours();
        Iterator<Planet> iterator = neighbours.iterator();
        while (iterator.hasNext()) {
            Planet neighbour = iterator.next();

            if (!visitedPlanets.contains(neighbour)) {
                visitedPlanets.add(neighbour);
            } else {
                iterator.remove();
            }
        }

        return neighbours;
    }

    private Planet getCenterPlanet(Collection<Planet> galaxy){
        PlanetType type = null;
        switch(Game.getCurrentType()){
            case BIG_BASES:
                type = PlanetType.TYPE_A;
                break;
            case BASE_IN_CENTER:
                type = PlanetType.TYPE_D;
                break;
            default:
                type = null;
                break;
        }
        return getCenterPlanet(galaxy, type);
    }

    private Planet getCenterPlanet(Collection<Planet> galaxy, PlanetType type){
        for(Planet planet : galaxy){
            if(type.equals(planet.getType())){
                return planet;
            }
        }
        return null;
    }







}
