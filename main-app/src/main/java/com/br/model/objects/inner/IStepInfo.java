package com.br.model.objects.inner;

import java.util.Collection;

import com.br.model.objects.IModelObject;
import com.br.model.services.inner.beans.Response;
import com.br.starwors.lx.galaxy.Action;

public interface IStepInfo extends IModelObject<IStepInfo> {

	Response getCurrentStep();
	Collection<Action> getCurrentActions();	

}