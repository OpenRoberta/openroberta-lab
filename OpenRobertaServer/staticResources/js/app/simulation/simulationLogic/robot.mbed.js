define([ 'simulation.simulation', 'interpreter.constants', 'util', 'simulation.robot' ], function(SIM, C, UTIL, Robot) {

    /**
     * Creates a new mbed device for a simulation.
     * 
     * This mbed is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Mbed(pose, num, robotBehaviour) {
        Robot.call(this, pose, robotBehaviour);

        this.endless = true;
        this.debug = true;
        this.idle = true;
        this.display = {
            leds : [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ],
            color : [ '255,255,255', '255,226,99', '255,227,0', '255,219,0', '255,201,0', '255,184,0', '255,143,0', '255, 113, 0', '255, 100, 0', '255, 76, 2' ],
            x : -60,
            y : 80,
            r : 5,
            rL : 7,
            rLight : 100,
            dx : 30,
            dy : 30,
            lightLevel : 100,
            brightness : 255, // cannot be changed for microbit
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
                        var thisLED = Math.min(this.leds[j][i], this.brightness);
                        var colorIndex = UTIL.round(thisLED * 0.035294118, 0);
                        if (colorIndex > 0) {
                            canvas.save();
                            canvas.beginPath();
                            var rad = canvas.createRadialGradient((this.x + i * this.dx) * 2, this.y - j * this.dy, 1.5 * this.r, (this.x + i * this.dx) * 2, this.y
                                    - j * this.dy, 3 * this.r);
                            rad.addColorStop(0, 'rgba(' + this.color[colorIndex] + ',1)');
                            rad.addColorStop(1, 'rgba(' + this.color[colorIndex] + ',0)');
                            canvas.fillStyle = rad;
                            canvas.scale(0.5, 1);
                            canvas.beginPath();
                            canvas.arc((this.x + i * this.dx) * 2, this.y - j * this.dy, 3 * this.r, 0, Math.PI * 2);
                            canvas.fill();
                            canvas.restore();
                            canvas.beginPath();
                        }
                    }
                }
            },
            finished : false
        };

        this.buttons = {
            A : false,
            B : false,
            Reset : false
        };
        this.temperature = {
            degree : 25
        };
        this.gesture = {
            up : true
        };
        this.compass = {
            degree : 0
        };
        this.time = 0;
        this.timer = {
            timer1 : false
        };
        this.tone = {};       
    }

    Mbed.prototype = Object.create(Robot.prototype);
    Mbed.prototype.constructor = Mbed;

    Mbed.prototype.reset = function() {
        for ( var property in this.buttons) {
            property = false;
        }
        this.display.leds = [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ];
        this.display.brightness = 255;
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
       
        $('#slider').on("input change", function(e) {
            e.preventDefault();
            $('#range').val($('#slider').val());
            that.compass.degree = $('#slider').val();
            e.stopPropagation();
        });

        $('#range').on("change", function(e) {
            e.preventDefault();
            var sval = $('#range').val();
            if(isNaN(sval)) $('#range').val(180);
            if(parseInt(sval) < 0) $('#range').val(0);
            if(parseInt(sval) > 360) $('#range').val(360);
            $('#slider').val($('#range').val());
            e.stopPropagation();
        });


        for (var i = 0; i < 4; i++) {
            if (this['pin' + i]) {
                delete this['pin' + i].analogIn;
                delete this['pin' + i].analogOut;
                delete this['pin' + i].digitalIn;
                delete this['pin' + i].digitalOut;
            }
        }

        this.pin = {};
        this.pin.no = 0;

        $("select#pin").on('change', function() {
            that.pin.no = $("select#pin option:selected").attr('id');
            if (that['pin' + that.pin.no].analogIn !== undefined) {
                $('#state').val('analog');
                $('#slider1').attr("max", 1023);
                $('#slider1').val(that['pin' + that.pin.no].analogIn);
            } else if (that['pin' + that.pin.no].digitalIn !== undefined) {
                $('#state').val('digital');
                $('#slider1').attr("max", 1);
                $('#slider1').val(that['pin' + that.pin.no].digitalIn);
            } else {
                $('#state').val('off');
                $('#slider1').attr("max", 1023);
                $('#slider1').val(0);
            }
        });

        $('#slider1').val(0);
        $("select#state").on('change', function() {
            if ($("select#state option:selected").attr('value') == 'off') {
                delete that['pin' + that.pin.no].analogIn;
                delete that['pin' + that.pin.no].digitalIn;
            } else if ($("select#state option:selected").attr('value') == 'analog') {
                delete that['pin' + that.pin.no].digitalIn;
                $('#slider1').attr("max", 1023);
                if (!that['pin' + that.pin.no].analogIn) {
                    $('#slider1').val(0);
                    that['pin' + that.pin.no].analogIn = $('#slider1').val();
                } else {
                    $('#slider1').val(that['pin' + that.pin.no].analogIn);
                }
            } else if ($("select#state option:selected").attr('value') == 'digital') {
                delete that['pin' + that.pin.no].analogIn;
                $('#slider1').attr("max", 1);
                if (!that['pin' + that.pin.no].digitalIn) {
                    $('#slider1').val(0);
                    that['pin' + that.pin.no].digitalIn = $('#slider1').val();
                } else {
                    $('#slider1').val(that['pin' + that.pin.no].digitalIn);
                }
            }
            that.pin.state = $("select#state option:selected").val();
        });

        $('#slider1').on("mousedown touchstart", function(e) {
            e.stopPropagation();
        });
        $('#slider1').change(function(e) {
            e.preventDefault();
            $('#range1').html($('#slider1').val());
            that['pin' + that.pin.no][that.pin.state + 'In'] = $('#slider1').val();
            e.stopPropagation();
        });
    };

    /**
     * Execution of the actions given by the running program.
     * 
     */
    Mbed.prototype.update = function() {
        var display = this.robotBehaviour.getActionState("display", true);
        if (display) {
            if (display.text) {
                var that = this;
                var textArray = generateText(display.text);

                function f(textArray, that) {
                    if (textArray && textArray.length >= 5) {
                        var array = textArray.slice(0, 5);
                        var newArray = array[0].map(function(col, i) {
                            return array.map(function(row) {
                                return row[i];
                            });
                        });
                        that.display.leds = newArray;
                        textArray.shift();
                        that.display.timeout = setTimeout(f, 150, textArray, that);
                    } else {
                        that.display.finished = true;
                    }
                }
                f(textArray, that);
            }
            if (display.character) {
                var that = this;
                var textArray = generatecharacter(display.character);

                function f(textArray, that) {
                    if (textArray && textArray.length >= 5) {
                        var array = textArray.slice(0, 5);
                        var newArray = array[0].map(function(col, i) {
                            return array.map(function(row) {
                                return row[i];
                            });
                        });
                        that.display.leds = newArray;
                        textArray = textArray.slice(5);
                        that.display.timeout = setTimeout(f, 400, textArray, that);
                    } else {
                        that.display.finished = true;
                    }
                }
                f(textArray, that);
            }
            if (display.picture) {
                if (display.mode == C.ANIMATION) {
                    var animation = display.picture;
                    var that = this;

                    function f(animation, index, that) {
                        if (animation && animation.length > index) {
                            that.display.leds = animation[index];
                            that.display.timeout = setTimeout(f, 150, animation, index + 1, that);
                        } else {
                            that.display.finished = true;
                        }
                    }
                    f(animation, 0, that);
                } else {
                    this.display.leds = display.picture;
                }
            }
            if (display.clear) {
                this.display.leds = [ [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ], [ 0, 0, 0, 0, 0 ] ];
            }
            if (display.pixel) {
                var pixel = display.pixel;

                if (0 <= pixel.y == pixel.y < this.display.leds.length && 0 <= pixel.x == pixel.x < this.display.leds[0].length) {
                    this.display.leds[pixel.y][pixel.x] = pixel.brightness * C.BRIGHTNESS_MULTIPLIER;
                } else {
                    if (0 <= pixel.y != pixel.y < this.display.leds.length) {
                        console.warn('actions.display.pixel.y out of range: ' + pixel.y);
                    }
                    if (0 <= pixel.x != pixel.x < this.display.leds[0].length) {
                        console.warn('actions.display.pixel.x out of range: ' + pixel.x);
                    }
                }
            }
            if (display.brightness || display.brightness === 0) {
                this.display.brightness = Math.round(display.brightness * 255.0 / 9.0, 0);
            }
        }
        for (var i = 0; i < 4; i++) {
            var pin = this.robotBehaviour.getActionState('pin' + i, true);
            if (pin !== undefined) {
                if (pin.digital !== undefined) {
                    this['pin' + i].digitalOut = pin.digital;
                }
                if (pin.analog !== undefined) {
                    this['pin' + i].analogOut = pin.analog;
                }
            }
        }

        // update timer
        var timer = this.robotBehaviour.getActionState("timer", false);
        if (timer) {
            for ( var key in timer) {
                if (timer[key] == 'reset') {
                    this.timer[key] = 0;
                }
            }
        }
        // update tone
        var volume = this.robotBehaviour.getActionState("volume", true);
        if ((volume || volume === 0) && this.webAudio.context) {
            this.webAudio.volume = volume / 100.0;
        }
        var tone = this.robotBehaviour.getActionState("tone", true);
        if (tone && this.webAudio.context) {
            var cT = this.webAudio.context.currentTime;
            if (tone.frequency && tone.duration > 0) {
                var oscillator = this.webAudio.context.createOscillator();
                oscillator.type = 'square';
                oscillator.connect(this.webAudio.context.destination);
                var that = this;
                function oscillatorFinish() {
                    that.tone.finished = true;
                    oscillator.disconnect(that.webAudio.context.destination);
                    delete oscillator;
                }
                oscillator.onended = function(e) {
                    oscillatorFinish();                    
                }
                oscillator.frequency.value = tone.frequency;
                oscillator.start(cT);
                oscillator.stop(cT + tone.duration / 1000.0);               
            }
        }
    };
    var generateText = function(text) {
        var string = [];
        string.push([ 0, 0, 0, 0, 0 ]);
        string.push([ 0, 0, 0, 0, 0 ]);
        for (var i = 0; i < text.length; i++) {
            var letter = letters[text[i]];
            if (!letter)
                letter = letters.blank;

            var newLetter = Array.apply(null, Array(letter[0] * 5)).map(Number.prototype.valueOf, 0);
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
    };
    var generatecharacter = function(character) {
        var string = [];
        for (var i = 0; i < character.length; i++) {
            var letter = letters[character[i]];
            if (!letter)
                letter = letters.blank;
            var newLetter = Array.apply(null, Array(25)).map(Number.prototype.valueOf, 0);
            var shift = Math.floor((5 - letter[0]) / 2);
            for (var j = 1; j < letter.length; j++) {
                newLetter[letter[j] - 1 + shift * 5] = 255;
            }
            while (newLetter.length) {
                string.push(newLetter.splice(0, 5));
            }
        }
        if (character.length > 1) {
            var newLetter = Array.apply(null, Array(25)).map(Number.prototype.valueOf, 0);
            while (newLetter.length) {
                string.push(newLetter.splice(0, 5));
            }
        }
        return string;
    };
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
        w : [ 5, 2, 3, 4, 5, 10, 14, 20, 22, 23, 24, 25 ],
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
    };
    Mbed.prototype.handleMouseUp = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    };
    Mbed.prototype.handleMouseOut = function(e, offsetX, offsetY) {
        return;
    };
    Mbed.prototype.handleMouseMove = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    };
    Mbed.prototype.handleMouseDown = function(e, offsetX, offsetY, scale, w, h) {
        this.handleMouse(e, offsetX, offsetY, scale, w, h);
    };
    Mbed.prototype.controle = function() {
        $('#simRobotContent').append('<div id="mbedContent"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons">' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_GESTURE + '</label>' + //
        '<label class="btn simbtn active"><input type="radio" id="up" name="options" autocomplete="off">' + Blockly.Msg.SENSOR_GESTURE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="down" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_up" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_down"name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="shake" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_SHAKE + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="freefall" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FREEFALL + '</label>' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_COMPASS
                + '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right;" id="range" />'
                + '<div style="margin:8px 0; "><input id="slider" type="range" min="0" max="360" value="0" step="5" /></div>' + //
                '<label style="width:100%;margin: 8px;margin-top: 12px; margin-left: 0"><select class="customDropdown" id="pin"><option id="0">'
                + Blockly.Msg.SENSOR_PIN + ' 0</option><option id="1">' + Blockly.Msg.SENSOR_PIN + ' 1</option><option id="2">' + Blockly.Msg.SENSOR_PIN
                + ' 2</option></select><select class="customDropdown" style="float: right;" id="state"><option value="off">' + Blockly.Msg.OFF
                + '</option><option value="analog">analog</option><option value="digital">digital</option></select></label>' + //
                '<div style="margin:8px 0; "><input id="slider1" type="range" min="0" max="1023" value="0" step="1" /></div></div>'); //
    };
    Mbed.prototype.resetPose = function() {
    };

    return Mbed;
});