package hu.unideb.inf.brickset.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
	propOrder = {
		"setNumber",
		"name",
		"pieces"
	}
)
public class SearchResultItem {

	@XmlAttribute(required = true)
	private String uri;

	@XmlElement(required = true)
	private String setNumber;

	@XmlElement(required = true)
	private String name;
	
	@XmlElement(required = false)
	private int pieces;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(String setNumber) {
		this.setNumber = setNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPieces() {
		return pieces;
	}
	
	public void setPieces(int pieces) {
		this.pieces = pieces;
	}

}
