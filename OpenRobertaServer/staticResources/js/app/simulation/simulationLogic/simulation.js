/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
 
/**
 * @namespace SIM
 */
 
define(['exports', 'simulation.scene', 'simulation.math', 'program.controller', 'simulation.constants', 'util', 'program.controller',
    'interpreter.interpreter', 'interpreter.robotSimBehaviour', 'volume-meter', 'simulation.constants', 'message', 'jquery', 'huebee'
], function(exports, Scene, SIMATH, ROBERTA_PROGRAM, CONST, UTIL, PROGRAM_C,
    SIM_I, MBED_R, Volume, C, MSG, $, huebee) {
 
    var interpreters;
    var scene;
    var userPrograms;
    var configurations = [];
    var canvasOffset;
    var offsetX;
    var offsetY;
    var isDownRobots = [];
    var isDownObstacle = false;
    var isDownRuler = false;
    var isDownColorBlock = false;
    var isDownColorBlockCorner = false;
    var isDownObstacleCorner = false;
    var colorBlocksActivated = true;
    var colorPickerIsShown = false;
    var startX;
    var startY;
    var scale = 1;
    var timerStep = 0;
    var selectedObstacle;
    var selectedColorBlock;
    var selectedObject;
    var selectedCorner;
    var selectedCornerObject;
    var hoverColorBlock;
    var hoverObstacle;
    var hoverObstacleCorners;
    var hoverColorBlockCorners;
    var hoveringCorner;
    var canceled;
    var storedPrograms;
    var copiedObject;
    var customBackgroundLoaded = false;
    var debugMode = false;
    var breakpoints = [];
    var customObstacleList = [];
    var colorBlockList = [];
    var observers = {};
    const simChangeObjectColorButton = document.getElementById('simChangeObjectColor');
    const simDeleteObjectButton = document.getElementById('simDeleteObject');

    var imgObstacle1 = new Image();
    var imgPattern = new Image();
    var imgRuler = new Image();
    var ev3_colors = ["#000000","#0056a6","#00642f","#532115", "#585858","#b30006","#f7e307"];
    var nxt_colors = ["#000000","#0057a6","#00642e","#00ff00","#585858","#800080",
            "#b30006","#dc143c","#ee82ee","#f7d117","#ff00ff","#ffa500"]

    var colorpicker = new huebee('#colorpicker', {
        shades: 1,
        hues: 8,
        customColors: ev3_colors
    });
    var imgList = ['/js/app/simulation/simBackgrounds/baustelle.svg', '/js/app/simulation/simBackgrounds/ruler.svg',
        '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.svg',
        '/js/app/simulation/simBackgrounds/microbitBackground.svg', '/js/app/simulation/simBackgrounds/simpleBackground.svg',
        '/js/app/simulation/simBackgrounds/drawBackground.svg', '/js/app/simulation/simBackgrounds/robertaBackground.svg',
        '/js/app/simulation/simBackgrounds/rescueBackground.svg', '/js/app/simulation/simBackgrounds/wroBackground.svg',
        '/js/app/simulation/simBackgrounds/mathBackground.svg'
    ];
    var imgListIE = ['/js/app/simulation/simBackgrounds/baustelle.png', '/js/app/simulation/simBackgrounds/ruler.png',
        '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.png',
        '/js/app/simulation/simBackgrounds/microbitBackground.png', '/js/app/simulation/simBackgrounds/simpleBackground.png',
        '/js/app/simulation/simBackgrounds/drawBackground.png', '/js/app/simulation/simBackgrounds/robertaBackground.png',
        '/js/app/simulation/simBackgrounds/rescueBackground.png', '/js/app/simulation/simBackgrounds/wroBackground.png',
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

    function setBackground(num, callback) {
        if (num == undefined) {
            deleteElements();
            setObstacle();
            setRuler();
            removeMouseEvents();
            scene = new Scene(imgObjectList[currentBackground], robots, customObstacleList, imgPattern, ruler, colorBlockList);
            scene.updateBackgrounds();
            scene.drawObstacles();
            scene.drawColorBlocks();
            scene.drawRuler();
            addMouseEvents();
            reloadProgram();
            resizeAll();
            return currentBackground;
        }
        setPause(true);
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
        if (num === -1) {
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
        require([moduleName], function(ROBOT) {
            createRobots(ROBOT, numRobots);
            for (var i = 0; i < robots.length; i++) {
                robots[i].debug = debug;
                robots[i].reset();
            }
            if(callback != null) callback();
        });
    }

    exports.setBackground = setBackground;

    function getBackground() {
        return currentBackground;
    }

    exports.getBackground = getBackground;

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
                colorBlocksActivated = true;
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
            } else {
                colorBlocksActivated = false;
                selectedColorBlock = null;
                selectedObject = null;
                scene.drawObstacles();
                $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
            }
            pause = value;
        }
        for (var i = 0; i < robots.length; i++) {
            if (robots[i].left) {
                robots[i].left = 0;
            }
            if (robots[i].right) {
                robots[i].right = 0;
            }
        }
    }

    exports.setPause = setPause;

    var stepCounter;
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
        for (var i = 0; i < numRobots; i++) {
            if (robots[i].resetPose) {
                robots[i].resetPose();
            }
            if (robots[i].time) {
                robots[i].time = 0;
            }
        }
        for (let key in customObstacleList) {
            customObstacleList[key].default = Object.assign({}, customObstacleList[key]);
        }
        for (let key in colorBlockList) {
            colorBlockList[key].default = Object.assign({}, colorBlockList[key]);
        }
        selectedObject = null;
        selectedObstacle = null;
        selectedColorBlock = null;
        updateColorLayer();
        updateObstacleLayer();
    }
    exports.resetPose = resetPose;

    function addObstacle(shape) {
        if (shape === "rectangle") {
            let newRectangleObstacle = {
                x: (Math.random() * ((ground.w-200) - 100) + 100),
                y: (Math.random() * ((ground.h-100) - 100) + 100),
                xOld: 0,
                yOld: 0,
                w: 100,
                h: 100,
                wOld: 0,
                hOld: 0,
                theta: 0,
                img: null,
                isParallelToAxis: true,
                color: "#2b2b2b",
                type: "obstacle",
                form: "rectangle",
                default: null
            };
            newRectangleObstacle.default = Object.assign({}, newRectangleObstacle);
            customObstacleList.push(newRectangleObstacle);
        } else if (shape === "triangle") {
            let x = (Math.random() * ((ground.w-125) - 125) + 125);
            let y = (Math.random() * ((ground.h-125) - 125) + 125);

            let newTriangleObstacle = {
                ax: x-50,
                ay: y+50,
                bx: x,
                by: y-50,
                cx: x+50,
                cy: y+50,
                isParallelToAxis: true,
                color: "#2b2b2b",
                type: "obstacle",
                form: "triangle",
                default: null
            };
            newTriangleObstacle.default = Object.assign({}, newTriangleObstacle);
            customObstacleList.push(newTriangleObstacle);
        } else if (shape === "circle") {
            let x = (Math.random() * ((ground.w-125) - 125) + 125);
            let y = (Math.random() * ((ground.h-125) - 125) + 125);

            let newCircleObstacle = {
                x: x,
                y: y,
                r: 50,
                startAngle: 50,
                endAngle: 0,
                isParallelToAxis: true,
                color: "#2b2b2b",
                type: "obstacle",
                form: "circle",
                default: null
            };
            newCircleObstacle.default = Object.assign({}, newCircleObstacle);
            customObstacleList.push(newCircleObstacle);
        }
        selectedColorBlock = null;
        selectedObstacle = customObstacleList.length-1;
        selectedObject = customObstacleList[selectedObstacle];
        enableChangeObjectButtons();
        exports.obstacleList = [ground, customObstacleList];
        scene.drawObstacles();
    }
    exports.addObstacle = addObstacle;

    function deleteElements() {
        resetSelection();
        while(customObstacleList.length > 0) {
            customObstacleList.pop();
        }
        while(colorBlockList.length > 0) {
            colorBlockList.pop();
        }
        setObstacle();
        updateColorLayer();
        updateObstacleLayer();
    }
    exports.deleteElements = deleteElements;


    function deleteSelectedObject(){
        if (selectedColorBlock != null){
            colorBlockList.splice(selectedColorBlock,1);
            resetSelection();
            updateColorLayer();
        }
        if (selectedObstacle != null){
            customObstacleList.splice(selectedObstacle, 1)
            resetSelection();
            updateObstacleLayer();
            }
        }
    exports.deleteSelectedObject = deleteSelectedObject;


    function addColorBlock(color) {
        let newColorBlock = {
            x: (Math.random() * ((ground.w-200) - 100) + 100),
            y: (Math.random() * ((ground.h-100) - 100) + 100),
            xOld: 0,
            yOld: 0,
            w: 43,
            h: 80,
            wOld: 0,
            hOld: 0,
            theta: 0,
            img: null,
            color: C.COLOR_ENUM.BLACK,
            form: "rectangle",
            type: "colorBlock",
            default: null
        };
        if(color === "black") newColorBlock.color = C.COLOR_ENUM.BLACK;
        if(color === "blue") newColorBlock.color = C.COLOR_ENUM.BLUE;
        if(color === "green") newColorBlock.color = C.COLOR_ENUM.GREEN;
        if(color === "red") newColorBlock.color = C.COLOR_ENUM.RED;
        if(color === "yellow") newColorBlock.color = C.COLOR_ENUM.YELLOW;
        newColorBlock.default = Object.assign({}, newColorBlock);
        colorBlockList.push(newColorBlock);
        selectedObstacle = null;
        selectedColorBlock = colorBlockList.length-1;
        selectedObject = colorBlockList[selectedColorBlock];
        enableChangeObjectButtons();
        scene.drawColorBlocks();
        scene.drawObstacles();
    }
    exports.addColorBlock = addColorBlock;

    function changeColorWithColorPicker(color) {
        if(selectedObject != null) {
            selectedObject.color = color;
            updateColorLayer();
            updateObstacleLayer();
        }
    }

    function displayColorPicker() {
        if(colorPickerIsShown === false) {
            colorpicker.open();
            colorPickerIsShown = true;
        } else if(colorPickerIsShown === true) {
            colorpicker.close();
            colorPickerIsShown = false;
        }
    }
    exports.displayColorPicker = displayColorPicker;

    function stopProgram() {
        setPause(true);
        for (var i = 0; i < numRobots; i++) {
            robots[i].reset();
        }
        reloadProgram();

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
    var ground = {
        x: 0,
        y: 0,
        w: 500,
        h: 500,
        isParallelToAxis: true,
        type: "ground"
    };

    var customObstacle = {
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
        type: "obstacle",
        default: null
    };

    customObstacleList.unshift(customObstacle);
    exports.obstacleList = [ground, customObstacleList];

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
    var mouseOnRobotIndex = 0;
    var simRobotType;
    var numRobots = 0;
    exports.getNumRobots = function() {
        return numRobots;
    };
    var ROBOT;

    function callbackOnTermination() {
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
            resetScene();
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead, #simEditButtons').hide();
        } else if (robotType === 'microbit') {
            resetScene();
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
                $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead, #simEditButtons').show();
            }
            if ($('#device-size').find('div:visible').first().attr('id')) {
                $('#simButtonsHead').show();
            }
        }
        interpreters = programs.map(function(x) {
            var src = JSON.parse(x.javaScriptProgram);
            configurations.push(x.javaScriptConfiguration);
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
                colorPickerIsShown = false;
                isDownRuler = false;
                isDownColorBlock = false;
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
            reloadProgram();
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
            if (!robots[i].pause && !pause) {
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
                    scene.drawVariables();

                    // some time to cancel all timeouts
                    setTimeout(function() {
                        init(userPrograms, false, simRobotType);
                        addMouseEvents();
                    }, 205);

                    if (!(allInterpretersTerminated() && !robots[i].endless)) {
                        setTimeout(function() {
                            //delete robot.button.Reset;
                            setPause(false);
                            for (var j = 0; j < robots.length; j++) {
                                robots[j].pause = false;
                            }
                        }, 1000);
                    }
                }
            }
            robots[i].update();
            updateBreakpointEvent();
        }
        var renderTimeStart = new Date().getTime();
        
        function allInterpretersTerminated() {
            for (var i = 0; i < interpreters.length; i++) {
                if (!interpreters[i].isTerminated()){
                    return false;
                }
            }
            return true;
        }

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

        reset = robots[0].buttons.Reset;
        scene.updateSensorValues(!pause);
        scene.drawRobots();
        scene.drawVariables();
        renderTime = new Date().getTime() - renderTimeStart;
    }

    function reloadProgram() {
        $('.simForward').removeClass('typcn-media-pause');
        $('.simForward').addClass('typcn-media-play');
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
    }

    //set standard obstacle
    function setObstacle() {
        if (customObstacleList.length>=1){
            if (currentBackground == 3) {
                customObstacleList[0].x = 500;
                customObstacleList[0].y = 250;
                customObstacleList[0].w = 100;
                customObstacleList[0].h = 100;
                customObstacleList[0].img = null;
                customObstacleList[0].color = "#33B8CA";
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 2) {
                customObstacleList[0].x = 580;
                customObstacleList[0].y = 290;
                customObstacleList[0].w = 100;
                customObstacleList[0].h = 100;
                customObstacleList[0].color = "#33B8CA";
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 4) {
                customObstacleList[0].x = 500;
                customObstacleList[0].y = 260;
                customObstacleList[0].w = 100;
                customObstacleList[0].h = 100;
                customObstacleList[0].img = imgObstacle1;
                customObstacleList[0].color = null;
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 7) {
                customObstacleList[0].x = 0;
                customObstacleList[0].y = 0;
                customObstacleList[0].w = 0;
                customObstacleList[0].h = 0;
                customObstacleList[0].color = null;
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 0) {
                customObstacleList[0].x = 0;
                customObstacleList[0].y = 0;
                customObstacleList[0].w = 0;
                customObstacleList[0].h = 0;
                customObstacleList[0].color = null;
                customObstacleList[0].img = null;
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 1) {
                customObstacleList[0].x = 0;
                customObstacleList[0].y = 0;
                customObstacleList[0].w = 0;
                customObstacleList[0].h = 0;
                customObstacleList[0].color = null;
                customObstacleList[0].img = null;
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 5) {
                customObstacleList[0].x = 505;
                customObstacleList[0].y = 405;
                customObstacleList[0].w = 20;
                customObstacleList[0].h = 20;
                customObstacleList[0].color = "#33B8CA";
                customObstacleList[0].img = null;
                customObstacleList[0].form = "rectangle";
            } else if (currentBackground == 6) {
                customObstacleList[0].x = 425;
                customObstacleList[0].y = 254;
                customObstacleList[0].w = 50;
                customObstacleList[0].h = 50;
                customObstacleList[0].color = "#009EE3";
                customObstacleList[0].img = null;
                customObstacleList[0].form = "rectangle";
            } else {
                var x = imgObjectList[currentBackground].width - 50;
                var y = imgObjectList[currentBackground].height - 50;
                customObstacleList[0].x = x;
                customObstacleList[0].y = y;
                customObstacleList[0].w = 50;
                customObstacleList[0].h = 50;
                customObstacleList[0].color = "#33B8CA";
                customObstacleList[0].img = null;
                customObstacleList[0].form = "rectangle";
            }
        } else {
            customObstacleList.unshift(customObstacle);
            setObstacle();
        }
        customObstacleList[0].default = Object.assign({}, customObstacleList[0]);
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

    function isAnyRobotDown() {
        for (var i = 0; i < numRobots; i++) {
            if (isDownRobots[i]) {
                return true;
            }
        }
        return false;
    }

    function handleKeyEvent(e) {
        var keyName = e.key;
        var keyCode = e.keyCode;
        if(!selectedObject) {
            switch (keyName) {
                case "ArrowUp":
                    robots[robotIndex].pose.x += Math.cos(robots[robotIndex].pose.theta);
                    robots[robotIndex].pose.y += Math.sin(robots[robotIndex].pose.theta);
                    e.preventDefault();
                    break;
                case "ArrowLeft":
                    robots[robotIndex].pose.theta -= Math.PI / 180;
                    e.preventDefault();
                    break;
                case "ArrowDown":
                    robots[robotIndex].pose.x -= Math.cos(robots[robotIndex].pose.theta);
                    robots[robotIndex].pose.y -= Math.sin(robots[robotIndex].pose.theta);
                    e.preventDefault();
                    break;
                case "ArrowRight":
                    robots[robotIndex].pose.theta += Math.PI / 180;
                    e.preventDefault();
                    break;
                default:
                // nothing to do so far
            }
        } else {
            const shift = 5;
            switch (keyName) {
                case "ArrowUp":
                    if(selectedObject.type == "obstacle") {
                        if (selectedObject.form == "rectangle") {
                            customObstacleList[selectedObstacle].y -= shift;
                        } else if (selectedObject.form == "triangle") {
                            customObstacleList[selectedObstacle].ay -= shift;
                            customObstacleList[selectedObstacle].by -= shift;
                            customObstacleList[selectedObstacle].cy -= shift;
                        }
                        updateObstacleLayer();
                    }
                    else if(selectedObject.type == "colorBlock"){
                        colorBlockList[selectedColorBlock].y -= shift;
                        updateColorLayer();
                    }
                    e.preventDefault();
                    break;
                case "ArrowLeft":
                    if(selectedObject.type == "obstacle") {
                        if (selectedObject.form == "rectangle") {
                            customObstacleList[selectedObstacle].x -= shift;
                        } else if (selectedObject.form == "triangle") {
                            customObstacleList[selectedObstacle].ax -= shift;
                            customObstacleList[selectedObstacle].bx -= shift;
                            customObstacleList[selectedObstacle].cx -= shift;
                        }
                        updateObstacleLayer();
                    }
                    else if(selectedObject.type == "colorBlock"){
                        colorBlockList[selectedColorBlock].x -= shift;
                        updateColorLayer();
                    }
                    e.preventDefault();
                    break;
                case "ArrowDown":
                    if(selectedObject.type == "obstacle") {
                        if (selectedObject.form == "rectangle") {
                            customObstacleList[selectedObstacle].y += shift;
                        } else if (selectedObject.form == "triangle") {
                            customObstacleList[selectedObstacle].ay += shift;
                            customObstacleList[selectedObstacle].by += shift;
                            customObstacleList[selectedObstacle].cy += shift;
                        }
                        updateObstacleLayer();
                    }
                    else if(selectedObject.type == "colorBlock"){
                        colorBlockList[selectedColorBlock].y += shift;
                        updateColorLayer();
                    }
                    e.preventDefault();
                    break;
                case "ArrowRight":
                    if(selectedObject.type == "obstacle") {
                        if (selectedObject.form == "rectangle") {
                            customObstacleList[selectedObstacle].x += shift;
                        } else if (selectedObject.form == "triangle") {
                            customObstacleList[selectedObstacle].ax += shift;
                            customObstacleList[selectedObstacle].bx += shift;
                            customObstacleList[selectedObstacle].cx += shift;
                        }
                        updateObstacleLayer();
                    }
                    else if(selectedObject.type == "colorBlock"){
                        colorBlockList[selectedColorBlock].x += shift;
                        updateColorLayer();
                    }
                    e.preventDefault();
                    break;
                default:
                // nothing to do so far
            }
        }
        switch (keyCode) {
            case 17 && 67:
                copiedObject = null;
                if(selectedObject) copiedObject = JSON.parse(JSON.stringify(selectedObject));
                e.preventDefault();
                break;
            case 17 && 86:
                if(copiedObject && selectedObject) {
                    copiedObject = JSON.parse(JSON.stringify(selectedObject));
                    if(copiedObject.form === "triangle") {
                        const diffx = copiedObject.ax - mouseX;
                        const diffy = copiedObject.ay - mouseY;
                        copiedObject.ax = mouseX;
                        copiedObject.ay = mouseY;
                        copiedObject.bx -= diffx;
                        copiedObject.by -= diffy;
                        copiedObject.cx -= diffx;
                        copiedObject.cy -= diffy;
                    } else if (copiedObject.form === "rectangle"){
                        copiedObject.x = mouseX - copiedObject.w/2;
                        copiedObject.y = mouseY - copiedObject.h/2;
                    }
                    if(copiedObject.type === "obstacle") {
                        customObstacleList.unshift(copiedObject);
                        exports.obstacleList = [ground, customObstacleList];
                        updateObstacleLayer();
                        e.preventDefault();
                        break;
                    } else if(copiedObject.type === "colorBlock") {
                        colorBlockList.unshift(copiedObject);
                        updateColorLayer();
                        e.preventDefault();
                        break;
                    }
                }
                e.preventDefault();
                break;
            case 8:
                deleteSelectedObject();
                break;
            case 46:
                deleteSelectedObject();
                break;
            default:
            // nothing to do so far
        }
    }

    function disableChangeObjectButtons() {
        simChangeObjectColorButton.disabled = true;
        simChangeObjectColorButton.style.background = "#a9a9a9";
        simChangeObjectColorButton.style.color = "#646464";
        simDeleteObjectButton.disabled = true;
        simDeleteObjectButton.style.background = "#a9a9a9";
        simDeleteObjectButton.style.color = "#646464";
    }

    function enableChangeObjectButtons() {
        simChangeObjectColorButton.disabled = false;
        simChangeObjectColorButton.style.background = "#ffffff";
        simChangeObjectColorButton.style.color = "#333333";
        simDeleteObjectButton.disabled = false;
        simDeleteObjectButton.style.background = "#ffffff";
        simDeleteObjectButton.style.color = "#333333";
    }

    function handleMouseDown(e) {
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var scsq = 1;
        if (scale < 1)
            scsq = scale * scale;
        var top = $('#robotLayer').offset().top;
        var left = $('#robotLayer').offset().left;
        startX = (parseInt(X - left, 10)) / scale;
        startY = (parseInt(Y - top, 10)) / scale;
        var dx;
        var dy;
        for (var i = 0; i < numRobots; i++) {
            dx = startX - robots[i].mouse.rx;
            dy = startY - robots[i].mouse.ry;
            var boolDown = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
            isDownRobots[i] = boolDown;
            if (boolDown) {
                mouseOnRobotIndex = i;
            }
        }
        colorpicker.on('change', function(color) {
            changeColorWithColorPicker(color);
        });

        if(selectedObstacle != null) {
            var obstacleCorners = [];
            if(customObstacleList[selectedObstacle].form != "circle") {
                obstacleCorners = calculateCorners(customObstacleList[selectedObstacle]);
            } else {
                isDownObstacleCorner = checkDownCircleCorner(startX, startY,customObstacleList[selectedObstacle].x, customObstacleList[selectedObstacle].y, customObstacleList[selectedObstacle].r);
                selectedCornerObject = customObstacleList[selectedObstacle];
            }
                for(let corner_index in obstacleCorners) {
                isDownObstacleCorner = (startX > obstacleCorners[corner_index].x && startX < obstacleCorners[corner_index].x + obstacleCorners[corner_index].w && startY > obstacleCorners[corner_index].y && startY < obstacleCorners[corner_index].y + obstacleCorners[corner_index].h);
                if(isDownObstacleCorner) {
                    selectedCorner = corner_index;
                    selectedCornerObject = customObstacleList[selectedObstacle];
                    break;
                }
            }
        }
        if(selectedColorBlock != null) {
            let colorBlockCorners  = calculateCorners(colorBlockList[selectedColorBlock]);
            for(let corner_index in colorBlockCorners) {
                isDownColorBlockCorner = (startX > colorBlockCorners[corner_index].x && startX < colorBlockCorners[corner_index].x + colorBlockCorners[corner_index].w && startY > colorBlockCorners[corner_index].y && startY < colorBlockCorners[corner_index].y + colorBlockCorners[corner_index].h);
                if(isDownColorBlockCorner) {
                    selectedCorner = corner_index;
                    selectedCornerObject = colorBlockList[selectedColorBlock];
                    break;
                }
            }
        }

        for(let key in colorBlockList) {
            let colorBlock = colorBlockList.slice().reverse()[key];

            isDownColorBlock = (startX > colorBlock.x && startX < colorBlock.x + colorBlock.w && startY > colorBlock.y && startY < colorBlock.y + colorBlock.h);
            key++;
            if (isDownColorBlock && !isDownObstacleCorner && colorBlocksActivated) {
                enableChangeObjectButtons();
                selectedObstacle = null;
                selectedColorBlock = colorBlockList.length - key;
                selectedObject = colorBlockList[selectedColorBlock];
                colorBlockList.splice(selectedColorBlock, 1);
                colorBlockList.push(selectedObject);
                selectedColorBlock = colorBlockList.length-1;
                updateColorLayer();
                updateObstacleLayer();
                break;
            }
        }
        for(let key in customObstacleList) {
            let obstacle = customObstacleList.slice().reverse()[key];

            if (obstacle.form === "rectangle") {
                isDownObstacle = (startX > obstacle.x && startX < obstacle.x + obstacle.w && startY > obstacle.y && startY < obstacle.y + obstacle.h);
            } else if (obstacle.form === "triangle") {
                isDownObstacle = checkDownTriangle(startX, startY, obstacle.ax, obstacle.ay, obstacle.bx, obstacle.by, obstacle.cx, obstacle.cy);
            } else if (obstacle.form === "circle") {
                isDownObstacle = checkDownCircle(startX, startY, obstacle.x, obstacle.y, obstacle.r);
            }
            key++;
            if(isDownObstacle && !isDownColorBlockCorner) {
                enableChangeObjectButtons();
                selectedColorBlock = null;
                selectedObstacle = customObstacleList.length - key;
                selectedObject = customObstacleList[selectedObstacle];
                customObstacleList.splice(selectedObstacle, 1);
                customObstacleList.push(selectedObject);
                selectedObstacle = customObstacleList.length-1;
                updateObstacleLayer();
                break;
            }
        }

        isDownRuler = (startX > ruler.x && startX < ruler.x + ruler.w && startY > ruler.y && startY < ruler.y + ruler.h);
        checkSelection();
        if (isDownRobots || isDownObstacle || isDownObstacleCorner || isDownRuler || isDownColorBlock || isDownColorBlockCorner || isAnyRobotDown()) {
            e.stopPropagation();
        }
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
    exports.checkDownTriangle = checkDownTriangle;

    function checkDownCircle(px, py, cx, cy, r) {
            return (px - cx) * (px - cx) + (py - cy) * (py - cy) <= r * r;
    }
    exports.checkDownCircle = checkDownCircle;

    function checkDownCircleCorner(px, py, cx, cy, r) {
        return checkDownCircle(px, py, cx, cy, r) && !checkDownCircle(px, py, cx, cy, r-10);
    }
    exports.checkDownCircle = checkDownCircle;

    function calculateCorners(object) {
        if(object.r != undefined) return [];
        const shift = 10;
        let objectCorners;
        if(object.form === "rectangle") {
            objectCorners = [
                {x: (Math.round(object.x-shift)), y: (Math.round(object.y-shift) + object.h), w: shift*2, h:shift*2},
                {x: Math.round(object.x-shift), y: Math.round(object.y-shift), w: shift*2, h:shift*2},
                {x: (Math.round(object.x-shift) + object.w), y: Math.round(object.y-shift), w: shift*2, h:shift*2},
                {x: (Math.round(object.x-shift) + object.w), y: (Math.round(object.y-shift) + object.h), w: shift*2, h:shift*2}
            ];
        } else if(object.form === "triangle") {
            objectCorners = [
                {x: Math.round(object.ax-shift), y: Math.round(object.ay-shift), w: shift*2, h:shift*2},
                {x: Math.round(object.bx-shift), y: Math.round(object.by-shift), w: shift*2, h:shift*2},
                {x: Math.round(object.cx-shift), y: Math.round(object.cy-shift), w: shift*2, h:shift*2},
            ];
        }
        return objectCorners;
    }
    exports.calculateCorners = calculateCorners;

    function checkSelection() {
        if(!isDownColorBlock && !isDownObstacle && !isDownObstacleCorner && !isDownColorBlockCorner) {
            resetSelection();
            scene.drawObstacles();
        }
    }
    exports.checkSelection = checkSelection;

    function resetSelection() {
        disableChangeObjectButtons();
        colorpicker.close();
        colorPickerIsShown = false;
        selectedObject = null;
        selectedColorBlock = null;
        selectedObstacle = null;
        selectedCornerObject = null;
    }
    exports.resetSelection = resetSelection;

    function handleDoubleMouseClick(e) {
        if (numRobots > 1) {
            var X = e.clientX || e.originalEvent.touches[0].pageX;
            var Y = e.clientY || e.originalEvent.touches[0].pageY;
            var dx;
            var dy;
            var top = $('#robotLayer').offset().top;
            var left = $('#robotLayer').offset().left;
            startX = (parseInt(X - left, 10)) / scale;
            startY = (parseInt(Y - top, 10)) / scale;
            for (var i = 0; i < numRobots; i++) {
                dx = startX - robots[i].mouse.rx;
                dy = startY - robots[i].mouse.ry;
                var boolDown = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
                if (boolDown) {
                    $("#brick" + robotIndex).hide();
                    robotIndex = i;
                    $("#brick" + robotIndex).show();
                    $("#robotIndex")[0][i].selected = true;
                    break;
                }
            }
        }
    }

    function handleMouseUp(e) {
        $("#robotLayer").css('cursor', 'auto');
        if (mouseOnRobotIndex >= 0 && robots[mouseOnRobotIndex].drawWidth) {
            robots[mouseOnRobotIndex].canDraw = true;
        }
        isDownObstacle = false;
        isDownRuler = false;
        isDownColorBlock = false;
        isDownColorBlockCorner = false;
        isDownObstacleCorner = false;
        for (var i = 0; i < numRobots; i++) {
            if (isDownRobots[i]) {
                isDownRobots[i] = false;
            }
        }
        mouseOnRobotIndex = -1;
    }

    function handleMouseOut(e) {
        e.preventDefault();
        isDownObstacle = false;
        isDownRuler = false;
        isDownColorBlock = false;
        for (var i = 0; i < numRobots; i++) {
            if (isDownRobots[i]) {
                isDownRobots[i] = false;
            }
        }
        mouseOnRobotIndex = -1;
        e.stopPropagation();
    }

    function updateColorLayer() {
        scene.updateBackgrounds();
        scene.drawColorBlocks();
        scene.drawObstacles();
    }
    exports.updateColorLayer = updateColorLayer;

    function updateObstacleLayer() {
        scene.drawRuler();
        scene.drawObstacles();
    }
    exports.updateObstacleLayer = updateObstacleLayer;

    function handleMouseMove(e) {
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top;
        var left = $('#robotLayer').offset().left;
        mouseX = (parseInt(X - left, 10)) / scale;
        mouseY = (parseInt(Y - top, 10)) / scale;
        var dx;
        var dy;
        if (!isAnyRobotDown() && !isDownObstacle && !isDownRuler && !isDownColorBlock && !isDownObstacleCorner && !isDownColorBlockCorner) {
            var hoverRobot = false;
            for (var i = 0; i < numRobots; i++) {
                dx = mouseX - robots[i].mouse.rx;
                dy = mouseY - robots[i].mouse.ry;
                var tempcheckhover = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
                if (tempcheckhover) {
                    hoverRobot = true;
                    break;
                }
            }
            for(let key in customObstacleList) {
                let obstacle = customObstacleList.slice()[key];
                if (obstacle.form === "rectangle") hoverObstacle = (mouseX > obstacle.x && mouseX < obstacle.x + obstacle.w && mouseY > obstacle.y && mouseY < obstacle.y + obstacle.h);
                else if (obstacle.form === "triangle") hoverObstacle = checkDownTriangle(mouseX, mouseY, obstacle.ax, obstacle.ay, obstacle.bx, obstacle.by, obstacle.cx, obstacle.cy);
                else if (obstacle.form === "circle") {
                    hoverObstacle = checkDownCircle(mouseX, mouseY, obstacle.x, obstacle.y, obstacle.r);
                    hoverObstacleCorners = checkDownCircleCorner(mouseX, mouseY, obstacle.x, obstacle.y, obstacle.r);
                }
                let obstacleCorners = calculateCorners(obstacle);
                for (let corner_index in obstacleCorners) {
                    hoverObstacleCorners = (mouseX > obstacleCorners[corner_index].x && mouseX < obstacleCorners[corner_index].x + obstacleCorners[corner_index].w && mouseY > obstacleCorners[corner_index].y && mouseY < obstacleCorners[corner_index].y + obstacleCorners[corner_index].h);
                    if (hoverObstacleCorners) {
                        hoveringCorner = corner_index;
                        break;
                    }
                }
                if (hoverObstacle) break;
            }
            for(let key in colorBlockList) {
                let colorBlock = colorBlockList.slice()[key];
                if (colorBlock.form === "rectangle") hoverColorBlock = (mouseX > colorBlock.x && mouseX < colorBlock.x + colorBlock.w && mouseY > colorBlock.y && mouseY < colorBlock.y + colorBlock.h);
                else if (colorBlock.form === "triangle") hoverColorBlock = checkDownTriangle(mouseX, mouseY, colorBlock.ax, colorBlock.ay, colorBlock.bx, colorBlock.by, colorBlock.cx, colorBlock.cy);
                let colorBlockCorners = calculateCorners(colorBlock);
                for (let corner_index in colorBlockCorners) {
                    hoverColorBlockCorners = (mouseX > colorBlockCorners[corner_index].x && mouseX < colorBlockCorners[corner_index].x + colorBlockCorners[corner_index].w && mouseY > colorBlockCorners[corner_index].y && mouseY < colorBlockCorners[corner_index].y + colorBlockCorners[corner_index].h);
                    if (hoverColorBlockCorners) {
                        hoveringCorner = corner_index;
                        break;
                    }
                }
                if (hoverColorBlock) break;
            }
            var hoverRuler = (mouseX > ruler.x && mouseX < ruler.x + ruler.w && mouseY > ruler.y && mouseY < ruler.y + ruler.h);
            if(hoverObstacleCorners || (hoverColorBlockCorners && colorBlocksActivated)) {
                if(hoveringCorner == 0) $("#robotLayer").css('cursor', 'nesw-resize');
                else if(hoveringCorner == 1) $("#robotLayer").css('cursor', 'nw-resize');
                else if(hoveringCorner == 2) $("#robotLayer").css('cursor', 'ne-resize');
                else if(hoveringCorner == 3) $("#robotLayer").css('cursor', 'nwse-resize');
                else $("#robotLayer").css('cursor', 'ne-resize');
            } else if(hoverColorBlock && !colorBlocksActivated) {
                $("#robotLayer").css('cursor', 'not-allowed');
            } else if (hoverRobot || hoverObstacle || hoverRuler || hoverColorBlock) {
                 $("#robotLayer").css('cursor', 'pointer');
            } else {
                $("#robotLayer").css('cursor', 'auto');
            }
            return;
        }
        dx = (mouseX - startX);
        dy = (mouseY - startY);
        startX = mouseX;
        startY = mouseY;
        const minSizeObjects = 15;
        if (isAnyRobotDown()) {
            if (robots[mouseOnRobotIndex].drawWidth) {
                robots[mouseOnRobotIndex].canDraw = false;
            }
            robots[mouseOnRobotIndex].pose.xOld = robots[mouseOnRobotIndex].pose.x;
            robots[mouseOnRobotIndex].pose.yOld = robots[mouseOnRobotIndex].pose.y;
            robots[mouseOnRobotIndex].pose.x += dx;
            robots[mouseOnRobotIndex].pose.y += dy;
            robots[mouseOnRobotIndex].mouse.rx += dx;
            robots[mouseOnRobotIndex].mouse.ry += dy;
        } else if (isDownObstacle && selectedObstacle != null && !isDownObstacleCorner && customObstacleList[selectedObstacle].form === "rectangle") {
            customObstacleList[selectedObstacle].x += dx;
            customObstacleList[selectedObstacle].y += dy;
            updateObstacleLayer();
        } else if (isDownObstacle && selectedObstacle != null && !isDownObstacleCorner && customObstacleList[selectedObstacle].form === "triangle") {
            customObstacleList[selectedObstacle].ax += dx;
            customObstacleList[selectedObstacle].ay += dy;
            customObstacleList[selectedObstacle].bx += dx;
            customObstacleList[selectedObstacle].by += dy;
            customObstacleList[selectedObstacle].cx += dx;
            customObstacleList[selectedObstacle].cy += dy;
            updateObstacleLayer();
        } else if (isDownObstacle && selectedObstacle != null && !isDownObstacleCorner && customObstacleList[selectedObstacle].form === "circle") {
            customObstacleList[selectedObstacle].x += dx;
            customObstacleList[selectedObstacle].y += dy;
            updateObstacleLayer();
        } else if(isDownObstacleCorner && selectedObject == selectedCornerObject && selectedObstacle != null) {
            if(customObstacleList[selectedObstacle].form === "triangle") {
                if(selectedCorner == 0) {
                    customObstacleList[selectedObstacle].ax += dx;
                    customObstacleList[selectedObstacle].ay += dy;
                } else if(selectedCorner == 1) {
                    customObstacleList[selectedObstacle].bx += dx;
                    customObstacleList[selectedObstacle].by += dy;
                } else if(selectedCorner == 2) {
                    customObstacleList[selectedObstacle].cx += dx;
                    customObstacleList[selectedObstacle].cy += dy;
                }
            } else if(customObstacleList[selectedObstacle].form === "circle") {
                if(customObstacleList[selectedObstacle].r >= minSizeObjects) {
                    if(mouseX >= customObstacleList[selectedObstacle].x) customObstacleList[selectedObstacle].r += dx;
                    else if(mouseX < customObstacleList[selectedObstacle].x) customObstacleList[selectedObstacle].r -= dx;
                } else if(customObstacleList[selectedObstacle].r < minSizeObjects){
                    customObstacleList[selectedObstacle].r = minSizeObjects;
                }
            } else if(customObstacleList[selectedObstacle].form === "rectangle") {
                if(customObstacleList[selectedObstacle].w >= minSizeObjects && customObstacleList[selectedObstacle].h >= minSizeObjects) {
                    if (selectedCorner == 0) {
                        customObstacleList[selectedObstacle].x += dx;
                        customObstacleList[selectedObstacle].w -= dx;
                        customObstacleList[selectedObstacle].h += dy;
                    } else if (selectedCorner == 1) {
                        customObstacleList[selectedObstacle].x += dx;
                        customObstacleList[selectedObstacle].y += dy;
                        customObstacleList[selectedObstacle].w -= dx;
                        customObstacleList[selectedObstacle].h -= dy;
                    } else if (selectedCorner == 2) {
                        customObstacleList[selectedObstacle].y += dy;
                        customObstacleList[selectedObstacle].w += dx;
                        customObstacleList[selectedObstacle].h -= dy;
                    } else if (selectedCorner == 3) {
                        customObstacleList[selectedObstacle].w += dx;
                        customObstacleList[selectedObstacle].h += dy;
                    }
                } else if(customObstacleList[selectedObstacle].w < minSizeObjects){
                    if(selectedCorner == 0 || selectedCorner == 1) customObstacleList[selectedObstacle].x -= minSizeObjects-customObstacleList[selectedObstacle].w;
                    customObstacleList[selectedObstacle].w = minSizeObjects;
                } else if(customObstacleList[selectedObstacle].h < minSizeObjects) {
                    if(selectedCorner == 1 || selectedCorner == 2) customObstacleList[selectedObstacle].y -= minSizeObjects-customObstacleList[selectedObstacle].h;
                    customObstacleList[selectedObstacle].h = minSizeObjects;
                }
            }
            updateObstacleLayer();
        }else if (isDownRuler) {
            ruler.x += dx;
            ruler.y += dy;
            scene.drawRuler();
        } else if (isDownColorBlock && selectedColorBlock != null && !isDownColorBlockCorner) {
            colorBlockList[selectedColorBlock].x += dx;
            colorBlockList[selectedColorBlock].y += dy;
            updateColorLayer();
        } else if(isDownColorBlockCorner && selectedObject == selectedCornerObject && selectedColorBlock != null) {
            if(colorBlockList[selectedColorBlock].w >= minSizeObjects && colorBlockList[selectedColorBlock].h >= 10) {
                if(selectedCorner == 0) {
                    colorBlockList[selectedColorBlock].x += dx;
                    colorBlockList[selectedColorBlock].w -= dx;
                    colorBlockList[selectedColorBlock].h += dy;
                } else if(selectedCorner == 1) {
                    colorBlockList[selectedColorBlock].x += dx;
                    colorBlockList[selectedColorBlock].y += dy;
                    colorBlockList[selectedColorBlock].w -= dx;
                    colorBlockList[selectedColorBlock].h -= dy;
                } else if(selectedCorner == 2) {
                    colorBlockList[selectedColorBlock].y += dy;
                    colorBlockList[selectedColorBlock].w += dx;
                    colorBlockList[selectedColorBlock].h -= dy;
                } else if(selectedCorner == 3) {
                    colorBlockList[selectedColorBlock].w += dx;
                    colorBlockList[selectedColorBlock].h += dy;
                }
            } else if(colorBlockList[selectedColorBlock].w < minSizeObjects){
                if(selectedCorner == 0 || selectedCorner == 1) colorBlockList[selectedColorBlock].x -= minSizeObjects-colorBlockList[selectedColorBlock].w;
                colorBlockList[selectedColorBlock].w = minSizeObjects;
            } else if(colorBlockList[selectedColorBlock].h < minSizeObjects) {
                if(selectedCorner == 1 || selectedCorner == 2) colorBlockList[selectedColorBlock].y -= minSizeObjects-colorBlockList[selectedColorBlock].h;
                colorBlockList[selectedColorBlock].h = minSizeObjects;
            }
            updateColorLayer();
        }
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
            scene.drawBackground();
            scene.drawRuler();
            scene.drawObstacles();
            scene.drawColorBlocks();
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
            scene.updateBackgrounds();
            scene.drawObstacles();
            scene.drawColorBlocks();
            scene.drawRuler();
        }
    }

    function addMouseEvents() {
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
        $("#robotLayer").on("dblclick", function(e) {
            handleDoubleMouseClick(e);
        });
        $(document).on('keydown', handleKeyEvent);
        $("#robotIndex").on('change', function(e) {
            $("#brick" + robotIndex).hide();
            robotIndex = e.target.selectedIndex;
            $("#brick" + robotIndex).show();
        });
    }

    function removeMouseEvents() {
        $("#robotLayer").off();
        $("#simDiv").off();
        $("#canvasDiv").off();
        $("#simRobotModal").off();
        $("#robotIndex").off();
        $(document).off('keydown', handleKeyEvent);
    }

    function initScene() {
        scene = new Scene(imgObjectList[currentBackground], robots, customObstacleList, imgPattern, ruler, colorBlockList);
        scene.updateBackgrounds();
        scene.drawObstacles();
        scene.drawColorBlocks();
        scene.drawRuler();
        scene.drawRobots();
        scene.drawVariables();
        addMouseEvents();
        disableChangeObjectButtons();

        if(simRobotType === 'nxt') {
            colorpicker = new huebee('#colorpicker', {
                shades: 1,
                hues: 8,
                customColors: nxt_colors
            });
        } else if(simRobotType === 'mbot') {
            colorpicker = new huebee('#colorpicker', {
                shades: 1,
                hues: 8
            });
        } else {
            colorpicker = new huebee('#colorpicker', {
                shades: 1,
                hues: 8,
                customColors: ev3_colors
            });
        }
        for (var i = 0; i < numRobots; i++) {
            readyRobots[i] = true;
        }
        resizeAll();
        $(window).on("resize", resizeAll);
        $('#backgroundDiv').on("resize", resizeAll);
        render();
    }

    function getSelectedObject () {
        return selectedObject;
    }

    exports.getSelectedObject = getSelectedObject;

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


    function getSimConfig() {
        return  {
            "robotPose": robots[0].pose,
            "obstacles": customObstacleList,
            "colorBlocks": colorBlockList,
            "ruler": ruler,
            "background": currentBackground
        }
    }
    exports.getSimConfig = getSimConfig;


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
        $('#backgroundFileSelector').trigger('click'); // opening dialog
        $('#backgroundFileSelector').change(function(event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onload = (function (theFile) {
                return function (e) {
                    try {
                        resetScene();
                        const configData = JSON.parse(e.target.result);
                        robots[0].pose = configData["robotPose"];
                        customObstacleList = configData["obstacles"];
                        colorBlockList = configData["colorBlocks"];
                        ruler = configData["ruler"];
                        currentBackground = configData["background"];
                        setBackground(currentBackground, null);
                        initScene();
                        exports.obstacleList = [ground, customObstacleList];
                    } catch (ex) {
                        MSG.displayPopupMessage("Blockly.Msg.POPUP_BACKGROUND_STORAGE", Blockly.Msg.POPUP_CONFIG_UPLOAD_ERROR);
                    }
                }
            })(file);
            reader.readAsText(file);
        });
    }
    exports.importConfigData = importConfigData;


    function resetScene() {
        deleteElements();
        copiedObject = null;
        resetPose();
        removeMouseEvents();
    }
    exports.resetScene = resetScene;


    function importImage() {
        $('#backgroundFileSelector').val(null);
        $('#backgroundFileSelector').attr("accept", ".png, .jpg, .jpeg, .svg");
        $('#backgroundFileSelector').trigger('click'); // opening dialog
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
                        setBackground(imgObjectList.length - 1, setBackground);
                        initScene();
                    }

                    if (UTIL.isLocalStorageAvailable()) {
                        $('#show-message-confirm').one('shown.bs.modal', function(e) {
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
                interpreters[i].removeEvent(CONST.DEBUG_BREAKPOINT);
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
            if (block.inTask && !block.disabled && !block.getInheritedDisabled()) {
                $(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '1' }, 0);
            }
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
        window.requestAnimationFrame = function(callback, element) {
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