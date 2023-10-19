import * as MSG from 'message';
import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as PROG_C from 'program.controller';
import * as ROBOT_C from 'robot.controller';
import * as IMPORT_C from 'import.controller';
//@ts-ignore
import * as Blockly from 'blockly';
import * as U from 'util.roberta';
import * as $ from 'jquery';
import { Modal } from 'bootstrap';

const INITIAL_WIDTH = 0.5;
let blocklyWorkspace: any;
let tutorialList;
let tutorialId: number;
let tutorial;
let step: number = 0;
let maxSteps: number = 0;
let credits: any[] = [];
let maxCredits: any[] = [];
let quiz: boolean = false;

export function init(): void {
    tutorialList = GUISTATE_C.getListOfTutorials();
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initEvents();
}

function initEvents(): void {
    $('.menu.tutorial').onWrap('click', function(event): void {
        //@ts-ignore
        startTutorial(event.target.id);
    });
    $('#tutorialButton').onWrap('click touchend', function(): boolean {
        toggleTutorial($(this));
        return false;
    });
}

export function loadFromTutorial(tutId: number): void {
    // initialize this tutorial
    tutorialId = tutId;
    tutorial = tutorialList && tutorialList[tutId];
    if (tutorial) {
        //@ts-ignore
        ROBOT_C.switchRobot(tutorial.robot, {}, false, startTutorial);
    }

    function startTutorial(): void {
        //@ts-ignore
        $('#tabProgram').tabWrapShow();
        if (GUISTATE_C.isKioskMode()) {
            $('#infoButton').hide();
            $('#feedbackButton').hide();
            // for beginner tutorials the code view is more confusing than helpful, so we don't show the button in kiosk mode
            if (tutorial.level.indexOf('1') === 0) {
                $('#codeButton').hide();
            }
            U.removeLinks($('#legalDiv a'));
        }
        if (tutorial.initXML) {
            IMPORT_C.loadProgramFromXML('egal', tutorial.initXML);
        }
        maxSteps = tutorial.step.length;
        step = 0;
        credits = [];
        maxCredits = [];
        quiz = false;
        for (let i: number = 0; i < tutorial.step.length; i++) {
            if (tutorial.step[i].quiz) {
                quiz = true;
                break;
            }
        }
        $('#tutorialHeader').empty();

        // create this tutorial navigation
        for (let i: number = 0; i < tutorial.step.length; i++) {
            $('#tutorialHeader').append(
                $('<li>')
                    .attr('class', 'step')
                    .append(
                        $('<a>')
                            .attr({
                                href: '#'
                            })
                            //@ts-ignore
                            .append(i + 1)
                    )
            );
        }
        $('#tutorialHeader li:last-child').addClass('last');
        $('#tutorial-header').html(tutorial.name);

        // prepare the view
        $('#tutorial-navigation, #tutorialEnd, #tutorialButton').show();
        $('#head-navigation, #tab-navigation').hide();

        $('#tutorialHeader :first-child').addClass('active');
        $('.blocklyToolboxDiv>.levelTabs').addClass('invisible');

        initStepEvents();
        createInstruction();
        openTutorialView();
        showOverview();
    }
}

function initStepEvents(): void {
    $('#tutorialHeader.nav li.step a').on('click', function(): void {
        step = parseInt($(this).text()) - 2;
        nextStep();
        openTutorialView();
    });
    $('#tutorialEnd').oneWrap('click', function(e): boolean {
        exitTutorial();
        return false;
    });
}

