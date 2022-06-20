package de.fhg.iais.roberta.util.ast;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.ClassPath;

import de.fhg.iais.roberta.bean.WaitUntilSensorBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This container holds a lot of maps and sets extracted from annotations of AST classes (starting with @Nepo...). These classes refer both to programs and configurations,
 * though the majority refers to program.
 * A robot adds new data to this container, when it has classes in the package "de.fhg.iais.roberta.syntax" or below.
 */
public class AstFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AstFactory.class);

    /**
     * maps a class name to the {@link BlockDescriptor}, that is extracted from the annotations
     */
    private static final Map<String, BlockDescriptor> blockTypesByClassName = new HashMap<>();
    /**
     * maps a blockly name to the {@link BlockDescriptor}, that is responsible fpr processing this block
     */
    private static final Map<String, BlockDescriptor> blockTypeByBlocklyName = new HashMap<>();
    /**
     * maps a field name used by blockly to describe a sensor usable in a getSensor block to the sensor and the sensor's mode, that is responsible fpr processing this block
     */
    private static final Map<String, WaitUntilSensorBean> waitUntilMap = new HashMap<>();
    /**
     * the list of all legal modes used in the blockly frontend
     */
    private static final String[] allLegalModesArray = {
        "ALTITUDE", "AMBIENTLIGHT", "ANALOG", "ANGLE", "BACKWARD", "CALIBRATE", "CALIBRATION", "CO2EQUIVALENT", "COLOUR", "COMPASS",
        "DATE", "DEFAULT", "DEGREE", "DIGITAL", "DISTANCE", "DOWN", "EDISON_CODE", "FACE_DOWN", "FACE_UP", "FOREWARD", "FREEFALL",
        "G3", "G6", "G8", "HUMIDITY", "IAQ", "IDALL", "IDONE", "LATITUDE", "LEFT", "LIGHT", "LIGHT_VALUE", "LINE", "LONGITUDE", "MODULATED",
        "NAMEALL", "NAMEONE", "NONE", "OBSTACLE", "OFF", "PM10", "PM25", "PRESENCE", "PRESSED", "PRESSURE", "PULSEHIGH", "PULSELOW", "RATE",
        "RCCODE", "RED", "REFLEXION", "RESET", "RGB", "RIGHT", "ROTATION", "SEEK", "SERIAL", "SHAKE", "SOUND", "SPEED", "STRENGTH", "TEMPERATURE",
        "TILTED", "TIME", "UNMODULATED", "UP", "UVLIGHT", "VALUE", "VOCEQUIVALENT", "WAIT_FOR_PRESS", "WAIT_FOR_PRESS_AND_RELEASE", "X", "Y", "Z"
    };
    /**
     * the hash set of all legal modes used in the blockly frontend
     */
    private static final Set<String> allLegalModes = new HashSet(Arrays.asList(allLegalModesArray));
    /**
     * a map build from the data found in the @NepoConfiguration annotation of configuration AST classes. It maps the blockly name of the configuration block
     * to an internal name that is used to generate the AST object (always of type {@link ConfigurationComponent}, which represent the confuguration block.
     */
    private static final Map<String, String> configurationComponentTypes = new HashMap<>();

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
        String className = astClass.getName();
        String name, category;
        String[] blocklyNames;
        NepoSampleValue[] nepoSampleValues;
        for ( Annotation general : astClass.getAnnotations() ) {
            if ( general instanceof NepoPhrase ) {
                NepoPhrase specific = (NepoPhrase) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                nepoSampleValues = specific.sampleValues();
                addToSampleValues(className, nepoSampleValues);
                addToBlockTypeByBlocklyName(name, category, blocklyNames, astClass);
                return;
            } else if ( general instanceof NepoExpr ) {
                NepoExpr specific = (NepoExpr) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                nepoSampleValues = specific.sampleValues();
                addToSampleValues(className, nepoSampleValues);
                addToBlockTypeByBlocklyName(name, category, blocklyNames, astClass);
                return;
            } else if ( general instanceof NepoBasic ) {
                NepoBasic specific = (NepoBasic) general;
                name = specific.containerType();
                category = specific.category();
                blocklyNames = specific.blocklyNames();
                nepoSampleValues = specific.sampleValues();
                addToSampleValues(className, nepoSampleValues);
                addToBlockTypeByBlocklyName(name, category, blocklyNames, astClass);
                return;
            } else if ( general instanceof NepoConfiguration ) {
                NepoConfiguration specific = (NepoConfiguration) general;
                String blockDescription = specific.containerType();
                for ( String blocklyName : specific.blocklyNames() ) {
                    configurationComponentTypes.put(blocklyName, blockDescription);
                }
                return;
            }
        }
    }

    public static BlockDescriptor getBlockType(Class<?> astClass) {
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
    public static BlockDescriptor getByBlocklyName(String blocklyName) {
        BlockDescriptor blockDescriptor = blockTypeByBlocklyName.get(blocklyName.toLowerCase());
        Assert.notNull(blockDescriptor, "blockly name is not found: " + blocklyName);
        return blockDescriptor;
    }

    public static String validateAndGetMode(String mode) {
        Assert.nonEmptyString(mode, "Invalid mode");
        final String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        if ( allLegalModes.contains(sUpper) ) {
            return sUpper;
        } else {
            throw new DbcException(String.format("Undefined mode %s", mode));
        }
    }

    public static WaitUntilSensorBean getWaitUntilSensorBeanByBlocklyFieldName(String blocklyFieldName) {
        WaitUntilSensorBean waitUntilSensorBean = waitUntilMap.get(blocklyFieldName);
        Assert.notNull(waitUntilSensorBean, "WaitUntilSensorBean not found for: %s", blocklyFieldName);
        return waitUntilSensorBean;
    }

    public static String getConfigurationComponentTypeByBlocklyName(String blocklyName) {
        Assert.nonEmptyString(blocklyName, "Invalid blockly name for a configuration block");
        String blockType = configurationComponentTypes.get(blocklyName);
        Assert.notNull(blockType, "No component type for configuration blockly name %s found", blocklyName);
        return blockType;
    }

    public static List<Class<?>> getAstClasses() {
        return blockTypesByClassName.values().stream()
            .map(BlockDescriptor::getAstClass)
            .collect(Collectors.toList());
    }

    public static Set<String> getNamesOfAllASTClasses() {
        return blockTypesByClassName.keySet();
    }

    private static void addToBlockTypeByBlocklyName(String name, String category, String[] blocklyNames, Class<?> astClass) {
        Assert.notNull(name);
        Assert.notNull(category);
        Assert.notNull(blocklyNames);
        Assert.notNull(astClass);

        BlockDescriptor blockDescriptor = new BlockDescriptor(name, Category.valueOf(category), astClass, blocklyNames);
        BlockDescriptor firstClass = blockTypesByClassName.put(astClass.getName(), blockDescriptor);
        boolean legaTwice = firstClass == null || blockDescriptor.equals(firstClass);
        if ( !legaTwice ) {
            Assert.fail(String.format("AST classes %s used twice (in different plugins?)", astClass.getName()));
        }
        for ( String blocklyName : blocklyNames ) {
            BlockDescriptor firstMapper = blockTypeByBlocklyName.put(blocklyName.toLowerCase(), blockDescriptor);
            boolean legalDuplicate = firstMapper == null || blockDescriptor.equals(firstMapper);
            if ( !legalDuplicate ) {
                Assert.fail(String.format("AST classes %s and %s mapped both blockly name %s. Initialization aborted",
                    astClass.getName(), firstMapper.getAstClass().getName(), blocklyName));
            }
        }
    }

    private static void addToSampleValues(String className, NepoSampleValue[] nepoSampleValues) {
        for ( NepoSampleValue nepoSampleValue : nepoSampleValues ) {
            waitUntilMap.put(nepoSampleValue.blocklyFieldName(), new WaitUntilSensorBean(className, nepoSampleValue.sensor(), nepoSampleValue.mode()));
        }
    }
}