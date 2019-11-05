package pw.erler.peerbuddy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.common.config.AccountConfig;
import pw.erler.peerbuddy.common.config.ConfigLoader;
import pw.erler.peerbuddy.common.config.ExportConfig;
import pw.erler.peerbuddy.common.config.PeerBuddyConfig;
import pw.erler.peerbuddy.common.credentials.CredentialsProvider;
import pw.erler.peerbuddy.common.credentials.CredentialsProviderFactory;
import pw.erler.peerbuddy.common.misc.LoggingUtil;
import pw.erler.peerbuddy.common.serialization.GsonFactory;
import pw.erler.peerbuddy.execution.AccountRunResult;
import pw.erler.peerbuddy.execution.AccountRunner;
import pw.erler.peerbuddy.export.AccountStatusOverviewModel;
import pw.erler.peerbuddy.export.ExportModelGenerator;
import pw.erler.peerbuddy.export.exporter.ExporterFactory;

@Log4j2
public final class PeerBuddy {

	public static void main(final String[] args) throws IOException, InterruptedException {

		LoggingUtil.redirectConsoleOutputStreamsToLog();
		try {
			// Load the config and retrieve credentials.
			final PeerBuddyConfig config = ConfigLoader.loadConfig(Paths.get("peerbuddy.json"));
			final CredentialsProvider credentialsProvider = CredentialsProviderFactory
					.createCredentialsProvider(config.getPasswordConfig());

			// Retrieve the status of all accounts.
			final Map<AccountConfig, List<AccountRunResult>> accountStatus = new AccountRunner(credentialsProvider,
					config, config.getAccounts()).runAll();

			// Create the export model and log it.
			final AccountStatusOverviewModel model = new ExportModelGenerator().createModel(config, accountStatus);
			log.info("\n" + model.asAsciiTable());

			// Create the JSON export and export it.
			final String jsonModel = GsonFactory.createGson().toJson(model);
			log.info("\n" + jsonModel);
			config.getExports().stream().filter(ExportConfig::isEnabled).forEach(exportConfig -> {
				log.info("Export " + exportConfig);
				try {
					ExporterFactory.createExporter(exportConfig, credentialsProvider).exportTo(
							exportConfig.getExportFile(),
							new ByteArrayInputStream(jsonModel.getBytes(StandardCharsets.UTF_8)));
					log.info("Finished exporting " + exportConfig);
				} catch (final Exception e) {
					log.error("Error while exporting " + exportConfig, e);
				}
			});
		} catch (Exception e) {
			log.error("An error occurred", e);
		}
	}

}
