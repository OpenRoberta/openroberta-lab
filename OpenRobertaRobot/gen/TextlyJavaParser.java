// Generated from /home/acalderon/openroberta-lab/OpenRobertaRobot/src/main/antlr4/de/fhg/iais/roberta/textly/generated/TextlyJava.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class TextlyJavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, IF=45, ELSEIF=46, 
		ELSE=47, STEP=48, WHILE=49, REPEATFOR=50, REPEATFOREACH=51, BREAK=52, 
		CONTINUE=53, WAIT=54, ORWAITFOR=55, WAITMS=56, RETURN=57, PRIMITIVETYPE=58, 
		NEWLINE=59, WS=60, FNAME=61, FNAMESTMT=62, CONST=63, NULL=64, INT=65, 
		FLOAT=66, COLOR=67, BOOL=68, USER_DEFINED_IMAGE=69, USER_IMAGE_ROW=70, 
		IMAGE_ELEMENT=71, ACCELEROMETER_SENSOR=72, COLOR_SENSOR=73, COMPASS_CALIBRATE=74, 
		COMPASS_SENSOR=75, DETECT_MARK_SENSOR=76, DROP_SENSOR=77, EHT_COLOR_SENSOR=78, 
		ENCODER_RESET=79, ENCODER_SENSOR=80, ENVIRONMENTAL_SENSOR=81, GESTURE_SENSOR=82, 
		GET_LINE_SENSOR=83, GYRO_RESET=84, GYRO_SENSOR=85, HT_COLOR_SENSOR=86, 
		HT_COMPASS_SENSOR=87, HT_INFRARED_SENSOR=88, HUMIDITY_SENSOR=89, IR_SEEKER_SENSOR=90, 
		INFRARED_SENSOR=91, KEYS_SENSOR=92, LIGHT_SENSOR=93, MOISTURE_SENSOR=94, 
		MOTION_SENSOR=95, PARTICLE_SENSOR=96, PIN_GET_VALUE_SENSOR=97, PIN_TOUCH_SENSOR=98, 
		PULSE_SENSOR=99, RFID_SENSOR=100, SOUND_SENSOR=101, TEMPERATURE_SENSOR=102, 
		TIMER_RESET=103, TIMER_SENSOR=104, TOUCH_SENSOR=105, ULTRASONIC_SENSOR=106, 
		VEML_LIGHT_SENSOR=107, VOLTAGE_SENSOR=108, CONNECT_TO_ROBOT=109, CAMERA_SENSOR=110, 
		CAMERA_THRESHOLD=111, CODE_PAD_SENSOR=112, COLOUR_BLOB=113, DETECT_FACE_INFORMATION=114, 
		DETECT_FACE_SENSOR=115, ELECTRIC_CURRENT_SENSOR=116, FSR_SENSOR=117, GPS_SENSOR=118, 
		GYRO_RESET_AXIS=119, JOYSTICK=120, LOGO_TOUCH_SENSOR=121, MARKER_INFORMATION=122, 
		NAO_MARK_INFORMATION=123, ODOMETRY_SENSOR=124, ODOMETRY_SENSOR_RESET=125, 
		OPTICAL_SENSOR=126, PIN_SET_TOUCH_MODE=127, QUAD_RGB_SENSOR=128, RADIO_RSSI_SENSOR=129, 
		RECOGNIZE_WORD=130, RESET_SENSOR=131, SOUND_RECORD=132, TAP_SENSOR=133, 
		HT_CCOMPASSSTARTCALIBRATION=134, GET_LED_BRIGTHNESS=135, SHOWTEXT=136, 
		SHOWIMAGE=137, SHOWANIMATION=138, CLEARDISPLAY=139, SETLED=140, SHOWONSERIALMOTOR=141, 
		PITCH=142, PLAYNOTE=143, PLAY=144, PLAYSOUND=145, SETVOLUME=146, SPEAKER=147, 
		WRITEVALUE=148, SWITCHLED=149, SHOWCHARACTER=150, TURNRGBON=151, TURNRGBOFF=152, 
		MOTORMOVE=153, MOTORSTOP=154, RADIOSEND=155, RADIOSET=156, RECEIVEMESSAGE=157, 
		TURNONMOTOR=158, ROTATEMOTOR=159, SETMOTORSPEED=160, STOPMOTOR=161, DRIVEDISTANCE=162, 
		REGULATEDDRIVE=163, STOPDRIVE=164, ROTATEDIRECTIONANGLE=165, ROTATEDIRECTIONREGULATED=166, 
		DRIVEINCURVE=167, SETLANGUAGE=168, DRAWTEXT=169, DRAWPICTURE=170, PLAYTONE=171, 
		SAYTEXT=172, LEDON=173, LEDOFF=174, RESETLED=175, GETSPEEDMOTOR=176, GETVOLUME=177, 
		SENDMESSAGE=178, WAIT_FOR_CONNECTION=179, NNSTEP=180, SETINPUTNEURON=181, 
		SETWEIGHT=182, SETBIAS=183, GETOUTPUTNEURON=184, GETWEIGHT=185, GETBIAS=186, 
		NAME=187, HEX=188, STR=189, SET=190, AND=191, OR=192, NOT=193, EQUAL=194, 
		NEQUAL=195, GET=196, LET=197, GEQ=198, LEQ=199, MOD=200, POW=201, MUL=202, 
		DIV=203, ADD=204, SUB=205;
	public static final int
		RULE_program = 0, RULE_declaration = 1, RULE_mainBlock = 2, RULE_userFunc = 3, 
		RULE_nameDecl = 4, RULE_statementList = 5, RULE_stmt = 6, RULE_robotStmt = 7, 
		RULE_expression = 8, RULE_expr = 9, RULE_imageExpr = 10, RULE_literal = 11, 
		RULE_connExpr = 12, RULE_funCall = 13, RULE_robotExpr = 14, RULE_robotMicrobitv2Expr = 15, 
		RULE_microbitv2SensorExpr = 16, RULE_robotMicrobitv2Stmt = 17, RULE_microbitv2SensorStmt = 18, 
		RULE_microbitv2ActuatorStmt = 19, RULE_robotWeDoExpr = 20, RULE_weDoSensorExpr = 21, 
		RULE_robotWeDoStmt = 22, RULE_wedoSensorStmt = 23, RULE_wedoActuatorStmt = 24, 
		RULE_robotEv3Expr = 25, RULE_ev3SensorExpr = 26, RULE_robotEv3Stmt = 27, 
		RULE_ev3SensorStmt = 28, RULE_ev3ActuatorStmt = 29, RULE_ev3xNN = 30;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "declaration", "mainBlock", "userFunc", "nameDecl", "statementList", 
			"stmt", "robotStmt", "expression", "expr", "imageExpr", "literal", "connExpr", 
			"funCall", "robotExpr", "robotMicrobitv2Expr", "microbitv2SensorExpr", 
			"robotMicrobitv2Stmt", "microbitv2SensorStmt", "microbitv2ActuatorStmt", 
			"robotWeDoExpr", "weDoSensorExpr", "robotWeDoStmt", "wedoSensorStmt", 
			"wedoActuatorStmt", "robotEv3Expr", "ev3SensorExpr", "robotEv3Stmt", 
			"ev3SensorStmt", "ev3ActuatorStmt", "ev3xNN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'void'", "'main'", "'('", "')'", "'{'", "'}'", "','", "':'", 
			"'?'", "'image'", "'.'", "'define'", "'shift'", "'invert'", "'\"'", "'['", 
			"']'", "'connect'", "'microbitv2'", "'isPressed'", "'getAngle'", "'currentGesture'", 
			"'getLevel'", "'analog'", "'digital'", "'pulseHigh'", "'pulseLow'", "'microphone'", 
			"'soundLevel'", "'capacitive'", "'resistive'", "'wedo'", "'isTilted'", 
			"'ev3'", "'getDistance'", "'getPresence'", "'getDegree'", "'getRotation'", 
			"'getRate'", "'getSoundLevel'", "'getModulated'", "'getUnmodulated'", 
			"'getCompass'", "'if'", null, "'else'", "'++'", "'while'", "'for'", null, 
			"'break'", "'continue'", "'waitUntil'", "'orWaitFor'", "'wait ms'", "'return'", 
			null, null, null, null, null, null, "'null'", null, null, null, null, 
			null, null, null, "'accelerometerSensor'", "'colorSensor'", "'compassCalibrate'", 
			"'compassSensor'", "'detectMarkSensor'", "'dropSensor'", "'ehtColorSensor'", 
			"'encoderReset'", "'encoderSensor'", "'environmentalSensor'", "'gestureSensor'", 
			"'getLineSensor'", "'gyroReset'", "'gyroSensor'", "'hiTechColorSensor'", 
			"'hiTechCompassSensor'", "'hiTechInfraredSensor'", "'humiditySensor'", 
			"'irSeekerSensor'", "'infraredSensor'", "'keysSensor'", "'lightSensor'", 
			"'moistureSensor'", "'motionSensor'", "'particleSensor'", "'pinGetValueSensor'", 
			"'pinTouchSensor'", "'pulseSensor'", "'rfidSensor'", "'soundSensor'", 
			"'temperatureSensor'", "'timerReset'", "'timerSensor'", "'touchSensor'", 
			"'ultrasonicSensor'", "'vemlLightSensor'", "'voltageSensor'", "'connectToRobot'", 
			"'cameraSensor'", "'cameraThreshold'", "'codePadSensor'", "'colourBlob'", 
			"'detectFaceInformation'", "'detectFaceSensor'", "'electricCurrentSensor'", 
			"'fsrSensor'", "'gpsSensor'", "'gyroResetAxis'", "'joystick'", "'logoTouchSensor'", 
			"'markerInformation'", "'naoMarkInformation'", "'odometrySensor'", "'odometrySensorReset'", 
			"'opticalSensor'", "'pinSetTouchMode'", "'quadRGBSensor'", "'radioRssiSensor'", 
			"'recognizeWord'", "'resetSensor'", "'soundRecord'", "'tapSensor'", "'hiTecCompassStartCalibration'", 
			"'getLedBrigthness'", "'showText'", "'showImage'", "'showAnimation'", 
			"'clearDisplay'", "'setLed'", "'showOnSerial'", "'pitch'", "'playNote'", 
			"'playFile'", "'playSound'", "'setVolume'", "'speaker'", "'writeValuePin'", 
			"'switchLed'", "'showCharacter'", "'turnRgbOn'", "'turnRgbOff'", "'motor.move'", 
			"'motor.stop'", "'radioSend'", "'radioSet'", "'receiveMessage'", "'turnOnRegulatedMotor'", 
			"'rotateRegulatedMotor'", "'setRegulatedMotorSpeed'", "'stopRegulatedMotor'", 
			"'driveDistance'", "'regulatedDrive'", "'stopRegulatedDrive'", "'rotateDirectionAngle'", 
			"'rotateDirectionRegulated'", "'driveInCurve'", "'setLanguage'", "'drawText'", 
			"'drawPicture'", "'playTone'", "'sayText'", "'ledOn'", "'ledOff'", "'resetLed'", 
			"'getSpeedMotor'", "'getVolume'", "'sendMessage'", "'waitForConnection'", 
			"'nnStep'", "'setInputNeuron'", "'setWeight'", "'setBias'", "'getOutputNeuron'", 
			"'getWeight'", "'getBias'", null, null, null, "'='", "'&&'", "'||'", 
			"'!'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "'%'", "'^'", "'*'", 
			"'/'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "IF", "ELSEIF", 
			"ELSE", "STEP", "WHILE", "REPEATFOR", "REPEATFOREACH", "BREAK", "CONTINUE", 
			"WAIT", "ORWAITFOR", "WAITMS", "RETURN", "PRIMITIVETYPE", "NEWLINE", 
			"WS", "FNAME", "FNAMESTMT", "CONST", "NULL", "INT", "FLOAT", "COLOR", 
			"BOOL", "USER_DEFINED_IMAGE", "USER_IMAGE_ROW", "IMAGE_ELEMENT", "ACCELEROMETER_SENSOR", 
			"COLOR_SENSOR", "COMPASS_CALIBRATE", "COMPASS_SENSOR", "DETECT_MARK_SENSOR", 
			"DROP_SENSOR", "EHT_COLOR_SENSOR", "ENCODER_RESET", "ENCODER_SENSOR", 
			"ENVIRONMENTAL_SENSOR", "GESTURE_SENSOR", "GET_LINE_SENSOR", "GYRO_RESET", 
			"GYRO_SENSOR", "HT_COLOR_SENSOR", "HT_COMPASS_SENSOR", "HT_INFRARED_SENSOR", 
			"HUMIDITY_SENSOR", "IR_SEEKER_SENSOR", "INFRARED_SENSOR", "KEYS_SENSOR", 
			"LIGHT_SENSOR", "MOISTURE_SENSOR", "MOTION_SENSOR", "PARTICLE_SENSOR", 
			"PIN_GET_VALUE_SENSOR", "PIN_TOUCH_SENSOR", "PULSE_SENSOR", "RFID_SENSOR", 
			"SOUND_SENSOR", "TEMPERATURE_SENSOR", "TIMER_RESET", "TIMER_SENSOR", 
			"TOUCH_SENSOR", "ULTRASONIC_SENSOR", "VEML_LIGHT_SENSOR", "VOLTAGE_SENSOR", 
			"CONNECT_TO_ROBOT", "CAMERA_SENSOR", "CAMERA_THRESHOLD", "CODE_PAD_SENSOR", 
			"COLOUR_BLOB", "DETECT_FACE_INFORMATION", "DETECT_FACE_SENSOR", "ELECTRIC_CURRENT_SENSOR", 
			"FSR_SENSOR", "GPS_SENSOR", "GYRO_RESET_AXIS", "JOYSTICK", "LOGO_TOUCH_SENSOR", 
			"MARKER_INFORMATION", "NAO_MARK_INFORMATION", "ODOMETRY_SENSOR", "ODOMETRY_SENSOR_RESET", 
			"OPTICAL_SENSOR", "PIN_SET_TOUCH_MODE", "QUAD_RGB_SENSOR", "RADIO_RSSI_SENSOR", 
			"RECOGNIZE_WORD", "RESET_SENSOR", "SOUND_RECORD", "TAP_SENSOR", "HT_CCOMPASSSTARTCALIBRATION", 
			"GET_LED_BRIGTHNESS", "SHOWTEXT", "SHOWIMAGE", "SHOWANIMATION", "CLEARDISPLAY", 
			"SETLED", "SHOWONSERIALMOTOR", "PITCH", "PLAYNOTE", "PLAY", "PLAYSOUND", 
			"SETVOLUME", "SPEAKER", "WRITEVALUE", "SWITCHLED", "SHOWCHARACTER", "TURNRGBON", 
			"TURNRGBOFF", "MOTORMOVE", "MOTORSTOP", "RADIOSEND", "RADIOSET", "RECEIVEMESSAGE", 
			"TURNONMOTOR", "ROTATEMOTOR", "SETMOTORSPEED", "STOPMOTOR", "DRIVEDISTANCE", 
			"REGULATEDDRIVE", "STOPDRIVE", "ROTATEDIRECTIONANGLE", "ROTATEDIRECTIONREGULATED", 
			"DRIVEINCURVE", "SETLANGUAGE", "DRAWTEXT", "DRAWPICTURE", "PLAYTONE", 
			"SAYTEXT", "LEDON", "LEDOFF", "RESETLED", "GETSPEEDMOTOR", "GETVOLUME", 
			"SENDMESSAGE", "WAIT_FOR_CONNECTION", "NNSTEP", "SETINPUTNEURON", "SETWEIGHT", 
			"SETBIAS", "GETOUTPUTNEURON", "GETWEIGHT", "GETBIAS", "NAME", "HEX", 
			"STR", "SET", "AND", "OR", "NOT", "EQUAL", "NEQUAL", "GET", "LET", "GEQ", 
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
	public String getGrammarFileName() { return "TextlyJava.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TextlyJavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public MainBlockContext mainBlock() {
			return getRuleContext(MainBlockContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TextlyJavaParser.EOF, 0); }
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitProgram(this);
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
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(62);
				declaration();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			mainBlock();
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PRIMITIVETYPE) {
				{
				{
				setState(69);
				userFunc();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75);
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
		public TerminalNode SET() { return getToken(TextlyJavaParser.SET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VariableDeclarationContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitVariableDeclaration(this);
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
			setState(77);
			nameDecl();
			setState(78);
			match(SET);
			setState(79);
			expr(0);
			setState(80);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterMainFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitMainFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitMainFunc(this);
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
			setState(82);
			match(T__1);
			setState(83);
			match(T__2);
			setState(84);
			match(T__3);
			setState(85);
			match(T__4);
			setState(86);
			match(T__5);
			setState(87);
			statementList();
			setState(88);
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
		public TerminalNode PRIMITIVETYPE() { return getToken(TextlyJavaParser.PRIMITIVETYPE, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
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
		public TerminalNode RETURN() { return getToken(TextlyJavaParser.RETURN, 0); }
		public FuncUserContext(UserFuncContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterFuncUser(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitFuncUser(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitFuncUser(this);
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
			setState(90);
			match(PRIMITIVETYPE);
			setState(91);
			match(NAME);
			setState(92);
			match(T__3);
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
				{
				{
				setState(93);
				expr(0);
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(94);
					match(T__7);
					setState(95);
					expr(0);
					}
					}
					setState(100);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(106);
			match(T__4);
			setState(107);
			match(T__5);
			setState(108);
			statementList();
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RETURN) {
				{
				setState(109);
				((FuncUserContext)_localctx).op = match(RETURN);
				setState(112);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(110);
					match(NAME);
					}
					break;
				case 2:
					{
					setState(111);
					expr(0);
					}
					break;
				}
				}
			}

			setState(116);
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
		public TerminalNode PRIMITIVETYPE() { return getToken(TextlyJavaParser.PRIMITIVETYPE, 0); }
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public ParamsMethodContext(NameDeclContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterParamsMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitParamsMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitParamsMethod(this);
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
			setState(118);
			match(PRIMITIVETYPE);
			setState(119);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterStatementList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitStatementList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitStatementList(this);
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
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4719244686853668864L) != 0) || _la==NAME) {
				{
				{
				setState(121);
				stmt();
				setState(122);
				match(T__0);
				}
				}
				setState(128);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StmtUsedDefCallContext extends StmtContext {
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StmtUsedDefCallContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterStmtUsedDefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitStmtUsedDefCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitStmtUsedDefCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FlowControlContext extends StmtContext {
		public Token op;
		public TerminalNode BREAK() { return getToken(TextlyJavaParser.BREAK, 0); }
		public TerminalNode CONTINUE() { return getToken(TextlyJavaParser.CONTINUE, 0); }
		public FlowControlContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterFlowControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitFlowControl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitFlowControl(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StmtFuncContext extends StmtContext {
		public TerminalNode FNAMESTMT() { return getToken(TextlyJavaParser.FNAMESTMT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StmtFuncContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterStmtFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitStmtFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitStmtFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WaitTimeStatementContext extends StmtContext {
		public TerminalNode WAITMS() { return getToken(TextlyJavaParser.WAITMS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public WaitTimeStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterWaitTimeStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitWaitTimeStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitWaitTimeStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryVarAssignContext extends StmtContext {
		public Token op;
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SET() { return getToken(TextlyJavaParser.SET, 0); }
		public BinaryVarAssignContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterBinaryVarAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitBinaryVarAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitBinaryVarAssign(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatForContext extends StmtContext {
		public Token op;
		public TerminalNode REPEATFOR() { return getToken(TextlyJavaParser.REPEATFOR, 0); }
		public NameDeclContext nameDecl() {
			return getRuleContext(NameDeclContext.class,0);
		}
		public List<TerminalNode> SET() { return getTokens(TextlyJavaParser.SET); }
		public TerminalNode SET(int i) {
			return getToken(TextlyJavaParser.SET, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public TerminalNode LET() { return getToken(TextlyJavaParser.LET, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public TerminalNode STEP() { return getToken(TextlyJavaParser.STEP, 0); }
		public TerminalNode ADD() { return getToken(TextlyJavaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(TextlyJavaParser.SUB, 0); }
		public RepeatForContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRepeatFor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRepeatFor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRepeatFor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatStatementContext extends StmtContext {
		public TerminalNode WHILE() { return getToken(TextlyJavaParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public RepeatStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRepeatStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRepeatStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRepeatStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConditionStatementBlockContext extends StmtContext {
		public Token op;
		public TerminalNode IF() { return getToken(TextlyJavaParser.IF, 0); }
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
		public List<TerminalNode> ELSEIF() { return getTokens(TextlyJavaParser.ELSEIF); }
		public TerminalNode ELSEIF(int i) {
			return getToken(TextlyJavaParser.ELSEIF, i);
		}
		public TerminalNode ELSE() { return getToken(TextlyJavaParser.ELSE, 0); }
		public ConditionStatementBlockContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterConditionStatementBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitConditionStatementBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitConditionStatementBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RepeatForEachContext extends StmtContext {
		public TerminalNode REPEATFOREACH() { return getToken(TextlyJavaParser.REPEATFOREACH, 0); }
		public NameDeclContext nameDecl() {
			return getRuleContext(NameDeclContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public RepeatForEachContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRepeatForEach(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRepeatForEach(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRepeatForEach(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WaitStatementContext extends StmtContext {
		public Token op;
		public TerminalNode WAIT() { return getToken(TextlyJavaParser.WAIT, 0); }
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
		public List<TerminalNode> ORWAITFOR() { return getTokens(TextlyJavaParser.ORWAITFOR); }
		public TerminalNode ORWAITFOR(int i) {
			return getToken(TextlyJavaParser.ORWAITFOR, i);
		}
		public WaitStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterWaitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitWaitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitWaitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserFuncIfStmtContext extends StmtContext {
		public TerminalNode IF() { return getToken(TextlyJavaParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RETURN() { return getToken(TextlyJavaParser.RETURN, 0); }
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public UserFuncIfStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterUserFuncIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitUserFuncIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitUserFuncIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_stmt);
		int _la;
		try {
			setState(272);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				_localctx = new StmtFuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				match(FNAMESTMT);
				setState(130);
				match(T__3);
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
					{
					setState(131);
					expr(0);
					setState(136);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(132);
						match(T__7);
						setState(133);
						expr(0);
						}
						}
						setState(138);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(141);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new StmtUsedDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				match(NAME);
				setState(143);
				match(T__3);
				setState(157);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(147);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
						{
						{
						setState(144);
						expr(0);
						}
						}
						setState(149);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(154);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(150);
						match(T__7);
						setState(151);
						expr(0);
						}
						}
						setState(156);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					break;
				}
				setState(159);
				match(T__4);
				}
				break;
			case 3:
				_localctx = new BinaryVarAssignContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(160);
				match(NAME);
				setState(161);
				((BinaryVarAssignContext)_localctx).op = match(SET);
				setState(162);
				expr(0);
				}
				break;
			case 4:
				_localctx = new ConditionStatementBlockContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(163);
				match(IF);
				setState(164);
				match(T__3);
				setState(165);
				expr(0);
				setState(166);
				match(T__4);
				setState(167);
				match(T__5);
				setState(168);
				statementList();
				setState(169);
				match(T__6);
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELSEIF) {
					{
					{
					setState(170);
					match(ELSEIF);
					setState(171);
					match(T__3);
					setState(172);
					expr(0);
					setState(173);
					match(T__4);
					setState(174);
					match(T__5);
					setState(175);
					statementList();
					setState(176);
					match(T__6);
					}
					}
					setState(182);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(183);
					((ConditionStatementBlockContext)_localctx).op = match(ELSE);
					setState(184);
					match(T__5);
					setState(185);
					statementList();
					setState(186);
					match(T__6);
					}
				}

				}
				break;
			case 5:
				_localctx = new RepeatForContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(190);
				match(REPEATFOR);
				setState(191);
				match(T__3);
				setState(192);
				nameDecl();
				setState(193);
				match(SET);
				setState(194);
				expr(0);
				setState(195);
				match(T__0);
				setState(196);
				match(NAME);
				setState(197);
				match(LET);
				setState(198);
				expr(0);
				setState(199);
				match(T__0);
				setState(211);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
				case 1:
					{
					setState(200);
					expr(0);
					setState(201);
					((RepeatForContext)_localctx).op = match(STEP);
					}
					break;
				case 2:
					{
					setState(203);
					match(NAME);
					setState(204);
					match(SET);
					setState(205);
					match(NAME);
					setState(206);
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
					setState(207);
					expr(0);
					}
					break;
				case 3:
					{
					setState(208);
					match(NAME);
					setState(209);
					match(SET);
					setState(210);
					expr(0);
					}
					break;
				}
				setState(213);
				match(T__4);
				setState(214);
				match(T__5);
				setState(215);
				statementList();
				setState(216);
				match(T__6);
				}
				break;
			case 6:
				_localctx = new RepeatStatementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(218);
				match(WHILE);
				setState(219);
				match(T__3);
				setState(220);
				expr(0);
				setState(221);
				match(T__4);
				setState(222);
				match(T__5);
				setState(223);
				statementList();
				setState(224);
				match(T__6);
				}
				break;
			case 7:
				_localctx = new RepeatForEachContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(226);
				match(REPEATFOREACH);
				setState(227);
				match(T__3);
				setState(228);
				nameDecl();
				setState(229);
				match(T__8);
				setState(230);
				expr(0);
				setState(231);
				match(T__4);
				setState(232);
				match(T__5);
				setState(233);
				statementList();
				setState(234);
				match(T__6);
				}
				break;
			case 8:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(236);
				match(WAIT);
				setState(237);
				match(T__3);
				setState(238);
				expr(0);
				setState(239);
				match(T__4);
				setState(240);
				match(T__5);
				setState(241);
				statementList();
				setState(242);
				match(T__6);
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ORWAITFOR) {
					{
					{
					setState(243);
					((WaitStatementContext)_localctx).op = match(ORWAITFOR);
					setState(244);
					match(T__3);
					setState(245);
					expr(0);
					setState(246);
					match(T__4);
					setState(247);
					match(T__5);
					setState(248);
					statementList();
					setState(249);
					match(T__6);
					}
					}
					setState(255);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 9:
				_localctx = new FlowControlContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(256);
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
				setState(257);
				match(WAITMS);
				setState(258);
				match(T__3);
				setState(259);
				expr(0);
				setState(260);
				match(T__4);
				}
				break;
			case 11:
				_localctx = new UserFuncIfStmtContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(262);
				match(IF);
				setState(263);
				match(T__3);
				setState(264);
				expr(0);
				setState(265);
				match(T__4);
				setState(266);
				match(RETURN);
				setState(269);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(267);
					match(NAME);
					}
					break;
				case 2:
					{
					setState(268);
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
				setState(271);
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
		public RobotEv3StmtContext robotEv3Stmt() {
			return getRuleContext(RobotEv3StmtContext.class,0);
		}
		public RobotStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotStmtContext robotStmt() throws RecognitionException {
		RobotStmtContext _localctx = new RobotStmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_robotStmt);
		try {
			setState(277);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__19:
				enterOuterAlt(_localctx, 1);
				{
				setState(274);
				robotMicrobitv2Stmt();
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 2);
				{
				setState(275);
				robotWeDoStmt();
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 3);
				{
				setState(276);
				robotEv3Stmt();
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
		public TerminalNode EOF() { return getToken(TextlyJavaParser.EOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			expr(0);
			setState(280);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterLiteralExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitLiteralExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitLiteralExp(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterIfElseOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitIfElseOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitIfElseOp(this);
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
		public TerminalNode AND() { return getToken(TextlyJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(TextlyJavaParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(TextlyJavaParser.EQUAL, 0); }
		public TerminalNode NEQUAL() { return getToken(TextlyJavaParser.NEQUAL, 0); }
		public TerminalNode GET() { return getToken(TextlyJavaParser.GET, 0); }
		public TerminalNode LET() { return getToken(TextlyJavaParser.LET, 0); }
		public TerminalNode GEQ() { return getToken(TextlyJavaParser.GEQ, 0); }
		public TerminalNode LEQ() { return getToken(TextlyJavaParser.LEQ, 0); }
		public BinaryBContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterBinaryB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitBinaryB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitBinaryB(this);
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
		public TerminalNode POW() { return getToken(TextlyJavaParser.POW, 0); }
		public TerminalNode MOD() { return getToken(TextlyJavaParser.MOD, 0); }
		public TerminalNode MUL() { return getToken(TextlyJavaParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(TextlyJavaParser.DIV, 0); }
		public TerminalNode ADD() { return getToken(TextlyJavaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(TextlyJavaParser.SUB, 0); }
		public BinaryNContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterBinaryN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitBinaryN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitBinaryN(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MathConstContext extends ExprContext {
		public TerminalNode CONST() { return getToken(TextlyJavaParser.CONST, 0); }
		public MathConstContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterMathConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitMathConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitMathConst(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterFuncExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitFuncExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitFuncExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullConstContext extends ExprContext {
		public TerminalNode NULL() { return getToken(TextlyJavaParser.NULL, 0); }
		public NullConstContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterNullConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitNullConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitNullConst(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryBContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NOT() { return getToken(TextlyJavaParser.NOT, 0); }
		public UnaryBContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterUnaryB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitUnaryB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitUnaryB(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImageExpressionContext extends ExprContext {
		public ImageExprContext imageExpr() {
			return getRuleContext(ImageExprContext.class,0);
		}
		public ImageExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterImageExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitImageExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitImageExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryNContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD() { return getToken(TextlyJavaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(TextlyJavaParser.SUB, 0); }
		public UnaryNContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterUnaryN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitUnaryN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitUnaryN(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarNameContext extends ExprContext {
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public VarNameContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterVarName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitVarName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitVarName(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterParenthese(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitParenthese(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitParenthese(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterConnExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitConnExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitConnExp(this);
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
			setState(299);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				_localctx = new NullConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(283);
				match(NULL);
				}
				break;
			case 2:
				{
				_localctx = new MathConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(284);
				match(CONST);
				}
				break;
			case 3:
				{
				_localctx = new VarNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(285);
				match(NAME);
				}
				break;
			case 4:
				{
				_localctx = new LiteralExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(286);
				literal();
				}
				break;
			case 5:
				{
				_localctx = new ConnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(287);
				connExpr();
				}
				break;
			case 6:
				{
				_localctx = new FuncExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(288);
				funCall();
				}
				break;
			case 7:
				{
				_localctx = new ParentheseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(289);
				match(T__3);
				setState(290);
				expr(0);
				setState(291);
				match(T__4);
				}
				break;
			case 8:
				{
				_localctx = new UnaryNContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(293);
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
				setState(294);
				expr(17);
				}
				break;
			case 9:
				{
				_localctx = new UnaryBContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(295);
				((UnaryBContext)_localctx).op = match(NOT);
				setState(296);
				expr(16);
				}
				break;
			case 10:
				{
				_localctx = new ImageExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(297);
				imageExpr();
				}
				break;
			case 11:
				{
				_localctx = new RobotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(298);
				robotExpr();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(345);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(343);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(301);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(302);
						((BinaryNContext)_localctx).op = match(POW);
						setState(303);
						expr(15);
						}
						break;
					case 2:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(304);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(305);
						((BinaryNContext)_localctx).op = match(MOD);
						setState(306);
						expr(14);
						}
						break;
					case 3:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(307);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(308);
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
						setState(309);
						expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryNContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(310);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(311);
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
						setState(312);
						expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(313);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(314);
						((BinaryBContext)_localctx).op = match(AND);
						setState(315);
						expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(316);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(317);
						((BinaryBContext)_localctx).op = match(OR);
						setState(318);
						expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(319);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(320);
						((BinaryBContext)_localctx).op = match(EQUAL);
						setState(321);
						expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(322);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(323);
						((BinaryBContext)_localctx).op = match(NEQUAL);
						setState(324);
						expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(325);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(326);
						((BinaryBContext)_localctx).op = match(GET);
						setState(327);
						expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(328);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(329);
						((BinaryBContext)_localctx).op = match(LET);
						setState(330);
						expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(331);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(332);
						((BinaryBContext)_localctx).op = match(GEQ);
						setState(333);
						expr(6);
						}
						break;
					case 12:
						{
						_localctx = new BinaryBContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(334);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(335);
						((BinaryBContext)_localctx).op = match(LEQ);
						setState(336);
						expr(5);
						}
						break;
					case 13:
						{
						_localctx = new IfElseOpContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(337);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(338);
						match(T__9);
						setState(339);
						expr(0);
						setState(340);
						match(T__8);
						setState(341);
						expr(4);
						}
						break;
					}
					} 
				}
				setState(347);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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
	public static class ImageExprContext extends ParserRuleContext {
		public ImageExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imageExpr; }
	 
		public ImageExprContext() { }
		public void copyFrom(ImageExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImageShiftContext extends ImageExprContext {
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ImageShiftContext(ImageExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterImageShift(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitImageShift(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitImageShift(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserDefinedImageContext extends ImageExprContext {
		public TerminalNode USER_DEFINED_IMAGE() { return getToken(TextlyJavaParser.USER_DEFINED_IMAGE, 0); }
		public UserDefinedImageContext(ImageExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterUserDefinedImage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitUserDefinedImage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitUserDefinedImage(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PredefinedImageContext extends ImageExprContext {
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public PredefinedImageContext(ImageExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterPredefinedImage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitPredefinedImage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitPredefinedImage(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ImageInvertContext extends ImageExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ImageInvertContext(ImageExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterImageInvert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitImageInvert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitImageInvert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImageExprContext imageExpr() throws RecognitionException {
		ImageExprContext _localctx = new ImageExprContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_imageExpr);
		try {
			setState(373);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				_localctx = new PredefinedImageContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(348);
				match(T__10);
				setState(349);
				match(T__3);
				setState(350);
				match(NAME);
				setState(351);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new UserDefinedImageContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(352);
				match(T__10);
				setState(353);
				match(T__11);
				setState(354);
				match(T__12);
				setState(355);
				match(T__3);
				setState(356);
				match(USER_DEFINED_IMAGE);
				setState(357);
				match(T__4);
				}
				break;
			case 3:
				_localctx = new ImageShiftContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(358);
				match(T__10);
				setState(359);
				match(T__11);
				setState(360);
				match(T__13);
				setState(361);
				match(T__3);
				setState(362);
				match(NAME);
				setState(363);
				match(T__7);
				setState(364);
				expr(0);
				setState(365);
				match(T__7);
				setState(366);
				expr(0);
				setState(367);
				match(T__4);
				}
				break;
			case 4:
				_localctx = new ImageInvertContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(369);
				match(T__10);
				setState(370);
				match(T__11);
				setState(371);
				match(T__14);
				{
				setState(372);
				expr(0);
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
		public TerminalNode COLOR() { return getToken(TextlyJavaParser.COLOR, 0); }
		public ColContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterCol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitCol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitCol(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolConstBContext extends LiteralContext {
		public TerminalNode BOOL() { return getToken(TextlyJavaParser.BOOL, 0); }
		public BoolConstBContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterBoolConstB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitBoolConstB(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitBoolConstB(this);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterListExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitListExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitListExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConstStrContext extends LiteralContext {
		public ConstStrContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterConstStr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitConstStr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitConstStr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntConstContext extends LiteralContext {
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public IntConstContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterIntConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitIntConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitIntConst(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FloatConstContext extends LiteralContext {
		public TerminalNode FLOAT() { return getToken(TextlyJavaParser.FLOAT, 0); }
		public FloatConstContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterFloatConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitFloatConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitFloatConst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_literal);
		int _la;
		try {
			int _alt;
			setState(404);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLOR:
				_localctx = new ColContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(375);
				match(COLOR);
				}
				break;
			case INT:
				_localctx = new IntConstContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(376);
				match(INT);
				}
				break;
			case FLOAT:
				_localctx = new FloatConstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(377);
				match(FLOAT);
				}
				break;
			case BOOL:
				_localctx = new BoolConstBContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(378);
				match(BOOL);
				}
				break;
			case T__15:
				_localctx = new ConstStrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(379);
				match(T__15);
				setState(388);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(383);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
					while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1+1 ) {
							{
							{
							setState(380);
							matchWildcard();
							}
							} 
						}
						setState(385);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
					}
					}
					break;
				case 2:
					{
					setState(386);
					match(T__11);
					}
					break;
				case 3:
					{
					setState(387);
					match(T__9);
					}
					break;
				}
				setState(390);
				match(T__15);
				}
				break;
			case T__16:
				_localctx = new ListExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(391);
				match(T__16);
				setState(397);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(392);
						expr(0);
						setState(393);
						match(T__7);
						}
						} 
					}
					setState(399);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
					{
					setState(400);
					expr(0);
					}
				}

				setState(403);
				match(T__17);
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
		public List<TerminalNode> STR() { return getTokens(TextlyJavaParser.STR); }
		public TerminalNode STR(int i) {
			return getToken(TextlyJavaParser.STR, i);
		}
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public ConnContext(ConnExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterConn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitConn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitConn(this);
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
			setState(406);
			match(T__18);
			setState(407);
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
			setState(408);
			match(T__7);
			setState(409);
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
		public TerminalNode FNAME() { return getToken(TextlyJavaParser.FNAME, 0); }
		public TerminalNode PRIMITIVETYPE() { return getToken(TextlyJavaParser.PRIMITIVETYPE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public FuncContext(FunCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitFunc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitFunc(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UserDefCallContext extends FunCallContext {
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public UserDefCallContext(FunCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterUserDefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitUserDefCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitUserDefCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunCallContext funCall() throws RecognitionException {
		FunCallContext _localctx = new FunCallContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_funCall);
		int _la;
		try {
			setState(442);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FNAME:
				_localctx = new FuncContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(411);
				match(FNAME);
				setState(412);
				match(T__3);
				setState(426);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(414);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==PRIMITIVETYPE) {
						{
						setState(413);
						match(PRIMITIVETYPE);
						}
					}

					}
					break;
				case 2:
					{
					setState(424);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
						{
						setState(416);
						expr(0);
						setState(421);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==T__7) {
							{
							{
							setState(417);
							match(T__7);
							setState(418);
							expr(0);
							}
							}
							setState(423);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
					}

					}
					break;
				}
				setState(428);
				match(T__4);
				}
				break;
			case NAME:
				_localctx = new UserDefCallContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(429);
				match(NAME);
				setState(430);
				match(T__3);
				setState(439);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -6917528984689637360L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 31L) != 0) || ((((_la - 187)) & ~0x3f) == 0 && ((1L << (_la - 187)) & 393281L) != 0)) {
					{
					setState(431);
					expr(0);
					setState(436);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__7) {
						{
						{
						setState(432);
						match(T__7);
						setState(433);
						expr(0);
						}
						}
						setState(438);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(441);
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
		public RobotEv3ExprContext robotEv3Expr() {
			return getRuleContext(RobotEv3ExprContext.class,0);
		}
		public RobotExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotExprContext robotExpr() throws RecognitionException {
		RobotExprContext _localctx = new RobotExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_robotExpr);
		try {
			setState(447);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__19:
				enterOuterAlt(_localctx, 1);
				{
				setState(444);
				robotMicrobitv2Expr();
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 2);
				{
				setState(445);
				robotWeDoExpr();
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 3);
				{
				setState(446);
				robotEv3Expr();
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotMicrobitv2Expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotMicrobitv2Expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotMicrobitv2Expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotMicrobitv2ExprContext robotMicrobitv2Expr() throws RecognitionException {
		RobotMicrobitv2ExprContext _localctx = new RobotMicrobitv2ExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_robotMicrobitv2Expr);
		try {
			_localctx = new RobotMicrobitv2ExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(449);
			match(T__19);
			setState(450);
			match(T__11);
			setState(451);
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
		public TerminalNode ACCELEROMETER_SENSOR() { return getToken(TextlyJavaParser.ACCELEROMETER_SENSOR, 0); }
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public TerminalNode LOGO_TOUCH_SENSOR() { return getToken(TextlyJavaParser.LOGO_TOUCH_SENSOR, 0); }
		public TerminalNode COMPASS_SENSOR() { return getToken(TextlyJavaParser.COMPASS_SENSOR, 0); }
		public TerminalNode GESTURE_SENSOR() { return getToken(TextlyJavaParser.GESTURE_SENSOR, 0); }
		public TerminalNode KEYS_SENSOR() { return getToken(TextlyJavaParser.KEYS_SENSOR, 0); }
		public TerminalNode LIGHT_SENSOR() { return getToken(TextlyJavaParser.LIGHT_SENSOR, 0); }
		public TerminalNode PIN_GET_VALUE_SENSOR() { return getToken(TextlyJavaParser.PIN_GET_VALUE_SENSOR, 0); }
		public TerminalNode PIN_TOUCH_SENSOR() { return getToken(TextlyJavaParser.PIN_TOUCH_SENSOR, 0); }
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public TerminalNode SOUND_SENSOR() { return getToken(TextlyJavaParser.SOUND_SENSOR, 0); }
		public TerminalNode TEMPERATURE_SENSOR() { return getToken(TextlyJavaParser.TEMPERATURE_SENSOR, 0); }
		public TerminalNode TIMER_SENSOR() { return getToken(TextlyJavaParser.TIMER_SENSOR, 0); }
		public TerminalNode GET_LED_BRIGTHNESS() { return getToken(TextlyJavaParser.GET_LED_BRIGTHNESS, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RECEIVEMESSAGE() { return getToken(TextlyJavaParser.RECEIVEMESSAGE, 0); }
		public TerminalNode PRIMITIVETYPE() { return getToken(TextlyJavaParser.PRIMITIVETYPE, 0); }
		public Microbitv2SensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_microbitv2SensorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterMicrobitv2SensorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitMicrobitv2SensorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitMicrobitv2SensorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Microbitv2SensorExprContext microbitv2SensorExpr() throws RecognitionException {
		Microbitv2SensorExprContext _localctx = new Microbitv2SensorExprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_microbitv2SensorExpr);
		int _la;
		try {
			setState(520);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ACCELEROMETER_SENSOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(453);
				match(ACCELEROMETER_SENSOR);
				setState(454);
				match(T__3);
				setState(455);
				match(NAME);
				setState(456);
				match(T__4);
				}
				break;
			case LOGO_TOUCH_SENSOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(457);
				match(LOGO_TOUCH_SENSOR);
				setState(458);
				match(T__11);
				setState(459);
				match(T__20);
				setState(460);
				match(T__3);
				setState(461);
				match(T__4);
				}
				break;
			case COMPASS_SENSOR:
				enterOuterAlt(_localctx, 3);
				{
				setState(462);
				match(COMPASS_SENSOR);
				setState(463);
				match(T__11);
				setState(464);
				match(T__21);
				setState(465);
				match(T__3);
				setState(466);
				match(T__4);
				}
				break;
			case GESTURE_SENSOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(467);
				match(GESTURE_SENSOR);
				setState(468);
				match(T__11);
				setState(469);
				match(T__22);
				setState(470);
				match(T__3);
				setState(471);
				match(NAME);
				setState(472);
				match(T__4);
				}
				break;
			case KEYS_SENSOR:
				enterOuterAlt(_localctx, 5);
				{
				setState(473);
				match(KEYS_SENSOR);
				setState(474);
				match(T__11);
				setState(475);
				match(T__20);
				setState(476);
				match(T__3);
				setState(477);
				match(NAME);
				setState(478);
				match(T__4);
				}
				break;
			case LIGHT_SENSOR:
				enterOuterAlt(_localctx, 6);
				{
				setState(479);
				match(LIGHT_SENSOR);
				setState(480);
				match(T__11);
				setState(481);
				match(T__23);
				setState(482);
				match(T__3);
				setState(483);
				match(T__4);
				}
				break;
			case PIN_GET_VALUE_SENSOR:
				enterOuterAlt(_localctx, 7);
				{
				setState(484);
				match(PIN_GET_VALUE_SENSOR);
				setState(485);
				match(T__3);
				setState(486);
				match(NAME);
				setState(487);
				match(T__7);
				setState(488);
				((Microbitv2SensorExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 503316480L) != 0)) ) {
					((Microbitv2SensorExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(489);
				match(T__4);
				}
				break;
			case PIN_TOUCH_SENSOR:
				enterOuterAlt(_localctx, 8);
				{
				setState(490);
				match(PIN_TOUCH_SENSOR);
				setState(491);
				match(T__11);
				setState(492);
				match(T__20);
				setState(493);
				match(T__3);
				setState(494);
				match(INT);
				setState(495);
				match(T__4);
				}
				break;
			case SOUND_SENSOR:
				enterOuterAlt(_localctx, 9);
				{
				setState(496);
				match(SOUND_SENSOR);
				setState(497);
				match(T__11);
				setState(498);
				match(T__28);
				setState(499);
				match(T__11);
				setState(500);
				match(T__29);
				setState(501);
				match(T__3);
				setState(502);
				match(T__4);
				}
				break;
			case TEMPERATURE_SENSOR:
				enterOuterAlt(_localctx, 10);
				{
				setState(503);
				match(TEMPERATURE_SENSOR);
				setState(504);
				match(T__3);
				setState(505);
				match(T__4);
				}
				break;
			case TIMER_SENSOR:
				enterOuterAlt(_localctx, 11);
				{
				setState(506);
				match(TIMER_SENSOR);
				setState(507);
				match(T__3);
				setState(508);
				match(T__4);
				}
				break;
			case GET_LED_BRIGTHNESS:
				enterOuterAlt(_localctx, 12);
				{
				setState(509);
				match(GET_LED_BRIGTHNESS);
				setState(510);
				match(T__3);
				setState(511);
				expr(0);
				setState(512);
				match(T__7);
				setState(513);
				expr(0);
				setState(514);
				match(T__4);
				}
				break;
			case RECEIVEMESSAGE:
				enterOuterAlt(_localctx, 13);
				{
				setState(516);
				match(RECEIVEMESSAGE);
				setState(517);
				match(T__3);
				setState(518);
				match(PRIMITIVETYPE);
				setState(519);
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
	public static class RobotMicrobitv2ActuatorStatementContext extends RobotMicrobitv2StmtContext {
		public Microbitv2ActuatorStmtContext microbitv2ActuatorStmt() {
			return getRuleContext(Microbitv2ActuatorStmtContext.class,0);
		}
		public RobotMicrobitv2ActuatorStatementContext(RobotMicrobitv2StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotMicrobitv2ActuatorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotMicrobitv2ActuatorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotMicrobitv2ActuatorStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotMicrobitv2SensorStatementContext extends RobotMicrobitv2StmtContext {
		public Microbitv2SensorStmtContext microbitv2SensorStmt() {
			return getRuleContext(Microbitv2SensorStmtContext.class,0);
		}
		public RobotMicrobitv2SensorStatementContext(RobotMicrobitv2StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotMicrobitv2SensorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotMicrobitv2SensorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotMicrobitv2SensorStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotMicrobitv2StmtContext robotMicrobitv2Stmt() throws RecognitionException {
		RobotMicrobitv2StmtContext _localctx = new RobotMicrobitv2StmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_robotMicrobitv2Stmt);
		try {
			setState(528);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				_localctx = new RobotMicrobitv2SensorStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(522);
				match(T__19);
				setState(523);
				match(T__11);
				setState(524);
				microbitv2SensorStmt();
				}
				break;
			case 2:
				_localctx = new RobotMicrobitv2ActuatorStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(525);
				match(T__19);
				setState(526);
				match(T__11);
				setState(527);
				microbitv2ActuatorStmt();
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
	public static class Microbitv2SensorStmtContext extends ParserRuleContext {
		public Token op;
		public TerminalNode PIN_SET_TOUCH_MODE() { return getToken(TextlyJavaParser.PIN_SET_TOUCH_MODE, 0); }
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public TerminalNode TIMER_RESET() { return getToken(TextlyJavaParser.TIMER_RESET, 0); }
		public Microbitv2SensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_microbitv2SensorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterMicrobitv2SensorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitMicrobitv2SensorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitMicrobitv2SensorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Microbitv2SensorStmtContext microbitv2SensorStmt() throws RecognitionException {
		Microbitv2SensorStmtContext _localctx = new Microbitv2SensorStmtContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_microbitv2SensorStmt);
		int _la;
		try {
			setState(539);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PIN_SET_TOUCH_MODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(530);
				match(PIN_SET_TOUCH_MODE);
				setState(531);
				match(T__3);
				setState(532);
				match(INT);
				setState(533);
				match(T__7);
				setState(534);
				((Microbitv2SensorStmtContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__30 || _la==T__31) ) {
					((Microbitv2SensorStmtContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(535);
				match(T__4);
				}
				break;
			case TIMER_RESET:
				enterOuterAlt(_localctx, 2);
				{
				setState(536);
				match(TIMER_RESET);
				setState(537);
				match(T__3);
				setState(538);
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
	public static class Microbitv2ActuatorStmtContext extends ParserRuleContext {
		public Token op;
		public TerminalNode SHOWTEXT() { return getToken(TextlyJavaParser.SHOWTEXT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode SHOWIMAGE() { return getToken(TextlyJavaParser.SHOWIMAGE, 0); }
		public TerminalNode SHOWANIMATION() { return getToken(TextlyJavaParser.SHOWANIMATION, 0); }
		public TerminalNode CLEARDISPLAY() { return getToken(TextlyJavaParser.CLEARDISPLAY, 0); }
		public TerminalNode SETLED() { return getToken(TextlyJavaParser.SETLED, 0); }
		public TerminalNode SHOWONSERIALMOTOR() { return getToken(TextlyJavaParser.SHOWONSERIALMOTOR, 0); }
		public TerminalNode PITCH() { return getToken(TextlyJavaParser.PITCH, 0); }
		public TerminalNode PLAYNOTE() { return getToken(TextlyJavaParser.PLAYNOTE, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public TerminalNode PLAY() { return getToken(TextlyJavaParser.PLAY, 0); }
		public TerminalNode PLAYSOUND() { return getToken(TextlyJavaParser.PLAYSOUND, 0); }
		public TerminalNode SETVOLUME() { return getToken(TextlyJavaParser.SETVOLUME, 0); }
		public TerminalNode SPEAKER() { return getToken(TextlyJavaParser.SPEAKER, 0); }
		public TerminalNode WRITEVALUE() { return getToken(TextlyJavaParser.WRITEVALUE, 0); }
		public TerminalNode SWITCHLED() { return getToken(TextlyJavaParser.SWITCHLED, 0); }
		public TerminalNode SHOWCHARACTER() { return getToken(TextlyJavaParser.SHOWCHARACTER, 0); }
		public TerminalNode RADIOSEND() { return getToken(TextlyJavaParser.RADIOSEND, 0); }
		public TerminalNode PRIMITIVETYPE() { return getToken(TextlyJavaParser.PRIMITIVETYPE, 0); }
		public TerminalNode RADIOSET() { return getToken(TextlyJavaParser.RADIOSET, 0); }
		public Microbitv2ActuatorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_microbitv2ActuatorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterMicrobitv2ActuatorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitMicrobitv2ActuatorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitMicrobitv2ActuatorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Microbitv2ActuatorStmtContext microbitv2ActuatorStmt() throws RecognitionException {
		Microbitv2ActuatorStmtContext _localctx = new Microbitv2ActuatorStmtContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_microbitv2ActuatorStmt);
		int _la;
		try {
			setState(635);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SHOWTEXT:
				enterOuterAlt(_localctx, 1);
				{
				setState(541);
				match(SHOWTEXT);
				setState(542);
				match(T__3);
				setState(543);
				expr(0);
				setState(544);
				match(T__4);
				}
				break;
			case SHOWIMAGE:
				enterOuterAlt(_localctx, 2);
				{
				setState(546);
				match(SHOWIMAGE);
				setState(547);
				match(T__3);
				setState(548);
				expr(0);
				setState(549);
				match(T__4);
				}
				break;
			case SHOWANIMATION:
				enterOuterAlt(_localctx, 3);
				{
				setState(551);
				match(SHOWANIMATION);
				setState(552);
				match(T__3);
				setState(553);
				expr(0);
				setState(554);
				match(T__4);
				}
				break;
			case CLEARDISPLAY:
				enterOuterAlt(_localctx, 4);
				{
				setState(556);
				match(CLEARDISPLAY);
				setState(557);
				match(T__3);
				setState(558);
				match(T__4);
				}
				break;
			case SETLED:
				enterOuterAlt(_localctx, 5);
				{
				setState(559);
				match(SETLED);
				setState(560);
				match(T__3);
				setState(561);
				expr(0);
				setState(562);
				match(T__7);
				setState(563);
				expr(0);
				setState(564);
				match(T__7);
				setState(565);
				expr(0);
				setState(566);
				match(T__4);
				}
				break;
			case SHOWONSERIALMOTOR:
				enterOuterAlt(_localctx, 6);
				{
				setState(568);
				match(SHOWONSERIALMOTOR);
				setState(569);
				match(T__3);
				setState(570);
				expr(0);
				setState(571);
				match(T__4);
				}
				break;
			case PITCH:
				enterOuterAlt(_localctx, 7);
				{
				setState(573);
				match(PITCH);
				setState(574);
				match(T__3);
				setState(575);
				expr(0);
				setState(576);
				match(T__7);
				setState(577);
				expr(0);
				setState(578);
				match(T__4);
				}
				break;
			case PLAYNOTE:
				enterOuterAlt(_localctx, 8);
				{
				setState(580);
				match(PLAYNOTE);
				setState(581);
				match(T__3);
				setState(582);
				match(NAME);
				setState(583);
				match(T__7);
				setState(584);
				match(NAME);
				setState(585);
				match(T__4);
				}
				break;
			case PLAY:
				enterOuterAlt(_localctx, 9);
				{
				setState(586);
				match(PLAY);
				setState(587);
				match(T__3);
				setState(588);
				match(NAME);
				setState(589);
				match(T__4);
				}
				break;
			case PLAYSOUND:
				enterOuterAlt(_localctx, 10);
				{
				setState(590);
				match(PLAYSOUND);
				setState(591);
				match(T__3);
				setState(592);
				match(NAME);
				setState(593);
				match(T__4);
				}
				break;
			case SETVOLUME:
				enterOuterAlt(_localctx, 11);
				{
				setState(594);
				match(SETVOLUME);
				setState(595);
				match(T__3);
				setState(596);
				expr(0);
				setState(597);
				match(T__4);
				}
				break;
			case SPEAKER:
				enterOuterAlt(_localctx, 12);
				{
				setState(599);
				match(SPEAKER);
				setState(600);
				match(T__3);
				setState(601);
				match(NAME);
				setState(602);
				match(T__4);
				}
				break;
			case WRITEVALUE:
				enterOuterAlt(_localctx, 13);
				{
				setState(603);
				match(WRITEVALUE);
				setState(604);
				match(T__3);
				setState(605);
				((Microbitv2ActuatorStmtContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__24 || _la==T__25) ) {
					((Microbitv2ActuatorStmtContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(606);
				match(T__7);
				setState(607);
				match(NAME);
				setState(608);
				match(T__7);
				setState(609);
				expr(0);
				setState(610);
				match(T__4);
				}
				break;
			case SWITCHLED:
				enterOuterAlt(_localctx, 14);
				{
				setState(612);
				match(SWITCHLED);
				setState(613);
				match(T__3);
				setState(614);
				match(NAME);
				setState(615);
				match(T__4);
				}
				break;
			case SHOWCHARACTER:
				enterOuterAlt(_localctx, 15);
				{
				setState(616);
				match(SHOWCHARACTER);
				setState(617);
				match(T__3);
				setState(618);
				expr(0);
				setState(619);
				match(T__4);
				}
				break;
			case RADIOSEND:
				enterOuterAlt(_localctx, 16);
				{
				setState(621);
				match(RADIOSEND);
				setState(622);
				match(T__3);
				setState(623);
				match(PRIMITIVETYPE);
				setState(624);
				match(T__7);
				setState(625);
				expr(0);
				setState(626);
				match(T__7);
				setState(627);
				expr(0);
				setState(628);
				match(T__4);
				}
				break;
			case RADIOSET:
				enterOuterAlt(_localctx, 17);
				{
				setState(630);
				match(RADIOSET);
				setState(631);
				match(T__3);
				setState(632);
				expr(0);
				setState(633);
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
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotWeDoExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotWeDoExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotWeDoExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotWeDoExprContext robotWeDoExpr() throws RecognitionException {
		RobotWeDoExprContext _localctx = new RobotWeDoExprContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_robotWeDoExpr);
		try {
			_localctx = new RobotWeDoExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			match(T__32);
			setState(638);
			match(T__11);
			setState(639);
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
		public TerminalNode GYRO_SENSOR() { return getToken(TextlyJavaParser.GYRO_SENSOR, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public TerminalNode INFRARED_SENSOR() { return getToken(TextlyJavaParser.INFRARED_SENSOR, 0); }
		public TerminalNode KEYS_SENSOR() { return getToken(TextlyJavaParser.KEYS_SENSOR, 0); }
		public TerminalNode TIMER_SENSOR() { return getToken(TextlyJavaParser.TIMER_SENSOR, 0); }
		public WeDoSensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_weDoSensorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterWeDoSensorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitWeDoSensorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitWeDoSensorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WeDoSensorExprContext weDoSensorExpr() throws RecognitionException {
		WeDoSensorExprContext _localctx = new WeDoSensorExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_weDoSensorExpr);
		try {
			setState(662);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GYRO_SENSOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(641);
				match(GYRO_SENSOR);
				setState(642);
				match(T__11);
				setState(643);
				match(T__33);
				setState(644);
				match(T__3);
				setState(645);
				match(NAME);
				setState(646);
				match(T__7);
				setState(647);
				match(NAME);
				setState(648);
				match(T__4);
				}
				break;
			case INFRARED_SENSOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(649);
				match(INFRARED_SENSOR);
				setState(650);
				match(T__3);
				setState(651);
				match(NAME);
				setState(652);
				match(T__4);
				}
				break;
			case KEYS_SENSOR:
				enterOuterAlt(_localctx, 3);
				{
				setState(653);
				match(KEYS_SENSOR);
				setState(654);
				match(T__11);
				setState(655);
				match(T__20);
				setState(656);
				match(T__3);
				setState(657);
				match(NAME);
				setState(658);
				match(T__4);
				}
				break;
			case TIMER_SENSOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(659);
				match(TIMER_SENSOR);
				setState(660);
				match(T__3);
				setState(661);
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
	public static class RobotWedoActuatorStatementContext extends RobotWeDoStmtContext {
		public WedoActuatorStmtContext wedoActuatorStmt() {
			return getRuleContext(WedoActuatorStmtContext.class,0);
		}
		public RobotWedoActuatorStatementContext(RobotWeDoStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotWedoActuatorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotWedoActuatorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotWedoActuatorStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotWeDoSensorStatementContext extends RobotWeDoStmtContext {
		public WedoSensorStmtContext wedoSensorStmt() {
			return getRuleContext(WedoSensorStmtContext.class,0);
		}
		public RobotWeDoSensorStatementContext(RobotWeDoStmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotWeDoSensorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotWeDoSensorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotWeDoSensorStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotWeDoStmtContext robotWeDoStmt() throws RecognitionException {
		RobotWeDoStmtContext _localctx = new RobotWeDoStmtContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_robotWeDoStmt);
		try {
			setState(670);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				_localctx = new RobotWeDoSensorStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(664);
				match(T__32);
				setState(665);
				match(T__11);
				setState(666);
				wedoSensorStmt();
				}
				break;
			case 2:
				_localctx = new RobotWedoActuatorStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(667);
				match(T__32);
				setState(668);
				match(T__11);
				setState(669);
				wedoActuatorStmt();
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
	public static class WedoSensorStmtContext extends ParserRuleContext {
		public TerminalNode TIMER_RESET() { return getToken(TextlyJavaParser.TIMER_RESET, 0); }
		public WedoSensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wedoSensorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterWedoSensorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitWedoSensorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitWedoSensorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WedoSensorStmtContext wedoSensorStmt() throws RecognitionException {
		WedoSensorStmtContext _localctx = new WedoSensorStmtContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_wedoSensorStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			match(TIMER_RESET);
			setState(673);
			match(T__3);
			setState(674);
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

	@SuppressWarnings("CheckReturnValue")
	public static class WedoActuatorStmtContext extends ParserRuleContext {
		public TerminalNode SHOWTEXT() { return getToken(TextlyJavaParser.SHOWTEXT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MOTORMOVE() { return getToken(TextlyJavaParser.MOTORMOVE, 0); }
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public TerminalNode MOTORSTOP() { return getToken(TextlyJavaParser.MOTORSTOP, 0); }
		public TerminalNode CLEARDISPLAY() { return getToken(TextlyJavaParser.CLEARDISPLAY, 0); }
		public TerminalNode PITCH() { return getToken(TextlyJavaParser.PITCH, 0); }
		public TerminalNode TURNRGBON() { return getToken(TextlyJavaParser.TURNRGBON, 0); }
		public TerminalNode TURNRGBOFF() { return getToken(TextlyJavaParser.TURNRGBOFF, 0); }
		public WedoActuatorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wedoActuatorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterWedoActuatorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitWedoActuatorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitWedoActuatorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WedoActuatorStmtContext wedoActuatorStmt() throws RecognitionException {
		WedoActuatorStmtContext _localctx = new WedoActuatorStmtContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_wedoActuatorStmt);
		int _la;
		try {
			setState(719);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SHOWTEXT:
				enterOuterAlt(_localctx, 1);
				{
				setState(676);
				match(SHOWTEXT);
				setState(677);
				match(T__3);
				setState(678);
				expr(0);
				setState(679);
				match(T__4);
				}
				break;
			case MOTORMOVE:
				enterOuterAlt(_localctx, 2);
				{
				setState(681);
				match(MOTORMOVE);
				setState(682);
				match(T__3);
				setState(683);
				match(NAME);
				setState(684);
				match(T__7);
				setState(685);
				expr(0);
				setState(688);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(686);
					match(T__7);
					setState(687);
					expr(0);
					}
				}

				setState(690);
				match(T__4);
				}
				break;
			case MOTORSTOP:
				enterOuterAlt(_localctx, 3);
				{
				setState(692);
				match(MOTORSTOP);
				setState(693);
				match(T__3);
				setState(694);
				match(NAME);
				setState(695);
				match(T__4);
				}
				break;
			case CLEARDISPLAY:
				enterOuterAlt(_localctx, 4);
				{
				setState(696);
				match(CLEARDISPLAY);
				setState(697);
				match(T__3);
				setState(698);
				match(T__4);
				}
				break;
			case PITCH:
				enterOuterAlt(_localctx, 5);
				{
				setState(699);
				match(PITCH);
				setState(700);
				match(T__3);
				setState(701);
				match(NAME);
				setState(702);
				match(T__7);
				setState(703);
				expr(0);
				setState(704);
				match(T__7);
				setState(705);
				expr(0);
				setState(706);
				match(T__4);
				}
				break;
			case TURNRGBON:
				enterOuterAlt(_localctx, 6);
				{
				setState(708);
				match(TURNRGBON);
				setState(709);
				match(T__3);
				setState(710);
				match(NAME);
				setState(711);
				match(T__7);
				setState(712);
				expr(0);
				setState(713);
				match(T__4);
				}
				break;
			case TURNRGBOFF:
				enterOuterAlt(_localctx, 7);
				{
				setState(715);
				match(TURNRGBOFF);
				setState(716);
				match(T__3);
				setState(717);
				match(NAME);
				setState(718);
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
	public static class RobotEv3ExprContext extends ParserRuleContext {
		public RobotEv3ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotEv3Expr; }
	 
		public RobotEv3ExprContext() { }
		public void copyFrom(RobotEv3ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotEv3ExpressionContext extends RobotEv3ExprContext {
		public Ev3SensorExprContext ev3SensorExpr() {
			return getRuleContext(Ev3SensorExprContext.class,0);
		}
		public RobotEv3ExpressionContext(RobotEv3ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotEv3Expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotEv3Expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotEv3Expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotEv3ExprContext robotEv3Expr() throws RecognitionException {
		RobotEv3ExprContext _localctx = new RobotEv3ExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_robotEv3Expr);
		try {
			_localctx = new RobotEv3ExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(721);
			match(T__34);
			setState(722);
			match(T__11);
			setState(723);
			ev3SensorExpr();
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
	public static class Ev3SensorExprContext extends ParserRuleContext {
		public TerminalNode GETSPEEDMOTOR() { return getToken(TextlyJavaParser.GETSPEEDMOTOR, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public TerminalNode GETVOLUME() { return getToken(TextlyJavaParser.GETVOLUME, 0); }
		public TerminalNode TOUCH_SENSOR() { return getToken(TextlyJavaParser.TOUCH_SENSOR, 0); }
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public TerminalNode ULTRASONIC_SENSOR() { return getToken(TextlyJavaParser.ULTRASONIC_SENSOR, 0); }
		public TerminalNode COLOR_SENSOR() { return getToken(TextlyJavaParser.COLOR_SENSOR, 0); }
		public TerminalNode INFRARED_SENSOR() { return getToken(TextlyJavaParser.INFRARED_SENSOR, 0); }
		public TerminalNode ENCODER_SENSOR() { return getToken(TextlyJavaParser.ENCODER_SENSOR, 0); }
		public TerminalNode KEYS_SENSOR() { return getToken(TextlyJavaParser.KEYS_SENSOR, 0); }
		public TerminalNode GYRO_SENSOR() { return getToken(TextlyJavaParser.GYRO_SENSOR, 0); }
		public TerminalNode TIMER_SENSOR() { return getToken(TextlyJavaParser.TIMER_SENSOR, 0); }
		public TerminalNode SOUND_SENSOR() { return getToken(TextlyJavaParser.SOUND_SENSOR, 0); }
		public TerminalNode HT_COLOR_SENSOR() { return getToken(TextlyJavaParser.HT_COLOR_SENSOR, 0); }
		public TerminalNode HT_INFRARED_SENSOR() { return getToken(TextlyJavaParser.HT_INFRARED_SENSOR, 0); }
		public TerminalNode HT_COMPASS_SENSOR() { return getToken(TextlyJavaParser.HT_COMPASS_SENSOR, 0); }
		public TerminalNode CONNECT_TO_ROBOT() { return getToken(TextlyJavaParser.CONNECT_TO_ROBOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RECEIVEMESSAGE() { return getToken(TextlyJavaParser.RECEIVEMESSAGE, 0); }
		public TerminalNode WAIT_FOR_CONNECTION() { return getToken(TextlyJavaParser.WAIT_FOR_CONNECTION, 0); }
		public TerminalNode GETOUTPUTNEURON() { return getToken(TextlyJavaParser.GETOUTPUTNEURON, 0); }
		public TerminalNode GETWEIGHT() { return getToken(TextlyJavaParser.GETWEIGHT, 0); }
		public TerminalNode GETBIAS() { return getToken(TextlyJavaParser.GETBIAS, 0); }
		public Ev3SensorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ev3SensorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterEv3SensorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitEv3SensorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitEv3SensorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ev3SensorExprContext ev3SensorExpr() throws RecognitionException {
		Ev3SensorExprContext _localctx = new Ev3SensorExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_ev3SensorExpr);
		try {
			setState(871);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(725);
				match(GETSPEEDMOTOR);
				setState(726);
				match(T__3);
				setState(727);
				match(NAME);
				setState(728);
				match(T__4);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(729);
				match(GETVOLUME);
				setState(730);
				match(T__3);
				setState(731);
				match(T__4);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(732);
				match(TOUCH_SENSOR);
				setState(733);
				match(T__11);
				setState(734);
				match(T__20);
				setState(735);
				match(T__3);
				setState(736);
				match(INT);
				setState(737);
				match(T__4);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(738);
				match(ULTRASONIC_SENSOR);
				setState(739);
				match(T__11);
				setState(740);
				match(T__35);
				setState(741);
				match(T__3);
				setState(742);
				match(INT);
				setState(743);
				match(T__4);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(744);
				match(ULTRASONIC_SENSOR);
				setState(745);
				match(T__11);
				setState(746);
				match(T__36);
				setState(747);
				match(T__3);
				setState(748);
				match(INT);
				setState(749);
				match(T__4);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(750);
				match(COLOR_SENSOR);
				setState(751);
				match(T__3);
				setState(752);
				match(NAME);
				setState(753);
				match(T__7);
				setState(754);
				match(INT);
				setState(755);
				match(T__4);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(756);
				match(INFRARED_SENSOR);
				setState(757);
				match(T__11);
				setState(758);
				match(T__35);
				setState(759);
				match(T__3);
				setState(760);
				match(INT);
				setState(761);
				match(T__4);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(762);
				match(INFRARED_SENSOR);
				setState(763);
				match(T__11);
				setState(764);
				match(T__36);
				setState(765);
				match(T__3);
				setState(766);
				match(INT);
				setState(767);
				match(T__4);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(768);
				match(ENCODER_SENSOR);
				setState(769);
				match(T__11);
				setState(770);
				match(T__37);
				setState(771);
				match(T__3);
				setState(772);
				match(NAME);
				setState(773);
				match(T__4);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(774);
				match(ENCODER_SENSOR);
				setState(775);
				match(T__11);
				setState(776);
				match(T__38);
				setState(777);
				match(T__3);
				setState(778);
				match(NAME);
				setState(779);
				match(T__4);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(780);
				match(ENCODER_SENSOR);
				setState(781);
				match(T__11);
				setState(782);
				match(T__35);
				setState(783);
				match(T__3);
				setState(784);
				match(NAME);
				setState(785);
				match(T__4);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(786);
				match(KEYS_SENSOR);
				setState(787);
				match(T__11);
				setState(788);
				match(T__20);
				setState(789);
				match(T__3);
				setState(790);
				match(NAME);
				setState(791);
				match(T__4);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(792);
				match(GYRO_SENSOR);
				setState(793);
				match(T__11);
				setState(794);
				match(T__21);
				setState(795);
				match(T__3);
				setState(796);
				match(INT);
				setState(797);
				match(T__4);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(798);
				match(GYRO_SENSOR);
				setState(799);
				match(T__11);
				setState(800);
				match(T__39);
				setState(801);
				match(T__3);
				setState(802);
				match(INT);
				setState(803);
				match(T__4);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(804);
				match(TIMER_SENSOR);
				setState(805);
				match(T__3);
				setState(806);
				match(INT);
				setState(807);
				match(T__4);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(808);
				match(SOUND_SENSOR);
				setState(809);
				match(T__11);
				setState(810);
				match(T__40);
				setState(811);
				match(T__3);
				setState(812);
				match(INT);
				setState(813);
				match(T__4);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(814);
				match(HT_COLOR_SENSOR);
				setState(815);
				match(T__3);
				setState(816);
				match(NAME);
				setState(817);
				match(T__7);
				setState(818);
				match(INT);
				setState(819);
				match(T__4);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(820);
				match(HT_INFRARED_SENSOR);
				setState(821);
				match(T__11);
				setState(822);
				match(T__41);
				setState(823);
				match(T__3);
				setState(824);
				match(INT);
				setState(825);
				match(T__4);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(826);
				match(HT_INFRARED_SENSOR);
				setState(827);
				match(T__11);
				setState(828);
				match(T__42);
				setState(829);
				match(T__3);
				setState(830);
				match(INT);
				setState(831);
				match(T__4);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(832);
				match(HT_COMPASS_SENSOR);
				setState(833);
				match(T__11);
				setState(834);
				match(T__21);
				setState(835);
				match(T__3);
				setState(836);
				match(INT);
				setState(837);
				match(T__4);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(838);
				match(HT_COMPASS_SENSOR);
				setState(839);
				match(T__11);
				setState(840);
				match(T__43);
				setState(841);
				match(T__3);
				setState(842);
				match(INT);
				setState(843);
				match(T__4);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(844);
				match(CONNECT_TO_ROBOT);
				setState(845);
				match(T__3);
				setState(846);
				expr(0);
				setState(847);
				match(T__4);
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(849);
				match(RECEIVEMESSAGE);
				setState(850);
				match(T__3);
				setState(851);
				expr(0);
				setState(852);
				match(T__4);
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(854);
				match(WAIT_FOR_CONNECTION);
				setState(855);
				match(T__3);
				setState(856);
				match(T__4);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(857);
				match(GETOUTPUTNEURON);
				setState(858);
				match(T__3);
				setState(859);
				match(NAME);
				setState(860);
				match(T__4);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(861);
				match(GETWEIGHT);
				setState(862);
				match(T__3);
				setState(863);
				match(NAME);
				setState(864);
				match(T__7);
				setState(865);
				match(NAME);
				setState(866);
				match(T__4);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(867);
				match(GETBIAS);
				setState(868);
				match(T__3);
				setState(869);
				match(NAME);
				setState(870);
				match(T__4);
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
	public static class RobotEv3StmtContext extends ParserRuleContext {
		public RobotEv3StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_robotEv3Stmt; }
	 
		public RobotEv3StmtContext() { }
		public void copyFrom(RobotEv3StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotEv3SensorStatementContext extends RobotEv3StmtContext {
		public Ev3SensorStmtContext ev3SensorStmt() {
			return getRuleContext(Ev3SensorStmtContext.class,0);
		}
		public RobotEv3SensorStatementContext(RobotEv3StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotEv3SensorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotEv3SensorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotEv3SensorStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotEv3ActuatorStatementContext extends RobotEv3StmtContext {
		public Ev3ActuatorStmtContext ev3ActuatorStmt() {
			return getRuleContext(Ev3ActuatorStmtContext.class,0);
		}
		public RobotEv3ActuatorStatementContext(RobotEv3StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotEv3ActuatorStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotEv3ActuatorStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotEv3ActuatorStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RobotEv3NeuralNetworksContext extends RobotEv3StmtContext {
		public Ev3xNNContext ev3xNN() {
			return getRuleContext(Ev3xNNContext.class,0);
		}
		public RobotEv3NeuralNetworksContext(RobotEv3StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterRobotEv3NeuralNetworks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitRobotEv3NeuralNetworks(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitRobotEv3NeuralNetworks(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RobotEv3StmtContext robotEv3Stmt() throws RecognitionException {
		RobotEv3StmtContext _localctx = new RobotEv3StmtContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_robotEv3Stmt);
		try {
			setState(882);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				_localctx = new RobotEv3SensorStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(873);
				match(T__34);
				setState(874);
				match(T__11);
				setState(875);
				ev3SensorStmt();
				}
				break;
			case 2:
				_localctx = new RobotEv3ActuatorStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(876);
				match(T__34);
				setState(877);
				match(T__11);
				setState(878);
				ev3ActuatorStmt();
				}
				break;
			case 3:
				_localctx = new RobotEv3NeuralNetworksContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(879);
				match(T__34);
				setState(880);
				match(T__11);
				setState(881);
				ev3xNN();
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
	public static class Ev3SensorStmtContext extends ParserRuleContext {
		public TerminalNode TIMER_RESET() { return getToken(TextlyJavaParser.TIMER_RESET, 0); }
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public TerminalNode ENCODER_RESET() { return getToken(TextlyJavaParser.ENCODER_RESET, 0); }
		public TerminalNode NAME() { return getToken(TextlyJavaParser.NAME, 0); }
		public TerminalNode GYRO_RESET() { return getToken(TextlyJavaParser.GYRO_RESET, 0); }
		public TerminalNode HT_CCOMPASSSTARTCALIBRATION() { return getToken(TextlyJavaParser.HT_CCOMPASSSTARTCALIBRATION, 0); }
		public Ev3SensorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ev3SensorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterEv3SensorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitEv3SensorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitEv3SensorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ev3SensorStmtContext ev3SensorStmt() throws RecognitionException {
		Ev3SensorStmtContext _localctx = new Ev3SensorStmtContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_ev3SensorStmt);
		try {
			setState(900);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TIMER_RESET:
				enterOuterAlt(_localctx, 1);
				{
				setState(884);
				match(TIMER_RESET);
				setState(885);
				match(T__3);
				setState(886);
				match(INT);
				setState(887);
				match(T__4);
				}
				break;
			case ENCODER_RESET:
				enterOuterAlt(_localctx, 2);
				{
				setState(888);
				match(ENCODER_RESET);
				setState(889);
				match(T__3);
				setState(890);
				match(NAME);
				setState(891);
				match(T__4);
				}
				break;
			case GYRO_RESET:
				enterOuterAlt(_localctx, 3);
				{
				setState(892);
				match(GYRO_RESET);
				setState(893);
				match(T__3);
				setState(894);
				match(INT);
				setState(895);
				match(T__4);
				}
				break;
			case HT_CCOMPASSSTARTCALIBRATION:
				enterOuterAlt(_localctx, 4);
				{
				setState(896);
				match(HT_CCOMPASSSTARTCALIBRATION);
				setState(897);
				match(T__3);
				setState(898);
				match(INT);
				setState(899);
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
	public static class Ev3ActuatorStmtContext extends ParserRuleContext {
		public TerminalNode TURNONMOTOR() { return getToken(TextlyJavaParser.TURNONMOTOR, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ROTATEMOTOR() { return getToken(TextlyJavaParser.ROTATEMOTOR, 0); }
		public TerminalNode SETMOTORSPEED() { return getToken(TextlyJavaParser.SETMOTORSPEED, 0); }
		public TerminalNode STOPMOTOR() { return getToken(TextlyJavaParser.STOPMOTOR, 0); }
		public TerminalNode DRIVEDISTANCE() { return getToken(TextlyJavaParser.DRIVEDISTANCE, 0); }
		public TerminalNode STOPDRIVE() { return getToken(TextlyJavaParser.STOPDRIVE, 0); }
		public TerminalNode ROTATEDIRECTIONANGLE() { return getToken(TextlyJavaParser.ROTATEDIRECTIONANGLE, 0); }
		public TerminalNode ROTATEDIRECTIONREGULATED() { return getToken(TextlyJavaParser.ROTATEDIRECTIONREGULATED, 0); }
		public TerminalNode DRIVEINCURVE() { return getToken(TextlyJavaParser.DRIVEINCURVE, 0); }
		public TerminalNode SETLANGUAGE() { return getToken(TextlyJavaParser.SETLANGUAGE, 0); }
		public TerminalNode DRAWTEXT() { return getToken(TextlyJavaParser.DRAWTEXT, 0); }
		public TerminalNode DRAWPICTURE() { return getToken(TextlyJavaParser.DRAWPICTURE, 0); }
		public TerminalNode CLEARDISPLAY() { return getToken(TextlyJavaParser.CLEARDISPLAY, 0); }
		public TerminalNode PLAYTONE() { return getToken(TextlyJavaParser.PLAYTONE, 0); }
		public TerminalNode PLAY() { return getToken(TextlyJavaParser.PLAY, 0); }
		public TerminalNode INT() { return getToken(TextlyJavaParser.INT, 0); }
		public TerminalNode SETVOLUME() { return getToken(TextlyJavaParser.SETVOLUME, 0); }
		public TerminalNode SAYTEXT() { return getToken(TextlyJavaParser.SAYTEXT, 0); }
		public TerminalNode LEDON() { return getToken(TextlyJavaParser.LEDON, 0); }
		public TerminalNode LEDOFF() { return getToken(TextlyJavaParser.LEDOFF, 0); }
		public TerminalNode RESETLED() { return getToken(TextlyJavaParser.RESETLED, 0); }
		public TerminalNode SENDMESSAGE() { return getToken(TextlyJavaParser.SENDMESSAGE, 0); }
		public Ev3ActuatorStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ev3ActuatorStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterEv3ActuatorStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitEv3ActuatorStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitEv3ActuatorStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ev3ActuatorStmtContext ev3ActuatorStmt() throws RecognitionException {
		Ev3ActuatorStmtContext _localctx = new Ev3ActuatorStmtContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_ev3ActuatorStmt);
		int _la;
		try {
			setState(1043);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TURNONMOTOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(902);
				match(TURNONMOTOR);
				setState(903);
				match(T__3);
				setState(904);
				match(NAME);
				setState(905);
				match(T__7);
				setState(906);
				expr(0);
				setState(907);
				match(T__4);
				}
				break;
			case ROTATEMOTOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(909);
				match(ROTATEMOTOR);
				setState(910);
				match(T__3);
				setState(911);
				match(NAME);
				setState(912);
				match(T__7);
				setState(913);
				expr(0);
				setState(914);
				match(T__7);
				setState(915);
				match(NAME);
				setState(916);
				match(T__7);
				setState(917);
				expr(0);
				setState(918);
				match(T__4);
				}
				break;
			case SETMOTORSPEED:
				enterOuterAlt(_localctx, 3);
				{
				setState(920);
				match(SETMOTORSPEED);
				setState(921);
				match(T__3);
				setState(922);
				match(NAME);
				setState(923);
				match(T__7);
				setState(924);
				expr(0);
				setState(925);
				match(T__4);
				}
				break;
			case STOPMOTOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(927);
				match(STOPMOTOR);
				setState(928);
				match(T__3);
				setState(929);
				match(NAME);
				setState(930);
				match(T__7);
				setState(931);
				match(NAME);
				setState(932);
				match(T__4);
				}
				break;
			case DRIVEDISTANCE:
				enterOuterAlt(_localctx, 5);
				{
				setState(933);
				match(DRIVEDISTANCE);
				setState(934);
				match(T__3);
				setState(935);
				match(NAME);
				setState(936);
				match(T__7);
				setState(937);
				expr(0);
				setState(940);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(938);
					match(T__7);
					setState(939);
					expr(0);
					}
				}

				setState(942);
				match(T__4);
				}
				break;
			case STOPDRIVE:
				enterOuterAlt(_localctx, 6);
				{
				setState(944);
				match(STOPDRIVE);
				setState(945);
				match(T__3);
				setState(946);
				match(T__4);
				}
				break;
			case ROTATEDIRECTIONANGLE:
				enterOuterAlt(_localctx, 7);
				{
				setState(947);
				match(ROTATEDIRECTIONANGLE);
				setState(948);
				match(T__3);
				setState(949);
				match(NAME);
				setState(950);
				match(T__7);
				setState(951);
				expr(0);
				setState(952);
				match(T__7);
				setState(953);
				expr(0);
				setState(954);
				match(T__4);
				}
				break;
			case ROTATEDIRECTIONREGULATED:
				enterOuterAlt(_localctx, 8);
				{
				setState(956);
				match(ROTATEDIRECTIONREGULATED);
				setState(957);
				match(T__3);
				setState(958);
				match(NAME);
				setState(959);
				match(T__7);
				setState(960);
				expr(0);
				setState(961);
				match(T__4);
				}
				break;
			case DRIVEINCURVE:
				enterOuterAlt(_localctx, 9);
				{
				setState(963);
				match(DRIVEINCURVE);
				setState(964);
				match(T__3);
				setState(965);
				match(NAME);
				setState(966);
				match(T__7);
				setState(967);
				expr(0);
				setState(968);
				match(T__7);
				setState(969);
				expr(0);
				setState(972);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(970);
					match(T__7);
					setState(971);
					expr(0);
					}
				}

				setState(974);
				match(T__4);
				}
				break;
			case SETLANGUAGE:
				enterOuterAlt(_localctx, 10);
				{
				setState(976);
				match(SETLANGUAGE);
				setState(977);
				match(T__3);
				setState(978);
				match(NAME);
				setState(979);
				match(T__4);
				}
				break;
			case DRAWTEXT:
				enterOuterAlt(_localctx, 11);
				{
				setState(980);
				match(DRAWTEXT);
				setState(981);
				match(T__3);
				setState(982);
				expr(0);
				setState(983);
				match(T__7);
				setState(984);
				expr(0);
				setState(985);
				match(T__7);
				setState(986);
				expr(0);
				setState(987);
				match(T__4);
				}
				break;
			case DRAWPICTURE:
				enterOuterAlt(_localctx, 12);
				{
				setState(989);
				match(DRAWPICTURE);
				setState(990);
				match(T__3);
				setState(991);
				match(NAME);
				setState(992);
				match(T__4);
				}
				break;
			case CLEARDISPLAY:
				enterOuterAlt(_localctx, 13);
				{
				setState(993);
				match(CLEARDISPLAY);
				setState(994);
				match(T__3);
				setState(995);
				match(T__4);
				}
				break;
			case PLAYTONE:
				enterOuterAlt(_localctx, 14);
				{
				setState(996);
				match(PLAYTONE);
				setState(997);
				match(T__3);
				setState(998);
				expr(0);
				setState(999);
				match(T__7);
				setState(1000);
				expr(0);
				setState(1001);
				match(T__4);
				}
				break;
			case PLAY:
				enterOuterAlt(_localctx, 15);
				{
				setState(1003);
				match(PLAY);
				setState(1004);
				match(T__3);
				setState(1005);
				match(INT);
				setState(1006);
				match(T__4);
				}
				break;
			case SETVOLUME:
				enterOuterAlt(_localctx, 16);
				{
				setState(1007);
				match(SETVOLUME);
				setState(1008);
				match(T__3);
				setState(1009);
				expr(0);
				setState(1010);
				match(T__4);
				}
				break;
			case SAYTEXT:
				enterOuterAlt(_localctx, 17);
				{
				setState(1012);
				match(SAYTEXT);
				setState(1013);
				match(T__3);
				setState(1014);
				expr(0);
				setState(1020);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__7) {
					{
					setState(1015);
					match(T__7);
					setState(1016);
					expr(0);
					setState(1017);
					match(T__7);
					setState(1018);
					expr(0);
					}
				}

				setState(1022);
				match(T__4);
				}
				break;
			case LEDON:
				enterOuterAlt(_localctx, 18);
				{
				setState(1024);
				match(LEDON);
				setState(1025);
				match(T__3);
				setState(1026);
				match(NAME);
				setState(1027);
				match(T__7);
				setState(1028);
				match(NAME);
				setState(1029);
				match(T__4);
				}
				break;
			case LEDOFF:
				enterOuterAlt(_localctx, 19);
				{
				setState(1030);
				match(LEDOFF);
				setState(1031);
				match(T__3);
				setState(1032);
				match(T__4);
				}
				break;
			case RESETLED:
				enterOuterAlt(_localctx, 20);
				{
				setState(1033);
				match(RESETLED);
				setState(1034);
				match(T__3);
				setState(1035);
				match(T__4);
				}
				break;
			case SENDMESSAGE:
				enterOuterAlt(_localctx, 21);
				{
				setState(1036);
				match(SENDMESSAGE);
				setState(1037);
				match(T__3);
				setState(1038);
				expr(0);
				setState(1039);
				match(T__7);
				setState(1040);
				expr(0);
				setState(1041);
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
	public static class Ev3xNNContext extends ParserRuleContext {
		public TerminalNode NNSTEP() { return getToken(TextlyJavaParser.NNSTEP, 0); }
		public TerminalNode SETINPUTNEURON() { return getToken(TextlyJavaParser.SETINPUTNEURON, 0); }
		public List<TerminalNode> NAME() { return getTokens(TextlyJavaParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(TextlyJavaParser.NAME, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SETWEIGHT() { return getToken(TextlyJavaParser.SETWEIGHT, 0); }
		public TerminalNode SETBIAS() { return getToken(TextlyJavaParser.SETBIAS, 0); }
		public Ev3xNNContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ev3xNN; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).enterEv3xNN(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TextlyJavaListener ) ((TextlyJavaListener)listener).exitEv3xNN(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TextlyJavaVisitor ) return ((TextlyJavaVisitor<? extends T>)visitor).visitEv3xNN(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ev3xNNContext ev3xNN() throws RecognitionException {
		Ev3xNNContext _localctx = new Ev3xNNContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_ev3xNN);
		try {
			setState(1071);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NNSTEP:
				enterOuterAlt(_localctx, 1);
				{
				setState(1045);
				match(NNSTEP);
				setState(1046);
				match(T__3);
				setState(1047);
				match(T__4);
				}
				break;
			case SETINPUTNEURON:
				enterOuterAlt(_localctx, 2);
				{
				setState(1048);
				match(SETINPUTNEURON);
				setState(1049);
				match(T__3);
				setState(1050);
				match(NAME);
				setState(1051);
				match(T__7);
				setState(1052);
				expr(0);
				setState(1053);
				match(T__4);
				}
				break;
			case SETWEIGHT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1055);
				match(SETWEIGHT);
				setState(1056);
				match(T__3);
				setState(1057);
				match(NAME);
				setState(1058);
				match(T__7);
				setState(1059);
				match(NAME);
				setState(1060);
				match(T__7);
				setState(1061);
				expr(0);
				setState(1062);
				match(T__4);
				}
				break;
			case SETBIAS:
				enterOuterAlt(_localctx, 4);
				{
				setState(1064);
				match(SETBIAS);
				setState(1065);
				match(T__3);
				setState(1066);
				match(NAME);
				setState(1067);
				match(T__7);
				setState(1068);
				expr(0);
				setState(1069);
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
		"\u0004\u0001\u00cd\u0432\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0001\u0000\u0005\u0000@\b\u0000\n\u0000\f\u0000C\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0005\u0000G\b\u0000\n\u0000\f\u0000J\t\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003a\b\u0003\n\u0003\f\u0003d\t"+
		"\u0003\u0005\u0003f\b\u0003\n\u0003\f\u0003i\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003q\b"+
		"\u0003\u0003\u0003s\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005}\b"+
		"\u0005\n\u0005\f\u0005\u0080\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0005\u0006\u0087\b\u0006\n\u0006\f\u0006\u008a"+
		"\t\u0006\u0003\u0006\u008c\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0005\u0006\u0092\b\u0006\n\u0006\f\u0006\u0095\t\u0006\u0001"+
		"\u0006\u0001\u0006\u0005\u0006\u0099\b\u0006\n\u0006\f\u0006\u009c\t\u0006"+
		"\u0003\u0006\u009e\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u00b3\b\u0006\n\u0006"+
		"\f\u0006\u00b6\t\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u00bd\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006\u00d4\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u00fc\b\u0006"+
		"\n\u0006\f\u0006\u00ff\t\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u010e\b\u0006\u0001"+
		"\u0006\u0003\u0006\u0111\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003"+
		"\u0007\u0116\b\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u012c\b\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0005\t\u0158\b\t\n\t\f\t\u015b\t\t\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0176\b\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u017e\b\u000b\n\u000b\f\u000b\u0181\t\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u0185\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0005\u000b\u018c\b\u000b\n\u000b\f\u000b\u018f\t\u000b\u0001"+
		"\u000b\u0003\u000b\u0192\b\u000b\u0001\u000b\u0003\u000b\u0195\b\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0003"+
		"\r\u019f\b\r\u0001\r\u0001\r\u0001\r\u0005\r\u01a4\b\r\n\r\f\r\u01a7\t"+
		"\r\u0003\r\u01a9\b\r\u0003\r\u01ab\b\r\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0005\r\u01b3\b\r\n\r\f\r\u01b6\t\r\u0003\r\u01b8\b\r"+
		"\u0001\r\u0003\r\u01bb\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e"+
		"\u01c0\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0003\u0010\u0209\b\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0003\u0011\u0211\b\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u021c\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u027c\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u0297\b\u0015"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0003\u0016\u029f\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0003\u0018\u02b1\b\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0003\u0018\u02d0\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u0368\b\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0373\b\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0003\u001c\u0385\b\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0003\u001d\u03ad\b\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u03cd\b\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u03fd\b\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0003\u001d\u0414\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e"+
		"\u0430\b\u001e\u0001\u001e\u0001\u017f\u0001\u0012\u001f\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \""+
		"$&(*,.02468:<\u0000\u0007\u0001\u0000\u00cc\u00cd\u0001\u000045\u0001"+
		"\u0000\u00ca\u00cb\u0002\u0000\u00bb\u00bb\u00bd\u00bd\u0001\u0000\u0019"+
		"\u001c\u0001\u0000\u001f \u0001\u0000\u0019\u001a\u04c1\u0000A\u0001\u0000"+
		"\u0000\u0000\u0002M\u0001\u0000\u0000\u0000\u0004R\u0001\u0000\u0000\u0000"+
		"\u0006Z\u0001\u0000\u0000\u0000\bv\u0001\u0000\u0000\u0000\n~\u0001\u0000"+
		"\u0000\u0000\f\u0110\u0001\u0000\u0000\u0000\u000e\u0115\u0001\u0000\u0000"+
		"\u0000\u0010\u0117\u0001\u0000\u0000\u0000\u0012\u012b\u0001\u0000\u0000"+
		"\u0000\u0014\u0175\u0001\u0000\u0000\u0000\u0016\u0194\u0001\u0000\u0000"+
		"\u0000\u0018\u0196\u0001\u0000\u0000\u0000\u001a\u01ba\u0001\u0000\u0000"+
		"\u0000\u001c\u01bf\u0001\u0000\u0000\u0000\u001e\u01c1\u0001\u0000\u0000"+
		"\u0000 \u0208\u0001\u0000\u0000\u0000\"\u0210\u0001\u0000\u0000\u0000"+
		"$\u021b\u0001\u0000\u0000\u0000&\u027b\u0001\u0000\u0000\u0000(\u027d"+
		"\u0001\u0000\u0000\u0000*\u0296\u0001\u0000\u0000\u0000,\u029e\u0001\u0000"+
		"\u0000\u0000.\u02a0\u0001\u0000\u0000\u00000\u02cf\u0001\u0000\u0000\u0000"+
		"2\u02d1\u0001\u0000\u0000\u00004\u0367\u0001\u0000\u0000\u00006\u0372"+
		"\u0001\u0000\u0000\u00008\u0384\u0001\u0000\u0000\u0000:\u0413\u0001\u0000"+
		"\u0000\u0000<\u042f\u0001\u0000\u0000\u0000>@\u0003\u0002\u0001\u0000"+
		"?>\u0001\u0000\u0000\u0000@C\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000"+
		"\u0000AB\u0001\u0000\u0000\u0000BD\u0001\u0000\u0000\u0000CA\u0001\u0000"+
		"\u0000\u0000DH\u0003\u0004\u0002\u0000EG\u0003\u0006\u0003\u0000FE\u0001"+
		"\u0000\u0000\u0000GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000"+
		"HI\u0001\u0000\u0000\u0000IK\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000"+
		"\u0000KL\u0005\u0000\u0000\u0001L\u0001\u0001\u0000\u0000\u0000MN\u0003"+
		"\b\u0004\u0000NO\u0005\u00be\u0000\u0000OP\u0003\u0012\t\u0000PQ\u0005"+
		"\u0001\u0000\u0000Q\u0003\u0001\u0000\u0000\u0000RS\u0005\u0002\u0000"+
		"\u0000ST\u0005\u0003\u0000\u0000TU\u0005\u0004\u0000\u0000UV\u0005\u0005"+
		"\u0000\u0000VW\u0005\u0006\u0000\u0000WX\u0003\n\u0005\u0000XY\u0005\u0007"+
		"\u0000\u0000Y\u0005\u0001\u0000\u0000\u0000Z[\u0005:\u0000\u0000[\\\u0005"+
		"\u00bb\u0000\u0000\\g\u0005\u0004\u0000\u0000]b\u0003\u0012\t\u0000^_"+
		"\u0005\b\u0000\u0000_a\u0003\u0012\t\u0000`^\u0001\u0000\u0000\u0000a"+
		"d\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000"+
		"\u0000cf\u0001\u0000\u0000\u0000db\u0001\u0000\u0000\u0000e]\u0001\u0000"+
		"\u0000\u0000fi\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001"+
		"\u0000\u0000\u0000hj\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000"+
		"jk\u0005\u0005\u0000\u0000kl\u0005\u0006\u0000\u0000lr\u0003\n\u0005\u0000"+
		"mp\u00059\u0000\u0000nq\u0005\u00bb\u0000\u0000oq\u0003\u0012\t\u0000"+
		"pn\u0001\u0000\u0000\u0000po\u0001\u0000\u0000\u0000qs\u0001\u0000\u0000"+
		"\u0000rm\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000st\u0001\u0000"+
		"\u0000\u0000tu\u0005\u0007\u0000\u0000u\u0007\u0001\u0000\u0000\u0000"+
		"vw\u0005:\u0000\u0000wx\u0005\u00bb\u0000\u0000x\t\u0001\u0000\u0000\u0000"+
		"yz\u0003\f\u0006\u0000z{\u0005\u0001\u0000\u0000{}\u0001\u0000\u0000\u0000"+
		"|y\u0001\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000"+
		"\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u000b\u0001\u0000\u0000"+
		"\u0000\u0080~\u0001\u0000\u0000\u0000\u0081\u0082\u0005>\u0000\u0000\u0082"+
		"\u008b\u0005\u0004\u0000\u0000\u0083\u0088\u0003\u0012\t\u0000\u0084\u0085"+
		"\u0005\b\u0000\u0000\u0085\u0087\u0003\u0012\t\u0000\u0086\u0084\u0001"+
		"\u0000\u0000\u0000\u0087\u008a\u0001\u0000\u0000\u0000\u0088\u0086\u0001"+
		"\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008c\u0001"+
		"\u0000\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008b\u0083\u0001"+
		"\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001"+
		"\u0000\u0000\u0000\u008d\u0111\u0005\u0005\u0000\u0000\u008e\u008f\u0005"+
		"\u00bb\u0000\u0000\u008f\u009d\u0005\u0004\u0000\u0000\u0090\u0092\u0003"+
		"\u0012\t\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092\u0095\u0001\u0000"+
		"\u0000\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000"+
		"\u0000\u0000\u0094\u009a\u0001\u0000\u0000\u0000\u0095\u0093\u0001\u0000"+
		"\u0000\u0000\u0096\u0097\u0005\b\u0000\u0000\u0097\u0099\u0003\u0012\t"+
		"\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0099\u009c\u0001\u0000\u0000"+
		"\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000"+
		"\u0000\u009b\u009e\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000"+
		"\u0000\u009d\u0093\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000"+
		"\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f\u0111\u0005\u0005\u0000"+
		"\u0000\u00a0\u00a1\u0005\u00bb\u0000\u0000\u00a1\u00a2\u0005\u00be\u0000"+
		"\u0000\u00a2\u0111\u0003\u0012\t\u0000\u00a3\u00a4\u0005-\u0000\u0000"+
		"\u00a4\u00a5\u0005\u0004\u0000\u0000\u00a5\u00a6\u0003\u0012\t\u0000\u00a6"+
		"\u00a7\u0005\u0005\u0000\u0000\u00a7\u00a8\u0005\u0006\u0000\u0000\u00a8"+
		"\u00a9\u0003\n\u0005\u0000\u00a9\u00b4\u0005\u0007\u0000\u0000\u00aa\u00ab"+
		"\u0005.\u0000\u0000\u00ab\u00ac\u0005\u0004\u0000\u0000\u00ac\u00ad\u0003"+
		"\u0012\t\u0000\u00ad\u00ae\u0005\u0005\u0000\u0000\u00ae\u00af\u0005\u0006"+
		"\u0000\u0000\u00af\u00b0\u0003\n\u0005\u0000\u00b0\u00b1\u0005\u0007\u0000"+
		"\u0000\u00b1\u00b3\u0001\u0000\u0000\u0000\u00b2\u00aa\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b6\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000"+
		"\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00bc\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005/\u0000\u0000"+
		"\u00b8\u00b9\u0005\u0006\u0000\u0000\u00b9\u00ba\u0003\n\u0005\u0000\u00ba"+
		"\u00bb\u0005\u0007\u0000\u0000\u00bb\u00bd\u0001\u0000\u0000\u0000\u00bc"+
		"\u00b7\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd"+
		"\u0111\u0001\u0000\u0000\u0000\u00be\u00bf\u00052\u0000\u0000\u00bf\u00c0"+
		"\u0005\u0004\u0000\u0000\u00c0\u00c1\u0003\b\u0004\u0000\u00c1\u00c2\u0005"+
		"\u00be\u0000\u0000\u00c2\u00c3\u0003\u0012\t\u0000\u00c3\u00c4\u0005\u0001"+
		"\u0000\u0000\u00c4\u00c5\u0005\u00bb\u0000\u0000\u00c5\u00c6\u0005\u00c5"+
		"\u0000\u0000\u00c6\u00c7\u0003\u0012\t\u0000\u00c7\u00d3\u0005\u0001\u0000"+
		"\u0000\u00c8\u00c9\u0003\u0012\t\u0000\u00c9\u00ca\u00050\u0000\u0000"+
		"\u00ca\u00d4\u0001\u0000\u0000\u0000\u00cb\u00cc\u0005\u00bb\u0000\u0000"+
		"\u00cc\u00cd\u0005\u00be\u0000\u0000\u00cd\u00ce\u0005\u00bb\u0000\u0000"+
		"\u00ce\u00cf\u0007\u0000\u0000\u0000\u00cf\u00d4\u0003\u0012\t\u0000\u00d0"+
		"\u00d1\u0005\u00bb\u0000\u0000\u00d1\u00d2\u0005\u00be\u0000\u0000\u00d2"+
		"\u00d4\u0003\u0012\t\u0000\u00d3\u00c8\u0001\u0000\u0000\u0000\u00d3\u00cb"+
		"\u0001\u0000\u0000\u0000\u00d3\u00d0\u0001\u0000\u0000\u0000\u00d3\u00d4"+
		"\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d6"+
		"\u0005\u0005\u0000\u0000\u00d6\u00d7\u0005\u0006\u0000\u0000\u00d7\u00d8"+
		"\u0003\n\u0005\u0000\u00d8\u00d9\u0005\u0007\u0000\u0000\u00d9\u0111\u0001"+
		"\u0000\u0000\u0000\u00da\u00db\u00051\u0000\u0000\u00db\u00dc\u0005\u0004"+
		"\u0000\u0000\u00dc\u00dd\u0003\u0012\t\u0000\u00dd\u00de\u0005\u0005\u0000"+
		"\u0000\u00de\u00df\u0005\u0006\u0000\u0000\u00df\u00e0\u0003\n\u0005\u0000"+
		"\u00e0\u00e1\u0005\u0007\u0000\u0000\u00e1\u0111\u0001\u0000\u0000\u0000"+
		"\u00e2\u00e3\u00053\u0000\u0000\u00e3\u00e4\u0005\u0004\u0000\u0000\u00e4"+
		"\u00e5\u0003\b\u0004\u0000\u00e5\u00e6\u0005\t\u0000\u0000\u00e6\u00e7"+
		"\u0003\u0012\t\u0000\u00e7\u00e8\u0005\u0005\u0000\u0000\u00e8\u00e9\u0005"+
		"\u0006\u0000\u0000\u00e9\u00ea\u0003\n\u0005\u0000\u00ea\u00eb\u0005\u0007"+
		"\u0000\u0000\u00eb\u0111\u0001\u0000\u0000\u0000\u00ec\u00ed\u00056\u0000"+
		"\u0000\u00ed\u00ee\u0005\u0004\u0000\u0000\u00ee\u00ef\u0003\u0012\t\u0000"+
		"\u00ef\u00f0\u0005\u0005\u0000\u0000\u00f0\u00f1\u0005\u0006\u0000\u0000"+
		"\u00f1\u00f2\u0003\n\u0005\u0000\u00f2\u00fd\u0005\u0007\u0000\u0000\u00f3"+
		"\u00f4\u00057\u0000\u0000\u00f4\u00f5\u0005\u0004\u0000\u0000\u00f5\u00f6"+
		"\u0003\u0012\t\u0000\u00f6\u00f7\u0005\u0005\u0000\u0000\u00f7\u00f8\u0005"+
		"\u0006\u0000\u0000\u00f8\u00f9\u0003\n\u0005\u0000\u00f9\u00fa\u0005\u0007"+
		"\u0000\u0000\u00fa\u00fc\u0001\u0000\u0000\u0000\u00fb\u00f3\u0001\u0000"+
		"\u0000\u0000\u00fc\u00ff\u0001\u0000\u0000\u0000\u00fd\u00fb\u0001\u0000"+
		"\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000\u00fe\u0111\u0001\u0000"+
		"\u0000\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u0100\u0111\u0007\u0001"+
		"\u0000\u0000\u0101\u0102\u00058\u0000\u0000\u0102\u0103\u0005\u0004\u0000"+
		"\u0000\u0103\u0104\u0003\u0012\t\u0000\u0104\u0105\u0005\u0005\u0000\u0000"+
		"\u0105\u0111\u0001\u0000\u0000\u0000\u0106\u0107\u0005-\u0000\u0000\u0107"+
		"\u0108\u0005\u0004\u0000\u0000\u0108\u0109\u0003\u0012\t\u0000\u0109\u010a"+
		"\u0005\u0005\u0000\u0000\u010a\u010d\u00059\u0000\u0000\u010b\u010e\u0005"+
		"\u00bb\u0000\u0000\u010c\u010e\u0003\u0012\t\u0000\u010d\u010b\u0001\u0000"+
		"\u0000\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010e\u0111\u0001\u0000"+
		"\u0000\u0000\u010f\u0111\u0003\u000e\u0007\u0000\u0110\u0081\u0001\u0000"+
		"\u0000\u0000\u0110\u008e\u0001\u0000\u0000\u0000\u0110\u00a0\u0001\u0000"+
		"\u0000\u0000\u0110\u00a3\u0001\u0000\u0000\u0000\u0110\u00be\u0001\u0000"+
		"\u0000\u0000\u0110\u00da\u0001\u0000\u0000\u0000\u0110\u00e2\u0001\u0000"+
		"\u0000\u0000\u0110\u00ec\u0001\u0000\u0000\u0000\u0110\u0100\u0001\u0000"+
		"\u0000\u0000\u0110\u0101\u0001\u0000\u0000\u0000\u0110\u0106\u0001\u0000"+
		"\u0000\u0000\u0110\u010f\u0001\u0000\u0000\u0000\u0111\r\u0001\u0000\u0000"+
		"\u0000\u0112\u0116\u0003\"\u0011\u0000\u0113\u0116\u0003,\u0016\u0000"+
		"\u0114\u0116\u00036\u001b\u0000\u0115\u0112\u0001\u0000\u0000\u0000\u0115"+
		"\u0113\u0001\u0000\u0000\u0000\u0115\u0114\u0001\u0000\u0000\u0000\u0116"+
		"\u000f\u0001\u0000\u0000\u0000\u0117\u0118\u0003\u0012\t\u0000\u0118\u0119"+
		"\u0005\u0000\u0000\u0001\u0119\u0011\u0001\u0000\u0000\u0000\u011a\u011b"+
		"\u0006\t\uffff\uffff\u0000\u011b\u012c\u0005@\u0000\u0000\u011c\u012c"+
		"\u0005?\u0000\u0000\u011d\u012c\u0005\u00bb\u0000\u0000\u011e\u012c\u0003"+
		"\u0016\u000b\u0000\u011f\u012c\u0003\u0018\f\u0000\u0120\u012c\u0003\u001a"+
		"\r\u0000\u0121\u0122\u0005\u0004\u0000\u0000\u0122\u0123\u0003\u0012\t"+
		"\u0000\u0123\u0124\u0005\u0005\u0000\u0000\u0124\u012c\u0001\u0000\u0000"+
		"\u0000\u0125\u0126\u0007\u0000\u0000\u0000\u0126\u012c\u0003\u0012\t\u0011"+
		"\u0127\u0128\u0005\u00c1\u0000\u0000\u0128\u012c\u0003\u0012\t\u0010\u0129"+
		"\u012c\u0003\u0014\n\u0000\u012a\u012c\u0003\u001c\u000e\u0000\u012b\u011a"+
		"\u0001\u0000\u0000\u0000\u012b\u011c\u0001\u0000\u0000\u0000\u012b\u011d"+
		"\u0001\u0000\u0000\u0000\u012b\u011e\u0001\u0000\u0000\u0000\u012b\u011f"+
		"\u0001\u0000\u0000\u0000\u012b\u0120\u0001\u0000\u0000\u0000\u012b\u0121"+
		"\u0001\u0000\u0000\u0000\u012b\u0125\u0001\u0000\u0000\u0000\u012b\u0127"+
		"\u0001\u0000\u0000\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012b\u012a"+
		"\u0001\u0000\u0000\u0000\u012c\u0159\u0001\u0000\u0000\u0000\u012d\u012e"+
		"\n\u000f\u0000\u0000\u012e\u012f\u0005\u00c9\u0000\u0000\u012f\u0158\u0003"+
		"\u0012\t\u000f\u0130\u0131\n\u000e\u0000\u0000\u0131\u0132\u0005\u00c8"+
		"\u0000\u0000\u0132\u0158\u0003\u0012\t\u000e\u0133\u0134\n\r\u0000\u0000"+
		"\u0134\u0135\u0007\u0002\u0000\u0000\u0135\u0158\u0003\u0012\t\u000e\u0136"+
		"\u0137\n\f\u0000\u0000\u0137\u0138\u0007\u0000\u0000\u0000\u0138\u0158"+
		"\u0003\u0012\t\r\u0139\u013a\n\u000b\u0000\u0000\u013a\u013b\u0005\u00bf"+
		"\u0000\u0000\u013b\u0158\u0003\u0012\t\f\u013c\u013d\n\n\u0000\u0000\u013d"+
		"\u013e\u0005\u00c0\u0000\u0000\u013e\u0158\u0003\u0012\t\u000b\u013f\u0140"+
		"\n\t\u0000\u0000\u0140\u0141\u0005\u00c2\u0000\u0000\u0141\u0158\u0003"+
		"\u0012\t\n\u0142\u0143\n\b\u0000\u0000\u0143\u0144\u0005\u00c3\u0000\u0000"+
		"\u0144\u0158\u0003\u0012\t\t\u0145\u0146\n\u0007\u0000\u0000\u0146\u0147"+
		"\u0005\u00c4\u0000\u0000\u0147\u0158\u0003\u0012\t\b\u0148\u0149\n\u0006"+
		"\u0000\u0000\u0149\u014a\u0005\u00c5\u0000\u0000\u014a\u0158\u0003\u0012"+
		"\t\u0007\u014b\u014c\n\u0005\u0000\u0000\u014c\u014d\u0005\u00c6\u0000"+
		"\u0000\u014d\u0158\u0003\u0012\t\u0006\u014e\u014f\n\u0004\u0000\u0000"+
		"\u014f\u0150\u0005\u00c7\u0000\u0000\u0150\u0158\u0003\u0012\t\u0005\u0151"+
		"\u0152\n\u0003\u0000\u0000\u0152\u0153\u0005\n\u0000\u0000\u0153\u0154"+
		"\u0003\u0012\t\u0000\u0154\u0155\u0005\t\u0000\u0000\u0155\u0156\u0003"+
		"\u0012\t\u0004\u0156\u0158\u0001\u0000\u0000\u0000\u0157\u012d\u0001\u0000"+
		"\u0000\u0000\u0157\u0130\u0001\u0000\u0000\u0000\u0157\u0133\u0001\u0000"+
		"\u0000\u0000\u0157\u0136\u0001\u0000\u0000\u0000\u0157\u0139\u0001\u0000"+
		"\u0000\u0000\u0157\u013c\u0001\u0000\u0000\u0000\u0157\u013f\u0001\u0000"+
		"\u0000\u0000\u0157\u0142\u0001\u0000\u0000\u0000\u0157\u0145\u0001\u0000"+
		"\u0000\u0000\u0157\u0148\u0001\u0000\u0000\u0000\u0157\u014b\u0001\u0000"+
		"\u0000\u0000\u0157\u014e\u0001\u0000\u0000\u0000\u0157\u0151\u0001\u0000"+
		"\u0000\u0000\u0158\u015b\u0001\u0000\u0000\u0000\u0159\u0157\u0001\u0000"+
		"\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u0013\u0001\u0000"+
		"\u0000\u0000\u015b\u0159\u0001\u0000\u0000\u0000\u015c\u015d\u0005\u000b"+
		"\u0000\u0000\u015d\u015e\u0005\u0004\u0000\u0000\u015e\u015f\u0005\u00bb"+
		"\u0000\u0000\u015f\u0176\u0005\u0005\u0000\u0000\u0160\u0161\u0005\u000b"+
		"\u0000\u0000\u0161\u0162\u0005\f\u0000\u0000\u0162\u0163\u0005\r\u0000"+
		"\u0000\u0163\u0164\u0005\u0004\u0000\u0000\u0164\u0165\u0005E\u0000\u0000"+
		"\u0165\u0176\u0005\u0005\u0000\u0000\u0166\u0167\u0005\u000b\u0000\u0000"+
		"\u0167\u0168\u0005\f\u0000\u0000\u0168\u0169\u0005\u000e\u0000\u0000\u0169"+
		"\u016a\u0005\u0004\u0000\u0000\u016a\u016b\u0005\u00bb\u0000\u0000\u016b"+
		"\u016c\u0005\b\u0000\u0000\u016c\u016d\u0003\u0012\t\u0000\u016d\u016e"+
		"\u0005\b\u0000\u0000\u016e\u016f\u0003\u0012\t\u0000\u016f\u0170\u0005"+
		"\u0005\u0000\u0000\u0170\u0176\u0001\u0000\u0000\u0000\u0171\u0172\u0005"+
		"\u000b\u0000\u0000\u0172\u0173\u0005\f\u0000\u0000\u0173\u0174\u0005\u000f"+
		"\u0000\u0000\u0174\u0176\u0003\u0012\t\u0000\u0175\u015c\u0001\u0000\u0000"+
		"\u0000\u0175\u0160\u0001\u0000\u0000\u0000\u0175\u0166\u0001\u0000\u0000"+
		"\u0000\u0175\u0171\u0001\u0000\u0000\u0000\u0176\u0015\u0001\u0000\u0000"+
		"\u0000\u0177\u0195\u0005C\u0000\u0000\u0178\u0195\u0005A\u0000\u0000\u0179"+
		"\u0195\u0005B\u0000\u0000\u017a\u0195\u0005D\u0000\u0000\u017b\u0184\u0005"+
		"\u0010\u0000\u0000\u017c\u017e\t\u0000\u0000\u0000\u017d\u017c\u0001\u0000"+
		"\u0000\u0000\u017e\u0181\u0001\u0000\u0000\u0000\u017f\u0180\u0001\u0000"+
		"\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u0180\u0185\u0001\u0000"+
		"\u0000\u0000\u0181\u017f\u0001\u0000\u0000\u0000\u0182\u0185\u0005\f\u0000"+
		"\u0000\u0183\u0185\u0005\n\u0000\u0000\u0184\u017f\u0001\u0000\u0000\u0000"+
		"\u0184\u0182\u0001\u0000\u0000\u0000\u0184\u0183\u0001\u0000\u0000\u0000"+
		"\u0185\u0186\u0001\u0000\u0000\u0000\u0186\u0195\u0005\u0010\u0000\u0000"+
		"\u0187\u018d\u0005\u0011\u0000\u0000\u0188\u0189\u0003\u0012\t\u0000\u0189"+
		"\u018a\u0005\b\u0000\u0000\u018a\u018c\u0001\u0000\u0000\u0000\u018b\u0188"+
		"\u0001\u0000\u0000\u0000\u018c\u018f\u0001\u0000\u0000\u0000\u018d\u018b"+
		"\u0001\u0000\u0000\u0000\u018d\u018e\u0001\u0000\u0000\u0000\u018e\u0191"+
		"\u0001\u0000\u0000\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u0190\u0192"+
		"\u0003\u0012\t\u0000\u0191\u0190\u0001\u0000\u0000\u0000\u0191\u0192\u0001"+
		"\u0000\u0000\u0000\u0192\u0193\u0001\u0000\u0000\u0000\u0193\u0195\u0005"+
		"\u0012\u0000\u0000\u0194\u0177\u0001\u0000\u0000\u0000\u0194\u0178\u0001"+
		"\u0000\u0000\u0000\u0194\u0179\u0001\u0000\u0000\u0000\u0194\u017a\u0001"+
		"\u0000\u0000\u0000\u0194\u017b\u0001\u0000\u0000\u0000\u0194\u0187\u0001"+
		"\u0000\u0000\u0000\u0195\u0017\u0001\u0000\u0000\u0000\u0196\u0197\u0005"+
		"\u0013\u0000\u0000\u0197\u0198\u0007\u0003\u0000\u0000\u0198\u0199\u0005"+
		"\b\u0000\u0000\u0199\u019a\u0007\u0003\u0000\u0000\u019a\u0019\u0001\u0000"+
		"\u0000\u0000\u019b\u019c\u0005=\u0000\u0000\u019c\u01aa\u0005\u0004\u0000"+
		"\u0000\u019d\u019f\u0005:\u0000\u0000\u019e\u019d\u0001\u0000\u0000\u0000"+
		"\u019e\u019f\u0001\u0000\u0000\u0000\u019f\u01ab\u0001\u0000\u0000\u0000"+
		"\u01a0\u01a5\u0003\u0012\t\u0000\u01a1\u01a2\u0005\b\u0000\u0000\u01a2"+
		"\u01a4\u0003\u0012\t\u0000\u01a3\u01a1\u0001\u0000\u0000\u0000\u01a4\u01a7"+
		"\u0001\u0000\u0000\u0000\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a5\u01a6"+
		"\u0001\u0000\u0000\u0000\u01a6\u01a9\u0001\u0000\u0000\u0000\u01a7\u01a5"+
		"\u0001\u0000\u0000\u0000\u01a8\u01a0\u0001\u0000\u0000\u0000\u01a8\u01a9"+
		"\u0001\u0000\u0000\u0000\u01a9\u01ab\u0001\u0000\u0000\u0000\u01aa\u019e"+
		"\u0001\u0000\u0000\u0000\u01aa\u01a8\u0001\u0000\u0000\u0000\u01ab\u01ac"+
		"\u0001\u0000\u0000\u0000\u01ac\u01bb\u0005\u0005\u0000\u0000\u01ad\u01ae"+
		"\u0005\u00bb\u0000\u0000\u01ae\u01b7\u0005\u0004\u0000\u0000\u01af\u01b4"+
		"\u0003\u0012\t\u0000\u01b0\u01b1\u0005\b\u0000\u0000\u01b1\u01b3\u0003"+
		"\u0012\t\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b3\u01b6\u0001\u0000"+
		"\u0000\u0000\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b5\u0001\u0000"+
		"\u0000\u0000\u01b5\u01b8\u0001\u0000\u0000\u0000\u01b6\u01b4\u0001\u0000"+
		"\u0000\u0000\u01b7\u01af\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000"+
		"\u0000\u0000\u01b8\u01b9\u0001\u0000\u0000\u0000\u01b9\u01bb\u0005\u0005"+
		"\u0000\u0000\u01ba\u019b\u0001\u0000\u0000\u0000\u01ba\u01ad\u0001\u0000"+
		"\u0000\u0000\u01bb\u001b\u0001\u0000\u0000\u0000\u01bc\u01c0\u0003\u001e"+
		"\u000f\u0000\u01bd\u01c0\u0003(\u0014\u0000\u01be\u01c0\u00032\u0019\u0000"+
		"\u01bf\u01bc\u0001\u0000\u0000\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000"+
		"\u01bf\u01be\u0001\u0000\u0000\u0000\u01c0\u001d\u0001\u0000\u0000\u0000"+
		"\u01c1\u01c2\u0005\u0014\u0000\u0000\u01c2\u01c3\u0005\f\u0000\u0000\u01c3"+
		"\u01c4\u0003 \u0010\u0000\u01c4\u001f\u0001\u0000\u0000\u0000\u01c5\u01c6"+
		"\u0005H\u0000\u0000\u01c6\u01c7\u0005\u0004\u0000\u0000\u01c7\u01c8\u0005"+
		"\u00bb\u0000\u0000\u01c8\u0209\u0005\u0005\u0000\u0000\u01c9\u01ca\u0005"+
		"y\u0000\u0000\u01ca\u01cb\u0005\f\u0000\u0000\u01cb\u01cc\u0005\u0015"+
		"\u0000\u0000\u01cc\u01cd\u0005\u0004\u0000\u0000\u01cd\u0209\u0005\u0005"+
		"\u0000\u0000\u01ce\u01cf\u0005K\u0000\u0000\u01cf\u01d0\u0005\f\u0000"+
		"\u0000\u01d0\u01d1\u0005\u0016\u0000\u0000\u01d1\u01d2\u0005\u0004\u0000"+
		"\u0000\u01d2\u0209\u0005\u0005\u0000\u0000\u01d3\u01d4\u0005R\u0000\u0000"+
		"\u01d4\u01d5\u0005\f\u0000\u0000\u01d5\u01d6\u0005\u0017\u0000\u0000\u01d6"+
		"\u01d7\u0005\u0004\u0000\u0000\u01d7\u01d8\u0005\u00bb\u0000\u0000\u01d8"+
		"\u0209\u0005\u0005\u0000\u0000\u01d9\u01da\u0005\\\u0000\u0000\u01da\u01db"+
		"\u0005\f\u0000\u0000\u01db\u01dc\u0005\u0015\u0000\u0000\u01dc\u01dd\u0005"+
		"\u0004\u0000\u0000\u01dd\u01de\u0005\u00bb\u0000\u0000\u01de\u0209\u0005"+
		"\u0005\u0000\u0000\u01df\u01e0\u0005]\u0000\u0000\u01e0\u01e1\u0005\f"+
		"\u0000\u0000\u01e1\u01e2\u0005\u0018\u0000\u0000\u01e2\u01e3\u0005\u0004"+
		"\u0000\u0000\u01e3\u0209\u0005\u0005\u0000\u0000\u01e4\u01e5\u0005a\u0000"+
		"\u0000\u01e5\u01e6\u0005\u0004\u0000\u0000\u01e6\u01e7\u0005\u00bb\u0000"+
		"\u0000\u01e7\u01e8\u0005\b\u0000\u0000\u01e8\u01e9\u0007\u0004\u0000\u0000"+
		"\u01e9\u0209\u0005\u0005\u0000\u0000\u01ea\u01eb\u0005b\u0000\u0000\u01eb"+
		"\u01ec\u0005\f\u0000\u0000\u01ec\u01ed\u0005\u0015\u0000\u0000\u01ed\u01ee"+
		"\u0005\u0004\u0000\u0000\u01ee\u01ef\u0005A\u0000\u0000\u01ef\u0209\u0005"+
		"\u0005\u0000\u0000\u01f0\u01f1\u0005e\u0000\u0000\u01f1\u01f2\u0005\f"+
		"\u0000\u0000\u01f2\u01f3\u0005\u001d\u0000\u0000\u01f3\u01f4\u0005\f\u0000"+
		"\u0000\u01f4\u01f5\u0005\u001e\u0000\u0000\u01f5\u01f6\u0005\u0004\u0000"+
		"\u0000\u01f6\u0209\u0005\u0005\u0000\u0000\u01f7\u01f8\u0005f\u0000\u0000"+
		"\u01f8\u01f9\u0005\u0004\u0000\u0000\u01f9\u0209\u0005\u0005\u0000\u0000"+
		"\u01fa\u01fb\u0005h\u0000\u0000\u01fb\u01fc\u0005\u0004\u0000\u0000\u01fc"+
		"\u0209\u0005\u0005\u0000\u0000\u01fd\u01fe\u0005\u0087\u0000\u0000\u01fe"+
		"\u01ff\u0005\u0004\u0000\u0000\u01ff\u0200\u0003\u0012\t\u0000\u0200\u0201"+
		"\u0005\b\u0000\u0000\u0201\u0202\u0003\u0012\t\u0000\u0202\u0203\u0005"+
		"\u0005\u0000\u0000\u0203\u0209\u0001\u0000\u0000\u0000\u0204\u0205\u0005"+
		"\u009d\u0000\u0000\u0205\u0206\u0005\u0004\u0000\u0000\u0206\u0207\u0005"+
		":\u0000\u0000\u0207\u0209\u0005\u0005\u0000\u0000\u0208\u01c5\u0001\u0000"+
		"\u0000\u0000\u0208\u01c9\u0001\u0000\u0000\u0000\u0208\u01ce\u0001\u0000"+
		"\u0000\u0000\u0208\u01d3\u0001\u0000\u0000\u0000\u0208\u01d9\u0001\u0000"+
		"\u0000\u0000\u0208\u01df\u0001\u0000\u0000\u0000\u0208\u01e4\u0001\u0000"+
		"\u0000\u0000\u0208\u01ea\u0001\u0000\u0000\u0000\u0208\u01f0\u0001\u0000"+
		"\u0000\u0000\u0208\u01f7\u0001\u0000\u0000\u0000\u0208\u01fa\u0001\u0000"+
		"\u0000\u0000\u0208\u01fd\u0001\u0000\u0000\u0000\u0208\u0204\u0001\u0000"+
		"\u0000\u0000\u0209!\u0001\u0000\u0000\u0000\u020a\u020b\u0005\u0014\u0000"+
		"\u0000\u020b\u020c\u0005\f\u0000\u0000\u020c\u0211\u0003$\u0012\u0000"+
		"\u020d\u020e\u0005\u0014\u0000\u0000\u020e\u020f\u0005\f\u0000\u0000\u020f"+
		"\u0211\u0003&\u0013\u0000\u0210\u020a\u0001\u0000\u0000\u0000\u0210\u020d"+
		"\u0001\u0000\u0000\u0000\u0211#\u0001\u0000\u0000\u0000\u0212\u0213\u0005"+
		"\u007f\u0000\u0000\u0213\u0214\u0005\u0004\u0000\u0000\u0214\u0215\u0005"+
		"A\u0000\u0000\u0215\u0216\u0005\b\u0000\u0000\u0216\u0217\u0007\u0005"+
		"\u0000\u0000\u0217\u021c\u0005\u0005\u0000\u0000\u0218\u0219\u0005g\u0000"+
		"\u0000\u0219\u021a\u0005\u0004\u0000\u0000\u021a\u021c\u0005\u0005\u0000"+
		"\u0000\u021b\u0212\u0001\u0000\u0000\u0000\u021b\u0218\u0001\u0000\u0000"+
		"\u0000\u021c%\u0001\u0000\u0000\u0000\u021d\u021e\u0005\u0088\u0000\u0000"+
		"\u021e\u021f\u0005\u0004\u0000\u0000\u021f\u0220\u0003\u0012\t\u0000\u0220"+
		"\u0221\u0005\u0005\u0000\u0000\u0221\u027c\u0001\u0000\u0000\u0000\u0222"+
		"\u0223\u0005\u0089\u0000\u0000\u0223\u0224\u0005\u0004\u0000\u0000\u0224"+
		"\u0225\u0003\u0012\t\u0000\u0225\u0226\u0005\u0005\u0000\u0000\u0226\u027c"+
		"\u0001\u0000\u0000\u0000\u0227\u0228\u0005\u008a\u0000\u0000\u0228\u0229"+
		"\u0005\u0004\u0000\u0000\u0229\u022a\u0003\u0012\t\u0000\u022a\u022b\u0005"+
		"\u0005\u0000\u0000\u022b\u027c\u0001\u0000\u0000\u0000\u022c\u022d\u0005"+
		"\u008b\u0000\u0000\u022d\u022e\u0005\u0004\u0000\u0000\u022e\u027c\u0005"+
		"\u0005\u0000\u0000\u022f\u0230\u0005\u008c\u0000\u0000\u0230\u0231\u0005"+
		"\u0004\u0000\u0000\u0231\u0232\u0003\u0012\t\u0000\u0232\u0233\u0005\b"+
		"\u0000\u0000\u0233\u0234\u0003\u0012\t\u0000\u0234\u0235\u0005\b\u0000"+
		"\u0000\u0235\u0236\u0003\u0012\t\u0000\u0236\u0237\u0005\u0005\u0000\u0000"+
		"\u0237\u027c\u0001\u0000\u0000\u0000\u0238\u0239\u0005\u008d\u0000\u0000"+
		"\u0239\u023a\u0005\u0004\u0000\u0000\u023a\u023b\u0003\u0012\t\u0000\u023b"+
		"\u023c\u0005\u0005\u0000\u0000\u023c\u027c\u0001\u0000\u0000\u0000\u023d"+
		"\u023e\u0005\u008e\u0000\u0000\u023e\u023f\u0005\u0004\u0000\u0000\u023f"+
		"\u0240\u0003\u0012\t\u0000\u0240\u0241\u0005\b\u0000\u0000\u0241\u0242"+
		"\u0003\u0012\t\u0000\u0242\u0243\u0005\u0005\u0000\u0000\u0243\u027c\u0001"+
		"\u0000\u0000\u0000\u0244\u0245\u0005\u008f\u0000\u0000\u0245\u0246\u0005"+
		"\u0004\u0000\u0000\u0246\u0247\u0005\u00bb\u0000\u0000\u0247\u0248\u0005"+
		"\b\u0000\u0000\u0248\u0249\u0005\u00bb\u0000\u0000\u0249\u027c\u0005\u0005"+
		"\u0000\u0000\u024a\u024b\u0005\u0090\u0000\u0000\u024b\u024c\u0005\u0004"+
		"\u0000\u0000\u024c\u024d\u0005\u00bb\u0000\u0000\u024d\u027c\u0005\u0005"+
		"\u0000\u0000\u024e\u024f\u0005\u0091\u0000\u0000\u024f\u0250\u0005\u0004"+
		"\u0000\u0000\u0250\u0251\u0005\u00bb\u0000\u0000\u0251\u027c\u0005\u0005"+
		"\u0000\u0000\u0252\u0253\u0005\u0092\u0000\u0000\u0253\u0254\u0005\u0004"+
		"\u0000\u0000\u0254\u0255\u0003\u0012\t\u0000\u0255\u0256\u0005\u0005\u0000"+
		"\u0000\u0256\u027c\u0001\u0000\u0000\u0000\u0257\u0258\u0005\u0093\u0000"+
		"\u0000\u0258\u0259\u0005\u0004\u0000\u0000\u0259\u025a\u0005\u00bb\u0000"+
		"\u0000\u025a\u027c\u0005\u0005\u0000\u0000\u025b\u025c\u0005\u0094\u0000"+
		"\u0000\u025c\u025d\u0005\u0004\u0000\u0000\u025d\u025e\u0007\u0006\u0000"+
		"\u0000\u025e\u025f\u0005\b\u0000\u0000\u025f\u0260\u0005\u00bb\u0000\u0000"+
		"\u0260\u0261\u0005\b\u0000\u0000\u0261\u0262\u0003\u0012\t\u0000\u0262"+
		"\u0263\u0005\u0005\u0000\u0000\u0263\u027c\u0001\u0000\u0000\u0000\u0264"+
		"\u0265\u0005\u0095\u0000\u0000\u0265\u0266\u0005\u0004\u0000\u0000\u0266"+
		"\u0267\u0005\u00bb\u0000\u0000\u0267\u027c\u0005\u0005\u0000\u0000\u0268"+
		"\u0269\u0005\u0096\u0000\u0000\u0269\u026a\u0005\u0004\u0000\u0000\u026a"+
		"\u026b\u0003\u0012\t\u0000\u026b\u026c\u0005\u0005\u0000\u0000\u026c\u027c"+
		"\u0001\u0000\u0000\u0000\u026d\u026e\u0005\u009b\u0000\u0000\u026e\u026f"+
		"\u0005\u0004\u0000\u0000\u026f\u0270\u0005:\u0000\u0000\u0270\u0271\u0005"+
		"\b\u0000\u0000\u0271\u0272\u0003\u0012\t\u0000\u0272\u0273\u0005\b\u0000"+
		"\u0000\u0273\u0274\u0003\u0012\t\u0000\u0274\u0275\u0005\u0005\u0000\u0000"+
		"\u0275\u027c\u0001\u0000\u0000\u0000\u0276\u0277\u0005\u009c\u0000\u0000"+
		"\u0277\u0278\u0005\u0004\u0000\u0000\u0278\u0279\u0003\u0012\t\u0000\u0279"+
		"\u027a\u0005\u0005\u0000\u0000\u027a\u027c\u0001\u0000\u0000\u0000\u027b"+
		"\u021d\u0001\u0000\u0000\u0000\u027b\u0222\u0001\u0000\u0000\u0000\u027b"+
		"\u0227\u0001\u0000\u0000\u0000\u027b\u022c\u0001\u0000\u0000\u0000\u027b"+
		"\u022f\u0001\u0000\u0000\u0000\u027b\u0238\u0001\u0000\u0000\u0000\u027b"+
		"\u023d\u0001\u0000\u0000\u0000\u027b\u0244\u0001\u0000\u0000\u0000\u027b"+
		"\u024a\u0001\u0000\u0000\u0000\u027b\u024e\u0001\u0000\u0000\u0000\u027b"+
		"\u0252\u0001\u0000\u0000\u0000\u027b\u0257\u0001\u0000\u0000\u0000\u027b"+
		"\u025b\u0001\u0000\u0000\u0000\u027b\u0264\u0001\u0000\u0000\u0000\u027b"+
		"\u0268\u0001\u0000\u0000\u0000\u027b\u026d\u0001\u0000\u0000\u0000\u027b"+
		"\u0276\u0001\u0000\u0000\u0000\u027c\'\u0001\u0000\u0000\u0000\u027d\u027e"+
		"\u0005!\u0000\u0000\u027e\u027f\u0005\f\u0000\u0000\u027f\u0280\u0003"+
		"*\u0015\u0000\u0280)\u0001\u0000\u0000\u0000\u0281\u0282\u0005U\u0000"+
		"\u0000\u0282\u0283\u0005\f\u0000\u0000\u0283\u0284\u0005\"\u0000\u0000"+
		"\u0284\u0285\u0005\u0004\u0000\u0000\u0285\u0286\u0005\u00bb\u0000\u0000"+
		"\u0286\u0287\u0005\b\u0000\u0000\u0287\u0288\u0005\u00bb\u0000\u0000\u0288"+
		"\u0297\u0005\u0005\u0000\u0000\u0289\u028a\u0005[\u0000\u0000\u028a\u028b"+
		"\u0005\u0004\u0000\u0000\u028b\u028c\u0005\u00bb\u0000\u0000\u028c\u0297"+
		"\u0005\u0005\u0000\u0000\u028d\u028e\u0005\\\u0000\u0000\u028e\u028f\u0005"+
		"\f\u0000\u0000\u028f\u0290\u0005\u0015\u0000\u0000\u0290\u0291\u0005\u0004"+
		"\u0000\u0000\u0291\u0292\u0005\u00bb\u0000\u0000\u0292\u0297\u0005\u0005"+
		"\u0000\u0000\u0293\u0294\u0005h\u0000\u0000\u0294\u0295\u0005\u0004\u0000"+
		"\u0000\u0295\u0297\u0005\u0005\u0000\u0000\u0296\u0281\u0001\u0000\u0000"+
		"\u0000\u0296\u0289\u0001\u0000\u0000\u0000\u0296\u028d\u0001\u0000\u0000"+
		"\u0000\u0296\u0293\u0001\u0000\u0000\u0000\u0297+\u0001\u0000\u0000\u0000"+
		"\u0298\u0299\u0005!\u0000\u0000\u0299\u029a\u0005\f\u0000\u0000\u029a"+
		"\u029f\u0003.\u0017\u0000\u029b\u029c\u0005!\u0000\u0000\u029c\u029d\u0005"+
		"\f\u0000\u0000\u029d\u029f\u00030\u0018\u0000\u029e\u0298\u0001\u0000"+
		"\u0000\u0000\u029e\u029b\u0001\u0000\u0000\u0000\u029f-\u0001\u0000\u0000"+
		"\u0000\u02a0\u02a1\u0005g\u0000\u0000\u02a1\u02a2\u0005\u0004\u0000\u0000"+
		"\u02a2\u02a3\u0005\u0005\u0000\u0000\u02a3/\u0001\u0000\u0000\u0000\u02a4"+
		"\u02a5\u0005\u0088\u0000\u0000\u02a5\u02a6\u0005\u0004\u0000\u0000\u02a6"+
		"\u02a7\u0003\u0012\t\u0000\u02a7\u02a8\u0005\u0005\u0000\u0000\u02a8\u02d0"+
		"\u0001\u0000\u0000\u0000\u02a9\u02aa\u0005\u0099\u0000\u0000\u02aa\u02ab"+
		"\u0005\u0004\u0000\u0000\u02ab\u02ac\u0005\u00bb\u0000\u0000\u02ac\u02ad"+
		"\u0005\b\u0000\u0000\u02ad\u02b0\u0003\u0012\t\u0000\u02ae\u02af\u0005"+
		"\b\u0000\u0000\u02af\u02b1\u0003\u0012\t\u0000\u02b0\u02ae\u0001\u0000"+
		"\u0000\u0000\u02b0\u02b1\u0001\u0000\u0000\u0000\u02b1\u02b2\u0001\u0000"+
		"\u0000\u0000\u02b2\u02b3\u0005\u0005\u0000\u0000\u02b3\u02d0\u0001\u0000"+
		"\u0000\u0000\u02b4\u02b5\u0005\u009a\u0000\u0000\u02b5\u02b6\u0005\u0004"+
		"\u0000\u0000\u02b6\u02b7\u0005\u00bb\u0000\u0000\u02b7\u02d0\u0005\u0005"+
		"\u0000\u0000\u02b8\u02b9\u0005\u008b\u0000\u0000\u02b9\u02ba\u0005\u0004"+
		"\u0000\u0000\u02ba\u02d0\u0005\u0005\u0000\u0000\u02bb\u02bc\u0005\u008e"+
		"\u0000\u0000\u02bc\u02bd\u0005\u0004\u0000\u0000\u02bd\u02be\u0005\u00bb"+
		"\u0000\u0000\u02be\u02bf\u0005\b\u0000\u0000\u02bf\u02c0\u0003\u0012\t"+
		"\u0000\u02c0\u02c1\u0005\b\u0000\u0000\u02c1\u02c2\u0003\u0012\t\u0000"+
		"\u02c2\u02c3\u0005\u0005\u0000\u0000\u02c3\u02d0\u0001\u0000\u0000\u0000"+
		"\u02c4\u02c5\u0005\u0097\u0000\u0000\u02c5\u02c6\u0005\u0004\u0000\u0000"+
		"\u02c6\u02c7\u0005\u00bb\u0000\u0000\u02c7\u02c8\u0005\b\u0000\u0000\u02c8"+
		"\u02c9\u0003\u0012\t\u0000\u02c9\u02ca\u0005\u0005\u0000\u0000\u02ca\u02d0"+
		"\u0001\u0000\u0000\u0000\u02cb\u02cc\u0005\u0098\u0000\u0000\u02cc\u02cd"+
		"\u0005\u0004\u0000\u0000\u02cd\u02ce\u0005\u00bb\u0000\u0000\u02ce\u02d0"+
		"\u0005\u0005\u0000\u0000\u02cf\u02a4\u0001\u0000\u0000\u0000\u02cf\u02a9"+
		"\u0001\u0000\u0000\u0000\u02cf\u02b4\u0001\u0000\u0000\u0000\u02cf\u02b8"+
		"\u0001\u0000\u0000\u0000\u02cf\u02bb\u0001\u0000\u0000\u0000\u02cf\u02c4"+
		"\u0001\u0000\u0000\u0000\u02cf\u02cb\u0001\u0000\u0000\u0000\u02d01\u0001"+
		"\u0000\u0000\u0000\u02d1\u02d2\u0005#\u0000\u0000\u02d2\u02d3\u0005\f"+
		"\u0000\u0000\u02d3\u02d4\u00034\u001a\u0000\u02d43\u0001\u0000\u0000\u0000"+
		"\u02d5\u02d6\u0005\u00b0\u0000\u0000\u02d6\u02d7\u0005\u0004\u0000\u0000"+
		"\u02d7\u02d8\u0005\u00bb\u0000\u0000\u02d8\u0368\u0005\u0005\u0000\u0000"+
		"\u02d9\u02da\u0005\u00b1\u0000\u0000\u02da\u02db\u0005\u0004\u0000\u0000"+
		"\u02db\u0368\u0005\u0005\u0000\u0000\u02dc\u02dd\u0005i\u0000\u0000\u02dd"+
		"\u02de\u0005\f\u0000\u0000\u02de\u02df\u0005\u0015\u0000\u0000\u02df\u02e0"+
		"\u0005\u0004\u0000\u0000\u02e0\u02e1\u0005A\u0000\u0000\u02e1\u0368\u0005"+
		"\u0005\u0000\u0000\u02e2\u02e3\u0005j\u0000\u0000\u02e3\u02e4\u0005\f"+
		"\u0000\u0000\u02e4\u02e5\u0005$\u0000\u0000\u02e5\u02e6\u0005\u0004\u0000"+
		"\u0000\u02e6\u02e7\u0005A\u0000\u0000\u02e7\u0368\u0005\u0005\u0000\u0000"+
		"\u02e8\u02e9\u0005j\u0000\u0000\u02e9\u02ea\u0005\f\u0000\u0000\u02ea"+
		"\u02eb\u0005%\u0000\u0000\u02eb\u02ec\u0005\u0004\u0000\u0000\u02ec\u02ed"+
		"\u0005A\u0000\u0000\u02ed\u0368\u0005\u0005\u0000\u0000\u02ee\u02ef\u0005"+
		"I\u0000\u0000\u02ef\u02f0\u0005\u0004\u0000\u0000\u02f0\u02f1\u0005\u00bb"+
		"\u0000\u0000\u02f1\u02f2\u0005\b\u0000\u0000\u02f2\u02f3\u0005A\u0000"+
		"\u0000\u02f3\u0368\u0005\u0005\u0000\u0000\u02f4\u02f5\u0005[\u0000\u0000"+
		"\u02f5\u02f6\u0005\f\u0000\u0000\u02f6\u02f7\u0005$\u0000\u0000\u02f7"+
		"\u02f8\u0005\u0004\u0000\u0000\u02f8\u02f9\u0005A\u0000\u0000\u02f9\u0368"+
		"\u0005\u0005\u0000\u0000\u02fa\u02fb\u0005[\u0000\u0000\u02fb\u02fc\u0005"+
		"\f\u0000\u0000\u02fc\u02fd\u0005%\u0000\u0000\u02fd\u02fe\u0005\u0004"+
		"\u0000\u0000\u02fe\u02ff\u0005A\u0000\u0000\u02ff\u0368\u0005\u0005\u0000"+
		"\u0000\u0300\u0301\u0005P\u0000\u0000\u0301\u0302\u0005\f\u0000\u0000"+
		"\u0302\u0303\u0005&\u0000\u0000\u0303\u0304\u0005\u0004\u0000\u0000\u0304"+
		"\u0305\u0005\u00bb\u0000\u0000\u0305\u0368\u0005\u0005\u0000\u0000\u0306"+
		"\u0307\u0005P\u0000\u0000\u0307\u0308\u0005\f\u0000\u0000\u0308\u0309"+
		"\u0005\'\u0000\u0000\u0309\u030a\u0005\u0004\u0000\u0000\u030a\u030b\u0005"+
		"\u00bb\u0000\u0000\u030b\u0368\u0005\u0005\u0000\u0000\u030c\u030d\u0005"+
		"P\u0000\u0000\u030d\u030e\u0005\f\u0000\u0000\u030e\u030f\u0005$\u0000"+
		"\u0000\u030f\u0310\u0005\u0004\u0000\u0000\u0310\u0311\u0005\u00bb\u0000"+
		"\u0000\u0311\u0368\u0005\u0005\u0000\u0000\u0312\u0313\u0005\\\u0000\u0000"+
		"\u0313\u0314\u0005\f\u0000\u0000\u0314\u0315\u0005\u0015\u0000\u0000\u0315"+
		"\u0316\u0005\u0004\u0000\u0000\u0316\u0317\u0005\u00bb\u0000\u0000\u0317"+
		"\u0368\u0005\u0005\u0000\u0000\u0318\u0319\u0005U\u0000\u0000\u0319\u031a"+
		"\u0005\f\u0000\u0000\u031a\u031b\u0005\u0016\u0000\u0000\u031b\u031c\u0005"+
		"\u0004\u0000\u0000\u031c\u031d\u0005A\u0000\u0000\u031d\u0368\u0005\u0005"+
		"\u0000\u0000\u031e\u031f\u0005U\u0000\u0000\u031f\u0320\u0005\f\u0000"+
		"\u0000\u0320\u0321\u0005(\u0000\u0000\u0321\u0322\u0005\u0004\u0000\u0000"+
		"\u0322\u0323\u0005A\u0000\u0000\u0323\u0368\u0005\u0005\u0000\u0000\u0324"+
		"\u0325\u0005h\u0000\u0000\u0325\u0326\u0005\u0004\u0000\u0000\u0326\u0327"+
		"\u0005A\u0000\u0000\u0327\u0368\u0005\u0005\u0000\u0000\u0328\u0329\u0005"+
		"e\u0000\u0000\u0329\u032a\u0005\f\u0000\u0000\u032a\u032b\u0005)\u0000"+
		"\u0000\u032b\u032c\u0005\u0004\u0000\u0000\u032c\u032d\u0005A\u0000\u0000"+
		"\u032d\u0368\u0005\u0005\u0000\u0000\u032e\u032f\u0005V\u0000\u0000\u032f"+
		"\u0330\u0005\u0004\u0000\u0000\u0330\u0331\u0005\u00bb\u0000\u0000\u0331"+
		"\u0332\u0005\b\u0000\u0000\u0332\u0333\u0005A\u0000\u0000\u0333\u0368"+
		"\u0005\u0005\u0000\u0000\u0334\u0335\u0005X\u0000\u0000\u0335\u0336\u0005"+
		"\f\u0000\u0000\u0336\u0337\u0005*\u0000\u0000\u0337\u0338\u0005\u0004"+
		"\u0000\u0000\u0338\u0339\u0005A\u0000\u0000\u0339\u0368\u0005\u0005\u0000"+
		"\u0000\u033a\u033b\u0005X\u0000\u0000\u033b\u033c\u0005\f\u0000\u0000"+
		"\u033c\u033d\u0005+\u0000\u0000\u033d\u033e\u0005\u0004\u0000\u0000\u033e"+
		"\u033f\u0005A\u0000\u0000\u033f\u0368\u0005\u0005\u0000\u0000\u0340\u0341"+
		"\u0005W\u0000\u0000\u0341\u0342\u0005\f\u0000\u0000\u0342\u0343\u0005"+
		"\u0016\u0000\u0000\u0343\u0344\u0005\u0004\u0000\u0000\u0344\u0345\u0005"+
		"A\u0000\u0000\u0345\u0368\u0005\u0005\u0000\u0000\u0346\u0347\u0005W\u0000"+
		"\u0000\u0347\u0348\u0005\f\u0000\u0000\u0348\u0349\u0005,\u0000\u0000"+
		"\u0349\u034a\u0005\u0004\u0000\u0000\u034a\u034b\u0005A\u0000\u0000\u034b"+
		"\u0368\u0005\u0005\u0000\u0000\u034c\u034d\u0005m\u0000\u0000\u034d\u034e"+
		"\u0005\u0004\u0000\u0000\u034e\u034f\u0003\u0012\t\u0000\u034f\u0350\u0005"+
		"\u0005\u0000\u0000\u0350\u0368\u0001\u0000\u0000\u0000\u0351\u0352\u0005"+
		"\u009d\u0000\u0000\u0352\u0353\u0005\u0004\u0000\u0000\u0353\u0354\u0003"+
		"\u0012\t\u0000\u0354\u0355\u0005\u0005\u0000\u0000\u0355\u0368\u0001\u0000"+
		"\u0000\u0000\u0356\u0357\u0005\u00b3\u0000\u0000\u0357\u0358\u0005\u0004"+
		"\u0000\u0000\u0358\u0368\u0005\u0005\u0000\u0000\u0359\u035a\u0005\u00b8"+
		"\u0000\u0000\u035a\u035b\u0005\u0004\u0000\u0000\u035b\u035c\u0005\u00bb"+
		"\u0000\u0000\u035c\u0368\u0005\u0005\u0000\u0000\u035d\u035e\u0005\u00b9"+
		"\u0000\u0000\u035e\u035f\u0005\u0004\u0000\u0000\u035f\u0360\u0005\u00bb"+
		"\u0000\u0000\u0360\u0361\u0005\b\u0000\u0000\u0361\u0362\u0005\u00bb\u0000"+
		"\u0000\u0362\u0368\u0005\u0005\u0000\u0000\u0363\u0364\u0005\u00ba\u0000"+
		"\u0000\u0364\u0365\u0005\u0004\u0000\u0000\u0365\u0366\u0005\u00bb\u0000"+
		"\u0000\u0366\u0368\u0005\u0005\u0000\u0000\u0367\u02d5\u0001\u0000\u0000"+
		"\u0000\u0367\u02d9\u0001\u0000\u0000\u0000\u0367\u02dc\u0001\u0000\u0000"+
		"\u0000\u0367\u02e2\u0001\u0000\u0000\u0000\u0367\u02e8\u0001\u0000\u0000"+
		"\u0000\u0367\u02ee\u0001\u0000\u0000\u0000\u0367\u02f4\u0001\u0000\u0000"+
		"\u0000\u0367\u02fa\u0001\u0000\u0000\u0000\u0367\u0300\u0001\u0000\u0000"+
		"\u0000\u0367\u0306\u0001\u0000\u0000\u0000\u0367\u030c\u0001\u0000\u0000"+
		"\u0000\u0367\u0312\u0001\u0000\u0000\u0000\u0367\u0318\u0001\u0000\u0000"+
		"\u0000\u0367\u031e\u0001\u0000\u0000\u0000\u0367\u0324\u0001\u0000\u0000"+
		"\u0000\u0367\u0328\u0001\u0000\u0000\u0000\u0367\u032e\u0001\u0000\u0000"+
		"\u0000\u0367\u0334\u0001\u0000\u0000\u0000\u0367\u033a\u0001\u0000\u0000"+
		"\u0000\u0367\u0340\u0001\u0000\u0000\u0000\u0367\u0346\u0001\u0000\u0000"+
		"\u0000\u0367\u034c\u0001\u0000\u0000\u0000\u0367\u0351\u0001\u0000\u0000"+
		"\u0000\u0367\u0356\u0001\u0000\u0000\u0000\u0367\u0359\u0001\u0000\u0000"+
		"\u0000\u0367\u035d\u0001\u0000\u0000\u0000\u0367\u0363\u0001\u0000\u0000"+
		"\u0000\u03685\u0001\u0000\u0000\u0000\u0369\u036a\u0005#\u0000\u0000\u036a"+
		"\u036b\u0005\f\u0000\u0000\u036b\u0373\u00038\u001c\u0000\u036c\u036d"+
		"\u0005#\u0000\u0000\u036d\u036e\u0005\f\u0000\u0000\u036e\u0373\u0003"+
		":\u001d\u0000\u036f\u0370\u0005#\u0000\u0000\u0370\u0371\u0005\f\u0000"+
		"\u0000\u0371\u0373\u0003<\u001e\u0000\u0372\u0369\u0001\u0000\u0000\u0000"+
		"\u0372\u036c\u0001\u0000\u0000\u0000\u0372\u036f\u0001\u0000\u0000\u0000"+
		"\u03737\u0001\u0000\u0000\u0000\u0374\u0375\u0005g\u0000\u0000\u0375\u0376"+
		"\u0005\u0004\u0000\u0000\u0376\u0377\u0005A\u0000\u0000\u0377\u0385\u0005"+
		"\u0005\u0000\u0000\u0378\u0379\u0005O\u0000\u0000\u0379\u037a\u0005\u0004"+
		"\u0000\u0000\u037a\u037b\u0005\u00bb\u0000\u0000\u037b\u0385\u0005\u0005"+
		"\u0000\u0000\u037c\u037d\u0005T\u0000\u0000\u037d\u037e\u0005\u0004\u0000"+
		"\u0000\u037e\u037f\u0005A\u0000\u0000\u037f\u0385\u0005\u0005\u0000\u0000"+
		"\u0380\u0381\u0005\u0086\u0000\u0000\u0381\u0382\u0005\u0004\u0000\u0000"+
		"\u0382\u0383\u0005A\u0000\u0000\u0383\u0385\u0005\u0005\u0000\u0000\u0384"+
		"\u0374\u0001\u0000\u0000\u0000\u0384\u0378\u0001\u0000\u0000\u0000\u0384"+
		"\u037c\u0001\u0000\u0000\u0000\u0384\u0380\u0001\u0000\u0000\u0000\u0385"+
		"9\u0001\u0000\u0000\u0000\u0386\u0387\u0005\u009e\u0000\u0000\u0387\u0388"+
		"\u0005\u0004\u0000\u0000\u0388\u0389\u0005\u00bb\u0000\u0000\u0389\u038a"+
		"\u0005\b\u0000\u0000\u038a\u038b\u0003\u0012\t\u0000\u038b\u038c\u0005"+
		"\u0005\u0000\u0000\u038c\u0414\u0001\u0000\u0000\u0000\u038d\u038e\u0005"+
		"\u009f\u0000\u0000\u038e\u038f\u0005\u0004\u0000\u0000\u038f\u0390\u0005"+
		"\u00bb\u0000\u0000\u0390\u0391\u0005\b\u0000\u0000\u0391\u0392\u0003\u0012"+
		"\t\u0000\u0392\u0393\u0005\b\u0000\u0000\u0393\u0394\u0005\u00bb\u0000"+
		"\u0000\u0394\u0395\u0005\b\u0000\u0000\u0395\u0396\u0003\u0012\t\u0000"+
		"\u0396\u0397\u0005\u0005\u0000\u0000\u0397\u0414\u0001\u0000\u0000\u0000"+
		"\u0398\u0399\u0005\u00a0\u0000\u0000\u0399\u039a\u0005\u0004\u0000\u0000"+
		"\u039a\u039b\u0005\u00bb\u0000\u0000\u039b\u039c\u0005\b\u0000\u0000\u039c"+
		"\u039d\u0003\u0012\t\u0000\u039d\u039e\u0005\u0005\u0000\u0000\u039e\u0414"+
		"\u0001\u0000\u0000\u0000\u039f\u03a0\u0005\u00a1\u0000\u0000\u03a0\u03a1"+
		"\u0005\u0004\u0000\u0000\u03a1\u03a2\u0005\u00bb\u0000\u0000\u03a2\u03a3"+
		"\u0005\b\u0000\u0000\u03a3\u03a4\u0005\u00bb\u0000\u0000\u03a4\u0414\u0005"+
		"\u0005\u0000\u0000\u03a5\u03a6\u0005\u00a2\u0000\u0000\u03a6\u03a7\u0005"+
		"\u0004\u0000\u0000\u03a7\u03a8\u0005\u00bb\u0000\u0000\u03a8\u03a9\u0005"+
		"\b\u0000\u0000\u03a9\u03ac\u0003\u0012\t\u0000\u03aa\u03ab\u0005\b\u0000"+
		"\u0000\u03ab\u03ad\u0003\u0012\t\u0000\u03ac\u03aa\u0001\u0000\u0000\u0000"+
		"\u03ac\u03ad\u0001\u0000\u0000\u0000\u03ad\u03ae\u0001\u0000\u0000\u0000"+
		"\u03ae\u03af\u0005\u0005\u0000\u0000\u03af\u0414\u0001\u0000\u0000\u0000"+
		"\u03b0\u03b1\u0005\u00a4\u0000\u0000\u03b1\u03b2\u0005\u0004\u0000\u0000"+
		"\u03b2\u0414\u0005\u0005\u0000\u0000\u03b3\u03b4\u0005\u00a5\u0000\u0000"+
		"\u03b4\u03b5\u0005\u0004\u0000\u0000\u03b5\u03b6\u0005\u00bb\u0000\u0000"+
		"\u03b6\u03b7\u0005\b\u0000\u0000\u03b7\u03b8\u0003\u0012\t\u0000\u03b8"+
		"\u03b9\u0005\b\u0000\u0000\u03b9\u03ba\u0003\u0012\t\u0000\u03ba\u03bb"+
		"\u0005\u0005\u0000\u0000\u03bb\u0414\u0001\u0000\u0000\u0000\u03bc\u03bd"+
		"\u0005\u00a6\u0000\u0000\u03bd\u03be\u0005\u0004\u0000\u0000\u03be\u03bf"+
		"\u0005\u00bb\u0000\u0000\u03bf\u03c0\u0005\b\u0000\u0000\u03c0\u03c1\u0003"+
		"\u0012\t\u0000\u03c1\u03c2\u0005\u0005\u0000\u0000\u03c2\u0414\u0001\u0000"+
		"\u0000\u0000\u03c3\u03c4\u0005\u00a7\u0000\u0000\u03c4\u03c5\u0005\u0004"+
		"\u0000\u0000\u03c5\u03c6\u0005\u00bb\u0000\u0000\u03c6\u03c7\u0005\b\u0000"+
		"\u0000\u03c7\u03c8\u0003\u0012\t\u0000\u03c8\u03c9\u0005\b\u0000\u0000"+
		"\u03c9\u03cc\u0003\u0012\t\u0000\u03ca\u03cb\u0005\b\u0000\u0000\u03cb"+
		"\u03cd\u0003\u0012\t\u0000\u03cc\u03ca\u0001\u0000\u0000\u0000\u03cc\u03cd"+
		"\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000\u03ce\u03cf"+
		"\u0005\u0005\u0000\u0000\u03cf\u0414\u0001\u0000\u0000\u0000\u03d0\u03d1"+
		"\u0005\u00a8\u0000\u0000\u03d1\u03d2\u0005\u0004\u0000\u0000\u03d2\u03d3"+
		"\u0005\u00bb\u0000\u0000\u03d3\u0414\u0005\u0005\u0000\u0000\u03d4\u03d5"+
		"\u0005\u00a9\u0000\u0000\u03d5\u03d6\u0005\u0004\u0000\u0000\u03d6\u03d7"+
		"\u0003\u0012\t\u0000\u03d7\u03d8\u0005\b\u0000\u0000\u03d8\u03d9\u0003"+
		"\u0012\t\u0000\u03d9\u03da\u0005\b\u0000\u0000\u03da\u03db\u0003\u0012"+
		"\t\u0000\u03db\u03dc\u0005\u0005\u0000\u0000\u03dc\u0414\u0001\u0000\u0000"+
		"\u0000\u03dd\u03de\u0005\u00aa\u0000\u0000\u03de\u03df\u0005\u0004\u0000"+
		"\u0000\u03df\u03e0\u0005\u00bb\u0000\u0000\u03e0\u0414\u0005\u0005\u0000"+
		"\u0000\u03e1\u03e2\u0005\u008b\u0000\u0000\u03e2\u03e3\u0005\u0004\u0000"+
		"\u0000\u03e3\u0414\u0005\u0005\u0000\u0000\u03e4\u03e5\u0005\u00ab\u0000"+
		"\u0000\u03e5\u03e6\u0005\u0004\u0000\u0000\u03e6\u03e7\u0003\u0012\t\u0000"+
		"\u03e7\u03e8\u0005\b\u0000\u0000\u03e8\u03e9\u0003\u0012\t\u0000\u03e9"+
		"\u03ea\u0005\u0005\u0000\u0000\u03ea\u0414\u0001\u0000\u0000\u0000\u03eb"+
		"\u03ec\u0005\u0090\u0000\u0000\u03ec\u03ed\u0005\u0004\u0000\u0000\u03ed"+
		"\u03ee\u0005A\u0000\u0000\u03ee\u0414\u0005\u0005\u0000\u0000\u03ef\u03f0"+
		"\u0005\u0092\u0000\u0000\u03f0\u03f1\u0005\u0004\u0000\u0000\u03f1\u03f2"+
		"\u0003\u0012\t\u0000\u03f2\u03f3\u0005\u0005\u0000\u0000\u03f3\u0414\u0001"+
		"\u0000\u0000\u0000\u03f4\u03f5\u0005\u00ac\u0000\u0000\u03f5\u03f6\u0005"+
		"\u0004\u0000\u0000\u03f6\u03fc\u0003\u0012\t\u0000\u03f7\u03f8\u0005\b"+
		"\u0000\u0000\u03f8\u03f9\u0003\u0012\t\u0000\u03f9\u03fa\u0005\b\u0000"+
		"\u0000\u03fa\u03fb\u0003\u0012\t\u0000\u03fb\u03fd\u0001\u0000\u0000\u0000"+
		"\u03fc\u03f7\u0001\u0000\u0000\u0000\u03fc\u03fd\u0001\u0000\u0000\u0000"+
		"\u03fd\u03fe\u0001\u0000\u0000\u0000\u03fe\u03ff\u0005\u0005\u0000\u0000"+
		"\u03ff\u0414\u0001\u0000\u0000\u0000\u0400\u0401\u0005\u00ad\u0000\u0000"+
		"\u0401\u0402\u0005\u0004\u0000\u0000\u0402\u0403\u0005\u00bb\u0000\u0000"+
		"\u0403\u0404\u0005\b\u0000\u0000\u0404\u0405\u0005\u00bb\u0000\u0000\u0405"+
		"\u0414\u0005\u0005\u0000\u0000\u0406\u0407\u0005\u00ae\u0000\u0000\u0407"+
		"\u0408\u0005\u0004\u0000\u0000\u0408\u0414\u0005\u0005\u0000\u0000\u0409"+
		"\u040a\u0005\u00af\u0000\u0000\u040a\u040b\u0005\u0004\u0000\u0000\u040b"+
		"\u0414\u0005\u0005\u0000\u0000\u040c\u040d\u0005\u00b2\u0000\u0000\u040d"+
		"\u040e\u0005\u0004\u0000\u0000\u040e\u040f\u0003\u0012\t\u0000\u040f\u0410"+
		"\u0005\b\u0000\u0000\u0410\u0411\u0003\u0012\t\u0000\u0411\u0412\u0005"+
		"\u0005\u0000\u0000\u0412\u0414\u0001\u0000\u0000\u0000\u0413\u0386\u0001"+
		"\u0000\u0000\u0000\u0413\u038d\u0001\u0000\u0000\u0000\u0413\u0398\u0001"+
		"\u0000\u0000\u0000\u0413\u039f\u0001\u0000\u0000\u0000\u0413\u03a5\u0001"+
		"\u0000\u0000\u0000\u0413\u03b0\u0001\u0000\u0000\u0000\u0413\u03b3\u0001"+
		"\u0000\u0000\u0000\u0413\u03bc\u0001\u0000\u0000\u0000\u0413\u03c3\u0001"+
		"\u0000\u0000\u0000\u0413\u03d0\u0001\u0000\u0000\u0000\u0413\u03d4\u0001"+
		"\u0000\u0000\u0000\u0413\u03dd\u0001\u0000\u0000\u0000\u0413\u03e1\u0001"+
		"\u0000\u0000\u0000\u0413\u03e4\u0001\u0000\u0000\u0000\u0413\u03eb\u0001"+
		"\u0000\u0000\u0000\u0413\u03ef\u0001\u0000\u0000\u0000\u0413\u03f4\u0001"+
		"\u0000\u0000\u0000\u0413\u0400\u0001\u0000\u0000\u0000\u0413\u0406\u0001"+
		"\u0000\u0000\u0000\u0413\u0409\u0001\u0000\u0000\u0000\u0413\u040c\u0001"+
		"\u0000\u0000\u0000\u0414;\u0001\u0000\u0000\u0000\u0415\u0416\u0005\u00b4"+
		"\u0000\u0000\u0416\u0417\u0005\u0004\u0000\u0000\u0417\u0430\u0005\u0005"+
		"\u0000\u0000\u0418\u0419\u0005\u00b5\u0000\u0000\u0419\u041a\u0005\u0004"+
		"\u0000\u0000\u041a\u041b\u0005\u00bb\u0000\u0000\u041b\u041c\u0005\b\u0000"+
		"\u0000\u041c\u041d\u0003\u0012\t\u0000\u041d\u041e\u0005\u0005\u0000\u0000"+
		"\u041e\u0430\u0001\u0000\u0000\u0000\u041f\u0420\u0005\u00b6\u0000\u0000"+
		"\u0420\u0421\u0005\u0004\u0000\u0000\u0421\u0422\u0005\u00bb\u0000\u0000"+
		"\u0422\u0423\u0005\b\u0000\u0000\u0423\u0424\u0005\u00bb\u0000\u0000\u0424"+
		"\u0425\u0005\b\u0000\u0000\u0425\u0426\u0003\u0012\t\u0000\u0426\u0427"+
		"\u0005\u0005\u0000\u0000\u0427\u0430\u0001\u0000\u0000\u0000\u0428\u0429"+
		"\u0005\u00b7\u0000\u0000\u0429\u042a\u0005\u0004\u0000\u0000\u042a\u042b"+
		"\u0005\u00bb\u0000\u0000\u042b\u042c\u0005\b\u0000\u0000\u042c\u042d\u0003"+
		"\u0012\t\u0000\u042d\u042e\u0005\u0005\u0000\u0000\u042e\u0430\u0001\u0000"+
		"\u0000\u0000\u042f\u0415\u0001\u0000\u0000\u0000\u042f\u0418\u0001\u0000"+
		"\u0000\u0000\u042f\u041f\u0001\u0000\u0000\u0000\u042f\u0428\u0001\u0000"+
		"\u0000\u0000\u0430=\u0001\u0000\u0000\u00004AHbgpr~\u0088\u008b\u0093"+
		"\u009a\u009d\u00b4\u00bc\u00d3\u00fd\u010d\u0110\u0115\u012b\u0157\u0159"+
		"\u0175\u017f\u0184\u018d\u0191\u0194\u019e\u01a5\u01a8\u01aa\u01b4\u01b7"+
		"\u01ba\u01bf\u0208\u0210\u021b\u027b\u0296\u029e\u02b0\u02cf\u0367\u0372"+
		"\u0384\u03ac\u03cc\u03fc\u0413\u042f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}