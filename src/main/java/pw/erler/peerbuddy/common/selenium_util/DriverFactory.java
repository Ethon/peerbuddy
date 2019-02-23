package pw.erler.peerbuddy.common.selenium_util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.config.SeleniumConfig;
import pw.erler.peerbuddy.common.config.SeleniumDriverConfig;

@UtilityClass
public final class DriverFactory {

	private static ChromeDriver createChromeDriver(final Path driverPath, final boolean headless) {
		System.setProperty("webdriver.chrome.driver", driverPath.toAbsolutePath().toString());
		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setHeadless(headless);
		chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		return new ChromeDriver(chromeOptions);
	}

	private static AutoCloseableWebDriver createDriver(final String driverName, final Path driverPath,
			final boolean headless) {
		switch (driverName.toLowerCase()) {
		case "chrome":
			return new AutoCloseableWebDriver(createChromeDriver(driverPath, headless));
		case "gecko":
			System.setProperty("webdriver.gecko.driver", driverPath.toAbsolutePath().toString());
			return new AutoCloseableWebDriver(new FirefoxDriver());
		default:
			throw new UnsupportedOperationException("Can not create unsupported driver with name '" + driverName + "'");
		}
	}

	public static AutoCloseableWebDriver createDriver(@NonNull final SeleniumConfig config,
			final boolean requiresRealBrowser) {
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
				return createDriver(driverConfig.getName(), driverPath,
						requiresRealBrowser ? false : driverConfig.isHeadless());
			}
			++i;
		}
		throw new IllegalArgumentException("seleniumConfig.availableDrivers does not contain a driver with name '"
				+ config.getSelectedDriver() + "'");
	}

}
