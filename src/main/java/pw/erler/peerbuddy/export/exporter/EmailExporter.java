package pw.erler.peerbuddy.export.exporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;

import lombok.NonNull;
import pw.erler.peerbuddy.common.credentials.Credentials;
import pw.erler.peerbuddy.common.credentials.CredentialsException;

public class EmailExporter extends ExporterWithCredentials {

	public EmailExporter(@NonNull final Credentials credentials) {
		super(credentials);
	}

	@Override
	public void exportTo(final String path, final InputStream content) throws ExportException {
		try {
			final Email email = new SimpleEmail();
			email.setHostName(credentials.getStringProperty("Host"));
			email.setSmtpPort(credentials.getIntProperty("Port"));
			email.setAuthenticator(new DefaultAuthenticator(credentials.getLogin(), credentials.getPassword()));
			email.setSSLOnConnect(credentials.getBooleanProperty("SSL"));
			email.setSubject(path);
			email.setFrom(credentials.getStringProperty("From", credentials.getLogin()));
			email.setMsg(CharStreams.toString(new InputStreamReader(content, StandardCharsets.UTF_8)));
			for (final String to : Splitter.on(",").split(credentials.getStringProperty("To"))) {
				email.addTo(to);
			}
			email.send();
		} catch (final CredentialsException e) {
			throw new ExportException("Could not send Email because credential properties are missing/wrong", e);
		} catch (final EmailException e) {
			throw new ExportException("Error sending email", e);
		} catch (final IOException e) {
			throw new ExportException("Could not send Email because of I/O error", e);
		}
	}

}
