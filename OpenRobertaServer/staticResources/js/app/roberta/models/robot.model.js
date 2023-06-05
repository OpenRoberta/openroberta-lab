define(["require","exports","comm"],(function(e,t,o){Object.defineProperty(t,"__esModule",{value:!0}),t.setRobot=t.setToken=t.updateFirmware=void 0,t.updateFirmware=function(e){o.json("/admin/updateFirmware",{cmd:"updateFirmware"},e,"update firmware")},t.setToken=function(e,t){o.json("/admin/setToken",{cmd:"setToken",token:e},t,"set token '"+e+"'")},t.setRobot=function(e,t){return o.json("/admin/setRobot",{cmd:"setRobot",robot:e},t,"set robot '"+e+"'")}}));
//# sourceMappingURL=robot.model.js.map
//# sourceMappingURL=robot.model.js.map
