package pw.erler.peerbuddy.common.misc;

import java.io.PrintStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {

	public LoggingPrintStream(final Logger log, final Level level) {
		super(new LoggingOutputStream(log, level));
	}

}
