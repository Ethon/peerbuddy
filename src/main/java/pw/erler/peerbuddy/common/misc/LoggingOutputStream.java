package pw.erler.peerbuddy.common.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingOutputStream extends OutputStream {

	private final Logger log;
	private final Level level;
	private final ByteArrayOutputStream buffer;

	public LoggingOutputStream(final Logger log, final Level level) {
		this.log = log;
		this.level = level;
		this.buffer = new ByteArrayOutputStream();
	}

	@Override
	public void write(final int b) throws IOException {
		final boolean isCarriageReturn = b == '\r';
		if (isCarriageReturn) {
			return;
		}

		final boolean isNewline = b == '\n';
		if (isNewline) {
			if (buffer.size() > 0) {
				flush();
			}
		} else {
			buffer.write(b);
		}
	}

	@Override
	public void flush() throws IOException {
		if (buffer.size() > 0) {
			log.log(level, buffer.toString());
			buffer.reset();
		}
	}

	@Override
	public void close() throws IOException {
		flush();
	}

}
