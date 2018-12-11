package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3StructureTest {

    private final HelperBob3ForXmlTest bob3Helper = new HelperBob3ForXmlTest();

    @Test
    public void listsTest() throws Exception {
        this.bob3Helper.compareExistingAndGeneratedSource("ast/variables/bob3_datatypes_test.ino", "/ast/variables/bob3_datatypes_test.xml");
    }

}
