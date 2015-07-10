onload = function() {
  
  var EV3HOST = "10.0.1.1:80";
  var ORAHOST = "10.0.1.10:1999";
  
  var TOKEN = null;
  
  var KEY_CMD = "cmd";
  var CMD_REGISTER = "register";
  var CMD_PUSH = "push";
  var CMD_REPEAT = "repeat";
  
  var CURRENTCMD = CMD_REGISTER;
  
  var connect = false;
  
  connect.onclick = function(){
    TOKEN = generateToken();
    //getBrickData();
    connect = true;
  };
  
  function generateToken(){
    var token = "";
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    for( var i=0; i < 8; i++ ) {
        token += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return token;
  }
  
  document.getElementById("close").onclick = function() {
    // TODO 
    window.close();
  };
  
  setInterval(function () {pushToBrick();}, 13000);

  var pushToServer = function(brickinfo){
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
      if (req.readyState == 4 && req.status == 200) {
        var response = JSON.parse(req.responseText);
        console.log(response);
        // TODO change currentcmd
      }
    };
    req.open("POST", "http://" + ORAHOST + "/pushcmd", true);
    req.setRequestHeader("Content-Type", "application/json; charset=utf8");
    req.send(JSON.stringify(brickinfo));
  };
  
  var pushToBrick = function(){
    if (connect === true){
      // TODO make switch case statement for repeat register etc
      var command = {};
      command[KEY_CMD] = CURRENTCMD;
      console.log("CURRENTCMD: " + CURRENTCMD);
    
      var req = new XMLHttpRequest();
      req.onreadystatechange = function() {
        if (req.readyState == 4 && req.status == 200) {
          var brickinfo = JSON.parse(req.responseText);
          brickinfo["token"] = TOKEN;
          brickinfo[KEY_CMD] = CURRENTCMD;
          debug("EV3 found");
          console.log(brickinfo);
          pushToServer(brickinfo);
        }
      };
      req.open("POST", "http://" + EV3HOST + "/brickinfo", true);
      req.send(JSON.stringify(command));
    }
  };
  
  var downloadProgram = function (){
    
  };
  
  var disconnect = function(){
    // TODO disconnect brick properly before exit
  };
  
  function debug(text){
    document.getElementById("debug").value += text + "\n";
  }
  
};
