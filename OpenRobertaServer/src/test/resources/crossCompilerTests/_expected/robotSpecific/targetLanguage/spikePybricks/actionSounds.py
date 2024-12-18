from pybricks.hubs import PrimeHub
from pybricks.tools import Matrix, wait
import umath as math
import urandom as random

def show_animation(image_list):
    hub.display.animate(image_list, 500)
    wait(500 * len(image_list) - 10)
    hub.display.icon(image_list[len(image_list)-1])

hub = PrimeHub()
hub.speaker.volume(15)

___A = 440
___C = float(261.6)

def run():
    global ___A, ___C
    hub.display.icon(Matrix([[0, 0, 100, 0, 0], [0, 0, 100, 100, 0], [0, 0, 100, 0, 100], [100, 100, 100, 0, 0], [100, 100, 100, 0, 0]]))
    hub.speaker.beep(261.626, 2000)
    hub.speaker.beep(391.995, 1000)
    hub.speaker.beep(783.991, 500)
    hub.speaker.beep(329.628, 250)
    hub.speaker.beep(146.832, 125)
    hub.speaker.beep(___A, 250)
    hub.speaker.beep(___C, 250)

def main():
    try:
        run()
    except Exception as e:
        while True:
            hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))

main()