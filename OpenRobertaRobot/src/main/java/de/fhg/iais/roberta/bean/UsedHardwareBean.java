package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.methods.Method;

/**
 * Container for all used hardware related information, used in for example code generation.
 * Currently used for more than just used hardware, should be split up into multiple separate beans in the future.
 */
//TODO move unrelated data to specific beans. Refactor fields from Mbed into usedActors/Sensors
public class UsedHardwareBean implements IProjectBean {

    private List<String> globalVariables = new ArrayList<>();
    private List<String> declaredVariables = new ArrayList<>();
    private List<VarDeclaration<Void>> visitedVars = new ArrayList<>();
    private List<Method<Void>> userDefinedMethods = new ArrayList<>();
    private Set<String> markedVariablesAsGlobal = new HashSet<>();
    private boolean isProgramEmpty = false;
    private boolean isListsUsed = false;

    private Map<Integer, Boolean> loopsLabelContainer = new HashMap<>();

    private Set<UsedSensor> usedSensors = new LinkedHashSet<>();
    private Set<UsedActor> usedActors = new LinkedHashSet<>();
    private Set<String> usedImages = new HashSet<>();
    private Map<String, String[][]> usedIDImages = new HashMap<>();

    public List<VarDeclaration<Void>> getVisitedVars() {
        return this.visitedVars;
    }

    public List<Method<Void>> getUserDefinedMethods() {
        return this.userDefinedMethods;
    }

    public Set<String> getMarkedVariablesAsGlobal() {
        return this.markedVariablesAsGlobal;
    }

    public boolean isProgramEmpty() {
        return this.isProgramEmpty;
    }

    public boolean isListsUsed() {
        return this.isListsUsed;
    }

    public Set<UsedSensor> getUsedSensors() {
        return this.usedSensors;
    }

    public Set<UsedActor> getUsedActors() {
        return this.usedActors;
    }

    public Set<String> getUsedImages() {
        return this.usedImages;
    }
    
    public Map<String, String[][]> getUsedIDImages() {
        return this.usedIDImages;
    }

    public boolean isSensorUsed(String type) {
        return this.usedSensors.stream().anyMatch(usedSensor -> usedSensor.getType().equals(type));
    }

    public boolean isActorUsed(String type) {
        return this.usedActors.stream().anyMatch(usedActor -> usedActor.getType().equals(type));
    }

    public Map<Integer, Boolean> getLoopsLabelContainer() {
        return loopsLabelContainer;
    }

    public static class Builder implements IBuilder<UsedHardwareBean> {
        private final UsedHardwareBean usedHardwareBean = new UsedHardwareBean();

        public Builder addGlobalVariable(String globalVariable) {
            this.usedHardwareBean.globalVariables.add(globalVariable);
            return this;
        }

        public Builder addDeclaredVariable(String declaredVariable) {
            this.usedHardwareBean.declaredVariables.add(declaredVariable);
            return this;
        }

        public Builder addVisitedVariable(VarDeclaration<Void> visitedVariable) {
            this.usedHardwareBean.visitedVars.add(visitedVariable);
            return this;
        }

        public Builder addUserDefinedMethod(Method<Void> userDefinedMethod) {
            this.usedHardwareBean.userDefinedMethods.add(userDefinedMethod);
            return this;
        }

        public Builder addMarkedVariableAsGlobal(String markedVariableAsGlobal) {
            this.usedHardwareBean.markedVariablesAsGlobal.add(markedVariableAsGlobal);
            return this;
        }

        public Builder setProgramEmpty(boolean isProgramEmpty) {
            this.usedHardwareBean.isProgramEmpty = isProgramEmpty;
            return this;
        }

        public Builder setListsUsed(boolean isListsUsed) {
            this.usedHardwareBean.isListsUsed = isListsUsed;
            return this;
        }

        public Builder addUsedSensor(UsedSensor usedSensor) {
            this.usedHardwareBean.usedSensors.add(usedSensor);
            return this;
        }

        public Builder addUsedActor(UsedActor usedActor) {
            this.usedHardwareBean.usedActors.add(usedActor);
            return this;
        }

        public Builder addUsedImage(String usedImage) {
            this.usedHardwareBean.usedImages.add(usedImage);
            return this;
        }
        
        public Builder addUsedIDImage(String id, String[][] usedImage) {
            this.usedHardwareBean.usedIDImages.put(id, usedImage);
            return this;
        }

        public Builder putLoopLabel(int loop, boolean isInWait) {
            this.usedHardwareBean.loopsLabelContainer.put(loop, isInWait);
            return this;
        }

        @Deprecated
        public boolean containsGlobalVariable(String variableName) {
            return this.usedHardwareBean.globalVariables.contains(variableName);
        }

        @Deprecated
        public boolean containsDeclaredVariable(String variableName) {
            return this.usedHardwareBean.declaredVariables.contains(variableName);
        }

        public UsedHardwareBean build() {
            return this.usedHardwareBean;
        }
    }
}
