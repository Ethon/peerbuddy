package pw.erler.peerbuddy.account.p2p.mintos;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pw.erler.peerbuddy.account.p2p.AbstractSeleniumP2PAccountSupport;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.account.transactions.Transaction;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.common.values.ValueParsing;

public final class MintosSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	private Map<String, MonetaryValue> parseTable(final List<WebElement> elements) {
		final Map<String, MonetaryValue> result = new TreeMap<>();
		if (elements.size() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < elements.size(); i += 2) {
			final String k = elements.get(i).getText().trim();
			final MonetaryValue v = ValueParsing.parseValue(elements.get(i + 1).getText()).monetaryValue(); // parseMonetaryAmount(elements.get(i
																											// +
																											// 1).getText().trim());
			result.put(k, v);
		}
		return result;
	}

	public MintosSeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get(MintosConstants.LOGIN_PAGE_URL);
		enterTextIntoInputField(MintosConstants.USERNAME_INPUT_FIELD, credentials.getLogin());
		enterTextIntoInputField(MintosConstants.PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(MintosConstants.LOGIN_BUTTON);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get(MintosConstants.OVERVIEW_PAGE_URL);
		final Map<String, MonetaryValue> overview = parseTable(
				getAll(find().withXPath("//li[@class='overview-box'][1]//tr/td")));
		return new P2PAccountStatus(overview.get("Total"), overview.get("Invested Funds"),
				overview.get("Available Funds"));
	}

	@Override
	public List<Transaction> retrieveTransactions(final LocalDate fromInclusive, final LocalDate toInclusive) {
		get(MintosConstants.OVERVIEW_PAGE_URL);
		clearInputField(MintosConstants.ACCOUNT_STATEMENT_PERIOD_FROM_INPUT_FIELD);
		enterTextIntoInputField(MintosConstants.ACCOUNT_STATEMENT_PERIOD_FROM_INPUT_FIELD, "");
		return Collections.emptyList();
	}

}
