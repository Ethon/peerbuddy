package pw.erler.peerbuddy.common.values;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PercentageValue implements AccountValue {

	BigDecimal value;

	public PercentageValue(final double value) {
		this.value = BigDecimal.valueOf(value);
	}

	public String toString(final Locale locale) {
		return NumberFormat.getPercentInstance(locale).format(value);
	}

	@Override
	public String toString() {
		return toString(Locale.getDefault());
	}

}
