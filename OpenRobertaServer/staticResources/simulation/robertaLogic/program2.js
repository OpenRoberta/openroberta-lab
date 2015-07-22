var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);
var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);
var stmt2 = createTurnLight(GREEN, ON);
var stmt3 = createStopDrive();
var stmt4 = createResetLight();
var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);

initProgram([ stmt0, stmt1, stmt2, stmt3, stmt4, stmt5]);