import * as MSG from 'message';
import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as PROG_C from 'program.controller';
import * as IMPORT_C from 'import.controller';
import * as Blockly from 'blockly';
import * as SIM from 'simulation.simulation';
import * as $ from 'jquery';

const INITIAL_WIDTH = 0.5;
var blocklyWorkspace;
var tutorialList;
var tutorial;
var step = 0;
var maxSteps = 0;
var credits = [];
var maxCredits = [];
var quiz = false;
var configData = {};
const TIMEOUT = 25000;
const READ_TIMEOUT = 30000;
const CHANGE_TIMEOUT = 10000;
var myTimeoutID;
var tutorialId;
var noTimeout = true;
var level = 0;

var Error = {
    No: 0,
    Direction: -1,
    Collect: -2,
    NoCollect: -3,
    Steps: -4,
};

function init() {
    tutorialList = GUISTATE_C.getListOfTutorials();
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initEvents();
}
export { init };

function initEvents() {
    // not used
    $('.menu.tutorial').on('click', function (event) {
        startTutorial(event.target.id);
    });
    // not used
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
        if (level === 0) {
            $('#volksbotStart').hide('slow'); //prop('disabled', true);
            $('#startVideo').one('ended', videoEnd);
            $('#startVideo')[0].play();
        } else if (level === 1) {
            $('#startButton').fadeIn('3000');
            myTimeoutID = setTimeout(chooseTutorial, READ_TIMEOUT);
            $('#volksbotStart').one('click', function () {
                $('#tutorialStartView').modal('hide');
                clearTimeout(myTimeoutID);
                if (once) {
                    myTimeoutID = setTimeout(blocklyListener, TIMEOUT);
                } else {
                    myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
                }
                return false;
            });
        }
    });
    $('#exit').on('click', function () {
        chooseTutorial();
    });
}

function chooseTutorial(opt_init) {
    clearTimeout(myTimeoutID);
    $('#tabProgram').trigger('click');
    $('#tutorialStartView .modal-dialog').show();
    $('#tutorialIntro').hide();
    $('#tutorialRobot').hide();
    $('#startButton').hide();
    $('#tutorialChoose').show();
    $('#tutorialStartView').modal({
        backdrop: 'static',
        keyboard: false,
        show: true,
    });
    $('#volksbotStartSimple').one('click', function () {
        reloadTutorial(0, opt_init);
        startTutorial();
        configureLEDs('oooooooooooooooooooo');
        return false;
    });
    $('#volksbotStartAdvanced').one('click', function () {
        reloadTutorial(1, opt_init);
        startTutorial();
        configureLEDs('oooooooooooooooooooo');
        return false;
    });
    if (opt_init) {
        tutorialId = 'volksbot01';
        loadFromTutorial(tutorialId);
        createInstruction();
        if (tutorial.initXML) {
            IMPORT_C.loadProgramFromXML('egal', tutorial.initXML);
        }
        SIM.loadConfigData(tutorial.step[step].simulationSettings);
    }
}
export { chooseTutorial };

function loadFromTutorial(tutId) {
    // initialize this tutorial
    tutorialId = tutId;
    tutorial = tutorialList[tutId];
    step = 0;
    maxSteps = 0;
    credits = [];
    maxCredits = [];
    quiz = false;
    configData = {};
    noTimeout = true;
    clearTimeout(myTimeoutID);
    blocklyWorkspace.options.maxBlocks = null;
    if (!tutorial) {
        console.error('no tutorial available!');
    }
}
export { loadFromTutorial };

function startTutorial() {
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
        $('#tutorial-list').append(
            $('<li>')
                .attr('class', 'step')
                .append(
                    $('<a>')
                        .attr({
                            href: '#',
                        })
                        .append(i + 1)
                )
        );
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
}

function reloadTutorial(newLevel, opt_init) {
    clearTimeout(myTimeoutID);
    var i = parseInt(tutorialId.slice(-1));
    if (!opt_init) {
        i++;
    }
    level = newLevel;
    var newTutorialId = tutorialId.slice(0, tutorialId.length - 2) + level + i;
    tutorialId = tutorialList[newTutorialId] ? newTutorialId : tutorialId.slice(0, tutorialId.length - 2) + level + 1;
    tutorialController.loadFromTutorial(tutorialId);
    function errorfunction(e) {
        alert(e);
    }
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
    } else {
        blocklyWorkspace.removeChangeListener(blocklyListener);
        blocklyWorkspace.addChangeListener(blocklyListener);
        $('#simControl').off('simTerminated', simTerminatedListener);
        $('#simControl').on('simTerminated', simTerminatedListener);
    }
}

var once = false;

