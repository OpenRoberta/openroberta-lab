package de.fhg.iais.roberta.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

public class Jaxb2ProgramAst {
    private final RobotFactory robotFactory;
    private int variableCounter = 0;

    public Jaxb2ProgramAst(RobotFactory factory) {
        this.robotFactory = factory;
    }

    public BlocklyDropdownFactory getDropdownFactory() {
        return this.robotFactory.getBlocklyDropdownFactory();
    }

    public RobotFactory getRobotFactory() {
        return this.robotFactory;
    }

    /**
     * @return the variableCounter
     */
    public int getVariableCounter() {
        return this.variableCounter;
    }

    /**
     * @param variableCounter the variableCounter to set
     */
    public void setVariableCounter(int variableCounter) {
        this.variableCounter = variableCounter;
    }

    /**
     * Converts object of type {@link BlockSet} to AST tree.
     *
     * @param set the BlockSet to transform
     */
    public ProgramAst blocks2ast(BlockSet set) {
        ProgramAst.Builder builder =
            new ProgramAst.Builder()
                .setRobotType(set.getRobottype())
                .setXmlVersion(set.getXmlversion())
                .setDescription(set.getDescription())
                .setTags(set.getTags());

        List<Instance> instances = set.getInstance();
        for ( Instance instance : instances ) {
            List<Block> blocks = instance.getBlock();
            Location location = new Location(instance.getX(), instance.getY());
            List<Phrase> range = new ArrayList();
            range.add(location);
            for ( Block block : blocks ) {
                range.add(block2ast(block));
            }
            builder.addToTree(range);
        }
        return builder.build();
    }

    /**
     * Transforms valid JAXB object to AST object
     *
     * @param block to be transformed
     * @return corresponding AST object
     */
    private Phrase block2ast(Block block) {
        if ( block == null ) {
            throw new DbcException("Invalid null block");
        }
        String type = block.getType().trim().toLowerCase();
        BlockDescriptor matchingBlockDescriptor = AstFactory.getByBlocklyName(type);
        Assert.notNull(matchingBlockDescriptor, "Invalid Block: " + block.getType());
        String className = matchingBlockDescriptor.getAstClass().getName();
        try {
            Class<?> astClass = Class.forName(className);
            if ( !Modifier.isFinal(astClass.getModifiers()) ) {
                throw new DbcException("class " + astClass.getSimpleName() + " is not final. Transformation xml -> ast will fail");
            }
            if ( AnnotationHelper.isNepoAnnotatedClass(astClass) ) {
                return AnnotationHelper.block2astByAnnotation(block, astClass, this);
            } else {
                return block2phraseWithoutAnnotation(block, astClass);
            }
        } catch ( ClassNotFoundException cnfe ) {
            throw new DbcException("Could not load AST class " + className);
        }
    }

    private Phrase block2phraseWithoutAnnotation(Block block, Class<?> astClass) {
        java.lang.reflect.Method method = null;
        try {
            method = astClass.getMethod("xml2ast", Block.class);
        } catch ( NoSuchMethodException | SecurityException e ) {
        }
        if ( method != null ) {
            try {
                return (Phrase) method.invoke(null, block);
            } catch ( IllegalAccessException | InvocationTargetException e ) {
                throw new DbcException("not annotated xml -> ast: static method xml2ast(Block) not found for AST class " + astClass.getSimpleName(), e);
            }
        }
        try {
            method = astClass.getMethod("xml2ast", Block.class, Jaxb2ProgramAst.class);
        } catch ( NoSuchMethodException | SecurityException e ) {
        }
        if ( method != null ) {
            try {
                return (Phrase) method.invoke(null, block, this);
            } catch ( IllegalAccessException | InvocationTargetException e ) {
                throw new DbcException("not annotated xml -> ast: static method xml2ast(Block,Jaxb2ProgramAst) fails for AST class " + astClass.getSimpleName(), e);
            }
        }
        throw new DbcException("Could not find one of the two static method xml2ast for AST class " + astClass.getSimpleName());
    }

    /**
     * Transforms JAXB object to AST unary object expression.<br>
     * <br>
     *
     * @param block to be transformed
     * @param exprParam of the unary operation
     * @param operationType performed on the exprParam
     * @return AST unary expression object
     */
    public Phrase blockToUnaryExpr(Block block, ExprParam exprParam, String operationType) {
        String op = Jaxb2Ast.getOperation(block, operationType);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase expr = extractValue(values, exprParam);
        return new Unary(Unary.Op.get(op), Jaxb2Ast.convertPhraseToExpr(expr), Jaxb2Ast.extractBlocklyProperties(block));
    }