function showOverview(): void {
    if (!tutorial.overview) return;
    let html: string = tutorial.overview.description;
    html += '</br></br><b>Lernziel: </b>';
    html += tutorial.overview.goal;
    html += '</br></br><b>Vorkenntnisse: </b>';
    html += tutorial.overview.previous;
    html += '<hr style="border: 2px solid #33B8CA; margin: 10px 0">';
    html += '<span class="typcn typcn-stopwatch"/>&emsp;&emsp;';
    html += tutorial.time;
    html += '</br><span class="typcn typcn-group"/>&emsp;&emsp;';
    html += tutorial.age;
    html += '</br><span class="typcn typcn-simulation"/>&emsp;&emsp;';
    html += tutorial.sim && (tutorial.sim === 'sim' || tutorial.sim === 1) ? 'ja' : 'nein';
    if (tutorial.level) {
        html += '</br><span class="typcn typcn-mortar-board"/>&emsp;&emsp;';
        let maxLevel: number = isNaN(tutorial.level) ? tutorial.level.split('/')[1] : 3;
        let thisLevel: number = isNaN(tutorial.level) ? tutorial.level.split('/')[0] : tutorial.level;
        for (let i: number = 1; i <= maxLevel; i++) {
            if (i <= thisLevel) {
                html += '<span class="typcn typcn-star-full-outline"/>';
            } else {
                html += '<span class="typcn typcn-star-outline"/>';
            }
        }
    }
    html += '</br><span class="typcn typcn-roberta"/>&emsp;&emsp;';
    html += GUISTATE_C.getMenuRobotRealName(tutorial.robot);
    $('#tutorialOverviewText').html(html);
    $('#tutorialOverviewTitle').html(tutorial.name);
    if (GUISTATE_C.isKioskMode()) {
        U.removeLinks($('#tutorialOverview a'));
    }
    $('#tutorialAbort').off('click.dismiss.bs.modal');
    $('#tutorialAbort').onWrap(
        'click.dismiss.bs.modal',
        function(event) {
            exitTutorial();
        },
        'tutorial exit'
    );
    $('#tutorialContinue').off('click.dismiss.bs.modal');
    $('#tutorialContinue').onWrap(
        'click.dismiss.bs.modal',
        function(event) {
            LOG.info('tutorial executed ' + tutorial.index + tutorialId);
        },
        'tuorial continue'
    );
    let myModal: Modal = new Modal(document.getElementById('tutorialOverview'), {
        backdrop: 'static',
        keyboard: false
    });
    myModal.show();
}

function createInstruction(): void {
    if (tutorial.step[step]) {
        $('#tutorialContent').empty();
        if (tutorial.step[step].instruction) {
            if (tutorial.step[step].toolbox) {
                try {
                    GUISTATE_C.setDynamicProgramToolbox(tutorial.step[step].toolbox);
                    PROG_C.loadExternalToolbox(tutorial.step[step].toolbox);
                    Blockly.mainWorkspace.options.maxBlocks = tutorial.step[step].maxBlocks;
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
                    for (let i: number = 0; i < tutorial.step[step].tip.length; i++) {
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
                                click: function() {
                                    showSolution();
                                }
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
                                click: function() {
                                    MSG.displayMessage(tutorial.end, 'POPUP', '');
                                    $('#show-message').oneWrap('hide.bs.modal', function(e) {
                                        exitTutorial();
                                        return false;
                                    });
                                    return false;
                                }
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
                                click: function() {
                                    createQuiz();
                                }
                            })
                        )
                );
            }
        } else {
            // apparently a step without an instruction -> go directly to the quiz
            createQuiz();
        }
        if (GUISTATE_C.isKioskMode()) {
            U.removeLinks($('#tutorialContent a'));
        }
    }
}

function createQuiz(): void {
    if (tutorial.step[step].quiz) {
        $('#tutorialContent').html('');
        $('#tutorialContent').append($('<div>').attr('class', 'quiz content'));
        tutorial.step[step].quiz.forEach(function(quiz, iQuiz): void {
            $('#tutorialContent .quiz.content').append($('<div>').attr('class', 'quiz question').append(quiz.question));
            let answers: any = shuffle(quiz.answer);
            quiz.answer.forEach(function(answer, iAnswer): void {
                let correct: boolean = answer.charAt(0) !== '!';
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
                                value: correct
                            })
                        )
                        .append(
                            $('<span>', {
                                for: iQuiz + '_' + iAnswer,
                                class: 'checkmark quiz'
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
                        click: function(): void {
                            checkQuiz();
                        }
                    })
                )
        );
    } else {
        // apparently no quiz provided, go to next step
        nextStep();
    }
}

