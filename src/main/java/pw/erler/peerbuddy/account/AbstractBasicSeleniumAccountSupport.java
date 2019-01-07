package pw.erler.peerbuddy.account;

import java.util.List;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;
import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.common.values.ValueParsing;

public abstract class AbstractBasicSeleniumAccountSupport extends AbstractSeleniumAccountSupport {

	protected BasicAccountStatus doRetrieveBasicAccountStatus(final String url,
			final WebElementDescription valueLabelElement) {
		get(url);
		final String balanceLabelValue = getLabelText(valueLabelElement);
		final MonetaryValue balance = ValueParsing.parseValue(balanceLabelValue).monetaryValue();
		return new BasicAccountStatus(balance);
	}

	protected abstract BasicAccountStatus retrieveBasicAccountStatus();

	protected AbstractBasicSeleniumAccountSupport(final WebDriver webDriver,
			final List<AccountFeature> accountFeatures) {
		super(webDriver, accountFeatures);
	}

	@Override
	public <T> T retrieveAccountStatus(final AccountStatusVisitor<T> visitor) {
		return retrieveBasicAccountStatus().accept(visitor);
	}

}
