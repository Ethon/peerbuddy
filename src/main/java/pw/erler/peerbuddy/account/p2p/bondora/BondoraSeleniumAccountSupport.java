package pw.erler.peerbuddy.account.p2p.bondora;

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
import pw.erler.peerbuddy.common.values.MonetaryValue;

public class BondoraSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private static final String DASHBOARD_PAGE = "https://www.bondora.com/en/dashboard";
	private static final String LOGIN_PAGE = "https://www.bondora.com/en/login";

	public BondoraSeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get(LOGIN_PAGE);
		sendKeys(finder -> finder.withName("Email").isDisplayed(true), 0, credentials.getLogin());
		sendKeys(finder -> finder.withName("Password").isDisplayed(true), 0, credentials.getPassword());
		click(finder -> finder.withXPath("//div[@class='container container-register-login login']//button")
				.isDisplayed(true), 0);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get(DASHBOARD_PAGE);
		click(finder -> finder.withXPath("//div[@class='dashboard__overview-numbers']//button[@class='btn']"));
		awaitAndGet(finder -> finder.withXPath("//div[@id='statNumbers']//tbody/tr"), 1);
		final Map<String, AccountValue> stats = getAll(find().withXPath("//div[@id='statNumbers']//tbody/tr")) //
				.stream() //
				.map(WebElement::getText)//
				.map(AccountAttributeParsing::parseAccountAttributePair) //
				.collect(Collectors.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue, (a, b) -> a));
		return new P2PAccountStatus((MonetaryValue) stats.get("Account value"), (MonetaryValue) stats.get("Go & Grow"),
				((MonetaryValue) stats.get("Go & Grow")).add((MonetaryValue) stats.get("Available funds")));

	}

}
