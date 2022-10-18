import spike
import math
from spike.control import wait_for_seconds, wait_until
hub = spike.PrimeHub()

def get_midi_from(frequency):
    midi = round(69 + math.log(frequency / 440, 2) * 12)
    if (midi < 44):
        return 44
    if (midi > 123):
        return 123
    return midi

def set_status_light(color):
    if (color is None):
        color = 'black'
    hub.status_light.on(color)

def run():
    # sounds
    hub.speaker.beep(65, 2.0);
    hub.speaker.beep(67, 1.0);
    hub.speaker.beep(69, 0.5);
    hub.speaker.beep(71, 0.25);
    hub.speaker.beep(72, 0.125);
    hub.speaker.beep(get_midi_from(400), 100 / 1000);
    # lights
    set_status_light('pink');
    wait_for_seconds(500/1000)
    set_status_light('violet');
    wait_for_seconds(500/1000)
    set_status_light('blue');
    wait_for_seconds(500/1000)
    set_status_light('azure');
    wait_for_seconds(500/1000)
    set_status_light('cyan');
    wait_for_seconds(500/1000)
    set_status_light('green');
    wait_for_seconds(500/1000)
    set_status_light('yellow');
    wait_for_seconds(500/1000)
    set_status_light('orange');
    wait_for_seconds(500/1000)
    set_status_light('red');
    wait_for_seconds(500/1000)
    set_status_light('black');
    wait_for_seconds(500/1000)
    set_status_light('white');
    wait_for_seconds(500/1000)
    set_status_light(None);
    wait_for_seconds(500/1000)
    set_status_light('pink');
    wait_for_seconds(500/1000)
    hub.status_light.off()

def main():
    try:
        run()
    except Exception as e:
        hub.light_matrix.show_image('SAD')

main()