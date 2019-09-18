package de.retest.recheck.junit;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.retest.recheck.RecheckLifecycle;

public class RecheckRule implements TestRule {

	private final Object testInstance;

	public RecheckRule( final Object testInstance ) {
		super();
		this.testInstance = testInstance;
	}

	@Override
	public Statement apply( final Statement base, final Description description ) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				before( description );
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};
	}

	private void before( final Description description ) throws Throwable {
		final Consumer<RecheckLifecycle> startTest = r -> r.startTest( description.getDisplayName() );
		final Stream<Field> field = findRecheckField();
		field.forEach( f -> execute( startTest, f ) );
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

	private void after() throws IllegalArgumentException, IllegalAccessException {
		final Stream<Field> field = findRecheckField();
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::capTest;
		field.forEach( f -> execute( cap, f ) );
	}

	private Stream<Field> findRecheckField() {
		final Class<?> clazz = testInstance.getClass();
		final Field[] fields = clazz.getDeclaredFields();
		return Stream.of( fields ).filter( f -> RecheckLifecycle.class.isAssignableFrom( f.getType() ) );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}

}
