define([ 'simulation.simulation', 'robertaLogic.constants', 'util' ], function(SIM, CONSTANTS, UTIL) {

    /**
     * Creates a new mbed device for a simulation.
     * 
     * This mbed is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Mbed(pose) {
        this.pose = pose;
    }

    Mbed.prototype.endless = true;
    Mbed.prototype.debug = true;
    Mbed.prototype.idle = true;

    Mbed.prototype.reset = function() {
        for ( var property in this.buttons) {
            property = false;
        }
        this.display.leds = [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ];
        clearTimeout(this.display.timeout);

        this.gesture = {};
        this.gesture.up = true;
        $("#simRobotContent").html('');
        $("#simRobotContent").html(this.controle);
        var that = this;
        $('input[name="options"]').change(function(e) {
            that.gesture = {};
            that.gesture[e.currentTarget.id] = true;
        });
        this.compass.degree = 0;
        $('#slider').on("mousedown touchstart", function(e) {
            e.stopPropagation();
        });
        $('#slider').change(function(e) {
            e.preventDefault();
            $('#range').html($('#slider').val());
            that.compass.degree = $('#slider').val();
            e.stopPropagation();
        });
    }

    Mbed.prototype.resetPose = function() {
    }

    Mbed.prototype.display = {
        leds : [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ],
        color : [ '#ffffff', '#ffe263', '#ffe300', '#ffdb00', '#ffc900', '#ffb800', '#ff8f00', '#ff7100', '#ff6400', '#ff4c02' ],
        x : -60,
        y : 80,
        r : 5,
        rL : 7,
        rLight : 100,
        dx : 30,
        dy : 30,
        lightLevel : 100,
        draw : function(canvas) {
            canvas.beginPath();
            canvas.fillStyle = '#ffffff';
            canvas.globalAlpha = this.lightLevel / 800;
            canvas.rect(this.x - this.dx / 2, this.y + this.dy / 2, 5 * this.dx, -5 * this.dy);
            canvas.fill();
            canvas.beginPath();
            canvas.globalAlpha = 1;
            for (var i = 0; i < this.leds.length; i++) {
                for (var j = 0; j < this.leds[i].length; j++) {
                    var colorIndex = UTIL.round(this.leds[j][i] * 0.035294118, 0);
                    canvas.fillStyle = this.color[colorIndex];
                    canvas.beginPath();
                    canvas.rect(this.x + i * this.dx - this.r + 2, this.y - j * this.dy - 2 * this.r + 2, 2 * (this.r - 2), 4 * this.r - 4);
                    canvas.fill();
                    if (colorIndex > 0) {
                        canvas.beginPath();
                        canvas.globalAlpha = 0.5;
                        canvas.arc(this.x + i * this.dx, this.y - j * this.dy - this.rL, this.rL, 0, Math.PI * 2);
                        canvas.arc(this.x + i * this.dx, this.y - j * this.dy + this.rL, this.rL, 0, Math.PI * 2);
                        canvas.rect(this.x + i * this.dx - this.rL, this.y - j * this.dy - this.rL, 2 * this.rL, 2 * this.rL);
                        canvas.fill();
                        canvas.beginPath();
                        canvas.globalAlpha = 1;
                    }
                }
            }
        }
    }

    Mbed.prototype.buttons = {
        A : false,
        B : false,
        Reset : false
    }

    Mbed.prototype.temperature = {
        degree : 25
    }

    Mbed.prototype.time = 0;
    Mbed.prototype.timer = {
        timer1 : false
    }
    /**
     * Update all actions of the Mbed. The new pose is calculated with the
     * forward kinematics equations for a differential drive Mbed.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Mbed.prototype.update = function(actions) {
        if (actions.display) {
            if (actions.display.text) {
                var that = Mbed.prototype;
                var textArray = generate(actions.display.text);
                function f(textArray, that) {
                    if (textArray && textArray.length >= 5) {
                        var array = textArray.slice(0, 5);
                        var newArray = array[0].map(function(col, i) {
                            return array.map(function(row) {
                                return row[i]
                            })
                        });
                        that.display.leds = newArray;
                        textArray.shift();
                        that.display.timeout = setTimeout(f, 150, textArray, that);
                    }
                }
                f(textArray, that);
            }
            if (actions.display.picture) {
                if (actions.display.mode == 'animation') {
                    var animation = actions.display.picture;
                    var that = this;
                    function f(animation, index, that) {
                        if (animation && animation.length > index) {
                            that.display.leds = animation[index];
                            that.display.timeout = setTimeout(f, 150, animation, index + 1, that);
                        }
                    }
                    f(animation, 0, that);
                } else {
                    this.display.leds = actions.display.picture;
                }
            }
            if (actions.display.clear) {
                this.display.leds = [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ];
            }
        }
        // update timer
        if (actions.timer) {
            for (key in actions.timer) {
                if (actions.timer[key] == 'reset') {
                    this.timer[key] = 0;
                }
            }
        }
    }

    var generate = function(text) {
        var string = [];
        string.push([ 0, 0, 0, 0, 0 ]);
        string.push([ 0, 0, 0, 0, 0 ]);
        for (var i = 0; i < text.length; i++) {
            var letter = letters[text[i]];
            if (!letter)
                letter = letters['blank'];
            var newLetter = Array(letter[0] * 5).fill(0);
            for (var j = 1; j < letter.length; j++) {
                newLetter[letter[j] - 1] = 255;
            }
            while (newLetter.length) {
                string.push(newLetter.splice(0, 5));
            }
            string.push([ 0, 0, 0, 0, 0 ]);
            string.push([ 0, 0, 0, 0, 0 ]);
        }
        string.push([ 0, 0, 0, 0, 0 ]);
        string.push([ 0, 0, 0, 0, 0 ]);
        string.push([ 0, 0, 0, 0, 0 ]);
        return string;
    }

    var letters = {
        A : [ 4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20 ],
        Ä : [ 4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20 ],
        B : [ 4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 17, 19 ],
        C : [ 4, 2, 3, 4, 6, 10, 11, 15, 16, 20 ],
        D : [ 4, 1, 2, 3, 4, 5, 6, 10, 11, 15, 17, 18, 19 ],
        E : [ 4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 16, 20 ],
        F : [ 4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 16 ],
        G : [ 5, 2, 3, 4, 6, 10, 11, 15, 16, 18, 20, 23, 24 ],
        H : [ 4, 1, 2, 3, 4, 5, 8, 13, 16, 17, 18, 19, 20 ],
        I : [ 3, 1, 5, 6, 7, 8, 9, 10, 11, 15 ],
        J : [ 5, 1, 4, 6, 10, 11, 15, 16, 17, 18, 19, 21 ],
        K : [ 4, 1, 2, 3, 4, 5, 8, 12, 14, 16, 20 ],
        L : [ 4, 1, 2, 3, 4, 5, 10, 15, 20 ],
        M : [ 5, 1, 2, 3, 4, 5, 7, 13, 17, 21, 22, 23, 24, 25 ],
        N : [ 5, 1, 2, 3, 4, 5, 7, 13, 19, 21, 22, 23, 24, 25 ],
        O : [ 4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19 ],
        Ö : [ 4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19 ],
        P : [ 4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17 ],
        Q : [ 4, 2, 3, 6, 9, 11, 14, 15, 17, 18, 20 ],
        R : [ 5, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17, 19, 25 ],
        S : [ 4, 2, 5, 6, 8, 10, 11, 13, 15, 16, 19 ],
        T : [ 5, 1, 6, 11, 12, 13, 14, 15, 16, 21 ],
        U : [ 4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19 ],
        Ü : [ 4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19 ],
        V : [ 5, 1, 2, 3, 9, 15, 19, 21, 22, 23 ],
        W : [ 5, 1, 2, 3, 4, 5, 9, 13, 19, 21, 22, 23, 24, 25 ],
        X : [ 4, 1, 2, 4, 5, 8, 13, 16, 17, 19, 20 ],
        Y : [ 5, 1, 7, 13, 14, 15, 17, 21 ],
        Z : [ 4, 1, 4, 5, 6, 8, 10, 11, 12, 15, 16, 20 ],
        a : [ 5, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 25 ],
        ä : [ 5, 1, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 21, 25 ],
        b : [ 4, 1, 2, 3, 4, 5, 8, 10, 13, 15, 19 ],
        c : [ 4, 3, 4, 7, 10, 12, 15, 17, 20 ],
        d : [ 4, 4, 8, 10, 13, 15, 16, 17, 18, 19, 20 ],
        e : [ 4, 2, 3, 4, 6, 8, 10, 11, 13, 15, 17, 20 ],
        f : [ 4, 3, 7, 8, 9, 10, 11, 13, 16 ],
        g : [ 4, 2, 6, 8, 10, 11, 13, 15, 16, 17, 18, 19 ],
        h : [ 5, 1, 2, 3, 4, 5, 8, 13, 19, 20 ],
        i : [ 1, 1, 3, 4, 5 ],
        j : [ 3, 5, 10, 11, 13, 14, ],
        k : [ 4, 1, 2, 3, 4, 5, 8, 12, 14, 20 ],
        l : [ 3, 1, 2, 3, 4, 10, 15 ],
        m : [ 5, 2, 3, 4, 5, 7, 13, 17, 22, 23, 24, 25 ],
        n : [ 4, 2, 3, 4, 5, 7, 12, 18, 19, 20 ],
        o : [ 4, 3, 4, 7, 10, 12, 15, 18, 19 ],
        ö : [ 4, 1, 3, 4, 7, 10, 12, 15, 16, 18, 19 ],
        p : [ 4, 2, 3, 4, 5, 7, 9, 12, 14, 18 ],
        q : [ 4, 3, 7, 9, 12, 14, 17, 18, 19, 20 ],
        r : [ 4, 3, 4, 5, 7, 12, 17 ],
        s : [ 4, 5, 8, 10, 12, 14, 17 ],
        t : [ 4, 1, 2, 3, 4, 8, 10, 13, 15, 20 ],
        u : [ 5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25 ],
        ü : [ 5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25 ],
        v : [ 5, 2, 3, 9, 15, 19, 22, 23 ],
        x : [ 4, 2, 5, 8, 9, 13, 14, 17, 20 ],
        y : [ 5, 2, 5, 8, 10, 14, 18, 22 ],
        z : [ 4, 2, 5, 7, 9, 10, 12, 13, 15, 17, 20 ],
        blank : [ 5 ],
        '!' : [ 1, 1, 2, 3, 5 ],
        '?' : [ 5, 2, 6, 11, 13, 15, 16, 18, 22 ],
        ',' : [ 2, 5, 9 ],
        '.' : [ 1, 4 ],
        '[' : [ 3, 1, 2, 3, 4, 5, 6, 10, 11, 15 ],
        ']' : [ 3, 1, 5, 6, 10, 11, 12, 13, 14, 15 ],
        '{' : [ 3, 3, 6, 7, 8, 9, 10, 11, 15 ],
        '}' : [ 3, 1, 5, 6, 7, 8, 9, 10, 13 ],
        '(' : [ 2, 2, 3, 4, 6, 10 ],
        ')' : [ 2, 1, 5, 7, 8, 9 ],
        '<' : [ 3, 3, 7, 9, 11, 15, ],
        '>' : [ 3, 1, 5, 7, 9, 13 ],
        '/' : [ 5, 5, 9, 13, 17, 21 ],
        '\\' : [ 5, 1, 7, 13, 19, 25 ],
        ':' : [ 1, 2, 4 ],
        ';' : [ 2, 5, 7, 9 ],
        ',' : [ 2, 5, 9 ],
        '"' : [ 3, 1, 2, 11, 12 ],
        "'" : [ 1, 1, 2 ],
        '@' : [ 5, 2, 3, 4, 6, 10, 11, 13, 15, 16, 19, 22, 23, 24 ],
        '#' : [ 5, 2, 4, 6, 7, 8, 9, 10, 12, 14, 16, 17, 18, 19, 20, 22, 24 ],
        '%' : [ 5, 1, 2, 5, 6, 9, 13, 17, 20, 21, 24, 25 ],
        '^' : [ 3, 2, 6, 12 ],
        '*' : [ 3, 2, 4, 8, 12, 14 ],
        '-' : [ 3, 3, 8, 13 ],
        '+' : [ 3, 3, 7, 8, 9, 13 ],
        '_' : [ 5, 5, 10, 15, 20, 25 ],
        '=' : [ 3, 2, 4, 7, 9, 12, 14 ],
        '|' : [ 1, 1, 2, 3, 4, 5 ],
        '~' : [ 4, 3, 8, 14, 19 ],
        '`' : [ 2, 1, 7 ],
        '´' : [ 2, 2, 6 ],
        0 : [ 4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19 ],
        1 : [ 3, 2, 5, 6, 7, 8, 9, 10, 15 ],
        2 : [ 4, 1, 4, 5, 6, 8, 10, 11, 13, 15, 17, 20 ],
        3 : [ 5, 1, 4, 6, 10, 11, 13, 15, 16, 17, 19 ],
        4 : [ 5, 3, 4, 7, 9, 11, 14, 16, 17, 18, 19, 20, 24 ],
        5 : [ 5, 1, 2, 3, 5, 6, 8, 10, 11, 13, 15, 16, 18, 20, 21, 24 ],
        6 : [ 5, 4, 8, 10, 12, 13, 15, 16, 18, 20, 24 ],
        7 : [ 5, 1, 5, 6, 9, 11, 13, 16, 17, 21 ],
        8 : [ 5, 2, 4, 6, 8, 10, 11, 13, 15, 16, 18, 20, 22, 24 ],
        9 : [ 5, 2, 6, 8, 10, 11, 13, 14, 16, 18, 22 ]
    }
    var isDownrobot = false;
    var isDownObstacle = false;
    var isDownRuler = false;
    var startX;
    var startY;

    Mbed.prototype.handleMouse = function(e, offsetX, offsetY, scale, w, h) {
        w = w / scale;
        h = h / scale;
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top + $('#robotLayer').width()/2;
        var left = $('#robotLayer').offset().left + $('#robotLayer').height()/2;
        startX = (parseInt(X - left, 10)) / scale;
        startY = (parseInt(Y - top, 10)) / scale;
        var scsq = 1;
        if (scale < 1)
            scsq = scale * scale;
        var dxA = startX - this.button.xA;
        var dyA = startY + this.button.yA;
        var A = (dxA * dxA + dyA * dyA < this.button.rA * this.button.rA / scsq);
        var dxB = startX - this.button.xB;
        var dyB = startY + this.button.yB;
        var B = (dxB * dxB + dyB * dyB < this.button.rB * this.button.rB / scsq);
        var dxReset = startX - this.button.xReset;
        var dyReset = startY + this.button.yReset;
        var Reset = (dxReset * dxReset + dyReset * dyReset < this.button.rReset * this.button.rReset / scsq);
        // special case, display (center: 0,0) represents light level
        var dxDisplay = startX;
        var dyDisplay = startY + 20;
        var Display = (dxDisplay * dxDisplay + dyDisplay * dyDisplay < this.display.rLight * this.display.rLight); //   
        this.display.lightLevel = 100;
        if (A || B || Reset || Display) {          
            if (e.type === 'mousedown') {
                if (A) {
                    this.buttons.A = true;
                }
                if (B) {
                    this.buttons.B = true;
                }
                if (Display) {
                    this.display.lightLevel = 150;
                }
                if (Reset) {
                    this.buttons.Reset = true;
                }
            } else if (e.type === 'mousemove' && Display) {
                this.display.lightLevel = 50;
            }
            if (Display) {
                $("#robotLayer").css('cursor', 'crosshair');
            } else {
                $("#robotLayer").css('cursor', 'pointer');
            }
        } else {
            $("#robotLayer").css('cursor', 'auto');        
        }      
    }

    Mbed.prototype.handleMouseUp = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    }
    Mbed.prototype.handleMouseOut = function(e, offsetX, offsetY) {
        return;
    }

    Mbed.prototype.handleMouseMove = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    }

    Mbed.prototype.handleMouseDown = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    }

    Mbed.prototype.controle = function() {
        $('#simRobotContent').append('<div id="mbedContent"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons">' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_GESTURE + '</label>' + //
        '<label class="btn simbtn active"><input type="radio" id="up" name="options" autocomplete="off">' + Blockly.Msg.SENSOR_GESTURE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="down" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_up" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_down"name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="shake" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_SHAKE + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="freefall" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FREEFALL + '</label>' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_COMPASS + '</label><span style="margin-bottom: 8px;margin-top: 12px; min-width: 25px; width: 25px; display: inline-block" id="range">0</span>' + '<div style="margin:8px 0; "><input id="slider" type="range" min="0" max="360" value="0" step="5" /></div></div></div>');
    }

    Mbed.prototype.gesture = {
        up : true
    }

    Mbed.prototype.compass = {
        degree : 0
    }

    return Mbed;
});
