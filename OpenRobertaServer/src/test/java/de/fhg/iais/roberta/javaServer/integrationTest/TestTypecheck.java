package de.fhg.iais.roberta.javaServer.integrationTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.InfoCollector;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.validate.MicrobitV2TypecheckVisitor;
import de.fhg.iais.roberta.visitor.validate.WedoTypecheckVisitor;

/**
 * This class contains unit tests to validate the type-checking functionality for the MicrobitV2
 * and WeDo robots using different expressions and statements. The class includes test cases for
 * number, boolean, list, and string expressions, as well as various robot-specific functions.
 * <p>
 * The tests are based on textual representations of Blockly programs which are processed, and their
 * type-checking is validated using the respective visitors (`MicrobitV2TypecheckVisitor` and
 * `WedoTypecheckVisitor`).
 * <p>
 * The test suite includes tests for:
 * - Number expressions
 * - Boolean expressions
 * - List expressions
 * - String expressions
 * - Robot-specific functions (e.g., for MicrobitV2 and WeDo robots)
 * - Statement evaluations, including assignment, function calls, and control statements
 * - Additionally, image-related expressions are evaluated for MicrobitV2, and color-related expressions
 * are evaluated for WeDo robots.
 * <p>
 * When adding or evaluating new textual examples, it is important to note that there are pre-declared
 * variables used across the tests:
 * - `num`: Represents a number.
 * - `listN`: Represents a list of numbers.
 * - `listN2`: Represents a list of numbers.
 * - `boolT`: Represents a boolean value true .
 * - `boolF`: Represents a boolean value false.
 * - `str`: Represents a string.
 * - `ima`: Represents an Image.
 * - `listIma`: Represents a list of Images.
 * - `color`: Represents a color.
 * <p>
 * This allows for quick and consistent testing of various expressions and statements within the context
 * of the Blockly programs being evaluated.
 * <p>
 * It also performs checks to ensure that certain expressions or statements result in expected
 * type-checking errors (negative tests).
 */
public class TestTypecheck {
    private static final Logger LOG = LoggerFactory.getLogger(TestTypecheck.class);
    private static RobotFactory testFactory;

    private static JSONObject robotsFromTestSpec;
    private static JSONObject progDeclsFromTestSpec;
    private static final String CROSS_COMPILER_TESTS = "/crossCompilerTests/";
    private static final String ROBOT_SPECIFIC = "robotSpecific/";
    public static final String TEST_SPEC_YML = "classpath:/crossCompilerTests/testSpec.yml";
    private int typecheckErrorCount = 0;
    private int parserErrorCount = 0;

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("<field\\s+name=\"EXPRESSION\">([^<]+)</field>");
    private static final Pattern STMTEXPRESSION_PATTERN = Pattern.compile("<field\\s+name=\"STMTEXPRESSION\">([^<]+)</field>");

    public static final List<String> NUMBER_EXPRESSIONS = Arrays.asList(
        "sin(num) + cos(num) + tan(num)",
        "exp(2) + square(4) + sqrt(9) + abs(-5) + log10(100) + log(2)",
        "randomInt(1, 10) + randomFloat()",
        "floor(3.7) + ceil(2.3) + round(4.6)",
        "pow10(2) + (10%3)",
        "num + 1",
        "sum(listN) + max(listN) - min(listN)",
        "get(listN, 1)",
        "castToNumber(\"2\")",
        "castStringToNumber(str,1)"
    );
    public static final List<String> NUMBER_STATEMENTS = convertToStatements(NUMBER_EXPRESSIONS, "num");


    public static final List<String> BOOLEAN_EXPRESSIONS = Arrays.asList(
        "isEven(10) && isOdd(7) || isPrime(11) && isWhole(8)",
        "isEmpty(listN) && isPositive(5) || isNegative(-3)",
        "(num > 5) && (num <= 10)",
        "!boolF",
        "(num == 10) || (num != 5)",
        "microbitv2.receiveMessage(Boolean)"
    );

