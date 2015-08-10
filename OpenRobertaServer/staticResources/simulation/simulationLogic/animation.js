var TRACKWIDTH = 40;
var FPS = 45;
var STEP_TIME = 1 / FPS;
var MAXDIAG = 0;
var MAXPOWER = 120;
var ENC = 360 / (2 * Math.PI * 5.6);

var userProgram;
var backCanvas;
var uniCanvas;
var objectCanvas;
var robotCanvas;
var bCtx;
var uCtx;
var oCtx;
var canvasOffset;
var offsetX;
var offsetY;
var isDownrobot = false;
var isDownObstacle = false;
var startX;
var startY;
var scale = 1;
var img;
var timerStep = 0;
var canceled;

var pause;
function setPause(value) {
    pause = value;
}

var stepCounter;
function setStep() {
    stepCounter = -30;
    setPause(false);
}
var info;
function setInfo() {
    if (info === true) {
        info = false;
    } else {
        info = true;
    }
    ;
}

// external program stuff
var cP = 1;
var turn = false;
var back = false;
var encoderTouch = 0;
var start = false;
var touch = false;
var line = false;
var ultra = false;
//speed set by the user
var speedRight = 0;
var speedLeft = 0;

// obstacles
// scaled playground
var ground = {
    x : 0,
    y : 0,
    w : 500,
    h : 500
}
var playground = {
    x : 0,
    y : 0,
    w : 500,
    h : 500
}
var obstacle = {
    x : 495,
    y : 396,
    w : 20,
    h : 20
}
var obstacleList = [ ground, obstacle ];

// input and output for executing user program
var input = {
    touch : false,
    color : '',
    light : 0,
    ultrasonic : 0,
    tacho : [ 0, 0 ]
}

var output = {
    left : 0,
    right : 0,
    led : ' ',
    ledMode : ' '
};

// render stuff
var oldTime = new Date().getTime();
var averageTimeStep = STEP_TIME;
var kP = 0.5;
var kI = 0.0001;
var error = 0;
var integral = 0;
var wave = 0;
var tread = 0;

var robot = new Robot();

//var COLOR_ENUM = {
//    NONE : 0,
//    BLACK : 1,
//    BLUE : 2,
//    GREEN : 3,
//    YELLOW : 4,
//    RED : 5,
//    WHITE : 6,
//    BROWN : 7
//};

function initOraSim(program) {
    userProgram = program;
    eval(userProgram);
    initLayers();
    canceled = false;
    isDownrobot = false;
    isDownObstacle = false;
    stepCounter = 0;
    pause = true;
    info = false;
    oldTime = new Date().getTime();
    averageTimeStep = STEP_TIME;

    robot = new Robot();
    loadImages();
}

function cancelOraSim() {
    removeMouseEvents();
    destroyLayers();
    canceled = true;
}

function oraSimRender() {
    if (canceled) {
        return;
    }
    setTimeStep();
    stepCounter += 1;
    if (!PROGRAM_SIMULATION.isTerminated() && !pause) {
        //executeProgram();  //for tests without OpenRobertaLab
        if (stepCounter == 0) {
            setPause(true);
        }
        step(input);
        output.left = ACTORS.getLeftMotor().getPower() * MAXPOWER;
        output.right = ACTORS.getRightMotor().getPower() * MAXPOWER;
        output.led = LIGHT.color;
        output.ledMode = LIGHT.mode;
    } else if (PROGRAM_SIMULATION.isTerminated()) {
        reloadProgram();
        eval(userProgram);
        $('.simForward').removeClass('typcn-media-pause');
        $('.simForward').addClass('typcn-media-play');
        setPause(true);
        output.left = 0;
        output.right = 0;
    } else {
        output.left = 0;
        output.right = 0;
    }
    diffDrive(output);
    adjustAllSizes();
    setSensorValues();
    drawObject(scale, objectCanvas, oCtx);
    setTimeout(function() {
        oraSimRender();
    }, timerStep);
};

function reloadProgram() {
    eval(userProgram);
    $('.simForward').removeClass('typcn-media-pause');
    $('.simForward').addClass('typcn-media-play');
    // robot = new Robot();
    setPause(true);
};

function executeProgram() {
    output.left = speedLeft;
    output.right = speedRight;
    if (start === true) {
        // nothing to do 
    } else if (touch === true) {
        if (robot.touchSensor.value === 1) {
            encoderTouch = robot.encoder.right;
            back = true;
            output.left = -20;
            output.right = -20;
        } else if (back === true && encoderTouch - 35 <= robot.encoder.right) {
            turn = true;
            output.left = -20;
            output.right = -20;
        } else if (back === true) {
            encoderTouch = robot.encoder.right;
            back = false;
            turn = true;
            output.left = -20;
            output.right = 20;
        } else if (turn === true && encoderTouch + 35 >= robot.encoder.right) {
            output.left = -20;
            output.right = 20;
        } else {
            back = false;
            turn = false;
        }
    } else if (ultra === true) {
        if (robot.ultraSensor.distance < 100) {
            encoderTouch = robot.encoder.right;
            back = true;
            output.left = -20;
            output.right = -20;
        } else if (back === true && encoderTouch - 35 <= robot.encoder.right) {
            turn = true;
            output.left = -20;
            output.right = -20;
        } else if (back === true) {
            encoderTouch = robot.encoder.right;
            back = false;
            turn = true;
            output.left = -20;
            output.right = 20;
        } else if (turn === true && encoderTouch + 35 >= robot.encoder.right) {
            output.left = -20;
            output.right = 20;
        } else {
            back = false;
            turn = false;
        }
    } else if (line === true) {
        var error = 50 - robot.colorSensor.lightValue;
        output.left = range(speedLeft + cP * error);
        output.right = range(speedLeft - cP * error);
    } else {
        output.left = 0;
        output.right = 0;
    }
};

