package pw.erler.peerbuddy.account.p2p.lenndy;

import java.util.Map;

import lombok.Value;
import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;
import pw.erler.peerbuddy.common.values.AccountAttributeMaps;
import pw.erler.peerbuddy.common.values.AccountValue;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Value
public class LenndyAccountOverview {

	private MonetaryValue accountBalance;
	private MonetaryValue currentlyInvestedAmount;
	private MonetaryValue availableFunds;

	public static LenndyAccountOverview ofAccountAttributeMap(final Map<String, AccountValue> accountAttributes) {
		final MonetaryValue accountBalance = AccountAttributeMaps.get(accountAttributes,
				LenndyConstants.ATTRIBUTE_KEY_ACCOUNT_BALANCE, MonetaryValue.class);
		final MonetaryValue currentlyInvestedAmount = AccountAttributeMaps.get(accountAttributes,
				LenndyConstants.ATTRIBUTE_KEY_INVESTED_AMOUNT, MonetaryValue.class);
		final MonetaryValue availableFunds = AccountAttributeMaps.get(accountAttributes,
				LenndyConstants.ATTRIBUTE_KEY_AVAILABLE_FUNDS, MonetaryValue.class);
		return new LenndyAccountOverview(accountBalance, currentlyInvestedAmount, availableFunds);
	}

	public P2PAccountStatus toAccountStatus() {
		return new P2PAccountStatus(accountBalance, currentlyInvestedAmount, availableFunds);
	}

}
