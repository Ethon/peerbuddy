package pw.erler.peerbuddy.common.values;

import org.openqa.selenium.InvalidArgumentException;

public final class AccountAttributeParsing {

	public static AccountAttributePair parseAccountAttributePair(final String s) {
		final String trimmed = s.trim();
		final int valueStartIndex = ValueParsing.getStartIndexOfValue(trimmed);
		if (valueStartIndex == -1) {
			throw new InvalidArgumentException("Input string '" + s + "' contains no value");
		}
		final AccountValue value;
		try {
			value = ValueParsing.parseValue(trimmed.substring(valueStartIndex).trim());
		} catch (final NumberFormatException e) {
			throw new AttributeException("Error parsing account attribute '" + s + "'", e);
		}
		if (valueStartIndex == 0) {
			return new AccountAttributePair(null, value);
		}
		final String key = trimmed.substring(0, valueStartIndex).trim();
		return new AccountAttributePair(key, value);
	}

}
