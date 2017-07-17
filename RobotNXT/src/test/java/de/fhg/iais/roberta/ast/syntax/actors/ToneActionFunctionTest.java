package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class ToneActionFunctionTest {
    Helper h = new Helper();

    @Test
    public void playTone() throws Exception {
        final String a =
            "bytevolume=0x02;\n"
                + "floatElement=0;\n"
                + "taskmain(){macheEtwas();"
                + "voidmacheEtwas(){\n"
                + "volume=50*4/100.0+0.5;"
                + "PlayToneEx(300, 100, volume, false);\n"
                + "Wait(100);}";

        this.h.assertCodeIsOk(a, "/ast/actions/action_PlaySoundFunc.xml");
    }
}
