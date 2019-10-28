package de.fhg.iais.roberta.syntax.programcheck.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.validate.Ev3BrickValidatorWorker;

public class Ev3RobProgramCheckVisitorTest extends Ev3LejosAstTest {

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        Project.Builder builder1 = UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/visitors/MoveWithZeroSpeed.xml"));

        builder1.setConfigurationAst(makeStandard());
        Ev3BrickValidatorWorker worker = new Ev3BrickValidatorWorker();
        Project project = builder1.build();
        worker.execute(project);
        //TODO implement warning counter
        //Assert.assertEquals(2, project.getWarningCount());

    }
}
