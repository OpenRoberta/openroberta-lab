package de.fhg.iais.roberta.worker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.exprEvaluator.EvalExprErrorListener;
import de.fhg.iais.roberta.exprly.generated.ExprlyBaseVisitor;
import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Task;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;

/**
 * Uses the {@link CommonTextlyVisitor} to transform textly to AST.
 */
public abstract class AbstractTextlyToAstWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        ExprlyParser parser = null;
        try {
            parser = mkParser(project.getTextlyProgram());
            EvalExprErrorListener err = new EvalExprErrorListener();
            parser.removeErrorListeners();
            parser.addErrorListener(err);
            ExprlyParser.ProgramContext program = parser.program();

            CommonTextlyVisitor visitor = this.getVisitor();

            List<Phrase> mainPart = (List<Phrase>) ((ExprlyBaseVisitor<?>) visitor).visitProgram(program);

            Task mainTask = (MainTask) mainPart.get(0);
            StmtList stmtList = (StmtList) mainPart.get(1);

            List<Phrase> phrases = new ArrayList<>(Arrays.asList(new Location("0", "0"), mainTask));
            phrases.add(stmtList);
            ProgramAst programAst = new ProgramAst.Builder()
                .addToTree(phrases)
                .build();

            project.setProgramAst(programAst);

        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

    }
    
    protected abstract CommonTextlyVisitor getVisitor();

    private static ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        CharStream input = CharStreams.fromStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }
}
