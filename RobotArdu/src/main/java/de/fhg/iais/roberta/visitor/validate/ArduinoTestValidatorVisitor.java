package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ArduinoTestValidatorVisitor extends AbstractConfigurationValidatorVisitor {

    private final List<String> freePins =
        Stream.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "A0", "A1", "A2", "A3", "A4", "A5").collect(Collectors.toList());

    private String incorrectPin;
    private String failingBlock;

    private boolean checkSuccess = false;

    int errorCount;

    public ArduinoTestValidatorVisitor(Configuration configuration) {
        super(configuration);
    }

    public void checkConfigurationBlock(Map<String, String> componentProperties, /*Map<String, List<String>> inputToPinsMapping,*/ String blockType) {
        List<String> blockPins = new ArrayList<>();
        componentProperties
            .forEach(
                (k, v) -> {
                    //if ( inputToPinsMapping.containsKey(k) ) {
                    //List<String> allowedPins = inputToPinsMapping.get(k);
                    if ( /*!(allowedPins.contains(v) &&*/ !this.freePins.contains(v) ) {
                        //System.err.println("Pin " + v + " is not allowed for " + k + " input/output");
                        //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        this.errorCount++;
                        this.incorrectPin = v;
                        this.failingBlock = blockType;
                        throw new DbcException("Pin " + v + " is not allowed for " + k + " input/output");
                    } else {
                        blockPins.add(v);
                        this.freePins.removeIf(s -> s.equals(v));
                    }
                    /*} else {
                        System.err.println("Input not allowed " + k);
                        block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        errorCount++;
                    }*/
                });
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            //System.err.println("Pins must be unique");
            //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
            this.incorrectPin = "NON_UNIQUE";
            throw new DbcException("Pins must be unique");
        }
        this.checkSuccess = true;
    }

    @Override
    public String getFailingBlock() {
        return this.failingBlock;
    }

    @Override
    public String getIncorrectPin() {
        return this.incorrectPin;
    }

    @Override
    public void checkConfiguration() {
        this.robotConfiguration.getConfigurationComponentsValues().forEach(v -> {
            checkConfigurationBlock(v.getComponentProperties(), /*inputToPinsMapping,*/ v.getComponentType());
        });
    }

    @Override
    public void validate() {
        checkConfiguration();
    }

    @Override
    public Map<String, String> getResult() {
        Map<String, String> result = new HashMap<>();
        result.put("TESTKEY", getFailingBlock());
        result.put("TESTVALUE", getIncorrectPin());
        return result;
    }
}
