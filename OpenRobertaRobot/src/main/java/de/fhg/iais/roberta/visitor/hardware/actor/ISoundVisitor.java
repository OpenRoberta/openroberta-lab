package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISoundVisitor<V> extends IHardwareVisitor<V>, ISimpleSoundVisitor<V> {

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