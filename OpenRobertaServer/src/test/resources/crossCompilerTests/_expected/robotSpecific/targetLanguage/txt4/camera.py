import fischertechnik.factories as txt_factory
from lib.display import display
from fischertechnik.models.Color import Color
import colorsys
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

txt_factory.init_usb_factory()
txt_factory.init_camera_factory()
TXT_M_USB1_1_camera = txt_factory.usb_factory.create_camera(TXT_M, 1)
TXT_M_USB1_1_camera.set_rotate(False)
TXT_M_USB1_1_camera.set_height(240)
TXT_M_USB1_1_camera.set_width(320)
TXT_M_USB1_1_camera.set_fps(15)
TXT_M_USB1_1_camera.start()
CAMERA_HEIGHT = 240
CAMERA_WIDTH = 320

motion_detector = txt_factory.camera_factory.create_motion_detector(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_WIDTH * CAMERA_HEIGHT * (2 / 100) / 500)
TXT_M_USB1_1_camera.add_detector(motion_detector)
color_detector = txt_factory.camera_factory.create_color_detector(112, 84, 96, 72, 1)
TXT_M_USB1_1_camera.add_detector(color_detector)
line_detector = txt_factory.camera_factory.create_line_detector(60, 45, 200, 150, 5, 100, -100, 100, 2)
TXT_M_USB1_1_camera.add_detector(line_detector)
ball_detector = txt_factory.camera_factory.create_ball_detector(0, 0, 320, 240, 10, 100, -100, 100, [255, 165, 0], 20)
TXT_M_USB1_1_camera.add_detector(ball_detector)
txt_factory.initialized()


def get_ball_information():
    if ball_detector.detected():
        return [ball_detector.get_center_x(),
                ball_detector.get_center_y(),
                ball_detector.get_diameter()]
    return [-1, -1, -1]

def get_camera_colour():
    if color_detector.detected():
        result = color_detector.get_result().value.get_hex()
        return int(result[1:], 16)
    else:
        return -1

def compare_colour(hex1, hex2, tolerance):
    r1, g1, b1 = (hex1 >> i & 0xFF for i in (16, 8, 0))
    r2, g2, b2 = (hex2 >> i & 0xFF for i in (16, 8, 0))
    
    hsv1 = colorsys.rgb_to_hsv(r1 / 255, g1 / 255, b1 / 255)
    hsv2 = colorsys.rgb_to_hsv(r2 / 255, g2 / 255, b2 / 255)
    
    hue_diff = abs(hsv1[0] - hsv2[0])
    if hue_diff > 0.5:
        hue_diff = 1 - hue_diff
    saturation_diff = abs(hsv1[1] - hsv2[1])
    value_diff = abs(hsv1[2] - hsv2[2])
    return (hue_diff <= (tolerance / 360) and saturation_diff <= 0.5 and value_diff <= 0.5)

def get_line_colour(index):
    line = line_detector.get_line_by_index(index)
    if line:
        colour = line.color.get_hex()
        return int(colour[1:], 16)
    else:
        return -1

def get_line_information(index):
    line = line_detector.get_line_by_index(index)
    if line is not None:
        return [line.position, line.width]
    else:
        return [-1, -1]

def camera_initialized():
    while True:
        try:
            ball_detector.detected()
            line_detector.detected()
            color_detector.detected()
            motion_detector.detected()
            break
        except Exception:
            pass

def run():
    camera_initialized()
    print("Camera Tests")
    print("Press right display button to switch to next Detector")
    print("Config Settings are 30, Orange, 2")
    print("Camera Color")
    while True:
        print(get_camera_colour())
        time.sleep(200/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    print("Camera Color compared to red")
    time.sleep(300/1000)
    while True:
        print(compare_colour(get_camera_colour(), 0xff0000, 50))
        time.sleep(200/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    print("Line Information")
    time.sleep(300/1000)
    while True:
        print("".join(str(arg) for arg in ["Number of lines: ", line_detector.get_line_count()]))
        print("".join(str(arg) for arg in ["Color of first line: ", get_line_colour(0)]))
        print("Position and Width of line 0")
        for ___item in get_line_information(0):
            print(___item)
        time.sleep(200/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    print("Ball Information")
    time.sleep(300/1000)
    while True:
        print("".join(str(arg) for arg in ["X: ", get_ball_information()[0]]))
        print("".join(str(arg) for arg in ["Y: ", get_ball_information()[1]]))
        print("".join(str(arg) for arg in ["Diameter: ", get_ball_information()[2]]))
        time.sleep(200/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    print("Motion")
    time.sleep(300/1000)
    while True:
        print(motion_detector.detected())
        time.sleep(200/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    print("DONE")
    time.sleep(500/1000)

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()