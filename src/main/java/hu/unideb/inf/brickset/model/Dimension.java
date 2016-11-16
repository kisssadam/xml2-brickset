package hu.unideb.inf.brickset.model;

import java.math.BigDecimal;

public class Dimension {

	private BigDecimal width;

	private BigDecimal height;

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
		builder.append("Dimension [width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", thickness=");
		builder.append(thickness);
		builder.append("]");
		return builder.toString();
	}

}
