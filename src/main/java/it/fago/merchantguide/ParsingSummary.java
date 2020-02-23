package it.fago.merchantguide;

import java.util.Collections;
import java.util.List;

public class ParsingSummary {
//
	private final int parsed;
//	
	private final int failed;
//	
	private final List<String> symbolsNotFound;
	
	public ParsingSummary(int parsed, int failed, List<String> symbolsNotFound){
		this.parsed=parsed;
		this.failed=failed;
		this.symbolsNotFound=Collections.unmodifiableList(symbolsNotFound);
	}
	
	
	public int linesParsed(){
		return parsed;
	}
	
	public int linesFailed(){
		return failed;
	}
	
	public int warnings(){
		return symbolsNotFound.size();
	}
	
	public List<String> simbolsNotFound(){
		return symbolsNotFound;
	}
	
}//END
