package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class MathTest {

    @Test
    public void mathArithmetic() throws Exception {
        String a = "BlockAST [project=[[Location [x=27, y=-575], Binary [ADD, NumConst [1], Funct [POWER, [NumConst [5], NumConst [8]]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_arithmetic.xml"));
    }

    @Test
    public void reverseTransformatinArithmetic() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_arithmetic.xml");
    }

    @Test
    public void mathSingle() throws Exception {
        String a = "BlockAST [project=[[Location [x=-7, y=-604], Funct [LN, [Funct [POWER, [NumConst [5], NumConst [8]]]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single.xml"));
    }

    @Test
    public void reverseTransformatinSingle() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_single.xml");
    }

    @Test
    public void mathSingle1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-46, y=111], Unary [NEG, NumConst [10]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single1.xml"));
    }

    @Test
    public void reverseTransformatinSingle1() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_single1.xml");
    }

    @Test
    public void mathSingle2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-667, y=112], Unary [NEG, NumConst [0]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single2.xml"));
    }

    @Test
    public void reverseTransformatinSingle2() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_single2.xml");
    }

    @Test
    public void mathSingle3() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=13, y=-6], Funct [ROOT, [NumConst [0]]], Location [x=11, y=43], Funct [ABS, [NumConst [0]]], Location [x=18, y=104], Unary [NEG, NumConst [0]], Location [x=20, y=164], Funct [LN, [NumConst [0]]], Location [x=22, y=233], Funct [LOG10, [NumConst [0]]], Location [x=17, y=304], Funct [EXP, [NumConst [0]]], Location [x=19, y=364], Funct [POW10, [NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_single3.xml"));
    }

    @Test
    public void reverseTransformatinSingle3() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_single3.xml");
    }

    @Test
    public void mathTrig() throws Exception {
        String a = "BlockAST [project=[[Location [x=41, y=-491], Funct [ATAN, [Funct [LN, [Funct [POWER, [NumConst [5], NumConst [8]]]]]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_trig.xml"));
    }

    @Test
    public void reverseTransformatinTrig() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_trig.xml");
    }

    @Test
    public void mathTrig1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-15, y=173], Funct [SIN, [NumConst [0]]], Location [x=5, y=213], Funct [COS, [NumConst [0]]], Location [x=25, y=253], Funct [TAN, [NumConst [0]]], Location [x=45, y=293], Funct [ASIN, [NumConst [0]]], Location [x=65, y=333], Funct [ACOS, [NumConst [0]]], Location [x=85, y=373], Funct [ATAN, [NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_trig1.xml"));
    }

    @Test
    public void reverseTransformatinTrig1() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_trig1.xml");
    }

    @Test
    public void mathConstant() throws Exception {
        String a = "BlockAST [project=[[Location [x=131, y=-615], Funct [COS, [MathConst [E]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_constant.xml"));
    }

    @Test
    public void reverseTransformatinConstant() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constant.xml");
    }

    @Test
    public void mathNumberProperty() throws Exception {
        String a = "BlockAST [project=[[Location [x=34, y=-547], Funct [PRIME, [NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_number_property.xml"));
    }

    @Test
    public void reverseTransformatinNumberProperty() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_number_property.xml");
    }

    @Test
    public void mathNumberProperty1() throws Exception {
        String a = "BlockAST [project=[[Location [x=64, y=-525], Funct [DIVISIBLE_BY, [NumConst [8], NumConst [5]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_number_property1.xml"));
    }

    @Test
    public void reverseTransformatinNumberProperty1() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_number_property1.xml");
    }

    @Test
    public void mathChange() throws Exception {
        String a = "BlockAST [project=[[Location [x=-3, y=-422], Binary [MATH_CHANGE, Var [item], NumConst [1]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_change.xml"));
    }

    @Test
    public void reverseTransformationMathChange() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_change.xml");
    }

    @Test
    public void mathRound() throws Exception {
        String a = "BlockAST [project=[[Location [x=34, y=-492], Funct [ROUNDUP, [NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_round.xml"));
    }

    @Test
    public void reverseTransformationMathRound() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_round.xml");
    }

    @Test
    public void math_on_list() throws Exception {
        String a = "BlockAST [project=[[Location [x=-25, y=-449], Funct [AVERAGE, [EmptyList []]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_on_list.xml"));
    }

    @Test
    public void reverseTransformationMathOnList() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_on_list.xml");
    }

    @Test
    public void math_on_constrain() throws Exception {
        String a = "BlockAST [project=[[Location [x=-44, y=-399], Funct [CONSTRAIN, [NumConst [8], NumConst [1], NumConst [100]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_constrain.xml"));
    }

    @Test
    public void reverseTransformationMathOnConstrain() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_constrain.xml");
    }

    @Test
    public void math_random_float() throws Exception {
        String a = "BlockAST [project=[[Location [x=-140, y=265], \nVar [item] := Funct [RANDOM_FLOAT, []]\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_random_float.xml"));
    }

    @Test
    public void reverseTransformationMathRandomFloat() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_random_float.xml");
    }

    @Test
    public void math_modulo() throws Exception {
        String a = "BlockAST [project=[[Location [x=-36, y=424], Binary [MOD, NumConst [10], NumConst [2]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_modulo.xml"));
    }

    @Test
    public void reverseTransformationMathModulo() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_modulo.xml");
    }

    @Test
    public void math_random_integer() throws Exception {
        String a = "BlockAST [project=[[Location [x=-73, y=616], Funct [RANDOM_INTEGER, [NumConst [1], NumConst [100]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_random_integer.xml"));
    }

    @Test
    public void reverseTransformationRandomInteger() throws Exception {
        Helper.assertTransformationIsOk("/ast/math/math_random_integer.xml");
    }
}
