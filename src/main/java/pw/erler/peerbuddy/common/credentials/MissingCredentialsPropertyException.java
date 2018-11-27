package pw.erler.peerbuddy.common.credentials;

public class MissingCredentialsPropertyException extends Exception {

	private static final long serialVersionUID = 7173380129215124346L;

	public MissingCredentialsPropertyException() {
	}

	public MissingCredentialsPropertyException(final String message) {
		super(message);
	}

	public MissingCredentialsPropertyException(final Throwable cause) {
		super(cause);
	}

	public MissingCredentialsPropertyException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MissingCredentialsPropertyException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
