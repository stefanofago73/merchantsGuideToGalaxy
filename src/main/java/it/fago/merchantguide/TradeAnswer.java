package it.fago.merchantguide;

public class TradeAnswer {
	//
	private final String answerText;

	public TradeAnswer(String answerText) {
		this.answerText = answerText;
	}

	public TradeAnswer(String patternForAnswer, Object ... values ) {
		this.answerText = String.format(patternForAnswer, values);
	}

	public String text() {
		return answerText;
	}

}// END
