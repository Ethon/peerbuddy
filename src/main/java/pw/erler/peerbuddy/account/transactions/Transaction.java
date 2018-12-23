package pw.erler.peerbuddy.account.transactions;

import java.time.LocalDate;

import pw.erler.peerbuddy.common.values.MonetaryValue;

public interface Transaction {

	public LocalDate getDate();

	public TransactionType getType();

	public MonetaryValue getAmount();

}
