package hu.unideb.inf.brickset.service;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SearchResource extends ServerResource {

	@Get
	public String findByKeywords() {
		System.out.println("hello");
		String keyword = getQueryValue("keyword");
		return String.format("Keyword: '%s'", keyword);
	}

}
