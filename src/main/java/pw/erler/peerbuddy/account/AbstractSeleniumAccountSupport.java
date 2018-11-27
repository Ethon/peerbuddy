package pw.erler.peerbuddy.account;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.common.selenium_util.ElementFinder;

@Log4j2
public abstract class AbstractSeleniumAccountSupport implements AccountSupport {

	protected final WebDriver webDriver;

	protected void trace(final Logger log, final String format, final Object... args) {
		log.trace(String.format(format, args));
	}

	protected ElementFinder find() {
		return new ElementFinder(webDriver.findElement(By.tagName("html")));
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

	protected List<WebElement> getAll(final ElementFinder finder) {
		trace(log, "Get all elements with criteria %s", finder.getCriteriaString());
		final List<WebElement> all = finder.getAll();
		trace(log, "Found %d elements", all.size());
		return all;
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

	protected void click(final Function<ElementFinder, ElementFinder> pipeline, final int index) {
		trace(log, "Click element");
		final WebElement element = awaitAndGet(pipeline, index);
		element.click();
	}

	protected void click(final Function<ElementFinder, ElementFinder> pipeline) {
		click(pipeline, 0);
	}

	protected AbstractSeleniumAccountSupport(final WebDriver webDriver) {
		this.webDriver = webDriver;
	}

}
