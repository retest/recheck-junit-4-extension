package de.retest.recheck.junit;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

	private final Object testInstance;

	/**
	 * @param testInstance
	 *            of junit test using {@link RecheckLifecycle} objects
	 */
	public RecheckRule( final Object testInstance ) {
		super();
		this.testInstance = testInstance;
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
				before( description.getDisplayName() );
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};
	}

	private void before( final String testName ) throws Throwable {
		final Consumer<RecheckLifecycle> startTest = r -> r.startTest( testName );
		execute( startTest );
	}

	private void after() throws IllegalArgumentException, IllegalAccessException {
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::capTest;
		execute( cap );
	}

	private void execute( final Consumer<RecheckLifecycle> consumer ) {
		final Stream<Field> field = findRecheckField();
		field.forEach( f -> execute( consumer, f ) );
	}

	private void execute( final Consumer<RecheckLifecycle> consumer, final Field field ) {
		unlock( field );
		doExecute( consumer, field );
		lock( field );
	}

	private void doExecute( final Consumer<RecheckLifecycle> startTest, final Field field ) {
		try {
			startTest.accept( (RecheckLifecycle) field.get( testInstance ) );
		} catch ( IllegalArgumentException | IllegalAccessException cause ) {
			throw new RuntimeException( cause );
		}
	}

	private Stream<Field> findRecheckField() {
		return FindFields.matching( FindFields.isRecheckLifecycle ).on( testInstance.getClass() );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}

}
