package it.fago.merchantguide.impl;

import java.util.function.Function;

import it.fago.merchantguide.constants.Metal;

public class ConversionRule {
	//
	private Metal metal;
	//
	private Function<Integer, Integer> conversion;

	ConversionRule(int fromNumerals, Metal metal, int credits) {
		conversion = fromNumerals==0?(input)->-1:(input) -> (credits * input) / fromNumerals;
		this.metal = metal;
	}

	public Metal metal() {
		return metal;
	}

	public int convert(int input) {
		return conversion.apply(input);
	}

}// END