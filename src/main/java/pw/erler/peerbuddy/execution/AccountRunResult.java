package pw.erler.peerbuddy.execution;

import java.util.Optional;

import lombok.Value;
import pw.erler.peerbuddy.account.BasicAccountStatus;

@Value
public class AccountRunResult {

	private final Optional<BasicAccountStatus> status;
	private final Optional<Exception> exception;

	public AccountRunResult(final BasicAccountStatus status) {
		this.status = Optional.of(status);
		this.exception = Optional.empty();
	}

	public AccountRunResult(final Exception exception) {
		this.status = Optional.empty();
		this.exception = Optional.of(exception);
	}

}
