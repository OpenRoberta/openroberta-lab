import microbit
import random
import math
import music
import machine

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    while True:
        # shows ultrasonic distance on display and plays c tone if distance smaller 5cm and h" tone if distance larger than
                                20cm

        microbit.display.scroll(str(ultrasonic_get_distance(microbit.pin12, microbit.pin8)))
        if ultrasonic_get_distance(microbit.pin12, microbit.pin8) < 5:
            music.pitch(130, 250, microbit.pin16)
        if ultrasonic_get_distance(microbit.pin12, microbit.pin8) > 20:
            music.pitch(987, 125, microbit.pin16)

def main():
    try:
        run()
    except Exception as e:
        raise

def ultrasonic_get_distance(echo, trigger):
    trigger.write_digital(0)
    microbit.sleep(2)
    trigger.write_digital(1) 
    microbit.sleep(10) 
    trigger.write_digital(0) 

    duration = machine.time_pulse_us(echo, 1)  
    distance = duration * 0.0343 / 2  

    return round(distance, 2)   

if __name__ == "__main__":
    main()