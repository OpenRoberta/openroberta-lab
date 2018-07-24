define([ 'simulation.simulation', 'robertaLogic.constants', 'simulation.robot' ], function(SIM, CONSTANTS, Robot) {

    /**
     * Creates a new Ev3 for a simulation.
     * 
     * This Ev3 is a differential drive Ev3. It has two wheels directly
     * connected to motors and several sensors. Each component of the Ev3 has a
     * position in the Ev3s coordinate system. The Ev3 itself has a pose in the
     * global coordinate system (x, y, theta).
     * 
     * @class
     */
    function Ev3(pose) {
        Robot.call(this, pose);
        this.ultraSensor = {
                x : 0,
                y : -20,
                rx : 0,
                ry : 0,
                distance : 0,
                u : [],
                cx : 0,
                cy : 0,
                color : '#FF69B4'
            };
        
         this.geom = {
                x : -20,
                y : -20,
                w : 40,
                h : 50,
                color : '#FCCC00'
            };
          this.wheelLeft = {
                x : 16,
                y : -8,
                w : 8,
                h : 16,
                color : '#000000'
            };
            this.wheelRight = {
                x : -24,
                y : -8,
                w : 8,
                h : 16,
                color : '#000000'
            };
            this.wheelBack = {
                x : -2.5,
                y : 30,
                w : 5,
                h : 5,
                color : '#000000'
            };
            this.led = {
                x : 0,
                y : 10,
                color : 'LIGHTGREY',
                blinkColor : 'LIGHTGREY',
                mode : '',
                timer : 0
            };
            this.encoder = {
                left : 0,
                right : 0
            };
            this.colorSensor = {
                x : 0,
                y : -15,
                rx : 0,
                ry : 0,
                r : 5,
                colorValue : 0,
                lightValue : 0,
                color : 'grey'
            };
            
            this.touchSensor = {
                    x : 0,
                    y : -25,
                    x1 : 0,
                    y1 : 0,
                    x2 : 0,
                    y2 : 0,
                    value : 0,
                    color : '#FFCC33'
                };
                this.gyroSensor = {
                    value : 0,
                    color : '#000'
                };
                this.display = {
                    OLDGLASSES : '<image  width="178" height="128" alt="old glasses" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAMxSURBVHja7Z3blqowEESJy///Zc7TjA6XpDvpSzWn9tOohGyKNiBoZtsIIYQQQgghhBBCCCGEEEIIIYQQQggBpZmtac/eFFPscjFY2d9oTdXSMN+m+VVcV279mB22a67xs4YGKdNB6xv+nwH/MBW0rpFtwJFDi6W52lvTwL6Go2K2Nld6Sxf3GyS8g5YcyGa2TuEtW9R7HPYL+sq8iZc0spYsuItar+0Ij5h1J2OO1TxebFe0XAnaOmZ5Da/Yi6xfE6rb1hwqz3ZI0kfsaK3ft/bVYB/EXMSz7sM19yo5qhqsifYe7pyXqql/xOuDxqz3Ss+Dti9Fs5gqXos5y7trPTrw6VRtDl7za5mvYtdPAm+hbIWxeAa7cPf7jF6Xix8BqIZJfM+FhGuTDBeSk58gXWWryIg7nEPWdu1Twfp16r3DrMeVDFINE/TMQ72PIes691T1NPHzvlzzqJJhqkHNvXm49+jiNZDq0pZFep96fusWD1TVnJuDn9fLP/H1N8oDjLMWA3r3NvLecLbbFW9+6H2uktGBGixGY/IVoXUs6UycaNo7sFgle9z38kdbycHV0H47bF/PfYtUCP1TydCHsyZ4ZkDk9h36encXbf3GMbTzn/vPgxuhHavA4cfkdvVnu3i1T+r79GM5+hJL9nCif1dlmrfrB9khjpX37jNY/AlZf56cw37xGGrc7VGjkutRspKP4qVKokrI7eJxmaDhT+G2bbsbfcuMyRVCbhOvQFEh5B4lYv6EjKqL6qVwrl7JJWDIAaCHXHGwOIEe8pgCu+E75AK6JTjliF3JD9nt2CE/BIgbTEK3e8CtWckBnHNHqYrM6U6MrVnJAVj/1t/Tqw+wNSs5gNyvmOqsRsBaI34H2XpCwHRrDhcB4P3wZu2jNKQ12k/I1q9WAFpj/d7Ub3KcVOvRApHCNedyFlj7TX/kIKsAyjpuPhYDWRVA1k+eRhLGOn9C1OiZOhOsc6f2rTEJ2rJ17lXbCpOgGVjnXVKMvUmaap1zvSvnLnSadfwVr9zb/CnWtv/MxbcvO4Kt/T/K4kT7HGtCCCGEEEIIIYQQQgghhBBCCCGEEEIIId/8A/WUnnVgvvqBAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjE3OjMyKzAxOjAw2vjCoQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoxNzozMiswMTowMKuleh0AAAAASUVORK5CYII=" />',
                    EYESOPEN : '<image width="178" height="128" alt="eyes open" xlink:href="data:image/png;base64,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" />',
                    EYESCLOSED : '<image width="178" height="128" alt="eyes closed" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAATbSURBVHja7ZxJksMgDEUhlftfmV7EiW3MIEDDh+ZvutoDoBcZgwR2bmtra2tra2tra2srlrdugJKCpbX/A3Kwtfdlbb+CwngRY1ofsjni9buLJ2IDi9f2ZAAvdm5tyCCIV+4uUoj3EI5VEH0xQNWCihEbW7ki5DtiAAsBmsCsK2IQ60CawSZAxEANYREk4nXHyUCIwRozKONYW14ozRkHBIsYo0E8o9owdLeo3qa180UXgBFbQuYM30AjtoIsEx8DRWzRsBrg9haZJkkp0vXkEuAxSMCIdRuXRgyNh0d6JoKFHzWlY+o/BqxlLlx8V1vyJv97xPJGgwYfdSVrOHDQRlOSxoNPdvUkBwB+HqYn2RmfHWKgpS3reRol9KRu80qQW2J7qnavApkypzSbdyJAHu8/W0bjBiN3S8g8/WcPNOo9TLNVG8itmZF8K/unO+k7RSLeCJkRSv9Zu6rHDr6fWurGTrU+3KXrOWaUKiMSq8xIS73p+7gm7XTME0DmerT97QhH+6mYu+vSWnA4Go+LPdhi0013nbrpJ+5Hm6v1wr6s4cl8IU9f+I+vXHbJQ+aNKiPMUJslDZk/cO+jv7ylikjnxcdpAsw+U7pkg/ZyQPg9z8st5JWEvF6OLzSEk75HvMaCw3UQ55SPswRpBDJ+LJuerXcYvnBlLoTlpT0Z0495JzXlQKxKd4GlkDnanomhpRO8c3KQRx5r/nVH9W6gFXNTnWieHB7/aUWLJexQGML1NSx1tD+tSlWthjhFVb4+eo75IXN5T+k9zlu37zhTb8XlXj7Iox+kyWWGvQvNk4Ca/MDZs06KNzvnOCDbxhL4N6zV5X+JA2LpvZC50dbKC9mhfgxAQ/6oO+3PjyN9kO281x/GSYTv24adJ+jqHUiji7vBlBef9XzSF/z5IlTI1n09VSR/nvXzOPw/QX+J/rg/W0IfZImQOfW8bBfRu2ChCHpWT+b25fHSCu8QHMiefE7Ol0dDnhl/loQs/eriz8mNK+nP49+uolQ5Uq4vXse5Fo7vCYlK7PVkqUfW30r22Xr0X709ZQ3n+Kw3c3Gvr+Nv669kWcj4mKWXLQTnkEYXI4b0vbiCOOKjZPnkjlb6nmdjjohwYxetaklTKe/l00hTSpiRr3uazZK928B6DW8VfaVP6S7Tbb/0lE6oXCH1YuGLNKgoD7m0TzMdDCn9YEgrlEE+xRAq51q2eXFjpk2CpvioSB5N/2ITvT131mmpSLnJyBm2G1fPMpW0pkRcnvHxb6bpnZuN3musEuSQOdYDvz4iKWtawM6VIHOvab9ibkM2sQ/Hpt/NSp8dXco69imGEYtM9YxdyEamroOrUk3heQCQHtnolGnto+c29XwzxTJSMqjn8Ko8ctaeubW+MAERf7oLSmRVYmtX63ataeUbpsbonzezbmNW3xdf/YNKkM2fQ6+Ei4TEf3MgBm3luxKvnAkxrG7jZH/743AQTz7ju0yrYRFTBdvSH+TpEQPr6C4ixFifA5m8szggJxFjAKYLuL2vRRBD67URy+vz4kNGPH2P/IGMjJgq6Fa/o6w0dGNn1ae72IhFdeYcMAFPHKo/9YJGvIjuo4stEWHjnTofcmqFjTngiNeADK8NWUHIj9oSwzfntieraENWEC7kBaJvX+FCXkgbsoL+APENFhEdy5OyAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjI0OjIzKzAxOjAwwvAUiQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoyNDoyMyswMTowMLOtrDUAAAAASUVORK5CYII=" />',
                    FLOWERS : '<image width="178" height="128" alt="flowers" xlink:href="data:image/png;base64,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" />',
                    TACHO : '<image width="178" height="128" alt="tacho" xlink:href="data:image/png;base64,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" />'
                }
                
                this.webAudio = {
                        context : context,
                        oscillator : oscillator,
                        gainNode : gainNode,
                        volume : 0.5,
                    }
                this.buttons = {
                        escape : false,
                        up : false,
                        left : false,
                        enter : false,
                        right : false,
                        down : false
                    };
                 this.frontLeft = {
                        x : 22.5,
                        y : -25,
                        rx : 0,
                        ry : 0,
                        bumped : false
                    };
                    this.frontRight = {
                        x : -22.5,
                        y : -25,
                        rx : 0,
                        ry : 0,
                        bumped : false
                    };
                    this.backLeft = {
                        x : 20,
                        y : 30,
                        rx : 0,
                        ry : 0,
                        bumped : false
                    };
                    this.backRight = {
                        x : -20,
                        y : 30,
                        rx : 0,
                        ry : 0,
                        bumped : false
                    };
                    this.backMiddle = {
                        x : 0,
                        y : 30,
                        rx : 0,
                        ry : 0
                    };
                    this.svg = '<svg xmlns="http://www.w3.org/2000/svg" width="313" height="482" viewBox="0 0 313 482">'
                        + '<path stroke-alignment="inner" d="M1 88h17.5v-87h276v87h17.5v306h-17.5v87h-276v-87h-17.5z" style="fill:#fff;stroke:#000;stroke-width:2"/>'
                        + '<rect x="19.5" y="2" width="274" height="225" style="fill:#A3A2A4;stroke:none"/>'
                        + '<rect x="19.5" y="202" width="274" height="25" style="fill:#635F61;stroke:none"/>'
                        + '<path d="M45 47.4c0-5.3 5.7-7.7 5.7-7.7s206.7 0 211 0c4.3 0 6.7 7.7 6.7 7.7v118.3c0 5.3-5.7 7.7-5.7 7.7s-206.7 0-211 0S44 164.7 44 164.7" fill="#333"/>'
                        + '<rect x="67" y="41" width="180" height="130" fill="#ddd"/>'
                        + '<line x1="155.7" y1="246" x2="155.7" y2="172.4" style="fill:none;stroke-width:9;stroke:#000"/>'
                        + '<path id="led" fill="url("#LIGHTGRAY") d="M155.5 242.5 l20 0 40 40 0 52 -40 40 -40 0 -40 -40 0 -52 40 -40z" fill="#977" />'
                        + '<path id="up" class="simKey" d="M156 286c0 0 7 0 14.3-0.2s9 7.2 9 7.2v12.3h10.5v-19.5l9.7-9.7c0 0 2.8-0.2 0-3.3-2.8-3.2-26.5-25.7-26.5-25.7h-17-0.3-17c0 0-23.7 22.5-26.5 25.7s0 3.3 0 3.3l9.7 9.7v19.5h10.5v-12.3c0 0 1.7-7.3 9-7.2s14.3 0.2 14.3 0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                        + '<path id="down" class="simKey" d="M156 331c0 0 7 0 14.3 0.2s9-7.2 9-7.2v-12.3h10.5v19.5l9.7 9.7c0 0 2.8 0.2 0 3.3-2.8 3.2-26.5 25.7-26.5 25.7h-17-0.3-17c0 0-23.7-22.5-26.5-25.7s0-3.3 0-3.3l9.7-9.7v-19.5h10.5v12.3c0 0 1.7 7.3 9 7.2s14.3-0.2 14.3-0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                        + '<path id="enter" class="simKey" d="M138 293c0-1.4 0.9-2 0.9-2s32.6 0 33.2 0 1.1 2 1.1 2v31.4c0 1.4-0.9 2-0.9 2s-32.5 0-33.2 0c-0.7 0-1-2-1-2V293.1z" style="fill:#3C3C3B;stroke-width:2;stroke:#000"/>'
                        + '<path id="escape" class="simKey" d="M37 227v26.4c0 0 1.2 4.8 4.9 4.8s44.8 0 44.8 0l15.7-15.6v-15.7z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                        + '<path id="left" class="simKey" d="M69 309c0 12.5 14 17.9 14 17.9s27.1 0 29.8 0 2.8-1.7 2.8-1.7v-16.4 0.1-16.4c0 0-0.2-1.7-2.8-1.7s-29.7 0-29.7 0S69.3 296.7 69.3 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                        + '<path id="right" class="simKey" d="M242 309c0 12.5-14 17.9-14 17.9s-27.1 0-29.7 0-2.8-1.7-2.8-1.7v-16.4 0.1-16.4c0 0 0.2-1.7 2.8-1.7s29.8 0 29.8 0S241.9 296.7 241.9 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                        + '<rect x="19" y="412.4" width="274" height="67.7" style="fill:#A3A2A4"/>'
                        + '<rect x="2" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                        + '<rect x="294" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                        + '<rect x="231.7" y="426.6" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                        + '<rect x="246.2" y="426.7" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                        + '<rect x="227.5" y="432.4" width="32.6" height="26.2" style="fill:#E52520;stroke:#000"/>'
                        + '<g id="display" clip-path="url(#clipPath)" fill="#000" transform="translate(67, 41)" font-family="Courier New" font-size="10pt">' + '</g>'
                        + '<defs>' + '<clipPath id="clipPath">' + '<rect x="0" y="0" width="178" height="128"/>' + '</clipPath>'
                        + '<radialGradient id="ORANGE" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'
                        + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                        + '<stop offset="100%" style="stop-color:rgb( 255, 165, 0);stop-opacity:1" />' + '</radialGradient>'
                        + '<radialGradient id="RED" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'
                        + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                        + '<stop offset="100%" style="stop-color:rgb(255,0,0);stop-opacity:1" />' + '</radialGradient>'
                        + '<radialGradient id="GREEN" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'
                        + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                        + '<stop offset="100%" style="stop-color:rgb(0,128,0);stop-opacity:1" />' + '</radialGradient>'
                        + '<radialGradient id="LIGHTGRAY" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'
                        + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                        + '<stop offset="100%" style="stop-color:rgb(211,211,211);stop-opacity:1" />' + '</radialGradient>' + '</defs>' + '</svg>';
                this.timer = {
                    timer1 : false,
                    timer2 : false,
                    timer3 : false,
                    timer4 : false,
                    timer5 : false
                };
                
    }

    Ev3.prototype = Object.create(Robot.prototype);
    Ev3.prototype.constructor = Ev3;

    Ev3.prototype.reset = function() {
        var Ev3 = this;
        Ev3.encoder.left = 0;
        Ev3.encoder.right = 0;
        Ev3.led.color = "LIGHTGRAY";
        Ev3.led.mode = "OFF";
        Ev3.led.blink = 0;
        // Ev3.time = 0;
        for (key in this.timer) {
            Ev3.timer[key] = 0;
        }
        var robot = this;
        $("#simRobotContent").html(this.svg);
        for ( var property in robot.buttons) {
            $('#' + property).off('mousedown touchstart');
            $('#' + property).on('mousedown touchstart', function() {
                robot.buttons[this.id] = true;
            });
            $('#' + property).off('mouseup touchend');
            $('#' + property).on('mouseup touchend', function() {
                robot.buttons[this.id] = false;
            });
        }
        $("#display").html('');
        Ev3.tone.duration = 0;
        Ev3.tone.frequency = 0;
        Ev3.webAudio.volume = 0.5;
    };

    var AudioContext = window.AudioContext // Default
            || window.webkitAudioContext // Safari and old versions of Chrome
            || false;

    if (AudioContext) {
        var context = new AudioContext();

        var oscillator = context.createOscillator();
        oscillator.type = 'square';
        var gainNode = context.createGain();
        oscillator.start(0);

        oscillator.connect(gainNode);
        gainNode.connect(context.destination);
        gainNode.gain.setValueAtTime(0, 0);

    } else {
        var context = null;
        console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
    }

    
    var SpeechSynthesis = window.speechSynthesis;
    
    if (SpeechSynthesis) {
        
    } else {
        var context = null;
        console.log("Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
    }
    
    //sayText also to be in included in constructor
    Ev3.prototype.sayText = {
        language : "en-US",
        say : function(text, lang, speed, pitch) {
            // IE apparently doesnt support default parameters, this prevents it from crashing the whole simulation
            speed = (speed === undefined) ? 30 : speed;
            pitch = (pitch === undefined) ? 50 : pitch;
            // Clamp values
            speed = Math.max(0, Math.min(100, speed));
            pitch = Math.max(0, Math.min(100, pitch));
            // Convert to SpeechSynthesis values
            speed = speed * 0.015 + 0.5; // use range 0.5 - 2; range should be 0.1 - 10, but some voices dont accept values beyond 2
            pitch = pitch * 0.02 + 0.001; // use range 0.0 - 2.0; + 0.001 as some voices dont accept 0
            
            var utterThis = new SpeechSynthesisUtterance(text);
            if (lang === "") {
                console.log("Language is not supported!")
            } else {
                var voices = SpeechSynthesis.getVoices();
                for (i = 0; i < voices.length ; i++) {
                    if (voices[i].lang.indexOf(lang) !== -1 || voices[i].lang.indexOf(lang.substr(0,2)) !== -1) {
                        utterThis.voice = voices[i];
                        break;
                    }
                }
                if (utterThis.voice === null) {
                    console.log("Language \"" + lang + "\" could not be found. Try a different browser or for chromium add the command line flag \"--enable-speech-dispatcher\".");
                }
            }
            utterThis.pitch = pitch;
            utterThis.rate = speed;
            SpeechSynthesis.speak(utterThis);
        }
    }
   //tone also to be included in constructor
    Ev3.prototype.tone = {
        duration : 0,
        timer : 0,
        file : {
            0 : function(a) {
                var ts = a.context.currentTime;
                a.oscillator.frequency.setValueAtTime(600, ts);
                a.gainNode.gain.setValueAtTime(a.volume, ts);
                ts += 1;
                a.gainNode.gain.setValueAtTime(0, ts);
            },
            1 : function(a) {
                var ts = a.context.currentTime;
                for (var i = 0; i < 2; i++) {
                    a.oscillator.frequency.setValueAtTime(600, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (150 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            2 : function(a) {
                const
                C2 = 523;
                var ts = a.context.currentTime;
                for (var i = 4; i < 8; i++) {
                    a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (100 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            3 : function(a) {
                const
                C2 = 523;
                var ts = a.context.currentTime;
                for (var i = 7; i >= 4; i--) {
                    a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (100 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            4 : function(a) {
                var ts = a.context.currentTime;
                a.oscillator.frequency.setValueAtTime(100, ts);
                a.gainNode.gain.setValueAtTime(a.volume, ts);
                ts += (500 / 1000.0);
                a.gainNode.gain.setValueAtTime(0, ts);
            }
        }
    }
    
    /**
     * Update all actions of the Ev3. The new pose is calculated with the
     * forward kinematics equations for a differential drive Ev3.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Ev3.prototype.update = function(actions) {
        // update debug
        this.debug = actions.debug || this.debug;
        // update pose
        if (actions.motors) {
            var left = actions.motors.powerLeft || 0;
            if (left > 100) {
                left = 100;
            } else if (left < -100) {
                left = -100
            }
            var right = actions.motors.powerRight || 0;
            if (right > 100) {
                right = 100;
            } else if (right < -100) {
                right = -100
            }
            this.left = left * CONSTANTS.MAXPOWER;
            this.right = right * CONSTANTS.MAXPOWER;
        } else {
            this.left = 0;
            this.right = 0;
        }
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += this.left * SIM.getDt();
        this.encoder.right += this.right * SIM.getDt();
        if (actions.encoder) {
            if (actions.encoder.leftReset) {
                this.encoder.left = 0;
            }
            if (actions.encoder.rightReset) {
                this.encoder.right = 0;
            }
        }
        if (this.frontLeft.bumped && this.left > 0) {
            this.left *= -1;
        }
        if (this.backLeft.bumped && this.left < 0) {
            this.left *= -1;
        }
        if (this.frontRight.bumped && this.right > 0) {
            this.right *= -1;
        }
        if (this.backRight.bumped && this.right < 0) {
            this.right *= -1;
        }
        if (this.right == this.left) {
            var moveXY = this.right * SIM.getDt();
            var mX = Math.cos(this.pose.theta) * moveXY;
            var mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
            this.pose.x += mX;
            if (moveXY >= 0) {
                if (this.pose.theta < Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            } else {
                if (this.pose.theta > Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            }
            this.pose.thetaDiff = 0;
        } else {
            var R = CONSTANTS.TRACKWIDTH / 2 * ((this.left + this.right) / (this.left - this.right));
            var rot = (this.left - this.right) / CONSTANTS.TRACKWIDTH;
            var iccX = this.pose.x - (R * Math.sin(this.pose.theta));
            var iccY = this.pose.y + (R * Math.cos(this.pose.theta));
            this.pose.x = (Math.cos(rot * SIM.getDt()) * (this.pose.x - iccX) - Math.sin(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccX;
            this.pose.y = (Math.sin(rot * SIM.getDt()) * (this.pose.x - iccX) + Math.cos(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccY;
            this.pose.thetaDiff = rot * SIM.getDt();
            this.pose.theta = this.pose.theta + this.pose.thetaDiff;
        }
        var sin = Math.sin(this.pose.theta);
        var cos = Math.cos(this.pose.theta);
        this.frontRight = this.translate(sin, cos, this.frontRight);
        this.frontLeft = this.translate(sin, cos, this.frontLeft);
        this.backRight = this.translate(sin, cos, this.backRight);
        this.backLeft = this.translate(sin, cos, this.backLeft);
        this.backMiddle = this.translate(sin, cos, this.backMiddle);

        this.touchSensor = this.translate(sin, cos, this.touchSensor);
        this.colorSensor = this.translate(sin, cos, this.colorSensor);
        this.ultraSensor = this.translate(sin, cos, this.ultraSensor);
        this.mouse = this.translate(sin, cos, this.mouse);

        this.touchSensor.x1 = this.frontRight.rx;
        this.touchSensor.y1 = this.frontRight.ry;
        this.touchSensor.x2 = this.frontLeft.rx;
        this.touchSensor.y2 = this.frontLeft.ry;

        //update led(s)
        if (actions.led) {
            if (actions.led.color) {
                this.led.color = actions.led.color.toUpperCase();
                this.led.blinkColor = actions.led.color.toUpperCase();
            }
            switch (actions.led.mode) {
            case "OFF":
                this.led.timer = 0;
                this.led.blink = 0;
                this.led.color = 'LIGHTGRAY';
                break;
            case "ON":
                this.led.timer = 0;
                this.led.blink = 0;
                break;
            case "FLASH":
                this.led.blink = 2;
                break;
            case "DOUBLE_FLASH":
                this.led.blink = 4;
                break;
            }
        }
        if (this.led.blink > 0) {
            if (this.led.timer > 0.5 && this.led.blink == 2) {
                this.led.color = this.led.blinkColor;
            } else if (this.led.blink == 4 && (this.led.timer > 0.5 && this.led.timer < 0.67 || this.led.timer > 0.83)) {
                this.led.color = this.led.blinkColor;
            } else {
                this.led.color = 'LIGHTGRAY';
            }
            this.led.timer += SIM.getDt();
            if (this.led.timer > 1.0) {
                this.led.timer = 0;
            }
        }
        $("#led").attr("fill", "url('#" + this.led.color + "')");
        // update display
        if (actions.display) {
            if (actions.display.text) {
                $("#display").html($("#display").html() + '<text x=' + actions.display.x * 10 + ' y=' + (actions.display.y + 1) * 16 + '>'
                        + actions.display.text + '</text>');
            }
            if (actions.display.picture) {
                $("#display").html(this.display[actions.display.picture]);
            }
            if (actions.display.clear) {
                $("#display").html('');
            }
        }
        // update tone
        if (actions.volume && AudioContext) {
            this.webAudio.volume = actions.volume / 100.0;
        }
        if (actions.tone && AudioContext) {
            var ts = this.webAudio.context.currentTime;
            if (actions.tone.frequency) {
                this.webAudio.oscillator.frequency.setValueAtTime(actions.tone.frequency, ts);
                this.webAudio.gainNode.gain.setValueAtTime(this.webAudio.volume, ts);
            }
            if (actions.tone.duration) {
                ts += actions.tone.duration / 1000.0;
                this.webAudio.gainNode.gain.setValueAtTime(0, ts);
            }
            if (actions.tone.file != undefined) {
                this.tone.file[actions.tone.file](this.webAudio);
            }
        }
        // update sayText
        if (actions.language != null && SpeechSynthesis) {
            this.sayText.language = actions.language;
        }
        if (actions.sayText && SpeechSynthesis) {
            if (actions.sayText.text) {
                this.sayText.say(actions.sayText.text, this.sayText.language, actions.sayText.speed, actions.sayText.pitch);
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
    };
    /**
     * Translate a position to the global coordinate system
     * 
     * @param {Number}
     *            sin the sine from the orientation from the Ev3
     * @param {Number}
     *            cos the cosine from the orientation from the Ev3
     * @param {point}
     *            point to translate
     * @returns the translated point
     * 
     */
    Ev3.prototype.translate = function(sin, cos, point) {
        point.rx = this.pose.x - point.y * cos + point.x * sin;
        point.ry = this.pose.y - point.y * sin - point.x * cos;
        return point;
    };

    return Ev3;
});
