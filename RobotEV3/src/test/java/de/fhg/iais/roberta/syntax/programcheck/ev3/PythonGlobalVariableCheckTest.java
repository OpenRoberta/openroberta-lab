package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.Ev3CodePreprocessVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class PythonGlobalVariableCheckTest {
    Helper h = new Helper();

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_one_used_variables.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, null);
        Assert.assertEquals("[Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithNoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_no_used_variables.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, null);
        Assert.assertEquals("[]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithTwoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_two_used_variables.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, null);
        Assert.assertEquals("[Element, Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

}
