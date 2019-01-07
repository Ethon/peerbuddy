package pw.erler.peerbuddy.common.credentials;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;

import lombok.SneakyThrows;
import pw.erler.peerbuddy.common.config.PasswordConfig;
import pw.erler.peerbuddy.common.misc.LoggingUtil;

public final class CredentialsProviderFactory {

	@SneakyThrows
	private static String getDatabasePassword(final PasswordConfig passwordConfig) {
		if (passwordConfig.getDatabasePassword() != null) {
			return passwordConfig.getDatabasePassword();
		}

		final PrintStream out = LoggingUtil.originalOutStream;
		out.println();
		out.print("Please enter database password: ");
		return new BufferedReader(new InputStreamReader(System.in)).readLine();
	}

	public static CredentialsProvider createCredentialsProvider(final PasswordConfig passwordConfig) {
		checkNotNull(passwordConfig, "passwordConfig missing in config");
		checkNotNull(passwordConfig.getDatabaseFile(), "passwordConfig.databaseFile missing in config");
		return new Keepass2CredentialsProvider(new File(passwordConfig.getDatabaseFile()),
				getDatabasePassword(passwordConfig));
	}

}
