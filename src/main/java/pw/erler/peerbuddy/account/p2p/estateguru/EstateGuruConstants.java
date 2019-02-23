package pw.erler.peerbuddy.account.p2p.estateguru;

import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;

@UtilityClass
class EstateGuruConstants {

	static final String PORTFOLIO_OVERVIEW_PAGE_URL = "https://estateguru.co/portal/portfolio/overview";
	static final String LOGIN_PAGE_URL = "https://estateguru.co/?logIn=true";
	static final String XPATH_OVERVIEW_ROWS = "//div[@class='col-12 col-lg-4'][3]//ul[@class='details-list']/li";

	static final int ROWINDEX_AVAILABLE_FUNDS = 3;
	static final int ROWINDEX_INVESTED_AMOUNT = 1;
	static final int ROWINDEX_ACCOUNT_BALANCE = 0;

	static final String ATTRIBUTE_KEY_AVAILABLE_FUNDS = "Available Funds";
	static final String ATTRIBUTE_KEY_ACCOUNT_BALANCE = "Account Balance";
	static final String ATTRIBUTE_KEY_INVESTED_AMOUNT = "Currently invested amount";

	static final WebElementDescription USERNAME_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Username") //
			.finder(finder -> finder.withName("username").isDisplayed(true)) //
			.build();

	static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Password") //
			.finder(finder -> finder.withName("password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withXPath("//form[@id='loginForm']//button").isDisplayed(true)) //
			.build();

}
