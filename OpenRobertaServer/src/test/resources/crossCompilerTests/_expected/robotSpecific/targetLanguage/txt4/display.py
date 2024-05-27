import fischertechnik.factories as txt_factory
from lib.display import display
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

txt_factory.initialized()
current_led = "redLed"
led_colors = {
    "red": 0xcc0000,
    "yellow": 0xffff00,
    "green": 0x33cc00,
    "cyan": 0x33ffff,
    "blue": 0x3366ff,
    "purple": 0xcc33cc,
    "white": 0xffffff,
    "black": 0x000000
}


def clear_display():
    for i in range(8):
        display.set_attr("line" + str(i) + ".text", "")

def led_on(color_hex):
    global current_led
    color_led = get_closest_color(color_hex) + "Led"
    if color_led != current_led:
        display.set_attr(color_led + ".visible", str(True).lower())
        display.set_attr(current_led + ".visible", str(False).lower())
        current_led = color_led
    display.set_attr(current_led + ".active", str(True).lower())

def get_closest_color(hex_value):
    r, g, b = (hex_value >> i & 0xFF for i in (16, 8, 0))
    color_diffs = []
    for name, color in led_colors.items():
        cr, cg, cb = (color >> i & 0xFF for i in (16, 8, 0))
        distance = math.sqrt((r - cr) ** 2 + (g - cg) ** 2 + (b - cb) ** 2)
        color_diffs.append((distance, name))
    return min(color_diffs)[1]

def run():
    for color, value in led_colors.items():
        if color != "red":
            display.set_attr(color + "Led.visible", str(False).lower())

    print("Display Test")
    print("Show Text on Display ")
    display.set_attr("line" + str(0) + ".text", str("ROW 0"))
    display.set_attr("line" + str(1) + ".text", str("ROW 1"))
    display.set_attr("line" + str(2) + ".text", str("ROW 2"))
    display.set_attr("line" + str(3) + ".text", str("ROW 3"))
    display.set_attr("line" + str(4) + ".text", str("ROW 4"))
    display.set_attr("line" + str(5) + ".text", str("ROW 5"))
    display.set_attr("line" + str(6) + ".text", str("ROW 6"))
    display.set_attr("line" + str(7) + ".text", str("ROW 7"))
    time.sleep(5000/1000)
    print("Show different Text same rows")
    display.set_attr("line" + str(0) + ".text", str("THIS LINE HAS A VERY LONG TEXT FOR TESTING PURPOSES"))
    display.set_attr("line" + str(1) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(2) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(3) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(4) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(5) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(6) + ".text", str("SOME MORE TEXT"))
    display.set_attr("line" + str(7) + ".text", str("SOME MORE TEXT"))
    print("Clear Display in 4s")
    time.sleep(4000/1000)
    clear_display()
    print("Press Left Display Button to Continue")
    while True:
        if display.get_attr("buttonLeft.pressed") == True:
            break
    print("Success")
    print("Press Right Display Button to Continue")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
    print("Success")
    print("DISPLAY TESTS DONE")
    print("LED COLORS")
    led_on(0xcc0000)
    time.sleep(500/1000)
    led_on(0xffffff)
    time.sleep(500/1000)
    led_on(0xffcc33)
    time.sleep(500/1000)
    led_on(0x33cc00)
    time.sleep(500/1000)
    led_on(0x00cccc)
    time.sleep(500/1000)
    led_on(0x3366ff)
    time.sleep(500/1000)
    led_on(0x6633ff)
    time.sleep(500/1000)
    led_on(int("{:02x}{:02x}{:02x}".format(min(max(255, 0), 255), min(max(20, 0), 255), min(max(150, 0), 255)), 16))
    time.sleep(1000/1000)
    print("LED OFF")
    display.set_attr(current_led + ".active", str(False).lower())
    print("DONE")

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()