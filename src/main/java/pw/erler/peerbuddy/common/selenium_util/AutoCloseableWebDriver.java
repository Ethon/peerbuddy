package pw.erler.peerbuddy.common.selenium_util;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AutoCloseableWebDriver implements WebDriver, AutoCloseable {

	private final WebDriver impl;

	public AutoCloseableWebDriver(final WebDriver impl) {
		this.impl = impl;
	}

	@Override
	public void get(final String url) {
		impl.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return impl.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return impl.getTitle();
	}

	@Override
	public List<WebElement> findElements(final By by) {
		return impl.findElements(by);
	}

	@Override
	public WebElement findElement(final By by) {
		return impl.findElement(by);
	}

	@Override
	public String getPageSource() {
		return impl.getPageSource();
	}

	@Override
	public void close() {
		impl.close();
	}

	@Override
	public void quit() {
		impl.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return impl.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return impl.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return impl.switchTo();
	}

	@Override
	public Navigation navigate() {
		return impl.navigate();
	}

	@Override
	public Options manage() {
		return impl.manage();
	}

}
