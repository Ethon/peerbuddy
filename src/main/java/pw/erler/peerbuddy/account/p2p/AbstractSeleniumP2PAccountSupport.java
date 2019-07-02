package pw.erler.peerbuddy.account.p2p;

import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import pw.erler.peerbuddy.account.AbstractSeleniumAccountSupport;
import pw.erler.peerbuddy.account.AccountFeature;
import pw.erler.peerbuddy.account.AccountStatusVisitor;
import pw.erler.peerbuddy.common.config.AccountConfig;

public abstract class AbstractSeleniumP2PAccountSupport extends AbstractSeleniumAccountSupport {

	protected abstract P2PAccountStatus retrieveP2PAccountStatus();

	public AbstractSeleniumP2PAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig,
			final AccountFeature... accountFeatures) {
		super(webDriver, accountConfig,
				Lists.newArrayList(Iterables.concat(Collections.singleton(AccountFeature.RETRIEVE_ACCOUNT_STATUS),
						Arrays.asList(accountFeatures))));
	}

	@Override
	public <T> T retrieveAccountStatus(final AccountStatusVisitor<T> visitor) {
		return retrieveP2PAccountStatus().accept(visitor);
	}

}
