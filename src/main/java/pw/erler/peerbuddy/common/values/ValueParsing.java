package pw.erler.peerbuddy.common.values;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValueParsing {

	private static final int GROUP_NEGATE = 1;
	private static final int GROUP_TYPE_SYMBOL_PRE = 2;
	private static final int GROUP_NEGATE2 = 3;
	private static final int GROUP_THOUSANDS_VALUE = 4;
	private static final int GROUP_LOW_VALUE = 5;
	private static final int GROUP_FRACTIONAL_VALUE = 6;
	private static final int GROUP_TYPE_SYMBOL_POST = 7;

	private static final String RE_NEGATE = "(-)?\\s*";
	private static final String RE_TYPE_SYMBOL = "\\s*([%€£]|EUR)?\\s*";
	private static final String RE_VALUE_FRACTIONAL = "([.]\\d{1,2})?";
	private static final String RE_LOW_VALUE = "(\\d{1,3})";
	private static final String RE_THOUSANDS_VALUE = "(\\d{1,3}[,\\s])?";
	private static final String RE_FULL_VALUE = RE_THOUSANDS_VALUE + RE_LOW_VALUE + RE_VALUE_FRACTIONAL;

	private static final Pattern PATTERN = Pattern
			.compile(RE_NEGATE + RE_TYPE_SYMBOL + RE_NEGATE + RE_FULL_VALUE + RE_TYPE_SYMBOL);

	private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1_000L);

	private static char detectDecimalSeparator(final String value) {
		final int lastDot = value.lastIndexOf('.');
		final int lastComma = value.lastIndexOf(',');
		if (lastComma > lastDot) {
			return ',';
		}
		return '.';
	}

	private static String mapToEnglishNumberFormat(final String value) {
		final String dummyReplacement = "<§§§>";
		final char separator = detectDecimalSeparator(value);
		if (separator == ',') {
			return value.replace(",", dummyReplacement).replace(".", ",").replace(dummyReplacement, ".");
		}
		return value;
	}

	private static BigDecimal buildNumber(final boolean isNegative, final String fractionalValue, final String lowValue,
			final String thousandsValue) {
		BigDecimal number = new BigDecimal(fractionalValue != null ? lowValue + fractionalValue : lowValue);
		if (thousandsValue != null) {
			number = number.add(new BigDecimal(thousandsValue).multiply(ONE_THOUSAND));
		}
		if (isNegative) {
			number = number.negate();
		}
		return number;
	}

	private static AccountValue buildAccountValue(final String typeSymbol, final BigDecimal value,
			final String accountValue) {
		switch (typeSymbol) {
		case "%":
			return new PercentageValue(value);
		case "€":
		case "EUR":
			return new MonetaryValue(value, Currency.getInstance("EUR"));
		case "£":
			return new MonetaryValue(value, Currency.getInstance("GBP"));
		default:
			throw new NumberFormatException("Can't determine type of account value '" + accountValue + "'");
		}
	}

	public static AccountValue parseValue(final String s) {
		// Match pattern.
		final Matcher matcher = PATTERN.matcher(mapToEnglishNumberFormat(s.trim()));
		if (!matcher.matches()) {
			throw new NumberFormatException("Can't parse account value '" + s + "'");
		}

		// Extract parts.
		final boolean isNegative = "-".equals(matcher.group(GROUP_NEGATE)) || "-".equals(matcher.group(GROUP_NEGATE2));
		final String typeSymbol = matcher.group(GROUP_TYPE_SYMBOL_PRE) != null ? matcher.group(GROUP_TYPE_SYMBOL_PRE)
				: matcher.group(GROUP_TYPE_SYMBOL_POST);
		if (typeSymbol == null) {
			throw new NumberFormatException("Can't determine type of account value '" + s + "'");
		}
		final String fractionalValue = matcher.group(GROUP_FRACTIONAL_VALUE);
		final String lowValue = matcher.group(GROUP_LOW_VALUE);
		String thousandsValue = matcher.group(GROUP_THOUSANDS_VALUE);
		if (thousandsValue != null) {
			thousandsValue = thousandsValue.replaceAll("[,\\s]+", "");
		}
		final BigDecimal value = buildNumber(isNegative, fractionalValue, lowValue, thousandsValue);
		return buildAccountValue(typeSymbol, value, s);
	}

	public static int getStartIndexOfValue(final String s) {
		final Matcher matcher = PATTERN.matcher(mapToEnglishNumberFormat(s.trim()));
		if (matcher.find()) {
			return matcher.start();
		}
		return -1;
	}

}