function diffDrive(output) {
    robot.pose.theta = (robot.pose.theta + 2 * Math.PI) % (2 * Math.PI);
    robot.encoder.left += output.left * STEP_TIME;
    robot.encoder.right += output.right * STEP_TIME;
    if (robot.frontLeft.bumped === true && output.left > 0) {
        output.left *= -1;
    }
    if (robot.backLeft.bumped === true && output.left < 0) {
        output.left *= -1;
    }
    if (robot.frontRight.bumped === true && output.right > 0) {
        output.right *= -1;
    }
    if (robot.backRight.bumped === true && output.right < 0) {
        output.right *= -1;
    }
    if (output.right === output.left) {
        var moveXY = output.right * STEP_TIME;
        var mX = Math.cos(robot.pose.theta) * moveXY;
        var mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
        robot.pose.x += mX;
        if (moveXY >= 0) {
            if (robot.pose.theta < Math.PI) {
                robot.pose.y += mY;
            } else {
                robot.pose.y -= mY;
            }
        } else {
            if (robot.pose.theta > Math.PI) {
                robot.pose.y += mY;
            } else {
                robot.pose.y -= mY;
            }
        }
        if (robot.pose.thetaDiff < 0) {
            robot.pose.thetaDiff += (output.left / 10 * STEP_TIME);
            robot.pose.thetaDiff = Math.min(robot.pose.thetaDiff, 0);
        } else {
            robot.pose.thetaDiff -= (output.left / 10 * STEP_TIME);
            robot.pose.thetaDiff = Math.max(robot.pose.thetaDiff, 0);
        }
    } else {
        var R = TRACKWIDTH / 2 * ((output.left + output.right) / (output.left - output.right));
        var rot = (output.left - output.right) / TRACKWIDTH;
        var iccX = robot.pose.x - (R * Math.sin(robot.pose.theta));
        var iccY = robot.pose.y + (R * Math.cos(robot.pose.theta));
        robot.pose.x = (Math.cos(rot * STEP_TIME) * (robot.pose.x - iccX) - Math.sin(rot * STEP_TIME) * (robot.pose.y - iccY)) + iccX;
        robot.pose.y = (Math.sin(rot * STEP_TIME) * (robot.pose.x - iccX) + Math.cos(rot * STEP_TIME) * (robot.pose.y - iccY)) + iccY;
        var thetaTemp = robot.pose.theta;
        robot.pose.theta = robot.pose.theta + (rot * STEP_TIME);
        robot.pose.thetaDiff += (rot * STEP_TIME);
        robot.pose.thetaDiff = Math.min(robot.pose.thetaDiff, Math.PI / 2 * Math.abs(output.right / output.left));
        robot.pose.thetaDiff = Math.max(robot.pose.thetaDiff, -Math.PI / 2 * Math.abs(output.right / output.left));
    }
    robot.updateLocations();
};

