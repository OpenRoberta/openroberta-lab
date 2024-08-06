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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, IF=37, ELSEIF=38, ELSE=39, 
		STEP=40, WHILE=41, REPEATFOR=42, REPEATFOREACH=43, BREAK=44, CONTINUE=45, 
		WAIT=46, ORWAITFOR=47, WAITMS=48, RETURN=49, PRIMITIVETYPE=50, NEWLINE=51, 
		WS=52, FNAME=53, FNAMESTMT=54, CONST=55, NULL=56, INT=57, FLOAT=58, COLOR=59, 
		BOOL=60, ACCELEROMETER_SENSOR=61, COLOR_SENSOR=62, COMPASS_CALIBRATE=63, 
		COMPASS_SENSOR=64, DETECT_MARK_SENSOR=65, DROP_SENSOR=66, EHT_COLOR_SENSOR=67, 
		ENCODER_RESET=68, ENCODER_SENSOR=69, ENVIRONMENTAL_SENSOR=70, GESTURE_SENSOR=71, 
		GET_LINE_SENSOR=72, GYRO_RESET=73, GYRO_SENSOR=74, HT_COLOR_SENSOR=75, 
		HUMIDITY_SENSOR=76, IR_SEEKER_SENSOR=77, INFRARED_SENSOR=78, KEYS_SENSOR=79, 
		LIGHT_SENSOR=80, MOISTURE_SENSOR=81, MOTION_SENSOR=82, PARTICLE_SENSOR=83, 
		PIN_GET_VALUE_SENSOR=84, PIN_TOUCH_SENSOR=85, PULSE_SENSOR=86, RFID_SENSOR=87, 
		SOUND_SENSOR=88, TEMPERATURE_SENSOR=89, TIMER_RESET=90, TIMER_SENSOR=91, 
		TOUCH_SENSOR=92, ULTRASONIC_SENSOR=93, VEML_LIGHT_SENSOR=94, VOLTAGE_SENSOR=95, 
		CAMERA_SENSOR=96, CAMERA_THRESHOLD=97, CODE_PAD_SENSOR=98, COLOUR_BLOB=99, 
		DETECT_FACE_INFORMATION=100, DETECT_FACE_SENSOR=101, ELECTRIC_CURRENT_SENSOR=102, 
		FSR_SENSOR=103, GPS_SENSOR=104, GYRO_RESET_AXIS=105, JOYSTICK=106, LOGO_TOUCH_SENSOR=107, 
		MARKER_INFORMATION=108, NAO_MARK_INFORMATION=109, ODOMETRY_SENSOR=110, 
		ODOMETRY_SENSOR_RESET=111, OPTICAL_SENSOR=112, PIN_SET_TOUCH_MODE=113, 
		QUAD_RGB_SENSOR=114, RADIO_RSSI_SENSOR=115, RECOGNIZE_WORD=116, RESET_SENSOR=117, 
		SOUND_RECORD=118, TAP_SENSOR=119, NAME=120, HEX=121, STR=122, SET=123, 
		AND=124, OR=125, NOT=126, EQUAL=127, NEQUAL=128, GET=129, LET=130, GEQ=131, 
		LEQ=132, MOD=133, POW=134, MUL=135, DIV=136, ADD=137, SUB=138, UP=139, 
		DOWN=140, BACK=141, FRONT=142, NO=143, ANY=144;
	public static final int
		RULE_program = 0, RULE_declaration = 1, RULE_mainBlock = 2, RULE_userFunc = 3, 
		RULE_nameDecl = 4, RULE_statementList = 5, RULE_stmt = 6, RULE_robotStmt = 7, 
		RULE_expression = 8, RULE_expr = 9, RULE_literal = 10, RULE_connExpr = 11, 
		RULE_funCall = 12, RULE_robotExpr = 13, RULE_robotMicrobitv2Expr = 14, 
		RULE_microbitv2SensorExpr = 15, RULE_robotMicrobitv2Stmt = 16, RULE_microbitv2SensorStmt = 17, 
		RULE_robotWeDoExpr = 18, RULE_weDoSensorExpr = 19, RULE_robotWeDoStmt = 20, 
		RULE_wedoSensorStmt = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "declaration", "mainBlock", "userFunc", "nameDecl", "statementList", 
			"stmt", "robotStmt", "expression", "expr", "literal", "connExpr", "funCall", 
			"robotExpr", "robotMicrobitv2Expr", "microbitv2SensorExpr", "robotMicrobitv2Stmt", 
			"microbitv2SensorStmt", "robotWeDoExpr", "weDoSensorExpr", "robotWeDoStmt", 
			"wedoSensorStmt"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'void'", "'main'", "'('", "')'", "'{'", "'}'", "','", "':'", 
			"'?'", "'\"'", "'.'", "'['", "']'", "'connect'", "'microbitv2'", "'isPressed'", 
			"'getAngle'", "'currentGesture'", "'up'", "'down'", "'faceDown'", "'faceUp'", 
			"'shake'", "'freefall'", "'getLevel'", "'analog'", "'digital'", "'pulseHigh'", 
			"'pulseLow'", "'microphone'", "'soundLevel'", "'capacitive'", "'resistive'", 
			"'wedo'", "'isTilted'", "'if'", null, "'else'", "'++'", "'while'", "'for'", 
			null, "'break'", "'continue'", "'waitUntil'", "'orWaitFor'", "'wait ms'", 
			"'return'", null, null, null, null, null, null, "'null'", null, null, 
			null, null, "'accelerometerSensor'", "'colorSensor'", "'compassCalibrate'", 
			"'compassSensor'", "'detectMarkSensor'", "'dropSensor'", "'ehtColorSensor'", 
			"'encoderReset'", "'encoderSensor'", "'environmentalSensor'", "'gestureSensor'", 
			"'getLineSensor'", "'gyroReset'", "'gyroSensor'", "'htColorSensor'", 
			"'humiditySensor'", "'irSeekerSensor'", "'infraredSensor'", "'keysSensor'", 
			"'lightSensor'", "'moistureSensor'", "'motionSensor'", "'particleSensor'", 
			"'pinGetValueSensor'", "'pinTouchSensor'", "'pulseSensor'", "'rfidSensor'", 
			"'soundSensor'", "'temperatureSensor'", "'timerReset'", "'timerSensor'", 
			"'touchSensor'", "'ultrasonicSensor'", "'vemlLightSensor'", "'voltageSensor'", 
			"'cameraSensor'", "'cameraThreshold'", "'codePadSensor'", "'colourBlob'", 
			"'detectFaceInformation'", "'detectFaceSensor'", "'electricCurrentSensor'", 
			"'fsrSensor'", "'gpsSensor'", "'gyroResetAxis'", "'joystick'", "'logoTouchSensor'", 
			"'markerInformation'", "'naoMarkInformation'", "'odometrySensor'", "'odometrySensorReset'", 
			"'opticalSensor'", "'pinSetTouchMode'", "'quadRGBSensor'", "'radioRssiSensor'", 
			"'recognizeWord'", "'resetSensor'", "'soundRecord'", "'tapSensor'", null, 
			null, null, "'='", "'&&'", "'||'", "'!'", "'=='", "'!='", "'>'", "'<'", 
			"'>='", "'<='", "'%'", "'^'", "'*'", "'/'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "IF", "ELSEIF", "ELSE", "STEP", "WHILE", "REPEATFOR", "REPEATFOREACH", 
			"BREAK", "CONTINUE", "WAIT", "ORWAITFOR", "WAITMS", "RETURN", "PRIMITIVETYPE", 
			"NEWLINE", "WS", "FNAME", "FNAMESTMT", "CONST", "NULL", "INT", "FLOAT", 
			"COLOR", "BOOL", "ACCELEROMETER_SENSOR", "COLOR_SENSOR", "COMPASS_CALIBRATE", 
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
			"RECOGNIZE_WORD", "RESET_SENSOR", "SOUND_RECORD", "TAP_SENSOR", "NAME", 
			"HEX", "STR", "SET", "AND", "OR", "NOT", "EQUAL", "NEQUAL", "GET", "LET", 
			"GEQ", "LEQ", "MOD", "POW", "MUL", "DIV", "ADD", "SUB", "UP", "DOWN", 
			"BACK", "FRONT", "NO", "ANY"
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
		public List<UserFuncContext> userFunc() {
			return getRuleContexts(UserFuncContext.class);
		}
		public UserFuncContext userFunc(int i) {
			return getRuleContext(UserFuncContext.class,i);
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
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(44);
				declaration();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
			mainBlock();
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(51);
				userFunc();
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
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
		public NameDeclContext nameDecl() {
			return getRuleContext(NameDeclContext.class,0);
		}
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
		enterRule(_localctx, 2, RULE_declaration);
		try {
			_localctx = new VariableDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			nameDecl();
			setState(60);
			match(SET);
			setState(61);
			expr(0);
			setState(62);
			match(T__0);
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
		enterRule(_localctx, 4, RULE_mainBlock);
		try {
			_localctx = new MainFuncContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(T__1);
			setState(65);
			match(T__2);
			setState(66);
			match(T__3);
			setState(67);
			match(T__4);
			setState(68);
			match(T__5);
			setState(69);
			statementList();
			setState(70);
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
	public static class UserFuncContext extends ParserRuleContext {
		public UserFuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userFunc; }
	 
		public UserFuncContext() { }
		public void copyFrom(UserFuncContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncUserContext extends UserFuncContext {
		public Token op;
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public List<TerminalNode> NAME() { return getTokens(ExprlyParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(ExprlyParser.NAME, i);
		}
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
		public FuncUserContext(UserFuncContext ctx) { copyFrom(ctx); }
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

	public final UserFuncContext userFunc() throws RecognitionException {
		UserFuncContext _localctx = new UserFuncContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_userFunc);
		int _la;
		try {
			_localctx = new FuncUserContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(PRIMITIVETYPE);
			setState(73);
			match(NAME);
			setState(74);
			match(T__3);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
				{
				{
				setState(75);
				expr(0);
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(76);
					match(T__7);
					setState(77);
					expr(0);
					}
					}
					setState(82);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(88);
			match(T__4);
			setState(89);
			match(T__5);
			setState(90);
			statementList();
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURN) {
				{
				setState(91);
				((FuncUserContext)_localctx).op = match(RETURN);
				setState(94);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(92);
					match(NAME);
					}
					break;
				case 2:
					{
					setState(93);
					expr(0);
					}
					break;
				}
				}
			}

			setState(98);
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
	public static class NameDeclContext extends ParserRuleContext {
		public NameDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nameDecl; }
	 
		public NameDeclContext() { }
		public void copyFrom(NameDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParamsMethodContext extends NameDeclContext {
		public TerminalNode PRIMITIVETYPE() { return getToken(ExprlyParser.PRIMITIVETYPE, 0); }
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
		public ParamsMethodContext(NameDeclContext ctx) { copyFrom(ctx); }
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

	public final NameDeclContext nameDecl() throws RecognitionException {
		NameDeclContext _localctx = new NameDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_nameDecl);
		try {
			_localctx = new ParamsMethodContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(PRIMITIVETYPE);
			setState(101);
			match(NAME);
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
		enterRule(_localctx, 10, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 18434583750049792L) != 0) || _la==NAME) {
				{
				{
				setState(103);
				stmt();
				setState(104);
				match(T__0);
				}
				}
				setState(110);
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
	public static class RobotStatementContext extends StmtContext {
		public RobotStmtContext robotStmt() {
			return getRuleContext(RobotStmtContext.class,0);
		}
		public RobotStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StmtUsedDefCallContext extends StmtContext {
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StmtUsedDefCallContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterStmtUsedDefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitStmtUsedDefCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitStmtUsedDefCall(this);
			else return visitor.visitChildren(this);
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
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
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
		public List<TerminalNode> NAME() { return getTokens(ExprlyParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(ExprlyParser.NAME, i);
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
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
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
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
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
		enterRule(_localctx, 12, RULE_stmt);
		int _la;
		try {
			setState(251);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				_localctx = new StmtFuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(111);
				match(FNAMESTMT);
				setState(112);
				match(T__3);
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
					{
					setState(113);
					expr(0);
					setState(118);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(114);
						match(T__7);
						setState(115);
						expr(0);
						}
						}
						setState(120);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(123);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new StmtUsedDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				match(NAME);
				setState(125);
				match(T__3);
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
					{
					setState(126);
					expr(0);
					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(127);
						match(T__7);
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
				match(T__4);
				}
				break;
			case 3:
				_localctx = new BinaryVarAssignContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(137);
				match(NAME);
				setState(138);
				((BinaryVarAssignContext)_localctx).op = match(SET);
				setState(139);
				expr(0);
				}
				break;
			case 4:
				_localctx = new ConditionStatementBlockContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(140);
				match(IF);
				setState(141);
				match(T__3);
				setState(142);
				expr(0);
				setState(143);
				match(T__4);
				setState(144);
				match(T__5);
				setState(145);
				statementList();
				setState(146);
				match(T__6);
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELSEIF) {
					{
					{
					setState(147);
					match(ELSEIF);
					setState(148);
					match(T__3);
					setState(149);
					expr(0);
					setState(150);
					match(T__4);
					setState(151);
					match(T__5);
					setState(152);
					statementList();
					setState(153);
					match(T__6);
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
					match(T__5);
					setState(162);
					statementList();
					setState(163);
					match(T__6);
					}
				}

				}
				break;
			case 5:
				_localctx = new RepeatForContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(167);
				match(REPEATFOR);
				setState(168);
				match(T__3);
				setState(169);
				match(PRIMITIVETYPE);
				setState(170);
				match(NAME);
				setState(171);
				match(SET);
				setState(172);
				expr(0);
				setState(173);
				match(T__0);
				setState(174);
				match(NAME);
				setState(175);
				match(LET);
				setState(176);
				expr(0);
				setState(177);
				match(T__0);
				setState(189);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
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
					match(NAME);
					setState(182);
					match(SET);
					setState(183);
					match(NAME);
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
					match(NAME);
					setState(187);
					match(SET);
					setState(188);
					expr(0);
					}
					break;
				}
				setState(191);
				match(T__4);
				setState(192);
				match(T__5);
				setState(193);
				statementList();
				setState(194);
				match(T__6);
				}
				break;
			case 6:
				_localctx = new RepeatStatementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(196);
				match(WHILE);
				setState(197);
				match(T__3);
				setState(198);
				expr(0);
				setState(199);
				match(T__4);
				setState(200);
				match(T__5);
				setState(201);
				statementList();
				setState(202);
				match(T__6);
				}
				break;
			case 7:
				_localctx = new RepeatForEachContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(204);
				match(REPEATFOREACH);
				setState(205);
				match(T__3);
				setState(206);
				match(PRIMITIVETYPE);
				setState(207);
				match(NAME);
				setState(208);
				match(T__8);
				setState(209);
				expr(0);
				setState(210);
				match(T__4);
				setState(211);
				match(T__5);
				setState(212);
				statementList();
				setState(213);
				match(T__6);
				}
				break;
			case 8:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(215);
				match(WAIT);
				setState(216);
				match(T__3);
				setState(217);
				expr(0);
				setState(218);
				match(T__4);
				setState(219);
				match(T__5);
				setState(220);
				statementList();
				setState(221);
				match(T__6);
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ORWAITFOR) {
					{
					{
					setState(222);
					((WaitStatementContext)_localctx).op = match(ORWAITFOR);
					setState(223);
					match(T__3);
					setState(224);
					expr(0);
					setState(225);
					match(T__4);
					setState(226);
					match(T__5);
					setState(227);
					statementList();
					setState(228);
					match(T__6);
					}
					}
					setState(234);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 9:
				_localctx = new FlowControlContext(_localctx);
				enterOuterAlt(_localctx, 9);
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
			case 10:
				_localctx = new WaitTimeStatementContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(236);
				match(WAITMS);
				setState(237);
				match(T__3);
				setState(238);
				expr(0);
				setState(239);
				match(T__4);
				}
				break;
			case 11:
				_localctx = new UserFuncIfStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(241);
				match(IF);
				setState(242);
				match(T__3);
				setState(243);
				expr(0);
				setState(244);
				match(T__4);
				setState(245);
				match(RETURN);
				setState(248);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(246);
					match(NAME);
					}
					break;
				case 2:
					{
					setState(247);
					expr(0);
					}
					break;
				}
				}
				break;
			case 12:
				_localctx = new RobotStatementContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(250);
				robotStmt();
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
		public RobotMicrobitv2StmtContext robotMicrobitv2Stmt() {
			return getRuleContext(RobotMicrobitv2StmtContext.class,0);
		}
		public RobotWeDoStmtContext robotWeDoStmt() {
			return getRuleContext(RobotWeDoStmtContext.class,0);
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
		enterRule(_localctx, 14, RULE_robotStmt);
		try {
			setState(255);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(253);
				robotMicrobitv2Stmt();
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 2);
				{
				setState(254);
				robotWeDoStmt();
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
		enterRule(_localctx, 16, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			expr(0);
			setState(258);
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
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
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
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				_localctx = new NullConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(261);
				match(NULL);
				}
				break;
			case 2:
				{
				_localctx = new MathConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(262);
				match(CONST);
				}
				break;
			case 3:
				{
				_localctx = new VarNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(263);
				match(NAME);
				}
				break;
			case 4:
				{
				_localctx = new LiteralExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				literal();
				}
				break;
			case 5:
				{
				_localctx = new ConnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(265);
				connExpr();
				}
				break;
			case 6:
				{
				_localctx = new FuncExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(266);
				funCall();
				}
				break;
			case 7:
				{
				_localctx = new ParentheseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(267);
				match(T__3);
				setState(268);
				expr(0);
				setState(269);
				match(T__4);
				}
				break;
			case 8:
				{
				_localctx = new UnaryNContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(271);
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
				setState(272);
				expr(16);
				}
				break;
			case 9:
				{
				_localctx = new UnaryBContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(273);
				((UnaryBContext)_localctx).op = match(NOT);
				setState(274);
				expr(15);
				}
				break;
			case 10:
				{
				_localctx = new RobotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(275);
				robotExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(322);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(320);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(278);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(279);
						((BinaryNContext)_localctx).op = match(POW);
						setState(280);
						expr(14);
						}
						break;
					case 2:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(281);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(282);
						((BinaryNContext)_localctx).op = match(MOD);
						setState(283);
						expr(13);
						}
						break;
					case 3:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(284);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(285);
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
						setState(286);
						expr(13);
						}
						break;
					case 4:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(287);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(288);
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
						setState(289);
						expr(12);
						}
						break;
					case 5:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(290);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(291);
						((BinaryBContext)_localctx).op = match(AND);
						setState(292);
						expr(11);
						}
						break;
					case 6:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(293);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(294);
						((BinaryBContext)_localctx).op = match(OR);
						setState(295);
						expr(10);
						}
						break;
					case 7:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(296);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(297);
						((BinaryBContext)_localctx).op = match(EQUAL);
						setState(298);
						expr(9);
						}
						break;
					case 8:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(299);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(300);
						((BinaryBContext)_localctx).op = match(NEQUAL);
						setState(301);
						expr(8);
						}
						break;
					case 9:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(302);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(303);
						((BinaryBContext)_localctx).op = match(GET);
						setState(304);
						expr(7);
						}
						break;
					case 10:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(305);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(306);
						((BinaryBContext)_localctx).op = match(LET);
						setState(307);
						expr(6);
						}
						break;
					case 11:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(308);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(309);
						((BinaryBContext)_localctx).op = match(GEQ);
						setState(310);
						expr(5);
						}
						break;
					case 12:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(311);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(312);
						((BinaryBContext)_localctx).op = match(LEQ);
						setState(313);
						expr(4);
						}
						break;
					case 13:
						{
						_localctx = new IfElseOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(314);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(315);
						match(T__9);
						setState(316);
						expr(0);
						setState(317);
						match(T__8);
						setState(318);
						expr(3);
						}
						break;
					}
					} 
				}
				setState(324);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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
		enterRule(_localctx, 20, RULE_literal);
		int _la;
		try {
			int _alt;
			setState(354);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLOR:
				_localctx = new ColContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(325);
				match(COLOR);
				}
				break;
			case INT:
				_localctx = new IntConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(326);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatConstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(327);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolConstBContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(328);
				match(BOOL);
				}
				break;
			case T__10:
				_localctx = new ConstStrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(329);
				match(T__10);
				setState(338);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(333);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(330);
							matchWildcard();
							}
							} 
						}
						setState(335);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
					}
					}
					break;
				case 2:
					{
					setState(336);
					match(T__11);
					}
					break;
				case 3:
					{
					setState(337);
					match(T__9);
					}
					break;
				}
				setState(340);
				match(T__10);
				}
				break;
			case T__12:
				_localctx = new ListExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(341);
				match(T__12);
				setState(347);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(342);
						expr(0);
						setState(343);
						match(T__7);
						}
						} 
					}
					setState(349);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
				}
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
					{
					setState(350);
					expr(0);
					}
				}

				setState(353);
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
		public List<TerminalNode> NAME() { return getTokens(ExprlyParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(ExprlyParser.NAME, i);
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
		enterRule(_localctx, 22, RULE_connExpr);
		int _la;
		try {
			_localctx = new ConnContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			match(T__14);
			setState(357);
			((ConnContext)_localctx).op0 = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==NAME || _la==STR) ) {
				((ConnContext)_localctx).op0 = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(358);
			match(T__7);
			setState(359);
			((ConnContext)_localctx).op1 = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==NAME || _la==STR) ) {
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
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
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
		enterRule(_localctx, 24, RULE_funCall);
		int _la;
		try {
			setState(387);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FNAME:
				_localctx = new FuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(361);
				match(FNAME);
				setState(362);
				match(T__3);
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
					{
					setState(363);
					expr(0);
					setState(368);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(364);
						match(T__7);
						setState(365);
						expr(0);
						}
						}
						setState(370);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(373);
				match(T__4);
				}
				break;
			case NAME:
				_localctx = new UserDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(374);
				match(NAME);
				setState(375);
				match(T__3);
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2278821445809317904L) != 0) || ((((_la - 120)) & ~0x3f) == 0 && ((1L << (_la - 120)) & 393281L) != 0)) {
					{
					setState(376);
					expr(0);
					setState(381);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(377);
						match(T__7);
						setState(378);
						expr(0);
						}
						}
						setState(383);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(386);
				match(T__4);
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
	public static class RobotExprContext extends ParserRuleContext {
		public RobotMicrobitv2ExprContext robotMicrobitv2Expr() {
			return getRuleContext(RobotMicrobitv2ExprContext.class,0);
		}
		public RobotWeDoExprContext robotWeDoExpr() {
			return getRuleContext(RobotWeDoExprContext.class,0);
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
		enterRule(_localctx, 26, RULE_robotExpr);
		try {
			setState(391);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(389);
				robotMicrobitv2Expr();
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 2);
				{
				setState(390);
				robotWeDoExpr();
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
	public static class RobotMicrobitv2ExprContext extends ParserRuleContext {
		public RobotMicrobitv2ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotMicrobitv2Expr; }
	 
		public RobotMicrobitv2ExprContext() { }
		public void copyFrom(RobotMicrobitv2ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotMicrobitv2ExpressionContext extends RobotMicrobitv2ExprContext {
		public Microbitv2SensorExprContext microbitv2SensorExpr() {
			return getRuleContext(Microbitv2SensorExprContext.class,0);
		}
		public RobotMicrobitv2ExpressionContext(RobotMicrobitv2ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotMicrobitv2Expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotMicrobitv2Expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotMicrobitv2Expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotMicrobitv2ExprContext robotMicrobitv2Expr() throws RecognitionException {
		RobotMicrobitv2ExprContext _localctx = new RobotMicrobitv2ExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_robotMicrobitv2Expr);
		try {
			_localctx = new RobotMicrobitv2ExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(T__15);
			setState(394);
			match(T__11);
			setState(395);
			microbitv2SensorExpr();
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
	public static class Microbitv2SensorExprContext extends ParserRuleContext {
		public Token op;
		public TerminalNode ACCELEROMETER_SENSOR() { return getToken(ExprlyParser.ACCELEROMETER_SENSOR, 0); }
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
		public TerminalNode LOGO_TOUCH_SENSOR() { return getToken(ExprlyParser.LOGO_TOUCH_SENSOR, 0); }
		public TerminalNode COMPASS_SENSOR() { return getToken(ExprlyParser.COMPASS_SENSOR, 0); }
		public TerminalNode GESTURE_SENSOR() { return getToken(ExprlyParser.GESTURE_SENSOR, 0); }
		public TerminalNode KEYS_SENSOR() { return getToken(ExprlyParser.KEYS_SENSOR, 0); }
		public TerminalNode LIGHT_SENSOR() { return getToken(ExprlyParser.LIGHT_SENSOR, 0); }
		public TerminalNode PIN_GET_VALUE_SENSOR() { return getToken(ExprlyParser.PIN_GET_VALUE_SENSOR, 0); }
		public TerminalNode PIN_TOUCH_SENSOR() { return getToken(ExprlyParser.PIN_TOUCH_SENSOR, 0); }
		public TerminalNode INT() { return getToken(ExprlyParser.INT, 0); }
		public TerminalNode SOUND_SENSOR() { return getToken(ExprlyParser.SOUND_SENSOR, 0); }
		public TerminalNode TEMPERATURE_SENSOR() { return getToken(ExprlyParser.TEMPERATURE_SENSOR, 0); }
		public TerminalNode TIMER_SENSOR() { return getToken(ExprlyParser.TIMER_SENSOR, 0); }
		public Microbitv2SensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_microbitv2SensorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMicrobitv2SensorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMicrobitv2SensorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMicrobitv2SensorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Microbitv2SensorExprContext microbitv2SensorExpr() throws RecognitionException {
		Microbitv2SensorExprContext _localctx = new Microbitv2SensorExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_microbitv2SensorExpr);
		int _la;
		try {
			setState(453);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ACCELEROMETER_SENSOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(397);
				match(ACCELEROMETER_SENSOR);
				setState(398);
				match(T__3);
				setState(399);
				match(NAME);
				setState(400);
				match(T__4);
				}
				break;
			case LOGO_TOUCH_SENSOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(401);
				match(LOGO_TOUCH_SENSOR);
				setState(402);
				match(T__11);
				setState(403);
				match(T__16);
				setState(404);
				match(T__3);
				setState(405);
				match(T__4);
				}
				break;
			case COMPASS_SENSOR:
				enterOuterAlt(_localctx, 3);
				{
				setState(406);
				match(COMPASS_SENSOR);
				setState(407);
				match(T__11);
				setState(408);
				match(T__17);
				setState(409);
				match(T__3);
				setState(410);
				match(T__4);
				}
				break;
			case GESTURE_SENSOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(411);
				match(GESTURE_SENSOR);
				setState(412);
				match(T__11);
				setState(413);
				match(T__18);
				setState(414);
				match(T__3);
				setState(415);
				((Microbitv2SensorExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 66060288L) != 0)) ) {
					((Microbitv2SensorExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(416);
				match(T__4);
				}
				break;
			case KEYS_SENSOR:
				enterOuterAlt(_localctx, 5);
				{
				setState(417);
				match(KEYS_SENSOR);
				setState(418);
				match(T__11);
				setState(419);
				match(T__16);
				setState(420);
				match(T__3);
				setState(421);
				match(NAME);
				setState(422);
				match(T__4);
				}
				break;
			case LIGHT_SENSOR:
				enterOuterAlt(_localctx, 6);
				{
				setState(423);
				match(LIGHT_SENSOR);
				setState(424);
				match(T__11);
				setState(425);
				match(T__25);
				setState(426);
				match(T__3);
				setState(427);
				match(T__4);
				}
				break;
			case PIN_GET_VALUE_SENSOR:
				enterOuterAlt(_localctx, 7);
				{
				setState(428);
				match(PIN_GET_VALUE_SENSOR);
				setState(429);
				match(T__3);
				setState(430);
				match(NAME);
				setState(431);
				match(T__7);
				setState(432);
				((Microbitv2SensorExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2013265920L) != 0)) ) {
					((Microbitv2SensorExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(433);
				match(T__4);
				}
				break;
			case PIN_TOUCH_SENSOR:
				enterOuterAlt(_localctx, 8);
				{
				setState(434);
				match(PIN_TOUCH_SENSOR);
				setState(435);
				match(T__11);
				setState(436);
				match(T__16);
				setState(437);
				match(T__3);
				setState(438);
				match(INT);
				setState(439);
				match(T__4);
				}
				break;
			case SOUND_SENSOR:
				enterOuterAlt(_localctx, 9);
				{
				setState(440);
				match(SOUND_SENSOR);
				setState(441);
				match(T__11);
				setState(442);
				match(T__30);
				setState(443);
				match(T__11);
				setState(444);
				match(T__31);
				setState(445);
				match(T__3);
				setState(446);
				match(T__4);
				}
				break;
			case TEMPERATURE_SENSOR:
				enterOuterAlt(_localctx, 10);
				{
				setState(447);
				match(TEMPERATURE_SENSOR);
				setState(448);
				match(T__3);
				setState(449);
				match(T__4);
				}
				break;
			case TIMER_SENSOR:
				enterOuterAlt(_localctx, 11);
				{
				setState(450);
				match(TIMER_SENSOR);
				setState(451);
				match(T__3);
				setState(452);
				match(T__4);
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
	public static class RobotMicrobitv2StmtContext extends ParserRuleContext {
		public RobotMicrobitv2StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotMicrobitv2Stmt; }
	 
		public RobotMicrobitv2StmtContext() { }
		public void copyFrom(RobotMicrobitv2StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotMicrobitv2StatementContext extends RobotMicrobitv2StmtContext {
		public Microbitv2SensorStmtContext microbitv2SensorStmt() {
			return getRuleContext(Microbitv2SensorStmtContext.class,0);
		}
		public RobotMicrobitv2StatementContext(RobotMicrobitv2StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotMicrobitv2Statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotMicrobitv2Statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotMicrobitv2Statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotMicrobitv2StmtContext robotMicrobitv2Stmt() throws RecognitionException {
		RobotMicrobitv2StmtContext _localctx = new RobotMicrobitv2StmtContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_robotMicrobitv2Stmt);
		try {
			_localctx = new RobotMicrobitv2StatementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(455);
			match(T__15);
			setState(456);
			match(T__11);
			setState(457);
			microbitv2SensorStmt();
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
	public static class Microbitv2SensorStmtContext extends ParserRuleContext {
		public Token op;
		public TerminalNode PIN_SET_TOUCH_MODE() { return getToken(ExprlyParser.PIN_SET_TOUCH_MODE, 0); }
		public TerminalNode INT() { return getToken(ExprlyParser.INT, 0); }
		public TerminalNode TIMER_RESET() { return getToken(ExprlyParser.TIMER_RESET, 0); }
		public Microbitv2SensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_microbitv2SensorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterMicrobitv2SensorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitMicrobitv2SensorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitMicrobitv2SensorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Microbitv2SensorStmtContext microbitv2SensorStmt() throws RecognitionException {
		Microbitv2SensorStmtContext _localctx = new Microbitv2SensorStmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_microbitv2SensorStmt);
		int _la;
		try {
			setState(468);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PIN_SET_TOUCH_MODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(459);
				match(PIN_SET_TOUCH_MODE);
				setState(460);
				match(T__3);
				setState(461);
				match(INT);
				setState(462);
				match(T__7);
				setState(463);
				((Microbitv2SensorStmtContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__32 || _la==T__33) ) {
					((Microbitv2SensorStmtContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(464);
				match(T__4);
				}
				break;
			case TIMER_RESET:
				enterOuterAlt(_localctx, 2);
				{
				setState(465);
				match(TIMER_RESET);
				setState(466);
				match(T__3);
				setState(467);
				match(T__4);
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
	public static class RobotWeDoExprContext extends ParserRuleContext {
		public RobotWeDoExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotWeDoExpr; }
	 
		public RobotWeDoExprContext() { }
		public void copyFrom(RobotWeDoExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotWeDoExpressionContext extends RobotWeDoExprContext {
		public WeDoSensorExprContext weDoSensorExpr() {
			return getRuleContext(WeDoSensorExprContext.class,0);
		}
		public RobotWeDoExpressionContext(RobotWeDoExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotWeDoExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotWeDoExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotWeDoExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotWeDoExprContext robotWeDoExpr() throws RecognitionException {
		RobotWeDoExprContext _localctx = new RobotWeDoExprContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_robotWeDoExpr);
		try {
			_localctx = new RobotWeDoExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(T__34);
			setState(471);
			match(T__11);
			setState(472);
			weDoSensorExpr();
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
	public static class WeDoSensorExprContext extends ParserRuleContext {
		public Token slot;
		public TerminalNode GYRO_SENSOR() { return getToken(ExprlyParser.GYRO_SENSOR, 0); }
		public TerminalNode NAME() { return getToken(ExprlyParser.NAME, 0); }
		public TerminalNode UP() { return getToken(ExprlyParser.UP, 0); }
		public TerminalNode DOWN() { return getToken(ExprlyParser.DOWN, 0); }
		public TerminalNode BACK() { return getToken(ExprlyParser.BACK, 0); }
		public TerminalNode FRONT() { return getToken(ExprlyParser.FRONT, 0); }
		public TerminalNode NO() { return getToken(ExprlyParser.NO, 0); }
		public TerminalNode ANY() { return getToken(ExprlyParser.ANY, 0); }
		public TerminalNode INFRARED_SENSOR() { return getToken(ExprlyParser.INFRARED_SENSOR, 0); }
		public TerminalNode KEYS_SENSOR() { return getToken(ExprlyParser.KEYS_SENSOR, 0); }
		public TerminalNode TIMER_SENSOR() { return getToken(ExprlyParser.TIMER_SENSOR, 0); }
		public WeDoSensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_weDoSensorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterWeDoSensorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitWeDoSensorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitWeDoSensorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WeDoSensorExprContext weDoSensorExpr() throws RecognitionException {
		WeDoSensorExprContext _localctx = new WeDoSensorExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_weDoSensorExpr);
		int _la;
		try {
			setState(495);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GYRO_SENSOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(474);
				match(GYRO_SENSOR);
				setState(475);
				match(T__11);
				setState(476);
				match(T__35);
				setState(477);
				match(T__3);
				setState(478);
				match(NAME);
				setState(479);
				match(T__7);
				setState(480);
				((WeDoSensorExprContext)_localctx).slot = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 139)) & ~0x3f) == 0 && ((1L << (_la - 139)) & 63L) != 0)) ) {
					((WeDoSensorExprContext)_localctx).slot = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(481);
				match(T__4);
				}
				break;
			case INFRARED_SENSOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(482);
				match(INFRARED_SENSOR);
				setState(483);
				match(T__3);
				setState(484);
				match(NAME);
				setState(485);
				match(T__4);
				}
				break;
			case KEYS_SENSOR:
				enterOuterAlt(_localctx, 3);
				{
				setState(486);
				match(KEYS_SENSOR);
				setState(487);
				match(T__11);
				setState(488);
				match(T__16);
				setState(489);
				match(T__3);
				setState(490);
				match(NAME);
				setState(491);
				match(T__4);
				}
				break;
			case TIMER_SENSOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(492);
				match(TIMER_SENSOR);
				setState(493);
				match(T__3);
				setState(494);
				match(T__4);
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
	public static class RobotWeDoStmtContext extends ParserRuleContext {
		public RobotWeDoStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotWeDoStmt; }
	 
		public RobotWeDoStmtContext() { }
		public void copyFrom(RobotWeDoStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotWeDoStatementContext extends RobotWeDoStmtContext {
		public WedoSensorStmtContext wedoSensorStmt() {
			return getRuleContext(WedoSensorStmtContext.class,0);
		}
		public RobotWeDoStatementContext(RobotWeDoStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterRobotWeDoStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitRobotWeDoStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitRobotWeDoStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotWeDoStmtContext robotWeDoStmt() throws RecognitionException {
		RobotWeDoStmtContext _localctx = new RobotWeDoStmtContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_robotWeDoStmt);
		try {
			_localctx = new RobotWeDoStatementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			match(T__34);
			setState(498);
			match(T__11);
			setState(499);
			wedoSensorStmt();
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
	public static class WedoSensorStmtContext extends ParserRuleContext {
		public TerminalNode TIMER_RESET() { return getToken(ExprlyParser.TIMER_RESET, 0); }
		public WedoSensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wedoSensorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).enterWedoSensorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExprlyListener ) ((ExprlyListener)listener).exitWedoSensorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExprlyVisitor ) return ((ExprlyVisitor<? extends T>)visitor).visitWedoSensorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WedoSensorStmtContext wedoSensorStmt() throws RecognitionException {
		WedoSensorStmtContext _localctx = new WedoSensorStmtContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_wedoSensorStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(TIMER_RESET);
			setState(502);
			match(T__3);
			setState(503);
			match(T__4);
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
		case 9:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 14);
		case 1:
			return precpred(_ctx, 13);
		case 2:
			return precpred(_ctx, 12);
		case 3:
			return precpred(_ctx, 11);
		case 4:
			return precpred(_ctx, 10);
		case 5:
			return precpred(_ctx, 9);
		case 6:
			return precpred(_ctx, 8);
		case 7:
			return precpred(_ctx, 7);
		case 8:
			return precpred(_ctx, 6);
		case 9:
			return precpred(_ctx, 5);
		case 10:
			return precpred(_ctx, 4);
		case 11:
			return precpred(_ctx, 3);
		case 12:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0090\u01fa\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0001\u0000\u0005\u0000.\b\u0000\n\u0000\f\u00001\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0005\u00005\b\u0000\n\u0000\f\u00008\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003O\b\u0003\n\u0003\f\u0003R\t"+
		"\u0003\u0005\u0003T\b\u0003\n\u0003\f\u0003W\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003_\b"+
		"\u0003\u0003\u0003a\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005k\b"+
		"\u0005\n\u0005\f\u0005n\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0005\u0006u\b\u0006\n\u0006\f\u0006x\t\u0006\u0003"+
		"\u0006z\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0005\u0006\u0082\b\u0006\n\u0006\f\u0006\u0085\t\u0006"+
		"\u0003\u0006\u0087\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u009c\b\u0006\n\u0006"+
		"\f\u0006\u009f\t\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u00a6\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u00be\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0005\u0006\u00e7\b\u0006\n\u0006\f\u0006\u00ea\t\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u00f9\b\u0006\u0001\u0006\u0003\u0006\u00fc\b\u0006\u0001\u0007"+
		"\u0001\u0007\u0003\u0007\u0100\b\u0007\u0001\b\u0001\b\u0001\b\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0115\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u0141\b\t\n\t\f\t\u0144"+
		"\t\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u014c\b\n"+
		"\n\n\f\n\u014f\t\n\u0001\n\u0001\n\u0003\n\u0153\b\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0005\n\u015a\b\n\n\n\f\n\u015d\t\n\u0001\n\u0003\n"+
		"\u0160\b\n\u0001\n\u0003\n\u0163\b\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005"+
		"\f\u016f\b\f\n\f\f\f\u0172\t\f\u0003\f\u0174\b\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0005\f\u017c\b\f\n\f\f\f\u017f\t\f\u0003\f"+
		"\u0181\b\f\u0001\f\u0003\f\u0184\b\f\u0001\r\u0001\r\u0003\r\u0188\b\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u01c6\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u01d5\b\u0011\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0003\u0013\u01f0\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u014d"+
		"\u0001\u0012\u0016\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u001c\u001e \"$&(*\u0000\b\u0001\u0000\u0089\u008a"+
		"\u0001\u0000,-\u0001\u0000\u0087\u0088\u0002\u0000xxzz\u0001\u0000\u0014"+
		"\u0019\u0001\u0000\u001b\u001e\u0001\u0000!\"\u0001\u0000\u008b\u0090"+
		"\u0235\u0000/\u0001\u0000\u0000\u0000\u0002;\u0001\u0000\u0000\u0000\u0004"+
		"@\u0001\u0000\u0000\u0000\u0006H\u0001\u0000\u0000\u0000\bd\u0001\u0000"+
		"\u0000\u0000\nl\u0001\u0000\u0000\u0000\f\u00fb\u0001\u0000\u0000\u0000"+
		"\u000e\u00ff\u0001\u0000\u0000\u0000\u0010\u0101\u0001\u0000\u0000\u0000"+
		"\u0012\u0114\u0001\u0000\u0000\u0000\u0014\u0162\u0001\u0000\u0000\u0000"+
		"\u0016\u0164\u0001\u0000\u0000\u0000\u0018\u0183\u0001\u0000\u0000\u0000"+
		"\u001a\u0187\u0001\u0000\u0000\u0000\u001c\u0189\u0001\u0000\u0000\u0000"+
		"\u001e\u01c5\u0001\u0000\u0000\u0000 \u01c7\u0001\u0000\u0000\u0000\""+
		"\u01d4\u0001\u0000\u0000\u0000$\u01d6\u0001\u0000\u0000\u0000&\u01ef\u0001"+
		"\u0000\u0000\u0000(\u01f1\u0001\u0000\u0000\u0000*\u01f5\u0001\u0000\u0000"+
		"\u0000,.\u0003\u0002\u0001\u0000-,\u0001\u0000\u0000\u0000.1\u0001\u0000"+
		"\u0000\u0000/-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000002\u0001"+
		"\u0000\u0000\u00001/\u0001\u0000\u0000\u000026\u0003\u0004\u0002\u0000"+
		"35\u0003\u0006\u0003\u000043\u0001\u0000\u0000\u000058\u0001\u0000\u0000"+
		"\u000064\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u000079\u0001\u0000"+
		"\u0000\u000086\u0001\u0000\u0000\u00009:\u0005\u0000\u0000\u0001:\u0001"+
		"\u0001\u0000\u0000\u0000;<\u0003\b\u0004\u0000<=\u0005{\u0000\u0000=>"+
		"\u0003\u0012\t\u0000>?\u0005\u0001\u0000\u0000?\u0003\u0001\u0000\u0000"+
		"\u0000@A\u0005\u0002\u0000\u0000AB\u0005\u0003\u0000\u0000BC\u0005\u0004"+
		"\u0000\u0000CD\u0005\u0005\u0000\u0000DE\u0005\u0006\u0000\u0000EF\u0003"+
		"\n\u0005\u0000FG\u0005\u0007\u0000\u0000G\u0005\u0001\u0000\u0000\u0000"+
		"HI\u00052\u0000\u0000IJ\u0005x\u0000\u0000JU\u0005\u0004\u0000\u0000K"+
		"P\u0003\u0012\t\u0000LM\u0005\b\u0000\u0000MO\u0003\u0012\t\u0000NL\u0001"+
		"\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000"+
		"PQ\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000"+
		"\u0000SK\u0001\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000US\u0001\u0000"+
		"\u0000\u0000UV\u0001\u0000\u0000\u0000VX\u0001\u0000\u0000\u0000WU\u0001"+
		"\u0000\u0000\u0000XY\u0005\u0005\u0000\u0000YZ\u0005\u0006\u0000\u0000"+
		"Z`\u0003\n\u0005\u0000[^\u00051\u0000\u0000\\_\u0005x\u0000\u0000]_\u0003"+
		"\u0012\t\u0000^\\\u0001\u0000\u0000\u0000^]\u0001\u0000\u0000\u0000_a"+
		"\u0001\u0000\u0000\u0000`[\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000"+
		"\u0000ab\u0001\u0000\u0000\u0000bc\u0005\u0007\u0000\u0000c\u0007\u0001"+
		"\u0000\u0000\u0000de\u00052\u0000\u0000ef\u0005x\u0000\u0000f\t\u0001"+
		"\u0000\u0000\u0000gh\u0003\f\u0006\u0000hi\u0005\u0001\u0000\u0000ik\u0001"+
		"\u0000\u0000\u0000jg\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000"+
		"lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000m\u000b\u0001\u0000"+
		"\u0000\u0000nl\u0001\u0000\u0000\u0000op\u00056\u0000\u0000py\u0005\u0004"+
		"\u0000\u0000qv\u0003\u0012\t\u0000rs\u0005\b\u0000\u0000su\u0003\u0012"+
		"\t\u0000tr\u0001\u0000\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000"+
		"\u0000\u0000vw\u0001\u0000\u0000\u0000wz\u0001\u0000\u0000\u0000xv\u0001"+
		"\u0000\u0000\u0000yq\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000\u0000"+
		"z{\u0001\u0000\u0000\u0000{\u00fc\u0005\u0005\u0000\u0000|}\u0005x\u0000"+
		"\u0000}\u0086\u0005\u0004\u0000\u0000~\u0083\u0003\u0012\t\u0000\u007f"+
		"\u0080\u0005\b\u0000\u0000\u0080\u0082\u0003\u0012\t\u0000\u0081\u007f"+
		"\u0001\u0000\u0000\u0000\u0082\u0085\u0001\u0000\u0000\u0000\u0083\u0081"+
		"\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0087"+
		"\u0001\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0086~\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0001"+
		"\u0000\u0000\u0000\u0088\u00fc\u0005\u0005\u0000\u0000\u0089\u008a\u0005"+
		"x\u0000\u0000\u008a\u008b\u0005{\u0000\u0000\u008b\u00fc\u0003\u0012\t"+
		"\u0000\u008c\u008d\u0005%\u0000\u0000\u008d\u008e\u0005\u0004\u0000\u0000"+
		"\u008e\u008f\u0003\u0012\t\u0000\u008f\u0090\u0005\u0005\u0000\u0000\u0090"+
		"\u0091\u0005\u0006\u0000\u0000\u0091\u0092\u0003\n\u0005\u0000\u0092\u009d"+
		"\u0005\u0007\u0000\u0000\u0093\u0094\u0005&\u0000\u0000\u0094\u0095\u0005"+
		"\u0004\u0000\u0000\u0095\u0096\u0003\u0012\t\u0000\u0096\u0097\u0005\u0005"+
		"\u0000\u0000\u0097\u0098\u0005\u0006\u0000\u0000\u0098\u0099\u0003\n\u0005"+
		"\u0000\u0099\u009a\u0005\u0007\u0000\u0000\u009a\u009c\u0001\u0000\u0000"+
		"\u0000\u009b\u0093\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000"+
		"\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000"+
		"\u0000\u009e\u00a5\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0005\'\u0000\u0000\u00a1\u00a2\u0005\u0006\u0000\u0000"+
		"\u00a2\u00a3\u0003\n\u0005\u0000\u00a3\u00a4\u0005\u0007\u0000\u0000\u00a4"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a5\u00a0\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a6\u00fc\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a8\u0005*\u0000\u0000\u00a8\u00a9\u0005\u0004\u0000\u0000\u00a9\u00aa"+
		"\u00052\u0000\u0000\u00aa\u00ab\u0005x\u0000\u0000\u00ab\u00ac\u0005{"+
		"\u0000\u0000\u00ac\u00ad\u0003\u0012\t\u0000\u00ad\u00ae\u0005\u0001\u0000"+
		"\u0000\u00ae\u00af\u0005x\u0000\u0000\u00af\u00b0\u0005\u0082\u0000\u0000"+
		"\u00b0\u00b1\u0003\u0012\t\u0000\u00b1\u00bd\u0005\u0001\u0000\u0000\u00b2"+
		"\u00b3\u0003\u0012\t\u0000\u00b3\u00b4\u0005(\u0000\u0000\u00b4\u00be"+
		"\u0001\u0000\u0000\u0000\u00b5\u00b6\u0005x\u0000\u0000\u00b6\u00b7\u0005"+
		"{\u0000\u0000\u00b7\u00b8\u0005x\u0000\u0000\u00b8\u00b9\u0007\u0000\u0000"+
		"\u0000\u00b9\u00be\u0003\u0012\t\u0000\u00ba\u00bb\u0005x\u0000\u0000"+
		"\u00bb\u00bc\u0005{\u0000\u0000\u00bc\u00be\u0003\u0012\t\u0000\u00bd"+
		"\u00b2\u0001\u0000\u0000\u0000\u00bd\u00b5\u0001\u0000\u0000\u0000\u00bd"+
		"\u00ba\u0001\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be"+
		"\u00bf\u0001\u0000\u0000\u0000\u00bf\u00c0\u0005\u0005\u0000\u0000\u00c0"+
		"\u00c1\u0005\u0006\u0000\u0000\u00c1\u00c2\u0003\n\u0005\u0000\u00c2\u00c3"+
		"\u0005\u0007\u0000\u0000\u00c3\u00fc\u0001\u0000\u0000\u0000\u00c4\u00c5"+
		"\u0005)\u0000\u0000\u00c5\u00c6\u0005\u0004\u0000\u0000\u00c6\u00c7\u0003"+
		"\u0012\t\u0000\u00c7\u00c8\u0005\u0005\u0000\u0000\u00c8\u00c9\u0005\u0006"+
		"\u0000\u0000\u00c9\u00ca\u0003\n\u0005\u0000\u00ca\u00cb\u0005\u0007\u0000"+
		"\u0000\u00cb\u00fc\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005+\u0000\u0000"+
		"\u00cd\u00ce\u0005\u0004\u0000\u0000\u00ce\u00cf\u00052\u0000\u0000\u00cf"+
		"\u00d0\u0005x\u0000\u0000\u00d0\u00d1\u0005\t\u0000\u0000\u00d1\u00d2"+
		"\u0003\u0012\t\u0000\u00d2\u00d3\u0005\u0005\u0000\u0000\u00d3\u00d4\u0005"+
		"\u0006\u0000\u0000\u00d4\u00d5\u0003\n\u0005\u0000\u00d5\u00d6\u0005\u0007"+
		"\u0000\u0000\u00d6\u00fc\u0001\u0000\u0000\u0000\u00d7\u00d8\u0005.\u0000"+
		"\u0000\u00d8\u00d9\u0005\u0004\u0000\u0000\u00d9\u00da\u0003\u0012\t\u0000"+
		"\u00da\u00db\u0005\u0005\u0000\u0000\u00db\u00dc\u0005\u0006\u0000\u0000"+
		"\u00dc\u00dd\u0003\n\u0005\u0000\u00dd\u00e8\u0005\u0007\u0000\u0000\u00de"+
		"\u00df\u0005/\u0000\u0000\u00df\u00e0\u0005\u0004\u0000\u0000\u00e0\u00e1"+
		"\u0003\u0012\t\u0000\u00e1\u00e2\u0005\u0005\u0000\u0000\u00e2\u00e3\u0005"+
		"\u0006\u0000\u0000\u00e3\u00e4\u0003\n\u0005\u0000\u00e4\u00e5\u0005\u0007"+
		"\u0000\u0000\u00e5\u00e7\u0001\u0000\u0000\u0000\u00e6\u00de\u0001\u0000"+
		"\u0000\u0000\u00e7\u00ea\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001\u0000"+
		"\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u00fc\u0001\u0000"+
		"\u0000\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00eb\u00fc\u0007\u0001"+
		"\u0000\u0000\u00ec\u00ed\u00050\u0000\u0000\u00ed\u00ee\u0005\u0004\u0000"+
		"\u0000\u00ee\u00ef\u0003\u0012\t\u0000\u00ef\u00f0\u0005\u0005\u0000\u0000"+
		"\u00f0\u00fc\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005%\u0000\u0000\u00f2"+
		"\u00f3\u0005\u0004\u0000\u0000\u00f3\u00f4\u0003\u0012\t\u0000\u00f4\u00f5"+
		"\u0005\u0005\u0000\u0000\u00f5\u00f8\u00051\u0000\u0000\u00f6\u00f9\u0005"+
		"x\u0000\u0000\u00f7\u00f9\u0003\u0012\t\u0000\u00f8\u00f6\u0001\u0000"+
		"\u0000\u0000\u00f8\u00f7\u0001\u0000\u0000\u0000\u00f9\u00fc\u0001\u0000"+
		"\u0000\u0000\u00fa\u00fc\u0003\u000e\u0007\u0000\u00fbo\u0001\u0000\u0000"+
		"\u0000\u00fb|\u0001\u0000\u0000\u0000\u00fb\u0089\u0001\u0000\u0000\u0000"+
		"\u00fb\u008c\u0001\u0000\u0000\u0000\u00fb\u00a7\u0001\u0000\u0000\u0000"+
		"\u00fb\u00c4\u0001\u0000\u0000\u0000\u00fb\u00cc\u0001\u0000\u0000\u0000"+
		"\u00fb\u00d7\u0001\u0000\u0000\u0000\u00fb\u00eb\u0001\u0000\u0000\u0000"+
		"\u00fb\u00ec\u0001\u0000\u0000\u0000\u00fb\u00f1\u0001\u0000\u0000\u0000"+
		"\u00fb\u00fa\u0001\u0000\u0000\u0000\u00fc\r\u0001\u0000\u0000\u0000\u00fd"+
		"\u0100\u0003 \u0010\u0000\u00fe\u0100\u0003(\u0014\u0000\u00ff\u00fd\u0001"+
		"\u0000\u0000\u0000\u00ff\u00fe\u0001\u0000\u0000\u0000\u0100\u000f\u0001"+
		"\u0000\u0000\u0000\u0101\u0102\u0003\u0012\t\u0000\u0102\u0103\u0005\u0000"+
		"\u0000\u0001\u0103\u0011\u0001\u0000\u0000\u0000\u0104\u0105\u0006\t\uffff"+
		"\uffff\u0000\u0105\u0115\u00058\u0000\u0000\u0106\u0115\u00057\u0000\u0000"+
		"\u0107\u0115\u0005x\u0000\u0000\u0108\u0115\u0003\u0014\n\u0000\u0109"+
		"\u0115\u0003\u0016\u000b\u0000\u010a\u0115\u0003\u0018\f\u0000\u010b\u010c"+
		"\u0005\u0004\u0000\u0000\u010c\u010d\u0003\u0012\t\u0000\u010d\u010e\u0005"+
		"\u0005\u0000\u0000\u010e\u0115\u0001\u0000\u0000\u0000\u010f\u0110\u0007"+
		"\u0000\u0000\u0000\u0110\u0115\u0003\u0012\t\u0010\u0111\u0112\u0005~"+
		"\u0000\u0000\u0112\u0115\u0003\u0012\t\u000f\u0113\u0115\u0003\u001a\r"+
		"\u0000\u0114\u0104\u0001\u0000\u0000\u0000\u0114\u0106\u0001\u0000\u0000"+
		"\u0000\u0114\u0107\u0001\u0000\u0000\u0000\u0114\u0108\u0001\u0000\u0000"+
		"\u0000\u0114\u0109\u0001\u0000\u0000\u0000\u0114\u010a\u0001\u0000\u0000"+
		"\u0000\u0114\u010b\u0001\u0000\u0000\u0000\u0114\u010f\u0001\u0000\u0000"+
		"\u0000\u0114\u0111\u0001\u0000\u0000\u0000\u0114\u0113\u0001\u0000\u0000"+
		"\u0000\u0115\u0142\u0001\u0000\u0000\u0000\u0116\u0117\n\u000e\u0000\u0000"+
		"\u0117\u0118\u0005\u0086\u0000\u0000\u0118\u0141\u0003\u0012\t\u000e\u0119"+
		"\u011a\n\r\u0000\u0000\u011a\u011b\u0005\u0085\u0000\u0000\u011b\u0141"+
		"\u0003\u0012\t\r\u011c\u011d\n\f\u0000\u0000\u011d\u011e\u0007\u0002\u0000"+
		"\u0000\u011e\u0141\u0003\u0012\t\r\u011f\u0120\n\u000b\u0000\u0000\u0120"+
		"\u0121\u0007\u0000\u0000\u0000\u0121\u0141\u0003\u0012\t\f\u0122\u0123"+
		"\n\n\u0000\u0000\u0123\u0124\u0005|\u0000\u0000\u0124\u0141\u0003\u0012"+
		"\t\u000b\u0125\u0126\n\t\u0000\u0000\u0126\u0127\u0005}\u0000\u0000\u0127"+
		"\u0141\u0003\u0012\t\n\u0128\u0129\n\b\u0000\u0000\u0129\u012a\u0005\u007f"+
		"\u0000\u0000\u012a\u0141\u0003\u0012\t\t\u012b\u012c\n\u0007\u0000\u0000"+
		"\u012c\u012d\u0005\u0080\u0000\u0000\u012d\u0141\u0003\u0012\t\b\u012e"+
		"\u012f\n\u0006\u0000\u0000\u012f\u0130\u0005\u0081\u0000\u0000\u0130\u0141"+
		"\u0003\u0012\t\u0007\u0131\u0132\n\u0005\u0000\u0000\u0132\u0133\u0005"+
		"\u0082\u0000\u0000\u0133\u0141\u0003\u0012\t\u0006\u0134\u0135\n\u0004"+
		"\u0000\u0000\u0135\u0136\u0005\u0083\u0000\u0000\u0136\u0141\u0003\u0012"+
		"\t\u0005\u0137\u0138\n\u0003\u0000\u0000\u0138\u0139\u0005\u0084\u0000"+
		"\u0000\u0139\u0141\u0003\u0012\t\u0004\u013a\u013b\n\u0002\u0000\u0000"+
		"\u013b\u013c\u0005\n\u0000\u0000\u013c\u013d\u0003\u0012\t\u0000\u013d"+
		"\u013e\u0005\t\u0000\u0000\u013e\u013f\u0003\u0012\t\u0003\u013f\u0141"+
		"\u0001\u0000\u0000\u0000\u0140\u0116\u0001\u0000\u0000\u0000\u0140\u0119"+
		"\u0001\u0000\u0000\u0000\u0140\u011c\u0001\u0000\u0000\u0000\u0140\u011f"+
		"\u0001\u0000\u0000\u0000\u0140\u0122\u0001\u0000\u0000\u0000\u0140\u0125"+
		"\u0001\u0000\u0000\u0000\u0140\u0128\u0001\u0000\u0000\u0000\u0140\u012b"+
		"\u0001\u0000\u0000\u0000\u0140\u012e\u0001\u0000\u0000\u0000\u0140\u0131"+
		"\u0001\u0000\u0000\u0000\u0140\u0134\u0001\u0000\u0000\u0000\u0140\u0137"+
		"\u0001\u0000\u0000\u0000\u0140\u013a\u0001\u0000\u0000\u0000\u0141\u0144"+
		"\u0001\u0000\u0000\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0143"+
		"\u0001\u0000\u0000\u0000\u0143\u0013\u0001\u0000\u0000\u0000\u0144\u0142"+
		"\u0001\u0000\u0000\u0000\u0145\u0163\u0005;\u0000\u0000\u0146\u0163\u0005"+
		"9\u0000\u0000\u0147\u0163\u0005:\u0000\u0000\u0148\u0163\u0005<\u0000"+
		"\u0000\u0149\u0152\u0005\u000b\u0000\u0000\u014a\u014c\t\u0000\u0000\u0000"+
		"\u014b\u014a\u0001\u0000\u0000\u0000\u014c\u014f\u0001\u0000\u0000\u0000"+
		"\u014d\u014e\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000"+
		"\u014e\u0153\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000\u0000"+
		"\u0150\u0153\u0005\f\u0000\u0000\u0151\u0153\u0005\n\u0000\u0000\u0152"+
		"\u014d\u0001\u0000\u0000\u0000\u0152\u0150\u0001\u0000\u0000\u0000\u0152"+
		"\u0151\u0001\u0000\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0154"+
		"\u0163\u0005\u000b\u0000\u0000\u0155\u015b\u0005\r\u0000\u0000\u0156\u0157"+
		"\u0003\u0012\t\u0000\u0157\u0158\u0005\b\u0000\u0000\u0158\u015a\u0001"+
		"\u0000\u0000\u0000\u0159\u0156\u0001\u0000\u0000\u0000\u015a\u015d\u0001"+
		"\u0000\u0000\u0000\u015b\u0159\u0001\u0000\u0000\u0000\u015b\u015c\u0001"+
		"\u0000\u0000\u0000\u015c\u015f\u0001\u0000\u0000\u0000\u015d\u015b\u0001"+
		"\u0000\u0000\u0000\u015e\u0160\u0003\u0012\t\u0000\u015f\u015e\u0001\u0000"+
		"\u0000\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160\u0161\u0001\u0000"+
		"\u0000\u0000\u0161\u0163\u0005\u000e\u0000\u0000\u0162\u0145\u0001\u0000"+
		"\u0000\u0000\u0162\u0146\u0001\u0000\u0000\u0000\u0162\u0147\u0001\u0000"+
		"\u0000\u0000\u0162\u0148\u0001\u0000\u0000\u0000\u0162\u0149\u0001\u0000"+
		"\u0000\u0000\u0162\u0155\u0001\u0000\u0000\u0000\u0163\u0015\u0001\u0000"+
		"\u0000\u0000\u0164\u0165\u0005\u000f\u0000\u0000\u0165\u0166\u0007\u0003"+
		"\u0000\u0000\u0166\u0167\u0005\b\u0000\u0000\u0167\u0168\u0007\u0003\u0000"+
		"\u0000\u0168\u0017\u0001\u0000\u0000\u0000\u0169\u016a\u00055\u0000\u0000"+
		"\u016a\u0173\u0005\u0004\u0000\u0000\u016b\u0170\u0003\u0012\t\u0000\u016c"+
		"\u016d\u0005\b\u0000\u0000\u016d\u016f\u0003\u0012\t\u0000\u016e\u016c"+
		"\u0001\u0000\u0000\u0000\u016f\u0172\u0001\u0000\u0000\u0000\u0170\u016e"+
		"\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u0174"+
		"\u0001\u0000\u0000\u0000\u0172\u0170\u0001\u0000\u0000\u0000\u0173\u016b"+
		"\u0001\u0000\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000\u0174\u0175"+
		"\u0001\u0000\u0000\u0000\u0175\u0184\u0005\u0005\u0000\u0000\u0176\u0177"+
		"\u0005x\u0000\u0000\u0177\u0180\u0005\u0004\u0000\u0000\u0178\u017d\u0003"+
		"\u0012\t\u0000\u0179\u017a\u0005\b\u0000\u0000\u017a\u017c\u0003\u0012"+
		"\t\u0000\u017b\u0179\u0001\u0000\u0000\u0000\u017c\u017f\u0001\u0000\u0000"+
		"\u0000\u017d\u017b\u0001\u0000\u0000\u0000\u017d\u017e\u0001\u0000\u0000"+
		"\u0000\u017e\u0181\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000"+
		"\u0000\u0180\u0178\u0001\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000"+
		"\u0000\u0181\u0182\u0001\u0000\u0000\u0000\u0182\u0184\u0005\u0005\u0000"+
		"\u0000\u0183\u0169\u0001\u0000\u0000\u0000\u0183\u0176\u0001\u0000\u0000"+
		"\u0000\u0184\u0019\u0001\u0000\u0000\u0000\u0185\u0188\u0003\u001c\u000e"+
		"\u0000\u0186\u0188\u0003$\u0012\u0000\u0187\u0185\u0001\u0000\u0000\u0000"+
		"\u0187\u0186\u0001\u0000\u0000\u0000\u0188\u001b\u0001\u0000\u0000\u0000"+
		"\u0189\u018a\u0005\u0010\u0000\u0000\u018a\u018b\u0005\f\u0000\u0000\u018b"+
		"\u018c\u0003\u001e\u000f\u0000\u018c\u001d\u0001\u0000\u0000\u0000\u018d"+
		"\u018e\u0005=\u0000\u0000\u018e\u018f\u0005\u0004\u0000\u0000\u018f\u0190"+
		"\u0005x\u0000\u0000\u0190\u01c6\u0005\u0005\u0000\u0000\u0191\u0192\u0005"+
		"k\u0000\u0000\u0192\u0193\u0005\f\u0000\u0000\u0193\u0194\u0005\u0011"+
		"\u0000\u0000\u0194\u0195\u0005\u0004\u0000\u0000\u0195\u01c6\u0005\u0005"+
		"\u0000\u0000\u0196\u0197\u0005@\u0000\u0000\u0197\u0198\u0005\f\u0000"+
		"\u0000\u0198\u0199\u0005\u0012\u0000\u0000\u0199\u019a\u0005\u0004\u0000"+
		"\u0000\u019a\u01c6\u0005\u0005\u0000\u0000\u019b\u019c\u0005G\u0000\u0000"+
		"\u019c\u019d\u0005\f\u0000\u0000\u019d\u019e\u0005\u0013\u0000\u0000\u019e"+
		"\u019f\u0005\u0004\u0000\u0000\u019f\u01a0\u0007\u0004\u0000\u0000\u01a0"+
		"\u01c6\u0005\u0005\u0000\u0000\u01a1\u01a2\u0005O\u0000\u0000\u01a2\u01a3"+
		"\u0005\f\u0000\u0000\u01a3\u01a4\u0005\u0011\u0000\u0000\u01a4\u01a5\u0005"+
		"\u0004\u0000\u0000\u01a5\u01a6\u0005x\u0000\u0000\u01a6\u01c6\u0005\u0005"+
		"\u0000\u0000\u01a7\u01a8\u0005P\u0000\u0000\u01a8\u01a9\u0005\f\u0000"+
		"\u0000\u01a9\u01aa\u0005\u001a\u0000\u0000\u01aa\u01ab\u0005\u0004\u0000"+
		"\u0000\u01ab\u01c6\u0005\u0005\u0000\u0000\u01ac\u01ad\u0005T\u0000\u0000"+
		"\u01ad\u01ae\u0005\u0004\u0000\u0000\u01ae\u01af\u0005x\u0000\u0000\u01af"+
		"\u01b0\u0005\b\u0000\u0000\u01b0\u01b1\u0007\u0005\u0000\u0000\u01b1\u01c6"+
		"\u0005\u0005\u0000\u0000\u01b2\u01b3\u0005U\u0000\u0000\u01b3\u01b4\u0005"+
		"\f\u0000\u0000\u01b4\u01b5\u0005\u0011\u0000\u0000\u01b5\u01b6\u0005\u0004"+
		"\u0000\u0000\u01b6\u01b7\u00059\u0000\u0000\u01b7\u01c6\u0005\u0005\u0000"+
		"\u0000\u01b8\u01b9\u0005X\u0000\u0000\u01b9\u01ba\u0005\f\u0000\u0000"+
		"\u01ba\u01bb\u0005\u001f\u0000\u0000\u01bb\u01bc\u0005\f\u0000\u0000\u01bc"+
		"\u01bd\u0005 \u0000\u0000\u01bd\u01be\u0005\u0004\u0000\u0000\u01be\u01c6"+
		"\u0005\u0005\u0000\u0000\u01bf\u01c0\u0005Y\u0000\u0000\u01c0\u01c1\u0005"+
		"\u0004\u0000\u0000\u01c1\u01c6\u0005\u0005\u0000\u0000\u01c2\u01c3\u0005"+
		"[\u0000\u0000\u01c3\u01c4\u0005\u0004\u0000\u0000\u01c4\u01c6\u0005\u0005"+
		"\u0000\u0000\u01c5\u018d\u0001\u0000\u0000\u0000\u01c5\u0191\u0001\u0000"+
		"\u0000\u0000\u01c5\u0196\u0001\u0000\u0000\u0000\u01c5\u019b\u0001\u0000"+
		"\u0000\u0000\u01c5\u01a1\u0001\u0000\u0000\u0000\u01c5\u01a7\u0001\u0000"+
		"\u0000\u0000\u01c5\u01ac\u0001\u0000\u0000\u0000\u01c5\u01b2\u0001\u0000"+
		"\u0000\u0000\u01c5\u01b8\u0001\u0000\u0000\u0000\u01c5\u01bf\u0001\u0000"+
		"\u0000\u0000\u01c5\u01c2\u0001\u0000\u0000\u0000\u01c6\u001f\u0001\u0000"+
		"\u0000\u0000\u01c7\u01c8\u0005\u0010\u0000\u0000\u01c8\u01c9\u0005\f\u0000"+
		"\u0000\u01c9\u01ca\u0003\"\u0011\u0000\u01ca!\u0001\u0000\u0000\u0000"+
		"\u01cb\u01cc\u0005q\u0000\u0000\u01cc\u01cd\u0005\u0004\u0000\u0000\u01cd"+
		"\u01ce\u00059\u0000\u0000\u01ce\u01cf\u0005\b\u0000\u0000\u01cf\u01d0"+
		"\u0007\u0006\u0000\u0000\u01d0\u01d5\u0005\u0005\u0000\u0000\u01d1\u01d2"+
		"\u0005Z\u0000\u0000\u01d2\u01d3\u0005\u0004\u0000\u0000\u01d3\u01d5\u0005"+
		"\u0005\u0000\u0000\u01d4\u01cb\u0001\u0000\u0000\u0000\u01d4\u01d1\u0001"+
		"\u0000\u0000\u0000\u01d5#\u0001\u0000\u0000\u0000\u01d6\u01d7\u0005#\u0000"+
		"\u0000\u01d7\u01d8\u0005\f\u0000\u0000\u01d8\u01d9\u0003&\u0013\u0000"+
		"\u01d9%\u0001\u0000\u0000\u0000\u01da\u01db\u0005J\u0000\u0000\u01db\u01dc"+
		"\u0005\f\u0000\u0000\u01dc\u01dd\u0005$\u0000\u0000\u01dd\u01de\u0005"+
		"\u0004\u0000\u0000\u01de\u01df\u0005x\u0000\u0000\u01df\u01e0\u0005\b"+
		"\u0000\u0000\u01e0\u01e1\u0007\u0007\u0000\u0000\u01e1\u01f0\u0005\u0005"+
		"\u0000\u0000\u01e2\u01e3\u0005N\u0000\u0000\u01e3\u01e4\u0005\u0004\u0000"+
		"\u0000\u01e4\u01e5\u0005x\u0000\u0000\u01e5\u01f0\u0005\u0005\u0000\u0000"+
		"\u01e6\u01e7\u0005O\u0000\u0000\u01e7\u01e8\u0005\f\u0000\u0000\u01e8"+
		"\u01e9\u0005\u0011\u0000\u0000\u01e9\u01ea\u0005\u0004\u0000\u0000\u01ea"+
		"\u01eb\u0005x\u0000\u0000\u01eb\u01f0\u0005\u0005\u0000\u0000\u01ec\u01ed"+
		"\u0005[\u0000\u0000\u01ed\u01ee\u0005\u0004\u0000\u0000\u01ee\u01f0\u0005"+
		"\u0005\u0000\u0000\u01ef\u01da\u0001\u0000\u0000\u0000\u01ef\u01e2\u0001"+
		"\u0000\u0000\u0000\u01ef\u01e6\u0001\u0000\u0000\u0000\u01ef\u01ec\u0001"+
		"\u0000\u0000\u0000\u01f0\'\u0001\u0000\u0000\u0000\u01f1\u01f2\u0005#"+
		"\u0000\u0000\u01f2\u01f3\u0005\f\u0000\u0000\u01f3\u01f4\u0003*\u0015"+
		"\u0000\u01f4)\u0001\u0000\u0000\u0000\u01f5\u01f6\u0005Z\u0000\u0000\u01f6"+
		"\u01f7\u0005\u0004\u0000\u0000\u01f7\u01f8\u0005\u0005\u0000\u0000\u01f8"+
		"+\u0001\u0000\u0000\u0000#/6PU^`lvy\u0083\u0086\u009d\u00a5\u00bd\u00e8"+
		"\u00f8\u00fb\u00ff\u0114\u0140\u0142\u014d\u0152\u015b\u015f\u0162\u0170"+
		"\u0173\u017d\u0180\u0183\u0187\u01c5\u01d4\u01ef";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}