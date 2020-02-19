package it.fago.merchantguide;

public interface TradeContext {

	public TradeAnswer answerFor(TradeQuery query);
	
	public void destroy();
	
}//END