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
import hu.unideb.inf.brickset.model.CurrentValue;
import hu.unideb.inf.brickset.model.Dimensions;
import hu.unideb.inf.brickset.model.Price;
import hu.unideb.inf.brickset.model.UriValuePair;

public class BricksetParser {

	private static final Logger log = LoggerFactory.getLogger(BricksetParser.class);

	private static final String MALFORMED_DOCUMENT = "Malformed document";

	public Brickset parse(String url) throws IOException {
		// Document doc = Jsoup.connect(url).userAgent("Mozilla").cookie("PreferredCountry2",
		// "CountryCode=GB&amp;CountryName=United Kingdom").get();
		Document doc = Jsoup.connect(url).userAgent("Mozilla").cookie("PreferredCountry2", "CountryCode=HU&CountryName=Hungary").get();
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

			List<UriValuePair<String>> tags = new ArrayList<>();
			Element tagElement = extractBricksetDescriptionElement(doc, "Tags");
			if (tagElement != null) {
				Elements tagElements = tagElement.select("span > a");
				tagElements.forEach(tag -> {
					String tagUri = tag.absUrl("href");
					String tagValue = tag.text().trim();

					log.debug("tagUri: '{}', tagValue: '{}'", tagUri, tagValue);

					UriValuePair<String> tagPair = new UriValuePair<>();
					tagPair.setUri(tagUri);
					tagPair.setValue(tagValue);
					tags.add(tagPair);
				});
			}
			brickset.setTags(tags.toArray(new UriValuePair[tags.size()]));
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
			if (StringUtils.isNotBlank(multipleRrp)) {
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
			}
			log.debug("Extracted rrp: {}", Arrays.toString(brickset.getRrp()));

			CurrentValue currentValue = new CurrentValue();
			String currentValueString = extractBricksetDescription(doc, "Current value");
			log.debug("Current value to parse: '{}'", currentValueString);
			if (StringUtils.isNotBlank(currentValueString)) {
				// Garantált, hogy "Ft" lesz benne, mert küldöm a PreferredCountry2 nevű Cookie-ket a szervernek!
				Pattern pattern = Pattern.compile("New:(( ~Ft(?<newCurrentValue>\\d+(\\.\\d+)?))|.*).*Used:(( ~Ft(?<usedCurrentValue>\\d+(\\.\\d+)?))|.*)");
				Matcher matcher = pattern.matcher(currentValueString);
				if (matcher.matches()) {
					String newValue = matcher.group("newCurrentValue");
					String usedValue = matcher.group("usedCurrentValue");

					log.debug("Current value: new: '{}', used: '{}'", newValue, usedValue);

					if (StringUtils.isNotBlank(newValue)) {
						currentValue.setNewValue(new Price(new BigDecimal(newValue), "HUF"));
					}
					if (StringUtils.isNotBlank(usedValue)) {
						currentValue.setUsedValue(new Price(new BigDecimal(usedValue), "HUF"));
					}
				}
			}
			brickset.setCurrentValue(currentValue);
			log.debug("Extracted current value: {}", brickset.getCurrentValue());

			brickset.setPricePerPiece(extractBricksetDescription(doc, "Price per piece"));
			log.debug("Extracted price per piece: '{}'", brickset.getPricePerPiece());

			brickset.setAgeRange(extractBricksetDescription(doc, "Age range"));
			log.debug("Extracted age range: '{}'", brickset.getAgeRange());

			brickset.setPackaging(extractBricksetDescription(doc, "Packaging"));
			log.debug("Extracted packaging: '{}'", brickset.getPackaging());

			Dimensions dimensions = new Dimensions();
			String dimensionsString = extractBricksetDescription(doc, "Dimensions");
			if (StringUtils.isNotBlank(dimensionsString)) {
				Pattern pattern = Pattern.compile("(?<width>\\d+\\.\\d+) x (?<height>\\d+\\.\\d+) x (?<thickness>\\d+\\.\\d+) cm.*");
				Matcher matcher = pattern.matcher(dimensionsString);
				if (matcher.matches()) {
					String width = matcher.group("width");
					String height = matcher.group("height");
					String thickness = matcher.group("thickness");

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

			// TODO
			log.debug("Extracted barcodes: {}", brickset.getBarCodes());

			String legoItemNumbersString = extractBricksetDescription(doc, "LEGO item numbers");
			if (StringUtils.isNotBlank(legoItemNumbersString)) {
				Pattern legoItemNumbersPattern = Pattern.compile(".*: (?<legoItemNumber>\\d+)");
				Matcher legoItemNumbersMatcher = legoItemNumbersPattern.matcher(legoItemNumbersString);
				if (legoItemNumbersMatcher.matches()) {
					String legoItemNumber = legoItemNumbersMatcher.group("legoItemNumber");
					if (StringUtils.isNotBlank(legoItemNumber)) {
						brickset.setLegoItemNumbers(Long.valueOf(legoItemNumber));
					}
				}
			}
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
			Element childElemenet = element.select("a").first();
			if (childElemenet != null) {
				uri = childElemenet.absUrl("href");
			}
		}

		return uri;
	}

	private Element extractBricksetDescriptionElement(Document doc, String descriptionName) {
		String cssQuery = ".featurebox > div > dl > dt:matchesOwn(^" + descriptionName + "$) + dd";
		return doc.select(cssQuery).first();
	}

	public static void main(String[] args) {
//		 String url = "http://brickset.com/sets/7965-1/Millennium-Falcon";
		 String url = "http://brickset.com/sets/4501-1/Mos-Eisley-Cantina";
//		String url = "http://brickset.com/sets/30346-1/Prison-Island-Helicopter";

		try {
			Brickset brickset = new BricksetParser().parse(url);
			log.info("Parsed brickset: {}", brickset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
