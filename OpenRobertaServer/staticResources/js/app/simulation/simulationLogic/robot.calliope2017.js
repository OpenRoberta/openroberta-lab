define([ 'simulation.robot.calliope' ], function(Calliope) {

    /**
     * Creates a new Calliope device for a simulation.
     * 
     * This Calliope is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Calliope2017(pose, num, robotBehaviour) {
        Calliope.call(this, pose, num, robotBehaviour);
    }
    Calliope2017.prototype = Object.create(Calliope.prototype);
    Calliope2017.prototype.constructor = Calliope2017;

    return Calliope2017;
});
