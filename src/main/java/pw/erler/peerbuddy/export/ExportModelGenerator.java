package pw.erler.peerbuddy.export;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;

public class ExportModelGenerator {

	public AccountStatusOverviewModel createModel(final PeerBuddyConfig config,
			final Map<AccountConfig, P2PAccountStatus> accountStatus) {
		final AccountStatusOverviewModel model = new AccountStatusOverviewModel();
		model.setDateTime(ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ISO_DATE_TIME));
		config.getAccounts().stream().filter(AccountConfig::isEnabled).forEach(account -> {
			final P2PAccountStatus status = accountStatus.get(account);
			model.getAccounts().add(new AccountStatusModel(account.getTitle(), status.getAccountBalance(),
					status.getInvestedFunds(), status.getAvailableFunds()));
		});
		return model;
	}

}
