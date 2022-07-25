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
    private final List<Integer> networkShape;

    public NNBean(List<String> inputNeurons, List<String> outputNeurons, JSONArray weights, JSONArray biases, List<Integer> networkShape) {
        this.inputNeurons = inputNeurons;
        this.outputNeurons = outputNeurons;
        this.weights = weights;
        this.biases = biases;
        this.networkShape = networkShape;
    }

    public List<String> getInputNeurons() {
        return inputNeurons;
    }

    public List<String> getOutputNeurons() {
        return outputNeurons;
    }

    public List<Integer> getNetworkShape() {
        return networkShape;
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
        private List<String> inputNeurons = new ArrayList<>();
        private List<String> outputNeurons = new ArrayList<>();
        private List<Integer> networkShape = new ArrayList<>();

        /**
         * data contains the NN definition. Save it for later usage.
         */
        public String setNN(Data data) {
            if ( data == null ) {
                this.weights = new JSONArray();
                this.bias = new JSONArray();
                return null;
            } else {
                JSONObject netStateDefinition = new JSONObject(data.getValue());
                this.weights = netStateDefinition.getJSONArray("weights");
                this.bias = netStateDefinition.getJSONArray("biases");
                netStateDefinition.getJSONArray("inputs").forEach(i -> inputNeurons.add((String) i));
                netStateDefinition.getJSONArray("outputs").forEach(o -> outputNeurons.add((String) o));
                netStateDefinition.getJSONArray("networkShape").forEach(c -> networkShape.add((Integer) c));
                if ( !neuronNamesConsistent(inputNeurons, outputNeurons) ) {
                    return "NN_INVALID_NEURONNAME";
                } else {
                    return null;
                }
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

        @Override
        public NNBean build() {
            return new NNBean(inputNeurons, outputNeurons, weights, bias, networkShape);
        }
    }
}
