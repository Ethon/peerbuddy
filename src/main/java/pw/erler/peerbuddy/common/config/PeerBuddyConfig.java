package pw.erler.peerbuddy.common.config;

import java.util.List;

import lombok.Data;

@Data
public class PeerBuddyConfig {

	private PasswordConfig passwordConfig;
	private SeleniumConfig seleniumConfig;
	private List<ExportConfig> exports;
	private List<AccountConfig> accounts;

}
