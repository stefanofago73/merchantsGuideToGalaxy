package it.fago.merchantguide.impl;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;

import it.fago.merchantguide.Parser;
import it.fago.merchantguide.ParsingSummary;
import it.fago.merchantguide.TradeContext;
import it.fago.merchantguide.TradeQuery;
import it.fago.merchantguide.constants.Metal;
import it.fago.merchantguide.constants.RomanNumeral;

public class ParserImpl implements Parser {

	private static final Logger logger = getLogger(ParserImpl.class);

	private HashMap<String, RomanNumeral> symbolsCache = new HashMap<>();

	private HashMap<Metal, ConversionRule> rulesCache = new HashMap<>();

	private ArrayList<TradeQuery> queriesCache = new ArrayList<>();

	private volatile boolean initialized;

	private ParserHelper helper = new ParserHelper();

	@Override
	public Parser parse(InputStream inputStream) {
		new LineNumberReader(new InputStreamReader(inputStream))
		.lines()
		  .map(this::dispatchLine)
		     .collect(toList())
				.forEach(Runnable::run);

		initialized = true;
		return this;
	}

	@Override
	public Optional<TradeContext> createContext() {
		return initialized ? Optional.of(new TradeContextImpl(rulesCache)) : Optional.empty();
	}

	@Override
	public void destroy() {
		initialized = false;
		rulesCache.clear();
		symbolsCache.clear();
		queriesCache.clear();
	}

	public Map<Metal, ConversionRule> rules() {
		return rulesCache;
	}

	public Map<String, RomanNumeral> symbols() {
		return symbolsCache;
	}

	@Override
	public List<TradeQuery> queries() {
		return queriesCache;
	}

	@Override
	public ParsingSummary parsingSummary() {
		return helper.summary();
	}

	// =======================================================
	//
	// Main Callback - Internal
	//
	// =======================================================

	protected Runnable dispatchLine(String line) {
		if (helper.isSymbolLine(line)) {
			return helper.defineSymbol(symbolsCache, line);
		}
		if (helper.isConversionRuleLine(line)) {
			return helper.defineRule(symbolsCache, rulesCache, line);
		}
		if (helper.isQuery(line)) {
			return helper.defineQuery(symbolsCache, queriesCache, line);
		}
		return helper.failedLineTask();
	}

}// END