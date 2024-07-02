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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, ROBOT=16, IF=17, 
		ELSEIF=18, ELSE=19, STEP=20, WHILE=21, REPEATFOR=22, REPEATFOREACH=23, 
		BREAK=24, CONTINUE=25, WAIT=26, ORWAITFOR=27, WAITMS=28, RETURN=29, PRIMITIVETYPE=30, 
		NEWLINE=31, WS=32, FNAME=33, FNAMESTMT=34, CONST=35, NULL=36, INT=37, 
		FLOAT=38, COLOR=39, BOOL=40, HEX=41, MICROBITV2_SENSORSEXPR=42, MICROBITV2_SENSORSTMT=43, 
		VAR=44, FUNCTIONNAME=45, STR=46, SET=47, AND=48, OR=49, NOT=50, EQUAL=51, 
		NEQUAL=52, GET=53, LET=54, GEQ=55, LEQ=56, MOD=57, POW=58, MUL=59, DIV=60, 
		ADD=61, SUB=62, ACCELEROMETER_SENSOR=63, COLOR_SENSOR=64, COMPASS_CALIBRATE=65, 
		COMPASS_SENSOR=66, DETECT_MARK_SENSOR=67, DROP_SENSOR=68, EHT_COLOR_SENSOR=69, 
		ENCODER_RESET=70, ENCODER_SENSOR=71, ENVIRONMENTAL_SENSOR=72, GESTURE_SENSOR=73, 
		GET_LINE_SENSOR=74, GYRO_RESET=75, GYRO_SENSOR=76, HT_COLOR_SENSOR=77, 
		HUMIDITY_SENSOR=78, IR_SEEKER_SENSOR=79, INFRARED_SENSOR=80, KEYS_SENSOR=81, 
		LIGHT_SENSOR=82, MOISTURE_SENSOR=83, MOTION_SENSOR=84, PARTICLE_SENSOR=85, 
		PIN_GET_VALUE_SENSOR=86, PIN_TOUCH_SENSOR=87, PULSE_SENSOR=88, RFID_SENSOR=89, 
		SOUND_SENSOR=90, TEMPERATURE_SENSOR=91, TIMER_RESET=92, TIMER_SENSOR=93, 
		TOUCH_SENSOR=94, ULTRASONIC_SENSOR=95, VEML_LIGHT_SENSOR=96, VOLTAGE_SENSOR=97, 
		CAMERA_SENSOR=98, CAMERA_THRESHOLD=99, CODE_PAD_SENSOR=100, COLOUR_BLOB=101, 
		DETECT_FACE_INFORMATION=102, DETECT_FACE_SENSOR=103, ELECTRIC_CURRENT_SENSOR=104, 
		FSR_SENSOR=105, GPS_SENSOR=106, GYRO_RESET_AXIS=107, JOYSTICK=108, LOGO_TOUCH_SENSOR=109, 
		MARKER_INFORMATION=110, NAO_MARK_INFORMATION=111, ODOMETRY_SENSOR=112, 
		ODOMETRY_SENSOR_RESET=113, OPTICAL_SENSOR=114, PIN_SET_TOUCH_MODE=115, 
		QUAD_RGB_SENSOR=116, RADIO_RSSI_SENSOR=117, RECOGNIZE_WORD=118, RESET_SENSOR=119, 
		SOUND_RECORD=120, TAP_SENSOR=121, SENSOR_EXPR=122, ACTUATOR_EXPR=123, 
		SPECIFIC_EXPR=124, SENSOR_STMT=125;
	public static final int
		RULE_expression = 0, RULE_expr = 1, RULE_robotExpr = 2, RULE_robotSensorExpr = 3, 
		RULE_robotActuatorExpr = 4, RULE_robotSpecificExpr = 5, RULE_statementList = 6, 
		RULE_stmt = 7, RULE_robotStmt = 8, RULE_robotSensorStmt = 9, RULE_program = 10, 
		RULE_declaration = 11, RULE_mainBlock = 12, RULE_literal = 13, RULE_connExpr = 14, 
		RULE_funCall = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "expr", "robotExpr", "robotSensorExpr", "robotActuatorExpr", 
			"robotSpecificExpr", "statementList", "stmt", "robotStmt", "robotSensorStmt", 
			"program", "declaration", "mainBlock", "literal", "connExpr", "funCall"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'?'", "':'", "'.'", "','", "';'", "'{'", "'}'", 
			"'void'", "'main'", "'\"'", "'['", "']'", "'connect'", null, "'if'", 
			null, "'else'", "'++'", "'while'", "'for'", null, "'break'", "'continue'", 
			"'waitUntil'", "'orWaitFor'", "'wait ms'", "'return'", null, null, null, 
			null, null, null, "'null'", null, null, null, null, null, null, null, 
			null, null, null, "'='", "'&&'", "'||'", "'!'", "'=='", "'!='", "'>'", 
			"'<'", "'>='", "'<='", "'%'", "'^'", "'*'", "'/'", "'+'", "'-'", "'accelerometerSensor'", 
			"'colorSensor'", "'compassCalibrate'", "'compassSensor'", "'detectMarkSensor'", 
			"'dropSensor'", "'ehtColorSensor'", "'encoderReset'", "'encoderSensor'", 
			"'environmentalSensor'", "'gestureSensor'", "'getLineSensor'", "'gyroReset'", 
			"'gyroSensor'", "'htColorSensor'", "'humiditySensor'", "'irSeekerSensor'", 
			"'infraredSensor'", "'keysSensor'", "'lightSensor'", "'moistureSensor'", 
			"'motionSensor'", "'particleSensor'", "'pinGetValueSensor'", "'pinTouchSensor'", 
			"'pulseSensor'", "'rfidSensor'", "'soundSensor'", "'temperatureSensor'", 
			"'timerReset'", "'timerSensor'", "'touchSensor'", "'ultrasonicSensor'", 
			"'vemlLightSensor'", "'voltageSensor'", "'cameraSensor'", "'cameraThreshold'", 
			"'codePadSensor'", "'colourBlob'", "'detectFaceInformation'", "'detectFaceSensor'", 
			"'electricCurrentSensor'", "'fsrSensor'", "'gpsSensor'", "'gyroResetAxis'", 
			"'joystick'", "'logoTouchSensor'", "'markerInformation'", "'naoMarkInformation'", 
			"'odometrySensor'", "'odometrySensorReset'", "'opticalSensor'", "'pinSetTouchMode'", 
			"'quadRGBSensor'", "'radioRssiSensor'", "'recognizeWord'", "'resetSensor'", 
			"'soundRecord'", "'tapSensor'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "ROBOT", "IF", "ELSEIF", "ELSE", "STEP", "WHILE", 
			"REPEATFOR", "REPEATFOREACH", "BREAK", "CONTINUE", "WAIT", "ORWAITFOR", 
			"WAITMS", "RETURN", "PRIMITIVETYPE", "NEWLINE", "WS", "FNAME", "FNAMESTMT", 
			"CONST", "NULL", "INT", "FLOAT", "COLOR", "BOOL", "HEX", "MICROBITV2_SENSORSEXPR", 
			"MICROBITV2_SENSORSTMT", "VAR", "FUNCTIONNAME", "STR", "SET", "AND", 
			"OR", "NOT", "EQUAL", "NEQUAL", "GET", "LET", "GEQ", "LEQ", "MOD", "POW", 
			"MUL", "DIV", "ADD", "SUB", "ACCELEROMETER_SENSOR", "COLOR_SENSOR", "COMPASS_CALIBRATE", 
			"COMPASS_SENSOR", "DETECT_MARK_SENSOR", "DROP_SENSOR", "EHT_COLOR_SENSOR", 
			"ENCODER_RESET", "ENCODER_SENSOR", "ENVIRONMENTAL_SENSOR", "GESTURE_SENSOR", 
			"GET_LINE_SENSOR", "GYRO_RESET", "GYRO_SENSOR", "HT_COLOR_SENSOR", "HUMIDITY_SENSOR", 
			"IR_SEEKER_SENSOR", "INFRARED_SENSOR", "KEYS_SENSOR", "LIGHT_SENSOR", 
			"MOISTURE_SENSOR", "MOTION_SENSOR", "PARTICLE_SENSOR", "PIN_GET_VALUE_SENSOR", 
			"PIN_TOUCH_SENSOR", "PULSE_SENSOR", "RFID_SENSOR", "SOUND_SENSOR", "TEMPERATURE_SENSOR", 
			"TIMER_RESET", "TIMER_SENSOR", "TOUCH_SENSOR", "ULTRASONIC_SENSOR", "VEML_LIGHT_SENSOR", 
			"VOLTAGE_SENSOR", "CAMERA_SENSOR", "CAMERA_THRESHOLD", "CODE_PAD_SENSOR", 
			"COLOUR_BLOB", "DETECT_FACE_INFORMATION", "DETECT_FACE_SENSOR", "ELECTRIC_CURRENT_SENSOR", 
			"FSR_SENSOR", "GPS_SENSOR", "GYRO_RESET_AXIS", "JOYSTICK", "LOGO_TOUCH_SENSOR", 
			"MARKER_INFORMATION", "NAO_MARK_INFORMATION", "ODOMETRY_SENSOR", "ODOMETRY_SENSOR_RESET", 
			"OPTICAL_SENSOR", "PIN_SET_TOUCH_MODE", "QUAD_RGB_SENSOR", "RADIO_RSSI_SENSOR", 
			"RECOGNIZE_WORD", "RESET_SENSOR", "SOUND_RECORD", "TAP_SENSOR", "SENSOR_EXPR", 
			"ACTUATOR_EXPR", "SPECIFIC_EXPR", "SENSOR_STMT"
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
			setState(32);
			expr(0);
			setState(33);
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
			setState(53);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				{
				_localctx = new NullConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(36);
				match(NULL);
				}
				break;
			case CONST:
				{
				_localctx = new MathConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37);
				match(CONST);
				}
				break;
			case VAR:
				{
				_localctx = new VarNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38);
				match(VAR);
				}
				break;
			case T__11:
			case T__12:
			case INT:
			case FLOAT:
			case COLOR:
			case BOOL:
				{
				_localctx = new LiteralExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(39);
				literal();
				}
				break;
			case T__14:
				{
				_localctx = new ConnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40);
				connExpr();
				}
				break;
			case FNAME:
			case FUNCTIONNAME:
				{
				_localctx = new FuncExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(41);
				funCall();
				}
				break;
			case T__0:
				{
				_localctx = new ParentheseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42);
				match(T__0);
				setState(43);
				expr(0);
				setState(44);
				match(T__1);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new UnaryNContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(46);
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
				setState(47);
				expr(17);
				}
				break;
			case NOT:
				{
				_localctx = new UnaryBContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48);
				((UnaryBContext)_localctx).op = match(NOT);
				setState(49);
				expr(16);
				}
				break;
			case PRIMITIVETYPE:
				{
				_localctx = new ParamsMethodContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(50);
				match(PRIMITIVETYPE);
				setState(51);
				match(VAR);
				}
				break;
			case ROBOT:
				{
				_localctx = new RobotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52);
				robotExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(99);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(97);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(55);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(56);
						((BinaryNContext)_localctx).op = match(POW);
						setState(57);
						expr(15);
						}
						break;
					case 2:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(58);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(59);
						((BinaryNContext)_localctx).op = match(MOD);
						setState(60);
						expr(14);
						}
						break;
					case 3:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(61);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(62);
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
						setState(63);
						expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(64);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(65);
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
						setState(66);
						expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(67);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(68);
						((BinaryBContext)_localctx).op = match(AND);
						setState(69);
						expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(70);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(71);
						((BinaryBContext)_localctx).op = match(OR);
						setState(72);
						expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(73);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(74);
						((BinaryBContext)_localctx).op = match(EQUAL);
						setState(75);
						expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(76);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(77);
						((BinaryBContext)_localctx).op = match(NEQUAL);
						setState(78);
						expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(79);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(80);
						((BinaryBContext)_localctx).op = match(GET);
						setState(81);
						expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(82);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(83);
						((BinaryBContext)_localctx).op = match(LET);
						setState(84);
						expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(85);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(86);
						((BinaryBContext)_localctx).op = match(GEQ);
						setState(87);
						expr(6);
						}
						break;
					case 12:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(88);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(89);
						((BinaryBContext)_localctx).op = match(LEQ);
						setState(90);
						expr(5);
						}
						break;
					case 13:
						{
						_localctx = new IfElseOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(91);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(92);
						match(T__2);
						setState(93);
						expr(0);
						setState(94);
						match(T__3);
						setState(95);
						expr(4);
						}
						break;
					}
					} 
				}
				setState(101);
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
			setState(102);
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
	public static class RobotSensorExpressionContext extends RobotSensorExprContext {
		public TerminalNode ROBOT() { return getToken(ExprlyParser.ROBOT, 0); }
		public TerminalNode SENSOR_EXPR() { return getToken(ExprlyParser.SENSOR_EXPR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public RobotSensorExpressionContext(RobotSensorExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotSensorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotSensorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotSensorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotSensorExprContext robotSensorExpr() throws RecognitionException {
		RobotSensorExprContext _localctx = new RobotSensorExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_robotSensorExpr);
		int _la;
		try {
			_localctx = new RobotSensorExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(ROBOT);
			setState(105);
			match(T__4);
			setState(106);
			match(SENSOR_EXPR);
			setState(107);
			match(T__0);
			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
				{
				setState(108);
				expr(0);
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(109);
					match(T__5);
					setState(110);
					expr(0);
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(118);
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
	public static class RobotActuatorExprContext extends ParserRuleContext {
		public RobotActuatorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotActuatorExpr; }
	 
		public RobotActuatorExprContext() { }
		public void copyFrom(RobotActuatorExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotActuatorExpressionContext extends RobotActuatorExprContext {
		public TerminalNode ROBOT() { return getToken(ExprlyParser.ROBOT, 0); }
		public TerminalNode ACTUATOR_EXPR() { return getToken(ExprlyParser.ACTUATOR_EXPR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public RobotActuatorExpressionContext(RobotActuatorExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotActuatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotActuatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotActuatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotActuatorExprContext robotActuatorExpr() throws RecognitionException {
		RobotActuatorExprContext _localctx = new RobotActuatorExprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_robotActuatorExpr);
		int _la;
		try {
			_localctx = new RobotActuatorExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(ROBOT);
			setState(121);
			match(T__4);
			setState(122);
			match(ACTUATOR_EXPR);
			setState(123);
			match(T__0);
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
				{
				setState(124);
				expr(0);
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(125);
					match(T__5);
					setState(126);
					expr(0);
					}
					}
					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(134);
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
	public static class RobotSpecificExprContext extends ParserRuleContext {
		public RobotSpecificExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotSpecificExpr; }
	 
		public RobotSpecificExprContext() { }
		public void copyFrom(RobotSpecificExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotSpecificExpressionContext extends RobotSpecificExprContext {
		public TerminalNode ROBOT() { return getToken(ExprlyParser.ROBOT, 0); }
		public TerminalNode SPECIFIC_EXPR() { return getToken(ExprlyParser.SPECIFIC_EXPR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public RobotSpecificExpressionContext(RobotSpecificExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotSpecificExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotSpecificExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotSpecificExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotSpecificExprContext robotSpecificExpr() throws RecognitionException {
		RobotSpecificExprContext _localctx = new RobotSpecificExprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_robotSpecificExpr);
		int _la;
		try {
			_localctx = new RobotSpecificExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(ROBOT);
			setState(137);
			match(T__4);
			setState(138);
			match(SPECIFIC_EXPR);
			setState(139);
			match(T__0);
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
				{
				setState(140);
				expr(0);
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(141);
					match(T__5);
					setState(142);
					expr(0);
					}
					}
					setState(147);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(150);
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
		enterRule(_localctx, 12, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17610840342528L) != 0)) {
				{
				{
				setState(152);
				stmt();
				setState(153);
				match(T__6);
				}
				}
				setState(159);
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
		enterRule(_localctx, 14, RULE_stmt);
		int _la;
		try {
			setState(314);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				_localctx = new StmtFuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				match(FNAMESTMT);
				setState(161);
				match(T__0);
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
					{
					setState(162);
					expr(0);
					setState(167);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__5) {
						{
						{
						setState(163);
						match(T__5);
						setState(164);
						expr(0);
						}
						}
						setState(169);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(172);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new BinaryVarAssignContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(VAR);
				setState(174);
				((BinaryVarAssignContext)_localctx).op = match(SET);
				setState(175);
				expr(0);
				}
				break;
			case 3:
				_localctx = new ConditionStatementBlockContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(176);
				match(IF);
				setState(177);
				match(T__0);
				setState(178);
				expr(0);
				setState(179);
				match(T__1);
				setState(180);
				match(T__7);
				setState(181);
				statementList();
				setState(182);
				match(T__8);
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELSEIF) {
					{
					{
					setState(183);
					match(ELSEIF);
					setState(184);
					match(T__0);
					setState(185);
					expr(0);
					setState(186);
					match(T__1);
					setState(187);
					match(T__7);
					setState(188);
					statementList();
					setState(189);
					match(T__8);
					}
					}
					setState(195);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(196);
					((ConditionStatementBlockContext)_localctx).op = match(ELSE);
					setState(197);
					match(T__7);
					setState(198);
					statementList();
					setState(199);
					match(T__8);
					}
				}

				}
				break;
			case 4:
				_localctx = new RepeatForContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(203);
				match(REPEATFOR);
				setState(204);
				match(T__0);
				setState(205);
				match(PRIMITIVETYPE);
				setState(206);
				match(VAR);
				setState(207);
				match(SET);
				setState(208);
				expr(0);
				setState(209);
				match(T__6);
				setState(210);
				match(VAR);
				setState(211);
				match(LET);
				setState(212);
				expr(0);
				setState(213);
				match(T__6);
				setState(225);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
				case 1:
					{
					setState(214);
					expr(0);
					setState(215);
					((RepeatForContext)_localctx).op = match(STEP);
					}
					break;
				case 2:
					{
					setState(217);
					match(VAR);
					setState(218);
					match(SET);
					setState(219);
					match(VAR);
					setState(220);
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
					setState(221);
					expr(0);
					}
					break;
				case 3:
					{
					setState(222);
					match(VAR);
					setState(223);
					match(SET);
					setState(224);
					expr(0);
					}
					break;
				}
				setState(227);
				match(T__1);
				setState(228);
				match(T__7);
				setState(229);
				statementList();
				setState(230);
				match(T__8);
				}
				break;
			case 5:
				_localctx = new RepeatStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(232);
				match(WHILE);
				setState(233);
				match(T__0);
				setState(234);
				expr(0);
				setState(235);
				match(T__1);
				setState(236);
				match(T__7);
				setState(237);
				statementList();
				setState(238);
				match(T__8);
				}
				break;
			case 6:
				_localctx = new RepeatForEachContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(240);
				match(REPEATFOREACH);
				setState(241);
				match(T__0);
				setState(242);
				match(PRIMITIVETYPE);
				setState(243);
				match(VAR);
				setState(244);
				match(T__3);
				setState(245);
				expr(0);
				setState(246);
				match(T__1);
				setState(247);
				match(T__7);
				setState(248);
				statementList();
				setState(249);
				match(T__8);
				}
				break;
			case 7:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(251);
				match(WAIT);
				setState(252);
				match(T__0);
				setState(253);
				expr(0);
				setState(254);
				match(T__1);
				setState(255);
				match(T__7);
				setState(256);
				statementList();
				setState(257);
				match(T__8);
				setState(268);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ORWAITFOR) {
					{
					{
					setState(258);
					((WaitStatementContext)_localctx).op = match(ORWAITFOR);
					setState(259);
					match(T__0);
					setState(260);
					expr(0);
					setState(261);
					match(T__1);
					setState(262);
					match(T__7);
					setState(263);
					statementList();
					setState(264);
					match(T__8);
					}
					}
					setState(270);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 8:
				_localctx = new FlowControlContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(271);
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
				setState(272);
				match(WAITMS);
				setState(273);
				match(T__0);
				setState(274);
				expr(0);
				setState(275);
				match(T__1);
				}
				break;
			case 10:
				_localctx = new FuncUserContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(277);
				match(PRIMITIVETYPE);
				setState(278);
				match(FUNCTIONNAME);
				setState(279);
				match(T__0);
				setState(290);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
					{
					{
					setState(280);
					expr(0);
					setState(285);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__5) {
						{
						{
						setState(281);
						match(T__5);
						setState(282);
						expr(0);
						}
						}
						setState(287);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					setState(292);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(293);
				match(T__1);
				setState(294);
				match(T__7);
				setState(295);
				statementList();
				setState(301);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==RETURN) {
					{
					setState(296);
					((FuncUserContext)_localctx).op = match(RETURN);
					setState(299);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						setState(297);
						match(VAR);
						}
						break;
					case 2:
						{
						setState(298);
						expr(0);
						}
						break;
					}
					}
				}

				setState(303);
				match(T__8);
				}
				break;
			case 11:
				_localctx = new UserFuncIfStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(305);
				match(IF);
				setState(306);
				match(T__0);
				setState(307);
				expr(0);
				setState(308);
				match(T__1);
				setState(309);
				match(RETURN);
				setState(312);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(310);
					match(VAR);
					}
					break;
				case 2:
					{
					setState(311);
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
		enterRule(_localctx, 16, RULE_robotStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
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
	public static class RobotSensorStatementContext extends RobotSensorStmtContext {
		public TerminalNode ROBOT() { return getToken(ExprlyParser.ROBOT, 0); }
		public TerminalNode SENSOR_STMT() { return getToken(ExprlyParser.SENSOR_STMT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public RobotSensorStatementContext(RobotSensorStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotSensorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotSensorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotSensorStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotSensorStmtContext robotSensorStmt() throws RecognitionException {
		RobotSensorStmtContext _localctx = new RobotSensorStmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_robotSensorStmt);
		int _la;
		try {
			_localctx = new RobotSensorStatementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(ROBOT);
			setState(319);
			match(T__4);
			setState(320);
			match(SENSOR_STMT);
			setState(321);
			match(T__0);
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
				{
				setState(322);
				expr(0);
				setState(327);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__5) {
					{
					{
					setState(323);
					match(T__5);
					setState(324);
					expr(0);
					}
					}
					setState(329);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(332);
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
		enterRule(_localctx, 20, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(334);
				declaration();
				}
				}
				setState(339);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(340);
			mainBlock();
			setState(341);
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
		enterRule(_localctx, 22, RULE_declaration);
		int _la;
		try {
			_localctx = new VariableDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(PRIMITIVETYPE);
			setState(344);
			match(VAR);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SET) {
				{
				setState(345);
				match(SET);
				setState(346);
				expr(0);
				}
			}

			setState(349);
			match(T__6);
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
		enterRule(_localctx, 24, RULE_mainBlock);
		try {
			_localctx = new MainFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(351);
			match(T__9);
			setState(352);
			match(T__10);
			setState(353);
			match(T__0);
			setState(354);
			match(T__1);
			setState(355);
			match(T__7);
			setState(356);
			statementList();
			setState(357);
			match(T__8);
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
		enterRule(_localctx, 26, RULE_literal);
		int _la;
		try {
			int _alt;
			setState(388);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLOR:
				_localctx = new ColContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(359);
				match(COLOR);
				}
				break;
			case INT:
				_localctx = new IntConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatConstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(361);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolConstBContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(362);
				match(BOOL);
				}
				break;
			case T__11:
				_localctx = new ConstStrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(363);
				match(T__11);
				setState(372);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(367);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(364);
							matchWildcard();
							}
							} 
						}
						setState(369);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
					}
					}
					break;
				case 2:
					{
					setState(370);
					match(T__4);
					}
					break;
				case 3:
					{
					setState(371);
					match(T__2);
					}
					break;
				}
				setState(374);
				match(T__11);
				}
				break;
			case T__12:
				_localctx = new ListExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(375);
				match(T__12);
				setState(381);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(376);
						expr(0);
						setState(377);
						match(T__5);
						}
						} 
					}
					setState(383);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				setState(385);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
					{
					setState(384);
					expr(0);
					}
				}

				setState(387);
				match(T__13);
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
		enterRule(_localctx, 28, RULE_connExpr);
		int _la;
		try {
			_localctx = new ConnContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(T__14);
			setState(391);
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
			setState(392);
			match(T__5);
			setState(393);
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
		enterRule(_localctx, 30, RULE_funCall);
		int _la;
		try {
			setState(421);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FNAME:
				_localctx = new FuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(395);
				match(FNAME);
				setState(396);
				match(T__0);
				setState(405);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
					{
					setState(397);
					expr(0);
					setState(402);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__5) {
						{
						{
						setState(398);
						match(T__5);
						setState(399);
						expr(0);
						}
						}
						setState(404);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(407);
				match(T__1);
				}
				break;
			case FUNCTIONNAME:
				_localctx = new UserDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(408);
				match(FUNCTIONNAME);
				setState(409);
				match(T__0);
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6918709878433361922L) != 0)) {
					{
					setState(410);
					expr(0);
					setState(415);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__5) {
						{
						{
						setState(411);
						match(T__5);
						setState(412);
						expr(0);
						}
						}
						setState(417);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(420);
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
		"\u0004\u0001}\u01a8\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u00016\b\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001"+
		"b\b\u0001\n\u0001\f\u0001e\t\u0001\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u0003p\b\u0003\n\u0003\f\u0003s\t\u0003\u0003\u0003u\b\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u0080\b\u0004\n\u0004\f\u0004"+
		"\u0083\t\u0004\u0003\u0004\u0085\b\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0005\u0005\u0090\b\u0005\n\u0005\f\u0005\u0093\t\u0005\u0003\u0005"+
		"\u0095\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006\u009c\b\u0006\n\u0006\f\u0006\u009f\t\u0006\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00a6\b\u0007\n"+
		"\u0007\f\u0007\u00a9\t\u0007\u0003\u0007\u00ab\b\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u0007\u00c0\b\u0007\n\u0007\f\u0007\u00c3\t\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00ca\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u00e2\b\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0005\u0007\u010b\b\u0007\n\u0007\f\u0007\u010e"+
		"\t\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0005\u0007\u011c\b\u0007\n\u0007\f\u0007\u011f\t\u0007\u0005\u0007"+
		"\u0121\b\u0007\n\u0007\f\u0007\u0124\t\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u012c\b\u0007\u0003"+
		"\u0007\u012e\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0139"+
		"\b\u0007\u0003\u0007\u013b\b\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u0146\b\t\n\t\f\t\u0149\t\t"+
		"\u0003\t\u014b\b\t\u0001\t\u0001\t\u0001\n\u0005\n\u0150\b\n\n\n\f\n\u0153"+
		"\t\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0003\u000b\u015c\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0005\r\u016e\b\r\n\r\f\r\u0171\t\r\u0001\r"+
		"\u0001\r\u0003\r\u0175\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u017c\b\r\n\r\f\r\u017f\t\r\u0001\r\u0003\r\u0182\b\r\u0001\r\u0003"+
		"\r\u0185\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u0191\b\u000f\n\u000f\f\u000f\u0194\t\u000f\u0003\u000f\u0196\b\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0005\u000f\u019e\b\u000f\n\u000f\f\u000f\u01a1\t\u000f\u0003\u000f\u01a3"+
		"\b\u000f\u0001\u000f\u0003\u000f\u01a6\b\u000f\u0001\u000f\u0001\u016f"+
		"\u0001\u0002\u0010\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e\u0000\u0004\u0001\u0000=>\u0001\u0000;"+
		"<\u0001\u0000\u0018\u0019\u0002\u0000,,..\u01df\u0000 \u0001\u0000\u0000"+
		"\u0000\u00025\u0001\u0000\u0000\u0000\u0004f\u0001\u0000\u0000\u0000\u0006"+
		"h\u0001\u0000\u0000\u0000\bx\u0001\u0000\u0000\u0000\n\u0088\u0001\u0000"+
		"\u0000\u0000\f\u009d\u0001\u0000\u0000\u0000\u000e\u013a\u0001\u0000\u0000"+
		"\u0000\u0010\u013c\u0001\u0000\u0000\u0000\u0012\u013e\u0001\u0000\u0000"+
		"\u0000\u0014\u0151\u0001\u0000\u0000\u0000\u0016\u0157\u0001\u0000\u0000"+
		"\u0000\u0018\u015f\u0001\u0000\u0000\u0000\u001a\u0184\u0001\u0000\u0000"+
		"\u0000\u001c\u0186\u0001\u0000\u0000\u0000\u001e\u01a5\u0001\u0000\u0000"+
		"\u0000 !\u0003\u0002\u0001\u0000!\"\u0005\u0000\u0000\u0001\"\u0001\u0001"+
		"\u0000\u0000\u0000#$\u0006\u0001\uffff\uffff\u0000$6\u0005$\u0000\u0000"+
		"%6\u0005#\u0000\u0000&6\u0005,\u0000\u0000\'6\u0003\u001a\r\u0000(6\u0003"+
		"\u001c\u000e\u0000)6\u0003\u001e\u000f\u0000*+\u0005\u0001\u0000\u0000"+
		"+,\u0003\u0002\u0001\u0000,-\u0005\u0002\u0000\u0000-6\u0001\u0000\u0000"+
		"\u0000./\u0007\u0000\u0000\u0000/6\u0003\u0002\u0001\u001101\u00052\u0000"+
		"\u000016\u0003\u0002\u0001\u001023\u0005\u001e\u0000\u000036\u0005,\u0000"+
		"\u000046\u0003\u0004\u0002\u00005#\u0001\u0000\u0000\u00005%\u0001\u0000"+
		"\u0000\u00005&\u0001\u0000\u0000\u00005\'\u0001\u0000\u0000\u00005(\u0001"+
		"\u0000\u0000\u00005)\u0001\u0000\u0000\u00005*\u0001\u0000\u0000\u0000"+
		"5.\u0001\u0000\u0000\u000050\u0001\u0000\u0000\u000052\u0001\u0000\u0000"+
		"\u000054\u0001\u0000\u0000\u00006c\u0001\u0000\u0000\u000078\n\u000f\u0000"+
		"\u000089\u0005:\u0000\u00009b\u0003\u0002\u0001\u000f:;\n\u000e\u0000"+
		"\u0000;<\u00059\u0000\u0000<b\u0003\u0002\u0001\u000e=>\n\r\u0000\u0000"+
		">?\u0007\u0001\u0000\u0000?b\u0003\u0002\u0001\u000e@A\n\f\u0000\u0000"+
		"AB\u0007\u0000\u0000\u0000Bb\u0003\u0002\u0001\rCD\n\u000b\u0000\u0000"+
		"DE\u00050\u0000\u0000Eb\u0003\u0002\u0001\fFG\n\n\u0000\u0000GH\u0005"+
		"1\u0000\u0000Hb\u0003\u0002\u0001\u000bIJ\n\t\u0000\u0000JK\u00053\u0000"+
		"\u0000Kb\u0003\u0002\u0001\nLM\n\b\u0000\u0000MN\u00054\u0000\u0000Nb"+
		"\u0003\u0002\u0001\tOP\n\u0007\u0000\u0000PQ\u00055\u0000\u0000Qb\u0003"+
		"\u0002\u0001\bRS\n\u0006\u0000\u0000ST\u00056\u0000\u0000Tb\u0003\u0002"+
		"\u0001\u0007UV\n\u0005\u0000\u0000VW\u00057\u0000\u0000Wb\u0003\u0002"+
		"\u0001\u0006XY\n\u0004\u0000\u0000YZ\u00058\u0000\u0000Zb\u0003\u0002"+
		"\u0001\u0005[\\\n\u0003\u0000\u0000\\]\u0005\u0003\u0000\u0000]^\u0003"+
		"\u0002\u0001\u0000^_\u0005\u0004\u0000\u0000_`\u0003\u0002\u0001\u0004"+
		"`b\u0001\u0000\u0000\u0000a7\u0001\u0000\u0000\u0000a:\u0001\u0000\u0000"+
		"\u0000a=\u0001\u0000\u0000\u0000a@\u0001\u0000\u0000\u0000aC\u0001\u0000"+
		"\u0000\u0000aF\u0001\u0000\u0000\u0000aI\u0001\u0000\u0000\u0000aL\u0001"+
		"\u0000\u0000\u0000aO\u0001\u0000\u0000\u0000aR\u0001\u0000\u0000\u0000"+
		"aU\u0001\u0000\u0000\u0000aX\u0001\u0000\u0000\u0000a[\u0001\u0000\u0000"+
		"\u0000be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000"+
		"\u0000\u0000d\u0003\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000"+
		"fg\u0003\u0006\u0003\u0000g\u0005\u0001\u0000\u0000\u0000hi\u0005\u0010"+
		"\u0000\u0000ij\u0005\u0005\u0000\u0000jk\u0005z\u0000\u0000kt\u0005\u0001"+
		"\u0000\u0000lq\u0003\u0002\u0001\u0000mn\u0005\u0006\u0000\u0000np\u0003"+
		"\u0002\u0001\u0000om\u0001\u0000\u0000\u0000ps\u0001\u0000\u0000\u0000"+
		"qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000ru\u0001\u0000\u0000"+
		"\u0000sq\u0001\u0000\u0000\u0000tl\u0001\u0000\u0000\u0000tu\u0001\u0000"+
		"\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0005\u0002\u0000\u0000w\u0007"+
		"\u0001\u0000\u0000\u0000xy\u0005\u0010\u0000\u0000yz\u0005\u0005\u0000"+
		"\u0000z{\u0005{\u0000\u0000{\u0084\u0005\u0001\u0000\u0000|\u0081\u0003"+
		"\u0002\u0001\u0000}~\u0005\u0006\u0000\u0000~\u0080\u0003\u0002\u0001"+
		"\u0000\u007f}\u0001\u0000\u0000\u0000\u0080\u0083\u0001\u0000\u0000\u0000"+
		"\u0081\u007f\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000"+
		"\u0082\u0085\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000"+
		"\u0084|\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085"+
		"\u0086\u0001\u0000\u0000\u0000\u0086\u0087\u0005\u0002\u0000\u0000\u0087"+
		"\t\u0001\u0000\u0000\u0000\u0088\u0089\u0005\u0010\u0000\u0000\u0089\u008a"+
		"\u0005\u0005\u0000\u0000\u008a\u008b\u0005|\u0000\u0000\u008b\u0094\u0005"+
		"\u0001\u0000\u0000\u008c\u0091\u0003\u0002\u0001\u0000\u008d\u008e\u0005"+
		"\u0006\u0000\u0000\u008e\u0090\u0003\u0002\u0001\u0000\u008f\u008d\u0001"+
		"\u0000\u0000\u0000\u0090\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001"+
		"\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0092\u0095\u0001"+
		"\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0094\u008c\u0001"+
		"\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0096\u0001"+
		"\u0000\u0000\u0000\u0096\u0097\u0005\u0002\u0000\u0000\u0097\u000b\u0001"+
		"\u0000\u0000\u0000\u0098\u0099\u0003\u000e\u0007\u0000\u0099\u009a\u0005"+
		"\u0007\u0000\u0000\u009a\u009c\u0001\u0000\u0000\u0000\u009b\u0098\u0001"+
		"\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000\u0000\u009d\u009b\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\r\u0001\u0000"+
		"\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005\"\u0000"+
		"\u0000\u00a1\u00aa\u0005\u0001\u0000\u0000\u00a2\u00a7\u0003\u0002\u0001"+
		"\u0000\u00a3\u00a4\u0005\u0006\u0000\u0000\u00a4\u00a6\u0003\u0002\u0001"+
		"\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a6\u00a9\u0001\u0000\u0000"+
		"\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000"+
		"\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000"+
		"\u0000\u00aa\u00a2\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u013b\u0005\u0002\u0000"+
		"\u0000\u00ad\u00ae\u0005,\u0000\u0000\u00ae\u00af\u0005/\u0000\u0000\u00af"+
		"\u013b\u0003\u0002\u0001\u0000\u00b0\u00b1\u0005\u0011\u0000\u0000\u00b1"+
		"\u00b2\u0005\u0001\u0000\u0000\u00b2\u00b3\u0003\u0002\u0001\u0000\u00b3"+
		"\u00b4\u0005\u0002\u0000\u0000\u00b4\u00b5\u0005\b\u0000\u0000\u00b5\u00b6"+
		"\u0003\f\u0006\u0000\u00b6\u00c1\u0005\t\u0000\u0000\u00b7\u00b8\u0005"+
		"\u0012\u0000\u0000\u00b8\u00b9\u0005\u0001\u0000\u0000\u00b9\u00ba\u0003"+
		"\u0002\u0001\u0000\u00ba\u00bb\u0005\u0002\u0000\u0000\u00bb\u00bc\u0005"+
		"\b\u0000\u0000\u00bc\u00bd\u0003\f\u0006\u0000\u00bd\u00be\u0005\t\u0000"+
		"\u0000\u00be\u00c0\u0001\u0000\u0000\u0000\u00bf\u00b7\u0001\u0000\u0000"+
		"\u0000\u00c0\u00c3\u0001\u0000\u0000\u0000\u00c1\u00bf\u0001\u0000\u0000"+
		"\u0000\u00c1\u00c2\u0001\u0000\u0000\u0000\u00c2\u00c9\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c1\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\u0013\u0000"+
		"\u0000\u00c5\u00c6\u0005\b\u0000\u0000\u00c6\u00c7\u0003\f\u0006\u0000"+
		"\u00c7\u00c8\u0005\t\u0000\u0000\u00c8\u00ca\u0001\u0000\u0000\u0000\u00c9"+
		"\u00c4\u0001\u0000\u0000\u0000\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca"+
		"\u013b\u0001\u0000\u0000\u0000\u00cb\u00cc\u0005\u0016\u0000\u0000\u00cc"+
		"\u00cd\u0005\u0001\u0000\u0000\u00cd\u00ce\u0005\u001e\u0000\u0000\u00ce"+
		"\u00cf\u0005,\u0000\u0000\u00cf\u00d0\u0005/\u0000\u0000\u00d0\u00d1\u0003"+
		"\u0002\u0001\u0000\u00d1\u00d2\u0005\u0007\u0000\u0000\u00d2\u00d3\u0005"+
		",\u0000\u0000\u00d3\u00d4\u00056\u0000\u0000\u00d4\u00d5\u0003\u0002\u0001"+
		"\u0000\u00d5\u00e1\u0005\u0007\u0000\u0000\u00d6\u00d7\u0003\u0002\u0001"+
		"\u0000\u00d7\u00d8\u0005\u0014\u0000\u0000\u00d8\u00e2\u0001\u0000\u0000"+
		"\u0000\u00d9\u00da\u0005,\u0000\u0000\u00da\u00db\u0005/\u0000\u0000\u00db"+
		"\u00dc\u0005,\u0000\u0000\u00dc\u00dd\u0007\u0000\u0000\u0000\u00dd\u00e2"+
		"\u0003\u0002\u0001\u0000\u00de\u00df\u0005,\u0000\u0000\u00df\u00e0\u0005"+
		"/\u0000\u0000\u00e0\u00e2\u0003\u0002\u0001\u0000\u00e1\u00d6\u0001\u0000"+
		"\u0000\u0000\u00e1\u00d9\u0001\u0000\u0000\u0000\u00e1\u00de\u0001\u0000"+
		"\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e4\u0005\u0002\u0000\u0000\u00e4\u00e5\u0005\b\u0000"+
		"\u0000\u00e5\u00e6\u0003\f\u0006\u0000\u00e6\u00e7\u0005\t\u0000\u0000"+
		"\u00e7\u013b\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005\u0015\u0000\u0000"+
		"\u00e9\u00ea\u0005\u0001\u0000\u0000\u00ea\u00eb\u0003\u0002\u0001\u0000"+
		"\u00eb\u00ec\u0005\u0002\u0000\u0000\u00ec\u00ed\u0005\b\u0000\u0000\u00ed"+
		"\u00ee\u0003\f\u0006\u0000\u00ee\u00ef\u0005\t\u0000\u0000\u00ef\u013b"+
		"\u0001\u0000\u0000\u0000\u00f0\u00f1\u0005\u0017\u0000\u0000\u00f1\u00f2"+
		"\u0005\u0001\u0000\u0000\u00f2\u00f3\u0005\u001e\u0000\u0000\u00f3\u00f4"+
		"\u0005,\u0000\u0000\u00f4\u00f5\u0005\u0004\u0000\u0000\u00f5\u00f6\u0003"+
		"\u0002\u0001\u0000\u00f6\u00f7\u0005\u0002\u0000\u0000\u00f7\u00f8\u0005"+
		"\b\u0000\u0000\u00f8\u00f9\u0003\f\u0006\u0000\u00f9\u00fa\u0005\t\u0000"+
		"\u0000\u00fa\u013b\u0001\u0000\u0000\u0000\u00fb\u00fc\u0005\u001a\u0000"+
		"\u0000\u00fc\u00fd\u0005\u0001\u0000\u0000\u00fd\u00fe\u0003\u0002\u0001"+
		"\u0000\u00fe\u00ff\u0005\u0002\u0000\u0000\u00ff\u0100\u0005\b\u0000\u0000"+
		"\u0100\u0101\u0003\f\u0006\u0000\u0101\u010c\u0005\t\u0000\u0000\u0102"+
		"\u0103\u0005\u001b\u0000\u0000\u0103\u0104\u0005\u0001\u0000\u0000\u0104"+
		"\u0105\u0003\u0002\u0001\u0000\u0105\u0106\u0005\u0002\u0000\u0000\u0106"+
		"\u0107\u0005\b\u0000\u0000\u0107\u0108\u0003\f\u0006\u0000\u0108\u0109"+
		"\u0005\t\u0000\u0000\u0109\u010b\u0001\u0000\u0000\u0000\u010a\u0102\u0001"+
		"\u0000\u0000\u0000\u010b\u010e\u0001\u0000\u0000\u0000\u010c\u010a\u0001"+
		"\u0000\u0000\u0000\u010c\u010d\u0001\u0000\u0000\u0000\u010d\u013b\u0001"+
		"\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000\u010f\u013b\u0007"+
		"\u0002\u0000\u0000\u0110\u0111\u0005\u001c\u0000\u0000\u0111\u0112\u0005"+
		"\u0001\u0000\u0000\u0112\u0113\u0003\u0002\u0001\u0000\u0113\u0114\u0005"+
		"\u0002\u0000\u0000\u0114\u013b\u0001\u0000\u0000\u0000\u0115\u0116\u0005"+
		"\u001e\u0000\u0000\u0116\u0117\u0005-\u0000\u0000\u0117\u0122\u0005\u0001"+
		"\u0000\u0000\u0118\u011d\u0003\u0002\u0001\u0000\u0119\u011a\u0005\u0006"+
		"\u0000\u0000\u011a\u011c\u0003\u0002\u0001\u0000\u011b\u0119\u0001\u0000"+
		"\u0000\u0000\u011c\u011f\u0001\u0000\u0000\u0000\u011d\u011b\u0001\u0000"+
		"\u0000\u0000\u011d\u011e\u0001\u0000\u0000\u0000\u011e\u0121\u0001\u0000"+
		"\u0000\u0000\u011f\u011d\u0001\u0000\u0000\u0000\u0120\u0118\u0001\u0000"+
		"\u0000\u0000\u0121\u0124\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000"+
		"\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u0125\u0001\u0000"+
		"\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0125\u0126\u0005\u0002"+
		"\u0000\u0000\u0126\u0127\u0005\b\u0000\u0000\u0127\u012d\u0003\f\u0006"+
		"\u0000\u0128\u012b\u0005\u001d\u0000\u0000\u0129\u012c\u0005,\u0000\u0000"+
		"\u012a\u012c\u0003\u0002\u0001\u0000\u012b\u0129\u0001\u0000\u0000\u0000"+
		"\u012b\u012a\u0001\u0000\u0000\u0000\u012c\u012e\u0001\u0000\u0000\u0000"+
		"\u012d\u0128\u0001\u0000\u0000\u0000\u012d\u012e\u0001\u0000\u0000\u0000"+
		"\u012e\u012f\u0001\u0000\u0000\u0000\u012f\u0130\u0005\t\u0000\u0000\u0130"+
		"\u013b\u0001\u0000\u0000\u0000\u0131\u0132\u0005\u0011\u0000\u0000\u0132"+
		"\u0133\u0005\u0001\u0000\u0000\u0133\u0134\u0003\u0002\u0001\u0000\u0134"+
		"\u0135\u0005\u0002\u0000\u0000\u0135\u0138\u0005\u001d\u0000\u0000\u0136"+
		"\u0139\u0005,\u0000\u0000\u0137\u0139\u0003\u0002\u0001\u0000\u0138\u0136"+
		"\u0001\u0000\u0000\u0000\u0138\u0137\u0001\u0000\u0000\u0000\u0139\u013b"+
		"\u0001\u0000\u0000\u0000\u013a\u00a0\u0001\u0000\u0000\u0000\u013a\u00ad"+
		"\u0001\u0000\u0000\u0000\u013a\u00b0\u0001\u0000\u0000\u0000\u013a\u00cb"+
		"\u0001\u0000\u0000\u0000\u013a\u00e8\u0001\u0000\u0000\u0000\u013a\u00f0"+
		"\u0001\u0000\u0000\u0000\u013a\u00fb\u0001\u0000\u0000\u0000\u013a\u010f"+
		"\u0001\u0000\u0000\u0000\u013a\u0110\u0001\u0000\u0000\u0000\u013a\u0115"+
		"\u0001\u0000\u0000\u0000\u013a\u0131\u0001\u0000\u0000\u0000\u013b\u000f"+
		"\u0001\u0000\u0000\u0000\u013c\u013d\u0003\u0012\t\u0000\u013d\u0011\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0005\u0010\u0000\u0000\u013f\u0140\u0005"+
		"\u0005\u0000\u0000\u0140\u0141\u0005}\u0000\u0000\u0141\u014a\u0005\u0001"+
		"\u0000\u0000\u0142\u0147\u0003\u0002\u0001\u0000\u0143\u0144\u0005\u0006"+
		"\u0000\u0000\u0144\u0146\u0003\u0002\u0001\u0000\u0145\u0143\u0001\u0000"+
		"\u0000\u0000\u0146\u0149\u0001\u0000\u0000\u0000\u0147\u0145\u0001\u0000"+
		"\u0000\u0000\u0147\u0148\u0001\u0000\u0000\u0000\u0148\u014b\u0001\u0000"+
		"\u0000\u0000\u0149\u0147\u0001\u0000\u0000\u0000\u014a\u0142\u0001\u0000"+
		"\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000\u014b\u014c\u0001\u0000"+
		"\u0000\u0000\u014c\u014d\u0005\u0002\u0000\u0000\u014d\u0013\u0001\u0000"+
		"\u0000\u0000\u014e\u0150\u0003\u0016\u000b\u0000\u014f\u014e\u0001\u0000"+
		"\u0000\u0000\u0150\u0153\u0001\u0000\u0000\u0000\u0151\u014f\u0001\u0000"+
		"\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0154\u0001\u0000"+
		"\u0000\u0000\u0153\u0151\u0001\u0000\u0000\u0000\u0154\u0155\u0003\u0018"+
		"\f\u0000\u0155\u0156\u0005\u0000\u0000\u0001\u0156\u0015\u0001\u0000\u0000"+
		"\u0000\u0157\u0158\u0005\u001e\u0000\u0000\u0158\u015b\u0005,\u0000\u0000"+
		"\u0159\u015a\u0005/\u0000\u0000\u015a\u015c\u0003\u0002\u0001\u0000\u015b"+
		"\u0159\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000\u0000\u015c"+
		"\u015d\u0001\u0000\u0000\u0000\u015d\u015e\u0005\u0007\u0000\u0000\u015e"+
		"\u0017\u0001\u0000\u0000\u0000\u015f\u0160\u0005\n\u0000\u0000\u0160\u0161"+
		"\u0005\u000b\u0000\u0000\u0161\u0162\u0005\u0001\u0000\u0000\u0162\u0163"+
		"\u0005\u0002\u0000\u0000\u0163\u0164\u0005\b\u0000\u0000\u0164\u0165\u0003"+
		"\f\u0006\u0000\u0165\u0166\u0005\t\u0000\u0000\u0166\u0019\u0001\u0000"+
		"\u0000\u0000\u0167\u0185\u0005\'\u0000\u0000\u0168\u0185\u0005%\u0000"+
		"\u0000\u0169\u0185\u0005&\u0000\u0000\u016a\u0185\u0005(\u0000\u0000\u016b"+
		"\u0174\u0005\f\u0000\u0000\u016c\u016e\t\u0000\u0000\u0000\u016d\u016c"+
		"\u0001\u0000\u0000\u0000\u016e\u0171\u0001\u0000\u0000\u0000\u016f\u0170"+
		"\u0001\u0000\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u0170\u0175"+
		"\u0001\u0000\u0000\u0000\u0171\u016f\u0001\u0000\u0000\u0000\u0172\u0175"+
		"\u0005\u0005\u0000\u0000\u0173\u0175\u0005\u0003\u0000\u0000\u0174\u016f"+
		"\u0001\u0000\u0000\u0000\u0174\u0172\u0001\u0000\u0000\u0000\u0174\u0173"+
		"\u0001\u0000\u0000\u0000\u0175\u0176\u0001\u0000\u0000\u0000\u0176\u0185"+
		"\u0005\f\u0000\u0000\u0177\u017d\u0005\r\u0000\u0000\u0178\u0179\u0003"+
		"\u0002\u0001\u0000\u0179\u017a\u0005\u0006\u0000\u0000\u017a\u017c\u0001"+
		"\u0000\u0000\u0000\u017b\u0178\u0001\u0000\u0000\u0000\u017c\u017f\u0001"+
		"\u0000\u0000\u0000\u017d\u017b\u0001\u0000\u0000\u0000\u017d\u017e\u0001"+
		"\u0000\u0000\u0000\u017e\u0181\u0001\u0000\u0000\u0000\u017f\u017d\u0001"+
		"\u0000\u0000\u0000\u0180\u0182\u0003\u0002\u0001\u0000\u0181\u0180\u0001"+
		"\u0000\u0000\u0000\u0181\u0182\u0001\u0000\u0000\u0000\u0182\u0183\u0001"+
		"\u0000\u0000\u0000\u0183\u0185\u0005\u000e\u0000\u0000\u0184\u0167\u0001"+
		"\u0000\u0000\u0000\u0184\u0168\u0001\u0000\u0000\u0000\u0184\u0169\u0001"+
		"\u0000\u0000\u0000\u0184\u016a\u0001\u0000\u0000\u0000\u0184\u016b\u0001"+
		"\u0000\u0000\u0000\u0184\u0177\u0001\u0000\u0000\u0000\u0185\u001b\u0001"+
		"\u0000\u0000\u0000\u0186\u0187\u0005\u000f\u0000\u0000\u0187\u0188\u0007"+
		"\u0003\u0000\u0000\u0188\u0189\u0005\u0006\u0000\u0000\u0189\u018a\u0007"+
		"\u0003\u0000\u0000\u018a\u001d\u0001\u0000\u0000\u0000\u018b\u018c\u0005"+
		"!\u0000\u0000\u018c\u0195\u0005\u0001\u0000\u0000\u018d\u0192\u0003\u0002"+
		"\u0001\u0000\u018e\u018f\u0005\u0006\u0000\u0000\u018f\u0191\u0003\u0002"+
		"\u0001\u0000\u0190\u018e\u0001\u0000\u0000\u0000\u0191\u0194\u0001\u0000"+
		"\u0000\u0000\u0192\u0190\u0001\u0000\u0000\u0000\u0192\u0193\u0001\u0000"+
		"\u0000\u0000\u0193\u0196\u0001\u0000\u0000\u0000\u0194\u0192\u0001\u0000"+
		"\u0000\u0000\u0195\u018d\u0001\u0000\u0000\u0000\u0195\u0196\u0001\u0000"+
		"\u0000\u0000\u0196\u0197\u0001\u0000\u0000\u0000\u0197\u01a6\u0005\u0002"+
		"\u0000\u0000\u0198\u0199\u0005-\u0000\u0000\u0199\u01a2\u0005\u0001\u0000"+
		"\u0000\u019a\u019f\u0003\u0002\u0001\u0000\u019b\u019c\u0005\u0006\u0000"+
		"\u0000\u019c\u019e\u0003\u0002\u0001\u0000\u019d\u019b\u0001\u0000\u0000"+
		"\u0000\u019e\u01a1\u0001\u0000\u0000\u0000\u019f\u019d\u0001\u0000\u0000"+
		"\u0000\u019f\u01a0\u0001\u0000\u0000\u0000\u01a0\u01a3\u0001\u0000\u0000"+
		"\u0000\u01a1\u019f\u0001\u0000\u0000\u0000\u01a2\u019a\u0001\u0000\u0000"+
		"\u0000\u01a2\u01a3\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001\u0000\u0000"+
		"\u0000\u01a4\u01a6\u0005\u0002\u0000\u0000\u01a5\u018b\u0001\u0000\u0000"+
		"\u0000\u01a5\u0198\u0001\u0000\u0000\u0000\u01a6\u001f\u0001\u0000\u0000"+
		"\u0000$5acqt\u0081\u0084\u0091\u0094\u009d\u00a7\u00aa\u00c1\u00c9\u00e1"+
		"\u010c\u011d\u0122\u012b\u012d\u0138\u013a\u0147\u014a\u0151\u015b\u016f"+
		"\u0174\u017d\u0181\u0184\u0192\u0195\u019f\u01a2\u01a5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}