package de.retest.recheck.junit;

import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class InternalRunner extends BlockJUnit4ClassRunner {

	public InternalRunner( final Class<?> clazz ) throws InitializationError {
		super( clazz );
	}

	@Override
	protected List<TestRule> classRules() {
		return AddRecheck.classRules( super.classRules() );
	}

	@Override
	protected List<TestRule> getTestRules( final Object target ) {
		return AddRecheck.rules( target, super.getTestRules( target ) );
	}

}
