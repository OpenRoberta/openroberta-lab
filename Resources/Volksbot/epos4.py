import logging
import sys
import time
from ctypes import *

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
PATH_TO_LIB = '/opt/EposCmdLib_6.6.2.0/lib/v7/libEposCmd.so.6.6.2.0'

cdll.LoadLibrary(PATH_TO_LIB)
epos = CDLL(PATH_TO_LIB)

NodeID_1 = 1
NodeID_2 = 2

SPEED = 1000
BLOCKED = 1750

ret = 0
pErrorCode1 = c_uint()
pErrorCode2 = c_uint()
pDeviceErrorCode = c_uint()


def openDevice(DeviceName=b'EPOS4', ProtocolStackName=b'MAXON SERIAL V2', InterfaceName=b'USB', PortName=b'USB0', pErrorCode=pErrorCode1):
    # VCS_ResetDevice(HANDLE KeyHandle, WORD NodeId, DWORD * pErrorCode)
    keyhandle = epos.VCS_OpenDevice(
        DeviceName, ProtocolStackName, InterfaceName, PortName, byref(pErrorCode))
    if keyhandle == 0:
        logging.info('Could not open Com-Port')
        logging.info('Error Openening Port: %#5.8x' % pErrorCode.value)
        raise ConnectionError('Keyhandle: %8d' % keyhandle)
    a = epos.VCS_ClearFault(keyhandle, NodeID_1, byref(pErrorCode))
    EvalError(pErrorCode)
    b = epos.VCS_ClearFault(keyhandle, NodeID_2, byref(pErrorCode))
    EvalError(pErrorCode)
    # Verify Error State of EPOS4
    epos.VCS_GetDeviceErrorCode(keyhandle, NodeID_1, 1, byref(
        pDeviceErrorCode), byref(pErrorCode))
    epos.VCS_GetDeviceErrorCode(keyhandle, NodeID_2, 1, byref(
        pDeviceErrorCode), byref(pErrorCode))
    logging.info('Device: %#5.8x' % pDeviceErrorCode.value)
    if pDeviceErrorCode.value != 0:
        raise Exception('Device Error: %#5.8x' % pDeviceErrorCode.value)
    return keyhandle


def closeDevice(keyhandle, pErrorCode):
    # Close Com-Port
    ret = epos.VCS_CloseDevice(keyhandle, byref(pErrorCode))
    logging.info('Error Code Closing Port: %#5.8x' % pErrorCode.value)


def activateCVP(keyhandle, node, pErrorCode):
    logging.info("Activating PPM")
    ret1 = epos.VCS_SetOperationMode(
        keyhandle, node, 9, byref(pErrorCode))
    EvalError(pErrorCode)
    return ret1 != 0


def setState(keyhandle: int, state: str):
    if state == 'enable':
        logging.info("Set Enable State")
        ret1 = epos.VCS_SetEnableState(keyhandle, NodeID_1, byref(pErrorCode1))
        ret2 = epos.VCS_SetEnableState(keyhandle, NodeID_2, byref(pErrorCode2))
    elif state == 'disable':
        logging.info("Set Disable State")
        ret1 = epos.VCS_SetDisableState(
            keyhandle, NodeID_1, byref(pErrorCode1))
        ret2 = epos.VCS_SetDisableState(
            keyhandle, NodeID_2, byref(pErrorCode2))
    elif state == 'quick_stop':
        logging.info("Set QuickStop State")
        ret1 = epos.VCS_SetQuickStopState(
            keyhandle, NodeID_1, byref(pErrorCode1))
        ret2 = epos.VCS_SetQuickStopState(
            keyhandle, NodeID_2, byref(pErrorCode2))

    return ret1 != 0 and ret2 != 0


def MoveToPosition(keyhandle: int, node: int, position: int, absolute: bool, immediately: bool):
    epos.VCS_MoveToPosition(keyhandle, node, position,
                            absolute, immediately, byref(pErrorCode1))
    PositionReached(keyhandle, node, 10, pErrorCode1)


