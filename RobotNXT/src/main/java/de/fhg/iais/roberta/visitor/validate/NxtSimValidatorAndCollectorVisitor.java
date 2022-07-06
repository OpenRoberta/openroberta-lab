package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;

public final class NxtSimValidatorAndCollectorVisitor extends NxtValidatorAndCollectorVisitor {

    public NxtSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        super.visitBluetoothCheckConnectAction(bluetoothCheckConnectAction);
        addWarningToPhrase(bluetoothCheckConnectAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        super.visitBluetoothReceiveAction(bluetoothReceiveAction);
        addWarningToPhrase(bluetoothReceiveAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        super.visitBluetoothSendAction(bluetoothSendAction);
        addWarningToPhrase(bluetoothSendAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        super.visitColorSensor(colorSensor);
        if ( colorSensor.getMode().equals("AMBIENTLIGHT") ) {
            addWarningToPhrase(colorSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        super.visitConnectConst(connectConst);
        addWarningToPhrase(connectConst, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        super.visitLightSensor(lightSensor);
        if ( lightSensor.getMode().equals("AMBIENTLIGHT") ) {
            addWarningToPhrase(lightSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        super.visitHTColorSensor(htColorSensor);
        addWarningToPhrase(htColorSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }
}
