package de.fhg.iais.roberta.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.guice.RobertaGuiceServletConfig;
import de.fhg.iais.roberta.javaServer.websocket.Ev3SensorLoggingWS;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;
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
    private static final Logger LOG = LoggerFactory.getLogger(ServerStarter.class);

    private final RobertaProperties robertaProperties; // for the startup
    private Injector injector;

    /**
     * startup and shutdown of the server. See {@link ServerStarter}. Uses the first element of the args array. This contains the URI of a property file and
     * starts either with "file:" if a path of the file system should be used or "classpath:" if the properties should be loaded as a resource from the
     * classpath. May be <code>null</code>, if the default resource from the classpath should be loaded.
     *
     * @param args first element may contain the URI of a property file.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        OptionSpec<String> defineOpt = parser.accepts("d").withRequiredArg().ofType(String.class);
        OptionSet options = parser.parse(args);
        List<String> defines = defineOpt.values(options);

        final ServerStarter serverStarter = new ServerStarter(null, defines);
        Server server = serverStarter.start();
        server.join();
        System.exit(0);
    }

    /**
     * create the starter. Load the properties.
     *
     * @param propertyPath optional URI to properties resource. May be null.
     * @param defines is a list of properties (from the command line ...) which overwrite the properties from the propertyPath. May be null.
     */
    public ServerStarter(String propertyPath, List<String> defines) {
        Properties properties = Util1.loadAndMergeProperties(propertyPath, defines);
        setupPropertyForDatabaseConnection(properties);
        robertaProperties = new RobertaProperties(properties);
        Util.setServerVersion(robertaProperties.getStringProperty("openRobertaServer.version"));
    }

    /**
     * startup. See {@link ServerStarter}. If the server could not be created, <b>the process will be terminated by System.exit(status) with status > 0</b>.
     *
     * @return the server
     */
    public Server start() throws IOException {
        String host = robertaProperties.getStringProperty("server.ip");
        int httpPort = robertaProperties.getIntProperty("server.port", 0);
        int httpsPort = robertaProperties.getIntProperty("server.portHttps", 0);

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
            String keyStoreUri = robertaProperties.getStringProperty("server.keystore.uri");
            if ( keyStoreUri == null ) {
                keyStoreUri = ServerStarter.class.getResource("/keystore.jks").toExternalForm();
            }
            String password = robertaProperties.getStringProperty("server.keystore.password");
            if ( password == null ) {
                password = "oraOra";
            }
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
        Map<String, IRobotFactory> robotPluginMap = configureRobotPlugins(robotCommunicator, robertaProperties);
        RobertaGuiceServletConfig robertaGuiceServletConfig = new RobertaGuiceServletConfig(robertaProperties, robotPluginMap, robotCommunicator);

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

        // 3. static resources and REST API without /rest prefix (deprecated, used by very old ev3 robots)
        ServletContextHandler defaultHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        defaultHandler.setContextPath("/*");
        defaultHandler.setSessionHandler(new SessionHandler());

        // 3.1 REST API without prefix (deprecated, used by very old ev3 robots)
        defaultHandler.addEventListener(robertaGuiceServletConfig);
        defaultHandler.addFilter(GuiceFilter.class, "/alive/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/admin/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/conf/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/ping/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/program/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/toolbox/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/user/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/hello/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/pushcmd/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/download/*", null);
        defaultHandler.addFilter(GuiceFilter.class, "/update/*", null);

        // 3.2 static resources
        ServletHolder staticResourceServlet = defaultHandler.addServlet(DefaultServlet.class, "/*");
        staticResourceServlet.setInitParameter("dirAllowed", "false");
        staticResourceServlet.setInitParameter("precompressed", "gzip=.gz");
        String dirNameStaticResources = robertaProperties.getStringProperty("server.staticresources.dir");
        staticResourceServlet.setInitParameter("resourceBase", new File(dirNameStaticResources).toPath().toAbsolutePath().normalize().toUri().toASCIIString());
        staticResourceServlet.setInitParameter("cacheControl", "private, must-revalidate");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(
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
            System.exit(16);
        }
        this.injector = robertaGuiceServletConfig.getCreatedInjector();
        Ev3SensorLoggingWS.setGuiceInjector(this.injector);

        checkRobotPluginsDB();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook("embedded".equals(robertaProperties.getStringProperty("database.mode")), this.injector));
        LOG.info("Shutdown hook added. If the server is gracefully stopped in the future, a shutdown message is logged");
        logTheNumberOfStoredPrograms();

        return server;
    }

    /**
     * setup the hibernate.connection.url<br>
     * <b>Note:</b> the "hibernate.connection.url" property is added to the properties!
     *
     * @param properties for configuring OpenRoberta, merged from property file and runtime arguments.
     */
    private void setupPropertyForDatabaseConnection(Properties properties) {
        String serverVersionForDbDirectory = properties.getProperty("openRobertaServer.version").replace("-SNAPSHOT", "");
        String databaseParentDir = properties.getProperty("database.parentdir");
        String databaseUri = properties.getProperty("database.uri");
        String databaseMode = properties.getProperty("database.mode");
        String dbUrl;
        if ( "embedded".equals(databaseMode) ) {
            dbUrl = "jdbc:hsqldb:file:" + databaseParentDir + "/db-" + serverVersionForDbDirectory + "/openroberta-db;ifexists=true";
        } else if ( "server".equals(databaseMode) ) {
            dbUrl = "jdbc:hsqldb:hsql://" + databaseUri + "/openroberta-db";
        } else {
            throw new DbcException("invalid database mode (use either embedded or server): " + databaseMode);
        }
        properties.put("hibernate.connection.url", dbUrl);
    }

    /**
     * returns the guice injector configured in this class. This not dangerous, but you should ask yourself, why you need that ...</b>
     *
     * @return the injector
     */
    public Injector getInjectorForTests() {
        return this.injector;
    }

    private void logTheNumberOfStoredPrograms() {
        try {
            DbSession session = this.injector.getInstance(SessionFactoryWrapper.class).getSession();
            ProgramDao projectDao = new ProgramDao(session);
            int numOfProgs = projectDao.loadAll().size();
            ServerStarter.LOG.info("Number of programs stored in the database: " + numOfProgs);
            session.close();
        } catch ( Exception e ) {
            ServerStarter.LOG.error("Server was started, but could not connect to the database", e);
            System.exit(20);
        }

    }

    /**
     * configure robot plugins, that may be used with this server. Uses the white list and the declarations from the openroberta.properties file.
     *
     * @param robotCommunicator
     * @return the mapping from robot names to the factory, that supplies all robot-specific data
     */
    public static Map<String, IRobotFactory> configureRobotPlugins(RobotCommunicator robotCommunicator, RobertaProperties robertaProperties) {
        if ( robotCommunicator == null ) {
            LOG.error("the robot communicator object was not found. This is a severe error. The system will crash!");
        }
        List<String> robotWhitelist = robertaProperties.getRobotWhitelist();
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        for ( String robotToUse : robotWhitelist ) {
            if ( robotToUse.equals("sim") ) {
                continue;
            }
            String pluginFactory = robertaProperties.getStringProperty("robot.plugin." + robotToUse + ".factory");
            if ( pluginFactory == null ) {
                throw new DbcException("robot plugin " + robotToUse + " has no factory. Check the properties. Server does NOT start");
            } else {
                try {
                    @SuppressWarnings("unchecked")
                    Class<IRobotFactory> factoryClass = (Class<IRobotFactory>) ServerStarter.class.getClassLoader().loadClass(pluginFactory);
                    Constructor<IRobotFactory> factoryConstructor = factoryClass.getDeclaredConstructor(robertaProperties.getClass());
                    robotPlugins.put(robotToUse, factoryConstructor.newInstance(robertaProperties));
                } catch ( Exception e ) {
                    throw new DbcException(
                        "no factory for robot plugin " + robotToUse + ". Plugin-jar not on the classpath? Invalid properties? Server does NOT start",
                        e);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ROBOT PLUGINS: ").append(robotPlugins.size()).append(" plugins are found: ");
        for ( String pluginName : robotWhitelist ) {
            sb.append(pluginName).append(" ");
        }
        LOG.info(sb.toString());
        return robotPlugins;
    }

    public static class WebSocketServiceServlet extends WebSocketServlet {
        private static final long serialVersionUID = -2697779106901658247L;

        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(Ev3SensorLoggingWS.class);
        }
    }

    /**
     * @deprecated This method shouldn't be called anymore when the robot type is no longer stored in the database.
     */
    @Deprecated
    private void checkRobotPluginsDB() {
        try {
            List<String> robotWhitelist = robertaProperties.getRobotWhitelist();
            DbSession session = this.injector.getInstance(SessionFactoryWrapper.class).getSession();
            RobotDao robotDao = new RobotDao(session);
            for ( String robotToUse : robotWhitelist ) {
                String pluginName = robotToUse;
                if ( robertaProperties.getStringProperty("robot.plugin." + pluginName + ".group") != null ) {
                    pluginName = robertaProperties.getStringProperty("robot.plugin." + pluginName + ".group");
                }
                Robot pluginRobot = robotDao.loadRobot(pluginName);
                if ( pluginRobot == null ) {
                    // add missing robot type to database
                    Pair<Key, Robot> result = robotDao.persistRobot(pluginName);
                    session.save(result.getSecond());
                    ServerStarter.LOG.info(result.getSecond().getName() + " added to the database");
                }
            }
            session.close();
        } catch ( Exception e ) {
            ServerStarter.LOG.error("Server was started, but could not connect to the database", e);
            System.exit(20);
        }
    }
}
