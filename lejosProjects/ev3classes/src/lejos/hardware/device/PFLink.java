package lejos.hardware.device; 

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;

/** 
 * Class for controlling PF Motors with MindSensors NRLink-Nx 
 * 
 * @author Alexander Vegh <alex@vegh.ch> 
 * 
 */ 
public class PFLink extends I2CSensor { 

    public final static byte NR_RANGE_SHORT = 0x53; 
    public final static byte NR_RANGE_LONG = 0x4c; 

    private static final byte NR_ID = 0X02; 
    private static final byte NR_COMMAND = 0X41; 
    private final static byte NR_RUN = 0X52; 
    private final static byte NR_SPEED_SLOW = 0X44; 
    private final static byte NR_FLUSH = 0X46; 
    private static final byte NR_SELECT_PF = 0X50; 


    public static final int MOTOR_CH1_A_FLOAT   = 0x50; 
    public static final int MOTOR_CH1_A_FORWARD = 0x53; 
    public static final int MOTOR_CH1_A_REVERSE = 0x56; 
    public static final int MOTOR_CH1_A_BRAKE   = 0x59; 

    public static final int MOTOR_CH1_B_FLOAT   = 0x5C; 
    public static final int MOTOR_CH1_B_FORWARD = 0x5F; 
    public static final int MOTOR_CH1_B_REVERSE = 0x62; 
    public static final int MOTOR_CH1_B_BRAKE   = 0x65; 

    public static final int MOTOR_CH2_A_FLOAT   = 0x68; 
    public static final int MOTOR_CH2_A_FORWARD = 0x6B; 
    public static final int MOTOR_CH2_A_REVERSE = 0x6E; 
    public static final int MOTOR_CH2_A_BRAKE   = 0x71; 

    public static final int MOTOR_CH2_B_FLOAT   = 0x74; 
    public static final int MOTOR_CH2_B_FORWARD = 0x77; 
    public static final int MOTOR_CH2_B_REVERSE = 0x7A; 
    public static final int MOTOR_CH2_B_BRAKE   = 0x7D; 

    public static final int MOTOR_CH3_A_FLOAT   = 0x80; 
    public static final int MOTOR_CH3_A_FORWARD = 0x83; 
    public static final int MOTOR_CH3_A_REVERSE = 0x86; 
    public static final int MOTOR_CH3_A_BRAKE   = 0x89; 

    public static final int MOTOR_CH3_B_FLOAT   = 0x8C; 
    public static final int MOTOR_CH3_B_FORWARD = 0x8F; 
    public static final int MOTOR_CH3_B_REVERSE = 0x92; 
    public static final int MOTOR_CH3_B_BRAKE   = 0x95; 

    public static final int MOTOR_CH4_A_FLOAT   = 0x98; 
    public static final int MOTOR_CH4_A_FORWARD = 0x9B; 
    public static final int MOTOR_CH4_A_REVERSE = 0x9E; 
    public static final int MOTOR_CH4_A_BRAKE   = 0xA1; 

    public static final int MOTOR_CH4_B_FLOAT   = 0xA4; 
    public static final int MOTOR_CH4_B_FORWARD = 0xA7; 
    public static final int MOTOR_CH4_B_REVERSE = 0xAA; 
    public static final int MOTOR_CH4_B_BRAKE   = 0xAD; 

    public static final int COMBO_CH1_A_FORWARD_B_FORWARD = 0XB0; 
    public static final int COMBO_CH1_A_FORWARD_B_REVERSE = 0XB3; 
    public static final int COMBO_CH1_A_REVERSE_B_FORWARD = 0XB6; 
    public static final int COMBO_CH1_A_REVERSE_B_REVERSE = 0XB9; 

    public static final int COMBO_CH2_A_FORWARD_B_FORWARD = 0XBC; 
    public static final int COMBO_CH2_A_FORWARD_B_REVERSE = 0XBF; 
    public static final int COMBO_CH2_A_REVERSE_B_FORWARD = 0XC2; 
    public static final int COMBO_CH2_A_REVERSE_B_REVERSE = 0XC5; 

