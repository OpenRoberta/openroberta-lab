package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This container holds all possible kind of objects that we can have to represent the AST (abstract syntax tree). The objects are separated in four main
 * {@link Category}. They have a unique (String) name to refer to them (in a code generator visitor, for instance). They can be retrieved<br>
 * - either by their (globally) unique name or<br>
 * - a list of (globally) unique names, each of which is used as element name in blockly XML.<br>
 * A concrete root can add dynamically new elements to this container, when the robot is registered during robot startup (this is why this container is
 * <i>not</i> implemented as a enum type. Duriong registraion of new block types the uniqueness of names is guaranteed by this container.
 */
public class BlockTypeContainer {
    private static final String[] NO_BLOCKLY_NAMES = new String[0];

    private static final List<String> loadedPropertyFiles = new ArrayList<>();

    private static final Map<String, BlockType> blockTypesByName = new HashMap<>();
    private static final Map<String, BlockType> blockTypesByBlocklyName = new HashMap<>();

    static {
        add("EXPR_LIST", Category.EXPR);
        add("SENSOR_EXPR", Category.EXPR);
        add("ACTION_EXPR", Category.EXPR);
        add("EMPTY_EXPR", Category.EXPR);
        add("SHADOW_EXPR", Category.EXPR);
        add("FUNCTION_EXPR", Category.EXPR);
        add("METHOD_EXPR", Category.EXPR);
        add("FUNCTIONS", Category.EXPR);
        add("EXPR_STMT", Category.STMT);
        add("STMT_LIST", Category.STMT);
        add("AKTION_STMT", Category.STMT);
        add("SENSOR_STMT", Category.STMT);
        add("FUNCTION_STMT", Category.STMT);
        add("METHOD_STMT", Category.STMT);
        add("LOCATION", Category.HELPER);
        add("TEXT_CHAR_AT_FUNCT", Category.FUNCTION);
        add("TEXT_TRIM_FUNCT", Category.FUNCTION);
        add("TEXT_PROMPT_FUNCT", Category.FUNCTION);
        add("TEXT_CHANGE_CASE_FUNCT", Category.FUNCTION);
    }

    public static void add(String name, Category category, Class<?> astClass, String... blocklyNames) {
        BlockType blockType = new BlockType(name, category, astClass, blocklyNames);
        BlockType oldValue = blockTypesByName.put(name.toLowerCase(), blockType);
        Assert.isNull(oldValue, "Block name %s is mapped twice. Initialization aborted", name);
        for ( String blocklyName : blocklyNames ) {
            oldValue = blockTypesByBlocklyName.put(blocklyName.toLowerCase(), blockType);
            Assert.isNull(oldValue, "Blockly name %s is mapped twice. Initialization aborted", blocklyName);
        }
    }

    private static void add(String name, Category category) {
        add(name, category, null, NO_BLOCKLY_NAMES);
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

    /**
     * Registers a property file to avoid loading property files more than once. Properties are not loaded by calling this method use the
     * {@link #add(String, Category, Class, String...)}
     *
     * @param propertyFileName
     * @return true if the property file was already loaded or false otherwise
     */
    public static boolean register(String propertyFileName) {
        if ( loadedPropertyFiles.contains(propertyFileName) ) {
            return true;
        } else {
            loadedPropertyFiles.add(propertyFileName);
            return false;
        }
    }

    public static class BlockType {
        private final String name;
        private final Category category;
        private final Class<?> astClass;
        private final String[] blocklyNames;

        private BlockType(String name, Category category, Class<?> astClass, String... blocklyNames) {
            this.name = name;
            this.category = category;
            this.astClass = astClass;
            this.blocklyNames = blocklyNames;
        }

        /**
         * @return the unique name in which {@link BlockType} belongs.
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return category in which {@link BlockType} belongs.
         */
        public Category getCategory() {
            return this.category;
        }

        /**
         * @return the astClass
         */
        public Class<?> getAstClass() {
            return this.astClass;
        }

        /**
         * @return the blocklyNames
         */
        public String[] getBlocklyNames() {
            return this.blocklyNames;
        }

        /**
         * check whether this block type has the name as expected
         *
         * @param nameToCheck
         * @return true, if the block type has the name expected; false otherwise
         */
        public boolean hasName(String... namesToCheck) {
            for ( String nameToCheck : namesToCheck ) {
                boolean found = this.name.equals(nameToCheck);
                if ( found ) {
                    return true;
                }
            }
            return false;
        }

    }
}