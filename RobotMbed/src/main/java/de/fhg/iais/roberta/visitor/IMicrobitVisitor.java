package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;

public interface IMicrobitVisitor<V> extends IMbedVisitor<V> {

    V visitPlayFileAction(PlayFileAction playFileAction);

}
