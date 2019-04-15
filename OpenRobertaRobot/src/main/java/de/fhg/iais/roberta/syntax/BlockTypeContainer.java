package de.fhg.iais.roberta.syntax;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Functions;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This container holds all possible kind of objects that we can have to represent the AST (abstract syntax tree). The objects are separated into
 * {@link Category}. They have a unique (String) name to refer to them (in a code generator visitor, for instance). They can be retrieved<br>
 * - either by their (globally) unique name or<br>
 * - a list of (globally) unique names, each of which is used as element name in blockly XML.<br>
 * A robot can add dynamically new elements to this container, when the robot is registered during robot startup (this is why this container is <i>not</i>
 * implemented as a enum type. During registraion of new block types the uniqueness of names is guaranteed by this container.
 */
public class BlockTypeContainer {
    private static final Logger LOG = LoggerFactory.getLogger(BlockTypeContainer.class);

    private static final Map<String, BlockType> blockTypesByName = new HashMap<>();
    private static final Map<String, BlockType> blockTypesByBlocklyName = new HashMap<>();

    static {
        add("EXPR_LIST", Category.EXPR, ExprList.class);
        add("SENSOR_EXPR", Category.EXPR, SensorExpr.class);
        add("ACTION_EXPR", Category.EXPR, ActionExpr.class);
        add("EMPTY_EXPR", Category.EXPR, EmptyExpr.class);
        add("FUNCTION_EXPR", Category.EXPR, FunctionExpr.class);
        add("METHOD_EXPR", Category.EXPR, MethodExpr.class);
        add("FUNCTIONS", Category.EXPR, Functions.class);
        add("EXPR_STMT", Category.STMT, ExprStmt.class);
        add("STMT_LIST", Category.STMT, StmtList.class);
        add("AKTION_STMT", Category.STMT, ActionStmt.class);
        add("SENSOR_STMT", Category.STMT, SensorStmt.class);
        add("FUNCTION_STMT", Category.STMT, FunctionStmt.class);
        add("METHOD_STMT", Category.STMT, MethodStmt.class);
        add("LOCATION", Category.HELPER, Location.class);

    }

    private static void add(String name, Category category, Class<?> astClass) {
        add(name, category, astClass, new String[0]);
    }

    public static void add(String name, Category category, Class<?> astClass, String... blocklyNames) {
        List<String> newNames = null;
        BlockType blockType = blockTypesByName.get(name.toLowerCase());
        if ( blockType == null ) {
            blockType = new BlockType(name, category, astClass, blocklyNames);
            blockTypesByName.put(name.toLowerCase(), blockType);
            newNames = Arrays.asList(blocklyNames);
        } else {
            Assert.isTrue(blockType.getCategory().equals(category), "Block name %s has different category when updated", name);
            Class<?> oldClass = blockType.getAstClass();
            if ( oldClass == null && astClass != null ) {
                throw new DbcException("Block name " + name + " has different implementation classes (1)");
            }
            if ( oldClass != null && astClass == null ) {
                throw new DbcException("Block name " + name + " has different implementation classes (2)");
            }
            if ( !(oldClass == null && astClass == null || oldClass.getCanonicalName().equals(astClass.getCanonicalName())) ) {
                throw new DbcException("Block name " + name + " has different implementation classes (3)");
            }
            newNames = blockType.addBlocklyNames(blocklyNames);
            if ( newNames.size() > 0 ) {
                LOG.error("blocktype " + name + " is extended!");
            }
        }
        for ( String blocklyName : newNames ) {
            BlockType checkBlocktype = blockTypesByBlocklyName.put(blocklyName.toLowerCase(), blockType);
            Assert.isNull(checkBlocktype, "In block %s the blockly name %s is mapped twice. Initialization aborted", name, blocklyName);
        }
    }

    /**
     * access block type values by its unique name
     *
     * @param name the unique name of the block type, never null
     * @return the block type value, never null
     */
    public static BlockType getByName(String name) {
        Assert.notNull(name);
        BlockType blockType = blockTypesByName.get(name.toLowerCase());
        Assert.notNull(blockType);
        return blockType;
    }

    /**
     * access block type values by its unique blockly name
     *
     * @param blocklyName the unique blockly name of the block type, never null
     * @return the block type value, never null
     */
    public static BlockType getByBlocklyName(String blocklyName) {
        BlockType blockType = blockTypesByBlocklyName.get(blocklyName.toLowerCase());
        Assert.notNull(blockType, "blockly name is not found: " + blocklyName);
        return blockType;
    }
}