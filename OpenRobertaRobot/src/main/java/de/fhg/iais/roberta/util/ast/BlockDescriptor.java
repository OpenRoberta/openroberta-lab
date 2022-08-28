package de.fhg.iais.roberta.util.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * contains the properties, that are relevant for AST classes. AST classes are always subclasses of {@link Phrase}.
 * AST classes are 1:n related to blockly blocks, i.e. each blockly block has exactly one AST class.
 * The properties are extracted ONLY from the annotations @Nepo... found in AST classes and stored redundantly in each object of a
 * AST class in its superclass in field {@link Phrase#blockDescriptor}.
 */
public class BlockDescriptor {
    /**
     * the name of the block. This is NOT a blockly name! Should not be used. But is used a lot, e.g. in {@link #hasName(String...)} hasName}
     * Todo: remove the name of the block descriptor and its usages. Hard work.
     */
    private final String name;
    /**
     * the category of the AST class. To distinguish sensors, actor, configuration for instance
     */
    private final Category category;
    /**
     * the class that represents a blockly block server side. It stores the fields and values found in the blockly xml for checks and code generation
     */
    private final Class<?> astClass;
    /**
     * the REAL names used by blockly in the blockly xml which are mapped to the AST class described here
     */
    private final Set<String> blocklyNames;
    /**
     * the huge get sample sensor allows to integrate many sensors together with a mode into one block. This map maps the blockly field name to the sensor mode
     * to be used. Only used by block descriptors for AST classes, which are external sensors.
     */
    private final Map<String, String> blocklyFieldToSensorMode;

    public BlockDescriptor(String name, Category category, Class<?> astClass, String[] blocklyNames, Map<String, String> blocklyFieldToSensorMode) {
        Assert.notNull(name);
        Assert.notNull(category);
        Assert.notNull(blocklyNames);
        Assert.notNull(astClass);
        this.name = name;
        this.category = category;
        this.astClass = astClass;
        this.blocklyNames = new HashSet<>(Arrays.asList(blocklyNames));
        this.blocklyFieldToSensorMode = blocklyFieldToSensorMode;
    }

    /**
     * @return the name of the block. This is NOT a blockly name!
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the category of the {@link BlockDescriptor}
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * @return the astClass which represents blockly blocks
     */
    public Class<?> getAstClass() {
        return this.astClass;
    }

    /**
     * @return the mode, that is declared responsible for the get sample sensor blockly field
     */
    public String getSensorModeFromBlocklyField(String blocklyField) {
        String mode = this.blocklyFieldToSensorMode.get(blocklyField);
        Assert.notNull(mode, "no mode found for: %s in block descriptor: %s", blocklyField, this.name);
        return mode;
    }

    /**
     * check whether this block descriptor is equals to one of the names given as parameter
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

    /**
     * only used in one test!
     *
     * @return the set of blockly names, which are connected to the AST class described in this descriptor
     */
    public Set<String> getBlocklyNames() {
        return Collections.unmodifiableSet(this.blocklyNames);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.astClass == null) ? 0 : this.astClass.hashCode());
        result = prime * result + ((this.blocklyNames == null) ? 0 : this.blocklyNames.hashCode());
        result = prime * result + ((this.category == null) ? 0 : this.category.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        BlockDescriptor other = (BlockDescriptor) obj;
        if ( this.astClass == null ) {
            if ( other.astClass != null ) {
                return false;
            }
        } else if ( !this.astClass.getCanonicalName().equals(other.astClass.getCanonicalName()) ) {
            return false;
        }
        if ( this.blocklyNames == null ) {
            if ( other.blocklyNames != null ) {
                return false;
            }
        } else if ( !this.blocklyNames.equals(other.blocklyNames) ) {
            return false;
        }
        if ( this.category != other.category ) {
            return false;
        }
        if ( this.name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !this.name.equals(other.name) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlockType [name="
            + this.name
            + ", category="
            + this.category
            + ", astClass="
            + (this.astClass == null ? "null" : this.astClass.getCanonicalName())
            + ", blocklyNames="
            + this.blocklyNames
            + "]";
    }

}