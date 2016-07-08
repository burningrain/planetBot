package com.github.br.starmarines.main.model.objects;

import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.model.objects.remote.IStrategies;

public interface IModel {

	public IGameInfo getGameInfo();

	public IStepInfo getStep();

	public IStrategies getStrategies();

}