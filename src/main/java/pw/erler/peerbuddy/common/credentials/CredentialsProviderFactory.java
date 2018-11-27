package pw.erler.peerbuddy.common.credentials;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import pw.erler.peerbuddy.common.config.PasswordConfig;

public final class CredentialsProviderFactory {

	private static String getDatabasePassword(final PasswordConfig passwordConfig) {
		if (passwordConfig.getDatabasePassword() != null) {
			return passwordConfig.getDatabasePassword();
		}
		final char[] password = System.console().readPassword("Please enter password for password database: ");
		return new String(password);
	}

	public static CredentialsProvider createCredentialsProvider(final PasswordConfig passwordConfig) {
		checkNotNull(passwordConfig, "passwordConfig missing in config");
		checkNotNull(passwordConfig.getDatabaseFile(), "passwordConfig.databaseFile missing in config");
		return new Keepass2CredentialsProvider(new File(passwordConfig.getDatabaseFile()),
				getDatabasePassword(passwordConfig));
	}

}
