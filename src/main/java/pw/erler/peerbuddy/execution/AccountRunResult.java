package pw.erler.peerbuddy.execution;

import java.util.Optional;

import lombok.Value;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;

@Value
public class AccountRunResult {

	private final AccountConfig accountConfig;
	private final Optional<BasicAccountStatus> status;
	private final Optional<Exception> exception;

	public AccountRunResult(AccountConfig accountConfig, final BasicAccountStatus status) {
		this.accountConfig = accountConfig;
		this.status = Optional.of(status);
		this.exception = Optional.empty();
	}

	public AccountRunResult(AccountConfig accountConfig, final Exception exception) {
		this.accountConfig = accountConfig;
		this.status = Optional.empty();
		this.exception = Optional.of(exception);
	}

}
