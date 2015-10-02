/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */

/**
 * @namespace SIM
 */
var SIM = (function() {
    var scene;
    var userProgram;
    var layers;
    var canvasOffset;
    var offsetX;
    var offsetY;
    var isDownrobot = false;
    var isDownObstacle = false;
    var startX;
    var startY;
    var scale = 1;
    var imgSrc = [ "simulation/simBackgrounds/baustelle-02.svg", "simulation/simBackgrounds/simpleBackground.svg",
            "simulation/simBackgrounds/drawBackground.svg", "simulation/simBackgrounds/robertaBackground.svg",
            "simulation/simBackgrounds/rescueBackground.svg", "simulation/simBackgrounds/mathBackground.svg" ]; //TODO combine to one image for better performance
    var imgSrcIE = [ "simulation/simBackgrounds/baustelle-02.svg", "simulation/simBackgrounds/simpleBackground.png",
            "simulation/simBackgrounds/drawBackground.png", "simulation/simBackgrounds/robertaBackground.png",
            "simulation/simBackgrounds/rescueBackground.png", "simulation/simBackgrounds/mathBackground.png" ];
    var img;
    var timerStep = 0;
    var ready;
    var canceled;

    var currentBackground = 1;

    function setBackground(num) {
        window.removeEventListener("resize", resizeAll);
        setPause(true);
        if (num === 0) {
            currentBackground += 1;
            if (currentBackground > 5) {
                currentBackground = 1;
            }
        } else {
            currentBackground = num;
        }
        if (currentBackground == 1) {
            robot = new SimpleRobot();
        } else if (currentBackground == 2) {
            robot = new DrawRobot();
        } else if (currentBackground == 3) {
            robot = new RobertaRobot();
        } else if (currentBackground == 4) {
            robot = new RescueRobot();
        } else if (currentBackground == 5) {
            robot = new MathRobot();
        }
        setObstacle();
        scene = new Scene(img[currentBackground], layers, robot, obstacle);
        resizeAll();
        scene.updateBackgrounds();
        scene.drawObjects();
        reloadProgram();
        window.addEventListener("resize", resizeAll);
        return currentBackground;
    }
    function getBackground() {
        return currentBackground;
    }
    var time;

    var dt = 0;
    function getDt() {
        return dt;
    }

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
            } else {
                $('.simForward').removeClass('typcn-media-play');
                $('.simForward').addClass('typcn-media-pause');
            }
            pause = value;
        }
    }

    var stepCounter;
    function setStep() {
        stepCounter = -50;
        setPause(false);
    }
    var info;
    function setInfo() {
        if (info === true) {
            info = false;
        } else {
            info = true;
        }
    }
    function stopProgram() {
        setPause(true);
        robot.reset();
        scene.updateBackgrounds();
        reloadProgram();
    }

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
    var obstacleList = [ ground, obstacle ];

// input and output for executing user program
    var input = {
        touch : false,
        color : '',
        light : 0,
        ultrasonic : 0,
        tacho : [ 0, 0 ],
        time : 0
    };

    var output = {
        left : 0,
        right : 0,
        led : ' ',
        ledMode : ' '
    };

