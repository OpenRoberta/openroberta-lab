import cyberpi, mbuild
import time
import math, random

_colors = {
            "red": (204,0,0),
            "yellow": (255,255,0),
            "green": (51,204,0),
            "cyan": (51,255,255),
            "blue": (51,102,255),
            "purple": (204,51,204),
            "white": (255,255,255),
            "black": (0,0,0)
        }

def _RGBAsString(rgb):
    r, g, b = rgb
    color_diffs = []
    for color in _colors:
        cr, cg, cb = _colors[color]
        color_diff = math.sqrt(abs(r - cr)**2 + abs(g - cg)**2 + abs(b - cb)**2)
        color_diffs.append((color_diff, color))
    return min(color_diffs)[1]


___colorVar = (204, 0, 0)
___stringVar = "1"
___booleanVar = True
___numberVar = 0
___numberList = [0, 0, 0]
___booleanList = [True, True, True]
___stringList = ["", "", ""]
___colorList = [(204, 0, 0), (204, 0, 0), (204, 0, 0)]
def colours():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    cyberpi.display.set_brush((204, 0, 0)[0], (204, 0, 0)[1], (204, 0, 0)[2])
    cyberpi.led.on((255, 102, 0)[0], (255, 102, 0)[1], (255, 102, 0)[2], 1)
    mbuild.quad_rgb_sensor.set_led(_RGBAsString((255, 255, 0)), 1)
    cyberpi.led.on((51, 204, 0)[0], (51, 204, 0)[1], (51, 204, 0)[2], 1)
    cyberpi.led.on((51, 255, 255)[0], (51, 255, 255)[1], (51, 255, 255)[2], 1)
    cyberpi.led.on((51, 102, 255)[0], (51, 102, 255)[1], (51, 102, 255)[2], 1)
    cyberpi.led.on((204, 51, 204)[0], (204, 51, 204)[1], (204, 51, 204)[2], 1)
    cyberpi.led.on((255, 255, 255)[0], (255, 255, 255)[1], (255, 255, 255)[2], 1)
    cyberpi.led.on((0, 0, 0)[0], (0, 0, 0)[1], (0, 0, 0)[2], 1)
    cyberpi.led.on((0, 102, 0)[0], (0, 102, 0)[1], (0, 102, 0)[2], 1)
    mbuild.quad_rgb_sensor.set_led(_RGBAsString((51, 51, 153)), 1)
    cyberpi.led.on((255, 20, 150)[0], (255, 20, 150)[1], (255, 20, 150)[2], 1)
    mbuild.quad_rgb_sensor.set_led(_RGBAsString((255, 20, 150)), 1)
    cyberpi.display.set_brush((255, 20, 150)[0], (255, 20, 150)[1], (255, 20, 150)[2])
    cyberpi.console.println((255, 255, 255))
    cyberpi.console.println((204, 204, 204))
    cyberpi.console.println((192, 192, 192))
    cyberpi.console.println((153, 153, 153))
    cyberpi.console.println((102, 102, 102))
    cyberpi.console.println((51, 51, 51))
    cyberpi.console.println((0, 0, 0))
    cyberpi.console.println((255, 204, 204))
    cyberpi.console.println((255, 102, 102))
    cyberpi.console.println((255, 0, 0))
    cyberpi.console.println((204, 0, 0))
    cyberpi.console.println((153, 0, 0))
    cyberpi.console.println((102, 0, 0))
    cyberpi.console.println((51, 0, 0))
    cyberpi.console.println((255, 204, 153))
    cyberpi.console.println((255, 153, 102))
    cyberpi.console.println((255, 153, 0))
    cyberpi.console.println((255, 102, 0))
    cyberpi.console.println((204, 102, 0))
    cyberpi.console.println((153, 51, 0))
    cyberpi.console.println((102, 51, 0))
    cyberpi.console.println((255, 255, 153))
    cyberpi.console.println((255, 255, 102))
    cyberpi.console.println((255, 204, 102))
    cyberpi.console.println((255, 204, 51))
    cyberpi.console.println((204, 153, 51))
    cyberpi.console.println((153, 102, 51))
    cyberpi.console.println((102, 51, 51))
    cyberpi.console.println((255, 255, 204))
    cyberpi.console.println((255, 255, 51))
    cyberpi.console.println((255, 255, 0))
    cyberpi.console.println((255, 204, 0))
    cyberpi.console.println((255, 204, 0))
    cyberpi.console.println((153, 153, 0))
    cyberpi.console.println((102, 102, 0))
    cyberpi.console.println((51, 51, 0))
    cyberpi.console.println((153, 255, 153))
    cyberpi.console.println((102, 255, 153))
    cyberpi.console.println((51, 255, 51))
    cyberpi.console.println((51, 204, 0))
    cyberpi.console.println((0, 153, 0))
    cyberpi.console.println((0, 102, 0))
    cyberpi.console.println((0, 51, 0))
    cyberpi.console.println((153, 255, 255))
    cyberpi.console.println((51, 255, 255))
    cyberpi.console.println((102, 204, 204))
    cyberpi.console.println((0, 204, 204))
    cyberpi.console.println((51, 153, 153))
    cyberpi.console.println((51, 102, 102))
    cyberpi.console.println((0, 51, 51))
    cyberpi.console.println((204, 255, 255))
    cyberpi.console.println((102, 255, 255))
    cyberpi.console.println((51, 204, 255))
    cyberpi.console.println((51, 102, 255))
    cyberpi.console.println((51, 51, 255))
    cyberpi.console.println((0, 0, 153))
    cyberpi.console.println((0, 0, 102))
    cyberpi.console.println((204, 204, 255))
    cyberpi.console.println((153, 153, 255))
    cyberpi.console.println((102, 102, 204))
    cyberpi.console.println((102, 51, 255))
    cyberpi.console.println((102, 0, 204))
    cyberpi.console.println((51, 51, 153))
    cyberpi.console.println((51, 0, 153))
    cyberpi.console.println((255, 204, 255))
    cyberpi.console.println((255, 153, 255))
    cyberpi.console.println((204, 102, 204))
    cyberpi.console.println((204, 51, 204))
    cyberpi.console.println((153, 51, 153))
    cyberpi.console.println((102, 51, 102))
    cyberpi.console.println((51, 0, 51))