    public static final List<String> LIST_EXPRESSIONS = Arrays.asList(
        "subList(listN2, 0, 3)",
        "subListFromIndexToEnd(listN2, 0, 1)",
        "subListFromEndToIndex(listN2, 1, 1)",
        "subListFromFirstToLast(listN2)",
        "createListWith(getFromEnd(listN, 0),2)",
        "createListWith(getLast(listN),2)",
        "subListFromEndToLast(listN, 3)",
        "createEmptyList(Number)"
    );

    public static final List<String> STRING_EXPRESSIONS = Arrays.asList(
        "createTextWith(\"Hello\", num)",
        "\"Hello World!\"",
        "createTextWith(sin(num), cos(num))",
        "createTextWith(getFirst(listN), get(listN, 1))",
        "castToString(5)",
        "castToChar(65)"
    );

    public static final List<String> COLOR_EXPRESSIONS = Arrays.asList(
        "#yellow",
        "#black",
        "#rgb(FF0000)"
    );

    public static final List<String> STMT_FUNC = Arrays.asList(
        "set(listN, 1000,0);",
        "setFromEnd(listN, 1000,0);",
        "setFirst(listN,666);",
        "setLast(listN,666);",

        "insert(listN, 1000,0);",
        "insertFromEnd(listN, 1000,0);",
        "insertFirst(listN,666);",
        "insertLast(listN,666);",

        "remove(listN,0);",
        "removeFromEnd(listN,0);",
        "removeFirst(listN);",
        "removeLast(listN);",

        "changeBy(num,2);",
        "appendText(str, \"aaa\");"
    );

    public static final List<String> STMT_FUNC_FAIL = Arrays.asList(
        //FNAMESTMT:
        "set(listN, \"hola\",0);",
        "setFromEnd(listN, 1000,0,2);",
        "setFirst(listN,false);",
        "setLast(listN);",
        "insert(listN, true,0);",
        "insertFromEnd(listN, 1000);",
        "insertFirst(listN,666,9);",
        "insertLast(listN,666,true);",
        "remove(listN,0,0);",
        "removeFromEnd(listN);",
        "removeFirst(listN,true);",
        "removeLast();",

        "set(4, 1000,0);",
        "removeFromEnd(3,0);",
        "insert(3, 1000,0);"
    );

