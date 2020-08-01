package de.fhg.iais.roberta.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedHardwareCollectorVisitor;

public class ConfigTest extends AstTest {

    private static class TestUsedHardwareVisitor extends AbstractUsedHardwareCollectorVisitor {
        TestUsedHardwareVisitor() {
            super(null, null);
        }
    }

    private static final ConfigurationComponent ledComp = new ConfigurationComponent("LED", true, "port1", "userPort1", Collections.emptyMap());
    private static final ConfigurationComponent tempComp = new ConfigurationComponent("TEMPERATURE", false, "port2", "userPort2", Collections.emptyMap());
    private static final ConfigurationComponent humiComp = new ConfigurationComponent("HUMIDITY", false, "port3", "userPort3", Collections.emptyMap());

    @Test
    public void builder_ShouldCreateCorrectConfig_WhenProvidedData() {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setDescription("test desc");
        builder.setTags("test tag");
        builder.setXmlVersion("test ver");
        builder.setIpAddress("0.1.2.3");
        builder.setUserName("testUser");
        builder.setPassword("testPass");
        builder.setRobotType("roboto");
        builder.setTrackWidth(42.0f);
        builder.setWheelDiameter(21.0f);
        builder.addComponents(Arrays.asList(ledComp, tempComp, humiComp));
        ConfigurationAst config = builder.build();

        Assert.assertEquals("test desc", config.getDescription());
        Assert.assertEquals("test tag", config.getTags());
        Assert.assertEquals("test ver", config.getXmlVersion());
        Assert.assertEquals("0.1.2.3", config.getIpAddress());
        Assert.assertEquals("testUser", config.getUserName());
        Assert.assertEquals("testPass", config.getPassword());
        Assert.assertEquals("roboto", config.getRobotType());
        Assert.assertEquals(42.0, config.getTrackWidth(), 0.0);
        Assert.assertEquals(21.0, config.getWheelDiameter(), 0.0);
        Assert.assertEquals(config.getConfigurationComponents().size(), 3L);
        List<ConfigurationComponent> actors = new ArrayList<>(config.getActors());
        List<ConfigurationComponent> sensors = new ArrayList<>(config.getSensors());
        Assert.assertEquals(actors.get(0), ledComp);
        Assert.assertEquals(sensors.get(0), humiComp);
        Assert.assertEquals(sensors.get(1), tempComp);
    }

    @Test
    public void getters_ShouldReturnCorrectComp_WhenUsedProperly() {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(ledComp, tempComp, humiComp));
        ConfigurationAst config = builder.build();

        Assert.assertEquals(config.getConfigurationComponent("userPort3"), humiComp);
        Assert.assertEquals(config.optConfigurationComponent("userPort2"), tempComp);
        Assert.assertNull(config.optConfigurationComponent("does not exist"));
        Assert.assertEquals(config.optConfigurationComponentByType("LED"), ledComp);
        Assert.assertNull(config.optConfigurationComponentByType("does not exist"));
    }

    @Test(expected = DbcException.class)
    public void getConfigurationComponent_ShouldThrowException_WhenNameDoesNotExist() {
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(ledComp, tempComp, humiComp));
        ConfigurationAst config = builder.build();

        config.getConfigurationComponent("does not exist");
    }

    @Test(expected = DbcException.class)
    public void compAccept_ShouldThrowDbcException_WhenTryingToVisit() {
        TestUsedHardwareVisitor visitor = new TestUsedHardwareVisitor();
        tempComp.accept(visitor);
    }
}
