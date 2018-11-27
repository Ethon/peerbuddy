package pw.erler.peerbuddy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;

import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.account.AccountSupport;
import pw.erler.peerbuddy.account.AccountSupportFactory;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.credentials.CredentialsProvider;
import pw.erler.peerbuddy.common.selenium_util.DriverFactory;

@Log4j2
public class AccountRunner {

	private final CredentialsProvider credentialsProvider;
	private final PeerBuddyConfig config;
	private final Map<String, List<AccountConfig>> accountsGroupedByType;

	private void runAccount(final ExecutorService executorService, final AccountConfig account,
			final Map<AccountConfig, P2PAccountStatus> accountStatusMap) {
		executorService.execute(() -> {
			final Credentials credentials = credentialsProvider.getCredentials(account.getTitle());
			final WebDriver driver = DriverFactory.createDriver(config.getSeleniumConfig());
			try {
				final AccountSupport accountSupport = AccountSupportFactory.createAccountSupport(driver, account);
				accountSupport.login(credentials);
				accountStatusMap.put(account, accountSupport.retrieveAccountStatus(P2PAccountStatus.class));
			} catch (final Exception e) {
				log.error("Error", e);
			} finally {
				driver.close();
			}
		});
	}

	public AccountRunner(final CredentialsProvider credentialsProvider, final PeerBuddyConfig config,
			final List<AccountConfig> accountConfigs) {
		this.credentialsProvider = credentialsProvider;
		this.config = config;
		this.accountsGroupedByType = accountConfigs.stream().filter(AccountConfig::isEnabled)
				.collect(Collectors.groupingBy(AccountConfig::getType));
	}

	public Map<AccountConfig, P2PAccountStatus> runAll() throws InterruptedException {
		final Map<AccountConfig, P2PAccountStatus> accountStatusMap = new HashMap<>();
		final Map<AccountConfig, P2PAccountStatus> synchronizedMap = Collections.synchronizedMap(accountStatusMap);
		final ExecutorService executorService = Executors.newFixedThreadPool(accountsGroupedByType.size());
		try {
			accountsGroupedByType.values().stream().flatMap(List::stream).filter(AccountConfig::isEnabled)
					.forEach(account -> runAccount(executorService, account, synchronizedMap));
		} finally {
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MINUTES);
		}
		return accountStatusMap;
	}

}
