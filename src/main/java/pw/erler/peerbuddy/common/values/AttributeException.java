package pw.erler.peerbuddy.common.values;

public class AttributeException extends RuntimeException {

	private static final long serialVersionUID = -4389797252544630140L;

	public AttributeException() {
	}

	public AttributeException(final String message) {
		super(message);
	}

	public AttributeException(final Throwable cause) {
		super(cause);
	}

	public AttributeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AttributeException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
