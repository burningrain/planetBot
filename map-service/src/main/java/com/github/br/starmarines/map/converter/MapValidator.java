package com.github.br.starmarines.map.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.br.starmarines.map.service.api.MapValidationException;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * Created by SBT-Burshinov-OA on 05.10.2016.
 */
public class MapValidator implements Validator {
	@Override
	public boolean validate(String jsonData, String jsonSchema) throws MapValidationException {
		JsonNode schemaNode;
		JsonNode data;
		JsonSchema schema;
		ProcessingReport report;
		try {
			schemaNode = JsonLoader.fromString(jsonSchema);
			data = JsonLoader.fromString(jsonData);
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			// load the schema and validate
			schema = factory.getJsonSchema(schemaNode);
			report = schema.validate(data);
		} catch (Exception e) {
			throw new MapValidationException("Error: JSON data invalid. ", e.getMessage());
		}
		if (!report.isSuccess()) {
			throw new MapValidationException("Error: Current map have some " + "problem in JSON meta info.\n\r"
					+ " Input json: " + jsonData  + "\n\r" +
					" Cause: " + this.getErrorsList(report, true)
					, " Try to reformat map");
		}
		return report.isSuccess();
	}

	private String getErrorsList(ProcessingReport report, boolean onlyErrors) {
		StringBuilder jsonValidationErrors = new StringBuilder();
		for (ProcessingMessage processingMessage : report) {
			if (onlyErrors && LogLevel.ERROR.equals(processingMessage.getLogLevel())) {
				jsonValidationErrors.append(processingMessage.getMessage()).append("\n\r");
			} else if (!onlyErrors) {
				jsonValidationErrors.append(processingMessage.getMessage()).append("\n\r");
			}
		}
		return jsonValidationErrors.toString();
	}
}
