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
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.exprEvaluator.EvalExprStmtErrorListener;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Task;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.textly.generated.TextlyJavaBaseVisitor;
import de.fhg.iais.roberta.textly.generated.TextlyJavaLexer;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.typecheck.NepoInfoProcessor;
import de.fhg.iais.roberta.util.Key;

/**
 * Uses the {@link CommonTextlyJavaVisitor} to transform TextlyJava to AST.
 */
public abstract class AbstractTextlyJavaToNepoWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        TextlyJavaParser parser = null;
        try {
            parser = mkParser(project.getProgramAsTextly());
            EvalExprStmtErrorListener err = new EvalExprStmtErrorListener();
            parser.removeErrorListeners();
            parser.addErrorListener(err);
            TextlyJavaParser.ProgramContext program = parser.program();

            CommonTextlyJavaVisitor visitor = this.getVisitor();

            if ( parser.getNumberOfSyntaxErrors() > 0 ) {
                project.setTextlyErrors(err.getError());
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToErrorCounter(parser.getNumberOfSyntaxErrors(), null);
            } else {
                List<Phrase> mainPart = (List<Phrase>) ((TextlyJavaBaseVisitor<?>) visitor).visitProgram(program);

                Task mainTask = (MainTask) mainPart.get(0);
                StmtList stmtList = (StmtList) mainPart.get(1);

                List<JSONObject> textlyErrors = new ArrayList<>();
                List<Phrase> phrases = new ArrayList<>(Arrays.asList(new Location("0", "0"), mainTask));
                for ( int i = 0; i < stmtList.sl.size(); i++ ) {
                    phrases.add((Phrase) stmtList.sl.get(i));
                    textlyErrors.addAll(NepoInfoProcessor.collectTextlyErrors(((Phrase) stmtList.sl.get(i))));
                }

                ProgramAst.Builder builder = new ProgramAst.Builder()
                    .setRobotType(project.getRobotFactory().getGroup())
                    .setXmlVersion(project.getProgramAst().getXmlVersion())
                    .addToTree(phrases);

                Location location = new Location(String.valueOf(400), "0");
                for ( int i = 2; i < mainPart.size(); i++ ) {
                    List<Phrase> phrasesMethod;
                    if ( mainPart.get(i) instanceof MethodReturn ) {
                        MethodReturn methodReturn = (MethodReturn) mainPart.get(i);
                        phrasesMethod = new ArrayList<>(Arrays.asList(location, methodReturn));
                    } else {
                        MethodVoid methodVoid = (MethodVoid) mainPart.get(i);
                        phrasesMethod = new ArrayList<>(Arrays.asList(location, methodVoid));
                    }
                    builder.addToTree(phrasesMethod);
                    location = new Location(String.valueOf(i * 400), "0");
                }

                project.setTextlyErrors(textlyErrors);
                project.setProgramAst(builder.build());
            }

        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

    }

    protected abstract CommonTextlyJavaVisitor getVisitor();

    private static TextlyJavaParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        CharStream input = CharStreams.fromStream(inputStream);
        TextlyJavaLexer lexer = new TextlyJavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TextlyJavaParser parser = new TextlyJavaParser(tokens);
        return parser;
    }

}