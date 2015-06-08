package com.br.model.objects;

import com.br.model.objects.inner.IGameInfo;
import com.br.model.objects.inner.IStepInfo;
import com.br.model.objects.remote.IStrategies;

public interface IModel {

	public IGameInfo getGameInfo();

	public IStepInfo getStep();

	public IStrategies getStrategies();

}