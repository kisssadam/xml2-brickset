package hu.unideb.inf.brickset.service;

import java.io.IOException;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import hu.unideb.inf.brickset.model.Brickset;
import hu.unideb.inf.brickset.parser.BricksetParser;

public class BricksetResource extends ServerResource {

	@Get("xml|json")
	public Brickset findBySetNumber() throws IOException {
		return new BricksetParser().parse("http://brickset.com/sets/" + getAttribute("setNumber"));
	}

}
