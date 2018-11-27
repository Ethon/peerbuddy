package pw.erler.peerbuddy.export;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusModel {

	private String title;
	private MonetaryValue accountBalance;
	private MonetaryValue investedFunds;
	private MonetaryValue availableFunds;

	public List<String> getCells() {
		return Arrays.asList(title, accountBalance.toString(), investedFunds.toString(), availableFunds.toString());
	}

}
