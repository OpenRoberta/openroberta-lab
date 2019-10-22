package de.fhg.iais.roberta.ast;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTest extends AstTest {

    @Test
    public void mathArithmetic() throws Exception {
        String a = "BlockAST [project=[[Location [x=27, y=-575], Binary [ADD, NumConst [1], MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_arithmetic.xml");
    }

    @Test
    public void reverseTransformatinArithmetic() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_arithmetic.xml");
    }

    @Test
    public void mathSingle() throws Exception {
        String a = "BlockAST [project=[[Location [x=-7, y=-604], MathSingleFunct [LN, [MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_single.xml");
    }

    @Test
    public void reverseTransformatinSingle() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_single.xml");
    }

    @Test
    public void mathSingle1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=111], Unary [NEG, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_single1.xml");
    }

    @Test
    public void reverseTransformatinSingle1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_single1.xml");
    }

    @Test
    public void mathSingle2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-667, y=112], Unary [NEG, NumConst [0]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_single2.xml");
    }

    @Test
    public void reverseTransformatinSingle2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_single2.xml");
    }

    @Test
    public void mathSingle3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=13, y=-6], MathSingleFunct [ROOT, [NumConst [0]]]], [Location [x=11, y=43], MathSingleFunct [ABS, [NumConst [0]]]], [Location [x=18, y=104], Unary [NEG, NumConst [0]]], [Location [x=20, y=164], MathSingleFunct [LN, [NumConst [0]]]], [Location [x=22, y=233], MathSingleFunct [LOG10, [NumConst [0]]]], [Location [x=17, y=304], MathSingleFunct [EXP, [NumConst [0]]]], [Location [x=19, y=364], MathSingleFunct [POW10, [NumConst [0]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_single3.xml");
    }

    @Test
    public void reverseTransformatinSingle3() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_single3.xml");
    }

    @Test
    public void mathTrig() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=41, y=-491], MathSingleFunct [ATAN, [FunctionExpr [MathSingleFunct [LN, [MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]]]]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_trig.xml");
    }

    @Test
    public void reverseTransformatinTrig() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_trig.xml");
    }

    @Test
    public void mathTrig1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-15, y=173], MathSingleFunct [SIN, [NumConst [0]]]], [Location [x=5, y=213], MathSingleFunct [COS, [NumConst [0]]]], [Location [x=25, y=253], MathSingleFunct [TAN, [NumConst [0]]]], [Location [x=45, y=293], MathSingleFunct [ASIN, [NumConst [0]]]], [Location [x=65, y=333], MathSingleFunct [ACOS, [NumConst [0]]]], [Location [x=85, y=373], MathSingleFunct [ATAN, [NumConst [0]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_trig1.xml");
    }

    @Test
    public void reverseTransformatinTrig1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_trig1.xml");
    }

    @Test
    public void mathConstant() throws Exception {
        String a = "BlockAST [project=[[Location [x=131, y=-615], MathSingleFunct [COS, [MathConst [E]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_constant.xml");
    }

    @Test
    public void reverseTransformatinConstant() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant.xml");
    }

    @Test
    public void mathConstant4() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=94, y=154], MathConst [PI]], [Location [x=114, y=194], MathConst [E]], [Location [x=134, y=234], MathConst [GOLDEN_RATIO]], [Location [x=154, y=274], MathConst [SQRT2]], [Location [x=174, y=314], MathConst [SQRT1_2]], [Location [x=194, y=354], MathConst [INFINITY]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_constant4.xml");
    }

    @Test
    public void reverseTransformatinConstant4() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constant4.xml");
    }

    @Test
    public void mathNumberProperty() throws Exception {
        String a = "BlockAST [project=[[Location [x=34, y=-547], MathNumPropFunct [PRIME, [NumConst [0]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_number_property.xml");
    }

    @Test
    public void reverseTransformatinNumberProperty() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_number_property.xml");
    }

    @Test
    public void mathNumberProperty1() throws Exception {
        String a = "BlockAST [project=[[Location [x=64, y=-525], MathNumPropFunct [DIVISIBLE_BY, [NumConst [8], NumConst [5]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_number_property1.xml");
    }

    @Test
    public void reverseTransformatinNumberProperty1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_number_property1.xml");
    }

    @Test
    public void mathNumberProperty2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-683, y=152], MathNumPropFunct [EVEN, [NumConst [0]]]], [Location [x=-663, y=192], MathNumPropFunct [ODD, [NumConst [0]]]], [Location [x=-675, y=232], MathNumPropFunct [PRIME, [NumConst [0]]]], [Location [x=-676, y=268], MathNumPropFunct [WHOLE, [NumConst [0]]]], [Location [x=-659, y=311], MathNumPropFunct [POSITIVE, [NumConst [0]]]], [Location [x=-583, y=352], MathNumPropFunct [NEGATIVE, [NumConst [0]]]], [Location [x=-603, y=541], MathNumPropFunct [DIVISIBLE_BY, [NumConst [0], NumConst [10]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_number_property2.xml");
    }

    @Test
    public void reverseTransformatinNumberProperty2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_number_property2.xml");
    }

    @Ignore
    public void mathChange() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=182, y=34], MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, variablenName, NumConst [0], false, true]], \n"
                + "exprStmt Binary [MATH_CHANGE, Var [variablenName], NumConst [1]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_change.xml");
    }

    @Ignore
    public void reverseTransformationMathChange() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_change.xml");
    }

    @Test
    public void mathRound() throws Exception {
        String a = "BlockAST [project=[[Location [x=34, y=-492], MathSingleFunct [ROUNDUP, [NumConst [0]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_round.xml");
    }

    @Test
    public void reverseTransformationMathRound() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_round.xml");
    }

    @Test
    public void mathRound1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-688, y=90], MathSingleFunct [ROUND, [NumConst [0]]]], [Location [x=-668, y=130], MathSingleFunct [ROUNDUP, [NumConst [0]]]], [Location [x=-648, y=170], MathSingleFunct [ROUNDDOWN, [NumConst [0]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_round1.xml");
    }

    @Test
    public void reverseTransformationMathRound1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_round1.xml");
    }

    @Test
    public void math_on_list() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-714, y=201], MathOnListFunct [SUM, [EmptyList [NUMBER]]]], [Location [x=-694, y=241], MathOnListFunct [MIN, [EmptyList [NUMBER]]]], [Location [x=-674, y=281], MathOnListFunct [MAX, [EmptyList [NUMBER]]]], [Location [x=-654, y=321], MathOnListFunct [AVERAGE, [EmptyList [NUMBER]]]], [Location [x=-634, y=361], MathOnListFunct [MEDIAN, [EmptyList [NUMBER]]]], [Location [x=-614, y=401], MathOnListFunct [MODE, [EmptyList [NUMBER]]]], [Location [x=-594, y=441], MathOnListFunct [STD_DEV, [EmptyList [NUMBER]]]], [Location [x=-574, y=481], MathOnListFunct [RANDOM, [EmptyList [NUMBER]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_on_list.xml");
    }

    @Test
    public void reverseTransformationMathOnList() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_on_list.xml");
    }

    @Test
    public void math_on_constrain() throws Exception {
        String a = "BlockAST [project=[[Location [x=-44, y=-399], MathConstrainFunct [[NumConst [8], NumConst [1], NumConst [100]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_constrain.xml");
    }

    @Test
    public void reverseTransformationMathOnConstrain() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_constrain.xml");
    }

    @Test
    public void math_random_float() throws Exception {
        String a = "BlockAST [project=[[Location [x=-140, y=265], \nVar [item] := FunctionExpr [MathRandomFloatFunct []]\n]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_random_float.xml");
    }

    @Test
    public void reverseTransformationMathRandomFloat() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_random_float.xml");
    }

    @Test
    public void math_modulo() throws Exception {
        String a = "BlockAST [project=[[Location [x=-36, y=424], Binary [MOD, NumConst [10], NumConst [2]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_modulo.xml");
    }

    @Test
    public void reverseTransformationMathModulo() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_modulo.xml");
    }

    @Test
    public void math_random_integer() throws Exception {
        String a = "BlockAST [project=[[Location [x=-73, y=616], MathRandomIntFunct [[NumConst [1], NumConst [100]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/math/math_random_integer.xml");
    }

    @Test
    public void reverseTransformationRandomInteger() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/math/math_random_integer.xml");
    }
}
