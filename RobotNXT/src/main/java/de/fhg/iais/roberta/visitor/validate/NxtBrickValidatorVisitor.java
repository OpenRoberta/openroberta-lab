package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.INxtVisitor;

public final class NxtBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements INxtVisitor<Void> {

    public NxtBrickValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        super.checkSensorPort(sensor);
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getPort());
        if ( usedSensor == null ) {
            // should be handled by super implementation
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "HTCOLOR_SENSING":
                    if ( !type.equals("HT_COLOR") ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        checkSensorPort(htColorSensor);
        String mode = htColorSensor.getMode();
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(htColorSensor.getPort(), SC.HT_COLOR, mode));
        return null;
    }
}
