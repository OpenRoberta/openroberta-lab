define([ 'exports', 'comm' ], function(exports, COMM) {

    /*
     * represents the state of connected wedo devices with the following
     * structure: {<name of the device> { 1 : { tiltsensor : "0.0" }, 2 : {
     * motionsensor : "4.0 }, batterylevel : "100", button : "false" }
     */

    var wedo = {};
    var tiltMode = {
        UP : '3.0',
        DOWN : '9.0',
        BACK : '5.0',
        FRONT : '7.0',
        NO : '0.0'
    }

    function update(data) {
        if (data.target !== "wedo") {
            return;
        }
        switch (data.type) {
        case "connect":
            if (data.state == "connected") {
                wedo[data.brickid] = {};
                wedo[data.brickid]["brickname"] = data.brickname.replace(/\s/g, '').toUpperCase();
                // for some reason we do not get the inital state of the button, so here it is hardcoded
                wedo[data.brickid]["button"] = 'false';
            } else if (data.state == "disconnected") {
                delete wedo[data.brickid];
            }
            break;
        case "didAddService":
            if (data.state == "connected") {
                if (data.id && data.sensor) {
                    wedo[data.brickid][data.id] = {};
                    wedo[data.brickid][data.id][data.sensor.replace(/\s/g, '').toLowerCase()] = '';
                } else if (data.id && data.actuator) {
                    wedo[data.brickid][data.id] = {};
                    wedo[data.brickid][data.id][data.actuator.replace(/\s/g, '').toLowerCase()] = '';
                } else if (data.sensor) {
                    wedo[data.brickid][data.sensor.replace(/\s/g, '').toLowerCase()] = '';
                } else {
                    wedo[data.brickid][data.actuator.replace(/\s/g, '').toLowerCase()] = '';
                }
            }
            break;
        case "didRemoveService":
            if (data.id) {
                delete wedo[data.brickid][data.id];
            } else if (data.sensor) {
                delete wedo[data.brickid][data.sensor.replace(/\s/g, '').toLowerCase()]
            } else {
                delete wedo[data.brickid][data.actuator.replace(/\s/g, '').toLowerCase()]
            }
            break;
        case "update":
            if (data.id) {
                wedo[data.brickid][data.id][data.sensor.replace(/\s/g, '').toLowerCase()] = data.state;
            } else {
                wedo[data.brickid][data.sensor.replace(/\s/g, '').toLowerCase()] = data.state;
            }
            break;
        default:
            // TODO think about what could happen here.
            break;
        }
        console.log(wedo);
    }
    exports.update = update;

    function getSensorValue(brickid, sensor, id, slot) {
        switch (sensor) {
        case "tiltsensor":
            if (slot === "ANY") {
                return wedo[brickid][id][sensor] !== tiltMode.NO;
            } else {
                return wedo[brickid][id][sensor] === tiltMode[slot];
            }
        case "motionsensor":
            return parseInt(wedo[brickid][id][sensor]);
        case "button":
            return wedo[brickid][sensor] === "true";
        }
    }
    exports.getSensorValue = getSensorValue;

    function getConnectedBricks() {
        var brickids = [];
        for ( var brickid in wedo) {
            if (wedo.hasOwnProperty(brickid)) {
                brickids.push(brickid);
            }
        }
        return brickids;

    }
    exports.getConnectedBricks = getConnectedBricks;

    function getBrickIdByName(name) {
        for ( var brickid in wedo) {
            if (wedo.hasOwnProperty(brickid)) {
                if (wedo[brickid].brickname === name.toUpperCase()) {
                    return brickid;
                }
            }
        }
        return null;
    }
    exports.getBrickIdByName = getBrickIdByName;

    function getBrickById(id) {
        return wedo[id];
    }
    exports.getBrickById = getBrickById;
});
