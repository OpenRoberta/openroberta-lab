package de.fhg.iais.roberta.transformer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;
import org.xmlunit.builder.Input;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnnotationHelperTest {

    private static final List<Class<?>> VALID_TEST_PHRASES = Arrays.asList(TestPhrase.class, TestPhraseField.class, TestPhraseWithAll.class, TestPhraseFieldBoolean.class, TestPhraseFieldDouble.class, TestPhraseFieldEnum.class, TestOperation.class);

    private Jaxb2ProgramAst<?> jaxb2ProgramAst;

    @BeforeClass
    public static void setupPhrases() {
        BlockTypeContainer.add("TEST_PHRASE", Category.ACTOR, TestPhrase.class, "TEST_PHRASE");
        BlockTypeContainer.add("TEST_PHRASE_FIELD", Category.EXPR, TestPhraseField.class, "TEST_PHRASE_FIELD");
        BlockTypeContainer.add("TEST_PHRASE_FIELD_BOOLEAN", Category.EXPR, TestPhraseFieldBoolean.class, "TEST_PHRASE_FIELD_BOOLEAN");
        BlockTypeContainer.add("TEST_PHRASE_FIELD_DOUBLE", Category.EXPR, TestPhraseFieldDouble.class, "TEST_PHRASE_FIELD_DOUBLE");
        BlockTypeContainer.add("TEST_PHRASE_FIELD_ENUM", Category.EXPR, TestPhraseFieldEnum.class, "TEST_PHRASE_FIELD_ENUM");
        BlockTypeContainer.add("TEST_PHRASE_ALL", Category.EXPR, TestPhraseWithAll.class, "TEST_PHRASE_ALL");
        BlockTypeContainer.add("TEST_PHRASE_WRONG_VALUE_TYPE", Category.EXPR, TestPhraseWrongValueType.class);
    }

    @Before
    public void setUp() throws Exception {
        jaxb2ProgramAst = new Jaxb2ProgramAst<>(null);
    }

    @Test
    public void isNepoAnnotatedClass() {
        assertThat(AnnotationHelper.isNepoAnnotatedClass(TestPhrase.class)).isEqualTo(true);
        assertThat(AnnotationHelper.isNepoAnnotatedClass(TestOperation.class)).isEqualTo(true);
        assertThat(AnnotationHelper.isNepoAnnotatedClass(TestPhraseNotAnnotated.class)).isEqualTo(false);
    }

    @Test
    public void getPrecedence() {
        assertThat(AnnotationHelper.getPrecedence(TestOperation.class)).isEqualTo(10);
    }

    @Test
    public void getPrecedence_annotationNotPresence() {
        assertThatThrownBy(() -> AnnotationHelper.getPrecedence(TestPhrase.class)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> AnnotationHelper.getPrecedence(TestPhraseNotAnnotated.class)).isInstanceOf(DbcException.class);
    }

    @Test
    public void getAssoc() {
        assertThat(AnnotationHelper.getAssoc(TestOperation.class)).isEqualTo(Assoc.LEFT);
    }

    @Test
    public void getAssoc_annotationNotPresence() {
        assertThatThrownBy(() -> AnnotationHelper.getAssoc(TestPhrase.class)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> AnnotationHelper.getAssoc(TestPhraseNotAnnotated.class)).isInstanceOf(DbcException.class);
    }

    @Test
    public void getVarType() {
        assertThat(AnnotationHelper.getVarType(TestOperation.class)).isEqualTo(BlocklyType.ARRAY);
    }

    @Test
    public void getVarType_annotationNotPresence() {
        assertThatThrownBy(() -> AnnotationHelper.getVarType(TestPhrase.class)).isInstanceOf(DbcException.class);
        assertThatThrownBy(() -> AnnotationHelper.getVarType(TestPhraseNotAnnotated.class)).isInstanceOf(DbcException.class);
    }

    @Test
    public void block2astByAnnotation_wrongValueType() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldAndValue.xml");

        assertThatThrownBy(() -> AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseWrongValueType.class, jaxb2ProgramAst))
            .isInstanceOf(DbcException.class);
    }

    @Test
    public void block2astByAnnotation_noData() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldMutation_withoutData.xml");

        assertThatThrownBy(() -> AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseWithAll.class, jaxb2ProgramAst))
            .isInstanceOf(DbcException.class);
    }

    @Test
    public void block2astByAnnotation_all() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldMutationData.xml");

        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseWithAll.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseWithAll.class);

        TestPhraseWithAll<?> testPhrase = (TestPhraseWithAll<?>) resultPhrase;
        assertThat(testPhrase.type).isEqualTo("IMAGE");
        assertThat(testPhrase.mutation.isNext()).isEqualTo(true);
        assertThat(testPhrase.mutation.getDeclarationType()).isEqualTo("Number");
        assertThat(testPhrase.data).isEqualTo("ev3");
        assertThat(testPhrase.hide.getName()).isEqualTo("TYPE");
        assertThat(testPhrase.hide.getValue()).isEqualTo("IMAGE");
    }

    @Test
    public void block2astByAnnotation_fieldAndValue() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldAndValue.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhrase.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhrase.class);
        TestPhrase<?> testPhrase = (TestPhrase<?>) resultPhrase;
        assertThat(testPhrase.type).isEqualTo("IMAGE");

        assertThat(testPhrase.value).isInstanceOf(TestPhraseField.class);
        TestPhraseField<?> testPhraseField = (TestPhraseField<?>) testPhrase.value;
        assertThat(testPhraseField.type).isEqualTo("WHATEVER");
        assertThat(testPhraseField.value).isEqualTo(2.15);
        assertThat(testPhraseField.flag).isEqualTo(false);
        assertThat(testPhraseField.operationType).isEqualTo(TestPhraseField.OperationType.RESET);
    }

    @Test
    public void block2astByAnnotation_fieldDefault() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDefault.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseField.class);
        TestPhraseField<?> testPhrase = (TestPhraseField<?>) resultPhrase;
        assertThat(testPhrase.type).isEqualTo("DEFAULT");
        assertThat(testPhrase.flag).isEqualTo(true);
        assertThat(testPhrase.value).isEqualTo(2.15);
        assertThat(testPhrase.operationType).isEqualTo(TestPhraseField.OperationType.SET);
    }

    @Test
    public void block2astByAnnotation_fieldBoolean() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldBoolean.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldBoolean.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseFieldBoolean.class);
        TestPhraseFieldBoolean<?> testPhrase = (TestPhraseFieldBoolean<?>) resultPhrase;
        assertThat(testPhrase.flag).isEqualTo(true);
    }

    @Test
    public void block2astByAnnotation_fieldDouble() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDouble.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldDouble.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseFieldDouble.class);
        TestPhraseFieldDouble<?> testPhrase = (TestPhraseFieldDouble<?>) resultPhrase;
        assertThat(testPhrase.value).isEqualTo(2.15);
    }

    @Test
    public void block2astByAnnotation_fieldEnum() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldEnum.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldEnum.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseFieldEnum.class);
        TestPhraseFieldEnum<?> testPhrase = (TestPhraseFieldEnum<?>) resultPhrase;
        assertThat(testPhrase.type).isEqualTo(TestPhraseFieldEnum.Type.RESET);
        assertThat(testPhrase.type1).isEqualTo(TestPhraseFieldEnum.Type.SET);
    }

    @Test
    public void block2astByAnnotation_propertiesComment() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_blockPropertiesAndComment.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);

        assertThat(resultPhrase).isInstanceOf(TestPhraseField.class);
        TestPhraseField<?> testPhrase = (TestPhraseField<?>) resultPhrase;
        assertThat(testPhrase.type).isEqualTo("WHATEVER");
        assertThat(testPhrase.getKind()).isEqualTo(BlockTypeContainer.getByName("TEST_PHRASE_FIELD"));
        assertThat(testPhrase.getComment().getComment()).isEqualTo("Test");
        assertThat(testPhrase.getComment().getHeight()).isEqualTo("80");
        assertThat(testPhrase.getComment().getWidth()).isEqualTo("160");
        assertThat(testPhrase.getComment().isPinned()).isEqualTo(false);
        assertThat(testPhrase.getProperty().isInTask()).isEqualTo(true);
        assertThat(testPhrase.getProperty().getBlocklyId()).isEqualTo("b2Ieob%0r|errWxr`reW");
        assertThat(testPhrase.getProperty().getBlockType()).isEqualTo("TEST_PHRASE_FIELD");
    }

    @Test
    public void toString_all() throws JAXBException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldMutationData.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseWithAll.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseWithAll[type: IMAGE]");
    }

    @Test
    public void toString_fieldAndValue() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldAndValue.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhrase.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhrase[type: IMAGE, value: TestPhraseField[type: WHATEVER, flag: false, value: 2.15, operationType: RESET]]");
    }

    @Test
    public void toString_fieldDefault() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDefault.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseField[type: DEFAULT, flag: true, value: 2.15, operationType: SET]");
    }

    @Test
    public void toString_fieldBoolean() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldBoolean.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldBoolean.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseFieldBoolean[flag: true]");
    }

    @Test
    public void toString_fieldDouble() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDouble.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldDouble.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseFieldDouble[value: 2.15]");
    }

    @Test
    public void toString_fieldEnum() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldEnum.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldEnum.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseFieldEnum[type: RESET, type1: SET]");
    }

    @Test
    public void toString_propertiesComment() throws JAXBException, URISyntaxException, IOException {
        List<Block> blocks = loadBlocksFromFile("testPhrase_blockPropertiesAndComment.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);
        assertThat(AnnotationHelper.toString(resultPhrase)).isEqualTo("TestPhraseField[type: WHATEVER, flag: false, value: 2.15, operationType: RESET]");
    }

    @Test
    public void astToBlock_all() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldMutationData.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseWithAll.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldMutationData.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void astToBlock_fieldAndValue() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldAndValue.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhrase.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldAndValue.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void astToBlock_fieldDefault() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDefault.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldDefault.xml"))
            .normalizeWhitespace()
            .areNotIdentical();

        Map<String, String> namespace = new HashMap<>();
        namespace.put("x", "http://de.fhg.iais.roberta.blockly");

        XmlAssert.assertThat(blockToXml(resultBlock))
            .withNamespaceContext(namespace)
            .valueByXPath("/x:block_set/x:instance/x:block/x:field/text()").isEqualTo("DEFAULT");
    }

    @Test
    public void astToBlock_fieldBoolean() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldBoolean.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldBoolean.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldBoolean.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void astToBlock_fieldDouble() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldDouble.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldDouble.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldDouble.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void astToBlock_fieldEnum() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_fieldEnum.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseFieldEnum.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_fieldEnum.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void astToBlock_propertiesComment() throws Exception {
        List<Block> blocks = loadBlocksFromFile("testPhrase_blockPropertiesAndComment.xml");
        Phrase<?> resultPhrase = AnnotationHelper.block2astByAnnotation(blocks.get(0), TestPhraseField.class, jaxb2ProgramAst);
        Block resultBlock = AnnotationHelper.astToBlock(resultPhrase);

        XmlAssert.assertThat(blockToXml(resultBlock))
            .and(loadXml("testPhrase_blockPropertiesAndComment.xml"))
            .normalizeWhitespace()
            .areIdentical();
    }

    @Test
    public void checkNepoAnnotatedClass_areAllValid() {
        Assertions.assertThat(VALID_TEST_PHRASES)
            .allSatisfy((astClass) -> Assertions
                .assertThatCode(() -> AnnotationHelper.checkNepoAnnotatedClass(astClass))
                .doesNotThrowAnyException());
    }

    @Test
    public void checkNepoAnnotatedClass_wrongValueType() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongValueType.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Fields annotated with NepoValue must have one of the following types [class de.fhg.iais.roberta.syntax.lang.expr.Var, class de.fhg.iais.roberta.syntax.lang.expr.Expr]");
    }

    @Test
    public void checkNepoAnnotatedClass_wrongFieldType() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongFieldType.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Fields annotated with NepoField must have one of the following types [class java.lang.String, class java.lang.Boolean, boolean, class java.lang.Double, double, class java.lang.Enum]");
    }

    @Test
    public void checkNepoAnnotatedClass_wrongMutationType() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongMutationType.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Fields annotated with NepoMutation must have one of the following types [class de.fhg.iais.roberta.blockly.generated.Mutation]");
    }

    @Test
    public void checkNepoAnnotatedClass_wrongDataType() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongDataType.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Fields annotated with NepoData must have one of the following types [class java.lang.String]");
    }

    @Test
    public void checkNepoAnnotatedClass_wrongHideType() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongHideType.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Fields annotated with NepoHide must have one of the following types [class de.fhg.iais.roberta.blockly.generated.Hide]");
    }

    @Test
    public void checkNepoAnnotatedClass_fieldNotPublic() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseFieldNotPublic.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Wrong usage at TestPhraseFieldNotPublic#message: Field should be public");
    }

    @Test
    public void checkNepoAnnotatedClass_wrongConstructor() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseWrongConstructor.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Excepted a constructor with the following parameter types [class de.fhg.iais.roberta.syntax.BlockType, class de.fhg.iais.roberta.syntax.BlocklyBlockProperties, class de.fhg.iais.roberta.syntax.BlocklyComment, class java.lang.String, class de.fhg.iais.roberta.syntax.lang.expr.Expr] on TestPhraseWrongConstructor");
    }

    @Test
    public void checkNepoAnnotatedClass_constructorNotPublic() {
        Assertions.assertThatThrownBy(() -> AnnotationHelper.checkNepoAnnotatedClass(TestPhraseConstructorNotPublic.class))
            .isInstanceOf(NepoAnnotationException.class)
            .hasMessageContaining("Constructor de.fhg.iais.roberta.transformer.AnnotationHelperTest$TestPhraseConstructorNotPublic(de.fhg.iais.roberta.syntax.BlockType,de.fhg.iais.roberta.syntax.BlocklyBlockProperties,de.fhg.iais.roberta.syntax.BlocklyComment,java.lang.String,de.fhg.iais.roberta.syntax.lang.expr.Expr) on TestPhraseConstructorNotPublic must be public");
    }

    @NepoPhrase(containerType = "TEST_PHRASE_WRONG_VALUE_TYPE")
    public static class TestPhraseWrongValueType<V> extends Phrase<V> {

        @NepoField(name = "TYPE")
        public final String type;
        @NepoValue(name = "VALUE", type = BlocklyType.ANY)
        public final Action<V> value;

        public TestPhraseWrongValueType(
            BlockType kind,
            BlocklyBlockProperties property,
            BlocklyComment comment,
            String type,
            Action<V> value) {
            super(kind, property, comment);
            this.type = type;
            this.value = value;
        }
    }

    @NepoPhrase
    public static class TestPhraseWrongFieldType<V> extends Phrase<V> {
        @NepoField(name = "TYPE")
        public final int type;

        public TestPhraseWrongFieldType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, int type) {
            super(kind, property, comment);
            this.type = type;
        }
    }

    @NepoPhrase
    public static class TestPhraseWrongMutationType<V> extends Phrase<V> {
        @NepoMutation
        public final String mutation;

        public TestPhraseWrongMutationType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String mutation) {
            super(kind, property, comment);
            this.mutation = mutation;
        }
    }

    @NepoPhrase
    public static class TestPhraseWrongDataType<V> extends Phrase<V> {
        @NepoData
        public final Data data;

        public TestPhraseWrongDataType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, Data data) {
            super(kind, property, comment);
            this.data = data;
        }
    }

    @NepoPhrase
    public static class TestPhraseWrongHideType<V> extends Phrase<V> {
        @NepoHide
        public final String hide;

        public TestPhraseWrongHideType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String hide) {
            super(kind, property, comment);
            this.hide = hide;
        }
    }

    @NepoPhrase
    public static class TestPhraseFieldNotPublic<V> extends Phrase<V> {
        @NepoField(name = "MESSAGE")
        private final String message;

        public TestPhraseFieldNotPublic(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String message) {
            super(kind, property, comment);
            this.message = message;
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE")
    public static class TestPhraseWrongConstructor<V> extends Phrase<V> {

        @NepoField(name = "TYPE")
        public final String type;
        @NepoValue(name = "VALUE", type = BlocklyType.ANY)
        public final Expr<V> value;

        public TestPhraseWrongConstructor(
            BlockType kind,
            BlocklyBlockProperties property,
            BlocklyComment comment,
            Expr<V> value, String type) {
            super(kind, property, comment);
            this.type = type;
            this.value = value;
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE")
    public static class TestPhraseConstructorNotPublic<V> extends Phrase<V> {

        @NepoField(name = "TYPE")
        public final String type;
        @NepoValue(name = "VALUE", type = BlocklyType.ANY)
        public final Expr<V> value;

        TestPhraseConstructorNotPublic(
            BlockType kind,
            BlocklyBlockProperties property,
            BlocklyComment comment,
            String type, Expr<V> value) {
            super(kind, property, comment);
            this.type = type;
            this.value = value;
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE_FIELD")
    public static class TestPhraseField<V> extends Expr<V> {

        @NepoField(name = "TYPE", value = "DEFAULT")
        public final String type;

        @NepoField(name = "FLAG", value = "true")
        public final boolean flag;

        @NepoField(name = "VALUE", value = "2.15")
        public final double value;

        @NepoField(name = "OP_TYPE", value = "SET")
        public final OperationType operationType;

        public TestPhraseField(
            BlockType kind,
            BlocklyBlockProperties properties,
            BlocklyComment comment,
            String type,
            boolean flag,
            double value,
            OperationType operationType) {
            super(kind, properties, comment);
            this.type = type;
            this.flag = flag;
            this.value = value;
            this.operationType = operationType;
        }

        public enum OperationType {
            SET, RESET,
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE")
    public static class TestPhrase<V> extends Phrase<V> {

        @NepoField(name = "TYPE")
        public final String type;
        @NepoValue(name = "VALUE", type = BlocklyType.ANY)
        public final Expr<V> value;

        public TestPhrase(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, String type, Expr<V> value) {
            super(kind, property, comment);
            this.type = type;
            this.value = value;
        }

    }

    @NepoPhrase(containerType = "TEST_PHRASE_FIELD_BOOLEAN")
    public static class TestPhraseFieldBoolean<V> extends Expr<V> {
        @NepoField(name = "FLAG")
        public final boolean flag;

        public TestPhraseFieldBoolean(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, boolean flag) {
            super(kind, properties, comment);
            this.flag = flag;
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE_FIELD_DOUBLE")
    public static class TestPhraseFieldDouble<V> extends Expr<V> {
        @NepoField(name = "VALUE")
        public final double value;

        public TestPhraseFieldDouble(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, double value) {
            super(kind, properties, comment);
            this.value = value;
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE_FIELD_ENUM")
    public static class TestPhraseFieldEnum<V> extends Expr<V> {
        @NepoField(name = "TYPE")
        public final Type type;

        @NepoField(name = "TYPE1")
        public final Type type1;

        public TestPhraseFieldEnum(
            BlockType kind,
            BlocklyBlockProperties properties,
            BlocklyComment comment,
            Type type, Type type1) {
            super(kind, properties, comment);
            this.type = type;
            this.type1 = type1;
        }

        public enum Type {
            RESET, SET
        }
    }

    @NepoPhrase(containerType = "TEST_PHRASE_ALL")
    public static class TestPhraseWithAll<V> extends Expr<V> {

        @NepoMutation
        public final Mutation mutation;

        @NepoData
        public final String data;

        @NepoField(name = "TYPE")
        public final String type;

        @NepoHide
        public final Hide hide;

        public TestPhraseWithAll(
            BlockType kind,
            BlocklyBlockProperties properties,
            BlocklyComment comment,
            Mutation mutation,
            String data,
            String type,
            Hide hide) {
            super(kind, properties, comment);
            this.mutation = mutation;
            this.data = data;
            this.type = type;
            this.hide = hide;
        }
    }

    @NepoOp(precedence = 10, assoc = Assoc.LEFT, blocklyType = BlocklyType.ARRAY)
    public static class TestOperation<V> extends Expr<V> {
        public TestOperation(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
            super(kind, properties, comment);
        }
    }

    public static class TestPhraseNotAnnotated<V> extends Phrase<V> {
        public TestPhraseNotAnnotated(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
            super(kind, properties, comment);
        }
    }

    private String blockToXml(Block resultBlock) throws Exception {
        BlockSet blockSet = new BlockSet();
        Instance instance = new Instance();
        instance.getBlock().add(resultBlock);
        blockSet.getInstance().add(instance);

        return JaxbHelper.blockSet2xml(blockSet);
    }

    private List<Block> loadBlocksFromFile(String filename) throws IOException, JAXBException {
        String xml = IOUtils.toString(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)), StandardCharsets.UTF_8);
        BlockSet blockSet = JaxbHelper.xml2BlockSet(xml);
        return blockSet.getInstance().stream().map(Instance::getBlock).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Input.Builder loadXml(String filename) {
        return Input.fromURL(Objects.requireNonNull(getClass().getResource(filename)));
    }
}