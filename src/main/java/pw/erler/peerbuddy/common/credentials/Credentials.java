package pw.erler.peerbuddy.common.credentials;

import java.util.Collections;
import java.util.Map;

import lombok.Value;

@Value
public final class Credentials {

	private String login;
	private String password;
	private Map<String, String> properties;

	public Credentials(final String login, final String password) {
		this(login, password, Collections.emptyMap());
	}

	public Credentials(final String login, final String password, final Map<String, String> properties) {
		this.login = login;
		this.password = password;
		this.properties = properties;
	}

	public String getStringProperty(final String key) throws CredentialsException {
		final String property = properties.get(key);
		if (property == null) {
			throw new CredentialsException("Property '" + key + "' is missing");
		}
		return property;
	}

	public String getStringProperty(final String key, final String defaultValue) {
		return properties.getOrDefault(key, defaultValue);
	}

	public Integer getIntProperty(final String key) throws CredentialsException {
		return Integer.valueOf(getStringProperty(key));
	}

	public Integer getIntProperty(final String key, final Integer defaultValue) {
		final String stringValue = properties.get(key);
		return stringValue != null ? Integer.valueOf(stringValue) : defaultValue;
	}

}
