package de.budde;

public class Curve {
    private static Configuration brickConfiguration;

    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH)));

    private Hal hal = new Hal(brickConfiguration, this.usedSensors);

    public static void main(String[] args) {

    }

    public void run() throws Exception {
        this.hal.startLogging();
    
        this.hal.closeResources();
    }
}
