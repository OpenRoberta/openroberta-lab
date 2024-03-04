package de.fhg.iais.roberta.worker.validate;

import java.util.HashMap;
import java.util.List;

public class CalliopeV3ValidatorAndCollectorWorker extends MbedValidatorAndCollectorWorker{
    /**
     * @param freePins All the pins that can be used on the calliope/microbit by the configuration blocks. Also used for checking if the configuration blocks only
     *     contain existing pins (variable existingPins)
     * @param defaultProps The default properties to get the pins from the calliope/microbit.
     * @param mapCorrectConfigPins There are a few configuration blocks that either have no property to get the pins or the pins aren't mapped correctly so this
     *     hashmap maps them to their specific pins, so they can be checked.
     */
    public CalliopeV3ValidatorAndCollectorWorker(
        List<String> freePins,
        List<String> defaultProps,
        HashMap<String, String> mapCorrectConfigPins) {
        super(freePins, defaultProps, mapCorrectConfigPins);
    }
}
