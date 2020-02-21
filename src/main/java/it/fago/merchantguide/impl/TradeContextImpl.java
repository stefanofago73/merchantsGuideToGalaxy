package it.fago.merchantguide.impl;

import java.util.HashMap;
import java.util.Map;

import it.fago.merchantguide.TradeAnswer;
import it.fago.merchantguide.TradeContext;
import it.fago.merchantguide.TradeQuery;
import it.fago.merchantguide.constants.Metal;

public class TradeContextImpl implements TradeContext {
	//
	private Map<Metal, ConversionRule> metalToRule; 

	public TradeContextImpl(Map<Metal, ConversionRule> rules) {
		metalToRule = rules==null?new HashMap<>():rules;
	}

	public void destroy() {
		metalToRule.clear();
	}

	public TradeAnswer answerFor(TradeQuery query) {
				
		if (query.isValid()) {
			if (query.metal() == Metal.Invalid) {
				return answerFor(query.sourceText(), query.numerals());
			}
			ConversionRule conversionRule = metalToRule.get(query.metal());
			if (conversionRule != null) {
				int conversion = conversionRule.convert(query.numerals());
				if (conversion == -1) {
					return dontKnow();
				}
				return creditsFor(query.sourceText(), conversion);
			}
		}
		return dontKnow();
	}

	// =============================================================
	//
	//
	//
	// ============================================================

	private final TradeAnswer dontKnow() {
		return new TradeAnswer("I have no idea what you are talking about");
	}

	private final TradeAnswer creditsFor(String sourceText, int credits) {
		return new TradeAnswer("%s is %s Credits", sourceText, credits);
	}

	private final TradeAnswer answerFor(String sourceText, int numerals) {
		return new TradeAnswer("%s is %s", sourceText, numerals);
	}

}// END