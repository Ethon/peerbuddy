package pw.erler.peerbuddy.account.p2p.mintos;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.values.MonetaryValue;

public final class MintosSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private MonetaryValue parseMonetaryAmount(final String text) {
		return new MonetaryValue(new BigDecimal(text.replaceAll("€|\\s", "")), Currency.getInstance("EUR"));
	}

	private Map<String, MonetaryValue> parseTable(final List<WebElement> elements) {
		final Map<String, MonetaryValue> result = new TreeMap<>();
		if (elements.size() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < elements.size(); i += 2) {
			final String k = elements.get(i).getText().trim();
			final MonetaryValue v = parseMonetaryAmount(elements.get(i + 1).getText().trim());
			result.put(k, v);
		}
		return result;
	}

	public MintosSeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get("https://www.mintos.com/en/");
		click(finder -> finder.withTagName("button").withText("Log In").isDisplayed(true), 0);
		sendKeys(finder -> finder.withName("_username").isDisplayed(true), 0, credentials.getLogin());
		sendKeys(finder -> finder.withName("_password").isDisplayed(true), 0, credentials.getPassword());
		click(finder -> finder.withTagName("button").withText("Log In").isDisplayed(true), 1);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get("https://www.mintos.com/en/overview/");
		final Map<String, MonetaryValue> overview = parseTable(
				getAll(find().withXPath("//li[@class='overview-box'][1]//tr/td")));
		return new P2PAccountStatus(overview.get("Total"), overview.get("Invested Funds"),
				overview.get("Available Funds"));
	}

}
