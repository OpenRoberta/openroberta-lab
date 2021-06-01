const URL = "wss://cyberbotics1.epfl.ch/1999/session?url=file:///home/cyberbotics/webots/projects/robots/softbank/nao/worlds/nao_room.wbt";

let view = null;
const connectButton = document.getElementById('webotsConnect');
const runButton = document.getElementById('webotsPlay');
const resetButton = document.getElementById('webotsReset');
const streamingViewer = document.getElementById('webotsDiv');
const regularSimulationDiv = document.getElementById('canvasDiv');
const simEditButtons = document.getElementById('simEditButtons');
const simButtons = document.getElementById("simButtons");
const webotsButtons = document.getElementById("webotsButtons");

let sourceCode = null;

function connect() {
    streamingViewer.connect(URL, "x3d", false, false, onConnect, onDisconnect)
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
    let message = {
        name: "supervisor",
        message: "upload:" + sourceCode
    };
    streamingViewer.sendMessage('robot:' + JSON.stringify(message));
}

function reset() {
    console.log("sending reset");
    streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
}

export function init(sc) {
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