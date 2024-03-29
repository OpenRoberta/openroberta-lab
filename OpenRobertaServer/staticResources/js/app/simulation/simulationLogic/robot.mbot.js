var __extends=this&&this.__extends||function(){var e=function(s,t){return e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,s){e.__proto__=s}||function(e,s){for(var t in s)Object.prototype.hasOwnProperty.call(s,t)&&(e[t]=s[t])},e(s,t)};return function(s,t){if("function"!=typeof t&&null!==t)throw new TypeError("Class extends value "+String(t)+" is not a constructor or null");function o(){this.constructor=s}e(s,t),s.prototype=null===t?Object.create(t):(o.prototype=t.prototype,new o)}}();define(["require","exports","robot.ev3","robot.actuators","robot.sensors","robot.base.mobile"],(function(e,s,t,o,n,r){Object.defineProperty(s,"__esModule",{value:!0});var i=function(e){function s(s,t,o,n,r){return e.call(this,s,t,o,n,r)||this}return __extends(s,e),s.prototype.configure=function(e){e.TRACKWIDTH=11.5,e.WHEELDIAMETER=6.5,this.chassis=new o.MbotChassis(this.id,e,3,this.pose),this.RGBLedLeft=new o.MbotRGBLed({x:20,y:-10},!0,"2"),this.RGBLedRight=new o.MbotRGBLed({x:20,y:10},!0,"1"),this.display=new o.MbotDisplay(this.id,{x:15,y:50});var s=e.SENSORS,t=function(e){switch(s[e].TYPE){case"ULTRASONIC":var t=[],o=i;Object.keys(i).forEach((function(e){o[e]&&o[e]instanceof n.DistanceSensor&&t.push(o[e])}));var a=t.length+1,c=Object.keys(s).filter((function(e){return"ULTRASONIC"==s[e].TYPE})).length,h=new r.Pose(i.chassis.geom.x+i.chassis.geom.w,0,0);if(3==c)1==a?h=new r.Pose(i.chassis.geom.h/2,-i.chassis.geom.h/2,-Math.PI/4):2==a&&(h=new r.Pose(i.chassis.geom.h/2,i.chassis.geom.h/2,Math.PI/4));else if(c%2==0)switch(a){case 1:h=new r.Pose(i.chassis.geom.x+i.chassis.geom.w,-i.chassis.geom.h/2,-Math.PI/4);break;case 2:h=new r.Pose(i.chassis.geom.x+i.chassis.geom.w,i.chassis.geom.h/2,Math.PI/4);break;case 3:h=new r.Pose(i.chassis.geom.x,-i.chassis.geom.h/2,-3*Math.PI/4);break;case 4:h=new r.Pose(i.chassis.geom.x,i.chassis.geom.h/2,3*Math.PI/4)}i[e]=new n.UltrasonicSensor(e,h.x,h.y,h.theta,255);break;case"INFRARED":var u=[],f=i;Object.keys(i).forEach((function(e){f[e]&&f[e]instanceof n.MbotInfraredSensor&&u.push(f[e])})),0==u.length&&(i[e]=new n.MbotInfraredSensor(e,{x:26,y:0}))}},i=this;for(var a in s)t(a);this.buttons=new n.MbotButton([{name:"center",value:!1,port:"center",touchColors:["#000000ff"]}],this.id)},s}(t.default);s.default=i}));
//# sourceMappingURL=robot.mbot.js.map
//# sourceMappingURL=robot.mbot.js.map
