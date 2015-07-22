//include("constants.js");

var n1 = createConstant(NUM_CONST, 1);
var n4 = createConstant(NUM_CONST, 4);
var n5 = createConstant(NUM_CONST, 5);
var n10 = createConstant(NUM_CONST, 10);
var b1 = createConstant(BOOL_CONST, true);
var b2 = createConstant(BOOL_CONST, false);

var refX = createVarReference(NUMERIC, "x");
var refY = createVarReference(NUMERIC, "y");

var xPlusOne = createBinaryExpr(ADD, refX, n1);
var yPlusOne = createBinaryExpr(ADD, refY, n1);
var yLessFive = createBinaryExpr(LESS, refY, n5);
var doubleX = createBinaryExpr(ADD, refX, refX);
var xTimesFour = createBinaryExpr(MULT, n4, refX);
var plusTen = createBinaryExpr(ADD, xTimesFour, n10);

var x1 = createVarDeclaration(NUMERIC, "x", n1);
var y1 = createVarDeclaration(NUMERIC, "y", xPlusOne);

var changeX = createAssignStmt("x", doubleX);
var changeY = createAssignStmt("y", yPlusOne);
var finalX = createAssignStmt("x", plusTen);

var w = createRepeatStmt("WHILE", yLessFive, [ changeX, changeY ])
var drive = createDriveAction(.80, "FORWARD", 39);
var drive1 = createDriveAction(-.50, "FORWARD", 39);

var touchSensor = createGetSample(TOUCH);
var isTouched = createBinaryExpr(EQ, touchSensor, b1);
var isTouched1 = createBinaryExpr(EQ, b1, b2);

var if0 = createIfStmt([ isTouched ], [ [ drive1 ] ]);
var if1 = createIfStmt([ isTouched1 ], [ [ drive1 ] ]);

var waitStmt1 = createWaitStmt([ if0 ]);

var waitStmt2 = createWaitStmt([ if1 ]);

 initProgram([drive, x1, y1, w, finalX, waitStmt1, waitStmt2]);
