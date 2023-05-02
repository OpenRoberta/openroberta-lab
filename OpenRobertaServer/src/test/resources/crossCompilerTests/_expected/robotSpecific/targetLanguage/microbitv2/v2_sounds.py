import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("Sound level, press A to go next")
    while not microbit.button_a.is_pressed():
        microbit.display.scroll(str(int((microbit.microphone.sound_level() / 255) * 100)))
    microbit.set_volume(int(2.55 * 100))
    microbit.display.scroll("Volume 100")
    music.pitch(300, 100)
    microbit.set_volume(int(2.55 * 50))
    microbit.display.scroll("Volume 50")
    music.pitch(300, 100)
    microbit.set_volume(int(2.55 * 0))
    microbit.display.scroll("Volume 0")
    music.pitch(300, 100)
    microbit.set_volume(int(2.55 * 100))
    microbit.display.scroll("Volume 100")
    music.pitch(300, 100)
    microbit.display.scroll("Toggle Speaker OFF")
    microbit.speaker.off()
    music.pitch(300, 100)
    microbit.speaker.on()
    microbit.display.scroll("Toggle Speaker ON")
    music.pitch(300, 100)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()