import * as $ from 'jquery';
import * as guiStateController from 'guiState.controller'

const MODE = "x3d";
const BROADCAST = false;

class StreamingViewer {
    private readonly element: HTMLElement;

    protected view;
    private webots;
    private disconnectCallback: Function;
    private readyCallback: Function;

    constructor(element: HTMLElement, webots) {
        this.element = element;
        this.webots = webots;
    }

    connect(url, mobileDevice: boolean, readyCallback: Function, disconnectCallback: Function) {
        if (!this.view) {
            this.view = new this.webots.View(this.element, mobileDevice);
        }
        this.view.broadcast = BROADCAST;
        this.view.setTimeout(-1); // Disable timeout

        this.disconnectCallback = disconnectCallback;
        this.readyCallback = readyCallback;

        this.view.open(url, MODE);
        this.view.onquit = () => this.disconnect();
        this.view.onready = () => {
            window.onresize = this.view.onresize;
            this.readyCallback();
        };
    }

    disconnect() {
        window.onresize = undefined;
        this.view.close();
        this.element.innerHTML = null;
        if (this.view.mode === "mjpeg") {
            this.view.multimediaClient = undefined;
        }
        this.disconnectCallback();
    }

    hideToolbar() {
        let toolbar = this.getToolbar();
        if (toolbar) {
            if (toolbar.style.display !== 'none') {
                toolbar.style.display = 'none';
                $("#view3d").height("100%");
                window.dispatchEvent(new Event('resize'));
            }
        }
    }

    showToolbar() {
        let toolbar = this.getToolbar();
        if (toolbar) {
            if (toolbar.style.display !== 'block')
                toolbar.style.display = 'block';
            $("#view3d").height("calc(100% - 48px)");
            window.dispatchEvent(new Event('resize'));
        }
    }

    showQuit(enable) {
        this.webots.showQuit = enable;
    }

    showRevert(enable) {
        this.webots.showRevert = enable;
    }

    showRun(enable) {
        this.webots.showRun = enable;
    }

    sendMessage(message: string) {
        if (typeof this.view !== 'undefined' && this.view.stream.socket.readyState === 1)
            this.view.stream.socket.send(message);
    }

    private getToolbar() {
        return this.element.querySelector<HTMLElement>('#toolBar');
    }
}

class WebotsSimulation extends StreamingViewer {
    private connected = false;
    private sourceCode: string;
    private context: any;
    private volume: number = 0.5;
    private lang: string;
    private SpeechSynthesis: any;
    private recognition: any;
    private final_transcript: string;

    constructor(element: HTMLElement, webots) {
        super(element, webots);
    }

    uploadController(sourceCode: string) {
        let message = {
            name: "supervisor",
            message: "upload:" + sourceCode
        };
        super.sendMessage('robot:' + JSON.stringify(message));
    }

    start(url: string, sourceCode: string) {
        this.sourceCode = sourceCode;

        if (this.connected) {
            return;
        }

        super.connect(url, false, () => {
            this.connected = true;
        }, () => {
            this.connected = false;
        });

        super.hideToolbar();
        this.initSpeechSynthesis();
        this.initSpeechRecognition();

        $("#webotsProgress").height("120px");

        let that = this;
        this.view.onstdout = function(text: string) {
            if (text.startsWith("finish")) {
                $("#simControl").trigger("click")
            } else if (text.startsWith("say")) {
                let data = text.split(":");
                that.sayText(data);
            } else if (text.startsWith("setLanguage")) {
                let data = text.split(":");
                that.lang = data[1];
            } else if (text.startsWith("setVolume")) {
                let data = text.split(":");
                that.volume = parseInt(data[1]) / 100;
            } else if (text.startsWith("getVolume")) {
                let message = {
                    name: "NAO",
                    message: "volume:" + that.volume * 100
                };
                that.sendMessage("robot:" + JSON.stringify(message));
            } else if (text.startsWith("recognizeSpeech")) {
                that.recognizeSpeech();
            } else {
                // console.log(text);  // enable this maybe for debugging
            }
        }
    }

    reset() {
        super.sendMessage('reset');
    }

    pause() {
        this.view.runOnLoad = false;
        super.sendMessage('pause');
    }

    run(sourceCode: string) {
        this.sourceCode = sourceCode;

        this.view.runOnLoad = true;
        super.sendMessage('pause');

        this.uploadController(sourceCode);

        super.sendMessage('real-time:-1');
    }

