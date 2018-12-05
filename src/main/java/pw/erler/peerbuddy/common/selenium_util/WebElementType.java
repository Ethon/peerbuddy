package pw.erler.peerbuddy.common.selenium_util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WebElementType {

	INPUT_FIELD("input field"), BUTTON("button");

	private final String description;

}
