package pw.erler.peerbuddy.account.p2p.mintos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import lombok.Value;
import pw.erler.peerbuddy.account.transactions.Transaction;
import pw.erler.peerbuddy.account.transactions.TransactionException;
import pw.erler.peerbuddy.account.transactions.TransactionType;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Value
public class MintosTransaction implements Transaction {

	private String transactionId;
	private LocalDate date;
	private String details;
	private BigDecimal turnover;
	private BigDecimal balance;
	private String currency;

	@Override
	public TransactionType getType() {
		throw new TransactionException(
				"Can't deduce transaction type from details '" + details + "' and turnover " + turnover);
	}

	@Override
	public MonetaryValue getAmount() {
		return new MonetaryValue(turnover, Currency.getInstance(currency));
	}

}
