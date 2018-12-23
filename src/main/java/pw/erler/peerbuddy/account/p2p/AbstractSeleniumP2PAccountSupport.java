package pw.erler.peerbuddy.account.p2p;

import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import pw.erler.peerbuddy.account.AbstractSeleniumAccountSupport;
import pw.erler.peerbuddy.account.AccountFeature;
import pw.erler.peerbuddy.account.BasicAccountStatus;

public abstract class AbstractSeleniumP2PAccountSupport extends AbstractSeleniumAccountSupport {

	protected abstract P2PAccountStatus retrieveP2PAccountStatus();

	public AbstractSeleniumP2PAccountSupport(final WebDriver webDriver, final AccountFeature... accountFeatures) {
		super(webDriver,
				Lists.newArrayList(Iterables.concat(Collections.singleton(AccountFeature.RETRIEVE_ACCOUNT_STATUS),
						Arrays.asList(accountFeatures))));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasicAccountStatus> T retrieveAccountStatus(final Class<T> accountStatusClass) {
		if (accountStatusClass == BasicAccountStatus.class || accountStatusClass == P2PAccountStatus.class) {
			return (T) retrieveP2PAccountStatus();
		}
		throw new UnsupportedOperationException("Unsupported account status type " + accountStatusClass.getName());
	}

}