def function_returnNumberVar():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___numberVar

def text():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    cyberpi.console.println("")
    # 
    cyberpi.console.println("".join(str(arg) for arg in [___stringVar, ___stringVar]))
    ___stringVar += str(___stringVar)
    cyberpi.console.println(float(___stringVar))
    cyberpi.console.println(ord(___stringVar[0]))

def function_returnBooleanVar():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___booleanVar

def function_returnStringVar():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___stringVar

def function_colorVar():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___colorVar

def function_parameters(___x, ___x2, ___x3, ___x4, ___x5, ___x6, ___x7, ___x8):
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    if ___booleanVar: return None

def function_returnNumberList():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___numberList

def function_returnBooleanList():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___booleanList

def function_returnStringList():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___stringList

def function_returnColorList():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    return ___colorList

def run():
    global ___colorVar, ___stringVar, ___booleanVar, ___numberVar, ___numberList, ___booleanList, ___stringList, ___colorList
    colours()
    text()
    function_parameters(___numberVar, ___booleanVar, ___stringVar, ___colorVar, ___numberList, ___booleanList, ___stringList, ___colorList)
    cyberpi.console.println(function_returnNumberVar())
    cyberpi.console.println(function_returnBooleanVar())
    cyberpi.console.println(function_returnStringVar())
    cyberpi.console.println(function_colorVar())
    cyberpi.console.println(function_returnNumberList())
    cyberpi.console.println(function_returnBooleanList())
    cyberpi.console.println(function_returnStringList())
    cyberpi.console.println(function_returnColorList())

def main():
    try:
        run()
    except Exception as e:
        raise
main()