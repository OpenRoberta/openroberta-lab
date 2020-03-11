package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class CalliopeValidatorVisitor extends MbedBoardValidatorVisitor {

    public CalliopeValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            indexOfFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitIndexOfFunct(indexOfFunct);
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            listGetIndex.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitListGetIndex(listGetIndex);
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            listSetIndex.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitListSetIndex(listSetIndex);
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            lengthOfIsEmptyFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitLengthOfIsEmptyFunct(lengthOfIsEmptyFunct);
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            mathOnListFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        if ( getSubFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            getSubFunct.addInfo(NepoInfo.error("BLOCK_USED_INCORRECTLY"));
            this.errorCount++;
        }
        return super.visitGetSubFunct(getSubFunct);
    }
}
