package pw.erler.peerbuddy.account.payment_service_providers.paypal;

import java.util.Collections;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.AbstractBasicSeleniumAccountSupport;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;

public class PaypalSeleniumAccountSupport extends AbstractBasicSeleniumAccountSupport {

	public PaypalSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig) {
		super(webDriver, accountConfig, Collections.emptyList());
	}

	@Override
	public void login(final Credentials credentials) {
		get(PaypalConstants.LOGIN_PAGE_URL);
		enterTextIntoInputField(PaypalConstants.USERNAME_INPUT_FIELD, credentials.getLogin());
		clickButton(PaypalConstants.NEXT_BUTTON);
		enterTextIntoInputField(PaypalConstants.PASSWORD_INPUT_FIELD, credentials.getPassword());
		clickButton(PaypalConstants.LOGIN_BUTTON);
		awaitPageLoad("Account summary page or Clickthru page or Home page", PaypalConstants.SUMMARY_PAGE_URL,
				PaypalConstants.CLICKTHRU_PAGE_URL, PaypalConstants.HOME_PAGE_URL);
	}

	@Override
	protected BasicAccountStatus retrieveBasicAccountStatus() {
		return doRetrieveBasicAccountStatus(accountConfig.getTitle(), PaypalConstants.SUMMARY_PAGE_URL,
				PaypalConstants.VALUE_LABEL);
	}

}
