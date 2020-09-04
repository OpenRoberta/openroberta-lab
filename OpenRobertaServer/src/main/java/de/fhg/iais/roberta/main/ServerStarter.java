package de.fhg.iais.roberta.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.guice.RobertaGuiceServletConfig;
import de.fhg.iais.roberta.javaServer.websocket.Ev3SensorLoggingWS;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.dbc.DbcException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * <b>the main class of the application, the main activity is starting the server.</b><br>
 * <br>
 * - starts an embedded jetty (see {@link ServletContainer}<br>
 * - configures jersey and the package with the resources<br>
 * - configures jaxb and the package with the providers<br>
 * - configures a resource holder for static content<br>
 * - configures hibernate and tests the connection to the database.<br>
 * <br>
 *
 * @author rbudde
 */
public class ServerStarter {
    private static Logger LOG; // assigned in main(...), consider it as final!
    private static final String LOG_CONFIGFILE = "server.log.configfile=";
    private static final String ADMIN_DIR_KEY = "server.admin.dir=";
    private static final String LOG_LEVEL_KEY = "server.log.level=";

    private static final long INTERVAL_HTTPSESSION_EXPIRE_SEC = TimeUnit.HOURS.toSeconds(3);
    private static final long INTERVAL_DB_SESSION_EXPIRE_SEC = TimeUnit.MINUTES.toSeconds(30);

    private final ServerProperties serverProperties; // for the startup
    private Injector injector;

    /**
     * startup and shutdown of the server. See {@link ServerStarter}.
     *
     * @param args a sequence of -d key=value and -D plugin:key=value for server resp. plugin property modifications
     */
    public static void main(String[] args) throws Exception {
        initLoggingBeforeFirstUse(args);

        OptionParser parser = new OptionParser();
        OptionSpec<String> serverDefineOpt = parser.acceptsAll(asList("d", "server-property")).withRequiredArg().ofType(String.class);
        OptionSpec<String> pluginDefineOpt = parser.acceptsAll(asList("D", "plugin-property")).withRequiredArg().ofType(String.class);
        OptionSet options = parser.parse(args);
        List<String> serverDefines = serverDefineOpt.values(options);
        List<String> pluginDefines = pluginDefineOpt.values(options);

        final ServerStarter serverStarter = new ServerStarter(null, serverDefines);
        try {
            Server server = serverStarter.start(pluginDefines);
            Statistics.info("ServerStart", "success", true);
            server.join();
            System.exit(0);
        } catch ( Exception e ) {
            Statistics.info("ServerStart", "success", false);
            LOG.error("Exception during server startup. Server NOT started", e);
            System.exit(12);
        }
    }

    /**
     * create the starter. Load the properties.
     *
     * @param propertyPath optional URI to properties resource. May be null.
     * @param serverDefines is a list of properties (from the command line ...) which overwrite the properties from the propertyPath. May be null.
     */
    public ServerStarter(String propertyPath, List<String> serverDefines) {
        Properties properties = Util.loadAndMergeProperties(propertyPath, serverDefines);
        setupPropertyForDatabaseConnection(properties);
        this.serverProperties = new ServerProperties(properties);
        UtilForREST.setServerVersion(this.serverProperties.getStringProperty("openRobertaServer.version"));
    }

    /**
     * startup. See {@link ServerStarter}. If the server could not be created, <b>the process will be terminated by System.exit(status) with status > 0</b>.
     *
     * @param pluginDefines
     * @return the server
     */
    public Server start(List<String> pluginDefines) throws IOException {
        String host = this.serverProperties.getStringProperty("server.ip");
        int httpPort = this.serverProperties.getIntProperty("server.port", 0);
        int httpsPort = this.serverProperties.getIntProperty("server.portHttps", 0);

        Server server = new Server();
        List<ServerConnector> connectors = new ArrayList<>();

        if ( httpPort > 0 ) {
            ServerConnector httpConnector = new ServerConnector(server); //NOSONAR : no need to close. Active until program termination
            httpConnector.setHost(host);
            httpConnector.setPort(httpPort);
            connectors.add(httpConnector);
        }
        if ( httpsPort > 0 ) {
            SslContextFactory sslContextFactory = new SslContextFactory(); //NOSONAR : no need to close. Active until program termination
            String keyStoreUri = this.serverProperties.getStringProperty("server.keystore.uri");
            if ( keyStoreUri == null ) {
                keyStoreUri = ServerStarter.class.getResource("/keystore.jks").toExternalForm();
            }
            String password = this.serverProperties.getStringProperty("server.keystore.password");
            sslContextFactory.setKeyStorePath(keyStoreUri);
            sslContextFactory.setKeyStorePassword(password);
            sslContextFactory.setKeyManagerPassword(password);
            ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory());
            sslConnector.setHost(host);
            sslConnector.setPort(httpsPort);
            connectors.add(sslConnector);
        }
        server.setConnectors(connectors.toArray(new ServerConnector[0]));

        // configure robot plugins
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        Map<String, IRobotFactory> robotPluginMap = configureRobotPlugins(robotCommunicator, this.serverProperties, pluginDefines);

        // setup services and threads to run the services
        IIpToCountry ipToCountry = configureIpToCountryDb();
        configureHttpSessionStateCleanup();
        configureDbSessionCleanup();

        RobertaGuiceServletConfig robertaGuiceServletConfig =
            new RobertaGuiceServletConfig(this.serverProperties, robotPluginMap, robotCommunicator, ipToCountry);

        // 1. REST API with /rest prefix
        ServletContextHandler restHttpHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        restHttpHandler.setContextPath("/rest");
        restHttpHandler.setSessionHandler(new SessionHandler());
        restHttpHandler.addEventListener(robertaGuiceServletConfig);
        restHttpHandler.addFilter(GuiceFilter.class, "/*", null);
        restHttpHandler.addServlet(DefaultServlet.class, "/*");

        // 2. websockets with /ws/<version>/ prefix
        ServletContextHandler wsHandler = new ServletContextHandler();
        wsHandler.setContextPath("/ws");
        wsHandler.addServlet(WebSocketServiceServlet.class, "/*");

        // 3. static resources
        ServletContextHandler defaultHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        defaultHandler.setContextPath("/*");
        defaultHandler.setSessionHandler(new SessionHandler());
        // see /rest endpoint, this will cover this: restHttpHandler.getSessionHandler().addEventListener(mkSessionListener(" for / static resources, no REST endpoint"));

        // 3.1 REST API without prefix (deprecated, used by very old ev3 robots. Initializes the guice injector and implicitly the db session wrapper a SECOND TIME)
        defaultHandler.addEventListener(robertaGuiceServletConfig);
        defaultHandler.addFilter(GuiceFilter.class, "/pushcmd/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/download/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/update/*", null);

        // 3.2 static resources
        ServletHolder staticResourceServlet = defaultHandler.addServlet(DefaultServlet.class, "/*");
        staticResourceServlet.setInitParameter("dirAllowed", "false");
        staticResourceServlet.setInitParameter("precompressed", "gzip=.gz");
        String dirNameStaticResources = this.serverProperties.getStringProperty("server.staticresources.dir");
        staticResourceServlet.setInitParameter("resourceBase", new File(dirNameStaticResources).toPath().toAbsolutePath().normalize().toUri().toASCIIString());
        staticResourceServlet.setInitParameter("cacheControl", "private, must-revalidate");

        HandlerList handlers = new HandlerList();
        handlers
            .setHandlers(
                new Handler[] {
                    restHttpHandler,
                    wsHandler,
                    defaultHandler
                });
        server.setHandler(handlers);

        StringBuilder sb = new StringBuilder();
        if ( httpPort > 0 ) {
            sb.append("http://").append(host).append(":").append(httpPort);
        }
        if ( httpPort > 0 && httpsPort > 0 ) {
            sb.append(" and ");
        }
        if ( httpsPort > 0 ) {
            sb.append("https://").append(host).append(":").append(httpsPort);
        }
        String serverMessage = sb.toString();

        try {
            server.start();
            ServerStarter.LOG.info("server started at " + serverMessage);
        } catch ( Exception e ) {
            ServerStarter.LOG.error("Could not start the server at " + serverMessage, e);
            System.exit(20);
        }
        this.injector = robertaGuiceServletConfig.getCreatedInjector();
        Ev3SensorLoggingWS.setGuiceInjector(this.injector);

        checkRobotPluginsDB(robotPluginMap.values());
        Runtime.getRuntime().addShutdownHook(new ShutdownHook("embedded".equals(this.serverProperties.getStringProperty("database.mode")), this.injector));
        LOG.info("Shutdown hook added. If the server is gracefully stopped in the future, a shutdown message is logged");
        logTheNumberOfStoredPrograms();

        return server;
    }

    /**
     * First initialize logging. This is tricky, because we have to avoid any call to a method, that expects logging initialized already. In this case, a
     * default initialization would occur. This is NOT wanted. The variables needed for logging are read from the args assuming a fixed layout (see
     * "openRoberta.properties" for details)
     *
     * @param args the command line arguments. Needed to extract three optional logging parameter given by -d KEY=VAL
     */
    public static void initLoggingBeforeFirstUse(String[] args) throws JoranException {
        String configFile = "/logback.xml";
        String adminDir = ".";
        String logLevel = "INFO";
        for ( String serverDefine : args ) {
            if ( serverDefine.startsWith(LOG_CONFIGFILE) ) {
                configFile = extractValue(serverDefine.substring(LOG_CONFIGFILE.length()));
            } else if ( serverDefine.startsWith(ADMIN_DIR_KEY) ) {
                adminDir = extractValue(serverDefine.substring(ADMIN_DIR_KEY.length()));
            } else if ( serverDefine.startsWith(LOG_LEVEL_KEY) ) {
                logLevel = extractValue(serverDefine.substring(LOG_LEVEL_KEY.length()));
            }
        }
        System.setProperty("ADMINISTRATION_DIR", adminDir);
        System.setProperty("LOG_LEVEL", logLevel);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        context.reset();
        jc.doConfigure(ServerStarter.class.getResource(configFile));
        LOG = LoggerFactory.getLogger(ServerStarter.class); // here the initialization occurs
        LOG.info("log config resource used: " + configFile);
        LOG.info("admin directory: " + adminDir);
        LOG.info("root logging level: " + logLevel);
    }

    /**
     * configure robot plugins, that may be used with this server. Uses the white list and the declarations from the openroberta.properties file.
     *
     * @param robotCommunicator
     * @param pluginDefines modifications of plugin properties as a list of "<pluginName>:<key>=<value>"
     * @return the mapping from robot names to the factory, that supplies all robot-specific data
     */
    public static Map<String, IRobotFactory> configureRobotPlugins(
        RobotCommunicator robotCommunicator,
        ServerProperties serverProperties,
        List<String> pluginDefines) {
        if ( robotCommunicator == null ) {
            throw new DbcException("the robot communicator object is missing - Server does NOT start");
        }
        List<String> robotWhitelist = serverProperties.getRobotWhitelist();
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        String resourceDir = serverProperties.getCrosscompilerResourceDir();
        String tempDir = serverProperties.getTempDir();
        for ( String robotName : robotWhitelist ) {
            if ( robotName.equals("sim") ) {
                continue;
            }
            IRobotFactory factory = Util.configureRobotPlugin(robotName, resourceDir, tempDir, pluginDefines);
            robotPlugins.put(robotName, factory);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ROBOT PLUGINS: ").append(robotPlugins.size()).append(" plugins are found: ");
        for ( String pluginName : robotWhitelist ) {
            sb.append(pluginName).append(" ");
        }
        LOG.info(sb.toString());
        return robotPlugins;
    }

    /**
     * returns the guice injector configured in this class. This is not dangerous, but you should ask yourself, why you need that ...</b>
     *
     * @return the injector
     */
    public Injector getInjectorForTests() {
        return this.injector;
    }

    /**
     * Configure the ipToCountry service, which maps the ip to the country code before something is logged. The ip of a user is not stored.<br>
     * To achieve this, database IpToCountry.csv has to be made available in the folder defined in the "server.iptocountry.dir" property, usually downloaded
     * from <a href="http://software77.net/geo-ip/?DL=1">http://software77.net</a> before</br>
     * This service MUST be configured for a public server, otherwise the server start will fail, for other server it is optional. In the latter case the ip is
     * replaced by the country code "ZZ"
     *
     * @return the translation service (either real or a dummy)
     */
    private IIpToCountry configureIpToCountryDb() {
        Boolean isPublicServer = this.serverProperties.getBooleanProperty("server.public");
        String pathIpToCountryDb = this.serverProperties.getStringProperty("server.iptocountry.dir");

        IIpToCountry ipToCountry;
        if ( pathIpToCountryDb != null ) {
            try {
                ipToCountry = new IpToCountry(pathIpToCountryDb);
            } catch ( IOException e ) {
                throw new DbcException("Path to IpToCountry.cvs or IpToCountry.cvs seems to be invalid. Server does NOT start", e);
            }
        } else if ( isPublicServer ) {
            throw new DbcException("Path to IpToCountry is obligatorily for public server. Server does NOT start");
        } else {
            ipToCountry = new IpToCountryDefault();
        }
        return ipToCountry;
    }

    /**
     * configure a thread, that runs periodically a service, that will remove expired HttpSessionState objects
     */
    private void configureHttpSessionStateCleanup() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable cleanupHttpSessionState = new Runnable() {
            @Override
            public void run() {
                HttpSessionState.removeExpired();
            }
        };
        scheduler.scheduleAtFixedRate(cleanupHttpSessionState, INTERVAL_HTTPSESSION_EXPIRE_SEC + 1, INTERVAL_HTTPSESSION_EXPIRE_SEC, TimeUnit.SECONDS);
    }

    /**
     * configure a thread, that runs periodically a service, that will remove expired database sessions
     */
    private void configureDbSessionCleanup() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable cleanupDbSession = new Runnable() {
            @Override
            public void run() {
                DbSession.cleanupSessions();
            }
        };
        scheduler.scheduleAtFixedRate(cleanupDbSession, INTERVAL_DB_SESSION_EXPIRE_SEC + 2, INTERVAL_DB_SESSION_EXPIRE_SEC, TimeUnit.SECONDS);
    }

    /**
     * setup the hibernate.connection.url<br>
     * <b>Note:</b> the "hibernate.connection.url" property is added to the properties!
     *
     * @param properties for configuring OpenRoberta, merged from property file and runtime arguments.
     */
    private void setupPropertyForDatabaseConnection(Properties properties) {
        String databaseParentDir = properties.getProperty("database.parentdir");
        String databaseUri = properties.getProperty("database.uri");
        String databaseName = properties.getProperty("database.name");
        String databaseMode = properties.getProperty("database.mode");
        String dbUrl;
        if ( "embedded".equals(databaseMode) ) {
            dbUrl = "jdbc:hsqldb:file:" + databaseParentDir + "/" + databaseName + ";ifexists=true;hsqldb.tx=mvcc";
        } else if ( "server".equals(databaseMode) ) {
            dbUrl = "jdbc:hsqldb:hsql://" + databaseUri + "/" + databaseName + ";hsqldb.tx=mvcc";
        } else {
            throw new DbcException("invalid database mode (use either embedded or server): " + databaseMode);
        }
        properties.put("hibernate.connection.url", dbUrl);
    }

    private void logTheNumberOfStoredPrograms() {
        try {
            DbSession session = this.injector.getInstance(SessionFactoryWrapper.class).getSession();
            List<?> numberOfProgramsInList = session.createSqlQuery("select count(*) from PROGRAM").list();
            ServerStarter.LOG.info("Number of programs stored in the database: " + numberOfProgramsInList);
            session.close();
        } catch ( Exception e ) {
            ServerStarter.LOG.error("Server could not connect to the database (exit 20)", e);
            System.exit(20);
        }

    }

    /**
     * step through all configured robot factories and make sure, that the group name of each robot (or its name, if no group is configured) is in the
     * database.<br>
     * This is required, because the robot (group) name is used as foreign key in some tables
     *
     * @param robotFactories collection of all configured robot factories
     */
    private void checkRobotPluginsDB(Collection<IRobotFactory> robotFactories) {
        try {
            DbSession session = this.injector.getInstance(SessionFactoryWrapper.class).getSession();
            RobotDao robotDao = new RobotDao(session);
            for ( IRobotFactory robotFactory : robotFactories ) {
                String robotForDb = robotFactory.getGroup();
                Robot pluginRobot = robotDao.loadRobot(robotForDb);
                if ( pluginRobot == null ) {
                    // add missing robot type to database
                    Robot robot = new Robot(robotForDb);
                    session.save(robot);
                    ServerStarter.LOG.info(robot.getName() + " added to the database");
                }
            }
            session.close();
        } catch ( Exception e ) {
            LOG.error("Server could not check robot names in the database (exit 20)", e);
            System.exit(20);
        }
    }

    private static String extractValue(String valueMaybeQuoted) {
        char first = valueMaybeQuoted.charAt(0);
        if ( first == '\'' || first == '"' ) {
            return valueMaybeQuoted.substring(1, valueMaybeQuoted.length() - 1);
        } else {
            return valueMaybeQuoted;
        }
    }

    private static List<String> asList(String... s) {
        return Arrays.asList(s);
    }

    public static class WebSocketServiceServlet extends WebSocketServlet {
        private static final long serialVersionUID = -2697779106901658247L;

        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(Ev3SensorLoggingWS.class);
        }
    }
}
