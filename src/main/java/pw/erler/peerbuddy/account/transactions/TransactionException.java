package pw.erler.peerbuddy.account.transactions;

public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = -8581415688405268766L;

	public TransactionException() {
	}

	public TransactionException(final String message) {
		super(message);
	}

	public TransactionException(final Throwable cause) {
		super(cause);
	}

	public TransactionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TransactionException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
