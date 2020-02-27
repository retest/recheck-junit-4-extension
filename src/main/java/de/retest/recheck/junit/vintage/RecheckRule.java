package de.retest.recheck.junit.vintage;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.retest.recheck.RecheckLifecycle;

/**
 * This {@link Rule} calls {@link RecheckLifecycle#startTest()} before and {@link RecheckLifecycle#capTest()} after each
 * test.
 */
public class RecheckRule implements TestRule {

	private RecheckLifecycle recheckLifecycle;
	private String currentTest;

	/**
	 * @param recheckLifecycle
	 *            {@link RecheckLifecycle} element to call lifecycle methods on
	 */
	public RecheckRule( final RecheckLifecycle recheckLifecycle ) {
		super();
		this.recheckLifecycle = recheckLifecycle;
	}

	public RecheckRule() {
		this( null );
	}

	/**
	 * Creates a {@link Statement} calling {@link RecheckLifecycle#startTest()} before and
	 * {@link RecheckLifecycle#capTest()} after evaluating the given {@link Statement}.
	 */
	@Override
	public Statement apply( final Statement base, final Description description ) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				updateCurrentTest( description );
				before( currentTest );
				try {
					base.evaluate();
				} finally {
					after();
				}
			}

		};
	}

	private void updateCurrentTest( final Description description ) {
		currentTest = extractTestName( description );
	}

	private String extractTestName( final Description description ) {
		final String baseName = description.getDisplayName();
		final int index = baseName.indexOf( '(' );
		return baseName.substring( 0, index );
	}

	private void before( final String testName ) throws Throwable {
		if ( null != recheckLifecycle ) {
			recheckLifecycle.startTest( testName );
		}
	}

	private void after() throws IllegalArgumentException, IllegalAccessException {
		verifyRecheckExists();
		try {
			recheckLifecycle.capTest();
		} finally {
			recheckLifecycle.cap();
		}
	}

	private void verifyRecheckExists() {
		if ( null == recheckLifecycle ) {
			throw new IllegalStateException(
					String.format( "%s element missing. Provide a %s element via constructor or setter method.",
							recheckClass(), recheckClass() ) );
		}
	}

	public void use( final RecheckLifecycle recheckLifecycle ) {
		if ( null == recheckLifecycle ) {
			throw new IllegalArgumentException( String.format( "%s element missing.", recheckClass() ) );
		}
		this.recheckLifecycle = recheckLifecycle;
		recheckLifecycle.startTest( currentTest );
	}

	private static String recheckClass() {
		return RecheckLifecycle.class.getSimpleName();
	}
}
