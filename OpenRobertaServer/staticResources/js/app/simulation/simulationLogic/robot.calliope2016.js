define([ 'simulation.robot.calliope' ], function(Calliope) {

    /**
     * Creates a new Calliope device for a simulation.
     * 
     * This Calliope is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Calliope2016(pose) {
        Calliope.call(this, pose);
    }
    Calliope2016.prototype = Object.create(Calliope.prototype);
    Calliope2016.prototype.constructor = Calliope2016;

    return Calliope2016;
});
