/**
 * A module representing the current program to be executed in the simulation.
 *
 * @module robertaLogic/program
 */
define([ 'robertaLogic.timer', 'util' ], function(Timer, UTIL) {
    var privateMem = new WeakMap();
    /**
     * @constructor Create an instance of the class program.
     * @alias module:robertaLogic/program
     */
    var Program = function() {
        var privateProperties = {
            methods : new Map(),
            program : [],
            currentMethods : [],
            nextStatement : true,
            nextFrameTimeDuration : 0,
            wait : false,
            timer : new Timer(),
            runningTimer : false
        };
        privateMem.set(this, privateProperties);
    };

    Program.prototype.setNextFrameTimeDuration = function(value) {
        privateMem.get(this).nextFrameTimeDuration = value;
    }

    Program.prototype.getNextFrameTimeDuration = function() {
        return privateMem.get(this).nextFrameTimeDuration;
    }

    /**
     * Set array of objects (statements of the program) as program.
     *
     * @param {Object}
     *            newProgram - array of objects (representing statements of the
     *            program).
     */
    Program.prototype.set = function(newProgram) {
        privateMem.get(this).program = newProgram.programStmts;
        if (newProgram.programMethods != undefined) {
            for (i in newProgram.programMethods) {
                method = newProgram.programMethods[i];
                privateMem.get(this).methods.set(method.name, method);
            }
        }
    };

    /**
     * Get created method in blockly program using the name of the method.
     *
     * @param {String}
     *            methodName - name of the method given by the user
     */
    Program.prototype.getMethod = function(methodName) {
        var method = privateMem.get(this).methods.get(methodName);
        if (method == undefined) {
            throw "Method not found";
        }
        return UTIL.clone(method);
    };

    Program.prototype.addCustomMethodForEvaluation = function(stmts, methodCallName) {
      if (stmts != undefined) {
          stmts.forEach(function(stmt) { 
             stmt['callFromFunction'] = methodCallName;       
          });
          privateMem.get(this).currentMethods = privateMem.get(this).currentMethods.concat(stmts);
      }
    };

    Program.prototype.merge = function() {
        this.prepend(privateMem.get(this).currentMethods);
        privateMem.get(this).currentMethods = [];
    };

    /**
     * Checks if there is new statement to be evaluated.
     *
     * @returns {Boolean} - True if there is still statement that needs to be
     *          evaluated
     */
    Program.prototype.isNextStatement = function() {
        return privateMem.get(this).nextStatement;
    };

    /**
     * Check if the program is finished.
     *
     * @returns {Boolean} - True if the program is finished.
     */
    Program.prototype.isTerminated = function() {
        return privateMem.get(this).program.length === 0 && this.isNextStatement();
    };

    /**
     * Get the next statement that needs to be evaluated.
     *
     * @returns {Object} - statement from the program that is next for
     *          evaluation.
     */
    Program.prototype.get = function() {
        if (privateMem.get(this).program.length === 0) {
            throw "Program is empty!";
        }
        return privateMem.get(this).program[0];
    }

    /**
     * Get the next statement for evaluation and remove from the queue of
     * program statements.
     *
     * @returns {Object} - statement from the program that is next for
     *          evaluation.
     */
    Program.prototype.getRemove = function() {
        if (privateMem.get(this).program.length === 0) {
            throw "Program is empty!";
        }
        var statement = privateMem.get(this).program[0];
        privateMem.get(this).program = privateMem.get(this).program.slice(1, privateMem.get(this).program.length);
        return statement;
    };

    /**
     * Appends statement to the array of the program statements. This statement
     * is added at position 0 of the array.
     *
     * @param programPrefix
     *            {Object} - Statement that needs to be added.
     */
    Program.prototype.prepend = function(programPrefix) {
        if (programPrefix != undefined) {
            privateMem.get(this).program = programPrefix.concat(privateMem.get(this).program);
        }
    };

    /**
     * Check if the current statement is blocking or not.
     *
     * @returns {Boolean} - true if we have evaluation of a blocking statement,
     *          o/w false
     */
    Program.prototype.isWait = function() {
        return privateMem.get(this).wait;
    };

    /**
     *
     * @param {Boolean}
     *            value - true if the statement is blocking
     */
    Program.prototype.setWait = function(value) {
        privateMem.get(this).wait = value;
    };

    /**
     * Reset the timer of the simulation.
     *
     * @param {Number}
     *            timeValue - current time
     */
    Program.prototype.resetTimer = function(timeValue) {
        privateMem.get(this).timer.setStartTime(timeValue);
        privateMem.get(this).timer.resetCurrentTime();
    };

    /**
     * Set the simulation timer to count and stop evaluation of statements until
     * the given time is reached.
     *
     * @param {Number}
     *            goalTime - until no statement is evaluated
     */
    Program.prototype.setTimer = function(goalTime) {
        this.setNextStatement(false)
        privateMem.get(this).timer.setTime(goalTime / 1000.);
    };

    /**
     *
     * @returns {Timer} - timer of the simulation
     */
    Program.prototype.getTimer = function() {
        return privateMem.get(this).timer;
    };

    Program.prototype.handleWaitTimer = function() {
        if (privateMem.get(this).runningTimer) {
            if (privateMem.get(this).timer.getCurrentTime() > privateMem.get(this).timer.getTime()) {
                privateMem.get(this).runningTimer = false;
                this.setNextStatement(true);
            }
        }
    };

    /**
     * Controls the execution of the next statement in the array.
     *
     * @param {Boolean}
     *            value - true for the next statement to be evaluated
     */
    Program.prototype.setNextStatement = function(value) {
        privateMem.get(this).nextStatement = value;
    };

    /**
     * @returns {Boolean} - true if the timer is running
     */
    Program.prototype.isRunningTimer = function() {
        return privateMem.get(this).runningTimer;
    };

    /**
     * Set the running timer
     *
     * @param {Boolean}
     *            value- true if the timer is running
     */
    Program.prototype.setIsRunningTimer = function(value) {
        privateMem.get(this).runningTimer = value;
    };

    /**
     * Converting the object to string for debugging.
     *
     * @returns {String} - String representation of the object.
     */
    Program.prototype.toString = function() {
        return JSON.stringify([ privateMem.get(this).program, privateMem.get(this).currentMethods, privateMem.get(this).timer, privateMem.get(this).nextFrameTimeDuration ]);
    };

    return Program;
});
