package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.visitor.C;

public class OrbStackMachineVisitor<V> extends Ev3StackMachineVisitor<V> {

    public OrbStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases, ILanguage language) {
        super(configuration, phrases, language);
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        final String mode = infraredSensor.getMode();
        final String port = infraredSensor.getUserDefinedPort();
        final JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "orb");
        return app(o);
    }

}
