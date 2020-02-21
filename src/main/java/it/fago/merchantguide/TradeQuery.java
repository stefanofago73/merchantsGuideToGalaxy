package it.fago.merchantguide;

import it.fago.merchantguide.constants.Metal;

public class TradeQuery {
//
	private final Metal metal;
	//
	private final int numerals;
	//
	private final String sourceText;
	
  public TradeQuery(String sourceText, Metal metal, int numerals){
	this.metal=metal;
	this.numerals=numerals;
	this.sourceText =sourceText;
  }
  
  public boolean isValid(){
	  return numerals>0;
  }
  
  public Metal metal(){
	  return metal;
  }
  
  public int numerals(){
	  return numerals;
  }
  
  public String sourceText(){
	  return sourceText;
  }
  
}// END
