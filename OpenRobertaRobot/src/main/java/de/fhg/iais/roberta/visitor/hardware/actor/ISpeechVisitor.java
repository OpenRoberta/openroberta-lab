package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISpeechVisitor<V> extends IHardwareVisitor<V> {

    V visitSetLanguageAction(SetLanguageAction<V> setLanguageAction);

    V visitSayTextAction(SayTextAction<V> sayTextAction);

}