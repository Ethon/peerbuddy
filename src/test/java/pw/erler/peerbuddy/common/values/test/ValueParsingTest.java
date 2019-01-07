package pw.erler.peerbuddy.common.values.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.common.values.PercentageValue;
import pw.erler.peerbuddy.common.values.ValueParsing;

public class ValueParsingTest {

	private static final Currency EUR = Currency.getInstance("EUR");
	private static final Currency GBP = Currency.getInstance("GBP");

	private void testMonetaryValue(final String toTest, final String normalizedValue, final Currency currency) {
		assertThat(ValueParsing.parseValue(toTest))
				.isEqualTo(new MonetaryValue(new BigDecimal(normalizedValue), currency));
	}

	private void testPercentValue(final String toTest, final String normalizedValue) {
		assertThat(ValueParsing.parseValue(toTest)).isEqualTo(new PercentageValue(new BigDecimal(normalizedValue)));
	}

	@Test
	public void testEuroValuesInEnglishFormat() {
		testMonetaryValue("€1,050.47", "1050.47", EUR);
		testMonetaryValue("€133.47", "133.47", EUR);
		testMonetaryValue("€199", "199", EUR);
		testMonetaryValue("- €1,050.47", "-1050.47", EUR);
		testMonetaryValue("- €133.47", "-133.47", EUR);
		testMonetaryValue("- €199", "-199", EUR);
		testMonetaryValue("€-1,044.06", "-1044.06", EUR);
		testMonetaryValue("€-44.06", "-44.06", EUR);
		testMonetaryValue("€-44", "-44", EUR);

		testMonetaryValue("1,225.61 €", "1225.61", EUR);
		testMonetaryValue("459.61 €", "459.61", EUR);
		testMonetaryValue("555 €", "555", EUR);
		testMonetaryValue("- 1,225.61 €", "-1225.61", EUR);
		testMonetaryValue("- 459.61 €", "-459.61", EUR);
		testMonetaryValue("- 555 €", "-555", EUR);

		testMonetaryValue("1,225.61 EUR", "1225.61", EUR);
		testMonetaryValue("459.61 EUR", "459.61", EUR);
		testMonetaryValue("555 EUR", "555", EUR);
		testMonetaryValue("- 1,225.61 EUR", "-1225.61", EUR);
		testMonetaryValue("- 459.61 EUR", "-459.61", EUR);
		testMonetaryValue("- 555 EUR", "-555", EUR);
	}

	@Test
	public void testPercentValuesInEnglishFormat() {
		testPercentValue("10.91%", "10.91");
		testPercentValue("10%", "10");
		testPercentValue("-10.91%", "-10.91");
		testPercentValue("-10%", "-10");

		testPercentValue("10.91 %", "10.91");
		testPercentValue("10 %", "10");
		testPercentValue("-10.91 %", "-10.91");
		testPercentValue("-10 %", "-10");
	}

	@Test
	public void testGbpValuesInEnglishFormat() {
		testMonetaryValue("£1,077.24", "1077.24", GBP);
		testMonetaryValue("£1.00", "1.00", GBP);
	}

	@Test
	public void testEuroValuesInGermanFormat() {
		testMonetaryValue("€1.050,47", "1050.47", EUR);
		testMonetaryValue("€133,47", "133.47", EUR);
		testMonetaryValue("€199", "199", EUR);
		testMonetaryValue("- €1.050,47", "-1050.47", EUR);
		testMonetaryValue("- €133,47", "-133.47", EUR);
		testMonetaryValue("- €199", "-199", EUR);
		testMonetaryValue("€-1.044,06", "-1044.06", EUR);
		testMonetaryValue("€-44,06", "-44.06", EUR);
		testMonetaryValue("€-44", "-44", EUR);

		testMonetaryValue("1.225,61 €", "1225.61", EUR);
		testMonetaryValue("459,61 €", "459.61", EUR);
		testMonetaryValue("555 €", "555", EUR);
		testMonetaryValue("- 1.225,61 €", "-1225.61", EUR);
		testMonetaryValue("- 459,61 €", "-459.61", EUR);
		testMonetaryValue("- 555 €", "-555", EUR);

		testMonetaryValue("1.225,61 EUR", "1225.61", EUR);
		testMonetaryValue("459,61 EUR", "459.61", EUR);
		testMonetaryValue("555 EUR", "555", EUR);
		testMonetaryValue("- 1.225,61 EUR", "-1225.61", EUR);
		testMonetaryValue("- 459,61 EUR", "-459.61", EUR);
		testMonetaryValue("- 555 EUR", "-555", EUR);
	}

	@Test
	public void testPercentValuesInGermanFormat() {
		testPercentValue("10,91%", "10.91");
		testPercentValue("10%", "10");
		testPercentValue("-10,91%", "-10.91");
		testPercentValue("-10%", "-10");

		testPercentValue("10,91 %", "10.91");
		testPercentValue("10 %", "10");
		testPercentValue("-10,91 %", "-10.91");
		testPercentValue("-10 %", "-10");
	}

	@Test
	public void testGbpValuesInGermanFormat() {
		testMonetaryValue("£1.077,24", "1077.24", GBP);
		testMonetaryValue("£1,00", "1.00", GBP);
	}

}
