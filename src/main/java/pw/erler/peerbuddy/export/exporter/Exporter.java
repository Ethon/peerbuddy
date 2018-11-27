package pw.erler.peerbuddy.export.exporter;

import java.io.InputStream;

public interface Exporter {

	public void exportTo(String path, InputStream content) throws ExportException;

}
