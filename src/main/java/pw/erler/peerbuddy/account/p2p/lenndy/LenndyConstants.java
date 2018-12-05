package pw.erler.peerbuddy.account.p2p.lenndy;

import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;

@UtilityClass
class LenndyConstants {

	static final String DASHBOARD_PAGE_URL = "https://system.lenndy.com/dashboard";
	static final String LOGIN_PAGE_URL = "https://system.lenndy.com/login";

	static final String XPATH_OVERVIEW_ROWS = "//div[@class='col-xs-12 col-md-4 text-left overview-margin'][1]//div[@class='row']";
	static final int ROWINDEX_AVAILABLE_FUNDS = 3;
	static final int ROWINDEX_INVESTED_AMOUNT = 1;
	static final int ROWINDEX_ACCOUNT_BALANCE = 0;

	static final String ATTRIBUTE_KEY_AVAILABLE_FUNDS = "Available Funds";
	static final String ATTRIBUTE_KEY_ACCOUNT_BALANCE = "Account Balance";
	static final String ATTRIBUTE_KEY_INVESTED_AMOUNT = "Currently invested amount";

	static final WebElementDescription USERNAME_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Username") //
			.finder(finder -> finder.withName("_username").isDisplayed(true)) //
			.build();

	static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Password") //
			.finder(finder -> finder.withName("_password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withXPath("//input[@class='buttons button-login']").isDisplayed(true)) //
			.build();

}
