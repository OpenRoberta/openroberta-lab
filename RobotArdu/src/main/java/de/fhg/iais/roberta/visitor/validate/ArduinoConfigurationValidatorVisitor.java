package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ArduinoConfigurationValidatorVisitor extends AbstractConfigurationValidatorVisitor {

    private List<String> freePins = Stream.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13").collect(Collectors.toList());

    int errorCount;

    public ArduinoConfigurationValidatorVisitor(Configuration configuration) {
        super(configuration);
    }

    public void checkConfigurationBlock(Map<String, String> componentProperties, /*Map<String, List<String>> inputToPinsMapping,*/ Phrase<Void> block) {
        List<String> blockPins = new ArrayList<>();
        componentProperties
            .forEach(
                (k, v) -> {
                    //if ( inputToPinsMapping.containsKey(k) ) {
                    //List<String> allowedPins = inputToPinsMapping.get(k);
                    if ( /*!(allowedPins.contains(v) &&*/ !freePins.contains(v) ) {
                        System.err.println("Pin " + v + " is not allowed for " + k + " input/output");
                        //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        errorCount++;
                        throw new DbcException("Pin " + v + " is not allowed for " + k + " input/output");
                    } else {
                        blockPins.add(v);
                        freePins.removeIf(s -> s.equals(v));
                    }
                    /*} else {
                        System.err.println("Input not allowed " + k);
                        block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        errorCount++;
                    }*/
                });
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            System.err.println("Pins must be unique");
            //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            errorCount++;
            throw new DbcException("Pins must be unique");
        }
    }

    @Override
    public void checkConfiguration() {
        robotConfiguration.getConfigurationComponentsValues().forEach(v -> {
            checkConfigurationBlock(v.getComponentProperties(), /*inputToPinsMapping,*/ null);
        });
    }
}
