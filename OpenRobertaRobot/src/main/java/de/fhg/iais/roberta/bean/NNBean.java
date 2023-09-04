package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Integer numHiddenLayers;
    private final Float learningRate;
    private final Float regularizationRate;
    private final Float noise;
    private final Integer batchSize;
    private final Boolean discretize;
    private final Float percTrainData;
    private final String activationKey;
    private final List<List<String>> hiddenNeurons;

    public NNBean(
        List<String> inputNeurons, List<String> outputNeurons, JSONArray weights, JSONArray biases, List<Integer> networkShape,
        Integer numHiddenLayers, Float learningRate,
        Float regularizationRate,
        Float noise,
        Integer batchSize,
        Boolean discretize,
        Float percTrainData,
        String activationKey, List<List<String>> hiddenNeurons) {
        this.inputNeurons = inputNeurons;
        this.outputNeurons = outputNeurons;
        this.weights = weights;
        this.biases = biases;
        this.networkShape = networkShape;
        this.numHiddenLayers = numHiddenLayers;
        this.learningRate = learningRate;
        this.regularizationRate = regularizationRate;
        this.noise = noise;
        this.batchSize = batchSize;
        this.discretize = discretize;
        this.percTrainData = percTrainData;
        this.activationKey = activationKey;
        this.hiddenNeurons = hiddenNeurons;
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

    public Float getLearningRate() {
        return learningRate;
    }

    public Float getRegularizationRate() {
        return regularizationRate;
    }

    public Float getNoise() {
        return noise;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public Boolean getDiscretize() {
        return discretize;
    }

    public Float getPercTrainData() {
        return percTrainData;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public Integer getNumHiddenLayers() {
        return numHiddenLayers;
    }

    public boolean hasAtLeastOneInputAndOutputNeuron() {
        return inputNeurons.size() > 0 && outputNeurons.size() > 0;
    }

    public List<String> getAllHiddenNeurons() {
        return hiddenNeurons.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<String> getHiddenNeuronsByLayer(int layerIdx) {
        return hiddenNeurons.get(layerIdx);
    }

    public static class Builder implements IBuilder<NNBean> {
        private JSONArray weights = null;
        private JSONArray bias = null;
        private List<String> inputNeurons = new ArrayList<>();
        private List<String> outputNeurons = new ArrayList<>();
        private List<Integer> networkShape = new ArrayList<>();
        private Float learningRate = null;
        private Float regularizationRate = null;
        private Float noise = null;
        private Integer batchSize = null;
        private Boolean discretize = null;
        private Float percTrainData = null;
        private String activationKey = null;
        private Integer numHiddenLayers = null;
        private List<List<String>> hiddenNeurons = new ArrayList<>();

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
                netStateDefinition.getJSONArray("hiddenNeurons").forEach(h -> {
                    List<String> hiddenLayerNeurons = new ArrayList<>();
                    ((JSONArray) h).forEach(neuron -> hiddenLayerNeurons.add((String) neuron));
                    hiddenNeurons.add(hiddenLayerNeurons);
                });
                netStateDefinition.getJSONArray("outputs").forEach(o -> outputNeurons.add((String) o));
                netStateDefinition.getJSONArray("networkShape").forEach(c -> networkShape.add((Integer) c));
                this.learningRate = netStateDefinition.getFloat("learningRate");
                this.regularizationRate = netStateDefinition.getFloat("regularizationRate");
                this.noise = netStateDefinition.getFloat("noise");
                this.batchSize = netStateDefinition.getInt("batchSize");
                this.discretize = netStateDefinition.getBoolean("discretize");
                this.percTrainData = netStateDefinition.getFloat("percTrainData");
                this.activationKey = netStateDefinition.getString("activationKey");
                this.numHiddenLayers = netStateDefinition.getInt("numHiddenLayers");
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
            return new NNBean(inputNeurons, outputNeurons, weights, bias, networkShape, numHiddenLayers, learningRate, regularizationRate, noise, batchSize, discretize, percTrainData, activationKey, hiddenNeurons);
        }
    }
}
