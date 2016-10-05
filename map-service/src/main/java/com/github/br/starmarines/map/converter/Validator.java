package com.github.br.starmarines.map.converter;

/**
 * Created by SBT-Burshinov-OA on 05.10.2016.
 */
public interface Validator {
    boolean validate(String jsonData, String jsonSchema) throws Exception;
}
