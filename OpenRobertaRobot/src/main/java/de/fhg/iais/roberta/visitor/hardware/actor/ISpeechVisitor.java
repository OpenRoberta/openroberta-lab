package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.actor.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.actor.speech.SetLanguageAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISpeechVisitor<V> extends IHardwareVisitor<V> {

    V visitSetLanguageAction(SetLanguageAction setLanguageAction);

    V visitSayTextAction(SayTextAction sayTextAction);

    V visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction);

}