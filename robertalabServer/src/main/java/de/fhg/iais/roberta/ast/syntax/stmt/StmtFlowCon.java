package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * Class that can control the flow in the statement. See enum {@link Flow} for all the possible kind of flow controls.
 * 
 * @author kcvejoski
 */
public class StmtFlowCon extends Stmt {
    private final Flow flow;

    private StmtFlowCon(Flow flow) {
        super(Phrase.Kind.StmtFlowCon);
        this.flow = flow;
        setReadOnly();
    }

    /**
     * create read only object of {@link StmtFlowCon}.
     * 
     * @param flow, see enum {@link Flow} for all the possible kind of flow controls
     * @return
     */
    public static StmtFlowCon make(Flow flow) {
        return new StmtFlowCon(flow);
    }

    /**
     * @return the kind of control. See enum {@link Flow} for all the possible kind of flow controls.
     */
    public Flow getFlow() {
        return this.flow;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        appendNewLine(sb, indentation, toString());
    }

    @Override
    public String toString() {
        return "StmtFlowCon [" + this.flow + "]";
    }

    /**
     * Flow controls that statement can have.
     * 
     * @author kcvejoski
     */
    public static enum Flow {
        CONTINUE(), BREAK();

        private final String[] values;

        private Flow(String... values) {
            this.values = values;
        }

        /**
         * get flow from {@link Flow} from string parameter. It is possible for one kind of flow to have multiple string mappings.
         * Throws exception if the kind of flow does not exists.
         * 
         * @param name of the flow
         * @return operator from the enum {@link Flow}
         */
        public static Flow get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid flow kind: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Flow fl : Flow.values() ) {
                if ( fl.toString().equals(sUpper) ) {
                    return fl;
                }
                for ( String value : fl.values ) {
                    if ( sUpper.equals(value) ) {
                        return fl;
                    }
                }
            }
            throw new DbcException("Invalid flow kind: " + s);
        }
    }

}