function setSensorValues() {
    // touchSensor
    robot.touchSensor.value = 0;
    robot.frontLeft.bumped = false;
    robot.frontRight.bumped = false;
    robot.backLeft.bumped = false;
    robot.backRight.bumped = false;

    for (var i = 0; i < obstacleList.length; i++) {
        var obstacleLines = getLines(obstacleList[i]);
        for (var k = 0; k < obstacleLines.length; k++) {
            var interPoint = intersectLines({
                x1 : robot.frontLeft.rx,
                x2 : robot.frontRight.rx,
                y1 : robot.frontLeft.ry,
                y2 : robot.frontRight.ry
            }, obstacleLines[k]);
            if (interPoint) {
                if (Math.abs(robot.frontLeft.rx - interPoint.x) < Math.abs(robot.frontRight.rx - interPoint.x)) {
                    robot.frontLeft.bumped = true;
                } else {
                    robot.frontRight.bumped = true;
                }
                robot.touchSensor.value = 1;
            } else {
                var p = distToParallel({
                    x : robot.touchSensor.rx,
                    y : robot.touchSensor.ry
                }, {
                    x : obstacleLines[k].x1,
                    y : obstacleLines[k].y1,
                }, {
                    x : obstacleLines[k].x2,
                    y : obstacleLines[k].y2
                });
                if (sqr(robot.touchSensor.rx - p.x) + sqr(robot.touchSensor.ry - p.y) < STEP_TIME * Math.max(Math.abs(output.right), Math.abs(output.left))) {
                    robot.frontLeft.bumped = true;
                    robot.frontRight.bumped = true;
                    robot.touchSensor.value = 1;
                } else {
                    var interPoint = intersectLines({
                        x1 : robot.backLeft.rx,
                        x2 : robot.backRight.rx,
                        y1 : robot.backLeft.ry,
                        y2 : robot.backRight.ry
                    }, obstacleLines[k]);
                    if (interPoint) {
                        if (Math.abs(robot.backLeft.rx - interPoint.x) < Math.abs(robot.backRight.rx - interPoint.x)) {
                            robot.backLeft.bumped = true;
                        } else {
                            robot.backRight.bumped = true;
                        }
                    } else {
                        var p = distToParallel({
                            x : robot.touchSensor.rx,
                            y : robot.touchSensor.ry
                        }, {
                            x : obstacleLines[k].x1,
                            y : obstacleLines[k].y1,
                        }, {
                            x : obstacleLines[k].x2,
                            y : obstacleLines[k].y2
                        });
                        if (sqr(robot.backMiddle.rx - p.x) + sqr(robot.backMiddle.ry - p.y) < STEP_TIME
                                * Math.max(Math.abs(output.right), Math.abs(output.left))) {
                            robot.backLeft.bumped = true;
                            robot.backRight.bumped = true;
                        }
                    }
                }
            }
        }
    }
    // colorSensor 
    var r = 0;
    var g = 0
    var b = 0;
    var colors = uCtx.getImageData(Math.round(robot.colorSensor.rx - 3), Math.round(robot.colorSensor.ry - 3), 6, 6);
    //var colors = uCtx.getImageData(Math.round(robot.colorSensor.rx - 4), Math.round(robot.colorSensor.ry - 4), 8, 8);
    var out = [ 0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140 ]; // outside the circle
    // var out = [ 0, 4, 24, 28, 32, 60, 192, 220, 224, 228, 248, 252 ]; // outside the circle
    var b = 0
    for (var j = 0; j < colors.data.length; j += 24) {
        //  for (var j = 0; j < colors.data.length; j += 32) {
        for (var i = j; i < j + 24; i += 4) {
            if (out.indexOf(i) < 0) {
                r += colors.data[i + 0];
                g += colors.data[i + 1];
                b += colors.data[i + 2];
                b++;
            }
        }
    }
    var num = colors.data.length / 4 - 12; // 12 are outside
    var red = r / num;
    var green = g / num;
    var blue = b / num;
    robot.colorSensor.lightValue = (red + green + blue) / 3 / 2.55;
    robot.colorSensor.colorValue = getColor(rgbToHsv(red, green, blue));
    if (robot.colorSensor.colorValue === COLOR_ENUM.NONE) {
        robot.colorSensor.color = 'grey';
    } else if (robot.colorSensor.colorValue === COLOR_ENUM.BLACK) {
        robot.colorSensor.color = 'black';
    } else if (robot.colorSensor.colorValue == COLOR_ENUM.WHITE) {
        robot.colorSensor.color = 'white';
    } else if (robot.colorSensor.colorValue === COLOR_ENUM.YELLOW) {
        robot.colorSensor.color = 'yellow';
    } else if (robot.colorSensor.colorValue === COLOR_ENUM.RED) {
        robot.colorSensor.color = 'red';
    } else if (robot.colorSensor.colorValue === COLOR_ENUM.BLUE) {
        robot.colorSensor.color = 'blue';
    } else if (robot.colorSensor.colorValue === COLOR_ENUM.GREEN) {
        robot.colorSensor.color = 'lime';
    }

    // ultraSensor - check for 5 rays
    var u3 = {
        x1 : robot.ultraSensor.rx,
        y1 : robot.ultraSensor.ry,
        x2 : robot.ultraSensor.rx + MAXDIAG * Math.cos(robot.pose.theta),
        y2 : robot.ultraSensor.ry + MAXDIAG * Math.sin(robot.pose.theta)
    }
    var u1 = {
        x1 : robot.ultraSensor.rx,
        y1 : robot.ultraSensor.ry,
        x2 : robot.ultraSensor.rx + MAXDIAG * Math.cos(robot.pose.theta - Math.PI / 8),
        y2 : robot.ultraSensor.ry + MAXDIAG * Math.sin(robot.pose.theta - Math.PI / 8)
    }
    var u2 = {
        x1 : robot.ultraSensor.rx,
        y1 : robot.ultraSensor.ry,
        x2 : robot.ultraSensor.rx + MAXDIAG * Math.cos(robot.pose.theta - Math.PI / 16),
        y2 : robot.ultraSensor.ry + MAXDIAG * Math.sin(robot.pose.theta - Math.PI / 16)
    }
    var u5 = {
        x1 : robot.ultraSensor.rx,
        y1 : robot.ultraSensor.ry,
        x2 : robot.ultraSensor.rx + MAXDIAG * Math.cos(robot.pose.theta + Math.PI / 8),
        y2 : robot.ultraSensor.ry + MAXDIAG * Math.sin(robot.pose.theta + Math.PI / 8)
    }
    var u4 = {
        x1 : robot.ultraSensor.rx,
        y1 : robot.ultraSensor.ry,
        x2 : robot.ultraSensor.rx + MAXDIAG * Math.cos(robot.pose.theta + Math.PI / 16),
        y2 : robot.ultraSensor.ry + MAXDIAG * Math.sin(robot.pose.theta + Math.PI / 16)
    }

    var uA = new Array(u1, u2, u3, u4, u5);
    robot.ultraSensor.distance = 9999;
    for (var i = 0; i < obstacleList.length; i++) {
        var obstacleLines = getLines(obstacleList[i]);
        for (var k = 0; k < obstacleLines.length; k++) {
            for (var j = 0; j < uA.length; j++) {
                var interPoint = intersectLines(uA[j], obstacleLines[k]);
                if (interPoint) {
                    robot.ultraSensor.u[j] = interPoint;
                    var dis = Math.sqrt((interPoint.x - robot.ultraSensor.rx) * (interPoint.x - robot.ultraSensor.rx) + (interPoint.y - robot.ultraSensor.ry)
                            * (interPoint.y - robot.ultraSensor.ry));
                    if (dis < robot.ultraSensor.distance) {
                        robot.ultraSensor.distance = dis;
                        robot.ultraSensor.cx = interPoint.x;
                        robot.ultraSensor.cy = interPoint.y;
                    }
                }
            }
        }
    }
    if (robot.touchSensor.value === 1) {
        input.touch = true;
    } else {
        input.touch = false;
    }
    input.light = robot.colorSensor.lightValue;
    input.color = robot.colorSensor.colorValue;
    input.ultrasonic = robot.ultraSensor.distance / 2;
    input.tacho[1] = robot.encoder.right * ENC;
    input.tacho[0] = robot.encoder.left * ENC;
};