function nextStep(): void {
    step += 1;
    if (step < maxSteps) {
        $('#tutorialHeader .active').removeClass('active');
        $('#tutorialHeader .preActive').removeClass('preActive');
        $('#tutorialHeader .step a:contains(\'' + (step + 1) + '\')')
            .parent()
            .addClass('active');
        $('#tutorialHeader .step a:contains(\'' + step + '\')')
            .parent()
            .addClass('preActive');
        createInstruction();
        if (step == maxSteps - 1 && quiz) {
            let finalMaxCredits: number = 0;
            for (let i: number = maxCredits.length; i--;) {
                if (maxCredits[i]) {
                    finalMaxCredits += maxCredits[i];
                }
            }
            let finalCredits: number = 0;
            for (let i: number = credits.length; i--;) {
                if (credits[i]) {
                    finalCredits += credits[i];
                }
            }
            let percent: number = 0;
            if (finalMaxCredits !== 0) {
                percent = Math.round((100 / finalMaxCredits) * finalCredits);
            }
            let thumbs: number = Math.round((percent - 50) / 17) + 1;
            let $quizFooter: JQuery<HTMLElement> = $('<div>')
                .attr('class', 'quiz footer')
                .attr('id', 'quizFooter')
                .append(finalCredits + ' von ' + finalMaxCredits + ' Antworten oder ' + percent + '% sind richtig! ');
            $quizFooter.insertBefore($('.quiz.continue'));
            $('#quizFooter').append(
                $('<span>', {
                    id: 'quizResult'
                })
            );
            if (percent > 0) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up'
                    })
                );
            }
            if (percent == 100) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up'
                    })
                );
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up'
                    })
                );
                $('#quizResult').append(' Spitze!');
            } else if (percent > 80) {
                $('#quizResult').append(
                    $('<span>', {
                        class: 'typcn typcn-thumbs-up'
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

function showSolution(): void {
    $('#helpDiv').append(
        $('<div>').append(tutorial.step[step].solution).attr({
            class: 'imgSol'
        })
    );
    $('#quizHelp').remove();
}

function checkQuiz(): void {
    let countCorrect: number = 0;
    let countChecked: number = 0;
    let totalQuestions: number = $('.quiz.question').length;
    let totalCorrect: number = $('.quiz.answer [value=true]').length;
    $('.quiz input').each(function(i: number, elem: HTMLElement): void {
        let $label: JQuery<HTMLElement> = $('label>span[for="' + $(this).attr('id') + '"]');
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
                click: function(): void {
                    createQuiz();
                }
            })
        );
        let confirmText: string;
        if (countChecked == 0) {
            confirmText = 'Bitte kreuze mindestens eine Anwort an.';
        } else if (totalQuestions == 1) {
            confirmText = 'Die Antwort ist leider nicht ganz richtig.';
        } else {
            confirmText = countCorrect + ' Anworten von ' + totalCorrect + ' sind richtig!';
        }
        $('#quizFooter').append(
            $('<span>', {
                text: confirmText
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
                    click: function(): void {
                        nextStep();
                    }
                })
            )
    );
    credits[step] = countCorrect;
    maxCredits[step] = totalCorrect;
}

function shuffle(answers: any[]) {
    for (let j, x, i: number = answers.length; i; j = Math.floor(Math.random() * i), x = answers[--i], answers[i] = answers[j], answers[j] = x) ;
    return answers;
}

function toggleTutorial($button?: any): void {
    if ($('#tutorialButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        $button.openRightView($('#tutorialDiv'), INITIAL_WIDTH);
    }
}

function openTutorialView(): void {
    if ($('#tutorialDiv').hasClass('rightActive')) {
        return;
    }
    if ($('#blocklyDiv').hasClass('rightActive')) {
        const waitForClose = function(): void {
            if (!$('#blocklyDiv').hasClass('rightActive')) {
                toggleTutorial();
            } else {
                setTimeout(waitForClose, 50);
            }
        };
        waitForClose();
    } else {
        toggleTutorial($('#tutorialButton'));
    }
}

function closeTutorialView(): void {
    if ($('.rightMenuButton.rightActive').length >= 0) {
        $('.rightMenuButton.rightActive').clickWrap();
    }
}

function exitTutorial(): void {
    $('#tutorial-navigation, #tutorialEnd, #tutorialButton').hide();
    $('#head-navigation, #tab-navigation').show();
    $('.blocklyToolboxDiv>.levelTabs').removeClass('invisible');
    GUISTATE_C.resetDynamicProgramToolbox();
    PROG_C.loadExternalToolbox(GUISTATE_C.getProgramToolbox());
    Blockly.mainWorkspace.options.maxBlocks = undefined;
    if (GUISTATE_C.isKioskMode()) {
        $('.modal').modal('hide');
        loadFromTutorial(tutorialId);
    } else {
        closeTutorialView();
        //@ts-ignore
        $('#tabTutorialList').tabWrapShow();
    }
}
