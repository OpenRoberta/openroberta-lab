package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

public final class AstJaxbTransformerHelper {

    public static void setBasicProperties(Phrase<?> astSource, Block jaxbDestination) {
        if ( astSource.getProperty() == null ) {
            return;
        }
        String blockType;
        blockType = astSource.getProperty().getBlockType();
        setProperties(astSource, jaxbDestination, blockType);
        addComment(astSource, jaxbDestination);
    }

    public static void addStatement(Block block, String name, Phrase<?> value) {
        Assert.isTrue(value.getKind() == Phrase.Kind.STMT_LIST, "Phrase is not STMT_LIST");
        if ( ((StmtList<?>) value).get().size() != 0 ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            block.getStatement().add(statement);
        }
    }

    public static void addStatement(Repetitions repetitions, String name, Phrase<?> value) {
        Assert.isTrue(value.getKind() == Phrase.Kind.STMT_LIST, "Phrase is not STMT_LIST");
        if ( ((StmtList<?>) value).get().size() != 0 ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            repetitions.getValueAndStatement().add(statement);
        }
    }

    public static void addValue(Block block, String name, Phrase<?> value) {
        if ( value.getKind() != Kind.EMPTY_EXPR ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(value.astToBlock());
            block.getValue().add(blockValue);
        }
    }

    public static void addValue(Repetitions repetitions, String name, Phrase<?> value) {
        if ( value.getKind() != Kind.EMPTY_EXPR ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(value.astToBlock());
            repetitions.getValueAndStatement().add(blockValue);
        }
    }

    public static void addField(Block block, String name, String value) {
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        block.getField().add(field);
    }

    private static List<Block> extractStmtList(Phrase<?> phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind() == Kind.STMT_LIST, "Phrase is not StmtList!");
        StmtList<?> stmtList = (StmtList<?>) phrase;
        for ( Stmt<?> stmt : stmtList.get() ) {
            result.add(stmt.astToBlock());
        }
        return result;
    }

    private static void setProperties(Phrase<?> astSource, Block block, String type) {
        block.setType(type);
        block.setId(astSource.getProperty().getBlocklyId());
        setDisabled(astSource, block);
        setCollapsed(astSource, block);
        setInline(astSource, block);
        setDeletable(astSource, block);
    }

    private static void setInline(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isInline() != null ) {
            block.setInline(astObject.getProperty().isInline());
        }
    }

    private static void setCollapsed(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isCollapsed() ) {
            block.setCollapsed(astObject.getProperty().isCollapsed());
        }
    }

    private static void setDisabled(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isDisabled() ) {
            block.setDisabled(astObject.getProperty().isDisabled());
        }
    }

    private static void setDeletable(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isDeletable() != null ) {
            block.setDeletable(astObject.getProperty().isDeletable());
        }
    }

    private static void addComment(Phrase<?> astSource, Block block) {
        if ( astSource.getComment() != null ) {
            Comment comment = new Comment();
            comment.setValue(astSource.getComment().getComment());
            comment.setPinned(astSource.getComment().isPinned());
            comment.setH(astSource.getComment().getHeight());
            comment.setW(astSource.getComment().getWidth());
            block.setComment(comment);
        }
    }
}