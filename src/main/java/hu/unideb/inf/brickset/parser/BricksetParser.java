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

import hu.unideb.inf.brickset.model.Barcode;
import hu.unideb.inf.brickset.model.Brickset;
import hu.unideb.inf.brickset.model.CurrentValue;
import hu.unideb.inf.brickset.model.Dimensions;
import hu.unideb.inf.brickset.model.Price;
import hu.unideb.inf.brickset.model.UriValuePair;
import hu.unideb.inf.jaxb.JAXBUtil;

public class BricksetParser {

	private static final Logger log = LoggerFactory.getLogger(BricksetParser.class);

	private static final String MALFORMED_DOCUMENT = "Malformed document";

	public Brickset parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent("Mozilla")
				.cookie("PreferredCountry2", "CountryCode=HU&CountryName=Hungary").get();
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
			parseSetNumber(doc, brickset);
			parseName(doc, brickset);
			parseSetType(doc, brickset);
			parseThemeGroup(doc, brickset);
			parseTheme(doc, brickset);
			parseSubTheme(doc, brickset);
			parseYearReleased(doc, brickset);
			parseTags(doc, brickset);
			parsePieces(doc, brickset);
			parseMinifigs(doc, brickset);
			parseRrp(doc, brickset);
			parseCurrentValue(doc, brickset);
			parsePricePerPiece(doc, brickset);
			parseAgeRange(doc, brickset);
			parsePackaging(doc, brickset);
			parseDimensions(doc, brickset);
			parseWeight(doc, brickset);
			parseBarcodes(doc, brickset);
			parseLegoItemNumbers(doc, brickset);
			parseAvailability(doc, brickset);
			parseRating(doc, brickset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(MALFORMED_DOCUMENT);
		}

