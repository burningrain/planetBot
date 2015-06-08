package com.br.model.objects.remote;

import java.util.Set;

import com.br.model.objects.IModelObject;

public interface IStrategies extends IModelObject<IStrategies> {

	
	Set<String> getStrategies();
	
	void setCurrentStrategy(String title);

}