package pw.erler.peerbuddy.account.p2p;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.erler.peerbuddy.account.BasicAccountModel;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Data
@EqualsAndHashCode(callSuper = true)
public class P2PAccountModel extends BasicAccountModel {

	private MonetaryValue investedFunds;
	private MonetaryValue availableFunds;

}
