package de.fhg.iais.roberta.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Util1 {
    private static final Logger LOG = LoggerFactory.getLogger(Util1.class);
    private static final String PROPERTY_DEFAULT_PATH = "openRoberta.properties";

    /**
     * YAML parser. NOT thread-safe!
     */
    private static final Yaml YAML;

    static {
        LoaderOptions lo = new LoaderOptions();
        lo.setAllowDuplicateKeys(false);
        YAML = new Yaml(lo);
    }

    private static final String[] reservedWords = new String[] {
        //  @formatter:off
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
        "throw", "throws", "transient", "true", "try", "void", "volatile", "while"
        //  @formatter:on
    };

    private static final AtomicInteger errorTicketNumber = new AtomicInteger(0);

    private Util1() {
        // no objects
    }

    /**
     * load a YAML object from a URI <b>recursively</b>. If the YAML object contains a top-level key "include", as value(s) either a sequence of URI's or a
     * single URI is/are expected. This/these URI(s) are further YAML files, that should be loaded and merged with this one.<br>
     * The URI(s) given refer either to the file system or to the classpath.<br>
     * - If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * - If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.<br>
     * <b>Note:</b> do not mix "file:" and "classpath:". This could rise confusion.
     *
     * @param prefixForDebug the path from the root of the YAML file include-hierarchy. For debugging of invalid YAML files.
     * @param accumulator the JSONObject into which the content of all YAML files should be merged
     * @param uri URI of the YAML file. Never null
     * @throws DbcException if an inconsistency is detected
     */
    public static void loadYAMLRecursive(String prefixForDebug, JSONObject accumulator, String uri) {
        Assert.notNull(accumulator, "accumulating JSONObject must not be null");
        Assert.nonEmptyString(uri, "URI is null or empty at %s", prefixForDebug);
        InputStream in = getInputStream(true, uri);
        try {
            Map<?, ?> map = (Map<?, ?>) YAML.load(in);
            JSONObject toAdd = new JSONObject(map);
            Object includeObject = toAdd.remove("include");
            Util1.mergeJsonIntoFirst(prefixForDebug, accumulator, toAdd);
            if ( includeObject != null ) {
                if ( includeObject instanceof String ) {
                    String include = (String) includeObject;
                    loadYAMLRecursive(prefixForDebug + " > " + uri, accumulator, include);
                } else if ( includeObject instanceof JSONArray ) {
                    JSONArray includes = (JSONArray) includeObject;
                    int length = includes.length();
                    for ( int i = 0; i < length; i++ ) {
                        loadYAMLRecursive(prefixForDebug + " > " + uri, accumulator, includes.getString(i));
                    }
                }
            }
        } catch ( Exception e ) {
            throw new DbcException("invalid YAML file " + uri + " at " + prefixForDebug, e);
        }
    }

    /**
     * load a YAML object from a URI. The URI refers either to the file system or to the classpath.<br>
     * - If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * - If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.
     *
     * @param uri URI of the YAML file. Never null
     * @return the YAML file as JSONObject. Never null, may be empty
     * @throws DbcException if the content is syntacically incorrect or duplicate keys are found
     */
    public static JSONObject loadYAML(String uri) {
        Assert.nonEmptyString(uri, "URI is null or empty");
        InputStream in = getInputStream(true, uri);
        try {
            Map<?, ?> map = (Map<?, ?>) YAML.load(in);
            return new JSONObject(map);
        } catch ( Exception e ) {
            throw new DbcException("invalid YAML file " + uri, e);
        }
    }

    /**
     * load the OpenRoberta properties. The URI of the properties refers either to the file system or to the classpath. It is used in both production and test.
     * <br>
     * <b>This methods loads the properties. It does NOT store them. See class {@link ServerProperties}</b> <br>
     * If the URI-parameter is null, the classpath is searched for the default resource "openRoberta.properties".<br>
     * If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.
     *
     * @param propertyURI URI of the property file. May be null
     * @return the properties. Never null, may be empty
     */
    public static Properties loadProperties(String propertyURI) {
        return loadProperties(false, propertyURI);
    }

    /**
     * load the OpenRoberta properties. The URI of the properties refers either to the file system or to the classpath. It is used in both production and test.
     * <br>
     * <b>This methods loads the properties. It does NOT store them. See class {@link ServerProperties}</b> <br>
     * If the URI-parameter is null, the classpath is searched for the default resource "openRoberta.properties".<br>
     * If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.
     *
     * @param propertyURI URI of the property file. May be null
     * @return the properties. Never null, maybe empty
     */
    public static Properties loadAndMergeProperties(String propertyURI, List<String> defines) {
        Properties serverProperties = loadProperties(false, propertyURI);
        if ( defines != null ) {
            for ( String define : defines ) {
                String[] property = define.split("\\s*=\\s*");
                if ( property.length == 2 ) {
                    LOG.info("new property from command line: " + define);
                    serverProperties.put(property[0], property[1]);
                } else {
                    LOG.info("command line property is invalid and thus ignored: " + define);
                }
            }
        }
        return serverProperties;
    }

    /**
     * get an input stream from a URI. The URI refers either to the file system or to the classpath. It is used in both production and test. <br>
     * <b>This methods creates an input stream, that must be closed by the caller</b><br>
     * - If the URI-parameter is null, the classpath is searched for the default resource "openRoberta.properties".<br>
     * - If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * - If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.
     *
     * @param doLogging if true: log debug info
     * @param propertyURI URI of the property file. May be null
     * @return input stream
     */
    public static InputStream getInputStream(boolean doLogging, String propertyURI) {
        try {
            if ( propertyURI == null || propertyURI.trim().equals("") ) {
                if ( doLogging ) {
                    Util1.LOG.info("default URI from classpath. Using: " + Util1.PROPERTY_DEFAULT_PATH);
                }
                return Util1.class.getClassLoader().getResourceAsStream(Util1.PROPERTY_DEFAULT_PATH);
            } else if ( propertyURI.startsWith("file:") ) {
                String filesystemPathName = propertyURI.substring(5);
                if ( doLogging ) {
                    Util1.LOG.info("Operating on the file system. Using the path: " + filesystemPathName);
                }
                return new FileInputStream(filesystemPathName);
            } else if ( propertyURI.startsWith("classpath:") ) {
                String classPathName = propertyURI.substring(10);
                if ( doLogging ) {
                    Util1.LOG.info("Operating on the classpath. Using the resource: " + classPathName);
                }
                return Util1.class.getClassLoader().getResourceAsStream(classPathName);
            } else {
                throw new DbcException("Could not open input stream for URI: " + propertyURI);
            }
        } catch ( Exception e ) {
            throw new DbcException("Could not open input stream of URI " + propertyURI + ". Inspect the stacktrace", e);
        }
    }

    /**
     * load the OpenRoberta properties. The URI of the properties refers either to the file system or to the classpath. It is used in both production and test.
     * <br>
     * <b>This methods loads the properties. It does NOT store them. See class {@link ServerProperties}</b> <br>
     * If the URI-parameter is null, the classpath is searched for the default resource "openRoberta.properties".<br>
     * If the URI-parameters start with "file:" the properties are loaded from the file system.<br>
     * If the URI-parameters start with "classpath:" the properties are loaded as a resource from the classpath.
     *
     * @param doLogging if true: log debug info
     * @param propertyURI URI of the property file. May be null
     * @return the properties. Never null, maybe empty
     */
    public static Properties loadProperties(boolean doLogging, String propertyURI) {
        Properties properties = new Properties();
        try {
            loadIncludes(properties, getInputStream(doLogging, propertyURI));
            properties.load(getInputStream(doLogging, propertyURI));
            return properties;
        } catch ( IOException e ) {
            throw new DbcException("Could not load properties of URI " + propertyURI + ". Inspect the stacktrace", e);
        }
    }

    private static void loadIncludes(Properties properties, InputStream inputStream) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("iso-8859-1")));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                if ( line.startsWith("#include ") ) {
                    line = line.substring(9);
                    Properties local = loadProperties(line);
                    properties.putAll(local);
                }
            }
        } finally {
            if ( reader != null ) {
                reader.close();
            }
        }
    }

    private static void loadOwn(Properties properties, InputStream inputStream) throws IOException {
        try {
            Properties local = new Properties();
            local.load(inputStream);
            properties.putAll(local);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Check whether a String is a valid Java identifier. It is checked, that no reserved word is used
     *
     * @param s String to check
     * @return <code>true</code> if the given String is a valid Java identifier; <code>false</code> otherwise.
     */
    public final static boolean isValidJavaIdentifier(String s) {
        if ( s == null || s.length() == 0 ) {
            return false;
        }
        CharacterIterator citer = new StringCharacterIterator(s);
        // first
        char c = citer.first();
        if ( c == CharacterIterator.DONE ) {
            return false;
        }
        if ( !Character.isJavaIdentifierStart(c) && !Character.isIdentifierIgnorable(c) ) {
            return false;
        }
        // remainder
        c = citer.next();
        while ( c != CharacterIterator.DONE ) {
            if ( !Character.isJavaIdentifierPart(c) && !Character.isIdentifierIgnorable(c) ) {
                return false;
            }
            c = citer.next();
        }
        return Arrays.binarySearch(Util1.reservedWords, s) < 0;
    }

    /**
     * get the actual date as timestamp
     *
     * @return the actual date as timestamp
     */
    public static Timestamp getNow() {
        return new Timestamp(new Date().getTime());
    }

    public static String getErrorTicketId() {
        return "E-" + Util1.errorTicketNumber.incrementAndGet();
    }

    public static String formatDouble1digit(double d) {
        return String.format(Locale.UK, "%.1f", d);
    }

    /**
     * compute 2 to the power of 'exp'.
     *
     * @param exp the exponent
     * @return 2 to the power of 'exp' if 'exp' >= 0; 0 otherwise
     */
    public static int pow2(int exp) {
        if ( exp < 0 ) {
            return 0;
        } else {
            return 1 << exp;
        }
    }

    /**
     * read all lines from a resource
     *
     * @param resourceName
     * @return the list of lines of the resource
     */
    public static List<String> readResourceLines(String resourceName) {
        try {
            return Files.readAllLines(Paths.get(Util1.class.getResource(resourceName).toURI()));
        } catch ( IOException | URISyntaxException e ) {
            throw new DbcException("read from resource failed for: " + resourceName, e);
        }
    }

    /**
     * read all lines from a resource, concatenate them to a string separated by a newline
     *
     * @param resourceName
     * @return the content of the resource as one String
     */
    public static String readResourceContent(String resourceName) {
        return readResourceLines(resourceName).stream().collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * read all lines from a file
     *
     * @param fileName of the file to be read
     * @return the list of lines of the file
     */
    public static List<String> readFileLines(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch ( IOException e ) {
            throw new DbcException("read from file failed for: " + fileName, e);
        }
    }

    /**
     * write String into a file
     *
     * @param fileName of the file to be written
     * @param content of the file. UTF-8 is enforced
     */
    public static void writeFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes(Charset.forName("UTF-8")));
        } catch ( IOException e ) {
            throw new DbcException("write to file failed for: " + fileName, e);
        }
    }

    /**
     * read all lines from a file, concatenate them to a string separated by a newline
     *
     * @param fileName
     * @return the content of the resource as one String
     */
    public static String readFileContent(String fileName) {
        return readFileLines(fileName).stream().collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * return a stream of all files found in a file directory
     *
     * @param directory whose files are requested
     * @return the stream of all files
     */
    public static Stream<String> fileStreamOfFileDirectory(String directory) {
        return Arrays.stream(new File(directory).list());
    }

    /**
     * return a stream of all files found in a resource directory
     *
     * @param directory whose files are requested
     * @return the stream of all files
     */
    public static Stream<String> fileStreamOfResourceDirectory(String directory) {
        try {
            return Arrays.stream(Paths.get(Util1.class.getResource(directory).toURI()).toFile().list());
        } catch ( URISyntaxException e ) {
            throw new DbcException("getting a file stream from a resource directory failed for: " + directory, e);
        }
    }

    /**
     * merge the properties of the JSONObject 'toAdd' RECURSIVELY to the JSONObject 'accumulator'. The keys of the leaves of the JSONObjects have to be
     * disjunct, otherwise an 'DbcException' is thrown.
     *
     * @param prefixForDebug the path from the root of the JSONObjects to be merged. For debugging of invalid JSONObjects.
     * @param accumulator the JSONObject into which 'toAdd' should be merged
     * @param toAdd the JSONObject which has to be merged into 'accumulator'
     */
    public static void mergeJsonIntoFirst(String prefixForDebug, JSONObject accumulator, JSONObject toAdd) {
        for ( String k2 : toAdd.keySet() ) {
            Object v1 = accumulator.opt(k2);
            Object v2 = toAdd.get(k2);
            if ( v1 == null ) {
                accumulator.put(k2, v2);
            } else {
                if ( v1 instanceof JSONObject && v2 instanceof JSONObject ) {
                    mergeJsonIntoFirst(prefixForDebug + "." + k2, (JSONObject) v1, (JSONObject) v2);
                } else {
                    throw new DbcException("could not merge JSON objects with prefix " + prefixForDebug + "." + k2);
                }
            }
        }
    }
}