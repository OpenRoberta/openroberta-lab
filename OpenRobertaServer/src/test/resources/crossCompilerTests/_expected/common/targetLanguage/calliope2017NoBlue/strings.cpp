#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



ManagedString ___text;
ManagedString ___eight;
double ___number;

int main()
{
    _uBit.init();
    ___text = ManagedString("start:");
    ___eight = ManagedString(" eight");
    ___number = 0;

    // String Concat -- Start --
    ___text = ___text + ManagedString(ManagedString(" one"));
    assertNepo((ManagedString("start: one") == ___text), "POS-1", ManagedString("start: one"), "EQ", ___text);
    ___text = ___text + ManagedString(ManagedString(ManagedString(" two")) + ManagedString(ManagedString(" three")));
    assertNepo((ManagedString("start: one two three") == ___text), "POS-2", ManagedString("start: one two three"), "EQ", ___text);
    ___text = ___text + ManagedString(ManagedString(4) + ManagedString(5));
    assertNepo((ManagedString("start: one two three45") == ___text), "POS-3", ManagedString("start: one two three45"), "EQ", ___text);
    ___text = ___text + ManagedString(ManagedString(6) + ManagedString(ManagedString(" seven")));
    assertNepo((ManagedString("start: one two three456 seven") == ___text), "POS-4", ManagedString("start: one two three456 seven"), "EQ", ___text);
    ___text = ManagedString(ManagedString(___text) + ManagedString(___eight)) + ManagedString(ManagedString(" nine"));
    assertNepo((ManagedString("start: one two three456 seven eight nine") == ___text), "POS-5", ManagedString("start: one two three456 seven eight nine"), "EQ", ___text);
    ___text = ManagedString(ManagedString(___text) + ManagedString(ManagedString("ten"))) + ManagedString(ManagedString(ManagedString(" eleven")) + ManagedString(ManagedString(" twelve")));
    _uBit.serial.setTxBufferSize(ManagedString((( ( ManagedString("start: one two three456 seven eight nine ten eleven twelve") == ___text ) ? ( ManagedString("String Concat SUCCESS") ) : ( ManagedString("String Concat FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( ManagedString("start: one two three456 seven eight nine ten eleven twelve") == ___text ) ? ( ManagedString("String Concat SUCCESS") ) : ( ManagedString("String Concat FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    ___number = std::atof((___text).toCharArray());
    ___number = (int)(___text.charAt(0));
    ___text = ManagedString(10);
    ___text = ManagedString((char)(30));
    // String Concat -- End --
    release_fiber();
}

