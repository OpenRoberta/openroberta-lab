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

    // Botnroll:
    // /dev/ttyUSB0
    // 0x10c4
    // 0xea60
    // Mbot:
    ///dev/ttyUSB0
    // 0x1a86
    // 0x7523
    // ArduinoUno:
    // /dev/ttyACM0
    // 0x2a03
    // 0x0043

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
                $('#menuConnect').parent().addClass('disabled');

                GUISTATE_C.setConnected(false);
            }
            if (portList.length === 1) {
                console.log('turn off choise of ports');
                GUISTATE_C.setConnected(true);
                $('#menuConnect').parent().addClass('disabled');
                ROBOT_C.setPort(portList[0]);
                GUISTATE.gui.blocklyWorkspace.robControls.enable('runOnBrick');
            } else if (portList.length > 1) {
                $('#menuConnect').parent().removeClass('disabled');
                //GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                console.log('turn on choise of ports');
            } else {
                GUISTATE_C.setConnected(false);
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').addClass('error');
                $('#menuConnect').parent().addClass('disabled');
                GUISTATE.gui.blocklyWorkspace.robControls.disable('runOnBrick');
                $('#menuRunProg').parent().addClass('disabled');
                console.log('turn off choise of ports');
            }
            console.log(new Date() + " " + portList);
            console.log(new Date() + " " + vendorList);
            console.log(new Date() + " " + productList);
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

    function uploadProgram() {
        var filename = GUISTATE_C.getProgramName() + '.hex';
        var fileContentHex = null;
        var board = null;
        window.alert("uploading " + filename);
        var signature = null;
        var postRequest = "{\"board\":\""
                + board
                + "\","
                + "\"port\":\""
                + GUISTATE_C.getRobotPort()
                + "\";"
                + "\"commandline\":\"\"{runtime.tools.avrdude.path}/bin/avrdude\" \"-C{runtime.tools.avrdude.path}/etc/avrdude.conf\" {upload.verbose} -patmega32u4 -cavr109 -P{serial.port} -b57600 -D \"-Uflash:w:{build.path}/{build.project_name}.hex:i\"\",:\"signature\":\""
                + signature
                + "\","
                + "\"hex\":\""
                + fileContentHex
                + "\","
                + "\"filename\":\""
                + filename
                + "\","
                + "\"extra\":{\n \"auth\":{\n \"password\":null \n },\"wait_for_upload_port]\:true,\"use_1200bps_touch\":true,\n \"network\":false,\n \"params_verbose\":\"-v\",\n \"params_quiet\":\"-q -q\",\n \"verbose\":true \ \n }\n}";
    }
    exports.uploadProgram = uploadProgram;

});
