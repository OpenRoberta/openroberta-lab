import logging
import sys
import time
from ctypes import *

logging.basicConfig(stream=sys.stdout, level=logging.INFO)


cdll.LoadLibrary(PATH_TO_LIB)
epos = CDLL(PATH_TO_LIB)

NodeID_1 = 1
NodeID_2 = 2

ret = 0
pErrorCode = c_uint()
pDeviceErrorCode = c_uint()


def openDevice(DeviceName=b'EPOS4', ProtocolStackName=b'MAXON SERIAL V2', InterfaceName=b'USB', PortName=b'USB0', pErrorCode=pErrorCode):
    keyhandle = epos.VCS_OpenDevice(DeviceName, ProtocolStackName, InterfaceName, PortName, byref(pErrorCode))
    if keyhandle == 0:
        logging.info('Could not open Com-Port')
        logging.info('Error Openening Port: %#5.8x' % pErrorCode.value)
        raise ConnectionError('Keyhandle: %8d' % keyhandle)

    # Verify Error State of EPOS4
    epos.VCS_GetDeviceErrorCode(keyhandle, NodeID_1, 1, byref(pDeviceErrorCode), byref(pErrorCode))
    epos.VCS_GetDeviceErrorCode(keyhandle, NodeID_2, 1, byref(pDeviceErrorCode), byref(pErrorCode))
    logging.info('Device Error: %#5.8x' % pDeviceErrorCode.value)
    if pDeviceErrorCode.value != 0:
        raise Exception('Device Error: %#5.8x' % pDeviceErrorCode.value)
    return keyhandle


def closeDevice(keyhandle, pErrorCode):
    # Close Com-Port
    ret = epos.VCS_CloseDevice(keyhandle, byref(pErrorCode))
    logging.info('Error Code Closing Port: %#5.8x' % pErrorCode.value)


def activatePPM(keyhandle: int, node: int, pErrorCode):
    logging.info("Activating PPM")
    ret1 = epos.VCS_ActivateProfilePositionMode(keyhandle, node, byref(pErrorCode))
    EvalError(pErrorCode)
    return ret1 != 0


def setPositionProfile(keyhandle: int, node: int, velocity: float, acceleration: float, deceleration: float, pErrorCode: int):
    logging.info(f"Setting position profile velocity {velocity}, acceleration {acceleration} and deceleration {deceleration}")
    ret = epos.VCS_SetPositionProfile(keyhandle, node, velocity, acceleration, deceleration, byref(pErrorCode))
    EvalError(pErrorCode)
    return ret != 0


def setState(keyhandle: int, state: str):
    if state == 'enable':
        logging.info("Set Enable State")
        ret1 = epos.VCS_SetEnableState(keyhandle, NodeID_1, byref(pErrorCode))
        ret2 = epos.VCS_SetEnableState(keyhandle, NodeID_2, byref(pErrorCode))
    elif state == 'disable':
        logging.info("Set Disable State")
        ret1 = epos.VCS_SetDisableState(keyhandle, NodeID_1, byref(pErrorCode))
        ret2 = epos.VCS_SetDisableState(keyhandle, NodeID_2, byref(pErrorCode))
    elif state == 'quick_stop':
        logging.info("Set QuickStop State")
        ret1 = epos.VCS_SetQuickStopState(keyhandle, NodeID_1, byref(pErrorCode))
        ret2 = epos.VCS_SetQuickStopState(keyhandle, NodeID_2, byref(pErrorCode))

    return ret1 != 0 and ret2 != 0


def MoveToPosition(keyhandle: int, node: int, position: int, absolute: bool, immediately: bool):
    epos.VCS_MoveToPosition(keyhandle, node, position, absolute, immediately, byref(pErrorCode))
    # time.sleep(10)
    PositionReached(keyhandle, node, 10, pErrorCode)


def Drive(keyhandle: int, position1: int, position2: int, velocity1: int, velocity2: int):
    setPositionProfile(keyhandle, NodeID_1, velocity1, 2000, 5000, pErrorCode)
    EvalError(pErrorCode)
    setPositionProfile(keyhandle, NodeID_2, velocity2, 2000, 5000, pErrorCode)
    EvalError(pErrorCode)
    epos.VCS_MoveToPosition(keyhandle, NodeID_1, -position1, 0, 0, byref(pErrorCode))
    EvalError(pErrorCode)
    epos.VCS_MoveToPosition(keyhandle, NodeID_2, position2, 0, 0, byref(pErrorCode))
    EvalError(pErrorCode)
    DriveReached(keyhandle, 10, pErrorCode)

def haltPositionMovement(keyhandle: int, node: int, pErrorCode:int):
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


def DriveReached(keyhandle, iTimeOut, pErrorCode):
    pTargetReachedNode1 = c_bool()
    pTargetReachedNode2 = c_bool()
    i = 0

    while True:
        # Timed out, Target not reached within time
        if i >= iTimeOut:
            print('Target Reached Timed Out')
            return 0
            break

        # Get Target Reached Bit of Statusword 0x6064
        ret1 = epos.VCS_GetMovementState(keyhandle, NodeID_1, byref(pTargetReachedNode1), byref(pErrorCode))
        ret2 = epos.VCS_GetMovementState(keyhandle, NodeID_2, byref(pTargetReachedNode2), byref(pErrorCode))
        if ret1 == 1 and ret2 == 1:
            # Check Error Code of Command
            ret = EvalError(pErrorCode)
            if ret == 1:
                if pTargetReachedNode1.value == 1 and pTargetReachedNode2.value == 1:
                    print('Target Reached')
                    return 1
                    break

                # Target not Reached
                else:
                    i += 1
                    time.sleep(1)

            # Error in Commanding
            else:
                print('Error Commanding')
                return 0
                break

        # Command not executed
        else:
            print('Error Command GetMovementState')
            return 0
            break


def PositionReached(keyhandle, NodeID, iTimeOut, pErrorCode):
    pTargetReached = c_bool()
    i = 0

    while True:
        # Timed out, Target not reached within time
        if i >= iTimeOut:
            print('Target Reached Timed Out')
            return 0
            break

        # Get Target Reached Bit of Statusword 0x6064
        ret = epos.VCS_GetMovementState(keyhandle, NodeID, byref(pTargetReached), byref(pErrorCode))
        if ret == 1:
            # Check Error Code of Command
            ret = EvalError(pErrorCode)
            if ret == 1:
                if pTargetReached.value == 1:
                    print('Target Reached')
                    return 1
                    break

                # Target not Reached
                else:
                    i += 1
                    time.sleep(1)

            # Error in Commanding
            else:
                print('Error Commanding')
                return 0
                break

        # Command not executed
        else:
            print('Error Command GetMovementState')
            return 0
            break
