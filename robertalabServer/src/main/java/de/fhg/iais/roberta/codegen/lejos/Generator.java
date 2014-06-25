package de.fhg.iais.roberta.codegen.lejos;

import de.fhg.iais.roberta.ast.syntax.Phrase;

public class Generator {
    StringBuilder sb = new StringBuilder();

    public void generate(Phrase phrase) {
        phrase.toStringBuilder(this.sb, 0);
    }
}
