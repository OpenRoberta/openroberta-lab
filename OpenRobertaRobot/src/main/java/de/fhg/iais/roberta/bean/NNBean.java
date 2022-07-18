package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.util.Util;

public class NNBean implements IProjectBean {

    private final List<String> inputNeurons;
    private final List<String> outputNeurons;
    private final JSONArray weights;
    private final JSONArray biases;

    public NNBean(List<String> inputNeurons, List<String> outputNeurons, JSONArray weights, JSONArray biases) {
        this.inputNeurons = inputNeurons;
        this.outputNeurons = outputNeurons;
        this.weights = weights;
        this.biases = biases;
    }

    public List<String> getInputNeurons() {
        return inputNeurons;
    }

    public List<String> getOutputNeurons() {
        return outputNeurons;
    }

    public JSONArray getWeights() {
        return weights;
    }

    public JSONArray getBiases() {
        return biases;
    }

    public boolean hasAtLeastOneInputAndOutputNeuron() {
        return inputNeurons.size() > 0 && outputNeurons.size() > 0;
    }

    public static class Builder implements IBuilder<NNBean> {
        private JSONArray weights = null;
        private JSONArray bias = null;
        private boolean nnStepWasFound = false;
        private List<String> inputNeurons = new ArrayList<>();
        private List<String> outputNeurons = new ArrayList<>();

        /**
         * data contains the NN definition. Save it for later usage.
         */
        public void setNN(Data data) {
            if ( data == null ) {
                this.weights = new JSONArray();
                this.bias = new JSONArray();
            } else {
                JSONObject netStateDefinition = new JSONObject(data.getValue());
                this.weights = netStateDefinition.getJSONArray("weights");
                this.bias = netStateDefinition.getJSONArray("biases");
            }
        }

        public String processInputOutputNeurons(List<String> inputNeurons, List<String> outputNeurons) {
            if ( inputNeurons.size() <= 0 || outputNeurons.size() <= 0 ) {
                return "NN_STEP_INPUTOUTPUT_MISSING";
            }
            if ( !neuronNamesConsistent(inputNeurons, outputNeurons) ) {
                return "NN_STEP_INCONSISTENT";
            }
            if ( nnStepWasFound ) {
                // check, that input/output of previous NNStep blocks match
                if ( !neuronsMatch(this.inputNeurons, inputNeurons) || !neuronsMatch(this.outputNeurons, outputNeurons) ) {
                    return "NN_STEPS_INCONSISTENT";
                }
            } else {
                nnStepWasFound = true;
                this.inputNeurons = inputNeurons;
                this.outputNeurons = outputNeurons;
            }
            if ( this.weights.length() < 2 || this.bias.length() < 2 ) {
                return "NN_INSPECT_NN";
            }
            if ( this.inputNeurons.size() != this.weights.getJSONArray(0).length() ) {
                return "NN_INSPECT_NN";
            }
            if ( this.outputNeurons.size() != this.weights.getJSONArray(this.weights.length() - 1).length() ) {
                return "NN_INSPECT_NN";
            }
            if ( this.outputNeurons.size() != this.bias.getJSONArray(this.bias.length() - 1).length() ) {
                return "NN_INSPECT_NN";
            }
            return null;
        }

        public String checkNameOfOutputNeuron(String name) {
            if ( outputNeurons.contains(name) ) {
                return null;
            } else {
                return "NN_INVALID_NEURONNAME";
            }
        }

        private boolean neuronNamesConsistent(List<String> l1, List<String> l2) {
            Set<String> set = new HashSet<>(l1);
            set.addAll(l2);
            for ( String s : set ) {
                if ( s.isEmpty() || !Util.isValidJavaIdentifier(s) ) {
                    return false;
                }
            }
            return l1.size() + l2.size() == set.size();
        }

        private boolean neuronsMatch(List<String> l1, List<String> l2) {
            return l1.equals(l2);
        }

        @Override
        public NNBean build() {
            return new NNBean(inputNeurons, outputNeurons, weights, bias);
        }
    }
}
