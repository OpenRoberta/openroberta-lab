package de.fhg.iais.roberta.ast.syntax.functions;

import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class TextFunctions {

    @Test
    public void concatination() throws Exception {
        Helper.generateSyntax("/syntax/functions/text_concat.xml");

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";
        // Assert.assertEquals(a, transformer.toString());
    }
}