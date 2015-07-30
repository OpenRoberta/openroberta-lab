function initProgram(program) {
    MEM.clear();
    PROGRAM_SIMULATION.setNextStatement(true);
    PROGRAM_SIMULATION.setWait(false);
    PROGRAM_SIMULATION.set(program);
}

function step(simulationSensorData) {
    SENSORS.setTouchSensor(simulationSensorData.touch);
    SENSORS.setColor(simulationSensorData.color);
    SENSORS.setLight(simulationSensorData.light);
    SENSORS.setUltrasonicSensor(simulationSensorData.ultrasonic);
    ACTORS.getLeftMotor().setCurrentRotations(simulationSensorData.tacho[0]);
    ACTORS.getRightMotor().setCurrentRotations(simulationSensorData.tacho[1]);
    if (PROGRAM_SIMULATION.isNextStatement()) {

        var stmt = PROGRAM_SIMULATION.getRemove();

        switch (stmt.stmt) {
        case ASSIGN_STMT:
            var value = evalExpr(stmt.expr);
            MEM.assign(stmt.name, value);
            break;

        case VAR_DECLARATION:
            var value = evalExpr(stmt.value);
            MEM.decl(stmt.name, value);
            break;

        case IF_STMT:
            evalIf(stmt);
            break;

        case REPEAT_STMT:
            evalRepeat(stmt);
            break;

        case DRIVE_ACTION:
            ACTORS.resetTacho(simulationSensorData.tacho[0], simulationSensorData.tacho[1]);
            ACTORS.setSpeed(stmt.speed, stmt[DRIVE_DIRECTION]);
            if (stmt.distance != undefined) {
                ACTORS.setDistanceToCover(stmt.distance);
            }
            break;

        case TURN_ACTION:
            ACTORS.resetTacho(simulationSensorData.tacho[0], simulationSensorData.tacho[1]);
            ACTORS.setAngleSpeed(stmt.speed, stmt[TURN_DIRECTION]);
            if (stmt.angle != undefined) {
                ACTORS.clculateAngleToCover(stmt.angle);
            }
            break;

        case WAIT_STMT:
            evalWaitStmt(stmt);
            break;

        case TURN_LIGHT:
            LIGHT.setColor(stmt.color);
            LIGHT.setMode(stmt.mode);
            break;

        case STOP_DRIVE:
            ACTORS.setSpeed(0);
            break;

        case RESET_LIGHT:
            LIGHT.setColor(GREEN);
            LIGHT.setMode(OFF);
            break;

        default:
            throw "Invalid Statement " + stmt.stmt + "!";
        }
    }
    ACTORS.calculateCoveredDistance();
}

function evalRepeat(stmt) {
    switch (stmt.mode) {
    case TIMES:
        for (var i = 0; i < evalExpr(stmt.expr); i++) {
            PROGRAM_SIMULATION.prepend(stmt.stmtList);
        }
        break;
    default:
        var value = evalExpr(stmt.expr);
        if (value) {
            PROGRAM_SIMULATION.prepend([ stmt ]);
            PROGRAM_SIMULATION.prepend(stmt.stmtList);
        }
    }
}

function evalIf(stmt) {
    var programPrefix;
    for (var i = 0; i < stmt.exprList.length; i++) {
        var value = evalExpr(stmt.exprList[i]);
        if (value) {
            programPrefix = stmt.thenList[i];
            if (PROGRAM_SIMULATION.isWait()) {
                PROGRAM_SIMULATION.getRemove();
                PROGRAM_SIMULATION.setWait(false);
            }
            break;
        }
    }
    if (programPrefix == undefined) {
        programPrefix = stmt.elseStmts;
    }
    PROGRAM_SIMULATION.prepend(programPrefix);

}

function evalWaitStmt(stmt) {
    PROGRAM_SIMULATION.setWait(true);
    PROGRAM_SIMULATION.prepend([ stmt ]);
    PROGRAM_SIMULATION.prepend([ stmt.statements ]);
}

function evalExpr(expr) {
    switch (expr.expr) {
    case NUM_CONST:
    case BOOL_CONST:
    case COLOR_CONST:
        return expr.value;
    case VAR:
        return MEM.get(expr.name);

    case BINARY:
        return evalBinary(expr.op, expr.left, expr.right);

    case GET_SAMPLE:
        return evalSensor(expr[SENSOR_TYPE], expr[SENSOR_MODE]);
    default:
        throw "Invalid Expression Type!";
    }
}

function evalSensor(sensorType, sensorMode) {
    switch (sensorType) {
    case TOUCH:
        return SENSORS.isPressed();
    case ULTRASONIC:
        return SENSORS.getUltrasonicSensor();
    case RED:
        return SENSORS.getLight();
    case COLOUR:
        return SENSORS.getColor();
    default:
        throw "Invalid Sensor!";
    }
}

function evalBinary(op, left, right) {
    var valLeft = evalExpr(left);
    var valRight = evalExpr(right);
    var val;
    switch (op) {
    case ADD:
        val = valLeft + valRight;
        break;
    case MINUS:
        val = valLeft - valRight;
        break;
    case MULTIPLY:
        val = valLeft * valRight;
        break;
    case DIVIDE:
        val = valLeft / valRight;
        break;
    case POWER:
        val = valLeft ^ valRight;
        break;
    case LT:
        val = valLeft < valRight;
        break;
    case GT:
        val = valLeft > valRight;
        break;
    case EQ:
        val = valLeft == valRight;
        break;
    case NEQ:
        val = valLeft != valRight;
        break;
    case GTE:
        val = valLeft >= valRight;
        break;
    case LTE:
        val = valLeft <= valRight;
        break;
    case OR:
        val = valLeft || valRight;
        break;
    case AND:
        val = valLeft && valRight;
        break;
    default:
        throw "Invalid Binary Operator";
    }
    return val;
}
