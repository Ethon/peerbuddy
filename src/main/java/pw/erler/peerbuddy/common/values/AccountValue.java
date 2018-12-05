package pw.erler.peerbuddy.common.values;

public abstract class AccountValue {

	public <T> T as(final Class<T> clazz) {
		if (!this.getClass().equals(clazz)) {
			throw new AttributeException("Expected " + clazz.getSimpleName() + " but got "
					+ this.getClass().getSimpleName() + " for value '" + this + "'");
		}
		return clazz.cast(this);
	}

	public MonetaryValue monetaryValue() {
		return as(MonetaryValue.class);
	}

	public PercentageValue percentageValue() {
		return as(PercentageValue.class);
	}

}
