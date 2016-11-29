package hu.unideb.inf.brickset.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.unideb.inf.brickset.model.SearchResults;
import hu.unideb.inf.brickset.search.KeywordSearch;

public class SearchResource extends ServerResource {

	private static final Logger log = LoggerFactory.getLogger(SearchResource.class);

	@Get("xml")
	public SearchResults findByKeywords() throws IOException {
		String maxItemsString = getQueryValue("maxItems");
		String query = getQueryValue("query");

		log.debug("maxItems: '{}' query: '{}'", maxItemsString, query);

		if (StringUtils.isBlank(query)) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Required parameter 'query' is missing");
		}

		KeywordSearch keywordSearch = null;

		try {
			int maxItems = Integer.parseInt(maxItemsString);
			keywordSearch = new KeywordSearch(maxItems);
		} catch (NumberFormatException e) {
			log.warn("Failed to parse number: '{}'", maxItemsString);
			keywordSearch = new KeywordSearch();
		}

		return keywordSearch.search(query);
	}

}
