var __extends=this&&this.__extends||function(){var e=function(t,s){return e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var s in t)Object.prototype.hasOwnProperty.call(t,s)&&(e[s]=t[s])},e(t,s)};return function(t,s){if("function"!=typeof s&&null!==s)throw new TypeError("Class extends value "+String(s)+" is not a constructor or null");function o(){this.constructor=t}e(t,s),t.prototype=null===s?Object.create(s):(o.prototype=s.prototype,new o)}}();define(["require","exports","robot.base.mobile","robot.sensors","robot.actuators","jquery"],(function(e,t,s,o,n,i){Object.defineProperty(t,"__esModule",{value:!0});var r=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.timer=new o.Timer(5),t}return __extends(t,e),t.prototype.updateActions=function(t,s,o){e.prototype.updateActions.call(this,t,s,o)},t.prototype.reset=function(){e.prototype.reset.call(this)},t.prototype.configure=function(e){this.chassis=new n.Txt4Chassis(this.id,e,1.75,this.pose),this.led=new n.Txt4RGBLed(this.id,{x:0,y:0},!0,null,3);var t=e.SENSORS,r=function(e){if(t[e].TYPE)switch(t[e].TYPE){case"TXT_CAMERA":var n=t[e].RESOLUTION.split("x"),i=0,r=0,c=t[e].SUBCOMPONENTS;for(var h in c){if(c[h].TYPE&&"CAMERA_COLORDETECTOR"===c[h].TYPE){var u=c[h].XEND-c[h].XSTART,f=c[h].YEND-c[h].YSTART;i=(100*u/n[0]+100*f/n[1])/2}if(c[h].TYPE&&"CAMERA_LINE"===c[h].TYPE){var p=c[h].MINIMUM;r=c[h].MAXIMUM-p}}a[e]=new o.Txt4CameraSensor(new s.Pose(0,0,0),2*Math.PI/5,i,r);break;case"INFRARED":a[e]=new o.Txt4InfraredSensors(e,{x:14,y:0});break;case"ULTRASONIC":var l=[],m=a;Object.keys(a).forEach((function(e){m[e]&&m[e]instanceof o.DistanceSensor&&l.push(m[e])}));var y=l.length+1,P=Object.keys(t).filter((function(e){return"ULTRASONIC"==t[e].TYPE})).length,w=new s.Pose(a.chassis.geom.x+a.chassis.geom.w,0,0);if(3==P)1==y?w=new s.Pose(a.chassis.geom.h/2,-a.chassis.geom.h/2,-Math.PI/4):2==y&&(w=new s.Pose(a.chassis.geom.h/2,a.chassis.geom.h/2,Math.PI/4));else if(P%2==0)switch(y){case 1:w=new s.Pose(a.chassis.geom.x+a.chassis.geom.w,-a.chassis.geom.h/2,-Math.PI/4);break;case 2:w=new s.Pose(a.chassis.geom.x+a.chassis.geom.w,a.chassis.geom.h/2,Math.PI/4);break;case 3:w=new s.Pose(a.chassis.geom.x,-a.chassis.geom.h/2,-3*Math.PI/4);break;case 4:w=new s.Pose(a.chassis.geom.x,a.chassis.geom.h/2,3*Math.PI/4)}a[e]=new o.UltrasonicSensor(e,w.x,w.y,w.theta,400)}},a=this;for(var c in t)r(c);this.buttons=new o.EV3Keys([{name:"txt4ButtonLeft",value:!1},{name:"txt4ButtonRight",value:!1}],this.id);var h=i("#txt4StopProgram"+this.id),u=this;for(var f in h.on("mousedown touchstart",(function(){u.interpreter.terminate()})),this.buttons.keys){var p=i("#"+this.buttons.keys[f].name+u.id);p.on("mousedown touchstart",(function(){u.buttons.keys[this.id.replace(/\d+$/,"")].value=!0})),p.on("mouseup touchend",(function(){u.buttons.keys[this.id.replace(/\d+$/,"")].value=!1}))}},t}(s.RobotBaseMobile);t.default=r}));
//# sourceMappingURL=robot.txt4.js.map
//# sourceMappingURL=robot.txt4.js.map
