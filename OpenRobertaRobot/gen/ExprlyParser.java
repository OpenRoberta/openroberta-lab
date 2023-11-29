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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, MICROBIT_STMT=17, 
		EV3_STMT=18, IF=19, ELSEIF=20, ELSE=21, STEP=22, WHILE=23, REPEATFOR=24, 
		REPEATFOREACH=25, BREAK=26, CONTINUE=27, WAIT=28, ORWAITFOR=29, WAITMS=30, 
		RETURN=31, PRIMITIVETYPE=32, NEWLINE=33, WS=34, FNAME=35, FNAMESTMT=36, 
		CONST=37, NULL=38, INT=39, FLOAT=40, COLOR=41, BOOL=42, HEX=43, MICROBITV2_SENSORSEXPR=44, 
		MICROBITV2_SENSORSTMT=45, VAR=46, FUNCTIONNAME=47, STR=48, SET=49, AND=50, 
		OR=51, NOT=52, EQUAL=53, NEQUAL=54, GET=55, LET=56, GEQ=57, LEQ=58, MOD=59, 
		POW=60, MUL=61, DIV=62, ADD=63, SUB=64, ACCELEROMETER_SENSOR=65, COLOR_SENSOR=66, 
		COMPASS_CALIBRATE=67, COMPASS_SENSOR=68, DETECT_MARK_SENSOR=69, DROP_SENSOR=70, 
		EHT_COLOR_SENSOR=71, ENCODER_RESET=72, ENCODER_SENSOR=73, ENVIRONMENTAL_SENSOR=74, 
		GESTURE_SENSOR=75, GET_LINE_SENSOR=76, GYRO_RESET=77, GYRO_SENSOR=78, 
		HT_COLOR_SENSOR=79, HUMIDITY_SENSOR=80, IR_SEEKER_SENSOR=81, INFRARED_SENSOR=82, 
		KEYS_SENSOR=83, LIGHT_SENSOR=84, MOISTURE_SENSOR=85, MOTION_SENSOR=86, 
		PARTICLE_SENSOR=87, PIN_GET_VALUE_SENSOR=88, PIN_TOUCH_SENSOR=89, PULSE_SENSOR=90, 
		RFID_SENSOR=91, SOUND_SENSOR=92, TEMPERATURE_SENSOR=93, TIMER_RESET=94, 
		TIMER_SENSOR=95, TOUCH_SENSOR=96, ULTRASONIC_SENSOR=97, VEML_LIGHT_SENSOR=98, 
		VOLTAGE_SENSOR=99, CAMERA_SENSOR=100, CAMERA_THRESHOLD=101, CODE_PAD_SENSOR=102, 
		COLOUR_BLOB=103, DETECT_FACE_INFORMATION=104, DETECT_FACE_SENSOR=105, 
		ELECTRIC_CURRENT_SENSOR=106, FSR_SENSOR=107, GPS_SENSOR=108, GYRO_RESET_AXIS=109, 
		JOYSTICK=110, LOGO_TOUCH_SENSOR=111, MARKER_INFORMATION=112, NAO_MARK_INFORMATION=113, 
		ODOMETRY_SENSOR=114, ODOMETRY_SENSOR_RESET=115, OPTICAL_SENSOR=116, PIN_SET_TOUCH_MODE=117, 
		QUAD_RGB_SENSOR=118, RADIO_RSSI_SENSOR=119, RECOGNIZE_WORD=120, RESET_SENSOR=121, 
		SOUND_RECORD=122, TAP_SENSOR=123;
	public static final int
		RULE_expression = 0, RULE_expr = 1, RULE_robotExpr = 2, RULE_robotSensorExpr = 3, 
		RULE_statementList = 4, RULE_stmt = 5, RULE_robotStmt = 6, RULE_robotSensorStmt = 7, 
		RULE_program = 8, RULE_declaration = 9, RULE_mainBlock = 10, RULE_literal = 11, 
		RULE_connExpr = 12, RULE_funCall = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "expr", "robotExpr", "robotSensorExpr", "statementList", 
			"stmt", "robotStmt", "robotSensorStmt", "program", "declaration", "mainBlock", 
			"literal", "connExpr", "funCall"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'?'", "':'", "'microbitV2'", "'.'", "','", "';'", 
			"'{'", "'}'", "'void'", "'main'", "'\"'", "'['", "']'", "'connect'", 
			"'showImage'", "'moveForward'", "'if'", null, "'else'", "'++'", "'while'", 
			"'for'", null, "'break'", "'continue'", "'waitUntil'", "'orWaitFor'", 
			"'wait ms'", "'return'", null, null, null, null, null, null, "'null'", 
			null, null, null, null, null, null, null, null, null, null, "'='", "'&&'", 
			"'||'", "'!'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "'%'", "'^'", 
			"'*'", "'/'", "'+'", "'-'", "'accelerometerSensor'", "'colorSensor'", 
			"'compassCalibrate'", "'compassSensor'", "'detectMarkSensor'", "'dropSensor'", 
			"'ehtColorSensor'", "'encoderReset'", "'encoderSensor'", "'environmentalSensor'", 
			"'gestureSensor'", "'getLineSensor'", "'gyroReset'", "'gyroSensor'", 
			"'htColorSensor'", "'humiditySensor'", "'irSeekerSensor'", "'infraredSensor'", 
			"'keysSensor'", "'lightSensor'", "'moistureSensor'", "'motionSensor'", 
			"'particleSensor'", "'pinGetValueSensor'", "'pinTouchSensor'", "'pulseSensor'", 
			"'rfidSensor'", "'soundSensor'", "'temperatureSensor'", "'timerReset'", 
			"'timerSensor'", "'touchSensor'", "'ultrasonicSensor'", "'vemlLightSensor'", 
			"'voltageSensor'", "'cameraSensor'", "'cameraThreshold'", "'codePadSensor'", 
			"'colourBlob'", "'detectFaceInformation'", "'detectFaceSensor'", "'electricCurrentSensor'", 
			"'fsrSensor'", "'gpsSensor'", "'gyroResetAxis'", "'joystick'", "'logoTouchSensor'", 
			"'markerInformation'", "'naoMarkInformation'", "'odometrySensor'", "'odometrySensorReset'", 
			"'opticalSensor'", "'pinSetTouchMode'", "'quadRGBSensor'", "'radioRssiSensor'", 
			"'recognizeWord'", "'resetSensor'", "'soundRecord'", "'tapSensor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "MICROBIT_STMT", "EV3_STMT", "IF", "ELSEIF", 
			"ELSE", "STEP", "WHILE", "REPEATFOR", "REPEATFOREACH", "BREAK", "CONTINUE", 
			"WAIT", "ORWAITFOR", "WAITMS", "RETURN", "PRIMITIVETYPE", "NEWLINE", 
			"WS", "FNAME", "FNAMESTMT", "CONST", "NULL", "INT", "FLOAT", "COLOR", 
			"BOOL", "HEX", "MICROBITV2_SENSORSEXPR", "MICROBITV2_SENSORSTMT", "VAR", 
			"FUNCTIONNAME", "STR", "SET", "AND", "OR", "NOT", "EQUAL", "NEQUAL", 
			"GET", "LET", "GEQ", "LEQ", "MOD", "POW", "MUL", "DIV", "ADD", "SUB", 
			"ACCELEROMETER_SENSOR", "COLOR_SENSOR", "COMPASS_CALIBRATE", "COMPASS_SENSOR", 
			"DETECT_MARK_SENSOR", "DROP_SENSOR", "EHT_COLOR_SENSOR", "ENCODER_RESET", 
			"ENCODER_SENSOR", "ENVIRONMENTAL_SENSOR", "GESTURE_SENSOR", "GET_LINE_SENSOR", 
			"GYRO_RESET", "GYRO_SENSOR", "HT_COLOR_SENSOR", "HUMIDITY_SENSOR", "IR_SEEKER_SENSOR", 
			"INFRARED_SENSOR", "KEYS_SENSOR", "LIGHT_SENSOR", "MOISTURE_SENSOR", 
			"MOTION_SENSOR", "PARTICLE_SENSOR", "PIN_GET_VALUE_SENSOR", "PIN_TOUCH_SENSOR", 
			"PULSE_SENSOR", "RFID_SENSOR", "SOUND_SENSOR", "TEMPERATURE_SENSOR", 
			"TIMER_RESET", "TIMER_SENSOR", "TOUCH_SENSOR", "ULTRASONIC_SENSOR", "VEML_LIGHT_SENSOR", 
			"VOLTAGE_SENSOR", "CAMERA_SENSOR", "CAMERA_THRESHOLD", "CODE_PAD_SENSOR", 
			"COLOUR_BLOB", "DETECT_FACE_INFORMATION", "DETECT_FACE_SENSOR", "ELECTRIC_CURRENT_SENSOR", 
			"FSR_SENSOR", "GPS_SENSOR", "GYRO_RESET_AXIS", "JOYSTICK", "LOGO_TOUCH_SENSOR", 
			"MARKER_INFORMATION", "NAO_MARK_INFORMATION", "ODOMETRY_SENSOR", "ODOMETRY_SENSOR_RESET", 
			"OPTICAL_SENSOR", "PIN_SET_TOUCH_MODE", "QUAD_RGB_SENSOR", "RADIO_RSSI_SENSOR", 
			"RECOGNIZE_WORD", "RESET_SENSOR", "SOUND_RECORD", "TAP_SENSOR"
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
			setState(28);
			expr(0);
			setState(29);
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
	public static class ParamsMethodContext extends ExprContext {
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public ParamsMethodContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterParamsMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitParamsMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitParamsMethod(this);
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
	@SuppressWarnings("CheckReturnValue")
	public static class RobotExpressionContext extends ExprContext {
		public RobotExprContext robotExpr() {
			return getRuleContext(RobotExprContext.class,0);
		}
		public RobotExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotExpression(this);
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
			setState(49);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				{
				_localctx = new NullConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(32);
				match(NULL);
				}
				break;
			case CONST:
				{
				_localctx = new MathConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(33);
				match(CONST);
				}
				break;
			case VAR:
				{
				_localctx = new VarNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34);
				match(VAR);
				}
				break;
			case T__12:
			case T__13:
			case INT:
			case FLOAT:
			case COLOR:
			case BOOL:
				{
				_localctx = new LiteralExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(35);
				literal();
				}
				break;
			case T__15:
				{
				_localctx = new ConnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(36);
				connExpr();
				}
				break;
			case FNAME:
			case FUNCTIONNAME:
				{
				_localctx = new FuncExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37);
				funCall();
				}
				break;
			case T__0:
				{
				_localctx = new ParentheseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38);
				match(T__0);
				setState(39);
				expr(0);
				setState(40);
				match(T__1);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new UnaryNContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42);
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
				setState(43);
				expr(17);
				}
				break;
			case NOT:
				{
				_localctx = new UnaryBContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(44);
				((UnaryBContext)_localctx).op = match(NOT);
				setState(45);
				expr(16);
				}
				break;
			case PRIMITIVETYPE:
				{
				_localctx = new ParamsMethodContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(46);
				match(PRIMITIVETYPE);
				setState(47);
				match(VAR);
				}
				break;
			case T__4:
				{
				_localctx = new RobotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48);
				robotExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(95);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(93);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(51);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(52);
						((BinaryNContext)_localctx).op = match(POW);
						setState(53);
						expr(15);
						}
						break;
					case 2:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(54);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(55);
						((BinaryNContext)_localctx).op = match(MOD);
						setState(56);
						expr(14);
						}
						break;
					case 3:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(57);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(58);
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
						setState(59);
						expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(60);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(61);
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
						setState(62);
						expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(63);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(64);
						((BinaryBContext)_localctx).op = match(AND);
						setState(65);
						expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(66);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(67);
						((BinaryBContext)_localctx).op = match(OR);
						setState(68);
						expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(69);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(70);
						((BinaryBContext)_localctx).op = match(EQUAL);
						setState(71);
						expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(72);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(73);
						((BinaryBContext)_localctx).op = match(NEQUAL);
						setState(74);
						expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(75);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(76);
						((BinaryBContext)_localctx).op = match(GET);
						setState(77);
						expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(78);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(79);
						((BinaryBContext)_localctx).op = match(LET);
						setState(80);
						expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(81);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(82);
						((BinaryBContext)_localctx).op = match(GEQ);
						setState(83);
						expr(6);
						}
						break;
					case 12:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(84);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(85);
						((BinaryBContext)_localctx).op = match(LEQ);
						setState(86);
						expr(5);
						}
						break;
					case 13:
						{
						_localctx = new IfElseOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(87);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(88);
						match(T__2);
						setState(89);
						expr(0);
						setState(90);
						match(T__3);
						setState(91);
						expr(4);
						}
						break;
					}
					} 
				}
				setState(97);
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
	public static class RobotExprContext extends ParserRuleContext {
		public RobotSensorExprContext robotSensorExpr() {
			return getRuleContext(RobotSensorExprContext.class,0);
		}
		public RobotExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotExprContext robotExpr() throws RecognitionException {
		RobotExprContext _localctx = new RobotExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_robotExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			robotSensorExpr();
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
	public static class RobotSensorExprContext extends ParserRuleContext {
		public RobotSensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotSensorExpr; }
	 
		public RobotSensorExprContext() { }
		public void copyFrom(RobotSensorExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MicrobitV2SensorExpressionContext extends RobotSensorExprContext {
		public TerminalNode MICROBITV2_SENSORSEXPR() { return getToken(ExprlyParser.MICROBITV2_SENSORSEXPR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MicrobitV2SensorExpressionContext(RobotSensorExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMicrobitV2SensorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMicrobitV2SensorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMicrobitV2SensorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotSensorExprContext robotSensorExpr() throws RecognitionException {
		RobotSensorExprContext _localctx = new RobotSensorExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_robotSensorExpr);
		int _la;
		try {
			_localctx = new MicrobitV2SensorExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(T__4);
			setState(101);
			match(T__5);
			setState(102);
			match(MICROBITV2_SENSORSEXPR);
			setState(103);
			match(T__0);
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
				{
				setState(104);
				expr(0);
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(105);
					match(T__6);
					setState(106);
					expr(0);
					}
					}
					setState(111);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(114);
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

	@SuppressWarnings("CheckReturnValue")
	public static class StatementListContext extends ParserRuleContext {
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public StatementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterStatementList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitStatementList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitStatementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementListContext statementList() throws RecognitionException {
		StatementListContext _localctx = new StatementListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70443361370112L) != 0)) {
				{
				{
				setState(116);
				stmt();
				setState(117);
				match(T__7);
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
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
	public static class StmtContext extends ParserRuleContext {
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	 
		public StmtContext() { }
		public void copyFrom(StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FlowControlContext extends StmtContext {
		public Token op;
		public TerminalNode BREAK() { return getToken(ExprlyParser.BREAK, 0); }
		public TerminalNode CONTINUE() { return getToken(ExprlyParser.CONTINUE, 0); }
		public FlowControlContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterFlowControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitFlowControl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitFlowControl(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StmtFuncContext extends StmtContext {
		public TerminalNode FNAMESTMT() { return getToken(ExprlyParser.FNAMESTMT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StmtFuncContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterStmtFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitStmtFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitStmtFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncUserContext extends StmtContext {
		public Token op;
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public TerminalNode FUNCTIONNAME() { return getToken(ExprlyParser.FUNCTIONNAME, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RETURN() { return getToken(ExprlyParser.RETURN, 0); }
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public FuncUserContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterFuncUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitFuncUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitFuncUser(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WaitTimeStatementContext extends StmtContext {
		public TerminalNode WAITMS() { return getToken(ExprlyParser.WAITMS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public WaitTimeStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterWaitTimeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitWaitTimeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitWaitTimeStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryVarAssignContext extends StmtContext {
		public Token op;
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SET() { return getToken(ExprlyParser.SET, 0); }
		public BinaryVarAssignContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterBinaryVarAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitBinaryVarAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitBinaryVarAssign(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatForContext extends StmtContext {
		public Token op;
		public TerminalNode REPEATFOR() { return getToken(ExprlyParser.REPEATFOR, 0); }
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public List<TerminalNode> VAR() { return getTokens(ExprlyParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(ExprlyParser.VAR, i);
		}
		public List<TerminalNode> SET() { return getTokens(ExprlyParser.SET); }
		public TerminalNode SET(int i) {
			return getToken(ExprlyParser.SET, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LET() { return getToken(ExprlyParser.LET, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public TerminalNode STEP() { return getToken(ExprlyParser.STEP, 0); }
		public TerminalNode ADD() { return getToken(ExprlyParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(ExprlyParser.SUB, 0); }
		public RepeatForContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRepeatFor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRepeatFor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRepeatFor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatStatementContext extends StmtContext {
		public TerminalNode WHILE() { return getToken(ExprlyParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public RepeatStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRepeatStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRepeatStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRepeatStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConditionStatementBlockContext extends StmtContext {
		public Token op;
		public TerminalNode IF() { return getToken(ExprlyParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<StatementListContext> statementList() {
			return getRuleContexts(StatementListContext.class);
		}
		public StatementListContext statementList(int i) {
			return getRuleContext(StatementListContext.class,i);
		}
		public List<TerminalNode> ELSEIF() { return getTokens(ExprlyParser.ELSEIF); }
		public TerminalNode ELSEIF(int i) {
			return getToken(ExprlyParser.ELSEIF, i);
		}
		public TerminalNode ELSE() { return getToken(ExprlyParser.ELSE, 0); }
		public ConditionStatementBlockContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterConditionStatementBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitConditionStatementBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitConditionStatementBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatForEachContext extends StmtContext {
		public TerminalNode REPEATFOREACH() { return getToken(ExprlyParser.REPEATFOREACH, 0); }
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public RepeatForEachContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRepeatForEach(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRepeatForEach(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRepeatForEach(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WaitStatementContext extends StmtContext {
		public Token op;
		public TerminalNode WAIT() { return getToken(ExprlyParser.WAIT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<StatementListContext> statementList() {
			return getRuleContexts(StatementListContext.class);
		}
		public StatementListContext statementList(int i) {
			return getRuleContext(StatementListContext.class,i);
		}
		public List<TerminalNode> ORWAITFOR() { return getTokens(ExprlyParser.ORWAITFOR); }
		public TerminalNode ORWAITFOR(int i) {
			return getToken(ExprlyParser.ORWAITFOR, i);
		}
		public WaitStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterWaitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitWaitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitWaitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserFuncIfStmtContext extends StmtContext {
		public TerminalNode IF() { return getToken(ExprlyParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RETURN() { return getToken(ExprlyParser.RETURN, 0); }
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public UserFuncIfStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterUserFuncIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitUserFuncIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitUserFuncIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_stmt);
		int _la;
		try {
			setState(278);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				_localctx = new StmtFuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				match(FNAMESTMT);
				setState(125);
				match(T__0);
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
					{
					setState(126);
					expr(0);
					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__6) {
						{
						{
						setState(127);
						match(T__6);
						setState(128);
						expr(0);
						}
						}
						setState(133);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(136);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new BinaryVarAssignContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(137);
				match(VAR);
				setState(138);
				((BinaryVarAssignContext)_localctx).op = match(SET);
				setState(139);
				expr(0);
				}
				break;
			case 3:
				_localctx = new ConditionStatementBlockContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(140);
				match(IF);
				setState(141);
				match(T__0);
				setState(142);
				expr(0);
				setState(143);
				match(T__1);
				setState(144);
				match(T__8);
				setState(145);
				statementList();
				setState(146);
				match(T__9);
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELSEIF) {
					{
					{
					setState(147);
					match(ELSEIF);
					setState(148);
					match(T__0);
					setState(149);
					expr(0);
					setState(150);
					match(T__1);
					setState(151);
					match(T__8);
					setState(152);
					statementList();
					setState(153);
					match(T__9);
					}
					}
					setState(159);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(160);
					((ConditionStatementBlockContext)_localctx).op = match(ELSE);
					setState(161);
					match(T__8);
					setState(162);
					statementList();
					setState(163);
					match(T__9);
					}
				}

				}
				break;
			case 4:
				_localctx = new RepeatForContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(167);
				match(REPEATFOR);
				setState(168);
				match(T__0);
				setState(169);
				match(PRIMITIVETYPE);
				setState(170);
				match(VAR);
				setState(171);
				match(SET);
				setState(172);
				expr(0);
				setState(173);
				match(T__7);
				setState(174);
				match(VAR);
				setState(175);
				match(LET);
				setState(176);
				expr(0);
				setState(177);
				match(T__7);
				setState(189);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
				case 1:
					{
					setState(178);
					expr(0);
					setState(179);
					((RepeatForContext)_localctx).op = match(STEP);
					}
					break;
				case 2:
					{
					setState(181);
					match(VAR);
					setState(182);
					match(SET);
					setState(183);
					match(VAR);
					setState(184);
					((RepeatForContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==ADD || _la==SUB) ) {
						((RepeatForContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(185);
					expr(0);
					}
					break;
				case 3:
					{
					setState(186);
					match(VAR);
					setState(187);
					match(SET);
					setState(188);
					expr(0);
					}
					break;
				}
				setState(191);
				match(T__1);
				setState(192);
				match(T__8);
				setState(193);
				statementList();
				setState(194);
				match(T__9);
				}
				break;
			case 5:
				_localctx = new RepeatStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(196);
				match(WHILE);
				setState(197);
				match(T__0);
				setState(198);
				expr(0);
				setState(199);
				match(T__1);
				setState(200);
				match(T__8);
				setState(201);
				statementList();
				setState(202);
				match(T__9);
				}
				break;
			case 6:
				_localctx = new RepeatForEachContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(204);
				match(REPEATFOREACH);
				setState(205);
				match(T__0);
				setState(206);
				match(PRIMITIVETYPE);
				setState(207);
				match(VAR);
				setState(208);
				match(T__3);
				setState(209);
				expr(0);
				setState(210);
				match(T__1);
				setState(211);
				match(T__8);
				setState(212);
				statementList();
				setState(213);
				match(T__9);
				}
				break;
			case 7:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(215);
				match(WAIT);
				setState(216);
				match(T__0);
				setState(217);
				expr(0);
				setState(218);
				match(T__1);
				setState(219);
				match(T__8);
				setState(220);
				statementList();
				setState(221);
				match(T__9);
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ORWAITFOR) {
					{
					{
					setState(222);
					((WaitStatementContext)_localctx).op = match(ORWAITFOR);
					setState(223);
					match(T__0);
					setState(224);
					expr(0);
					setState(225);
					match(T__1);
					setState(226);
					match(T__8);
					setState(227);
					statementList();
					setState(228);
					match(T__9);
					}
					}
					setState(234);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 8:
				_localctx = new FlowControlContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(235);
				((FlowControlContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==BREAK || _la==CONTINUE) ) {
					((FlowControlContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 9:
				_localctx = new WaitTimeStatementContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(236);
				match(WAITMS);
				setState(237);
				match(T__0);
				setState(238);
				expr(0);
				setState(239);
				match(T__1);
				}
				break;
			case 10:
				_localctx = new FuncUserContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(241);
				match(PRIMITIVETYPE);
				setState(242);
				match(FUNCTIONNAME);
				setState(243);
				match(T__0);
				setState(254);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
					{
					{
					setState(244);
					expr(0);
					setState(249);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__6) {
						{
						{
						setState(245);
						match(T__6);
						setState(246);
						expr(0);
						}
						}
						setState(251);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					setState(256);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(257);
				match(T__1);
				setState(258);
				match(T__8);
				setState(259);
				statementList();
				setState(265);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==RETURN) {
					{
					setState(260);
					((FuncUserContext)_localctx).op = match(RETURN);
					setState(263);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						setState(261);
						match(VAR);
						}
						break;
					case 2:
						{
						setState(262);
						expr(0);
						}
						break;
					}
					}
				}

				setState(267);
				match(T__9);
				}
				break;
			case 11:
				_localctx = new UserFuncIfStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(269);
				match(IF);
				setState(270);
				match(T__0);
				setState(271);
				expr(0);
				setState(272);
				match(T__1);
				setState(273);
				match(RETURN);
				setState(276);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(274);
					match(VAR);
					}
					break;
				case 2:
					{
					setState(275);
					expr(0);
					}
					break;
				}
				}
				break;
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
	public static class RobotStmtContext extends ParserRuleContext {
		public RobotSensorStmtContext robotSensorStmt() {
			return getRuleContext(RobotSensorStmtContext.class,0);
		}
		public RobotStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotStmtContext robotStmt() throws RecognitionException {
		RobotStmtContext _localctx = new RobotStmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_robotStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			robotSensorStmt();
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
	public static class RobotSensorStmtContext extends ParserRuleContext {
		public RobotSensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotSensorStmt; }
	 
		public RobotSensorStmtContext() { }
		public void copyFrom(RobotSensorStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MicrobitV2SensorStatementContext extends RobotSensorStmtContext {
		public TerminalNode MICROBITV2_SENSORSTMT() { return getToken(ExprlyParser.MICROBITV2_SENSORSTMT, 0); }
		public MicrobitV2SensorStatementContext(RobotSensorStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMicrobitV2SensorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMicrobitV2SensorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMicrobitV2SensorStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotSensorStmtContext robotSensorStmt() throws RecognitionException {
		RobotSensorStmtContext _localctx = new RobotSensorStmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_robotSensorStmt);
		try {
			_localctx = new MicrobitV2SensorStatementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(T__4);
			setState(283);
			match(T__5);
			setState(284);
			match(MICROBITV2_SENSORSTMT);
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
	public static class ProgramContext extends ParserRuleContext {
		public MainBlockContext mainBlock() {
			return getRuleContext(MainBlockContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ExprlyParser.EOF, 0); }
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(286);
				declaration();
				}
				}
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(292);
			mainBlock();
			setState(293);
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
	public static class DeclarationContext extends ParserRuleContext {
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
	 
		public DeclarationContext() { }
		public void copyFrom(DeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends DeclarationContext {
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public TerminalNode VAR() { return getToken(ExprlyParser.VAR, 0); }
		public TerminalNode SET() { return getToken(ExprlyParser.SET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VariableDeclarationContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_declaration);
		int _la;
		try {
			_localctx = new VariableDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(PRIMITIVETYPE);
			setState(296);
			match(VAR);
			setState(299);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SET) {
				{
				setState(297);
				match(SET);
				setState(298);
				expr(0);
				}
			}

			setState(301);
			match(T__7);
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
	public static class MainBlockContext extends ParserRuleContext {
		public MainBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainBlock; }
	 
		public MainBlockContext() { }
		public void copyFrom(MainBlockContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MainFuncContext extends MainBlockContext {
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public MainFuncContext(MainBlockContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMainFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMainFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMainFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainBlockContext mainBlock() throws RecognitionException {
		MainBlockContext _localctx = new MainBlockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_mainBlock);
		try {
			_localctx = new MainFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(T__10);
			setState(304);
			match(T__11);
			setState(305);
			match(T__0);
			setState(306);
			match(T__1);
			setState(307);
			match(T__8);
			setState(308);
			statementList();
			setState(309);
			match(T__9);
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
		enterRule(_localctx, 22, RULE_literal);
		int _la;
		try {
			int _alt;
			setState(340);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLOR:
				_localctx = new ColContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(311);
				match(COLOR);
				}
				break;
			case INT:
				_localctx = new IntConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(312);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatConstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(313);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolConstBContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(314);
				match(BOOL);
				}
				break;
			case T__12:
				_localctx = new ConstStrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(315);
				match(T__12);
				setState(324);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(319);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(316);
							matchWildcard();
							}
							} 
						}
						setState(321);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
					}
					}
					break;
				case 2:
					{
					setState(322);
					match(T__5);
					}
					break;
				case 3:
					{
					setState(323);
					match(T__2);
					}
					break;
				}
				setState(326);
				match(T__12);
				}
				break;
			case T__13:
				_localctx = new ListExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(327);
				match(T__13);
				setState(333);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(328);
						expr(0);
						setState(329);
						match(T__6);
						}
						} 
					}
					setState(335);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
				}
				setState(337);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
					{
					setState(336);
					expr(0);
					}
				}

				setState(339);
				match(T__14);
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
		enterRule(_localctx, 24, RULE_connExpr);
		int _la;
		try {
			_localctx = new ConnContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			match(T__15);
			setState(343);
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
			setState(344);
			match(T__6);
			setState(345);
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
	@SuppressWarnings("CheckReturnValue")
	public static class UserDefCallContext extends FunCallContext {
		public TerminalNode FUNCTIONNAME() { return getToken(ExprlyParser.FUNCTIONNAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public UserDefCallContext(FunCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterUserDefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitUserDefCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitUserDefCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunCallContext funCall() throws RecognitionException {
		FunCallContext _localctx = new FunCallContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_funCall);
		int _la;
		try {
			setState(373);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FNAME:
				_localctx = new FuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				match(FNAME);
				setState(348);
				match(T__0);
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
					{
					setState(349);
					expr(0);
					setState(354);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__6) {
						{
						{
						setState(350);
						match(T__6);
						setState(351);
						expr(0);
						}
						}
						setState(356);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(359);
				match(T__1);
				}
				break;
			case FUNCTIONNAME:
				_localctx = new UserDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				match(FUNCTIONNAME);
				setState(361);
				match(T__0);
				setState(370);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 1)) & ~0x3f) == 0 && ((1L << (_la - 1)) & -4609324316843003887L) != 0)) {
					{
					setState(362);
					expr(0);
					setState(367);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__6) {
						{
						{
						setState(363);
						match(T__6);
						setState(364);
						expr(0);
						}
						}
						setState(369);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(372);
				match(T__1);
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
			return precpred(_ctx, 15);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 11);
		case 5:
			return precpred(_ctx, 10);
		case 6:
			return precpred(_ctx, 9);
		case 7:
			return precpred(_ctx, 8);
		case 8:
			return precpred(_ctx, 7);
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 5);
		case 11:
			return precpred(_ctx, 4);
		case 12:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001{\u0178\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001"+
		"2\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001^\b\u0001\n\u0001\f\u0001a\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0005\u0003l\b\u0003\n\u0003\f\u0003o\t\u0003"+
		"\u0003\u0003q\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0005\u0004x\b\u0004\n\u0004\f\u0004{\t\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u0082\b\u0005"+
		"\n\u0005\f\u0005\u0085\t\u0005\u0003\u0005\u0087\b\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005\u009c\b\u0005\n\u0005\f\u0005\u009f\t\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00a6\b\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00be\b\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u00e7\b\u0005\n\u0005\f\u0005\u00ea"+
		"\t\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0005\u0005\u00f8\b\u0005\n\u0005\f\u0005\u00fb\t\u0005\u0005\u0005"+
		"\u00fd\b\u0005\n\u0005\f\u0005\u0100\t\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u0108\b\u0005\u0003"+
		"\u0005\u010a\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u0115"+
		"\b\u0005\u0003\u0005\u0117\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0005\b\u0120\b\b\n\b\f\b"+
		"\u0123\t\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\t\u012c\b\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0005\u000b\u013e\b\u000b\n\u000b\f\u000b\u0141\t\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000b\u0145\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u014c\b\u000b\n\u000b"+
		"\f\u000b\u014f\t\u000b\u0001\u000b\u0003\u000b\u0152\b\u000b\u0001\u000b"+
		"\u0003\u000b\u0155\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u0161\b\r\n\r\f\r\u0164\t\r"+
		"\u0003\r\u0166\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u016e\b\r\n\r\f\r\u0171\t\r\u0003\r\u0173\b\r\u0001\r\u0003\r\u0176"+
		"\b\r\u0001\r\u0001\u013f\u0001\u0002\u000e\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0004\u0001\u0000?"+
		"@\u0001\u0000=>\u0001\u0000\u001a\u001b\u0002\u0000..00\u01ab\u0000\u001c"+
		"\u0001\u0000\u0000\u0000\u00021\u0001\u0000\u0000\u0000\u0004b\u0001\u0000"+
		"\u0000\u0000\u0006d\u0001\u0000\u0000\u0000\by\u0001\u0000\u0000\u0000"+
		"\n\u0116\u0001\u0000\u0000\u0000\f\u0118\u0001\u0000\u0000\u0000\u000e"+
		"\u011a\u0001\u0000\u0000\u0000\u0010\u0121\u0001\u0000\u0000\u0000\u0012"+
		"\u0127\u0001\u0000\u0000\u0000\u0014\u012f\u0001\u0000\u0000\u0000\u0016"+
		"\u0154\u0001\u0000\u0000\u0000\u0018\u0156\u0001\u0000\u0000\u0000\u001a"+
		"\u0175\u0001\u0000\u0000\u0000\u001c\u001d\u0003\u0002\u0001\u0000\u001d"+
		"\u001e\u0005\u0000\u0000\u0001\u001e\u0001\u0001\u0000\u0000\u0000\u001f"+
		" \u0006\u0001\uffff\uffff\u0000 2\u0005&\u0000\u0000!2\u0005%\u0000\u0000"+
		"\"2\u0005.\u0000\u0000#2\u0003\u0016\u000b\u0000$2\u0003\u0018\f\u0000"+
		"%2\u0003\u001a\r\u0000&\'\u0005\u0001\u0000\u0000\'(\u0003\u0002\u0001"+
		"\u0000()\u0005\u0002\u0000\u0000)2\u0001\u0000\u0000\u0000*+\u0007\u0000"+
		"\u0000\u0000+2\u0003\u0002\u0001\u0011,-\u00054\u0000\u0000-2\u0003\u0002"+
		"\u0001\u0010./\u0005 \u0000\u0000/2\u0005.\u0000\u000002\u0003\u0004\u0002"+
		"\u00001\u001f\u0001\u0000\u0000\u00001!\u0001\u0000\u0000\u00001\"\u0001"+
		"\u0000\u0000\u00001#\u0001\u0000\u0000\u00001$\u0001\u0000\u0000\u0000"+
		"1%\u0001\u0000\u0000\u00001&\u0001\u0000\u0000\u00001*\u0001\u0000\u0000"+
		"\u00001,\u0001\u0000\u0000\u00001.\u0001\u0000\u0000\u000010\u0001\u0000"+
		"\u0000\u00002_\u0001\u0000\u0000\u000034\n\u000f\u0000\u000045\u0005<"+
		"\u0000\u00005^\u0003\u0002\u0001\u000f67\n\u000e\u0000\u000078\u0005;"+
		"\u0000\u00008^\u0003\u0002\u0001\u000e9:\n\r\u0000\u0000:;\u0007\u0001"+
		"\u0000\u0000;^\u0003\u0002\u0001\u000e<=\n\f\u0000\u0000=>\u0007\u0000"+
		"\u0000\u0000>^\u0003\u0002\u0001\r?@\n\u000b\u0000\u0000@A\u00052\u0000"+
		"\u0000A^\u0003\u0002\u0001\fBC\n\n\u0000\u0000CD\u00053\u0000\u0000D^"+
		"\u0003\u0002\u0001\u000bEF\n\t\u0000\u0000FG\u00055\u0000\u0000G^\u0003"+
		"\u0002\u0001\nHI\n\b\u0000\u0000IJ\u00056\u0000\u0000J^\u0003\u0002\u0001"+
		"\tKL\n\u0007\u0000\u0000LM\u00057\u0000\u0000M^\u0003\u0002\u0001\bNO"+
		"\n\u0006\u0000\u0000OP\u00058\u0000\u0000P^\u0003\u0002\u0001\u0007QR"+
		"\n\u0005\u0000\u0000RS\u00059\u0000\u0000S^\u0003\u0002\u0001\u0006TU"+
		"\n\u0004\u0000\u0000UV\u0005:\u0000\u0000V^\u0003\u0002\u0001\u0005WX"+
		"\n\u0003\u0000\u0000XY\u0005\u0003\u0000\u0000YZ\u0003\u0002\u0001\u0000"+
		"Z[\u0005\u0004\u0000\u0000[\\\u0003\u0002\u0001\u0004\\^\u0001\u0000\u0000"+
		"\u0000]3\u0001\u0000\u0000\u0000]6\u0001\u0000\u0000\u0000]9\u0001\u0000"+
		"\u0000\u0000]<\u0001\u0000\u0000\u0000]?\u0001\u0000\u0000\u0000]B\u0001"+
		"\u0000\u0000\u0000]E\u0001\u0000\u0000\u0000]H\u0001\u0000\u0000\u0000"+
		"]K\u0001\u0000\u0000\u0000]N\u0001\u0000\u0000\u0000]Q\u0001\u0000\u0000"+
		"\u0000]T\u0001\u0000\u0000\u0000]W\u0001\u0000\u0000\u0000^a\u0001\u0000"+
		"\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`\u0003"+
		"\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bc\u0003\u0006\u0003"+
		"\u0000c\u0005\u0001\u0000\u0000\u0000de\u0005\u0005\u0000\u0000ef\u0005"+
		"\u0006\u0000\u0000fg\u0005,\u0000\u0000gp\u0005\u0001\u0000\u0000hm\u0003"+
		"\u0002\u0001\u0000ij\u0005\u0007\u0000\u0000jl\u0003\u0002\u0001\u0000"+
		"ki\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000"+
		"\u0000\u0000ph\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000qr\u0001"+
		"\u0000\u0000\u0000rs\u0005\u0002\u0000\u0000s\u0007\u0001\u0000\u0000"+
		"\u0000tu\u0003\n\u0005\u0000uv\u0005\b\u0000\u0000vx\u0001\u0000\u0000"+
		"\u0000wt\u0001\u0000\u0000\u0000x{\u0001\u0000\u0000\u0000yw\u0001\u0000"+
		"\u0000\u0000yz\u0001\u0000\u0000\u0000z\t\u0001\u0000\u0000\u0000{y\u0001"+
		"\u0000\u0000\u0000|}\u0005$\u0000\u0000}\u0086\u0005\u0001\u0000\u0000"+
		"~\u0083\u0003\u0002\u0001\u0000\u007f\u0080\u0005\u0007\u0000\u0000\u0080"+
		"\u0082\u0003\u0002\u0001\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0082"+
		"\u0085\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0001\u0000\u0000\u0000\u0084\u0087\u0001\u0000\u0000\u0000\u0085"+
		"\u0083\u0001\u0000\u0000\u0000\u0086~\u0001\u0000\u0000\u0000\u0086\u0087"+
		"\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u0117"+
		"\u0005\u0002\u0000\u0000\u0089\u008a\u0005.\u0000\u0000\u008a\u008b\u0005"+
		"1\u0000\u0000\u008b\u0117\u0003\u0002\u0001\u0000\u008c\u008d\u0005\u0013"+
		"\u0000\u0000\u008d\u008e\u0005\u0001\u0000\u0000\u008e\u008f\u0003\u0002"+
		"\u0001\u0000\u008f\u0090\u0005\u0002\u0000\u0000\u0090\u0091\u0005\t\u0000"+
		"\u0000\u0091\u0092\u0003\b\u0004\u0000\u0092\u009d\u0005\n\u0000\u0000"+
		"\u0093\u0094\u0005\u0014\u0000\u0000\u0094\u0095\u0005\u0001\u0000\u0000"+
		"\u0095\u0096\u0003\u0002\u0001\u0000\u0096\u0097\u0005\u0002\u0000\u0000"+
		"\u0097\u0098\u0005\t\u0000\u0000\u0098\u0099\u0003\b\u0004\u0000\u0099"+
		"\u009a\u0005\n\u0000\u0000\u009a\u009c\u0001\u0000\u0000\u0000\u009b\u0093"+
		"\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000\u0000\u009d\u009b"+
		"\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u00a5"+
		"\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u00a0\u00a1"+
		"\u0005\u0015\u0000\u0000\u00a1\u00a2\u0005\t\u0000\u0000\u00a2\u00a3\u0003"+
		"\b\u0004\u0000\u00a3\u00a4\u0005\n\u0000\u0000\u00a4\u00a6\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a0\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000"+
		"\u0000\u0000\u00a6\u0117\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005\u0018"+
		"\u0000\u0000\u00a8\u00a9\u0005\u0001\u0000\u0000\u00a9\u00aa\u0005 \u0000"+
		"\u0000\u00aa\u00ab\u0005.\u0000\u0000\u00ab\u00ac\u00051\u0000\u0000\u00ac"+
		"\u00ad\u0003\u0002\u0001\u0000\u00ad\u00ae\u0005\b\u0000\u0000\u00ae\u00af"+
		"\u0005.\u0000\u0000\u00af\u00b0\u00058\u0000\u0000\u00b0\u00b1\u0003\u0002"+
		"\u0001\u0000\u00b1\u00bd\u0005\b\u0000\u0000\u00b2\u00b3\u0003\u0002\u0001"+
		"\u0000\u00b3\u00b4\u0005\u0016\u0000\u0000\u00b4\u00be\u0001\u0000\u0000"+
		"\u0000\u00b5\u00b6\u0005.\u0000\u0000\u00b6\u00b7\u00051\u0000\u0000\u00b7"+
		"\u00b8\u0005.\u0000\u0000\u00b8\u00b9\u0007\u0000\u0000\u0000\u00b9\u00be"+
		"\u0003\u0002\u0001\u0000\u00ba\u00bb\u0005.\u0000\u0000\u00bb\u00bc\u0005"+
		"1\u0000\u0000\u00bc\u00be\u0003\u0002\u0001\u0000\u00bd\u00b2\u0001\u0000"+
		"\u0000\u0000\u00bd\u00b5\u0001\u0000\u0000\u0000\u00bd\u00ba\u0001\u0000"+
		"\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf\u0001\u0000"+
		"\u0000\u0000\u00bf\u00c0\u0005\u0002\u0000\u0000\u00c0\u00c1\u0005\t\u0000"+
		"\u0000\u00c1\u00c2\u0003\b\u0004\u0000\u00c2\u00c3\u0005\n\u0000\u0000"+
		"\u00c3\u0117\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\u0017\u0000\u0000"+
		"\u00c5\u00c6\u0005\u0001\u0000\u0000\u00c6\u00c7\u0003\u0002\u0001\u0000"+
		"\u00c7\u00c8\u0005\u0002\u0000\u0000\u00c8\u00c9\u0005\t\u0000\u0000\u00c9"+
		"\u00ca\u0003\b\u0004\u0000\u00ca\u00cb\u0005\n\u0000\u0000\u00cb\u0117"+
		"\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005\u0019\u0000\u0000\u00cd\u00ce"+
		"\u0005\u0001\u0000\u0000\u00ce\u00cf\u0005 \u0000\u0000\u00cf\u00d0\u0005"+
		".\u0000\u0000\u00d0\u00d1\u0005\u0004\u0000\u0000\u00d1\u00d2\u0003\u0002"+
		"\u0001\u0000\u00d2\u00d3\u0005\u0002\u0000\u0000\u00d3\u00d4\u0005\t\u0000"+
		"\u0000\u00d4\u00d5\u0003\b\u0004\u0000\u00d5\u00d6\u0005\n\u0000\u0000"+
		"\u00d6\u0117\u0001\u0000\u0000\u0000\u00d7\u00d8\u0005\u001c\u0000\u0000"+
		"\u00d8\u00d9\u0005\u0001\u0000\u0000\u00d9\u00da\u0003\u0002\u0001\u0000"+
		"\u00da\u00db\u0005\u0002\u0000\u0000\u00db\u00dc\u0005\t\u0000\u0000\u00dc"+
		"\u00dd\u0003\b\u0004\u0000\u00dd\u00e8\u0005\n\u0000\u0000\u00de\u00df"+
		"\u0005\u001d\u0000\u0000\u00df\u00e0\u0005\u0001\u0000\u0000\u00e0\u00e1"+
		"\u0003\u0002\u0001\u0000\u00e1\u00e2\u0005\u0002\u0000\u0000\u00e2\u00e3"+
		"\u0005\t\u0000\u0000\u00e3\u00e4\u0003\b\u0004\u0000\u00e4\u00e5\u0005"+
		"\n\u0000\u0000\u00e5\u00e7\u0001\u0000\u0000\u0000\u00e6\u00de\u0001\u0000"+
		"\u0000\u0000\u00e7\u00ea\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001\u0000"+
		"\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u0117\u0001\u0000"+
		"\u0000\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00eb\u0117\u0007\u0002"+
		"\u0000\u0000\u00ec\u00ed\u0005\u001e\u0000\u0000\u00ed\u00ee\u0005\u0001"+
		"\u0000\u0000\u00ee\u00ef\u0003\u0002\u0001\u0000\u00ef\u00f0\u0005\u0002"+
		"\u0000\u0000\u00f0\u0117\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005 \u0000"+
		"\u0000\u00f2\u00f3\u0005/\u0000\u0000\u00f3\u00fe\u0005\u0001\u0000\u0000"+
		"\u00f4\u00f9\u0003\u0002\u0001\u0000\u00f5\u00f6\u0005\u0007\u0000\u0000"+
		"\u00f6\u00f8\u0003\u0002\u0001\u0000\u00f7\u00f5\u0001\u0000\u0000\u0000"+
		"\u00f8\u00fb\u0001\u0000\u0000\u0000\u00f9\u00f7\u0001\u0000\u0000\u0000"+
		"\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u00fd\u0001\u0000\u0000\u0000"+
		"\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fc\u00f4\u0001\u0000\u0000\u0000"+
		"\u00fd\u0100\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000\u0000"+
		"\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u0101\u0001\u0000\u0000\u0000"+
		"\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0102\u0005\u0002\u0000\u0000"+
		"\u0102\u0103\u0005\t\u0000\u0000\u0103\u0109\u0003\b\u0004\u0000\u0104"+
		"\u0107\u0005\u001f\u0000\u0000\u0105\u0108\u0005.\u0000\u0000\u0106\u0108"+
		"\u0003\u0002\u0001\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0107\u0106"+
		"\u0001\u0000\u0000\u0000\u0108\u010a\u0001\u0000\u0000\u0000\u0109\u0104"+
		"\u0001\u0000\u0000\u0000\u0109\u010a\u0001\u0000\u0000\u0000\u010a\u010b"+
		"\u0001\u0000\u0000\u0000\u010b\u010c\u0005\n\u0000\u0000\u010c\u0117\u0001"+
		"\u0000\u0000\u0000\u010d\u010e\u0005\u0013\u0000\u0000\u010e\u010f\u0005"+
		"\u0001\u0000\u0000\u010f\u0110\u0003\u0002\u0001\u0000\u0110\u0111\u0005"+
		"\u0002\u0000\u0000\u0111\u0114\u0005\u001f\u0000\u0000\u0112\u0115\u0005"+
		".\u0000\u0000\u0113\u0115\u0003\u0002\u0001\u0000\u0114\u0112\u0001\u0000"+
		"\u0000\u0000\u0114\u0113\u0001\u0000\u0000\u0000\u0115\u0117\u0001\u0000"+
		"\u0000\u0000\u0116|\u0001\u0000\u0000\u0000\u0116\u0089\u0001\u0000\u0000"+
		"\u0000\u0116\u008c\u0001\u0000\u0000\u0000\u0116\u00a7\u0001\u0000\u0000"+
		"\u0000\u0116\u00c4\u0001\u0000\u0000\u0000\u0116\u00cc\u0001\u0000\u0000"+
		"\u0000\u0116\u00d7\u0001\u0000\u0000\u0000\u0116\u00eb\u0001\u0000\u0000"+
		"\u0000\u0116\u00ec\u0001\u0000\u0000\u0000\u0116\u00f1\u0001\u0000\u0000"+
		"\u0000\u0116\u010d\u0001\u0000\u0000\u0000\u0117\u000b\u0001\u0000\u0000"+
		"\u0000\u0118\u0119\u0003\u000e\u0007\u0000\u0119\r\u0001\u0000\u0000\u0000"+
		"\u011a\u011b\u0005\u0005\u0000\u0000\u011b\u011c\u0005\u0006\u0000\u0000"+
		"\u011c\u011d\u0005-\u0000\u0000\u011d\u000f\u0001\u0000\u0000\u0000\u011e"+
		"\u0120\u0003\u0012\t\u0000\u011f\u011e\u0001\u0000\u0000\u0000\u0120\u0123"+
		"\u0001\u0000\u0000\u0000\u0121\u011f\u0001\u0000\u0000\u0000\u0121\u0122"+
		"\u0001\u0000\u0000\u0000\u0122\u0124\u0001\u0000\u0000\u0000\u0123\u0121"+
		"\u0001\u0000\u0000\u0000\u0124\u0125\u0003\u0014\n\u0000\u0125\u0126\u0005"+
		"\u0000\u0000\u0001\u0126\u0011\u0001\u0000\u0000\u0000\u0127\u0128\u0005"+
		" \u0000\u0000\u0128\u012b\u0005.\u0000\u0000\u0129\u012a\u00051\u0000"+
		"\u0000\u012a\u012c\u0003\u0002\u0001\u0000\u012b\u0129\u0001\u0000\u0000"+
		"\u0000\u012b\u012c\u0001\u0000\u0000\u0000\u012c\u012d\u0001\u0000\u0000"+
		"\u0000\u012d\u012e\u0005\b\u0000\u0000\u012e\u0013\u0001\u0000\u0000\u0000"+
		"\u012f\u0130\u0005\u000b\u0000\u0000\u0130\u0131\u0005\f\u0000\u0000\u0131"+
		"\u0132\u0005\u0001\u0000\u0000\u0132\u0133\u0005\u0002\u0000\u0000\u0133"+
		"\u0134\u0005\t\u0000\u0000\u0134\u0135\u0003\b\u0004\u0000\u0135\u0136"+
		"\u0005\n\u0000\u0000\u0136\u0015\u0001\u0000\u0000\u0000\u0137\u0155\u0005"+
		")\u0000\u0000\u0138\u0155\u0005\'\u0000\u0000\u0139\u0155\u0005(\u0000"+
		"\u0000\u013a\u0155\u0005*\u0000\u0000\u013b\u0144\u0005\r\u0000\u0000"+
		"\u013c\u013e\t\u0000\u0000\u0000\u013d\u013c\u0001\u0000\u0000\u0000\u013e"+
		"\u0141\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000\u0000\u0000\u013f"+
		"\u013d\u0001\u0000\u0000\u0000\u0140\u0145\u0001\u0000\u0000\u0000\u0141"+
		"\u013f\u0001\u0000\u0000\u0000\u0142\u0145\u0005\u0006\u0000\u0000\u0143"+
		"\u0145\u0005\u0003\u0000\u0000\u0144\u013f\u0001\u0000\u0000\u0000\u0144"+
		"\u0142\u0001\u0000\u0000\u0000\u0144\u0143\u0001\u0000\u0000\u0000\u0145"+
		"\u0146\u0001\u0000\u0000\u0000\u0146\u0155\u0005\r\u0000\u0000\u0147\u014d"+
		"\u0005\u000e\u0000\u0000\u0148\u0149\u0003\u0002\u0001\u0000\u0149\u014a"+
		"\u0005\u0007\u0000\u0000\u014a\u014c\u0001\u0000\u0000\u0000\u014b\u0148"+
		"\u0001\u0000\u0000\u0000\u014c\u014f\u0001\u0000\u0000\u0000\u014d\u014b"+
		"\u0001\u0000\u0000\u0000\u014d\u014e\u0001\u0000\u0000\u0000\u014e\u0151"+
		"\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u0150\u0152"+
		"\u0003\u0002\u0001\u0000\u0151\u0150\u0001\u0000\u0000\u0000\u0151\u0152"+
		"\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000\u0000\u0153\u0155"+
		"\u0005\u000f\u0000\u0000\u0154\u0137\u0001\u0000\u0000\u0000\u0154\u0138"+
		"\u0001\u0000\u0000\u0000\u0154\u0139\u0001\u0000\u0000\u0000\u0154\u013a"+
		"\u0001\u0000\u0000\u0000\u0154\u013b\u0001\u0000\u0000\u0000\u0154\u0147"+
		"\u0001\u0000\u0000\u0000\u0155\u0017\u0001\u0000\u0000\u0000\u0156\u0157"+
		"\u0005\u0010\u0000\u0000\u0157\u0158\u0007\u0003\u0000\u0000\u0158\u0159"+
		"\u0005\u0007\u0000\u0000\u0159\u015a\u0007\u0003\u0000\u0000\u015a\u0019"+
		"\u0001\u0000\u0000\u0000\u015b\u015c\u0005#\u0000\u0000\u015c\u0165\u0005"+
		"\u0001\u0000\u0000\u015d\u0162\u0003\u0002\u0001\u0000\u015e\u015f\u0005"+
		"\u0007\u0000\u0000\u015f\u0161\u0003\u0002\u0001\u0000\u0160\u015e\u0001"+
		"\u0000\u0000\u0000\u0161\u0164\u0001\u0000\u0000\u0000\u0162\u0160\u0001"+
		"\u0000\u0000\u0000\u0162\u0163\u0001\u0000\u0000\u0000\u0163\u0166\u0001"+
		"\u0000\u0000\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u0165\u015d\u0001"+
		"\u0000\u0000\u0000\u0165\u0166\u0001\u0000\u0000\u0000\u0166\u0167\u0001"+
		"\u0000\u0000\u0000\u0167\u0176\u0005\u0002\u0000\u0000\u0168\u0169\u0005"+
		"/\u0000\u0000\u0169\u0172\u0005\u0001\u0000\u0000\u016a\u016f\u0003\u0002"+
		"\u0001\u0000\u016b\u016c\u0005\u0007\u0000\u0000\u016c\u016e\u0003\u0002"+
		"\u0001\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016e\u0171\u0001\u0000"+
		"\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u016f\u0170\u0001\u0000"+
		"\u0000\u0000\u0170\u0173\u0001\u0000\u0000\u0000\u0171\u016f\u0001\u0000"+
		"\u0000\u0000\u0172\u016a\u0001\u0000\u0000\u0000\u0172\u0173\u0001\u0000"+
		"\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000\u0174\u0176\u0005\u0002"+
		"\u0000\u0000\u0175\u015b\u0001\u0000\u0000\u0000\u0175\u0168\u0001\u0000"+
		"\u0000\u0000\u0176\u001b\u0001\u0000\u0000\u0000\u001e1]_mpy\u0083\u0086"+
		"\u009d\u00a5\u00bd\u00e8\u00f9\u00fe\u0107\u0109\u0114\u0116\u0121\u012b"+
		"\u013f\u0144\u014d\u0151\u0154\u0162\u0165\u016f\u0172\u0175";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}