package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3ActorsTest {
    private final HelperBob3ForXmlTest bob3Helper = new HelperBob3ForXmlTest();

    @Test
    public void listsTest() throws Exception {
        this.bob3Helper.compareExistingAndGeneratedSource("ast/actions/bob3_actors_test.ino", "/ast/actions/bob3_actors_test.xml");
    }

}
