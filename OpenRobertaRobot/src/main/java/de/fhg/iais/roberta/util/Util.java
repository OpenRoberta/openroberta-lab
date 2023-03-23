package de.fhg.iais.roberta.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String PROPERTY_DEFAULT_PATH = "classpath:/openRoberta.properties";
    // see: https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
    private static final Pattern VALID_EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private static final Pattern PROGRAM_NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z_$0-9]*$");
    private static final Pattern CONFIG_NAME_PATTERN = Pattern.compile("^$|^[a-zA-Z][a-zA-Z_$0-9]*$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^(-?\\d+((.|e-|e\\+)?\\d*|_?\\d*[a-zA-Z]{0,3})?|(0x|0X)[0-9a-fA-F]*)$");
    private static final Pattern FILENAME_PATTERN = Pattern.compile("^[\\w-]+.[A-Za-z]{1,6}$");
    private static final Pattern PORT_NAME_PATTERN = Pattern.compile("^\\w+$");
    private static final String INVALID = "invalid";
    /**
     * YAML parser. NOT thread-safe!
     */
    private static final Yaml YAML;
    private static final String[] reservedWords = new String[] {
        //  @formatter:off
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
        "throw", "throws", "transient", "true", "try", "void", "volatile", "while"
        //  @formatter:on
    };
    private static final AtomicInteger errorTicketNumber = new AtomicInteger(0);
    static {
        LoaderOptions lo = new LoaderOptions();
        lo.setAllowDuplicateKeys(false);
        YAML = new Yaml(lo);
    }

    private Util() {
        // no objects
    }

    /**
     * Save generated program to the temp filesystem
     *
     * @param tempDirectory TODO
     * @param generatedSourceCode
     * @param token
     * @param programName
     * @param ext
     */
    public static void storeGeneratedProgram(String tempDirectory, String generatedSourceCode, String token, String programName, String ext) {
        try {
            File sourceFile = new File(tempDirectory + token + "/" + programName + "/source/" + programName + ext);
            Path path = Paths.get(tempDirectory + token + "/" + programName + "/target/");
            try {
                Files.createDirectories(path);
                FileUtils.writeStringToFile(sourceFile, generatedSourceCode, StandardCharsets.UTF_8.displayName());
            } catch ( IOException e ) {
                String msg = "could not write source code to file system";
                LOG.error(msg, e);
                throw new DbcException(msg, e);
            }
            LOG.info("stored under: " + sourceFile.getPath());
        } catch ( Exception e ) {
            LOG.error("Storing the generated program " + programName + " into directory " + token + " failed", e);
        }
    }

    /**
     * configure a plugin by reading the plugin properties and creating a factory used later for code generation and cross compilation
     *
     * @param robotName the name of the plugin. Used to access the plugin's properties. Never null.
     * @param resourceDir base directory from which the resources needed by the cross compiler are accessed, never null.
     * @param tempDir directory to store generated source programs and the binaries generated by the cross compilers
     * @param pluginDefines modifications of the plugin's properties as a list of "<pluginName>:<key>=<value>"
     * @return the factory for this plugin
     */
    public static RobotFactory configureRobotPlugin(String robotName, String resourceDir, String tempDir, List<String> pluginDefines) {
        Properties basicPluginProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
        if ( basicPluginProperties == null ) {
            throw new DbcException("robot plugin " + robotName + " has no property file " + robotName + ".properties -  Server does NOT start");
        }
        String robotNameColon = robotName + ":";
        if ( pluginDefines != null ) {
            for ( String pluginDefine : pluginDefines ) {
                if ( pluginDefine.startsWith(robotNameColon) ) {
                    String define = pluginDefine.substring(robotNameColon.length());
                    String[] property = define.split("\\s*=\\s*");
                    if ( property.length == 2 ) {
                        LOG.info("new plugin property from command line: " + pluginDefine);
                        basicPluginProperties.put(property[0], property[1]);
                    } else {
                        LOG.info("command line plugin property is invalid and thus ignored: " + pluginDefine);
                    }
                }
            }
        }
        try {
            PluginProperties pluginProperties = new PluginProperties(robotName, resourceDir, tempDir, basicPluginProperties);
            return new RobotFactory(pluginProperties);
        } catch ( Exception e ) {
            throw new DbcException(
                " factory for robot plugin "
                    + robotName
                    + " could not be build. Plugin-jar not on the classpath? Invalid properties? Problems with validators? Server does NOT start",
                e);
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
        Properties serverProperties = loadProperties(propertyURI);
        if ( defines != null ) {
            for ( String define : defines ) {
                String[] property = define.split("\\s*=\\s*");
                if ( property.length == 2 ) {
                    LOG.info("new server property from command line: " + define);
                    serverProperties.put(property[0], property[1]);
                } else {
                    LOG.info("command line server property is invalid and thus ignored: " + define);
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
    static InputStream getInputStream(boolean doLogging, String propertyURI) {
        try {
            if ( propertyURI.startsWith("file:") ) {
                String filesystemPathName = propertyURI.substring(5);
                if ( doLogging ) {
                    Util.LOG.info("Operating on the file system. Using the path: " + filesystemPathName);
                }
                return new FileInputStream(filesystemPathName);
            } else if ( propertyURI.startsWith("classpath:") ) {
                String classPathName = propertyURI.substring(10);
                if ( doLogging ) {
                    Util.LOG.info("Operating on the classpath. Using the resource: " + classPathName);
                }
                InputStream resourceAsStream = Util.class.getResourceAsStream(classPathName);
                if ( resourceAsStream == null ) {
                    throw new DbcException("Could not open input stream for URI: " + propertyURI);
                }
                return resourceAsStream;
            } else {
                throw new DbcException("Could not open input stream for URI, missing file:/ or classpath:/ before the resource name? " + propertyURI);
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
    private static Properties loadProperties(boolean doLogging, String propertyURI) {
        Properties properties = new Properties();
        propertyURI = propertyURI == null || propertyURI.trim().equals("") ? Util.PROPERTY_DEFAULT_PATH : propertyURI;
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
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
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

    /**
     * Check whether a String is a valid Java identifier. It is checked, that no reserved word is used
     *
     * @param s String to check
     * @return <code>true</code> if the given String is a valid Java identifier; <code>false</code> otherwise.
     */
    public final static boolean isValidEmailAddress(String s) {
        return s != null && VALID_EMAIL.matcher(s).matches();
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
        return Arrays.binarySearch(Util.reservedWords, s) < 0;
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
        return "E-" + Util.errorTicketNumber.incrementAndGet();
    }

    /**
     * read all lines from a resource, concatenate them to a string separated by a newline
     *
     * @param resourceName
     * @return the content of the resource as one String
     */
    public static String readResourceContent(String resourceName) {
        final Class<?> clazz = Util.class;
        final String lineSeparator = System.lineSeparator();
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(resourceName), StandardCharsets.UTF_8))) {
            String line;
            while ( (line = in.readLine()) != null ) {
                sb.append(line).append(lineSeparator);
            }
            return sb.toString();
        } catch ( IOException e ) {
            throw new DbcException("reading resource failed for: " + resourceName, e);
        }
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
     * @param override whether the specific files should override content of the upstream files
     * @throws DbcException if an inconsistency is detected
     */
    public static void loadYAMLRecursive(String prefixForDebug, JSONObject accumulator, String uri, boolean override) {
        Assert.notNull(accumulator, "accumulating JSONObject must not be null");
        Assert.nonEmptyString(uri, "URI is null or empty at %s", prefixForDebug);
        InputStream in = getInputStream(false, uri);
        try {
            Map<?, ?> map = YAML.load(in);
            JSONObject toAdd = new JSONObject(map);
            Object includeObject = toAdd.remove("include");
            Util.mergeJsonIntoFirst(prefixForDebug, accumulator, toAdd, override);
            if ( includeObject != null ) {
                if ( includeObject instanceof String ) {
                    String include = (String) includeObject;
                    loadYAMLRecursive(prefixForDebug + " > " + uri, accumulator, include, override);
                } else if ( includeObject instanceof JSONArray ) {
                    JSONArray includes = (JSONArray) includeObject;
                    int length = includes.length();
                    for ( int i = 0; i < length; i++ ) {
                        loadYAMLRecursive(prefixForDebug + " > " + uri, accumulator, includes.getString(i), override);
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
     * @throws DbcException if the content is syntactically incorrect or duplicate keys are found
     */
    public static JSONObject loadYAML(String uri) {
        Assert.nonEmptyString(uri, "URI is null or empty");
        InputStream in = getInputStream(false, uri);
        try {
            Map<?, ?> map = YAML.load(in);
            return new JSONObject(map);
        } catch ( Exception e ) {
            throw new DbcException("invalid YAML file " + uri, e);
        }
    }

    /**
     * merge the properties of the JSONObject 'toAdd' RECURSIVELY to the JSONObject 'accumulator'. The keys of the leaves of the JSONObjects have to be disjunct
     * when ignoreExisting is false, otherwise an 'DbcException' is thrown. If the ignoreExisting parameter is true the process wont fail and the older values
     * are kept.
     *
     * @param prefixForDebug the path from the root of the JSONObjects to be merged. For debugging of invalid JSONObjects.
     * @param accumulator the JSONObject into which 'toAdd' should be merged
     * @param toAdd the JSONObject which has to be merged into 'accumulator'
     * @param ignoreExisting whether the 'toAdd' objects that already exist in 'accumulator' should be ignored
     */
    static void mergeJsonIntoFirst(String prefixForDebug, JSONObject accumulator, JSONObject toAdd, boolean ignoreExisting) {
        for ( String k2 : toAdd.keySet() ) {
            Object v1 = accumulator.opt(k2);
            Object v2 = toAdd.get(k2);
            if ( v1 == null ) {
                accumulator.put(k2, v2);
            } else {
                if ( v1 instanceof JSONObject && v2 instanceof JSONObject ) {
                    mergeJsonIntoFirst(prefixForDebug + "." + k2, (JSONObject) v1, (JSONObject) v2, ignoreExisting);
                } else if ( ignoreExisting ) {
                    // ignore
                } else {
                    throw new DbcException("could not merge JSON objects with prefix " + prefixForDebug + "." + k2);
                }
            }
        }
    }

    /**
     * create a map with Strings as keys and values
     *
     * @param args key-value pairs (thus: the length MUST be even)
     * @return the map created
     */
    public static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    /**
     * run a crosscompiler in a process of its own, store the compiler response in field crosscompilerResponse
     *
     * @param executableWithParameters
     * @param crosscompilerSourceForDebuggingOnly for logging if the crosscompiler fails. Allows debugging of erros in the code generators
     * @return true, when the crosscompiler succeeds; false, otherwise
     */
    public static Pair<Boolean, String> runCrossCompiler(String[] executableWithParameters, String crosscompilerSourceForDebuggingOnly, boolean isNativeEditorCode) {
        int ecode = -1;
        String crosscompilerResponse;
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(executableWithParameters);
            procBuilder.redirectErrorStream(true);
            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.PIPE);
            Process p = procBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            crosscompilerResponse = sj.toString();
            ecode = p.waitFor();
            p.destroy();
            if ( ecode != 0 ) {
                Util.logCrosscompilerError(LOG, crosscompilerResponse, crosscompilerSourceForDebuggingOnly, isNativeEditorCode);
            }
            return Pair.of(ecode == 0, crosscompilerResponse);
        } catch ( Exception e ) {
            crosscompilerResponse = "exception when calling the cross compiler";
            LOG.error(crosscompilerResponse, e);
            return Pair.of(false, crosscompilerResponse);
        }
    }

    /**
     * log that the cross compiler returned with an error, but only if the source is not from the source code editor
     *
     * @param reporterLogger the logger for the class, to which the error was returned
     * @param crosscompilerResponse the response of the crosscompiler
     * @param crosscompilerSourceForDebuggingOnly the program, that produced the error
     * @param isNativeEditorCode flag to distinguish error source. True: Source code editor, False: NEPO generated
     */
    public static void logCrosscompilerError(
        Logger reporterLogger,
        String crosscompilerResponse,
        String crosscompilerSourceForDebuggingOnly,
        boolean isNativeEditorCode) //
    {
        if ( !isNativeEditorCode ) {
            reporterLogger.error("crosscompilation of NEPO generated program failed. Messages are logged to logger 'crosscompiler_error'");
            StringBuilder sb = new StringBuilder();
            sb.append("\n***** cross compilation failed with response:\n").append(crosscompilerResponse);
            sb.append("\n***** for program:\n").append(crosscompilerSourceForDebuggingOnly).append("\n*****");
            LoggerFactory.getLogger("crosscompiler_error").info(sb.toString());
        }
    }

    /**
     * transform the binary to a <b>base64 encoded hexadecimal</b> string
     *
     * @param path path to the file, which contains the binary generated by a crosscompiler
     * @param optPrefix prefix to be prepended to the binary generated by a crosscompiler
     * @return the encoded binary
     */
    public static final String getBase64EncodedHex(String path, String optPrefix) {
        try {
            String compiledHex = FileUtils.readFileToString(new File(path), "UTF-8");
            final Base64.Encoder urec = Base64.getEncoder();
            compiledHex = optPrefix + compiledHex;
            compiledHex = urec.encodeToString(compiledHex.getBytes());
            return compiledHex;
        } catch ( IOException e ) {
            LOG.error("Exception when reading the compiled code from " + path, e);
            return null;
        }
    }

    /**
     * transform the binary to a <b>base64 encoded</b> string
     *
     * @param path path to the file, which contains the binary generated by a crosscompiler
     * @return the encoded binary
     */
    public static String getBase64EncodedBinary(String path) {
        try {
            byte[] compiledBin = FileUtils.readFileToByteArray(new File(path));
            final Base64.Encoder urec = Base64.getEncoder();
            String compiledString = urec.encodeToString(compiledBin);
            return compiledString;
        } catch ( IOException e ) {
            LOG.error("Exception when reading the compiled code from " + path, e);
            return null;
        }
    }

    /**
     * Compares two version strings.
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive integer if str1 is _numerically_ greater than
     *     str2. The result is zero if the strings are _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;

        while ( i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]) ) {
            i++;
        }

        if ( i < vals1.length && i < vals2.length ) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }

    /**
     * Helper method to get the absolute path for relevant OS being used.
     * Expects a UNIX style path delimited by "/" as its input.
     *
     * @param pathToBeResolved the UNIX path that needs to be resolved.
     * @return the OS specific absolute path.
     * @throws AssertionError when called with empty path.
     */
    public static String getOsSpecificAbsolutePath(String pathToBeResolved) {
        Assert.nonEmptyString(pathToBeResolved, "Path cannot be empty!");
        Path path = pathToBeResolved.startsWith("/") ? Paths.get("/") : Paths.get("");
        for ( String dir : pathToBeResolved.split("/") ) {
            path = path.resolve(dir);
        }
        return path.toAbsolutePath().normalize().toString();
    }

    /**
     * Helper method to sanitize the configuration component properties.
     * Invalid property values will be replaced with the value "invalid".
     *
     * @param componentProperties
     */
    public static void sanitizeConfigurationProperties(Map<String, String> componentProperties) {
        for ( Map.Entry<String, String> pair : componentProperties.entrySet() ) {
            String key = pair.getKey();
            String value = pair.getValue();
            boolean isValid = key.equals("NAO_FILENAME") ? FILENAME_PATTERN.matcher(value).matches() : CONFIG_NAME_PATTERN.matcher(value).matches() || NUMBER_PATTERN.matcher(value).matches();
            if ( !isValid ) {
                try {
                    pair.setValue(INVALID);
                } catch ( UnsupportedOperationException uoe ) {
                    LOG.error("SingeltonMap is immutable");
                }
            }
        }
    }

    /**
     * Helper method to sanitize the configuration component port name set by the user
     *
     * @param internalPortName
     * @return the original value or "invalid" if the value does not match a pattern.
     */
    public static String sanitizeConfigurationInternalPortName(String internalPortName) {
        boolean isValid = internalPortName == null || PORT_NAME_PATTERN.matcher(internalPortName).matches();
        return isValid ? internalPortName : INVALID;
    }

    /**
     * Helper method to sanitize the program block name set by the user
     *
     * @param value
     * @return the original value or if NUM_CONST, "0", otherwise "invalid" if the value does not match a pattern.
     */
    public static String sanitizeProgramProperty(String value, String blockName) {
        boolean isValid = PROGRAM_NAME_PATTERN.matcher(value).matches() || NUMBER_PATTERN.matcher(value).matches();
        if ( !isValid ) {
            value = blockName.equals("NUM_CONST") ? "0" : INVALID;
        }
        return value;
    }

    public static String sanitizeProgramProperty(String value) {
        return sanitizeProgramProperty(value, "");
    }
}
