package de.fhg.iais.roberta.main;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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
        OptionSpec<String> propertiesOpt = parser.accepts("properties").withOptionalArg().ofType(String.class);
        OptionSpec<String> defineOpt = parser.accepts("d").withRequiredArg().ofType(String.class);
        OptionSet options = parser.parse(args);
        String propertyPath = propertiesOpt.value(options);
        List<String> defines = defineOpt.values(options);

        ServerStarter serverStarter = new ServerStarter(propertyPath, defines);
        Server server = serverStarter.start();
        serverStarter.checkRobotPluginsDB();
        serverStarter.logTheNumberOfStoredPrograms();
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
        Properties mailProperties = Util1.loadProperties("classpath:openRobertaMailServer.properties");
        Properties robertaProperties = Util1.loadProperties(propertyPath);
        if ( mailProperties != null ) {
            robertaProperties.putAll(mailProperties);
        }
        if ( defines != null ) {
            for ( String define : defines ) {
                String[] property = define.split("\\s*=\\s*");
                if ( property.length == 2 ) {
                    LOG.info("new property from command line: " + define);
                    robertaProperties.put(property[0], property[1]);
                } else {
                    LOG.info("command line property is invalid and thus ignored: " + define);

                }
            }
        }
        RobertaProperties.setRobertaProperties(robertaProperties);
    }

    /**
     * startup. See {@link ServerStarter}. If the server could not be created, <b>the process will be terminated by System.exit(status) with status > 0</b>.
     *
     * @return the server
     */
    public Server start() throws IOException {
        // String versionFrom = this.properties.getProperty("validversionrange.From", "?");
        // String versionTo = this.properties.getProperty("validversionrange.To", "?");
        // Assert.isTrue(new VersionChecker(versionFrom, versionTo).validateServerSide(), "invalid versions found - this should NEVER occur");
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        String host = RobertaProperties.getStringProperty("server.ip");
        int port = RobertaProperties.getIntProperty("server.port");
        http.setHost(host);
        http.setPort(port);
        server.setConnectors(new ServerConnector[] {
            http
        });

        // configure robot plugins
        RobotCommunicator robotCommunicator = new RobotCommunicator();
        Map<String, IRobotFactory> robotPluginMap = configureRobotPlugins(robotCommunicator);
        RobertaGuiceServletConfig robertaGuiceServletConfig =
            new RobertaGuiceServletConfig(RobertaProperties.getRobertaProperties(), robotPluginMap, robotCommunicator);

        // REST API with /rest/<version>/ prefix
        ServletContextHandler versionedHttpHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        versionedHttpHandler.setContextPath("/rest");
        versionedHttpHandler.setSessionHandler(new SessionHandler(new HashSessionManager()));

        versionedHttpHandler.addEventListener(robertaGuiceServletConfig);
        versionedHttpHandler.addFilter(GuiceFilter.class, "/*", null);
        versionedHttpHandler.addServlet(DefaultServlet.class, "/*");

        // REST API without prefix (deprecated) and static resources
        ServletContextHandler rootHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        rootHandler.setContextPath("/*");
        rootHandler.setSessionHandler(new SessionHandler(new HashSessionManager()));

        rootHandler.addEventListener(robertaGuiceServletConfig);
        rootHandler.addFilter(GuiceFilter.class, "/alive/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/admin/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/conf/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/ping/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/program/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/toolbox/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/user/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/hello/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/pushcmd/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/download/*", null);
        rootHandler.addFilter(GuiceFilter.class, "/update/*", null);
        ServletHolder staticResourceServlet = rootHandler.addServlet(DefaultServlet.class, "/*");

        staticResourceServlet.setInitParameter("gzip", "true");
        staticResourceServlet.setInitParameter("resourceBase", ServerStarter.class.getResource("/staticResources").toExternalForm());

        // websockets with /ws/<version>/ prefix
        ServletContextHandler wsHandler = new ServletContextHandler();
        wsHandler.setContextPath("/ws");
        wsHandler.addServlet(WebSocketServiceServlet.class, "/*");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
            versionedHttpHandler,
            wsHandler,
            rootHandler
        });
        server.setHandler(handlers);

        try {
            server.start();
            ServerStarter.LOG.info("server started at " + server.getURI());
        } catch ( Exception e ) {
            ServerStarter.LOG.error("Could not start the server at " + host + ":" + port, e);
            System.exit(16);
        }
        this.injector = robertaGuiceServletConfig.getCreatedInjector();
        Ev3SensorLoggingWS.setGuiceInjector(this.injector);

        return server;
    }

    /**
     * configure robot plugins, that may be used with this server. Uses the white list and the declarations from the openroberta.properties file.
     *
     * @param robotCommunicator
     * @return the mapping from robot names to the factory, that supplies all robot-specific data
     */
    private Map<String, IRobotFactory> configureRobotPlugins(RobotCommunicator robotCommunicator) {
        if ( robotCommunicator == null ) {
            LOG.error("the robot communicator object was not found. This is a severe error. The system will crash!");
        }
        Set<String> pluginNumbers = new HashSet<>();
        Pattern pluginPattern = Pattern.compile("robot\\.plugin\\.(\\d+)\\..*");
        Properties robertaProperties = RobertaProperties.getRobertaProperties();
        for ( String key : robertaProperties.stringPropertyNames() ) {
            Matcher keyMatcher = pluginPattern.matcher(key);
            if ( keyMatcher.matches() ) {
                pluginNumbers.add(keyMatcher.group(1));
            }
        }
        List<String> robotWhitelist = RobertaProperties.getRobotWhitelist();
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        whitelist: for ( String robotToUse : robotWhitelist ) {
            for ( String pluginNumber : pluginNumbers ) {
                String pluginName = robertaProperties.getProperty("robot.plugin." + pluginNumber + ".name");
                if ( pluginName == null ) {
                    throw new DbcException("robot plugin with number " + pluginNumber + " is invalid. Check the properties. Server does NOT start");
                }
                if ( robotToUse.equals("sim") )
                    continue whitelist;
                if ( robotToUse.equals(pluginName) ) {
                    String pluginFactory = robertaProperties.getProperty("robot.plugin." + pluginNumber + ".factory");
                    if ( pluginFactory == null ) {
                        throw new DbcException("robot plugin " + pluginName + " has no factory. Check the properties. Server does NOT start");
                    } else {
                        try {
                            @SuppressWarnings("unchecked")
                            Class<IRobotFactory> factoryClass = (Class<IRobotFactory>) ServerStarter.class.getClassLoader().loadClass(pluginFactory);
                            Constructor<IRobotFactory> factoryConstructor = factoryClass.getDeclaredConstructor(RobotCommunicator.class);
                            robotPlugins.put(pluginName, factoryConstructor.newInstance(robotCommunicator));
                        } catch ( Exception e ) {
                            throw new DbcException("robot plugin " + pluginName + " has an invalid factory. Check the properties. Server does NOT start", e);
                        }
                    }
                    continue whitelist;
                }
            }
            throw new DbcException("robot plugin " + robotToUse + " not found. Check the properties. Server does NOT start");
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
        Properties robertaProperties = RobertaProperties.getRobertaProperties();
        Set<String> pluginNumbers = new HashSet<>();
        Pattern pluginPattern = Pattern.compile("robot\\.plugin\\.(\\d+)\\..*");
        for ( String key : robertaProperties.stringPropertyNames() ) {
            Matcher keyMatcher = pluginPattern.matcher(key);
            if ( keyMatcher.matches() ) {
                pluginNumbers.add(keyMatcher.group(1));
            }
        }
        try {
            DbSession session = this.injector.getInstance(SessionFactoryWrapper.class).getSession();
            RobotDao robotDao = new RobotDao(session);
            for ( String pluginNumber : pluginNumbers ) {
                String pluginName = robertaProperties.getProperty("robot.plugin." + pluginNumber + ".name");
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
