package pw.erler.peerbuddy.account.savings.addiko;

import java.util.Collections;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.AbstractBasicSeleniumAccountSupport;
import pw.erler.peerbuddy.account.AccountFeature;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.credentials.CredentialsException;
import pw.erler.peerbuddy.common.values.MonetaryValue;

public final class AddikoSeleniumAccountSupport extends AbstractBasicSeleniumAccountSupport {

	@Override
	protected BasicAccountStatus retrieveBasicAccountStatus() {
		return new BasicAccountStatus(accountConfig.getTitle(),
				MonetaryValue.valueOf(getLabelText(AddikoConstants.ACCOUNT_VALUE_LABEL) + " EUR"));
	}

	public AddikoSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig) {
		super(webDriver, accountConfig, Collections.singleton(AccountFeature.RETRIEVE_ACCOUNT_STATUS));
	}

	@Override
	public void login(final Credentials credentials) throws CredentialsException {
		getPageAndAwaitLoad("Main page", AddikoConstants.MAIN_PAGE_URL);
		recordWindows();
		clickButton(AddikoConstants.ONLINE_BANKING_BUTTON);
		switchToNewWindow();
		awaitPageLoad("Login page or overview page", AddikoConstants.LOGIN_PAGE_URL, AddikoConstants.OVERVIEW_PAGE_URL);
		if (!isPageActive(AddikoConstants.OVERVIEW_PAGE_URL)) {
			enterTextIntoInputField(AddikoConstants.USERID_INPUT_FIELD, credentials.getLogin());
			enterTextIntoInputField(AddikoConstants.USERNAME_INPUT_FIELD,
					credentials.getStringProperty("Verfügername"));
			enterTextIntoInputField(AddikoConstants.PASSWORD_INPUT_FIELD, credentials.getPassword());
			clickButton(AddikoConstants.LOGIN_BUTTON);
		}
	}

}
