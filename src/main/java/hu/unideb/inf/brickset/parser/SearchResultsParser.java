package hu.unideb.inf.brickset.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.unideb.inf.brickset.model.SearchResultItem;
import hu.unideb.inf.brickset.model.SearchResults;

public class SearchResultsParser {

	private static Logger log = LoggerFactory.getLogger(SearchResultsParser.class);

	public static final int MAX_ITEMS = 30;

	private int maxItems;

	public SearchResultsParser() {
		this(MAX_ITEMS);
	}

	public SearchResultsParser(int maxItems) {
		this.maxItems = maxItems;
	}

	public SearchResults parse(Document document) throws IOException {
		List<SearchResultItem> searchResultItems = new LinkedList<>();

		int itemsTotal = getItemsTotal(document);
		loop: while (itemsTotal != 0 && document != null) {
			for (SearchResultItem searchResultItem : extractItems(document)) {
				if (0 <= maxItems && maxItems <= searchResultItems.size()) {
					break loop;
				}
				searchResultItems.add(searchResultItem);
			}
			if (0 <= maxItems && maxItems <= searchResultItems.size()) {
				break;
			}
			document = getNextPage(document);
		}

		int to = searchResultItems.isEmpty() ? 1 : searchResultItems.size();
		return new SearchResults(itemsTotal, 1, to, searchResultItems);
	}

	private Document getNextPage(Document document) throws IOException {
		String nextPageUri = null;

		try {
			Element nextPageElement = document.select("div.pagination li.next a").first();
			if (nextPageElement != null) {
				nextPageUri = nextPageElement.absUrl("href");
			}
			log.debug("next page uri: '{}'", nextPageUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Document nextPageDocument = null;

		if (StringUtils.isNotBlank(nextPageUri)) {
			nextPageDocument = Jsoup.connect(nextPageUri)
					.userAgent("Mozilla")
					.cookie("PreferredCountry2", "CountryCode=HU&CountryName=Hungary")
					.get();
		}

		return nextPageDocument;
	}

	private int getItemsTotal(Document document) {
		int itemsTotal = 0;

		try {
			String resultsString = document.select("div.resultsfilter > div.results").first().text();
			log.debug("results string: '{}'", resultsString);

			Pattern pattern = Pattern.compile(".*\\d+ to \\d+ of (?<itemsTotal>\\d+) matches.*");
			Matcher matcher = pattern.matcher(resultsString);

			if (matcher.matches()) {
				itemsTotal = Integer.parseInt(matcher.group("itemsTotal"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("items total: {}", itemsTotal);

		return itemsTotal;
	}

	private List<SearchResultItem> extractItems(Document document) {
		List<SearchResultItem> searchResultItems = new LinkedList<>();

		for (Element element : document.select("section.setlist > article.set")) {
			searchResultItems.add(extractResultItem(element));
		}

		return searchResultItems;
	}

	private SearchResultItem extractResultItem(Element element) {
		SearchResultItem searchResultItem = new SearchResultItem();
		
		try {
			String name = element.select("div.meta > h1 > a").text();
			log.debug("Extracting name from string: '{}'", name);
			
			Pattern pattern = Pattern.compile(".*:\\s*(?<name>.*)\\s*");
			Matcher matcher = pattern.matcher(name);
			
			if (matcher.matches()) {
				name = matcher.group("name");
			}
			
			searchResultItem.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("name: '{}'", searchResultItem.getName());
		
		try {
			String setNumber = element.select("div.meta > h1 > a").attr("href");
			log.debug("Extracting setNumber from string: '{}'", setNumber);
			
			Pattern pattern = Pattern.compile(".*/sets/(?<setNumber>.*)/.*");
			Matcher matcher = pattern.matcher(setNumber);
			
			if (matcher.matches()) {
				setNumber = matcher.group("setNumber");
			}
			
			searchResultItem.setSetNumber(setNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("setNumber: '{}'", searchResultItem.getSetNumber());
		
		try {
			String uri = element.select("div.meta > h1 > a").first().absUrl("href");
			searchResultItem.setUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("uri: '{}'", searchResultItem.getUri());
		
		try {
			String pieces = element.select("div.col > dl > dt:matchesOwn(^Pieces$) + dd").text();
			log.debug("Extracting pieces from string: '{}'", pieces);
			
			if (StringUtils.isNotBlank(pieces)) {
				searchResultItem.setPieces(Integer.parseInt(pieces));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("pieces: {}", searchResultItem.getPieces());
		
		return searchResultItem;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

}
