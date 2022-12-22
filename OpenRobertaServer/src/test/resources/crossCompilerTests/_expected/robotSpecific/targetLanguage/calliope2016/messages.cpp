#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___n;
bool ___b;
ManagedString ___s;

int main()
{
    _uBit.init();
    ___n = 0;
    ___b = true;
    ___s = ManagedString("");

    _uBit.radio.enable();
    _uBit.radio.setTransmitPower(0);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(1);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(2);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(3);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(4);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(5);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(6);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(7);
    _uBit.radio.datagram.send(ManagedString((int)(___n)));
    _uBit.radio.setTransmitPower(0);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(1);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(2);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(3);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(4);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(5);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(6);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(7);
    _uBit.radio.datagram.send(ManagedString((int)(___b)?true:false));
    _uBit.radio.setTransmitPower(0);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(1);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(2);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(3);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(4);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(5);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(6);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.radio.setTransmitPower(7);
    _uBit.radio.datagram.send(ManagedString((___s)));
    _uBit.display.scroll(ManagedString(atoi((char*)_uBit.radio.datagram.recv().getBytes())));
    _uBit.display.scroll(ManagedString(atoi((char*)_uBit.radio.datagram.recv().getBytes())));
    _uBit.display.scroll(ManagedString(ManagedString(_uBit.radio.datagram.recv())));
    _uBit.radio.setGroup(___n);
    _uBit.display.scroll(ManagedString(_uBit.radio.getRSSI()));
    release_fiber();
}
