function Timer() {
    this.startTime = 0;
    this.currentTime = 0;
    this.time = 0;
}

Timer.prototype.setStartTime = function(value) {
    this.startTime = value;
};

Timer.prototype.getStartTime = function() {
    return this.startTime;
};

Timer.prototype.setCurrentTime = function(value) {
    this.currentTime = Math.abs(value - this.startTime);
};

Timer.prototype.getCurrentTime = function() {
    return this.currentTime;
};

Timer.prototype.setTime = function(value) {
    this.time = value;
};

Timer.prototype.getTime = function() {
    return this.time;
};

function Motor() {
    this.power = 0;
    this.stopped = false;
    this.startRotations = 0;
    this.currentRotations = 0;
    this.rotations = 0;
}

Motor.prototype.getPower = function() {
    return this.power;
};

Motor.prototype.setPower = function(value) {
    this.power = value;
};

Motor.prototype.isStopped = function() {
    return this.stopped;
};

Motor.prototype.setStopped = function(value) {
    this.stopped = value;
};

Motor.prototype.getCurrentRotations = function() {
    return this.currentRotations;
};

Motor.prototype.setCurrentRotations = function(value) {
    this.currentRotations = Math.abs(value / 360. - this.startRotations);
};

Motor.prototype.getRotations = function() {
    return this.rotations;
};

Motor.prototype.setRotations = function(value) {
    this.rotations = value;
};
