package it.fago.merchantguide.constants;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RomanNumeral {

	I(1, 'I'), V(5, 'V'), X(10, 'X'), L(50, 'L'), C(100, 'C'), D(500, 'D'), M(1000, 'M'), Invalid(-1, '_');

	private static final HashMap<String, RomanNumeral> cached = new HashMap<>();

	private static final HashMap<Character, RomanNumeral> cachedByChar = new HashMap<>();

	private static final Pattern romansValidator = Pattern
			.compile("(?<=^)M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})(?=$)");

	private static final Logger logger = LoggerFactory.getLogger(RomanNumeral.class);

	static {
		for (RomanNumeral numeral : RomanNumeral.values()) {
			cached.putIfAbsent(numeral.name(), numeral);
			cachedByChar.putIfAbsent(numeral.charLetteral(), numeral);
		}
	}
	private int decimalValue;

	private char charLetteral;

	private RomanNumeral(int decimalValue, char charLetteral) {
		this.decimalValue = decimalValue;
		this.charLetteral = charLetteral;
	}

	public int decimalValue() {
		return decimalValue;
	}

	public char charLetteral() {
		return charLetteral;
	}

	/**
	 * Used To Avoid Exception as for Enum.valueOf
	 * 
	 * @param text
	 * @return RomanNumeral
	 */
	public static final RomanNumeral lookup(String text) {
		RomanNumeral m;
		return (m = cached.get(text)) == null ? Invalid : m;
	}

	/**
	 * 
	 * Used To Avoid Exception as for Enum.valueOf
	 * Lookup by character
	 * 
	 * @param letteral
	 * @return
	 */
	public static final RomanNumeral lookupByChar(char letteral) {
		RomanNumeral m;
		return (m = cachedByChar.get(letteral)) == null ? Invalid : m;
	}

	
	public static int toDecimal(List<RomanNumeral> numerals) {
		if (numerals == null || numerals.size() == 0) {
			return 0;
		}
		StringBuilder buffer = new StringBuilder();
		for (RomanNumeral romanNumeral : numerals) {
			buffer.append(romanNumeral);
		}
		return toDecimal(buffer.toString());
	}

	public static int toDecimal(String numerals) {
		String workData = numerals;
		int size;
		if (workData == null || (size = workData.length()) == 0) {
			return 0;
		}

		if (!romansValidator.matcher(workData).matches()) {
			logger.warn("INVALID SEQUENCE: [{}] size: {}", workData, size);
			return 0;
		}

		RomanNumeral[] elements = workData
				.chars()
				   .mapToObj(c->RomanNumeral.lookupByChar((char)c))
				.collect(Collectors.toList())
			.toArray(new RomanNumeral[size]);

		int num = 0;
		int decimalNum = 0;
		int previousnum = 0;
		for (int i = size - 1; i >= 0; i--) {
			if (elements[i] == null) {
				continue;
			}
			switch (elements[i]) {
			case I:
				previousnum = num;
				num = I.decimalValue();
				break;
			case V:
				previousnum = num;
				num = V.decimalValue();
				break;
			case X:
				previousnum = num;
				num = X.decimalValue();
				break;
			case L:
				previousnum = num;
				num = L.decimalValue();
				break;
			case C:
				previousnum = num;
				num = C.decimalValue();
				break;
			case D:
				previousnum = num;
				num = D.decimalValue();
				break;
			case M:
				previousnum = num;
				num = M.decimalValue();
				break;
			case Invalid:
				continue;
			default:
				continue;
			}

			if (num < previousnum) {
				decimalNum = decimalNum - num;
			} else {
				decimalNum = decimalNum + num;
			}
		}
		return decimalNum;
	}

}// END