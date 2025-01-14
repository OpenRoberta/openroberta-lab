define(["require","exports","guiState.controller","interpreter.interpreter","interpreter.robotWeDoBehaviour","log","blockly","jquery"],(function(e,t,o,r,n,i,a,s){var l,c,p,d,f;function g(){o.setConnectionState("wait"),o.getBlocklyWorkspace().robControls.switchToStart()}function u(e){try{if("Android"===p)OpenRoberta.jsToAppInterface(JSON.stringify(e));else{if("IOS"!==p)throw"invalid webview type";window.webkit.messageHandlers.OpenRoberta.postMessage(JSON.stringify(e))}}catch(t){i.error("jsToAppInterface >"+t+" caused by: "+e)}}function y(e){void 0!==e.show?(s("#showDisplayText").append("<div>"+e.show+"</div>"),s("#showDisplayText").is(":visible")||(s("#showDisplay").oneWrap("hidden.bs.modal",(function(){s("#showDisplayText").empty()})),s("#showDisplay").modal("show")),s("#showDisplayText").scrollTop(s("#showDisplayText").prop("scrollHeight"))):void 0!==e.clear&&s("#showDisplayText").empty()}Object.defineProperty(t,"__esModule",{value:!0}),t.jsToDisplay=t.jsToAppInterface=t.setRobotBehaviour=t.isRobotConnected=t.getInterpreter=t.appToJsInterface=t.init=void 0,t.init=function(e){c=e,l=s.Deferred();var t={target:"internal",type:"identify"};return!function(e){try{return OpenRoberta.jsToAppInterface(JSON.stringify(e)),!0}catch(e){return!1}}(t)?!function(e){try{return window.webkit.messageHandlers.OpenRoberta.postMessage(JSON.stringify(e)),!0}catch(e){return!1}}(t)?l.resolve(e):p="IOS":p="Android",l.promise()},t.appToJsInterface=function(e){try{var t=JSON.parse(e);if(!t.target||!t.type)throw"invalid arguments";if("internal"==t.target){if("identify"!=t.type)throw"invalid arguments";l.resolve(c,t.name)}else{if(t.target!==o.getRobot())throw"invalid arguments";if("scan"==t.type&&"appeared"==t.state)s("#show-available-connections").trigger("add",t);else if("scan"==t.type&&"error"==t.state)s("#show-available-connections").modal("hide");else if("scan"==t.type&&"disappeared"==t.state)console.log(t);else if("connect"==t.type&&"connected"==t.state){s("#show-available-connections").trigger("connect",t),f.update(t),o.setConnectionState("wait");for(var r=(u=o.getBricklyWorkspace()).getAllBlocks(),n=0;n<r.length;n++)if("robBrick_WeDo-Brick"===r[n].type){r[n].getField("VAR").setValue(t.brickname.replace(/\s/g,"")),r[n].render();var p=a.Xml.workspaceToDom(u),g=a.Xml.domToText(p);o.setConfigurationXML(g);break}}else if("connect"===t.type&&"disconnected"===t.state){f.update(t),null!=d&&d.terminate();var u;for(r=(u=o.getBricklyWorkspace()).getAllBlocks(),n=0;n<r.length;n++)if("robBrick_WeDo-Brick"===r[n].type){r[n].getField("VAR").setValue(a.Msg.ROBOT_DEFAULT_NAME_WEDO||a.Msg.ROBOT_DEFAULT_NAME||"Brick1"),r[n].render();p=a.Xml.workspaceToDom(u),g=a.Xml.domToText(p);o.setConfigurationXML(g);break}o.setConnectionState("error")}else f.update(t)}}catch(t){i.error("appToJsInterface >"+t+" caused by: "+e)}},t.getInterpreter=function(e){return d=new r.Interpreter(e,f,g,[],null,null)},t.isRobotConnected=function(){return f&&f.getConnectedBricks().length>0},t.setRobotBehaviour=function(){"wedo"===o.getRobot()&&(f=new n.RobotWeDoBehaviour(u,y)),i.error("Webview: no robot behaviour for "+o.getRobot()+" available!")},t.jsToAppInterface=u,t.jsToDisplay=y}));
//# sourceMappingURL=webview.controller.js.map
//# sourceMappingURL=webview.controller.js.map
