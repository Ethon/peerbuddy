package pw.erler.peerbuddy.export;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import pw.erler.peerbuddy.common.misc.AsciiTablePrinter;

@Data
public class AccountStatusOverviewModel {

	private List<AccountStatusModel> accounts = new ArrayList<>();
	private String dateTime;

	public List<List<String>> getRows() {
		return accounts.stream().map(AccountStatusModel::getCells).collect(Collectors.toList());
	}

	public String asAsciiTable() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		new AsciiTablePrinter().print(ps, getRows());
		ps.flush();
		return baos.toString();
	}

}
