package pw.erler.peerbuddy.account;

import static com.google.common.base.Preconditions.checkNotNull;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.p2p.bondora.BondoraSeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.estateguru.EstateGuruSeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.lendy.LendySeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.lenndy.LenndySeleniumAccountSupport;
import pw.erler.peerbuddy.account.p2p.mintos.MintosSeleniumAccountSupport;
import pw.erler.peerbuddy.common.config.AccountConfig;

public final class AccountSupportFactory {

	private static final String ESTATEGURU = "estateguru.co";
	private static final String LENNDY = "lenndy.com";
	private static final String MINTOS = "mintos.com";
	private static final String LENDY = "lendy.co.uk";
	private static final String BONDORA = "bondora.com";

	public static AccountSupport createAccountSupport(final WebDriver driver, final AccountConfig config) {
		checkNotNull(driver);
		checkNotNull(config, "accountConfig is missing in config");
		checkNotNull(config.getType(), "accountConfig.type is missing in config");
		switch (config.getType().toLowerCase()) {
		case MINTOS:
			return new MintosSeleniumAccountSupport(driver);
		case LENNDY:
			return new LenndySeleniumAccountSupport(driver);
		case ESTATEGURU:
			return new EstateGuruSeleniumAccountSupport(driver);
		case LENDY:
			return new LendySeleniumAccountSupport(driver);
		case BONDORA:
			return new BondoraSeleniumAccountSupport(driver);
		default:
			throw new UnsupportedOperationException("Unsupported account type '" + config.getType() + "'");
		}
	}

}
