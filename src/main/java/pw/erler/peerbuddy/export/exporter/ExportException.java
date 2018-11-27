package pw.erler.peerbuddy.export.exporter;

public class ExportException extends Exception {

	private static final long serialVersionUID = -5802943486135024599L;

	public ExportException() {
	}

	public ExportException(final String message) {
		super(message);
	}

	public ExportException(final Throwable cause) {
		super(cause);
	}

	public ExportException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ExportException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
