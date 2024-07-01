import calliopemini
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


def run():
    global timer1
    calliopemini.speaker.on()
    print("Press Logo to start A to step through")
    while True:
        if calliopemini.pin_logo.is_touched() == True:
            break
    music.pitch(300, 100)
    calliopemini.sleep(500)
    calliopemini.display.scroll("Press logo one more time")
    while True:
        calliopemini.sleep(50)
        if calliopemini.pin_logo.is_touched():
            break
    calliopemini.sleep(500)
    music.pitch(261, 2000)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("dadadadum file")
    music.play(music.DADADADUM)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("entertainer file")
    music.play(music.ENTERTAINER)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("prelude file")
    music.play(music.PRELUDE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("ode file")
    music.play(music.ODE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("nyan file")
    music.play(music.NYAN)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("ringtone")
    music.play(music.RINGTONE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("funk")
    music.play(music.FUNK)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("blues")
    music.play(music.BLUES)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("birthday")
    music.play(music.BIRTHDAY)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("wedding")
    music.play(music.WEDDING)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("funeral")
    music.play(music.FUNERAL)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("punchline")
    music.play(music.PUNCHLINE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("python")
    music.play(music.PYTHON)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("baddy")
    music.play(music.BADDY)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("chase")
    music.play(music.CHASE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("chase")
    music.play(music.CHASE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("ba ding")
    music.play(music.BA_DING)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("wawawawawa")
    music.play(music.WAWAWAWAA)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("jump up")
    music.play(music.JUMP_UP)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("jump down")
    music.play(music.JUMP_DOWN)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("power up")
    music.play(music.POWER_UP)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("power down")
    music.play(music.POWER_DOWN)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("set Volume to 50%")
    calliopemini.set_volume(int(2.55 * 50))
    calliopemini.sleep(500)
    calliopemini.display.scroll("giggle")
    calliopemini.audio.play(calliopemini.Sound.GIGGLE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("set Volume to 100%")
    calliopemini.set_volume(int(2.55 * 100))
    calliopemini.sleep(500)
    calliopemini.display.scroll("happy")
    calliopemini.audio.play(calliopemini.Sound.HAPPY)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("hello")
    calliopemini.audio.play(calliopemini.Sound.HELLO)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("mysterious")
    calliopemini.audio.play(calliopemini.Sound.MYSTERIOUS)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("sad")
    calliopemini.audio.play(calliopemini.Sound.SAD)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("slide")
    calliopemini.audio.play(calliopemini.Sound.SLIDE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("soaring")
    calliopemini.audio.play(calliopemini.Sound.SOARING)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("spring")
    calliopemini.audio.play(calliopemini.Sound.SPRING)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("twinkle")
    calliopemini.audio.play(calliopemini.Sound.TWINKLE)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)
    calliopemini.display.scroll("yawn")
    calliopemini.audio.play(calliopemini.Sound.YAWN)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.display.scroll("speaker off")
    calliopemini.speaker.off()
    calliopemini.sleep(500)
    calliopemini.display.scroll("giggle with off speaker")
    calliopemini.audio.play(calliopemini.Sound.GIGGLE)
    calliopemini.display.scroll("done")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()