package pw.erler.peerbuddy.account.p2p;

import org.openqa.selenium.WebDriver;

import pw.erler.peerbuddy.account.AbstractSeleniumAccountSupport;
import pw.erler.peerbuddy.account.BasicAccountStatus;

public abstract class AbstractSeleniumP2PAccountSupport extends AbstractSeleniumAccountSupport {

	protected abstract P2PAccountStatus retrieveP2PAccountStatus();

	public AbstractSeleniumP2PAccountSupport(final WebDriver webDriver) {
		super(webDriver);
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
