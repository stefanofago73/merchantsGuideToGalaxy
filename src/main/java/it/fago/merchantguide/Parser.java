package it.fago.merchantguide;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface Parser {

	public Parser parse(InputStream inputStream);

	public Optional<TradeContext> createContext();

	public List<TradeQuery> queries();

	public void destroy();

	public int linesParsed();
	
	public int linesFailed();
	
}// END
