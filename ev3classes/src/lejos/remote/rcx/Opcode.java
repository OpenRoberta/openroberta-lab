package lejos.remote.rcx;

/**
 * Opcode constants.
 * 
 */
public interface Opcode
{
  public static final byte OPCODE_MASK = (byte)0xf7;

  public static final byte OPCODE_ALIVE = (byte)0x10;
  public static final byte OPCODE_GET_VALUE = (byte)0x12;
  public static final byte OPCODE_SET_MOTOR_POWER = (byte)0x13;
  public static final byte OPCODE_SET_VARIABLE = (byte)0x14;
  public static final byte OPCODE_GET_VERSIONS = (byte)0x15;
  public static final byte OPCODE_SET_MOTOR_DIRECTION_REPLY = (byte)0x16;
  public static final byte OPCODE_CALL_SUBROUTINE = (byte)0x17;

  public static final byte OPCODE_GET_MEMORY_MAP = (byte)0x20;
  public static final byte OPCODE_SET_MOTOR_ON_OFF = (byte)0x21;
  public static final byte OPCODE_SET_TIME = (byte)0x22;
  public static final byte OPCODE_PLAY_TONE = (byte)0x23;
  public static final byte OPCODE_ADD_TO_VARIABLE = (byte)0x24;
  public static final byte OPCODE_START_TASK_DOWNLOAD = (byte)0x25;
  public static final byte OPCODE_CLEAR_SENSOR_VALUE_REPLY = (byte)0x26;
  public static final byte OPCODE_BRANCH_ALWAYS_NEAR = (byte)0x27;

  public static final byte OPCODE_GET_BATTERY_POWER = (byte)0x30;
  public static final byte OPCODE_SET_TRANSMITTER_RANGE = (byte)0x31;
  public static final byte OPCODE_SET_SENSOR_TYPE = (byte)0x32;
  public static final byte OPCODE_SET_DISPLAY = (byte)0x33;
  public static final byte OPCODE_SUBTRACT_FROM_VARIABLE = (byte)0x34;
  public static final byte OPCODE_START_SUBROUTINE_DOWNLOAD = (byte)0x35;
  public static final byte OPCODE_DELETE_SUBROUTINE_REPLY = (byte)0x36;
  public static final byte OPCODE_DECREMENT_LOOP_COUNTER_NEAR = (byte)0x37;

  public static final byte OPCODE_DELETE_ALL_TASKS = (byte)0x40;
  public static final byte OPCODE_SET_SENSOR_MODE = (byte)0x42;
  public static final byte OPCODE_WAIT = (byte)0x43;
  public static final byte OPCODE_DIVIDE_VARIABLE = (byte)0x44;
  public static final byte OPCODE_TRANSFER_DATA = (byte)0x45;
  public static final byte OPCODE_SET_POWER_DOWN_DELAY_REPLY = (byte)0x46;

  public static final byte OPCODE_STOP_ALL_TASKS = (byte)0x50;
  public static final byte OPCODE_SET_DATALOG_SIZE = (byte)0x52;
  public static final byte OPCODE_UNLOCK_FIRMWARE_REPLY = (byte)0x52;
  public static final byte OPCODE_UPLOAD_DATALOG_REPLY = (byte)0x53;
  public static final byte OPCODE_MULTIPLY_VARIABLE = (byte)0x54;
  public static final byte OPCODE_CLEAR_TIMER_REPLY = (byte)0x56;

  public static final byte OPCODE_POWER_OFF = (byte)0x60;
  public static final byte OPCODE_DELETE_TASK = (byte)0x61;
  public static final byte OPCODE_DATALOG_NEXT = (byte)0x62;
  public static final byte OPCODE_OR_VARIABLE_REPLY = (byte)0x63;
  public static final byte OPCODE_SIGN_VARIABLE = (byte)0x64;
  public static final byte OPCODE_DELETE_FIRMWARE = (byte)0x65;
  public static final byte OPCODE_SET_PROGRAM_NUMBER_REPLY = (byte)0x66;

  public static final byte OPCODE_DELETE_ALL_SUBROUTINES = (byte)0x70;
  public static final byte OPCODE_START_TASK = (byte)0x71;
  public static final byte OPCODE_BRANCH_ALWAYS_FAR = (byte)0x72;
  public static final byte OPCODE_AND_VARIABLE_REPLY = (byte)0x73;
  public static final byte OPCODE_ABSOLUTE_VALUE = (byte)0x74;
  public static final byte OPCODE_START_FIRMWARE_DOWNLOAD = (byte)0x75;
  public static final byte OPCODE_STOP_TASK_REPLY = (byte)0x76;

