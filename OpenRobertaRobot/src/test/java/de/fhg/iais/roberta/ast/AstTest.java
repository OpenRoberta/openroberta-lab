package de.fhg.iais.roberta.ast;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public abstract class AstTest {
    protected static IRobotFactory testFactory;

    @BeforeClass
    public static void setup() {
        testFactory = new UnitTestHelper.TestFactory();
    }
}
