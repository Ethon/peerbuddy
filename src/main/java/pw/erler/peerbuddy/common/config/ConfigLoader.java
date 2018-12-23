package pw.erler.peerbuddy.common.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.google.common.io.Files;
import com.google.gson.Gson;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConfigLoader {

	public static PeerBuddyConfig loadConfig(final Path path) throws IOException {
		final String content = Files.asCharSource(path.toFile(), StandardCharsets.UTF_8).read();
		return new Gson().fromJson(content, PeerBuddyConfig.class);
	}

}
