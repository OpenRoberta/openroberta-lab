package lejos.hardware.sensor;

/**
 * Basic constants for use when accessing EV3 Sensors.
 * @author andy
 *
 */
public interface EV3SensorConstants
{
    public static final int PORTS = 4;
    public static final int MOTORS = 4;
    
    public static final int CONN_UNKNOWN    = 111;  //!< Connection is fake (test)
    public static final int CONN_DAISYCHAIN = 117;  //!< Connection is daisy chained
    public static final int CONN_NXT_COLOR  = 118;  //!< Connection type is NXT color sensor
    public static final int CONN_NXT_DUMB   = 119;  //!< Connection type is NXT analog sensor
    public static final int CONN_NXT_IIC    = 120;  //!< Connection type is NXT IIC sensor
    public static final int CONN_INPUT_DUMB = 121;  //!< Connection type is LMS2012 input device with ID resistor
    public static final int CONN_INPUT_UART = 122;  //!< Connection type is LMS2012 UART sensor
    public static final int CONN_OUTPUT_DUMB= 123;  //!< Connection type is LMS2012 output device with ID resistor^
    public static final int CONN_OUTPUT_INTELLIGENT= 124;  //!< Connection type is LMS2012 output device with communication
    public static final int CONN_OUTPUT_TACHO= 125;  //!< Connection type is LMS2012 tacho motor with series ID resistance
    public static final int CONN_NONE       = 126;  //!< Port empty or not available
    public static final int CONN_ERROR      = 127;  //!< Port not empty and type is invalid^M

    public static final int TYPE_NXT_TOUCH                =   1;  //!< Device is NXT touch sensor
    public static final int TYPE_NXT_LIGHT                =   2;  //!< Device is NXT light sensor
    public static final int TYPE_NXT_SOUND                =   3;  //!< Device is NXT sound sensor
    public static final int TYPE_NXT_COLOR                =   4;  //!< Device is NXT color sensor
    public static final int TYPE_TACHO                    =   7;  //!< Device is a tacho motor
    public static final int TYPE_MINITACHO                =   8;  //!< Device is a mini tacho motor
    public static final int TYPE_NEWTACHO                 =   9;  //!< Device is a new tacho motor
    public static final int TYPE_THIRD_PARTY_START        =  50;
    public static final int TYPE_THIRD_PARTY_END          =  99;
    public static final int TYPE_IIC_UNKNOWN              = 100;
    public static final int TYPE_NXT_TEST                 = 101;  //!< Device is a NXT ADC test sensor
    public static final int TYPE_NXT_IIC                  = 123;  //!< Device is NXT IIC sensor
    public static final int TYPE_TERMINAL                 = 124;  //!< Port is connected to a terminal
    public static final int TYPE_UNKNOWN                  = 125;  //!< Port not empty but type has not been determined
    public static final int TYPE_NONE                     = 126;  //!< Port empty or not available
    public static final int TYPE_ERROR                    = 127;  //!< Port not empty and type is invalid


    public static final int UART_MAX_MODES = 8;
    public static final int MAX_DEVICE_DATALENGTH = 32;
    public static final int IIC_DATA_LENGTH = MAX_DEVICE_DATALENGTH;
    public static final int STATUS_OK = 0;
    public static final int STATUS_BUSY = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_STOP = 4;
    
    public static final byte CMD_NONE = (byte)'-';
    public static final byte CMD_FLOAT = (byte)'f';
    public static final byte CMD_SET = (byte)'0';
    public static final byte CMD_COL_COL = 0xd;
    public static final byte CMD_COL_RED = 0xe;
    public static final byte CMD_COL_GRN = 0xf;
    public static final byte CMD_COL_BLU = 0x11;
    public static final byte CMD_COL_AMB = 0x12;
    public static final byte CMD_PIN1 = 0x1;
    public static final byte CMD_PIN5 = 0x2;
    
    public static final float ADC_REF = 5.0f; // 5.0 Volts
    public static final int ADC_RES = 4095;
}
