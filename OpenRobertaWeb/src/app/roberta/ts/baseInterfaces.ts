export interface WebAudioBase {
    context: AudioContext;
    gainNode: GainNode;
    tone: {
        duration: number;
        timer: number;
        file: {
            0: (webAudio: WebAudioBase, osci: OscillatorNode) => void;
            1: (webAudio: WebAudioBase, osci: OscillatorNode) => void;
            2: (webAudio: WebAudioBase, osci: OscillatorNode) => void;
            3: (webAudio: WebAudioBase, osci: OscillatorNode) => void;
            4: (webAudio: WebAudioBase, osci: OscillatorNode) => void;
        };
    };
    volume: number;
}
