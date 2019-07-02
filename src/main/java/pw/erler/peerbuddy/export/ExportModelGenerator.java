package pw.erler.peerbuddy.export;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import pw.erler.peerbuddy.account.AccountStatusVisitor;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;
import pw.erler.peerbuddy.common.values.MonetaryValue;
import pw.erler.peerbuddy.execution.AccountRunResult;

public class ExportModelGenerator {

	private static final class StatusVisitor implements AccountStatusVisitor<AccountStatusModel> {

		private final AccountConfig account;

		public StatusVisitor(final AccountConfig account) {
			this.account = account;
		}

		@Override
		public AccountStatusModel visit(final BasicAccountStatus status) {
			return new AccountStatusModel(account.getTitle(), status.getAccountBalance(),
					new MonetaryValue(status.getAccountBalance().getCurrency()), status.getAccountBalance(), null);
		}

		@Override
		public AccountStatusModel visit(final P2PAccountStatus status) {
			return new AccountStatusModel(account.getTitle(), status.getAccountBalance(), status.getInvestedFunds(),
					status.getAvailableFunds(), null);
		}

	}

	public AccountStatusOverviewModel createModel(final PeerBuddyConfig config,
			final Map<AccountConfig, List<AccountRunResult>> accountStatus) {
		final AccountStatusOverviewModel model = new AccountStatusOverviewModel();
		model.setDateTime(ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ISO_DATE_TIME));
		config.getAccounts().stream().filter(AccountConfig::isEnabled).forEach(account -> {
			accountStatus.get(account).forEach(result -> {
				result.getStatus()
						.ifPresent(status -> model.getAccounts().add(status.accept(new StatusVisitor(account))));
				result.getException().ifPresent(e -> model.getAccounts()
						.add(new AccountStatusModel(account.getTitle(), null, null, null, e.getMessage())));
			});
		});
		return model;
	}

}
