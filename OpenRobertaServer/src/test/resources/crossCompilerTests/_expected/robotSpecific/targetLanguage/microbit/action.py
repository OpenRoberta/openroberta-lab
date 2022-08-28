import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___numberVar = 0
___booleanVar = True
___stringVar = ""
___imageVar = microbit.Image.HEART
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___imageList = [microbit.Image.HEART, microbit.Image.HEART]
def action():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    display()
    pin()
    play()

def display():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.scroll(str(___numberVar))
    microbit.display.scroll(str(___booleanVar))
    microbit.display.scroll(str(___stringVar))
    microbit.display.show(str(___numberVar))
    microbit.display.show(str(___booleanVar))
    microbit.display.show(str(___stringVar))
    microbit.display.show(___imageVar)
    microbit.display.show(___imageList)
    microbit.display.clear()
    microbit.display.set_pixel(___numberVar, ___numberVar, ___numberVar)
    microbit.display.scroll(str(microbit.display.get_pixel(___numberVar, ___numberVar)))
    print(___numberVar)
    print(___booleanVar)
    print(___stringVar)

def play():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    music.pitch(300, 100)
    music.pitch(400, 50)
    music.pitch(130, 2000)
    music.pitch(146, 1000)
    music.pitch(164, 500)

def pin():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.pin1.write_analog(___numberVar);
    microbit.pin2.write_digital(___numberVar);

def run():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    action()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()