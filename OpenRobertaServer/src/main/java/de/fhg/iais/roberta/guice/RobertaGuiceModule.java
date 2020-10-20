package de.fhg.iais.roberta.guice;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientPing;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.RestExample;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotCommand;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotDownloadProgram;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotSensorLogging;
import de.fhg.iais.roberta.main.IIpToCountry;
import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.NotificationService;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.UtilForREST;

public class RobertaGuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(RobertaGuiceModule.class);
    private final ServerProperties serverProperties;
    private final Map<String, IRobotFactory> robotPluginMap;
    private final RobotCommunicator robotCommunicator;
    private final IIpToCountry ipToCountry;

    public RobertaGuiceModule(
        ServerProperties serverProperties,
        Map<String, IRobotFactory> robotPluginMap,
        RobotCommunicator robotCommunicator,
        IIpToCountry ipToCountry) {
        this.serverProperties = serverProperties;
        this.robotPluginMap = robotPluginMap;
        this.robotCommunicator = robotCommunicator;
        this.ipToCountry = ipToCountry;
    }

    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(ClientAdmin.class);
        bind(ClientConfiguration.class);
        bind(ClientUser.class);
        bind(RobotDownloadProgram.class);
        bind(RobotCommand.class);
        bind(RobotSensorLogging.class);
        bind(RestExample.class);
        bind(ClientPing.class);

        bind(ServerProperties.class).toInstance(this.serverProperties);
        bind(NotificationService.class).in(Singleton.class);
        bind(SessionFactoryWrapper.class).in(Singleton.class);
        bind(RobotCommunicator.class).toInstance(this.robotCommunicator);
        bind(MailManagement.class).in(Singleton.class);
        bind(IIpToCountry.class).toInstance(this.ipToCountry);

        requestStaticInjection(UtilForREST.class);

        bind(new TypeLiteral<Map<String, IRobotFactory>>() {
        }).annotatedWith(Names.named("robotPluginMap")).toInstance(this.robotPluginMap);
        bind(String.class).annotatedWith(Names.named("hibernate.config.xml")).toInstance("hibernate-cfg.xml");

        try {
            Names.bindProperties(binder(), this.serverProperties.getserverProperties());
        } catch ( Exception e ) {
            LOG.error("Could not bind global properties to guice", e);
        }
    }
}
