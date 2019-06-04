package de.fhg.iais.roberta.util.exprblk.ast;

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
// Generated from Textly0.g4 by ANTLR 4.3
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
        RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__1 = 1, T__0 = 2, INT = 3, VAR = 4, NEWLINE = 5, WS = 6, AND = 7, OR = 8, NOT = 9, EQUAL = 10, MUL = 11, DIV = 12, ADD = 13,
        SUB = 14, SEMI = 15, ASSIGN = 16;
    public static final String[] tokenNames =
        {
            "<INVALID>",
            "'('",
            "')'",
            "INT",
            "VAR",
            "NEWLINE",
            "WS",
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
    public static final int RULE_expression = 0, RULE_expr = 1;
    public static final String[] ruleNames =
        {
            "expression",
            "expr"
        };

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
    public ATN getATN() {
        return _ATN;
    }

    public Textly0Parser(TokenStream input) {
        super(input);
        this._interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ExpressionContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).enterExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if ( listener instanceof Textly0Listener ) {
                ((Textly0Listener) listener).exitExpression(this);
            }
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof Textly0Visitor ) {
                return ((Textly0Visitor<? extends T>) visitor).visitExpression(this);
            } else {
                return visitor.visitChildren(this);
            }
        }
    }

    public final ExpressionContext expression() throws RecognitionException {
        ExpressionContext _localctx = new ExpressionContext(this._ctx, getState());
        enterRule(_localctx, 0, RULE_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(4);
                expr(0);
            }
        } catch ( RecognitionException re ) {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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

        public TerminalNode EQUAL() {
            return getToken(Textly0Parser.EQUAL, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode SUB() {
            return getToken(Textly0Parser.SUB, 0);
        }

        public TerminalNode ADD() {
            return getToken(Textly0Parser.ADD, 0);
        }

        public TerminalNode AND() {
            return getToken(Textly0Parser.AND, 0);
        }

        public TerminalNode OR() {
            return getToken(Textly0Parser.OR, 0);
        }

        public TerminalNode DIV() {
            return getToken(Textly0Parser.DIV, 0);
        }

        public TerminalNode MUL() {
            return getToken(Textly0Parser.MUL, 0);
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

        public TerminalNode NOT() {
            return getToken(Textly0Parser.NOT, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode SUB() {
            return getToken(Textly0Parser.SUB, 0);
        }

        public TerminalNode ADD() {
            return getToken(Textly0Parser.ADD, 0);
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
        int _startState = 2;
        enterRecursionRule(_localctx, 2, RULE_expr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(17);
                switch ( this._input.LA(1) ) {
                    case ADD:
                    case SUB: {
                        _localctx = new UnaryContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;

                        setState(7);
                        ((UnaryContext) _localctx).op = this._input.LT(1);
                        _la = this._input.LA(1);
                        if ( !(_la == ADD || _la == SUB) ) {
                            ((UnaryContext) _localctx).op = this._errHandler.recoverInline(this);
                        }
                        consume();
                        setState(8);
                        expr(10);
                    }
                        break;
                    case NOT: {
                        _localctx = new UnaryContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(9);
                        ((UnaryContext) _localctx).op = match(NOT);
                        setState(10);
                        expr(6);
                    }
                        break;
                    case VAR: {
                        _localctx = new VarNameContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(11);
                        match(VAR);
                    }
                        break;
                    case INT: {
                        _localctx = new IntConstContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(12);
                        match(INT);
                    }
                        break;
                    case T__1: {
                        _localctx = new ParenthesesContext(_localctx);
                        this._ctx = _localctx;
                        _prevctx = _localctx;
                        setState(13);
                        match(T__1);
                        setState(14);
                        expr(0);
                        setState(15);
                        match(T__0);
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                this._ctx.stop = this._input.LT(-1);
                setState(36);
                this._errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(this._input, 2, this._ctx);
                while ( _alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
                    if ( _alt == 1 ) {
                        if ( this._parseListeners != null ) {
                            triggerExitRuleEvent();
                        }
                        _prevctx = _localctx;
                        {
                            setState(34);
                            switch ( getInterpreter().adaptivePredict(this._input, 1, this._ctx) ) {
                                case 1: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(19);
                                    if ( !precpred(this._ctx, 9) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    }
                                    setState(20);
                                    ((BinaryContext) _localctx).op = this._input.LT(1);
                                    _la = this._input.LA(1);
                                    if ( !(_la == MUL || _la == DIV) ) {
                                        ((BinaryContext) _localctx).op = this._errHandler.recoverInline(this);
                                    }
                                    consume();
                                    setState(21);
                                    expr(10);
                                }
                                    break;

                                case 2: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(22);
                                    if ( !precpred(this._ctx, 8) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    }
                                    setState(23);
                                    ((BinaryContext) _localctx).op = this._input.LT(1);
                                    _la = this._input.LA(1);
                                    if ( !(_la == ADD || _la == SUB) ) {
                                        ((BinaryContext) _localctx).op = this._errHandler.recoverInline(this);
                                    }
                                    consume();
                                    setState(24);
                                    expr(9);
                                }
                                    break;

                                case 3: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(25);
                                    if ( !precpred(this._ctx, 7) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    }
                                    setState(26);
                                    ((BinaryContext) _localctx).op = match(EQUAL);
                                    setState(27);
                                    expr(8);
                                }
                                    break;

                                case 4: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(28);
                                    if ( !precpred(this._ctx, 5) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    }
                                    setState(29);
                                    ((BinaryContext) _localctx).op = match(AND);
                                    setState(30);
                                    expr(6);
                                }
                                    break;

                                case 5: {
                                    _localctx = new BinaryContext(new ExprContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(31);
                                    if ( !precpred(this._ctx, 4) ) {
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    }
                                    setState(32);
                                    ((BinaryContext) _localctx).op = match(OR);
                                    setState(33);
                                    expr(5);
                                }
                                    break;
                            }
                        }
                    }
                    setState(38);
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
            case 1:
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
        "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\22*\4\2\t\2\4\3\t"
            + "\3\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\24\n\3\3\3"
            + "\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3%\n\3\f\3"
            + "\16\3(\13\3\3\3\2\3\4\4\2\4\2\4\3\2\17\20\3\2\r\16\60\2\6\3\2\2\2\4\23"
            + "\3\2\2\2\6\7\5\4\3\2\7\3\3\2\2\2\b\t\b\3\1\2\t\n\t\2\2\2\n\24\5\4\3\f"
            + "\13\f\7\13\2\2\f\24\5\4\3\b\r\24\7\6\2\2\16\24\7\5\2\2\17\20\7\3\2\2\20"
            + "\21\5\4\3\2\21\22\7\4\2\2\22\24\3\2\2\2\23\b\3\2\2\2\23\13\3\2\2\2\23"
            + "\r\3\2\2\2\23\16\3\2\2\2\23\17\3\2\2\2\24&\3\2\2\2\25\26\f\13\2\2\26\27"
            + "\t\3\2\2\27%\5\4\3\f\30\31\f\n\2\2\31\32\t\2\2\2\32%\5\4\3\13\33\34\f"
            + "\t\2\2\34\35\7\f\2\2\35%\5\4\3\n\36\37\f\7\2\2\37 \7\t\2\2 %\5\4\3\b!"
            + "\"\f\6\2\2\"#\7\n\2\2#%\5\4\3\7$\25\3\2\2\2$\30\3\2\2\2$\33\3\2\2\2$\36"
            + "\3\2\2\2$!\3\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'\5\3\2\2\2(&\3\2\2"
            + "\2\5\23$&";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for ( int i = 0; i < _ATN.getNumberOfDecisions(); i++ ) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}