define([ 'exports', 'comm' ], function(exports, COMM) {

    /*
     * represents the state of connected wedo devices with the following
     * structure: {<name of the device> { 1 : { tiltsensor : "0.0" }, 2 : {
     * motionsensor : "4.0 }, batterylevel : "100", button : "false" }
     */

    var wedo = {};

    function update(data) {
        if (data.target !== "wedo") {
            return;
        }
        switch (data.op.type) {
        case "connect":
            wedo[data.op.brickid] = {};
            wedo[data.op.brickid]["brickname"] = data.op.brickname.replace(/\s/g, '');
            // for some reason we do not get the inital state of the button, so here it is hardcoded
            wedo[data.op.brickid]["button"] = 'false';
            // update configuration for brickname

            break;
        case "disconnect":
            delete exports.wedo[data.op.brickid];
            break;
        case "didAddService":
            if (data.op.state == "connected") {
                if (data.op.id && data.op.sensor) {
                    wedo[data.op.brickid][data.op.id] = {};
                    wedo[data.op.brickid][data.op.id][data.op.sensor.replace(/\s/g, '').toLowerCase()] = '';
                } else if (data.op.id && data.op.actuator) {
                    wedo[data.op.brickid][data.op.id] = {};
                    wedo[data.op.brickid][data.op.id][data.op.actuator.replace(/\s/g, '').toLowerCase()] = '';
                } else if (data.op.sensor) {
                    wedo[data.op.brickid][data.op.sensor.replace(/\s/g, '').toLowerCase()] = '';
                } else {
                    wedo[data.op.brickid][data.op.actuator.replace(/\s/g, '').toLowerCase()] = '';
                }
            }
            break;
        case "didRemoveService":
            if (data.op.id) {
                delete wedo[data.op.brickid][data.op.id];
            } else if (data.op.sensor) {
                delete wedo[data.op.brickid][data.op.sensor.replace(/\s/g, '').toLowerCase()]
            } else {
                delete wedo[data.op.brickid][data.op.actuator.replace(/\s/g, '').toLowerCase()]
            }
            break;
        case "update":
            if (data.op.id) {
                wedo[data.op.brickid][data.op.id][data.op.sensor.replace(/\s/g, '').toLowerCase()] = data.op.state;
            } else {
                wedo[data.op.brickid][data.op.sensor.replace(/\s/g, '').toLowerCase()] = data.op.state;
            }
            break;
        default:
            // TODO think about what could happen here.
            break;
        }
        console.log(wedo);
    }
    exports.update = update;

    function getSensorValue(brickid, sensor, opt_id) {
        console.log(brickid + ' ' + sensor + ' ' + opt_id);
        var returnValue = undefined;
        try {
            if (opt_id) {
                returnValue = wedo[brickid][opt_id][sensor];
            } else {
                returnValue = wedo[brickid][sensor];
            }
            return returnValue;
        } catch (error) {
            // TODO check if we want to stop program execution here
            return returnValue;
            alert(error.message);
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
                if (wedo[brickid].brickname === name) {
                    return brickid;
                }
            }
        }
        return null;
    }
    exports.getBrickIdByName = getBrickIdByName;

});
