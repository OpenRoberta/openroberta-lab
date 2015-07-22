var stmt0 = createIfStmt([createBinaryExpr(LTE, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)]], [createTurnAction(createConstant(NUM_CONST, 50), RIGHT, createConstant(NUM_CONST, 80))]);
var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);
var stmt2 = createRepeatStmt(TIMES, createConstant(NUM_CONST, 3), [createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 20)), createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 30))]);

initProgram([stmt0, stmt1, stmt2]);