function drawBackground(scale, canvas, ctx) {
    ctx.restore();
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.save();
    ctx.scale(scale, scale);
    ctx.fillStyle = "#00FFFF";
    ctx.fillRect(0, 0, 2500, 2000);
    ctx.fillStyle = "#00FFFF";
    ctx.fillRect(15, 15, ground.w - 30, ground.w - 30);
    ctx.drawImage(img, 0, 0);
//    ctx.fillStyle = "red";
//    ctx.fillRect(120, ground.h - 80, 40, 60);
//    ctx.fillStyle = "green";
//    ctx.fillRect(160, ground.h - 80, 40, 60);
//    ctx.fillStyle = "yellow";
//    ctx.fillRect(200, ground.h - 80, 40, 60);
//    ctx.fillStyle = "blue";
//    ctx.fillRect(240, ground.h - 80, 40, 60);
//    ctx.strokeStyle = "#ffffff";
//    ctx.lineWidth = "18";
//    ctx.beginPath();
//    ctx.arc(ground.w / 2, ground.h / 2, 125, 0, Math.PI * 1.5);
//    ctx.closePath();
//    ctx.stroke();
//    ctx.strokeStyle = "#000000";
//    ctx.lineWidth = "6";
//    ctx.beginPath();
//    ctx.arc(ground.w / 2, ground.h / 2, 125, 0, Math.PI * 1.5);
//    ctx.closePath();
//    ctx.stroke();
};