def Drive(keyhandle: int, position1: int, position2: int):
    FACTOR = 73.5 * 2000
    timeout = max(abs(position1), abs(position2)) * 3
    ret = PositionReached(keyhandle, timeout, int(
        (position1/56)*FACTOR), int(
        (position2/56)*FACTOR))
    if ret == 1:
        print("Drive executed")
    else:
        print("Drive stopped with timeout")


def Turn(keyhandle: int, position1: int, position2: int):
    Drive(keyhandle, -11, -11)
    Drive(keyhandle, position1, position2)
    Drive(keyhandle, 11, 11)


def Align(keyhandle: int):
    Drive(keyhandle, 80, 80)
    time.sleep(1)
    Drive(keyhandle, -31, -31)
    time.sleep(1)
    Drive(keyhandle, -29.29, 29.29)
    time.sleep(1)
    Drive(keyhandle, 80, 80)
    time.sleep(1)
    Drive(keyhandle, -22, -22)


def haltPositionMovement(keyhandle: int, node: int, pErrorCode: int):
    logging.info(f"Halt position movement profile node {node}")
    ret = epos.VCS_HaltPositionMovement(keyhandle, node, byref(pErrorCode))
    EvalError(pErrorCode)
    return ret


def EvalError(pErrorCode):
    pErrorInfo = ' ' * 40

    if pErrorCode.value != 0x0:
        epos.VCS_GetErrorInfo(pErrorCode.value, pErrorInfo, 40)
        logging.info('Error Code %#5.8x' % (pErrorCode.value))
        logging.info('Description: %s' % pErrorInfo)
        return 0

    # No Error
    else:
        return 1