function blocklyListener(event) {
    if (event.blockId !== 'step_dummy' && (event.newParentId || (event.name && event.name === 'NUMBER')) && blocklyWorkspace.remainingCapacity() == 0) {
        if (checkSolutionCorrect(blocklyWorkspace.getAllBlocks()) === -4 && !once) {
            once = true;
            clearTimeout(myTimeoutID);
            myTimeoutID = setTimeout(function () {
                blocklyListener(event);
            }, CHANGE_TIMEOUT);
            return;
        }
        configData = SIM.exportConfigData();
        noTimeout = true;
        var blocks = blocklyWorkspace.getAllBlocks();
        for (var i = 0; i < blocks.length; i++) {
            blocks[i].setMovable(false);
            blocks[i]['markNotChangeable'] = true;
        }
        // only allow blocks to be moved at the end of the program
        if (blocks[blocks.length - 2].id !== event.blockId) {
            blocklyWorkspace.getBlockById(event.blockId).dispose(true, true);
            clearTimeout(myTimeoutID);
            myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
            return;
        }
        setTimeout(function () {
            clearTimeout(myTimeoutID);
            Blockly.hideChaff();
            $('.blocklyWorkspace>.blocklyFlyout').fadeOut();
            $('#simControl').trigger('click');
            configureLEDs('oooooooooooooooooooo');
        }, 500);
    } else if (!noTimeout) {
        clearTimeout(myTimeoutID);
        myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
    }
}

function simTerminatedListener() {
    var blocks = blocklyWorkspace.getAllBlocks();
    let thisSolutionCorrect = checkSolutionCorrect(blocks);
    clearTimeout(myTimeoutID);
    switch (thisSolutionCorrect) {
        case Error.No: {
            once = false;
            if (step + 1 >= maxSteps) {
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
                    } else {
                        configureLEDs('oooooooooooooooooooo');
                    }
                }
                $('#volksbotStart').one('click', function () {
                    configureLEDs(tutorial.ledSettings);
                    setTimeout(function () {
                        configureLEDs(tutorial.ledSettings);
                        $('#menuRunProg').trigger('click');
                        $('#tutorialStartView .modal-dialog').hide();
                        $('#specificChart').show();
                        waitClock(120);
                        return false;
                    }, 500);
                });
                $('#tutorialIntro').html(tutorial.end).fadeIn('slow');
                setTimeout(function () {
                    $('#tutorialStartView').modal({
                        backdrop: 'static',
                        keyboard: false,
                        show: true,
                    });
                }, 500);
            } else {
                for (var i = 1; i < blocks.length; i++) {
                    blocks[i].setDisabled(true);
                }
                setTimeout(function () {
                    Blockly.hideChaff();
                    $('.blocklyWorkspace>.blocklyFlyout').fadeIn(function () {
                        nextStep();
                    });
                    clearTimeout(myTimeoutID);
                    myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
                    noTimeout = false;
                }, 500);
            }
            break;
        }
        case Error.Collect:
        case Error.NoCollect:
        case Error.Direction: {
            function correct(msg) {
                setTimeout(function () {
                    SIM.loadConfigData(configData);
                    var blocks = blocklyWorkspace.getAllBlocks();
                    var toDelete = 1;
                    if (tutorial.step[step - 1]) {
                        toDelete = blocks.length - tutorial.step[step - 1].maxBlocks;
                    } else {
                        toDelete = blocks.length - 1; // start block always stays
                    }
                    for (i = 0; i < toDelete; i++) {
                        blocks[blocks.length - 2].dispose(true, true);
                        blocks = blocklyWorkspace.getAllBlocks();
                    }
                    Blockly.hideChaff();
                    $('.blocklyWorkspace>.blocklyFlyout').fadeIn();
                    MSG.displayMessage(msg, 'POPUP', '');
                    $('#show-message').one('hidden.bs.modal', function () {
                        clearTimeout(myTimeoutID);
                        myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
                    });
                    myTimeoutID = setTimeout(function () {
                        $('#show-message').hide();
                        chooseTutorial();
                    }, TIMEOUT);
                    noTimeout = false;
                }, 500);
            }
            if (thisSolutionCorrect === Error.Direction) {
                correct('Das ist nicht der richtige Weg. Überlege noch einmal und wähle den passenden Programmierbefehl!');
            } else if (thisSolutionCorrect === Error.Collect) {
                correct('Die Aufgabe des Roboters ist, die grünen Felder einzusammeln. Ergänze diesen Schritt!');
            } else if (thisSolutionCorrect === Error.NoCollect) {
                correct('Du hast dieses Feld schon eingesammelt. Fahre weiter!');
            }
            break;
        }
        case Error.Steps: {
            setTimeout(function () {
                blocks[blocks.length - 2]['markNotChangeable'] = false;
                SIM.loadConfigData(configData);
                Blockly.hideChaff();
                MSG.displayMessage('Wähle die richtige Anzahl an Feldern, die vorwärts gefahren werden sollen.', 'POPUP', '');
                $('#show-message').one('hidden.bs.modal', function () {
                    clearTimeout(myTimeoutID);
                    myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
                });
                myTimeoutID = setTimeout(function () {
                    $('#show-message').hide();
                    chooseTutorial();
                }, TIMEOUT);
                noTimeout = false;
            }, 500);
            break;
        }
        default:
            break;
    }
}

