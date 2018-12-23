package pw.erler.peerbuddy.common.config;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import lombok.Data;

@Data
public class PeerBuddyConfig {

	private int maximumNumberOfParallelThreads = 1;
	private int maximumNumberOfRetriesPerAccount = 0;
	private PasswordConfig passwordConfig;
	private SeleniumConfig seleniumConfig;
	private List<ExportConfig> exports;
	private List<AccountConfig> accounts;

	public int getMaximumNumberOfParallelThreads() {
		checkState(maximumNumberOfParallelThreads > 0,
				"The maximum number of parallel threads must be a number greater 0");
		return maximumNumberOfParallelThreads;
	}

	public int getMaximumNumberOfRetriesPerAccount() {
		checkState(maximumNumberOfRetriesPerAccount >= 0,
				"The maximum number of retries per account must be be a number greater or equal 0");
		return maximumNumberOfRetriesPerAccount;
	}

}
