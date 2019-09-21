package de.retest.recheck.junit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import de.retest.recheck.RecheckLifecycle;

public class RecheckClassRule implements TestRule {

	private static final Object staticField = null;

	@Override
	public Statement apply( final Statement base, final Description description ) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
				} finally {
					afterClass( description.getTestClass() );
				}

			}
		};
	}

	private void afterClass( final Class<?> testClass ) {
		execute( RecheckLifecycle::cap, testClass );
	}

	private void execute( final Consumer<RecheckLifecycle> consumer, final Class<?> testClass ) {
		final Stream<Field> field = findRecheckField( testClass );
		field.forEach( f -> execute( consumer, f ) );
	}

	private void execute( final Consumer<RecheckLifecycle> consumer, final Field field ) {
		unlock( field );
		doExecute( consumer, field );
		lock( field );
	}

	private void doExecute( final Consumer<RecheckLifecycle> startTest, final Field field ) {
		try {
			startTest.accept( (RecheckLifecycle) field.get( staticField ) );
		} catch ( IllegalArgumentException | IllegalAccessException cause ) {
			throw new RuntimeException( cause );
		}
	}

	private Stream<Field> findRecheckField( final Class<?> testClass ) {
		final Predicate<Field> predicate = FindFields.isRecheckLifecycle.and( this::isStatic );
		return FindFields.matching( predicate ).on( testClass );
	}

	private boolean isStatic( final Field field ) {
		return Modifier.isStatic( field.getModifiers() );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}

}
