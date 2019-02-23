package pw.erler.peerbuddy.execution;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.account.AccountStatusVisitor;
import pw.erler.peerbuddy.account.AccountSupport;
import pw.erler.peerbuddy.account.AccountSupportFactory;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.credentials.CredentialsProvider;
import pw.erler.peerbuddy.common.selenium_util.AutoCloseableWebDriver;
import pw.erler.peerbuddy.common.selenium_util.DriverFactory;
import pw.erler.peerbuddy.common.selenium_util.error.ErrorUtil;

@Log4j2
public class AccountRunner {

	private static class StatusVisitor implements AccountStatusVisitor<AccountRunResult> {

		private final AccountConfig account;
		private final Map<AccountConfig, AccountRunResult> accountStatusMap;

		public StatusVisitor(final AccountConfig account, final Map<AccountConfig, AccountRunResult> accountStatusMap) {
			this.account = account;
			this.accountStatusMap = accountStatusMap;
		}

		@Override
		public AccountRunResult visit(final BasicAccountStatus status) {
			return accountStatusMap.put(account, new AccountRunResult(status));
		}

		@Override
		public AccountRunResult visit(final P2PAccountStatus status) {
			return accountStatusMap.put(account, new AccountRunResult(status));
		}

	}

	private final CredentialsProvider credentialsProvider;
	private final PeerBuddyConfig config;
	private final Map<String, List<AccountConfig>> accountsGroupedByType;

	private void runAccountImpl(final AccountConfig account,
			final Map<AccountConfig, AccountRunResult> accountStatusMap, final int retriesLeft) {
		boolean error = false;
		try (AutoCloseableWebDriver driver = DriverFactory.createDriver(config.getSeleniumConfig(),
				AccountSupportFactory.requiresRealBrowser(account))) {
			try {
				log.info(String.format("Running account '%s'", account.getTitle()));
				final Credentials credentials = credentialsProvider.getCredentials(account.getTitle());
				final AccountSupport accountSupport = AccountSupportFactory.createAccountSupport(driver, account);
				accountSupport.login(credentials);
				accountSupport.retrieveAccountStatus(new StatusVisitor(account, accountStatusMap));
				log.info(String.format("Finished running account '%s'", account.getTitle()));
			} catch (final Exception e) {
				log.error(String.format("Error while running account '%s'", account.getTitle()), e);
				ErrorUtil.dumpPageSource(driver, account.getTitle());
				accountStatusMap.put(account, new AccountRunResult(e));
				error = true;
			}
		}

		if (error) {
			if (retriesLeft > 0) {
				log.info(String.format("Retry running account '%s' - new retry count is %s", account.getTitle(),
						retriesLeft - 1));
				runAccountImpl(account, accountStatusMap, retriesLeft - 1);
			} else {
				log.debug(String.format("Dont retry running account '%s' - no retries left", account.getTitle()));
			}
		}

	}

	private void runAccount(final ExecutorService executorService, final AccountConfig account,
			final Map<AccountConfig, AccountRunResult> accountStatusMap, final int retriesLeft) {
		executorService.execute(() -> runAccountImpl(account, accountStatusMap, retriesLeft));
	}

	public AccountRunner(final CredentialsProvider credentialsProvider, final PeerBuddyConfig config,
			final List<AccountConfig> accountConfigs) {
		this.credentialsProvider = credentialsProvider;
		this.config = config;
		this.accountsGroupedByType = accountConfigs.stream().filter(AccountConfig::isEnabled)
				.collect(Collectors.groupingBy(AccountConfig::getType));
	}

	public Map<AccountConfig, AccountRunResult> runAll() throws InterruptedException {
		final Map<AccountConfig, AccountRunResult> accountStatusMap = new HashMap<>();
		final Map<AccountConfig, AccountRunResult> synchronizedMap = Collections.synchronizedMap(accountStatusMap);
		final ExecutorService executorService = Executors
				.newFixedThreadPool(config.getMaximumNumberOfParallelThreads());
		try {
			accountsGroupedByType.values().stream().flatMap(List::stream).filter(AccountConfig::isEnabled)
					.forEach(account -> runAccount(executorService, account, synchronizedMap,
							config.getMaximumNumberOfRetriesPerAccount()));
		} finally {
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MINUTES);
		}
		return accountStatusMap;
	}

}
