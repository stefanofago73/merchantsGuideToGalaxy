package it.fago.merchantguide.second;

import static it.fago.merchantguide.second.SampleData.complete1;
import static it.fago.merchantguide.second.SampleData.completeCorrupted;
import static it.fago.merchantguide.second.SampleData.completeEmpty;
import static it.fago.merchantguide.second.SampleData.from;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import it.fago.merchantguide.TradeContext;

public class TestTradeContext extends BaseTest {

	@Test
	public void testEmpty() {

		Optional<TradeContext> ctx = parser().parse(from(completeEmpty())).createContext();
		ctx.ifPresent(this::answering);

		assertEquals("Parsed Lines", 0, parser().linesParsed());
		assertEquals("Failed Lines", 0, parser().linesFailed());

	}

	@Test
	public void testComplete() {

		Optional<TradeContext> ctx = parser().parse(from(complete1())).createContext();
		ctx.ifPresent(this::answering);

		assertEquals("Parsed Lines", 12, parser().linesParsed());
		assertEquals("Failed Lines", 0, parser().linesFailed());

	}

	@Test
	public void testCorrupted() {

		Optional<TradeContext> ctx = parser().parse(from(completeCorrupted())).createContext();
		ctx.ifPresent(this::answering);

		assertEquals("Parsed Lines", 11, parser().linesParsed());
		assertEquals("Failed Lines", 0, parser().linesFailed());

	}

	// ========================================
	//
	//
	//
	// ========================================

	protected void answering(TradeContext ctx) {

		parser().queries().stream().map(ctx::answerFor).collect(Collectors.toList())
				.forEach(a -> System.out.println(a.text()));

		ctx.destroy();
	}

}// END
