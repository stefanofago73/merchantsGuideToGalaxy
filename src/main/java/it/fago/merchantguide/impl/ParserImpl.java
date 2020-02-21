package it.fago.merchantguide.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fago.merchantguide.Parser;
import it.fago.merchantguide.TradeContext;
import it.fago.merchantguide.TradeQuery;
import it.fago.merchantguide.constants.Metal;
import it.fago.merchantguide.constants.RomanNumeral;

public class ParserImpl implements Parser {

	private static final Logger logger = LoggerFactory.getLogger(ParserImpl.class);
	
	private Pattern symbolPattern = Pattern.compile("([a-z]+) means ([I|V|X|L|C|D|M])");

	private Pattern creditPattern = Pattern
			.compile("([a-z\\s]+)units of (Silver|Iron|Gold) are worth (\\d+)\\sCredits");

	private Pattern isQueryPattern = Pattern.compile("(how\\s)(much|many)\\s(Credits\\s?)?(is\\s?)?(.+)(\\s\\?)");
	
	private Pattern queryMetal = Pattern.compile("(.+)(Silver|Iron|Gold)(.*)");

	private HashMap<String, RomanNumeral> symbolsCache = new HashMap<>();

	private HashMap<Metal, ConversionRule> rulesCache = new HashMap<>();

	private ArrayList<TradeQuery> queriesCache = new ArrayList<>();

	private volatile boolean initialized;
	
	private int failed=0;
	
	private int parsed=0;
	
	private final Runnable failedLineTask = () -> failed++;

	
	public Parser parse(InputStream inputStream) {

		new LineNumberReader(new InputStreamReader(inputStream))
		.lines()
		  .map(this::dispatchLine)
	        .collect(Collectors.toList())
	     .forEach(Runnable::run);

		initialized=true;
		return this;
	}

	public Optional<TradeContext> createContext() {
		return initialized?Optional.of(new TradeContextImpl(rulesCache)):Optional.empty();
	}

	public void destroy() {
		initialized = false;
	}

	public Map<Metal, ConversionRule> rules() {
		return rulesCache;
	}

	public Map<String, RomanNumeral> symbols() {
		return symbolsCache;
	}

	public List<TradeQuery> queries() {
		return queriesCache;
	}

	public int linesParsed(){
		return parsed;
	}
	
	public int linesFailed(){
		return failed;
	}
	// =======================================================
	//
	// Main Callback - Internal
	//
	// =======================================================

	protected Runnable dispatchLine(String line) {
		parsed++;
		if (isSymbolLine(line)) {
		    return defineSymbol(line);
		}
		if (isConversionRuleLine(line)) {
			return defineRule(line);
		}
		if (isQuery(line)) {
			return defineQuery(line);
		}
		return failedLineTask;
	}

	// =======================================================
	//
	// Internals : Line Type Verification
	//
	// =======================================================

	protected boolean isSymbolLine(String line) {
		return symbolPattern.matcher(line).matches();
	}

	protected boolean isConversionRuleLine(String line) {
		return creditPattern.matcher(line).matches();
	}

	protected boolean isQuery(String line) {
		return isQueryPattern.matcher(line).matches();
	}
	
	// =======================================================
	//
	// Internals : Element Definitions
	//
	// =======================================================
	
	protected Runnable defineSymbol(String line) {
		Matcher matcher = symbolPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 2) {
				return failedLineTask;
			}
			return createSymbolTask(matcher.group(1), matcher.group(2));
		}
		return failedLineTask;
	}

	protected Runnable defineRule(String line) {
		Matcher matcher = creditPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 3) {
				return failedLineTask;
			}
			return createRuleTask(matcher.group(1), matcher.group(2), matcher.group(3));
		}
		return failedLineTask;
	}

	protected Runnable defineQuery(String line) {
		Matcher matcher = isQueryPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 6) {
				return failedLineTask;
			}
			return createQueryTask(matcher.group(5));
		}
		return failedLineTask;
	}

	// =======================================================
	//
	// Internals : Tasks
	//
	// =======================================================
	
	private String [] symbolsAndMetal(String input){
		Matcher matcher = queryMetal.matcher(input);
		if(!matcher.matches()){
			return new String[]{input,""};
		}
		return new String[]{matcher.group(1),matcher.group(2)};
	}
	
	protected Runnable createSymbolTask(String letteral, String value) {
		return () -> symbolsCache.put(letteral, RomanNumeral.lookup(value));
	}

	protected Runnable createRuleTask(String symbols, String metal, String credits) {
		return () -> {
			List<RomanNumeral> items = 
					Stream.of(symbols.split("\\s"))
					.map(symbol -> symbolsCache.getOrDefault(symbol, RomanNumeral.Invalid))
					  .collect(Collectors.toList());
			ConversionRule rule = new ConversionRule(RomanNumeral.toDecimal(items), Metal.lookup(metal), Integer.valueOf(credits));
			rulesCache.put(rule.metal(),rule);
		};
	}

	protected Runnable createQueryTask(final String data) {
		return () -> {
						
			String [] symbolsAndMetal = symbolsAndMetal(data);
			StringBuilder sb = new StringBuilder();
			String[] elements = symbolsAndMetal[0].split("\\s");
			RomanNumeral value = null;
			for (String element : elements) {
				if( (value=symbolsCache.get(element))==null){
					continue;
				}
				sb.append(value.name());
			}

			int fromNumerals = RomanNumeral.toDecimal(sb.toString());
			queriesCache.add(new TradeQuery(data, Metal.lookup(symbolsAndMetal[1]), fromNumerals));
		};
	}

}// END