function drawObject(scale, canvas, ctx) {
    canvas.width = Math.max(ground.w, playground.w);
    canvas.height = Math.max(ground.h, playground.h);
    // provide new user information   
    if (info === true) {
        var endLabel = playground.w - 40;
        var endValue = playground.w - 5;
        var line = 40;
        ctx.textAlign = "end";
        ctx.font = "10px Verdana";
        ctx.fillStyle = "#333333";
        ctx.fillText("FPS", endLabel, line);
        ctx.fillText(Math.round(1000 / averageTimeStep), endValue, line);
        line += 15;
        ctx.fillText("Pose X", endLabel, line);
        ctx.fillText(Math.round(robot.pose.x), endValue, line);
        line += 15;
        ctx.fillText("Pose Y", endLabel, line);
        ctx.fillText(Math.round(robot.pose.y), endValue, line);
        line += 15;
        ctx.fillText("Pose θ", endLabel, line);
        ctx.fillText(Math.round(Math.round(toDegree(robot.pose.theta))), endValue, line);
        line += 25;
        ctx.fillText("Motor left", endLabel, line);
        ctx.fillText(Math.round(robot.encoder.left * ENC), endValue, line);
        line += 15;
        ctx.fillText("Motor right", endLabel, line);
        ctx.fillText(Math.round(robot.encoder.right * ENC), endValue, line);
        line += 15;
        ctx.fillText("Touch Sensor", endLabel, line);
        ctx.fillText(Math.round(robot.touchSensor.value), endValue, line);
        line += 15;
        ctx.fillText("Light Sensor", endLabel, line);
        ctx.fillText(Math.round(Math.round(robot.colorSensor.lightValue)), endValue, line);
        line += 15;
        ctx.fillText("Ultra Sensor", endLabel, line);
        ctx.fillText(Math.round(robot.ultraSensor.distance / 2), endValue, line);
        line += 15;
        ctx.fillText("Color Sensor", endLabel, line);
        ctx.fillStyle = robot.colorSensor.color;
        ctx.fillRect(endValue, line, -10, -10);
    }
    ctx.scale(scale, scale);
    ctx.save();
    ctx.translate(robot.pose.x, robot.pose.y);
    ctx.rotate(toRadians(toDegree(robot.pose.theta) - 90));
    ctx.scale(1, -1);
    //axis
    ctx.lineWidth = "2.5";
    ctx.strokeStyle = robot.wheelLeft.color;
    ctx.beginPath();
    ctx.moveTo(robot.geom.x - 5, 0);
    ctx.lineTo(robot.geom.x + robot.geom.w + 5, 0);
    ctx.closePath();
    ctx.stroke();
    //back wheel
    ctx.fillStyle = "black";
    //ctx.ellipse(0, 30, 3, 6, robot.pose.thetaDiff, 0, Math.PI * 2);
    //ctx.fill();
    ctx.fillRect(-2.5, 30, 5, 5);
    //robot
    ctx.shadowBlur = 0;
    ctx.shadowOffsetX = 0;
    ctx.fillStyle = robot.touchSensor.color;
    ctx.fillRect(robot.frontRight.x + 12.5, robot.frontRight.y, 20, 10);
    ctx.fillStyle = robot.led.color;
    var grd = ctx.createRadialGradient(0, 10, 1, 0, 10, 15);
    grd.addColorStop(0, robot.led.color);
    grd.addColorStop(0.5, robot.geom.color);
    ctx.shadowBlur = 10;
    ctx.shadowColor = "black";
    ctx.fillStyle = grd;
    ctx.beginPath();
    ctx.moveTo(robot.geom.x + 2.5, robot.geom.y);
    ctx.lineTo(robot.geom.x + robot.geom.w - 2.5, robot.geom.y);
    ctx.quadraticCurveTo(robot.geom.x + robot.geom.w, robot.geom.y, robot.geom.x + robot.geom.w, robot.geom.y + 2.5);
    ctx.lineTo(robot.geom.x + robot.geom.w, robot.geom.y + robot.geom.h - 2.5);
    ctx.quadraticCurveTo(robot.geom.x + robot.geom.w, robot.geom.y + robot.geom.h, robot.geom.x + robot.geom.w - 2.5, robot.geom.y + robot.geom.h);
    ctx.lineTo(robot.geom.x + 2.5, robot.geom.y + robot.geom.h);
    ctx.quadraticCurveTo(robot.geom.x, robot.geom.y + robot.geom.h, robot.geom.x, robot.geom.y + robot.geom.h - 2.5);
    ctx.lineTo(robot.geom.x, robot.geom.y + 2.5);
    ctx.quadraticCurveTo(robot.geom.x, robot.geom.y, robot.geom.x + 2.5, robot.geom.y);
    ctx.closePath();
    ctx.fill();

    //touch
    ctx.shadowBlur = 10;
    ctx.shadowOffsetX = 2;
    if (robot.touchSensor.value === 1) {
        ctx.fillStyle = 'red';
        ctx.fillRect(robot.frontRight.x, robot.frontRight.y, robot.frontLeft.x - robot.frontRight.x, 3.5);
    } else {
        ctx.fillStyle = robot.touchSensor.color;
        ctx.fillRect(robot.frontRight.x, robot.frontRight.y, robot.frontLeft.x - robot.frontRight.x, 3.5);
    }
    ctx.shadowBlur = 0;
    ctx.shadowOffsetX = 0;
    //LED
    ctx.fillStyle = "#f0f0f0";
    ctx.beginPath();
    ctx.arc(0, 10, 2.5, 0, Math.PI * 2);
    ctx.fill();
    ctx.closePath();
    //wheels
    ctx.fillStyle = robot.wheelLeft.color;
    ctx.fillRect(robot.wheelLeft.x, robot.wheelLeft.y, robot.wheelLeft.w, robot.wheelLeft.h);
    ctx.fillStyle = robot.wheelRight.color;
    ctx.fillRect(robot.wheelRight.x, robot.wheelRight.y, robot.wheelRight.w, robot.wheelRight.h);
//    ctx.beginPath();
//    ctx.lineWidth = "2";
//    ctx.strokeStyle = "#ffffff";
//    tread += (output.left / 400);
//    tread = tread % 3;
//    ctx.lineDashOffset = tread;
//    ctx.setLineDash([ 1, 2 ]);
//    ctx.moveTo(robot.wheelLeft.x + 1, robot.wheelLeft.y);
//    ctx.lineTo(robot.wheelLeft.x + 1, robot.wheelLeft.y + robot.wheelRight.h);
//    ctx.moveTo(robot.wheelLeft.x + 9, robot.wheelLeft.y);
//    ctx.lineTo(robot.wheelLeft.x + 9, robot.wheelLeft.y + robot.wheelRight.h);
//    ctx.lineDashOffset = 3 - tread;
//    ctx.moveTo(robot.wheelRight.x + 1, robot.wheelRight.y);
//    ctx.lineTo(robot.wheelRight.x + 1, robot.wheelRight.y + robot.wheelRight.h);
//    ctx.moveTo(robot.wheelRight.x + 9, robot.wheelRight.y);
//    ctx.lineTo(robot.wheelRight.x + 9, robot.wheelRight.y + robot.wheelRight.h);
//    ctx.stroke();
//    ctx.closePath();
    ctx.lineWidth = "0.5";
    ctx.setLineDash([]);
    //color   
    ctx.beginPath();
    ctx.arc(0, -15, robot.colorSensor.r, 0, Math.PI * 2);
    ctx.closePath();
    ctx.fillStyle = robot.colorSensor.color;
    ctx.fill();
    ctx.strokeStyle = "black";
    ctx.stroke();
    ctx.restore();

    // ultra 
    ctx.beginPath();
    ctx.lineWidth = "0.5";
    ctx.strokeStyle = "#999999";
    wave += 1;
    wave = wave % 60;
    ctx.lineDashOffset = 60 - wave;
    ctx.setLineDash([ 20, 40 ]);
    for (var i = 0; i < robot.ultraSensor.u.length; i++) {
        ctx.moveTo(robot.ultraSensor.rx, robot.ultraSensor.ry);
        ctx.lineTo(robot.ultraSensor.u[i].x, robot.ultraSensor.u[i].y);
    }
    ctx.stroke();
    ctx.closePath();
    ctx.beginPath();
    ctx.strokeStyle = "black";
    ctx.moveTo(robot.ultraSensor.rx, robot.ultraSensor.ry);
    ctx.lineTo(robot.ultraSensor.cx, robot.ultraSensor.cy);
    ctx.stroke();
    ctx.closePath();
    //obstacle
    ctx.beginPath();
    ctx.setLineDash([ 0 ]);
    ctx.strokeStyle = "black";
    ctx.shadowBlur = 10;
    ctx.shadowColor = "black";
    ctx.fillStyle = "#b3b3b3";
    ctx.fillRect(obstacle.x, obstacle.y, obstacle.w, obstacle.h);
    ctx.lineWidth = "1";
    ctx.rect(obstacle.x, obstacle.y, obstacle.w, obstacle.h);
    ctx.stroke();
    ctx.closePath();
};

