package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;

public class NaoPythonSimVisitorTest {

    private NaoPythonSimVisitor setupPythonSimVisitor(List<List<Phrase<Void>>> phrases) {
        ClassToInstanceMap<IProjectBean> beans =
            ImmutableClassToInstanceMap
                .<IProjectBean> builder()
                .build();

        return Mockito.spy(new NaoPythonSimVisitor(phrases, Language.ENGLISH, beans));
    }

    @Test
    public void basicSetup() {
        List<List<Phrase<Void>>> phrases = Arrays.asList(Collections.emptyList(), Collections.emptyList());
        NaoPythonSimVisitor naoPythonSimVisitor = setupPythonSimVisitor(phrases);
        naoPythonSimVisitor.generateCode(true);

        String sourceCode = naoPythonSimVisitor.getSb().toString();
        System.out.println(sourceCode);
    }
}