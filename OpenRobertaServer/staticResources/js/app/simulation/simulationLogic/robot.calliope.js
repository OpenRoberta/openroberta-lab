var __extends=this&&this.__extends||function(){var e=function(t,o){return e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&(e[o]=t[o])},e(t,o)};return function(t,o){if("function"!=typeof o&&null!==o)throw new TypeError("Class extends value "+String(o)+" is not a constructor or null");function r(){this.constructor=t}e(t,o),t.prototype=null===o?Object.create(o):(r.prototype=o.prototype,new r)}}();define(["require","exports","robot.sensors","robot.actuators","robot.base.stationary"],(function(e,t,o,r,n){Object.defineProperty(t,"__esModule",{value:!0});var s=function(e){function t(t,n,s,i,a){var f=e.call(this,t,n,s,i,a)||this;return f.volume=.5,f.webAudio=new r.WebAudio,f.topView='<div class="stationaryContent"><form id="mbed-form"><div id="mbedButtons" class="btn-group btn-group-vertical" data-bs-toggle="buttons"></div></form></div>',f.timer=new o.Timer(1),f.configure(n),f}return __extends(t,e),t.prototype.handleMouseDown=function(e){},t.prototype.handleMouseMove=function(e){},t.prototype.handleMouseOutUp=function(e){},t.prototype.configure=function(e){$("#simRobotContent").append(this.topView),$("#simRobotWindow button").removeClass("btn-close-white"),$.validator.addClassRules("range",{required:!0,number:!0}),this.gestureSensor=new o.GestureSensor;var t=[],n=[],s=[{cx:562,cy:720,theta:0,color:"grey",name:"",speed:0,port:"A",timeout:0},{cx:362,cy:720,theta:0,color:"grey",name:"",speed:0,port:"B",timeout:0}],i=[{x:71,y:463,r:51,type:"TOUCH",value:!1,port:"0",name:"0",touchColors:["#efda49ff","#d8d8d8ff","#efdc5cff","#e6e2c6ff","#dcdcdaff","#e0dfd6ff","#e9e2b5ff","#dfded9ff"],color:"#008000",typeValue:0},{x:270,y:802,r:51,type:"TOUCH",value:!1,port:"1",name:"1",touchColors:["#efda4aff","#d8d8d9ff","#ded8b3ff","#eed94bff","#e7da7eff","#d8d8d9ff","#eeda52ff"],color:"#008000",typeValue:0},{x:659,y:801,r:51,type:"TOUCH",value:!1,port:"2",name:"2",touchColors:["#efda4bff","#d8d8daff","#ebda65ff","#e3d992ff","#e5d98aff","#ddd8b6ff","#dbd9c7ff","#dcd8bfff"],color:"#008000",typeValue:0},{x:857,y:463,r:51,type:"TOUCH",value:!1,port:"3",name:"3",touchColors:["#efda4cff","#d8d8dbff"],color:"#008000",typeValue:0}],a=function(r){var n=e.SENSORS[r].TYPE,s="_"===r.substring(0,1)?n:r;switch(n){case"COMPASS":f[s]=new o.CompassSensor;break;case"KEY":var a=e.SENSORS[r].PIN1,c=void 0;"A"===a?c=["#0000ffff"]:"B"===a&&(c=["#ff0000ff"]),t.push({name:s,touchColors:c,value:!1,port:a});break;case"LIGHT":f[s]=new o.CalliopeLightSensor;break;case"SOUND":f[s]=new o.VolumeMeterSensor(f);break;case"TEMPERATURE":f[s]=new o.TemperatureSensor;break;case"DIGITAL_PIN":(u=i.find((function(t){return t.port===e.SENSORS[r].PIN1}))).name=s,u.type="DIGITAL_PIN",u.color="#ff0000";break;case"ANALOG_PIN":var u;(u=i.find((function(t){return t.port===e.SENSORS[r].PIN1}))).name=s,u.type="ANALOG_PIN",u.color="#ff0000"}},f=this;for(var c in e.SENSORS)a(c);var u=function(t){var o=e.ACTUATORS[t].TYPE,a="_"===t.substring(0,1)?o:t;switch(o){case"BUZZER":d[a]=new r.WebAudio;break;case"RGBLED":d[a]=new r.RGBLed({x:463,y:643},!1);break;case"ANALOG_INPUT":var f=i.findIndex((function(o){return o.port===e.ACTUATORS[t].PIN1}));(c=i.splice(f,1))[0].name=a,c[0].type="ANALOG_INPUT",n.push(c[0]);break;case"DIGITAL_INPUT":var c;f=i.findIndex((function(o){return o.port===e.ACTUATORS[t].PIN1}));(c=i.splice(f,1))[0].name=a,c[0].type="DIGITAL_INPUT",n.push(c[0]);break;case"MOTOR":s.find((function(o){return o.port===e.ACTUATORS[t].PIN1})).name=a}},d=this;for(var c in e.ACTUATORS)u(c);t.length>0&&(this.buttons=new o.TouchKeys(t,this.id),this.buttons.color2Keys["#ff0000b3"]=["A","B"],this.buttons.color2Keys["#cb0034e1"]=["A","B"],this.buttons.color2Keys["#cc0033e0"]=["A","B"],this.buttons.color2Keys["#0000ff99"]=["A","B"],this.buttons.color2Keys["#fe0000b3"]=["A","B"],this.buttons.color2Keys["#cb0033e0"]=["A","B"],this.buttons.color2Keys["#0000fe99"]=["A","B"],this.buttons.color2Keys["#ca0034e0"]=["A","B"],this.buttons.color2Keys["#d3002bd8"]=["A","B"],this.buttons.color2Keys["#cb0033e0"]=["A","B"]),i.length>0&&(this.pinSensors=new o.Pins(i,this.id,{x:-28,y:30},{x:269,y:125})),n.length>0&&(this.pinActuators=new r.PinActuators(n,this.id,{x:-28,y:30}));var p=s.filter((function(e){return""!==e.name}));p.length>0&&(this.motors=new r.Motors(p,this.id)),this.display=new r.MbedDisplay({x:343,y:304})},t.prototype.handleKeyEvent=function(e){throw new Error("Method not implemented.")},t.prototype.reset=function(){e.prototype.reset.call(this),this.volume=.5},t.prototype.updateActions=function(t,o,r){e.prototype.updateActions.call(this,t,o,r);var n=this.interpreter.getRobotBehaviour().getActionState("volume",!0);(n||0===n)&&(this.volume=n/100)},t.prototype.handleNewSelection=function(e){},t}(n.RobotBaseStationary);t.default=s}));
//# sourceMappingURL=robot.calliope.js.map
//# sourceMappingURL=robot.calliope.js.map
