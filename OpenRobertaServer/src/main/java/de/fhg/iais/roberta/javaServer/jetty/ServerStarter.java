package de.fhg.iais.roberta.javaServer.jetty;

import java.io.IOException;

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

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import de.fhg.iais.roberta.guice.RobertaGuiceServletConfig;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;

/**
 * the main class of the application.<br>
 * <br>
 * - starts an embedded jetty (see {@link ServletContainer}<br>
 * - configures jersey and the package with the resources<br>
 * - configures jaxb and the package with the providers<br>
 * - configures a resource holder for static content<br>
 * - configures hibernate and tests the connection to the database. Hibernate uses Sqlite as underlying database<br>
 *
 * @author rbudde
 */
public class ServerStarter {
    private static final Logger LOG = LoggerFactory.getLogger(ServerStarter.class);
    private static final int PORT = 1999;

    /**
     * startup and shutdown. See {@link ServerStarter}
     *
     * @param args unused
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server server = new ServerStarter().start(PORT);
        server.join();
    }

    /**
     * startup. See {@link ServerStarter}
     *
     * @param port the port jetty should listen to
     * @throws Exception
     */
    public Server start(int port) throws IOException {
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        SessionManager sm = new HashSessionManager();
        context.setSessionHandler(new SessionHandler(sm));

        RobertaGuiceServletConfig robertaGuiceServletConfig = new RobertaGuiceServletConfig();
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
            e.printStackTrace();
        }

        SessionWrapper session = robertaGuiceServletConfig.getCreatedInjector().getInstance(SessionFactoryWrapper.class).getSession();
        ProgramDao projectDao = new ProgramDao(session);
        int numberOfPrograms = projectDao.loadAll().size();
        LOG.info("There are " + numberOfPrograms + " programs stored in the database");
        session.close();

        return server;
    }
}