		return brickset;
	}

	private void parseSetNumber(Document doc, Brickset brickset) {
		brickset.setNumber(extractBricksetDescription(doc, "Set number"));
		log.debug("Extracted set number: '{}'", brickset.getNumber());
	}

	private void parseName(Document doc, Brickset brickset) {
		brickset.setName(extractBricksetDescription(doc, "Name"));
		log.debug("Extracted name: '{}'", brickset.getName());
	}

	private void parseSetType(Document doc, Brickset brickset) {
		brickset.setType(extractBricksetDescription(doc, "Set type"));
		log.debug("Extracted set type: '{}'", brickset.getType());
	}

	private void parseThemeGroup(Document doc, Brickset brickset) {
		brickset.setThemeGroup(extractBricksetDescription(doc, "Theme group"));
		log.debug("Extracted theme group: '{}'", brickset.getThemeGroup());
	}

	private void parseTheme(Document doc, Brickset brickset) {
		UriValuePair<String> theme = new UriValuePair<>();
		theme.setUri(extractBricksetDescriptionUri(doc, "Theme"));
		theme.setValue(extractBricksetDescription(doc, "Theme"));
		brickset.setTheme(theme);
		log.debug("Extracted theme: {}", brickset.getTheme());
	}

	private void parseSubTheme(Document doc, Brickset brickset) {
		UriValuePair<String> subTheme = new UriValuePair<>();
		subTheme.setUri(extractBricksetDescriptionUri(doc, "Subtheme"));
		subTheme.setValue(extractBricksetDescription(doc, "Subtheme"));
		brickset.setSubTheme(subTheme);
		log.debug("Extracted subtheme: {}", brickset.getSubTheme());
	}

	private void parseYearReleased(Document doc, Brickset brickset) {
		UriValuePair<Integer> yearReleased = new UriValuePair<>();
		yearReleased.setUri(extractBricksetDescriptionUri(doc, "Year released"));
		String yearReleasedValue = extractBricksetDescription(doc, "Year released");
		yearReleased.setValue(yearReleasedValue == null ? null : Integer.valueOf(yearReleasedValue));
		brickset.setYearReleased(yearReleased);
		log.debug("Extracted year released: {}", brickset.getYearReleased());
	}

	@SuppressWarnings("unchecked")
	private void parseTags(Document doc, Brickset brickset) {
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
	}

	private void parsePieces(Document doc, Brickset brickset) {
		UriValuePair<Long> pieces = new UriValuePair<>();
		pieces.setUri(extractBricksetDescriptionUri(doc, "Pieces"));
		String piecesValue = extractBricksetDescription(doc, "Pieces");
		pieces.setValue(piecesValue == null ? null : Long.valueOf(piecesValue));
		brickset.setPieces(pieces);
		log.debug("Extracted pieces: {}", brickset.getPieces());
	}

	private void parseMinifigs(Document doc, Brickset brickset) {
		UriValuePair<Integer> minifigs = new UriValuePair<>();
		minifigs.setUri(extractBricksetDescriptionUri(doc, "Minifigs"));
		String minifigsValue = extractBricksetDescription(doc, "Minifigs");
		minifigs.setValue(minifigsValue == null ? null : Integer.valueOf(minifigsValue));
		brickset.setMinifigs(minifigs);
		log.debug("Extracted minifigs: {}", brickset.getMinifigs());
	}

	private void parseRrp(Document doc, Brickset brickset) {
		String multipleRrp = extractBricksetDescription(doc, "RRP");
		if (StringUtils.isNotBlank(multipleRrp)) {
			Pattern pattern = Pattern.compile(
					"(£(?<pounds>\\d+\\.\\d+))?( / )?(\\$(?<dollars>\\d+\\.\\d+))?( / )?((?<euros>\\d+\\.\\d+)€)?");
			Matcher matcher = pattern.matcher(multipleRrp);
			List<Price> rrp = new ArrayList<>();
			if (matcher.matches()) {
				String pounds = matcher.group("pounds");
				String dollars = matcher.group("dollars");
				String euros = matcher.group("euros");

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
	}

	private void parseCurrentValue(Document doc, Brickset brickset) {
		CurrentValue currentValue = new CurrentValue();
		String currentValueString = extractBricksetDescription(doc, "Current value");
		log.debug("Current value to parse: '{}'", currentValueString);
		if (StringUtils.isNotBlank(currentValueString)) {
			// Garantált, hogy "Ft" lesz benne, mert küldöm a PreferredCountry2 nevű Cookie-ket a szervernek!
			Pattern pattern = Pattern.compile(
					"New:(( ~Ft(?<newCurrentValue>\\d+(\\.\\d+)?))|.*).*Used:(( ~Ft(?<usedCurrentValue>\\d+(\\.\\d+)?))|.*)");
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
	}

	private void parsePricePerPiece(Document doc, Brickset brickset) {
		brickset.setPricePerPiece(extractBricksetDescription(doc, "Price per piece"));
		log.debug("Extracted price per piece: '{}'", brickset.getPricePerPiece());
	}

	private void parseAgeRange(Document doc, Brickset brickset) {
		brickset.setAgeRange(extractBricksetDescription(doc, "Age range"));
		log.debug("Extracted age range: '{}'", brickset.getAgeRange());
	}

	private void parsePackaging(Document doc, Brickset brickset) {
		brickset.setPackaging(extractBricksetDescription(doc, "Packaging"));
		log.debug("Extracted packaging: '{}'", brickset.getPackaging());
	}

	private void parseDimensions(Document doc, Brickset brickset) {
		Dimensions dimensions = new Dimensions();
		String dimensionsString = extractBricksetDescription(doc, "Dimensions");
		if (StringUtils.isNotBlank(dimensionsString)) {
			Pattern pattern = Pattern
					.compile("(?<width>\\d+\\.\\d+) x (?<height>\\d+\\.\\d+) x (?<thickness>\\d+\\.\\d+) cm.*");
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
	}

	private void parseWeight(Document doc, Brickset brickset) {
		String weightString = extractBricksetDescription(doc, "Weight");
		if (StringUtils.isNotBlank(weightString)) {
			Pattern pattern = Pattern.compile("(?<weight>\\d+\\.\\d+)Kg.*");
			Matcher matcher = pattern.matcher(weightString);
			if (matcher.matches()) {
				String weight = matcher.group("weight");
				if (StringUtils.isNotBlank(weight)) {
					brickset.setWeight(new BigDecimal(weight));
				}
			}
		}
		log.debug("Extracted weight: {}", brickset.getWeight());
	}

	private void parseBarcodes(Document doc, Brickset brickset) {
		String barcodesString = extractBricksetDescription(doc, "Barcodes");
		if (StringUtils.isNotBlank(barcodesString)) {
			String upc = null;
			String ean = null;

			Pattern upcPattern = Pattern.compile(".*UPC: (?<UPC>\\d+).*");
			Matcher matcher = upcPattern.matcher(barcodesString);
			if (matcher.matches()) {
				upc = matcher.group("UPC");
			}

			Pattern eanPattern = Pattern.compile(".*EAN: (?<EAN>\\d+).*");
			matcher = eanPattern.matcher(barcodesString);
			if (matcher.matches()) {
				ean = matcher.group("EAN");
			}
			log.debug("Barcodes UPC: '{}', EAN: '{}'", upc, ean);

			List<Barcode> barcodes = new ArrayList<>();
			if (StringUtils.isNotBlank(upc)) {
				Barcode barcode = new Barcode();
				barcode.setType("UPC");
				barcode.setValue(upc);
				barcodes.add(barcode);
			}
			if (StringUtils.isNotBlank(ean)) {
				Barcode barcode = new Barcode();
				barcode.setType("EAN");
				barcode.setValue(ean);
				barcodes.add(barcode);
			}
			brickset.setBarcodes(barcodes.toArray(new Barcode[barcodes.size()]));
		}
		log.debug("Extracted barcodes: {}", Arrays.toString(brickset.getBarcodes()));
	}

	private void parseLegoItemNumbers(Document doc, Brickset brickset) {
		String legoItemNumbersString = extractBricksetDescription(doc, "LEGO item numbers");
		if (StringUtils.isNotBlank(legoItemNumbersString)) {
			Pattern pattern = Pattern.compile(".*: (?<legoItemNumber>\\d+)");
			Matcher matcher = pattern.matcher(legoItemNumbersString);
			if (matcher.matches()) {
				String legoItemNumber = matcher.group("legoItemNumber");
				if (StringUtils.isNotBlank(legoItemNumber)) {
					brickset.setLegoItemNumbers(Long.valueOf(legoItemNumber));
				}
			}
		}
		log.debug("Extracted lego item numbers: {}", brickset.getLegoItemNumbers());
	}

	private void parseAvailability(Document doc, Brickset brickset) {
		brickset.setAvailability(extractBricksetDescription(doc, "Availability"));
		log.debug("Extracted availability: '{}'", brickset.getAvailability());
	}

	private void parseRating(Document doc, Brickset brickset) {
		UriValuePair<BigDecimal> rating = new UriValuePair<>();
		Element ratingElement = extractBricksetDescriptionElement(doc, "Rating");
		String ratingValue = ratingElement.children().attr("title");
		if (StringUtils.isNotBlank(ratingValue)) {
			rating.setValue(new BigDecimal(ratingValue));
		}
		rating.setUri(ratingElement.children().attr("abs:href"));
		brickset.setRating(rating);
		log.debug("Extracted rating: {}", brickset.getRating());
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

	public static void main(String[] args) throws Exception {
		String url = "http://brickset.com/sets/7965-1/Millennium-Falcon";
		// String url = "http://brickset.com/sets/4501-1/Mos-Eisley-Cantina";
		// String url = "http://brickset.com/sets/30346-1/Prison-Island-Helicopter";

		Brickset brickset = new BricksetParser().parse(url);

		JAXBUtil.toXML(brickset, System.out);

		log.info("Parsed brickset: {}", brickset);
	}

}
