package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.util.dbc.Assert;

public class BlockType {
    private final String name;
    private final Category category;
    private final Class<?> astClass;
    private Set<String> blocklyNames;

    public BlockType(String name, Category category, Class<?> astClass, String... blocklyNames) {
        Assert.notNull(name);
        Assert.notNull(category);
        Assert.notNull(blocklyNames);
        this.name = name;
        this.category = category;
        this.astClass = astClass;
        this.blocklyNames = new HashSet<>(Arrays.asList(blocklyNames));
    }

    public List<String> addBlocklyNames(String[] blocklyNames) {
        List<String> newNames = new ArrayList<>();
        for ( String blocklyName : blocklyNames ) {
            if ( this.blocklyNames.add(blocklyName) ) {
                newNames.add(blocklyName);
            }
        }
        return newNames;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((astClass == null) ? 0 : astClass.hashCode());
        result = prime * result + ((blocklyNames == null) ? 0 : blocklyNames.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        BlockType other = (BlockType) obj;
        if ( astClass == null ) {
            if ( other.astClass != null ) {
                return false;
            }
        } else if ( !astClass.getCanonicalName().equals(other.astClass.getCanonicalName()) ) {
            return false;
        }
        if ( blocklyNames == null ) {
            if ( other.blocklyNames != null ) {
                return false;
            }
        } else if ( !blocklyNames.equals(other.blocklyNames) ) {
            return false;
        }
        if ( category != other.category ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals(other.name) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlockType [name="
            + name
            + ", category="
            + category
            + ", astClass="
            + (astClass == null ? "null" : astClass.getCanonicalName())
            + ", blocklyNames="
            + blocklyNames
            + "]";
    }

}