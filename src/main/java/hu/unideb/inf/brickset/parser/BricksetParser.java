package hu.unideb.inf.brickset.parser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.unideb.inf.brickset.model.Brickset;
import hu.unideb.inf.brickset.model.Dimensions;
import hu.unideb.inf.brickset.model.Price;
import hu.unideb.inf.brickset.model.UriValuePair;

public class BricksetParser {

	private static final Logger log = LoggerFactory.getLogger(BricksetParser.class);

	private static final String MALFORMED_DOCUMENT = "Malformed document";

	public Brickset parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
		Brickset brickset = parse(doc);
		brickset.setUri(url);
		return brickset;
	}

	public Brickset parse(File file) throws IOException {
		Document doc = Jsoup.parse(file, null);
		Brickset brickset = parse(doc);
		brickset.setUri(file.toURI().toString());
		return brickset;
	}

	public Brickset parse(Document doc) throws IOException {
		Brickset brickset = new Brickset();

		try {
			brickset.setNumber(extractBricksetDescription(doc, "Set number"));
			log.debug("Extracted set number: '{}'", brickset.getNumber());

			brickset.setName(extractBricksetDescription(doc, "Name"));
			log.debug("Extracted name: '{}'", brickset.getName());

			brickset.setType(extractBricksetDescription(doc, "Set type"));
			log.debug("Extracted set type: '{}'", brickset.getType());

			brickset.setThemeGroup(extractBricksetDescription(doc, "Theme group"));
			log.debug("Extracted theme group: '{}'", brickset.getThemeGroup());

			UriValuePair<String> theme = new UriValuePair<>();
			theme.setUri(extractBricksetDescriptionUri(doc, "Theme"));
			theme.setValue(extractBricksetDescription(doc, "Theme"));
			brickset.setTheme(theme);
			log.debug("Extracted theme: {}", brickset.getTheme());

			UriValuePair<String> subTheme = new UriValuePair<>();
			subTheme.setUri(extractBricksetDescriptionUri(doc, "Subtheme"));
			subTheme.setValue(extractBricksetDescription(doc, "Subtheme"));
			brickset.setSubTheme(subTheme);
			log.debug("Extracted subtheme: {}", brickset.getSubTheme());

			UriValuePair<Integer> yearReleased = new UriValuePair<>();
			yearReleased.setUri(extractBricksetDescriptionUri(doc, "Year released"));
			String yearReleasedValue = extractBricksetDescription(doc, "Year released");
			yearReleased.setValue(yearReleasedValue == null ? null : Integer.valueOf(yearReleasedValue));
			brickset.setYearReleased(yearReleased);
			log.debug("Extracted year released: {}", brickset.getYearReleased());

			// TODO tags
			log.debug("Extracted tags: {}", Arrays.toString(brickset.getTags()));

			UriValuePair<Long> pieces = new UriValuePair<>();
			pieces.setUri(extractBricksetDescriptionUri(doc, "Pieces"));
			String piecesValue = extractBricksetDescription(doc, "Pieces");
			pieces.setValue(piecesValue == null ? null : Long.valueOf(piecesValue));
			brickset.setPieces(pieces);
			log.debug("Extracted pieces: {}", brickset.getPieces());

			UriValuePair<Integer> minifigs = new UriValuePair<>();
			minifigs.setUri(extractBricksetDescriptionUri(doc, "Minifigs"));
			String minifigsValue = extractBricksetDescription(doc, "Minifigs");
			minifigs.setValue(minifigsValue == null ? null : Integer.valueOf(minifigsValue));
			brickset.setMinifigs(minifigs);
			log.debug("Extracted minifigs: {}", brickset.getMinifigs());

			String multipleRrp = extractBricksetDescription(doc, "RRP");
			Pattern rrpPattern = Pattern.compile("(£(?<pounds>\\d+\\.\\d+))?( / )?(\\$(?<dollars>\\d+\\.\\d+))?( / )?((?<euros>\\d+\\.\\d+)€)?");
			Matcher rrpMatcher = rrpPattern.matcher(multipleRrp);
			List<Price> rrp = new ArrayList<>();
			if (rrpMatcher.matches()) {
				String pounds = rrpMatcher.group("pounds");
				String dollars = rrpMatcher.group("dollars");
				String euros = rrpMatcher.group("euros");

				log.debug("pounds: {}, dollars: {}, euros: {}", pounds, dollars, euros);

				if (StringUtils.isNotBlank(pounds)) {
					rrp.add(new Price(new BigDecimal(pounds), "GBP"));
				}
				if (StringUtils.isNotBlank(dollars)) {
					rrp.add(new Price(new BigDecimal(dollars), "USD"));
				}
				if (StringUtils.isNotBlank(euros)) {
					rrp.add(new Price(new BigDecimal(euros), "EUR"));
				}
			}
			brickset.setRrp(rrp.toArray(new Price[rrp.size()]));
			log.debug("Extracted rrp: {}", Arrays.toString(brickset.getRrp()));

			brickset.setPricePerPiece(extractBricksetDescription(doc, "Price per piece"));
			log.debug("Extracted price per piece: '{}'", brickset.getPricePerPiece());

			brickset.setAgeRange(extractBricksetDescription(doc, "Age range"));
			log.debug("Extracted age range: '{}'", brickset.getAgeRange());

			brickset.setPackaging(extractBricksetDescription(doc, "Packaging"));
			log.debug("Extracted packaging: '{}'", brickset.getPackaging());

			Dimensions dimensions = new Dimensions();
			String dimensionsString = extractBricksetDescription(doc, "Dimensions");
			if (StringUtils.isNotBlank(dimensionsString)) {
				Pattern dimensionsPattern = Pattern.compile("(?<width>\\d+\\.\\d+) x (?<height>\\d+\\.\\d+) x (?<thickness>\\d+\\.\\d+) cm.*");
				Matcher dimensionsMatcher = dimensionsPattern.matcher(dimensionsString);
				if (dimensionsMatcher.matches()) {
					String width = dimensionsMatcher.group("width");
					String height = dimensionsMatcher.group("height");
					String thickness = dimensionsMatcher.group("thickness");

					log.debug("Dimension width: {}, height: {}, thickness: {}", width, height, thickness);

					if (StringUtils.isNotBlank(width)) {
						dimensions.setWidth(new BigDecimal(width));
					}
					if (StringUtils.isNotBlank(height)) {
						dimensions.setHeight(new BigDecimal(height));
					}
					if (StringUtils.isNotBlank(thickness)) {
						dimensions.setThickness(new BigDecimal(thickness));
					}
				}
			}
			brickset.setDimensions(dimensions);
			log.debug("Extracted dimensions: '{}'", brickset.getDimensions());

			String weightString = extractBricksetDescription(doc, "Weight");
			if (StringUtils.isNotBlank(weightString)) {
				Pattern weightPattern = Pattern.compile("(?<weight>\\d+\\.\\d+)Kg.*");
				Matcher weightMatcher = weightPattern.matcher(weightString);
				if (weightMatcher.matches()) {
					String weight = weightMatcher.group("weight");
					if (StringUtils.isNotBlank(weight)) {
						brickset.setWeight(new BigDecimal(weight));
					}
				}
			}
			log.debug("Extracted weight: {}", brickset.getWeight());

			log.debug("Extracted barcodes: {}", brickset.getBarCodes());
			log.debug("Extracted lego item numbers: {}", brickset.getLegoItemNumbers());

			brickset.setAvailability(extractBricksetDescription(doc, "Availability"));
			log.debug("Extracted availability: '{}'", brickset.getAvailability());

			UriValuePair<BigDecimal> rating = new UriValuePair<>();
			Element ratingElement = extractBricksetDescriptionElement(doc, "Rating");
			String ratingValue = ratingElement.children().attr("title");
			if (StringUtils.isNotBlank(ratingValue)) {
				rating.setValue(new BigDecimal(ratingValue));
			}
			rating.setUri(ratingElement.children().attr("href"));
			brickset.setRating(rating);
			log.debug("Extracted rating: {}", brickset.getRating());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(MALFORMED_DOCUMENT);
		}

		return brickset;
	}

	private String extractBricksetDescription(Document doc, String descriptionName) {
		Element element = extractBricksetDescriptionElement(doc, descriptionName);
		return element == null ? null : element.text();
	}

	private String extractBricksetDescriptionUri(Document doc, String descriptionName) {
		String uri = null;

		Element element = extractBricksetDescriptionElement(doc, descriptionName);
		if (element != null) {
			Elements children = element.children();
			if (children != null && !children.isEmpty()) {
				uri = children.attr("abs:href");
			}
		}

		return uri;
	}

	private Element extractBricksetDescriptionElement(Document doc, String descriptionName) {
		String cssQuery = ".featurebox > div > dl > dt:matchesOwn(^" + descriptionName + "$) + dd";
		return doc.select(cssQuery).first();
	}

	public static void main(String[] args) {
		String url = "http://brickset.com/sets/7965-1/Millennium-Falcon";
		// String url = "http://brickset.com/sets/4501-1/Mos-Eisley-Cantina";

		BricksetParser bricksetParser = new BricksetParser();
		try {
			Brickset brickset = bricksetParser.parse(url);
			log.info("Parsed brickset: {}", brickset);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
