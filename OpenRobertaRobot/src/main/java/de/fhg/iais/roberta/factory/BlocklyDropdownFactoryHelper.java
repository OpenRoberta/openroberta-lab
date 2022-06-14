package de.fhg.iais.roberta.factory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.bean.WaitUntilSensorBean;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class BlocklyDropdownFactoryHelper {

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

    public static Map<String, String> getColors(JSONObject robotDescription) {
        Map<String, String> colorDefs = new HashMap<>();
        JSONObject colors = robotDescription.optJSONObject("color");
        if ( colors != null ) {
            for ( String colourName : colors.keySet() ) {
                String rgbValue = colors.getString(colourName);
                colorDefs.put(rgbValue, colourName);
            }
        }
        return colorDefs;
    }

    // TODO probably should be a set, the modes are mapped to themselves
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

    public static Map<String, String> getConfigurationComponents(JSONObject robotDescription) {
        Map<String, String> r = new HashMap<>();
        JSONObject configurations = robotDescription.getJSONObject("configuration");
        for ( String key : configurations.keySet() ) {
            JSONArray value = configurations.getJSONArray(key);
            for ( int i = 0; i < value.length(); i++ ) {
                String old = r.put(value.getString(i), key);
                Assert.isNull(old, "Duplicate binding for %s", old);
            }
        }
        return r;
    }
}