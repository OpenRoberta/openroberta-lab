package de.fhg.iais.roberta.util.visitor;

import java.util.Collection;
import java.util.stream.Collectors;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.Assert;
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

    /**
     * Append N new lines if not already present and Vals to SourceBuilder
     *
     * @param numOfLines make sure to have atleast this many new lines before adding Vals, must >= 0
     * @param vals Vals to be appended
     * @return SourceBuilder
     */
    public SourceBuilder addNLine(int numOfLines, Object... vals) {
        Assert.isTrue(numOfLines >= 0);

        //if we want 2 blank lines we need 3 new lines
        numOfLines += 1;

        final String sourceStringTrimmed = this.sb.toString().replace(" ", "");
        int newLineCounter = 0;

        //an Empty SourceBuilder does not have to go to the next line to ensure given number of new lines
        if(sourceStringTrimmed.isEmpty())
            newLineCounter++;

        for(int i=0; (i < numOfLines) && (sourceStringTrimmed.length() - 1 - i >= 0); i++){
            if(sourceStringTrimmed.charAt(sourceStringTrimmed.length() - 1 - i) == '\n') {
                newLineCounter++;
            }else {
                break;
            }
        }

        for(int i=0; (i < (numOfLines - newLineCounter)); i++){
            nlI();
        }

        add(vals);
        return this;
    }

    public SourceBuilder addLine(Object... vals) {
        return addNLine(0, vals);
    }

    /**
     * Makes sure there are a given amount of new Lines, 0 will make sure we are in the next
     * Line 1 will make sure we have one Line white space, etc.,
     *
     * @param numOfLines to ensure are there, will add additional new Lines if needed
     * @return SourceBuilder
     */
    public SourceBuilder ensureBlankLines(int numOfLines) {
        return addNLine(numOfLines, "");
    }

    public SourceBuilder ensureNextLine() {
        return ensureBlankLines(0);
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
