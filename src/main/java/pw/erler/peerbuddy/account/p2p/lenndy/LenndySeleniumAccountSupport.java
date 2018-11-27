package pw.erler.peerbuddy.account.p2p.lenndy;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.values.MonetaryValue;

public class LenndySeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private MonetaryValue parseMonetaryAmount(final String text) {
		return new MonetaryValue(new BigDecimal(text.replaceAll("€|\\s", "")), Currency.getInstance("EUR"));
	}

	public LenndySeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get("https://system.lenndy.com/login");
		sendKeys(finder -> finder.withName("_username").isDisplayed(true), 0, credentials.getLogin());
		sendKeys(finder -> finder.withName("_password").isDisplayed(true), 0, credentials.getPassword());
		click(finder -> finder.withXPath("//input[@class='buttons button-login']").isDisplayed(true), 0);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get("https://system.lenndy.com/dashboard");
		final List<String> elements = getAll(
				find().withXPath("//div[@class='col-xs-12 col-md-4 text-left overview-margin'][1]//div[@class='row']"))
						.stream().limit(5).map(WebElement::getText).collect(Collectors.toList());
		final MonetaryValue accountBalance = parseMonetaryAmount(elements.get(0).replaceAll("[a-zA-Z]+", ""));
		final MonetaryValue investedAmount = parseMonetaryAmount(elements.get(1).replaceAll("[a-zA-Z]+", ""));
		final MonetaryValue availableFunds = parseMonetaryAmount(elements.get(2).replaceAll("[a-zA-Z]+", ""));
		return new P2PAccountStatus(accountBalance, investedAmount, availableFunds);
	}

}
