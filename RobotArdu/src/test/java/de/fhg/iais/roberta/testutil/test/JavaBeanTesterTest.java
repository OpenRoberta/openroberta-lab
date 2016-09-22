package de.fhg.iais.roberta.testutil.test;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.IValueBuilder;
import de.fhg.iais.roberta.testutil.JavaBeanTester;

public class JavaBeanTesterTest {
    @Test
    public void testSuccess() throws Exception {
        JavaBeanTester.test(BeanTesterSuccess.class, new PBuilder());
    }

    @Test(expected = Exception.class)
    public void testException1() throws Exception {
        JavaBeanTester.test(BeanTesterFail1.class, new PBuilder());
    }

    @Test(expected = Exception.class)
    public void testException2() throws Exception {
        JavaBeanTester.test(BeanTesterFail2.class, new PBuilder());

    }

    private static class PBuilder implements IValueBuilder {
        @Override
        public Object build(Class<?> clazz) {
            if ( clazz == BeanTesterSuccess.class ) {
                return new BeanTesterSuccess(42, "zweiundvierzig", null);
            } else if ( clazz == BeanTesterFail1.class ) {
                return new BeanTesterFail1(42, "zweiundvierzig", null);
            } else {
                return null;
            }
        }
    }
}