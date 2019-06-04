package de.fhg.iais.roberta.util.exprblk.ast;

import org.antlr.v4.runtime.CharStream;
// Generated from Textly0.g4 by ANTLR 4.3
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({
    "all",
    "warnings",
    "unchecked",
    "unused",
    "cast"
})
public class Textly0Lexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__1 = 1, T__0 = 2, INT = 3, VAR = 4, NEWLINE = 5, WS = 6, AND = 7, OR = 8, NOT = 9, EQUAL = 10, MUL = 11, DIV = 12, ADD = 13,
        SUB = 14, SEMI = 15, ASSIGN = 16;
    public static String[] modeNames =
        {
            "DEFAULT_MODE"
        };

    public static final String[] tokenNames =
        {
            "'\\u0000'",
            "'\\u0001'",
            "'\\u0002'",
            "'\\u0003'",
            "'\\u0004'",
            "'\\u0005'",
            "'\\u0006'",
            "'\\u0007'",
            "'\b'",
            "'\t'",
            "'\n'",
            "'\\u000B'",
            "'\f'",
            "'\r'",
            "'\\u000E'",
            "'\\u000F'",
            "'\\u0010'"
        };
    public static final String[] ruleNames =
        {
            "T__1",
            "T__0",
            "INT",
            "VAR",
            "NEWLINE",
            "WS",
            "AND",
            "OR",
            "NOT",
            "EQUAL",
            "MUL",
            "DIV",
            "ADD",
            "SUB",
            "SEMI",
            "ASSIGN"
        };

    public Textly0Lexer(CharStream input) {
        super(input);
        this._interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "Textly0.g4";
    }

    @Override
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
        "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\22Z\b\1\4\2\t\2\4"
            + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
            + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3"
            + "\3\3\3\3\4\6\4)\n\4\r\4\16\4*\3\5\3\5\3\5\7\5\60\n\5\f\5\16\5\63\13\5"
            + "\3\6\5\6\66\n\6\3\6\3\6\3\6\3\6\3\7\6\7=\n\7\r\7\16\7>\3\7\3\7\3\b\3\b"
            + "\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17"
            + "\3\17\3\20\3\20\3\21\3\21\3\21\2\2\22\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"
            + "\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22\3\2\3\4\2\13\13\"\"]\2"
            + "\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"
            + "\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"
            + "\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3#\3\2\2"
            + "\2\5%\3\2\2\2\7(\3\2\2\2\t,\3\2\2\2\13\65\3\2\2\2\r<\3\2\2\2\17B\3\2\2"
            + "\2\21E\3\2\2\2\23H\3\2\2\2\25J\3\2\2\2\27M\3\2\2\2\31O\3\2\2\2\33Q\3\2"
            + "\2\2\35S\3\2\2\2\37U\3\2\2\2!W\3\2\2\2#$\7*\2\2$\4\3\2\2\2%&\7+\2\2&\6"
            + "\3\2\2\2\')\4\62;\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2*+\3\2\2\2+\b\3\2\2"
            + "\2,\61\4c|\2-.\4c|\2.\60\4\62;\2/-\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61"
            + "\62\3\2\2\2\62\n\3\2\2\2\63\61\3\2\2\2\64\66\7\17\2\2\65\64\3\2\2\2\65"
            + "\66\3\2\2\2\66\67\3\2\2\2\678\7\f\2\289\3\2\2\29:\b\6\2\2:\f\3\2\2\2;"
            + "=\t\2\2\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\7\2\2"
            + "A\16\3\2\2\2BC\7(\2\2CD\7(\2\2D\20\3\2\2\2EF\7~\2\2FG\7~\2\2G\22\3\2\2"
            + "\2HI\7#\2\2I\24\3\2\2\2JK\7?\2\2KL\7?\2\2L\26\3\2\2\2MN\7,\2\2N\30\3\2"
            + "\2\2OP\7\61\2\2P\32\3\2\2\2QR\7-\2\2R\34\3\2\2\2ST\7/\2\2T\36\3\2\2\2"
            + "UV\7=\2\2V \3\2\2\2WX\7<\2\2XY\7?\2\2Y\"\3\2\2\2\7\2*\61\65>\3\b\2\2";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for ( int i = 0; i < _ATN.getNumberOfDecisions(); i++ ) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}