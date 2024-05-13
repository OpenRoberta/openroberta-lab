package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOnHiddenAction;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;

public class CalliopeV3StackMachineVisitor extends CalliopeStackMachineVisitor implements ICalliopeVisitor<Void> {
    public CalliopeV3StackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        super(configuration, phrases, usedHardwareBean, nnBean);
        Assert.isTrue(!phrases.isEmpty());
    }

    public Void visitRgbLedsOnHiddenAction(RgbLedsOnHiddenAction rgbLedsOnHiddenAction) {
        rgbLedsOnHiddenAction.colour.accept(this);
        JSONObject o = makeNode(C.RGBLED_ON_ACTION).put(C.PORT, rgbLedsOnHiddenAction.slot);
        return add(o);
    }

    @Override
    public Void visitRgbLedsOffHiddenAction(RgbLedsOffHiddenAction rgbLedsOffHiddenAction) {
        JSONObject o = makeNode(C.RGBLED_OFF_ACTION).put(C.PORT, rgbLedsOffHiddenAction.slot);
        return add(o);
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        rgbLedOnHiddenAction.colour.accept(this);
        JSONObject o = makeNode(C.RGBLED_ON_ACTION).put(C.PORT, "1");
        return add(o);
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        JSONObject o = makeNode(C.RGBLED_OFF_ACTION).put(C.PORT, "1");
        return add(o);
    }
}
