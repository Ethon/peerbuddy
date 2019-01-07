package pw.erler.peerbuddy.common.selenium_util;

import java.util.function.Function;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WebElementDescription {

	private final WebElementType type;
	private final String description;
	private final Function<ElementFinder, ElementFinder> finder;

	@Builder.Default
	private final int index = 0;

	@Builder.Default
	private final boolean isPasswordField = false;

	public static WebElementDescriptionBuilder inputField() {
		return new WebElementDescriptionBuilder().type(WebElementType.INPUT_FIELD);
	}

	public static WebElementDescriptionBuilder button() {
		return new WebElementDescriptionBuilder().type(WebElementType.BUTTON);
	}

	public static WebElementDescriptionBuilder label() {
		return new WebElementDescriptionBuilder().type(WebElementType.LABEL);
	}

}
