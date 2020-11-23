package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISoundVisitor<V> extends IHardwareVisitor<V> {

    /**
     * visit a {@link ToneAction}.
     *
     * @param toneAction to be visited
     */
    default V visitToneAction(ToneAction<V> toneAction) {
        throw new DbcException("Not supported!");
    }

    /**
     * visit a {@link PlayNoteAction}.
     *
     * @param playNoteAction
     */
    default V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        throw new DbcException("Not supported!");
    }


    /**
     * visit a {@link VolumeAction}.
     *
     * @param volumeAction to be visited
     */
    default V visitVolumeAction(VolumeAction<V> volumeAction) {
        throw new DbcException("Not supported!");
    }


    /**
     * visit a {@link PlayFileAction}.
     *
     * @param playFileAction
     */
    default V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        throw new DbcException("Not supported!");
    }


}