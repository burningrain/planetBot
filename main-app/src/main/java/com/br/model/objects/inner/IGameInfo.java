package com.br.model.objects.inner;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.br.game.api.galaxy.Planet;
import com.br.model.objects.IModelObject;
import com.br.model.objects.inner.impl.GameInfo.GameType;

public interface IGameInfo extends IModelObject<IGameInfo> {

	Set<String> getPlayers();

	Map<String, Integer> getUnitsMap();

	GameType getCurrentGameType();

	int getStepsCount();

	Collection<Planet> getGalaxy();

}