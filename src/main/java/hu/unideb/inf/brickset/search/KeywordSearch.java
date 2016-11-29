package hu.unideb.inf.brickset.search;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import hu.unideb.inf.brickset.model.SearchResults;
import hu.unideb.inf.brickset.parser.SearchResultsParser;

public class KeywordSearch extends SearchResultsParser {

	private static final String SEARCH_URI = "http://brickset.com/sets?";

	public KeywordSearch() {
		super();
	}

	public KeywordSearch(int maxItems) {
		super(maxItems);
	}

	public SearchResults search(String query) throws IOException {
		Document document = Jsoup.connect(SEARCH_URI)
				.userAgent("Mozilla")
				.cookie("PreferredCountry2", "CountryCode=HU&CountryName=Hungary")
				.data("query", query)
				.get();
		return parse(document);
	}

}
