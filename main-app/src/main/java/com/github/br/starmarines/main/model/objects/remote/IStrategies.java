package com.github.br.starmarines.main.model.objects.remote;

import java.util.Collection;
import java.util.Set;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.main.model.objects.IModelObject;

public interface IStrategies extends IModelObject<IStrategies> {

	
	Set<String> getStrategies();
	
	void setCurrentStrategy(String title);
	
	String getCurrentStrategy();
	
	Collection<Move> step(Collection<Planet> galaxy);

}