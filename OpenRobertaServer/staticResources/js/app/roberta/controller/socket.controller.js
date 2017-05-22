define([ 'exports', 'util', 'log', 'message', 'jquery', 'robot.controller', 'guiState.controller', 'guiState.model', 'socket.io' ], function(exports, UTIL,
        LOG, MSG, $, ROBOT_C, GUISTATE_C, GUISTATE, IO) {

    var socket = IO('ws://localhost:8991/');
    console.log(socket);
    var portList = [];
    var vendorList = [];
    var productList = [];
    var system;
    var cmd;
    var port;
    var robotList = [];

    socket.on('connect', function() {
        console.log('connect');
        socket.emit('command', 'log on');
        socket.emit('command', 'list');
        console.log('listed');
        window.setInterval(function() {
            portList = [];
            vendorList = [];
            productList = [];
            robotList = [];
            socket.emit('command', 'list');
            console.log('refreshed robot ports');
        }, 3000);
    });

    /*
     * Vendor and Product IDs for some robots Botnroll: /dev/ttyUSB0, VID:
     * 0x10c4, PID: 0xea60 Mbot: /dev/ttyUSB0, VID: 0x1a86, PID: 0x7523
     * ArduinoUno: /dev/ttyACM0, VID: 0x2a03, PID: 0x0043
     */
    socket.on('message', function(data) {
        if (data.includes('"Network": false')) {
            var robot;
            jsonObject = JSON.parse(data);
            jsonObject['Ports'].forEach(function(port) {
                portList.push(port['Name']);
                vendorList.push(port['VendorID']);
                productList.push(port['ProductID']);
                console.log(port['VendorID'].toUpperCase());
                switch (port['VendorID']) {
                case '0x10C4':
                    robot = 'Bot\'n Roll';
                    break;
                case '0x1A86':
                    robot = 'MBot';
                    break;
                case '0x2A03':
                    robot = 'Arduino Uno';
                    break;
                default:
                    robot = 'Unknown robot';
                }
                robotList.push(robot);
            });

            if (portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
                if (GUISTATE_C.getRobotPort() != "") {
                    MSG.displayMessage('An active robot was disconnected', 'POPUP', '');
                }
                GUISTATE_C.setRobotPort("");
                console.log("port is not in the list");
            }
            updateMenuStatus();
            //console.log(new Date() + " " + portList);
            //console.log(new Date() + " " + vendorList);
            //console.log(new Date() + " " + productList);
        } else if (data.includes('OS')) {
            jsonObject = JSON.parse(data);
            system = jsonObject['OS'];
            console.log(system);
        }
    });

    socket.on('disconnect', function() {
    });

    socket.on('error', function(err) {
        console.log("Socket.IO Error");
        console.log(err.stack);
    });

    function init() {
    }

    exports.init = init;

    function getPortList() {
        return portList;
    }
    exports.getPortList = getPortList;

    function getRobotList() {
        return robotList;
    }
    exports.getRobotList = getRobotList;

    function updateMenuStatus() {
        console.log(getPortList().length)
        switch (getPortList().length) {
        case 0:
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
            }
            $('#menuRunProg').parent().addClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            break;
        case 1:
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            if (GUISTATE.gui.blocklyWorkspace) {
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            }
            $('#menuRunProg').parent().removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            break;
        default:
            // Always:
            $('#menuConnect').parent().removeClass('disabled');
            // If the port is not chosen:
            if (GUISTATE_C.getRobotPort() == "") {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                if (GUISTATE.gui.blocklyWorkspace) {
                    GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                }
                $('#menuRunProg').parent().addClass('disabled');
                //$('#menuConnect').parent().addClass('disabled');
            } else {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                if (GUISTATE.gui.blocklyWorkspace) {
                    GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
                }
                $('#menuRunProg').parent().removeClass('disabled')
            }
            break;
        }
    }
    exports.updateMenuStatus = updateMenuStatus;

    function uploadProgram() {
        var URL = 'http://localhost:8991/upload';
        var filename = GUISTATE_C.getProgramName() + '.hex';
        var fileContentHex = 'OjEwMDAwMDAwMEM5NDVDMDAwQzk0NkUwMDBDOTQ2RTAwMEM5NDZFMDBDQQ0KOjEwMDAxMDAwMEM5NDZFMDAwQzk0NkUwMDBDOTQ2RTAwMEM5NDZFMDBBOA0KOjEwMDAyMDAwMEM5NDZF'
                + 'MDAwQzk0NkUwMDBDOTQ2RTAwMEM5NDZFMDA5OA0KOjEwMDAzMDAwMEM5NDZFMDAwQzk0NkUwMDBDOTQ2RTAwMEM5NDZFMDA4OA0KOjEwMDA0MDAwMEM5NDE0MDEwQzk0NkUwMDBDOTQ2RTAwMEM5N'
                + 'DZFMDBEMQ0KOjEwMDA1MDAwMEM5NDZFMDAwQzk0NkUwMDBDOTQ2RTAwMEM5NDZFMDA2OA0KOjEwMDA2MDAwMEM5NDZFMDAwQzk0NkUwMDAwMDAwMDAwMjQwMDI3MDAyOQ0KOjEwMDA3MDAwMkEwMD'
                + 'AwMDAwMDAwMjUwMDI4MDAyQjAwMDQwNDA0MDRDRQ0KOjEwMDA4MDAwMDQwNDA0MDQwMjAyMDIwMjAyMDIwMzAzMDMwMzAzMDM0Mg0KOjEwMDA5MDAwMDEwMjA0MDgxMDIwNDA4MDAxMDIwNDA4MTA'
                + 'yMDAxMDIxRg0KOjEwMDBBMDAwMDQwODEwMjAwMDAwMDAwODAwMDIwMTAwMDAwMzA0MDdGQg0KOjEwMDBCMDAwMDAwMDAwMDAwMDAwMDAwMDExMjQxRkJFQ0ZFRkQ4RTBCOA0KOjEwMDBDMDAwREVC'
                + 'RkNEQkYyMUUwQTBFMEIxRTAwMUMwMUQ5MkE5MzBBQw0KOjEwMDBEMDAwQjIwN0UxRjcwRTk0NUUwMTBDOTRDRDAxMEM5NDAwMDA4MA0KOjEwMDBFMDAwRTFFQkYwRTAyNDkxRURFOUYwRTA5NDkxR'
                + 'TlFOEYwRTA1Mw0KOjEwMDBGMDAwRTQ5MUVFMjMwOUY0M0JDMDIyMjMzOUYxMjMzMDkxRjAzRg0KOjEwMDEwMDAwMzhGNDIxMzBBOUYwMjIzMDAxRjUyNEI1MkY3RDEyQzAzQQ0KOjEwMDExMDAwMj'
                + 'czMDkxRjAyODMwQTFGMDI0MzBCOUY0MjA5MTgwMDBFQw0KOjEwMDEyMDAwMkY3RDAzQzAyMDkxODAwMDJGNzcyMDkzODAwMDBEQzA4OQ0KOjEwMDEzMDAwMjRCNTJGNzcyNEJEMDlDMDIwOTFCMDA'
                + 'wMkY3NzAzQzBDQw0KOjEwMDE0MDAwMjA5MUIwMDAyRjdEMjA5M0IwMDBGMEUwRUUwRkZGMUY1NA0KOjEwMDE1MDAwRUU1OEZGNEZBNTkxQjQ5MTJGQjdGODk0RUM5MTgxMTEwRg0KOjEwMDE2MDAw'
                + 'MDNDMDkwOTU5RTIzMDFDMDlFMkI5QzkzMkZCRjA4OTVBMg0KOjEwMDE3MDAwM0ZCN0Y4OTQ4MDkxMDUwMTkwOTEwNjAxQTA5MTA3MDE4NQ0KOjEwMDE4MDAwQjA5MTA4MDEyNkI1QTg5QjA1QzAyR'
                + 'jNGMTlGMDAxOTYzNA0KOjEwMDE5MDAwQTExREIxMUQzRkJGQkEyRkE5MkY5ODJGODgyNzgyMEYwRA0KOjEwMDFBMDAwOTExREExMURCMTFEQkMwMUNEMDE0MkUwNjYwRjc3MUY1RA0KOjEwMDFCMD'
                + 'AwODgxRjk5MUY0QTk1RDFGNzA4OTU4RjkyOUY5MkFGOTIwOQ0KOjEwMDFDMDAwQkY5MkNGOTJERjkyRUY5MkZGOTIwRTk0QjgwMDRCMDE1NA0KOjEwMDFEMDAwNUMwMTg0RTZDODJFRDEyQ0UxMkN'
                + 'GMTJDMEU5NEI4MDBFMQ0KOjEwMDFFMDAwREMwMUNCMDE4ODE5OTkwOUFBMDlCQjA5ODgzRTkzNDAxMw0KOjEwMDFGMDAwQTEwNUIxMDU1OEYwMjFFMEMyMUFEMTA4RTEwOEYxMDhDMw0KOjEwMDIw'
                + 'MDAwODhFRTg4MEU4M0UwOTgxRUExMUNCMTFDQzExNEQxMDQ5NQ0KOjEwMDIxMDAwRTEwNEYxMDQxOUY3RkY5MEVGOTBERjkwQ0Y5MEJGOTBDOQ0KOjEwMDIyMDAwQUY5MDlGOTA4RjkwMDg5NTFGO'
                + 'TIwRjkyMEZCNjBGOTJFQw0KOjEwMDIzMDAwMTEyNDJGOTMzRjkzOEY5MzlGOTNBRjkzQkY5MzgwOTFGQw0KOjEwMDI0MDAwMDEwMTkwOTEwMjAxQTA5MTAzMDFCMDkxMDQwMTMwOTE0Qw0KOjEwMD'
                + 'I1MDAwMDAwMTIzRTAyMzBGMkQzNzIwRjQwMTk2QTExREIxMURDRA0KOjEwMDI2MDAwMDVDMDI2RTgyMzBGMDI5NkExMURCMTFEMjA5MzAwMDFCMQ0KOjEwMDI3MDAwODA5MzAxMDE5MDkzMDIwMUE'
                + 'wOTMwMzAxQjA5MzA0MDFDNA0KOjEwMDI4MDAwODA5MTA1MDE5MDkxMDYwMUEwOTEwNzAxQjA5MTA4MDFBQw0KOjEwMDI5MDAwMDE5NkExMURCMTFEODA5MzA1MDE5MDkzMDYwMUEwOTNDNQ0KOjEw'
                + 'MDJBMDAwMDcwMUIwOTMwODAxQkY5MUFGOTE5RjkxOEY5MTNGOTE0QQ0KOjEwMDJCMDAwMkY5MTBGOTAwRkJFMEY5MDFGOTAxODk1Nzg5NDg0QjVEMg0KOjEwMDJDMDAwODI2MDg0QkQ4NEI1ODE2M'
                + 'Dg0QkQ4NUI1ODI2MDg1QkQ1Mg0KOjEwMDJEMDAwODVCNTgxNjA4NUJEODA5MTZFMDA4MTYwODA5MzZFMDBFMA0KOjEwMDJFMDAwMTA5MjgxMDA4MDkxODEwMDgyNjA4MDkzODEwMDgwOTFEMg0KOj'
                + 'EwMDJGMDAwODEwMDgxNjA4MDkzODEwMDgwOTE4MDAwODE2MDgwOTM4Mw0KOjEwMDMwMDAwODAwMDgwOTFCMTAwODQ2MDgwOTNCMTAwODA5MUIwMDA0Mg0KOjEwMDMxMDAwODE2MDgwOTNCMDAwODA'
                + '5MTdBMDA4NDYwODA5MzdBMDAzRA0KOjEwMDMyMDAwODA5MTdBMDA4MjYwODA5MzdBMDA4MDkxN0EwMDgxNjA2Nw0KOjEwMDMzMDAwODA5MzdBMDA4MDkxN0EwMDgwNjg4MDkzN0EwMDEwOTI4RQ0K'
                + 'OjEwMDM0MDAwQzEwMEVERTlGMEUwMjQ5MUU5RThGMEUwODQ5MTg4MjMzMA0KOjEwMDM1MDAwOTlGMDkwRTA4ODBGOTkxRkZDMDFFODU5RkY0RkE1OTE5Mw0KOjEwMDM2MDAwQjQ5MUZDMDFFRTU4R'
                + 'kY0Rjg1OTE5NDkxOEZCN0Y4OTRBQQ0KOjEwMDM3MDAwRUM5MUUyMkJFQzkzOEZCRkMwRTBEMEUwODFFMDBFOTREMw0KOjEwMDM4MDAwNzAwMDBFOTRERDAwODBFMDBFOTQ3MDAwMEU5NEREMDA4RA'
                + '0KOjBFMDM5MDAwMjA5N0ExRjMwRTk0MDAwMEYxQ0ZGODk0RkZDRjU4DQo6MDAwMDAwMDFGRg0K';
        var port = 'COM6';
        var board = 'arduino:avr:leonardo';
        console.log("uploading " + filename);
        var signature = "8ca56849f32e00f72e8a9a67360513761f8b25d25b9a0fd4b6bbc3eb68dfbbca1a8e40159456ef8c375186af9cdfaeb3ceabaa198a0313d0ab7f4ce67229381c3d84bd3b2632538957dab40d17f7bdc560cf82e540d51bf29f70f9ebee1abab1c0a18bdeb74e0d8b94b966744563251e0e868d4195719961ce0c5023c1f0a489";
        var commandLine = "\"{runtime.tools.avrdude.path}/bin/avrdude\" \"-C{runtime.tools.avrdude.path}/etc/avrdude.conf\" {upload.verbose} -patmega328p -carduino -P{serial.port} -b115200 -D \"-Uflash:w:{build.path}/{build.project_name}.hex:i\""

        var request = {
            'board' : board,
            'port' : port,
            'commandline' : commandLine,
            'signature' : signature,
            'hex' : fileContentHex,
            'filename' : filename,
            'extra' : {
                'auth' : {
                    'password' : null
                }
            },
            'wait_for_upload_port' : true,
            'use_1200bps_touch' : true,
            'network' : false,
            'params_verbose' : '-v',
            'params_quiet' : '-q -q',
            'verbose' : true
        }

        console.log(JSON.stringify(request));

        var JSONrequest = JSON.stringify(request);

        console.log(JSONrequest);

        /*
         * var postRequest = "{\"board\":\"" + board + "\"," + "\"port\":\"" +
         * GUISTATE_C.getRobotPort() + "\";" + "commandline" + ":" +
         * "\"/usr/bin/avrdude\"\"-C/etc/avrdude/avrdude.conf\" {upload.verbose}
         * -patmega328p -carduino -P/dev/ttyACM0 -b115200 -D
         * \"-Uflash:w:/tmp/build4940972228428756877.tmp/MotorsVariableSpeed.cpp.hex:i\"" +
         * "signature:" + signature + "\"," + "\"hex\":\"" + fileContentHex +
         * "\"," + "\"filename\":\"" + filename + "\"," + "\"extra\":{\n
         * \"auth\":{\n \"password\":null \n
         * },\"wait_for_upload_port]\:true,\"use_1200bps_touch\":true,\n
         * \"network\":false,\n \"params_verbose\":\"-v\",\n
         * \"params_quiet\":\"-q -q\",\n \"verbose\":true \ \n }\n}";
         */
        var xhr = new XMLHttpRequest();
        xhr.open("POST", URL, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {//Call a function when the state changes.
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 202) {
                console.log('Accepted for flashing');
            } else {
                console.log('something went wrong');
                console.log(xhr.readyState + ' ' + xhr.status);
            }
        }
        xhr.send(JSONrequest);
    }
    exports.uploadProgram = uploadProgram;

});