def PositionReached(keyhandle, iTimeOut, ticks1, ticks2):
    print(iTimeOut)
    direction1 = (-1, 1)[ticks1 < 0]
    direction2 = (1, -1)[ticks2 < 0]
    ticksReached1 = c_long()
    ticksReached2 = c_long()
    pCurrentIs1 = c_short()
    pCurrentIs2 = c_short()
    ret1 = epos.VCS_GetPositionIs(
        keyhandle, NodeID_1, byref(ticksReached1), byref(pErrorCode1))
    ret2 = epos.VCS_GetPositionIs(
        keyhandle, NodeID_2, byref(ticksReached2), byref(pErrorCode2))
    offsetTicks1 = ticksReached1.value
    offsetTicks2 = ticksReached2.value
    EvalError(pErrorCode1)
    EvalError(pErrorCode2)
    ret1 = epos.VCS_MoveWithVelocity(
        keyhandle, NodeID_1, direction1 * SPEED, pErrorCode1)
    ret2 = epos.VCS_MoveWithVelocity(
        keyhandle, NodeID_2, direction2 * SPEED, pErrorCode2)
    EvalError(pErrorCode1)
    EvalError(pErrorCode2)
    i = 0
    errorD = 0
    start = time.time()
    integral = 0
    motor1Blocked = False
    motor2Blocked = False
    while True:
        # Timed out, Target not reached within time
        if i >= iTimeOut:
            print('Target Reached Timed Out')
            epos.VCS_SetQuickStopState(
                keyhandle, NodeID_1, 0, byref(pErrorCode1))
            epos.VCS_SetQuickStopState(
                keyhandle, NodeID_2, 0, byref(pErrorCode2))
            EvalError(pErrorCode1)
            EvalError(pErrorCode2)
            return 0

        # Read Encoders
        if i % 2 == 0:
            ret1 = epos.VCS_GetPositionIs(
                keyhandle, NodeID_1, byref(ticksReached1), byref(pErrorCode1))
            ret2 = epos.VCS_GetPositionIs(
                keyhandle, NodeID_2, byref(ticksReached2), byref(pErrorCode2))
            epos.VCS_GetCurrentIs(keyhandle, NodeID_1,
                                  byref(pCurrentIs1), byref(pErrorCode1))
            epos.VCS_GetCurrentIs(keyhandle, NodeID_2,
                                  byref(pCurrentIs2), byref(pErrorCode2))
        else:
            ret2 = epos.VCS_GetPositionIs(
                keyhandle, NodeID_2, byref(ticksReached2), byref(pErrorCode2))
            ret1 = epos.VCS_GetPositionIs(
                keyhandle, NodeID_1, byref(ticksReached1), byref(pErrorCode1))
            epos.VCS_GetCurrentIs(keyhandle, NodeID_2,
                                  byref(pCurrentIs2), byref(pErrorCode2))
            epos.VCS_GetCurrentIs(keyhandle, NodeID_1,
                                  byref(pCurrentIs1), byref(pErrorCode1))
        EvalError(pErrorCode1)
        EvalError(pErrorCode2)

        if ret1 == 1 and ret2 == 1:
            cTicks2 = ticksReached2.value - offsetTicks2
            cTicks1 = ticksReached1.value - offsetTicks1
            # Target Reached
            if (direction2 < 0 and cTicks2 <= ticks2) or \
                (direction2 > 0 and cTicks2 >= ticks2) or \
                (direction1 < 0 and cTicks1 <= -ticks1) or \
                    (direction1 > 0 and cTicks1 >= -ticks1):
                print('Target Reached')
                epos.VCS_SetQuickStopState(
                    keyhandle, NodeID_2, 0, byref(pErrorCode2))
                epos.VCS_SetQuickStopState(
                    keyhandle, NodeID_1, 0, byref(pErrorCode1))
                print("Loop Time ", (time.time()-start) / i)
                ret1 = epos.VCS_GetPositionIs(
                    keyhandle, NodeID_1, byref(ticksReached1), byref(pErrorCode1))
                ret2 = epos.VCS_GetPositionIs(
                    keyhandle, NodeID_2, byref(ticksReached2), byref(pErrorCode2))
                print("Final Error1: ", ticksReached1.value - offsetTicks1)
                print("Final Error2: ", ticksReached2.value - offsetTicks2)
                print("Average Error ", errorD / i)
                EvalError(pErrorCode1)
                EvalError(pErrorCode2)
                return 1
            # Motors Blocked
            elif abs(pCurrentIs1.value) > BLOCKED or motor1Blocked or abs(pCurrentIs2.value) > BLOCKED or motor2Blocked:
                if abs(pCurrentIs1.value) > BLOCKED:
                    epos.VCS_SetQuickStopState(
                        keyhandle, NodeID_1, 0, byref(pErrorCode2))
                    motor1Blocked = True
                else:
                    epos.VCS_SetQuickStopState(
                        keyhandle, NodeID_2, 0, byref(pErrorCode2))
                    motor2Blocked = True
                if (motor1Blocked and motor2Blocked):
                    return 0
            # Target not Reached
            else:
                error = abs(ticksReached2.value - offsetTicks2) - \
                    abs(ticksReached1.value - offsetTicks1)
                integral = integral + error
                errorD += error
                correction = error * 0.05 + integral * 0.0001
                if i % 2 == 0:
                    epos.VCS_MoveWithVelocity(keyhandle, NodeID_2, int(
                        direction2 * (SPEED - correction)), byref(pErrorCode2))
                    epos.VCS_MoveWithVelocity(keyhandle, NodeID_1, int(
                        direction1 * (SPEED + correction)), byref(pErrorCode1))
                else:
                    epos.VCS_MoveWithVelocity(keyhandle, NodeID_1, int(
                        direction1 * (SPEED + correction)), byref(pErrorCode1))
                    epos.VCS_MoveWithVelocity(keyhandle, NodeID_2, int(
                        direction2 * (SPEED - correction)), byref(pErrorCode2))
                EvalError(pErrorCode2)
                EvalError(pErrorCode1)

            i += 1
            time.sleep(0.01)
        # Error in Commanding
        else:
            print('Error Command Drive Reached')
            return 0
