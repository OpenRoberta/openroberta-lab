package de.fhg.iais.roberta.visitor.actor;

import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;

public interface AstActorSoundVisitor<V> extends AstActorVisitor<V> {
    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    V visitToneAction(ToneAction<V> toneAction);

    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    V visitVolumeAction(VolumeAction<V> volumeAction);

    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    V visitPlayFileAction(PlayFileAction<V> playFileAction);

}