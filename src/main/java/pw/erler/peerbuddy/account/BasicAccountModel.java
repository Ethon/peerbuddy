package pw.erler.peerbuddy.account;

import lombok.Data;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Data
public class BasicAccountModel {

	private String title;
	private MonetaryValue accountBalance;

}
