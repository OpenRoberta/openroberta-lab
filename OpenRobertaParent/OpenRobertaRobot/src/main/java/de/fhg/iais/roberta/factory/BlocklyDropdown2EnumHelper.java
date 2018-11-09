package de.fhg.iais.roberta.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.DropDown;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class BlocklyDropdown2EnumHelper {

    public static <E extends IMode> E getModeValue(String modeName, Class<E> modes) {
        if ( modeName == null ) {
            throw new DbcException("Invalid " + modes.getName() + ": " + modeName);
        }
        final String sUpper = modeName.trim().toUpperCase(Locale.GERMAN);
        for ( final E mode : modes.getEnumConstants() ) {
            if ( mode.toString().equals(sUpper) ) {
                return mode;
            }
            for ( final String value : mode.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return mode;
                }
            }
        }
        throw new DbcException("Invalid " + modes.getName() + ": " + modeName);
    }

    public static Set<String> getSensorPortsFromProperties(Properties robotProperties) {
        String sensors = robotProperties.getProperty("robot.hw.sensors");
        Assert.notNull(sensors, "Undefined property robot.hw.sensors");
        String[] sensorPorts = sensors.split("\\s*,\\s*");
        return new HashSet<>(Arrays.asList(sensorPorts));
    }

    public static Set<String> getActorPortsFromProperties(Properties robotProperties) {
        String actors = robotProperties.getProperty("robot.hw.actors");
        Assert.notNull(actors, "Undefined property robot.hw.actors");
        String[] actorPorts = actors.split("\\s*,\\s*");
        return new HashSet<>(Arrays.asList(actorPorts));
    }

    public static Map<String, String> getSlotFromProperties(Properties properties) {
        Map<String, String> map = new HashMap<>();
        String filter = "robot.port.slot.";
        for ( Entry<Object, Object> e : properties.entrySet() ) {
            String key = (String) e.getKey();
            if ( key.startsWith(filter) ) {
                String value = (String) e.getValue();
                key = key.substring(filter.length());
                map.put(key, value);
            }
        }
        return map;
    }

    public static DropDown getColors(JSONObject robotDescription) {
        DropDown dropdownItem = new DropDown();
        JSONObject colors = robotDescription.getJSONObject("color");
        for ( String key : colors.keySet() ) {
            String value = colors.getString(key);
            dropdownItem.add(key, value);
        }
        return dropdownItem;
    }

    public static Map<String, String> getModes(JSONObject robotDescription) {
        Map<String, String> map = new HashMap<>();
        JSONObject modes = robotDescription.getJSONObject("mode");
        for ( String key : modes.keySet() ) {
            JSONArray v = modes.getJSONArray(key);
            for ( int i = 0; i < v.length(); i++ ) {
                String mode = v.getString(i);
                map.put(mode, mode);
            }
        }
        return map;
    }

    public static Map<String, WaitUntilSensorBean> getWaitUntils(JSONObject robotDescription) {
        Map<String, WaitUntilSensorBean> map = new HashMap<>();
        JSONObject waitUntils = robotDescription.getJSONObject("wait");
        for ( String key : waitUntils.keySet() ) {
            JSONObject v = waitUntils.getJSONObject(key);
            map.put(key, new WaitUntilSensorBean(v.getString("implementor"), v.getString("sensor"), v.getString("mode")));
        }
        return map;
    }

    public static DropDown getConfigurationComponents(JSONObject robotDescription) {
        DropDown dropdownItems = new DropDown();
        JSONObject configurations = robotDescription.getJSONObject("configuration");
        for ( String key : configurations.keySet() ) {
            String value = configurations.getString(key);
            dropdownItems.add(key, value);
        }
        return dropdownItems;
    }
}