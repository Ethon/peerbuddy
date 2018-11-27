package pw.erler.peerbuddy.common.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.common.values.PercentageValue;

public final class GsonFactory {

	public static Gson createGson() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MonetaryValue.class, new ToStringSerializer<>());
		builder.registerTypeAdapter(PercentageValue.class, new ToStringSerializer<>());
		return builder.create();
	}

}
