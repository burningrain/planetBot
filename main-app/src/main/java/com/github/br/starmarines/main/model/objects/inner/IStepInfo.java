package com.github.br.starmarines.main.model.objects.inner;

import java.util.Collection;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.main.model.objects.IModelObject;
import com.github.br.starmarines.main.model.services.inner.beans.Response;

public interface IStepInfo extends IModelObject<IStepInfo> {

	Response getCurrentStep();
	
	Collection<Action> getCurrentActions();	
	
	void updateResponse(Response response);
	
	void updateCurrentActions(Collection<Action> actions);

}