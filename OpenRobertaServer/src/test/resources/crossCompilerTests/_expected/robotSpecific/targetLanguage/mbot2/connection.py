import cyberpi
import time
import math, random


def run():
    cyberpi.wifi_broadcast.set("Channel1", "1")
    cyberpi.wifi_broadcast.set("Channel1", 0)
    cyberpi.wifi_broadcast.set("Channel1", True)
    cyberpi.console.println(cyberpi.wifi_broadcast.get("Channel1"))

def main():
    try:
        run()
    except Exception as e:
        cyberpi.display.show_label("Exeption on Mbot 2", 16, int(8 * 0 + 5), int(17 * 0))
        cyberpi.display.show_label(e, 16, int(8 * 0 + 5), int(17 * 1))
        raise
main()