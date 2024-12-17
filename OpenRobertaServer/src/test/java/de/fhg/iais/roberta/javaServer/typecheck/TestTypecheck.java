package de.fhg.iais.roberta.javaServer.typecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfoProcessor;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

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
 * - `listColor`: Represents a list of Colors.
 * - `conn`: Represents a connection.
 * - `listConn`: Represents a list of Connections.
 * <p>
 * This allows for quick and consistent testing of various expressions and statements within the context
 * of the Blockly programs being evaluated.
 * <p>
 * It also performs checks to ensure that certain expressions or statements result in expected
 * type-checking errors (negative tests).
 */
public class TestTypecheck {
    private static final Logger LOG = LoggerFactory.getLogger(TestTypecheck.class);
    private static final boolean SHOW_MESSAGES = true;

    private static Map<String, RobotFactory> robotFactories = new HashMap<>();

    private static List<String> ROBOTS_ALL = Arrays.asList("microbitv2", "ev3lejosv1", "wedo");
    private static List<String> ROBOTS_WITH_LISTS = Arrays.asList("microbitv2", "ev3lejosv1");

    public static final String TEST_SPEC_YML = "classpath:/crossCompilerTests/testSpec.yml";

    private static JSONObject robotsFromTestSpec;
    private static JSONObject progDeclsFromTestSpec;

    private RobotFactory testFactory;

    private int typecheckErrorCount = 0;
    private int parserErrorCount = 0;
    private List<String> messages = null;

    private static int tp = 0;
    private static int tn = 0;
    private static int fp = 0;
    private static int fn = 0;

    private static class TC {
        public final BlocklyType resultType;
        public final String underTest;
        public final List<String> robotsToBeTested;

        private TC(BlocklyType resultType, String underTest, List<String> robotsToBeTested) {
            this.resultType = resultType;
            this.underTest = underTest;
            this.robotsToBeTested = robotsToBeTested;
        }

        public static TC of(BlocklyType resultType, String underTest, List<String> robotsToBeTested) {
            return new TC(resultType, underTest, robotsToBeTested);
        }
    }