    public static final List<String> MICROBITV2_SPECIFIC = Arrays.asList(
        "ima = image(heart); ima = image(heartSmall); ima = image(happy); ima = image(smile); ima = image(sad); ima = image(confused); ima = image(angry); ima = image(asleep); ima = image(surprised); ima = image(silly); ima = image(fabulous); ima = image(meh); ima = image(yes); ima = image(no); ima = image(triangle); ima = image(triangleLeft); ima = image(chessboard); ima = image(diamond); ima = image(diamondSmall); ima = image(squareBig);",
        "ima = image(squareSmall); ima = image(rabbit); ima = image(cow); ima = image(musicCrotchet); ima = image(musicQuaver); ima = image(musicQuavers); ima = image(pitchfork); ima = image(xmas); ima = image(pacman); ima = image(target); ima = image(tshirt); ima = image(rollerskate); ima = image(duck); ima = image(house); ima = image(tortoise); ima = image(butterfly); ima = image(stickFigure); ima = image(ghost); ima = image(sword);",
        "ima = image(giraffe); ima = image(skull); ima = image(umbrella); ima = image(snake);",
        "microbitv2.showText(\"hola\");",
        "microbitv2.showCharacter(\"hola\");",
        "microbitv2.pitch(200,300);",
        "microbitv2.playFile(dadadadum);",
        "microbitv2.playFile(entertainer);",
        "microbitv2.playFile(prelude);",
        "microbitv2.playFile(ode);",
        "microbitv2.playFile(nyan);",
        "microbitv2.playFile(ringtone);",
        "microbitv2.playFile(funk);",
        "microbitv2.playFile(blues);",
        "microbitv2.playFile(birthday);",
        "microbitv2.playFile(wedding);",
        "microbitv2.playFile(funeral);",
        "microbitv2.playFile(punchline);",
        "microbitv2.playFile(python);",
        "microbitv2.playFile(baddy);",
        "microbitv2.playFile(chase);",
        "microbitv2.playFile(ba_ding);",
        "microbitv2.playFile(wawawawaa);",
        "microbitv2.playFile(jump_up);",
        "microbitv2.playFile(jump_down);",
        "microbitv2.playFile(power_up);",
        "microbitv2.playFile(power_down);",
        "microbitv2.showImage(image(smile));",
        "microbitv2.showImage(image.define([0,0,0,0,#][0,0,0,0,#][0,0,0,0,#][0,0,0,0,#][0,0,0,0,#]));",
        "microbitv2.showAnimation(listIma);",
        "microbitv2.clearDisplay();",
        "microbitv2.setLed(30,30,500);",
        "microbitv2.showOnSerial(\"Halloooo\");",
        "microbitv2.playSound(giggle);",
        "microbitv2.playSound(Happy);",
        "microbitv2.playSound(hello);",
        "microbitv2.playSound(mysterious);",
        "microbitv2.playSound(sad);",
        "microbitv2.playSound(slide);",
        "microbitv2.playSound(soaring);",
        "microbitv2.playSound(spring);",
        "microbitv2.playSound(twinkle);",
        "microbitv2.playSound(yawn);",
        "microbitv2.setVolume(30);",
        "microbitv2.speaker(on);",
        "microbitv2.speaker(off);",
        "microbitv2.writeValuePin(digital, A2, 10);",
        "microbitv2.switchLed(on);",
        "microbitv2.switchLed(off);",
        "num = microbitv2.getLedBrigthness(300,500);",
        "num = microbitv2.accelerometerSensor(x);",
        "boolT = microbitv2.logoTouchSensor.isPressed();",
        "num = microbitv2.compassSensor.getAngle();",
        "boolT = microbitv2.gestureSensor.currentGesture(up);",
        "boolT = microbitv2.gestureSensor.currentGesture(down);",
        "boolT = microbitv2.gestureSensor.currentGesture(faceDown);",
        "boolT = microbitv2.gestureSensor.currentGesture(faceUp);",
        "boolT = microbitv2.gestureSensor.currentGesture(shake);",
        "boolT = microbitv2.gestureSensor.currentGesture(freefall);",
        "boolT = microbitv2.keysSensor.isPressed(A);",
        "boolT = microbitv2.keysSensor.isPressed(B);",
        "num = microbitv2.lightSensor.getLevel();",
        "num = microbitv2.pinGetValueSensor(S, analog);",
        "num = microbitv2.pinGetValueSensor(S2, digital);",
        "num = microbitv2.pinGetValueSensor(S2, pulseHigh);",
        "num = microbitv2.pinGetValueSensor(S2, pulseLow);",
        "boolT = microbitv2.pinTouchSensor.isPressed(0);",
        "boolT = microbitv2.pinTouchSensor.isPressed(1);",
        "boolT = microbitv2.pinTouchSensor.isPressed(2);",
        "num = microbitv2.soundSensor.microphone.soundLevel();",
        "num = microbitv2.temperatureSensor();",
        "num = microbitv2.timerSensor();",
        "microbitv2.pinSetTouchMode(1, capacitive);",
        "microbitv2.timerReset();",
        "microbitv2.radioSend(String, 7, \"asd\");",
        "microbitv2.radioSend(Number, 7, 2);",
        "microbitv2.radioSend(Boolean, 7, false);",
        "microbitv2.radioSet(2);",
        "boolT = microbitv2.receiveMessage(Boolean);",
        "ima = image.invert(ima);",
        "ima = image.shift(right, ima, 1);",
        "ima = image.shift(left, image(smile), 1);",
        "ima = image.shift(up, ima, 1);",
        "ima = image.shift(down, image.define([0,0,0,0,#][0,0,0,0,#][0,0,0,0,#][0,0,0,0,#][0,0,0,0,#]), 1);"
    );

