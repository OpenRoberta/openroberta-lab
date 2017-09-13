define([ 'exports', 'util', 'log', 'message', 'jquery', 'robot.controller', 'guiState.controller', 'socket.io', 'comm' ], function(exports, UTIL, LOG, MSG, $,
        ROBOT_C, GUISTATE_C, IO, COMM) {

    var portList = [];
    var vendorList = [];
    var productList = [];
    var system;
    var cmd;
    var port;
    var robotList = [];
    var agentPortList = "";
    
    function listRobotStart() {
        GUISTATE_C.setIsAgent(true);
        $('#menuConnect').parent().addClass('disabled');
        window.setInterval(function() {
           portList = [];
           vendorList = [];
           productList = [];
           robotList = [];
           agentPortList = COMM.listRobotsFromAgent();
           jsonObject = JSON.parse(agentPortList);
           jsonObject['Ports'].forEach(function(port) {
               if (GUISTATE_C.getVendor() === port['VendorID'].toLowerCase()) {
                   portList.push(port['Name']);
                   vendorList.push(port['VendorID']);
                   productList.push(port['ProductID']);
                   robotList.push(GUISTATE_C.getRobotRealName());
               }
           });
           if (portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
               GUISTATE_C.setRobotPort("");
           }
           if (portList.length == 1) {
               ROBOT_C.setPort(portList[0]);
           }
           GUISTATE_C.updateMenuStatus();
        }, 3000);
    }
    
    function init() {
        robotSocket = GUISTATE_C.getSocket()
        if (robotSocket == null || GUISTATE_C.getIsAgent() == false) {
            robotSocket = IO('ws://127.0.0.1:8991/');
            GUISTATE_C.setSocket(robotSocket);
            GUISTATE_C.setIsAgent(true);
            $('#menuConnect').parent().addClass('disabled');
            robotSocket.on('connect_error', function(err) {
                GUISTATE_C.setIsAgent(false);
            });

            robotSocket.on('connect', function() {
                robotSocket.emit('command', 'log on');
                GUISTATE_C.setIsAgent(true);
                window.setInterval(function() {
                    portList = [];
                    vendorList = [];
                    productList = [];
                    robotList = [];
                    robotSocket.emit('command', 'list');
                }, 3000);
            });

            /*
             * Vendor and Product IDs for some robots Botnroll: /dev/ttyUSB0,
             * VID: 0x10c4, PID: 0xea60 Mbot: /dev/ttyUSB0, VID: 0x1a86, PID:
             * 0x7523 ArduinoUno: /dev/ttyACM0, VID: 0x2a03, PID: 0x0043
             */
            robotSocket.on('message', function(data) {
                if (data.includes('"Network": false')) {
                    var robot;
                    jsonObject = JSON.parse(data);
                    jsonObject['Ports'].forEach(function(port) {
                        if (GUISTATE_C.getVendor() === port['VendorID'].toLowerCase()) {
                            portList.push(port['Name']);
                            vendorList.push(port['VendorID']);
                            productList.push(port['ProductID']);
                            robotList.push(GUISTATE_C.getRobotRealName());
                        }
                    });
                    GUISTATE_C.setIsAgent(true);

                    robotSocket.on('connect_error', function(err) {
                        GUISTATE_C.setIsAgent(false);
                        $('#menuConnect').parent().removeClass('disabled');
                    });
                    if (portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
                        if (GUISTATE_C.getRobotPort() != "") {
                            //MSG.displayMessage(Blockly.Msg["MESSAGE_ROBOT_DISCONNECTED"], 'POPUP', '');
                        }
                        GUISTATE_C.setRobotPort("");
                    }
                    if (portList.length == 1) {
                        ROBOT_C.setPort(portList[0]);
                    }
                    GUISTATE_C.updateMenuStatus();
                } else if (data.includes('OS')) {
                    jsonObject = JSON.parse(data);
                    system = jsonObject['OS'];
                }
            });

            robotSocket.on('disconnect', function() {
            });

            robotSocket.on('error', function(err) {
            });
        }
    }

    exports.init = init;

    function closeConnection() {
        robotSocket = GUISTATE_C.getSocket()

        if (robotSocket != null) {
            robotSocket.disconnect();
            GUISTATE_C.setSocket(null);
        }
    }
    exports.closeConnection = closeConnection;

    function getPortList() {
        return portList;
    }
    exports.getPortList = getPortList;

    function getRobotList() {
        return robotList;
    }
    exports.getRobotList = getRobotList;

    function uploadProgram(programHex, robotPort) {
        GUISTATE_C.getSocket().emit('command', 'downloadtool avrdude 6.3.0-arduino9');
        COMM.sendProgramHexToAgent(programHex, robotPort, GUISTATE_C.getProgramName(), GUISTATE_C.getSignature(), GUISTATE_C.getCommandLine(), function() {
            LOG.text("Create agent upload success");
            $('#menuRunProg').parent().removeClass('disabled');
            $('#runOnBrick').parent().removeClass('disabled');
        });
    }
    exports.uploadProgram = uploadProgram;
});
