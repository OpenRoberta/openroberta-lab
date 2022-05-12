package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.util.NNStepDecl;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class NNBean implements IProjectBean {

    private NNStepDecl nnStepDecl;


    public static class Builder implements IBuilder<NNBean> {
        private final NNBean nnBean = new NNBean();

        // temporary to check NNStep consistency
        private boolean nnStepWithoutNetWasUsed = false;
        private List<String> inputNeurons = new ArrayList<>();
        private List<String> outputNeurons = new ArrayList<>();
        private Set<String> theseOutputNeuronNamesMustExist = new HashSet<>();

        /**
         * save the data of a NNStep block and check consistency of input and output neurons
         *
         * @param withNNDecl was a net declaration part of the block?
         * @param nnStepDecl the declaration including the neurons
         * @return null, if everything is ok, a string was an error message key otherwise
         */
        public String addNNStepDeclAndCheckConsistency(boolean withNNDecl, NNStepDecl nnStepDecl) {
            if ( this.nnBean.nnStepDecl != null && withNNDecl ) {
                throw new DbcException("multiple NN definitions found. Frontend inconsistency.");
            }
            if ( !neuronNamesConsistent(nnStepDecl.getInputNeurons()) || !neuronNamesConsistent(nnStepDecl.getOutputNeurons()) ) {
                return "NN_STEP_INCONSISTENT";
            }
            if ( withNNDecl ) {
                // a net declaration is encountered
                this.nnBean.nnStepDecl = nnStepDecl;

                if ( nnStepWithoutNetWasUsed ) {
                    // check, that previous NNStep blocks without net declaration match
                    if ( !neuronsMatch(inputNeurons, nnStepDecl.getInputNeurons()) || !neuronsMatch(outputNeurons, nnStepDecl.getOutputNeurons()) ) {
                        return "NN_STEPS_INCONSISTENT";
                    }
                }
                return checkNameOfOutputNeuron(this.nnBean.nnStepDecl.getOutputNeurons(), theseOutputNeuronNamesMustExist);
            } else {
                // no net declaration encountered
                if ( nnStepWithoutNetWasUsed ) {
                    // a second NNStep block without net declaration is encountered
                    if ( !neuronsMatch(inputNeurons, nnStepDecl.getInputNeurons()) || !neuronsMatch(outputNeurons, nnStepDecl.getOutputNeurons()) ) {
                        return "NN_STEPS_INCONSISTENT";
                    } else {
                        return null;
                    }
                } else {
                    // the first NNStep block without net declaration is encountered
                    nnStepWithoutNetWasUsed = true;
                    inputNeurons = nnStepDecl.getInputNeurons();
                    outputNeurons = nnStepDecl.getOutputNeurons();
                    return null;
                }
            }
        }

        public String checkNameOfOutputNeuron(String name) {
            theseOutputNeuronNamesMustExist.add(name);
            if ( nnBean.nnStepDecl != null ) {
                return checkNameOfOutputNeuron(nnBean.nnStepDecl.getOutputNeurons(), theseOutputNeuronNamesMustExist);
            } else {
                return null;
            }
        }

        private String checkNameOfOutputNeuron(List<String> existing, Set<String> shouldExist) {
            for ( String e : shouldExist ) {
                if ( !existing.contains(e) ) {
                    return "NN_OUTPUT_NEURON_MISSING";
                }
            }
            return null;
        }

        private boolean neuronNamesConsistent(List<String> list) {
            Set<String> set = new HashSet<>(list);
            for ( String s : set ) {
                if ( s.isEmpty() || !Util.isValidJavaIdentifier(s) ) {
                    return false;
                }
            }
            return list.size() == set.size();
        }

        private boolean neuronsMatch(List<String> l1, List<String> l2) {
            return l1.equals(l2);
        }

        @Override
        public NNBean build() {
            return nnBean;
        }
    }

    public NNStepDecl getNnStepDecl() {
        return nnStepDecl;
    }
}
