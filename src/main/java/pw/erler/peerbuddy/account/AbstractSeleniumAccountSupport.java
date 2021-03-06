package pw.erler.peerbuddy.account;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.account.transactions.Transaction;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.selenium_util.ElementFinder;
import pw.erler.peerbuddy.common.selenium_util.WebElementDescription;
import pw.erler.peerbuddy.common.selenium_util.WebElementType;
import pw.erler.peerbuddy.common.selenium_util.error.PageLoadException;
import pw.erler.peerbuddy.common.selenium_util.error.WebElementNotFoundException;
import pw.erler.peerbuddy.common.values.AccountAttributePair;
import pw.erler.peerbuddy.common.values.AccountAttributeParsing;
import pw.erler.peerbuddy.common.values.AccountValue;

@Log4j2
public abstract class AbstractSeleniumAccountSupport implements AccountSupport {

	protected final WebDriver webDriver;
	protected final AccountConfig accountConfig;
	protected Set<String> activeWindowHandles;
	private final Set<AccountFeature> accountFeatures;

	protected void trace(final Logger log, final String format, final Object... args) {
		log.trace(String.format(format, args));
	}

	protected void recordWindows() {
		activeWindowHandles = webDriver.getWindowHandles();
	}

	protected boolean hasNewWindows() {
		return getNewWindow() != null;
	}

	protected String getNewWindow() {
		final Set<String> windowHandles = webDriver.getWindowHandles();
		windowHandles.removeAll(activeWindowHandles);
		return windowHandles.size() == 1 ? windowHandles.iterator().next() : null;
	}

	protected void awaitNewWindow() {
		log.trace("Wait for new window to appear");
		Awaitility.await("Wait for new window to appear").atMost(1, TimeUnit.MINUTES).until(this::hasNewWindows);
		log.trace("New window appeared");
	}

	protected void switchToNewWindow() {
		awaitNewWindow();
		webDriver.switchTo().window(getNewWindow());
	}

	protected ElementFinder find() {
		return new ElementFinder(webDriver.findElement(By.tagName("html")));
	}

	protected boolean isPageActive(final String url) {
		return webDriver.getCurrentUrl().startsWith(url);
	}

	protected void awaitOnePageToBecomeActive(final String... urls) {
		log.trace(String.format("Wait for one of the pages to become active: %s", Arrays.toString(urls)));
		Awaitility.await(String.format("Wait for one of the pages to become active: %s", Arrays.toString(urls)))
				.atMost(1, TimeUnit.MINUTES).until(() -> Arrays.stream(urls).anyMatch(this::isPageActive));
		log.trace("Page became active");
	}

	protected WebElement awaitAndGet(final Function<ElementFinder, ElementFinder> pipeline, final int index) {
		trace(log, "Wait and get element with criteria %s and index %d", pipeline.apply(find()).getCriteriaString(),
				index);
		Awaitility.await("Wait for the element to become visible").atMost(1, TimeUnit.MINUTES).until(() -> {
			final ElementFinder finder = pipeline.apply(find());
			try {
				finder.get(index);
				return true;
			} catch (final IndexOutOfBoundsException e) {
				return false;
			} catch (final StaleElementReferenceException e) {
				return false;
			}
		});
		trace(log, "Found element");
		return pipeline.apply(find()).get(index);
	}

	protected WebElement awaitAndGet(final WebElementDescription description) {
		return awaitAndGet(description.getFinder(), description.getIndex());
	}

	protected List<WebElement> getAll(final ElementFinder finder) {
		trace(log, "Get all elements with criteria %s", finder.getCriteriaString());
		final List<WebElement> all = finder.getAll();
		trace(log, "Found %d elements", all.size());
		return all;
	}

	protected Map<String, AccountValue> getAttributes(final ElementFinder finder) {
		final List<WebElement> all = getAll(finder);
		return all //
				.stream() //
				.map(WebElement::getText) //
				.filter(s -> !s.isEmpty()) //
				.map(AccountAttributeParsing::parseAccountAttributePair) //
				.collect(Collectors.toMap(AccountAttributePair::getKey, AccountAttributePair::getValue));
	}

	protected void get(final String url) {
		trace(log, "Get '%s'", url);
		webDriver.get(url);
	}

	protected void sendKeys(final Function<ElementFinder, ElementFinder> pipeline, final int index, final String text) {
		trace(log, "Sends text to element");
		final WebElement element = awaitAndGet(pipeline, index);
		element.sendKeys(text);
	}

