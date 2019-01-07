package pw.erler.peerbuddy.account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pw.erler.peerbuddy.common.values.MonetaryValue;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BasicAccountStatus {

	private MonetaryValue accountBalance;

	public <T> T accept(final AccountStatusVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
