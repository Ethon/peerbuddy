package pw.erler.peerbuddy.account.p2p.lendy;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;
import pw.erler.peerbuddy.common.values.AccountValue;
import pw.erler.peerbuddy.common.values.MonetaryValue;

public class LendySeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private static final String LOANS_PAGE = "https://lendy.co.uk/loans/available";
	private static final String LOGIN_PAGE = "https://lendy.co.uk/login";

	private static final WebElementDescription EMAIL_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Email") //
			.finder(finder -> finder.withName("email").isDisplayed(true)) //
			.build();

	private static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Password") //
			.finder(finder -> finder.withName("password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	private static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withXPath("//form[@id='login']//button").isDisplayed(true)) //
			.build();

	public LendySeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get(LOGIN_PAGE);
		enterTextIntoInputField(EMAIL_INPUT_FIELD, credentials.getLogin());
		enterTextIntoInputField(PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(LOGIN_BUTTON);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get(LOANS_PAGE);
		final Map<String, AccountValue> attributes = getAttributes(
				find().withXPath("//div[@class='c-account-overview__inner']//article"));
		return new P2PAccountStatus((MonetaryValue) attributes.get("BALANCE"),
				(MonetaryValue) attributes.get("LIVE LOAN PARTS"), (MonetaryValue) attributes.get("AVAILABLE FUNDS"));
	}

}
