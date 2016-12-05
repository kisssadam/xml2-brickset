package hu.unideb.inf.brickset.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.unideb.inf.jaxb.JAXBUtil;

public class SearchResultsTest {

	private List<SearchResultItem> items;

	@Before
	public void setUp() {
		SearchResultItem item = new SearchResultItem();
		item.setUri("http://brickset.com/sets?query=star%20wars");
		item.setName("3219: Mini TIE Fighter");
		item.setPieces(12);
		item.setSetNumber("3219-1");

		items = new ArrayList<SearchResultItem>();
		items.add(item);
		items.add(item);
	}

	@Test
	public void test() throws JAXBException, IOException {
		SearchResults results = new SearchResults(2, 1, 2, items);
		String xml = JAXBUtil.toXML(results);

		Assert.assertNotNull(xml);
		Assert.assertNotEquals(StringUtils.EMPTY, xml);
	}

}
