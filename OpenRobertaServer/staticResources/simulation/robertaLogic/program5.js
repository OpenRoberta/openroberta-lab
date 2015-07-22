var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);
var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createBinaryExpr(OR, createConstant(BOOL_CONST, false), createConstant(BOOL_CONST, true)))], [])]);

initProgram([stmt0, stmt1]);