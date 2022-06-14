package de.fhg.iais.roberta.util.syntax;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.ClassPath;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This container holds a 1:1 map of {@link BlockType}-objects for all classes we use to represent the AST (abstract syntax tree). This map stores data
 * extracted from class annotations (starting with @Nepo...). In particular:
 * - a {@link Category}, used in a code generator visitor, for instance
 * - a list of (globally) unique names, each of which is used as element name in blockly XML. They can be retrieved by this name<br>
 * A robot adds new classes to this container, when they are found in the package "de.fhg.iais.roberta.syntax" or below.
 */
public class BlockTypeContainer {
    private static final Logger LOG = LoggerFactory.getLogger(BlockTypeContainer.class);

    private static final Map<String, BlockType> blockTypesByClassName = new HashMap<>();
    private static final Map<String, BlockType> blockTypesByBlocklyName = new HashMap<>();

    private static boolean alreadyLoaded = false;

    public static void loadBlocks() {
        if ( alreadyLoaded ) {
            return;
        }
        alreadyLoaded = true;
        try {
            ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getName().startsWith("de.fhg.iais.roberta.syntax."))
                .map(clazzInfo -> clazzInfo.load())
                .forEach(clazz -> add(clazz));
        } catch ( IOException e ) {
            throw new DbcException("loading of blocks failed", e);
        }
    }

    public static void add(Class<?> astClass) {
        String name, category;
        String[] blocklyNames;
        for ( Annotation general : astClass.getAnnotations() ) {
            if ( general instanceof NepoPhrase ) {
                NepoPhrase specific = (NepoPhrase) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                add(name, category, blocklyNames, astClass);
                return;
            } else if ( general instanceof NepoExpr ) {
                NepoExpr specific = (NepoExpr) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                add(name, category, blocklyNames, astClass);
                return;
            } else if ( general instanceof NepoBasic ) {
                NepoBasic specific = (NepoBasic) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                add(name, category, blocklyNames, astClass);
                return;
            }
        }
    }

    public static BlockType getBlockType(Class<?> astClass) {
        String name;
        for ( Annotation general : astClass.getAnnotations() ) {
            if ( general instanceof NepoPhrase ) {
                NepoPhrase specific = (NepoPhrase) general;
                name = specific.containerType();
                return blockTypesByClassName.get(astClass.getName());
            } else if ( general instanceof NepoExpr ) {
                NepoExpr specific = (NepoExpr) general;
                name = specific.containerType();
                return blockTypesByClassName.get(astClass.getName());
            } else if ( general instanceof NepoBasic ) {
                NepoBasic specific = (NepoBasic) general;
                name = specific.containerType();
                return blockTypesByClassName.get(astClass.getName());
            }
        }
        throw new DbcException("no valid @Nepo... class annotation found");
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

    public static List<Class<?>> getAstClasses() {
        return blockTypesByClassName.values().stream()
            .map(BlockType::getAstClass)
            .collect(Collectors.toList());
    }

    public static Set<String> getNamesOfAllASTClasses() {
        return blockTypesByClassName.keySet();
    }

    private static void add(String name, String category, String[] blocklyNames, Class<?> astClass) {
        Assert.notNull(name);
        Assert.notNull(category);
        Assert.notNull(blocklyNames);
        Assert.notNull(astClass);

        BlockType blockType = new BlockType(name, Category.valueOf(category), astClass, blocklyNames);
        BlockType firstClass = blockTypesByClassName.put(astClass.getName(), blockType);
        boolean legaTwice = firstClass == null || blockType.equals(firstClass);
        if ( !legaTwice ) {
            Assert.fail(String.format("AST classes %s used twice (in different plugins?)", astClass.getName()));
        }
        for ( String blocklyName : blocklyNames ) {
            BlockType firstMapper = blockTypesByBlocklyName.put(blocklyName.toLowerCase(), blockType);
            boolean legalDuplicate = firstMapper == null || blockType.equals(firstMapper);
            if ( !legalDuplicate ) {
                Assert.fail(String.format("AST classes %s and %s mapped both blockly name %s. Initialization aborted",
                    astClass.getName(), firstMapper.getAstClass().getName(), blocklyName));
            }
        }
    }
}