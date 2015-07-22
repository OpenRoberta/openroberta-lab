var stmt0 = createVarDeclaration(NUMERIC, "x", createConstant(NUM_CONST, 1));
var stmt1 = createVarDeclaration(NUMERIC, "y", createBinaryExpr(ADD, createVarReference(NUMERIC, "x"), createConstant(NUM_CONST, 1)));
var stmt2 = createDriveAction(createConstant(NUM_CONST, 30), FOREWARD);
var stmt3 = createRepeatStmt(WHILE, createBinaryExpr(LT, createVarReference(NUMERIC, "y"), createConstant(NUM_CONST, 5)), [createAssignStmt("x", createBinaryExpr(MULTIPLY, createVarReference(NUMERIC, "x"), createVarReference(NUMERIC, "x"))), createAssignStmt("y", createBinaryExpr(ADD, createVarReference(NUMERIC, "y"), createConstant(NUM_CONST, 1)))]);
var stmt4 = createAssignStmt("x", createBinaryExpr(ADD, createVarReference(NUMERIC, "x"), createConstant(NUM_CONST, 10)));
var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);
var stmt6 = createDriveAction(createConstant(NUM_CONST, -50), FOREWARD, createConstant(NUM_CONST, 100));
var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, -30))], [])]);

initProgram([stmt0, stmt1, stmt2, stmt3, stmt4, stmt4, stmt5, stmt6, stmt7]);