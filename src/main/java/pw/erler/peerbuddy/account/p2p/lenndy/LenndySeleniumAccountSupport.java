package pw.erler.peerbuddy.account.p2p.lenndy;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.values.AccountAttributePair;
import pw.erler.peerbuddy.common.values.AccountAttributeParsing;
import pw.erler.peerbuddy.common.values.AccountValue;

public class LenndySeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	public LenndySeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get(LenndyConstants.LOGIN_PAGE_URL);
		enterTextIntoInputField(LenndyConstants.USERNAME_INPUT_FIELD, credentials.getLogin());
		enterTextIntoInputField(LenndyConstants.PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(LenndyConstants.LOGIN_BUTTON);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get(LenndyConstants.DASHBOARD_PAGE_URL);
		final Map<String, AccountValue> accountAttributes = getAll(
				find().withXPath(LenndyConstants.XPATH_OVERVIEW_ROWS)) //
						.stream() //
						.limit(3) //
						.map(WebElement::getText) //
						.map(AccountAttributeParsing::parseAccountAttributePair).collect(Collectors
								.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue, (a, b) -> a));
		return LenndyAccountOverview.ofAccountAttributeMap(accountAttributes).toAccountStatus();
	}

}
