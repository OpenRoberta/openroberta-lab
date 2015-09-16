onload = function () {
	var warningdialog = document.getElementById("warningdialog");
	var infodialog = document.getElementById("infodialog");

	var EV3HOST = "10.0.1.1:80";

	var STANDARDHOST = "lab.open-roberta.org";
	var CUSTOMHOST = null;
	var ORAHOST = STANDARDHOST;

	var TOKEN = "";

	var state = {
		SEARCH : 0,
		WAIT : 1,
		WAITFORUSER : 2,
		REGISTER : 3,
		CONNECTED : 4,
		DOWNLOAD : 5,
		UPDATE : 6,
		ABORT : 7
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
	var fileIndex = 0;

	var blink = true;
	var notID = 0;
	var cancel = false;
	
	document.getElementById('cancelwarning').onclick = function () {
	  warningdialog.close();
	};
	
  document.getElementById("discwarning").onclick = function () {
	  signOutEV3(true);
	  warningdialog.close();
	};
	
	document.getElementById('navabout').onclick = function () {
	  document.getElementById('infodialogimg').src = "resources/iais_logo.gif";
	  document.getElementById("infodialogtxt").innerHTML = chrome.i18n.getMessage("dialog_about");
	  infodialog.showModal();
	};
	
	document.getElementById('closeinfodialog').onclick = function () {
	  infodialog.close();
	};
	
	document.getElementById("close").onclick = function () {
	  confirmDisconnect();
	};
	
	document.getElementById("navclose").onclick = function () {
    confirmDisconnect();
	};

  document.getElementById("navfiletitle").innerHTML = chrome.i18n.getMessage("nav_title_file");
  document.getElementById("navabouttitle").innerHTML = chrome.i18n.getMessage("nav_title_about");
  document.getElementById("navclose").innerHTML = chrome.i18n.getMessage("button_close");
  document.getElementById("navabout").innerHTML = chrome.i18n.getMessage("button_about");
  document.getElementById("discwarning").innerHTML = chrome.i18n.getMessage("button_close");
  document.getElementById("cancelwarning").innerHTML = chrome.i18n.getMessage("button_cancel");
	document.getElementById("connect").innerHTML = chrome.i18n.getMessage("button_connect");
	document.getElementById("close").innerHTML = chrome.i18n.getMessage("button_close");
	document.getElementById("advancedop").value = chrome.i18n.getMessage("advanced_options");
	document.getElementById("customaddressinfo").value = chrome.i18n.getMessage("custom_server");

	document.getElementById("connect").onclick = function () {
		if ( STATE == state.WAITFORUSER ) {
			generateToken();
			STATE = state.REGISTER;
			document.getElementById("connect").innerHTML = chrome.i18n.getMessage("button_disconnect");
		} else if (STATE == state.CONNECTED || STATE == state.REGISTER) {
		  signOutEV3(false);
		  cancel = true;
		  if (serverreq !== null) {
		    serverreq.abort();
		  }
		  reset();
		  pushFinished = true;
		}
	};

	document.getElementById("checkboxadvopt").onchange = function () {
		if (document.getElementById("checkboxadvopt").checked === true) {
			document.getElementById("alternative").style.visibility = "visible";
			chrome.app.window.current().innerBounds.setSize(chrome.app.window.current().innerBounds.width, chrome.app.window.current().innerBounds.height + 45);
		} else {
			document.getElementById("alternative").style.visibility = "hidden";
			chrome.app.window.current().innerBounds.setSize(chrome.app.window.current().innerBounds.width, chrome.app.window.current().innerBounds.height - 45);
		}
	};
	
	function confirmDisconnect() {
	  if (STATE == state.CONNECTED) {
	    document.getElementById('warningimg').src = "resources/Roberta.png";
	    document.getElementById("warningtext").innerHTML = chrome.i18n.getMessage("dialog_confirm_disconnect");
		  warningdialog.showModal();
	  } else {
	    window.close();
	  }
	}

	function generateToken() {
		TOKEN = "";
		var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (var i = 0; i < 8; i++) {
			TOKEN += chars.charAt(Math.floor(Math.random() * chars.length));
		}
		showTextInTokenField("Token", TOKEN);
	}

	setInterval(function () { loop(); }, 1000);

	function loop() {
		if (pushFinished === true) {
			pushFinished = false;
			switch (STATE) {
			case state.SEARCH:
				displayInfotext(chrome.i18n.getMessage("infotext_plugin"));
				setMainPicture("plug.gif");
				checkBrickState();
				break;
			case state.WAITFORUSER:
				checkBrickState();
				displayInfotext(chrome.i18n.getMessage("infotext_connect"));
				setMainPicture("connect.gif");
				updateConnStatus("Roberta_Menu_Icon_red.png");
				break;
			case state.WAIT:
				checkBrickState();
				if (blink) {
					updateConnStatus("Roberta_Menu_Icon_red.png");
				} else {
					updateConnStatus("Roberta_Menu_Icon_green.png");
				}
				blink = !blink;
				break;
			case state.ABORT:
			  document.getElementById('infodialogimg').src = "resources/warning-outline.png";
	      document.getElementById("infodialogtxt").innerHTML = chrome.i18n.getMessage("dialog_timeout_msg");
	      infodialog.showModal();
	      chrome.app.window.current().drawAttention();
				reset();
				pushFinished = true;
				break;
			case state.REGISTER:
				if (document.getElementById("checkboxadvopt").checked === true) {
				  var ip = document.getElementById("ip").value;
				  var port = document.getElementById("port").value;
				  if ( ip !== "" && port !== ""){
				    CUSTOMHOST = document.getElementById("ip").value + ":" + document.getElementById("port").value;
					  ORAHOST = CUSTOMHOST;
				  } else {
				    ORAHOST = STANDARDHOST;
				  }
				} else {
					ORAHOST = STANDARDHOST;
				}
				displayInfotext(chrome.i18n.getMessage("infotext_tokencopy"));
				setMainPicture("server.gif");
				pushToBrick(CMD_REGISTER, CMD_REGISTER);
				break;
			case state.CONNECTED:
				showTextInTokenField("Name", ev3info["brickname"]);
				updateConnStatus("Roberta_Menu_Icon_green.png");
				displayInfotext(chrome.i18n.getMessage("infotext_runprogram"));
				setMainPicture("connected.gif");
				pushToBrick(CMD_REPEAT, CMD_PUSH);
				break;
			case state.DOWNLOAD:
				downloadProgram(CMD_PUSH);
				break;
			case state.UPDATE:
				dlFirmwareFile();
				break;
			default:
				console.log("Unknown state. Help!");
			}
		}
	}

	function pushToBrick(ev3cmd, servercmd) {
		var command = {};
		command[KEY_CMD] = ev3cmd;

		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				ev3info = JSON.parse(brickreq.responseText);
				pushToServer(servercmd);
			}
			if (brickreq.readyState == 4 && brickreq.status === 0) {
			  document.getElementById('infodialogimg').src = "resources/warning-outline.png";
	      document.getElementById("infodialogtxt").innerHTML = chrome.i18n.getMessage("dialog_brickerror");
	      chrome.app.window.current().drawAttention();
	      infodialog.showModal();
	      reset();
	      pushFinished = true;
			}
		};
		brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
		brickreq.timeout = 3000;
		brickreq.send(JSON.stringify(command));
	}

	function pushToServer(servercmd) {
		ev3info["token"] = TOKEN;
		ev3info[KEY_CMD] = servercmd;

		serverreq = new XMLHttpRequest();
		serverreq.onreadystatechange = function () {
			if (serverreq.readyState == 4 && serverreq.status == 200) {
				var response = JSON.parse(serverreq.responseText);
				switch (response[KEY_CMD]) {
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
					STATE = state.ABORT;
					break;
				}
				pushFinished = true;
			}
			if (serverreq.readyState == 4 && serverreq.status === 0) {
				if (STATE == state.REGISTER && cancel === false) {
				  document.getElementById('infodialogimg').src = "resources/warning-outline.png";
	        document.getElementById("infodialogtxt").innerHTML = chrome.i18n.getMessage("dialog_interneterror");
	        infodialog.showModal();
	        chrome.app.window.current().drawAttention();
				}
				cancel = false;
				reset();
				pushFinished = true;
			}
		};
		serverreq.onabort = function () {
			// ok
		};
		serverreq.open("POST", "http://" + ORAHOST + "/pushcmd", true);
		serverreq.setRequestHeader("Content-Type", "application/json; charset=utf8");
		serverreq.send(JSON.stringify(ev3info));
	}

	function downloadProgram(servercmd) {
		ev3info["token"] = TOKEN;
		ev3info[KEY_CMD] = servercmd;

		serverreq = new XMLHttpRequest();
		serverreq.onreadystatechange = function () {
			if (serverreq.readyState == 4 && serverreq.status == 200) {
				var blob = new Blob([serverreq.response], {
						type : "binary/jar"
					});
				var filename = serverreq.getResponseHeader("Filename");
				uploadProgram(blob, filename);
			}
		};
		serverreq.open("POST", "http://" + ORAHOST + "/download", true);
		serverreq.responseType = "blob";
		serverreq.setRequestHeader("Content-Type", "application/json; charset=utf8");
		serverreq.send(JSON.stringify(ev3info));
	}

	function uploadProgram(file, filename) {
		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				var info = JSON.parse(brickreq.responseText);
				STATE = state.WAIT;
				pushFinished = true;
			}
		};
		brickreq.open("POST", "http://" + EV3HOST + "/program", true);
		brickreq.setRequestHeader("Filename", filename);
		brickreq.send(file);
	}

	function checkBrickState() {
		var command = {};
		command[KEY_CMD] = ISRUNNING;

		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				var brickstate = JSON.parse(brickreq.responseText);
				// console.log(brickstate);
				if (STATE == state.SEARCH) {
					STATE = state.WAITFORUSER;
					document.getElementById("connect").disabled = false;
				} else if (STATE == state.WAIT && brickstate["isrunning"] == "false") {
					STATE = state.CONNECTED;
				}
				pushFinished = true;
			}
			if (brickreq.readyState == 4 && brickreq.status === 0) {
				if (STATE == state.WAITFORUSER) {
					reset();
				}
				pushFinished = true;
			}
		};
		brickreq.ontimeout = function () {
			// ok
		};
		brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
		brickreq.timeout = 3000;
		brickreq.send(JSON.stringify(command));
	}

	function dlFirmwareFile() {
		if (fileIndex < 4) {
			serverreq = new XMLHttpRequest();
			serverreq.onreadystatechange = function () {
				if (serverreq.readyState == 4 && serverreq.status == 200) {
					var blob = new Blob([serverreq.response], {
							type : "binary/jar"
						});
					var filename = serverreq.getResponseHeader("Filename");
					ulFirmwareFile(blob, filename);
				}
			};
			serverreq.open("GET", "http://" + ORAHOST + "/update/" + filenames[fileIndex], true);
			serverreq.responseType = "blob";
			serverreq.send();
		} else {
		  restartEV3();
			setTimeout(function () {
			  reset();
			  pushFinished = true;
			}, 3000);
		}
	}

	function reset() {
		document.getElementById("token").value = "";
		document.getElementById("connect").disabled = true;
		document.getElementById("connect").innerHTML = chrome.i18n.getMessage("button_connect");
		updateConnStatus("Roberta_Menu_Icon_grey.png");
		STATE = state.SEARCH;
	}

	function ulFirmwareFile(file, filename) {
		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				var info = JSON.parse(brickreq.responseText);
				fileIndex++;
				dlFirmwareFile();
			}
		};
		brickreq.open("POST", "http://" + EV3HOST + "/firmware", true);
		brickreq.setRequestHeader("Filename", filename);
		brickreq.send(file);
	}

	function signOutEV3(close) {
		var command = {};
		command[KEY_CMD] = CMD_ABORT;

		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				var brickstate = JSON.parse(brickreq.responseText);
				STATE = state.SEARCH;
				pushFinished = true;
				if (close === true) {
		      window.close();
				}
			}
		};
		brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
		brickreq.send(JSON.stringify(command));
	}

	function restartEV3() {
		var command = {};
		command[KEY_CMD] = CMD_UPDATE;

		var brickreq = new XMLHttpRequest();
		brickreq.onreadystatechange = function () {
			if (brickreq.readyState == 4 && brickreq.status == 200) {
				var brickstate = JSON.parse(brickreq.responseText);
				STATE = state.SEARCH;
				document.getElementById("connect").disabled = true;
				document.getElementById("connect").innerHTML = chrome.i18n.getMessage("button_connect");
				pushFinished = true;
			}
		};
		brickreq.open("POST", "http://" + EV3HOST + "/brickinfo", true);
		brickreq.send(JSON.stringify(command));
	}

	function displayInfotext(infotext) {
		document.getElementById("infotext").value = infotext;
	}

	function showTextInTokenField(info, token) {
		document.getElementById("token").value = info + ": " + token;
	}

	function updateConnStatus(imgName) {
		document.getElementById("connstatus").src = "resources/" + imgName;
	}

	function setMainPicture(imgName) {
		document.getElementById("mainpicture").src = "resources/" + imgName;
	}

};
