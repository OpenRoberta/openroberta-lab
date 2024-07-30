package de.fhg.iais.roberta.worker.validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.MicrobitMethods;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;


public abstract class MbedValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {
    private final List<String> freePins;
    private final List<String> defaultProperties;
    private final List<String> existingPins;
    private final HashMap<String, String> mapCorrectConfigPins;

    /**
     * @param freePins All the pins that can be used on the calliope/microbit by the configuration blocks. Also used for checking if the configuration blocks only
     *     contain existing pins (variable existingPins)
     * @param defaultProps The default properties to get the pins from the calliope/microbit.
     * @param mapCorrectConfigPins There are a few configuration blocks that either have no property to get the pins or the pins aren't mapped correctly so this
     *     hashmap maps them to their specific pins, so they can be checked.
     */
    public MbedValidatorAndCollectorWorker(List<String> freePins, List<String> defaultProps, HashMap<String, String> mapCorrectConfigPins) {
        this.freePins = Collections.unmodifiableList(freePins);
        this.defaultProperties = Collections.unmodifiableList(defaultProps);
        this.existingPins = Collections.unmodifiableList(freePins);
        this.mapCorrectConfigPins = mapCorrectConfigPins;
    }

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(MicrobitMethods.class);
    }

    protected boolean isDisplaySwitchUsed(Project project) {
        for ( List<Phrase> subTree : project.getProgramAst().getTree() ) {
            BlocklyProperties blocklyProperties = subTree.get(1).getProperty();
            if ( blocklyProperties.blocklyRegion.inTask && subTree.toString().contains("SwitchLedMatrix") ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(Project project) {
        MbedConfigurationValidatorWorker mbedConfigurationValidatorWorker = new MbedConfigurationValidatorWorker(project);
        mbedConfigurationValidatorWorker.validateConfiguration(freePins, defaultProperties, existingPins, mapCorrectConfigPins);
        super.execute(project);
    }
}
