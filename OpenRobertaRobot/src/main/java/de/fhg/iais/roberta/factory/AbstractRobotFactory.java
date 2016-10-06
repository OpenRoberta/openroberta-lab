package de.fhg.iais.roberta.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.mode.general.PickColor;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class AbstractRobotFactory implements IRobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRobotFactory.class);

    @Override
    public IIndexLocation getIndexLocation(String indexLocation) {
        if ( indexLocation == null || indexLocation.isEmpty() ) {
            throw new DbcException("Invalid Index Location: " + indexLocation);
        }
        String sUpper = indexLocation.trim().toUpperCase(Locale.GERMAN);
        for ( IndexLocation po : IndexLocation.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid Index Location: " + indexLocation);
    }

    @Override
    public List<IIndexLocation> getIndexLocations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IListElementOperations getListElementOpertaion(String operation) {
        if ( operation == null || operation.isEmpty() ) {
            throw new DbcException("Invalid List Operation: " + operation);
        }
        String sUpper = operation.trim().toUpperCase(Locale.GERMAN);
        for ( ListElementOperations po : ListElementOperations.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid List Operation: " + operation);
    }

    @Override
    public List<IListElementOperations> getListElementOpertaions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPickColor getPickColor(String color) {
        if ( color == null || color.isEmpty() ) {
            throw new DbcException("Invalid Color: " + color);
        }
        String sUpper = color.trim().toUpperCase(Locale.GERMAN);
        for ( PickColor po : PickColor.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid Color: " + color);
    }

    @Override
    public IPickColor getPickColor(int colorId) {
        for ( PickColor sp : PickColor.values() ) {
            if ( sp.getColorID() == colorId ) {
                return sp;
            }
        }
        throw new DbcException("Invalid color: " + colorId);
    }

    @Override
    public List<IPickColor> getPickColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITurnDirection getTurnDirection(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Turn Direction: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( TurnDirection dir : TurnDirection.values() ) {
            if ( dir.toString().equals(sUpper) ) {
                return dir;
            }
            for ( String value : dir.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return dir;
                }
            }
        }
        throw new DbcException("Invalid Turn Direction: " + direction);

    }

    @Override
    public List<ITurnDirection> getTurnDirections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorMoveMode getMotorMoveMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Motor Move Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorMoveMode mo : MotorMoveMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Motor Move Mode: " + mode);
    }

    @Override
    public List<IMotorMoveMode> getMotorMoveModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorStopMode getMotorStopMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Motor Stop Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( MotorStopMode mo : MotorStopMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Stop Move Mode: " + mode);
    }

    @Override
    public List<IMotorStopMode> getMotorStopModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMotorSide getMotorSide(String motorSide) {
        if ( motorSide == null || motorSide.isEmpty() ) {
            throw new DbcException("Invalid Motor Side: " + motorSide);
        }
        String sUpper = motorSide.trim().toUpperCase(Locale.GERMAN);
        for ( MotorSide mo : MotorSide.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Motor Side: " + motorSide);
    }

    @Override
    public List<IMotorStopMode> getMotorSides() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDriveDirection getDriveDirection(String driveDirection) {
        if ( driveDirection == null || driveDirection.isEmpty() ) {
            throw new DbcException("Invalid Drive Direction: " + driveDirection);
        }
        String sUpper = driveDirection.trim().toUpperCase(Locale.GERMAN);
        for ( DriveDirection sp : DriveDirection.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Drive Direction: " + driveDirection);
    }

    @Override
    public List<IDriveDirection> getDriveDirections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractModule getGuiceModule() {
        return null;
    }

    protected void addBlockTypesFromProperties(Properties properties) {
        for ( Entry<Object, Object> property : properties.entrySet() ) {
            String propertyKey = (String) property.getKey();
            if ( propertyKey.startsWith("blockType.") ) {
                String name = propertyKey.substring(10);
                String propertyValue = (String) property.getValue();
                String[] attributes = propertyValue.split(",");
                Assert.isTrue(attributes.length >= 3, "Invalid block type property with key: %s", propertyKey);
                String astClassName = attributes[1];
                Category category = Category.valueOf(attributes[0]); // does the category exist?
                Assert.notNull(category);
                Class<?> astClass;
                try {
                    astClass = AbstractRobotFactory.class.getClassLoader().loadClass(astClassName); // does the class exist?
                } catch ( Exception e ) {
                    LOG.error("AstClass \"{}\" of block type with key \"{}\" could not be loaded", astClassName, propertyKey);
                    throw new DbcException("Class not found", e);
                }
                String[] blocklyNames = Arrays.copyOfRange(attributes, 2, attributes.length);
                BlockTypeContainer.add(name, category, astClass, blocklyNames);
            }
        }
    }
}
