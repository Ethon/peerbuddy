package pw.erler.peerbuddy.common.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ToStringSerializer<T> implements JsonSerializer<T> {

	@Override
	public JsonElement serialize(final T src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

}
