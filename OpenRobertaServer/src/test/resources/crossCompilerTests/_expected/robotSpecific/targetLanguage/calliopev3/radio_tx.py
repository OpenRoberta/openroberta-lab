import calliopemini
import random
import math
import radio
import music
import neopixel

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
np = neopixel.NeoPixel(calliopemini.pin_RGB, 3)
brightness = 9
rssi = 0
radio.on()

___colours = [(255, 0, 0), (255, 153, 0), (255, 255, 0), (51, 255, 51), (102, 204, 204)]
___idx = 0

def run():
    global timer1, ___colours, ___idx
    radio.config(group=7)
    calliopemini.display.scroll("Hallo!")
    for ___k0 in range(int(0), int(3), int(1)):
        calliopemini.display.show(calliopemini.Image.HEART)
        calliopemini.sleep(500)
        calliopemini.display.show(calliopemini.Image.HEART_SMALL)
        calliopemini.sleep(500)
    calliopemini.display.clear()
    for ___k1 in range(int(0), int(5), int(1)):
        np[1] = (___colours[___idx])
        np.show()
        calliopemini.sleep(500)
        ___idx += 1
    np.clear()
    calliopemini.display.show(calliopemini.Image.MUSIC_QUAVERS)
    music.pitch(261, 500)
    music.pitch(195, 500)
    music.pitch(466, 1000)
    calliopemini.display.clear()
    calliopemini.display.show(calliopemini.Image('00900:09900:99999:09900:00900'))
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    radio.config(power=7)
    radio.send(str(77))
    radio.config(power=7)
    radio.send(str(True))
    radio.config(power=7)
    radio.send(str("Hallo Roberta!"))
    calliopemini.sleep(100)
    calliopemini.display.clear()
    calliopemini.display.show(calliopemini.Image.YES)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()