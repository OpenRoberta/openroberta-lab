package de.fhg.iais.roberta.components.ev3c4ev3;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static de.fhg.iais.roberta.components.ev3c4ev3.ResourceUtils.getFileFromResourcesAsByteArray;
import static org.junit.Assert.assertArrayEquals;

public class RbfBuilderTest {

    @Test
    public void build() throws Exception {
        String programName = "../prjs/BrkProg_SAVE/NEPOprog.elf";
        RbfBuilder builder = new RbfBuilder(null);
        byte[] rbfToPatch = getFileFromResourcesAsByteArray("/components/ev3c4ev3/rbf_test_to_patch.rbf");

        byte[] rbf = builder.patch(rbfToPatch, programName.getBytes(StandardCharsets.UTF_8));

        byte[] expectedRbf = getFileFromResourcesAsByteArray("/components/ev3c4ev3/rbf_test_patched.rbf");
        assertArrayEquals(expectedRbf, rbf);
    }
}
