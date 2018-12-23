package pw.erler.peerbuddy.common.selenium_util.error;

public class PageLoadException extends SeleniumException {

	private static final long serialVersionUID = -1292949608069356519L;

	public PageLoadException() {
		super();
	}

	public PageLoadException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PageLoadException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PageLoadException(final String message) {
		super(message);
	}

	public PageLoadException(final Throwable cause) {
		super(cause);
	}

}
