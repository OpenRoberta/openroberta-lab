package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.Collections;
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

public class UsedHardwareBean {

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
    private boolean isTimerSensorUsed = false;
    private Set<String> usedImages = new HashSet<>();
    private boolean isSayTextUsed = false;

    //TODO: from MbedUsedHardwareVisitor, needs refactoring
    private boolean radioUsed;
    private boolean accelerometerUsed;
    private boolean greyScale;
    private boolean fourDigitDisplayUsed;
    private boolean ledBarUsed;
    private boolean humidityUsed;
    private boolean calliBotUsed;

    private final Set<Enum<?>> usedMethods = new HashSet<>(); //All needed helper methods as a Set

    //TODO: from NXT
    private boolean isVolumeVariableNeeded;

    //TODO: edison nicer
    public enum EdisonMethods {
        OBSTACLEDETECTION, //Obstacle detection
        IRSEND, //IR sender
        IRSEEK, //IR seeker
        MOTORON, //Motor on / motor on for... block
        SHORTEN, //shorten a number for Edisons drive() methods
        GETDIR, //reverse direction when negative speed is applied
        DIFFCURVE, //for the steer block
        DIFFDRIVE, //for driving
        DIFFTURN, //for turning
    }

    public List<String> getGlobalVariables() {
        return this.globalVariables;
    }

    public List<String> getDeclaredVariables() {
        return this.declaredVariables;
    }

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

    public boolean isTimerSensorUsed() {
        return this.isTimerSensorUsed;
    }

    public Set<String> getUsedImages() {
        return this.usedImages;
    }

    public boolean isSayTextUsed() {
        return this.isSayTextUsed;
    }

    public boolean isRadioUsed() {
        return this.radioUsed;
    }

    public boolean isAccelerometerUsed() {
        return this.accelerometerUsed;
    }

    public boolean isGreyScale() {
        return this.greyScale;
    }

    public boolean isFourDigitDisplayUsed() {
        return this.fourDigitDisplayUsed;
    }

    public boolean isLedBarUsed() {
        return this.ledBarUsed;
    }

    public boolean isHumidityUsed() {
        return this.humidityUsed;
    }

    public boolean isCalliBotUsed() {
        return this.calliBotUsed;
    }

    public Set<Enum<?>> getUsedMethods() {
        return Collections.unmodifiableSet(this.usedMethods);
    }

    public Map<Integer, Boolean> getLoopsLabelContainer() {
        return loopsLabelContainer;
    }

    public boolean isVolumeVariableNeeded() {
        return isVolumeVariableNeeded;
    }

    public static class Builder {
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

        public Builder setTimerSensorUsed(boolean isTimerSensorUsed) {
            this.usedHardwareBean.isTimerSensorUsed = isTimerSensorUsed;
            return this;
        }

        public Builder addUsedImage(String usedImage) {
            this.usedHardwareBean.usedImages.add(usedImage);
            return this;
        }

        public Builder setSayTextUsed(boolean isSayTextUsed) {
            this.usedHardwareBean.isSayTextUsed = isSayTextUsed;
            return this;
        }

        public Builder setRadioUsed(boolean radioUsed) {
            this.usedHardwareBean.radioUsed = radioUsed;
            return this;
        }

        public Builder setAccelerometerUsed(boolean accelerometerUsed) {
            this.usedHardwareBean.accelerometerUsed = accelerometerUsed;
            return this;
        }

        public Builder setGreyScale(boolean greyScale) {
            this.usedHardwareBean.greyScale = greyScale;
            return this;
        }

        public Builder setFourDigitDisplayUsed(boolean fourDigitDisplayUsed) {
            this.usedHardwareBean.fourDigitDisplayUsed = fourDigitDisplayUsed;
            return this;
        }

        public Builder setLedBarUsed(boolean ledBarUsed) {
            this.usedHardwareBean.ledBarUsed = ledBarUsed;
            return this;
        }

        public Builder setHumidityUsed(boolean humidityUsed) {
            this.usedHardwareBean.humidityUsed = humidityUsed;
            return this;
        }

        public Builder setCalliBotUsed(boolean calliBotUsed) {
            this.usedHardwareBean.calliBotUsed = calliBotUsed;
            return this;
        }

        public Builder addUsedMethod(Enum<?> usedMethod) {
            this.usedHardwareBean.usedMethods.add(usedMethod);
            return this;
        }

        public Builder putLoopLabel(int loop, boolean isInWait) {
            this.usedHardwareBean.loopsLabelContainer.put(loop, isInWait);
            return this;
        }

        public Builder setVolumeVariableNeeded(boolean isVolumeVariableNeeded) {
            this.usedHardwareBean.isVolumeVariableNeeded = isVolumeVariableNeeded;
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
