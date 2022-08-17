package com.github.br.starmarines.gamecore.impl.store;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.service.api.MapService;
import com.github.br.starmarines.map.service.api.MapValidationException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheMapService implements MapService {

    private final ConcurrentHashMap<String, Galaxy> cache = new ConcurrentHashMap<>();

    private final MapService delegate;

    public MemoryCacheMapService(MapService delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public List<String> getAllTitles() {
        return delegate.getAllTitles();
    }

    @Override
    public List<String> getTitles(int from, int to) {
        return delegate.getTitles(from, to);
    }

    @Override
    public Galaxy getMap(String title) throws MapValidationException {
        Galaxy galaxy = cache.get(title);
        if(galaxy == null) {
            galaxy = getFromDelegate(title);
            cache.put(title, galaxy);
        }

        return galaxy;
    }

    private Galaxy getFromDelegate(String title) throws MapValidationException {
        return delegate.getMap(title);
    }

}
