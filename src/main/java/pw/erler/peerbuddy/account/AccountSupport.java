package pw.erler.peerbuddy.account;

import java.time.LocalDate;
import java.util.List;

import pw.erler.peerbuddy.account.transactions.Transaction;
import pw.erler.peerbuddy.common.credentials.Credentials;

public interface AccountSupport {

	public void login(Credentials credentials);

	public <T extends BasicAccountStatus> T retrieveAccountStatus(Class<T> accountStatusClass);

	public List<Transaction> retrieveTransactions(LocalDate fromInclusive, LocalDate toInclusive);

	public default List<Transaction> retrieveTransactions() {
		return retrieveTransactions(LocalDate.of(2000, 1, 1), LocalDate.now());
	}

	public boolean supportsFeature(final AccountFeature feature);

}
