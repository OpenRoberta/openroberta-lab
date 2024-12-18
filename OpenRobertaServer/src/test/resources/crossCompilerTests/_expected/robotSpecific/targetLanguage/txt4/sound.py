import fischertechnik.factories as txt_factory
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

txt_factory.initialized()
time.sleep(0.1)

def run():
    TXT_M.get_loudspeaker().play("01_Airplane.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("02_Alarm.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("03_Bell.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("04_Braking.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("05_Car_horn_long.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("06_Car_horn_short.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("07_Crackling_wood.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("08_Excavator.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("09_Fantasy_1.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("10_Fantasy_2.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("11_Fantasy_3.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("12_Fantasy_4.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("13_Farm.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("14_Fire_department.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("15_Fire_noises.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("16_Formula1.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("17_Helicopter.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("18_Hydraulic.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("19_Motor_sound.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("20_Motor_starting.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("21_Propeller_airplane.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("22_Roller_coaster.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("23_Ships_horn.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("24_Tractor.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("25_Truck.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("26_Augenzwinkern.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("27_Fahrgeraeusch.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("28_Kopf_heben.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass
    TXT_M.get_loudspeaker().play("29_Kopf_neigen.wav", False)
    while TXT_M.get_loudspeaker().is_playing():
        pass

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()