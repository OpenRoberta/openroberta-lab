onload = function() {
  
  var HOST = "mp-devel.iais.fraunhofer.de:1999";
  var EV3HOST = "10.0.1.1:80";
  var ORAHOST = "10.0.1.10:1999";
  
  var TOKEN = "";
  
  var state = {
    SEARCH: 0,
    WAIT: 1,
    WAITFORUSER: 2,
    REGISTER: 3,
    CONNECTED: 4,
    DOWNLOAD: 5,
    UPDATE: 6,
  };
  
  var STATE = state.SEARCH;
  
  var KEY_CMD = "cmd";
  var CMD_REGISTER = "register";
  var CMD_PUSH = "push";
  var CMD_REPEAT = "repeat";
  var CMD_DOWNLOAD = "download";
  var CMD_UPDATE = "update";
  var CMD_ABORT = "abort";
  var ISRUNNING = "isrunning";
  
  var ev3info = null;
  var pushFinished = true;
  var serverreq = null;
  
  var filenames = ["ev3menu", "jsonlib", "shared", "runtime"];
  var i = 0;
  
  connect.onclick = function(){
    if (STATE != state.CONNECTED && STATE != state.REGISTER){
      generateToken();
      STATE = state.REGISTER;
      document.getElementById("connect").innerHTML = "Trennen";
    } else {
      if (serverreq !== null){
        serverreq.abort();
      }
      document.getElementById("connect").disabled = true;
      document.getElementById("connect").innerHTML = "Verbinden";
    }
  };
  
  document.getElementById("close").onclick = function() {
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
  
  setInterval(function() {loop();}, 1000);
  
  var loop = function(){
    if (pushFinished === true){
      pushFinished = false;
      switch (STATE){
        case state.SEARCH:
          displayText("Detecting EV3...");
          checkBrickState();
          break;
        case state.WAITFORUSER:
          // do nothing until user clicks connect button
          // also used as a transition state after disconnecting the EV3 
          pushFinished = true;
          break;
        case state.WAIT:
          // brick is executing a program we check every few seconds if it is finished
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
          dlFirmwareFile();
          break;
        default:
          console.log("wtf");
      }
    }
  };
  
  function changeProgramState(brickstate){
    if (STATE == state.SEARCH && brickstate == "false"){
        STATE = state.WAITFORUSER;
        document.getElementById("connect").disabled = false;
        displayText("EV3 found!");
    } else if(STATE == state.WAIT && brickstate == "false") {
      STATE = state.CONNECTED;
    }
  }
  
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
            discFlag = true;
            break;
        }
        pushFinished = true;
      }
    };
    serverreq.onabort = function(){
      signOutEV3();
      STATE = state.WAITFORUSER;
      pushFinished = true;
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
        STATE = state.WAIT;
        pushFinished = true;
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/program", true);
    brickreq.setRequestHeader("Filename", filename);
    brickreq.send(file);
  };
  
  var checkBrickState = function(){
    var command = {};
    command[KEY_CMD] = ISRUNNING;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var brickstate = JSON.parse(brickreq.responseText);
        changeProgramState(brickstate[ISRUNNING]);
        pushFinished = true;
      }
    };
    brickreq.ontimeout = function(){
      changeProgramState("timeout");
      pushFinished = true;
    };
    brickreq.onerror = function(){
      changeProgramState("error");
      pushFinished = true;
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.timeout = 3000;
    brickreq.send(JSON.stringify(command));
  };
  
  function dlFirmwareFile(){
    if (i < 4){
      serverreq = new XMLHttpRequest();
      serverreq.onreadystatechange = function() {
        if (serverreq.readyState == 4 && serverreq.status == 200) {
          var blob = new Blob([serverreq.response], {type: "binary/jar"});
          var filename = serverreq.getResponseHeader("Filename");
          ulFirmwareFile(blob, filename);
        }
      };
      serverreq.open("GET", "http://" + ORAHOST + "/update/" + filenames[i], true);
      serverreq.responseType = "blob";
      serverreq.send();
    } else {
      restartEV3();
    }
  }
  
  function ulFirmwareFile(file, filename){
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var info = JSON.parse(brickreq.responseText);
        i++;
        dlFirmwareFile();
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/firmware", true);
    brickreq.setRequestHeader("Filename", filename);
    brickreq.send(file);
  }
  
  var signOutEV3 = function(){
    var command = {};
    command[KEY_CMD] = CMD_ABORT;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var brickstate = JSON.parse(brickreq.responseText);
        STATE = state.SEARCH;
        pushFinished = true;
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.send(JSON.stringify(command));
  };
  
  function restartEV3(){
    var command = {};
    command[KEY_CMD] = CMD_UPDATE;
    
    var brickreq = new XMLHttpRequest();
    brickreq.onreadystatechange = function() {
      if (brickreq.readyState == 4 && brickreq.status == 200) {
        var brickstate = JSON.parse(brickreq.responseText);
        STATE = state.SEARCH;
        document.getElementById("connect").disabled = true;
        document.getElementById("connect").innerHTML = "Verbinden";
        pushFinished = true;
      }
    };
    brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
    brickreq.send(JSON.stringify(command));
  }
  
  function displayText(text){
    document.getElementById("debug").value = text;
  }
  
};
