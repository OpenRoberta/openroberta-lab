package de.fhg.iais.roberta.util.test;

/**
 * Classes implementing this interface are responsible to create objects used in testing Java-bean-like classes.<br>
 * See {@link JavaBeanTester}
 *
 * @author ae#11ta
 */
public interface IValueBuilder {
    /**
     * given a type, return an object of this type, that can be used in Junit tests.
     *
     * @param type the type, an object of which has to be created
     * @return an object of the type given; return {@code null} if the type is not appropriate for this builder
     */
    Object build(Class<?> type);
}
