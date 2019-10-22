package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>controls_flow_statements</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for flow control of a statement. <br>
 * See enum {@link Flow} for all possible flows.
 */
public class StmtFlowCon<V> extends Stmt<V> {
    private final Flow flow;

    private StmtFlowCon(Flow flow, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("STMT_FLOW_CONTROL"), properties, comment);
        Assert.isTrue(flow != null);
        this.flow = flow;
        setReadOnly();
    }

    /**
     * Create read only object of {@link StmtFlowCon}.
     *
     * @param flow; must be <b>not</b> null; see enum {@link Flow} for all the possible kind of flow controls,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StmtFlowCon}
     */
    public static <V> StmtFlowCon<V> make(Flow flow, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StmtFlowCon<V>(flow, properties, comment);
    }

    /**
     * @return the kind of control. See enum {@link Flow} for all the possible kind of flow controls
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
         * get flow from {@link Flow} from string parameter. It is possible for one kind of flow to have multiple string mappings. Throws exception if the kind
         * of flow does not exists.
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitStmtFlowCon(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String mode = helper.extractField(fields, BlocklyConstants.FLOW);
        return StmtFlowCon.make(Flow.get(mode), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.FLOW, getFlow().name());
        return jaxbDestination;

    }

}
