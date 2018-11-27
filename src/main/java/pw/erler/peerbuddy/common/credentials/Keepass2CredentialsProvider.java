package pw.erler.peerbuddy.common.credentials;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.KeePassFile;
import de.slackspace.openkeepass.domain.Property;

public class Keepass2CredentialsProvider implements CredentialsProvider {

	private final KeePassFile database;

	public Keepass2CredentialsProvider(final File databasePath, final String masterPassword) {
		checkArgument(databasePath.isFile() && databasePath.canRead(),
				"Could not read KeePass2 database from file '%s' : file is no readable file", databasePath);
		database = KeePassDatabase.getInstance(databasePath).openDatabase(masterPassword);
	}

	@Override
	public Credentials getCredentials(final String title) {
		final Entry entry = database.getEntryByTitle(title);
		final Map<String, String> properties = entry.getProperties().stream()
				.collect(Collectors.toMap(Property::getKey, Property::getValue));
		return new Credentials(entry.getUsername(), entry.getPassword(), properties);
	}

}
