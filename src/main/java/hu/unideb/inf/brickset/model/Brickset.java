package hu.unideb.inf.brickset.model;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Azért nem használok primitív típusokat, mert mi lenne akkor, ha nem létezik az adott érték az oldalon.
 */
public class Brickset {

	private String uri;

	private String number;

	private String name;

	private String type;

	private String themeGroup;

	private UriValuePair<String> theme;

	private UriValuePair<String> subTheme;

	private UriValuePair<Integer> yearReleased;

	private UriValuePair<String>[] tags;

	private UriValuePair<Long> pieces;

	private UriValuePair<Integer> minifigs;

	/**
	 * Recommended retail price
	 */
	private Price[] rrp;

	private CurrentValue currentValue;

	/**
	 * TODO ehhez biztos String kellene: "10.6p / 11.2c / 12.0c"?
	 */
	private String pricePerPiece;

	private String ageRange;

	private String packaging;

	private Dimensions dimensions;

	private BigDecimal weight;

	private Barcode[] barcodes;

	private Long legoItemNumbers;

	private String availability;

	private UriValuePair<BigDecimal> rating;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThemeGroup() {
		return themeGroup;
	}

	public void setThemeGroup(String themeGroup) {
		this.themeGroup = themeGroup;
	}

	public UriValuePair<String> getTheme() {
		return theme;
	}

	public void setTheme(UriValuePair<String> theme) {
		this.theme = theme;
	}

	public UriValuePair<String> getSubTheme() {
		return subTheme;
	}

	public void setSubTheme(UriValuePair<String> subTheme) {
		this.subTheme = subTheme;
	}

	public UriValuePair<Integer> getYearReleased() {
		return yearReleased;
	}

	public void setYearReleased(UriValuePair<Integer> yearReleased) {
		this.yearReleased = yearReleased;
	}

	public UriValuePair<String>[] getTags() {
		return tags;
	}

	public void setTags(UriValuePair<String>[] tags) {
		this.tags = tags;
	}

	public UriValuePair<Long> getPieces() {
		return pieces;
	}

	public void setPieces(UriValuePair<Long> pieces) {
		this.pieces = pieces;
	}

	public UriValuePair<Integer> getMinifigs() {
		return minifigs;
	}

	public void setMinifigs(UriValuePair<Integer> minifigs) {
		this.minifigs = minifigs;
	}

	public Price[] getRrp() {
		return rrp;
	}

	public void setRrp(Price[] rrp) {
		this.rrp = rrp;
	}

	public CurrentValue getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(CurrentValue currentValue) {
		this.currentValue = currentValue;
	}

	public String getPricePerPiece() {
		return pricePerPiece;
	}

	public void setPricePerPiece(String pricePerPiece) {
		this.pricePerPiece = pricePerPiece;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public Dimensions getDimensions() {
		return dimensions;
	}

	public void setDimensions(Dimensions dimensions) {
		this.dimensions = dimensions;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Barcode[] getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(Barcode[] barcodes) {
		this.barcodes = barcodes;
	}

	public Long getLegoItemNumbers() {
		return legoItemNumbers;
	}

	public void setLegoItemNumbers(Long legoItemNumbers) {
		this.legoItemNumbers = legoItemNumbers;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public UriValuePair<BigDecimal> getRating() {
		return rating;
	}

	public void setRating(UriValuePair<BigDecimal> rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Brickset [uri=");
		builder.append(uri);
		builder.append(", number=");
		builder.append(number);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", themeGroup=");
		builder.append(themeGroup);
		builder.append(", theme=");
		builder.append(theme);
		builder.append(", subTheme=");
		builder.append(subTheme);
		builder.append(", yearReleased=");
		builder.append(yearReleased);
		builder.append(", tags=");
		builder.append(Arrays.toString(tags));
		builder.append(", pieces=");
		builder.append(pieces);
		builder.append(", minifigs=");
		builder.append(minifigs);
		builder.append(", rrp=");
		builder.append(Arrays.toString(rrp));
		builder.append(", currentValue=");
		builder.append(currentValue);
		builder.append(", pricePerPiece=");
		builder.append(pricePerPiece);
		builder.append(", ageRange=");
		builder.append(ageRange);
		builder.append(", packaging=");
		builder.append(packaging);
		builder.append(", dimensions=");
		builder.append(dimensions);
		builder.append(", weight=");
		builder.append(weight);
		builder.append(", barcodes=");
		builder.append(Arrays.toString(barcodes));
		builder.append(", legoItemNumbers=");
		builder.append(legoItemNumbers);
		builder.append(", availability=");
		builder.append(availability);
		builder.append(", rating=");
		builder.append(rating);
		builder.append("]");
		return builder.toString();
	}

}
