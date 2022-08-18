package com.github.br.starmarines.service.strategy.impl;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.strategy.IStrategy;
import com.github.br.starmarines.service.strategy.IStrategyService;
import org.osgi.service.component.annotations.*;
import org.osgi.service.log.LogService;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component(service = IStrategyService.class)
public class StrategyServiceImpl implements IStrategyService {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
    private volatile LogService logService;

    private ConcurrentHashMap<String, IStrategy> strategies = new ConcurrentHashMap<>();
    private AtomicReference<IStrategy> currentStrategy = new AtomicReference<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, unbind = "unsetIStrategy")
    private void setIStrategy(IStrategy strategy) {
        if(logService != null) {
            logService.log(LogService.LOG_INFO, "add strategy - " + strategy.getTitle());
        }

        strategies.put(strategy.getTitle(), strategy);
    }

    private void unsetIStrategy(IStrategy strategy) {
        if(logService != null) {
            logService.log(LogService.LOG_INFO, "remove strategy - " + strategy.getTitle());
        }
        strategies.remove(strategy.getTitle());
        currentStrategy.compareAndSet(strategy, null);
    }

    @Override
    public Collection<String> getStrategies() {
        return strategies.keySet();
    }

    @Override
    public void setCurrentStrategy(String title) {
        currentStrategy.set(strategies.get(title));
    }

    @Override
    public String getCurrentStrategy() {
        IStrategy iStrategy = currentStrategy.get();
        if (iStrategy != null) {
            return iStrategy.getTitle();
        }
        return null;
    }

    @Override
    public Collection<Move> step(Collection<Planet> galaxy) {
        IStrategy iStrategy = currentStrategy.get();
        if (iStrategy != null) {
            return iStrategy.step(galaxy);
        }
        return null;
    }

}
