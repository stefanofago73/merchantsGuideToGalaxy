package it.fago.merchantguide.impl;

import static it.fago.merchantguide.constants.RomanNumeral.Invalid;
import static it.fago.merchantguide.constants.RomanNumeral.lookup;
import static it.fago.merchantguide.constants.RomanNumeral.toDecimal;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;

import it.fago.merchantguide.ParsingSummary;
import it.fago.merchantguide.TradeQuery;
import it.fago.merchantguide.constants.Metal;
import it.fago.merchantguide.constants.RomanNumeral;

class ParserHelper {

	private static Logger logger = getLogger(ParserHelper.class.getName());
	
	private Pattern symbolPattern = Pattern.compile("([a-z]+) means ([I|V|X|L|C|D|M])");

	private Pattern creditPattern = Pattern
			.compile("([a-z\\s]+)units of (Silver|Iron|Gold) are worth (\\d+)\\sCredits");

	private Pattern isQueryPattern = Pattern.compile("(how\\s)(much|many)\\s(Credits\\s?)?(is\\s?)?(.+)(\\s\\?)");
	
	private Pattern queryMetal = Pattern.compile("(.+)(Silver|Iron|Gold)(.*)");
	
	private int failed=0;
	
	private int parsed=0;
		
	private final Runnable failedLineTask = () -> failed++;
	
	private ArrayList<String> galaxiansNotFound = new ArrayList<>();
	
	
	
	
	boolean isSymbolLine(String line) {
		return symbolPattern.matcher(line).matches();
	}

	boolean isConversionRuleLine(String line) {
		return creditPattern.matcher(line).matches();
	}

	boolean isQuery(String line) {
		return isQueryPattern.matcher(line).matches();
	}
	
	Runnable defineSymbol(Map<String,RomanNumeral> symbolsCache,String line) {
		updateParsedLines();
		Matcher matcher = symbolPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 2) {
				return failedLineTask;
			}
			return createSymbolTask(symbolsCache,matcher.group(1), matcher.group(2));
		}
		return failedLineTask;
	}

	Runnable defineRule(Map<String,RomanNumeral> symbolsCache, Map<Metal, ConversionRule> rulesCache,String line) {
		updateParsedLines();
		Matcher matcher = creditPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 3) {
				return failedLineTask;
			}
			return createRuleTask(symbolsCache,rulesCache,matcher.group(1), matcher.group(2), matcher.group(3));
		}
		return failedLineTask;
	}

	Runnable defineQuery(Map<String,RomanNumeral> symbolsCache,ArrayList<TradeQuery> queriesCache,String line) {
		updateParsedLines();
		Matcher matcher = isQueryPattern.matcher(line);
		if (matcher.matches()) {
			int groupCount = matcher.groupCount();
			if (groupCount < 6) {
				return failedLineTask;
			}
			return createQueryTask(symbolsCache,queriesCache,matcher.group(5));
		}
		return failedLineTask;
	}
	
    Runnable createSymbolTask(Map<String,RomanNumeral> symbolsCache,String word, String letteral) {
		return () -> symbolsCache.put(word, lookup(letteral));
	}
	
    
    Runnable createRuleTask(Map<String,RomanNumeral> symbolsCache, Map<Metal, ConversionRule> rulesCache, String symbols, String metal, String credits) {
		return () -> {
			List<RomanNumeral> items = 
					Stream.of(symbols.split("\\s"))
					.map(symbol -> retrieveRoman(symbolsCache,symbols,symbol))
					  .collect(toList());
			ConversionRule rule = new ConversionRule(toDecimal(items), Metal.lookup(metal), Integer.valueOf(credits));
			rulesCache.put(rule.metal(),rule);
		};
	}
    
    
    protected Runnable createQueryTask(Map<String,RomanNumeral> symbolsCache,ArrayList<TradeQuery> queriesCache, final String data) {
		return () -> {
						
			String [] symbolsAndMetal = symbolsAndMetal(data);
			StringBuilder sb = new StringBuilder();
			String[] elements = symbolsAndMetal[0].split("\\s");
			RomanNumeral value = null;
			for (String element : elements) {
				if( (value=retrieveRoman(symbolsCache,data, element))==Invalid){
					continue;
				}
				sb.append(value.name());
			}

			int fromNumerals = RomanNumeral.toDecimal(sb.toString());
			queriesCache.add(new TradeQuery(data, Metal.lookup(symbolsAndMetal[1]), fromNumerals));
		};
	}
    
    
    Runnable failedLineTask() {
    	return failedLineTask;
    }
    
    
    ParsingSummary summary(){
    	return new ParsingSummary(parsed, failed, galaxiansNotFound);
    }
    //=========================================================================
    //
    //
    //
    //=========================================================================
    
    private RomanNumeral retrieveRoman(Map<String,RomanNumeral> symbolsCache, String source, String letteral){
		RomanNumeral result;
		if((result=symbolsCache.get(letteral))==null){
			logger.warn("Galaxian Symbol not found: [{}] - sequence: [{}] ",letteral,source);
			updateGalaxianLetteralNotFound(letteral);
			return Invalid;
		}
		return result;
	}
    
    private String [] symbolsAndMetal(String input){
		Matcher matcher = queryMetal.matcher(input);
		if(!matcher.matches()){
			return new String[]{input,""};
		}
		return new String[]{matcher.group(1),matcher.group(2)};
	}
    
   private void updateParsedLines(){
	   parsed++;
   }
   
   private void updateGalaxianLetteralNotFound(String letteral){
	    galaxiansNotFound.add(letteral);
   }

   
}//END