	protected void sendKeysToClear(final Function<ElementFinder, ElementFinder> pipeline, final int index) {
		trace(log, "Send backspaces to element until it is cleared");
		final WebElement element = awaitAndGet(pipeline, index);
		while (!element.getText().isEmpty()) {
			element.sendKeys(Keys.BACK_SPACE);
		}
	}

	protected void click(final Function<ElementFinder, ElementFinder> pipeline, final int index) {
		trace(log, "Click element");
		final WebElement element = awaitAndGet(pipeline, index);
		element.click();
	}

	protected void click(final Function<ElementFinder, ElementFinder> pipeline) {
		click(pipeline, 0);
	}

	protected AbstractSeleniumAccountSupport(final WebDriver webDriver, final AccountConfig accountConfig,
			final Collection<AccountFeature> accountFeatures) {
		this.webDriver = webDriver;
		this.accountConfig = accountConfig;
		this.accountFeatures = Sets.immutableEnumSet(accountFeatures);
	}

	@Override
	public boolean supportsFeature(final AccountFeature feature) {
		return accountFeatures.contains(feature);
	}

	@Override
	public List<Transaction> retrieveTransactions(final LocalDate fromInclusive, final LocalDate toInclusive)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	// High-Level methods with error handling.

	protected void getPageAndAwaitLoad(final String description, final String url) {
		get(url);
		awaitPageLoad(description, url);
	}

	protected void awaitPageLoad(final String description, @NonNull final String... urls) {
		try {
			awaitOnePageToBecomeActive(urls);
		} catch (final ConditionTimeoutException e) {
			throw new PageLoadException(String.format("%s was not loaded (expected '%s' but still is '%s')",
					description, Arrays.toString(urls), webDriver.getCurrentUrl()));
		}
	}

	protected void enterTextIntoInputField(final WebElementDescription inputField, final String text) {
		checkArgument(inputField.getType() == WebElementType.INPUT_FIELD);
		if (log.isTraceEnabled()) {
			if (inputField.isPasswordField()) {
				log.trace(String.format("Entering password into '%s' text field", inputField.getDescription()));
			} else {
				log.trace(String.format("Entering text '%s' into '%s' text field", text, inputField.getDescription()));
			}
		}
		try {
			sendKeys(inputField.getFinder(), inputField.getIndex(), text);
		} catch (final ConditionTimeoutException e) {
			throw new WebElementNotFoundException(
					String.format("Could not locate '%s' text field", inputField.getDescription()));
		}
	}

	protected void clickButton(final WebElementDescription button) {
		checkArgument(button.getType() == WebElementType.BUTTON);
		log.trace(String.format("Clicking '%s' button", button.getDescription()));
		try {
			click(button.getFinder(), button.getIndex());
		} catch (final ConditionTimeoutException e) {
			throw new WebElementNotFoundException(
					String.format("Could not locate '%s' button", button.getDescription()));
		}
	}

	protected void clearInputField(final WebElementDescription inputField) {
		checkArgument(inputField.getType() == WebElementType.INPUT_FIELD);
		log.trace(String.format("Clearing '%s' text field", inputField.getDescription()));
		try {
			sendKeysToClear(inputField.getFinder(), inputField.getIndex());
		} catch (final ConditionTimeoutException e) {
			throw new WebElementNotFoundException(
					String.format("Could not locate '%s' text field", inputField.getDescription()));
		}
	}

	protected String getLabelText(final WebElementDescription label) {
		checkArgument(label.getType() == WebElementType.LABEL);
		log.trace(String.format("Getting value of '%s' text field", label.getDescription()));
		try {
			return awaitAndGet(label).getText();
		} catch (final ConditionTimeoutException e) {
			throw new WebElementNotFoundException(String.format("Could not locate '%s' label", label.getDescription()));
		}
	}

	// JSOUP bridges.

	protected Connection openJsoupConnection(final String url) {
		final Connection connection = Jsoup.connect(url);
		webDriver.manage().getCookies().forEach(cookie -> connection.cookie(cookie.getName(), cookie.getValue()));
		return connection;
	}

	// HTTP connection bridges.

	protected HttpURLConnection createHttpUrlConnection(final String url) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestProperty("Cookie", Joiner.on(';').join(webDriver.manage().getCookies()));
		return connection;
	}

	protected byte[] rawHttpGet(final String url) throws IOException {
		final HttpURLConnection connection = createHttpUrlConnection(url);
		connection.setRequestMethod("GET");
		connection.connect();
		try (InputStream is = connection.getInputStream()) {
			return ByteStreams.toByteArray(is);
		}
	}

}
