define( ['exports', 'util', 'log', 'message', 'program.controller', 'program.model', 'socket.controller', 'guiState.controller', 'webview.controller',
    'interpreter.interpreter', 'interpreter.robotWeDoBehaviour', 'interpreter.robotWeDoBehaviourTest', 'jquery'], function( exports, UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, GUISTATE_C, WEBVIEW_C,
        WEDO_I, WEDO_N, WEDO_T, $ ) {

        var blocklyWorkspace;
        var interpreter;
        /**
         *
         */
        function init( workspace ) {
            blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
            //initView();
            initEvents();
        }
        exports.init = init;

        function initEvents() {
            Blockly.bindEvent_( blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function( e ) {
                LOG.info( 'runOnBrick from blockly button' );
                runOnBrick();
                return false;
            } );
            Blockly.bindEvent_( blocklyWorkspace.robControls.stopBrick, 'mousedown', null, function( e ) {
                LOG.info( 'stopBrick from blockly button' );
                stopBrick();
                return false;
            } );
            if ( GUISTATE_C.getConnection() != 'autoConnection' ) {
                blocklyWorkspace.robControls.disable( 'runOnBrick' );
            }
        }

        /**
         * Start the program on the brick
         */
        function runOnBrick() {
            GUISTATE_C.setConnectionState( "busy" );
            LOG.info( 'run ' + GUISTATE_C.getProgramName() + 'on brick' );
            var xmlProgram = Blockly.Xml.workspaceToDom( blocklyWorkspace );
            var xmlTextProgram = Blockly.Xml.domToText( xmlProgram );

            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

            var language = GUISTATE_C.getLanguage();

            var connectionType = GUISTATE_C.getConnectionTypeEnum();
            if ( GUISTATE_C.getConnection() == connectionType.AUTO || GUISTATE_C.getConnection() == connectionType.LOCAL ) {
                PROGRAM.runOnBrickBack( GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function( result ) {
                    runForAutoConnection( result );
                    PROG_C.reloadProgram( result );
                } );
            } else if ( GUISTATE_C.getConnection() == connectionType.AGENT || GUISTATE_C.getConnection() == connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent() ) {
                PROGRAM.runOnBrickBack( GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function( result ) {
                    runForAgentConnection( result );
                    PROG_C.reloadProgram( result );
                } );
            } else if ( GUISTATE_C.getConnection() == connectionType.WEBVIEW ) {
                PROGRAM.runOnBrickBack(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function (result) {
                    runForWebviewConnection(result);
                    PROG_C.reloadProgram(result);
                });
            } else if ( GUISTATE_C.getConnection() == connectionType.JSPLAY ) {
                //For all robots that play their program file in the browser
                PROGRAM.runOnBrickBack( GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function( result ) {
                    runForJSPlayConnection( result );
                    PROG_C.reloadProgram( result );
                } );
            } else {
                PROGRAM.runOnBrick( GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function( result ) {
                    runForToken( result );
                    PROG_C.reloadProgram( result );
                } );
            }
        }
        exports.runOnBrick = runOnBrick;

    function runForAutoConnection(result) {
        GUISTATE_C.setState(result);
        if (result.rc == "ok") {
            var filename = GUISTATE_C.getProgramName();
            if (GUISTATE_C.getRobot() === 'sensebox') {
                filename += '.bin';
                result.compiledCode = UTIL.base64decode(result.compiledCode);
            } else if (GUISTATE_C.getRobot() === 'ev3c4ev3') {
                filename += '.uf2';
                result.compiledCode = UTIL.base64decode(result.compiledCode);
                // TODO: Update the popup message to tell the user to start the program (selecting it in the EV3 menu)
            } else {
                filename += '.hex';
            }

            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) != null) {
                // either the user doesn't want to see the modal anymore or he uses a smartphone / tablet, where you cannot choose the download folder.
                UTIL.download(filename, result.compiledCode);
                setTimeout(function() {
                    GUISTATE_C.setConnectionState("wait");
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else if (GUISTATE_C.getConnection() == GUISTATE_C.getConnectionTypeEnum().LOCAL) {
                setTimeout(function() {
                    GUISTATE_C.setConnectionState("wait");
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else {
                createDownloadLink(filename, result.compiledCode);

                var textH = $("#popupDownloadHeader").text();
                $("#popupDownloadHeader").text(textH.replace("$", $.trim(GUISTATE_C.getRobotRealName())));
                for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                    var step = $('<li class="typcn typcn-roberta">');
                    var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] || Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]
                            || 'POPUP_DOWNLOAD_STEP_' + i;
                        step.html( '<span class="download-message">' + a + '</span>' );
                        step.css( 'opacity', '0' );
                        $( '#download-instructions' ).append( step );
                }

                var substituteName = GUISTATE_C.getRobotGroup().toUpperCase();
                $( "#download-instructions li" ).each( function( index ) {
                    if ( GUISTATE_C.getRobotGroup() === "calliope" ) {
                        substituteName = "MINI";
                    }
                    $( this ).html( $( this ).html().replace( "$", substituteName ) );
                } );

                $( "#save-client-compiled-program" ).one( "shown.bs.modal", function( e ) {
                    $( '#download-instructions li' ).each( function( index ) {
                        $( this ).delay( 750 * index ).animate( {
                            opacity: 1
                        }, 1000 );
                    } );
                } );

                $( '#save-client-compiled-program' ).one( 'hidden.bs.modal', function( e ) {
                    var textH = $( "#popupDownloadHeader" ).text();
                    $( "#popupDownloadHeader" ).text( textH.replace( $.trim( GUISTATE_C.getRobotRealName() ), "$" ) );
                    if ( $( '#label-checkbox' ).is( ':checked' ) ) {
                        GUISTATE_C.setProgramToDownload();
                    }
                    $( '#programLink' ).remove();
                    $( '#download-instructions' ).empty();
                    GUISTATE_C.setConnectionState( "wait" );
                    MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot() );
                } );

                $( '#save-client-compiled-program' ).modal( 'show' );
            }
        } else {
            GUISTATE_C.setConnectionState( "wait" );
            MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot() );
        }
        }

    /**
     * Creates the pop-up for robots that play sound inside the browser instead of downloading a file (f.e. Edison)
     * This function is very similar to runForAutoConnection, but instead of a download link a Play button is created.
     * Also, some parts of the autoConnection pop-up are hidden:
     *  - the "I've changed my download folder" checkbox
     *  - the "OK" button in the footer
     *
     * @param result the result that is received from the server after sending the program to it
     */
    function runForJSPlayConnection( result ) {
        if ( result.rc != "ok" ) {
            GUISTATE_C.setConnectionState( "wait" );
            MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot() );
        } else {
            wavFileContent = UTIL.base64decode(result.compiledCode);
            $( '#changedDownloadFolder' ).addClass( 'hidden' );

            //This detects IE11 (and IE11 only), see: https://developer.mozilla.org/en-US/docs/Web/API/Window/crypto
            if (window.msCrypto) {
                //Internet Explorer (all ver.) does not support playing WAV files in the browser
                //If the user uses IE11 the file will not be played, but downloaded instead
                //See: https://caniuse.com/#feat=wav, https://www.w3schools.com/html/html5_audio.asp
                createDownloadLink(GUISTATE_C.getProgramName() + '.wav', wavFileContent);
            } else {
                //All non-IE browsers can play WAV files in the browser, see: https://www.w3schools.com/html/html5_audio.asp
                $( '#OKButtonModalFooter' ).addClass( 'hidden' );
                createPlayButton(GUISTATE_C.getProgramName(), wavFileContent);
            }

            var textH = $("#popupDownloadHeader").text();
            $("#popupDownloadHeader").text(textH.replace("$", $.trim(GUISTATE_C.getRobotRealName())));
            for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                var step = $('<li class="typcn typcn-roberta">');
                var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] || Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]
                    || 'POPUP_DOWNLOAD_STEP_' + i;
                step.html( '<span class="download-message">' + a + '</span>' );
                step.css( 'opacity', '0' );
                $( '#download-instructions' ).append( step );
            }

            $( "#save-client-compiled-program" ).one( "shown.bs.modal", function( e ) {
                $( '#download-instructions li' ).each( function( index ) {
                    $( this ).delay( 750 * index ).animate( {
                        opacity: 1
                    }, 1000 );
                } );
            } );

            $( '#save-client-compiled-program' ).one( 'hidden.bs.modal', function( e ) {
                var textH = $( "#popupDownloadHeader" ).text();
                $( "#popupDownloadHeader" ).text( textH.replace( $.trim( GUISTATE_C.getRobotRealName() ), "$" ) );
                $( '#programLink' ).remove();
                $( '#download-instructions' ).empty();
                GUISTATE_C.setConnectionState( "wait" );
                MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot() );

                //Un-hide the div if it was hidden before
                $( '#changedDownloadFolder' ).removeClass( 'hidden' );
                $( '#OKButtonModalFooter' ).removeClass( 'hidden' );
            } );

            $( '#save-client-compiled-program' ).modal( 'show' );
        }
    }

        function runForAgentConnection( result ) {
            $( '#menuRunProg' ).parent().addClass( 'disabled' );
            $( '#head-navi-icon-robot' ).addClass( 'busy' );
            GUISTATE_C.setState( result );
            if ( result.rc == "ok" ) {
                SOCKET_C.uploadProgram( result.compiledCode, GUISTATE_C.getRobotPort() );
                setTimeout( function() {
                    GUISTATE_C.setConnectionState( "error" );
                }, 5000 );
            } else {
                GUISTATE_C.setConnectionState( "error" );
            }
            MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName() );
        }

        function runForToken( result ) {
            GUISTATE_C.setState( result );
            MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot() );
            if ( result.rc == "ok" ) {
                if ( Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()] ) {
                    MSG.displayMessage( 'MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST' );
                }
            } else {
                GUISTATE_C.setConnectionState( "error" );
            }
        }

        function switch2blockly() {
            GUISTATE_C.setConnectionState( "wait" );
            blocklyWorkspace.robControls.switchToStart();
        }

        function stopBrick() {
            if ( interpreter !== null ) {
                interpreter.terminate();
            }
        }

        /**
         * after the duration specified, call the callback function given. The duration is partitioned into 100 millisec intervals to allow termination of the running interpreter during
         * a timeout. Be careful: the termination is NOT effected here, but by the callback function (this should be @see evalOperation() in ALMOST ALL cases)
         *
         * . @param callback called when the time has elapsed
         *
         * . @param durationInMilliSec time that should elapse before the callback is called
         */
        function timeout(callback, durationInMilliSec) {
            if ( durationInMilliSec > 100 ) {
                // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
                durationInMilliSec -= 100;
                setTimeout(this.timeout, 100, callback, durationInMilliSec);
             } else {
                // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
                setTimeout(callback, durationInMilliSec);
            }
        }

        function runStepWedo( interpreter ) {
            while ( !interpreter.isTerminated() && !reset ) {
                var waitTime = interpreter.run( 100 );
                if ( waitTime > 0 ) {
                    timeout( runStepWedo, waitTime );
                }
            }
        }

        function runForWebviewConnection( result ) {
            if ( result.rc === "ok" ) {
                var programSrc = result.compiledCode;
                var program = JSON.parse( programSrc );
                var ops = program.ops;
                var functionDeclaration = program.functionDeclaration;
                switch ( GUISTATE_C.getRobot() ) {
                    case "wedo":
                        interpreter = new WEDO_I.Interpreter( program, new WEDO_N.NativeWeDo() );
                        if ( interpreter !== null ) {
                            GUISTATE_C.setConnectionState( "busy" );
                            blocklyWorkspace.robControls.switchToStop();
                            try {
                                runStepWedo( interpreter );
                            } catch ( error ) {
                                interpreter.terminate();
                                interpreter = null;
                                alert( error );
                            }
                        }
                        break;
                    default:
                    // TODO
                }
                MSG.displayInformation( result, result.message, result.message, GUISTATE_C.getProgramName() );
            }
        }

    /**
     * Creates a blob from the program content for file download and a click-able html download link for the blob:
     * <a download="PROGRAM_NAME" href="CONTENT_AS_BLOB" style="font-size:36px">PROGRAM_NAME</a>
     *
     * This is needed f.e. for Calliope where the file has to be downloaded and copied onto the brick manually
     *
     * @param fileName the file name (for PROGRAM_NAME)
     * @param content for the blob (for CONTENT_AS_BLOB)
     */
    function createDownloadLink( fileName, content ) {
        if ( !( 'msSaveOrOpenBlob' in navigator ) ) {
            $( '#trA' ).removeClass( 'hidden' );
        } else {
            $( '#trA' ).addClass( 'hidden' );
            UTIL.download( fileName, content );
            GUISTATE_C.setConnectionState( "error" );
        }

        if ( 'Blob' in window ) {
            var contentAsBlob = new Blob( [content], {
                type: 'application/octet-stream'
            } );
            if ( 'msSaveOrOpenBlob' in navigator ) {
                navigator.msSaveOrOpenBlob( contentAsBlob, fileName );
            } else {
                var downloadLink = document.createElement( 'a' );
                downloadLink.download = fileName;
                downloadLink.innerHTML = fileName;
                downloadLink.href = window.URL.createObjectURL( contentAsBlob );
            }
        } else {
            var downloadLink = document.createElement( 'a' );
            downloadLink.setAttribute( 'href', 'data:text/plain;charset=utf-8,' + encodeURIComponent( content ) );
            downloadLink.setAttribute( 'download', fileName );
            downloadLink.style.display = 'none';
        }

        //create link with content
        if ( downloadLink && !( 'msSaveOrOpenBlob' in navigator ) ) {
            var programLinkDiv = document.createElement( 'div' );
            programLinkDiv.setAttribute( 'id', 'programLink' );
            var linebreak = document.createElement( 'br' );
            programLinkDiv.setAttribute( 'style', 'text-align: center;' );
            programLinkDiv.appendChild( linebreak );
            programLinkDiv.appendChild( downloadLink );
            downloadLink.setAttribute( 'style', 'font-size:36px' );
            $( '#downloadLink' ).append( programLinkDiv );
        }
    }


    /**
     * Creates a WAV file blob from the program content and creates a Play button so that the sound can be played inside the browser:
     *
     * <button type="button" class="btn btn-primary" style="font-size:36px">
     *     <span class="typcn typcn-media-play" style="color : black"></span>
     * </button>
     *
     *
     * @param fileName the name of the program
     * @param content the content of the WAV file as a Base64 encoded String
     */
    function createPlayButton(fileName, content) {
        $('#trA').removeClass('hidden');

        if ( 'Blob' in window ) {
            //Create a new blob from the file content
            var contentAsBlob = new Blob( [content], {
                type: 'audio/wav'
            } );
            //Create a bootstrap button
            var playButton = document.createElement( 'button' );
            playButton.setAttribute( 'type', 'button' );
            playButton.setAttribute( 'class', 'btn btn-primary' );
            playButton.setAttribute( 'data-dismiss', 'modal' );
            playButton.onclick = function () {
                //Play the newly created blob with the WAV file content
                new Audio(window.URL.createObjectURL( contentAsBlob )).play();
            };

            //Create the play icon inside the button
            var playIcon = document.createElement( 'span' );
            playIcon.setAttribute( 'class', 'typcn typcn-media-play' );
            playIcon.setAttribute( 'style', 'color : black' );
            playButton.appendChild( playIcon );
        }

        if ( playButton ) {
            var programLinkDiv = document.createElement( 'div' );
            programLinkDiv.setAttribute( 'id', 'programLink' );
            programLinkDiv.setAttribute( 'style', 'text-align: center;' );
            programLinkDiv.appendChild( document.createElement( 'br' ) );
            programLinkDiv.appendChild( playButton );
            playButton.setAttribute( 'style', 'font-size:36px' );
            $( '#downloadLink' ).append( programLinkDiv );
        }
    }
} );
