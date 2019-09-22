package de.retest.recheck.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;

public class AddRecheck {

	public static List<TestRule> rules( final Object target, final List<TestRule> baseRules ) {
		if ( baseRules.stream().anyMatch( r -> RecheckRule.class.equals( r.getClass() ) ) ) {
			return baseRules;
		}
		final List<TestRule> rules = new ArrayList<>( baseRules );
		rules.add( new RecheckRule( target ) );
		return rules;
	}

	public static List<TestRule> classRules( final List<TestRule> baseRules ) {
		if ( baseRules.stream().anyMatch( r -> RecheckClassRule.class.equals( r.getClass() ) ) ) {
			return baseRules;
		}
		final List<TestRule> rules = new ArrayList<>( baseRules );
		rules.add( new RecheckClassRule() );
		return rules;
	}

}
