package hu.unideb.inf.brickset.service;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class BricksetApplication extends Application {

	public static void main(String[] args) throws Exception {
		Component component = new Component();
		component.getDefaultHost().attach("/brickset", new BricksetApplication());
		Server server = new Server(Protocol.HTTP, 8111, component);
		server.start();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.setDefaultMatchingQuery(true);

		router.attach("/set/{setNumber}", BricksetResource.class);
		router.attach("/search?{maxItems}{query}", SearchResource.class);

		return router;
	}

}