function checkSolutionCorrect(blocks) {
    if (tutorial.step[step].solution) {
        for (let i = 0; i < tutorial.step[step].solution.length; i++) {
            let solution = tutorial.step[step].solution[i].split(',');
            if (blocks[i + 1].type != solution[0]) {
                if (solution[0].indexOf('collect') >= 0) {
                    return Error.Collect;
                } else if (blocks[i + 1].type.indexOf('collect') >= 0) {
                    return Error.NoCollect;
                } else {
                    return Error.Direction;
                }
            } else {
                if (solution[1]) {
                    if (blocks[i + 1].getFieldValue('NUMBER') !== solution[1]) {
                        return Error.Steps;
                    }
                }
            }
        }
    }
    return Error.No;
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
    } else if (tutorial.startView) {
        GUISTATE_C.setRunEnabled(true);
        clearTimeout(myTimeoutID);
        $('#tutorialStartView .modal-dialog').show();
        $('#tutorialChoose').hide();
        $('#tutorialIntro').hide().html(tutorial.startView).fadeIn('slow');
        if (level === 0) {
            $('#tutorialIntro').removeClass('text', 'start');
            $('#tutorialIntro').addClass('video');
            $('#startAudio').one('ended', audioEnd);
            $('#startAudio')[0].play();
        } else {
            $('#tutorialIntro').removeClass('video', 'start');
            $('#tutorialIntro').addClass('text');
            $('#startButton').fadeIn('3000');
            myTimeoutID = setTimeout(chooseTutorial, READ_TIMEOUT);
            $('#volksbotStart').one('click', function () {
                $('#tutorialStartView').modal('hide');
                clearTimeout(myTimeoutID);
                myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
                return false;
            });
        }
    }
}

function audioEnd() {
    setTimeout(function () {
        $('#startVideo').one('ended', videoEnd);
        $('#startVideo')[0].play();
    }, 500);
}

function videoEnd() {
    $('#volksbotStart').show('slow'); //prop('disabled', false);
    $('#tutorialStartView').modal('hide');
    clearTimeout(myTimeoutID);
    myTimeoutID = setTimeout(chooseTutorial, TIMEOUT);
}

function createInstruction() {
    if (tutorial.step[step]) {
        if (tutorial.step[step].toolbox) {
            try {
                blocklyWorkspace.options.maxBlocks = tutorial.step[step].maxBlocks;
                PROG_C.loadExternalToolbox(tutorial.step[step].toolbox);
            } catch (e) {
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
                    } catch (e) {
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
                    } else {
                        $('#tutorialContent ul.tip').append('<li>' + tutorial.step[step].tip + '</li>');
                    }
                }

                if (tutorial.step[step].solution) {
                    $('#tutorialContent').append(
                        $('<div>')
                            .attr('id', 'helpDiv')
                            .append(
                                $('<button>', {
                                    text: 'Hilfe',
                                    id: 'quizHelp',
                                    class: 'btn test',
                                    click: function () {
                                        showSolution();
                                    },
                                })
                            )
                    );
                }

                if (step == maxSteps - 1) {
                    // last step
                    $('#tutorialContent').append(
                        $('<div>')
                            .attr('class', 'quiz continue')
                            .append(
                                $('<button>', {
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
                                })
                            )
                    );
                } else {
                    $('#tutorialContent').append(
                        $('<div>')
                            .attr('class', 'quiz continue')
                            .append(
                                $('<button>', {
                                    text: 'weiter',
                                    class: 'btn',
                                    click: function () {
                                        createQuiz();
                                    },
                                })
                            )
                    );
                }
            } else {
                // apparently a step without an instruction -> go directly to the quiz
                createQuiz();
            }
        }
        if (tutorial.step[step].simulationSettings) {
            SIM.loadConfigData(tutorial.step[step].simulationSettings);
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
                $('#tutorialContent .quiz.content').append(
                    $('<label>')
                        .attr('class', 'quiz answer')
                        .append(answer)
                        .append(
                            $('<input>', {
                                type: 'checkbox',
                                class: 'quiz',
                                name: 'answer_' + iAnswer,
                                id: iQuiz + '_' + iAnswer,
                                value: correct,
                            })
                        )
                        .append(
                            $('<span>', {
                                for: iQuiz + '_' + iAnswer,
                                class: 'checkmark quiz',
                            })
                        )
                );
            });
        });
        $('#tutorialContent .quiz.content').append(
            $('<div>')
                .attr('class', 'quiz footer')
                .attr('id', 'quizFooter')
                .append(
                    $('<button/>', {
                        text: 'prüfen!',
                        class: 'btn test left',
                        click: function () {
                            checkQuiz();
                        },
                    })
                )
        );
    } else {
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
            for (var i = maxCredits.length; i--; ) {
                if (maxCredits[i]) {
                    finalMaxCredits += maxCredits[i];
                }
            }
            var finalCredits = 0;
            for (var i = credits.length; i--; ) {
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
            $('#quizFooter').append(
                $('<span>', {
                    id: 'quizResult',
                })
            );
            if (percent > 0) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    })
                );
            }
            if (percent == 100) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    })
                );
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    })
                );
                $('#quizResult').append(' Spitze!');
            } else if (percent > 80) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up',
                    })
                );
                $('#quizResult').append(' Super!');
            } else if (percent > 60) {
                $('#quizResult').append(' Gut gemacht!');
            } else if (percent == 0) {
                $('#quizResult').append(' Du kannst die Quizfragen jederzeit wiederholen, wenn du möchtest!');
            } else {
                $('#quizResult').append(' Das ist ok!');
            }
        }
    } else {
        // end of the tutorial
    }
}

