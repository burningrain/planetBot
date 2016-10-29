package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.map.service.api.MapValidationException;

/**
 * Created by SBT-Burshinov-OA on 05.10.2016.
 */
public interface Validator {
    boolean validate(String jsonData, String jsonSchema) throws MapValidationException;
}
