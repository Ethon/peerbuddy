package pw.erler.peerbuddy.common.values;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MonetaryValue implements AccountValue {

	BigDecimal amount;
	Currency currency;

	public MonetaryValue(final double amount, final Currency currency) {
		this(BigDecimal.valueOf(amount), currency);
	}

	public MonetaryValue add(final MonetaryValue other) {
		checkArgument(other.getCurrency().equals(getCurrency()));
		return new MonetaryValue(getAmount().add(other.getAmount()), getCurrency());
	}

	public MonetaryValue negate() {
		return new MonetaryValue(getAmount().negate(), getCurrency());
	}

	public String toString(final Locale locale) {
		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
		numberFormat.setCurrency(currency);
		return numberFormat.format(amount);
	}

	@Override
	public String toString() {
		return toString(Locale.getDefault());
	}

}