function showSolution() {
    $('#helpDiv').append(
        $('<div>').append(tutorial.step[step].solution).attr({
            class: 'imgSol',
        })
    );
    $('#quizHelp').remove();
}

function checkQuiz() {
    var countCorrect = 0;
    var countChecked = 0;
    var totalQuestions = $('.quiz.question').length;
    var totalCorrect = $('.quiz.answer [value=true]').length;
    $('.quiz input').each(function (i, elem) {
        let $label = $('label>span[for="' + $this.attr('id') + '"]');
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
        $('#quizFooter').append(
            $('<button/>', {
                text: 'nochmal',
                class: 'btn test',
                click: function () {
                    createQuiz();
                },
            })
        );
        var confirmText;
        if (countChecked == 0) {
            confirmText = 'Bitte kreuze mindestens eine Anwort an.';
        } else if (totalQuestions == 1) {
            confirmText = 'Die Antwort ist leider nicht ganz richtig.';
        } else {
            confirmText = countCorrect + ' Anworten von ' + totalCorrect + ' sind richtig!';
        }
        $('#quizFooter').append(
            $('<span>', {
                text: confirmText,
            })
        );
    }
    $('#tutorialContent').append(
        $('<div>')
            .attr('class', 'quiz continue')
            .append(
                $('<button>', {
                    text: 'weiter',
                    class: 'btn',
                    click: function () {
                        nextStep();
                    },
                })
            )
    );
    credits[step] = countCorrect;
    maxCredits[step] = totalCorrect;
}

function shuffle(answers) {
    for (var j, x, i = answers.length; i; j = Math.floor(Math.random() * i), x = answers[--i], answers[i] = answers[j], answers[j] = x);
    return answers;
}

function toggleTutorial() {
    if ($('#tutorialButton').hasClass('rightActive')) {
        $('#blockly').closeRightView();
    } else {
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
            } else {
                setTimeout(waitForClose, 50);
            }
        }
        waitForClose();
    } else {
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
    } else if (percent < 0) {
        percent = 0;
    }
    var deg = Math.round(360 * (percent / 100));

    if (percent > 50) {
        $(el + ' .pie').css('clip', 'rect(auto, auto, auto, auto)');
        $(el + ' .right-side').css('transform', 'rotate(180deg)');
    } else {
        $(el + ' .pie').css('clip', 'rect(0,1em, 1em, 0.5em)');
        $(el + ' .right-side').css('transform', 'rotate(0deg)');
    }
    $(el + ' .right-side').css('border-width', '0.5em');
    $(el + ' .left-side').css('border-width', '0.5em');
    $(el + ' .shadow').css('border-width', '0.5em');
    $(el + ' .left-side').css('transform', 'rotate(' + deg + 'deg)');
}

function configureLEDs(payload) {
    return $.ajax({
        url: 'http://192.168.0.136:5555/Home/Message/1?payload=' + payload,
        type: 'GET',
        crossDomain: true,
        dataType: 'json',
        data: '',
        success: function (result) {
            console.log(result);
        },
        error: function (error) {
            console.error(error);
        },
    });
}
