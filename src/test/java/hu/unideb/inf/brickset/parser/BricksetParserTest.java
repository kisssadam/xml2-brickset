package hu.unideb.inf.brickset.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.unideb.inf.brickset.model.Brickset;
import hu.unideb.inf.jaxb.JAXBUtil;

@RunWith(Parameterized.class)
public class BricksetParserTest {

	private static final Logger log = LoggerFactory.getLogger(BricksetParserTest.class);

	private static BricksetParser bricksetParser;
	
	private String url;

	public BricksetParserTest(String url) {
		this.url = url;
	}
	
	@BeforeClass
	public static void init() {
		bricksetParser = new BricksetParser(); 
	}
	
	@Parameters(name = "{index}: testParse({0})")
	public static Collection<String> generateUrls() {
		return Arrays.asList(
			new String[] {
				"http://brickset.com/sets/7965-1/Millennium-Falcon",
				"http://brickset.com/sets/4501-1/Mos-Eisley-Cantina",
				"http://brickset.com/sets/30346-1/Prison-Island-Helicopter"
			}
		);
	}

	@Test
	public void testParse() throws JAXBException, IOException {
		Brickset brickset = bricksetParser.parse(url);
		String xml = JAXBUtil.toXML(brickset);

		log.info("Parsed url: {}, brickset: {} in xml: {}", url, brickset, xml);

		Assert.assertNotNull(xml);
		Assert.assertNotEquals(StringUtils.EMPTY, xml);
	}

}
