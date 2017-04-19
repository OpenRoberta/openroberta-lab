package de.fhg.iais.roberta.syntax.hardwarecheck.nao;

import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SayText;
import de.fhg.iais.roberta.syntax.action.nao.SetIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.nao.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Dialog;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.ForceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Gyrometer;
import de.fhg.iais.roberta.syntax.sensor.nao.LearnFace;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMark;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizedWord;
import de.fhg.iais.roberta.syntax.sensor.nao.Sonar;
import de.fhg.iais.roberta.syntax.sensor.nao.Touchsensors;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

public abstract class CheckVisitor implements NaoAstVisitor<Void> {

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        var.getValue().visit(this);
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        unary.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        binary.getLeft().visit(this);
        binary.getRight().visit(this);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        for ( Expr<Void> expr : mathPowerFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        for ( Expr<Void> expr : exprList.get() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            ifStmt.getExpr().get(i).visit(this);
            ifStmt.getThenList().get(i).visit(this);
        }
        ifStmt.getElseList().visit(this);
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        repeatStmt.getExpr().visit(this);
        repeatStmt.getList().visit(this);
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        for ( Stmt<Void> stmt : stmtList.get() ) {
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        waitStmt.getStatements().visit(this);
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        waitTimeStmt.getTime().visit(this);
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        for ( Expr<Void> expr : textPrintFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        return functionExpr.getFunction().visit(this);
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        for ( Expr<Void> expr : getSubFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        for ( Expr<Void> expr : indexOfFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        for ( Expr<Void> expr : lengthOfIsEmptyFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        listCreate.getValue().visit(this);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        for ( Expr<Void> expr : listGetIndex.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        for ( Expr<Void> expr : listRepeat.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        for ( Expr<Void> expr : listSetIndex.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        for ( Expr<Void> expr : mathConstrainFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        for ( Expr<Void> expr : mathNumPropFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        for ( Expr<Void> expr : mathOnListFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        for ( Expr<Void> expr : mathRandomIntFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        for ( Expr<Void> expr : mathSingleFunct.getParam() ) {
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        textJoinFunct.getParam().visit(this);
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        methodVoid.getBody().visit(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        methodReturn.getBody().visit(this);
        methodReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        methodIfReturn.getCondition().visit(this);
        methodIfReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        methodCall.getParametersValues().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> SetStiffness) {
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> Hand) {
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        moveJoint.getDegrees().visit(this);
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        walkDistance.getDistanceToWalk().visit(this);
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        turnDegrees.getDegreesToTurn().visit(this);
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        walkTo.getWalkToX().visit(this);
        walkTo.getWalkToY().visit(this);
        walkTo.getWalkToTheta().visit(this);
        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        pointLookAt.getpointX().visit(this);
        pointLookAt.getpointY().visit(this);
        pointLookAt.getpointZ().visit(this);
        pointLookAt.getSpeed().visit(this);

        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        setVolume.getVolume().visit(this);
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        return null;
    }

    @Override
    public Void visitSetLanguage(SetLanguage<Void> setLanguage) {
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        sayText.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        playFile.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitDialog(Dialog<Void> dialog) {
        return null;
    }

    @Override
    public Void visitRecognizedWord(RecognizedWord<Void> recognizedWord) {
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        setLeds.getColor().visit(this);
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        setIntensity.getIntensity().visit(this);
        return null;
    }

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        randomEyesDuration.getDuration().visit(this);
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        rastaDuration.getDuration().visit(this);
        return null;
    }

    @Override
    public Void visitTouchsensors(Touchsensors<Void> touchsensors) {
        return null;
    }

    @Override
    public Void visitSonar(Sonar<Void> sonar) {
        return null;
    }

    @Override
    public Void visitGyrometer(Gyrometer<Void> gyrometer) {
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        return null;
    }

    @Override
    public Void visitForceSensor(ForceSensor<Void> forceSensor) {
        return null;
    }

    @Override
    public Void visitNaoMark(NaoMark<Void> naoMark) {
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        return null;
    }

    /*@Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        return null;
    }*/

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        learnFace.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        forgetFace.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        return null;
    }

    @Override
    public Void visitNaoGetSampleSensor(NaoGetSampleSensor<Void> naoGetSampleSensor) {
        return null;
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrent<Void> electricCurrent) {
        return null;
    }

}