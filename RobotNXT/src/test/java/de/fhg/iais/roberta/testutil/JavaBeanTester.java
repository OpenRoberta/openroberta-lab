package de.fhg.iais.roberta.testutil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a utility class for Junit test classes responsible for testing Java-bean-like classes. From the many possibilites to guarantee
 * immutability the following design is used here:<br>
 * - each class (a common superclass ...) has a transient field "readOnly" defaulting to "false".<br>
 * - each setter checks whether this field is false<br>
 * - after the construction of an immutable object is finished, the method setReadOnly() has to be called. After that call the object becomes
 * immutable forever.<br>
 * <br>
 * This class provides a utility method, that for a given class ...<br>
 * - detects all getter/setter-pairs<br>
 * - generates parameter objects (see interface {@link IValueBuilder}<br>
 * - creates a mutable object, and checks whether the getter returns the setted object<br>
 * - makes the object immutable and expects an exception, when a setter is called<br>
 * <br>
 * The class is adopted from https://github.com/codebox/javabean-tester/blob/master/JavaBeanTester.java, written by Rob Dawson --- many thanx.<br>
 */
public class JavaBeanTester<T> {
    private static final Logger LOG = LoggerFactory.getLogger(JavaBeanTester.class);

    private Set<Class<?>> warningsAbout = new HashSet<Class<?>>();
    private int countErrors = 0;
    private String lastError = null;
    private final IValueBuilder builder;
    private final Class<T> clazz;
    private final String[] skipThese;

    public static <T> void test(Class<T> clazz, IValueBuilder builder, String... skipThese) throws Exception {
        JavaBeanTester<T> tester = new JavaBeanTester<>(clazz, builder, skipThese);
        tester.test();
        tester.logWarnings();
    }

    private JavaBeanTester(Class<T> clazz, IValueBuilder builder, String... skipThese) {
        this.builder = builder;
        this.clazz = clazz;
        this.skipThese = skipThese;
    }

    /**
     * Tests the get/set methods of the specified class and the behavior if it is a immutable class.
     * 
     * @param <T>
     *        the type parameter associated with the class under test
     * @param clazz
     *        the Class under test
     * @param builder
     *        a builder to create objects needed as parameters for setter. May be mock objects, may be objects. See {@link IValueBuilder}
     * @param skipThese
     *        the names of any properties that should not be tested
     * @throws IntrospectionException
     *         thrown if the Introspector.getBeanInfo() method throws this exception for the class under test
     */
    private void test() throws Exception {
        String clazzName = this.clazz.getName();
        LOG.info("CLASS UNDER TEST (SUT) " + clazzName);
        BeanInfo beanInfo = Introspector.getBeanInfo(this.clazz);

        Method makeImmutableMethod = null;
        for ( MethodDescriptor md : beanInfo.getMethodDescriptors() ) {
            if ( md.getName().matches("setReadOnly") ) {
                makeImmutableMethod = md.getMethod();
                break;
            }
        }

        // process all getter/setter pairs
        final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        nextProp: for ( PropertyDescriptor prop : props ) {
            // Check the list of properties that we don't want to test
            String propName = prop.getName();
            if ( "class".equals(propName) ) {
                continue nextProp;
            }
            for ( String skipThis : this.skipThese ) {
                if ( skipThis.equals(propName) ) {
                    continue nextProp;
                }
            }

            findBooleanIsMethods(this.clazz, prop);
            final Method getter = prop.getReadMethod();
            final Method setter = prop.getWriteMethod();

            if ( getter != null && setter != null ) {
                LOG.info("  property : " + propName);

                // We have both a get and set method for this property
                final Class<?> returnType = getter.getReturnType();
                final Class<?>[] params = setter.getParameterTypes();
                if ( params.length == 1 && params[0] == returnType ) {
                    // The set method has 1 argument, which is of the same type as the return type of the get method, so we can test this property
                    try {
                        // Build a value of the correct type to be passed to the set method
                        Object value = makeParameter(returnType);
                        if ( value == null ) {
                            continue nextProp;
                        }
                        // Build an instance of the bean that we are testing (each property test gets a fresh instance)
                        T mutable = this.clazz.newInstance();
                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(mutable, value);

                        final Object expected = value;
                        final Object actual = getter.invoke(mutable);
                        if ( !equalsRegardingNull(expected, actual) ) {
                            String msg = "Expected value [" + expected + "] and actual value [" + actual + "] differ for " + clazzName + "#" + propName;
                            LOG.error(msg);
                            this.lastError = msg;
                            this.countErrors++;
                        }
                    } catch ( Exception e ) {
                        throw new RuntimeException("Exception while testing the property " + clazzName + "#" + propName, e);
                    }
                }
            } else if ( getter != null ) {
                LOG.info("  property with getter only IGNORED: " + propName);
            } else if ( setter != null ) {
                LOG.info("  property with setter only IGNORED: " + propName);
            }
        }
        if ( makeImmutableMethod != null ) {
            T immutable = this.clazz.newInstance();
            makeImmutableMethod.invoke(immutable);
            nextMthd: for ( MethodDescriptor md : beanInfo.getMethodDescriptors() ) {
                if ( md.getName().startsWith("set") || md.getName().startsWith("add") ) {
                    Method setter = md.getMethod();
                    final Class<?>[] params = setter.getParameterTypes();
                    if ( params.length == 1 ) {
                        LOG.info("  immutable check for: " + md.getName());
                        try {
                            // Build a value of the correct type to be passed to the set method
                            Class<?> paramType = params[0];
                            Object value = makeParameter(paramType);
                            if ( value == null ) {
                                continue nextMthd;
                            }
                            try {
                                setter.invoke(immutable, value);
                                String msg = "a setter of an immmutable object succeeded for the setter " + clazzName + "#" + md.getName();
                                LOG.error(msg);
                                this.lastError = msg;
                                this.countErrors++;
                            } catch ( Exception e ) {
                                // expected behavior
                            }
                        } catch ( Exception e ) {
                            throw new RuntimeException("Exception while testing setter " + clazzName + "#" + md.getName(), e);
                        }
                    } else {
                        LOG.info("  property IGNORED for immutable check: " + md.getName());
                    }
                }
            }
        }

        if ( this.countErrors > 0 ) {
            throw new RuntimeException("" + this.countErrors + " errors while testing class " + clazzName + ". Last msg was: " + this.lastError);
        }
    }

    /**
     * log the assembled warnings about classes, that are NOT created by custom builders, but instead by calling their no-arg constructors.<br>
     * Nothing goes wrong, if this method is NOT called at the end of a test run. The log is a hint to extend the own builder. This is not strictly
     * necessary, if the no-arg constructor creates a Junit-friendly object.
     */
    private void logWarnings() {
        for ( Class<?> c : this.warningsAbout ) {
            LOG.info("WARNING: default constructor for " + c.getName() + " used, better add code to your builder to create such an instance");
        }
    }

    /**
     * make a parameter object of given type
     * 
     * @param clazz
     * @return the parameter object, maybe null
     * @throws Exception
     */
    private Object makeParameter(final Class<?> clazz) throws Exception {
        Object value = buildValue(clazz, this.builder);
        if ( value == null ) {
            String msg = "Unable to build an instance of " + clazz.getName() + ", add code to your builder to create such an instance.";
            LOG.error(msg);
            this.lastError = msg;
            this.countErrors++;
        }
        return value;
    }

    private Object buildValue(Class<?> clazz, IValueBuilder builder) throws Exception {
        Object value = null;
        if ( builder != null ) {
            value = builder.build(clazz);
        }
        if ( value == null ) {
            value = buildPrimitiveValue(clazz);
        }
        if ( value == null ) {
            value = buildNoArgCstrValue(clazz);
        }
        return value;
    }

    private Object buildPrimitiveValue(Class<?> clazz) throws Exception {
        if ( clazz == String.class ) {
            return "testvalue";
        } else if ( clazz.isArray() ) {
            return Array.newInstance(clazz.getComponentType(), 1);
        } else if ( clazz == boolean.class || clazz == Boolean.class ) {
            return true;
        } else if ( clazz == int.class || clazz == Integer.class ) {
            return 1;
        } else if ( clazz == long.class || clazz == Long.class ) {
            return 1L;
        } else if ( clazz == double.class || clazz == Double.class ) {
            return 1.0D;
        } else if ( clazz == float.class || clazz == Float.class ) {
            return 1.0F;
        } else if ( clazz == char.class || clazz == Character.class ) {
            return 'Y';
        } else if ( clazz.isEnum() ) {
            return clazz.getEnumConstants()[0];
        } else {
            return null;
        }
    }

    private Object buildNoArgCstrValue(Class<?> clazz) throws Exception {
        final Constructor<?>[] ctrs = clazz.getConstructors();
        for ( Constructor<?> ctr : ctrs ) {
            if ( ctr.getParameterTypes().length == 0 ) {
                this.warningsAbout.add(clazz);
                return ctr.newInstance();
            }
        }
        return null;
    }

    /**
     * Hunt down missing Boolean read method if needed as Introspector cannot find 'is' getters for Boolean type properties.
     * 
     * @param clazz
     *        the type being introspected
     * @param descriptor
     *        the property descriptor found so far
     */
    private void findBooleanIsMethods(Class<T> clazz, PropertyDescriptor descriptor) throws IntrospectionException {
        if ( needToFindReadMethod(descriptor) ) {
            findTheReadMethod(descriptor, clazz);
        }
    }

    private boolean needToFindReadMethod(PropertyDescriptor property) {
        return property.getReadMethod() == null && property.getPropertyType() == Boolean.class;
    }

    private void findTheReadMethod(PropertyDescriptor descriptor, Class<T> clazz) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(descriptor.getName(), clazz);
            descriptor.setReadMethod(pd.getReadMethod());
        } catch ( IntrospectionException e ) {
        }
    }

    private boolean equalsRegardingNull(Object expected, Object actual) {
        if ( expected == null ) {
            return actual == null;
        }

        return expected.equals(actual);
    }
}