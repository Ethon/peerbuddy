package pw.erler.peerbuddy.common.credentials;

public class CredentialsException extends Exception {

	private static final long serialVersionUID = 7173380129215124346L;

	public CredentialsException() {
	}

	public CredentialsException(final String message) {
		super(message);
	}

	public CredentialsException(final Throwable cause) {
		super(cause);
	}

	public CredentialsException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CredentialsException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
