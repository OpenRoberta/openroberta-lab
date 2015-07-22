var stmt0 = createIfStmt([createBinaryExpr(EQ, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 6)), createBinaryExpr(GTE, createConstant(NUM_CONST, 6), createConstant(NUM_CONST, 0))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]);
var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);

initProgram([stmt0, stmt1]);