    /**
     * Transforms JAXB object to AST binary object expression.<br>
     * <br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block to be transformed
     * @param leftExpr parameter of the expression
     * @param rightExpr parameter of the expression
     * @param operationType of the expression
     * @return AST binary expression object
     */
    public Binary blockToBinaryExpr(Block block, ExprParam leftExpr, ExprParam rightExpr, String operationType) {
        String op = Jaxb2Ast.getOperation(block, operationType);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        Phrase left = extractValue(values, leftExpr);
        Phrase right = extractValue(values, rightExpr);
        String operationRange = "";
        if ( block.getMutation() != null && block.getMutation().getOperatorRange() != null ) {
            operationRange = block.getMutation().getOperatorRange();
        }
        return new Binary(Binary.Op.get(op), Jaxb2Ast.convertPhraseToExpr(left), Jaxb2Ast.convertPhraseToExpr(right), operationRange, Jaxb2Ast.extractBlocklyProperties(block));
    }

    /**
     * Extracts expression parameters from JAXB {@link Block} class.<br>
     * <br>
     * Client must provide the JAXB source block and list of {@link ExprParam} with the correct names of the parameters.<br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block as source class
     * @param exprParams that are extracted from the block
     * @return list of parameters represented with the {@link Expr} class.
     */
    public List<Expr> extractExprParameters(Block block, List<ExprParam> exprParams) {
        List<Expr> params = new ArrayList();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) exprParams.size());
        for ( ExprParam exprParam : exprParams ) {
            params.add(Jaxb2Ast.convertPhraseToExpr(extractValue(values, exprParam)));
        }
        return params;
    }

    /**
     * Transforms JAXB object to AST if statement object.<br>
     * <br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block that is transformed
     * @param _else number else's
     * @param _elseIf number of else if's
     * @return if statement object from the AST representation
     */
    public Phrase blocksToIfStmt(Block block, int _else, int _elseIf) {
        List<Expr> exprsList = new ArrayList();
        List<StmtList> thenList = new ArrayList();
        StmtList elseList = null;

        List<Value> values = new ArrayList();
        List<Statement> statements = new ArrayList();

        if ( _else + _elseIf != 0 ) {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            Assert.isTrue(valAndStmt.size() <= 2 * _elseIf + 2 + _else);
            Jaxb2Ast.convertStmtValList(values, statements, valAndStmt);
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 1);
            statements = Jaxb2Ast.extractStatements(block, (short) 1);
        }

        for ( int i = 0; i < _elseIf + _else + 1; i++ ) {
            if ( _else != 0 && i == _elseIf + _else ) {
                elseList = extractStatement(statements, BlocklyConstants.ELSE);
            } else {
                Phrase p = extractValue(values, new ExprParam(BlocklyConstants.IF + i, BlocklyType.BOOLEAN));
                exprsList.add(Jaxb2Ast.convertPhraseToExpr(p));
                thenList.add(extractStatement(statements, BlocklyConstants.DO + i));
            }
        }

        if ( _else != 0 ) {
            return new IfStmt(Jaxb2Ast.extractBlocklyProperties(block), exprsList, thenList, elseList, _else, _elseIf);
        }
        StmtList elseList1 = new StmtList();
        elseList1.setReadOnly();
        return new IfStmt(Jaxb2Ast.extractBlocklyProperties(block), exprsList, thenList, elseList1, _else, _elseIf);
    }

    /**
     * Transforms JAXB {@link Block} to list of expressions.
     *
     * @param block to be transformed
     * @param defVal if the expression is missing
     * @return list of expressions
     */
    public ExprList blockToExprList(Block block, BlocklyType defVal) {
        int items = 0;
        if ( block.getMutation().getItems() != null ) {
            items = block.getMutation().getItems().intValue();
        }
        BlocklyType[] types = new BlocklyType[items];
        Arrays.fill(types, defVal);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= items, "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, types, items, BlocklyConstants.ADD);
    }

    /**
     * Transforms JAXB list of {@link Arg} objects to list of AST expressions.
     *
     * @param arguments to be transformed
     * @return list of AST expressions
     */
    public static ExprList argumentsToExprList(List<Arg> arguments) {
        ExprList parameters = new ExprList();
        for ( Arg arg : arguments ) {
            Var parametar = new Var(BlocklyType.get(arg.getType()), arg.getName(), BlocklyProperties.make("PARAMETER", "1"));
            parameters.addExpr(parametar);
        }
        parameters.setReadOnly();
        return parameters;
    }

    /**
     * Convert list of values ({@link Value}) to list of expressions ({@link ExprList}).
     *
     * @param values to be converted
     * @param nItems that should be converted
     * @param name of the values
     */
    public ExprList valuesToExprList(List<Value> values, BlocklyType[] parametersTypes, int nItems, String name) {
        ExprList exprList = new ExprList();
        for ( int i = 0; i < nItems; i++ ) {
            exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(extractValue(values, new ExprParam(name + i, parametersTypes[i]))));
        }
        exprList.setReadOnly();
        return exprList;
    }

    /**
     * Extract specific value from the list of values.
     *
     * @param values as a source
     * @param param with name of the value and default value if the value is missing (see. {@link ExprParam})
     * @return AST object or {@link EmptyExpr} if the value is missing
     */
    public Phrase extractValue(List<Value> values, ExprParam param) {
        for ( Value value : values ) {
            if ( value.getName().equals(param.getName()) ) {
                return extractBlock(value);
            }
        }
        return new EmptyExpr(param.getDefaultValue());
    }

    /**
     * Extract {@link Statement} from list of statements.
     *
     * @param statements as source
     * @param stmtName to be extracted
     */
    public StmtList extractStatement(List<Statement> statements, String stmtName) {
        StmtList stmtList = new StmtList();
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToStmtList(statement.getBlock());
            }
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    /**
     * Convert list of {@link Statement} to {@link ExprList} of {@link VarDeclaration}.
     *
     * @param statements as source
     * @param stmtName of statement to be extracted
     */
    public ExprList statementsToMethodParameterDeclaration(List<Statement> statements, String stmtName) {
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToMethodParameterDeclaration(statement.getBlock());
            }
        }
        // Parameter list is empty
        ExprList exprList = new ExprList();
        exprList.setReadOnly();
        return exprList;
    }

    private ExprList blocksToMethodParameterDeclaration(List<Block> exprBolcks) {
        ExprList exprList = new ExprList();
        for ( Block exb : exprBolcks ) {
            Phrase p = block2ast(exb);
            if ( p instanceof VarDeclaration ) {
                exprList.addExpr((VarDeclaration) p);
            } else {
                throw new DbcException("invalid delaration of parameters of a function");
            }
        }
        exprList.setReadOnly();
        return exprList;
    }

    /**
     * get from a value list (a XML substructure) the phrase matching a variable.<br>
     * Returning null is dangerous. Currently it is needed for the checking of empty fields of nano33ble
     *
     * @param values
     * @param name name of the variable
     * @return the Var phrase; return null, if not found
     */
    public Expr getVar(List<Value> values, String name) {
        Phrase p = extractValue(values, new ExprParam(name, BlocklyType.NUMBER));
        if ( p instanceof Var ) {
            return (Var) p;
        } else {
            return new EmptyExpr(BlocklyType.NUMBER);
        }
    }

    public Phrase extractRepeatStatement(Block block, Phrase expr, String mode, String location, int mutation) {
        List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) mutation);
        StmtList stmtList = extractStatement(statements, location);
        return new RepeatStmt(RepeatStmt.Mode.get(mode), Jaxb2Ast.convertPhraseToExpr(expr), stmtList, Jaxb2Ast.extractBlocklyProperties(block));
    }

    private Phrase extractBlock(Value value) {
        Shadow shadow = value.getShadow();
        Block block = value.getBlock();
        if ( shadow != null ) {
            Block shadowBlock = Jaxb2Ast.shadow2block(shadow);
            if ( block != null ) {
                return new ShadowExpr(Jaxb2Ast.convertPhraseToExpr(block2ast(shadowBlock)), Jaxb2Ast.convertPhraseToExpr(block2ast(block)));
            }
            return new ShadowExpr(Jaxb2Ast.convertPhraseToExpr(block2ast(shadowBlock)), null);
        } else {
            return block2ast(block);
        }
    }

    private StmtList blocksToStmtList(List<Block> statementBlocks) {
        StmtList stmtList = new StmtList();
        for ( Block sb : statementBlocks ) {
            convertPhraseToStmt(stmtList, sb);
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    private void convertPhraseToStmt(StmtList stmtList, Block sb) {
        Phrase p;

        p = block2ast(sb);

        Stmt stmt;
        if ( p.getKind().getCategory() == Category.EXPR ) {
            stmt = new ExprStmt((Expr) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            stmt = new ActionStmt((Action) p);
        } else if ( p.getKind().getCategory() == Category.SENSOR ) {
            stmt = new SensorStmt((Sensor) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            stmt = new FunctionStmt((Function) p);
        } else if ( p.getKind().getCategory() == Category.METHOD ) {
            stmt = new MethodStmt((Method) p);
        } else {
            stmt = (Stmt) p;
        }
        stmtList.addStmt(stmt);
    }

}
