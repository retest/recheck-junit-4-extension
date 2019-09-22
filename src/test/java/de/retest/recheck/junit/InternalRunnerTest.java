package de.retest.recheck.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class InternalRunnerTest {

	@Ignore
	public static class WithoutRecheckClassRuleTest {
		@ClassRule
		public static TestRule nonRecheck = mock( TestRule.class );

		@Test
		public void dummyTest() throws Exception {
			// do nothing
		}
	}

	@Ignore
	public static class WithRecheckClassRuleTest {
		@ClassRule
		public static TestRule recheck = new RecheckClassRule();

		@Test
		public void dummyTest() throws Exception {
			// do nothing
		}
	}

	@Ignore
	public static class WithoutRecheckRuleTest {
		@Rule
		public TestRule nonRecheck = mock( TestRule.class );

		@Test
		public void dummyTest() throws Exception {
			// do nothing
		}
	}

	@Ignore
	public static class WithRecheckRuleTest {
		@Rule
		public TestRule recheck = new RecheckRule( this );

		@Test
		public void dummyTest() throws Exception {
			// do nothing
		}
	}

	@Test
	public void addsClassRule() throws Exception {
		final InternalRunner runner = new InternalRunner( WithoutRecheckClassRuleTest.class );

		final List<TestRule> rules = runner.classRules();

		assertThat( rules ).anyMatch( r -> WithoutRecheckClassRuleTest.nonRecheck.equals( r ) )
				.hasAtLeastOneElementOfType( RecheckClassRule.class );
	}

	@Test
	public void doesNotAddClassRuleIfAlreadyExisting() throws Exception {
		final InternalRunner runner = new InternalRunner( WithRecheckClassRuleTest.class );

		final List<TestRule> rules = runner.classRules();

		assertThat( rules ).containsOnly( WithRecheckClassRuleTest.recheck );
	}

	@Test
	public void addsRule() throws Exception {
		final WithoutRecheckRuleTest testInstance = new WithoutRecheckRuleTest();
		final InternalRunner runner = new InternalRunner( WithoutRecheckRuleTest.class );

		final List<TestRule> rules = runner.getTestRules( testInstance );

		assertThat( rules ).anyMatch( r -> testInstance.nonRecheck.equals( r ) )
				.hasAtLeastOneElementOfType( RecheckRule.class );
	}

	@Test
	public void doesNotAddRuleIfAlreadyExisting() throws Exception {
		final WithRecheckRuleTest testInstance = new WithRecheckRuleTest();
		final InternalRunner runner = new InternalRunner( WithRecheckRuleTest.class );

		final List<TestRule> rules = runner.getTestRules( testInstance );

		assertThat( rules ).containsOnly( testInstance.recheck );
	}
}
