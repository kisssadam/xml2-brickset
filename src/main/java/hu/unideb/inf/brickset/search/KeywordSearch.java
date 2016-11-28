package hu.unideb.inf.brickset.search;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import hu.unideb.inf.brickset.model.SearchResults;

public class KeywordSearch {

	private static final int DEFAULT_MAX_ITEMS = 25;
	private static final String SEARCH_URI = "http://brickset.com/sets?";

	private int maxItems;

	public KeywordSearch() {
		this(DEFAULT_MAX_ITEMS);
	}

	public KeywordSearch(int maxItems) {
		this.maxItems = maxItems;
	}

	public SearchResults search(String query) throws IOException {
		Document document = Jsoup.connect(SEARCH_URI)
				.userAgent("Mozilla")
				.cookie("PreferredCountry2", "CountryCode=HU&CountryName=Hungary")
				.data("query", query)
				.get();
		return parse(document);
	}

	private SearchResults parse(Document document) {
		SearchResults searchResults = new SearchResults();
		System.out.println("document: " + document);

		return searchResults;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

}
