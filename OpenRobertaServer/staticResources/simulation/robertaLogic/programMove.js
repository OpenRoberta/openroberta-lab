var stmt2 = createDriveAction(createConstant(NUM_CONST, 90), FOREWARD);
var stmt4 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);
var stmt5 = createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 50));
var stmt6 = createDriveAction(createConstant(NUM_CONST, 30), FOREWARD, createConstant(NUM_CONST, 30));
var stmt3 = createTurnAction(createConstant(NUM_CONST, 30), RIGHT, createConstant(NUM_CONST, 90));

var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, 30))], [])]);

initProgram([ stmt2, stmt4, stmt5, stmt6, stmt3, stmt7]);