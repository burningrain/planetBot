package com.github.br.starmarines.main.model.objects.remote;

import java.util.Set;

import com.github.br.starmarines.main.model.objects.IModelObject;

public interface IStrategies extends IModelObject<IStrategies> {

	
	Set<String> getStrategies();
	
	void setCurrentStrategy(String title);

}