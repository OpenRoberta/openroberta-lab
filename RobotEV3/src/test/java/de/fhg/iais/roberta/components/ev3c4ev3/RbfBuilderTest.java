package de.fhg.iais.roberta.components.ev3c4ev3;

import org.junit.Test;

import static de.fhg.iais.roberta.components.ev3c4ev3.ResourceUtils.getFileFromResourcesAsByteArray;
import static org.junit.Assert.assertArrayEquals;

public class RbfBuilderTest {

    @Test
    public void build() throws Exception {
        String programName = "../prjs/BrkProg_SAVE/NEPOprog.elf";
        RbfBuilder builder = new RbfBuilder();

        byte[] rbf = builder.build(programName);

        byte[] expectedRbf = getFileFromResourcesAsByteArray("/components/ev3c4ev3/rbf_test.rbf");
        assertArrayEquals(expectedRbf, rbf);
    }
}
