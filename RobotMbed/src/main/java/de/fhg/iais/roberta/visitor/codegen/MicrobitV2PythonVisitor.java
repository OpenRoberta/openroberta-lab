package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;

public class MicrobitV2PythonVisitor extends MbedV2PythonVisitor implements IMicrobitV2Visitor<Void> {
    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param robotConfiguration
     * @param beans
     */
    public MicrobitV2PythonVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, robotConfiguration, "microbit", beans);
    }

}
