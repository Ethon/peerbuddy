package pw.erler.peerbuddy.account;

import java.util.Collection;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;
import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.common.values.ValueParsing;

public abstract class AbstractBasicSeleniumAccountSupport extends AbstractSeleniumAccountSupport {

	protected BasicAccountStatus doRetrieveBasicAccountStatus(final String name, final String url,
			final WebElementDescription valueLabelElement) {
		get(url);
		final String balanceLabelValue = getLabelText(valueLabelElement);
		final MonetaryValue balance = ValueParsing.parseValue(balanceLabelValue).monetaryValue();
		return new BasicAccountStatus(name, balance);
	}

	protected abstract BasicAccountStatus retrieveBasicAccountStatus();

	protected AbstractBasicSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig,
			final Collection<AccountFeature> accountFeatures) {
		super(webDriver, accountConfig, accountFeatures);
	}

	@Override
	public <T> T retrieveAccountStatus(final AccountStatusVisitor<T> visitor) {
		return retrieveBasicAccountStatus().accept(visitor);
	}

}
