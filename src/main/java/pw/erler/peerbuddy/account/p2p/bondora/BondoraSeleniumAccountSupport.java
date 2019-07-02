package pw.erler.peerbuddy.account.p2p.bondora;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;
import pw.erler.peerbuddy.common.values.AccountAttributePair;
import pw.erler.peerbuddy.common.values.AccountAttributeParsing;
import pw.erler.peerbuddy.common.values.AccountValue;
import pw.erler.peerbuddy.common.values.AttributeException;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Log4j2
public class BondoraSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private static final String GOGROW_PAGE = "https://www.bondora.com/en/gogrow";
	private static final String DASHBOARD_PAGE = "https://www.bondora.com/en/dashboard";
	private static final String LOGIN_PAGE = "https://www.bondora.com/en/login";

	private static final WebElementDescription EMAIL_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Email") //
			.finder(finder -> finder.withName("Email").isDisplayed(true)) //
			.build();

	private static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Password") //
			.finder(finder -> finder.withName("Password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	private static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withXPath("//div[@class='container container-register-login login']//button")
					.isDisplayed(true)) //
			.build();

	private static final WebElementDescription MORE_STATS_BUTTON = WebElementDescription //
			.button() //
			.description("More stats") //
			.finder(finder -> finder.withXPath("//div[@class='dashboard__overview-numbers']//button[@class='btn']")) //
			.build();

	public BondoraSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig) {
		super(webDriver, accountConfig);
	}

	@Override
	public void login(final Credentials credentials) {
		get(LOGIN_PAGE);
		enterTextIntoInputField(EMAIL_INPUT_FIELD, credentials.getLogin());
		enterTextIntoInputField(PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(LOGIN_BUTTON);
		awaitPageLoad("Dashboard or Go&Grow", DASHBOARD_PAGE, GOGROW_PAGE);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get(DASHBOARD_PAGE);
		clickButton(MORE_STATS_BUTTON);
		awaitAndGet(finder -> finder.withXPath("//div[@id='statNumbers']//tbody/tr"), 1);
		final Map<String, AccountValue> stats = getAll(find().withXPath("//div[@id='statNumbers']//tbody/tr")) //
				.stream() //
				.map(WebElement::getText)//
				.map(AccountAttributeParsing::parseAccountAttributePair) //
				.collect(Collectors.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue, (a, b) -> a));

		final MonetaryValue accountValue = (MonetaryValue) stats.get("Account value");
		if (accountValue == null) {
			throw new AttributeException("Account value attribute not found");
		}
		log.debug(String.format("Account value = %s", accountValue));

		final MonetaryValue availableFunds = (MonetaryValue) stats.get("Available funds");
		if (availableFunds == null) {
			throw new AttributeException("Available funds attribute not found");
		}
		log.debug(String.format("Available funds = %s", availableFunds));

		// The Go&Grow account has no Go&Grow attribute as it is implicit.
		final MonetaryValue goAndGrow = (MonetaryValue) stats.get("Go & Grow");
		if (goAndGrow == null) {
			log.debug("Account is a Go&Grow account");
			return new P2PAccountStatus(accountConfig.getTitle(), accountValue, accountValue, accountValue);
		} else {
			log.debug(String.format("Go&Grow = %s", goAndGrow));
			return new P2PAccountStatus(accountConfig.getTitle(), accountValue, goAndGrow,
					goAndGrow.add(availableFunds));
		}

	}

}
