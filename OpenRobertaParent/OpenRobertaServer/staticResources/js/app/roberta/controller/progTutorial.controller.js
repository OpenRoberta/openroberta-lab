define([ 'exports', 'comm', 'message', 'log', 'guiState.controller', 'program.controller', 'blocks', 'jquery' ],
    function(exports, COMM, MSG, LOG, GUISTATE_C, PROG_C, Blockly, $) {

        var blocklyWorkspace;
        var tutorialList;
        var tutorial;
        var step = 0;
        var maxSteps = 0;
        var credits = [];
        var maxCredits = [];

        function init() {
            blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
            tutorialList = GUISTATE_C.getListOfTutorials();
            for (var tutorial in tutorialList) {
                if (tutorialList.hasOwnProperty(tutorial)) {
                    $("#head-navigation-tutorial-dropdown").append("<li class='" + tutorialList[tutorial].language + " " + tutorialList[tutorial].robot + "'><a href='#' id='" + tutorial + "' class='menu tutorial typcn typcn-contacts'>" + tutorialList[tutorial].name + "</a></li>");
                }
            }
            GUISTATE_C.updateTutorialMenu();
            initEvents();
        }
        exports.init = init;

        function initEvents() {
            $(".menu.tutorial").on("click", function(event) {
                startTutorial(event.target.id);
            });
            $('#progTutorial').on('click touchend', function() {
                toggleTutorial();
                return false;
            });
        }

        function startTutorial(tutorialId) {
            // initialize this tutorial
            tutorial = tutorialList[tutorialId];
            maxSteps = tutorial.step.length;
            step = 0;
            credits = [];
            maxCredits = [];
            $('#tutorial-list').empty();

            // create this tutorial navigation
            for (var i = 0; i < tutorial.step.length; i++) {
                $('#tutorial-list').append(
                    $('<li>').attr('class', 'step').append(
                        $('<a>').attr({
                            'href' : '#'
                        }).append(i + 1)));
            }
            $('#tutorial-list').append(
                $('<li>').attr('id', 'tutorialEnd').append(
                    $('<a>').attr({
                        'href' : '#',
                        'class' : 'typcn typcn-delete'
                    })));

            // prepare the view
            $('#tutorial-navigation').fadeIn(750);
            $('#head-navigation').fadeOut(750);

            $('#tutorial-list :first-child').addClass('active');
            $('#progTutorial').show();
            $('.blocklyToolboxDiv>.levelTabs').addClass('invisible');

            initStepEvents();
            createInstruction();
            openTutorialView();
            showOverview();
        }

        function initStepEvents() {
            $("#tutorial-list.nav li.step a").on("click", function() {
                Blockly.hideChaff();
                $("#tutorial-navigation").find(".active").removeClass("active");
                step = $(this).text() - 2;
                nextStep();
                openTutorialView();
            });
            $("#tutorialEnd").on("click", function() {
                exitTutorial();
            });
        }

        function showOverview() {
            if (!tutorial.overview)
                return;
            $('#tutorialOverviewText').html(tutorial.overview);
            $('#tutorialOverviewTitle').html(tutorial.name);
            $('#tutorialOverview').modal();
            $('#tutorialOverview').on('click.dismiss.bs.modal', function(event) {
                if (event.target.id === 'tutorialContinue') {
                    return false;
                }
                exitTutorial();
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
                    $('#tutorialContent').append(
                        tutorial.step[step].instruction);
                    if (step == maxSteps - 1) { // last step
                        $('#tutorialContent').append($('<p>').attr('class', 'quiz continue').append($('<button>', {
                            'text' : 'Tutorial beenden',
                            'class' : 'btn',
                            'click' : function() {
                                MSG.displayMessage(tutorial.end, "POPUP", "");
                                $("#tutorialEnd").trigger("click");
                            }
                        })));
                    } else {
                        $('#tutorialContent').append($('<p>').attr('class', 'quiz continue').append($('<button>', {
                            'text' : 'weiter',
                            'class' : 'btn',
                            'click' : function() {
                                createQuiz();
                            }
                        })));
                    }
                    if (tutorial.step[step].solution) {
                        $('#tutorialContent .quiz.continue').append($('<button>', {
                            'text' : 'Hilfe',
                            'id' : 'quizHelp',
                            'class' : 'btn test',
                            'click' : function() {
                                showSolution();
                            }
                        }));
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
                $("#tutorial-navigation").find(".active").removeClass("active");
                $("#tutorial-navigation .step a:contains('" + (step + 1) + "')").parent().addClass("active");
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
                    var percent = Math.round(100 / finalMaxCredits * finalCredits);
                    var thumbs = Math.round((percent - 50) / 17) + 1;
                    var $quizFooter = $('<div>').attr('class', 'quiz footer').attr('id', 'quizFooter').append(finalCredits + ' von ' +
                        finalMaxCredits + ' Antworten oder ' + percent + '% sind richtig!<br>');
                    $quizFooter.insertBefore($('.quiz.continue'));
                    $('#quizFooter').append($('<div>', {
                        'style' : 'text-align:center; font-size:30px; margin-top:32px',
                        'id' : 'quizResult'
                    }));
                    $('#quizResult').append($('<span>', {
                        'class' : 'typcn typcn-thumbs-up'
                    }));
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
                    } else {
                        $('#quizResult').append(' Das ist ok!');
                    }
                }
            } else {
                // end of the tutorial                
            }
        }

        function showSolution() {
            $('#tutorialContent').append($('<div>').append($('<img>').attr({
                'src' : 'tutorial/img/' + tutorial.step[step].solution + '.png',
                'class' : 'imgSol'
            })));
            $('#quizHelp').remove();
        }


        function checkQuiz() {
            var countCorrect = 0;
            var countTotal = 0;
            countTotal = $('.quiz.question').length;
            $('.quiz input').each(function(i, elem) {
                $this = $(this);
                $label = $('label>span[for="' + $this.attr('id') + '"]');
                if ($(this).val() === 'true' && $(this).is(':checked')) {
                    $label.parent().addClass('correct');
                    countCorrect++;
                } else if ($(this).val() === 'false' && !$(this).is(':checked')) {
                    // $label.parent().addClass('correct');
                    // countCorrect++;
                } else {
                    $label.parent().addClass('fail');
                }
                $(this).attr('onclick', 'return false;');
            });
            $('#quizFooter').html('');
            if (countCorrect !== countTotal) {
                $('#quizFooter').append($('<button/>', {
                    text : 'nochmal',
                    'class' : 'btn test left',
                    click : function() {
                        createQuiz();
                    }
                }));
            }
            var confirmText;
            if (countTotal == 1) {
                if (countCorrect == 1) {
                    confirmText = 'Die Antwort ist richtig!';
                } else {
                    confirmText = 'Die Antwort ist leider falsch.';
                }
            } else {
                confirmText = countCorrect + ' Anworten von ' + countTotal + ' sind richtig!';
            }
            $('#quizFooter').append(confirmText).append($('<button/>', {
                text : 'weiter',
                'class' : 'btn right',
                click : function() {
                    nextStep();
                }
            }));
            credits[step] = countCorrect;
            maxCredits[step] = countTotal;
        }


        function shuffle(answers) {
            for (var j, x, i = answers.length; i; j = Math.floor(Math.random() * i), x = answers[--i], answers[i] = answers[j], answers[j] = x)
                ;
            return answers;
        }

        function toggleTutorial() {
            Blockly.hideChaff();
            if ($('#blocklyDiv').hasClass('rightActive')) {
                $('.blocklyToolboxDiv').css('display', 'inherit');
                Blockly.svgResize(blocklyWorkspace);
                $('#progTutorial').animate({
                    right : '0px',
                }, {
                    duration : 750
                });
                $('#blocklyDiv').animate({
                    width : '100%'
                }, {
                    duration : 750,
                    step : function() {
                        $(window).resize();
                        Blockly.svgResize(blocklyWorkspace);
                    },
                    done : function() {
                        $('#blocklyDiv').removeClass('rightActive');
                        $('#tutorialDiv').removeClass('rightActive');
                        $(window).resize();
                        Blockly.svgResize(blocklyWorkspace);
                        $('#sliderDiv').hide();
                        $('#progTutorial').removeClass('shifted');
                    }
                });
            } else {
                $('#blocklyDiv').addClass('rightActive');
                $('#tutorialDiv').addClass('rightActive');
                var width;
                var smallScreen;
                if ($(window).width() < 768) {
                    smallScreen = true;
                    width = 52;
                } else {
                    smallScreen = false;
                    width = $('#blocklyDiv').width() * 0.7;
                }
                $('#progTutorial').animate({
                    right : $('#blocklyDiv').width() - width + 4,
                }, {
                    duration : 750
                });
                $('#blocklyDiv').animate({
                    width : width
                }, {
                    duration : 750,
                    step : function() {
                        $(window).resize();
                        Blockly.svgResize(blocklyWorkspace);
                    },
                    done : function() {
                        if (smallScreen) {
                            $('.blocklyToolboxDiv').css('display', 'none');
                        }
                        $('#sliderDiv').css({
                            'left' : width - 10
                        });
                        $('#sliderDiv').show();
                        $('#progTutorial').addClass('shifted');
                        $(window).resize();
                        Blockly.svgResize(blocklyWorkspace);
                    }
                });
            }
        }

        function openTutorialView() {
            if ($('#tutorialDiv').hasClass('rightActive')) {
                return;
            }
            if ($('.rightMenuButton.shifted').length >= 0) {
                function waitForClose() {
                    if ($('.rightMenuButton.shifted').length <= 0) {
                        toggleTutorial();
                    } else {
                        setTimeout(waitForClose, 50);
                    }
                }
                waitForClose();
                $('.rightMenuButton.shifted').trigger('click');
            } else {
                toggleTutorial();
            }
        }

        function closeTutorialView() {
            if ($('.rightMenuButton.shifted').length >= 0) {
                $('.rightMenuButton.shifted').trigger('click');
            }
        }

        function exitTutorial() {
            Blockly.hideChaff();
            closeTutorialView();
            $('#tutorial-navigation').fadeOut(750);
            $('#head-navigation').fadeIn(750);
            $('#progTutorial').fadeOut();
            $('.blocklyToolboxDiv>.levelTabs').removeClass('invisible');
            PROG_C.loadExternalToolbox(GUISTATE_C.getProgramToolbox());
        }
    });