// render stuff
    var globalID;

    var robot = new SimpleRobot();

    function init(program) {
        ready = false;
        userProgram = program;
        eval(userProgram);
        canceled = false;
        isDownrobot = false;
        isDownObstacle = false;
        stepCounter = 0;
        pause = true;
        info = false;
        robot.reset();
        img = [];
        if (isIE()) {
            imgSrc = imgSrcIE;
        }
        loadImages(0);
    }

    function cancel() {
        $(window).off("resize");
        canceled = true;
        removeMouseEvents();
        destroyLayers();
    }

    function render() {
        if (canceled) {
            cancelAnimationFrame(globalID);
            return;
        }
        globalID = requestAnimationFrame(render);
        var now = new Date().getTime();
        dt = now - (time || now);
        dt /= 1000;

        time = now;

        stepCounter += 1;
        output.left = 0;
        output.right = 0;
        if (!PROGRAM_SIMULATION.isTerminated() && !pause) {
            //executeProgram();  //for tests without OpenRobertaLab
            if (stepCounter === 0) {
                setPause(true);
            }
            step(input);
            setOutput();
        } else if (PROGRAM_SIMULATION.isTerminated()) {
            reloadProgram();
            eval(userProgram);
            $('.simForward').removeClass('typcn-media-pause');
            $('.simForward').addClass('typcn-media-play');
            setPause(true);
        }
        robot.updatePose(output);
        input = scene.updateSensorValues(!pause);
        scene.drawRobot();
    }

    function reloadProgram() {
        eval(userProgram);
        $('.simForward').removeClass('typcn-media-pause');
        $('.simForward').addClass('typcn-media-play');
    }

    function setOutput() {
        var left = ACTORS.getLeftMotor().getPower();
        if (left > 100) {
            left = 100;
        } else if (left < -100) {
            left = -100
        }
        var right = ACTORS.getRightMotor().getPower();
        if (right > 100) {
            right = 100;
        } else if (right < -100) {
            right = -100
        }
        output.left = left * MAXPOWER || 0;
        output.right = right * MAXPOWER || 0;

        robot.led.mode = output.ledMode = LIGHT.getMode() || "OFF";
        if (LIGHT.getMode() && LIGHT.getMode() == "OFF") {
            robot.led.color = output.led = "#dddddd"; // = led off
        } else {
            robot.led.color = output.led = LIGHT.getColor();
        }
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
            obstacle.img = img[0];
            obstacle.color = null;
        } else if (currentBackground == 5) {
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
    }

    function handleMouseUp(e) {
        e.preventDefault();
        if (!isDownrobot && !isDownObstacle) {
            if (startX < ground.w / 2) {
                robot.pose.theta += SIMATH.toRadians(-5);
            } else {
                robot.pose.theta += SIMATH.toRadians(5);
            }
        }
        $("#robotLayer").css('cursor', 'auto');
        if (robot instanceof DrawRobot) {
            robot.canDraw = true;
        }
        isDownrobot = false;
        isDownObstacle = false;
    }

    function handleMouseOut(e) {
        e.preventDefault();
        isDownrobot = false;
        isDownObstacle = false;
    }

    function handleMouseMove(e) {
        e.preventDefault();
        if (!isDownrobot && !isDownObstacle) {
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
            if (robot instanceof DrawRobot) {
                robot.canDraw = false;
            }
            robot.pose.xOld = robot.pose.x;
            robot.pose.yOld = robot.pose.y;
            robot.pose.x += dx;
            robot.pose.y += dy;
            robot.mouse.rx += dx;
            robot.mouse.ry += dy;
        } else {
            obstacle.x += dx;
            obstacle.y += dy;
            scene.drawObjects();
        }
        return;
    }

    function resizeAll() {
        if (!canceled) {
            canvasOffset = $("#backgroundDiv").offset();
            offsetX = canvasOffset.left;
            offsetY = canvasOffset.top;
            scene.playground.w = $('#simDiv').width();
            scene.playground.h = $(window).height() - offsetY;
            var oldScale = scale;
            scale = 1;
            if ($(window).width() < 768) {// extra small devices
                scale = 0.5
            } else if ($(window).width() < 1024) {// medium and large devices     
                scale = 0.7;
            } else if ($(window).width() < 1280) {// medium and large devices     
                scale = 0.8;
            } else if ($(window).width() >= 1920) {// medium and large devices     
                scale = 1.5;
            }
            ground.w = scene.playground.w / scale;
            ground.h = scene.playground.h / scale;
            // MAXDIAG = Math.sqrt(SIMATH.sqr(ground.w) + SIMATH.sqr(ground.h));
//            if (oldScale != scale) {
            scene.updateBackgrounds();
            scene.drawObjects();
//            }
        }
    }

    function loadImages(i) {
        if (i < imgSrc.length - 1) {
            img[i] = new Image();
            img[i].onload = function() {
                loadImages(i + 1);
            }
            img[i].src = imgSrc[i];
        } else {
            img[i] = new Image();
            img[i].onload = initScene;
            img[i].src = imgSrc[i];
        }
    }

    function addMouseEvents() {
        $("#robotLayer").mousedown(function(e) {
            handleMouseDown(e);
        });
        $("#robotLayer").mousemove(function(e) {
            handleMouseMove(e);
        });
        $("#robotLayer").mouseup(function(e) {
            handleMouseUp(e);
        });
        $("#robotLayer").mouseout(function(e) {
            handleMouseOut(e);
        });
        $("#robotLayer").bind('touchmove', function(e) {
            handleMouseMove(e);
        });
        $("#robotLayer").bind('touchleave', function(e) {
            handleMouseOut(e);
        });
        $("#robotLayer").bind('touchstart', function(e) {
            handleMouseDown(e);
        });
        $("#robotLayer").bind('touchend', function(e) {
            handleMouseUp(e);
        });
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
        scene = new Scene(img[currentBackground], layers, robot, obstacle);
        setObstacle();
        scene.updateBackgrounds();
        scene.drawObjects();
        scene.drawRobot();
        addMouseEvents();
        ready = true;
        $(window).on("resize", resizeAll);
        resizeAll();
        render();
    }

    function createLayers() {
        $('<canvas id ="unitBackgroundLayer" width=' + MAX_WIDTH + ' height=' + MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("uniDiv"));
        var uniCanvas = document.getElementById("unitBackgroundLayer");
        $('<canvas id ="backgroundLayer" width=' + MAX_WIDTH + ' height=' + MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("backgroundDiv"));
        var backCanvas = document.getElementById("backgroundLayer");
        $('<canvas id ="objectLayer" width=' + MAX_WIDTH + ' height=' + MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("objectDiv"));
        var objectCanvas = document.getElementById("objectLayer");
        $('<canvas id ="robotLayer" width=' + MAX_WIDTH + ' height=' + MAX_HEIGHT + '></canvas>').appendTo(document.getElementById("robotDiv"));
        var robotCanvas = document.getElementById("robotLayer");
        return [ uniCanvas.getContext("2d"), backCanvas.getContext("2d"), objectCanvas.getContext("2d"), robotCanvas.getContext("2d") ];
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
    }
    function getScale() {
        return scale;
    }
    function getInfo() {
        return info;
    }
    function getAverageTimeStep() {
        return averageTimeStep;
    }
    
    function isIE() {
        var ua = window.navigator.userAgent;
        var ie = ua.indexOf('MSIE ');
        var ie11 = ua.indexOf('Trident/');

        if ((ie > -1) || (ie11 > -1)) {
            return true;
        }
        return false;
    }
    
    return {
        "init" : init,
        "setPause" : setPause,
        "setStep" : setStep,
        "setInfo" : setInfo,
        "setBackground" : setBackground,
        "getBackground" : getBackground,
        "stopProgram" : stopProgram,
        "obstacleList" : obstacleList,
        "output" : output,
        "input" : input,
        "getScale" : getScale,
        "getInfo" : getInfo,
        "cancel" : cancel,
        "getAverageTimeStep" : getAverageTimeStep,
        "getDt" : getDt
    };
})();

//http://paulirish.com/2011/requestanimationframe-for-smart-animating/
//http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating

//requestAnimationFrame polyfill by Erik Möller
//fixes from Paul Irish and Tino Zijdel

(function() {
    var lastTime = 0;
    var vendors = [ 'ms', 'moz', 'webkit', 'o' ];
    for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
        window.cancelAnimationFrame = window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame'];
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
