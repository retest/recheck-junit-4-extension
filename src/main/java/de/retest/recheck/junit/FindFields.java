package de.retest.recheck.junit;

import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.retest.recheck.RecheckLifecycle;
import lombok.RequiredArgsConstructor;

/**
 * Finds all {@link Field}s matching given {@link Predicate}.
 */
@RequiredArgsConstructor
public class FindFields {

	public static final Predicate<Field> isRecheckLifecycle =
			f -> RecheckLifecycle.class.isAssignableFrom( f.getType() );

	private final Predicate<? super Field> predicate;

	public static FindFields matching( final Predicate<? super Field> predicate ) {
		return new FindFields( predicate );
	}

	Stream<Field> on( final Class<?> clazz ) {
		return concat( getFields( clazz ), getSuperClassFieldsOf( clazz ) );
	}

	/**
	 * Return fields directly declared at the given {@link Class} and at the interfaces implemented by the given
	 * {@link Class}.
	 *
	 * @param clazz
	 *            {@link Class} to search for fields
	 * @return {@link Field}s of the given {@link Class} and the interfaces implemented by the given {@link Class}
	 */
	private Stream<Field> getFields( final Class<?> clazz ) {
		return concat( getDeclaredFieldsOf( clazz ), getInterfaceFields( clazz ) );
	}

	/**
	 * Return fields declared interfaces implemented by the given {@link Class}.
	 *
	 * @param clazz
	 *            {@link Class} to search for fields
	 * @return {@link Field}s of interfaces implemented by the given {@link Class}
	 */
	private Stream<Field> getInterfaceFields( final Class<?> clazz ) {
		return of( clazz.getInterfaces() ).flatMap( this::getFields );
	}

	/**
	 * Return fields declared at super classes of given {@link Class}
	 *
	 * @param clazz
	 *            {@link Class} to search for fields
	 * @return {@link Field}s of the super classes of the given {@link Class}
	 */
	private Stream<Field> getSuperClassFieldsOf( final Class<?> clazz ) {
		if ( null == clazz.getSuperclass() || Object.class.equals( clazz.getSuperclass() ) ) {
			return getDeclaredFieldsOf( clazz );
		}
		return concat( getFields( clazz ), getSuperClassFieldsOf( clazz.getSuperclass() ) );
	}

	/**
	 * Return fields directly declared at given {@link Class}.
	 *
	 * @param clazz
	 *            {@link Class} to search for fields
	 * @return {@link Field}s of the given {@link Class}
	 */
	private Stream<Field> getDeclaredFieldsOf( final Class<?> clazz ) {
		return Stream.of( clazz.getDeclaredFields() ).filter( predicate );
	}
}
