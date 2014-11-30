package de.fhg.iais.roberta.main;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.guice.RobertaGuiceServletConfig;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.VersionChecker;

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

    private final Properties properties;
    private Injector injector;

    /**
     * create the starter. Load the properties.
     *
     * @param propertyPath optional URI to properties resource. May be null.
     */
    public ServerStarter(String propertyPath) {
        this.properties = Util.loadProperties(propertyPath);
    }

    /**
     * startup and shutdown of the server. See {@link ServerStarter}. Uses the first element of the args array. This contains the URI of a property file and
     * starts either with "file:" if a path of the file system should be used or "classpath:" if the properties should be loaded as a resource from the
     * classpath. May be <code>null</code>, if the default resource from the classpath should be loaded.
     *
     * @param args first element may contain the URI of a property file.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ServerStarter serverStarter;
        if ( args != null && args.length >= 1 ) {
            serverStarter = new ServerStarter(args[0]);
        } else {
            serverStarter = new ServerStarter(null);
        }
        Server server = serverStarter.start();
        LOG.info("*** server started using URI: " + server.getURI() + " ***");
        serverStarter.logTheNumberOfStoredPrograms();
        server.join();
        System.exit(0);
    }

    /**
     * startup. See {@link ServerStarter}. If the server could not be created, <b>the process will be terminated by System.exit(status) with status > 0</b>.
     *
     * @return the server
     */
    public Server start() throws IOException {
        int port = 1999;
        String versionFrom = this.properties.getProperty("validversionrange.From", "?");
        String versionTo = this.properties.getProperty("validversionrange.To", "?");
        Assert.isTrue(new VersionChecker(versionFrom, versionTo).validateServerSide(), "invalid versions found - this should NEVER occur");
        String serverPort = this.properties.getProperty("server.jetty.port", "1999");
        try {
            port = Integer.parseInt(serverPort);
        } catch ( Exception e ) {
            LOG.error("Could not get server port from properties. Server start aborted ... . Invalid value was: " + serverPort, e);
            System.exit(12);
        }
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        SessionManager sm = new HashSessionManager();
        context.setSessionHandler(new SessionHandler(sm));

        RobertaGuiceServletConfig robertaGuiceServletConfig = new RobertaGuiceServletConfig(this.properties);
        context.addEventListener(robertaGuiceServletConfig);
        context.addFilter(GuiceFilter.class, "/*", null);
        context.addServlet(DefaultServlet.class, "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("staticResources");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
            resourceHandler, context
        });
        server.setHandler(handlers);

        try {
            server.start();
        } catch ( Exception e ) {
            LOG.error("Could not start the server at port " + serverPort, e);
            System.exit(16);
        }
        this.injector = robertaGuiceServletConfig.getCreatedInjector();
        return server;
    }

    /**
     * returns the guice injector configured in this class. Even if this not dangerous per se, it should
     * <b>only be used in tests</b>
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
            int numberOfPrograms = projectDao.loadAll().size();
            LOG.info("There are " + numberOfPrograms + " programs stored in the database");
            session.close();
        } catch ( Exception e ) {
            LOG.error("Server was started, but could not connect to the database", e);
            System.exit(20);
        }

    }
}
