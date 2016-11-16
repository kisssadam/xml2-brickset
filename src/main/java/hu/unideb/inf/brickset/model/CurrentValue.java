package hu.unideb.inf.brickset.model;

public class CurrentValue {

	private Price newValue;

	private Price usedValue;

	public Price getNewValue() {
		return newValue;
	}

	public void setNewValue(Price newValue) {
		this.newValue = newValue;
	}

	public Price getUsedValue() {
		return usedValue;
	}

	public void setUsedValue(Price usedValue) {
		this.usedValue = usedValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrentValue [newValue=");
		builder.append(newValue);
		builder.append(", usedValue=");
		builder.append(usedValue);
		builder.append("]");
		return builder.toString();
	}

}
