package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>controls_flow_statements</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for flow control of a statement. <br>
 * See enum {@link Flow} for all possible flows.
 */
public class StmtFlowCon<V> extends Stmt<V> {
    private final Flow flow;

    private StmtFlowCon(Flow flow, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.STMT_FLOW_CONTROL, properties, comment);
        this.flow = flow;
        setReadOnly();
    }

    /**
     * Create read only object of {@link StmtFlowCon}.
     *
     * @param flow, see enum {@link Flow} for all the possible kind of flow controls,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StmtFlowCon}.
     */
    public static <V> StmtFlowCon<V> make(Flow flow, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StmtFlowCon<V>(flow, properties, comment);
    }

    /**
     * @return the kind of control. See enum {@link Flow} for all the possible kind of flow controls.
     */
    public Flow getFlow() {
        return this.flow;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, "StmtFlowCon [" + this.flow + "]");
        return sb.toString();
    }

    /**
     * Flow controls that statement can have.
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

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitStmtFlowCon(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addField(jaxbDestination, "FLOW", getFlow().name());
        return jaxbDestination;

    }

}
