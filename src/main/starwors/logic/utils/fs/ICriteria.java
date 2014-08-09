package main.starwors.logic.utils.fs;


import main.starwors.galaxy.Planet;

public interface ICriteria {

    boolean isRemoveFromPath(Planet planet);

    boolean isSuccess(Planet planet);

}
