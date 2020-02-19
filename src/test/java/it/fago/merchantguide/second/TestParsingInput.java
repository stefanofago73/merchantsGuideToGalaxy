package it.fago.merchantguide.second;

import static it.fago.merchantguide.constants.RomanNumeral.C;
import static it.fago.merchantguide.constants.RomanNumeral.D;
import static it.fago.merchantguide.constants.RomanNumeral.I;
import static it.fago.merchantguide.constants.RomanNumeral.L;
import static it.fago.merchantguide.constants.RomanNumeral.M;
import static it.fago.merchantguide.constants.RomanNumeral.V;
import static it.fago.merchantguide.constants.RomanNumeral.X;
import static it.fago.merchantguide.second.SampleData.complete1;
import static it.fago.merchantguide.second.SampleData.from;
import static it.fago.merchantguide.second.SampleData.numeralEmpty;
import static it.fago.merchantguide.second.SampleData.numeralSet1;
import static it.fago.merchantguide.second.SampleData.numeralSet2;
import static it.fago.merchantguide.second.SampleData.numeralSet3;
import static it.fago.merchantguide.second.SampleData.numeralsCorruptedAtTheEnd;
import static it.fago.merchantguide.second.SampleData.numeralsCorruptedAtTheStart;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import it.fago.merchantguide.Parser;
import it.fago.merchantguide.TradeQuery;
import it.fago.merchantguide.constants.Metal;
import it.fago.merchantguide.constants.RomanNumeral;
import it.fago.merchantguide.impl.ConversionRule;
import it.fago.merchantguide.impl.ParserImpl;

public class TestParsingInput extends BaseTest {

	@Test
	public void testNumeralSet1() {
		parser().parse(from(numeralSet1()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 4, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(I, V, X, L), numerals);
	}

	@Test
	public void testNumeralSet2() {
		parser().parse(from(numeralSet2()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 3, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(I, V, L), numerals);
	}

	@Test
	public void testNumeralSet3() {
		parser().parse(from(numeralSet3()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 5, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(I, L, C, D, M), numerals);
	}

	@Test
	public void testEmpty() {
		parser().parse(from(numeralEmpty()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 0, romans.size());
	}

	@Test
	public void testNumeralCorruptedAtTheStart() {
		parser().parse(from(numeralsCorruptedAtTheStart()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 3, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(L, C, M), numerals);
	}

	@Test
	public void testNumeralCorruptedAtTheEnd() {
		parser().parse(from(numeralsCorruptedAtTheEnd()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		assertNotNull("Symbols", symbols);
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 3, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(L, D, M), numerals);
	}

	@Test
	public void testComplete1() {
		parser().parse(from(complete1()));
		Map<String, RomanNumeral> symbols = asImpl(parser()).symbols();
		Map<Metal, ConversionRule> rules = asImpl(parser()).rules();
		List<TradeQuery> queries = asImpl(parser()).queries();
		Collection<RomanNumeral> romans = symbols.values();
		assertEquals("Symbols Size", 4, romans.size());
		RomanNumeral[] numerals = arrayFromCollection(romans);
		assertArrayEquals("Romans Verification by position", array(I, V, X, L), numerals);
		assertEquals("Rules Size", 3, rules.size());
		assertEquals("Queries Size", 5, queries.size());
	}

	// ========================================
	//
	//
	//
	// ========================================

	protected ParserImpl asImpl(Parser parser) {
		return (ParserImpl) parser;
	}

}// END