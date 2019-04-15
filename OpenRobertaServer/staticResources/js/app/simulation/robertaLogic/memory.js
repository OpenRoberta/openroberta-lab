/**
 * Module representing the Memory part of the simulation. This is used for
 * storing all the variables used in a program.
 *
 * @module robertaLogic/memory
 */
define(function() {
    /**
     * @constructor
     * @alias module:robertaLogic/sensors
     */
    var Memory = function() {
        /**
         * Object where all program variables are stored (pairs of name and
         * value).
         */
        this.memory = {};
        this.methodCalls = new Map();
    };

    /**
     * Declaration of a variable used in the program.
     *
     * @param {String}
     *            name - of the variable,
     * @param {}
     *            init - initial value of the variable.
     */
    Memory.prototype.decl = function(name, init) {
        if (this.memory[name] != undefined) {
            throw "Variable " + name + " is defined!";
        }
        if (init == undefined) {
            throw "Variable " + name + " not initialized!";
        }
        this.memory[name] = init;
    };

    /**
     * Assign value to an existing variable in the memory.
     *
     * @param {String}
     *            name - of the variable,
     * @param {}
     *            value - assigned to the variable.
     */
    Memory.prototype.assign = function(name, value) {
        if (this.memory[name] == undefined) {
            throw "Variable " + name + " is undefined!";
        }
        if (value == undefined) {
            throw "Variable " + name + " not assigned!";
        }
        this.memory[name] = value;
    };

    /**
     * Return the value of a given variable.
     *
     * @param {String}
     *            name - of the variable,
     * @returns {} - value of the required variable
     */
    Memory.prototype.get = function(name) {
        //        if (this.memory[name] == undefined) {
        //            throw "Variable " + name + " is undefined!";
        //        }
        return this.memory[name];
    };

    /**
     * Remove given variable.
     *
     * @param {String}
     *            name - of the variable,
     */
    Memory.prototype.remove = function(name) {
        if (this.memory[name] == undefined) {
            throw "Variable " + name + " is undefined!";
        }
        delete this.memory[name];
    };

    /**
     * Clears all stored variables from the memory.
     */
    Memory.prototype.clear = function() {
        this.memory = {};
        this.methodCalls.clear();
    };

    /**
     * Increases the number of calls counter for user defined method.
     *
     * @param {String}
     *            methodName - name of the user defined method,
     */
    Memory.prototype.increaseMethodCalls = function(methodName) {
        var numberOfCalls = this.methodCalls.get(methodName);
        if (numberOfCalls == undefined) {
            numberOfCalls = 1;
        } else {
            numberOfCalls++;
        }
        this.methodCalls.set(methodName, numberOfCalls);
    };

    /**
     * Get the number of times a user defined function has been called.
     *
     * @param {String}
     *            methodName - name of the user defined method,
     * @returns {Number} - of calls of the user defined method
     */
    Memory.prototype.getNumberOfMethodCalls = function(methodName) {
        var numberOfCalls = this.methodCalls.get(methodName);
        if (numberOfCalls == undefined) {
            throw 'The method is not defined yet!';
        }
        return numberOfCalls;
    };

    /**
     * Converting the object to string for debugging.
     *
     * @returns {String} - String representation of the object.
     */
    Memory.prototype.toString = function() {
        return JSON.stringify(this.memory);
    };

    return Memory;
});
