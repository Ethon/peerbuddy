package pw.erler.peerbuddy.account.p2p.estateguru;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.values.AccountValue;
import pw.erler.peerbuddy.common.values.MonetaryValue;

public class EstateGuruSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	public EstateGuruSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig) {
		super(webDriver, accountConfig);
	}

	@Override
	public void login(final Credentials credentials) {
		getPageAndAwaitLoad("Login page", EstateGuruConstants.LOGIN_PAGE_URL);
		enterTextIntoInputField(EstateGuruConstants.USERNAME_INPUT_FIELD, credentials.getLogin());
		enterTextIntoInputField(EstateGuruConstants.PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(EstateGuruConstants.LOGIN_BUTTON);
		awaitPageLoad("Overview page", EstateGuruConstants.PORTFOLIO_OVERVIEW_PAGE_URL);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		getPageAndAwaitLoad("Overview page", EstateGuruConstants.PORTFOLIO_OVERVIEW_PAGE_URL);
		final Map<String, AccountValue> attributes = getAttributes(
				find().withXPath(EstateGuruConstants.XPATH_OVERVIEW_ROWS));

		final MonetaryValue totalAccountValue = (MonetaryValue) attributes.get("Total Account Value");
		final MonetaryValue invested = ((MonetaryValue) attributes.get("Invested")).negate();
		final MonetaryValue reserved = ((MonetaryValue) attributes.get("Reserved")).negate();
		final MonetaryValue availableAmount = (MonetaryValue) attributes.get("Available Amount");
		return new P2PAccountStatus(accountConfig.getTitle(), totalAccountValue, invested.add(reserved),
				availableAmount);
	}

}