    public static final int COMBO_CH3_A_FORWARD_B_FORWARD = 0XC8; 
    public static final int COMBO_CH3_A_FORWARD_B_REVERSE = 0XCB; 
    public static final int COMBO_CH3_A_REVERSE_B_FORWARD = 0XCE; 
    public static final int COMBO_CH3_A_REVERSE_B_REVERSE = 0XD1; 

    public static final int COMBO_CH4_A_FORWARD_B_FORWARD = 0XD4; 
    public static final int COMBO_CH4_A_FORWARD_B_REVERSE = 0XD7; 
    public static final int COMBO_CH4_A_REVERSE_B_FORWARD = 0XDA; 
    public static final int COMBO_CH4_A_REVERSE_B_REVERSE = 0XDD; 


    private static final int[] COMMANDS = new int[]{ 

            0x50, 0x01, 0x00, //Motor Ch1 A Float 
            0x53, 0x01, 0x10, //Motor Ch1 A Forward 
            0x56, 0x01, 0x20, //Motor Ch1 A Reversed 
            0x59, 0x01, 0x30, //Motor Ch1 A Break 

            0x5C, 0x01, 0x00, //Motor Ch1 B Float 
            0x5F, 0x01, 0x40, //Motor Ch1 B Forward 
            0x62, 0x01, 0x80, //Motor Ch1 B Reversed 
            0x65, 0x01, 0xc0, //Motor Ch1 B Break 

            0x68, 0x11, 0x00, //Motor Ch2 A Float 
            0x6B, 0x11, 0x10, //Motor Ch2 A Forward 
            0x6E, 0x11, 0x20, //Motor Ch2 A Reversed 
            0x71, 0x11, 0x30, //Motor Ch2 A Break 

            0x74, 0x11, 0x00, //Motor Ch2 B Float 
            0x77, 0x11, 0x40, //Motor Ch2 B Forward 
            0x7A, 0x11, 0x80,  //Motor Ch2 B Reversed 
            0x7D, 0x11, 0xc0, //Motor Ch2 B Break 

            0x80, 0x21, 0x00, //Motor Ch3 A Float 
            0x83, 0x21, 0x10, //Motor Ch3 A Forward 
            0x86, 0x21, 0x20, //Motor Ch3 A Reversed 
            0x89, 0x21, 0x30, //Motor Ch3 A Break 

            0x8C, 0x21, 0x00, //Motor Ch3 B Float 
            0x8F, 0x21, 0x40, //Motor Ch3 B Forward 
            0x92, 0x21, 0x80, //Motor Ch3 B Reversed 
            0x95, 0x21, 0xc0, //Motor Ch3 B Break 

            0x98, 0x31, 0x00, //Motor Ch4 A Float 
            0x9B, 0x31, 0x10, //Motor Ch4 A Forward 
            0x9E, 0x31, 0x20, //Motor Ch4 A Reversed 
            0xA1, 0x31, 0x30, //Motor Ch4 A Break 

            0xA4, 0x31, 0x00, //Motor Ch4 B Float 
            0xA7, 0x31, 0x40, //Motor Ch4 B Forward 
            0xAA, 0x31, 0x80, //Motor Ch4 B Reversed 
            0xAD, 0x31, 0xC0  //Motor Ch4 B Break 
    }; 

