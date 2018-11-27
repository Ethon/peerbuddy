package pw.erler.peerbuddy.common.config;

import lombok.Value;

@Value
public class ExportConfig {

	private String type;
	private String title;
	private String exportFile;
	private boolean disabled;

	public boolean isEnabled() {
		return !isDisabled();
	}

}
