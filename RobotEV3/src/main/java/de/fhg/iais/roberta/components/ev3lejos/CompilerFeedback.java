package de.fhg.iais.roberta.components.ev3lejos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompilerFeedback {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerFeedback.class);
    private final boolean success;
    private final List<CompilerMessage> messages = new ArrayList<>();

    public CompilerFeedback(final Boolean success, final DiagnosticCollector<JavaFileObject> diagnostics) {
        this.success = success != null && success;
        for ( Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics() ) {
            this.messages.add(new CompilerMessage(diagnostic));
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("SUCCESS: ").append(this.success).append('\n');
        final int iTop = this.messages.size();
        for ( int i = 0; i < iTop; i++ ) {
            sb.append("\n[MESSAGE ").append(i + 1).append(" OF ").append(iTop).append("]\n\n");
            sb.append(this.messages.get(i).toStringForDebugging()).append("\n");
        }
        return sb.toString();
    }

    public final static class CompilerMessage {
        public final Diagnostic<? extends JavaFileObject> compilerInfo;

        public final String typeOfProblem;
        public final String typeOfProblem_forDebugging;

        public final String multiLineMessage;

        public final int lineNumber;
        public final int columnNumber;

        public final int textHighlightPos_lineStart;
        public final int textHighlightPos_problemStart;
        public final int textHighlightPos_problemEnd;

        public final String sourceCode;
        public final String codeOfConcern;
        public final String codeOfConcernLong;

        CompilerMessage(final Diagnostic<? extends JavaFileObject> diagnostic) {

            final JavaFileObject sourceFileObject = diagnostic.getSource();
            String sourceCodePreliminary = null;
            if ( sourceFileObject instanceof SimpleJavaFileObject ) {
                final SimpleJavaFileObject simpleSourceFileObject = (SimpleJavaFileObject) sourceFileObject;

                try {
                    final CharSequence charSequence = simpleSourceFileObject.getCharContent(false);
                    sourceCodePreliminary = charSequence.toString();
                } catch ( IOException e ) {
                    CompilerFeedback.LOG.error(e.getMessage());
                }
            }
            if ( sourceCodePreliminary == null ) {
                this.sourceCode = "[SOURCE CODE UNAVAILABLE]";
            } else {
                this.sourceCode = sourceCodePreliminary;
            }

            this.compilerInfo = diagnostic;

            this.typeOfProblem = diagnostic.getKind().name();
            this.typeOfProblem_forDebugging = "toString() = " + diagnostic.getKind().toString() + "; name() = " + this.typeOfProblem;

            this.lineNumber = (int) this.compilerInfo.getLineNumber();
            this.columnNumber = (int) this.compilerInfo.getColumnNumber();

            final int sourceLen = this.sourceCode.length();
            this.textHighlightPos_lineStart = (int) Math.min(Math.max(0, diagnostic.getStartPosition()), sourceLen);
            this.textHighlightPos_problemStart = (int) Math.min(Math.max(0, diagnostic.getPosition()), sourceLen);
            this.textHighlightPos_problemEnd = (int) Math.min(Math.max(0, diagnostic.getEndPosition()), sourceLen);

            final StringBuilder reformattedMessage = new StringBuilder();
            final String message = diagnostic.getMessage(Locale.US);
            final int messageCutOffPosition = message.indexOf("location:");
            final String[] messageParts;
            if ( messageCutOffPosition >= 0 ) {
                messageParts = message.substring(0, messageCutOffPosition).split("\n");
            } else {
                messageParts = message.split("\n");
            }
            for ( String s : messageParts ) {
                String s2 = s.trim();
                if ( s2.length() > 0 ) {
                    boolean lengthChanged;
                    do {
                        final int lBeforeReplace = s2.length();
                        s2 = s2.replace("  ", " ");
                        lengthChanged = (s2.length() != lBeforeReplace);
                    } while ( lengthChanged );
                    reformattedMessage.append(s2).append("\n");
                }
            }

            this.codeOfConcern = this.sourceCode.substring(this.textHighlightPos_problemStart, this.textHighlightPos_problemEnd);
            this.codeOfConcernLong = this.sourceCode.substring(this.textHighlightPos_lineStart, this.textHighlightPos_problemEnd);
            if ( !this.codeOfConcern.isEmpty() ) {
                reformattedMessage.append("Code of concern: \"").append(this.codeOfConcern).append('\"');
            }
            this.multiLineMessage = reformattedMessage.toString();
        }

        public String toStringForList() {

            if ( this.compilerInfo == null ) {
                return "No compiler!";
            } else {
                return this.compilerInfo.getCode();
            }
        }

        public String toStringForDebugging() {

            final StringBuilder ret = new StringBuilder();

            ret.append("Type of problem: ").append(this.typeOfProblem_forDebugging).append("\n\n");
            ret.append("Message:\n").append(this.multiLineMessage).append("\n\n");

            ret.append(this.compilerInfo.getCode()).append("\n\n");

            ret.append("line number: ").append(this.lineNumber).append("\n");
            ret.append("column number: ").append(this.columnNumber).append("\n");

            ret.append("textHighlightPos_lineStart: ").append(this.textHighlightPos_lineStart).append("\n");
            ret.append("textHighlightPos_problemStart: ").append(this.textHighlightPos_problemStart).append("\n");
            ret.append("textHighlightPos_problemEnd: ").append(this.textHighlightPos_problemEnd).append("\n");

            return ret.toString();
        }

        @Override
        public String toString() {
            return this.typeOfProblem + ": " + this.multiLineMessage + "\n";
        }
    }
}
