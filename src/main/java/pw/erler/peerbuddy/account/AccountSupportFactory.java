package pw.erler.peerbuddy.account;

import static com.google.common.base.Preconditions.checkNotNull;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.p2p.bondora.BondoraSeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.estateguru.EstateGuruSeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.lendy.LendySeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.lenndy.LenndySeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.mintos.MintosSeleniumAccountSupport;
import pw.erler.peerbuddy.account.payment_service_providers.paypal.PaypalSeleniumAccountSupport;
import pw.erler.peerbuddy.account.savings.addiko.AddikoSeleniumAccountSupport;
import pw.erler.peerbuddy.common.config.AccountConfig;

public final class AccountSupportFactory {

	private static final String ESTATEGURU = "estateguru.co";
	private static final String LENNDY = "lenndy.com";
	private static final String MINTOS = "mintos.com";
	private static final String LENDY = "lendy.co.uk";
	private static final String BONDORA = "bondora.com";

	private static final String PAYPAL = "paypal.com";
	private static final String ADDIKO = "addiko.at";

	private static void checkConfig(final AccountConfig config) {
		checkNotNull(config, "accountConfig is missing in config");
		checkNotNull(config.getType(), "accountConfig.type is missing in config");
	}

	public static AccountSupport createAccountSupport(final WebDriver driver, final AccountConfig config) {
		checkNotNull(driver);
		checkConfig(config);
		switch (config.getType().toLowerCase()) {

		case MINTOS:
			return new MintosSeleniumAccountSupport(driver, config);
		case LENNDY:
			return new LenndySeleniumAccountSupport(driver, config);
		case ESTATEGURU:
			return new EstateGuruSeleniumAccountSupport(driver, config);
		case LENDY:
			return new LendySeleniumAccountSupport(driver, config);
		case BONDORA:
			return new BondoraSeleniumAccountSupport(driver, config);

		case PAYPAL:
			return new PaypalSeleniumAccountSupport(driver, config);
		case ADDIKO:
			return new AddikoSeleniumAccountSupport(driver, config);

		default:
			throw new UnsupportedOperationException("Unsupported account type '" + config.getType() + "'");
		}
	}

	public static boolean requiresRealBrowser(final AccountConfig config) {
		checkConfig(config);
		return PAYPAL.equalsIgnoreCase(config.getType());
	}

}