function toRadians(degree) {
    return degree * (Math.PI / 180);
};

function toDegree(radians) {
    return radians * (180 / Math.PI);
};

function range(num) {
    if (num > MAXPOWER) {
        return MAXPOWER;
    }
    if (num < -MAXPOWER) {
        return -MAXPOWER;
    }
    return num;
};

// copy from http://stackoverflow.com/questions/2348597/why-doesnt-this-javascript-rgb-to-hsl-code-work
function rgbToHsv(r, g, b) {
    var min = Math.min(r, g, b), max = Math.max(r, g, b), delta = max - min, h, s, v = max;

    v = Math.floor(max / 255 * 100);
    if (max != 0)
        s = Math.floor(delta / max * 100);
    else {
        // black
        return [ 0, 0, 0 ];
    }

    if (r == max)
        h = (g - b) / delta; // between yellow & magenta
    else if (g == max)
        h = 2 + (b - r) / delta; // between cyan & yellow
    else
        h = 4 + (r - g) / delta; // between magenta & cyan

    h = Math.floor(h * 60); // degrees
    if (h < 0)
        h += 360;

    return [ h, s, v ];
}

function getColor(hsv) {
    if (hsv[2] <= 10)
        return COLOR_ENUM.BLACK;
    if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 90 && hsv[2] > 90)
        return COLOR_ENUM.RED;
    if (hsv[0] > 50 && hsv[0] < 70 && hsv[1] > 90 && hsv[2] > 90)
        return COLOR_ENUM.YELLOW;
    if (hsv[1] < 10 && hsv[2] > 90)
        return COLOR_ENUM.WHITE;
    if (hsv[0] > 110 && hsv[0] < 130 && hsv[1] > 90)
        return COLOR_ENUM.GREEN;
    if (hsv[0] > 230 && hsv[0] < 250 && hsv[1] > 90 && hsv[2] > 90)
        return COLOR_ENUM.BLUE;
    return COLOR_ENUM.NONE;
}

function intersectLines(l, o) {
    var d = (l.x1 - l.x2) * (o.y1 - o.y2) - (l.y1 - l.y2) * (o.x1 - o.x2);
    if (d == 0)
        return null;
    var xi = ((o.x1 - o.x2) * (l.x1 * l.y2 - l.y1 * l.x2) - (l.x1 - l.x2) * (o.x1 * o.y2 - o.y1 * o.x2)) / d;
    var yi = ((o.y1 - o.y2) * (l.x1 * l.y2 - l.y1 * l.x2) - (l.y1 - l.y2) * (o.x1 * o.y2 - o.y1 * o.x2)) / d;

    if (xi < Math.min(l.x1, l.x2) - 0.01 || xi > Math.max(l.x1, l.x2) + 0.01) {
        return null;
    }
    if (xi < Math.min(o.x1, o.x2) - 0.01 || xi > Math.max(o.x1, o.x2) + 0.01) {
        return null;
    }
    if (yi < Math.min(l.y1, l.y2) - 0.01 || yi > Math.max(l.y1, l.y2) + 0.01) {
        return null;
    }
    if (yi < Math.min(o.y1, o.y2) - 0.01 || yi > Math.max(o.y1, o.y2) + 0.01) {
        return null;
    }
    return {
        x : xi,
        y : yi
    };
}

