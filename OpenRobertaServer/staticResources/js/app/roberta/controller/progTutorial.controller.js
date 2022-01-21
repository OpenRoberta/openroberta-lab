define(["require", "exports", "message", "log", "guiState.controller", "program.controller", "robot.controller", "import.controller", "blockly", "simulation.simulation", "jquery"], function (require, exports, MSG, LOG, GUISTATE_C, PROG_C, ROBOT_C, IMPORT_C, Blockly, SIM, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.loadFromTutorial = exports.init = void 0;
    var INITIAL_WIDTH = 0.5;
    var blocklyWorkspace;
    var tutorialList;
    var tutorial;
    var step = 0;
    var maxSteps = 0;
    var credits = [];
    var maxCredits = [];
    var quiz = false;
    var configData = {};
    var TIMEOUT = 25000;
    var myTimeoutID;
    var tutorialId;
    var initTutorial;
    var noTimeout = true;
    function init() {
        tutorialList = GUISTATE_C.getListOfTutorials();
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initEvents();
    }
    exports.init = init;
    function initEvents() {
        $('.menu.tutorial').on('click', function (event) {
            startTutorial(event.target.id);
        });
        $('#tutorialButton').on('click touchend', function () {
            toggleTutorial();
            return false;
        });
        $('#helpTutorial').on('click', function () {
            clearTimeout(myTimeoutID);
            $('#tutorialStartView').modal({
                backdrop: 'static',
                keyboard: false,
                show: true,
            });
            $('#volksbotStart').hide('slow'); //prop('disabled', true);
            $('#startVideo').one('ended', videoEnd);
            $('#startVideo')[0].play();
        });
    }
    function loadFromTutorial(tutId, opt_init) {
        // initialize this tutorial
        if (opt_init) {
            initTutorial = tutId;
        }
        clearTimeout(myTimeoutID);
        tutorialId = tutId;
        tutorial = tutorialList[tutId];
        step = 0;
        maxSteps = 0;
        credits = [];
        maxCredits = [];
        quiz = false;
        configData = {};
        noTimeout = true;
        myTimeoutID;
        clearTimeout(myTimeoutID);
        blocklyWorkspace.options.maxBlocks = null;
        if (tutorial) {
            ROBOT_C.switchRobot(tutorial.robot, undefined, startTutorial);
        }
        function startTutorial() {
            $('#tabProgram').trigger('click');
            if (tutorial.initXML) {
                IMPORT_C.loadProgramFromXML('egal', tutorial.initXML);
            }
            maxSteps = tutorial.step.length;
            step = 0;
            credits = [];
            maxCredits = [];
            quiz = false;
            for (var i = 0; i < tutorial.step.length; i++) {
                if (tutorial.step[i].quiz) {
                    quiz = true;
                    break;
                }
            }
            $('#tutorial-list').empty();
            // create this tutorial navigation
            for (var i = 0; i < tutorial.step.length; i++) {
                $('#tutorial-list').append($('<li>')
                    .attr('class', 'step')
                    .append($('<a>')
                    .attr({
                    href: '#',
                })
                    .append(i + 1)));
            }
            $('#tutorial-list li:last-child').addClass('last');
            $('#tutorial-header').html(tutorial.name);
            // prepare the view
            $('#tutorial-navigation').fadeIn(750);
            $('#head-navigation').hide();
            $('#tutorial-list :first-child').addClass('active');
            $('#tutorialButton').show();
            $('.blocklyToolboxDiv>.levelTabs').addClass('invisible');
            initStepEvents();
            createInstruction();
            if (tutorial.instructions) {
                openTutorialView();
            }
            showOverview();
            //videoEnd();
        }
    }
    exports.loadFromTutorial = loadFromTutorial;
    function reloadTutorial() {
        clearTimeout(myTimeoutID);
        var i = tutorialId.slice(-1);
        var newTutorialId = tutorialId.slice(0, tutorialId.length - 1) + (parseInt(i) + 1);
        tutorialId = tutorialList[newTutorialId] ? newTutorialId : initTutorial;
        tutorialController.loadFromTutorial(tutorialId);
    }
    function initStepEvents() {
        if (tutorial.instructions) {
            $('#tutorial-list.nav li.step a').on('click', function () {
                ckly;
                Blockly.hideChaff();
                step = $(this).text() - 2;
                nextStep();
                openTutorialView();
            });
            $('#tutorialEnd').one('click', function () {
                exitTutorial();
            });
        }
        else {
            blocklyWorkspace.removeChangeListener(blocklyListener);
            blocklyWorkspace.addChangeListener(blocklyListener);
            $('#simControl').off('simTerminated', simTerminatedListener);
            $('#simControl').on('simTerminated', simTerminatedListener);
        }
    }
    function blocklyListener(event) {
        clearTimeout(myTimeoutID);
        if (event.blockId !== 'step_dummy' && event.newParentId && blocklyWorkspace.remainingCapacity() == 0) {
            configData = SIM.exportConfigData();
            noTimeout = true;
            var blocks = blocklyWorkspace.getAllBlocks();
            for (var i = 0; i < blocks.length; i++) {
                blocks[i].setMovable(false);
            }
            // only allow blocks to be moved at the end of the program
            if (blocks[blocks.length - 2].id !== event.blockId) {
                blocklyWorkspace.getBlockById(event.blockId).dispose(true, true);
                clearTimeout(myTimeoutID);
                myTimeoutID = setTimeout(reloadTutorial, TIMEOUT);
                return;
            }
            setTimeout(function () {
                clearTimeout(myTimeoutID);
                Blockly.hideChaff();
                $('.blocklyWorkspace>.blocklyFlyout').fadeOut();
                $('#simControl').trigger('click');
            }, 500);
        }
        else if (!noTimeout) {
            clearTimeout(myTimeoutID);
            myTimeoutID = setTimeout(reloadTutorial, TIMEOUT);
        }
    }
    function simTerminatedListener() {
        var blocks = blocklyWorkspace.getAllBlocks();
        if (checkSolutionCorrect(blocks)) {
            if (step + 1 >= maxSteps) {
                clearTimeout(myTimeoutID);
                var blocks = blocklyWorkspace.getAllBlocks();
                blocks[blocks.length - 1].dispose(false, true);
                for (var i = 0; i < blocks.length - 1; i++) {
                    blocks[i].setDisabled(false);
                    blocks[i].setMovable(false);
                }
                function waitClock(t) {
                    var tt = t - 1;
                    if (tt >= 0) {
                        updateDonutChart('#specificChart', (100 / 120) * (120 - tt));
                        setTimeout(function () {
                            waitClock(tt);
                        }, 1000);
                    }
                }
                $('#volksbotStart').one('click', function () {
                    setTimeout(function () {
                        $('#menuRunProg').trigger('click');
                        $('#tutorialStartView .modal-dialog').hide();
                        $('#specificChart').show();
                        waitClock(120);
                    }, 1000);
                });
                $('#tutorialStartViewText').html(tutorial.end);
                $('#tutorialStartView').one('hidden.bs.modal', function (e) {
                    reloadTutorial();
                    return;
                });
                setTimeout(function () {
                    $('#tutorialStartView').modal({
                        backdrop: 'static',
                        keyboard: false,
                        show: true,
                    });
                });
            }
            else {
                // delete last dummy block from program
                for (var i = 1; i < blocks.length; i++) {
                    blocks[i].setDisabled(true);
                }
                setTimeout(function () {
                    // $("#state").fadeOut(550, function() {
                    //     $("#state").removeClass("typcn-eye-outline").addClass("typcn-hand").fadeIn(550);
                    // });
                    Blockly.hideChaff();
                    $('.blocklyWorkspace>.blocklyFlyout').fadeIn(function () {
                        nextStep();
                    });
                    clearTimeout(myTimeoutID);
                    myTimeoutID = setTimeout(reloadTutorial, TIMEOUT);
                    noTimeout = false;
                }, 500);
            }
        }
        else {
            setTimeout(function () {
                SIM.relatives2coordinates(configData);
                var blocks = blocklyWorkspace.getAllBlocks();
                var toDelete = 1;
                if (tutorial.step[step - 1]) {
                    toDelete = blocks.length - tutorial.step[step - 1].maxBlocks;
                }
                else {
                    toDelete = blocks.length - 1; // start block always stays
                }
                for (i = 0; i < toDelete; i++) {
                    blocks[blocks.length - 2].dispose(true, true);
                    blocks = blocklyWorkspace.getAllBlocks();
                }
                // $("#state").fadeOut(550, function() {
                //     $("#state").removeClass("typcn-eye-outline").addClass("typcn-hand").fadeIn(550);
                // });
                Blockly.hideChaff();
                $('.blocklyWorkspace>.blocklyFlyout').fadeIn();
                myTimeoutID = setTimeout(reloadTutorial, TIMEOUT);
                noTimeout = false;
            }, 500);
        }
    }
    function checkSolutionCorrect(blocks) {
        if (tutorial.step[step].solution) {
            for (i = 0; i < tutorial.step[step].solution.length; i++) {
                if (blocks[i + 1].type != tutorial.step[step].solution[i]) {
                    return false;
                }
            }
        }
        return true;
    }
    function showOverview() {
        if (tutorial.overview) {
            var html = tutorial.overview.description;
            $('#tutorialOverviewText').html(html);
            $('#tutorialOverviewTitle').html(tutorial.name);
            $('#tutorialAbort').off('click.dismiss.bs.modal');
            $('#tutorialAbort').onWrap('click.dismiss.bs.modal', function (event) {
                exitTutorial();
                return false;
            });
            $('#tutorialContinue').off('click.dismiss.bs.modal');
            $('#tutorialContinue').onWrap('click.dismiss.bs.modal', function (event) {
                LOG.info('tutorial executed ' + tutorial.index + tutorialId);
                return false;
            });
            $('#tutorialOverview').modal({
                backdrop: 'static',
                keyboard: false,
                show: true,
            });
        }
        else if (tutorial.startView) {
            GUISTATE_C.setRunEnabled(true);
            $('#tutorialStartView .modal-dialog').show();
            $('#tutorialStartViewText').html("<img height='401px' src='css/img/DieMaus_KV_Museum_mit_der_Maus_RGB.png' alt='Die Maus'>");
            $('#volksbotStart').one('click', function () {
                clearTimeout(myTimeoutID);
                $('#startAudio').one('ended', audioEnd);
                $('#volksbotStart').hide('slow'); //prop('disabled', true);
                $('#startAudio')[0].play();
                return;
            });
            clearTimeout(myTimeoutID);
            $('#tutorialStartView').modal({
                backdrop: 'static',
                keyboard: false,
                show: true,
            });
        }
    }
    function audioEnd() {
        setTimeout(function () {
            $('#tutorialStartViewText').hide().html(tutorial.startView).fadeIn('slow');
            $('#startVideo').one('ended', videoEnd);
            //$('#startVideo')[0].play();
        }, 500);
    }
    function videoEnd() {
        $('#volksbotStart').show('slow'); //prop('disabled', false);
        $('#tutorialStartView').modal('hide');
        clearTimeout(myTimeoutID);
        myTimeoutID = setTimeout(reloadTutorial, TIMEOUT);
    }
    function createInstruction() {
        if (tutorial.step[step]) {
            if (tutorial.step[step].toolbox) {
                try {
                    blocklyWorkspace.options.maxBlocks = tutorial.step[step].maxBlocks;
                    PROG_C.loadExternalToolbox(tutorial.step[step].toolbox);
                }
                catch (e) {
                    console.log(e);
                }
            }
            if (tutorial.instructions) {
                $('#tutorialContent').empty();
                if (tutorial.step[step].instruction) {
                    if (tutorial.step[step].toolbox) {
                        try {
                            PROG_C.loadExternalToolbox(tutorial.step[step].toolbox);
                            blocklyWorkspace.options.maxBlocks = tutorial.step[step].maxBlocks;
                        }
                        catch (e) {
                            console.log(e);
                        }
                    }
                    if (tutorial.step[step].header) {
                        $('#tutorialContent').append($('<h3>').attr('class', 'quiz header').append(tutorial.step[step].header));
                    }
                    $('#tutorialContent').append(tutorial.step[step].instruction);
                    if (tutorial.step[step].tip) {
                        $('#tutorialContent').append('<br><br>').append($('<ul>').attr('class', 'tip'));
                        if (Array.isArray(tutorial.step[step].tip)) {
                            for (var i = 0; i < tutorial.step[step].tip.length; i++) {
                                $('#tutorialContent ul.tip').append('<li>' + tutorial.step[step].tip[i] + '</li>');
                            }
                        }
                        else {
                            $('#tutorialContent ul.tip').append('<li>' + tutorial.step[step].tip + '</li>');
                        }
                    }
                    if (tutorial.step[step].solution) {
                        $('#tutorialContent').append($('<div>')
                            .attr('id', 'helpDiv')
                            .append($('<button>', {
                            text: 'Hilfe',
                            id: 'quizHelp',
                            class: 'btn test',
                            click: function () {
                                showSolution();
                            },
                        })));
                    }
                    if (step == maxSteps - 1) {
                        // last step
                        $('#tutorialContent').append($('<div>')
                            .attr('class', 'quiz continue')
                            .append($('<button>', {
                            text: 'Tutorial beenden',
                            class: 'btn',
                            click: function () {
                                MSG.displayMessage(tutorial.end, 'POPUP', '');
                                $('.modal').one('hide.bs.modal', function (e) {
                                    $('#tutorialEnd').trigger('click');
                                    return false;
                                });
                                return false;
                            },
                        })));
                    }
                    else {
                        $('#tutorialContent').append($('<div>')
                            .attr('class', 'quiz continue')
                            .append($('<button>', {
                            text: 'weiter',
                            class: 'btn',
                            click: function () {
                                createQuiz();
                            },
                        })));
                    }
                }
                else {
                    // apparently a step without an instruction -> go directly to the quiz
                    createQuiz();
                }
            }
            if (tutorial.step[step].simulationSettings) {
                SIM.relatives2coordinates(tutorial.step[step].simulationSettings);
            }
        }
    }
    function createQuiz() {
        if (tutorial.step[step].quiz) {
            $('#tutorialContent').html('');
            $('#tutorialContent').append($('<div>').attr('class', 'quiz content'));
            tutorial.step[step].quiz.forEach(function (quiz, iQuiz) {
                $('#tutorialContent .quiz.content').append($('<div>').attr('class', 'quiz question').append(quiz.question));
                var answers = shuffle(quiz.answer);
                quiz.answer.forEach(function (answer, iAnswer) {
                    var correct = answer.charAt(0) !== '!';
                    if (!correct) {
                        answer = answer.substr(1);
                    }
                    $('#tutorialContent .quiz.content').append($('<label>')
                        .attr('class', 'quiz answer')
                        .append(answer)
                        .append($('<input>', {
                        type: 'checkbox',
                        class: 'quiz',
                        name: 'answer_' + iAnswer,
                        id: iQuiz + '_' + iAnswer,
                        value: correct,
                    }))
                        .append($('<span>', {
                        for: iQuiz + '_' + iAnswer,
                        class: 'checkmark quiz',
                    })));
                });
            });
            $('#tutorialContent .quiz.content').append($('<div>')
                .attr('class', 'quiz footer')
                .attr('id', 'quizFooter')
                .append($('<button/>', {
                text: 'prüfen!',
                class: 'btn test left',
                click: function () {
                    checkQuiz();
                },
            })));
        }
        else {
            // apparently no quiz provided, go to next step
            nextStep();
        }
    }
    function nextStep() {
        step += 1;
        if (step < maxSteps) {
            $('#tutorial-list .active').removeClass('active');
            $('#tutorial-list .preActive').removeClass('preActive');
            $("#tutorial-list .step a:contains('" + (step + 1) + "')")
                .parent()
                .addClass('active');
            $("#tutorial-list .step a:contains('" + step + "')")
                .parent()
                .addClass('preActive');
            createInstruction();
            if (step == maxSteps - 1 && quiz) {
                var finalMaxCredits = 0;
                for (var i = maxCredits.length; i--;) {
                    if (maxCredits[i]) {
                        finalMaxCredits += maxCredits[i];
                    }
                }
                var finalCredits = 0;
                for (var i = credits.length; i--;) {
                    if (credits[i]) {
                        finalCredits += credits[i];
                    }
                }
                var percent = 0;
                if (finalMaxCredits !== 0) {
                    percent = Math.round((100 / finalMaxCredits) * finalCredits);
                }
                var thumbs = Math.round((percent - 50) / 17) + 1;
                var $quizFooter = $('<div>')
                    .attr('class', 'quiz footer')
                    .attr('id', 'quizFooter')
                    .append(finalCredits + ' von ' + finalMaxCredits + ' Antworten oder ' + percent + '% sind richtig! ');
                $quizFooter.insertBefore($('.quiz.continue'));
                $('#quizFooter').append($('<span>', {
                    id: 'quizResult',
                }));
                if (percent > 0) {
                    $('#quizResult').append($('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    }));
                }
                if (percent == 100) {
                    $('#quizResult').append($('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    }));
                    $('#quizResult').append($('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    }));
                    $('#quizResult').append(' Spitze!');
                }
                else if (percent > 80) {
                    $('#quizResult').append($('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    }));
                    $('#quizResult').append(' Super!');
                }
                else if (percent > 60) {
                    $('#quizResult').append(' Gut gemacht!');
                }
                else if (percent == 0) {
                    $('#quizResult').append(' Du kannst die Quizfragen jederzeit wiederholen, wenn du möchtest!');
                }
                else {
                    $('#quizResult').append(' Das ist ok!');
                }
            }
        }
        else {
            // end of the tutorial
        }
    }
    function showSolution() {
        $('#helpDiv').append($('<div>').append(tutorial.step[step].solution).attr({
            class: 'imgSol',
        }));
        $('#quizHelp').remove();
    }
    function checkQuiz() {
        var countCorrect = 0;
        var countChecked = 0;
        var totalQuestions = $('.quiz.question').length;
        var totalCorrect = $('.quiz.answer [value=true]').length;
        $('.quiz input').each(function (i, elem) {
            var $label = $('label>span[for="' + $this.attr('id') + '"]');
            if ($(this).is(':checked')) {
                countChecked++;
            }
            if ($(this).val() === 'true' && $(this).is(':checked')) {
                $label.parent().addClass('correct');
                countCorrect++;
            }
            if ($(this).val() === 'false' && $(this).is(':checked')) {
                $label.parent().addClass('fail');
            }
            $(this).attr('onclick', 'return false;');
        });
        $('#quizFooter').html('');
        if (countCorrect !== totalCorrect) {
            $('#quizFooter').append($('<button/>', {
                text: 'nochmal',
                class: 'btn test',
                click: function () {
                    createQuiz();
                },
            }));
            var confirmText;
            if (countChecked == 0) {
                confirmText = 'Bitte kreuze mindestens eine Anwort an.';
            }
            else if (totalQuestions == 1) {
                confirmText = 'Die Antwort ist leider nicht ganz richtig.';
            }
            else {
                confirmText = countCorrect + ' Anworten von ' + totalCorrect + ' sind richtig!';
            }
            $('#quizFooter').append($('<span>', {
                text: confirmText,
            }));
        }
        $('#tutorialContent').append($('<div>')
            .attr('class', 'quiz continue')
            .append($('<button>', {
            text: 'weiter',
            class: 'btn',
            click: function () {
                nextStep();
            },
        })));
        credits[step] = countCorrect;
        maxCredits[step] = totalCorrect;
    }
    function shuffle(answers) {
        for (var j, x, i = answers.length; i; j = Math.floor(Math.random() * i), x = answers[--i], answers[i] = answers[j], answers[j] = x)
            ;
        return answers;
    }
    function toggleTutorial() {
        if ($('#tutorialButton').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        }
        else {
            $('#blockly').openRightView('tutorial', INITIAL_WIDTH);
        }
    }
    function openTutorialView() {
        if ($('#tutorialDiv').hasClass('rightActive')) {
            return;
        }
        if ($('#blockly').hasClass('rightActive')) {
            function waitForClose() {
                if (!$('#blockly').hasClass('rightActive')) {
                    toggleTutorial();
                }
                else {
                    setTimeout(waitForClose, 50);
                }
            }
            waitForClose();
        }
        else {
            toggleTutorial();
        }
    }
    function closeTutorialView() {
        if ($('.rightMenuButton.rightActive').length >= 0) {
            $('.rightMenuButton.rightActive').trigger('click');
        }
    }
    function exitTutorial() {
        Blockly.hideChaff();
        closeTutorialView();
        $('#tutorial-navigation').fadeOut(750);
        $('#head-navigation').fadeIn(750);
        $('#tutorialButton').fadeOut();
        $('.blocklyToolboxDiv>.levelTabs').removeClass('invisible');
        PROG_C.loadExternalToolbox(GUISTATE_C.getProgramToolbox());
        Blockly.mainWorkspace.options.maxBlocks = undefined;
        $('#tabTutorialList').trigger('click');
    }
    function updateDonutChart(el, percent) {
        percent = Math.round(percent);
        if (percent > 100) {
            percent = 100;
        }
        else if (percent < 0) {
            percent = 0;
        }
        var deg = Math.round(360 * (percent / 100));
        if (percent > 50) {
            $(el + ' .pie').css('clip', 'rect(auto, auto, auto, auto)');
            $(el + ' .right-side').css('transform', 'rotate(180deg)');
        }
        else {
            $(el + ' .pie').css('clip', 'rect(0,1em, 1em, 0.5em)');
            $(el + ' .right-side').css('transform', 'rotate(0deg)');
        }
        $(el + ' .right-side').css('border-width', '0.5em');
        $(el + ' .left-side').css('border-width', '0.5em');
        $(el + ' .shadow').css('border-width', '0.5em');
        $(el + ' .left-side').css('transform', 'rotate(' + deg + 'deg)');
    }
});
