package com.github.br.starmarines.map.service.api;

/**
 * Created by mr.zoom on 11.09.2016.
 */
public class MapValidationException extends Exception {

    private String cause;

    public MapValidationException(String message, String cause) {
        super(message);
        this.cause = cause;
    }
}

