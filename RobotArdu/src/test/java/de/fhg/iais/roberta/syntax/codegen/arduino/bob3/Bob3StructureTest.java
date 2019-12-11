package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3StructureTest extends Bob3AstTest {

    @Test
    @Ignore
    public void listsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/variables/bob3_datatypes_test.ino", "/ast/variables/bob3_datatypes_test.xml");
    }

}
