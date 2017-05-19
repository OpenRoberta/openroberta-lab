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
        var fileContentHex = null;
        var board = 'arduino:avr:leonardo';
        console.log("uploading " + filename);
        var signature = "67d5a421776c68df026b8b01a181e1cf3ff3e6bd2b96c44fbc1240d82191f81924b6ebb8a54015b2ef6600e9f73d52db0c2a95f0461e7b0422399ab1209d97d9b2de3af5bf7f1b2b3589ee3a905209d29a410963fb656e52492037d986731552f610488444429021d909e6fd9448442e053c41ceae815ccc211093561a39f704";
        var commandLine = "\"/usr/bin/avrdude\"\"-C/etc/avrdude/avrdude.conf\" {upload.verbose} -patmega328p -carduino  -P{} -b115200 -D \"-Uflash:w:/tmp/build4940972228428756877.tmp/MotorsVariableSpeed.cpp.hex:i\""

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

        JSONrequest = JSON.parse(JSON.stringify(request));

        var postRequest = "{\"board\":\""
                + board
                + "\","
                + "\"port\":\""
                + GUISTATE_C.getRobotPort()
                + "\";"
                + "commandline"
                + ":"
                + "\"/usr/bin/avrdude\"\"-C/etc/avrdude/avrdude.conf\" {upload.verbose} -patmega328p -carduino  -P/dev/ttyACM0 -b115200 -D \"-Uflash:w:/tmp/build4940972228428756877.tmp/MotorsVariableSpeed.cpp.hex:i\""
                + "signature:"
                + signature
                + "\","
                + "\"hex\":\""
                + fileContentHex
                + "\","
                + "\"filename\":\""
                + filename
                + "\","
                + "\"extra\":{\n \"auth\":{\n \"password\":null \n },\"wait_for_upload_port]\:true,\"use_1200bps_touch\":true,\n \"network\":false,\n \"params_verbose\":\"-v\",\n \"params_quiet\":\"-q -q\",\n \"verbose\":true \ \n }\n}";
        var xhr = new XMLHttpRequest();
        xhr.open("POST", URL, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSONrequest);
    }
    exports.uploadProgram = uploadProgram;

});
