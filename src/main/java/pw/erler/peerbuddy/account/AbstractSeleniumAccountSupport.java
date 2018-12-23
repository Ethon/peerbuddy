package pw.erler.peerbuddy.account;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Sets;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.account.transactions.Transaction;
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
	private final Set<AccountFeature> accountFeatures;

	protected void trace(final Logger log, final String format, final Object... args) {
		log.trace(String.format(format, args));
	}

	protected ElementFinder find() {
		return new ElementFinder(webDriver.findElement(By.tagName("html")));
	}

	protected void awaitPageToBecomeActive(final String url) {
		log.trace(String.format("Wait for page to become active: %s", url));
		Awaitility.await(String.format("Wait for page to become active: %s", url)).atMost(1, TimeUnit.MINUTES)
				.until(() -> url.equals(webDriver.getCurrentUrl()));
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

	protected AbstractSeleniumAccountSupport(final WebDriver webDriver, final List<AccountFeature> accountFeatures) {
		this.webDriver = webDriver;
		this.accountFeatures = Sets.immutableEnumSet(accountFeatures);
	}

	@Override
	public boolean supportsFeature(final AccountFeature feature) {
		return accountFeatures.contains(feature);
	}

	@Override
	public List<Transaction> retrieveTransactions(final LocalDate fromInclusive, final LocalDate toInclusive) {
		throw new UnsupportedOperationException();
	}

	// High-Level methods with error handling.

	protected void awaitPageLoad(final String description, @NonNull final String url) {
		try {
			awaitPageToBecomeActive(url);
		} catch (final ConditionTimeoutException e) {
			throw new PageLoadException(String.format("%s was not loaded", description));
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

}
