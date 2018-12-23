package pw.erler.peerbuddy.account.p2p.estateguru;

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

public class EstateGuruSeleniumAccountSupport extends AbstractSeleniumP2PAccountSupport {

	public EstateGuruSeleniumAccountSupport(final WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public void login(final Credentials credentials) {
		get("https://estateguru.co/portal/login/auth");
		sendKeys(finder -> finder.withId("username").isDisplayed(true), 0, credentials.getLogin());
		sendKeys(finder -> finder.withId("password").isDisplayed(true), 0, credentials.getPassword());
		click(finder -> finder.withXPath("//form[@id='loginForm']//button").isDisplayed(true), 0);
	}

	@Override
	protected P2PAccountStatus retrieveP2PAccountStatus() {
		get("https://estateguru.co/portal/portfolio/overview");
		final List<WebElement> all = getAll(find().withXPath(
				"//section[@class='section'][1]//div[@class='col-12 col-lg-4'][3]//ul[@class='details-list']/li"));
		final Map<String, AccountValue> attributes = all //
				.stream() //
				.map(WebElement::getText) //
				.filter(s -> !s.isEmpty()) //
				.map(AccountAttributeParsing::parseAccountAttributePair) //
				.collect(Collectors.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue));

		final MonetaryValue totalAccountValue = (MonetaryValue) attributes.get("Total Account Value");
		final MonetaryValue invested = ((MonetaryValue) attributes.get("Invested")).negate();
		final MonetaryValue reserved = ((MonetaryValue) attributes.get("Reserved")).negate();
		final MonetaryValue availableAmount = (MonetaryValue) attributes.get("Available Amount");
		return new P2PAccountStatus(totalAccountValue, invested.add(reserved), availableAmount);
	}

}
