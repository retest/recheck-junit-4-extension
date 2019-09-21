package de.retest.recheck.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class RecheckJUnitRunner extends BlockJUnit4ClassRunner {

	public RecheckJUnitRunner( final Class<?> clazz ) throws InitializationError {
		super( clazz );
	}

}