    public static final List<String> WEDO_SPECIFIC = Arrays.asList(
        "color = #rgb(ff1493); color = #pink;",
        "color = #rgb(800080); color = #purple;",
        "color = #rgb(4876ff); color = #blue;",
        "color = #rgb(00ffff); color = #cyan;",
        "color = #rgb(90ee90); color = #lightgreen;",
        "color = #rgb(008000); color = #green;",
        "color = #rgb(ffff00); color = #yellow;",
        "color = #rgb(ffa500); color = #orange;",
        "color = #rgb(ff0000); color = #red;",
        "color = #rgb(fffffe); color = #white;",
        "boolT = wedo.gyroSensor.isTilted(T2,up);",
        "boolT = wedo.gyroSensor.isTilted(T2,down);",
        "boolT = wedo.gyroSensor.isTilted(T2,back);",
        "boolT = wedo.gyroSensor.isTilted(T2,front);",
        "boolT = wedo.gyroSensor.isTilted(T2,no);",
        "boolT = wedo.gyroSensor.isTilted(T2,any);",
        "num = wedo.infraredSensor(I);",
        "boolT = wedo.keysSensor.isPressed(T);",
        "num = wedo.timerSensor();",
        "wedo.timerReset();",
        "wedo.showText(\"holis\");",
        "wedo.motor.move(M,300,100);", // For test this actuator, change the robot configuration",
        "wedo.motor.stop(M);", // For test actuator, change the robot configuration",
        "wedo.clearDisplay();",
        "wedo.pitch(S,300,300);",
        "wedo.turnRgbOn(R,#pink);",
        "wedo.turnRgbOff(R);"
    );
    public static final List<String> STMT_VAR_ASSIGN_SIMPLE_EXPRESSIONS = Arrays.asList(
        // Number expressions
        "num = num;",
        "num = 1;",
        "num = 1.4;",
        "num = pi;",
        "num = sqrt_1_2;",
        "num = randomInt(1, 100);",
        "num = randomFloat();",
        "num = sin(num);",
        "num = (sin(45) + cos(45)) * tan(60);",
        "num = exp(num);",
        "num = log10(num) * (square(10));",
        "num = ceil(3.782) + floor(3.1782);",
        "num = round(phi);",
        "num = min(listN);",
        "num = max(listN) * average(listN);",
        "num = sum(listN);",
        "num = randomItem(listN);",
        "num = size(listN);",
        "num = indexOfFirst(listN, 2);",
        "num = indexOfLast(listN, 2);",
        "num = getFirst(listN);",
        "num = getAndRemove(listN, 2);",
        "num = constrain(102, 1, 100);",
        "num = ((((3))));",
        "num = 2^2;",
        "num = num^num;",
        "num = 5%2;",
        "num = 10*10;",
        "num = 100/10;",
        "num = 1+2;",
        "num = pi + e;",
        "num = pi + 4.5;",
        "num = randomFloat() + cos(num);",
        "num = +-1;",
        "num = -+1;",
        "num = true ? 1 : 2;",

        // Boolean expressions
        "boolT = boolF;",
        "boolT = true;",
        "boolT = !!!!true;",
        "boolT = (num > 4) ? (num == 2) : (num + 1 == num + 3);",
        "boolT = isPositive(----6);",
        "boolT = isDivisibleBy(6, 2);",
        "boolT = isWhole(6.21234);",
        "boolT = isPrime(6);",
        "boolT = isEmpty(listN);",
        "boolT = !boolF;",
        "boolT = b || true;",
        "boolT = num == num;",
        "boolT = 7 == 7;",
        "boolT = str == str;",
        "boolT = num > 0;",
        "boolT = pi > 20;",
        "boolT = num >= num;",
        "boolT = 10 > 0;",
        "boolT = true || false;",
        "boolT = num > 4;",
        "boolT = num != 5;",
        "boolT = (num == 10) || (num != 5);",
        "boolT = 1 < 20;",
        "boolT = 2 <= 3;",

        // String expressions
        "str = \"OpenRoberta is awesome!\";",
        "str = createTextWith(str, 12, true);",
        "str = createTextWith(true, 'true', 12);"
    );

