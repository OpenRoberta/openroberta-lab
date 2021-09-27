/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */

/**
 * @namespace SIM
 */

define(['exports', 'simulation.scene', 'simulation.constants', 'util', 'interpreter.interpreter', 'interpreter.robotSimBehaviour', 'volume-meter', 'message', 'jquery', 'huebee', 'blockly'], function(exports, Scene, C, UTIL, SIM_I, MBED_R, Volume, MSG, $, HUEBEE, Blockly) {

    const standColorObstacle = "#33B8CA";
    const standColorArea = "#FBED00";
    var interpreters;
    var scene;
    var userPrograms;
    var configurations = [];
    var canvasOffset;
    var offsetX;
    var offsetY;
    var startX;
    var startY;
    var scale = 1;
    var selectedRobot = -1;
    var selectedObstacle = -1;
    var selectedColorArea = -1;
    var downCorner = -1;
    var downRuler = false;
    var downRobot = -1;
    var downColorArea = -1;
    var downObstacle = -1;
    var highLightCorners = [];
    var canceled;
    var storedPrograms;
    var copiedObject;
    var customBackgroundLoaded = false;
    var debugMode = false;
    var breakpoints = [];
    var obstacleList = [];
    var colorAreaList = [];
    var observers = {};
    var imgObstacle1 = new Image();
    var imgPattern = new Image();
    var imgRuler = new Image();
    var mouseX;
    var mouseY;

    var colorpicker = new HUEBEE('#colorpicker', {
        shades: 1,
        hues: 8,
        setText: false
    });
    var imgList = ['/js/app/simulation/simBackgrounds/baustelle.svg', '/js/app/simulation/simBackgrounds/ruler.svg',
        '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.svg',
        '/js/app/simulation/simBackgrounds/microbitBackground.svg', '/js/app/simulation/simBackgrounds/simpleBackground.svg',
        '/js/app/simulation/simBackgrounds/drawBackground.svg', '/js/app/simulation/simBackgrounds/robertaBackground.svg',
        '/js/app/simulation/simBackgrounds/rescueBackground.svg', '/js/app/simulation/simBackgrounds/blank.svg',
        '/js/app/simulation/simBackgrounds/mathBackground.svg'
    ];
    var imgListIE = ['/js/app/simulation/simBackgrounds/baustelle.png', '/js/app/simulation/simBackgrounds/ruler.png',
        '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.png',
        '/js/app/simulation/simBackgrounds/microbitBackground.png', '/js/app/simulation/simBackgrounds/simpleBackground.png',
        '/js/app/simulation/simBackgrounds/drawBackground.png', '/js/app/simulation/simBackgrounds/robertaBackground.png',
        '/js/app/simulation/simBackgrounds/rescueBackground.png', '/js/app/simulation/simBackgrounds/blank.png',
        '/js/app/simulation/simBackgrounds/mathBackground.png'
    ];
    var imgObjectList = [];

    function preloadImages() {
        if (isIE()) {
            imgList = imgListIE;
        }
        var i = 0;
        for (i = 0; i < imgList.length; i++) {
            if (i === 0) {
                imgObstacle1.src = imgList[i];
            } else if (i == 1) {
                imgRuler.src = imgList[i];
            } else if (i == 2) {
                imgPattern.src = imgList[i];
            } else {
                imgObjectList[i - 3] = new Image();
                imgObjectList[i - 3].src = imgList[i];
            }
        }
        if (UTIL.isLocalStorageAvailable()) {
            var customBackground = localStorage.getItem("customBackground");

            if (customBackground) {
                // TODO backwards compatibility for non timestamped background images; can be removed after some time
                try {
                    JSON.parse(customBackground);
                } catch (e) {
                    localStorage.setItem("customBackground", JSON.stringify({
                        image: customBackground,
                        timestamp: new Date().getTime()
                    }));
                    customBackground = localStorage.getItem("customBackground");
                }

                customBackground = JSON.parse(customBackground);
                // remove images older than 30 days
                var currentTimestamp = new Date().getTime();
                if (currentTimestamp - customBackground.timestamp > 63 * 24 * 60 * 60 * 1000) {
                    localStorage.removeItem('customBackground');
                } else {
                    // add image to backgrounds if recent
                    var dataImage = customBackground.image;
                    imgObjectList[i - 3] = new Image();
                    imgObjectList[i - 3].src = "data:image/png;base64," + dataImage;
                    customBackgroundLoaded = true;
                }
            }
        }
    }

    preloadImages();

    var currentBackground = 2;

    function setBackground(num) {
        num = num || -1;
        let configData = {};
        if (num === -1) {
            configData = exportConfigData();
            currentBackground += 1;
            if (currentBackground >= imgObjectList.length) {
                currentBackground = 2;
            }
            if (currentBackground == (imgObjectList.length - 1) && customBackgroundLoaded && UTIL.isLocalStorageAvailable()) {
                // update timestamp of custom background
                localStorage.setItem("customBackground", JSON.stringify({
                    image: JSON.parse(localStorage.getItem("customBackground")).image,
                    timestamp: new Date().getTime()
                }));
            }
        } else {
            currentBackground = num;
        }
        var debug = robots[0].debug;
        var moduleName = 'simulation.robot.' + simRobotType;

        removeMouseEvents();
        resetSelection();
        require([moduleName], function(ROBOT) {
            createRobots(ROBOT, numRobots);
            for (var i = 0; i < robots.length; i++) {
                robots[i].debug = debug;
                robots[i].reset();
            }
            scene.robots = robots;
        });
        let config = coordinates2relatives();
        scene.resetAllCanvas(imgObjectList[currentBackground]);
        relatives2coordinates(config);
        setObstacle();
        setRuler();
        scene.drawObstacles(highLightCorners);
        scene.drawColorAreas(highLightCorners);
        scene.drawRuler();
        addMouseEvents();
        resizeAll();
    }
    exports.setBackground = setBackground;

    function getBackground() {
        return currentBackground;
    }
    exports.getBackground = getBackground;

    function getObstacleList() {
        return obstacleList;
    }
    exports.getObstacleList = getObstacleList;

    function getColorAreaList() {
        return colorAreaList;
    }
    exports.getColorAreaList = getColorAreaList;

    function initMicrophone(robot) {
        if (navigator.mediaDevices === undefined) {
            navigator.mediaDevices = {};
        }
        navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;

        try {
            // ask for an audio input
            navigator.mediaDevices.getUserMedia({
                "audio": {
                    "mandatory": {
                        "googEchoCancellation": "false",
                        "googAutoGainControl": "false",
                        "googNoiseSuppression": "false",
                        "googHighpassFilter": "false"
                    },
                    "optional": []
                },
            }).then(function(stream) {
                var mediaStreamSource = robot.webAudio.context.createMediaStreamSource(stream);
                robot.sound = Volume.createAudioMeter(robot.webAudio.context);
                mediaStreamSource.connect(robot.sound);
            }, function() {
                console.log("Sorry, but there is no microphone available on your system");
            });
        } catch (e) {
            console.log("Sorry, but there is no microphone available on your system");
        }
    }

    exports.initMicrophone = initMicrophone;

    var time;
    var renderTime = 5; // approx. time in ms only for the first rendering

    var dt = 0;

    function getDt() {
        return dt;
    }

    exports.getDt = getDt;

    var pause = false;

    function setPause(value) {
        if (!value && readyRobots.indexOf(false) > -1) {
            setTimeout(function() {
                setPause(false);
            }, 100);
        } else {
            if (value && !debugMode) {
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
            } else if (!value && !debugMode) {
                $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
            }
            pause = value;
        }
    }
    exports.setPause = setPause;

    var runRenderUntil;

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

    function resetPose() {
        for (var i = 0; i < robots.length; i++) {
            if (robots[i].resetPose) {
                robots[i].resetPose();
            }
            if (robots[i].time) {
                robots[i].time = 0;
            }
        }
        resetSelection();
        scene.resetAllCanvas();
        scene.drawColorAreas(highLightCorners);
        scene.drawObstacles(highLightCorners);
        scene.drawRuler();
    }
    exports.resetPose = resetPose;

    function deleteSelectedObject() {
        if (selectedColorArea >= 0) {
            colorAreaList.splice(selectedColorArea, 1);
            selectedColorArea = -1;
            highLightCorners = [];
            updateColorAreaLayer();
        }
        if (selectedObstacle >= 0) {
            obstacleList.splice(selectedObstacle, 1)
            selectedObstacle = -1;
            highLightCorners = [];
            updateObstacleLayer();
        }
    }
    exports.deleteSelectedObject = deleteSelectedObject;

    function addObstacle(shape) {
        let obstacle = addObject(shape, "obstacle", obstacleList);
        if (selectedColorArea >= 0) {
            highLightCorners = [];
            updateColorAreaLayer();
        }
        resetSelection();
        selectedObstacle = obstacleList.length - 1;

        highLightCorners = calculateCorners(obstacle);
        updateObstacleLayer();
    }
    exports.addObstacle = addObstacle;

    function addColorArea(shape) {
        let colorArea = addObject(shape, "colorArea", colorAreaList);
        if (selectedObstacle >= 0) {
            highLightCorners = [];
            updateObstacleLayer();
        }
        resetSelection();
        selectedColorArea = colorAreaList.length - 1;

        highLightCorners = calculateCorners(colorArea);
        updateColorAreaLayer();
    }
    exports.addColorArea = addColorArea;

    function addObject(shape, type, objectList) {
        $("#robotLayer").attr("tabindex", 0);
        $("#robotLayer").focus();
        let newObject = {};
        let x = Math.random() * ((ground.w - 200) - 100) + 100;
        let y = Math.random() * ((ground.h - 100) - 100) + 100;
        switch (shape) {
            case "rectangle":
                newObject = {
                    form: "rectangle",
                    x: x,
                    y: y,
                    w: 100,
                    h: 100,
                    theta: 0,
                    img: null
                };
                break;
            case "triangle":
                newObject = {
                    form: "triangle",
                    ax: x - 50,
                    ay: y + 50,
                    bx: x,
                    by: y - 50,
                    cx: x + 50,
                    cy: y + 50
                };
                break;
            case "circle":
                newObject = {
                    form: "circle",
                    x: x,
                    y: y,
                    r: 50,
                    startAngle: 50,
                    endAngle: 0
                };
                break;
            default:
                console.error("SIMULATION: no or wrong shape");
        }
        if (type === "obstacle") {
            newObject.color = standColorObstacle;
        } else {
            newObject.color = standColorArea;
        }
        newObject.type = type;
        objectList.push(newObject);
        enableChangeObjectButtons();
        return newObject;
    }

    function changeColorWithColorPicker(color) {
        let selectedObject = selectedObstacle >= 0 ? obstacleList[selectedObstacle] : selectedColorArea >= 0 ? colorAreaList[selectedColorArea] : null;
        if (selectedObject != null) {
            selectedObject.color = color;
            updateSelectedObjects();
        }
    }

    function toggleColorPicker() {
        if ($(".huebee").length) {
            colorpicker.close();
        } else {
            colorpicker.open();
        }
    }
    exports.toggleColorPicker = toggleColorPicker;

    function resetColorpickerCursor(event) {
        colorpicker.color = null;
        colorpicker.setTexts();
        colorpicker.setBackgrounds();
        colorpicker.cursor.classList.add('is-hidden');
    }

    function stopProgram() {
        setPause(true);
        for (var i = 0; i < numRobots; i++) {
            robots[i].reset();
        }
        resetButtons();

        if (debugMode) {
            for (var i = 0; i < numRobots; i++) {
                interpreters[i].removeHighlights();
            }
        }
        setTimeout(function() {
            init(userPrograms, false, simRobotType);
            addMouseEvents();
        }, 205);


    }

    exports.stopProgram = stopProgram;

    // obstacles
    // scaled playground
    ground = {
        x: 0,
        y: 0,
        w: 500,
        h: 500,
        isParallelToAxis: true,
        type: "ground",
        form: "rectangle"
    };

    var defaultObstacle = {
        x: 0,
        y: 0,
        xOld: 0,
        yOld: 0,
        w: 0,
        h: 0,
        wOld: 0,
        hOld: 0,
        isParallelToAxis: true,
        theta: 0,
        form: "rectangle",
        type: "obstacle"
    };

    obstacleList = [defaultObstacle];

    var ruler = {
        x: 0,
        y: 0,
        xOld: 0,
        yOld: 0,
        w: 0,
        h: 0,
        wOld: 0,
        hOld: 0,
        type: "ruler"
    };
    // Note: The ruler is not considered an obstacle. The robot will
    // simply drive over it.

    var globalID;
    var robots = [];
    var robotIndex = 0;
    var simRobotType;
    var numRobots = 0;
    exports.getNumRobots = function() {
        return numRobots;
    };

    function callbackOnTermination() {
        if (allInterpretersTerminated()) {
            if (!robots[0].endless) {
                if (debugMode) {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-play');
                    $('#simStop').removeClass("disabled");
                } else {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                }
            } else if (debugMode) {
                if (!$('#simStop').hasClass("disabled")) {
                    $('#simStop').hide();
                    $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play').removeClass('blue');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                }
            }

        }
        console.log("END of Sim");
    }

    function init(programs, refresh, robotType) {
        mouseOnRobotIndex = -1;
        storedPrograms = programs;
        numRobots = programs.length;
        reset = false;
        simRobotType = robotType;
        userPrograms = programs;
        runRenderUntil = [];
        configurations = [];
        for (i = 0; i < programs.length; i++) {
            runRenderUntil[i] = 0;
        }
        if (robotType.indexOf("calliope") >= 0) {
            currentBackground = 0;
            resetScene([], []);
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead, #simEditButtons').hide();
        } else if (robotType === 'microbit') {
            resetScene([], []);
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead, #simEditButtons').hide();
            currentBackground = 1;
        } else if (currentBackground === 0 || currentBackground == 1) {
            currentBackground = 2;
        }
        if (currentBackground > 1) {
            if (isIE() || isEdge()) { // TODO IE and Edge: Input event not firing for file type of input
                $('.dropdown.sim, .simScene').show();
                $('#simImport').hide();
            } else {
                $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simEditButtons').show();
            }
        }
        $('#simButtons, #canvasDiv').show();
        $('#webotsButtons, #webotsDiv').hide();
        interpreters = programs.map(function(x) {
            var src = JSON.parse(x.javaScriptProgram);
            configurations.push(x.configuration.SENSORS);
            return new SIM_I.Interpreter(src, new MBED_R.RobotMbedBehaviour(), callbackOnTermination, breakpoints);
        });
        updateDebugMode(debugMode);

        isDownRobots = [];
        for (var i = 0; i < numRobots; i++) {
            isDownRobots.push(false);
        }
        if (refresh) {
            robotIndex = 0;
            robots = [];
            readyRobots = [];
            isDownRobots = [];
            require(['simulation.robot.' + simRobotType], function(reqRobot) {
                createRobots(reqRobot, numRobots);
                for (var i = 0; i < numRobots; i++) {
                    robots[i].reset();
                    robots[i].resetPose();
                    readyRobots.push(false);
                    isDownRobots.push(false);
                }
                removeMouseEvents();
                canceled = false;
                isDownObstacle = false;
                colorpicker.close();
                isDownRuler = false;
                isDownColorArea = false;
                stepCounter = 0;
                pause = true;
                info = false;
                setObstacle();
                setRuler();
                initScene();

            });

        } else {
            for (var i = 0; i < numRobots; i++) {
                robots[i].replaceState(interpreters[i].getRobotBehaviour());
                if (robots[i].endless) {
                    robots[i].reset();
                }
            }
            resetButtons();
        }

    }

    exports.init = init;

    function run(refresh, robotType) {
        init(storedPrograms, refresh, robotType);
    }

    exports.run = run;

    function getRobotIndex() {
        return robotIndex;
    }
    exports.getRobotIndex = getRobotIndex;

    function getSelectedRobot() {
        return selectedRobot;
    }
    exports.getSelectedRobot = getSelectedRobot;

    function cancel() {
        canceled = true;
        removeMouseEvents();
    }

    exports.cancel = cancel;

    var reset = false;

    /*
        * The below Colors are picked from the toolkit and should be used to color
        * the robots
        */
    var colorsAdmissible = [
        [242, 148, 0],
        [143, 164, 2],
        [235, 106, 10],
        [51, 184, 202],
        [0, 90, 148],
        [186, 204, 30],
        [235, 195, 0],
        [144, 133, 186]
    ];

    function render() {
        if (canceled) {
            cancelAnimationFrame(globalID);
            return;
        }
        var actionValues = [];
        for (var i = 0; i < numRobots; i++) {
            actionValues.push({});
        }
        globalID = requestAnimationFrame(render);
        var now = new Date().getTime();
        var dtSim = now - (time || now);
        var dtRobot = Math.min(15, (dtSim - renderTime) / numRobots);
        var dtRobot = Math.abs(dtRobot);
        dt = dtSim / 1000;
        time = now;
        stepCounter += 1;

        for (var i = 0; i < numRobots; i++) {
            if ((robots[i] && !robots[i].pause) && !pause) {
                if (!interpreters[i].isTerminated() && !reset) {
                    if (runRenderUntil[i] <= now) {
                        var delayMs = interpreters[i].run(now + dtRobot);
                        var nowNext = new Date().getTime();
                        runRenderUntil[i] = nowNext + delayMs;
                    }
                } else if (reset || allInterpretersTerminated() && !robots[i].endless) {
                    reset = false;
                    for (var j = 0; j < robots.length; j++) {
                        robots[j].buttons.Reset = false;
                        robots[j].pause = true;
                        robots[j].reset();
                    }
                    removeMouseEvents();
                    scene.drawRobots();

                    // some time to cancel all timeouts
                    setTimeout(function() {
                        init(userPrograms, false, simRobotType);
                        addMouseEvents();
                    }, 205);

                    if (!(allInterpretersTerminated() && !robots[i].endless)) {
                        setTimeout(function() {
                            setPause(false);
                            for (var j = 0; j < robots.length; j++) {
                                robots[j].pause = false;
                            }
                        }, 1000);
                    }
                }
            }
            robots[i] && robots[i].update();
            updateBreakpointEvent();
        }
        var renderTimeStart = new Date().getTime();

        function allPause() {
            for (var i = 0; i < robots.length; i++) {
                if (!robots[i].pause) {
                    return false;
                }
            }
            return true;
        }

        if (allPause()) {
            setPause(true);
            for (var i = 0; i < robots.length; i++) {
                robots[i].pause = false;
            }
        }

        if (robots[0] !== undefined && robots[0].buttons !== undefined) {
            reset = robots[0].buttons.Reset;
        }
        scene.updateSensorValues(!pause);
        scene.drawRobots();
        renderTime = new Date().getTime() - renderTimeStart;
    }

    function allInterpretersTerminated() {
        for (var i = 0; i < interpreters.length; i++) {
            if (!interpreters[i].isTerminated()) {
                return false;
            }
        }
        return true;
    }

    function resetButtons() {
        if (debugMode) {
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
            $('#simStop').addClass("disabled");
        } else {
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
        }
    }

    //set standard obstacle
    function setObstacle() {
        if (obstacleList.length == 0) {
            obstacleList.push(defaultObstacle);
        }
        if (obstacleList.length == 1) {
            var standObst = {};
            switch (currentBackground) {
                case 0:
                case 1:
                case 7:
                    standObst.x = 0;
                    standObst.y = 0;
                    standObst.w = 0;
                    standObst.h = 0;
                    standObst.color = null;
                    standObst.img = null;
                    standObst.form = "rectangle";
                    break;
                case 2:
                    standObst.x = 580;
                    standObst.y = 290;
                    standObst.w = 100;
                    standObst.h = 100;
                    standObst.color = standColorObstacle;
                    standObst.form = "rectangle";
                    break;
                case 3:
                    standObst.x = 500;
                    standObst.y = 250;
                    standObst.w = 100;
                    standObst.h = 100;
                    standObst.img = null;
                    standObst.color = standColorObstacle;
                    standObst.form = "rectangle";
                    break;
                case 4:
                    standObst.x = 500;
                    standObst.y = 260;
                    standObst.w = 100;
                    standObst.h = 100;
                    standObst.img = imgObstacle1;
                    standObst.color = null;
                    standObst.form = "rectangle";
                    break;
                case 5:
                    standObst.x = 505;
                    standObst.y = 405;
                    standObst.w = 20;
                    standObst.h = 20;
                    standObst.color = standColorObstacle;
                    standObst.img = null;
                    standObst.form = "rectangle";
                    break;
                case 6:
                    standObst.x = 425;
                    standObst.y = 254;
                    standObst.w = 50;
                    standObst.h = 50;
                    standObst.color = "#009EE3";
                    standObst.img = null;
                    standObst.form = "rectangle";
                    break;
                default:
                    var x = imgObjectList[currentBackground].width - 50;
                    var y = imgObjectList[currentBackground].height - 50;
                    standObst.x = x;
                    standObst.y = y;
                    standObst.w = 50;
                    standObst.h = 50;
                    standObst.color = standColorObstacle;
                    standObst.img = null;
                    standObst.form = "rectangle";
            }
            standObst.type = "obstacle";
            obstacleList[0] = standObst;
        }
    }

    function setRuler() {
        if (currentBackground == 4) {
            ruler.x = 430;
            ruler.y = 400;
            ruler.w = 300;
            ruler.h = 30;
            ruler.img = imgRuler;
            ruler.color = '#ff0000';
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

    function handleKeyEvent(e) {
        var keyName = e.key;
        var keyCode = e.keyCode;
        let selectedObject = selectedObstacle >= 0 ? obstacleList[selectedObstacle] : selectedColorArea >= 0 ? colorAreaList[selectedColorArea] : null;
        if (selectedRobot >= 0) {
            if (robots[selectedRobot].drawWidth) {
                robots[selectedRobot].canDraw = false;
            }
            switch (keyName) {
                case "ArrowUp":
                    robots[selectedRobot].pose.x += Math.cos(robots[selectedRobot].pose.theta);
                    robots[selectedRobot].pose.y += Math.sin(robots[selectedRobot].pose.theta);
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowLeft":
                    robots[robotIndex].pose.theta -= Math.PI / 180;
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowDown":
                    robots[selectedRobot].pose.x -= Math.cos(robots[selectedRobot].pose.theta);
                    robots[selectedRobot].pose.y -= Math.sin(robots[selectedRobot].pose.theta);
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowRight":
                    robots[selectedRobot].pose.theta += Math.PI / 180;
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                default:
            }
        } else if (selectedObject) {
            const shift = 5;
            switch (keyName) {
                case "ArrowUp":
                    switch (selectedObject.form) {
                        case "rectangle":
                        case "circle":
                            selectedObject.y -= shift;
                            break;
                        case "triangle":
                            selectedObject.ay -= shift;
                            selectedObject.by -= shift;
                            selectedObject.cy -= shift;
                            break;
                        default:
                    }
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowLeft":
                    switch (selectedObject.form) {
                        case "rectangle":
                        case "circle":
                            selectedObject.x -= shift;
                            break;
                        case "triangle":
                            selectedObject.ax -= shift;
                            selectedObject.bx -= shift;
                            selectedObject.cx -= shift;
                            break;
                        default:
                    }
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowDown":
                    switch (selectedObject.form) {
                        case "rectangle":
                        case "circle":
                            selectedObject.y += shift;
                            break;
                        case "triangle":
                            selectedObject.ay += shift;
                            selectedObject.by += shift;
                            selectedObject.cy += shift;
                            break;
                        default:
                    }
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case "ArrowRight":
                    switch (selectedObject.form) {
                        case "rectangle":
                        case "circle":
                            selectedObject.x += shift;
                            break;
                        case "triangle":
                            selectedObject.ax += shift;
                            selectedObject.bx += shift;
                            selectedObject.cx += shift;
                            break;
                        default:
                    }
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                default:
                // nothing to do so far
            }
            highLightCorners = calculateCorners(selectedObject);
            selectedObject.type === "obstacle" ? updateObstacleLayer() : updateColorAreaLayer();
        }
        switch (keyCode) {
            case 17 && 67:
                copiedObject = {};
                if (selectedObject) {
                    copiedObject = $.extend(copiedObject, selectedObject);
                }
                e.preventDefault();
                e.stopPropagation();
                break;
            case 17 && 86:
                var toCopyObject = {};
                toCopyObject = $.extend(toCopyObject, selectedObject);
                if (!$.isEmptyObject(toCopyObject) && selectedObject) {
                    mouseX = mouseX || ground.w / 2;
                    mouseY = mouseY || ground.h / 2;
                    if (toCopyObject.form === "triangle") {
                        const diffx = toCopyObject.ax - mouseX;
                        const diffy = toCopyObject.ay - mouseY;
                        toCopyObject.ax = mouseX;
                        toCopyObject.ay = mouseY;
                        toCopyObject.bx -= diffx;
                        toCopyObject.by -= diffy;
                        toCopyObject.cx -= diffx;
                        toCopyObject.cy -= diffy;
                    } else if (toCopyObject.form === "rectangle") {
                        toCopyObject.x = mouseX - toCopyObject.w / 2;
                        toCopyObject.y = mouseY - toCopyObject.h / 2;
                    } else if (toCopyObject.form === "circle") {
                        toCopyObject.x = mouseX;
                        toCopyObject.y = mouseY;
                    }
                    if (toCopyObject.type === "obstacle") {
                        obstacleList.push(toCopyObject);
                        updateObstacleLayer();
                    } else if (toCopyObject.type === "colorArea") {
                        colorAreaList.push(toCopyObject);
                        updateColorAreaLayer();

                    }
                }
                e.preventDefault();
                e.stopPropagation();
                break;
            case 8:
                deleteSelectedObject();
                e.preventDefault();
                e.stopPropagation();
                break;
            case 46:
                deleteSelectedObject();
                e.preventDefault();
                e.stopPropagation();
                break;
            default:
            // nothing to do so far
        }
        $("#robotLayer").attr("tabindex", 0);
        $("#robotLayer").focus();
    }

    function disableChangeObjectButtons() {
        $(".simChangeObject").removeClass("disabled").addClass("disabled");
    }

    function enableChangeObjectButtons() {
        $(".simChangeObject").removeClass("disabled");
    }

    function handleMouseDown(e) {
        e.preventDefault();
        e.stopPropagation();
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top;
        var left = $('#robotLayer').offset().left;
        startX = (parseInt(X - left, 10)) / scale;
        startY = (parseInt(Y - top, 10)) / scale;
        var dx;
        var dy;
        for (var i = 0; i < numRobots; i++) {
            dx = startX - robots[i].mouse.rx;
            dy = startY - robots[i].mouse.ry;
            let boolDown = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
            if (boolDown) {
                downRobot = i;
                if (selectedRobot !== i) {
                    $("#brick" + robotIndex).hide();
                    robotIndex = i;
                    $("#brick" + robotIndex).show();
                    if ($("#robotIndex")[0]) {
                        $("#robotIndex")[0][i].selected = true;
                    }
                    highLightCorners = [];
                    if (selectedObstacle >= 0) {
                        selectedObstacle = -1;
                        updateObstacleLayer();
                    }
                    if (selectedColorArea >= 0) {
                        selectedColorArea = -1;
                        updateColorAreaLayer();
                    }
                    downCorner = -1;
                    highLightCorners = [];
                    downRuler = false;
                }
                selectedRobot = i;
                if (robots[i].drawWidth) {
                    robots[i].canDraw = false;
                }
                return;
            }
        }

        if (highLightCorners.length > 0 && (selectedObstacle >= 0 || selectedColorArea >= 0)) {
            for (var i = 0; i < highLightCorners.length; i++) {
                let corner = highLightCorners[i];
                let isDownObstacleCorner = checkDownCircle(startX, startY, corner.x, corner.y, C.CORNER_RADIUS * 3);
                if (isDownObstacleCorner) {
                    downCorner = i;
                    return;
                }
            }
        }

        for (var i = obstacleList.length - 1; i >= 0; i--) {
            let obstacle = obstacleList[i];
            let obstacleIsDown = false;
            if (obstacle.form === "rectangle") {
                obstacleIsDown = (startX > obstacle.x && startX < obstacle.x + obstacle.w && startY > obstacle.y && startY < obstacle.y + obstacle.h);
            } else if (obstacle.form === "triangle") {
                obstacleIsDown = checkDownTriangle(startX, startY, obstacle.ax, obstacle.ay, obstacle.bx, obstacle.by, obstacle.cx, obstacle.cy);
            } else if (obstacle.form === "circle") {
                obstacleIsDown = checkDownCircle(startX, startY, obstacle.x, obstacle.y, obstacle.r);
            }
            if (obstacleIsDown) {
                downObstacle = i;
                if (selectedObstacle !== i) {
                    enableChangeObjectButtons();
                    selectedRobot = -1;
                    if (selectedColorArea >= 0) {
                        selectedColorArea = -1;
                        highLightCorners = [];
                        updateColorAreaLayer();
                    }
                    highLightCorners = calculateCorners(obstacleList[i]);
                    selectedObstacle = i;
                    downCorner = -1;
                    downRuler = false;
                    updateObstacleLayer();
                }
                return;
            }
        }


        for (var i = colorAreaList.length - 1; i >= 0; i--) {
            let colorArea = colorAreaList[i];
            let isDownColorArea = false;
            if (colorArea.form === "rectangle") {
                isDownColorArea = (startX > colorArea.x && startX < colorArea.x + colorArea.w && startY > colorArea.y && startY < colorArea.y + colorArea.h);
            } else if (colorArea.form === "triangle") {
                isDownColorArea = checkDownTriangle(startX, startY, colorArea.ax, colorArea.ay, colorArea.bx, colorArea.by, colorArea.cx, colorArea.cy);
            } else if (colorArea.form === "circle") {
                isDownColorArea = checkDownCircle(startX, startY, colorArea.x, colorArea.y, colorArea.r);
            }
            if (isDownColorArea) {
                downColorArea = i;
                if (selectedColorArea !== i) {
                    enableChangeObjectButtons();
                    selectedRobot = -1;
                    if (selectedObstacle >= 0) {
                        selectedObstacle = -1;
                        highLightCorners = [];
                        updateObstacleLayer();
                    }
                    highLightCorners = calculateCorners(colorAreaList[i]);
                    selectedColorArea = i;
                    downCorner = -1;
                    downRuler = false;
                    updateColorAreaLayer();
                }
                return;
            }
        }

        let rulerIsDown = (startX > ruler.x && startX < ruler.x + ruler.w && startY > ruler.y && startY < ruler.y + ruler.h);

        if (!downRuler && rulerIsDown) {
            downRuler = true;
            selectedRobot = -1;
            highLightCorners = [];
            if (selectedObstacle >= 0) {
                selectedObstacle = -1;
                updateObstacleLayer();
            }
            if (selectedColorArea >= 0) {
                selectedColorArea = -1;
                updateColorAreaLayer();
            }
            downCorner = -1;
            return;
        }
        if (selectedObstacle >= 0) {
            highLightCorners = [];
            updateObstacleLayer();
            disableChangeObjectButtons();
        }
        if (selectedColorArea >= 0) {
            highLightCorners = [];
            updateColorAreaLayer();
            disableChangeObjectButtons();
        }
        resetSelection();
    }

    function checkDownTriangle(px, py, x1, y1, x2, y2, x3, y3) {
        var areaOrig = Math.floor(Math.abs((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)));
        var area1 = Math.floor(Math.abs((x1 - px) * (y2 - py) - (x2 - px) * (y1 - py)));
        var area2 = Math.floor(Math.abs((x2 - px) * (y3 - py) - (x3 - px) * (y2 - py)));
        var area3 = Math.floor(Math.abs((x3 - px) * (y1 - py) - (x1 - px) * (y3 - py)));

        if (area1 + area2 + area3 <= areaOrig) {
            return true;
        }
        return false;
    }

    function checkDownCircle(px, py, cx, cy, r) {
        return ((px - cx) * (px - cx) + (py - cy) * (py - cy) <= r * r);
    }

    function calculateCorners(object) {
        let objectCorners;
        if (object.form === "rectangle") {
            objectCorners = [
                { x: (Math.round(object.x)), y: (Math.round(object.y) + object.h) },
                { x: Math.round(object.x), y: Math.round(object.y) },
                { x: (Math.round(object.x) + object.w), y: Math.round(object.y) },
                { x: (Math.round(object.x) + object.w), y: (Math.round(object.y) + object.h) }
            ];
        } else if (object.form === "triangle") {
            objectCorners = [
                { x: Math.round(object.ax), y: Math.round(object.ay) },
                { x: Math.round(object.bx), y: Math.round(object.by) },
                { x: Math.round(object.cx), y: Math.round(object.cy) },
            ];
        } else if (object.form === "circle") {
            var cx = Math.round(object.x);
            var cy = Math.round(object.y);
            var r = Math.round(object.r);
            objectCorners = [
                { x: cx - r, y: cy - r },
                { x: cx + r, y: cy - r },
                { x: cx - r, y: cy + r },
                { x: cx + r, y: cy + r }
            ];
        }
        return objectCorners;
    }

    function resetSelection() {
        selectedRobot = -1;
        selectedObstacle = -1;
        selectedColorArea = -1;
        highLightCorners = [];
        downCorner = -1;
        downRuler = false;
        downRobot = -1;
        downColorArea = -1;
        downObstacle = -1;
    }

    function handleMouseUp(e) {
        $("#robotLayer").css('cursor', 'auto');
        if (selectedRobot >= 0 && robots[selectedRobot].drawWidth) {
            robots[selectedRobot].canDraw = true;
        }
        handleMouseOut(e);
    }

    function handleMouseOut(e) {
        e.preventDefault();
        e.stopPropagation();
        downCorner = -1;
        downRuler = false;
        downRobot = -1;
        downColorArea = -1;
        downObstacle = -1;
    }

    function updateColorAreaLayer() {
        scene.drawColorAreas(highLightCorners);
    }

    function updateObstacleLayer() {
        scene.drawRuler();
        scene.drawObstacles(highLightCorners);
    }

    function handleMouseMove(e) {
        e.preventDefault();
        $("#robotLayer").css('cursor', 'default');
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top;
        var left = $('#robotLayer').offset().left;
        mouseX = (parseInt(X - left, 10)) / scale;
        mouseY = (parseInt(Y - top, 10)) / scale;
        var dx;
        var dy;

        // move robots
        for (var i = 0; i < numRobots; i++) {
            dx = mouseX - robots[i].mouse.rx;
            dy = mouseY - robots[i].mouse.ry;
            let hoverRobot = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
            if (hoverRobot) {
                $("#robotLayer").css('cursor', 'pointer');
            }
            if (downRobot === i && selectedRobot === i) {
                robots[i].pose.xOld = robots[i].pose.x;
                robots[i].pose.yOld = robots[i].pose.y;
                robots[i].pose.x += dx;
                robots[i].pose.y += dy;
                robots[i].mouse.rx += dx;
                robots[i].mouse.ry += dy;
                return;
            }
        }

        dx = (mouseX - startX);
        dy = (mouseY - startY);
        startX = mouseX;
        startY = mouseY;

        function resizeObject(object, corner) {
            switch (object.form) {
                case "triangle":
                    switch (corner) {
                        case 0:
                            object.ax += dx;
                            object.ay += dy;
                            break;
                        case 1:
                            object.bx += dx;
                            object.by += dy;
                            break;
                        case 2:
                            object.cx += dx;
                            object.cy += dy;

                            break;
                        default:
                            break;
                    }
                    break;
                case "circle":
                    if (Math.abs(dx) >= Math.abs(dy)) {
                        if (mouseX < object.x) {
                            object.r -= dx;
                        } else {
                            object.r += dx;
                        }
                    } else {
                        if (mouseY < object.y) {
                            object.r -= dy;
                        } else {
                            object.r += dy;
                        }
                    }
                    object.r = Math.max(object.r, C.MIN_SIZE_OBJECT);
                    break;
                case "rectangle":
                    if (object.w >= C.MIN_SIZE_OBJECT && object.h >= C.MIN_SIZE_OBJECT) {
                        switch (corner) {
                            case 0:
                                object.x += dx;
                                object.w -= dx;
                                object.h += dy;
                                break;
                            case 1:
                                object.x += dx;
                                object.y += dy;
                                object.w -= dx;
                                object.h -= dy;
                                break;
                            case 2:
                                object.y += dy;
                                object.w += dx;
                                object.h -= dy;
                                break;
                            case 3:
                                object.w += dx;
                                object.h += dy;
                                break;
                            default:
                                break;
                        }
                    } else if (object.w < C.MIN_SIZE_OBJECT) {
                        if (downCorner == 0 || downCorner == 1) {
                            object.x -= C.MIN_SIZE_OBJECT - object.w;
                        }
                        object.w = C.MIN_SIZE_OBJECT;
                    } else if (object.h < C.MIN_SIZE_OBJECT) {
                        if (downCorner == 1 || downCorner == 2) {
                            object.y -= C.MIN_SIZE_OBJECT - object.h;
                        }
                        object.h = C.MIN_SIZE_OBJECT;
                    }
                    break;
                default:
                    break;
            }
            highLightCorners = calculateCorners(object);
        }
        let hoverCorners = false;
        for (var i = 0; i < highLightCorners.length; i++) {
            let corner = highLightCorners[i];
            hoverCorners = checkDownCircle(mouseX, mouseY, corner.x, corner.y, C.CORNER_RADIUS * 3);
            if (hoverCorners) {
                if ((selectedObstacle >= 0 && obstacleList[selectedObstacle].form !== "circle" || selectedColorArea >= 0 && colorAreaList[selectedColorArea].form !== "circle")) {
                    switch (i) {
                        case 0:
                            $("#robotLayer").css('cursor', 'sw-resize');
                            break;
                        case 1:
                            $("#robotLayer").css('cursor', 'nw-resize');
                            break;
                        case 2:
                            $("#robotLayer").css('cursor', 'ne-resize');
                            break;
                        case 3:
                            $("#robotLayer").css('cursor', 'se-resize');
                    }
                } else {
                    switch (i) {
                        case 0:
                        case 2:
                            $("#robotLayer").css('cursor', 'nesw-resize');
                            break;
                        case 1:
                        case 3:
                            $("#robotLayer").css('cursor', 'nwse-resize');
                            break;
                        default:
                    }
                }
                break;
            }
        }

        if (downCorner >= 0 && (selectedObstacle >= 0 || selectedColorArea >= 0)) {
            if (selectedObstacle >= 0) {
                resizeObject(obstacleList[selectedObstacle], downCorner);
                updateObstacleLayer();
            } else if (selectedColorArea >= 0) {
                resizeObject(colorAreaList[selectedColorArea], downCorner);
                updateColorAreaLayer();
            }
            return;
        }

        // move obstacles
        for (let i = 0; i < obstacleList.length; i++) {
            let obstacle = obstacleList[i];
            let hoverObstacle = false;
            if (obstacle.form === "rectangle") {
                hoverObstacle = (mouseX > obstacle.x && mouseX < obstacle.x + obstacle.w && mouseY > obstacle.y && mouseY < obstacle.y + obstacle.h);
            } else if (obstacle.form === "triangle") {
                hoverObstacle = checkDownTriangle(mouseX, mouseY, obstacle.ax, obstacle.ay, obstacle.bx, obstacle.by, obstacle.cx, obstacle.cy);
            } else if (obstacle.form === "circle") {
                hoverObstacle = checkDownCircle(mouseX, mouseY, obstacle.x, obstacle.y, obstacle.r);
            }
            if (hoverObstacle && !hoverCorners) {
                $("#robotLayer").css('cursor', 'pointer');
            }
            if (downObstacle === i && selectedObstacle === i) {
                $("#robotLayer").css('cursor', 'pointer');
                switch (obstacle.form) {
                    case "rectangle":
                    case "circle":
                        obstacle.x += dx;
                        obstacle.y += dy;
                        break;
                    case "triangle":
                        obstacle.ax += dx;
                        obstacle.ay += dy;
                        obstacle.bx += dx;
                        obstacle.by += dy;
                        obstacle.cx += dx;
                        obstacle.cy += dy;
                        break;
                    default:
                }
                highLightCorners = calculateCorners(obstacle);
                updateObstacleLayer();
                return;
            }
        }

        // move colorAreas
        for (let i = 0; i < colorAreaList.length; i++) {
            let colorArea = colorAreaList[i];
            let hoverColorArea = false;
            if (colorArea.form === "rectangle") {
                hoverColorArea = (mouseX > colorArea.x && mouseX < colorArea.x + colorArea.w && mouseY > colorArea.y && mouseY < colorArea.y + colorArea.h);
            } else if (colorArea.form === "triangle") {
                hoverColorArea = checkDownTriangle(mouseX, mouseY, colorArea.ax, colorArea.ay, colorArea.bx, colorArea.by, colorArea.cx, colorArea.cy);
            } else if (colorArea.form === "circle") {
                hoverColorArea = checkDownCircle(mouseX, mouseY, colorArea.x, colorArea.y, colorArea.r);
            }
            if (hoverColorArea && !hoverCorners) {
                $("#robotLayer").css('cursor', 'pointer');
            }
            if (downColorArea === i && selectedColorArea === i) {
                $("#robotLayer").css('cursor', 'pointer');
                switch (colorArea.form) {
                    case "rectangle":
                    case "circle":
                        colorArea.x += dx;
                        colorArea.y += dy;
                        break;
                    case "triangle":
                        colorArea.ax += dx;
                        colorArea.ay += dy;
                        colorArea.bx += dx;
                        colorArea.by += dy;
                        colorArea.cx += dx;
                        colorArea.cy += dy;
                        break;
                    default:
                }
                highLightCorners = calculateCorners(colorArea);
                updateColorAreaLayer();
                return;
            }
        }

        let hoverRuler = (mouseX > ruler.x && mouseX < ruler.x + ruler.w && mouseY > ruler.y && mouseY < ruler.y + ruler.h);
        if (hoverRuler) {
            $("#robotLayer").css('cursor', 'pointer');
            if (downRuler) {
                ruler.x += dx;
                ruler.y += dy;
                scene.drawRuler();
                e.preventDefault();
                return;
            }
        }
        e.preventDefault();
    }

    var dist = 0;

    function handleMouseWheel(e) {
        var delta = 0;
        if (e.originalEvent.wheelDelta !== undefined) {
            delta = e.originalEvent.wheelDelta;
        } else {
            if (e.originalEvent.touches) {
                if (e.originalEvent.touches[0] && e.originalEvent.touches[1]) {
                    var diffX = e.originalEvent.touches[0].pageX - e.originalEvent.touches[1].pageX;
                    var diffY = e.originalEvent.touches[0].pageY - e.originalEvent.touches[1].pageY;
                    var newDist = diffX * diffX + diffY * diffY;
                    if (dist == 0) {
                        dist = newDist;
                        return;
                    } else {
                        delta = newDist - dist;
                        dist = newDist;
                    }
                } else {
                    dist = 0;
                    return;
                }
            } else {
                delta = -e.originalEvent.deltaY;
            }
        }
        var zoom = false;
        if (delta > 0) {
            scale *= 1.025;
            if (scale > 2) {
                scale = 2;
            }
            zoom = true;
        } else if (delta < 0) {
            scale *= 0.925;
            if (scale < 0.25) {
                scale = 0.25;
            }
            zoom = true;
        }
        if (zoom) {
            scene.resizeBackgrounds(scale);
            scene.drawRuler();
            updateSelectedObjects();
            e.stopPropagation();
        }
    }

    function resizeAll() {
        if (!canceled) {
            canvasOffset = $("#simDiv").offset();
            offsetX = canvasOffset.left;
            offsetY = canvasOffset.top;
            scene.playground.w = $('#simDiv').outerWidth();
            scene.playground.h = $(window).height() - offsetY;
            ground.x = 10;
            ground.y = 10;
            ground.w = imgObjectList[currentBackground].width;
            ground.h = imgObjectList[currentBackground].height;
            var scaleX = scene.playground.w / (ground.w + 20);
            var scaleY = scene.playground.h / (ground.h + 20);
            scale = Math.min(scaleX, scaleY) - 0.05;
            scene.resizeBackgrounds(scale);
            scene.drawRuler();
            updateSelectedObjects();
        }
    }

    function updateSelectedObjects() {
        if (selectedObstacle >= 0) {
            scene.drawColorAreas([]);
            updateObstacleLayer();
            return;
        }
        if (selectedColorArea >= 0) {
            scene.drawObstacles([]);
            updateColorAreaLayer();
            return;
        }
        scene.drawObstacles([]);
        scene.drawColorAreas([]);
    }

    function addMouseEvents() {
        removeMouseEvents();
        $("#robotLayer").on('mousedown touchstart', function(e) {
            if (robots[robotIndex].handleMouseDown) {
                robots[robotIndex].handleMouseDown(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            } else {
                handleMouseDown(e);
            }
        });
        $("#robotLayer").on('mousemove touchmove', function(e) {
            if (robots[robotIndex].handleMouseMove) {
                robots[robotIndex].handleMouseMove(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            } else {
                handleMouseMove(e);
            }
        });
        $("#robotLayer").mouseup(function(e) {
            if (robots[robotIndex].handleMouseUp) {
                robots[robotIndex].handleMouseUp(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
            } else {
                handleMouseUp(e);
            }
        });
        $("#robotLayer").on('mouseout touchcancel', function(e) {
            if (robots[robotIndex].handleMouseOut) {
                robots[robotIndex].handleMouseOut(e, offsetX, offsetY, scene.playground.w / 2, scene.playground.h / 2);
            } else {
                handleMouseOut(e);
            }
        });
        $("#simDiv").on('wheel mousewheel touchmove', function(e) {
            handleMouseWheel(e);
        });
        $("#canvasDiv").draggable();
        $("#robotLayer").attr("tabindex", 0);
        $("#robotLayer").on("click touchstart", function(e) {
            $("#robotLayer").attr("tabindex", 0);
            $("#robotLayer").focus();
            e.preventDefault();
        });
        $("#blocklyDiv").on("click touchstart", setFocusBlocklyDiv);
        $("#robotLayer").on("keydown", handleKeyEvent);
        $("#robotLayer").on("keyup", function() {
            if (selectedRobot >= 0 && robots[selectedRobot].drawWidth) {
                robots[selectedRobot].pose.xOld = robots[selectedRobot].pose.x;
                robots[selectedRobot].pose.yOld = robots[selectedRobot].pose.y;
                robots[selectedRobot].canDraw = true;
            }
        });
        $("#robotIndex").on('change', function(e) {
            $("#brick" + robotIndex).hide();
            robotIndex = e.target.selectedIndex;
            selectedRobot = e.target.selectedIndex;
            $("#brick" + robotIndex).show();
        });
    }

    function setFocusBlocklyDiv(e) {
        $("#blocklyDiv").attr("tabindex", 0);
        $("#blocklyDiv").focus();
        e.preventDefault();
    }

    function removeMouseEvents() {
        $("#robotLayer").off();
        $("#simDiv").off();
        $("#canvasDiv").off();
        $("#robotIndex").off();
        $("#blocklyDiv").off("click touchstart", setFocusBlocklyDiv);
    }

    function initScene() {
        scene = new Scene(imgObjectList[currentBackground], robots, imgPattern, ruler);
        scene.drawRobots();
        resetSelection();
        addMouseEvents();
        disableChangeObjectButtons();
        if (robots[0].colorRange) {
            colorpicker = new HUEBEE('#colorpicker', {
                shades: 1,
                hues: 8,
                customColors: robots[0].colorRange,
                setText: false
            });
            colorpicker.on('change', function(color) {
                changeColorWithColorPicker(color);
            });
            let close = HUEBEE.prototype.close;
            HUEBEE.prototype.close = function() {
                $(".huebee__container").off("mouseup touchend", resetColorpickerCursor);
                close.apply(this);
            };
            let open = HUEBEE.prototype.open;
            HUEBEE.prototype.open = function() {
                open.apply(this);
                $(".huebee__container").on("mouseup touchend", resetColorpickerCursor)
            };
        }
        for (var i = 0; i < numRobots; i++) {
            readyRobots[i] = true;
        }
        resizeAll();
        $(window).on("resize", resizeAll);
        $('#backgroundDiv').on("resize", resizeAll);
        render();
    }

    function getScale() {
        return scale;
    }
    exports.getScale = getScale;

    function getGround() {
        return ground;
    }
    exports.getGround = getGround;

    function getInfo() {
        return info;
    }
    exports.getInfo = getInfo;

    function isIE() {
        var ua = window.navigator.userAgent;
        var ie = ua.indexOf('MSIE ');
        var ie11 = ua.indexOf('Trident/');

        if ((ie > -1) || (ie11 > -1)) {
            return true;
        }
        return false;
    }

    function isEdge() {
        var ua = window.navigator.userAgent;
        var edge = ua.indexOf('Edge');
        return edge > -1;
    }

    function importConfigData() {
        $('#backgroundFileSelector').val(null);
        $('#backgroundFileSelector').attr("accept", ".json");
        $('#backgroundFileSelector').clickWrap(); // opening dialog
        $('#backgroundFileSelector').change(function(event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onload = (function(theFile) {
                return function(e) {
                    try {
                        const configData = JSON.parse(e.target.result);
                        relatives2coordinates(configData);
                        resetSelection();
                        resetScene(obstacleList || [], colorAreaList || [])
                        initScene();
                    } catch (ex) {
                        MSG.displayPopupMessage("Blockly.Msg.POPUP_BACKGROUND_STORAGE", Blockly.Msg.POPUP_CONFIG_UPLOAD_ERROR);
                    }
                }
            })(file);
            reader.readAsText(file);
            event.preventDefault();
            event.stopPropagation();
        });
    }
    exports.importConfigData = importConfigData;

    function exportConfigData() {
        return coordinates2relatives();
    }
    exports.exportConfigData = exportConfigData;

    function coordinates2relatives() {
        let height = $('#unitBackgroundLayer').height();
        let width = $('#unitBackgroundLayer').width();
        let relatives = {};
        function calculateShape(object) {
            switch (object.form) {
                case "rectangle":
                    return {
                        "x": object.x / width,
                        "y": object.y / height,
                        "w": object.w / width,
                        "h": object.h / height,
                        "theta": object.theta,
                        color: object.color,
                        form: object.form,
                        type: object.type
                    };
                case "triangle":
                    return {
                        ax: object.ax / width,
                        ay: object.ay / height,
                        bx: object.bx / width,
                        by: object.by / height,
                        cx: object.cx / width,
                        cy: object.cy / height,
                        color: object.color,
                        form: object.form,
                        type: object.type
                    }
                case "circle":
                    return {
                        "x": object.x / width,
                        "y": object.y / height,
                        "r": object.r / height / width,
                        color: object.color,
                        form: object.form,
                        type: object.type,
                        startAngle: 50,
                        endAngle: 0
                    };
            }
        }
        relatives.robotPoses = robots.map(function(robot) {
            return {
                "x": robot.pose.x / width,
                "y": robot.pose.y / height,
                "theta": robot.pose.theta,
                "xOld": robot.pose.x / width,
                "yOld": robot.pose.y / height,
                "transX": 0,
                "transY": 0,
                "thetaOld": robot.pose.thetaOld,
                "thetaDiff": 0
            }
        })
        relatives.obstacles = obstacleList.map(function(object) {
            return calculateShape(object);
        });
        relatives.colorAreas = colorAreaList.map(function(object) {
            return calculateShape(object);
        });
        relatives.ruler = ruler;
        return relatives;
    }

    function relatives2coordinates(relatives) {
        let height = $('#unitBackgroundLayer').height();
        let width = $('#unitBackgroundLayer').width();
        function calculateShape(object) {
            switch (object.form) {
                case "rectangle":
                    return {
                        "x": object.x * width,
                        "y": object.y * height,
                        "w": object.w * width,
                        "h": object.h * height,
                        "theta": object.theta,
                        color: object.color,
                        form: object.form,
                        type: object.type
                    };
                case "triangle":
                    return {
                        ax: object.ax * width,
                        ay: object.ay * height,
                        bx: object.bx * width,
                        by: object.by * height,
                        cx: object.cx * width,
                        cy: object.cy * height,
                        color: object.color,
                        form: object.form,
                        type: object.type
                    }
                case "circle":
                    return {
                        "x": object.x * width,
                        "y": object.y * height,
                        "r": object.r * height * width,
                        color: object.color,
                        form: object.form,
                        type: object.type,
                        startAngle: 50,
                        endAngle: 0
                    };
            }
        }
        for (var i = 0; i < robots.length; i++) {
            if (relatives.robotPoses[i]) {
                robots[i].pose.x = relatives.robotPoses[i].x * width;
                robots[i].pose.y = relatives.robotPoses[i].y * height;
                robots[i].pose.theta = relatives.robotPoses[i].theta;
                robots[i].pose.xOld = relatives.robotPoses[i].xOld * width;
                robots[i].pose.yOld = relatives.robotPoses[i].yOld * height;
                robots[i].pose.thetaOld = relatives.robotPoses[i].thetaOld;
            }
        }
        obstacleList = relatives.obstacles.map(function(object) {
            return calculateShape(object);
        });
        colorAreaList = relatives.colorAreas.map(function(object) {
            return calculateShape(object);
        });
        ruler = relatives.ruler;
    }

    function resetScene(obstacleL, colorAreaL) {
        obstacleList = obstacleL;
        colorAreaList = colorAreaL;
        copiedObject = null;
    }
    exports.resetScene = resetScene;


    function importImage() {
        $('#backgroundFileSelector').val(null);
        $('#backgroundFileSelector').attr("accept", ".png, .jpg, .jpeg, .svg");
        $('#backgroundFileSelector').clickWrap(); // opening dialog
        $('#backgroundFileSelector').change(function(event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onload = function(event) {
                var img = new Image();
                img.onload = function() {
                    var canvas = document.createElement("canvas");
                    var scaleX = 1;
                    var scaleY = 1;
                    // - 20 because of the border pattern which is 10 pixels wide on both sides
                    if (img.width > C.MAX_WIDTH - 20) {
                        scaleX = (C.MAX_WIDTH - 20) / img.width;
                    }
                    if (img.height > C.MAX_HEIGHT - 20) {
                        scaleY = (C.MAX_HEIGHT - 20) / img.height;
                    }
                    var scale = Math.min(scaleX, scaleY);
                    canvas.width = img.width * scale;
                    canvas.height = img.height * scale;
                    var ctx = canvas.getContext("2d");
                    ctx.scale(scale, scale);
                    ctx.drawImage(img, 0, 0);
                    var dataURL = canvas.toDataURL("image/png");
                    var image = new Image(canvas.width, canvas.height);
                    image.src = dataURL;
                    image.onload = function() {
                        if (customBackgroundLoaded) {
                            // replace previous image
                            imgObjectList[imgObjectList.length - 1] = image;
                        } else {
                            imgObjectList[imgObjectList.length] = image;
                        }
                        setBackground(imgObjectList.length - 1);
                        initScene();
                    }

                    if (UTIL.isLocalStorageAvailable()) {
                        $('#show-message-confirm').oneWrap('shown.bs.modal', function(e) {
                            $('#confirm').off();
                            $('#confirm').on('click', function(e) {
                                e.preventDefault();
                                localStorage.setItem("customBackground", JSON.stringify({
                                    image: dataURL.replace(/^data:image\/(png|jpg);base64,/, ""),
                                    timestamp: new Date().getTime()
                                }));
                            });
                            $('#confirmCancel').off();
                            $('#confirmCancel').on('click', function(e) {
                                e.preventDefault();
                            });
                        });
                        MSG.displayPopupMessage("Blockly.Msg.POPUP_BACKGROUND_STORAGE", Blockly.Msg.POPUP_BACKGROUND_STORAGE, Blockly.Msg.YES, Blockly.Msg.NO);
                    }
                };
                img.src = reader.result;
            };
            reader.readAsDataURL(file);
            return false;
        });
    }

    exports.importImage = importImage;

    function arrToRgb(values) {
        return 'rgb(' + values.join(', ') + ')';
    }

    function createRobots(reqRobot, numRobots) {
        $("#simRobotContent").empty();
        $("#simRobotModal").modal("hide");
        robots = [];
        if (numRobots >= 1) {
            var tempRobot = createRobot(reqRobot, configurations[0], 0, 0, interpreters[0].getRobotBehaviour());
            tempRobot.savedName = userPrograms[0].savedName;
            robots[0] = tempRobot;
            if (robots[0].brick) {
                $("#simRobotContent").append(robots[0].brick);
            }
            for (var i = 1; i < numRobots; i++) {
                var yOffset = 60 * (Math.floor((i + 1) / 2)) * (Math.pow((-1), i));
                tempRobot = createRobot(reqRobot, configurations[i], i, yOffset, interpreters[i].getRobotBehaviour());
                tempRobot.savedName = userPrograms[i].savedName;
                var tempcolor = arrToRgb(colorsAdmissible[((i - 1) % (colorsAdmissible.length))]);
                tempRobot.geom.color = tempcolor;
                robots[i] = tempRobot;
                if (robots[i].brick) {
                    $("#simRobotContent").append(robots[i].brick);
                    $("#brick" + i).hide();
                }
            }
        } else {
            // should not happen
            // TODO throw exception
        }
    }

    function createRobot(reqRobot, configuration, num, optYOffset, robotBehaviour) {
        var yOffset = optYOffset || 0;
        var robot;
        if (currentBackground == 2) {
            robot = new reqRobot({
                x: 240,
                y: 200 + yOffset,
                theta: 0,
                xOld: 240,
                yOld: 200 + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = false;
        } else if (currentBackground == 3) {
            robot = new reqRobot({
                x: 200,
                y: 200 + yOffset,
                theta: 0,
                xOld: 200,
                yOld: 200 + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = true;
            robot.drawColor = "#000000";
            robot.drawWidth = 10;
        } else if (currentBackground == 4) {
            var robotY = 104 + yOffset;
            if (num >= 2) {
                robotY = 104 + 60 * (num - 1);
            }
            robot = new reqRobot({
                x: 70,
                y: robotY,
                theta: 0,
                xOld: 70,
                yOld: 104 + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = false;
        } else if (currentBackground == 5) {
            robot = new reqRobot({
                x: 400,
                y: 50 + 60 * num,
                theta: 0,
                xOld: 400,
                yOld: 50 + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = false;
        } else if (currentBackground == 6) {
            var robotY = 440 + yOffset;
            if (num > 2) {
                robotY = 440 - 60 * (num - 1);
            }
            robot = new reqRobot({
                x: 800,
                y: robotY,
                theta: -Math.PI / 2,
                xOld: 800,
                yOld: 440 + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = false;
        } else if (currentBackground == 7) {
            var cx = imgObjectList[currentBackground].width / 2.0 + 10;
            var cy = imgObjectList[currentBackground].height / 2.0 + 10;
            robot = new reqRobot({
                x: cx,
                y: cy + yOffset,
                theta: 0,
                xOld: cx,
                yOld: cy + yOffset,
                transX: -cx,
                transY: -cy
            }, configuration, num, robotBehaviour);
            robot.canDraw = true;
            robot.drawColor = "#ffffff";
            robot.drawWidth = 1;
        } else {
            var cx = imgObjectList[currentBackground].width / 2.0 + 10;
            var cy = imgObjectList[currentBackground].height / 2.0 + 10;
            robot = new reqRobot({
                x: cx,
                y: cy + yOffset,
                theta: 0,
                xOld: cx,
                yOld: cy + yOffset,
                transX: 0,
                transY: 0
            }, configuration, num, robotBehaviour);
            robot.canDraw = false;
        }
        return robot;
    }

    function getWebAudio() {
        if (!this.webAudio) {
            this.webAudio = {};
            var AudioContext = window.AudioContext || window.webkitAudioContext || false;
            if (AudioContext) {
                this.webAudio.context = new AudioContext();
            } else {
                this.webAudio.context = null;
                this.webAudio.oscillator = null;
                console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
            }
        }
        return this.webAudio;
    }
    exports.getWebAudio = getWebAudio;

    /** adds/removes the ability for a block to be a breakpoint to a block */
    function updateBreakpointEvent() {
        if (debugMode) {
            Blockly.getMainWorkspace().getAllBlocks().forEach(function(block) {
                if (!$(block.svgGroup_).hasClass('blocklyDisabled')) {

                    if (observers.hasOwnProperty(block.id)) {
                        observers[block.id].disconnect();
                    }

                    var observer = new MutationObserver(function(mutations) {
                        mutations.forEach(function(mutation) {
                            if ($(block.svgGroup_).hasClass('blocklyDisabled')) {
                                removeBreakPoint(block);
                                $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                            } else {
                                if ($(block.svgGroup_).hasClass('blocklySelected')) {
                                    if ($(block.svgPath_).hasClass('breakpoint')) {
                                        removeBreakPoint(block);
                                        $(block.svgPath_).removeClass('breakpoint');
                                    } else if ($(block.svgPath_).hasClass('selectedBreakpoint')) {
                                        removeBreakPoint(block);
                                        $(block.svgPath_).removeClass('selectedBreakpoint').stop(true, true).animate({ 'fill-opacity': '1' }, 0);
                                    } else {
                                        breakpoints.push(block.id);
                                        $(block.svgPath_).addClass('breakpoint');
                                    }
                                }
                            }
                        });
                    });
                    observers[block.id] = observer;
                    observer.observe(block.svgGroup_, { attributes: true });
                }
            });
        } else {
            Blockly.getMainWorkspace().getAllBlocks().forEach(function(block) {
                if (observers.hasOwnProperty(block.id)) {
                    observers[block.id].disconnect();
                }
                $(block.svgPath_).removeClass('breakpoint');
            })
        }
    }

    function getDebugMode() {
        return debugMode;
    }
    exports.getDebugMode = getDebugMode;

    /** updates the debug mode for all interpreters */
    function updateDebugMode(mode) {
        debugMode = mode;
        if (interpreters !== null) {
            for (var i = 0; i < numRobots; i++) {
                interpreters[i].setDebugMode(mode);
            }
        }
        updateBreakpointEvent();
    }
    exports.updateDebugMode = updateDebugMode;

    /** removes breakpoint block */
    function removeBreakPoint(block) {
        for (var i = 0; i < breakpoints.length; i++) {
            if (breakpoints[i] === block.id) {
                breakpoints.splice(i, 1);
            }
        }
        if (!breakpoints.length > 0 && interpreters !== null) {
            for (var i = 0; i < numRobots; i++) {
                interpreters[i].removeEvent(C.DEBUG_BREAKPOINT);
            }
        }
    }

    /** adds an event to the interpreters */
    function interpreterAddEvent(mode) {
        updateBreakpointEvent();
        if (interpreters !== null) {
            for (var i = 0; i < numRobots; i++) {
                interpreters[i].addEvent(mode);
            }
        }
    }

    exports.interpreterAddEvent = interpreterAddEvent;

    /** called to signify debugging is finished in simulation */
    function endDebugging() {
        if (interpreters !== null) {
            for (var i = 0; i < numRobots; i++) {
                interpreters[i].setDebugMode(false);
                interpreters[i].breakPoints = [];
            }
        }
        Blockly.getMainWorkspace().getAllBlocks().forEach(function(block) {
            $(block.svgPath_).stop(true, true).removeAttr('style');
        });
        breakpoints = [];
        debugMode = false;
        updateBreakpointEvent();
    }

    exports.endDebugging = endDebugging;

    /** returns the simulations variables */
    function getSimVariables() {
        if (interpreters !== null) {
            return interpreters[0].getVariables();
        } else {
            return {};
        }
    }

    exports.getSimVariables = getSimVariables;
});

//http://paulirish.com/2011/requestanimationframe-for-smart-animating/
//http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating
//requestAnimationFrame polyfill by Erik Möller
//fixes from Paul Irish and Tino Zijdel
(function() {
    var lastTime = 0;
    var vendors = ['ms', 'moz', 'webkit', 'o'];
    for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
        window.cancelAnimationFrame = (window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame']);
    }

    if (!window.requestAnimationFrame) {
        window.requestAnimationFrame = function(callback) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, frameRateMs - (currTime - lastTime));
            var id = window.setTimeout(function() {
                callback();
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