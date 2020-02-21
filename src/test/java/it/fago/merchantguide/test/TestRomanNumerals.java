package it.fago.merchantguide.test;

import static it.fago.merchantguide.constants.RomanNumeral.C;
import static it.fago.merchantguide.constants.RomanNumeral.I;
import static it.fago.merchantguide.constants.RomanNumeral.L;
import static it.fago.merchantguide.constants.RomanNumeral.M;
import static it.fago.merchantguide.constants.RomanNumeral.X;
import static it.fago.merchantguide.constants.RomanNumeral.toDecimal;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import it.fago.merchantguide.constants.RomanNumeral;

public class TestRomanNumerals extends BaseTest {

	@Test
	public void testParsing2DigitNumber() {
		int val1 = toDecimal(asList(X, X));
		int val2 = toDecimal(asList(X, C));
		int val3 = toDecimal(asList(C, C));
		int val4 = toDecimal(asList(C, M));
		assertEquals("val1", 20, val1);
		assertEquals("val2", 90, val2);
		assertEquals("val3", 200, val3);
		assertEquals("val4", 900, val4);
	}

	@Test
	public void testParsing4DigitNumber() {
		int val1 = toDecimal(asList(X, I, X));
		int val2 = toDecimal(asList(M, C, M, I));
		int val3 = toDecimal(asList(C, C, C, X));
		int val4 = toDecimal(asList(M, M, I, I));
		assertEquals("val1", 19, val1);
		assertEquals("val2", 1901, val2);
		assertEquals("val3", 310, val3);
		assertEquals("val4", 2002, val4);
	}

	@Test
	public void testWrongNumbers() {
		int val1 = toDecimal(asList(M, M, X, X, X, L, I, I, I));
		int val2 = toDecimal(asList(X, X, X, X, X, X, X, X, X));
		assertEquals("val1", 0, val1);
		assertEquals("val2", 0, val2);
	}

	// =====================================
	//
	//
	// =====================================

	protected List<RomanNumeral> asList(RomanNumeral... numerals) {
		return Arrays.asList(numerals);
	}

}// END
