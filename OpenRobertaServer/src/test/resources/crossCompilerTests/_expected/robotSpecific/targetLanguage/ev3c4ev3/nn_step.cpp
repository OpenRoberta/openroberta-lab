#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"



double ___o1 = 0;
double ___o2 = 0;

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NULL, NULL, NULL);
    
    
    // NNstep not yet available for target code generation
    
    NEPOFreeEV3();
    return 0;
}