function handleMouseDown(e) {
    e.preventDefault();
    var X = e.clientX || e.originalEvent.touches[0].pageX;
    var Y = e.clientY || e.originalEvent.touches[0].pageY;
    startX = (parseInt(X - offsetX)) / scale;
    startY = (parseInt(Y - offsetY)) / scale;
    var dx = startX - robot.mouse.rx;
    var dy = startY - robot.mouse.ry;
    isDownrobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
    isDownObstacle = (startX > obstacle.x && startX < obstacle.x + obstacle.w && startY > obstacle.y && startY < obstacle.y + obstacle.h);
};

function handleMouseUp(e) {
    e.preventDefault();
    if (!isDownrobot && !isDownObstacle) {
        if (startX < ground.w / 2)
            robot.pose.theta += toRadians(-5);
        else
            robot.pose.theta += toRadians(5);
    }
    $("#objectLayer").css('cursor', 'auto');
    isDownrobot = false;
    isDownObstacle = false;
};

function handleMouseOut(e) {
    e.preventDefault();
    isDownrobot = false;
    isDownObstacle = false;
};

function handleMouseMove(e) {
    e.preventDefault();
    if (!isDownrobot && !isDownObstacle) {
        return;
    }
    $("#objectLayer").css('cursor', 'pointer');
    var X = e.clientX || e.originalEvent.touches[0].pageX;
    var Y = e.clientY || e.originalEvent.touches[0].pageY;
    mouseX = (parseInt(X - offsetX)) / scale;
    mouseY = (parseInt(Y - offsetY)) / scale;
    var dx = (mouseX - startX);
    var dy = (mouseY - startY);
    startX = mouseX;
    startY = mouseY;
    if (isDownrobot) {
        robot.pose.x += dx;
        robot.pose.y += dy;
        robot.mouse.rx += dx;
        robot.mouse.ry += dy;
    } else {
        obstacle.x += dx;
        obstacle.y += dy;
    }
    return false;
};

function getLines(obstacle) {
    return [ {
        x1 : obstacle.x,
        x2 : obstacle.x,
        y1 : obstacle.y,
        y2 : obstacle.y + obstacle.h
    }, {

        x1 : obstacle.x,
        x2 : obstacle.x + obstacle.w,
        y1 : obstacle.y,
        y2 : obstacle.y
    }, {
        x1 : obstacle.x + obstacle.w,
        x2 : obstacle.x,
        y1 : obstacle.y + obstacle.h,
        y2 : obstacle.y + obstacle.h
    }, {
        x1 : obstacle.x + obstacle.w,
        x2 : obstacle.x + obstacle.w,
        y1 : obstacle.y + obstacle.h,
        y2 : obstacle.y
    } ];
}

function sqr(x) {
    return x * x
}
function dist2(v, w) {
    return sqr(v.x - w.x) + sqr(v.y - w.y)
}
function distToParallel(p, v, w) {
    var l2 = dist2(v, w);
    if (l2 == 0)
        return v;
    var t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
    if (t < 0)
        return v;
    if (t > 1)
        return w;
    return ({
        x : v.x + t * (w.x - v.x),
        y : v.y + t * (w.y - v.y)
    });
}

function adjustAllSizes() {
    canvasOffset = $("#backgroundDiv").offset();
    offsetX = canvasOffset.left;
    offsetY = canvasOffset.top;
    playground.w = $('#simDiv').width();
    playground.h = $(window).height() - offsetY;
    var oldScale = scale;
    scale = 1;
    if (window.outerWidth < 800) {// extra small devices
        scale = 0.5
    } else if (window.outerWidth < 1280) {// medium and large devices     
        scale = 0.8;
    } else if (window.outerWidth >= 1920) {// medium and large devices     
        scale = 1.5;
    }
    ground.w = playground.w / scale;
    ground.h = playground.h / scale;
    MAXDIAG = Math.sqrt(sqr(ground.w) + sqr(ground.h));
    if (oldScale != scale) {
        drawBackground(scale, backCanvas, bCtx);
    }
}

