package it.fago.merchantguide.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum RomanNumeral {

	I(1), V(5), X(10), L(50), C(100), D(500), M(1000), Invalid(-1);

	private static HashMap<String, RomanNumeral> cached = new HashMap<>();

	static {
		for (RomanNumeral numeral : RomanNumeral.values()) {
			cached.putIfAbsent(numeral.name(), numeral);
		}
	}
	private int decimalValue;

	private RomanNumeral(int decimalValue) {
		this.decimalValue = decimalValue;
	}

	public int decimalValue() {
		return decimalValue;
	}

	public static final RomanNumeral lookup(String text) {
		RomanNumeral m;
		return (m = cached.get(text)) == null ? Invalid : m;
	}

	public static int toDecimal(List<RomanNumeral> numerals) {
		int size;
		if (numerals == null || (size = numerals.size()) == 0) {
			return -1;
		}
		RomanNumeral[] elements = numerals.toArray(new RomanNumeral[0]);
		
		int num = 0;
		int decimalNum = 0;
		int previousnum = 0;
		for (int i = size - 1; i >= 0; i--) {
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