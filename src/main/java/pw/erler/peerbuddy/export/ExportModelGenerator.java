package pw.erler.peerbuddy.export;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;
import pw.erler.peerbuddy.execution.AccountRunResult;

public class ExportModelGenerator {

	public AccountStatusOverviewModel createModel(final PeerBuddyConfig config,
			final Map<AccountConfig, AccountRunResult> accountStatus) {
		final AccountStatusOverviewModel model = new AccountStatusOverviewModel();
		model.setDateTime(ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ISO_DATE_TIME));
		config.getAccounts().stream().filter(AccountConfig::isEnabled).forEach(account -> {
			final AccountRunResult result = accountStatus.get(account);
			result.getStatus().ifPresent(status -> model.getAccounts().add(new AccountStatusModel(account.getTitle(),
					status.getAccountBalance(), status.getInvestedFunds(), status.getAvailableFunds(), null)));
			result.getException().ifPresent(e -> model.getAccounts()
					.add(new AccountStatusModel(account.getTitle(), null, null, null, e.getMessage())));

		});
		return model;
	}

}
