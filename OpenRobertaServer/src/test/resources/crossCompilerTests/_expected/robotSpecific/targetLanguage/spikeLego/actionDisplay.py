import spike
import math
from spike.control import wait_for_seconds, wait_until
import hub as _hub
display = _hub.display
Image = _hub.Image
hub = spike.PrimeHub()

def run():
    # display
    hub.light_matrix.write("Hallo")
    display.show(str("Hallo"))
    display.show([Image('09090:99999:99999:09990:00900')])
    wait_for_seconds(500/1000)
    display.show([Image('10005:02060:99999:07030:80004')])
    wait_for_seconds(1000/1000)
    hub.light_matrix.off()

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()