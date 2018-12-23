package pw.erler.peerbuddy.common.selenium_util.error;

public class WebElementNotFoundException extends SeleniumException {

	private static final long serialVersionUID = -406654597965111017L;

	public WebElementNotFoundException() {
	}

	public WebElementNotFoundException(final String message) {
		super(message);
	}

	public WebElementNotFoundException(final Throwable cause) {
		super(cause);
	}

	public WebElementNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WebElementNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
