define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    var URL = "wss://cyberbotics1.epfl.ch/1999/session?url=file:///home/cyberbotics/webots/projects/robots/softbank/nao/worlds/nao_room.wbt";
    var view = null;
    var connectButton = document.getElementById('webotsConnect');
    var runButton = document.getElementById('webotsPlay');
    var resetButton = document.getElementById('webotsReset');
    var streamingViewer = document.getElementById('webotsDiv');
    var regularSimulationDiv = document.getElementById('canvasDiv');
    var simEditButtons = document.getElementById('simEditButtons');
    var simButtons = document.getElementById("simButtons");
    var webotsButtons = document.getElementById("webotsButtons");
    var sourceCode = null;
    function connect() {
        streamingViewer.connect(URL, "x3d", false, false, onConnect, onDisconnect);
        streamingViewer.hideToolbar();
    }
    function disconnect() {
        view.close();
        connectButton.onclick = connect;
        runButton.disabled = true;
        resetButton.disabled = true;
    }
    function onConnect() {
        connectButton.onclick = disconnect;
        runButton.disabled = false;
        console.log("connected");
        //sendController(sourceCode)
    }
    function onDisconnect() {
        connectButton.onclick = connect;
        runButton.disabled = true;
        console.log("disconnected");
    }
    function run() {
        streamingViewer.sendMessage('real-time:-1');
        runButton.onclick = pause;
    }
    function pause() {
        streamingViewer.sendMessage('pause');
        runButton.onclick = run;
    }
    function sendController(sourceCode) {
        var message = {
            name: "supervisor",
            message: "upload:" + sourceCode
        };
        streamingViewer.sendMessage('robot:' + JSON.stringify(message));
    }
    function reset() {
        console.log("sending reset");
        streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
    }
    function init(sc) {
        regularSimulationDiv.hidden = true;
        simEditButtons.hidden = true;
        simButtons.hidden = true;
        webotsButtons.hidden = false;
        streamingViewer.hidden = false;
        runButton.disabled = true;
        sourceCode = sc;
        connectButton.onclick = connect;
        runButton.onclick = run;
        resetButton.onclick = reset;
    }
    exports.init = init;
});
