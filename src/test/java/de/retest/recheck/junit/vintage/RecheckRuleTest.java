package de.retest.recheck.junit.vintage;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.retest.recheck.RecheckLifecycle;

@RunWith( MockitoJUnitRunner.class )
public class RecheckRuleTest {

	private static final String testName = "dummyTest";

	public class DummyTest {

	}

	@Rule
	public ExpectedException expect = ExpectedException.none();
	@Mock
	private Statement base;
	@Mock
	private RecheckLifecycle recheckLifecycle;
	private Description description;

	@Before
	public void before() {
		recheckLifecycle = mock( RecheckLifecycle.class );
		description = Description.createTestDescription( DummyTest.class, testName );
	}

	@Test
	public void callsLifecycleMethodsWithInstantiatedRule() throws Throwable {
		final InOrder inOrder = inOrder( recheckLifecycle, base );

		final RecheckRule rule = new RecheckRule( recheckLifecycle );

		rule.apply( base, description ).evaluate();

		inOrder.verify( recheckLifecycle ).startTest( testName );
		inOrder.verify( base ).evaluate();
		inOrder.verify( recheckLifecycle ).capTest();
		inOrder.verify( recheckLifecycle ).cap();
	}

	@Test
	public void failsForMissingRecheckElement() throws Throwable {
		final RecheckRule rule = new RecheckRule();

		expect.expect( IllegalStateException.class );
		rule.apply( base, description ).evaluate();
	}

	@Test
	public void callsLifecycleMethodsWithSetter() throws Throwable {
		final InOrder inOrder = inOrder( recheckLifecycle, base );

		final RecheckRule rule = new RecheckRule();

		doAnswer( i -> {
			rule.use( recheckLifecycle );
			return null;
		} ).when( base ).evaluate();
		rule.apply( base, description ).evaluate();

		inOrder.verify( base ).evaluate();
		inOrder.verify( recheckLifecycle ).startTest( testName );
		inOrder.verify( recheckLifecycle ).capTest();
		inOrder.verify( recheckLifecycle ).cap();
	}

	@Test
	public void useRequiresRecheckElement() throws Exception {
		final RecheckRule rule = new RecheckRule();

		expect.expect( IllegalArgumentException.class );
		rule.use( null );
	}

	@Test
	public void ensureCapIsCalledOnFailingTest() throws Throwable {
		final InOrder inOrder = inOrder( recheckLifecycle, base );
		doThrow( new IllegalStateException() ).when( recheckLifecycle ).capTest();

		final RecheckRule rule = new RecheckRule( recheckLifecycle );

		try {
			rule.apply( base, description ).evaluate();
		} catch ( final IllegalStateException e ) {
			// ignore exception
		}

		inOrder.verify( recheckLifecycle ).startTest( testName );
		inOrder.verify( base ).evaluate();
		inOrder.verify( recheckLifecycle ).capTest();
		inOrder.verify( recheckLifecycle ).cap();
	}

}
