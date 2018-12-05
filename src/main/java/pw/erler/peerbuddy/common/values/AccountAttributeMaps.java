package pw.erler.peerbuddy.common.values;

import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountAttributeMaps {

	public static <T> T get(final Map<String, AccountValue> attributes, final String key, final Class<T> valueClazz) {
		final AccountValue value = attributes.get(key);
		if (value == null) {
			throw new AttributeException(String.format("Missing attribute with key '%s'", key));
		}
		try {
			return value.as(valueClazz);
		} catch (final AttributeException e) {
			throw new AttributeException(String.format("Attribute with key '%s' is of wrong value", key));
		}
	}

}
