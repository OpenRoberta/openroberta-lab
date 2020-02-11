package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public final class Calliope2017ValidatorVisitor extends MbedBoardValidatorVisitor {

    public Calliope2017ValidatorVisitor(Builder builder, ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        radioSendAction.getMsg().accept(this);
        radioSendAction.addInfo(NepoInfo.error("BLOCK_NOT_SUPPORTED"));
        this.errorCount++;
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        radioReceiveAction.addInfo(NepoInfo.error("BLOCK_NOT_SUPPORTED"));
        this.errorCount++;
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        radioSetChannelAction.getChannel().accept(this);
        radioSetChannelAction.addInfo(NepoInfo.error("BLOCK_NOT_SUPPORTED"));
        this.errorCount++;
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor<Void> radioRssiSensor) {
        radioRssiSensor.addInfo(NepoInfo.error("BLOCK_NOT_SUPPORTED"));
        this.errorCount++;
        return null;
    }


}
