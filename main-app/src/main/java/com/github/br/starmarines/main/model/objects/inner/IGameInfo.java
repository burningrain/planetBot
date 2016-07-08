package com.github.br.starmarines.main.model.objects.inner;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.main.model.objects.IModelObject;
import com.github.br.starmarines.main.model.objects.inner.impl.GameInfo.GameType;

public interface IGameInfo extends IModelObject<IGameInfo> {

	Set<String> getPlayers();

	Map<String, Integer> getUnitsMap();

	GameType getCurrentGameType();

	int getStepsCount();

	Collection<Planet> getGalaxy();

}