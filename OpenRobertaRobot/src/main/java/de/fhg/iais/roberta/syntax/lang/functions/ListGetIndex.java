package de.fhg.iais.roberta.syntax.lang.functions;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>lists_getIndex</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(ListElementOperations, IndexLocation, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link IndexLocation} contains all allowed functions.
 */
public class ListGetIndex<V> extends Function<V> {
    private final IListElementOperations mode;
    private final IIndexLocation location;
    private final String dataType;

    private final List<Expr<V>> param;

    private ListGetIndex(
        IListElementOperations mode,
        IIndexLocation name,
        List<Expr<V>> param,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIST_INDEX_OF"), properties, comment);
        Assert.isTrue(mode != null && name != null && param != null);
        this.mode = mode;
        this.location = name;
        this.param = param;
        this.dataType = dataType;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ListGetIndex}. This instance is read only and can not be modified.
     *
     * @param mode; must be <b>not</b> null,
     * @param name of the function; must be <b>not</b> null,
     * @param param list of parameters for the function; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link ListGetIndex}
     */
    public static <V> ListGetIndex<V> make(
        IListElementOperations mode,
        IIndexLocation name,
        List<Expr<V>> param,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new ListGetIndex<>(mode, name, param, dataType, properties, comment);
    }

    /**
     * @return name of the function
     */
    public IIndexLocation getLocation() {
        return this.location;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    public IListElementOperations getElementOperation() {
        return this.mode;
    }

    public String getDataType() {
        return this.dataType;
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return this.param.get(0).getVarType();
    }

    @Override
    public String toString() {
        return "ListGetIndex [" + this.mode + ", " + this.location + ", " + this.param + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<ExprParam> exprParams = new ArrayList<>();
        String op = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        exprParams.add(new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        if ( block.getMutation().isAt() ) {
            exprParams.add(new ExprParam(BlocklyConstants.AT, BlocklyType.NUMBER_INT));
        }
        String dataType = block.getMutation().getDatatype();
        List<Expr<V>> params = helper.extractExprParameters(block, exprParams);
        return ListGetIndex
            .make(
                factory.getListElementOpertaion(op),
                factory.getIndexLocation(Jaxb2Ast.extractField(fields, BlocklyConstants.WHERE)),
                params,
                dataType,
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setAt(false);
        mutation.setStatement(getElementOperation().isStatment());
        mutation.setDatatype(this.dataType);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getElementOperation().toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.WHERE, getLocation().toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, getParam().get(0));
        if ( getParam().size() > 1 ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.AT, getParam().get(1));
            mutation.setAt(true);
        }
        jaxbDestination.setMutation(mutation);
        return jaxbDestination;
    }
}
