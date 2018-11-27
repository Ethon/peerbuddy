package pw.erler.peerbuddy.common.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import pw.erler.peerbuddy.common.values.MonetaryValue;

public class MonetaryValueSerializer implements JsonSerializer<MonetaryValue> {

	@Override
	public JsonElement serialize(final MonetaryValue src, final Type typeOfSrc,
			final JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

}
