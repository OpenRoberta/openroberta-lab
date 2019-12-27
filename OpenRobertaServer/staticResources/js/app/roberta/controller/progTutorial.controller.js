define([ 'exports', 'comm', 'message', 'log', 'guiState.controller', 'program.controller', 'robot.controller', 'blocks', 'jquery' ], function(exports, COMM,
        MSG, LOG, GUISTATE_C, PROG_C, ROBOT_C, Blockly, $) {

    const INITIAL_WIDTH = 0.5;
    var blocklyWorkspace;
    var tutorialList;
    var tutorial;
    var step = 0;
    var maxSteps = 0;
    var credits = [];
    var maxCredits = [];

    function init() {
        tutorialList = GUISTATE_C.getListOfTutorials();
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initEvents();
    }
    exports.init = init;

    function initEvents() {
        $(".menu.tutorial").on("click", function(event) {
            startTutorial(event.target.id);
        });
        $('#tutorialButton').on('click touchend', function() {
            toggleTutorial();
            return false;
        });
    }

    function loadFromTutorial(tutorialId) {
        // initialize this tutorial       
        tutorial = tutorialList[tutorialId];
        if (tutorial) {
            ROBOT_C.switchRobot(tutorial.robot, null, startTutorial);
        }

        function startTutorial() {
            $('#tabProgram').trigger('click');
            maxSteps = tutorial.step.length;
            step = 0;
            credits = [];
            maxCredits = [];
            $('#tutorial-list').empty();

            // create this tutorial navigation
            for (var i = 0; i < tutorial.step.length; i++) {
                $('#tutorial-list').append($('<li>').attr('class', 'step').append($('<a>').attr({
                    'href' : '#'
                }).append(i + 1)));
            }
            $('#tutorial-list li:last-child').addClass('last');
            $('#tutorial-header').html(tutorial.name);

            // prepare the view
            $('#tutorial-navigation').fadeIn(750);
            $('#head-navigation').fadeOut(750);

            $('#tutorial-list :first-child').addClass('active');
            $('#tutorialButton').show();
            $('.blocklyToolboxDiv>.levelTabs').addClass('invisible');

            initStepEvents();
            createInstruction();
            openTutorialView();
            showOverview();
        }
    }
    exports.loadFromTutorial = loadFromTutorial;

    function initStepEvents() {
        $("#tutorial-list.nav li.step a").on("click", function() {
            Blockly.hideChaff();
            step = $(this).text() - 2;
            nextStep();
            openTutorialView();
        });
        $("#tutorialEnd").one("click", function() {
            exitTutorial();
        });
    }

    function showOverview() {
        if (!tutorial.overview)
            return;
        $('#tutorialOverviewText').html(tutorial.overview.description + '</br></br><b>Lernziel: </b>' + tutorial.overview.goal
                + '</br></br><b>Vorkenntnisse: </b>' + tutorial.overview.previous);
        $('#tutorialOverviewTitle').html(tutorial.name);
        $('#tutorialOverview').modal();
        $('#tutorialOverview.modal').one('click.dismiss.bs.modal', function(event) {
            if (event.target.id === 'tutorialContinue') {
                return false;
            }
            exitTutorial();
            return false;
        });
    }

    function createInstruction() {
        if (tutorial.step[step]) {
            $('#tutorialContent').empty();
            if (tutorial.step[step].instruction) {
                if (tutorial.step[step].toolbox) {
                    try {
                        PROG_C.loadExternalToolbox(tutorial.step[step].toolbox);
                        Blockly.mainWorkspace.options.maxBlocks = tutorial.step[step].maxBlocks;
                    } catch (e) {
                        console.log(e);
                    }
                }
                if (tutorial.step[step].header) {
                    $('#tutorialContent').append($('<h3>').attr('class', 'quiz header').append(tutorial.step[step].header));
                }
                if (tutorial.step[step].tip) {
                    $('#tutorialContent').append($('<div>').attr('class', 'quiz tip').append(tutorial.step[step].tip));
                }
                $('#tutorialContent').append(tutorial.step[step].instruction);
                if (tutorial.step[step].solution) {
                    $('#tutorialContent').append($('<div>').attr('id', 'helpDiv').append($('<button>', {
                        'text' : 'Hilfe',
                        'id' : 'quizHelp',
                        'class' : 'btn test',
                        'click' : function() {
                            showSolution();
                        }
                    })));
                }
                if (step == maxSteps - 1) { // last step
                    $('#tutorialContent').append($('<div>').attr('class', 'quiz continue').append($('<button>', {
                        'text' : 'Tutorial beenden',
                        'class' : 'btn',
                        'click' : function() {
                            MSG.displayMessage(tutorial.end, "POPUP", "");
                            $(".modal").one('hide.bs.modal', function(e) {
                                $("#tutorialEnd").trigger("click");
                                return false;
                            });
                            return false;
                        }
                    })));
                } else {
                    $('#tutorialContent').append($('<div>').attr('class', 'quiz continue').append($('<button>', {
                        'text' : 'weiter',
                        'class' : 'btn',
                        'click' : function() {
                            createQuiz();
                        }
                    })));
                }
            } else {
                // apparently a step without an instruction -> go directly to the quiz
                createQuiz();
            }
        }
    }

    function createQuiz() {
        if (tutorial.step[step].quiz) {
            $('#tutorialContent').html('');
            $('#tutorialContent').append($('<div>').attr('class', 'quiz content'));
            tutorial.step[step].quiz.forEach(function(quiz, iQuiz) {
                $('#tutorialContent .quiz.content').append($('<div>').attr('class', 'quiz question').append(quiz.question));
                var answers = shuffle(quiz.answer);
                quiz.answer.forEach(function(answer, iAnswer) {
                    var correct = answer.charAt(0) !== '!';
                    if (!correct) {
                        answer = answer.substr(1);
                    }
                    $('#tutorialContent .quiz.content').append($('<label>').attr('class', 'quiz answer').append(answer).append($('<input>', {
                        'type' : 'checkbox',
                        'class' : 'quiz',
                        'name' : 'answer_' + iAnswer,
                        'id' : iQuiz + '_' + iAnswer,
                        'value' : correct,
                    })).append($('<span>', {
                        'for' : iQuiz + '_' + iAnswer,
                        'class' : 'checkmark quiz'
                    })));
                });
            });
            $('#tutorialContent .quiz.content').append($('<div>').attr('class', 'quiz footer').attr('id', 'quizFooter').append($('<button/>', {
                text : 'prüfen!',
                'class' : 'btn test left',
                click : function() {
                    checkQuiz();
                }
            })));
        } else {
            // apparently no quiz provided, go to next step
            nextStep();
        }
    }

    function nextStep() {
        step += 1;
        if (step < maxSteps) {
            $("#tutorial-list .active").removeClass("active");
            $("#tutorial-list .preActive").removeClass("preActive");
            $("#tutorial-list .step a:contains('" + (step + 1) + "')").parent().addClass("active");
            $("#tutorial-list .step a:contains('" + (step) + "')").parent().addClass("preActive");
            createInstruction();
            if (step == maxSteps - 1) {
                var finalMaxCredits = 0;
                for (var i = maxCredits.length; i--;) {
                    if (maxCredits[i]) {
                        finalMaxCredits += maxCredits[i];
                    }
                }
                var finalCredits = 0;
                for (var i = credits.length; i--;) {
                    if (credits[i]) {
                        finalCredits += credits[i]
                    }
                }
                var percent = 100;
                if (finalMaxCredits !== 0) {
                    percent = Math.round(100 / finalMaxCredits * finalCredits);
                }
                var thumbs = Math.round((percent - 50) / 17) + 1;
                var $quizFooter = $('<div>').attr('class', 'quiz footer').attr('id', 'quizFooter').append(finalCredits + ' von ' + finalMaxCredits
                        + ' Antworten oder ' + percent + '% sind richtig!<br>');
                $quizFooter.insertBefore($('.quiz.continue'));
                $('#quizFooter').append($('<div>', {
                    'style' : 'text-align:center; font-size:30px; margin-top:32px',
                    'id' : 'quizResult'
                }));
                if (percent > 0) {
                    $('#quizResult').append($('<span>', {
                        'class' : 'typcn typcn-thumbs-up'
                    }));
                }
                if (percent == 100) {
                    $('#quizResult').append($('<span>', {
                        'class' : 'typcn typcn-thumbs-up'
                    }));
                    $('#quizResult').append($('<span>', {
                        'class' : 'typcn typcn-thumbs-up'
                    }));
                    $('#quizResult').append(' Spitze!');
                } else if (percent > 80) {
                    $('#quizResult').append($('<span>', {
                        'class' : 'typcn typcn-thumbs-up'
                    }));
                    $('#quizResult').append(' Super!');
                } else if (percent > 60) {
                    $('#quizResult').append(' Gut gemacht!');
                } else if (percent == 0) {
                    $('#quizResult').append(' Du kannst die Quizzfragen jederzeit wiederholen, wenn du möchtest!');
                } else {
                    $('#quizResult').append(' Das ist ok!');
                }
            }
        } else {
            // end of the tutorial                
        }
    }

    function showSolution() {
        $('#helpDiv').append($('<div>').append(tutorial.step[step].solution).attr({
            'class' : 'imgSol'
        }));
        $('#quizHelp').remove();
    }

    function checkQuiz() {
        var countCorrect = 0;
        var totalQuestions = $('.quiz.question').length;
        var totalCorrect = $('.quiz.answer [value=true').length;
        $('.quiz input').each(function(i, elem) {
            $this = $(this);
            $label = $('label>span[for="' + $this.attr('id') + '"]');
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
                text : 'nochmal',
                'class' : 'btn test',
                click : function() {
                    createQuiz();
                }
            }));
            var confirmText;
            if (totalQuestions == 1) {
                confirmText = 'Die Antwort ist leider nicht ganz richtig.';
            } else {
                confirmText = countCorrect + ' Anworten von ' + totalCorrect + ' sind richtig!';
            }
            $('#quizFooter').append($('<span>', {
                text : confirmText,
            }));
        }
        $('#tutorialContent').append($('<div>').attr('class', 'quiz continue').append($('<button>', {
            'text' : 'weiter',
            'class' : 'btn',
            'click' : function() {
                nextStep();
            }
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
        $('#tabTutorialList').trigger('click');
    }
});
