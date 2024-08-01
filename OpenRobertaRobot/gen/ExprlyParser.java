// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/exprly/generated/Exprly.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ExprlyParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, NEWLINE=11, WS=12, FNAME=13, CONST=14, NULL=15, INT=16, FLOAT=17, 
		COLOR=18, BOOL=19, HEX=20, VAR=21, STR=22, AND=23, OR=24, NOT=25, EQUAL=26, 
		NEQUAL=27, GET=28, LET=29, GEQ=30, LEQ=31, MOD=32, POW=33, MUL=34, DIV=35, 
		ADD=36, SUB=37;
	public static final int
		RULE_expression = 0, RULE_expr = 1, RULE_literal = 2, RULE_connExpr = 3, 
		RULE_funCall = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "expr", "literal", "connExpr", "funCall"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'?'", "':'", "'\"'", "'.'", "'['", "','", "']'", 
			"'connect'", null, null, null, null, "'null'", null, null, null, null, 
			null, null, null, "'&&'", "'||'", "'!'", "'=='", "'!='", "'>'", "'<'", 
			"'>='", "'<='", "'%'", "'^'", "'*'", "'/'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "NEWLINE", 
			"WS", "FNAME", "CONST", "NULL", "INT", "FLOAT", "COLOR", "BOOL", "HEX", 
			"VAR", "STR", "AND", "OR", "NOT", "EQUAL", "NEQUAL", "GET", "LET", "GEQ", 
			"LEQ", "MOD", "POW", "MUL", "DIV", "ADD", "SUB"
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
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Exprly.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExprlyParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ExprlyParser.EOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			expr(0);
			setState(11);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExpContext extends ExprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterLiteralExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitLiteralExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitLiteralExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfElseOpContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IfElseOpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterIfElseOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitIfElseOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitIfElseOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryBContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NOT() { return getToken(ExprlyParser.NOT, 0); }
		public UnaryBContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterUnaryB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitUnaryB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitUnaryB(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryBContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(ExprlyParser.AND, 0); }
		public TerminalNode OR() { return getToken(ExprlyParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(ExprlyParser.EQUAL, 0); }
		public TerminalNode NEQUAL() { return getToken(ExprlyParser.NEQUAL, 0); }
		public TerminalNode GET() { return getToken(ExprlyParser.GET, 0); }
		public TerminalNode LET() { return getToken(ExprlyParser.LET, 0); }
		public TerminalNode GEQ() { return getToken(ExprlyParser.GEQ, 0); }
		public TerminalNode LEQ() { return getToken(ExprlyParser.LEQ, 0); }
		public BinaryBContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterBinaryB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitBinaryB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitBinaryB(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryNContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD() { return getToken(ExprlyParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(ExprlyParser.SUB, 0); }
		public UnaryNContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterUnaryN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitUnaryN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitUnaryN(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryNContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode POW() { return getToken(ExprlyParser.POW, 0); }
		public TerminalNode MOD() { return getToken(ExprlyParser.MOD, 0); }
		public TerminalNode MUL() { return getToken(ExprlyParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(ExprlyParser.DIV, 0); }
		public TerminalNode ADD() { return getToken(ExprlyParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(ExprlyParser.SUB, 0); }
		public BinaryNContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterBinaryN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitBinaryN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitBinaryN(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarNameContext extends ExprContext {
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public VarNameContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterVarName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitVarName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitVarName(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParentheseContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParentheseContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterParenthese(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitParenthese(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitParenthese(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MathConstContext extends ExprContext {
		public TerminalNode CONST() { return getToken(ExprlyParser.CONST, 0); }
		public MathConstContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMathConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMathConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMathConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConnExpContext extends ExprContext {
		public ConnExprContext connExpr() {
			return getRuleContext(ConnExprContext.class,0);
		}
		public ConnExpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterConnExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitConnExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitConnExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncExpContext extends ExprContext {
		public FunCallContext funCall() {
			return getRuleContext(FunCallContext.class,0);
		}
		public FuncExpContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterFuncExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitFuncExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitFuncExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullConstContext extends ExprContext {
		public TerminalNode NULL() { return getToken(ExprlyParser.NULL, 0); }
		public NullConstContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterNullConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitNullConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitNullConst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				{
				_localctx = new NullConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(14);
				match(NULL);
				}
				break;
			case CONST:
				{
				_localctx = new MathConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(15);
				match(CONST);
				}
				break;
			case VAR:
				{
				_localctx = new VarNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(16);
				match(VAR);
				}
				break;
			case T__4:
			case T__6:
			case INT:
			case FLOAT:
			case COLOR:
			case BOOL:
				{
				_localctx = new LiteralExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(17);
				literal();
				}
				break;
			case T__9:
				{
				_localctx = new ConnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(18);
				connExpr();
				}
				break;
			case FNAME:
				{
				_localctx = new FuncExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(19);
				funCall();
				}
				break;
			case T__0:
				{
				_localctx = new ParentheseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(20);
				match(T__0);
				setState(21);
				expr(0);
				setState(22);
				match(T__1);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new UnaryNContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24);
				((UnaryNContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((UnaryNContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(25);
				expr(15);
				}
				break;
			case NOT:
				{
				_localctx = new UnaryBContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(26);
				((UnaryBContext)_localctx).op = match(NOT);
				setState(27);
				expr(14);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(74);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(72);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(30);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(31);
						((BinaryNContext)_localctx).op = match(POW);
						setState(32);
						expr(13);
						}
						break;
					case 2:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(33);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(34);
						((BinaryNContext)_localctx).op = match(MOD);
						setState(35);
						expr(12);
						}
						break;
					case 3:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(36);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(37);
						((BinaryNContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((BinaryNContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(38);
						expr(12);
						}
						break;
					case 4:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(39);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(40);
						((BinaryNContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((BinaryNContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(41);
						expr(11);
						}
						break;
					case 5:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(42);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(43);
						((BinaryBContext)_localctx).op = match(AND);
						setState(44);
						expr(10);
						}
						break;
					case 6:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(45);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(46);
						((BinaryBContext)_localctx).op = match(OR);
						setState(47);
						expr(9);
						}
						break;
					case 7:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(48);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(49);
						((BinaryBContext)_localctx).op = match(EQUAL);
						setState(50);
						expr(8);
						}
						break;
					case 8:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(51);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(52);
						((BinaryBContext)_localctx).op = match(NEQUAL);
						setState(53);
						expr(7);
						}
						break;
					case 9:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(54);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(55);
						((BinaryBContext)_localctx).op = match(GET);
						setState(56);
						expr(6);
						}
						break;
					case 10:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(57);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(58);
						((BinaryBContext)_localctx).op = match(LET);
						setState(59);
						expr(5);
						}
						break;
					case 11:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(60);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(61);
						((BinaryBContext)_localctx).op = match(GEQ);
						setState(62);
						expr(4);
						}
						break;
					case 12:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(63);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(64);
						((BinaryBContext)_localctx).op = match(LEQ);
						setState(65);
						expr(3);
						}
						break;
					case 13:
						{
						_localctx = new IfElseOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(66);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(67);
						match(T__2);
						setState(68);
						expr(0);
						setState(69);
						match(T__3);
						setState(70);
						expr(2);
						}
						break;
					}
					} 
				}
				setState(76);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ColContext extends LiteralContext {
		public TerminalNode COLOR() { return getToken(ExprlyParser.COLOR, 0); }
		public ColContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterCol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitCol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitCol(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolConstBContext extends LiteralContext {
		public TerminalNode BOOL() { return getToken(ExprlyParser.BOOL, 0); }
		public BoolConstBContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterBoolConstB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitBoolConstB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitBoolConstB(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ListExprContext extends LiteralContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ListExprContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterListExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitListExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitListExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConstStrContext extends LiteralContext {
		public ConstStrContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterConstStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitConstStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitConstStr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntConstContext extends LiteralContext {
		public TerminalNode INT() { return getToken(ExprlyParser.INT, 0); }
		public IntConstContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterIntConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitIntConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitIntConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FloatConstContext extends LiteralContext {
		public TerminalNode FLOAT() { return getToken(ExprlyParser.FLOAT, 0); }
		public FloatConstContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterFloatConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitFloatConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitFloatConst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_literal);
		int _la;
		try {
			int _alt;
			setState(106);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLOR:
				_localctx = new ColContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				match(COLOR);
				}
				break;
			case INT:
				_localctx = new IntConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatConstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(79);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolConstBContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(80);
				match(BOOL);
				}
				break;
			case T__4:
				_localctx = new ConstStrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(81);
				match(T__4);
				setState(90);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(85);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(82);
							matchWildcard();
							}
							} 
						}
						setState(87);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
					}
					}
					break;
				case 2:
					{
					setState(88);
					match(T__5);
					}
					break;
				case 3:
					{
					setState(89);
					match(T__2);
					}
					break;
				}
				setState(92);
				match(T__4);
				}
				break;
			case T__6:
				_localctx = new ListExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(93);
				match(T__6);
				setState(99);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(94);
						expr(0);
						setState(95);
						match(T__7);
						}
						} 
					}
					setState(101);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				}
				setState(103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 206195123362L) != 0)) {
					{
					setState(102);
					expr(0);
					}
				}

				setState(105);
				match(T__8);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConnExprContext extends ParserRuleContext {
		public ConnExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connExpr; }
	 
		public ConnExprContext() { }
		public void copyFrom(ConnExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConnContext extends ConnExprContext {
		public Token op0;
		public Token op1;
		public List<TerminalNode> STR() { return getTokens(ExprlyParser.STR); }
		public TerminalNode STR(int i) {
			return getToken(ExprlyParser.STR, i);
		}
		public List<TerminalNode> VAR() { return getTokens(ExprlyParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(ExprlyParser.VAR, i);
		}
		public ConnContext(ConnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterConn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitConn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitConn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnExprContext connExpr() throws RecognitionException {
		ConnExprContext _localctx = new ConnExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_connExpr);
		int _la;
		try {
			_localctx = new ConnContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(T__9);
			setState(109);
			((ConnContext)_localctx).op0 = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==VAR || _la==STR) ) {
				((ConnContext)_localctx).op0 = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(110);
			match(T__7);
			setState(111);
			((ConnContext)_localctx).op1 = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==VAR || _la==STR) ) {
				((ConnContext)_localctx).op1 = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunCallContext extends ParserRuleContext {
		public FunCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funCall; }
	 
		public FunCallContext() { }
		public void copyFrom(FunCallContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncContext extends FunCallContext {
		public TerminalNode FNAME() { return getToken(ExprlyParser.FNAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public FuncContext(FunCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunCallContext funCall() throws RecognitionException {
		FunCallContext _localctx = new FunCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_funCall);
		int _la;
		try {
			_localctx = new FuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(FNAME);
			setState(114);
			match(T__0);
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 206195123362L) != 0)) {
				{
				setState(115);
				expr(0);
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(116);
					match(T__7);
					setState(117);
					expr(0);
					}
					}
					setState(122);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(125);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 8);
		case 6:
			return precpred(_ctx, 7);
		case 7:
			return precpred(_ctx, 6);
		case 8:
			return precpred(_ctx, 5);
		case 9:
			return precpred(_ctx, 4);
		case 10:
			return precpred(_ctx, 3);
		case 11:
			return precpred(_ctx, 2);
		case 12:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001%\u0080\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001\u001d\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0005\u0001I\b\u0001\n\u0001\f\u0001L\t\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005"+
		"\u0002T\b\u0002\n\u0002\f\u0002W\t\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002[\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0005\u0002b\b\u0002\n\u0002\f\u0002e\t\u0002\u0001\u0002\u0003"+
		"\u0002h\b\u0002\u0001\u0002\u0003\u0002k\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004w\b\u0004\n\u0004\f\u0004z\t\u0004"+
		"\u0003\u0004|\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001U\u0001"+
		"\u0002\u0005\u0000\u0002\u0004\u0006\b\u0000\u0003\u0001\u0000$%\u0001"+
		"\u0000\"#\u0001\u0000\u0015\u0016\u009b\u0000\n\u0001\u0000\u0000\u0000"+
		"\u0002\u001c\u0001\u0000\u0000\u0000\u0004j\u0001\u0000\u0000\u0000\u0006"+
		"l\u0001\u0000\u0000\u0000\bq\u0001\u0000\u0000\u0000\n\u000b\u0003\u0002"+
		"\u0001\u0000\u000b\f\u0005\u0000\u0000\u0001\f\u0001\u0001\u0000\u0000"+
		"\u0000\r\u000e\u0006\u0001\uffff\uffff\u0000\u000e\u001d\u0005\u000f\u0000"+
		"\u0000\u000f\u001d\u0005\u000e\u0000\u0000\u0010\u001d\u0005\u0015\u0000"+
		"\u0000\u0011\u001d\u0003\u0004\u0002\u0000\u0012\u001d\u0003\u0006\u0003"+
		"\u0000\u0013\u001d\u0003\b\u0004\u0000\u0014\u0015\u0005\u0001\u0000\u0000"+
		"\u0015\u0016\u0003\u0002\u0001\u0000\u0016\u0017\u0005\u0002\u0000\u0000"+
		"\u0017\u001d\u0001\u0000\u0000\u0000\u0018\u0019\u0007\u0000\u0000\u0000"+
		"\u0019\u001d\u0003\u0002\u0001\u000f\u001a\u001b\u0005\u0019\u0000\u0000"+
		"\u001b\u001d\u0003\u0002\u0001\u000e\u001c\r\u0001\u0000\u0000\u0000\u001c"+
		"\u000f\u0001\u0000\u0000\u0000\u001c\u0010\u0001\u0000\u0000\u0000\u001c"+
		"\u0011\u0001\u0000\u0000\u0000\u001c\u0012\u0001\u0000\u0000\u0000\u001c"+
		"\u0013\u0001\u0000\u0000\u0000\u001c\u0014\u0001\u0000\u0000\u0000\u001c"+
		"\u0018\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000\u0000\u0000\u001d"+
		"J\u0001\u0000\u0000\u0000\u001e\u001f\n\r\u0000\u0000\u001f \u0005!\u0000"+
		"\u0000 I\u0003\u0002\u0001\r!\"\n\f\u0000\u0000\"#\u0005 \u0000\u0000"+
		"#I\u0003\u0002\u0001\f$%\n\u000b\u0000\u0000%&\u0007\u0001\u0000\u0000"+
		"&I\u0003\u0002\u0001\f\'(\n\n\u0000\u0000()\u0007\u0000\u0000\u0000)I"+
		"\u0003\u0002\u0001\u000b*+\n\t\u0000\u0000+,\u0005\u0017\u0000\u0000,"+
		"I\u0003\u0002\u0001\n-.\n\b\u0000\u0000./\u0005\u0018\u0000\u0000/I\u0003"+
		"\u0002\u0001\t01\n\u0007\u0000\u000012\u0005\u001a\u0000\u00002I\u0003"+
		"\u0002\u0001\b34\n\u0006\u0000\u000045\u0005\u001b\u0000\u00005I\u0003"+
		"\u0002\u0001\u000767\n\u0005\u0000\u000078\u0005\u001c\u0000\u00008I\u0003"+
		"\u0002\u0001\u00069:\n\u0004\u0000\u0000:;\u0005\u001d\u0000\u0000;I\u0003"+
		"\u0002\u0001\u0005<=\n\u0003\u0000\u0000=>\u0005\u001e\u0000\u0000>I\u0003"+
		"\u0002\u0001\u0004?@\n\u0002\u0000\u0000@A\u0005\u001f\u0000\u0000AI\u0003"+
		"\u0002\u0001\u0003BC\n\u0001\u0000\u0000CD\u0005\u0003\u0000\u0000DE\u0003"+
		"\u0002\u0001\u0000EF\u0005\u0004\u0000\u0000FG\u0003\u0002\u0001\u0002"+
		"GI\u0001\u0000\u0000\u0000H\u001e\u0001\u0000\u0000\u0000H!\u0001\u0000"+
		"\u0000\u0000H$\u0001\u0000\u0000\u0000H\'\u0001\u0000\u0000\u0000H*\u0001"+
		"\u0000\u0000\u0000H-\u0001\u0000\u0000\u0000H0\u0001\u0000\u0000\u0000"+
		"H3\u0001\u0000\u0000\u0000H6\u0001\u0000\u0000\u0000H9\u0001\u0000\u0000"+
		"\u0000H<\u0001\u0000\u0000\u0000H?\u0001\u0000\u0000\u0000HB\u0001\u0000"+
		"\u0000\u0000IL\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000JK\u0001"+
		"\u0000\u0000\u0000K\u0003\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000"+
		"\u0000Mk\u0005\u0012\u0000\u0000Nk\u0005\u0010\u0000\u0000Ok\u0005\u0011"+
		"\u0000\u0000Pk\u0005\u0013\u0000\u0000QZ\u0005\u0005\u0000\u0000RT\t\u0000"+
		"\u0000\u0000SR\u0001\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000UV\u0001"+
		"\u0000\u0000\u0000US\u0001\u0000\u0000\u0000V[\u0001\u0000\u0000\u0000"+
		"WU\u0001\u0000\u0000\u0000X[\u0005\u0006\u0000\u0000Y[\u0005\u0003\u0000"+
		"\u0000ZU\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000ZY\u0001\u0000"+
		"\u0000\u0000[\\\u0001\u0000\u0000\u0000\\k\u0005\u0005\u0000\u0000]c\u0005"+
		"\u0007\u0000\u0000^_\u0003\u0002\u0001\u0000_`\u0005\b\u0000\u0000`b\u0001"+
		"\u0000\u0000\u0000a^\u0001\u0000\u0000\u0000be\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000dg\u0001\u0000\u0000"+
		"\u0000ec\u0001\u0000\u0000\u0000fh\u0003\u0002\u0001\u0000gf\u0001\u0000"+
		"\u0000\u0000gh\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000ik\u0005"+
		"\t\u0000\u0000jM\u0001\u0000\u0000\u0000jN\u0001\u0000\u0000\u0000jO\u0001"+
		"\u0000\u0000\u0000jP\u0001\u0000\u0000\u0000jQ\u0001\u0000\u0000\u0000"+
		"j]\u0001\u0000\u0000\u0000k\u0005\u0001\u0000\u0000\u0000lm\u0005\n\u0000"+
		"\u0000mn\u0007\u0002\u0000\u0000no\u0005\b\u0000\u0000op\u0007\u0002\u0000"+
		"\u0000p\u0007\u0001\u0000\u0000\u0000qr\u0005\r\u0000\u0000r{\u0005\u0001"+
		"\u0000\u0000sx\u0003\u0002\u0001\u0000tu\u0005\b\u0000\u0000uw\u0003\u0002"+
		"\u0001\u0000vt\u0001\u0000\u0000\u0000wz\u0001\u0000\u0000\u0000xv\u0001"+
		"\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y|\u0001\u0000\u0000\u0000"+
		"zx\u0001\u0000\u0000\u0000{s\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000"+
		"\u0000|}\u0001\u0000\u0000\u0000}~\u0005\u0002\u0000\u0000~\t\u0001\u0000"+
		"\u0000\u0000\n\u001cHJUZcgjx{";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}