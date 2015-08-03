var TRACKWIDTH = 40;
var FPS = 30;
var STEP_TIME = 1 / FPS;
var MAXDIAG = 0;
var ENC = 360 / (2 * Math.PI * 5.6)

var canvas;
var ctx;
var canvasOffset;
var offsetX;
var offsetY;
var isDownrobot = false;
var isDownObstacle = false;
var startX;
var startY;

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
var playground = {
    x : 0,
    y : 0,
    w : 500,
    h : 500
}
var obstacle = {
    x : 300,
    y : 25,
    w : 75,
    h : 75
}
var obstacleList = [ playground, obstacle ];

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

function initOraSim() {
    $('<canvas id ="' + 'ground' + '" width="' + $("#WebGLCanvas").width() + '" height="' + $("#blocklyDiv").height() + '""></canvas>').appendTo(
            document.getElementById("WebGLCanvas"));
    canvas = document.getElementById("ground");
    ctx = canvas.getContext("2d");
    isDownrobot = false;
    isDownObstacle = false;
    stepCounter = 0;
    pause = true;
    info = false;
    oldTime = new Date().getTime();
    averageTimeStep = STEP_TIME;
    var error = 0;
    var integral = 0;
    $("#ground").mousedown(function(e) {
        handleMouseDown(e);
    });
    $("#ground").mousemove(function(e) {
        handleMouseMove(e);
    });
    $("#ground").mouseup(function(e) {
        handleMouseUp(e);
    });
    $("#ground").mouseout(function(e) {
        handleMouseOut(e);
    });
    adjustAllSizes();
    robot = new Robot();
    oraSimRender();
}

