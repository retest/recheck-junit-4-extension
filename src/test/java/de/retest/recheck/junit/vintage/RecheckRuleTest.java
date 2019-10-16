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

import de.retest.recheck.Recheck;

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
	private Recheck recheck;
	private Description description;

	@Before
	public void before() {
		recheck = mock( Recheck.class );
		description = Description.createTestDescription( DummyTest.class, testName );
	}

	@Test
	public void callsLifecycleMethodsWithInstantiatedRule() throws Throwable {
		final InOrder inOrder = inOrder( recheck, base );

		final RecheckRule rule = new RecheckRule( recheck );

		rule.apply( base, description ).evaluate();

		inOrder.verify( recheck ).startTest( testName );
		inOrder.verify( base ).evaluate();
		inOrder.verify( recheck ).capTest();
		inOrder.verify( recheck ).cap();
	}

	@Test
	public void failsForMissingRecheckElement() throws Throwable {
		final RecheckRule rule = new RecheckRule();

		expect.expect( IllegalStateException.class );
		rule.apply( base, description ).evaluate();
	}

	@Test
	public void callsLifecycleMethodsWithSetter() throws Throwable {
		final InOrder inOrder = inOrder( recheck, base );

		final RecheckRule rule = new RecheckRule();

		doAnswer( i -> {
			rule.use( recheck );
			return null;
		} ).when( base ).evaluate();
		rule.apply( base, description ).evaluate();

		inOrder.verify( base ).evaluate();
		inOrder.verify( recheck ).startTest( testName );
		inOrder.verify( recheck ).capTest();
		inOrder.verify( recheck ).cap();
	}

	@Test
	public void useRequiresRecheckElement() throws Exception {
		final RecheckRule rule = new RecheckRule();

		expect.expect( IllegalArgumentException.class );
		rule.use( null );
	}

	@Test
	public void ensureCapIsCalledOnFailingTest() throws Throwable {
		final InOrder inOrder = inOrder( recheck, base );
		doThrow( new IllegalStateException() ).when( recheck ).capTest();

		final RecheckRule rule = new RecheckRule( recheck );

		try {
			rule.apply( base, description ).evaluate();
		} catch ( final IllegalStateException e ) {
			// ignore exception
		}

		inOrder.verify( recheck ).startTest( testName );
		inOrder.verify( base ).evaluate();
		inOrder.verify( recheck ).capTest();
		inOrder.verify( recheck ).cap();
	}

}
