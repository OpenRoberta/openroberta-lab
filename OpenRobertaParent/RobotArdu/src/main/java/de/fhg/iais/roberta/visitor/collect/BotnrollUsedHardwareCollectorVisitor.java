package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.arduino.BotNrollConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.control.RelayAction;
import de.fhg.iais.roberta.visitor.hardware.IBotnrollVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class BotnrollUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IBotnrollVisitor<Void> {

    public BotnrollUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, BotNrollConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

}