function oraSimRender() {
    var actualTime = new Date().getTime();
    var actualTimeStep = actualTime - oldTime;
    oldTime = actualTime;
    error = actualTimeStep / 1000 - STEP_TIME;
    integral = integral + error;
    var newTimerStep = (STEP_TIME - kP * error - kP * integral) * 1000;
    if (newTimerStep < 1) {
        integral = 0;
        newTimerStep = 1;
    }
    timeOld = actualTime;
    averageTimeStep = 0.9 * averageTimeStep + 0.1 * actualTimeStep;
    stepCounter += 1;
    if (!PROGRAM_SIMULATION.isTerminated() && !pause === true) {
        //executeProgram();
        if (stepCounter == 0) {
            setPause(true);
        }
        step(input);
        output.left = ACTORS.getLeftMotor().getPower() * 80;
        output.right = ACTORS.getRightMotor().getPower() * 80;
        //output.led = LIGHT.getColor();
        //output.ledMode = LIGHT.getMode();
    } else {
        output.left = 0;
        output.right = 0;

    }
    diffDrive(output);
    drawBackground();
    setSensorValues();
    drawrobot();
    setTimeout(function() {
        oraSimRender();
    }, newTimerStep);
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

    $('#labelSpeedLeft').text(Math.round(output.left / 0.8));
    $('#labelSpeedRight').text(Math.round(output.right / 0.8));
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
    var colors = ctx.getImageData(Math.round(robot.colorSensor.rx - 4), Math.round(robot.colorSensor.ry - 4), 8, 8);
    var out = [ 0, 4, 24, 28, 32, 60, 192, 220, 224, 228, 248, 252 ]; // outside the circle
    var b = 0
    for (var j = 0; j < colors.data.length; j += 32) {
        for (var i = j; i < j + 32; i += 4) {
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

function drawBackground() {
    adjustAllSizes();
    canvas.width = playground.w;
    canvas.height = playground.h;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#00FFFF";
    ctx.fillRect(playground.x, playground.y, playground.w, playground.h);
    ctx.fillStyle = "red";
    ctx.fillRect(120, playground.h - 80, 40, 60);
    ctx.fillStyle = "green";
    ctx.fillRect(160, playground.h - 80, 40, 60);
    ctx.fillStyle = "yellow";
    ctx.fillRect(200, playground.h - 80, 40, 60);
    ctx.fillStyle = "blue";
    ctx.fillRect(240, playground.h - 80, 40, 60);
    ctx.strokeStyle = "#ffffff";
    ctx.lineWidth = "30";
    ctx.beginPath();
    ctx.arc(playground.w / 2, playground.h / 2, 125, 0, Math.PI * 1.5);
    ctx.closePath();
    ctx.stroke();
    ctx.strokeStyle = "#000000";
    ctx.lineWidth = "10";
    ctx.beginPath();
    ctx.arc(playground.w / 2, playground.h / 2, 125, 0, Math.PI * 1.5);
    ctx.closePath();
    ctx.stroke();
    ctx.fillStyle = "#b3b3b3";
    ctx.fillRect(obstacle.x, obstacle.y, obstacle.w, obstacle.h);
    ctx.lineWidth = "1";
    ctx.rect(obstacle.x, obstacle.y, obstacle.w, obstacle.h);
    ctx.stroke();
};

function drawrobot() {
    // provide new user information   
    if (info === true) {
        var endLabel = playground.w - 45;
        var endValue = playground.w - 5;
        ctx.textAlign = "end";
        ctx.font = "10px Verdana";
        ctx.fillStyle = "#333333";
        ctx.fillText("FPS", endLabel, 25);
        ctx.fillText(Math.round(1000 / averageTimeStep), endValue, 25);
        ctx.fillText("Pose X", endLabel, 40);
        ctx.fillText(Math.round(robot.pose.x), endValue, 40);
        ctx.fillText("Pose Y", endLabel, 55);
        ctx.fillText(Math.round(robot.pose.y), endValue, 55);
        ctx.fillText("Pose θ", endLabel, 70);
        ctx.fillText(Math.round(Math.round(toDegree(robot.pose.theta))), endValue, 70);
        ctx.fillText("Motor left", endLabel, 90);
        ctx.fillText(Math.round(robot.encoder.left * ENC), endValue, 90);
        ctx.fillText("Motor right", endLabel, 105);
        ctx.fillText(Math.round(robot.encoder.right * ENC), endValue, 105);
        ctx.fillText("Touch Sensor", endLabel, 120);
        ctx.fillText(Math.round(robot.touchSensor.value), endValue, 120);
        ctx.fillText("Light Sensor", endLabel, 135);
        ctx.fillText(Math.round(Math.round(robot.colorSensor.lightValue)), endValue, 135);
        ctx.fillText("Ultra Sensor", endLabel, 150);
        ctx.fillText(Math.round(robot.ultraSensor.distance / 2), endValue, 150);
        ctx.fillText("Color Sensor", endLabel, 165);
        if (robot.colorSensor.colorValue === COLOR_ENUM.NONE) {
            ctx.fillStyle = 'grey';
        } else if (robot.colorSensor.colorValue === COLOR_ENUM.BLACK) {
            ctx.fillStyle = 'black';
        } else if (robot.colorSensor.colorValue == COLOR_ENUM.WHITE) {
            ctx.fillStyle = 'white';
        } else if (robot.colorSensor.colorValue === COLOR_ENUM.YELLOW) {
            ctx.fillStyle = 'yellow';
        } else if (robot.colorSensor.colorValue === COLOR_ENUM.RED) {
            ctx.fillStyle = 'red';
        } else if (robot.colorSensor.colorValue === COLOR_ENUM.BLUE) {
            ctx.fillStyle = 'blue';
        } else if (robot.colorSensor.colorValue === COLOR_ENUM.GREEN) {
            ctx.fillStyle = 'green';
        }
        ctx.fillRect(endValue, 165, -10, -10);
    }
    ctx.save();
    ctx.translate(robot.pose.x, robot.pose.y);
    ctx.rotate(toRadians(toDegree(robot.pose.theta) - 90));
    ctx.scale(1, -1);
    //axis
    ctx.lineWidth = "5";
    ctx.strokeStyle = robot.wheelLeft.color;
    ctx.beginPath();
    ctx.moveTo(robot.geom.x - 7, 0);
    ctx.lineTo(robot.geom.x + robot.geom.w + 7, 0);
    ctx.closePath();
    ctx.stroke();
    //back wheel
    ctx.fillStyle = "black";
    ctx.ellipse(0, 30, 3, 6, robot.pose.thetaDiff, 0, Math.PI * 2);
    ctx.fill();
    //robot
    ctx.fillStyle = robot.led.color;
    var grd = ctx.createRadialGradient(0, 10, 1, 0, 10, 15);
    grd.addColorStop(0, robot.led.color);
    grd.addColorStop(0.5, robot.geom.color);
    ctx.fillStyle = grd;
    ctx.fillRect(robot.geom.x, robot.geom.y, robot.geom.w, robot.geom.h);
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
    ctx.fillStyle = 'red';
    //ultrasonic
//    ctx.lineWidth = "5";
//    ctx.strokeStyle = robot.ultraSensor.color;
//    ctx.beginPath();
//    ctx.arc(robot.ultraSensor.x, robot.ultraSensor.y, 50, -Math.PI / 8 - Math.PI / 2, Math.PI / 8 - Math.PI / 2);
//    ctx.stroke();
//    ctx.beginPath();
//    ctx.arc(robot.ultraSensor.x, robot.ultraSensor.y, 25, -Math.PI / 8 - Math.PI / 2, Math.PI / 8 - Math.PI / 2);
//    ctx.stroke();
//    ctx.beginPath();
//    ctx.arc(robot.ultraSensor.x, robot.ultraSensor.y, 75, -Math.PI / 8 - Math.PI / 2, Math.PI / 8 - Math.PI / 2);
//    ctx.stroke();
    ctx.lineWidth = "1";
    //color   
    ctx.strokeStyle = "black";
    ctx.beginPath();
    ctx.arc(0, -15, robot.colorSensor.r, 0, Math.PI * 2);
    ctx.stroke();
    ctx.closePath();
//    //ultra   
//    ctx.beginPath();
//
//    ctx.fillStyle = "black";
//    ctx.arc(-2.5, -20, 2.5, 0, Math.PI * 2);
//    ctx.fill();
//    ctx.arc(2.5, -20, 2.5, 0, Math.PI * 2);
//    ctx.fill();
//    ctx.closePath();

    //touch
    if (robot.touchSensor.value === 1) {
        ctx.fillStyle = 'red';
        ctx.fillRect(robot.frontRight.x, robot.frontRight.y, robot.frontLeft.x - robot.frontRight.x, 3.5);
    } else {
        ctx.fillStyle = robot.touchSensor.color;
        ctx.fillRect(robot.frontRight.x, robot.frontRight.y, robot.frontLeft.x - robot.frontRight.x, 3.5);
    }
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
    if (num > 80) {
        return 80;
    }
    if (num < -80) {
        return -80;
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
    console.log(hsv);
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
    startX = parseInt(e.clientX - offsetX);
    startY = parseInt(e.clientY - offsetY);
    var dx = startX - robot.mouse.rx;
    var dy = startY - robot.mouse.ry;
    isDownrobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
    isDownObstacle = (startX > obstacle.x && startX < obstacle.x + obstacle.w && startY > obstacle.y && startY < obstacle.y + obstacle.w);
};

function handleMouseUp(e) {
    if (!isDownrobot && !isDownObstacle) {
        if (startX < playground.w / 2)
            robot.pose.theta += toRadians(-5);
        else
            robot.pose.theta += toRadians(5);
    }
    $("#ground").css('cursor','auto');
    e.preventDefault();
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
    $("#ground").css('cursor','pointer');
    mouseX = parseInt(e.clientX - offsetX);
    mouseY = parseInt(e.clientY - offsetY);
    var dx = mouseX - startX;
    var dy = mouseY - startY;
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
};

//programs
//$('#start').on('click', function() {
//    if (start === false) {
//        start = true;
//        $('#start').text("speed off");
//        touch = false;
//        $('#touch').text("touch on");
//        line = false;
//        $('#line').text("line on");
//        ultra = false;
//        $('#ultra').text("ultra on");
//    } else {
//        start = false;
//        $('#start').text("speed on");
//    }
//});
//$('#touch').on('click', function() {
//    if (touch === false) {
//        touch = true;
//        $('#touch').text("touch off");
//        start = false;
//        $('#start').text("speed on");
//        line = false;
//        $('#line').text("line on");
//        ultra = false;
//        $('#ultra').text("ultra on");
//    } else {
//        touch = false;
//        $('#touch').text("touch on");
//    }
//});
//$('#line').on('click', function() {
//    if (line === false) {
//        line = true;
//        $('#line').text("line off");
//        start = false;
//        $('#start').text("speed on");
//        touch = false;
//        $('#touch').text("touch on");
//        ultra = false;
//        $('#ultra').text("ultra on");
//    } else {
//        line = false;
//        $('#line').text("line on");
//    }
//});
//$('#ultra').on('click', function() {
//    if (ultra === false) {
//        ultra = true;
//        $('#ultra').text("ultra off");
//        touch = false;
//        $('#touch').text("touch on");
//        start = false;
//        $('#start').text("speed on");
//        line = false;
//        $('#line').text("line on");
//    } else {
//        ultra = false;
//        $('#ultra').text("ultra on");
//    }
//});
//// speeds
//$('#leftSpeed').on('change', function() {
//    speedLeft = 0.8 * $(this).val();
//    $('#labelUSpeedLeft').text(Math.round(speedLeft / 0.8));
//});
//$('#rightSpeed').on('change', function() {
//    speedRight = 0.8 * $(this).val();
//    $('#labelUSpeedRight').text(Math.round(speedRight / 0.8));
//});
//$('#fps').on('change', function() {
//    FPS = $(this).val();
//    STEP_TIME = 1 / FPS;
//    $('#labelFps').text(FPS);
//});

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
    canvasOffset = $("#ground").offset();
    offsetX = canvasOffset.left;
    offsetY = canvasOffset.top;
    playground.w = $('#simDiv').width();
    playground.h = $(window).height() - offsetY;
    MAXDIAG = Math.sqrt(sqr(playground.w) + sqr(playground.h));
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
        x : playground.w / 2,
        y : playground.h / 2,
        theta : 0,
        thetaDiff : 0
    };
    this.wheelLeft = {
        x : -25,
        y : -10,
        w : 10,
        h : 20,
        color : '#000000'
    };
    this.wheelRight = {
        x : 15,
        y : -10,
        w : 10,
        h : 20,
        color : '#0000000'
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
        color : 'red'
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
        x : 25,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false,
    };
    this.frontRight = {
        x : -25,
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
