import fischertechnik.factories as txt_factory
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_output_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()
TXT_M_O5_led = txt_factory.output_factory.create_led(TXT_M, 5)
txt_factory.initialized()


def run():
    print("LED on port O5 test")
    print("LED ON")
    TXT_M_O5_led.set_brightness(int(max(min(int((100 / 100) * 512), 512), 0)))
    time.sleep(2000/1000)
    print("LED OFF")
    TXT_M_O5_led.set_brightness(int(max(min(int((0 / 100) * 512), 512), 0)))
    for ___i in range(int(1), int(100), int(1)):
        print("".join(str(arg) for arg in ["Brightness =", ___i]))
        TXT_M_O5_led.set_brightness(int(max(min(int((___i / 100) * 512), 512), 0)))
    print("DONE")

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()