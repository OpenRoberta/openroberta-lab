package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedImport;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.ast.AstFactory;

/**
 * Container for all used hardware related information, used in for example code generation. Currently used for more than just used hardware, should be split up
 * into multiple separate beans in the future.
 */
//TODO change name of class, no longer only used hardware
//TODO move unrelated data to specific beans. Refactor fields from Mbed into usedActors/Sensors
public class UsedHardwareBean implements IProjectBean {

    private List<String> inScopeVariables = new ArrayList<>();
    private List<String> globalVariables = new ArrayList<>();
    private Map<String, BlocklyType> declaredVariables = new HashMap<>();
    private List<VarDeclaration> visitedVars = new ArrayList<>();
    private List<Method> userDefinedMethods = new ArrayList<>();
    private Set<String> markedVariablesAsGlobal = new HashSet<>();
    private boolean isProgramEmpty = false;
    private boolean isListsUsed = false;
    private boolean isNNBlockUsed = false;

    private Map<Integer, Boolean> loopsLabelContainer = new HashMap<>();

    private Set<UsedSensor> usedSensors = new LinkedHashSet<>();
    private Set<UsedActor> usedActors = new LinkedHashSet<>();
    private Set<String> usedImages = new HashSet<>();
    private Map<String, String[][]> usedIDImages = new HashMap<>();
    private Set<UsedImport> usedImports = new LinkedHashSet<>();
    private Map<String, String> lockedComponent = new HashMap<>();

    public List<VarDeclaration> getVisitedVars() {
        return this.visitedVars;
    }

    public List<Method> getUserDefinedMethods() {
        return this.userDefinedMethods;
    }

    public Set<String> getDeclaredVariables() {
        return this.declaredVariables.keySet();
    }

    public Set<String> getMarkedVariablesAsGlobal() {
        return this.markedVariablesAsGlobal;
    }

    public BlocklyType getTypeOfDeclaredVariable(String variableName) {
        BlocklyType type = this.declaredVariables.get(variableName);
        return type == null ? BlocklyType.NOTHING : type;
    }

    public Sig getSignatureOfMethod(String methodName) {
        Method method = null;
        for ( Method m : this.userDefinedMethods ) {
            if ( m.getMethodName().equals(methodName) ) {
                method = m;
                break;
            }
        }
        if ( method == null ) {
            return Sig.of(BlocklyType.NOTHING);
        } else {
            List<BlocklyType> parameterTypes = new ArrayList<>();
            for ( Expr e : method.getParameters().el ) {
                if ( e instanceof VarDeclaration ) {
                    VarDeclaration decl = (VarDeclaration) e;
                    parameterTypes.add(decl.getBlocklyType());
                } else {
                    throw new DbcException("invalid declaration of method " + methodName);
                }
            }
            return Sig.ofParamList(method.getReturnType(), parameterTypes);
        }
    }

    public boolean isProgramEmpty() {
        return this.isProgramEmpty;
    }

    public boolean isListsUsed() {
        return this.isListsUsed;
    }

    public boolean isNNBlockUsed() {
        return this.isNNBlockUsed;
    }

    public Set<UsedImport> getUsedImports() {
        return this.usedImports;
    }

    public Map<String, String> getLockedComponent() { return this.lockedComponent; }

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

    public boolean isImportUsed(String type) {
        return this.usedImports.stream().anyMatch(usedImport -> usedImport.getType().equals(type));
    }

    public Map<Integer, Boolean> getLoopsLabelContainer() {
        return this.loopsLabelContainer;
    }

    public static class Builder implements IBuilder<UsedHardwareBean> {
        private final UsedHardwareBean usedHardwareBean = new UsedHardwareBean();

        public Builder addGlobalVariable(String globalVariable) {
            this.usedHardwareBean.globalVariables.add(globalVariable);
            return this;
        }

        public Builder addDeclaredVariable(String declaredVariable, BlocklyType type) {
            this.usedHardwareBean.declaredVariables.put(declaredVariable, type);
            return this;
        }

        public Builder addLockedComponent(String component, String port) {
            this.usedHardwareBean.lockedComponent.put(component, port);
            return this;
        }

        public Builder addVisitedVariable(VarDeclaration visitedVariable) {
            this.usedHardwareBean.visitedVars.add(visitedVariable);
            return this;
        }

        public Builder addInScopeVariable(String inScopeVariable) {
            this.usedHardwareBean.inScopeVariables.add(inScopeVariable);
            return this;
        }

        public Builder addUserDefinedMethod(Method userDefinedMethod) {
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

        public Builder setNNBlockUsed(boolean isNNBlockUsed) {
            this.usedHardwareBean.isNNBlockUsed = isNNBlockUsed;
            return this;
        }

        public Builder addUsedImport(UsedImport usedImport) {
            this.usedHardwareBean.usedImports.add(usedImport);
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

        public boolean containsInScopeVariable(String variableName) {
            return this.usedHardwareBean.inScopeVariables.contains(variableName);
        }

        public boolean removeInScopeVariable(String inScopeVariable) {
            return this.usedHardwareBean.inScopeVariables.remove(inScopeVariable);
        }

        public boolean containsGlobalVariable(String variableName) {
            return this.usedHardwareBean.globalVariables.contains(variableName);
        }

        @Override
        public UsedHardwareBean build() {
            return this.usedHardwareBean;
        }
    }
}
