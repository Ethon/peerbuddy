package pw.erler.peerbuddy.account.payment_service_providers.paypal;

import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;

@UtilityClass
class PaypalConstants {

	static final String LOGIN_PAGE_URL = "https://www.paypal.com/en/signin";
	static final String SUMMARY_PAGE_URL = "https://www.paypal.com/myaccount/summary/";
	static final String CLICKTHRU_PAGE_URL = "https://www.paypal.com/at/webapps/mpp/clickthru/";
	static final String HOME_PAGE_URL = "https://www.paypal.com/myaccount/home";

	static final WebElementDescription USERNAME_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Username") //
			.finder(finder -> finder.withId("email").isDisplayed(true)) //
			.build();

	static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Password") //
			.finder(finder -> finder.withId("password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withId("btnLogin").isDisplayed(true)) //
			.build();

	static final WebElementDescription NEXT_BUTTON = WebElementDescription //
			.button() //
			.description("Next") //
			.finder(finder -> finder.withId("btnNext").isDisplayed(true)) //
			.build();

	static final WebElementDescription VALUE_LABEL = WebElementDescription //
			.label() //
			.description("Balance")
			.finder(finder -> finder.withXPath("//div[@class='cw_tile-currencyContainer']/span[1]").isDisplayed(true)) //
			.build();

}
