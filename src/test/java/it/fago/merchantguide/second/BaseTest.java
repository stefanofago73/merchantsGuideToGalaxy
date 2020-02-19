package it.fago.merchantguide.second;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.fago.merchantguide.Parser;
import it.fago.merchantguide.impl.ParserImpl;

public class BaseTest {
	//
	protected static Logger logger;
	//
	private Parser parser;
	//
	private SampleData data;

	@Rule
	public TestName name = new TestName();

	@Before
	public void init() {
		parser = new ParserImpl();
		data = new SampleData();
		logger = LoggerFactory.getLogger(TestParsingInput.class);
		testName();
	}

	@After
	public void destroy() {
		data = null;
		parser.destroy();
		parser = null;
	}

	@SafeVarargs
	protected final <T> T[] arrayFromCollection(Collection<T> collection, T... ignore) {
		T[] array = collection.toArray(ignore);
		Arrays.sort(array);
		return array;
	}

	@SafeVarargs
	protected final <T> T[] array(T... elements) {
		return elements;
	}

	protected void testName() {
		logger.info("executing {} ...", name.getMethodName());
	}

	protected Parser parser() {
		return parser;
	}

	protected SampleData data() {
		return data;
	}

}
