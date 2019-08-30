package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class RaspberryPiConfigurationValidatorVisitor extends AbstractConfigurationValidatorVisitor {

    private String incorrectPin;
    private String failingBlock;

    private boolean checkSuccess = false;

    int errorCount;

    public RaspberryPiConfigurationValidatorVisitor(Configuration configuration) {
        super(configuration);
    }

    public void checkConfigurationBlock(Map<String, String> componentProperties, String blockType) {
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
            checkConfigurationBlock(v.getComponentProperties(), v.getComponentType());
        });
    }

    @Override
    public void validate() {
        checkConfiguration();
    }

    @Override
    public Map<String, String> getResult() {
        Map<String, String> result = new HashMap<>();
        result.put("BLOCK", getFailingBlock());
        result.put("PIN", getIncorrectPin());
        return result;
    }
}
