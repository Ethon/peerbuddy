package pw.erler.peerbuddy.common.selenium_util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementFinder {

	private Stream<WebElement> currentSelection;
	private final List<String> criteria = new ArrayList<>();

	public ElementFinder(final WebElement root) {
		this.currentSelection = Arrays.asList(root).stream();
	}

	public ElementFinder withId(final String id) {
		criteria.add("id='" + id + "'");
		currentSelection = currentSelection.flatMap(elem -> elem.findElements(By.id(id)).stream());
		return this;
	}

	public ElementFinder withTagName(final String tagName) {
		criteria.add("tagName='" + tagName + "'");
		currentSelection = currentSelection.flatMap(elem -> elem.findElements(By.tagName(tagName)).stream());
		return this;
	}

	public ElementFinder withName(final String name) {
		criteria.add("name='" + name + "'");
		currentSelection = currentSelection.flatMap(elem -> elem.findElements(By.name(name)).stream());
		return this;
	}

	public ElementFinder withText(final String text) {
		criteria.add("text='" + text + "'");
		currentSelection = currentSelection.filter(elem -> elem.getText().equals(text));
		return this;
	}

	public ElementFinder withXPath(final String xPath) {
		criteria.add("xPath='" + xPath + "'");
		currentSelection = currentSelection.flatMap(elem -> elem.findElements(By.xpath(xPath)).stream());
		return this;
	}

	public ElementFinder isDisplayed(final boolean value) {
		criteria.add("displayed=" + value + "");
		currentSelection = currentSelection.filter(WebElement::isDisplayed);
		return this;
	}

	public WebElement get(final int index) {
		criteria.add("index=" + index + "");
		final List<WebElement> results = getAll();
		if (index >= results.size()) {
			throw new IndexOutOfBoundsException("Index " + index + " is invalid for " + results.size()
					+ " results found with criteria " + criteria);
		}
		return results.get(index);
	}

	public WebElement get() {
		return get(0);
	}

	public List<WebElement> getAll() {
		return currentSelection.collect(Collectors.toList());
	}

	public String getCriteriaString() {
		return criteria.stream().collect(Collectors.joining(", ", "[", "]"));
	}

}
