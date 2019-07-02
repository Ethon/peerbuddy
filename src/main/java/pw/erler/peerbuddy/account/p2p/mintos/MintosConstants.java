package pw.erler.peerbuddy.account.p2p.mintos;

import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;

@UtilityClass
class MintosConstants {

	static final String OVERVIEW_PAGE_URL = "https://www.mintos.com/en/overview/";
	static final String LOGIN_PAGE_URL = "https://www.mintos.com/en/login";
	static final String ACCOUNT_STATEMENT_PAGE_URL = "https://www.mintos.com/en/account-statement/";
	static final String ACCOUNT_STATEMENT_DOWNLOAD_URL = "https://www.mintos.com/en/account-statement/list.xlsx";

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
			.finder(finder -> finder.withTagName("button").withText("Log In").isDisplayed(true)) //
			.build();

	static final WebElementDescription ACCOUNT_STATEMENT_PERIOD_FROM_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Period From") //
			.finder(finder -> finder.withId("period-from").isDisplayed(true)) //
			.build();

	static final WebElementDescription ACCOUNT_STATEMENT_PERIOD_TO_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Period To") //
			.finder(finder -> finder.withId("period-to").isDisplayed(true)) //
			.build();

	static final WebElementDescription ACCOUNT_STATEMENT_FILTER_BUTTON = WebElementDescription //
			.button() //
			.description("Filter Button") //
			.finder(finder -> finder.withId("filter-button").isDisplayed(true)) //
			.build();

}
