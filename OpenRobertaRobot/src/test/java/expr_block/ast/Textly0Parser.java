package expr_block.ast;

import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
// Generated from Textly0.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({
    "all",
    "warnings",
    "unchecked",
    "unused",
    "cast"
})
public class Textly0Parser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, INT = 3, VAR = 4, NEWLINE = 5, WS = 6, AND = 7, OR = 8, NOT = 9, EQUAL = 10, MUL = 11, DIV = 12, ADD = 13,
        SUB = 14, SEMI = 15, ASSIGN = 16;
    public static final int RULE_expr = 0;

    private static String[] makeRuleNames() {
        return new String[] {
            "expr"
        };
    }
    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] {
            null,
            "'('",
            "')'",
            null,
            null,
            null,
            null,
            "'&&'",
            "'||'",
            "'!'",
            "'=='",
            "'*'",
            "'/'",
            "'+'",
            "'-'",
            "';'",
            "':='"
        };
    }
    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] {
            null,
            null,
            null,
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
    }
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for ( int i = 0; i < tokenNames.length; i++ ) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if ( tokenNames[i] == null ) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if ( tokenNames[i] == null ) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    //@Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "Textly0.g4";
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
    public ATN getATN() {
        return _ATN;
    }

    public Textly0Parser(TokenStream input) {
        super(input);
        this._interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ExprContext extends ParserRuleContext {
        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        public ExprContext() {
        }

        public void copyFrom(ExprContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class VarNameContext extends ExprContext {
        public TerminalNode VAR() {
            return getToken(Textly0Parser.VAR, 0);
        }

        public VarNameContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterVarName(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitVarName(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitVarName(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class BinaryContext extends ExprContext {
        public Token op;

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode MUL() {
            return getToken(Textly0Parser.MUL, 0);
        }

        public TerminalNode DIV() {
            return getToken(Textly0Parser.DIV, 0);
        }

        public TerminalNode ADD() {
            return getToken(Textly0Parser.ADD, 0);
        }

        public TerminalNode SUB() {
            return getToken(Textly0Parser.SUB, 0);
        }

        public TerminalNode EQUAL() {
            return getToken(Textly0Parser.EQUAL, 0);
        }

        public TerminalNode AND() {
            return getToken(Textly0Parser.AND, 0);
        }

        public TerminalNode OR() {
            return getToken(Textly0Parser.OR, 0);
        }

        public BinaryContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterBinary(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitBinary(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitBinary(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class IntConstContext extends ExprContext {
        public TerminalNode INT() {
            return getToken(Textly0Parser.INT, 0);
        }

        public IntConstContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterIntConst(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitIntConst(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitIntConst(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class UnaryContext extends ExprContext {
        public Token op;

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode ADD() {
            return getToken(Textly0Parser.ADD, 0);
        }

        public TerminalNode SUB() {
            return getToken(Textly0Parser.SUB, 0);
        }

        public TerminalNode NOT() {
            return getToken(Textly0Parser.NOT, 0);
        }

        public UnaryContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterUnary(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitUnary(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitUnary(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public static class ParenthesesContext extends ExprContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public ParenthesesContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterParentheses(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitParentheses(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitParentheses(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = this._ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(this._ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 0;
        enterRecursionRule(_localctx, 0, RULE_expr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(13);
                this._errHandler.sync(this);
                switch ( this._input.LA(1) ) {
                    case ADD:
                    case SUB: {
                        _localctx = new UnaryContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;

                        setState(3);
                        ((UnaryContext) _localctx).op = this._input.LT(1);
                        _la = this._input.LA(1);
                        if ( !(_la == ADD || _la == SUB) ) {
                            ((UnaryContext) _localctx).op = this._errHandler.recoverInline(this);
                        } else {
                            if ( this._input.LA(1) == Token.EOF ) {
                                matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            consume();
                        }
                        setState(4);
                        expr(10);
                    }
                        break;
                    case NOT: {
                        _localctx = new UnaryContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(5);
                        ((UnaryContext) _localctx).op = match(NOT);
                        setState(6);
                        expr(6);
                    }
                        break;
                    case VAR: {
                        _localctx = new VarNameContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(7);
                        match(VAR);
                    }
                        break;
                    case INT: {
                        _localctx = new IntConstContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(8);
                        match(INT);
                    }
                        break;
                    case T__0: {
                        _localctx = new ParenthesesContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(9);
                        match(T__0);
                        setState(10);
                        expr(0);
                        setState(11);
                        match(T__1);
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                this._ctx.stop = this._input.LT(-1);
                setState(32);
                this._errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(this._input, 2, this._ctx);
                while ( _alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
                    if ( _alt == 1 ) {
                        if ( this._parseListeners != null ) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(30);
                            this._errHandler.sync(this);
                            switch ( getInterpreter().adaptivePredict(this._input, 1, this._ctx) ) {
                                case 1: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(15);
                                    if ( !precpred(this._ctx, 9) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    }
                                    setState(16);
                                    ((BinaryContext) _localctx).op = this._input.LT(1);
                                    _la = this._input.LA(1);
                                    if ( !(_la == MUL || _la == DIV) ) {
                                        ((BinaryContext) _localctx).op = this._errHandler.recoverInline(this);
                                    } else {
                                        if ( this._input.LA(1) == Token.EOF ) {
                                            matchedEOF = true;
                                        }
                                        this._errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(17);
                                    expr(10);
                                }
                                    break;
                                case 2: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(18);
                                    if ( !precpred(this._ctx, 8) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    }
                                    setState(19);
                                    ((BinaryContext) _localctx).op = this._input.LT(1);
                                    _la = this._input.LA(1);
                                    if ( !(_la == ADD || _la == SUB) ) {
                                        ((BinaryContext) _localctx).op = this._errHandler.recoverInline(this);
                                    } else {
                                        if ( this._input.LA(1) == Token.EOF ) {
                                            matchedEOF = true;
                                        }
                                        this._errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(20);
                                    expr(9);
                                }
                                    break;
                                case 3: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(21);
                                    if ( !precpred(this._ctx, 7) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    }
                                    setState(22);
                                    ((BinaryContext) _localctx).op = match(EQUAL);
                                    setState(23);
                                    expr(8);
                                }
                                    break;
                                case 4: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(24);
                                    if ( !precpred(this._ctx, 5) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    }
                                    setState(25);
                                    ((BinaryContext) _localctx).op = match(AND);
                                    setState(26);
                                    expr(6);
                                }
                                    break;
                                case 5: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(27);
                                    if ( !precpred(this._ctx, 4) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    }
                                    setState(28);
                                    ((BinaryContext) _localctx).op = match(OR);
                                    setState(29);
                                    expr(5);
                                }
                                    break;
                            }
                        }
                    }
                    setState(34);
                    this._errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(this._input, 2, this._ctx);
                }
            }
        } catch ( RecognitionException re ) {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch ( ruleIndex ) {
            case 0:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch ( predIndex ) {
            case 0:
                return precpred(this._ctx, 9);
            case 1:
                return precpred(this._ctx, 8);
            case 2:
                return precpred(this._ctx, 7);
            case 3:
                return precpred(this._ctx, 5);
            case 4:
                return precpred(this._ctx, 4);
        }
        return true;
    }

    public static final String _serializedATN =
        "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\22&\4\2\t\2\3\2\3"
            + "\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\20\n\2\3\2\3\2\3\2\3\2\3\2"
            + "\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2!\n\2\f\2\16\2$\13\2\3\2\2"
            + "\3\2\3\2\2\4\3\2\17\20\3\2\r\16\2-\2\17\3\2\2\2\4\5\b\2\1\2\5\6\t\2\2"
            + "\2\6\20\5\2\2\f\7\b\7\13\2\2\b\20\5\2\2\b\t\20\7\6\2\2\n\20\7\5\2\2\13"
            + "\f\7\3\2\2\f\r\5\2\2\2\r\16\7\4\2\2\16\20\3\2\2\2\17\4\3\2\2\2\17\7\3"
            + "\2\2\2\17\t\3\2\2\2\17\n\3\2\2\2\17\13\3\2\2\2\20\"\3\2\2\2\21\22\f\13"
            + "\2\2\22\23\t\3\2\2\23!\5\2\2\f\24\25\f\n\2\2\25\26\t\2\2\2\26!\5\2\2\13"
            + "\27\30\f\t\2\2\30\31\7\f\2\2\31!\5\2\2\n\32\33\f\7\2\2\33\34\7\t\2\2\34"
            + "!\5\2\2\b\35\36\f\6\2\2\36\37\7\n\2\2\37!\5\2\2\7 \21\3\2\2\2 \24\3\2"
            + "\2\2 \27\3\2\2\2 \32\3\2\2\2 \35\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2"
            + "\2#\3\3\2\2\2$\"\3\2\2\2\5\17 \"";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for ( int i = 0; i < _ATN.getNumberOfDecisions(); i++ ) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}