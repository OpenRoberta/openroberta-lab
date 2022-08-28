package de.fhg.iais.roberta.transformer;

import java.util.List;

import javax.annotation.Nonnull;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

public class Jaxb2Ast {
    private Jaxb2Ast() {
    }

    /**
     * Converts mixed list of {@link Value} and {@link Statement} into to separated lists
     *
     * @param values list for saving values
     * @param statements list for saving statements
     * @param valAndStmt list to be separated
     */
    public static void convertStmtValList(List<Value> values, List<Statement> statements, List<Object> valAndStmt) {
        for ( Object ob : valAndStmt ) {
            if ( ob.getClass() == Value.class ) {
                values.add((Value) ob);
            } else {
                statements.add((Statement) ob);
            }
        }
    }

    /**
     * Transforms JAXB list of {@link Arg} objects to list of AST parameters type.
     *
     * @param arguments to be transformed
     * @return array of AST expressions
     */
    public static BlocklyType[] argumentsToParametersType(List<Arg> arguments) {
        BlocklyType[] types = new BlocklyType[arguments.size()];
        int i = 0;
        for ( Arg arg : arguments ) {
            types[i] = BlocklyType.get(arg.getType());
            i++;
        }

        return types;
    }

    /**
     * Extract the operation from block.
     *
     * @param operationType name of the xml element where the operation is stored
     * @return the name of the operation
     */
    public static String getOperation(Block block, String operationType) {
        String op = operationType;
        if ( !block.getField().isEmpty() ) {
            op = extractOperation(block, operationType);
        }
        return op;
    }

