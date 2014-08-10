package starwors.model.lx.logic.utils.fs;


import starwors.model.lx.galaxy.Planet;

public interface ICriteria {

    boolean isRemoveFromPath(Planet planet);

    boolean isSuccess(Planet planet);

}