    List<String> STMT_VAR_ASSIGN_SIMPLE_EXPRESSIONS_ERROR_PARSER = Arrays.asList(
        "num = 1,,;",
        "num = randomInt(1,,100);",
        "num = pi +;",
        "boolT = (num > );",
        "num = sin(num num);",
        "num = true ? : 2;",
        "num = true ? 1 2;",
        "str = \"Unclosed string;",
        "num = 4 ? 1 : ;",
        "num = randomFloat(12,);",
        "num = max(listN,,);",
        "boolT = (num == );",
        "str = createTextWith(str,);",
        "num = ;",
        "num = exp(num,);",
        "num = pi + sqrt(,2);",
        "boolT = !;",
        "num = 2 *;",
        "boolT = (7 == );"
    );

    public static final List<String> STMT_VAR_ASSIGN = Arrays.asList(
        //EXPRESSIONS:
        "num=sin(num)+cos(num)+tan(num)+asin(num)+acos(num)+atan(num);",
        "num=exp(2) + square(4) + sqrt(9) + abs(-5) + log10(100) + log(2);",
        "num=randomInt(1,10)+ randomFloat();",
        "num= floor(3.7) + ceil(2.3) + round(4.6);",
        "boolT = isEven(10) && isOdd(7) || isPrime(11) && isWhole(8) || isEmpty(listN) && isPositive(5) || isNegative(-3) && isDivisibleBy(10, 5);",

        "num = sum(listN) + max(listN) - min(listN) * average(listN) / median(listN) + stddev(listN) % size(listN) + randomItem(listN);",
        "num = indexOfFirst(listN, 0);",
        "num = indexOfLast(listN, 0);",
        "num= getFirst(listN)+get(listN,1)+ getFromEnd(listN,0)+ getLast(listN);",

        "str = createTextWith(getFirst(listN), get(listN,1), getFromEnd(listN,2), get(listN,3), getLast(listN));",

        "num = getAndRemoveFirst(listN2) + getAndRemove(listN2, 1) + getAndRemoveFromEnd(listN2, 0) + getAndRemoveLast(listN2);",

        "listN2 = subList(listN, 0, 3);",
        "listN2 = subListFromIndexToEnd(listN, 0, 1);",
        "listN2 = subListFromEndToIndex(listN, 1, 1);",
        "listN2 = subListFromEndToEnd(listN, 4, 2);",
        "listN2 = subListFromIndexToLast(listN, 1);",
        "listN2 = subListFromFirstToIndex(listN, 2);",
        "listN2 = subListFromFirstToLast(listN);",
        "listN2 = subListFromFirstToIndex(listN, 3);",
        "listN2 = subListFromEndToLast(listN, 3);",
        "str = castToString(5);",
        "str = castToChar(65);",
        "num = castToNumber(\"2\");",
        "num = castStringToNumber(str,1);"
    );
    public static final List<String> STMT_VAR_ASSIGN_FAIL = Arrays.asList(
        //EXPRESSIONS:
        "num=sin(num+true)+cos(num)+tan(num)+asin(num)+acos(num)+atan(num);",
        "num=exp(2) + square(#black) + sqrt(9) + abs(-5) + log10(100) + log(2);",
        "num=randomInt(1,10,0)+ randomFloat(1);",
        "num= floor(3.7) + ceil(2.3) + round(true);",
        "boolT = isEven(10) && isOdd(7,0) || isPrime(11,0) && isWhole(8) || isEmpty(listN) && isPositive(5) || isNegative(-3) && isDivisibleBy(10, 5);",
        "num = sum(listN) + max(listN) - min(listN) * average(listN) / median(listN) + stddev(listN) % size(listN+2) + randomItem(listN,0);",
        "num = indexOfFirst(listN);",
        "num = indexOfLast(listN, true);",
        "str = createTextWith(sin(true));",
        "num = getFirst(listN) +get(listN,1)+getFromEnd(listN,0,1)+getLast(listN,true);",
        "num = getAndRemoveFirst(listN,0) + getAndRemove(listN, 1,2) + getAndRemoveFromEnd(listN, 0,true) + getAndRemoveLast(listN,false);",

        "listN = subList(listN2,0,true);",
        "listN = subListFromIndexToEnd(listN2,#black,1);",
        "listN = subListFromEndToIndex(listN2,1,1,2);",
        "listN = subListFromEndToEnd(listN2,4,2,false);",
        "listN = subListFromIndexToLast(listN2,1,3);",
        "listN = subListFromFirstToIndex();",
        "listN = subListFromFirstToLast(listN2,2);",
        "listN = subListFromFirstToIndex(listN2,false);",
        "listN = subListFromEndToLast(listN2,3,3);"
    );
    public static final List<String> STMT_CONTROLS_STATEMENTS = Arrays.asList(
        "for ( Number i=1; i<10; i++){};",
        "for ( Number i=1; i<10; i=i+1){};",
        "for ( Number i=1; i<10; i=i+2+4){};",
        "for ( Number i=1; i<10; i=num+1){};",
        "for each( Number item: listN){num=1;};",
        "if(true){num=1;} else if(false){num=2;} else{num=5;};",
        "if(boolT||boolF){num=1;} else if(false){num=2;} else{num=5;};",
        "while(true){num=1;};",
        "while(boolT==true){num=2;};",
        "while((boolT==true) && (boolF==false)){num=2;};",
        "for (Number i = 0; i < 10; i = i + 2) { if (isEven(i)) {insertLast(listN, i * 2);} else {insertFirst(listN, i);}; };",
        "waitUntil(average(listN) == 5) {insertLast(listN, 7);} orWaitFor(average(listN) == 2) {insertFirst(listN, 666);};",
        "waitUntil(false) {num=1;} orWaitFor(true) {num=2;};",
        "wait ms(500);",
        "while(true){break;};",
        "while(true){continue;};"
    );

