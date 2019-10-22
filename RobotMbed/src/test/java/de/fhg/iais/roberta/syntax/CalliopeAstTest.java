package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.BeforeClass;

import com.google.common.collect.Lists;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.Calliope2016Factory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class CalliopeAstTest extends AstTest {

    protected static ConfigurationAst configuration;

    @BeforeClass
    public static void setup() {
        testFactory = new Calliope2016Factory(new PluginProperties("calliope2017", "", "", Util1.loadProperties("classpath:/calliope2017.properties")));

        ConfigurationComponent pin0 = new ConfigurationComponent("pin0", false, "P12", "0", Collections.emptyMap());
        ConfigurationComponent pin1 = new ConfigurationComponent("pin1", false, "P0", "1", Collections.emptyMap());
        ConfigurationComponent pin2 = new ConfigurationComponent("pin2", false, "P1", "2", Collections.emptyMap());
        ConfigurationComponent C16 = new ConfigurationComponent("C16", false, "P2", "C16", Collections.emptyMap());
        ConfigurationComponent C04 = new ConfigurationComponent("C04", false, "P3", "C04", Collections.emptyMap());
        ConfigurationComponent C05 = new ConfigurationComponent("C05", false, "P4", "C05", Collections.emptyMap());
        ConfigurationComponent C12 = new ConfigurationComponent("C12", false, "P6", "C12", Collections.emptyMap());
        ConfigurationComponent C11 = new ConfigurationComponent("C11", false, "P7", "C11", Collections.emptyMap());
        ConfigurationComponent C17 = new ConfigurationComponent("C17", false, "P8", "C17", Collections.emptyMap());
        ConfigurationComponent C10 = new ConfigurationComponent("C10", false, "P9", "C10", Collections.emptyMap());
        ConfigurationComponent C06 = new ConfigurationComponent("C06", false, "P10", "C06", Collections.emptyMap());
        ConfigurationComponent C07 = new ConfigurationComponent("C07", false, "P13", "C07", Collections.emptyMap());
        ConfigurationComponent C08 = new ConfigurationComponent("C08", false, "P14", "C08", Collections.emptyMap());
        ConfigurationComponent C09 = new ConfigurationComponent("C09", false, "P15", "C09", Collections.emptyMap());
        ConfigurationComponent pin3 = new ConfigurationComponent("3", false, "P16", "3", Collections.emptyMap());
        ConfigurationComponent C19 = new ConfigurationComponent("C19", false, "P19", "C19", Collections.emptyMap());
        ConfigurationComponent C18 = new ConfigurationComponent("C18", false, "P20", "C18", Collections.emptyMap());
        ConfigurationComponent pin4 = new ConfigurationComponent("4", false, "P19", "4", Collections.emptyMap());
        ConfigurationComponent pin5 = new ConfigurationComponent("5", false, "P2", "5", Collections.emptyMap());
        ConfigurationComponent X = new ConfigurationComponent("X", false, "Pitch", "X", Collections.emptyMap());
        ConfigurationComponent Y = new ConfigurationComponent("Y", false, "Roll", "Y", Collections.emptyMap());
        ConfigurationComponent Z = new ConfigurationComponent("Z", false, "Yaw", "Z", Collections.emptyMap());
        ConfigurationComponent STRENGTH = new ConfigurationComponent("STRENGTH", false, "STRENGTH", "STRENGTH", Collections.emptyMap());
        ConfigurationComponent NO_PORT = new ConfigurationComponent("NO_PORT", false, "NO_PORT", "NO_PORT", Collections.emptyMap());
        ConfigurationComponent A = new ConfigurationComponent("A", false, "A", "A", Collections.emptyMap());
        ConfigurationComponent B = new ConfigurationComponent("B", false, "B", "B", Collections.emptyMap());
        ConfigurationComponent AB = new ConfigurationComponent("AB", false, "AB", "AB", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components =
            Lists
                .newArrayList(
                    pin0,
                    pin1,
                    pin2,
                    C16,
                    C04,
                    C05,
                    C12,
                    C11,
                    C17,
                    C10,
                    C06,
                    C07,
                    C08,
                    C09,
                    pin3,
                    C19,
                    C18,
                    pin4,
                    pin5,
                    X,
                    Y,
                    Z,
                    STRENGTH,
                    NO_PORT,
                    A,
                    B,
                    AB);
        configuration = new ConfigurationAst.Builder().addComponents(components).build();
    }
}
