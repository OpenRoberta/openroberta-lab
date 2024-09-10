package de.fhg.iais.roberta.util.visitor;

import java.util.Collection;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IVisitor;

public class SourceBuilder {
    private static final String INDENT = "    ";
    private int indentation = 0;
    private final StringBuilder sb;

    public SourceBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    /**
     * return the StringBuilder underlying this SourceBuilder.<br>
     * <b>Don't use this method. Think about rewriting code, that uses this method</b>
     *
     * @return the StringBuilder underlying this SourceBuilder
     */
    public StringBuilder getStringBuilder() {
        return this.sb;
    }

    public SourceBuilder INCR() {
        this.indentation += 1;
        return this;
    }

    public SourceBuilder DECR() {
        this.indentation -= 1;
        if ( this.indentation < 0 ) {
            this.indentation = 0;
        }
        return this;
    }

    public SourceBuilder indent() {
        if ( this.indentation > 0 ) {
            for ( int i = 0; i < this.indentation; i++ ) {
                this.sb.append(INDENT);
            }
        }
        return this;
    }

    public SourceBuilder nlI() {
        // removes trailing whitespace, e.g. \n  \n -> \n\n
        int last = this.sb.length() - 1;
        if (last > 0) {
            while ( this.sb.charAt(last) == ' ' ) {
                this.sb.deleteCharAt(last--);
            }
        }
        this.sb.append("\n");
        indent();
        return this;
    }

    public SourceBuilder add(Object... vals) {
        for ( Object val : vals ) {
            this.sb.append(val);
        }
        return this;
    }

    public boolean addIf(boolean first, Object val) {
        if ( !first ) {
            add(val);
        }
        return false;
    }

    public SourceBuilder accept(Phrase phrase, IVisitor<?> visitor) {
        phrase.accept(visitor);
        return this;
    }

    public SourceBuilder collect(Collection<? extends CharSequence> collection, String separator) {
        add(collection.stream().collect(Collectors.joining(separator)));
        return this;
    }

    public SourceBuilder collect(Collection<? extends CharSequence> collection, String prefix, String separator, String suffix) {
        add(collection.stream().collect(Collectors.joining(separator, prefix, suffix)));
        return this;
    }
}