    public static final List<String> STMT_CONTROLS_STATEMENTS_FAIL = Arrays.asList(
        "for ( Boolean i=1; i<10; i++){};",
        "for ( Number i=1; i<10; i=true){};",
        "for ( String i=1; i<10; i=i+2+4){};",
        "for ( Number i=1; i<1+true; i++){};",
        "for ( Number i=1; i<10; i=num+true){};",

        "for each( Boolean item: listN){num=1;};",
        "for each( Number item: true){num=1;};",
        "if(5){num=1;} else if(false){num=2;} else{num=5;};",
        "if(boolT||[0,0,0]){num=1;} else if(false){num=2;} else{num=5;};",
        "while(boolT==2){num=1;};",
        "while(boolT==2+2){num=2;};",
        "while(2){num=1;};",
        "while((boolT==#black) && (boolF==false)){num=2;};",
        "for (Number i = 0; i < 10; i = i + 2) { if (isEven(i+ #black)) {insertLast(listN, i * 2);} else {insertFirst(listN, i);}; };",
        "waitUntil(average(listN) == 5) {insertLast(listN, 7,9);} orWaitFor(average(listN) == 2) {insertFirst(listN, 666);};",
        "waitUntil(#black) {num=1;} orWaitFor(true) {num=2;}; ",
        "waitUntil(2) {num=1;} orWaitFor(false) {num=0;};",
        "wait ms(false);"
    );

