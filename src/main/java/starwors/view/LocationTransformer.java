package starwors.view;


import org.apache.commons.collections15.Transformer;
import starwors.model.lx.galaxy.Planet;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocationTransformer implements Transformer<Planet, Point2D> {

    private Map<String, Point2D> aPlanetMap = new HashMap<String, Point2D>();
    private Map<String, Point2D> bPlanetMap = new HashMap<String, Point2D>();
    private Map<String, Point2D> cPlanetMap = new HashMap<String, Point2D>();
    private Map<String, Point2D> dPlanetMap = new HashMap<String, Point2D>();


    @Override
    public Point2D transform(Planet planet) {
        Point2D p = getPoint(planet);
        return p;
    }

    private Point2D getPoint(Planet planet){
        Point2D point = null;
        Map<String, Point2D> map = getMap(planet);
        if(map.containsKey(planet.getId())){
            point = map.get(planet.getId());
        } else{
            point = new Point();
            double x = findPlanetX(planet);
            double y = 0;
            if(map.size() != 0){
                y = findMinPlanetY(map) - 40;
            } else{
                y = SwingView.START_HEIGHT - 50; // устанавливаем начальное значение
            }
            point.setLocation(x, y);

            map.put(planet.getId(), point);
        }

        return point;
    }



    private Map<String, Point2D> getMap(Planet planet){
        switch(planet.getType()){
            case TYPE_A:
                return aPlanetMap;
            case TYPE_B:
                return bPlanetMap;
            case TYPE_C:
                return cPlanetMap;
            case TYPE_D:
                return dPlanetMap;
            default:
                return null;
        }
    }

    private double findPlanetX(Planet planet){
        double x = 0;
        switch(planet.getType()){
            case TYPE_A:
                x = SwingView.START_HEIGHT/4;
                break;
            case TYPE_B:
                x = SwingView.START_HEIGHT/2;
                break;
            case TYPE_C:
                x = SwingView.START_HEIGHT* 3/4;
                break;
            case TYPE_D:
                x = SwingView.START_HEIGHT - 50;
                break;
        }
        return x;
    }


    private double findMinPlanetY(Map<String, Point2D> map){
        ArrayList<Double> list = new ArrayList<Double>(map.size());
        for(Map.Entry<String, Point2D> entry : map.entrySet()){
            list.add(entry.getValue().getY());
        }
        Collections.sort(list);

        return list.get(0);
    }


}
