package pw.erler.peerbuddy.account.p2p;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pw.erler.peerbuddy.account.AccountStatusVisitor;
import pw.erler.peerbuddy.account.BasicAccountStatus;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class P2PAccountStatus extends BasicAccountStatus {

	private final MonetaryValue investedFunds;
	private final MonetaryValue availableFunds;

	public P2PAccountStatus(final String name, final MonetaryValue accountBalance, final MonetaryValue investedFunds,
			final MonetaryValue availableFunds) {
		super(name, accountBalance);
		this.investedFunds = investedFunds;
		this.availableFunds = availableFunds;
	}

	@Override
	public <T> T accept(final AccountStatusVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
