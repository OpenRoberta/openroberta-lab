import microbit
import random
import math
import radio

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()
radio.on()

___numberVar = 0
___booleanVar = True
___stringVar = ""
___imageVar = microbit.Image.HEART
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___imageList = [microbit.Image.HEART, microbit.Image.HEART]
def text():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.scroll("")
    # 
    microbit.display.scroll(str("".join(str(arg) for arg in [___numberVar, ___booleanVar, ___stringVar])))
    ___stringVar += str(___stringVar)

def images():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.show(microbit.Image('00000:00000:00000:00000:00000'))
    microbit.display.show(___imageVar.invert())
    microbit.display.show(___imageVar.shift_up(___numberVar))
    microbit.display.show(___imageVar.shift_down(___numberVar))
    microbit.display.show(___imageVar.shift_right(___numberVar))
    microbit.display.show(___imageVar.shift_left(___numberVar))
    microbit.display.show(microbit.Image.HEART)

def messages():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    radio.config(power=0)
    radio.send(str(___numberVar))
    radio.config(power=1)
    radio.send(str(___numberVar))
    radio.config(power=2)
    radio.send(str(___numberVar))
    radio.config(power=3)
    radio.send(str(___numberVar))
    radio.config(power=4)
    radio.send(str(___numberVar))
    radio.config(power=5)
    radio.send(str(___numberVar))
    radio.config(power=6)
    radio.send(str(___numberVar))
    radio.config(power=7)
    radio.send(str(___numberVar))
    radio.config(power=0)
    radio.send(str(___booleanVar))
    radio.config(power=1)
    radio.send(str(___booleanVar))
    radio.config(power=2)
    radio.send(str(___booleanVar))
    radio.config(power=3)
    radio.send(str(___booleanVar))
    radio.config(power=4)
    radio.send(str(___booleanVar))
    radio.config(power=5)
    radio.send(str(___booleanVar))
    radio.config(power=6)
    radio.send(str(___booleanVar))
    radio.config(power=7)
    radio.send(str(___booleanVar))
    radio.config(power=0)
    radio.send(str(___stringVar))
    radio.config(power=1)
    radio.send(str(___stringVar))
    radio.config(power=2)
    radio.send(str(___stringVar))
    radio.config(power=3)
    radio.send(str(___stringVar))
    radio.config(power=4)
    radio.send(str(___stringVar))
    radio.config(power=5)
    radio.send(str(___stringVar))
    radio.config(power=6)
    radio.send(str(___stringVar))
    radio.config(power=7)
    radio.send(str(___stringVar))
    microbit.display.scroll(str(((lambda x: 0 if x is None else float(x))(radio.receive()))))
    microbit.display.scroll(str(('True' == radio.receive())))
    microbit.display.scroll(str(radio.receive()))
    radio.config(group=___numberVar)

def function_parameter(___x, ___x2, ___x3, ___x4, ___x5, ___x6, ___x7, ___x8):
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    if ___booleanVar: return None

def function_return_numberVar():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___numberVar

def function_return_booleanVar():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___booleanVar

def function_return_stringVar():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___stringVar

def function_return_imageVar():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___imageVar

def function_return_numberList():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___numberList

def function_return_booleanList():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___booleanList

def function_return_stringList():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___stringList

def function_return_imageList():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    return ___imageList

def run():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    text()
    images()
    messages()
    function_parameter(___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList)
    microbit.display.scroll(str(function_return_numberVar()))
    microbit.display.scroll(str(function_return_booleanVar()))
    microbit.display.scroll(str(function_return_stringVar()))
    microbit.display.show(function_return_imageVar())
    ___numberList = function_return_numberList()
    ___booleanList = function_return_booleanList()
    ___stringList = function_return_stringList()
    microbit.display.show(function_return_imageList())

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()