onload = function() {
  
  var EV3HOST = "10.0.1.1:80";
  var ORAHOST = "10.0.1.10:1999";
  
  var TOKEN = "";
  
  var KEY_CMD = "cmd";
  
  var state = {
    SEARCH: 0,
    WAIT: 1,
    WAITFORUSER: 2,
    REGISTER: 3,
    CONNECTED: 4,
    DOWNLOAD: 5,
    UPDATE: 6,
    DISCONNECT: 7
  };
  
  var STATE = state.SEARCH;
  
  var CMD_REGISTER = "register";
  var CMD_PUSH = "push";
  var CMD_REPEAT = "repeat";
  var CMD_DOWNLOAD = "download";
  var CMD_UPDATE = "update";
  var CMD_ABORT = "abort";
  var ISRUNNING = "isrunning";
  
  
  var ev3info = null;
  var ev3reachable = "";
  
  var pushFinished = true;
  
  var serverreq = null;
  
  connect.onclick = function(){
    generateToken();
    STATE = state.REGISTER;
  };
  
  document.getElementById("close").onclick = function() {
    if (serverreq !== null){
      serverreq.abort();
    }
    STATE = state.DISCONNECT;
    window.close();
  };
  
  function generateToken(){
    TOKEN = "";
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for( var i=0; i < 8; i++ ) {
        TOKEN += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    displayText(TOKEN);
  }
  
  setInterval(function() {doPush();}, 1000);
  
  //setInterval(function() {searchEV3();}, 3000);
  
  function searchEV3(){
    if (STATE === state.SEARCH){
      displayText("Detecting EV3...");
      switch (ev3reachable){
        // false means EV3 is reachable and no user program is running
        case "false":
          STATE = state.REGISTER;
          document.getElementById("connect").disabled = false;
          displayText("EV3 found!");
          break;
        default:
          break;
      }
      checkBrickState();
    }
  }
  
  var doPush = function(){
    if (pushFinished === true){
      pushFinished = false;
      switch (STATE){
        case state.SEARCH:
          displayText("Detecting EV3...");
          //console.log(ev3reachable);
          switch (ev3reachable){
            // false means EV3 is reachable and no user program is running
            case "false":
              STATE = state.WAITFORUSER;
              document.getElementById("connect").disabled = false;
              displayText("EV3 found!");
            break;
          default:
            break;
          }
          checkBrickState();
          break;
        case state.WAITFORUSER:
          // do nothing until user clicks connect button
          pushFinished = true;
          break;
        case state.WAIT:
          // TODO check wtf process returns
          if (ev3reachable === "false"){
            STATE = state.CONNECTED;
          }
          checkBrickState();
          break;
        case state.REGISTER:
          pushToBrick(CMD_REGISTER, CMD_REGISTER);
          break;
        case state.CONNECTED:
          pushToBrick(CMD_REPEAT, CMD_PUSH);
          break;
        case state.DOWNLOAD:
          downloadProgram(CMD_PUSH);
          break;
        case state.UPDATE:
          // TODO update thing
          break;
        case state.DISCONNECT:
          // TODO
          // state SEARCH
          disconnect();
          break;
        default:
          console.log("wtf");
      }
    }
  };
  
  var pushToBrick = function(ev3cmd, servercmd){
    var command = {};
    command[KEY_CMD] = ev3cmd;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        ev3info = JSON.parse(brickreq.responseText);
        pushToServer(servercmd);
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.send(JSON.stringify(command));
  };

  var pushToServer = function(servercmd){
    ev3info["token"] = TOKEN;
    ev3info[KEY_CMD] = servercmd;
    console.log(ev3info);
    
    serverreq = new XMLHttpRequest();
    serverreq.onreadystatechange = function() {
      if (serverreq.readyState == 4 && serverreq.status == 200) {
        var response = JSON.parse(serverreq.responseText);
        switch (response[KEY_CMD]){
          case CMD_REPEAT:
            STATE = state.CONNECTED;
            break;
          case CMD_DOWNLOAD:
            STATE = state.DOWNLOAD;
            break;
          case CMD_UPDATE:
            STATE = state.UPDATE;
            break;
          case CMD_ABORT:
            STATE = state.DISCONNECT;
            break;
        }
        pushFinished = true;
      }
    };
    serverreq.open("POST", "http://" + ORAHOST + "/pushcmd", true);
    serverreq.setRequestHeader("Content-Type", "application/json; charset=utf8");
    serverreq.send(JSON.stringify(ev3info));
  };
  
  var downloadProgram = function(servercmd){
    ev3info["token"] = TOKEN;
    ev3info[KEY_CMD] = servercmd;
    
    serverreq = new XMLHttpRequest();
    serverreq.onreadystatechange = function() {
      if (serverreq.readyState == 4 && serverreq.status == 200) {
        var blob = new Blob([serverreq.response], {type: "binary/jar"});
        var filename = serverreq.getResponseHeader("Filename");
        uploadProgram(blob, filename);
      }
    };
    serverreq.open("POST", "http://" + ORAHOST + "/download", true);
    serverreq.responseType = "blob";
    serverreq.setRequestHeader("Content-Type", "application/json; charset=utf8");
    serverreq.send(JSON.stringify(ev3info));
  };
  
  var uploadProgram = function(file, filename){
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var info = JSON.parse(brickreq.responseText);
        console.log(info);
        STATE = state.WAIT;
        pushFinished = true;
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/program", true);
    brickreq.setRequestHeader("Filename", filename);
    brickreq.send(file);
  };
  
  function checkBrickState(){
    var command = {};
    command[KEY_CMD] = ISRUNNING;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var brickstate = JSON.parse(brickreq.responseText);
        ev3reachable = brickstate[ISRUNNING];
        pushFinished = true;
      }
    };
    brickreq.ontimeout = function(){
      ev3reachable = "timeout";
      pushFinished = true;
    };
    brickreq.onerror = function(){
      ev3reachable = "error";
      pushFinished = true;
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.timeout = 3000;
    brickreq.send(JSON.stringify(command));
  }
  
  var disconnect = function(){
    var command = {};
    command[KEY_CMD] = CMD_ABORT;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var brickstate = JSON.parse(brickreq.responseText);
        console.log(Disconnected);
        STATE = SEARCH;
        pushFinished = true;
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.send(JSON.stringify(command));
  };
  
  function displayText(text){
    document.getElementById("debug").value = text;
  }
  
};
