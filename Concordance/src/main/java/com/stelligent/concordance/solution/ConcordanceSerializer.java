package com.stelligent.concordance.solution;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author phillipodam
 *
 * Custom JSON serializer for object {@link com.stelligent.concordance.solution.Concordance}
 */
public class ConcordanceSerializer extends JsonSerializer<Concordance> {
	/**
	 * Serialize {@link com.stelligent.concordance.solution.Concordance} to JSON
	 * 
	 * @param {@link com.stelligent.concordance.solution.Concordance} to be serialized
	 * @param {@link com.fasterxml.jackson.core.JsonGenerator}
	 * @param {@link com.fasterxml.jackson.databind.SerializerProvider}
	 */
	@Override
	public void serialize(Concordance value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		if (value.getError() == null) {
			generator.writeArrayFieldStart("concordance");
			for (Map.Entry<String, OccurrenceAndAppearance> entry : value.getConcordance().entrySet()) {
				generator.writeStartObject();
				generator.writeStringField("word", entry.getKey());
				generator.writeNumberField("occurrence", entry.getValue().getOccurrence());
				generator.writeArrayFieldStart("appearances");
				for (Long appearance : entry.getValue().getAppearances()) {
					generator.writeNumber(appearance);
				}
				generator.writeEndArray();
				generator.writeEndObject();
			}
			generator.writeEndArray();
		} else {
			generator.writeStringField("error", value.getError());
		}
		generator.writeEndObject();
	}
}