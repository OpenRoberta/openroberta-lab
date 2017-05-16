define([ 'exports', 'util', 'log', 'message', 'jquery', 'robot.controller', 'guiState.controller', 'guiState.model', 'socket.io' ], function(exports, UTIL, LOG, MSG, $,
        ROBOT_C, GUISTATE_C, GUISTATE, IO) {

    var socket = IO('ws://localhost:8991/');
    console.log(socket);
    var portList = [];
    var vendorList = [];
    var productList = [];
    var system;
    var cmd;

    socket.on('connect', function() {
        console.log('connect');
        socket.emit('command', 'log on');
        socket.emit('command', 'list');
        console.log('listed');
        window.setInterval(function() {
            portList = [];
            vendorList = [];
            productList = [];
            socket.emit('command', 'list');
            console.log('refreshed robot ports');
        }, 3000);
    });

    socket.on('message', function(data) {
        if (data.includes('"Network": false')) {
            jsonObject = JSON.parse(data);
            jsonObject['Ports'].forEach(function(port) {
                portList.push(port['Name']);
                vendorList.push(port['VendorID']);
                productList.push(port['ProductID']);
            });

            if (portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
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
            }
            else {
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

});
