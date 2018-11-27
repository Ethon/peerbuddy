package pw.erler.peerbuddy.account.p2p.lendy;

import java.util.List;
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

public class LendySeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	public LendySeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get("https://lendy.co.uk/login");
		sendKeys(finder -> finder.withName("email").isDisplayed(true), 0, credentials.getLogin());
		sendKeys(finder -> finder.withName("password").isDisplayed(true), 0, credentials.getPassword());
		click(finder -> finder.withXPath("//form[@id='login']//button").isDisplayed(true), 0);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get("https://lendy.co.uk/loans/available");
		final List<WebElement> all = getAll(find().withXPath("//div[@class='c-account-overview__inner']//article"));
		final Map<String, AccountValue> attributes = all //
				.stream() //
				.map(WebElement::getText) //
				.map(AccountAttributeParsing::parseAccountAttributePair) //
				.collect(Collectors.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue));
		return new P2PAccountStatus((MonetaryValue) attributes.get("BALANCE"),
				(MonetaryValue) attributes.get("LIVE LOAN PARTS"), (MonetaryValue) attributes.get("AVAILABLE FUNDS"));
	}

}