    initSpeechSynthesis() {
        this.SpeechSynthesis = window.speechSynthesis;
        //cancel needed so speak works in chrome because it's created already speaking
        this.SpeechSynthesis.cancel();
        if (!this.SpeechSynthesis) {
            this.context = null;
            console.log("Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
        }
    }

    sayText(data) {
        let text = data[1];
        let speed = data[2];
        let pitch = data[3];
        let lang = this.lang || guiStateController.getLanguage();
        // Prevents an empty string from crashing the simulation
        if (text === "") text = " ";
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
        // https://bugs.chromium.org/p/chromium/issues/detail?id=509488#c11
        // Workaround to keep utterance object from being garbage collected by the browser
        (window as any).utterances = [];
        (window as any).utterances.push(utterThis);
        if (lang === "") {
            console.log("Language is not supported!");
        } else {
            var voices = (this.SpeechSynthesis as any).getVoices();
            for (var i = 0; i < voices.length; i++) {
                if (voices[i].lang.indexOf(this.lang) !== -1 || voices[i].lang.indexOf(lang.substr(0, 2)) !== -1) {
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
        utterThis.volume = this.volume;
        let that = this;
        let message = {
            name: "NAO",
            message: "finish"
        };
        utterThis.onend = function(event) {
            that.sendMessage("robot:" + JSON.stringify(message));
        };
        //does not work for volume = 0 thus workaround with if statement
        if (this.volume != 0) {
            (this.SpeechSynthesis as any).speak(utterThis);
        } else {
            this.sendMessage("robot:" + JSON.stringify(message));
        }
    }

    initSpeechRecognition() {
        var SpeechRecognition = SpeechRecognition || (window as any).webkitSpeechRecognition;
        let that = this;
        if (SpeechRecognition) {
            this.recognition = new SpeechRecognition();
            this.recognition.continuous = false;
            this.recognition.interimResults = true;

            this.recognition.onresult = function(event) {
                for (var i = event.resultIndex; i < event.results.length; ++i) {
                    if (event.results[i].isFinal) {
                        that.final_transcript += event.results[i][0].transcript;
                    }
                }
            }

            this.recognition.onend = function() {
                let message = {
                    name: "NAO",
                    message: "transcript:" + that.final_transcript
                };
                that.sendMessage("robot:" + JSON.stringify(message));
                this.stop();
            }
        }
    }

    recognizeSpeech() {
        if (this.recognition) {
            this.final_transcript = "";
            this.recognition.lang = this.lang || guiStateController.getLanguage();
            this.recognition.start();
        } else {
            alert("Sorry, your browser does not support speech recognition. Please use the latest version of Chrome, Edge, Safari or Opera");
            let message = {
                name: "NAO",
                message: "transcript:" + ""
            };
            this.sendMessage("robot:" + JSON.stringify(message));
        }
    }
}

async function waitFor(predicate: () => boolean, interval: number, timeout: number): Promise<void> {
    const start = Date.now()
    return new Promise((resolve, reject) => {
        function check() {
            return setTimeout(() => {
                if (predicate()) {
                    resolve();
                } else if ((Date.now() - start) > timeout) {
                    reject()
                } else {
                    check();
                }
            }, interval);
        }
        check();
    });
}

class WebotsSimulationController {
    private isPrepared = false;
    private webotsSimulation: WebotsSimulation;

    async init(sourceCode) {

        $('#simEditButtons, #canvasDiv, #simRobot, #simValues').hide();
        $('#webotsDiv, #simButtons').show();


        if (!this.webotsSimulation) {
            this.prepareWebots();
            // Lazy load webots and all other dependencies only if the webots simulation is actually used
            // @ts-ignore
            const { webots } = await import('webots');

            try {
                await this.wasmLoaded();
            } catch (e) {
                console.error("Could not load webots simulation", e);
                return;
            }

            this.webotsSimulation = new WebotsSimulation(WebotsSimulationController.createWebotsDiv(), webots);
        }

        this.webotsSimulation.start(guiStateController.getWebotsUrl(), sourceCode)

    }

    run(sourceCode) {
        this.webotsSimulation.run(sourceCode);
    }

    resetPose() {
        this.webotsSimulation.reset();
    }

    stopProgram() {
        this.webotsSimulation.pause();
    }

    disconnect() {
        this.webotsSimulation.disconnect();
    }

    private async wasmLoaded() {
        await waitFor(() => window['Module']['asm'], 10, 1000);
    }

    private prepareWebots() {
        if (!this.isPrepared) {
            WebotsSimulationController.loadCss();
            WebotsSimulationController.prepareModuleForWebots();
            this.isPrepared = true;
        }
    }

    private static createWebotsDiv(): HTMLElement {
        let webotsDiv: HTMLElement = document.querySelector('#webotsDiv');
        if (webotsDiv) {
            return;
        }

        webotsDiv = document.createElement('webots-streaming');
        webotsDiv.id = 'webotsDiv';
        document.getElementById('simDiv').prepend(webotsDiv);

        return webotsDiv;
    }

    private static loadCss() {
        let link = document.createElement('link');
        link.href = 'https://cyberbotics.com/wwi/R2021c/css/wwi.css';
        link.type = 'text/css';
        link.rel = 'stylesheet';
        document.head.appendChild(link);
    }

    private static prepareModuleForWebots() {
        if (!window.hasOwnProperty("Module")) {
            window['Module'] = [];
        }
        window['Module']['locateFile'] = function(path, prefix) {
            // if it's a data file, use a custom dir
            if (path.endsWith(".data"))
                return window.location.origin + "/js/libs/webots/" + path;

            // otherwise, use the default, the prefix (JS file's dir) + the path
            return prefix + path;
        };
    }
}

export = new WebotsSimulationController()