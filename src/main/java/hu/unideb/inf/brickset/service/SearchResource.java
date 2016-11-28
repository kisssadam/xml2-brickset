package hu.unideb.inf.brickset.service;

import java.io.IOException;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.unideb.inf.brickset.model.SearchResults;
import hu.unideb.inf.brickset.search.KeywordSearch;

public class SearchResource extends ServerResource {

	private static final Logger log = LoggerFactory.getLogger(SearchResource.class);

	@Get
	public SearchResults findByKeywords() throws IOException {
		String query = getQueryValue("query");
		log.debug("query: '{}'", query);
		return new KeywordSearch().search(query);
	}

}
