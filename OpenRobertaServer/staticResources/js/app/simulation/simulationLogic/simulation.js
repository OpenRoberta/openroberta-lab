/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */

/**
 * @namespace SIM
 */

define([ 'exports', 'simulation.scene', 'simulation.program.eval', 'simulation.math', 'program.controller', 'robertaLogic.constants', 'simulation.program.builder' ], function(exports, Scene, ProgramEval, SIMATH, ROBERTA_PROGRAM, CONST, BUILDER) {

    var programEval = new ProgramEval();

    var scene;
    var userProgram;
    var layers;
    var canvasOffset;
    var offsetX;
    var offsetY;
    var isDownrobot = false;
    var isDownObstacle = false;
    var isDownRuler = false;
    var startX;
    var startY;
    var scale = 1;
    var timerStep = 0;
    var ready;
    var canceled;

    var imgList = [ '/js/app/simulation/simBackgrounds/baustelle.svg', '/js/app/simulation/simBackgrounds/simpleBackground.svg', '/js/app/simulation/simBackgrounds/drawBackground.svg', '/js/app/simulation/simBackgrounds/robertaBackground.svg', '/js/app/simulation/simBackgrounds/rescueBackground.svg', '/js/app/simulation/simBackgrounds/mathBackground.svg', '/js/app/simulation/simBackgrounds/calliopeBackground.svg', '/js/app/simulation/simBackgrounds/microbitBackground.svg', '/js/app/simulation/simBackgrounds/ruler.svg' ];
    var imgListIE = [ '/js/app/simulation/simBackgrounds/baustelle.png', '/js/app/simulation/simBackgrounds/simpleBackground.png', '/js/app/simulation/simBackgrounds/drawBackground.png', '/js/app/simulation/simBackgrounds/robertaBackground.png', '/js/app/simulation/simBackgrounds/rescueBackground.png', '/js/app/simulation/simBackgrounds/mathBackground.png', '/js/app/simulation/simBackgrounds/ruler.png' ];
    var imgObjectList = [];

    function preloadImages() {
        if (isIE()) {
            imgList = imgListIE;
        }
        for (var i = 0; i < imgList.length; i++) {
            imgObjectList[i] = new Image();
            imgObjectList[i].src = imgList[i];
        }
    }
    preloadImages();

    var currentBackground = 1;

    function setBackground(num, callback) {
        if (num == undefined) {
            setObstacle();
            setRuler();
            scene = new Scene(imgObjectList[currentBackground], layers, robot, obstacle, ruler);
            scene.updateBackgrounds();
            scene.drawObjects();
            scene.drawRuler();
            reloadProgram();

            return currentBackground;
        }
        setPause(true);
        ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimStart(true);
        if (num === 0) {
            currentBackground += 1;
            if (currentBackground > 5) {
                currentBackground = 1;
            }
        } else {
            currentBackground = num;
        }
        var debug = robot.debug;
        var moduleName = 'simulation.robot.' + simRobotType;
        require([ moduleName ], function(ROBOT) {
            if (currentBackground == 1) {
                robot = new ROBOT({
                    x : 240,
                    y : 200,
                    theta : 0,
                    xOld : 240,
                    yOld : 200,
                    transX : 0,
                    transY : 0
                });
                robot.canDraw = false;
            } else if (currentBackground == 2) {
                robot = new ROBOT({
                    x : 200,
                    y : 200,
                    theta : 0,
                    xOld : 200,
                    yOld : 200,
                    transX : 0,
                    transY : 0
                });
                robot.canDraw = true;
                robot.drawColor = "#000000";
                robot.drawWidth = 10;
            } else if (currentBackground == 3) {
                robot = new ROBOT({
                    x : 70,
                    y : 86,
                    theta : 0,
                    xOld : 70,
                    yOld : 86,
                    transX : 0,
                    transY : 0
                });
                robot.canDraw = false;
            } else if (currentBackground == 4) {
                robot = new ROBOT({
                    x : 400,
                    y : 40,
                    theta : 0,
                    xOld : 400,
                    yOld : 40,
                    transX : 0,
                    transY : 0
                });
                robot.canDraw = false;
            } else if (currentBackground == 5) {
                robot = new ROBOT({
                    x : 400,
                    y : 250,
                    theta : 0,
                    xOld : 400,
                    yOld : 250,
                    transX : -400,
                    transY : -250
                });
                robot.canDraw = true;
                robot.drawColor = "#ffffff";
                robot.drawWidth = 1;
            }
            robot.debug = debug;
            callback();
        });

    }
    exports.setBackground = setBackground;

    function getBackground() {
        return currentBackground;
    }

    exports.getBackground = getBackground;

    var time;

    var dt = 0;

    function getDt() {
        return dt;
    }
    exports.getDt = getDt;

    var pause;

    function setPause(value) {
        if (!value && !ready) {
            setTimeout(function() {
                setPause(false);
            }, 100);
        } else {
            if (value) {
                $('.simForward').removeClass('typcn-media-pause');
                $('.simForward').addClass('typcn-media-play');
                ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimForward(true);
            } else {
                $('.simForward').removeClass('typcn-media-play');
                $('.simForward').addClass('typcn-media-pause');
                ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimForward(false);
                ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimStart(false);
            }
            pause = value;
        }
        if (robot.left)
            robot.left = 0;
        if (robot.right)
            robot.right = 0;
    }
    exports.setPause = setPause;

    var stepCounter;

    function setStep() {
        stepCounter = -50;
        setPause(false);
    }
    exports.setStep = setStep;

    var info;
    function setInfo() {
        if (info === true) {
            info = false;
        } else {
            info = true;
        }
    }
    exports.setInfo = setInfo;

    function stopProgram() {
        setPause(true);
        scene.updateBackgrounds();
        reloadProgram();
    }

    exports.stopProgram = stopProgram;

    var cP = 1;
    var turn = false;
    var back = false;
    var encoderTouch = 0;
    var start = false;
    var touch = false;
    var line = false;
    var ultra = false;

// obstacles
// scaled playground
    var ground = {
        x : 0,
        y : 0,
        w : 500,
        h : 500
    };

    var obstacle = {
        x : 0,
        y : 0,
        xOld : 0,
        yOld : 0,
        w : 0,
        h : 0,
        wOld : 0,
        hOld : 0
    };

    var ruler = {
        x : 0,
        y : 0,
        xOld : 0,
        yOld : 0,
        w : 0,
        h : 0,
        wOld : 0,
        hOld : 0
    }
    // Note: The ruler is not considered an obstacle. The robot will
    // simply drive over it.
    exports.obstacleList = [ ground, obstacle ];

// render stuff
    var globalID;
    var robot;
    var simRobotType;
    var ROBOT;

    function init(program, refresh, robotType) {
        reset = false;
        simRobotType = robotType;
        userProgram = program;
        if (robotType === 'calliope') {
            currentBackground = 6;
            $('.dropdown.sim, .simScene').hide();
        } else if (robotType === 'microbit') {
            $('.dropdown.sim,.simScene').hide();
            currentBackground = 7;
        } else if (currentBackground == 6 || currentBackground == 7) {
            currentBackground = 1;
            $('.dropdown.sim,.simScene').show();
        }
        var blocklyProgram = BUILDER.build(userProgram);
        programEval.initProgram(blocklyProgram);
        if (refresh) {
            require([ 'simulation.robot.' + simRobotType ], function(reqRobot) {
                robot = new reqRobot({
                    x : 240,
                    y : 200,
                    theta : 0,
                    xOld : 240,
                    yOld : 200,
                    transX : 0,
                    transY : 0
                });
                robot.reset();
                robot.resetPose();
                ready = false;
                removeMouseEvents();
                canceled = false;
                isDownrobot = false;
                isDownObstacle = false;
                isDownRuler = false;
                stepCounter = 0;
                pause = true;
                info = false;
                setObstacle();
                initScene();
            });

        } else {
            robot.debug = false;
            if (robot.endless)
                robot.reset();
            reloadProgram();
        }
    }
    exports.init = init;

    function cancel() {
        $(window).off("resize");
        canceled = true;
        removeMouseEvents();
        destroyLayers();
    }
    exports.cancel = cancel;

    var sensorValues = {};
    var reset = false;

    function render() {
        if (canceled) {
            cancelAnimationFrame(globalID);
            return;
        }
        var actionValues = {};
        globalID = requestAnimationFrame(render);
        var now = new Date().getTime();
        dt = now - (time || now);
        dt /= 1000;
        time = now;

        stepCounter += 1;

        if (!programEval.getProgram().isTerminated() && !pause && !reset) {
//            executeProgram();  //for tests without OpenRobertaLab
//            if (stepCounter === 0) {
//                setPause(true);
//            }
            actionValues = programEval.step(sensorValues);

        } else if (programEval.getProgram().isTerminated() && !pause && !robot.endless) {
            setPause(true);
            robot.reset();
            ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimStart(true);
        } else if (reset && !pause) {
            reset = false;
            robot.buttons.Reset = false;
            removeMouseEvents();
            setPause(true);
            robot.reset();
            scene.drawRobot();
            // some time to cancel all timeouts
            setTimeout(function() {
                init(userProgram, false, simRobotType);
                addMouseEvents();
            }, 205);
            setTimeout(function() {
                //delete robot.button.Reset;
                setPause(false);
            }, 1000);
        }
        robot.update(actionValues);
        sensorValues = scene.updateSensorValues(!pause);
        reset = sensorValues.buttons.Reset;
        scene.drawRobot();
    }

    function reloadProgram() {
        $('.simForward').removeClass('typcn-media-pause');
        $('.simForward').addClass('typcn-media-play');
        ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.setSimForward(true);
    }

    function setObstacle() {
        if (currentBackground == 2) {
            obstacle.x = 500;
            obstacle.y = 250;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = null;
            obstacle.color = "#33B8CA";
        } else if (currentBackground == 1) {
            obstacle.x = 580;
            obstacle.y = 290;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = null;
            obstacle.color = "#33B8CA";
        } else if (currentBackground == 3) {
            obstacle.x = 500;
            obstacle.y = 250;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = imgObjectList[0];
            obstacle.color = null;
        } else if (currentBackground == 5) {
            obstacle.x = 0;
            obstacle.y = 0;
            obstacle.w = 0;
            obstacle.h = 0;
            obstacle.color = null;
        } else if (currentBackground == 6) {
            obstacle.x = 0;
            obstacle.y = 0;
            obstacle.w = 0;
            obstacle.h = 0;
            obstacle.color = null;
        } else {
            obstacle.x = 495;
            obstacle.y = 396;
            obstacle.w = 20;
            obstacle.h = 20;
            obstacle.color = "#33B8CA";
            obstacle.img = null;
        }
    }

    function setRuler() {
        if (currentBackground == 3) {
            ruler.x = 430;
            ruler.y = 380;
            ruler.w = 300;
            ruler.h = 30;
            ruler.img = imgObjectList[8];
            ruler.color = null;
        } else {
            // All other scenes currently don't have a movable ruler.
            ruler.x = 0;
            ruler.y = 0;
            ruler.w = 0;
            ruler.h = 0;
            ruler.img = null;
            ruler.color = null;
        }
    }

    function handleMouseDown(e) {
        e.preventDefault();
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        startX = (parseInt(X - offsetX, 10)) / scale;
        startY = (parseInt(Y - offsetY, 10)) / scale;
        var dx = startX - robot.mouse.rx;
        var dy = startY - robot.mouse.ry;
        isDownrobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
        isDownObstacle = (startX > obstacle.x && startX < obstacle.x + obstacle.w && startY > obstacle.y && startY < obstacle.y + obstacle.h);
        isDownRuler = (startX > ruler.x && startX < ruler.x + ruler.w && startY > ruler.y && startY < ruler.y + ruler.h);
        e.stopPropagation();
    }

    function handleMouseUp(e) {
        e.preventDefault();
        if (!isDownrobot && !isDownObstacle && !isDownRuler) {
            if (startX < ground.w / 2) {
                robot.pose.theta += SIMATH.toRadians(-5);
            } else {
                robot.pose.theta += SIMATH.toRadians(5);
            }
        }
        $("#robotLayer").css('cursor', 'auto');
        if (robot.drawWidth) {
            robot.canDraw = true;
        }
        isDownrobot = false;
        isDownObstacle = false;
        isDownRuler = false;
        e.stopPropagation();
    }

    function handleMouseOut(e) {
        e.preventDefault();
        isDownrobot = false;
        isDownObstacle = false;
        isDownRuler = false;
        e.stopPropagation();
    }

    function handleMouseMove(e) {
        e.preventDefault();
        if (!isDownrobot && !isDownObstacle && !isDownRuler) {
            var X = e.clientX || e.originalEvent.touches[0].pageX;
            var Y = e.clientY || e.originalEvent.touches[0].pageY;
            var startx = (parseInt(X - offsetX, 10)) / scale;
            var starty = (parseInt(Y - offsetY, 10)) / scale;
            var dx = startx - robot.mouse.rx;
            var dy = starty - robot.mouse.ry;
            var hoverRobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
            var hoverObstacle = (startx > obstacle.x && startx < obstacle.x + obstacle.w && starty > obstacle.y && starty < obstacle.y + obstacle.h);
            var hoverRuler = (startx > ruler.x && startx < ruler.x + ruler.w && starty > ruler.y && starty < ruler.y + ruler.h);
            if (hoverRobot || hoverObstacle || hoverRuler)
                $("#robotLayer").css('cursor', 'pointer');
            else
                $("#robotLayer").css('cursor', 'auto');
            e.stopPropagation();
            return;
        }
        $("#robotLayer").css('cursor', 'pointer');
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        mouseX = (parseInt(X - offsetX, 10)) / scale;
        mouseY = (parseInt(Y - offsetY, 10)) / scale;
        var dx = (mouseX - startX);
        var dy = (mouseY - startY);
        startX = mouseX;
        startY = mouseY;
        if (isDownrobot) {
            if (robot.drawWidth) {
                robot.canDraw = false;
            }
            robot.pose.xOld = robot.pose.x;
            robot.pose.yOld = robot.pose.y;
            robot.pose.x += dx;
            robot.pose.y += dy;
            robot.mouse.rx += dx;
            robot.mouse.ry += dy;
        } else if (isDownObstacle) {
            obstacle.x += dx;
            obstacle.y += dy;
            scene.drawObjects();
        } else { // isDownRuler
            ruler.x += dx;
            ruler.y += dy;
            scene.drawRuler();
        }
        e.stopPropagation();
    }

    function resizeAll() {
        if (!canceled) {
            canvasOffset = $("#backgroundDiv").offset();
            offsetX = canvasOffset.left;
            offsetY = canvasOffset.top;
            scene.playground.w = $('#simDiv').outerWidth();
            scene.playground.h = $(window).height() - offsetY;
            var oldScale = scale;
            scale = 1;
            if ($(window).width() < 768) {// extra small devices
                scale = 0.5
            } else if ($(window).width() < 1024) {
                // medium and large devices
                scale = 0.7;
            } else if ($(window).width() < 1280) {
                // medium and large devices
                scale = 0.8;
            } else if ($(window).width() >= 1920) {
                // medium and large devices
                scale = 1.5;
            }
            ground.w = scene.playground.w / scale;
            ground.h = scene.playground.h / scale;
            scene.updateBackgrounds();
            scene.drawObjects();
            scene.drawRuler();
        }
    }

    function addMouseEvents() {
        $("#robotLayer").mousedown(function(e) {
            if (robot.handleMouseDown)
                robot.handleMouseDown(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseDown(e);
        });
        $("#robotLayer").mousemove(function(e) {
            if (robot.handleMouseMove)
                robot.handleMouseMove(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseMove(e);
        });
        $("#robotLayer").mouseup(function(e) {
            if (robot.handleMouseUp)
                robot.handleMouseUp(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseUp(e);
        });
        $("#robotLayer").mouseout(function(e) {
            if (robot.handleMouseOut)
                robot.handleMouseOut(e, offsetX, offsetY);
            else
                handleMouseOut(e);
        });
        $("#robotLayer").bind('touchmove', function(e) {
            if (robot.handleMouseMove)
                robot.handleMouseMove(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseMove(e);
        });
        $("#robotLayer").bind('touchcancel', function(e) {
            if (robot.handleMouseOut)
                robot.handleMouseOut(e, offsetX, offsetY, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseOut(e);
        });
        $("#robotLayer").bind('touchstart', function(e) {
            if (robot.handleMouseDown)
                robot.handleMouseDown(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            else
                handleMouseDown(e);
        });
//        $("#robotLayer").bind('touchend', function(e) {
//            if (robot.handleMouseUp)
//                robot.handleMouseUp(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
//            else
//                handleMouseUp(e);
//        });
    }

    function removeMouseEvents() {
        $("#robotLayer").off("mousedown");
        $("#robotLayer").off("mousemove");
        $("#robotLayer").off("mouseup");
        $("#robotLayer").off("mouseout");
        $("#robotLayer").unbind();
    }

    function initScene() {

        layers = createLayers();
        scene = new Scene(imgObjectList[currentBackground], layers, robot, obstacle, ruler);
        scene.updateBackgrounds();
        scene.drawObjects();
        scene.drawRuler();
        scene.drawRobot();
        addMouseEvents();
        ready = true;
        resizeAll();
        $(window).on("resize", resizeAll);
        render();
    }

    function createLayers() {
        $('<canvas id ="unitBackgroundLayer" width=' + CONST.MAX_WIDTH + ' height=' + CONST.MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("uniDiv"));
        var uniCanvas = document.getElementById("unitBackgroundLayer");
        $('<canvas id ="backgroundLayer" width=' + CONST.MAX_WIDTH + ' height=' + CONST.MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("backgroundDiv"));
        var backCanvas = document.getElementById("backgroundLayer");
        $('<canvas id ="objectLayer" width=' + CONST.MAX_WIDTH + ' height=' + CONST.MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("objectDiv"));
        var objectCanvas = document.getElementById("objectLayer");
        $('<canvas id ="rulerLayer" width=' + CONST.MAX_WIDTH + ' height=' + CONST.MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("rulerDiv"));
        var rulerCanvas = document.getElementById("rulerLayer");
        $('<canvas id ="robotLayer" width=' + CONST.MAX_WIDTH + ' height=' + CONST.MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("robotDiv"));
        var robotCanvas = document.getElementById("robotLayer");
        return [ uniCanvas.getContext("2d"), backCanvas.getContext("2d"), objectCanvas.getContext("2d"), robotCanvas.getContext("2d"), rulerCanvas.getContext("2d") ];
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
        layer = document.getElementById("robotLayer");
        while (layer.firstChild) {
            layer.removeChild(myNode.firstChild);
        }
        layer = document.getElementById("rulerLayer");
        while (layer.firstChild) {
            layer.removeChild(myNode.firstChild);
        }
    }

    function getScale() {
        return scale;
    }
    exports.getScale = getScale;

    function getInfo() {
        return info;
    }
    exports.getInfo = getInfo;

    function getAverageTimeStep() {
        return averageTimeStep;
    }
    exports.getAverageTimeStep = getAverageTimeStep;

    function isIE() {
        var ua = window.navigator.userAgent;
        var ie = ua.indexOf('MSIE ');
        var ie11 = ua.indexOf('Trident/');

        if ((ie > -1) || (ie11 > -1)) {
            return true;
        }
        return false;
    }
});

//http://paulirish.com/2011/requestanimationframe-for-smart-animating/
//http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating
//requestAnimationFrame polyfill by Erik Möller
//fixes from Paul Irish and Tino Zijdel
(function() {
    var lastTime = 0;
    var vendors = [ 'ms', 'moz', 'webkit', 'o' ];
    for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
        window.cancelAnimationFrame = (window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame']);
    }

    if (!window.requestAnimationFrame) {
        window.requestAnimationFrame = function(callback, element) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, 16 - (currTime - lastTime));
            var id = window.setTimeout(function() {
                callback(currTime + timeToCall);
            }, timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };
    }

    if (!window.cancelAnimationFrame) {
        window.cancelAnimationFrame = function(id) {
            clearTimeout(id);
        };
    }
}());
