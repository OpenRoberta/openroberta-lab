import fischertechnik.factories as txt_factory
from lib.display import display
from fischertechnik.models.Color import Color
import colorsys
import math
import time

def get_ball_information(detector):
    if detector.detected():
        return [detector.get_center_x(),
                detector.get_center_y(),
                detector.get_diameter()]
    return [-1, -1, -1]

def get_camera_colour(detector):
    if detector.detected():
        result = detector.get_result().value.get_hex()
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

def get_line_colour(detector, index):
    line = detector.get_line_by_index(index)
    if line:
        colour = line.color.get_hex()
        return int(colour[1:], 16)
    else:
        return -1

def get_line_information(detector, index):
    line = detector.get_line_by_index(index)
    if line is not None:
        return [line.position, line.width]
    else:
        return [-1, -1]

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
motion_detector_M = txt_factory.camera_factory.create_motion_detector(0, 100, 320, 20, 100)
TXT_M_USB1_1_camera.add_detector(motion_detector_M)
motion_detector_M2 = txt_factory.camera_factory.create_motion_detector(0, 100, 320, 20, 49)
TXT_M_USB1_1_camera.add_detector(motion_detector_M2)
color_detector_C2 = txt_factory.camera_factory.create_color_detector(0, 100, 320, 20, 1)
TXT_M_USB1_1_camera.add_detector(color_detector_C2)
color_detector_C3 = txt_factory.camera_factory.create_color_detector(0, 100, 320, 20, 1)
TXT_M_USB1_1_camera.add_detector(color_detector_C3)
line_detector_L = txt_factory.camera_factory.create_line_detector(0, 100, 100, 100, 12, 50, -100, 100, 6)
TXT_M_USB1_1_camera.add_detector(line_detector_L)
line_detector_L2 = txt_factory.camera_factory.create_line_detector(20, 10, 479, 290, 20, 60, -100, 100, 4)
TXT_M_USB1_1_camera.add_detector(line_detector_L2)
ball_detector_B = txt_factory.camera_factory.create_ball_detector(0, 100, 320, 20, 10,100, -100, 100, [255, 165, 0], 20)
TXT_M_USB1_1_camera.add_detector(ball_detector_B)
ball_detector_B2 = txt_factory.camera_factory.create_ball_detector(0, 100, 320, 20, 10,100, -100, 100, [102, 102, 0], 20)
TXT_M_USB1_1_camera.add_detector(ball_detector_B2)
txt_factory.initialized()
time.sleep(0.1)

def camera_initialized():
    while True:
        try:
            ball_detector_B.detected()
            ball_detector_B2.detected()
            line_detector_L.detected()
            line_detector_L2.detected()
            color_detector_C2.detected()
            color_detector_C3.detected()
            motion_detector_M.detected()
            motion_detector_M2.detected()
            break
        except Exception:
            pass

def ____motion():
    print("Motiondetectors")
    while True:
        print(motion_detector_M.detected())
        print(motion_detector_M2.detected())
        print("#######")
        time.sleep(500/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    time.sleep(500/1000)

def ____ball():
    print("Ball Detectors")
    while True:
        print(get_ball_information(ball_detector_B)[0])
        print(get_ball_information(ball_detector_B)[1])
        print(get_ball_information(ball_detector_B)[2])
        print("---------")
        print(get_ball_information(ball_detector_B2)[0])
        print(get_ball_information(ball_detector_B2)[1])
        print(get_ball_information(ball_detector_B2)[2])
        print("#######")
        time.sleep(500/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    time.sleep(500/1000)

def ____line():
    print("Line Detector 1")
    while True:
        print(line_detector_L.get_line_count())
        for ___i in range(int(0), int(line_detector_L.get_line_count()), int(1)):
            print("".join(str(arg) for arg in ["Line:", ___i]))
            print(get_line_information(line_detector_L, ___i)[0])
            print(get_line_information(line_detector_L, ___i)[1])
            print(get_line_colour(line_detector_L, ___i))
        print("#######")
        time.sleep(500/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    time.sleep(500/1000)
    print("Line Detector 2")
    while True:
        print(line_detector_L2.get_line_count())
        for ___j in range(int(0), int(line_detector_L2.get_line_count()), int(1)):
            print("".join(str(arg) for arg in ["Line:", ___j]))
            for ___item in get_line_information(line_detector_L2, ___j):
                print(___item)
        print("#######")
        time.sleep(500/1000)
        if display.get_attr("buttonRight.pressed"):
            break

def ____color():
    print("Color Detectors")
    while True:
        print(get_camera_colour(color_detector_C2))
        print(get_camera_colour(color_detector_C3))
        print(compare_colour(get_camera_colour(color_detector_C2), 0x000000, 20))
        print("#######")
        time.sleep(500/1000)
        if display.get_attr("buttonRight.pressed"):
            break
    time.sleep(500/1000)

def run():
    camera_initialized()
    ____ball()
    ____color()
    ____motion()
    ____line()

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()