package de.fhg.iais.roberta.codegen.lejos;

import java.util.ArrayList;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.Project;

public class JavaGenerator {
    StringBuilder sb = new StringBuilder();

    public void generate(Project project) {
        JaxbTransformer transformer = new JaxbTransformer();
        transformer.projectToAST(project);
        generateCodeFrmoAST(transformer);
    }

    private void generateCodeFrmoAST(JaxbTransformer transformer) {
        boolean firstT = true;
        for ( ArrayList<Phrase> instance : transformer.getProject() ) {
            if ( firstT ) {
                firstT = false;
            } else {
                this.sb.append("\n");
            }
            generateCodeFromInstance(instance);
        }
    }

    private void generateCodeFromInstance(ArrayList<Phrase> instance) {
        boolean first = true;
        for ( Phrase phrase : instance ) {
            if ( first ) {
                first = false;
            } else {
                this.sb.append("\n");
            }
            JavaVisitor astVisitor = new JavaVisitor(this.sb, 0);
            phrase.accept(astVisitor);
        }
    }

    public StringBuilder getSb() {
        return this.sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }
}
