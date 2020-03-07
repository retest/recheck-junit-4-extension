package de.retest.recheck.junit.vintage;

import java.util.Objects;

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

	private static final String NPE_EXCEPTION_MESSAGE = RecheckLifecycle.class.getSimpleName()
			+ " must not be null. Set it via this rule's constructor or the use method.";

	private RecheckLifecycle recheckLifecycle;
	private String currentTest;

	/**
	 * @param recheckLifecycle
	 *            {@link RecheckLifecycle} element to call lifecycle methods on
	 */
	public RecheckRule( final RecheckLifecycle recheckLifecycle ) {
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
		if ( recheckLifecycle == null ) {
			throw new NullPointerException( NPE_EXCEPTION_MESSAGE );
		}
		recheckLifecycle.startTest( testName );
	}

	private void after() throws IllegalArgumentException, IllegalAccessException {
		try {
			recheckLifecycle.capTest();
		} finally {
			recheckLifecycle.cap();
		}
	}

	public void use( final RecheckLifecycle recheckLifecycle ) {
		this.recheckLifecycle = Objects.requireNonNull( recheckLifecycle, NPE_EXCEPTION_MESSAGE );
		recheckLifecycle.startTest( currentTest );
	}

}
