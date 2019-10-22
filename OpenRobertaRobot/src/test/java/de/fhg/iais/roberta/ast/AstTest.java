package de.fhg.iais.roberta.ast;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public abstract class AstTest {
    protected static IRobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        testFactory = new UnitTestHelper.TestFactory();
    }

    public static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }
}