    /**
     * tests common to all robot plugins (as implemented in TypecheckCommonLanguageVisitor)
     * executed for the microbitv2 plugin
     */
    public static final List<TC> EXPRESSIONS = Arrays.asList(
        TC.of(BlocklyType.NUMBER, "sin(num) + cos(num) + tan(num)", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "exp(2) + square(4) + sqrt(9) + abs(-5) + log10(100) + log(2)", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "randomInt(1, 10) + randomFloat()", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "floor(3.7) + ceil(2.3) + round(4.6)", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "pow10(2) + (10%3)", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "num + 1", ROBOTS_ALL),
        TC.of(BlocklyType.NUMBER, "sum(listN) + max(listN) - min(listN)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.NUMBER, "get(listN, 1)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.NUMBER, "castToNumber(\"2\")", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.NUMBER, "castStringToNumber(str,1)", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.BOOLEAN, "isEven(10) && isOdd(7) || isPrime(11) && isWhole(8)", ROBOTS_ALL),
        TC.of(BlocklyType.BOOLEAN, "isEmpty(listN)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.BOOLEAN, "isPositive(5) || isNegative(-3)", ROBOTS_ALL),
        TC.of(BlocklyType.BOOLEAN, "(num > 5) && (num <= 10)", ROBOTS_ALL),
        TC.of(BlocklyType.BOOLEAN, "!boolF", ROBOTS_ALL),
        TC.of(BlocklyType.BOOLEAN, "(num == 10) || (num != 5)", ROBOTS_ALL),

        TC.of(BlocklyType.ARRAY_NUMBER, "subList(listN2, 0, 3)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "subListFromIndexToEnd(listN2, 0, 1)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "subListFromEndToIndex(listN2, 1, 1)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "subListFromFirstToLast(listN2)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "createListWith(getFromEnd(listN, 0),2)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "createListWith(getLast(listN),2)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "subListFromEndToLast(listN, 3)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.ARRAY_NUMBER, "createEmptyList(Number)", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.STRING, "createTextWith(\"Hello\", num)", ROBOTS_ALL),
        TC.of(BlocklyType.STRING, "\"Hello World!\"", ROBOTS_ALL),
        TC.of(BlocklyType.STRING, "createTextWith(sin(num), cos(num))", ROBOTS_ALL),
        TC.of(BlocklyType.STRING, "createTextWith(getFirst(listN), get(listN, 1))", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.STRING, "castToString(5)", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.STRING, "castToChar(65)", ROBOTS_WITH_LISTS)
    );

    public static final List<TC> GENERIC_STMTS = Arrays.asList(
        TC.of(BlocklyType.VOID, "set(listN, 1000,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setFromEnd(listN, 1000,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setFirst(listN,666);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setLast(listN,666);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "insert(listN, 1000,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertFromEnd(listN, 1000,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertFirst(listN,666);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertLast(listN,666);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "remove(listN,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeFromEnd(listN,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeFirst(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeLast(listN);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "changeBy(num,2);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "appendText(str, \"aaa\");", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "appendText(str, str);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "appendText(str, 1-4);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "appendText(str, true||false);", ROBOTS_ALL),

        // very simple cases
        TC.of(BlocklyType.VOID, ";", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "// comment", ROBOTS_ALL),
        // Number expressions
        TC.of(BlocklyType.VOID, "num = num;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 1;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 1.4;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = pi;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = sqrt_1_2;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = randomInt(1, 100);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = randomFloat();", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = sin(num);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = (sin(45) + cos(45)) * tan(60);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = exp(num);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = log10(num) * (square(10));", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = ceil(3.782) + floor(3.1782);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = round(phi);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = min(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = max(listN) * average(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = sum(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = randomItem(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = size(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfFirst(listN, 2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfLast(listN, 2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = getFirst(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = getAndRemove(listN, 2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = constrain(102, 1, 100);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = ((((3))));", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 2^2;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = num^num;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 5%2;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 10*10;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 100/10;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = 1+2;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = pi + e;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = pi + 4.5;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = randomFloat() + cos(num);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = +-1;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = -+1;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = true ? 1 : 2;", ROBOTS_ALL),

        // Boolean expressions
        TC.of(BlocklyType.VOID, "boolT = boolF;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = true;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = !!!!true;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = (num > 4) ? (num == 2) : (num + 1 == num + 3);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isPositive(----6);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isDivisibleBy(6, 2);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isWhole(6.21234);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isPrime(6);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isEmpty(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "boolT = !boolF;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = boolT || true;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = num == num;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = 7 == 7;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = str == str;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = num > 0;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = pi > 20;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = num >= num;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = 10 > 0;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = true || false;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = num > 4;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = num != 5;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = (num == 10) || (num != 5);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = 1 < 20;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = 2 <= 3;", ROBOTS_ALL),

        // String expressions
        TC.of(BlocklyType.VOID, "str = \"OpenRoberta is awesome!\";", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "str = createTextWith(str, 12, true);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "str = createTextWith(true, 'true', 12);", ROBOTS_ALL),

        TC.of(BlocklyType.VOID, "num=sin(num)+cos(num)+tan(num)+asin(num)+acos(num)+atan(num);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=exp(2) + square(4) + sqrt(9) + abs(-5) + log10(100) + log(2);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=randomInt(1,10)+ randomFloat();", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num= floor(3.7) + ceil(2.3) + round(4.6);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isEven(10) && isOdd(7) || isPrime(11) && isWhole(8) && isPositive(5) || isNegative(-3) && isDivisibleBy(10, 5);", ROBOTS_ALL),

        TC.of(BlocklyType.VOID, "num = sum(listN) + max(listN) - min(listN) * average(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = median(listN) + stddev(listN) % size(listN) + randomItem(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfFirst(listN, 0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfLast(listN, 0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num= getFirst(listN)+get(listN,1)+ getFromEnd(listN,0)+ getLast(listN);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "str = createTextWith(getFirst(listN), get(listN,1), getFromEnd(listN,2), get(listN,3), getLast(listN));", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "num = getAndRemoveFirst(listN2) + getAndRemove(listN2, 1);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = getAndRemoveFromEnd(listN2, 0) + getAndRemoveLast(listN2);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "listN2 = subList(listN, 0, 3);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromIndexToEnd(listN, 0, 1);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromEndToIndex(listN, 1, 1);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromEndToEnd(listN, 4, 2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromIndexToLast(listN, 1);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromFirstToIndex(listN, 2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromFirstToLast(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromFirstToIndex(listN, 3);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN2 = subListFromEndToLast(listN, 3);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "str = castToString(5);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "str = castToChar(65);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = castToNumber(\"2\");", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = castStringToNumber(str,1);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i++){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i=i+1){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i=i+2+4){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i=num+1){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for each( Number item: listN){num=1;};", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "if(true){num=1;} else if(false){num=2;} else{num=5;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "if(boolT||boolF){num=1;} else if(false){num=2;} else{num=5;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(true){num=1;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(boolT==true){num=2;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while((boolT==true) && (boolF==false)){num=2;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for (Number i = 0; i < 10; i = i + 2) { if (isEven(i)) {num = num + i*2;} else {num = num - i;}; };", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "waitUntil(num == 5) {num = 6;} orWaitFor(num == 2) {num = num + 1;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "waitUntil(false) {num=1;} orWaitFor(true) {num=2;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "wait ms(500);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(true){break;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(true){continue;};", ROBOTS_ALL)
    );

    private static final Map<String, List<String>> MICROBITV2_SPECIFIC = new HashMap<>();
    static {
        MICROBITV2_SPECIFIC.put("default", Arrays.asList(
            //"ima = image(heart); ima = image(heartSmall); ima = image(happy); ima = image(smile); ima = image(sad); ima = image(confused); ima = image(angry); ima = image(asleep); ima = image(surprised); ima = image(silly); ima = image(fabulous); ima = image(meh); ima = image(yes); ima = image(no); ima = image(triangle); ima = image(triangleLeft); ima = image(chessboard); ima = image(diamond); ima = image(diamondSmall); ima = image(squareBig);",
            //"ima = image(squareSmall); ima = image(rabbit); ima = image(cow); ima = image(musicCrotchet); ima = image(musicQuaver); ima = image(musicQuavers); ima = image(pitchfork); ima = image(xmas); ima = image(pacman); ima = image(target); ima = image(tshirt); ima = image(rollerskate); ima = image(duck); ima = image(house); ima = image(tortoise); ima = image(butterfly); ima = image(stickFigure); ima = image(ghost); ima = image(sword);",
            //"ima = image(giraffe); ima = image(skull); ima = image(umbrella); ima = image(snake);",
            "ima = image(heart);",
            "ima = image(heartSmall);",
            "ima = image(happy);",
            "ima = image(smile);",
            "ima = image(sad);",
            "ima = image(confused);",
            "ima = image(angry);",
            "ima = image(asleep);",
            "ima = image(surprised);",
            "ima = image(silly);",
            "ima = image(fabulous);",
            "ima = image(meh);",
            "ima = image(yes);",
            "ima = image(no);",
            "ima = image(triangle);",
            "ima = image(triangleLeft);",
            "ima = image(chessboard);",
            "ima = image(diamond);",
            "ima = image(diamondSmall);",
            "ima = image(squareBig);",
            "ima = image(squareSmall);",
            "ima = image(rabbit);",
            "ima = image(cow);",
            "ima = image(musicCrotchet);",
            "ima = image(musicQuaver);",
            "ima = image(musicQuavers);",
            "ima = image(pitchfork);",
            "ima = image(xmas);",
            "ima = image(pacman);",
            "ima = image(target);",
            "ima = image(tshirt);",
            "ima = image(rollerskate);",
            "ima = image(duck);",
            "ima = image(house);",
            "ima = image(tortoise);",
            "ima = image(butterfly);",
            "ima = image(stickFigure);",
            "ima = image(ghost);",
            "ima = image(sword);",
            "ima = image(giraffe);",
            "ima = image(skull);",
            "ima = image(umbrella);",
            "ima = image(snake);",
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
        ));
    }
    private static final Map<String, List<String>> WEDO_SPECIFIC = new HashMap<>();
    static {
        WEDO_SPECIFIC.put("default", Arrays.asList(
            "color = color;",
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
        ));
        WEDO_SPECIFIC.put("configuration2", Arrays.asList(
            "boolT = wedo.gyroSensor.isTilted(T2,up);",
            "boolT = wedo.gyroSensor.isTilted(T2,down);",
            "boolT = wedo.gyroSensor.isTilted(T2,back);",
            "boolT = wedo.gyroSensor.isTilted(T2,front);",
            "boolT = wedo.gyroSensor.isTilted(T2,no);",
            "boolT = wedo.gyroSensor.isTilted(T2,any);"
        ));
    }
    private static final Map<String, List<String>> EV3_SPECIFIC = new HashMap<>();
    static {
        EV3_SPECIFIC.put("default", Arrays.asList(
            //ACTUATORS
            "ev3.turnOnRegulatedMotor(B,30);",
            "ev3.rotateRegulatedMotor(B, 30, rotations, 1);",
            "ev3.rotateRegulatedMotor(B,30,degree,1);",
            "ev3.rotateRegulatedMotor(B,30,degree,1);",
            "num =ev3.getSpeedMotor(B);",
            "ev3.setRegulatedMotorSpeed(B,30);",
            "ev3.stopRegulatedMotor(B,float);",
            "ev3.stopRegulatedMotor(B,brake);",
            "ev3.driveDistance(forward,30,20);",
            "ev3.driveDistance(forward,30);",
            "ev3.driveDistance(backward,30,20);",
            "ev3.driveDistance(backward,30);",
            "ev3.stopRegulatedDrive();",
            "ev3.rotateDirectionAngle(right,30,20);",
            "ev3.rotateDirectionRegulated(right,30);",
            "ev3.rotateDirectionAngle(left,30,20);",
            "ev3.rotateDirectionRegulated(left,30);",
            "ev3.driveInCurve(forward,10,30,20);",
            "ev3.driveInCurve(backward,10,30,20);",
            "ev3.driveInCurve(forward,10,30);",
            "ev3.driveInCurve(backward,10,30);",
            "ev3.drawText(\"Hallo\",0,0);",
            "ev3.drawText(123, 0, 0);",
            "ev3.drawPicture(oldGlasses);",
            "ev3.drawPicture(eyesOpen);",
            "ev3.drawPicture(eyesClosed);",
            "ev3.drawPicture(flowers);",
            "ev3.drawPicture(speedo);",
            "ev3.clearDisplay();",
            "ev3.playTone(300,100);",
            "ev3.playFile(1);",
            "ev3.playFile(2);",
            "ev3.playFile(3);",
            "ev3.playFile(4);",
            "ev3.playFile(5);",
            "ev3.setVolume(50);",
            "num =ev3.getVolume();",
            "ev3.setLanguage(de);",
            "ev3.setLanguage(en);",
            "ev3.setLanguage(fr);",
            "ev3.setLanguage(es);",
            "ev3.setLanguage(it);",
            "ev3.setLanguage(nl);",
            "ev3.setLanguage(fi);",
            "ev3.setLanguage(pl);",
            "ev3.setLanguage(ru);",
            "ev3.setLanguage(te);",
            "ev3.setLanguage(cs);",
            "ev3.setLanguage(pt);",
            "ev3.setLanguage(da);",
            "ev3.sayText(\"Hallo\");",
            "ev3.sayText(123);",
            "ev3.sayText(\"Hallo\",30,50);",
            "ev3.sayText(123,30,50);",
            "ev3.ledOn(on,green);",
            "ev3.ledOn(on,orange);",
            "ev3.ledOn(on,red);",
            "ev3.ledOn(flashing,green);",
            "ev3.ledOn(flashing,orange);",
            "ev3.ledOn(flashing,red);",
            "ev3.ledOn(doubleFlashing,green);",
            "ev3.ledOn(doubleFlashing,orange);",
            "ev3.ledOn(doubleFlashing,red);",
            "ev3.ledOff();",
            "ev3.resetLed();",
            //SENSORS
            "boolT = ev3.touchSensor.isPressed(1);",
            "num = ev3.ultrasonicSensor.getDistance(4);",
            "boolT = ev3.ultrasonicSensor.getPresence(4);",
            "color = ev3.colorSensor(colour,3);",
            "num = ev3.colorSensor(light,3);",
            "num = ev3.colorSensor(ambientlight,3);",
            "listN = ev3.colorSensor(rgb,3);",
            "ev3.encoderReset(B);",
            "num =ev3.encoderSensor.getDegree(B);",
            "num =ev3.encoderSensor.getRotation(B);",
            "num =ev3.encoderSensor.getDistance(B);",
            "boolT =ev3.keysSensor.isPressed(enter);",
            "boolT =ev3.keysSensor.isPressed(up);",
            "boolT =ev3.keysSensor.isPressed(down);",
            "boolT =ev3.keysSensor.isPressed(left);",
            "boolT =ev3.keysSensor.isPressed(right);",
            "boolT =ev3.keysSensor.isPressed(escape);",
            "boolT =ev3.keysSensor.isPressed(any);",
            "ev3.gyroReset(2);",
            "num =ev3.gyroSensor.getAngle(2);",
            "num =ev3.gyroSensor.getRate(2);",
            "num =ev3.timerSensor(1);",
            "num =ev3.timerSensor(5);",
            "ev3.timerReset(1);",
            "ev3.timerReset(5);",
            "ev3.setInputNeuron(n1,2);",
            "num = ev3.getOutputNeuron(n2);",
            "ev3.setWeight(n1,n2,3);",
            "ev3.setBias(n2,4);",
            "num = ev3.getWeight(n1,n2);",
            "num = ev3.getBias(n2);",
            " ev3.nnStep();",
            //CONNECTIONS
            "conn = ev3.connectToRobot(\"hola\");",
            "ev3.sendMessage(\"hola\",conn);",
            "str =ev3.receiveMessage(conn);",
            "conn =ev3.waitForConnection();"
        ));
        EV3_SPECIFIC.put("configuration2", Arrays.asList(
            "num = ev3.infraredSensor.getDistance(1);",
            "listN =ev3.infraredSensor.getPresence(1);",
            "num =ev3.soundSensor.getSoundLevel(2);",
            "ev3.hiTecCompassStartCalibration(3);",
            "num =ev3.hiTechCompassSensor.getAngle(3);",
            "num =ev3.hiTechCompassSensor.getCompass(3);",
            "num = ev3.hiTechInfraredSensor.getModulated(4);",
            "num = ev3.hiTechInfraredSensor.getUnmodulated(4);"
        ));
        EV3_SPECIFIC.put("configuration3", Arrays.asList(
            "color = ev3.hiTechColorSensor(colour,4);",
            "num = ev3.hiTechColorSensor(light, 4);",
            "num =ev3.hiTechColorSensor(ambientlight,4);",
            "listN =ev3.hiTechColorSensor(rgb,4);"
        ));

    }
    List<String> PARSER_ERRORS = Arrays.asList(
        "num = 1,,;",
        "num = 1",
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
        "boolT = (7 == );",
        "undefinedVar = ;"
    );

    public static final List<TC> TYPECHECK_ERRORS = Arrays.asList(
        //FNAMESTMT:
        TC.of(BlocklyType.VOID, "set(listN, \"hola\",0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setFromEnd(listN, 1000,0,2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setFirst(listN,false);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "setLast(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insert(listN, true,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertFromEnd(listN, 1000);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertFirst(listN,666,9);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insertLast(listN,666,true);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "remove(listN,0,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeFromEnd(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeFirst(listN,true);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeLast();", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "set(4, 1000,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "removeFromEnd(3,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "insert(3, 1000,0);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "appendText(str, listN);", ROBOTS_WITH_LISTS),

        //EXPRESSIONS
        TC.of(BlocklyType.VOID, "num=true;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=str;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=sin(num+true)+cos(num)+tan(num)+asin(num)+acos(num)+atan(num);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=exp(2) + square(#black) + sqrt(9) + abs(-5) + log10(100) + log(2);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num=randomInt(1,10,0)+ randomFloat(1);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num= floor(3.7) + ceil(2.3) + round(true);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "boolT = isEven(10) && isOdd(7,0) || isPrime(11,0) && isWhole(8) && isPositive(5) || isNegative(-3) && isDivisibleBy(10, 5);", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = sum(listN) + max(listN) - min(listN) * average(listN) / median(listN) + stddev(listN) % size(listN+2) + randomItem(listN,0);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfFirst(listN);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = indexOfLast(listN, true);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "str = createTextWith(sin(true));", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = getFirst(listN) +get(listN,1)+getFromEnd(listN,0,1)+getLast(listN,true);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "num = getAndRemoveFirst(listN,0) + getAndRemove(listN, 1,2) + getAndRemoveFromEnd(listN, 0,true) + getAndRemoveLast(listN,false);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "listN = subList(listN2,0,true);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromIndexToEnd(listN2,#black,1);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromEndToIndex(listN2,1,1,2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromEndToEnd(listN2,4,2,false);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromIndexToLast(listN2,1,3);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromFirstToIndex();", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromFirstToLast(listN2,2);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromFirstToIndex(listN2,false);", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "listN = subListFromEndToLast(listN2,3,3);", ROBOTS_WITH_LISTS),

        TC.of(BlocklyType.VOID, "undefinedVariable = \"str\";", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "num = undefinedVariable;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "undefinedVariable = undefinedVariable;", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "appendText(str, undefinedVariable);", ROBOTS_ALL),

        TC.of(BlocklyType.VOID, "for ( Boolean i=1; i<10; i++){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i=true){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( String i=1; i<10; i=i+2+4){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<1+true; i++){};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for ( Number i=1; i<10; i=num+true){};", ROBOTS_ALL),

        TC.of(BlocklyType.VOID, "for each( Boolean item: listN){num=1;};", ROBOTS_WITH_LISTS),
        TC.of(BlocklyType.VOID, "for each( Number item: true){num=1;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "if(5){num=1;} else if(false){num=2;} else{num=5;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "if(boolT||[0,0,0]){num=1;} else if(false){num=2;} else{num=5;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(boolT==2){num=1;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(boolT==2+2){num=2;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while(2){num=1;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "while((boolT==#black) && (boolF==false)){num=2;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "for (Number i = 0; i < 10; i = i + 2) { if (isEven(i+ #black)) {num = 1;} else {num = 2;}; };", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "waitUntil(num == true) {num = 2;} orWaitFor(boolT) {num = 4;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "waitUntil(#black) {num=1;} orWaitFor(true) {num=2;}; ", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "waitUntil(2) {num=1;} orWaitFor(false) {num=0;};", ROBOTS_ALL),
        TC.of(BlocklyType.VOID, "wait ms(false);", ROBOTS_ALL)
    );

    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        JSONObject testSpecification = Util.loadYAML(TEST_SPEC_YML);
        progDeclsFromTestSpec = testSpecification.getJSONObject("progs");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
    }

    @BeforeClass
    public static void resetCounters() {
        tp = 0;
        tn = 0;
        fp = 0;
        fn = 0;
    }

    @AfterClass
    public static void printMetricsSummary() {
        int totalTests = tp + tn + fp + fn;
        double accuracy = (double) (tp + tn) / totalTests;
        double precision = tp + fp > 0 ? (double) tp / (tp + fp) : 1.0;
        double recall = tp + fn > 0 ? (double) tp / (tp + fn) : 1.0;
        double f1Score = precision + recall > 0 ? 2 * (precision * recall) / (precision + recall) : 1.0;

        System.out.println("==== METRICS SUMMARY ====");
        System.out.printf("Total Tests: %d%n", totalTests);
        System.out.printf("True Positives (TP): %d%n", tp);
        System.out.printf("True Negatives (TN): %d%n", tn);
        System.out.printf("False Positives (FP): %d%n", fp);
        System.out.printf("False Negatives (FN): %d%n", fn);
        System.out.printf("Accuracy: %.2f%n", accuracy);
        System.out.printf("Precision: %.2f%n", precision);
        System.out.printf("Recall: %.2f%n", recall);
        System.out.printf("F1 Score: %.2f%n", f1Score);
    }

    private void setupRobotFactoryForRobot(String robotName) {
        testFactory = robotFactories.getOrDefault(robotName, null);
        if ( testFactory == null ) {
            List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
            testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
            robotFactories.put(robotName, testFactory);
        }
    }

    @Test
    public void testSuccessExpr() throws Exception {
        for ( TC testSpec : EXPRESSIONS ) {
            for ( String robotName : testSpec.robotsToBeTested ) {
                setupRobotFactoryForRobot(robotName);
                LOG.info(String.format("%-16s t: %-16s e: %s", robotName, testSpec.resultType, testSpec.underTest));
                String xmlUnderTest1 = TestTypecheckUtil.getProgramUnderTestForEvalExpr(testFactory, testSpec.resultType, testSpec.underTest);
                typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest1);
                checkMustSucceed();
                String statement = TestTypecheckUtil.convertToStatement(testSpec.underTest, testSpec.resultType);
                String xmlUnderTest2 = TestTypecheckUtil.getProgramUnderTestForEvalStmt(testFactory, statement);
                typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest2);
                checkMustSucceed();
                tp++;
            }
        }
    }

    @Test
    public void testSuccessStmts() throws Exception {
        for ( TC testSpec : GENERIC_STMTS ) {
            for ( String robotName : testSpec.robotsToBeTested ) {
                setupRobotFactoryForRobot(robotName);
                LOG.info(String.format("%-16s t: %-16s s: %s", robotName, testSpec.resultType, testSpec.underTest));
                String xmlUnderTest = TestTypecheckUtil.getProgramUnderTestForEvalStmt(testFactory, testSpec.underTest);
                typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
                checkMustSucceed();
                tp++;
            }
        }
    }

    @Test
    public void testSuccessSpecificRobottestSuccessSpecificRobot() throws Exception {
        Map<String, Map<String, List<String>>> robotsWithSpecificTests = new HashMap<>();
        robotsWithSpecificTests.put("ev3lejosv1", EV3_SPECIFIC);
        robotsWithSpecificTests.put("microbitv2", MICROBITV2_SPECIFIC);
        robotsWithSpecificTests.put("wedo", WEDO_SPECIFIC);

        for ( Map.Entry<String, Map<String, List<String>>> robotEntry : robotsWithSpecificTests.entrySet() ) {
            String robotName = robotEntry.getKey();
            Map<String, List<String>> specificExpressions = robotEntry.getValue();
            setupRobotFactoryForRobot(robotName);

            for ( Map.Entry<String, List<String>> configEntry : specificExpressions.entrySet() ) {
                String configKey = configEntry.getKey();
                List<String> expressions = configEntry.getValue();


                for ( String expression : expressions ) {
                    LOG.info(String.format("Testing %s with %s configuration: %s", robotName, configKey, expression));
                    String xmlUnderTest = TestTypecheckUtil.getProgramWithConfiguration(testFactory, expression, configKey);
                    typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
                    checkMustSucceed();
                    tp++;
                }
            }
        }
    }

    /**
     * parser errors are generic and tested for all robot plugins only by using microbitv2
     */
    @Test
    public void testParserErrors() throws Exception {
        String robotName = "microbitv2";
        setupRobotFactoryForRobot(robotName);
        LOG.info("");
        LOG.info("========= EVALUATING for robot " + robotName + " (generic) parser errors");

        for ( String underTest : PARSER_ERRORS ) {
            LOG.info(underTest);
            String xmlUnderTest = TestTypecheckUtil.getProgramUnderTestForEvalStmt(testFactory, underTest);
            typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
            checkMustHaveParseErrors();
            tn++;
        }
    }

    @Test
    public void testTypecheckErrors() throws Exception {
        LOG.info("");
        LOG.info("========= TESTING typecheck errors");
        for ( TC testSpec : TYPECHECK_ERRORS ) {
            for ( String robotName : testSpec.robotsToBeTested ) {
                setupRobotFactoryForRobot(robotName);
                LOG.info(String.format("%-16s t: %-16s s: %s", robotName, testSpec.resultType, testSpec.underTest));
                String xmlUnderTest = TestTypecheckUtil.getProgramUnderTestForEvalStmt(testFactory, testSpec.underTest);
                typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
                checkMustHaveTypeErrors();
                tn++;
            }
        }
    }

    @Ignore
    @Test
    public void testSingleStmt() throws Exception {
        String robotName = "ev3lejosv1";
        setupRobotFactoryForRobot(robotName);
        String statement = "ev3.ledOn(on,green);";
        LOG.info("expect a typecheck error for robot: " + robotName + " statement: " + statement);

        String xmlUnderTest = TestTypecheckUtil.getProgramUnderTestForEvalStmt(testFactory, statement);
        typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
        //checkMustHaveTypeErrors(); // check if the stmt should fail
        checkMustSucceed(); // check if the stmt should not fail
    }

    @Ignore
    @Test
    public void testSingleExpression() throws Exception {
        String robotName = "microbitv2";
        BlocklyType expectedType = BlocklyType.NUMBER;
        String expectedResult = "ok"; // "ok" or "typeerror"
        String expression = "sin(num) + cos(num) + tan(num)";

        setupRobotFactoryForRobot(robotName);
        String xmlUnderTest = TestTypecheckUtil.getProgramUnderTestForEvalExpr(testFactory, expectedType, expression);
        typecheckAndCollectInfosForProgram(testFactory, xmlUnderTest);
        if ( expectedResult.equals("ok") ) {
            checkMustSucceed();
        } else if ( expectedResult.equals("typeerror") ) {
            checkMustHaveTypeErrors();
        } else {
            Assert.fail("expected result invalid");
        }
    }

    private void typecheckAndCollectInfosForProgram(RobotFactory factory, String xmlUnderTest) throws Exception {
        typecheckErrorCount = 0;
        parserErrorCount = 0;
        messages = new ArrayList<>();

        Project.Builder builder = UnitTestHelper.setupWithExportXML(factory, xmlUnderTest);
        Project project = builder.setLanguage(Language.ENGLISH).setRobot(factory.getPluginProperties().getRobotName()).build();
        ProjectService.executeWorkflow("showsource", project);

        for ( List<Phrase> listOfPhrases : project.getProgramAst().getTree() ) {
            for ( Phrase phrase : listOfPhrases ) {
                List<NepoInfo> infos = NepoInfoProcessor.collectNepoInfos(phrase);
                for ( NepoInfo info : infos ) {
                    if ( info.getSeverity() == NepoInfo.Severity.ERROR ) {
                        if ( info.getMessage().contains("PARSE") ) {
                            messages.add(info.getMessage());
                            parserErrorCount++;
                        } else {
                            messages.add(info.getMessage());
                            typecheckErrorCount++;
                        }
                    }
                }
            }
        }
    }

    private void checkMustSucceed() {
        if ( parserErrorCount > 0 || typecheckErrorCount > 0 || SHOW_MESSAGES ) {
            for ( String message : messages ) {
                LOG.info(message);
            }
        }
        Assert.assertTrue("unexpected parser error", parserErrorCount == 0);
        Assert.assertTrue("unexpected typecheck error", typecheckErrorCount == 0);
    }

    private void checkMustHaveTypeErrors() {
        if ( SHOW_MESSAGES ) {
            for ( String message : messages ) {
                LOG.info(message);
            }
        }
        Assert.assertTrue("unexpected parser error", parserErrorCount == 0);
        Assert.assertTrue("typecheck error was expected", typecheckErrorCount != 0);
    }

    private void checkMustHaveParseErrors() {
        if ( SHOW_MESSAGES ) {
            for ( String message : messages ) {
                LOG.info(message);
            }
        }
        Assert.assertTrue("parse error was expected", parserErrorCount != 0);
    }
}
