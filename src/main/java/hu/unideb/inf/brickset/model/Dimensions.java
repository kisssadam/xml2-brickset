package hu.unideb.inf.brickset.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.inf.unideb.hu/Brickset")
public class Dimensions {

	@XmlAttribute
	private BigDecimal width;

	@XmlAttribute
	private BigDecimal height;

	@XmlAttribute
	private BigDecimal thickness;

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getThickness() {
		return thickness;
	}

	public void setThickness(BigDecimal thickness) {
		this.thickness = thickness;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Dimensions [width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", thickness=");
		builder.append(thickness);
		builder.append("]");
		return builder.toString();
	}

}
