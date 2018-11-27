package pw.erler.peerbuddy.common.config;

import lombok.Data;

@Data
public class SeleniumDriverConfig {

	private String name;
	private String driverPath;
	private boolean headless;

}
