package com.github.br.starmarines.map.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;


/**
 * Created by SBT-Burshinov-OA on 05.10.2016.
 */
public class MapValidator implements Validator {
    @Override
    public boolean validate(String jsonData, String jsonSchema) throws Exception {
        JsonNode schemaNode = JsonLoader.fromString(jsonSchema); // throws JsonProcessingException if error
        JsonNode data = JsonLoader.fromString(jsonData);         // same here

        JsonSchemaFactory factory =  JsonSchemaFactory .byDefault();
        // load the schema and validate
        JsonSchema schema = factory.getJsonSchema(schemaNode);
        ProcessingReport report;

        report = schema.validate(data);

        return report.isSuccess();
    }
}
