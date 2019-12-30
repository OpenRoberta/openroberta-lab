define([ 'simulation.simulation', 'interpreter.constants', 'simulation.robot', 'guiState.controller' ], function(SIM, C, Robot, GUISTATE_C) {

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
    function Ev3(pose, num, robotBehaviour) {
        Robot.call(this, pose, robotBehaviour);
        that = this;

        num = num || 0;
        this.id = num;
        this.left = 0;
        this.right = 0;
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
            SMILEY: '<image  width="178" height="128" alt="old glasses" xlink:href="data:image/png;base64,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" />',
            OLDGLASSES : '<image  width="178" height="128" alt="old glasses" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAMxSURBVHja7Z3blqowEESJy///Zc7TjA6XpDvpSzWn9tOohGyKNiBoZtsIIYQQQgghhBBCCCGEEEIIIYQQQggBpZmtac/eFFPscjFY2d9oTdXSMN+m+VVcV279mB22a67xs4YGKdNB6xv+nwH/MBW0rpFtwJFDi6W52lvTwL6Go2K2Nld6Sxf3GyS8g5YcyGa2TuEtW9R7HPYL+sq8iZc0spYsuItar+0Ij5h1J2OO1TxebFe0XAnaOmZ5Da/Yi6xfE6rb1hwqz3ZI0kfsaK3ft/bVYB/EXMSz7sM19yo5qhqsifYe7pyXqql/xOuDxqz3Ss+Dti9Fs5gqXos5y7trPTrw6VRtDl7za5mvYtdPAm+hbIWxeAa7cPf7jF6Xix8BqIZJfM+FhGuTDBeSk58gXWWryIg7nEPWdu1Twfp16r3DrMeVDFINE/TMQ72PIes691T1NPHzvlzzqJJhqkHNvXm49+jiNZDq0pZFep96fusWD1TVnJuDn9fLP/H1N8oDjLMWA3r3NvLecLbbFW9+6H2uktGBGixGY/IVoXUs6UycaNo7sFgle9z38kdbycHV0H47bF/PfYtUCP1TydCHsyZ4ZkDk9h36encXbf3GMbTzn/vPgxuhHavA4cfkdvVnu3i1T+r79GM5+hJL9nCif1dlmrfrB9khjpX37jNY/AlZf56cw37xGGrc7VGjkutRspKP4qVKokrI7eJxmaDhT+G2bbsbfcuMyRVCbhOvQFEh5B4lYv6EjKqL6qVwrl7JJWDIAaCHXHGwOIEe8pgCu+E75AK6JTjliF3JD9nt2CE/BIgbTEK3e8CtWckBnHNHqYrM6U6MrVnJAVj/1t/Tqw+wNSs5gNyvmOqsRsBaI34H2XpCwHRrDhcB4P3wZu2jNKQ12k/I1q9WAFpj/d7Ub3KcVOvRApHCNedyFlj7TX/kIKsAyjpuPhYDWRVA1k+eRhLGOn9C1OiZOhOsc6f2rTEJ2rJ17lXbCpOgGVjnXVKMvUmaap1zvSvnLnSadfwVr9zb/CnWtv/MxbcvO4Kt/T/K4kT7HGtCCCGEEEIIIYQQQgghhBBCCCGEEEIIId/8A/WUnnVgvvqBAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjE3OjMyKzAxOjAw2vjCoQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoxNzozMiswMTowMKuleh0AAAAASUVORK5CYII=" />',
            EYESOPEN : '<image width="178" height="128" alt="eyes open" xlink:href="data:image/png;base64,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" />',
            EYESCLOSED : '<image width="178" height="128" alt="eyes closed" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAATbSURBVHja7ZxJksMgDEUhlftfmV7EiW3MIEDDh+ZvutoDoBcZgwR2bmtra2tra2tra2srlrdugJKCpbX/A3Kwtfdlbb+CwngRY1ofsjni9buLJ2IDi9f2ZAAvdm5tyCCIV+4uUoj3EI5VEH0xQNWCihEbW7ki5DtiAAsBmsCsK2IQ60CawSZAxEANYREk4nXHyUCIwRozKONYW14ozRkHBIsYo0E8o9owdLeo3qa180UXgBFbQuYM30AjtoIsEx8DRWzRsBrg9haZJkkp0vXkEuAxSMCIdRuXRgyNh0d6JoKFHzWlY+o/BqxlLlx8V1vyJv97xPJGgwYfdSVrOHDQRlOSxoNPdvUkBwB+HqYn2RmfHWKgpS3reRol9KRu80qQW2J7qnavApkypzSbdyJAHu8/W0bjBiN3S8g8/WcPNOo9TLNVG8itmZF8K/unO+k7RSLeCJkRSv9Zu6rHDr6fWurGTrU+3KXrOWaUKiMSq8xIS73p+7gm7XTME0DmerT97QhH+6mYu+vSWnA4Go+LPdhi0013nbrpJ+5Hm6v1wr6s4cl8IU9f+I+vXHbJQ+aNKiPMUJslDZk/cO+jv7ylikjnxcdpAsw+U7pkg/ZyQPg9z8st5JWEvF6OLzSEk75HvMaCw3UQ55SPswRpBDJ+LJuerXcYvnBlLoTlpT0Z0495JzXlQKxKd4GlkDnanomhpRO8c3KQRx5r/nVH9W6gFXNTnWieHB7/aUWLJexQGML1NSx1tD+tSlWthjhFVb4+eo75IXN5T+k9zlu37zhTb8XlXj7Iox+kyWWGvQvNk4Ca/MDZs06KNzvnOCDbxhL4N6zV5X+JA2LpvZC50dbKC9mhfgxAQ/6oO+3PjyN9kO281x/GSYTv24adJ+jqHUiji7vBlBef9XzSF/z5IlTI1n09VSR/nvXzOPw/QX+J/rg/W0IfZImQOfW8bBfRu2ChCHpWT+b25fHSCu8QHMiefE7Ol0dDnhl/loQs/eriz8mNK+nP49+uolQ5Uq4vXse5Fo7vCYlK7PVkqUfW30r22Xr0X709ZQ3n+Kw3c3Gvr+Nv669kWcj4mKWXLQTnkEYXI4b0vbiCOOKjZPnkjlb6nmdjjohwYxetaklTKe/l00hTSpiRr3uazZK928B6DW8VfaVP6S7Tbb/0lE6oXCH1YuGLNKgoD7m0TzMdDCn9YEgrlEE+xRAq51q2eXFjpk2CpvioSB5N/2ITvT131mmpSLnJyBm2G1fPMpW0pkRcnvHxb6bpnZuN3musEuSQOdYDvz4iKWtawM6VIHOvab9ibkM2sQ/Hpt/NSp8dXco69imGEYtM9YxdyEamroOrUk3heQCQHtnolGnto+c29XwzxTJSMqjn8Ko8ctaeubW+MAERf7oLSmRVYmtX63ataeUbpsbonzezbmNW3xdf/YNKkM2fQ6+Ei4TEf3MgBm3luxKvnAkxrG7jZH/743AQTz7ju0yrYRFTBdvSH+TpEQPr6C4ixFifA5m8szggJxFjAKYLuL2vRRBD67URy+vz4kNGPH2P/IGMjJgq6Fa/o6w0dGNn1ae72IhFdeYcMAFPHKo/9YJGvIjuo4stEWHjnTofcmqFjTngiNeADK8NWUHIj9oSwzfntieraENWEC7kBaJvX+FCXkgbsoL+APENFhEdy5OyAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjI0OjIzKzAxOjAwwvAUiQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoyNDoyMyswMTowMLOtrDUAAAAASUVORK5CYII=" />',
            FLOWERS : '<image width="178" height="128" alt="flowers" xlink:href="data:image/png;base64,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" />',
            TACHO : '<image width="178" height="128" alt="tacho" xlink:href="data:image/png;base64,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" />'
        };
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
        this.time = 0;
        this.timer = {
            timer1 : false,
            timer2 : false,
            timer3 : false,
            timer4 : false,
            timer5 : false
        };
        var SpeechSynthesis = window.speechSynthesis;
        if (!SpeechSynthesis) {
            context = null;
            console.log("Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
        }
        this.sayText = {
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
                    console.log("Language is not supported!");
                } else {
                    var voices = SpeechSynthesis.getVoices();
                    for (var i = 0; i < voices.length; i++) {
                        if (voices[i].lang.indexOf(lang) !== -1 || voices[i].lang.indexOf(lang.substr(0, 2)) !== -1) {
                            utterThis.voice = voices[i];
                            break;
                        }
                    }
                    if (utterThis.voice === null) {
                        console.log("Language \"" + lang
                                + "\" could not be found. Try a different browser or for chromium add the command line flag \"--enable-speech-dispatcher\".");
                    }
                }
                utterThis.pitch = pitch;
                utterThis.rate = speed;
                utterThis.onend = function(event) {
                    that.sayText.finished = true;
                }
                SpeechSynthesis.speak(utterThis);
            },
            finished : false
        };
        this.tone = {
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
                    var C2 = 523;
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
                    var C2 = 523;
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
        };
        this.svg = '<svg id="svg'
                + this.id
                + '" xmlns="http://www.w3.org/2000/svg" width="313" height="482" viewBox="0 0 313 482">'
                + '<path stroke-alignment="inner" d="M1 88h17.5v-87h276v87h17.5v306h-17.5v87h-276v-87h-17.5z" style="fill:#fff;stroke:#000;stroke-width:2"/>'
                + '<rect x="19.5" y="2" width="274" height="225" style="fill:#A3A2A4;stroke:none"/>'
                + '<rect x="19.5" y="202" width="274" height="25" style="fill:#635F61;stroke:none"/>'
                + '<path d="M45 47.4c0-5.3 5.7-7.7 5.7-7.7s206.7 0 211 0c4.3 0 6.7 7.7 6.7 7.7v118.3c0 5.3-5.7 7.7-5.7 7.7s-206.7 0-211 0S44 164.7 44 164.7" fill="#333"/>'
                + '<rect x="67" y="41" width="180" height="130" fill="#ddd"/>'
                + '<line x1="155.7" y1="246" x2="155.7" y2="172.4" style="fill:none;stroke-width:9;stroke:#000"/>'
                + '<path id="led'
                + this.id
                + '" fill="url("#LIGHTGRAY'
                + this.id
                + '") d="M155.5 242.5 l20 0 40 40 0 52 -40 40 -40 0 -40 -40 0 -52 40 -40z" fill="#977" />'
                + '<path id="up'
                + this.id
                + '" class="simKey" d="M156 286c0 0 7 0 14.3-0.2s9 7.2 9 7.2v12.3h10.5v-19.5l9.7-9.7c0 0 2.8-0.2 0-3.3-2.8-3.2-26.5-25.7-26.5-25.7h-17-0.3-17c0 0-23.7 22.5-26.5 25.7s0 3.3 0 3.3l9.7 9.7v19.5h10.5v-12.3c0 0 1.7-7.3 9-7.2s14.3 0.2 14.3 0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="down'
                + this.id
                + '" class="simKey" d="M156 331c0 0 7 0 14.3 0.2s9-7.2 9-7.2v-12.3h10.5v19.5l9.7 9.7c0 0 2.8 0.2 0 3.3-2.8 3.2-26.5 25.7-26.5 25.7h-17-0.3-17c0 0-23.7-22.5-26.5-25.7s0-3.3 0-3.3l9.7-9.7v-19.5h10.5v12.3c0 0 1.7 7.3 9 7.2s14.3-0.2 14.3-0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="enter'
                + this.id
                + '" class="simKey" d="M138 293c0-1.4 0.9-2 0.9-2s32.6 0 33.2 0 1.1 2 1.1 2v31.4c0 1.4-0.9 2-0.9 2s-32.5 0-33.2 0c-0.7 0-1-2-1-2V293.1z" style="fill:#3C3C3B;stroke-width:2;stroke:#000"/>'
                + '<path id="escape'
                + this.id
                + '" class="simKey" d="M37 227v26.4c0 0 1.2 4.8 4.9 4.8s44.8 0 44.8 0l15.7-15.6v-15.7z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="left'
                + this.id
                + '" class="simKey" d="M69 309c0 12.5 14 17.9 14 17.9s27.1 0 29.8 0 2.8-1.7 2.8-1.7v-16.4 0.1-16.4c0 0-0.2-1.7-2.8-1.7s-29.7 0-29.7 0S69.3 296.7 69.3 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="right'
                + this.id
                + '" class="simKey" d="M242 309c0 12.5-14 17.9-14 17.9s-27.1 0-29.7 0-2.8-1.7-2.8-1.7v-16.4 0.1-16.4c0 0 0.2-1.7 2.8-1.7s29.8 0 29.8 0S241.9 296.7 241.9 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<rect x="19" y="412.4" width="274" height="67.7" style="fill:#A3A2A4"/>'
                + '<rect x="2" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                + '<rect x="294" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                + '<rect x="231.7" y="426.6" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                + '<rect x="246.2" y="426.7" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                + '<rect x="227.5" y="432.4" width="32.6" height="26.2" style="fill:#E52520;stroke:#000"/>' + '<g id="display' + this.id
                + '" clip-path="url(#clipPath)" fill="#000" transform="translate(67, 41)" font-family="Courier New" font-size="10pt">' + '</g>' + '<defs>'
                + '<clipPath id="clipPath">' + '<rect x="0" y="0" width="178" height="128"/>' + '</clipPath>' + '<radialGradient id="ORANGE' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb( 255, 165, 0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="RED' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(255,0,0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="GREEN' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(0,128,0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="LIGHTGRAY' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(211,211,211);stop-opacity:1" />' + '</radialGradient>' + '</defs>' + '</svg>';
        $("#simRobotContent").append(this.svg);
        $("#svg" + this.id).hide();
    }

    Ev3.prototype = Object.create(Robot.prototype);
    Ev3.prototype.constructor = Ev3;

    Ev3.prototype.reset = function() {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left = 0;
        this.right = 0;
        this.led.color = "LIGHTGRAY";
        this.led.mode = C.OFF;
        this.led.blink = 0;
        // Ev3.time = 0;
        for ( var key in this.timer) {
            this.timer[key] = 0;
        }
        var that = this;
        for ( var property in that.buttons) {
            $('#' + property + that.id).off('mousedown touchstart');
            $('#' + property + that.id).on('mousedown touchstart', function() {
                that.buttons[this.id.replace(/\d+$/, "")] = true;
            });
            $('#' + property + that.id).off('mouseup touchend');
            $('#' + property + that.id).on('mouseup touchend', function() {
                that.buttons[this.id.replace(/\d+$/, "")] = false;
            });
        }
        $("#display" + this.id).html('');
        this.tone.duration = 0;
        this.tone.frequency = 0;
        this.webAudio.volume = 0.5;
    };

    /**
     * Update all actions of the Ev3. The new pose is calculated with the
     * forward kinematics equations for a differential drive Ev3.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Ev3.prototype.update = function() {
        // update pose
        var motors = this.robotBehaviour.getActionState("motors", true);
        if (motors) {
            var left = motors.c;
            if (left !== undefined) {
                if (left > 100) {
                    left = 100;
                } else if (left < -100) {
                    left = -100;
                }
                this.left = left * C.MAXPOWER;
            }
            var right = motors.b;
            if (right !== undefined) {
                if (right > 100) {
                    right = 100;
                } else if (right < -100) {
                    right = -100;
                }
                this.right = right * C.MAXPOWER;
            }
        }
        var tempRight = this.right;
        var tempLeft = this.left;
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += this.left * SIM.getDt();
        this.encoder.right += this.right * SIM.getDt();
        var encoder = this.robotBehaviour.getActionState("encoder", true);
        if (encoder) {
            if (encoder.leftReset) {
                this.encoder.left = 0;
            }
            if (encoder.rightReset) {
                this.encoder.right = 0;
            }
        }
        if (this.frontLeft.bumped && this.left > 0) {
            tempLeft *= -1;
        }
        if (this.backLeft.bumped && this.left < 0) {
            tempLeft *= -1;
        }
        if (this.frontRight.bumped && this.right > 0) {
            tempRight *= -1;
        }
        if (this.backRight.bumped && this.right < 0) {
            tempRight *= -1;
        }
        if (tempRight == tempLeft) {
            var moveXY = tempRight * SIM.getDt();
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
            var R = C.TRACKWIDTH / 2 * ((tempLeft + tempRight) / (tempLeft - tempRight));
            var rot = (tempLeft - tempRight) / C.TRACKWIDTH;
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
        var led = this.robotBehaviour.getActionState("led", true);
        if (led) {
            var color = led.color;
            var mode = led.mode;
            if (color) {
                this.led.color = color.toUpperCase();
                this.led.blinkColor = color.toUpperCase();
            }
            switch (mode) {
            case C.OFF:
                this.led.timer = 0;
                this.led.blink = 0;
                this.led.color = 'LIGHTGRAY';
                break;
            case C.ON:
                this.led.timer = 0;
                this.led.blink = 0;
                break;
            case C.FLASH:
                this.led.blink = 2;
                break;
            case C.DOUBLE_FLASH:
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
        $("#led" + this.id).attr("fill", "url('#" + this.led.color + this.id + "')");
        // update display
        var display = this.robotBehaviour.getActionState("display", true);
        if (display) {
            var text = display.text;
            var x = display.x;
            var y = display.y;
            if (text) {
                $("#display" + this.id).html($("#display" + this.id).html() + '<text x=' + x * 10 + ' y=' + (y + 1) * 16 + '>' + text + '</text>');
            }
            if (display.picture) {
                $("#display" + this.id).html(this.display[display.picture]);
            }
            if (display.clear) {
                $("#display" + this.id).html('');
            }
        }
        // update tone
        var volume = this.robotBehaviour.getActionState("volume", true);
        if (volume && this.webAudio.context) {
            this.webAudio.volume = volume / 100.0;
        }
        var tone = this.robotBehaviour.getActionState("tone", true);
        if (tone && this.webAudio.context) {
            var ts = this.webAudio.context.currentTime;
            if (tone.frequency) {
                this.webAudio.oscillator.frequency.setValueAtTime(tone.frequency, ts);
                this.webAudio.gainNode.gain.setValueAtTime(this.webAudio.volume, ts);
            }
            if (tone.duration) {
                ts += tone.duration / 1000.0;
                this.webAudio.gainNode.gain.setValueAtTime(0, ts);
            }
            if (tone.file !== undefined) {
                this.tone.file[tone.file](this.webAudio);
            }
        }
        // update sayText
        this.sayText.language = GUISTATE_C.getLanguage(); // reset language
        var language = this.robotBehaviour.getActionState("language", true);
        if (language !== null && language !== undefined && window.speechSynthesis) {
            this.sayText.language = language;
        }
        var sayText = this.robotBehaviour.getActionState("sayText", true);
        if (sayText && window.speechSynthesis) {
            if (sayText.text) {
                this.sayText.say(sayText.text, this.sayText.language, sayText.speed, sayText.pitch);
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