function Robot() {
    this.geom = {
        x : -20,
        y : -20,
        w : 40,
        h : 50,
        color : '#FCCC00'
    };
    this.pose = {
        x : 400,
        y : 40,
        theta : 0,
        thetaDiff : 0
    };
    this.wheelLeft = {
        x : 16,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    this.wheelRight = {
        x : -24,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    this.led = {
        x : 0,
        y : 5,
        color : '#dddddd',
        // color : 'DeepSkyBlue',
        // color : 'orange',
        mode : ''
    };
    this.encoder = {
        left : 0,
        right : 0
    }
    this.colorSensor = {
        x : 0,
        y : -15,
        rx : 0,
        ry : 0,
        r : 5,
        lightValue : 0,
        colorValue : 0,
        color : 'grey'
    };
    this.ultraSensor = {
        x : 0,
        y : -20,
        rx : 0,
        ry : 0,
        distance : 0,
        u : [],
        cx : 0,
        cy : 0,
        color : '#FF69B4'
    };
    this.touchSensor = {
        x : 0,
        y : -25,
        x1 : 0,
        y1 : 0,
        x2 : 0,
        y2 : 0,
        value : 0,
        color : "#FFCC33"
    };
    this.frontLeft = {
        x : 22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false,
    };
    this.frontRight = {
        x : -22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false,
    };
    this.backLeft = {
        x : 20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false,
    };
    this.backRight = {
        x : -20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false,
    };
    this.backMiddle = {
        x : 0,
        y : 30,
        rx : 0,
        ry : 0,
    };
    this.mouse = {
        x : 0,
        y : 5,
        rx : 0,
        ry : 0,
        r : 30
    };
    this.updateLocations = function() {
        var sin = Math.sin(this.pose.theta);
        var cos = Math.cos(this.pose.theta);
        this.frontRight = this.translate(sin, cos, this.frontRight);
        this.frontLeft = this.translate(sin, cos, this.frontLeft);
        this.backRight = this.translate(sin, cos, this.backRight);
        this.backLeft = this.translate(sin, cos, this.backLeft);
        this.backMiddle = this.translate(sin, cos, this.backMiddle);

        this.touchSensor = this.translate(sin, cos, this.touchSensor);
        this.colorSensor = this.translate(sin, cos, this.colorSensor);
        this.ultraSensor = this.translate(sin, cos, this.ultraSensor);
        this.mouse = this.translate(sin, cos, this.mouse);

        this.touchSensor.x1 = this.frontRight.rx;
        this.touchSensor.y1 = this.frontRight.ry;
        this.touchSensor.x2 = this.frontLeft.rx;
        this.touchSensor.y2 = this.frontLeft.ry;

    }

    this.translate = function(sin, cos, point) {
        point.rx = this.pose.x - point.y * cos + point.x * sin;
        point.ry = this.pose.y - point.y * sin - point.x * cos;
        return point;
    }
}

function loadImages() {
    img = new Image();
    img.onload = initScene;
    img.src = "simulation/simulationLogic/beta.svg";
}

function addMouseEvents() {
    $("#objectLayer").mousedown(function(e) {
        handleMouseDown(e);
    });
    $("#objectLayer").mousemove(function(e) {
        handleMouseMove(e);
    });
    $("#objectLayer").mouseup(function(e) {
        handleMouseUp(e);
    });
    $("#objectLayer").mouseout(function(e) {
        handleMouseOut(e);
    });
    $("#objectLayer").bind('touchmove', function(e) {
        handleMouseMove(e);
    });
    $("#objectLayer").bind('touchleave', function(e) {
        handleMouseOut(e);
    });
    $("#objectLayer").bind('touchstart', function(e) {
        handleMouseDown(e);
    });
    $("#objectLayer").bind('touchend', function(e) {
        handleMouseUp(e);
    });
}

function removeMouseEvents() {
    $("#objectLayer").off("mousedown");
    $("#objectLayer").off("mousemove");
    $("#objectLayer").off("mouseup");
    $("#objectLayer").off("mouseout");
    $("#objectLayer").unbind();
}

function initLayers() {
    $('<canvas id ="backgroundLayer" width=1400 height=950></canvas>').appendTo(document.getElementById("backgroundDiv"));
    backCanvas = document.getElementById("backgroundLayer");
    bCtx = backCanvas.getContext("2d");
    $('<canvas id ="unitBackgroundLayer" width=1400 height=950></canvas>').appendTo(document.getElementById("backgroundDiv"));
    uniCanvas = document.getElementById("unitBackgroundLayer");
    uCtx = uniCanvas.getContext("2d");
    $('<canvas id ="objectLayer" width=1400 height=950></canvas>').appendTo(document.getElementById("objectDiv"));
    objectCanvas = document.getElementById("objectLayer");
    oCtx = objectCanvas.getContext("2d");
}

function destroyLayers() {
    var layer = document.getElementById("backgroundLayer");
    while (layer.firstChild) {
        layer.removeChild(myNode.firstChild);
    }
    layer = document.getElementById("unitBackgroundLayer");
    while (layer.firstChild) {
        layer.removeChild(myNode.firstChild);
    }
    layer = document.getElementById("objectLayer");
    while (layer.firstChild) {
        layer.removeChild(myNode.firstChild);
    }
}

function initScene() {
    adjustAllSizes();
    drawBackground(1, uniCanvas, uCtx); // unitary background for sensor calculations (only once)
    drawBackground(scale, backCanvas, bCtx);
    drawObject(scale, objectCanvas, oCtx);
    addMouseEvents();
    oraSimRender();
    setTimeout(function() {
        setPause(false)
    }, 1000);
}

function setTimeStep() {
    var actualTime = new Date().getTime();
    var actualTimeStep = actualTime - oldTime;
    oldTime = actualTime;
    error = actualTimeStep / 1000 - STEP_TIME;
    integral = integral + error;
    timerStep = (STEP_TIME - kP * error - kP * integral) * 1000;
    if (timerStep < 1) {
        integral = 0;
        timerStep = 1;
    }
    timeOld = actualTime;
    averageTimeStep = 0.9 * averageTimeStep + 0.1 * actualTimeStep;
}
