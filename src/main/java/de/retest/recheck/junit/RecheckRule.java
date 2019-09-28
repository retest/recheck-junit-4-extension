package de.retest.recheck.junit;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckLifecycle;

/**
 * This {@link Rule} calls {@link RecheckLifecycle#startTest()} before and {@link RecheckLifecycle#capTest()} after each
 * test.
 */
public class RecheckRule implements TestRule {

	private Recheck recheck;
	private String currentTest;

	/**
	 * @param recheck
	 *            {@link RecheckLifecycle} element to call lifecycle methods on
	 */
	public RecheckRule( final Recheck recheck ) {
		super();
		this.recheck = recheck;
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
		if ( null != recheck ) {
			recheck.startTest( testName );
		}
	}

	private void after() throws IllegalArgumentException, IllegalAccessException {
		verifyRecheckExists();
		try {
			recheck.capTest();
		} finally {
			recheck.cap();
		}
	}

	private void verifyRecheckExists() {
		if ( null == recheck ) {
			throw new IllegalStateException(
					String.format( "%s element missing. Provide a %s element via constructor or setter method.",
							recheckClass(), recheckClass() ) );
		}
	}

	public void use( final Recheck recheck ) {
		if ( null == recheck ) {
			throw new IllegalArgumentException( String.format( "%s element missing.", recheckClass() ) );
		}
		this.recheck = recheck;
		recheck.startTest( currentTest );
	}

	private static String recheckClass() {
		return Recheck.class.getSimpleName();
	}
}
