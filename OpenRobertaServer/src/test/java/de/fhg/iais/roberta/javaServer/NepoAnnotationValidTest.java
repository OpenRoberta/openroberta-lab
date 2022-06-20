package de.fhg.iais.roberta.javaServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;

@RunWith(Parameterized.class)
public class NepoAnnotationValidTest {
    private static final JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
    private static final JSONObject robots = testSpecification.getJSONObject("robots");

    private final Class<?> nepoAnnotatedClass;

    public NepoAnnotationValidTest(Class<?> nepoAnnotatedClass) {
        this.nepoAnnotatedClass = nepoAnnotatedClass;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() throws IOException {
        // Initialise all Robot Factories, so all AST classes are in BlockTypeContainer
        robots.keySet().forEach(robotName -> Util.configureRobotPlugin(robotName, "", "", new ArrayList<>()));

        return AstFactory.getAstClasses()
            .stream()
            .filter(AnnotationHelper::isNepoAnnotatedClass)
            .sorted(Comparator.comparing(Class::getSimpleName))
            .map(astClass -> new Object[] {astClass})
            .collect(Collectors.toList());

    }

    @Test
    public void isValidNepoAnnotatedClass() {
        Assertions.assertThatCode(() -> AnnotationHelper.checkNepoAnnotatedClass(nepoAnnotatedClass)).doesNotThrowAnyException();
    }
}
