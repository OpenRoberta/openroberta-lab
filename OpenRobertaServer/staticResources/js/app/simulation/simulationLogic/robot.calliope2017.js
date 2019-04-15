define([ 'simulation.robot.calliope' ], function(Calliope) {

    /**
     * Creates a new Calliope device for a simulation.
     * 
     * This Calliope is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Calliope2017(pose) {
        Calliope.call(this, pose);
    }
    Calliope2017.prototype = Object.create(Calliope.prototype);
    Calliope2017.prototype.constructor = Calliope2017;

    return Calliope2017;
});
