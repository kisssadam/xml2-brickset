package hu.unideb.inf.brickset.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.inf.unideb.hu/Brickset")
public class CurrentValue {

	@XmlElement
	private Price newValue;

	@XmlElement
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
