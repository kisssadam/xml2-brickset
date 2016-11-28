package hu.unideb.inf.brickset.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public Brickset createBrickset() {
		return new Brickset();
	}

	public SearchResults createSearchResults() {
		return new SearchResults();
	}

}
