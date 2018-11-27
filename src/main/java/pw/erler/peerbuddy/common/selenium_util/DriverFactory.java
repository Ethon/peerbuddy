package pw.erler.peerbuddy.common.selenium_util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import lombok.NonNull;
import pw.erler.peerbuddy.common.config.SeleniumConfig;
import pw.erler.peerbuddy.common.config.SeleniumDriverConfig;

public final class DriverFactory {

	private static WebDriver createDriver(final String driverName, final Path driverPath, final boolean headless) {
		switch (driverName.toLowerCase()) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", driverPath.toAbsolutePath().toString());
			return new ChromeDriver(new ChromeOptions().setHeadless(headless));
		case "gecko":
			System.setProperty("webdriver.gecko.driver", driverPath.toAbsolutePath().toString());
			return new FirefoxDriver();
		default:
			throw new UnsupportedOperationException("Can not create unsupported driver with name '" + driverName + "'");
		}
	}

	public static WebDriver createDriver(@NonNull final SeleniumConfig config) {
		checkNotNull(config, "seleniumConfig missing in config");
		checkNotNull(config.getSelectedDriver(), "seleniumConfig.selectedDriver missing in config");
		int i = 0;
		for (final SeleniumDriverConfig driverConfig : config.getAvailableDrivers()) {
			checkNotNull(driverConfig.getName(), "seleniumConfig.availableDrivers[%d].name missing in config", i);
			if (config.getSelectedDriver().equalsIgnoreCase(driverConfig.getName())) {
				checkNotNull(driverConfig.getDriverPath(),
						"seleniumConfig.availableDrivers[%d].driverPath missing in config", i);
				final Path driverPath = Paths.get(driverConfig.getDriverPath());
				checkArgument(Files.exists(driverPath),
						"Driver executable '%s' stored in seleniumConfig.availableDrivers[%d].driverPath does not exist",
						driverPath, i);
				checkArgument(Files.isExecutable(driverPath),
						"Driver executable '%s' stored in seleniumConfig.availableDrivers[%d].driverPath is not executable",
						driverPath, i);
				return createDriver(driverConfig.getName(), driverPath, driverConfig.isHeadless());
			}
			++i;
		}
		throw new IllegalArgumentException("seleniumConfig.availableDrivers does not contain a driver with name '"
				+ config.getSelectedDriver() + "'");
	}

}
