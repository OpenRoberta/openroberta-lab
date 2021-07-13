package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISoundVisitor<V> extends IHardwareVisitor<V>, ISimpleSoundVisitor<V> {

    V visitVolumeAction(VolumeAction<V> volumeAction);

    V visitPlayFileAction(PlayFileAction<V> playFileAction);

}