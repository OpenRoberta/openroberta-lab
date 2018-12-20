package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBob3ForXmlTest;

public class Bob3MathTest {
    private final HelperBob3ForXmlTest bob3Helper = new HelperBob3ForXmlTest();

    @Test
    public void clampTest() throws Exception {
        this.bob3Helper.compareExistingAndGeneratedSource("/ast/math/bob3_clamp_test.ino", "/ast/math/bob3_clamp_test.xml");
    }

}
