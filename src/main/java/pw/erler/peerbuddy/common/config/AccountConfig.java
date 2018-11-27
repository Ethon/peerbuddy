package pw.erler.peerbuddy.common.config;

import lombok.Data;

@Data
public class AccountConfig {

	private String type;
	private String title;
	private boolean disabled;

	public boolean isEnabled() {
		return !disabled;
	}

}
