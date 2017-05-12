define([ 'exports', 'util', 'log', 'message', 'jquery', 'guiState.controller' ], function(exports, UTIL, LOG, MSG, $, GUISTATE_C) {
    var arduinoCreateAgentSocket = new WebSocket("ws://localhost:8992");
    var robotList = {};
    arduinoCreateAgentSocket.onopen = function(event) {
        arduinoCreateAgentSocket.send("log on");
        robotList = arduinoCreateAgentSocket.send("list");
    };
});
