package hu.unideb.inf.brickset.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.inf.unideb.hu/Brickset")
public class Price {

	@XmlValue
	private BigDecimal value;

	@XmlAttribute(name = "currency", required = true)
	@XmlSchemaType(name = "token")
	private String currency;

	public Price() {
		super();
	}

	public Price(BigDecimal value, String currency) {
		super();
		this.value = value;
		this.currency = currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Price [value=");
		builder.append(value);
		builder.append(", currency=");
		builder.append(currency);
		builder.append("]");
		return builder.toString();
	}

}
