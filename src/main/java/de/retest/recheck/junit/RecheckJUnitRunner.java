package de.retest.recheck.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class RecheckJUnitRunner extends InternalRunner {

	public RecheckJUnitRunner( final Class<?> clazz ) throws InitializationError {
		super( clazz );
	}

	@Override
	public void run( final RunNotifier notifier ) {
		notifier.addListener( new RunListener() {
			@Override
			public void testStarted( final Description description ) throws Exception {
				super.testStarted( description );
			}

			@Override
			public void testFinished( final Description description ) throws Exception {
				super.testFinished( description );
			}

			@Override
			public void testRunFinished( final Result result ) throws Exception {
				super.testRunFinished( result );
			}
		} );
		super.run( notifier );
	}

}
