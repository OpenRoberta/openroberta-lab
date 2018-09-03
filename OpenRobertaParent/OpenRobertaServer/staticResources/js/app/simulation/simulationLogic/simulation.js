/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */

/**
 * @namespace SIM
 */

define([ 'exports', 'simulation.scene', 'simulation.program.eval', 'simulation.math', 'program.controller', 'robertaLogic.constants', 'simulation.program.builder', 'util', 'program.controller', 'jquery' ], function(exports, Scene, ProgramEval, SIMATH, ROBERTA_PROGRAM, CONST, BUILDER, UTIL, PROGRAM_C, $) {

    var programEval = new ProgramEval();

    var scene;
    var userProgram;
    var canvasOffset;
    var offsetX;
    var offsetY;
    var isDownRobot = false;
    var isDownObstacle = false;
    var isDownRuler = false;
    var startX;
    var startY;
    var scale = 1;
    var timerStep = 0;
    var ready;
    var canceled;

    var imgObstacle1 = new Image();
    var imgPattern = new Image();
    var imgRuler = new Image();
    var imgList = [ '/js/app/simulation/simBackgrounds/baustelle.svg', '/js/app/simulation/simBackgrounds/ruler.svg', '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.svg', '/js/app/simulation/simBackgrounds/microbitBackground.svg', '/js/app/simulation/simBackgrounds/simpleBackground.svg', '/js/app/simulation/simBackgrounds/drawBackground.svg', '/js/app/simulation/simBackgrounds/robertaBackground.svg', '/js/app/simulation/simBackgrounds/rescueBackground.svg', '/js/app/simulation/simBackgrounds/wroBackground.svg', '/js/app/simulation/simBackgrounds/mathBackground.svg' ];
    var imgListIE = [ '/js/app/simulation/simBackgrounds/baustelle.png', '/js/app/simulation/simBackgrounds/ruler.png', '/js/app/simulation/simBackgrounds/wallPattern.png', '/js/app/simulation/simBackgrounds/calliopeBackground.png', '/js/app/simulation/simBackgrounds/microbitBackground.png', '/js/app/simulation/simBackgrounds/simpleBackground.png', '/js/app/simulation/simBackgrounds/drawBackground.png', '/js/app/simulation/simBackgrounds/robertaBackground.png', '/js/app/simulation/simBackgrounds/rescueBackground.png', '/js/app/simulation/simBackgrounds/wroBackground.png', '/js/app/simulation/simBackgrounds/mathBackground.png' ];
    var imgObjectList = [];

    function preloadImages() {
        if (isIE()) {
            imgList = imgListIE;
        }
        var i = 0;
        for (i = 0; i < imgList.length; i++) {
            if (i == 0) {
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
        if (localStorage.getItem("customBackground") !== null) {
            var dataImage = localStorage.getItem('customBackground');
            imgObjectList[i - 3] = new Image();
            imgObjectList[i - 3].src = "data:image/png;base64," + dataImage;
        }
    }
    preloadImages();

    var currentBackground = 2;

    function setBackground(num, callback) {
        if (num == undefined) {
            setObstacle();
            setRuler();
            if(!multipleSwitch){
                scene = new Scene(imgObjectList[currentBackground], robot, imgPattern, ruler);
            }else{
                scene = new Scene(imgObjectList[currentBackground], robots, imgPattern, ruler);
            }
//            scene = new Scene(imgObjectList[currentBackground], robot, imgPattern, ruler);
            scene.updateBackgrounds();
            scene.drawObjects();
            scene.drawRuler();
            reloadProgram();
            resizeAll();

            return currentBackground;
        }
        setPause(true);
        $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
        if (num === -1) {
            currentBackground += 1;
            if (currentBackground >= imgObjectList.length) {
                currentBackground = 2;
            }
        } else {
            currentBackground = num;
        }
        if(!multipleSwitch){
            var debug = robot.debug;
        }else{
            var debug = robots[0].debug;
        }
//        var debug = robot.debug;
        var moduleName = 'simulation.robot.' + simRobotType;
        require([ moduleName ], function(ROBOT) {
            if(!multipleSwitch){
                createRobot(ROBOT);
                robot.debug = debug;
            }else{
                createRobots(ROBOT, numprogs);
                for(var i=0;i<robots.length;i++){
                    robots[i].debug = debug;
                }
            }
//            createRobot(ROBOT);
//            robot.debug = debug;
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
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
            } else {
                $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
            }
            pause = value;
        }
        if(!multipleSwitch){
            if (robot.left)
                robot.left = 0;
            if (robot.right)
                robot.right = 0;
        }else{
            for(var i=0;i<robots.length;i++){
                if(robots[i].left){
                    robots[i].left = 0;
                }
                if(robots[i].right){
                    robots[i].right = 0;
                }
            }
        }

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
    
    function resetPose() {
        if(!multipleSwitch){
            if (robot.resetPose) {
                robot.resetPose();
            }
            if (robot.time) {
                robot.time = 0;
            }
        }else{
            for(var i=0;i<numprogs;i++){
                if (robots[i].resetPose) {
                    robots[i].resetPose();
                }
                if (robots[i].time) {
                    robots[i].time = 0;
                }
            }
        }

    }
    exports.resetPose = resetPose;

    function stopProgram() {
        setPause(true);
        if(!multipleSwitch){
            robot.reset();
        }else{
            for(var i=0;i<numprogs;i++){
                robots[i].reset();
            }
            removeKeyEvents();
        }
        //scene.updateBackgrounds();
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
        h : 500,
        isParallelToAxis : true
    };

    var obstacle = {
        x : 0,
        y : 0,
        xOld : 0,
        yOld : 0,
        w : 0,
        h : 0,
        wOld : 0,
        hOld : 0,
        isParallelToAxis : true
    }; 
    var obslist= [ground, obstacle];
    /*
     * The below code if uncommented will give multiple obstacles
     * 
     * 
    for(var i=0;i<7;i++){
        var tempobs= {
                x : 0,
                y : 0,
                xOld : 0,
                yOld : 0,
                w : 0,
                h : 0,
                wOld : 0,
                hOld : 0,
                isParallelToAxis : true
            };
        obslist.push(tempobs);
    }
    */
    var hoverindex =1;
    
    exports.hoverindex = hoverindex;
    
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
    
//    var obslist = [ ground, obstacle, obstacle1 ];
    exports.obstacleList = obslist;
// render stuff
    var globalID;
    var robot;
    var simRobotType;
    var ROBOT;

    function init(program, refresh, robotType) {
        console.log("when was simulation.init executed");
        reset = false;
        simRobotType = robotType;
        userProgram = program;
        multipleSwitch = false;
        if (robotType.indexOf("calliope") >= 0) {
            currentBackground = 0;
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead').hide();
        } else if (robotType === 'microbit') {
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead').hide();
            currentBackground = 1;
        } else if (currentBackground == 0 || currentBackground == 1) {
            currentBackground = 2;
        }
        if (currentBackground > 1) {
            if (isIE() || isEdge()) { // TODO IE and Edge: Input event not firing for file type of input
                $('.dropdown.sim, .simScene').show();
                $('#simImport').hide();
            } else {
                $('.dropdown.sim, .simScene, #simImport, #simResetPose').show();
            }
            if ($('#device-size').find('div:visible').first().attr('id')) {
                $('#simButtonsHead').show();
            }
        }
        var blocklyProgram = BUILDER.build(userProgram);
        programEval.initProgram(blocklyProgram);
        if (refresh) {
            console.log("was refresh executed? ");
            require([ 'simulation.robot.' + simRobotType ], function(reqRobot) {
                createRobot(reqRobot);
                robot.reset();
                robot.resetPose();
                ready = false;
                removeMouseEvents();
                canceled = false;
                isDownRobot = false;
                isDownObstacle = false;
                isDownRuler = false;
                stepCounter = 0;
                pause = true;
                info = false;
                setObstacle();
                setRuler();
                initScene();
            });

        } else {
            if (robot.endless)
                robot.reset();
            reloadProgram();
        }
    }
    exports.init = init;
    
    var robots = [];
    var readymultiple =[];
    var isDownRobotmul = [];
    var programEvals;
    var sensorValuesMultiple = [];
    var numprogs;
    var multipleSwitch = false;
    var storedPrograms;
    var isDownRobots = [];
    var mouseonrobot = -1;
    var robotOfConsideration = -1;
//    exports.multipleSwitch = multipleSwitch;
    function initMultiple(programs, refresh, robotType){
        
        mouseonrobot =-1;
        storedPrograms = programs;
        multipleSwitch = true;
        numprogs = programs.length;
        reset = false;
        simRobotType = robotType;
        userProgram = programs;
        robotOfConsideration =0;
        console.log("simRobottype is ", simRobotType);
        if (robotType.indexOf("calliope") >= 0) {
            currentBackground = 0;
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead').hide();
        } else if (robotType === 'microbit') {
            $('.dropdown.sim, .simScene, #simImport, #simResetPose, #simButtonsHead').hide();
            currentBackground = 1;
        } else if (currentBackground == 0 || currentBackground == 1) {
            currentBackground = 2;
        }
        if (currentBackground > 1) {
            if (isIE() || isEdge()) { // TODO IE and Edge: Input event not firing for file type of input
                $('.dropdown.sim, .simScene').show();
                $('#simImport').hide();
            } else {
                $('.dropdown.sim, .simScene, #simImport, #simResetPose').show();
            }
            if ($('#device-size').find('div:visible').first().attr('id')) {
                $('#simButtonsHead').show();
            }
        }
        var blocklyprograms = programs.map(x => BUILDER.build(x.result.javaScriptProgram));
        programEvals = programs.map(x => new ProgramEval());
        isDownRobots = [];
        for(var i=0;i<numprogs;i++){
            programEvals[i].initProgram(blocklyprograms[i]);
            isDownRobots.push(false);
        }
        if (refresh) {
            robots = [];
//            programEvals = null;
            readymultiple = [];
            isDownRobotmul = [];
            
            console.log("was refresh executed? ");
            require([ 'simulation.robot.' + simRobotType ], function(reqRobot) {
                createRobots(reqRobot, numprogs);
                for(var i=0;i<numprogs;i++){
                    robots[i].reset();
                    robots[i].resetPose();
                    readymultiple.push(false);
                    isDownRobotmul.push(false);
                    
                }
                removeMouseEvents();
                canceled = false;
                isDownObstacle = false;
                isDownRuler = false;
                stepCounter = 0;
                pause= true;
                ready=false;
                info = false;
                setObstacle();
                setRuler();
                initSceneMultiple();
            });

        } else {
            for(var i=0;i<numprogs;i++){
                if(robots[i].endless){
                    robots[i].reset();
                }
            }
            reloadProgram();
        }
        
    }
    exports.initMultiple = initMultiple;
    
    function startMultiple(refresh, robotType){
        initMultiple(storedPrograms, refresh, robotType);
    }
    exports.startMultiple = startMultiple;
    
    function isMultiple(){
        return multipleSwitch;
    }
    exports.isMultiple = isMultiple;

    function getRobotOfConsideration(){
        if($("#robotOfConsideration")[0]==null){
            return 0;
        }else{
            var temp = $("#robotOfConsideration")[0].selectedIndex;
            return temp;

        }
//        var temp = $("#robotOfConsideration")[0].selectedIndex;
////        return 0;
//        return temp;
    }
    exports.getRobotOfConsideration = getRobotOfConsideration;
    
    function cancel() {
        //$(window).off("resize");
        canceled = true;
        removeMouseEvents();
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
            actionValues = programEval.step(sensorValues);
        } else if (programEval.getProgram().isTerminated() && !pause && !robot.endless) {
            setPause(true);
            robot.reset();
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
        reset = robot.buttons.Reset;
        sensorValues = scene.updateSensorValues(!pause);
        scene.drawRobot();
    }
/*
 * The below Colors are picked from the toolkit and should be used to color the robots
 */
    const colorsAdmissible = 
        [
            [242, 148, 0],
            [143, 164, 2],
            [235, 106, 10],
            [51, 184, 202],
            [0, 90, 148],
            [186, 204, 30],
            [235, 195, 0],
            [144, 133, 186]
        ];    
    function renderMultiple(){
        if (canceled) {
            cancelAnimationFrame(globalID);
            return;
        }
        var actionValuesarr = [];
        for(var i =0;i<numprogs;i++){
            actionValuesarr.push({});
        }
        globalID = requestAnimationFrame(renderMultiple);
        var now = new Date().getTime();
        dt = now - (time || now);
        dt /= 1000;
        time = now;
        var temprobotOfConsideration = robotOfConsideration;
        robotOfConsideration = getRobotOfConsideration();
        if(temprobotOfConsideration!=robotOfConsideration){
            var blockxml = userProgram[getRobotOfConsideration()].programText;
            PROGRAM_C.programToBlocklyWorkspace(blockxml);
            $("#robotOfConsideration").css('background-color',robots[robotOfConsideration].geom.color);
        }
        stepCounter += 1;
        addKeyEvents();
        for(var i=0;i<numprogs;i++){
            if (!programEvals[i].getProgram().isTerminated() && !pause && !reset) {
                actionValuesarr[i] = programEvals[i].step(sensorValuesMultiple[i]);
            } else if (isAllTerminated() && !pause && !robots[i].endless) {
                setPause(true);
                robots[i].reset();
            } else if (reset && !pause) {
                reset = false;
                robots[i].buttons.Reset = false;
                removeMouseEvents();
                setPause(true);
                robots[i].reset();
                scene.drawRobots();
                // some time to cancel all timeouts
                setTimeout(function() {
                    initMultiple(userProgram, false, simRobotType);
                    addMouseEvents();
                }, 205);
                setTimeout(function() {
                    //delete robot.button.Reset;
                    setPause(false);
                }, 1000);
            }
            robots[i].update(actionValuesarr[i]);
        }   
        reset = robots[0].buttons.Reset;
        sensorValuesMultiple = scene.updateSensorValuesMultiple(!pause);
        scene.drawRobots();
    }
    
    function isAllTerminated(){
        for(var i = 0; i<programEvals.length;i++){
            if(!programEvals[i].getProgram().isTerminated()){
                return false;
            }
        }
        return true;
    }

    function reloadProgram() {
        $('.simForward').removeClass('typcn-media-pause');
        $('.simForward').addClass('typcn-media-play');
        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
        $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
    }

    function setObstacle() {
        console.log("currentBackground is "+currentBackground);
        if (currentBackground == 3) {
            obstacle.x = 500;
            obstacle.y = 250;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = null;
            obstacle.color = "#33B8CA";
        } else if (currentBackground == 2) {
            obstacle.x = 580;
            obstacle.y = 290;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = null;
            obstacle.color = "#33B8CA";
            /*
             * The below commentated code if uncommented will generate multiple obstacles
             * 
            for(var i=1;i<obslist.length; i++){
                var batchx;
                var prevy;
                if(i===1){
                    obslist[i].x = 500;
                    obslist[i].y = 20;
                    obslist[i].color = "white";
                    batchx = 500;
                }else{
                    if(i%11===0){
                        batchx += 140;
                        prevy = 20;
                        obslist[i].y = 20;
                    }else{
                        prevy = obslist[i-1].y;
                        obslist[i].y = prevy + 40;
                    }
                    obslist[i].x = batchx + Math.random()*60 -30;
                    var letters = '0123456789ABCDEF';
                    var tempcol= "#";
                    for (var j = 0; j < 6; j++) {
                        tempcol += letters[Math.floor(Math.random() * 16)];
                      }
                    obslist[i].color = tempcol;
                }
                obslist[i].w = 30;
                obslist[i].h = 30;
                obslist[i].img = null;
            }
            */
        } else if (currentBackground == 4) {
            obstacle.x = 500;
            obstacle.y = 260;
            obstacle.w = 100;
            obstacle.h = 100;
            obstacle.img = imgObstacle1;
            obstacle.color = null;
        } else if (currentBackground == 7) {
            obstacle.x = 0;
            obstacle.y = 0;
            obstacle.w = 0;
            obstacle.h = 0;
            obstacle.color = null;
        } else if (currentBackground == 0) {
            obstacle.x = 0;
            obstacle.y = 0;
            obstacle.w = 0;
            obstacle.h = 0;
            obstacle.color = null;
            obstacle.img = null;
        } else if (currentBackground == 1) {
            obstacle.x = 0;
            obstacle.y = 0;
            obstacle.w = 0;
            obstacle.h = 0;
            obstacle.color = null;
            obstacle.img = null;
        } else if (currentBackground == 5) {
            obstacle.x = 505;
            obstacle.y = 405;
            obstacle.w = 20;
            obstacle.h = 20;
            obstacle.color = "#33B8CA";
            obstacle.img = null;
        } else if (currentBackground == 6) {
            obstacle.x = 425;
            obstacle.y = 254;
            obstacle.w = 50;
            obstacle.h = 50;
            obstacle.color = "#009EE3";
            obstacle.img = null;
        } else {
            var x = imgObjectList[currentBackground].width - 50;
            var y = imgObjectList[currentBackground].height - 50;
            obstacle.x = x;
            obstacle.y = y;
            obstacle.w = 50;
            obstacle.h = 50;
            obstacle.color = "#33B8CA";
            obstacle.img = null;
        }
        console.log("what is obstacle list");
        console.log(obslist);
    }

    function setRuler() {
        if (currentBackground == 4) {
            ruler.x = 430;
            ruler.y = 400;
            ruler.w = 300;
            ruler.h = 30;
            ruler.img = imgRuler;
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
    
    function isAnyRobotDown(){
        if(!multipleSwitch){
            return false;
        }
        for(var i=0;i<numprogs;i++){
            if(isDownRobots[i]){
                return true;
            }
        }
        return false;
    }
    function addKeyEvents(){
        if(multipleSwitch){
            document.addEventListener('keydown', (event) => {
                const keyName = event.key;
                if(keyName=== "ArrowUp" && multipleSwitch){
                    robots[getRobotOfConsideration()].pose.theta -= Math.PI/18000;
                }
                if(keyName=== "ArrowDown" && multipleSwitch){
                    robots[getRobotOfConsideration()].pose.theta += Math.PI/18000;
                }
//                console.log('keydown event\n\n' + 'key: ' + keyName);
              });
//            document.addEventListener('keyup', (event) => {
//                document.off
//              });
//            
        }
    }
    function removeKeyEvents(){
        document.off('keydown');
    }
    function handleMouseDown(e) {
        // e.preventDefault();
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
        if(multipleSwitch){
            for(var i=0;i<numprogs;i++){
                dx = startX - robots[i].mouse.rx;
                dy = startY - robots[i].mouse.ry;
                var boolDown = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
                isDownRobots[i] = boolDown;
                if(boolDown){
                    mouseonrobot = i;
                }
            }
        }else{
            dx = startX - robot.mouse.rx;
            dy = startY - robot.mouse.ry;
            isDownRobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
        }
        
        for(var i=0;i<obslist.length;i++){
            isDownObstacle = (startX > obslist[i].x && startX < obslist[i].x + obslist[i].w && startY > obslist[i].y && startY < obslist[i].y + obslist[i].h);
            if(isDownObstacle){
                break;
            }
        }
//        isDownObstacle = (startX > obslist[i].x && startX < obslist[i].x + obslist[i].w && startY > obslist[i].y && startY < obslist[i].y + obslist[i].h);
        isDownRuler = (startX > ruler.x && startX < ruler.x + ruler.w && startY > ruler.y && startY < ruler.y + ruler.h);
        if (isDownRobot || isDownObstacle || isDownRuler || isAnyRobotDown()) {
            e.stopPropagation();
        }
    }
    
    function handleDoubleMouseClick(e){
        if(multipleSwitch){
            var X = e.clientX || e.originalEvent.touches[0].pageX;
            var Y = e.clientY || e.originalEvent.touches[0].pageY;
            var dx;
            var dy;
            var top = $('#robotLayer').offset().top;
            var left = $('#robotLayer').offset().left;
            startX = (parseInt(X - left, 10)) / scale;
            startY = (parseInt(Y - top, 10)) / scale;
            for(var i=0;i<numprogs;i++){
                dx = startX - robots[i].mouse.rx;
                dy = startY - robots[i].mouse.ry;
                var boolDown = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
//                isDownRobots[i] = boolDown;
                if(boolDown){
                    $("#robotOfConsideration")[0][i].selected = true;
                    break;
                }
            }
        }else{
            //do nothing
        }
    }
    
    function handleMouseUp(e) {
        $("#robotLayer").css('cursor', 'auto');
        if(!multipleSwitch){
            if (robot.drawWidth) {
                robot.canDraw = true;
            }
            isDownRobot = false;
        }

 
        isDownObstacle = false;
        isDownRuler = false;
        if(multipleSwitch){
            for(var i=0;i<numprogs;i++){
                if(isDownRobots[i]){
                    isDownRobots[i] = false;
                }
            }
            mouseonrobot = -1;
        }
    }

    function handleMouseOut(e) {
        e.preventDefault();
        isDownObstacle = false;
        isDownRuler = false;
        if(multipleSwitch){
            for(var i=0;i<numprogs;i++){
                if(isDownRobots[i]){
                    isDownRobots[i] = false;
                }
            }
            mouseonrobot = -1;
        }else{
            isDownRobot = false;
        }
        e.stopPropagation();
    }

    function handleMouseMove(e) {
        
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top;
        var left = $('#robotLayer').offset().left;
        mouseX = (parseInt(X - left, 10)) / scale;
        mouseY = (parseInt(Y - top, 10)) / scale;
        if(!multipleSwitch){
            var dx = mouseX - robot.mouse.rx;
            var dy = mouseY - robot.mouse.ry;
            if (!isDownRobot && !isDownObstacle && !isDownRuler) {
                var hoverRobot = (dx * dx + dy * dy < robot.mouse.r * robot.mouse.r);
                var hoverRuler = (mouseX > ruler.x && mouseX < ruler.x + ruler.w && mouseY > ruler.y && mouseY < ruler.y + ruler.h);
                if (hoverRobot || hoverRuler)
                    $("#robotLayer").css('cursor', 'pointer');
                var hoverObstacle;
                for(var i=1;i<obslist.length;i++){
                    hoverObstacle = (mouseX > obslist[i].x && mouseX < obslist[i].x + obslist[i].w && mouseY > obslist[i].y && mouseY < obslist[i].y + obslist[i].h);
                    if(hoverObstacle){
                        $("#robotLayer").css('cursor', 'pointer');
                        hoverindex =i;
//                        console.log("hoverindex now is "+ i);
                        return;
                    }
                }
                $("#robotLayer").css('cursor', 'auto');
                return;
            }
            

            $("#robotLayer").css('cursor', 'pointer');
            dx = (mouseX - startX);
            dy = (mouseY - startY);
            startX = mouseX;
            startY = mouseY;
            if (isDownRobot) {
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
//                console.log("when does this isDownObstacle occur");
//                console.log(hoverindex);
                obslist[hoverindex].x += dx;
                obslist[hoverindex].y += dy;
                scene.drawObjects();
            } else if (isDownRuler) {
                ruler.x += dx;
                ruler.y += dy;
                scene.drawRuler();
            }
        }else if(multipleSwitch){
            var dx;
            var dy;
            if (!isAnyRobotDown() && !isDownObstacle && !isDownRuler) {
                var hoverRobot=false;
                for(var i =0;i<numprogs;i++){
                    dx = mouseX - robots[i].mouse.rx;
                    dy = mouseY - robots[i].mouse.ry;
                    var tempcheckhover = (dx * dx + dy * dy < robots[i].mouse.r * robots[i].mouse.r);
                    if(tempcheckhover){
                        hoverRobot = true;
                        break;
                    }
                }   
                var hoverRuler = (mouseX > ruler.x && mouseX < ruler.x + ruler.w && mouseY > ruler.y && mouseY < ruler.y + ruler.h);
                if (hoverRobot || hoverRuler)
                    $("#robotLayer").css('cursor', 'pointer');
                var hoverObstacle;
                for(var i=1;i<obslist.length;i++){
                    hoverObstacle = (mouseX > obslist[i].x && mouseX < obslist[i].x + obslist[i].w && mouseY > obslist[i].y && mouseY < obslist[i].y + obslist[i].h);
                    if(hoverObstacle){
                        $("#robotLayer").css('cursor', 'pointer');
                        hoverindex =i;
//                        console.log("hoverindex now is "+ i);
                        return;
                    }
                }
                $("#robotLayer").css('cursor', 'auto');
                return;
            }
            $("#robotLayer").css('cursor', 'pointer');
            dx = (mouseX - startX);
            dy = (mouseY - startY);
            startX = mouseX;
            startY = mouseY;
            if(isAnyRobotDown()){
                if (robots[mouseonrobot].drawWidth) {
                    robots[mouseonrobot].canDraw = false;
                }
                robots[mouseonrobot].pose.xOld = robots[mouseonrobot].pose.x;
                robots[mouseonrobot].pose.yOld = robots[mouseonrobot].pose.y;
                robots[mouseonrobot].pose.x += dx;
                robots[mouseonrobot].pose.y += dy;
                robots[mouseonrobot].mouse.rx += dx;
                robots[mouseonrobot].mouse.ry += dy;
            } else if (isDownObstacle) {
//                console.log("when does this isDownObstacle occur");
//                console.log(hoverindex);
                obslist[hoverindex].x += dx;
                obslist[hoverindex].y += dy;
                scene.drawObjects();
            } else if (isDownRuler) {
                ruler.x += dx;
                ruler.y += dy;
                scene.drawRuler();
            }
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
            scene.drawObjects();
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
            ground.w = imgObjectList[currentBackground].width;// + 20;
            ground.h = imgObjectList[currentBackground].height;// + 20;
            var scaleX = scene.playground.w / (ground.w + 20);
            var scaleY = scene.playground.h / (ground.h + 20);
            scale = Math.min(scaleX, scaleY) - 0.05;
            scene.updateBackgrounds();
            scene.drawObjects();
            scene.drawRuler();
        }
    }

    function addMouseEvents() {
        $("#robotLayer").on('mousedown touchstart', function(e) {
            if(multipleSwitch){
                handleMouseDown(e);
            }else{
                if (robot.handleMouseDown)
                    robot.handleMouseDown(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
                else
                    handleMouseDown(e);
            }

        });
        $("#robotLayer").on('mousemove touchmove', function(e) {
            if(multipleSwitch){
                handleMouseMove(e);
            }else{
                if (robot.handleMouseMove)
                    robot.handleMouseMove(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
                else
                    handleMouseMove(e);
            }

        });
        $("#robotLayer").mouseup(function(e) {
            if(multipleSwitch){
                handleMouseUp(e);
            }else{
                if (robot.handleMouseUp)
                    robot.handleMouseUp(e, offsetX, offsetY, scale, scene.playground.w / 2, scene.playground.h / 2);
                else
                    handleMouseUp(e);
            }

        });
        $("#robotLayer").on('mouseout touchcancel', function(e) {
            if(multipleSwitch){
                handleMouseOut(e);
            }else{
                if (robot.handleMouseOut)
                    robot.handleMouseOut(e, offsetX, offsetY, scene.playground.w / 2, scene.playground.h / 2);
                else
                    handleMouseOut(e);
            }

        });
        $("#simDiv").on('wheel mousewheel touchmove', function(e) {
            handleMouseWheel(e);
        });
        $("#canvasDiv").draggable();
        $("#robotLayer").on("dblclick", function(e){
            handleDoubleMouseClick(e);
        });
    }

    function removeMouseEvents() {
        $("#robotLayer").off();
        $("#simDiv").off();
        $("#canvasDiv").off();
    }

    function initScene() {
        scene = new Scene(imgObjectList[currentBackground], robot, imgPattern, ruler);
        scene.updateBackgrounds();
        scene.drawObjects();
        scene.drawRuler();
        scene.drawRobot();
        addMouseEvents();
        ready = true;
        resizeAll();
        $(window).on("resize", resizeAll);
        $('#backgroundDiv').on("resize", resizeAll);
        render();
    }
    
    
    function initSceneMultiple(){
        scene = new Scene(imgObjectList[currentBackground], robots, imgPattern, ruler);
        scene.updateBackgrounds();
        scene.drawObjects();
        scene.drawRuler();
        scene.drawRobots();
        addMouseEvents();
        for(var i=0;i<numprogs;i++){
            readymultiple[i] = true;
        }
        ready=true;
        resizeAll();
        $(window).on("resize", resizeAll);
        $('#backgroundDiv').on("resize", resizeAll);
        renderMultiple();
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

    function isEdge() {
        var ua = window.navigator.userAgent;
        var edge = ua.indexOf('Edge');
        return edge > -1;
    }

    function importImage() {
        var input = $(document.createElement('input'));
        input.attr("type", "file");
        input.attr("accept", ".png, .jpg, .jpeg, .svg");
        input.trigger('click'); // opening dialog
        input.change(function(event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onload = function(event) {
                var img = new Image();
                img.onload = function() {
                    var canvas = document.createElement("canvas");
                    var scale = 1;
                    if (img.height > 800) {
                        scale = 800.0 / img.height;
                    }
                    canvas.width = img.width * scale;
                    canvas.height = img.height * scale;
                    var ctx = canvas.getContext("2d");
                    ctx.scale(scale, scale);
                    ctx.drawImage(img, 0, 0);
                    var dataURL = canvas.toDataURL("image/png");
                    localStorage.setItem("customBackground", dataURL.replace(/^data:image\/(png|jpg);base64,/, ""));
                    var dataImage = localStorage.getItem('customBackground');
                    var image = new Image();
                    image.src = "data:image/png;base64," + dataImage;
                    imgObjectList[imgObjectList.length] = image;
                    setBackground(imgObjectList.length - 1, setBackground);
                    initScene();
                }
                img.src = reader.result;
            }
            reader.readAsDataURL(file);
        })
    }
    exports.importImage = importImage;

    function arrToRgb(values) {
        return 'rgb(' + values.join(', ') + ')';
    }
    function createRobots(reqRobot, numprogs){
        robots = [];
        var posvec = []
        for(var i=0;i<numprogs;i++){
            posvec.push(200 + 60*(Math.floor((i+1)/2))*((-1)**(i)));
        }
        posvec.sort(function(a, b){return a - b});
        for(var i=0; i<numprogs;i++){
            var temprobot = new reqRobot({
                x : 240,
                y : posvec[i],
                theta : 0,
                xOld : 240,
                yOld : posvec[i],
                transX : 0,
                transY : 0
            });
            temprobot.savedName = userProgram[i].savedName;
            temprobot.canDraw = false;
            /*
             * The below code gives random color to robots
             * 
            var letters = '0123456789ABCDEF';
            if(i!=0){
                var tempcol= "#";
                for (var j = 0; j < 6; j++) {
                    tempcol += letters[Math.floor(Math.random() * 8)+8];
                }
                temprobot.geom.color = tempcol;
                temprobot.touchSensor.color = tempcol;
            }
            */
            if(i!=0){
                var tempcolor = arrToRgb(colorsAdmissible[((i-1)%(colorsAdmissible.length))]);
                temprobot.geom.color = tempcolor;
                temprobot.touchSensor.color = tempcolor;
                
            }
            robots.push(temprobot);
        }
    }
    
    function createRobot(reqRobot) {
        if (currentBackground == 2) {
            robot = new reqRobot({
                x : 240,
                y : 200,
                theta : 0,
                xOld : 240,
                yOld : 200,
                transX : 0,
                transY : 0
            });
            robot.canDraw = false;
        } else if (currentBackground == 3) {
            robot = new reqRobot({
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
        } else if (currentBackground == 4) {
            robot = new reqRobot({
                x : 70,
                y : 104,
                theta : 0,
                xOld : 70,
                yOld : 104,
                transX : 0,
                transY : 0
            });
            robot.canDraw = false;
        } else if (currentBackground == 5) {
            robot = new reqRobot({
                x : 400,
                y : 50,
                theta : 0,
                xOld : 400,
                yOld : 50,
                transX : 0,
                transY : 0
            });
            robot.canDraw = false;
        } else if (currentBackground == 6) {
            robot = new reqRobot({
                x : 800,
                y : 440,
                theta : -Math.PI / 2,
                xOld : 800,
                yOld : 440,
                transX : 0,
                transY : 0
            });
            robot.canDraw = false;
        } else if (currentBackground == 7) {
            var cx = imgObjectList[currentBackground].width / 2.0 + 10;
            var cy = imgObjectList[currentBackground].height / 2.0 + 10;
            robot = new reqRobot({
                x : cx,
                y : cy,
                theta : 0,
                xOld : cx,
                yOld : cy,
                transX : -cx,
                transY : -cy
            });
            robot.canDraw = true;
            robot.drawColor = "#ffffff";
            robot.drawWidth = 1;
        } else {
            var cx = imgObjectList[currentBackground].width / 2.0 + 10;
            var cy = imgObjectList[currentBackground].height / 2.0 + 10;
            robot = new reqRobot({
                x : cx,
                y : cy,
                theta : 0,
                xOld : cx,
                yOld : cy,
                transX : 0,
                transY : 0
            });
            robot.canDraw = false;
        }
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
