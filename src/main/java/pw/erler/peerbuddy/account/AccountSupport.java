package pw.erler.peerbuddy.account;

import pw.erler.peerbuddy.common.credentials.Credentials;

public interface AccountSupport {

	public void login(Credentials credentials);

	public <T extends BasicAccountStatus> T retrieveAccountStatus(Class<T> accountStatusClass);

}
