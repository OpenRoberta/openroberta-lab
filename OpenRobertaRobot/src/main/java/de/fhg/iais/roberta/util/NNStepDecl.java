package de.fhg.iais.roberta.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class NNStepDecl {
    private List<String> inputNeurons = new ArrayList<>();
    private List<String> outputNeurons = new ArrayList<>();
    private JSONArray weights;
    private JSONArray biases;

    public NNStepDecl(JSONArray weights, JSONArray biases) {
        this.weights = weights;
        this.biases = biases;
    }

    public void addInputNeuron(String inputNeuronName) {
        inputNeurons.add(inputNeuronName);
    }

    public void addOutputNeuron(String outputNeuronName) {
        outputNeurons.add(outputNeuronName);
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
}