    /**
     * Extract values from a {@link Block}. <br>
     * <br>
     * Throws {@link DbcException} if the number of values is not less or equal to the numOfValues
     *
     * @param block from which the values are extracted
     * @param numOfValues to be extracted
     * @return list of {@link Value}
     */
    public static List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    /**
     * Extract {@link Statement} from the list of statements. <br>
     * <br>
     * Throws {@link DbcException} if the number of statements is not less or equal to the numOfStatements
     *
     * @param block as source
     * @param numOfStatements to be extracted
     * @return list of statements
     */
    public static List<Statement> extractStatements(Block block, short numOfStatements) {
        List<Statement> statements;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= numOfStatements, "Statements size is not less or equal to " + numOfStatements + "!");
        return statements;
    }

    /**
     * Extract {@link Field} from a {@link Block}. <br>
     * <br>
     * Throws {@link DbcException} if the number of fields is not less or equal to the numOfFields
     *
     * @param block as source
     * @param numOfFields to be extracted
     * @return list of fields
     */
    public static List<Field> extractFields(Block block, short numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() <= numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    /**
     * Extract field from list of {@link Field}. If the field with the given name is not found it returns the default {@link Value}.<br>
     * <br>
     * Throws {@link DbcException} if the field is not found and the defaultValue is set to <b>null</b>.
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @param defaultValue if the field is not existent
     * @return value containing the field
     */
    public static String extractField(List<Field> fields, String name, String defaultValue) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        if ( defaultValue == null ) {
            throw new DbcException("There is no field with name " + name);
        } else {
            return defaultValue;
        }
    }

    /**
     * Extract field from list of {@link Field}. If the field with the given name is not found or it is empty, it returns the default Value.<br>
     *
     * @param fields list as a source
     * @param name of the field to be extracted
     * @param defaultValue if the field does not existent
     * @return value of the field
     */
    public static String extractNonEmptyField(List<Field> fields, String name, @Nonnull String defaultValue) {
        Assert.notNull(defaultValue);
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                String value = field.getValue();
                if ( value.trim().equals("") ) {
                    return defaultValue;
                } else {
                    return value;
                }
            }
        }
        return defaultValue;
    }

    /**
     * Extract field from list of {@link Field}. <br>
     * <br>
     * Throws {@link DbcException} if the field is not found
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @return value containing the field
     */
    public static String extractField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        throw new DbcException("There is no field with name " + name);
    }

    /**
     * Extract an optional field from a list of {@link Field}. <br>
     * <br>
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @return value containing the field; if the key was not found, return null
     */
    public static String optField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        return null;
    }

    public static String extractOperation(Block block, String name) {
        List<Field> fields = extractFields(block, (short) 1);
        return extractField(fields, name);
    }

    /**
     * Extracts the blockly properties of the {@link Block}.
     *
     * @param block the blockly block
     */
    public static BlocklyProperties extractBlocklyProperties(Block block) {
        return new BlocklyProperties(block.getType(), block.getId(), isDisabled(block), isCollapsed(block), isInline(block), isDeletable(block), isMovable(block), isInTask(block), isShadow(block), isErrorAttribute(block), block.getComment());
    }

    public static int getElseIf(Mutation mutation) {
        if ( mutation != null && mutation.getElseif() != null ) {
            return mutation.getElseif().intValue();
        }
        return 0;
    }

    public static int getElse(Mutation mutation) {
        if ( mutation != null && mutation.getElse() != null ) {
            return mutation.getElse().intValue();
        }
        return 0;
    }

    private static boolean isDisabled(Block block) {
        return block.isDisabled() != null;
    }

    private static boolean isCollapsed(Block block) {
        return block.isCollapsed() != null;
    }

    private static Boolean isInline(Block block) {
        if ( block.isInline() == null ) {
            return null;
        }
        return block.isInline();
    }

    private static Boolean isDeletable(Block block) {
        if ( block.isDeletable() == null ) {
            return null;
        }
        return block.isDeletable();
    }

    private static Boolean isMovable(Block block) {
        if ( block.isMovable() == null ) {
            return null;
        }
        return block.isMovable();
    }

    private static Boolean isInTask(Block block) {
        if ( block.isIntask() == null ) {
            return null;
        }
        return block.isIntask();
    }

    private static Boolean isShadow(Block block) {
        if ( block.isShadow() == null ) {
            return null;
        }
        return block.isShadow();
    }

    private static Boolean isErrorAttribute(Block block) {
        if ( block.isErrorAttribute() == null ) {
            return null;
        }
        return block.isErrorAttribute();
    }

    /**
     * Converts {@link Phrase} to {@link Expr}.
     *
     * @param p to be converted to expression
     */
    public static Expr convertPhraseToExpr(Phrase p) {
        Expr expr;
        if ( p.getKind().getCategory() == Category.SENSOR ) {
            expr = new SensorExpr((Sensor) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            expr = new ActionExpr((Action) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            expr = new FunctionExpr((Function) p);
        } else if ( p.getKind().getCategory() == Category.METHOD ) {
            expr = new MethodExpr((Method) p);
        } else {
            expr = (Expr) p;
        }
        return expr;
    }

    /**
     * Extracts variable from a {@link Block}.
     *
     * @param block from which variable is extracted
     * @return AST object representing variable
     */
    public static Phrase extractVar(Block block) {
        String typeVar = block.getMutation() != null ? block.getMutation().getDatatype() : BlocklyConstants.NUMBER;
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, BlocklyConstants.VAR);
        return new Var(BlocklyType.get(typeVar), field, extractBlocklyProperties(block));
    }

    public static Block shadow2block(Shadow shadow) {
        Block block = new Block();
        block.setId(shadow.getId());
        block.setType(shadow.getType());
        block.setIntask(shadow.isIntask());
        block.getField().add(shadow.getField());
        block.setShadow(true);
        return block;
    }

    public static String sanitizePort(String port) {
        if ( port == null || port.isEmpty() ) {
            return BlocklyConstants.EMPTY_PORT;
        }
        return port;
    }

    /**
     * Get a sensor port from {@link ISensorPort} given string parameter. It is possible for one sensor port to have multiple string mappings. Throws exception
     * if the sensor port does not exists.
     *
     * @param name of the sensor port
     * @return the sensor port from the enum {@link ISensorPort}
     */
    public static String sanitizeSlot(String slot) {
        Assert.notNull(slot, "Null slot port!");
        if ( slot.isEmpty() ) {
            return BlocklyConstants.EMPTY_SLOT;
        }
        return slot;
    }
}
