package de.fhg.iais.roberta.util.visitor;

import org.json.JSONObject;
import org.junit.Test;

import de.fhg.iais.roberta.util.Util;

public class StackmachinePrettyPrinterTest {
    @Test
    public void test1() {
        String codeAsString = Util.readResourceContent("/visitor/stackMachineCode-1.json");
        JSONObject codeAdsJson = new JSONObject(codeAsString);
        String prettyPrint = StackmachinePrettyPrinter.prettyPrint(codeAdsJson, true, true);
        System.out.println(prettyPrint);
    }

    @Test
    public void test2() {
        String codeAsString = Util.readResourceContent("/visitor/stackMachineCode-2.json");
        JSONObject codeAdsJson = new JSONObject(codeAsString);
        String prettyPrint = StackmachinePrettyPrinter.prettyPrint(codeAdsJson, true, true);
        System.out.println(prettyPrint);
    }

    @Test
    public void test3() {
        String codeAsString = Util.readResourceContent("/visitor/x.json");
        JSONObject codeAdsJson = new JSONObject(codeAsString);
        String prettyPrint = StackmachinePrettyPrinter.prettyPrint(codeAdsJson, true, true);
        System.out.println(prettyPrint);
    }
}
