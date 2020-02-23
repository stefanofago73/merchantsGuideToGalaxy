package it.fago.merchantguide.test;

import static it.fago.merchantguide.test.SampleData.complete1;
import static it.fago.merchantguide.test.SampleData.completeCorrupted;
import static it.fago.merchantguide.test.SampleData.completeEmpty;
import static it.fago.merchantguide.test.SampleData.from;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import it.fago.merchantguide.ParsingSummary;
import it.fago.merchantguide.TradeContext;

public class TestTradeContext extends BaseTest {

	@Test
	public void testEmpty() {

		Optional<TradeContext> ctx = parser().parse(from(completeEmpty())).createContext();
		ctx.ifPresent(this::answering);

		ParsingSummary summary = parser().parsingSummary();
		assertEquals("Parsed Lines", 0, summary.linesParsed());
		assertEquals("Failed Lines", 0, summary.linesFailed());
		assertEquals("Warning Lines", 0, summary.warnings());
	}

	@Test
	public void testComplete() {

		Optional<TradeContext> ctx = parser().parse(from(complete1())).createContext();
		ctx.ifPresent(this::answering);

		ParsingSummary summary = parser().parsingSummary();
		
		assertEquals("Parsed Lines", 12, summary.linesParsed());
		assertEquals("Failed Lines", 0, summary.linesFailed());
		assertEquals("Warning Lines", 11, summary.warnings());
	}

	
	@Test
	public void testCorrupted() {

		Optional<TradeContext> ctx = parser().parse(from(completeCorrupted())).createContext();
		ctx.ifPresent(this::answering);

		ParsingSummary summary = parser().parsingSummary();
		
		assertEquals("Parsed Lines", 11, summary.linesParsed());
		assertEquals("Failed Lines", 0, summary.linesFailed());
		assertEquals("Warning Lines", 14, summary.warnings());
	}

	// ========================================
	//
	//
	//
	// ========================================

	protected void answering(TradeContext ctx) {

		parser()
		  .queries()
		  .stream()
		  .map(ctx::answerFor)
		  .collect(Collectors.toList())
				.forEach(a -> System.out.println(a.text()));

		ctx.destroy();
	}

}// END
