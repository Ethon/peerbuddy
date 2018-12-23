package pw.erler.peerbuddy.common.selenium_util.error;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;

import com.google.common.io.Files;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class ErrorUtil {

	private final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static void dumpPageSource(final WebDriver driver, final String filePrefix) {
		final File dumpDirectory = new File("dump");
		final File dumpFile = new File(dumpDirectory,
				filePrefix.replaceAll("\\W+", "") + "_" + LocalDateTime.now().format(DT_FORMAT) + ".htm");
		try {
			dumpDirectory.mkdirs();
			Files.asCharSink(dumpFile, StandardCharsets.UTF_8).write(driver.getPageSource());
		} catch (final Exception e) {
			// We expect this to be called in exception handlers so don't throw here.
			log.error("Failed to dump page source to " + dumpFile, e);
		}
	}

}
