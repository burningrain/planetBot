package com.github.br.starmarines.service.strategy;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.strategy.IStrategy;

import java.util.Collection;

/**
 * Контракты именно такие, чтобы наружу не просочился {@link IStrategy}
 * Отцеплять сервис не планируется
 */
public interface IStrategyService {

    Collection<String> getStrategies();

    void setCurrentStrategy(String title);

    String getCurrentStrategy();

    Collection<Move> step(Collection<Planet> galaxy);

}
