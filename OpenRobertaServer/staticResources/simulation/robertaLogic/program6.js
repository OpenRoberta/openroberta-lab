var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);
var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, false))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]), createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, -30))], [[createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])])]])]);

initProgram([stmt0, stmt1]);
