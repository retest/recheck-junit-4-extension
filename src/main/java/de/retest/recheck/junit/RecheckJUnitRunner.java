package de.retest.recheck.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class RecheckJUnitRunner extends BlockJUnit4ClassRunner {

	public RecheckJUnitRunner( final Class<?> clazz ) throws InitializationError {
		super( clazz );
	}

	@Override
	protected List<TestRule> classRules() {
		final List<TestRule> baseRules = super.classRules();
		if ( baseRules.stream().anyMatch( r -> RecheckClassRule.class.equals( r.getClass() ) ) ) {
			return baseRules;
		}
		final List<TestRule> rules = new ArrayList<>( baseRules );
		rules.add( new RecheckClassRule() );
		return rules;
	}

	@Override
	protected List<TestRule> getTestRules( final Object target ) {
		final List<TestRule> baseRules = super.getTestRules( target );
		if ( baseRules.stream().anyMatch( r -> RecheckRule.class.equals( r.getClass() ) ) ) {
			return baseRules;
		}
		final List<TestRule> rules = new ArrayList<>( baseRules );
		rules.add( new RecheckRule( target ) );
		return rules;
	}

}
