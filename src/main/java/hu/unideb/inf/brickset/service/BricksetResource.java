package hu.unideb.inf.brickset.service;

import java.io.IOException;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import hu.unideb.inf.brickset.model.Brickset;
import hu.unideb.inf.brickset.parser.BricksetParser;

public class BricksetResource extends ServerResource {

	@Get("xml|json")
	public Brickset represent() throws IOException {
		String setNumber = getAttribute("setNumber");
		BricksetParser bricksetParser = new BricksetParser();
		return bricksetParser.parse("http://brickset.com/sets/" + setNumber);
	}

}
