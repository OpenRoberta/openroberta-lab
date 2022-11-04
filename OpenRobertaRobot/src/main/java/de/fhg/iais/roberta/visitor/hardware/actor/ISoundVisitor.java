package de.fhg.iais.roberta.visitor.hardware.actor;

import de.fhg.iais.roberta.syntax.actor.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.actor.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.actor.sound.SetVolumeAction;
import de.fhg.iais.roberta.visitor.hardware.IHardwareVisitor;

public interface ISoundVisitor<V> extends IHardwareVisitor<V>, ISimpleSoundVisitor<V> {

    V visitGetVolumeAction(GetVolumeAction getVolumeAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);

    V visitPlayFileAction(PlayFileAction playFileAction);

}