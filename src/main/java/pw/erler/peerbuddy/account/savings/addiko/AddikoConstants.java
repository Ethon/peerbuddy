package pw.erler.peerbuddy.account.savings.addiko;

import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;

@UtilityClass
class AddikoConstants {

	static final String MAIN_PAGE_URL = "https://www.addiko.at/";
	static final String OVERVIEW_PAGE_URL = "https://onlinebanking.addiko.at/banking/finanzuebersicht";
	static final String LOGIN_PAGE_URL = "https://onlinebanking.addiko.at/banking/login.xhtml";

	static final WebElementDescription ONLINE_BANKING_BUTTON = WebElementDescription //
			.button() //
			.description("Online Banking") //
			.finder(finder -> finder.withId("menu-item-254").isDisplayed(true)) //
			.build();

	static final WebElementDescription USERID_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Verfügernummer") //
			.finder(finder -> finder.withId("loginform:userId").isDisplayed(true)) //
			.build();

	static final WebElementDescription USERNAME_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("Verfügernummer") //
			.finder(finder -> finder.withId("loginform:userName").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	static final WebElementDescription PASSWORD_INPUT_FIELD = WebElementDescription //
			.inputField() //
			.description("PIN") //
			.finder(finder -> finder.withId("loginform:password").isDisplayed(true)) //
			.isPasswordField(true) //
			.build();

	static final WebElementDescription LOGIN_BUTTON = WebElementDescription //
			.button() //
			.description("Login") //
			.finder(finder -> finder.withId("loginform:loginButtonLegacy").isDisplayed(true)) //
			.build();

	static final WebElementDescription ACCOUNT_VALUE_LABEL = WebElementDescription //
			.label() //
			.description("Gesamtvermögen Label") //
			.finder(finder -> finder.withXPath("//div[@id='finanzueberblick:gesamt']//span[@class='value']")
					.isDisplayed(true)) //
			.build();

}
