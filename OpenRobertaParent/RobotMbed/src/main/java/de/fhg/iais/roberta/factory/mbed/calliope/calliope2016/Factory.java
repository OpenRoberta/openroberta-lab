package de.fhg.iais.roberta.factory.mbed.calliope.calliope2016;

import java.util.Map;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.AbstractFactory;
import de.fhg.iais.roberta.factory.mbed.calliope.CompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractFactory {

    Map<String, SensorPort> sensorToPorts = IRobotFactory.getPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2016.properties");
        this.name = this.calliopeProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));

        addBlockTypesFromProperties("Calliope2016.properties", this.calliopeProperties);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getPortValue(port, this.sensorToPorts);
    }
}