    private static final int[] COMBOS = new int[]{ 
            0xB0, 0x01, 0x50, // Motor Ch1 A Forw B Forw 
            0xB3, 0x01, 0x90, // Motor Ch1 A Forw B Rev 
            0xB6, 0x01, 0x60, // Motor Ch1 A Rev B Forw 
            0xB9, 0x01, 0xa0, // Motor Ch1 A Rev B Rev 
            0xBC, 0x11, 0x50, // Motor Ch2 A Forw B Forw 
            0xBF, 0x11, 0x90, // Motor Ch2 A Forw B Rev 
            0xC2, 0x11, 0x60, // Motor Ch2 A Rev B Forw 
            0xC5, 0x11, 0xa0, // Motor Ch2 A Rev B Rev 
            0xC8, 0x21, 0x50, // Motor Ch3 A Forw B Forw 
            0xCB, 0x21, 0x90, // Motor Ch3 A Forw B Rev 
            0xCE, 0x21, 0x60, // Motor Ch3 A Rev B Forw 
            0xD1, 0x21, 0xa0, // Motor Ch3 A Rev B Rev 
            0xD4, 0x31, 0x50, // Motor Ch4 A Forw B Forw 
            0xD7, 0x31, 0x90, // Motor Ch4 A Forw B Rev 
            0xDA, 0x31, 0x60, // Motor Ch4 A Rev B Forw 
            0xDD, 0x31, 0xa0  // Motor Ch4 A Rev B Rev 
    }; 

    public PFLink(I2CPort _Port, int address) {
        super(_Port, address);
    }

    public PFLink(I2CPort _Port) {
        super(_Port);
    }

    public PFLink(Port _Port, int address) {
        super(_Port, address, TYPE_LOWSPEED);
    }

    public PFLink(Port _Port) {
        super(_Port);
    }

    /** 
     * Should be called once to set up the NRLink for usage in the program 
     * 
     * @param _Range Either NR_RANGE_SHORT or NR_RANGE_LONG, which uses more power 
     */ 
    public void initialize(byte _Range) { 
        runCommand(NR_FLUSH); 
        runCommand(NR_SPEED_SLOW); 
        runCommand(NR_SELECT_PF); 
        runCommand(_Range); 
    } 

    /** 
     * Executes a command 
     * 
     * @param command 
     */ 
    public void runCommand(int command) { 
        byte[] buf = new byte[2]; 

        buf[0] = NR_ID; 
        buf[1] = (byte) (command); 
        sendData(NR_COMMAND, buf, 2); 
    } 

    /** 
     * Installs a macro into the NRLink 
     * 
     * You really should call this only once for a new NRLink since 
     * it stores them in EEPROM even if turned of. And I have no idea how 
     * often you can overwrite them before they burn out... 
     * 
     *  
     * @param _Address  The address up from 0x40 for the macro 
     * @param _Macro    The macro 
     */ 
    public void installMacro(int _Address, byte[] _Macro) { 
        sendData((byte) _Address, (byte) _Macro.length);
        Delay.msDelay(10);
        sendData((byte) _Address + 1, _Macro, _Macro.length); 

    } 


    /** 
     * Installs the macro definitions used by this class. 
     * 
     * You really should call this only once for a new NRLink since 
     * it stores them in EEPROM even if turned of. And I have no idea how 
     * often you can overwrite them before they burn out... 
     */ 
    public void installDefaultMacros() { 
        byte[] buffer = new byte[2]; 

        for (int i = 0; i < COMMANDS.length; i += 3) { 
            buffer[0] = (byte) (COMMANDS[i + 1] & 0xff); 
            buffer[1] = (byte) (COMMANDS[i + 2] & 0xff); 
            installMacro(COMMANDS[i], buffer); 
        } 

        for (int i = 0; i < COMBOS.length; i += 3) { 
            buffer[0] = (byte) (COMBOS[i + 1] & 0xff); 
            buffer[1] = (byte) (COMBOS[i + 2] & 0xff); 
            installMacro(COMBOS[i], buffer); 
        } 
    } 

    /** 
     * Runs a macro which has been previously installed on the NRLink. 
     * 
     * 
     * @param _Address The address of the macro, one of the MOTOR_* for a single motor 
     *                 or one of the COMBO_* macros which work on both motors simultaneously 
     */ 
    public void runMacro(int _Address) { 
        byte[] buf = new byte[2]; 
        buf[0] = NR_RUN; 
        buf[1] = (byte) _Address; 
        sendData(NR_COMMAND, buf, 2); 
    } 
}
