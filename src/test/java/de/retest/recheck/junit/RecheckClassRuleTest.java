package de.retest.recheck.junit;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckLifecycle;

@RunWith( MockitoJUnitRunner.class )
public class RecheckClassRuleTest {

	private static class DummyTest {

		private static Object nonRecheck;
		private static RecheckLifecycle recheckLifecycle;
		private static Recheck recheck;
		private Object instanceNonRecheck;
		private Recheck instanceRecheck;
	}

	@Mock
	private Statement base;
	private DummyTest testInstance;
	private Statement statement;
	private Description description;

	@Before
	public void before() {
		testInstance = new DummyTest();
		DummyTest.recheckLifecycle = mock( RecheckLifecycle.class );
		DummyTest.recheck = mock( Recheck.class );
		DummyTest.nonRecheck = mock( Object.class );
		testInstance.instanceRecheck = mock( Recheck.class );
		testInstance.instanceNonRecheck = mock( Object.class );
		description = Description.createTestDescription( testInstance.getClass(), "dummy-instance" );
		statement = new RecheckClassRule().apply( base, description );
	}

	@Test
	public void callsLifecycleMethods() throws Throwable {
		final InOrder inOrder = inOrder( DummyTest.recheckLifecycle, DummyTest.recheck, base );

		statement.evaluate();

		inOrder.verify( base ).evaluate();
		inOrder.verify( DummyTest.recheckLifecycle ).cap();
		inOrder.verify( DummyTest.recheck ).cap();
	}

	@After
	public void after() {
		verifyZeroInteractions( DummyTest.nonRecheck );
		verifyZeroInteractions( testInstance.instanceRecheck );
		verifyZeroInteractions( testInstance.instanceNonRecheck );
	}

}