  public static final byte OPCODE_STOP_TASK = (byte)0x81;
  public static final byte OPCODE_START_FIRMWARE_DOWNLOAD_REPLY = (byte)0x82;
  public static final byte OPCODE_SET_LOOP_COUNTER = (byte)0x82;
  public static final byte OPCODE_ABSOLUTE_VALUE_REPLY = (byte)0x83;
  public static final byte OPCODE_AND_VARIABLE = (byte)0x84;
  public static final byte OPCODE_TEST_AND_BRANCH_NEAR = (byte)0x85;
  public static final byte OPCODE_START_TASK_REPLY = (byte)0x86;
  public static final byte OPCODE_DELETE_ALL_SUBROUTINES_REPLY = (byte)0x87;

  public static final byte OPCODE_CLEAR_MESSAGE = (byte)0x90;
  public static final byte OPCODE_SET_PROGRAM_NUMBER = (byte)0x91;
  public static final byte OPCODE_DELETE_FIRMWARE_REPLY = (byte)0x92;
  public static final byte OPCODE_DECREMENT_LOOP_COUNTER_FAR = (byte)0x92;
  public static final byte OPCODE_SIGN_VARIABLE_REPLY = (byte)0x93;
  public static final byte OPCODE_OR_VARIABLE = (byte)0x94;
  public static final byte OPCODE_DATALOG_NEXT_REPLY = (byte)0x95;
  public static final byte OPCODE_TEST_AND_BRANCH_FAR = (byte)0x95;
  public static final byte OPCODE_DELETE_TASK_REPLY = (byte)0x96;
  public static final byte OPCODE_POWER_OFF_REPLY = (byte)0x97;

  public static final byte OPCODE_CLEAR_TIMER = (byte)0xa1;
  public static final byte OPCODE_MULTIPLY_VARIABLE_REPLY = (byte)0xa3;
  public static final byte OPCODE_UPLOAD_DATALOG = (byte)0xa4;
  public static final byte OPCODE_UNLOCK_FIRMWARE = (byte)0xa5;
  public static final byte OPCODE_SET_DATALOG_SIZE_REPLY = (byte)0xa5;
  public static final byte OPCODE_PLAY_SOUND_REPLY = (byte)0xa6;
  public static final byte OPCODE_STOP_ALL_TASKS_REPLY = (byte)0xa7;

  public static final byte OPCODE_SET_POWER_DOWN_DELAY = (byte)0xb1;
  public static final byte OPCODE_TRANSFER_DATA_REPLY = (byte)0xb2;
  public static final byte OPCODE_SEND_MESSAGE = (byte)0xb2;
  public static final byte OPCODE_DIVIDE_VARIABLE_REPLY = (byte)0xb3;
  public static final byte OPCODE_SET_SENSOR_MODE_REPLY = (byte)0xb5;
  public static final byte OPCODE_DELETE_ALL_TASKS_REPLY = (byte)0xb7;

  public static final byte OPCODE_DELETE_SUBROUTINE = (byte)0xc1;
  public static final byte OPCODE_START_SUBROUTINE_DOWNLOAD_REPLY = (byte)0xc2;
  public static final byte OPCODE_SUBTRACT_FROM_VARIABLE_REPLY = (byte)0xc3;
  public static final byte OPCODE_SET_DISPLAY_REPLY = (byte)0xc4;
  public static final byte OPCODE_SET_SENSOR_TYPE_REPLY = (byte)0xc5;
  public static final byte OPCODE_SET_TRANSMITTER_RANGE_REPLY = (byte)0xc6;
  public static final byte OPCODE_GET_BATTERY_POWER_REPLY = (byte)0xc7;

  public static final byte OPCODE_CLEAR_SENSOR_VALUE = (byte)0xd1;
  public static final byte OPCODE_START_TASK_DOWNLOAD_REPLY = (byte)0xd2;
  public static final byte OPCODE_REMOTE_COMMAND = (byte)0xd2;
  public static final byte OPCODE_ADD_TO_VARIABLE_REPLY = (byte)0xd3;
  public static final byte OPCODE_PLAY_TONE_REPLY = (byte)0xd4;
  public static final byte OPCODE_SET_TIME_REPLY = (byte)0xd5;
  public static final byte OPCODE_SET_MOTOR_ON_OFF_REPLY = (byte)0xd6;
  public static final byte OPCODE_GET_MEMORY_MAP_REPLY = (byte)0xd7;

  public static final byte OPCODE_SET_MOTOR_DIRECTION = (byte)0xe1;
  public static final byte OPCODE_GET_VERSIONS_REPLY = (byte)0xe2;
  public static final byte OPCODE_SET_VARIABLE_REPLY = (byte)0xe3;
  public static final byte OPCODE_SET_MOTOR_POWER_REPLY = (byte)0xe4;
  public static final byte OPCODE_GET_VALUE_REPLY = (byte)0xe5;
  public static final byte OPCODE_ALIVE_REPLY = (byte)0xe7;

  public static final byte OPCODE_SET_MESSAGE = (byte)0xf7;
}


