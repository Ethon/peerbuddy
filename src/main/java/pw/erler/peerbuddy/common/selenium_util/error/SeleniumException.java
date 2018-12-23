package pw.erler.peerbuddy.common.selenium_util.error;

public class SeleniumException extends RuntimeException {

	private static final long serialVersionUID = 996864535111516762L;

	public SeleniumException() {
		super();
	}

	public SeleniumException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SeleniumException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SeleniumException(final String message) {
		super(message);
	}

	public SeleniumException(final Throwable cause) {
		super(cause);
	}

}
