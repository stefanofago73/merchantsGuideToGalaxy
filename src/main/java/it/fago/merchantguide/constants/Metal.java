package it.fago.merchantguide.constants;

import java.util.HashMap;

public enum Metal {
	Silver, Gold, Iron, Invalid;

	private static HashMap<String, Metal> cached = new HashMap<>();

	static {
		for (Metal metal : Metal.values()) {
			cached.putIfAbsent(metal.name(), metal);
		}
	}

	public static final Metal lookup(String text) {
		Metal m;
		return (m = cached.get(text)) == null ? Invalid : m;
	}

}// END
