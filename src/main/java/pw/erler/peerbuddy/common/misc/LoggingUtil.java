package pw.erler.peerbuddy.common.misc;

import java.io.PrintStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggingUtil {

	public static final PrintStream originalOutStream = System.out;
	public static final PrintStream originalErrStream = System.err;

	public static void redirectConsoleOutputStreamsToLog() {
		// Selenium writes to err stream - no other code should do that.
		// In most of the cases the written information is at most debug relevant so
		// don't log it as error.
		System.setOut(new LoggingPrintStream(LogManager.getLogger("System.out"), Level.DEBUG));
		System.setErr(new LoggingPrintStream(LogManager.getLogger("System.err"), Level.DEBUG));
	}

}
