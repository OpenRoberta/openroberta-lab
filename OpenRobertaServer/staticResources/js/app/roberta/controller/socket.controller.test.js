const WebSocket = require("nodejs-websocket");

var arduinoCreateAgentSocket = new WebSocket("ws://localhost:8992");
var robotList = {};
arduinoCreateAgentSocket.onopen = function(event) {
    arduinoCreateAgentSocket.send("log on");
    robotList = arduinoCreateAgentSocket.send("list");
};

console.log(robotList);
