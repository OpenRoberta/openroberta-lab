package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.PythonGlobalVariableCheck;
import de.fhg.iais.roberta.testutil.Helper;

public class PythonGlobalVariableCheckTest {

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/python_global_variables_check_one_used_variables.xml");

        PythonGlobalVariableCheck checkVisitor = new PythonGlobalVariableCheck(phrases);
        Assert.assertEquals("[Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithNoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/python_global_variables_check_no_used_variables.xml");

        PythonGlobalVariableCheck checkVisitor = new PythonGlobalVariableCheck(phrases);
        Assert.assertEquals("[]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithTwoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = Helper.generateASTs("/visitors/python_global_variables_check_two_used_variables.xml");

        PythonGlobalVariableCheck checkVisitor = new PythonGlobalVariableCheck(phrases);
        Assert.assertEquals("[Element, Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

}
