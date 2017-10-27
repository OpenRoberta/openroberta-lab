package de.fhg.iais.roberta.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class Prop2Map {
    private static final Logger LOG = LoggerFactory.getLogger(Prop2Map.class);
    private static final boolean HEAVY_DEBUG = false;

    private static final Map<String, String> key2text = new HashMap<>();
    private static final Pattern KEY = Pattern.compile("^>>>>>> ([\\.\\w]+) <<<<<<\\s*$");
    private static final Pattern SEMI = Pattern.compile("^;$");

    private Prop2Map() {
        // no objects
    }

    public static void init(String resourceName) {
        fillKey2text(key2text, resourceName);
    }

    static void fillKey2text(Map<String, String> key2text, String resourceName) {
        List<String> keySequence = new ArrayList<>();
        String line = "<<no line>>";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(Prop2Map.class.getResourceAsStream(resourceName), "UTF-8"));
            String key = null;
            StringBuilder sb = new StringBuilder();
            while ( (line = br.readLine()) != null ) {
                line = line.trim();
                if ( line.length() == 0 || line.startsWith("#") || line.startsWith("--") || line.startsWith("//") ) {
                    // comment, ignore
                } else {
                    Matcher keyMatcher = KEY.matcher(line);
                    Matcher semiMatcher = SEMI.matcher(line);
                    if ( keyMatcher.matches() ) {
                        if ( sb.length() != 0 ) {
                            throw new DbcException("Invalid data in resource " + resourceName + " detected at line: \"" + line + "\"");
                        }
                        key = keyMatcher.group(1);
                    } else if ( semiMatcher.matches() ) {
                        if ( key == null ) {
                            throw new DbcException("Invalid key in resource " + resourceName + " detected: \"" + key + "\"");
                        }
                        String oldValue = key2text.put(key, sb.toString().trim());
                        if ( oldValue != null ) {
                            throw new DbcException("Duplicate key in resource " + resourceName + " detected: \"" + key + "\"");
                        }
                        if ( HEAVY_DEBUG ) {
                            keySequence.add(key);
                        }
                        sb = new StringBuilder();
                    } else {
                        if ( sb.length() > 0 ) {
                            sb.append("\n");
                        }
                        sb.append(line);
                    }
                }
            }
            if ( sb.length() != 0 ) {
                throw new DbcException("Last entry has no terminating ; in " + resourceName);
            }
        } catch ( NullPointerException e ) {
            throw new DbcException("Exception reading resource " + resourceName, e);
        } catch ( UnsupportedEncodingException e ) {
            throw new DbcException("Exception reading resource " + resourceName + " detected at line: \"" + line + "\"", e);
        } catch ( IOException e ) {
            throw new DbcException("Exception reading resource " + resourceName + " detected at line: \"" + line + "\"", e);
        } finally {
            if ( br != null ) {
                try {
                    br.close();
                } catch ( IOException e ) {
                    // OK;
                }
            }
        }
        LOG.info("Prop2Map found " + key2text.size() + " entries in resource " + resourceName);
        if ( HEAVY_DEBUG ) {
            logMap(key2text, keySequence);
        }
    }

    static void logMap(Map<String, String> key2text, List<String> keySequence) {
        int max = 0;
        for ( String key : keySequence ) {
            if ( max < key.length() ) {
                max = key.length();
            }
        }
        max += 1;
        String format = "    %-" + max + "s : %s";
        for ( String key : keySequence ) {
            String value = key2text.get(key).replaceAll("\\n", " ");
            LOG.info(String.format(format, key, value.substring(0, Math.min(value.length(), 60))));
        }
    }

    public static String get(String key) {
        if ( key == null || key.trim().equals("") ) {
            throw new DbcException("Null or empty key detected");
        }
        String value = key2text.get(key);
        if ( value == null || value.trim().equals("") ) {
            throw new DbcException("key not found: " + key);
        }
        return value;
    }

    public static Set<String> getKeys() {
        return Collections.unmodifiableSet(key2text.keySet());
    }
}
