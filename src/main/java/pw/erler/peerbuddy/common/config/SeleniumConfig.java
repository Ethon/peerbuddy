package pw.erler.peerbuddy.common.config;

import java.util.List;

import lombok.Data;

@Data
public class SeleniumConfig {

	private String selectedDriver;
	private List<SeleniumDriverConfig> availableDrivers;

}