    private static List<String> convertToStatements(List<String> expressions, String variableName) {
        return expressions.stream()
            .map(expr -> variableName + " = " + expr + ";")
            .collect(Collectors.toList());
    }

    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        Properties properties = Util.loadPropertiesRecursively("classpath:/microbitv2.properties");
        testFactory = new RobotFactory(new PluginProperties("microbitv2", "", "", properties));
        JSONObject testSpecification = Util.loadYAML(TEST_SPEC_YML);
        progDeclsFromTestSpec = testSpecification.getJSONObject("progs");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
    }

    private void setupRobotFactoryForRobot(String robotName) {
        List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
        testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
    }

    protected String setupRobotFactoryAndGetResourceDirForRobotSpecificTests(String robotName) {
        setupRobotFactoryForRobot(robotName);
        JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
        final String robotDir = robot.getString("dir");
        return CROSS_COMPILER_TESTS + ROBOT_SPECIFIC + robotDir + (robotDir.endsWith("/") ? "" : "/");
    }

    @Test
    public void testExpr() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "textly/";

        System.out.println("========= EVALUATING NUMBER EXPRESSIONS  =========");
        for ( String expression : NUMBER_EXPRESSIONS ) {

            TestTypecheckUtilModifyXmlProgram.newProgramEvalExpr(BlocklyType.NUMBER, expression, "testTextlyExpr");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyExpr.xml", EXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println("========= EVALUATING BOOLEAN EXPRESSIONS  =========");
        for ( String expression : BOOLEAN_EXPRESSIONS ) {

            TestTypecheckUtilModifyXmlProgram.newProgramEvalExpr(BlocklyType.BOOLEAN, expression, "testTextlyExpr");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyExpr.xml", EXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println("========= EVALUATING LIST EXPRESSIONS  =========");
        for ( String expression : LIST_EXPRESSIONS ) {

            TestTypecheckUtilModifyXmlProgram.newProgramEvalExpr(BlocklyType.ARRAY_NUMBER, expression, "testTextlyExpr");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyExpr.xml", EXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println("========= EVALUATING STRING EXPRESSIONS  =========");
        for ( String expression : STRING_EXPRESSIONS ) {

            TestTypecheckUtilModifyXmlProgram.newProgramEvalExpr(BlocklyType.STRING, expression, "testTextlyExpr");
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyExpr.xml", EXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

    }

    @Test
    public void testStmtsExpr() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "textly/";


        System.out.println("========= EVALUATING MICROBITV2 SPECIFIC STATEMENTS =========");
        for ( String stmts : MICROBITV2_SPECIFIC ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING FUNCTION STATEMENTS =========");
        for ( String stmts : STMT_FUNC ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING VAR ASSIGNMENT  SINGLE EXPRESSIONS STATEMENTS =========");
        for ( String stmts : STMT_VAR_ASSIGN_SIMPLE_EXPRESSIONS ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING VAR ASSIGNMENT  WITH PARSER ERRORS =========");
        for ( String stmts : STMT_VAR_ASSIGN_SIMPLE_EXPRESSIONS_ERROR_PARSER ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkParserFailResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING VAR ASSIGNMENT STATEMENTS =========");
        for ( String stmts : STMT_VAR_ASSIGN ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING CONTROL STATEMENTS =========");
        for ( String stmts : STMT_CONTROLS_STATEMENTS ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING CONTROL STATEMENTS =========");
        for ( String stmts : STMT_CONTROLS_STATEMENTS_FAIL ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckFailResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING CONTROL STATEMENTS =========");
        for ( String stmts : STMT_FUNC_FAIL ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckFailResults();
        }

        System.out.println(" ");
        System.out.println("========= EVALUATING CONTROL STATEMENTS =========");
        for ( String stmts : STMT_VAR_ASSIGN_FAIL ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckFailResults();
        }

        String robotNameWedo = "wedo";
        final String resourceDirectoryWedo = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotNameWedo);
        String folderPathWedo = resourceDirectoryWedo + "textly/";
        System.out.println(" ");
        System.out.println("========= EVALUATING SPECIFIC STATEMENTS FOR WEDO =========");
        for ( String stmts : WEDO_SPECIFIC ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotNameWedo);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPathWedo + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }
    }

    @Ignore
    @Test
    public void testSingleStmtExpr() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "textly/";

        String expression = "microbitv2.showOnSerial(\"Halloooo\");";

        TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(expression, "testTextlyStmt", robotName);
        getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
        //checkTypecheckFailResults(); // check if the stmt should fail
        checkTypecheckGoodResults(); // check if the stmt should not fail

        System.out.println("========= EVALUATING MICROBITV2 SPECIFIC STATEMENTS =========");
        for ( String stmts : MICROBITV2_SPECIFIC ) {
            TestTypecheckUtilModifyXmlProgram.newProgramStmtExpr(stmts, "testTextlyStmt", robotName);
            getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyStmt.xml", STMTEXPRESSION_PATTERN);
            checkTypecheckGoodResults();
        }

    }

    @Ignore
    @Test
    public void testSingleExpression() throws Exception {
        String robotName = "microbitv2";
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        String folderPath = resourceDirectory + "textly/";

        String expression = "sin(num) + cos(num) + tan(num)";

        TestTypecheckUtilModifyXmlProgram.newProgramEvalExpr(BlocklyType.NUMBER, expression, "testTextlyExpr");
        getProgramAstFromExportXmlAndExpr(testFactory, folderPath + "testTextlyExpr.xml", EXPRESSION_PATTERN);
        checkTypecheckFailResults(); // check if the expression should fail
        //checkTypecheckGoodResults(); // check if the expression should not fail

    }

    private static List<NepoInfo> typecheckAndCollectInfos(Phrase ast, UsedHardwareBean usedHardwareBean, String robotName) throws Exception {

        if ( robotName.equals("wedo") ) {
            WedoTypecheckVisitor visitor = new WedoTypecheckVisitor(usedHardwareBean);
            ast.accept(visitor);
            return InfoCollector.collectInfos(ast);
        } else if ( robotName.equals("microbitv2") ) {
            MicrobitV2TypecheckVisitor visitor = new MicrobitV2TypecheckVisitor(usedHardwareBean);
            ast.accept(visitor);
            return InfoCollector.collectInfos(ast);
        } else {
            LOG.info("Invalid Robot name" + robotName);
            return null;
        }
    }

    public void getProgramAstFromExportXmlAndExpr(RobotFactory factory, String xml, Pattern pattern) throws Exception {
        String exportedProgram = new String(Files.readAllBytes(Paths.get("src/test/resources/" + xml)));
        Project.Builder builder = UnitTestHelper.setupWithExportXML(factory, exportedProgram);

        List<String> expression = extractExpression(factory, exportedProgram, pattern);
        Project project = builder.build();

        UnitTestHelper.executeWorkflow("showsource", testFactory, project);
        UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);

        LOG.info("========= Evaluating the expression\n " + expression);
        for ( List<Phrase> listOfPhrases : project.getProgramAst().getTree() ) {
            for ( Phrase phrase : listOfPhrases ) {
                List<NepoInfo> infos = typecheckAndCollectInfos(phrase, usedHardwareBean, factory.getPluginProperties().getRobotName());
                for ( NepoInfo info : infos ) {
                    if ( info.getSeverity() == NepoInfo.Severity.ERROR ) {
                        if ( info.getMessage().contains("PARSE") ) {
                            parserErrorCount++;
                        }
                        if ( info.getMessage().contains("TYPECHECK") ) {
                            typecheckErrorCount++;
                        }
                    }
                }
            }
        }
    }

    private void checkTypecheckGoodResults() {
        LOG.info("Typecheck errors: " + typecheckErrorCount);
        if ( parserErrorCount > 0 ) {
            Assert.fail("Parser errors found: " + parserErrorCount);
        }
        Assert.assertTrue("There should not be any errors", typecheckErrorCount == 0);
        typecheckErrorCount = 0;
        parserErrorCount = 0;
    }

    private void checkTypecheckFailResults() {
        LOG.info("Typecheck errors: " + typecheckErrorCount);
        if ( parserErrorCount > 0 ) {
            Assert.fail("Parser errors found: " + parserErrorCount);
        }
        Assert.assertTrue("There should be typecheck errors", typecheckErrorCount > 0);
        typecheckErrorCount = 0;
        parserErrorCount = 0;
    }

    private void checkParserFailResults() {
        LOG.info("Parser errors: " + parserErrorCount);
        Assert.assertTrue("There should be parser errors", parserErrorCount > 0);
        parserErrorCount = 0;
        typecheckErrorCount = 0;
    }


    public static List<String> extractExpression(RobotFactory factory, String exportXmlAsString, Pattern pattern) {
        List<String> expressions = new ArrayList<>();
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];

        Matcher matcher = pattern.matcher(program);


        while ( matcher.find() ) {
            String expressionValue = matcher.group(1);
            expressions.add(expressionValue);
        }

        if ( expressions.isEmpty() ) {
            System.out.println("Expression not found in the program");
        }
        return expressions;
    }

}
