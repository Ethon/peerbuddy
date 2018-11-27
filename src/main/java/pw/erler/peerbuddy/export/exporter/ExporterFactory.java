package pw.erler.peerbuddy.export.exporter;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import pw.erler.peerbuddy.common.config.ExportConfig;
import pw.erler.peerbuddy.common.credentials.CredentialsProvider;

@UtilityClass
public final class ExporterFactory {

	public static Exporter createExporter(@NonNull final ExportConfig config,
			@NonNull final CredentialsProvider provider) {
		switch (checkNotNull(config.getType()).toLowerCase()) {
		case "ftp":
			return new FtpExporter(provider.getCredentials(checkNotNull(config.getTitle())));
		default:
			throw new UnsupportedOperationException("Unsupported exporter type '" + config.getType() + "'");
		}
	}

}
