package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;

public class MicrobitV2ValidatorAndCollectorVisitor extends MbedV2ValidatorAndCollectorVisitor implements IMicrobitV2Visitor<Void> {

    private final boolean isSim;

    public MicrobitV2ValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed) {
        super(brickConfiguration, beanBuilders, displaySwitchUsed);
        this.isSim = isSim;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addToPhraseIfUnsupportedInSim(playFileAction, false, isSim);
        return super.visitPlayFileAction(playFileAction);
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return null;
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        addToPhraseIfUnsupportedInSim(soundToggleAction, false, isSim);
        return super.visitSoundToggleAction(soundToggleAction);
    }
}
