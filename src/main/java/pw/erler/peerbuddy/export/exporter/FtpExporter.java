package pw.erler.peerbuddy.export.exporter;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.credentials.CredentialsException;

@Log4j2
public class FtpExporter extends ExporterWithCredentials {

	private class FtpClientWrapper extends FTPClient implements AutoCloseable {

		public void connect() throws ExportException {
			try {
				final String host = credentials.getStringProperty("Host");
				final int port = credentials.getIntProperty("Port");
				log.info(String.format("Connecting to FTP host '%s' on port %d", host, port));
				super.connect(host, port);
			} catch (final IOException e) {
				throw new ExportException("Failed to connect to FTP host due to I/O error", e);
			} catch (final CredentialsException e) {
				throw new ExportException("Could not connect to FTP because credential properties are missing/wrong",
						e);
			}
		}

		public void login() throws ExportException {
			try {
				final String login = credentials.getLogin();
				final String password = credentials.getPassword();
				log.info(String.format("Logging in as user '%s'", login));
				if (!super.login(login, password)) {
					throw new ExportException("Failed to login at FTP host (invalid credentials?)");
				}
			} catch (final IOException e) {
				throw new ExportException("Failed to login at FTP host due to I/O error", e);
			}
		}

		public void uploadFile(final String path, final InputStream content) throws ExportException {
			try {
				log.info(String.format("Uploading file content to '%s'", path));
				if (!super.storeFile(path, content)) {
					throw new ExportException("Failed to upload file to FTP host (is the export path valid?)");
				}
			} catch (final IOException e) {
				throw new ExportException("Failed to upload file to FTP host due to I/O error", e);
			}
		}

		@Override
		public void close() throws ExportException {
			try {
				log.info("Disconnecting from FTP host");
				disconnect();
			} catch (final IOException e) {
				throw new ExportException("Failed to disconnect from FTP host due to I/O error", e);
			}
		}

	}

	public FtpExporter(@NonNull final Credentials credentials) {
		super(credentials);
	}

	@Override
	public void exportTo(@NonNull final String path, @NonNull final InputStream content) throws ExportException {
		try (FtpClientWrapper client = new FtpClientWrapper()) {
			client.connect();
			client.login();
			client.uploadFile(path, content);
		}
	}

}
