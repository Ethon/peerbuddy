package pw.erler.peerbuddy.export.exporter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pw.erler.peerbuddy.common.credentials.Credentials;

@RequiredArgsConstructor
abstract class ExporterWithCredentials implements Exporter {

	@NonNull
	protected final Credentials credentials;

}
