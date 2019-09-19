package de.retest.recheck.junit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class ReflectionUtilsTest {

	private Map<String, Class<?>> recheckFields;

	@Before
	public void before() {
		recheckFields = new LinkedHashMap<>();
	}

	@Test
	public void findRecheckFieldsOfClass() throws Exception {
		recheckFields.put( "somePublicField", DummyClass.class );
		recheckFields.put( "otherPublicField", DummyClass.class );
		recheckFields.put( "somePrivateField", DummyClass.class );
		recheckFields.put( "someSuperClassField", SuperClass.class );
		recheckFields.put( "someSuperSuperClassField", SuperSuperClass.class );
		recheckFields.put( "someInterfaceField", DummyInterface.class );
		recheckFields.put( "someSuperClassInterfaceField", SuperClassInterface.class );
		assertThat( ReflectionUtils.getRecheckFieldsOf( DummyClass.class ) ).containsOnlyElementsOf( fields() );
	}

	@Test
	public void doesNotFindNonRecheckLifecycleFields() throws Exception {
		assertThat( ReflectionUtils.getRecheckFieldsOf( DummyClass.class ) )
				.doesNotContain( getField( DummyClass.class, "nonRecheckLifecyleField" ) );
	}

	@Test
	public void doesNotFailOnTestWithoutFields() throws Exception {
		ReflectionUtils.getRecheckFieldsOf( EmptyClass.class );
	}

	private List<Field> fields() throws NoSuchFieldException, SecurityException {
		return recheckFields.entrySet().stream().map( this::toField ).collect( toList() );
	}

	private Field toField( final Entry<String, Class<?>> entry ) {
		return getField( entry.getValue(), entry.getKey() );
	}

	private Field getField( final Class<?> clazz, final String field ) {
		try {
			return clazz.getDeclaredField( field );
		} catch ( NoSuchFieldException | SecurityException e ) {
			throw new RuntimeException( e );
		}
	}

}
