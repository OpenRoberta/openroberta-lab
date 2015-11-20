package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;

import com.sun.net.httpserver.HttpServer;

import lejos.hardware.Battery;
import lejos.hardware.Bluetooth;
import lejos.hardware.BluetoothException;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.RemoteBTDevice;
import lejos.hardware.Sound;
import lejos.hardware.Sounds;
import lejos.hardware.Wifi;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.CommonLCD;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.Image;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.MindsensorsGlideWheelMRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.PortException;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.BaseSensor;
import lejos.internal.ev3.EV3IOPort;
import lejos.internal.io.NativeHCI.LocalVersion;
import lejos.internal.io.Settings;
import lejos.internal.io.SystemSettings;
import lejos.remote.ev3.EV3Reply;
import lejos.remote.ev3.EV3Request;
import lejos.remote.ev3.Menu;
import lejos.remote.ev3.MenuReply;
import lejos.remote.ev3.MenuRequest;
import lejos.remote.ev3.RMIRemoteEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class GraphicStartup implements Menu {
    private static final int REMOTE_MENU_PORT = 8002;

    private static final String JAVA_RUN_CP = "jrun -cp ";
    private static final String JAVA_DEBUG_CP = "jrun -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y -cp ";

    private static final int TYPE_PROGRAM = 0;
    private static final int TYPE_SAMPLE = 1;
    private static final int TYPE_TOOL = 2;

    private static final String defaultProgramProperty = "lejos.default_program";
    private static final String defaultProgramAutoRunProperty = "lejos.default_autoRun";
    private static final String sleepTimeProperty = "lejos.sleep_time";
    private static final String pinProperty = "lejos.bluetooth_pin";
    private static final String ntpProperty = "lejos.ntp_host";

    private static final String ICMProgram =
        "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00ff\u00cf\u00c3\u0003\u00ff\u00cf\u00c3\u0003\u0000\u0000\u00c3\u0003\u0000\u0000\u00c3\u0003\u00fc\u00f3\u00c0\u0003\u00fc\u00f3\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00c3\u00ff\u003f\u00c0\u00c3\u00ff\u003f\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    private static final String ICMSound =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00cc\u00c0\u0030\u0000\u00cc\u00c0\u0030\u0000\u00c3\u000c\u0033\u0000\u00c3\u000c\u0033\u00fc\u00c3\u0030\u0033\u00fc\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u00cc\u00cf\u0030\u0033\u00cc\u00cf\u0030\u0033\u00fc\u00f3\u0030\u0033\u00fc\u00f3\u0030\u0033\u0000\u00cf\u000c\u0033\u0000\u00cf\u000c\u0033\u0000\u00fc\u00c0\u0030\u0000\u00fc\u00c0\u0030\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICMFile =
        "\u0000\u00c0\u0000\u0000\u0000\u00c0\u0000\u0000\u0000\u0030\u00ff\u000f\u0000\u0030\u00ff\u000f\u0000\u000c\u000c\u0030\u0000\u000c\u000c\u0030\u00fc\u0003\u0030\u00cc\u00fc\u0003\u0030\u00cc\u00c3\u0000\u00c0\u00f0\u00c3\u0000\u00c0\u00f0\u003f\u0000\u0000\u00c3\u003f\u0000\u0000\u00c3\u00ff\u003f\u0000\u00fc\u00ff\u003f\u0000\u00fc\u0003\u00c0\u0000\u00f0\u0003\u00c0\u0000\u00f0\u0003\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";

    private static final String ICDefault =
        "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u00f3\u00cf\u00cf\u00c3\u00f3\u00cf\u00cf\u00c3\u00c3\u000f\u0000\u00c3\u00c3\u000f\u0000\u00c3\u00f3\u00cf\u00f3\u00c0\u00f3\u00cf\u00f3\u00c0\u00f3\u000c\u0000\u00c0\u00f3\u000c\u0000\u00c0\u00f3\u00c3\u003f\u00c0\u00f3\u00c3\u003f\u00c0\u00c3\u0003\u0000\u00c0\u00c3\u0003\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    private static final String ICProgram =
        "\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0003\u00c0\u0003\u0000\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u000c\u00c0\u0003\u00c0\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u000c\u00c0\u0003\u0030\u0003\u00c0\u0003\u0030\u0003\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00c0\u0000\u00c0\u0003\u00ff\u00cf\u00c3\u0003\u00ff\u00cf\u00c3\u0003\u0000\u0000\u00c3\u0003\u0000\u0000\u00c3\u0003\u00fc\u00f3\u00c0\u0003\u00fc\u00f3\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00c3\u00ff\u003f\u00c0\u00c3\u00ff\u003f\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u0000\u00c0\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    private static final String ICFiles =
        "\u0000\u00c0\u0000\u0000\u0000\u00c0\u0000\u0000\u0000\u0030\u00ff\u000f\u0000\u0030\u00ff\u000f\u0000\u000c\u000c\u0030\u0000\u000c\u000c\u0030\u00fc\u0003\u0030\u00cc\u00fc\u0003\u0030\u00cc\u00c3\u0000\u00c0\u00f0\u00c3\u0000\u00c0\u00f0\u003f\u0000\u0000\u00c3\u003f\u0000\u0000\u00c3\u00ff\u003f\u0000\u00fc\u00ff\u003f\u0000\u00fc\u0003\u00c0\u0000\u00f0\u0003\u00c0\u0000\u00f0\u0003\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0083\u0007\u0000\u00f3\u0083\u0008\u0000\u00f3\u0083\u0008\u00c0\u00cc\u0083\u0008\u00c0\u00cc\u0083\u0007\u0000\u00f3\u0083\u0000\u0000\u00f3\u0083\u0000\u0000\u00cc\u0083\u0000\u0000\u00cc\u0083\u0000\u0000\u00f3\u0083\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    private static final String ICSamples =
        "\u0000\u00c0\u0000\u0000\u0000\u00c0\u0000\u0000\u0000\u0030\u00ff\u000f\u0000\u0030\u00ff\u000f\u0000\u000c\u000c\u0030\u0000\u000c\u000c\u0030\u00fc\u0003\u0030\u00cc\u00fc\u0003\u0030\u00cc\u00c3\u0000\u00c0\u00f0\u00c3\u0000\u00c0\u00f0\u003f\u0000\u0000\u00c3\u003f\u0000\u0000\u00c3\u00ff\u003f\u0000\u00fc\u00ff\u003f\u0000\u00fc\u0003\u00c0\u0000\u00f0\u0003\u00c0\u0000\u00f0\u0003\u0000\u00ff\u00ff\u0003\u001e\u00ff\u00ff\u0003\u0001\u0000\u00f3\u0003\u0001\u0000\u00f3\u0003\u0001\u00c0\u00cc\u0003\u001e\u00c0\u00cc\u0003\u0010\u0000\u00f3\u0003\u0010\u0000\u00f3\u0003\u0010\u0000\u00cc\u0003\u000f\u0000\u00cc\u0003\u0000\u0000\u00f3\u0003\u0000\u0000\u00f3\u0003\u0000\u00c0\u00cc\u0003\u0000\u00c0\u00cc\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f";
    private static final String ICTools =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00f0\u000f\u0000\u0000\u00f8\u001f\u0000\u0000\u001c\u0038\u0000\u0000\u000c\u0030\u0000\u0000\u000c\u0030\u0000\u0000\u0000\u0000\u0000\u00f0\u00ff\u00ff\u000f\u00fc\u00ff\u00ff\u003f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u007e\u00f8\u001f\u007e\u0000\u0000\u0000\u0000\u007e\u00f8\u001f\u007e\u007e\u00f8\u001f\u007e\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fe\u00ff\u00ff\u007f\u00fc\u00ff\u00ff\u003f\u00f0\u00ff\u00ff\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICBlue =
        "\u0000\u00f0\u000f\u0000\u0000\u00f0\u000f\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00f0\u003c\u00f0\u000f\u00f0\u003c\u00f0\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0003\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u0030\u00c3\u000f\u00f0\u003c\u00f0\u000f\u00f0\u003c\u00f0\u000f\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00ff\u0003\u00c0\u003f\u00ff\u0003\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00f0\u000f\u0000\u0000\u00f0\u000f\u0000";
    private static final String ICWifi =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00f8\u001f\u0000\u0000\u00ff\u00ff\u0000\u00c0\u00ff\u00ff\u0003\u00f0\u00ff\u00ff\u000f\u00f8\u003f\u00fc\u001f\u00fe\u0003\u00c0\u007f\u00ff\u0000\u0000\u00ff\u003f\u0000\u0000\u00fc\u001f\u0000\u0000\u00f8\u000e\u00f8\u001f\u0070\u0000\u00fe\u007f\u0000\u0000\u00ff\u00ff\u0000\u0080\u00ff\u00ff\u0001\u00c0\u003f\u00fc\u0003\u00c0\u0007\u00e0\u0003\u00c0\u0003\u00c0\u0001\u0000\u0000\u0000\u0000\u0000\u00c0\u0003\u0000\u0000\u00e0\u0007\u0000\u0000\u00e0\u0007\u0000\u0000\u00e0\u0007\u0000\u0000\u00e0\u0007\u0000\u0000\u00c0\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICPAN =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u000c\u0038\u0000\u0000\u000c\u0038\u0000\u0000\u000c\u0038\u0000\u0000\u000c\u0038\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00f8\u001f\u0000\u0000\u00c0\u0001\u0000\u0000\u00c0\u0001\u0000\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0070\u0000\u0000\u000e\u0070\u0000\u0000\u000e\u00ff\u0007\u00e0\u00ff\u00ff\u000f\u00f0\u00ff\u0003\u000e\u0070\u00c0\u0003\u000e\u0070\u00c0\u0003\u000e\u0070\u00c0\u0003\u000e\u0070\u00c0\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u0007\u00e0\u00ff\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICAccessPoint =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0001\u0080\u0003\u00e0\u0001\u0080\u0007\u00f0\u0001\u0080\u000f\u00f0\u000c\u0030\u000f\u0078\u001e\u0078\u001e\u0078\u001f\u00f8\u001e\u0078\u00cf\u00f3\u001e\u0038\u00ef\u00f7\u001c\u0038\u00e7\u00e7\u001c\u0038\u00e7\u00e7\u001c\u0038\u00ef\u00f7\u001c\u0078\u00cf\u00f3\u001e\u0078\u001f\u00f8\u001e\u00f8\u001e\u0078\u001f\u00f0\u000c\u0030\u000f\u00f0\u0001\u0080\u000f\u00e0\u0003\u00c0\u0007\u00c0\u0001\u0080\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICAccessPointPlus =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0001\u0080\u0003\u00e0\u0001\u0080\u0007\u00f0\u0001\u0080\u000f\u00f0\u000c\u0030\u000f\u0070\u001e\u0078\u000e\u0078\u001e\u0078\u001e\u0078\u00cf\u00f3\u001e\u0038\u00ef\u00f7\u001c\u0038\u00e7\u00e7\u001c\u0038\u00e7\u00e7\u001c\u0038\u00ef\u0077\u001c\u0078\u00cf\u00e3\u0017\u0078\u001e\u00f0\u000f\u0070\u001e\u0078\u001e\u00f0\u000c\u007c\u003e\u00f0\u0001\u007c\u003e\u00e0\u0003\u000c\u0030\u00c0\u0001\u000c\u0030\u0000\u0000\u007c\u003e\u0000\u0000\u007c\u003e\u0000\u0000\u0078\u001e\u0000\u0000\u00f0\u000f\u0000\u0000\u00e0\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICUSBClient =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0030\u0000\u0000\u0000\u003e\u0000\u0000\u0080\u001f\u0000\u0000\u00e0\u001f\u0000\u0078\u00c0\u001f\u0000\u00fc\u00c0\u000f\u0000\u00fe\u00c1\u000f\u0000\u00fe\u00e1\u0007\u0000\u00fe\u00f1\u0004\u0000\u00fe\u0079\u0000\u0000\u00ff\u003c\u0000\u0080\u007f\u001e\u0001\u0080\u0003\u008f\u0003\u0000\u0083\u00c7\u0007\u0000\u00c7\u00e3\u000f\u0000\u00e6\u00f1\u001f\u0000\u00fe\u00e1\u000f\u0000\u00fc\u00e7\u0007\u00e0\u003f\u00ff\u0003\u00f8\u001f\u003e\u0001\u00f8\u000f\u0018\u0000\u00fc\u000f\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u000f\u0000\u0000\u00f8\u0007\u0000\u0000\u00f8\u0007\u0000\u0000\u00e0\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICNone =
        "\u00c0\u00ff\u00ff\u0003\u00e0\u00ff\u00ff\u0007\u0070\u0000\u0000\u000e\u0038\u0000\u0000\u001c\u007c\u0000\u0000\u0038\u00fe\u0000\u0000\u0070\u00f7\u0001\u0000\u00e0\u00e3\u0003\u0080\u00c3\u00e3\u0007\u0080\u00c7\u00f3\u000f\u0080\u00cf\u00f3\u001f\u0030\u00cf\u007b\u003e\u0078\u00de\u007b\u007f\u00f8\u00de\u007b\u00ff\u00f3\u00de\u003b\u00ff\u00f7\u00dc\u003b\u00e7\u00e7\u00dc\u003b\u00e7\u00e7\u00dc\u003b\u00ef\u00ff\u00dc\u007b\u00cf\u00ff\u00de\u007b\u001f\u00fe\u00de\u00fb\u001e\u007c\u00df\u00f3\u000c\u00f8\u00cf\u00f3\u0001\u00f0\u00cf\u00e3\u0003\u00e0\u00c7\u00c3\u0001\u00c0\u00c7\u0007\u0000\u0080\u00cf\u000e\u0000\u0000\u00ff\u001c\u0000\u0000\u007e\u0038\u0000\u0000\u003c\u0070\u0000\u0000\u001c\u00e0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u0007";
    private static final String ICBTClient = ICBlue;
    private static final String ICSound =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00cc\u00c0\u0030\u0000\u00cc\u00c0\u0030\u0000\u00c3\u000c\u0033\u0000\u00c3\u000c\u0033\u00fc\u00c3\u0030\u0033\u00fc\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u000c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u003c\u00c3\u0030\u0033\u00cc\u00cf\u0030\u0033\u00cc\u00cf\u0030\u0033\u00fc\u00f3\u0030\u0033\u00fc\u00f3\u0030\u0033\u0000\u00cf\u000c\u0033\u0000\u00cf\u000c\u0033\u0000\u00fc\u00c0\u0030\u0000\u00fc\u00c0\u0030\u0000\u00f0\u0030\u000c\u0000\u00f0\u0030\u000c\u0000\u00c0\u0000\u0003\u0000\u00c0\u0000\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICInfo =
        "\u0000\u00f0\u000f\u0000\u0000\u007e\u007e\u0000\u0080\u0007\u00e0\u0001\u00c0\u0001\u0080\u0003\u0060\u0000\u0000\u0006\u0030\u0000\u0000\u000c\u0018\u0000\u0000\u0018\u000c\u0000\u0001\u0030\u000c\u0080\u0003\u0030\u0006\u0080\u0003\u0060\u0006\u0080\u0001\u0060\u0002\u0000\u0000\u0040\u0003\u00e0\u0003\u00c0\u0003\u00f0\u0003\u00c0\u0003\u0080\u0003\u00c0\u0001\u0080\u0003\u0080\u0001\u0080\u0003\u0080\u0003\u0080\u0003\u00c0\u0003\u0080\u0003\u00c0\u0003\u0080\u0003\u00c0\u0002\u0080\u0003\u0040\u0006\u0080\u0003\u0060\u0006\u0080\u0003\u0060\u000c\u0080\u0003\u0030\u000c\u00f0\u000f\u0030\u0018\u0000\u0000\u0018\u0030\u0000\u0000\u000c\u0060\u0000\u0000\u0006\u00c0\u0001\u0080\u0003\u0080\u0007\u00e0\u0001\u0000\u007e\u007e\u0000\u0000\u00f0\u000f\u0000";

    private static final String ICEV3 =
        "\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0030\u0000\u0000\u000c\u0030\u0000\u0000\u000c\u0030\u00ff\u00ff\u000c\u0030\u00ff\u00ff\u000c\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u0033\u00c0\u000c\u0030\u0033\u00c0\u000c\u0030\u00cf\u00cc\u000c\u0030\u00cf\u00cc\u000c\u0030\u00ff\u00ff\u000c\u0030\u00ff\u00ff\u000c\u0030\u0000\u0000\u000c\u0030\u0000\u0000\u000c\u0030\u00cf\u00f3\u000c\u0030\u00cf\u00f3\u000c\u0030\u00cc\u0033\u000c\u0030\u00cc\u0033\u000c\u00f0\u00c0\u0003\u000c\u00f0\u00c0\u0003\u000c\u0030\u0033\u0000\u000c\u0030\u0033\u0000\u000c\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003";
    private static final String ICDebug =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00e0\u0001\u0080\u0007\u00e0\u00e1\u00c7\u0007\u0000\u00f3\u00ee\u0000\u0000\u00ff\u007f\u0000\u0000\u00de\u003f\u0000\u0000\u00fa\u0077\u0000\u0000\u007f\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0008\u00ef\u00fd\u0010\u001c\u00ff\u00df\u0038\u003c\u007e\u007f\u001c\u0078\u00fc\u003f\u001e\u00f0\u00f8\u001f\u000f\u00e0\u00e1\u0087\u0007\u00e0\u0003\u00c0\u0007\u00f0\u000f\u00f0\u000f\u00fc\u00ff\u00ff\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00fd\u00bf\u00ff\u00fe\u00f8\u001f\u007f\u00f2\u00f8\u001f\u002f\u00e0\u00fd\u00bf\u0007\u00e0\u007f\u00ff\u0007\u00f0\u003f\u00fe\u000f\u00f8\u003f\u00fe\u001f\u00fc\u007f\u00ff\u003f\u003c\u00ff\u00ff\u003c\u0018\u00fe\u007f\u0018\u0000\u007c\u003e\u0000\u0000\u0060\u0006\u0000";
    @SuppressWarnings("unused")
    private static final String ICLeJOS =
        "\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u000f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u00c0\u00cc\u003f\u0000\u00c0\u00cc\u003f\u0000\u0030\u00c3\u003f\u0000\u0030\u00c3\u003f\u0000\u00c0\u00cc\u003f\u0000\u00c0\u00cc\u003f\u00fc\u0033\u00c3\u003f\u00fc\u0033\u00c3\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00fc\u00ff\u00ff\u003f\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000";

    @SuppressWarnings("unused")
    private static final String ICPower =
        "\u0000\u00c0\u0003\u0000\u0000\u00c0\u0003\u0000\u00c0\u00cf\u00f3\u0003\u00c0\u00cf\u00f3\u0003\u00f0\u00cf\u00f3\u000f\u00f0\u00cf\u00f3\u000f\u00fc\u00c3\u00c3\u003f\u00fc\u00c3\u00c3\u003f\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u00ff\u00c0\u0003\u00ff\u00ff\u00c0\u0003\u00ff\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u00c0\u0003\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00fc\u0000\u0000\u003f\u00fc\u0000\u0000\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000";
    private static final String ICVisibility =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u0003\u00c0\u000f\u00f0\u0003\u00c0\u000f\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u003f\u00f0\u000f\u00fc\u00fc\u00c0\u0003\u003f\u00fc\u00c0\u0003\u003f\u00f0\u0003\u00c0\u000f\u00f0\u0003\u00c0\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICSearch =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u0003\u0000\u0000\u00fc\u0003\u0000\u00c0\u0003\u003c\u0000\u00c0\u0003\u003c\u0000\u00c0\u003c\u0030\u0000\u00c0\u003c\u0030\u0000\u0030\u000f\u00c0\u0000\u0030\u000f\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0003\u00c0\u0000\u0030\u0000\u00c0\u0000\u0030\u0000\u00c0\u0000\u00c0\u0000\u0030\u0000\u00c0\u0000\u0030\u0000\u00c0\u0003\u00fc\u0000\u00c0\u0003\u00fc\u0000\u0000\u00fc\u0033\u0003\u0000\u00fc\u0033\u0003\u0000\u0000\u00c0\u000c\u0000\u0000\u00c0\u000c\u0000\u0000\u0000\u0033\u0000\u0000\u0000\u0033\u0000\u0000\u0000\u003c\u0000\u0000\u0000\u003c\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICPIN =
        "\u0000\u0000\u00ff\u0003\u0000\u0000\u00ff\u0003\u0000\u00c0\u0000\u000c\u0000\u00c0\u0000\u000c\u0000\u0030\u0000\u0030\u0000\u0030\u0000\u0030\u0000\u000c\u00f0\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u0030\u00c3\u0000\u000c\u0030\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u00f0\u00c3\u0000\u000c\u0000\u00f0\u0000\u000c\u0000\u00f0\u0000\u000c\u0000\u00cc\u0000\u000c\u0000\u00cc\u0000\u0033\u0030\u0033\u0000\u0033\u0030\u0033\u00c0\u000c\u00cc\u000c\u00c0\u000c\u00cc\u000c\u0030\u00c3\u00ff\u0003\u0030\u00c3\u00ff\u0003\u00cc\u00c0\u0000\u0000\u00cc\u00c0\u0000\u0000\u0033\u00fc\u0000\u0000\u0033\u00fc\u0000\u0000\u000f\u000c\u0000\u0000\u000f\u000c\u0000\u0000\u0003\u000f\u0000\u0000\u0003\u000f\u0000\u0000\u00ff\u0000\u0000\u0000\u00ff\u0000\u0000\u0000";

    private static final String ICDelete =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00c0\u000f\u0000\u0000\u00c0\u000f\u0000\u00c0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u000f\u0030\u0000\u0000\u0030\u0030\u0000\u0000\u0030\u00c0\u00ff\u00ff\u000f\u00c0\u00ff\u00ff\u000f\u00c0\u0000\u0000\u000c\u00c0\u0000\u0000\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u000c\u000f\u00c0\u00cc\u000c\u000f\u00c0\u00cc\u00cc\u000c\u00c0\u00cc\u00cc\u000c\u00c0\u000c\u0030\u000f\u00c0\u000c\u0030\u000f\u00c0\u00c0\u00cc\u000c\u00c0\u00c0\u00cc\u000c\u0000\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICFormat =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u0003\u00c0\u0000\u0000\u0003\u00c0\u0000\u0000\u00f3\u00cf\u0000\u0000\u00f3\u00cf\u0000\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u0000\u0000\u0003\u00c0\u0000\u0000\u0003\u0030\u0003\u00c0\u000c\u0030\u0003\u00c0\u000c\u0030\u00fc\u003f\u000c\u0030\u00fc\u003f\u000c\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u000c\u0000\u00cc\u0030\u000c\u0000\u00cc\u0030\u00cc\u00ff\u0000\u0030\u00cc\u00ff\u0000\u0030\u00f0\u00ff\u00ff\u000f\u00f0\u00ff\u00ff\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICSleep =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00fc\u0003\u0000\u0000\u00fc\u0003\u0000\u0000\u00c0\u00f0\u000f\u0000\u00c0\u00f0\u000f\u0000\u0030\u0000\u0033\u0000\u0030\u0000\u0033\u0000\u00fc\u00cf\u00c0\u0003\u00fc\u00cf\u00c0\u0003\u0000\u00f3\u000f\u000c\u0000\u00f3\u000f\u000c\u0000\u0003\u0000\u000c\u0000\u0003\u0000\u000c\u00c0\u000c\u000f\u0033\u00c0\u000c\u000f\u0033\u00c0\u00f0\u00f0\u0030\u00c0\u00f0\u00f0\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u00c0\u0000\u0000\u0030\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u0003\u000f\u000c\u0000\u003c\u00c0\u0003\u0000\u003c\u00c0\u0003\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICAutoRun =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00ff\u00f0\u00ff\u00ff\u00ff\u00f0\u00ff\u00ff\u0000\u000c\u0000\u00c0\u0000\u000c\u0000\u00c0\u003f\u000c\u00c0\u00cc\u003f\u000c\u00c0\u00cc\u0000\u00ff\u00ff\u003f\u0000\u00ff\u00ff\u003f\u000f\u0003\u0000\u0030\u000f\u0003\u0000\u0030\u00c0\u0000\u0000\u000c\u00c0\u0000\u0000\u000c\u00c3\u0000\u0000\u000f\u00c3\u0000\u0000\u000f\u0030\u0000\u00cc\u0003\u0030\u0000\u00cc\u0003\u0033\u0030\u0033\u0003\u0033\u0030\u0033\u0003\u00f0\u00ff\u00ff\u0000\u00f0\u00ff\u00ff\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";

    private static final String ICYes =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u000f\u0000\u0000\u0000\u000f\u0000\u0000\u00c0\u003f\u0000\u0000\u00c0\u003f\u0000\u0000\u00f0\u00ff\u0000\u0000\u00f0\u00ff\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0030\u0000\u00ff\u000f\u0030\u0000\u00ff\u000f\u00fc\u00c0\u00ff\u0003\u00fc\u00c0\u00ff\u0003\u00ff\u00f3\u00ff\u0000\u00ff\u00f3\u00ff\u0000\u00ff\u00ff\u003f\u0000\u00ff\u00ff\u003f\u0000\u00fc\u00ff\u000f\u0000\u00fc\u00ff\u000f\u0000\u00f0\u00ff\u0003\u0000\u00f0\u00ff\u0003\u0000\u00c0\u00ff\u0000\u0000\u00c0\u00ff\u0000\u0000\u0000\u003f\u0000\u0000\u0000\u003f\u0000\u0000\u0000\u000c\u0000\u0000\u0000\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    private static final String ICNo =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00f0\u0000\u0000\u000f\u00f0\u0000\u0000\u000f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00fc\u003f\u0000\u0000\u00ff\u00ff\u0000\u0000\u00ff\u00ff\u0000\u00c0\u00ff\u00ff\u0003\u00c0\u00ff\u00ff\u0003\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00fc\u000f\u00f0\u003f\u00fc\u000f\u00f0\u003f\u00fc\u0003\u00c0\u003f\u00fc\u0003\u00c0\u003f\u00f0\u0000\u0000\u000f\u00f0\u0000\u0000\u000f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";

    private static final String ICRoberta =
        "\u0000\u0000\u0080\u0000\u0000\u0000\u0080\u0000\u0000\u00c0\u00c3\u0001\u0010\u0001\u00c0\u0001\u00dc\u00c0\u00c1\u0000\u0058\u002f\u00fe\u0000\u009c\u0010\u007c\u0000\u007c\u0080\u001c\u0000\u0078\u0084\u001d\u0000\u0070\u000c\u0004\u0000\u0070\u0000\u0002\u0000\u0080\u00f0\u0001\u0000\u0000\u003f\u0000\u0000\u0000\u0038\u0000\u0000\u0000\u0018\u0000\u0000\u0000\u001c\u0000\u0000\u0000\u001c\u00c0\u0001\u0000\u001c\u00f8\u0003\u0000\u00dc\u00ff\u0003\u0000\u00dc\u00ff\u0003\u0000\u00bc\u00ff\r\u0000\u00f8\u00ff\u001e\u0000\u00f4\u007f\u001f\u0080\u00cf\u00c7\u003f\u00c0\u00ff\u00bb\u003f\u00e0\u00fb\u00fd\u0033\u00e0\u0007\u00ff\u003b\u00e0\u00ff\u00cf\u003b\u00e0\u00ff\u00ef\u001f\u00c0\u00ff\u00ff\u001f\u0080\u00f7\u00fe\u000f\u0000\u0000\u00b8\u0007";
    private static final String ICRobertaInfo =
        "\u0000\u00fc\u003f\u0000\u0000\u00ff\u00ff\u0000\u00c0\u00ff\u00ff\u0003\u00e0\u00ff\u00ff\u0007\u00f0\u0007\u00f0\u000f\u00f8\u0003\u00c0\u001f\u00fc\u0000\u0000\u003f\u007c\u0000\u0007\u003e\u003e\u0080\u000f\u007c\u003e\u0080\u000f\u007c\u001f\u0080\u000f\u00f8\u001f\u0080\u000f\u00f0\u000f\u0000\u0007\u00f0\u000f\u0000\u0000\u00f0\u000f\u00e0\u0001\u00f0\u000f\u00f0\u0007\u00f0\u0007\u00f0\u0007\u00f0\u000f\u00e0\u0007\u00f0\u000f\u00e0\u0007\u00f0\u000f\u00f0\u0007\u00f0\u000f\u00f0\u0003\u00f8\u001f\u00f0\u0003\u00f8\u003e\u00f0\u0007\u007c\u003e\u00f0\u0007\u007c\u007c\u00c0\u0003\u003e\u00fc\u0000\u0000\u003f\u00f8\u0003\u00c0\u001f\u00f0\u000f\u00e0\u000f\u00e0\u00ff\u00fe\u0007\u00c0\u00ff\u00ff\u0003\u0000\u00ff\u00ff\u0000\u0000\u00fc\u003f\u0000";

    private static final String PROGRAMS_DIRECTORY = "/home/lejos/programs";
    private static final String LIB_DIRECTORY = "/home/lejos/lib";
    private static final String SAMPLES_DIRECTORY = "/home/root/lejos/samples";
    private static final String TOOLS_DIRECTORY = "/home/root/lejos/tools";
    private static final String MENU_DIRECTORY = "/home/root/lejos/bin/utils";
    private static final String START_BLUETOOTH = "/home/root/lejos/bin/startbt";
    private static final String START_WLAN = "/home/root/lejos/bin/startwlan";
    private static final String START_PAN = "/home/root/lejos/bin/startpan";
    private static final String PAN_CONFIG = "/home/root/lejos/config/pan.config";
    private static final String WIFI_CONFIG = "/home/root/lejos/config/wpa_supplicant.conf";
    private static final String WIFI_BASE = "wpa_supplicant.txt";
    private static final String WLAN_INTERFACE = "wlan0";
    private static final String PAN_INTERFACE = "br0";

    public static final int IND_SUSPEND = -1;
    public static final int IND_NONE = 0;
    public static final int IND_NORMAL = 1;
    public static final int IND_FULL = 2;

    @SuppressWarnings("unused")
    private static final int defaultSleepTime = 2;
    private static final int maxSleepTime = 10;

    // Threads
    public final IndicatorThread ind = new IndicatorThread();
    private final BatteryIndicator indiBA = new BatteryIndicator();
    private final PipeReader pipeReader = new PipeReader();
    private final RConsole rcons = new RConsole();
    private final RemoteMenuThread remoteMenuThread = new RemoteMenuThread();

    //private GraphicMenu curMenu;
    private int timeout = 0;
    private boolean btVisibility;
    private final PANConfig panConfig = new PANConfig();
    private final WaitScreen waitScreen = new WaitScreen();
    private static String version = "Unknown";
    private static String hostname;
    private static List<String> ips = new ArrayList<String>();
    private static String wlanAddress;
    private static String panAddress;

    private static LocalBTDevice bt;
    public static GraphicStartup menu = new GraphicStartup();

    private static ORAhandler oraHandler = new ORAhandler();
    public static boolean orUSBconnected = false;
    private static String OPENROBERTAHEAD = " OR Lab";
    private static HttpServer usbconn = null;

    private static final String OPENROBERTAPROPERTIES = "/home/roberta/openroberta.properties";

    private final static Properties menuProperties = loadProperties();
    private static Properties openrobertaProperties = null;

    private static TextLCD lcd = LocalEV3.get().getTextLCD();

    private static Process program; // the running user program, if any
    private static String programName; // The name of the running program

    private static boolean suspend = false;
    private static EchoThread echoIn, echoErr;

    private static GraphicMenu curMenu;

    /**
     * Main method
     */
    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.out.println("Menu started");
        LocalEV3.get().getLED().setPattern(3);
        if ( args.length > 0 ) {
            hostname = args[0];
        }

        if ( args.length > 1 ) {
            version = args[1];
        }

        System.out.println("Host name: " + hostname);
        System.out.println("Version: " + version);
        waitForEV3BootComplete();
        System.out.println("Boot complete");
        LocalEV3.get().getLED().setPattern(1);
        menu = new GraphicStartup();
        lcd = LocalEV3.get().getTextLCD();
        // Check for autorun
        File file = getDefaultProgram();
        if ( file != null ) {
            String auto = Settings.getProperty(defaultProgramAutoRunProperty, "");
            if ( auto.equals("ON") && !Button.LEFT.isDown() ) {
                System.out.println("Auto executing default program " + file.getPath());
                try {
                    JarFile jar = new JarFile(file);
                    String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
                    jar.close();
                    exec(file, JAVA_RUN_CP + file.getPath() + " lejos.internal.ev3.EV3Wrapper " + mainClass, PROGRAMS_DIRECTORY);
                } catch ( IOException e ) {
                    System.err.println("Exception running program");
                }
            }
        }

        TuneThread tuneThread = new TuneThread();
        tuneThread.start();

        USBservice usbservice = new USBservice();
        usbservice.start();

        System.out.println("Getting IP addresses");
        menu.updateIPAddresses();

        InitThread initThread = new InitThread();
        initThread.start();

        System.out.println("Starting background threads");
        menu.start();

        System.out.println("Starting the menu");
        LocalEV3.get().getLED().setPattern(0);
        menu.mainMenu();

        System.out.println("Menu finished");
        System.exit(0);
    }

    private static void waitForEV3BootComplete() {
        File bootLock = new File("/var/run/bootlock");
        while ( bootLock.exists() ) {
            Delay.msDelay(500);
        }
    }

    /**
     * Class to handle PAN configuration
     *
     * @author andy
     */
    class PANConfig {
        public static final int MODE_NONE = 0;
        public static final int MODE_AP = 1;
        public static final int MODE_APP = 2;
        public static final int MODE_BTC = 3;
        public static final int MODE_USBC = 4;

        final String[] modeIDS = {
            "NONE",
            "AP",
            "AP+",
            "BT",
            "USB"
        };
        final String[] modeNames = {
            "None",
            "Access Pt",
            "Access Pt+",
            "BT Client",
            "USB Client"
        };
        final String[] serviceNames = {
            "NAP",
            "PANU",
            "GN"
        };
        static final String autoIP = "0.0.0.0";
        static final String anyAP = "*";

        String[] IPAddresses = {
            autoIP,
            autoIP,
            autoIP,
            autoIP,
            autoIP
        };
        String[] IPNames = {
            "Address",
            "Netmask",
            "Brdcast",
            "Gateway",
            "DNS    "
        };
        String[] IPIDS = {
            "IP",
            "NM",
            "BC",
            "GW",
            "DN"
        };

        int curMode = MODE_NONE;
        String BTAPName = anyAP;
        String BTAPAddress = anyAP;
        String BTService = "NAP";
        String persist = "N";
        Boolean changed = false;

        public PANConfig() {
            loadConfig();
        }

        public int getCurrentMode() {
            return this.curMode;
        }

        public void saveConfig() {
            System.out.println("Save PAN config");
            try {
                PrintWriter out = new PrintWriter(PAN_CONFIG);
                out.print(this.modeIDS[this.curMode] + " " + this.BTAPName.replace(" ", "\\ ") + " " + this.BTAPAddress);
                for ( String ip : this.IPAddresses ) {
                    out.print(" " + ip);
                }
                out.print(" " + this.BTService + " " + this.persist);
                out.println();
                out.close();
                this.changed = false;
            } catch ( IOException e ) {
                System.out.println("Failed to write PAN config to " + PAN_CONFIG + ": " + e);
            }
        }

        private String getConfigString(String[] vals, int offset, String def) {
            if ( vals == null || offset >= vals.length || vals[offset] == null || vals[offset].length() == 0 ) {
                return def;
            }
            return vals[offset];
        }

        public void loadConfig() {
            System.out.println("Load PAN config");
            String[] vals = null;
            try {
                BufferedReader in = new BufferedReader(new FileReader(PAN_CONFIG));
                // nasty cludge preserve escaped spaces (convert them to no-break space
                String line = in.readLine().replace("\\ ", "\u00a0");
                vals = line.split("\\s+");
                in.close();
            } catch ( IOException e ) {
                System.out.println("Failed to load PAN config from " + PAN_CONFIG + ": " + e);
            }
            String mode = getConfigString(vals, 0, this.modeIDS[MODE_NONE]);
            // turn mode into value
            this.curMode = MODE_NONE;
            for ( int i = 0; i < this.modeIDS.length; i++ ) {
                if ( this.modeIDS[i].equalsIgnoreCase(mode) ) {
                    this.curMode = i;
                    break;
                }
            }
            // be sure to convert no-break space back - ahem.
            this.BTAPName = getConfigString(vals, 1, anyAP).replace("\u00a0", " ");
            this.BTAPAddress = getConfigString(vals, 2, anyAP);
            for ( int i = 0; i < this.IPAddresses.length; i++ ) {
                this.IPAddresses[i] = getConfigString(vals, i + 3, autoIP);
            }
            if ( this.curMode == MODE_AP && this.IPAddresses[0].equals(autoIP) ) {
                this.IPAddresses[0] = "10.0.1.1";
            }
            this.BTService = getConfigString(vals, 8, "NAP");
            this.persist = getConfigString(vals, 9, "N");
            this.changed = false;
        }

        public void init(int mode) {
            if ( mode != this.curMode ) {
                for ( int i = 0; i < this.IPAddresses.length; i++ ) {
                    this.IPAddresses[i] = autoIP;
                }
                switch ( mode ) {
                    case MODE_AP:
                        this.IPAddresses[0] = "10.0.1.1";
                        break;
                    case MODE_APP:
                        // For access point plus we need to use a sub-net within the
                        // sub-net being used for WiFi. Set a default that may work for
                        // most - well it does for me!
                        if ( wlanAddress != null ) {
                            String[] parts = wlanAddress.split("\\.");
                            if ( parts.length == 4 ) {
                                this.IPAddresses[0] = parts[0] + "." + parts[1] + "." + parts[2] + ".208";
                            }
                        }
                        break;
                }
                this.BTAPName = anyAP;
                this.BTAPAddress = anyAP;
                this.BTService = "NAP";
                this.persist = "N";
                this.curMode = mode;
                this.changed = true;
            }
        }

        /**
         * Test to see if the IP address string is the special case auto address
         *
         * @param ip
         * @return true if the address is the auto address.
         */
        private boolean isAutoIP(String ip) {
            return ip.equals(autoIP);
        }

        /**
         * Return an IP address suitable for display, replace the auto address with a
         * more readable version.
         *
         * @param ip
         * @return the display string
         */
        private String getDisplayIP(String ip) {
            return isAutoIP(ip) ? "<Auto>" : ip;
        }

        private boolean isAnyAP(String bt) {
            return bt.equals(anyAP);
        }

        private String getDisplayAP(String bt) {
            return isAnyAP(bt) ? "Any Access Point" : bt;
        }

        /**
         * Validate and cleanup the IP address
         *
         * @param address
         * @return validated IP or null if there is an error.
         */
        private String getValidatedIP(String address) {
            try {
                return InetAddress.getByName(address).getHostAddress();
            } catch ( UnknownHostException e ) {
                return null;
            }
        }

        /**
         * Allow the user to enter an IP address
         *
         * @param title String to display as the title of the screen
         * @param ip IP address to edit
         * @return new validated address
         */
        private String enterIP(String title, String ip) {
            String[] parts = ip.split("\\.");
            for ( int i = 0; i < parts.length; i++ ) {
                parts[i] = "000".substring(parts[i].length()) + parts[i];
            }
            String address = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
            int curDigit = 0;
            while ( true ) {
                newScreen(title);
                lcd.drawString(address, 2, 4);
                if ( curDigit < 0 ) {
                    curDigit = 14;
                }
                if ( curDigit >= 15 ) {
                    curDigit = 0;
                }
                Utils.drawRect(curDigit * 10 + 18, 60, 13, 20);
                lcd.refresh();
                int key = getButtonPress();
                switch ( key ) {
                    case Button.ID_ENTER: { // ENTER
                                            // remove leading zeros
                        String ret = getValidatedIP(address);
                        if ( ret == null ) {
                            msg("Invalid address");
                        } else {
                            return ret;
                        }
                        break;
                    }
                    case Button.ID_LEFT: { // LEFT
                        curDigit--;
                        if ( curDigit < 0 ) {
                            curDigit = 14;
                        }
                        if ( address.charAt(curDigit) == '.' ) {
                            curDigit--;
                        }
                        break;
                    }
                    case Button.ID_RIGHT: { // RIGHT
                        curDigit++;
                        if ( curDigit >= 15 ) {
                            curDigit = 0;
                        }
                        if ( address.charAt(curDigit) == '.' ) {
                            curDigit++;
                        }
                        break;
                    }
                    case Button.ID_ESCAPE: { // ESCAPE
                        return ip;
                    }
                    case Button.ID_UP: {
                        int val = (address.charAt(curDigit) - '0');
                        if ( ++val > 9 ) {
                            val = 0;
                        }
                        address = address.substring(0, curDigit) + ((char) ('0' + val)) + address.substring(curDigit + 1);
                        break;
                    }
                    case Button.ID_DOWN: {
                        int val = (address.charAt(curDigit) - '0');
                        if ( --val < 0 ) {
                            val = 9;
                        }
                        address = address.substring(0, curDigit) + ((char) ('0' + val)) + address.substring(curDigit + 1);
                        break;
                    }
                }
            }
        }

        /**
         * Allow the user to choose between an automatic or manual IP address if
         * manual allow the address to be edited
         *
         * @param title
         * @param ip
         * @return new ip address
         */
        private String getIPAddress(String title, String ip) {
            String[] strings = {
                "Automatic",
                "Advanced"
            };
            String[] icons = new String[strings.length];
            GraphicMenu menu = new GraphicListMenu(strings, icons, 4);
            newScreen(title);
            String dispIP = getDisplayIP(ip);
            lcd.drawString(dispIP, (lcd.getTextWidth() - dispIP.length()) / 2, 2);
            menu.setItems(strings, icons);
            int selection = getSelection(menu, isAutoIP(ip) ? 0 : 1);
            switch ( selection ) {
                case 0:
                    return autoIP;
                case 1:
                    return enterIP(title, ip);
                default:
                    return ip;
            }
        }

        /**
         * Allow the user to choose the Bluetooth service to connect to.
         *
         * @param title
         * @param service
         * @return new service
         */
        private String getBTService(String title, String service) {
            String[] strings = this.serviceNames;
            String[] icons = new String[strings.length];
            GraphicMenu menu = new GraphicListMenu(strings, icons, 4);
            newScreen(title);
            lcd.drawString(service, (lcd.getTextWidth() - service.length()) / 2, 2);
            menu.setItems(strings, icons);
            int item = 0;
            while ( !strings[item].equalsIgnoreCase(service) ) {
                item++;
            }
            int selection = getSelection(menu, item);
            if ( selection > 0 ) {
                return strings[selection];
            } else {
                return service;
            }
        }

        public void configureAdvanced() {
            int selection = 0;
            int extra = (this.curMode == MODE_BTC ? 2 : this.curMode == MODE_USBC ? 1 : 0);
            String[] strings = new String[this.IPAddresses.length + extra];
            String[] icons = new String[this.IPAddresses.length + extra];
            GraphicMenu menu = new GraphicListMenu(strings, icons);
            for ( ;; ) {
                newScreen(this.modeNames[this.curMode]);
                for ( int i = 0; i < this.IPAddresses.length; i++ ) {
                    strings[i] = this.IPNames[i] + " " + getDisplayIP(this.IPAddresses[i]);
                }
                if ( extra > 0 ) {
                    strings[this.IPAddresses.length] = "Persist " + this.persist;
                }
                if ( extra > 1 ) {
                    strings[this.IPAddresses.length + 1] = "Service " + this.BTService;
                }

                menu.setItems(strings, icons);
                selection = getSelection(menu, selection);
                if ( selection < 0 ) {
                    break;
                }
                this.changed = true;
                if ( selection < this.IPAddresses.length ) {
                    this.IPAddresses[selection] = getIPAddress(this.IPNames[selection], this.IPAddresses[selection]);
                } else if ( selection == this.IPAddresses.length ) {
                    switch ( getYesNo("Persist Connection", this.persist.equalsIgnoreCase("Y")) ) {
                        case 0:
                            this.persist = "N";
                            break;
                        case 1:
                            this.persist = "Y";
                            break;
                    }
                } else {
                    this.BTService = getBTService("Service", this.BTService);
                }

            }
        }

        /**
         * Display all currently known Bluetooth devices.
         */
        private void selectAP() {
            newScreen("Devices");
            lcd.drawString("Searching...", 3, 2);
            List<RemoteBTDevice> devList;
            try {
                devList = (List<RemoteBTDevice>) Bluetooth.getLocalDevice().getPairedDevices();
            } catch ( BluetoothException e ) {
                return;
            }
            if ( devList.size() <= 0 ) {
                msg("No known devices");
                return;
            }

            String[] names = new String[devList.size()];
            String[] icons = new String[devList.size()];
            int i = 0;
            for ( RemoteBTDevice btrd : devList ) {
                names[i] = btrd.getName();
                i++;
            }

            GraphicListMenu deviceMenu = new GraphicListMenu(names, icons);
            int selected = 0;
            newScreen("Devices");
            selected = getSelection(deviceMenu, selected);
            if ( selected >= 0 ) {
                RemoteBTDevice btrd = devList.get(selected);
                //byte[] devclass = btrd.getDeviceClass();
                this.BTAPName = btrd.getName();
                this.BTAPAddress = btrd.getAddress();
                this.changed = true;
            }
        }

        public void configureBTClient(String title) {
            String[] strings = {
                "Any",
                "Select",
                "Advanced"
            };
            String[] icons = new String[strings.length];
            GraphicMenu menu = new GraphicListMenu(strings, icons, 4);
            while ( true ) {
                newScreen(title);
                String dispIP = getDisplayAP(this.BTAPName);
                lcd.drawString(dispIP, (lcd.getTextWidth() - dispIP.length()) / 2, 2);
                if ( !isAnyAP(this.BTAPName) ) {
                    lcd.drawString(this.BTAPAddress, (lcd.getTextWidth() - this.BTAPAddress.length()) / 2, 3);
                }
                int selection = getSelection(menu, isAnyAP(this.BTAPName) ? 0 : 1);
                switch ( selection ) {
                    case 0:
                        this.BTAPName = anyAP;
                        this.BTAPAddress = anyAP;
                        this.changed = true;
                        return;
                    case 1:
                        selectAP();
                        break;
                    case 2:
                        configureAdvanced();
                        break;
                    default:
                        return;
                }
            }
        }

        public void configure() {
            if ( this.curMode == MODE_NONE ) {
                return;
            }
            if ( this.curMode == MODE_BTC ) {
                configureBTClient(this.modeNames[this.curMode]);
            } else {
                configureAdvanced();
            }
        }

        public void panMenu() {
            int selection = 0;
            GraphicMenu menu = new GraphicMenu(null, null, 3);
            do {
                newScreen("PAN");
                menu.setItems(this.modeNames, new String[] {
                    ICNone,
                    ICAccessPoint,
                    ICAccessPointPlus,
                    ICBTClient,
                    ICUSBClient
                });
                selection = getSelection(menu, this.curMode);
                if ( selection >= 0 ) {
                    init(selection);
                    configure();
                }
            } while ( selection >= 0 );
            if ( this.changed ) {
                GraphicStartup.this.waitScreen.begin("Restart\nPAN\nServices");
                GraphicStartup.this.waitScreen.status("Save configuration");
                saveConfig();
                startNetwork(START_PAN, true);
                GraphicStartup.this.waitScreen.status("Restart name server");
                BrickFinder.stopDiscoveryServer();
                BrickFinder.startDiscoveryServer(this.curMode == MODE_APP);
                GraphicStartup.this.waitScreen.end();
            }
        }
    }

    private class WaitScreen {
        final GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
        final int scrWidth;
        final int scrHeight;
        final int chHeight;
        final int basePos;
        final int statusPos;

        public WaitScreen() {
            this.g.setFont(Font.getDefaultFont());
            this.scrWidth = this.g.getWidth();
            this.scrHeight = this.g.getHeight();
            this.chHeight = this.g.getFont().getHeight();
            this.basePos = this.scrHeight / 3;
            this.statusPos = this.basePos * 2;
        }

        public void begin(String title) {
            System.out.println("Start wait");
            suspend();
            this.g.clear();
            this.g.drawRegion(
                hourglass,
                0,
                0,
                hourglass.getWidth(),
                hourglass.getHeight(),
                GraphicsLCD.TRANS_NONE,
                50,
                50,
                GraphicsLCD.HCENTER | GraphicsLCD.VCENTER);
            int x = LCD.SCREEN_WIDTH / 2;
            String[] strings = title.split("\n");
            int y = this.basePos - (strings.length / 2) * this.chHeight;
            for ( String s : strings ) {
                this.g.drawString(s, x, y, 0);
                y += this.chHeight;
            }
            this.g.refresh();
        }

        public void end() {
            this.g.clear();
            resume();
        }

        public void status(String msg) {
            this.g.bitBlt(null, this.scrWidth, this.chHeight, 0, 0, 0, this.statusPos, this.scrWidth, this.chHeight, CommonLCD.ROP_CLEAR);
            this.g.drawString(msg, 0, this.statusPos, 0);
            this.g.refresh();
        }
    }

    /**
     * Start-up thread
     */
    static class InitThread extends Thread {
        /**
         * Create the Bluetooth local device and connect to DBus
         * Start the RMI server
         * Get the time from a name server
         */
        @Override
        public void run() {
            // Create the Bluetooth local device and connect to DBus
            try {
                System.out.println("Creating bluetooth local device");
                bt = Bluetooth.getLocalDevice();
            } catch ( Exception e ) {
                // Ignore
            }
            System.out.println("Got bluetooth device");
            menu.startNetworkServices();
            System.out.println("Initialisation complete");
        }
    }

    static class USBservice extends Thread {
        @Override
        public void run() {
            try {
                usbconn = HttpServer.create(new InetSocketAddress(InetAddress.getByName("10.0.1.1"), 80), 0);
                usbconn.createContext("/brickinfo", new ORAbrickInfo());
                usbconn.createContext("/program", new ORAprogram());
                usbconn.createContext("/firmware", new ORAfirmware());
                usbconn.setExecutor(null); // creates a default executor
                usbconn.start();
            } catch ( IOException e ) {
                System.out.println("HttpServer startup failed!!");
            }
        }
    }

    /**
     * Start the background threads
     */
    private void start() {
        this.ind.start();
        this.rcons.start();
        this.pipeReader.start();
        BrickFinder.startDiscoveryServer(this.panConfig.getCurrentMode() == PANConfig.MODE_APP);
        this.remoteMenuThread.start();
    }

    /**
     * Display the main system menu.
     * Allow the user to select File, Bluetooth, Sound, System operations.
     */
    private void mainMenu() {
        GraphicMenu menu = new GraphicMenu(new String[] {
            "Open Roberta Lab",
            "Wifi",
            "Programs",
            "Samples",
            "Tools",
            "Run Default",
            "Bluetooth",
            "PAN",
            "Sound",
            "System",
            "Version",
        }, new String[] {
            ICRoberta,
            ICWifi,
            ICFiles,
            ICSamples,
            ICTools,
            ICDefault,
            ICBlue,
            ICPAN,
            ICSound,
            ICEV3,
            ICRobertaInfo,
        }, 3);
        int selection = 0;
        do {
            newScreen(hostname);
            this.ind.setDisplayState(IND_FULL);
            selection = getSelection(menu, selection);
            this.ind.setDisplayState(IND_NORMAL);
            switch ( selection ) {
                case 0:
                    robertaMenu();
                    break;
                case 1:
                    wifiMenu();
                    break;
                case 2:
                    filesMenu();
                    break;
                case 3:
                    samplesMenu();
                    break;
                case 4:
                    toolsMenu();
                    break;
                case 5:
                    mainRunDefault();
                    break;
                case 6:
                    bluetoothMenu();
                    break;
                case 7:
                    this.panConfig.panMenu();
                    break;
                case 8:
                    soundMenu();
                    break;
                case 9:
                    systemMenu();
                    break;
                case 10:
                    displayVersion();
                    break;
            }

            if ( selection < 0 ) {
                if ( getYesNo("  Shut down EV3 ?", false) == 1 ) {
                    break;
                }
            }
        } while ( true );

        // Shut down the EV3
        shutdown();
    }

    public class RemoteMenuThread extends Thread {
        @SuppressWarnings({
            "resource",
            "deprecation"
        })
        @Override
        public void run() {

            ServerSocket ss;
            Socket conn = null;

            // Create a server socket
            try {
                ss = new ServerSocket(REMOTE_MENU_PORT);
                System.out.println("Remote menu server socket created");
            } catch ( IOException e ) {
                System.err.println("Error creating server socket: " + e);
                return;
            }

            Port[] ports = new Port[] {
                SensorPort.S1,
                SensorPort.S2,
                SensorPort.S3,
                SensorPort.S4,
                MotorPort.A,
                MotorPort.B,
                MotorPort.C,
                MotorPort.D
            };
            IOPort[] ioPorts = new IOPort[8];
            GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
            SampleProvider[] providers = new SampleProvider[4];
            BaseSensor[] sensors = new BaseSensor[4];
            DifferentialPilot pilot = null;
            int pilotLeftMotor = 0, pilotRightMotor = 0;

            RegulatedMotor[] motors = new RegulatedMotor[4];

            while ( true ) {
                try {
                    System.out.println("Waiting for a remote menu connection");
                    conn = ss.accept();
                    //conn.setSoTimeout(2000);
                    conn.setTcpNoDelay(true);
                    ObjectOutputStream os = new ObjectOutputStream(conn.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(conn.getInputStream());

                    try {
                        while ( true ) {
                            os.reset();
                            Object obj = is.readObject();

                            if ( obj instanceof MenuRequest ) {
                                MenuRequest request = (MenuRequest) obj;
                                MenuReply reply = new MenuReply();

                                switch ( request.request ) {
                                    case RUN_PROGRAM:
                                        runProgram(request.name);
                                        break;
                                    case DEBUG_PROGRAM:
                                        debugProgram(request.name);
                                        break;
                                    case DELETE_ALL_PROGRAMS:
                                        deleteAllPrograms();
                                        break;
                                    case DELETE_FILE:
                                        reply.result = deleteFile(request.name);
                                        os.writeObject(reply);
                                        break;
                                    case FETCH_FILE:
                                        reply.contents = fetchFile(request.name);
                                        os.writeObject(reply);
                                        break;
                                    case GET_FILE_SIZE:
                                        reply.reply = (int) getFileSize(request.name);
                                        os.writeObject(reply);
                                        break;
                                    case GET_MENU_VERSION:
                                        reply.value = getMenuVersion();
                                        os.writeObject(reply);
                                        break;
                                    case GET_NAME:
                                        reply.value = menu.getName();
                                        os.writeObject(reply);
                                        break;
                                    case GET_PROGRAM_NAMES:
                                        reply.names = getProgramNames();
                                        os.writeObject(reply);
                                        break;
                                    case GET_SAMPLE_NAMES:
                                        reply.names = getSampleNames();
                                        os.writeObject(reply);
                                        break;
                                    case GET_SETTING:
                                        reply.value = getSetting(request.name);
                                        os.writeObject(reply);
                                        break;
                                    case GET_VERSION:
                                        reply.value = getVersion();
                                        os.writeObject(reply);
                                        break;
                                    case RUN_SAMPLE:
                                        runSample(request.name);
                                        break;
                                    case SET_NAME:
                                        setName(request.name);
                                        break;
                                    case SET_SETTING:
                                        setSetting(request.name, request.value);
                                        break;
                                    case UPLOAD_FILE:
                                        reply.result = uploadFile(request.name, request.contents);
                                        os.writeObject(reply);
                                        break;
                                    case STOP_PROGRAM:
                                        stopProgram();
                                        break;
                                    case SHUT_DOWN:
                                        shutdown();
                                        break;
                                    case GET_EXECUTING_PROGRAM_NAME:
                                        reply.value = programName;
                                        os.writeObject(reply);
                                        break;
                                    case SUSPEND:
                                        GraphicStartup.this.suspend();
                                        break;
                                    case RESUME:
                                        GraphicStartup.this.resume();
                                }
                            } else if ( obj instanceof EV3Request ) {
                                EV3Request request = (EV3Request) obj;
                                EV3Reply reply = new EV3Reply();
                                //System.out.println("Request: " + request.request);
                                try {
                                    switch ( request.request ) {
                                        case GET_VOLTAGE_MILLIVOLTS:
                                            reply.reply = Battery.getVoltageMilliVolt();
                                            os.writeObject(reply);
                                            break;
                                        case GET_VOLTAGE:
                                            reply.floatReply = Battery.getVoltage();
                                            os.writeObject(reply);
                                            break;
                                        case GET_BATTERY_CURRENT:
                                            reply.floatReply = Battery.getBatteryCurrent();
                                            os.writeObject(reply);
                                            break;
                                        case GET_MOTOR_CURRENT:
                                            reply.floatReply = Battery.getMotorCurrent();
                                            os.writeObject(reply);
                                            break;
                                        case SYSTEM_SOUND:
                                            Sound.systemSound(false, request.intValue);
                                            break;
                                        case PLAY_SAMPLE:
                                            reply.reply = Sound.playSample(request.file);
                                            os.writeObject(reply);
                                            break;
                                        case GET_NAME:
                                            reply.value = menu.getName();
                                            os.writeObject(reply);
                                            break;
                                        case LED_PATTERN:
                                            LocalEV3.get().getLED().setPattern(request.intValue);
                                            break;
                                        case WAIT_FOR_ANY_EVENT:
                                            reply.reply = Button.waitForAnyEvent(request.intValue);
                                            os.writeObject(reply);
                                            break;
                                        case WAIT_FOR_ANY_PRESS:
                                            reply.reply = Button.waitForAnyPress(request.intValue);
                                            os.writeObject(reply);
                                            break;
                                        case GET_BUTTONS:
                                            reply.reply = Button.getButtons();
                                            os.writeObject(reply);
                                            break;
                                        case READ_BUTTONS:
                                            reply.reply = Button.readButtons();
                                            os.writeObject(reply);
                                            break;
                                        case LCD_REFRESH:
                                            LCD.refresh();
                                            break;
                                        case LCD_CLEAR:
                                            LCD.clear();
                                            break;
                                        case LCD_GET_WIDTH:
                                            reply.reply = LCD.SCREEN_WIDTH;
                                            os.writeObject(reply);
                                            break;
                                        case LCD_GET_HEIGHT:
                                            reply.reply = LCD.SCREEN_HEIGHT;
                                            os.writeObject(reply);
                                            break;
                                        case LCD_GET_HW_DISPLAY:
                                            break;
                                        case LCD_BITBLT_1:
                                            break;
                                        case LCD_BITBLT_2:
                                            break;
                                        case LCD_SET_AUTO_REFRESH:
                                            LCD.setAutoRefresh(request.flag);
                                            break;
                                        case LCD_SET_AUTO_REFRESH_PERIOD:
                                            LCD.setAutoRefreshPeriod(request.intValue);
                                            break;
                                        case LCD_DRAW_CHAR:
                                            LCD.drawChar(request.ch, request.intValue, request.intValue2);
                                            break;
                                        case LCD_DRAW_STRING_INVERTED:
                                            LCD.drawString(request.str, request.intValue, request.intValue2, request.flag);
                                            break;
                                        case LCD_DRAW_STRING:
                                            LCD.drawString(request.str, request.intValue, request.intValue2);
                                            break;
                                        case LCD_DRAW_INT:
                                            LCD.drawInt(request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_DRAW_INT_PLACES:
                                            LCD.drawInt(request.intValue, request.intValue2, request.intValue3, request.intValue4);
                                            break;
                                        case LCD_CLEAR_LINES:
                                            LCD.clear(request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_CLEAR_LINE:
                                            LCD.clear(request.intValue);
                                            break;
                                        case LCD_SCROLL:
                                            LCD.scroll();
                                            break;
                                        case LCD_GET_FONT:
                                            break;
                                        case LCD_GET_TEXT_WIDTH:
                                            reply.reply = LCD.DISPLAY_CHAR_WIDTH;
                                            os.writeObject(reply);
                                            break;
                                        case LCD_GET_TEXT_HEIGHT:
                                            reply.reply = LCD.DISPLAY_CHAR_DEPTH;
                                            os.writeObject(reply);
                                            break;
                                        case OPEN_MOTOR_PORT:
                                            ioPorts[4 + request.intValue] = ports[4 + request.intValue].open(TachoMotorPort.class);
                                            break;
                                        case CLOSE_MOTOR_PORT:
                                            ioPorts[4 + request.intValue].close();
                                            break;
                                        case CONTROL_MOTOR:
                                            ((TachoMotorPort) ioPorts[4 + request.intValue]).controlMotor(request.intValue2, request.intValue3);
                                            break;
                                        case GET_TACHO_COUNT:
                                            reply.reply = ((TachoMotorPort) ioPorts[4 + request.intValue]).getTachoCount();
                                            os.writeObject(reply);
                                            break;
                                        case RESET_TACHO_COUNT:
                                            ((TachoMotorPort) ioPorts[4 + request.intValue]).resetTachoCount();
                                            break;
                                        case KEY_IS_DOWN:
                                            reply.result = LocalEV3.get().getKey(request.str).isDown();
                                            os.writeObject(reply);
                                            break;
                                        case KEY_WAIT_FOR_PRESS:
                                            LocalEV3.get().getKey(request.str).waitForPress();
                                            os.writeObject(reply);
                                            break;
                                        case KEY_WAIT_FOR_PRESS_AND_RELEASE:
                                            LocalEV3.get().getKey(request.str).waitForPress();
                                            os.writeObject(reply);
                                            break;
                                        case KEY_SIMULATE_EVENT:
                                            LocalEV3.get().getKey(request.str).simulateEvent(request.intValue);
                                            break;
                                        case OPEN_ANALOG_PORT:
                                            ioPorts[request.intValue] = ports[request.intValue].open(AnalogPort.class);
                                            os.writeObject(reply);
                                            break;
                                        case OPEN_I2C_PORT:
                                            ioPorts[request.intValue] = ports[request.intValue].open(I2CPort.class);
                                            os.writeObject(reply);
                                            break;
                                        case OPEN_UART_PORT:
                                            ioPorts[request.intValue] = ports[request.intValue].open(UARTPort.class);
                                            os.writeObject(reply);
                                            break;
                                        case CLOSE_SENSOR_PORT:
                                            ioPorts[request.intValue].close();
                                            break;
                                        case GET_PIN_6:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.floatReply = ((AnalogPort) ioPorts[request.intValue]).getPin6();
                                            os.writeObject(reply);
                                            break;
                                        case GET_PIN_1:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.floatReply = ((AnalogPort) ioPorts[request.intValue]).getPin1();
                                            os.writeObject(reply);
                                            break;
                                        case SET_PIN_MODE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            ((AnalogPort) ioPorts[request.intValue]).setMode(request.intValue);
                                            break;
                                        case GET_FLOATS:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.floats = new float[request.intValue2];
                                            ((AnalogPort) ioPorts[request.intValue]).getFloats(reply.floats, 0, request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case LCD_G_SET_PIXEL:
                                            g.setPixel(request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_G_GET_PIXEL:
                                            break;
                                        case LCD_G_DRAW_STRING:
                                            g.drawString(request.str, request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_G_DRAW_STRING_INVERTED:
                                            g.drawString(request.str, request.intValue, request.intValue2, request.intValue3, request.flag);
                                            break;
                                        case LCD_G_DRAW_CHAR:
                                            g.drawChar(request.ch, request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_G_DRAW_SUBSTRING:
                                            g.drawSubstring(
                                                request.str,
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5);
                                            break;
                                        case LCD_G_DRAW_CHARS:
                                            g.drawChars(
                                                request.chars,
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5);
                                            break;
                                        case LCD_G_GET_STROKE_STYLE:
                                            break;
                                        case LCD_G_SET_STROKE_STYLE:
                                            g.setStrokeStyle(request.intValue);
                                            break;
                                        case LCD_G_DRAW_REGION_ROP:
                                            g.drawRegionRop(
                                                request.image,
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6,
                                                request.intValue7,
                                                request.intValue8);
                                            break;
                                        case LCD_G_DRAW_REGION_ROP_TRANSFORM:
                                            g.drawRegionRop(
                                                request.image,
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6,
                                                request.intValue7,
                                                request.intValue8,
                                                request.intValue9);
                                            break;
                                        case LCD_G_DRAW_REGION:
                                            g.drawRegion(
                                                request.image,
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6,
                                                request.intValue7,
                                                request.intValue8);
                                            break;
                                        case LCD_G_DRAW_IMAGE:
                                            g.drawImage(request.image, request.intValue, request.intValue2, request.intValue3);
                                            break;
                                        case LCD_G_DRAW_LINE:
                                            g.drawLine(request.intValue, request.intValue2, request.intValue3, request.intValue4);
                                            break;
                                        case LCD_G_DRAW_ARC:
                                            g.drawArc(
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6);
                                            break;
                                        case LCD_G_FILL_ARC:
                                            g.fillArc(
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6);
                                            break;
                                        case LCD_G_DRAW_ROUND_RECT:
                                            g.drawRoundRect(
                                                request.intValue,
                                                request.intValue2,
                                                request.intValue3,
                                                request.intValue4,
                                                request.intValue5,
                                                request.intValue6);
                                            break;
                                        case LCD_G_DRAW_RECT:
                                            g.drawRect(request.intValue, request.intValue2, request.intValue3, request.intValue4);
                                            break;
                                        case LCD_G_FILL_RECT:
                                            g.fillRect(request.intValue, request.intValue2, request.intValue3, request.intValue4);
                                            break;
                                        case LCD_G_TRANSLATE:
                                            g.translate(request.intValue, request.intValue2);
                                            break;
                                        case LCD_G_GET_TRANSLATE_X:
                                            break;
                                        case LCD_G_GET_TRANSLATE_Y:
                                            break;
                                        case I2C_TRANSACTION:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.contents = new byte[request.intValue6];
                                            ((I2CPort) ioPorts[request.intValue]).i2cTransaction(
                                                request.intValue2,
                                                request.byteData,
                                                request.intValue3,
                                                request.intValue5,
                                                reply.contents,
                                                0,
                                                request.intValue7);
                                            os.writeObject(reply);
                                            break;
                                        case UART_GET_BYTE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply = ((UARTPort) ioPorts[request.intValue]).getByte();
                                            os.writeObject(reply);
                                            break;
                                        case UART_GET_BYTES:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.contents = new byte[request.intValue2];
                                            ((UARTPort) ioPorts[request.intValue]).getBytes(reply.contents, 0, request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case UART_GET_SHORT:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply = ((UARTPort) ioPorts[request.intValue]).getShort();
                                            os.writeObject(reply);
                                            break;
                                        case UART_GET_SHORTS:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.shorts = new short[request.intValue2];
                                            ((UARTPort) ioPorts[request.intValue]).getShorts(reply.shorts, 0, request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case UART_INITIALISE_SENSOR:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.result = ((UARTPort) ioPorts[request.intValue]).initialiseSensor(request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case UART_RESET_SENSOR:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            ((UARTPort) ioPorts[request.intValue]).resetSensor();
                                            break;
                                        case UART_SET_MODE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.result = ((UARTPort) ioPorts[request.intValue]).setMode(request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case UART_WRITE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply = ((UARTPort) ioPorts[request.intValue]).write(request.byteData, request.intValue2, request.intValue3);
                                            os.writeObject(reply);
                                            break;
                                        case UART_RAW_WRITE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply =
                                                ((UARTPort) ioPorts[request.intValue]).rawWrite(request.byteData, request.intValue2, request.intValue3);
                                            os.writeObject(reply);
                                            break;
                                        case UART_RAW_READ:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply =
                                                ((UARTPort) ioPorts[request.intValue]).rawRead(request.byteData, request.intValue2, request.intValue3);
                                            os.writeObject(reply);
                                            break;
                                        case UART_SET_BIT_RATE:
                                            if ( ioPorts[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            ((UARTPort) ioPorts[request.intValue]).setBitRate(request.intValue2);
                                            break;
                                        case CREATE_REGULATED_MOTOR:
                                            System.out.println("Creating motor on port " + request.str);
                                            Port p = LocalEV3.get().getPort(request.str); // port name
                                            RegulatedMotor motor = null;
                                            switch ( request.ch ) {
                                                case 'N':
                                                    motor = new NXTRegulatedMotor(p);
                                                    break;
                                                case 'L':
                                                    motor = new EV3LargeRegulatedMotor(p);
                                                    break;
                                                case 'M':
                                                    motor = new EV3MediumRegulatedMotor(p);
                                                    break;
                                                case 'G':
                                                    motor = new MindsensorsGlideWheelMRegulatedMotor(p);
                                            }
                                            motors[request.str.charAt(0) - 'A'] = motor;
                                            break;
                                        case MOTOR_FORWARD:
                                            motors[request.intValue].forward();
                                            break;
                                        case MOTOR_BACKWARD:
                                            motors[request.intValue].backward();
                                            break;
                                        case MOTOR_STOP:
                                            motors[request.intValue].stop();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_FLT:
                                            motors[request.intValue].flt();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_IS_MOVING:
                                            reply.result = motors[request.intValue].isMoving();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_GET_ROTATION_SPEED:
                                            reply.reply = motors[request.intValue].getRotationSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_GET_TACHO_COUNT:
                                            reply.reply = motors[request.intValue].getTachoCount();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_RESET_TACHO_COUNT:
                                            motors[request.intValue].resetTachoCount();
                                            break;
                                        case MOTOR_STOP_IMMEDIATE:
                                            motors[request.intValue].stop(request.flag);
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_FLT_IMMEDIATE:
                                            motors[request.intValue].flt(request.flag);
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_WAIT_COMPLETE:
                                            motors[request.intValue].waitComplete();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_ROTATE:
                                            System.out.println("Rotating port " + request.intValue + " by " + request.intValue2);
                                            motors[request.intValue].rotate(request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_ROTATE_IMMEDIATE:
                                            motors[request.intValue].rotate(request.intValue2, request.flag);
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case MOTOR_ROTATE_TO:
                                            motors[request.intValue].rotateTo(request.intValue2);
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_ROTATE_TO_IMMEDIATE:
                                            motors[request.intValue].rotateTo(request.intValue2, request.flag);
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case MOTOR_GET_LIMIT_ANGLE:
                                            reply.reply = motors[request.intValue].getLimitAngle();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_GET_SPEED:
                                            reply.reply = motors[request.intValue].getSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_SET_SPEED:
                                            motors[request.intValue].setSpeed(request.intValue2);
                                            break;
                                        case MOTOR_GET_MAX_SPEED:
                                            reply.floatReply = motors[request.intValue].getMaxSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_IS_STALLED:
                                            reply.result = motors[request.intValue].isStalled();
                                            os.writeObject(reply);
                                            break;
                                        case MOTOR_SET_STALL_THRESHOLD:
                                            motors[request.intValue].setStallThreshold(request.intValue, request.intValue2);
                                            break;
                                        case MOTOR_SET_ACCELERATION:
                                            motors[request.intValue].setAcceleration(request.intValue2);
                                            break;
                                        case MOTOR_CLOSE:
                                            motors[request.intValue].close();
                                            os.writeObject(reply);
                                            break;
                                        case CREATE_SAMPLE_PROVIDER_PUBLISH:
                                        case CREATE_SAMPLE_PROVIDER:
                                            float frequency = (request.request == EV3Request.Request.CREATE_SAMPLE_PROVIDER_PUBLISH ? request.floatValue : 0f);
                                            System.out.println("Creating " + request.str + " on " + request.str2 + " with mode " + request.str3);
                                            Class<?> c = Class.forName(request.str); // sensor class
                                            Class<?>[] params = new Class<?>[1];
                                            params[0] = Port.class;
                                            Constructor<?> con = c.getConstructor(params);
                                            Object[] args = new Object[1];
                                            args[0] = LocalEV3.get().getPort(request.str2); // port name
                                            BaseSensor sensor = (BaseSensor) con.newInstance(args);
                                            SampleProvider provider;
                                            if ( request.str3 == null ) {
                                                provider = sensor;
                                            } else {
                                                provider = sensor.getMode(request.str3);
                                            }
                                            int pn = request.str2.charAt(1) - '1';
                                            if ( frequency > 0 ) {
                                                providers[pn] = new PublishFilter(provider, request.str4, frequency);
                                            } else {
                                                providers[pn] = provider;
                                            }
                                            sensors[pn] = sensor;
                                            os.writeObject(reply);
                                            break;
                                        case SAMPLE_SIZE:
                                            if ( providers[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.reply = providers[request.intValue].sampleSize();
                                            os.writeObject(reply);
                                            break;
                                        case FETCH_SAMPLE:
                                            if ( providers[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            reply.floats = new float[providers[request.intValue].sampleSize()];
                                            providers[request.intValue].fetchSample(reply.floats, 0);
                                            os.writeObject(reply);
                                            break;
                                        case CLOSE_SENSOR:
                                            if ( sensors[request.intValue] == null ) {
                                                throw new PortException("Port not open");
                                            }
                                            sensors[request.intValue].close();
                                            break;
                                        case CREATE_PILOT:
                                            pilotLeftMotor = request.str.charAt(0) - 'A';
                                            pilotRightMotor = request.str2.charAt(0) - 'A';
                                            pilot =
                                                new DifferentialPilot(
                                                    request.doubleValue,
                                                    request.doubleValue2,
                                                    motors[pilotLeftMotor],
                                                    motors[pilotRightMotor],
                                                    false);
                                            os.writeObject(reply);
                                            break;
                                        case CLOSE_PILOT:
                                            if ( motors[pilotLeftMotor] != null ) {
                                                motors[pilotLeftMotor].close();
                                            }
                                            if ( motors[pilotRightMotor] != null ) {
                                                motors[pilotRightMotor].close();
                                            }
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_GET_MIN_RADIUS:
                                            reply.doubleReply = pilot.getMinRadius();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_SET_MIN_RADIUS:
                                            pilot.setMinRadius(request.doubleValue);
                                            break;
                                        case PILOT_ARC_FORWARD:
                                            pilot.arcForward(request.doubleValue);
                                            break;
                                        case PILOT_ARC_BACKWARD:
                                            pilot.arcBackward(request.doubleValue);
                                            break;
                                        case PILOT_ARC:
                                            pilot.arc(request.doubleValue, request.doubleValue2);
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_ARC_IMMEDIATE:
                                            pilot.arc(request.doubleValue, request.doubleValue2, request.flag);
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case PILOT_TRAVEL_ARC:
                                            pilot.travelArc(request.doubleValue, request.doubleValue2);
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_TRAVEL_ARC_IMMEDIATE:
                                            pilot.travelArc(request.doubleValue, request.doubleValue2, request.flag);
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case PILOT_FORWARD:
                                            pilot.forward();
                                            break;
                                        case PILOT_BACKWARD:
                                            pilot.backward();
                                            break;
                                        case PILOT_STOP:
                                            pilot.stop();
                                            break;
                                        case PILOT_IS_MOVING:
                                            reply.result = pilot.isMoving();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_TRAVEL:
                                            pilot.travel(request.doubleValue);
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_TRAVEL_IMMEDIATE:
                                            pilot.travel(request.doubleValue, request.flag);
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case PILOT_SET_LINEAR_SPEED:
                                            pilot.setLinearSpeed(request.doubleValue);
                                            break;
                                        case PILOT_GET_LINEAR_SPEED:
                                            reply.doubleReply = pilot.getLinearSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_SET_LINEAR_ACCELERATION:
                                            pilot.setLinearAcceleration(request.doubleValue);
                                            break;
                                        case PILOT_GET_LINEAR_ACCELERATION:
                                            reply.doubleReply = pilot.getLinearAcceleration();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_GET_MAX_LINEAR_SPEED:
                                            reply.doubleReply = pilot.getLinearSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_GET_MOVEMENT:
                                            break;
                                        case PILOT_ROTATE:
                                            pilot.rotate(request.doubleValue);
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_ROTATE_IMMEDIATE:
                                            if ( request.doubleValue == Double.POSITIVE_INFINITY ) {
                                                pilot.rotateRight();
                                            } else if ( request.doubleValue == Double.NEGATIVE_INFINITY ) {
                                                pilot.rotateLeft();
                                            } else {
                                                pilot.rotate(request.doubleValue, request.flag);
                                            }
                                            if ( !request.flag ) {
                                                os.writeObject(reply);
                                            }
                                            break;
                                        case PILOT_GET_ANGULAR_SPEED:
                                            reply.doubleReply = pilot.getAngularSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_SET_ANGULAR_SPEED:
                                            pilot.setAngularSpeed(request.doubleValue);
                                            break;
                                        case PILOT_GET_MAX_ANGULAR_SPEED:
                                            reply.doubleReply = pilot.getMaxAngularSpeed();
                                            os.writeObject(reply);
                                            break;
                                        case PILOT_STEER:
                                            pilot.steer(request.doubleValue);
                                            break;
                                        default:
                                            break;
                                    }
                                } catch ( Exception e ) {
                                    e.printStackTrace();
                                    if ( request.replyRequired ) {
                                        reply.e = e;
                                        os.writeObject(reply);
                                    }
                                }
                            }
                        }

                    } catch ( SocketException e ) {
                        System.out.println("Error reading from remote request socket: " + e);
                        try {
                            conn.close();
                        } catch ( IOException e1 ) {
                            System.err.println("Error closing connection: " + e);
                        }
                    }

                } catch ( Exception e ) {
                    System.err.println("Error accepting connection " + e);
                }
            }
        }
    }

    /**
     * Present the Bluetooth menu to the user.
     */
    private void bluetoothMenu() {
        // Check if BT initialisation is complete
        if ( bt == null ) {
            msg("BT not started");
            return;
        }

        int selection = 0;
        GraphicMenu menu = new GraphicMenu(null, null, 4);
        boolean visible = false;
        do {
            newScreen("Bluetooth");
            visible = bt.getVisibility();
            System.out.println("Visibility is " + visible);

            lcd.drawString("Visibility", 0, 2);
            lcd.drawString(visible ? "on" : "off", 11, 2);
            menu.setItems(new String[] {
                "Search/Pair",
                "Devices",
                "Visibility",
                "Change PIN",
                "Information"
            }, new String[] {
                ICSearch,
                ICEV3,
                ICVisibility,
                ICPIN,
                ICInfo
            });
            selection = getSelection(menu, selection);
            switch ( selection ) {
                case 0:
                    bluetoothSearch();
                    break;
                case 1:
                    bluetoothDevices();
                    break;
                case 2:
                    visible = !visible;
                    this.btVisibility = visible;
                    System.out.println("Setting visibility to " + this.btVisibility);
                    try {
                        bt.setVisibility(this.btVisibility);
                    } catch ( BluetoothException e ) {
                        System.err.println("Failed to set visibility: " + e);
                    }
                    //updateBTIcon();
                    this.ind.updateNow();
                    break;
                case 3:
                    bluetoothChangePIN();
                    break;
                case 4:
                    bluetoothInformation();
            }
        } while ( selection >= 0 );
    }

    /**
     * Clears the screen, displays a number and allows user to change
     * the digits of the number individually using the EV3 buttons.
     * Note the array of bytes represent ASCII characters, not actual numbers.
     *
     * @param digits Number of digits in the PIN.
     * @param title The text to display above the numbers.
     * @param number Start with a default PIN. Array of bytes up to 8 length.
     * @return
     */
    private boolean enterNumber(String title, byte[] number, int digits) {
        // !! Should probably check to make sure defaultNumber is digits in size
        int curDigit = 0;

        while ( true ) {
            newScreen();
            lcd.drawString(title, 0, 2);
            for ( int i = 0; i < digits; i++ ) {
                lcd.drawChar((char) number[i], i * 2 + 1, 4);
            }

            if ( curDigit >= digits ) {
                return true;
            }

            Utils.drawRect(curDigit * 20 + 3, 60, 20, 20);

            int ret = getButtonPress();
            switch ( ret ) {
                case Button.ID_ENTER: { // ENTER
                    return true;

                }
                case Button.ID_DOWN: { // LEFT
                    number[curDigit]--;
                    if ( number[curDigit] < '0' ) {
                        number[curDigit] = '9';
                    }
                    break;
                }
                case Button.ID_UP: { // RIGHT
                    number[curDigit]++;
                    if ( number[curDigit] > '9' ) {
                        number[curDigit] = '0';
                    }
                    break;
                }
                case Button.ID_LEFT: { // LEFT
                    if ( --curDigit < 0 ) {
                        curDigit = digits - 1;
                    }
                    break;
                }
                case Button.ID_RIGHT: { // RIGHT
                    if ( ++curDigit >= digits ) {
                        curDigit = 0;
                    }
                    break;
                }
                case Button.ID_ESCAPE: { // ESCAPE
                    curDigit--;
                    // Return false if user backs out
                    if ( curDigit < 0 ) {
                        return false;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Allow the user to change the Bluetooth PIN.
     */
    private void bluetoothChangePIN() {
        // 1. Retrieve PIN from System properties
        String pinStr = SystemSettings.getStringSetting(pinProperty, "1234");
        int len = pinStr.length();
        byte[] pin = new byte[len];
        for ( int i = 0; i < len; i++ ) {
            pin[i] = (byte) pinStr.charAt(i);
        }

        // 2. Call enterNumber() method
        if ( enterNumber("Enter EV3 PIN", pin, 4) ) {
            // 3. Set PIN in system memory.
            StringBuilder sb = new StringBuilder();
            for ( int i = 0; i < pin.length; i++ ) {
                sb.append((char) pin[i]);
            }

            try {
                PrintStream out = new PrintStream(new FileOutputStream("/etc/bluetooth/btpin"));
                out.println(sb.toString());
                out.close();
            } catch ( IOException e ) {
                System.out.println("Failed to write pin to /etc/bluetooth/btpin: " + e);
            }

            // 4. Run startbt to restart the agent with the new pin
            try {
                lcd.clear();
                lcd.drawString("Restarting agent", 0, 1);
                Process p = Runtime.getRuntime().exec(START_BLUETOOTH);
                int status = p.waitFor();
                System.out.println("startbt returned " + status);
            } catch ( Exception e ) {
                System.err.println("Failed to execute startbt: " + e);
            }
        }
    }

    /**
     * Perform the Bluetooth search operation
     * Search for Bluetooth devices
     * Present those that are found
     * Allow pairing
     */
    private void bluetoothSearch() {
        newScreen("Searching");
        ArrayList<RemoteBTDevice> devList;
        devList = null;
        try {
            devList = (ArrayList<RemoteBTDevice>) bt.search();
        } catch ( BluetoothException e ) {
            System.out.println("Search exeception " + e);
            devList = null;
        }
        if ( devList == null || devList.size() <= 0 ) {
            msg("No devices found");
            return;
        }

        String[] names = new String[devList.size()];
        String[] icons = new String[devList.size()];
        for ( int i = 0; i < devList.size(); i++ ) {
            RemoteBTDevice btrd = devList.get(i);
            names[i] = btrd.getName();
            //icons[i] = getDeviceIcon(btrd.getDeviceClass());
        }
        GraphicListMenu searchMenu = new GraphicListMenu(names, icons);
        GraphicMenu subMenu = new GraphicMenu(new String[] {
            "Pair"
        }, new String[] {
            ICYes
        }, 4);
        int selected = 0;
        do {
            newScreen("Found");
            selected = getSelection(searchMenu, selected);
            if ( selected >= 0 ) {
                RemoteBTDevice btrd = devList.get(selected);
                newScreen();
                //LCD.bitBlt(
                //	Utils.stringToBytes8(getDeviceIcon(btrd.getDeviceClass()))
                //	, 7, 7, 0, 0, 2, 16, 7, 7, LCD.ROP_COPY);
                lcd.drawString(names[selected], 2, 2);
                lcd.drawString(btrd.getAddress(), 0, 3);
                int subSelection = getSelection(subMenu, 0);
                if ( subSelection == 0 ) {
                    newScreen("Pairing");
                    //Bluetooth.addDevice(btrd);
                    // !! Assuming 4 length
                    byte[] pin = {
                        '0',
                        '0',
                        '0',
                        '0'
                    };
                    if ( !enterNumber("PIN for " + btrd.getName(), pin, pin.length) ) {
                        break;
                    }
                    lcd.drawString("Please wait...", 0, 6);
                    try {
                        Bluetooth.getLocalDevice().authenticate(btrd.getAddress(), new String(pin));
                        // Indicate Success or failure:
                        lcd.drawString("Paired!      ", 0, 6);
                    } catch ( Exception e ) {
                        System.err.println("Failed to pair:" + e);
                        lcd.drawString("UNSUCCESSFUL  ", 0, 6);
                        //Bluetooth.removeDevice(btrd);
                    }
                    lcd.drawString("Press any key", 0, 7);
                    getButtonPress();
                }
            }
        } while ( selected >= 0 );
    }

    /**
     * Display all currently known Bluetooth devices.
     */
    private void bluetoothDevices() {
        List<RemoteBTDevice> devList = (List<RemoteBTDevice>) Bluetooth.getLocalDevice().getPairedDevices();
        if ( devList.size() <= 0 ) {
            msg("No known devices");
            return;
        }

        newScreen("Devices");
        String[] names = new String[devList.size()];
        String[] icons = new String[devList.size()];
        int i = 0;
        for ( RemoteBTDevice btrd : devList ) {
            names[i] = btrd.getName();
            i++;
        }

        GraphicListMenu deviceMenu = new GraphicListMenu(names, icons);
        GraphicMenu subMenu = new GraphicMenu(new String[] {
            "Remove"
        }, new String[] {
            ICDelete
        }, 4);
        int selected = 0;
        do {
            newScreen();
            selected = getSelection(deviceMenu, selected);
            if ( selected >= 0 ) {
                newScreen();
                RemoteBTDevice btrd = devList.get(selected);
                @SuppressWarnings("unused")
                int devclass = btrd.getDeviceClass();
                lcd.drawString(btrd.getName(), 2, 2);
                lcd.drawString(btrd.getAddress(), 0, 3);
                // TODO device class is overwritten by menu
                //LCD.drawString("0x"+Integer.toHexString(devclass), 0, 4);
                int subSelection = getSelection(subMenu, 0);
                if ( subSelection == 0 ) {
                    try {
                        Bluetooth.getLocalDevice().removeDevice(btrd.getAddress());
                        // Indicate Success or failure:
                        lcd.drawString("Deleted!      ", 0, 6);
                    } catch ( Exception e ) {
                        System.err.println("Failed to delete:" + e);
                        lcd.drawString("UNSUCCESSFUL  ", 0, 6);
                    }
                    lcd.drawString("Press any key", 0, 7);
                    getButtonPress();
                }
            }
        } while ( selected >= 0 );
    }

    private static final String[] bluetoothVersions = {
        "1.0b",
        "1.1",
        "1.2",
        "2.0",
        "2.1",
        "3.0",
        "4.0"
    };

    /**
     * Display Bluetooth information
     */
    private void bluetoothInformation() {
        newScreen("Information");
        LocalBTDevice lbt = Bluetooth.getLocalDevice();
        LocalVersion ver = lbt.getLocalVersion();
        String v = ver.hci_ver >= bluetoothVersions.length ? "" : bluetoothVersions[ver.hci_ver];
        lcd.drawString("HCI " + v + " " + Integer.toHexString(ver.hci_ver) + "/" + Integer.toHexString(ver.hci_rev), 0, 2);
        v = ver.lmp_ver >= bluetoothVersions.length ? "n/a" : bluetoothVersions[ver.lmp_ver];
        lcd.drawString("LMP " + v + " " + Integer.toHexString(ver.lmp_ver) + "/" + Integer.toHexString(ver.lmp_subver), 0, 3);
        lcd.drawString("ID: " + lbt.getFriendlyName(), 0, 5);
        lcd.drawString(lbt.getBluetoothAddress(), 0, 6);
        getButtonPress();
    }

    /**
     * Run the default program (if set).
     */
    private void mainRunDefault() {
        File file = getDefaultProgram();
        if ( file == null ) {
            msg("No default set");
        } else {
            System.out.println("Executing default program " + file.getPath());
            try {
                JarFile jar = new JarFile(file);
                String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
                jar.close();
                exec(file, JAVA_RUN_CP + file.getPath() + " lejos.internal.ev3.EV3Wrapper " + mainClass, PROGRAMS_DIRECTORY);
            } catch ( IOException e ) {
                System.err.println("Exception running program");
            }
        }
    }

    /**
     * Load maven properties file from within jar.
     *
     * @return maven properties
     */
    private static Properties loadProperties() {
        Properties menuProperties = new Properties();
        try {
            menuProperties.load(ClassLoader.getSystemResourceAsStream("EV3Menu.properties"));
            return menuProperties;
        } catch ( IOException e ) {
            System.out.println(e.getMessage());
            return menuProperties;
        }
    }

    /**
     * Load Open Roberta Properties from /home/roberta/openroberta.properties .
     * Used for storing the menu type, the last server address and the last used token for debugging in NEPO program.
     * The file is created during runtime. Default menu type is set and taken from maven properties.
     */
    private static void loadOpenRobertaProperties() {
        File f = new File(OPENROBERTAPROPERTIES);
        try {
            if ( !f.exists() ) {
                openrobertaProperties = new Properties();
                openrobertaProperties.setProperty("menutype", menuProperties.getProperty("menutype"));
                storeOpenRobertaProperties();
            } else {
                openrobertaProperties = new Properties();
                openrobertaProperties.load(new FileInputStream(f));
            }
        } catch ( IOException e ) {
            System.out.println(e.getMessage());
            return;
        }
    }

    /**
     * Save Open Roberta Lab Properties to /home/roberta/openroberta.properties .
     */
    private static void storeOpenRobertaProperties() {
        try {
            File f = new File(OPENROBERTAPROPERTIES);
            OutputStream os = new FileOutputStream(f);
            openrobertaProperties.store(os, "Open Roberta Lab Settings");
            os.close();
        } catch ( IOException e ) {
            System.out.println(e.getMessage());
            return;
        }
    }

    /**
     * Roberta submenu implementation.
     */
    private void robertaMenu() {
        if ( ORAhandler.isRestarted() == false ) {
            newScreen(OPENROBERTAHEAD);
            lcd.drawString("Firmware updated!", 0, 2);
            lcd.drawString("Please restart", 0, 4);
            lcd.drawString("the brick first!", 0, 5);
            Delay.msDelay(3000);
            return;
        }

        loadOpenRobertaProperties();

        if ( ORAhandler.isRegistered() == false ) {
            if ( this.indiBA.getWifi() == false ) {
                newScreen(OPENROBERTAHEAD);
                if ( getYesNo(" Set up WLAN now?", true) == 1 ) {
                    wifiMenu();
                }
            }

            String ip = "";
            String menutype = openrobertaProperties.getProperty("menutype");
            if ( menutype.equals("custom") ) {
                ip = getIPAddress();
                if ( ip.equals("") ) {
                    return;
                }
            } else if ( menutype.equals("standard") ) {
                ip = "lab.open-roberta.org";
            }

            String token = new ORAtokenGenerator().generateToken();
            openrobertaProperties.setProperty("lasttoken", token);
            openrobertaProperties.setProperty("lastaddress", ip);
            storeOpenRobertaProperties();
            oraHandler.startServerCommunicator(ip, token);

            newScreen(OPENROBERTAHEAD);
            lcd.drawString("    Wating for", 0, 2);
            lcd.drawString("  registration...", 0, 3);
            lcd.drawString("     " + token, 0, 5);

            while ( ORAhandler.isRegistered() == false ) {
                int id = Button.waitForAnyEvent(500);
                if ( ORAhandler.hasConnectionError() ) {
                    oraHandler.disconnect();
                    int wifistate = startWlanInterface();
                    newScreen(OPENROBERTAHEAD);
                    lcd.drawString("Open Roberta Lab", 0, 1);
                    lcd.drawString("is unreachable!", 0, 2);
                    lcd.drawString("Reason:", 0, 4);
                    switch ( wifistate ) {
                        case 1:
                            lcd.drawString("No Wifi dongle", 0, 5);
                            lcd.drawString("plugged in!", 0, 6);
                            break;
                        case 0:
                            if ( wlanAddress == null ) {
                                lcd.drawString("Not connected to", 0, 5);
                                lcd.drawString("any Wifi network!", 0, 6);
                            } else {
                                lcd.drawString("No access from", 0, 5);
                                lcd.drawString("this network!", 0, 6);
                            }
                            break;
                        default:
                            break;
                    }

                    lcd.drawString(" (Press any key)", 0, 7);
                    LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
                    LocalEV3.get().getKeys().waitForAnyPress();
                    Delay.msDelay(500);
                    return;
                } else if ( ORAhandler.hasTimeout() ) {
                    oraHandler.disconnect();
                    newScreen(OPENROBERTAHEAD);
                    lcd.drawString("     Timeout!", 0, 2);
                    lcd.drawString("   Try again...", 0, 3);
                    lcd.drawString(" (press any key)", 0, 5);
                    LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
                    LocalEV3.get().getKeys().waitForAnyPress();
                    Delay.msDelay(500);
                    return;
                } else if ( id == Button.ID_ESCAPE ) {
                    oraHandler.disconnect();
                    newScreen(OPENROBERTAHEAD);
                    lcd.drawString("    Canceled!", 0, 3);
                    Delay.msDelay(2000);
                    return;
                }
            }
            newScreen(OPENROBERTAHEAD);
            lcd.drawString("     Success!", 0, 3);
            LocalEV3.get().getAudio().systemSound(Sounds.ASCENDING);
            Delay.msDelay(2000);
        } else {
            oraMenu();
        }
    }

    /**
     * Get the MAC address from wlan0 interface to send to Open Roberta Lab server.
     * Used for deleting "old" sessions.
     *
     * @return MAC address
     */
    public static String getWlanMACaddress() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while ( interfaces.hasMoreElements() ) {
                NetworkInterface current = interfaces.nextElement();
                if ( !current.isUp() || current.isLoopback() || current.isVirtual() ) {
                    continue;
                }
                if ( current.getDisplayName().equals("wlan0") ) {
                    return convertToReadableMAC(current.getHardwareAddress());
                }
            }
            return "usb";
        } catch ( SocketException e ) {
            return "unknown";
        }
    }

    /**
     * Convert MAC address of bytearray to a readable format.
     *
     * @param macRaw Mac address as bytearray
     * @return MAC address as String
     */
    private static String convertToReadableMAC(byte[] macRaw) {
        if ( macRaw != null ) {
            StringBuilder sb = new StringBuilder();
            for ( int i = 0; i < macRaw.length; i++ ) {
                sb.append(String.format("%02X%s", macRaw[i], (i < macRaw.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * Get Open Roberta Lab EV3Menu version from EV3Menu.properties in jar file.
     * This will be send to the server to check if the brick firmware is up-to-date.
     * This method crashes if the brick firmware was updated but not restarted.
     * Classloader will not find properties file in the jar in this case.
     *
     * @return Open Roberta Lab menu version
     */
    public static String getORAmenuVersion() {
        String tmp = menuProperties.getProperty("version");
        tmp = tmp.substring(0, tmp.indexOf("-"));
        return tmp;
    }

    /**
     * Get the leJOS Firmware version.
     *
     * @return
     */
    public static String getLejosVersion() {
        return version;
    }

    /**
     * Get the name of the brick to send it to ORA server t odisplay it on client page.
     *
     * @return brickname
     */
    public static String getBrickName() {
        return hostname;
    }

    /**
     * Get the battery status to send it to ORA server to display it on client page.
     *
     * @return battery
     */
    public static String getBatteryStatus() {
        float volts = LocalEV3.ev3.getPower().getVoltage();
        return String.format("%3.1f", volts);
    }

    /**
     * Expose userprogram process to check if it is running (!= null).
     * Do not allow execution of a second userprogram from ORA.
     *
     * @return
     */
    public static Process getUserprogram() {
        return program;
    }

    /**
     * Requests an IP Address from the user where the server is running.<br>
     * For developer version only.
     */
    private String getIPAddress() {
        String customaddress = "";
        if ( openrobertaProperties != null ) {
            customaddress = openrobertaProperties.getProperty("customaddress");
        }
        if ( customaddress == null || customaddress.equals("") ) {
            customaddress = "";
        }

        int i = 1;
        newScreen("Server?");
        lcd.drawString("lab.open-roberta.org", 0, 1, true);
        lcd.drawString("10.0.1.10:1999", 0, 2, false);
        lcd.drawString(customaddress, 0, 3, false);
        lcd.drawString("Another (type in)", 0, 4, false);
        lcd.drawString("(ESCAPE to exit)", 0, 7);

        while ( true ) {
            int id = Button.waitForAnyEvent(500);
            if ( id == Button.ID_ENTER ) {
                customaddress = select(i, customaddress);
                if ( customaddress == null ) {
                    return enterIP();
                } else {
                    return customaddress;
                }
            }
            if ( id == Button.ID_ESCAPE ) {
                return "";
            }
            if ( id == Button.ID_DOWN || id == Button.ID_RIGHT ) {
                if ( i != 4 ) {
                    rewrite(i, false, customaddress);
                    i++;
                    rewrite(i, true, customaddress);
                } else {
                    rewrite(i, false, customaddress);
                    i = 1;
                    rewrite(i, true, customaddress);
                }
            }
            if ( id == Button.ID_UP || id == Button.ID_LEFT ) {
                if ( i != 1 ) {
                    rewrite(i, false, customaddress);
                    i--;
                    rewrite(i, true, customaddress);
                } else {
                    rewrite(i, false, customaddress);
                    i = 4;
                    rewrite(i, true, customaddress);
                }
            }
        }
    }

    private void rewrite(int i, boolean invert, String customaddress) {
        switch ( i ) {
            case 1:
                lcd.drawString("lab.open-roberta.org", 0, 1, invert);
                break;
            case 2:
                lcd.drawString("10.0.1.10:1999", 0, 2, invert);
                break;
            case 3:
                if ( customaddress.equals("") ) {
                    lcd.drawString(customaddress + " ", 0, 3, invert);
                } else {
                    lcd.drawString(customaddress, 0, 3, invert);
                }
                break;
            case 4:
                lcd.drawString("Another (type in)", 0, 4, invert);
                break;
        }
    }

    private String select(int i, String temp) {
        String official = "lab.open-roberta.org";
        String usb = "10.0.1.10:1999";
        switch ( i ) {
            case 1:
                return official;
            case 2:
                return usb;
            case 3:
                return temp;
            default:
                return null;
        }
    }

    private String enterIP() {
        newScreen(" Enter IP");
        String customaddress = new ORAipKeyboard().getString();
        if ( !customaddress.equals("") ) {
            openrobertaProperties.setProperty("customaddress", customaddress);
            storeOpenRobertaProperties();
        }
        return customaddress;
    }

    /**
     * Submenu which is accessible from the main menu if the brick is connected to Open Roberta Lab.
     * Shows entries for displaying firmware informations and for disconnecting the brick.
     */
    private void oraMenu() {
        GraphicMenu menu = new GraphicMenu(new String[] {
            "Disconnect"
        }, new String[] {
            ICRoberta
        }, 3);
        int selected = 0;
        do {
            newScreen(OPENROBERTAHEAD);
            selected = getSelection(menu, selected);
            switch ( selected ) {
                case 0:
                    newScreen(OPENROBERTAHEAD);
                    if ( orUSBconnected == true ) {
                        newScreen(OPENROBERTAHEAD);
                        lcd.drawString(" Use USB program", 0, 2);
                        lcd.drawString(" to disconnect!", 0, 3);
                        Delay.msDelay(3000);
                        break;
                    }
                    // check if brick is already disconnected by USB program.
                    if ( ORAhandler.isRegistered() == false ) {
                        return;
                    }
                    if ( getYesNo("   Disconnect?", true) == 1 ) {
                        disconnect();
                        return;
                    }
                    break;
            }
        } while ( selected >= 0 );
    }

    private void disconnect() {
        oraHandler.disconnect();
        newScreen(OPENROBERTAHEAD);
        lcd.drawString("  Disconnected!", 0, 3);
        LocalEV3.get().getAudio().systemSound(Sounds.DESCENDING);
        Delay.msDelay(2000);
    }

    /**
     * Present the system menu.
     * Allow the user to format the filesystem. Change timeouts and control
     * the default program usage.
     */
    private void systemMenu() {
        String[] menuData = {
            "Toggle Menu",
            "Delete all",
            "",
            "Auto Run",
            "Change name",
            "NTP host",
            "Suspend menu",
            "Reset",
            "Unset default"
        };
        String[] iconData = {
            ICRoberta,
            ICFormat,
            ICSleep,
            ICAutoRun,
            ICDefault,
            ICDefault,
            ICDefault,
            ICDefault,
            ICDefault
        };
        boolean rechargeable = false;
        GraphicMenu menu = new GraphicMenu(menuData, iconData, 4);
        int selection = 0;
        do {
            newScreen("System");
            lcd.drawString("Battery", 0, 1);
            float volts = LocalEV3.ev3.getPower().getVoltage();
            lcd.drawString(String.format("%6.3f", volts), 10, 1);
            if ( rechargeable ) {
                lcd.drawString("R", 16, 1);
            }
            lcd.drawString("Current", 0, 2);
            float current = LocalEV3.ev3.getPower().getBatteryCurrent();
            lcd.drawString(String.format("%6.3f", current), 10, 2);
            menuData[2] = "Sleep time: " + (this.timeout == 0 ? "off" : String.valueOf(this.timeout));
            File f = getDefaultProgram();
            if ( f == null ) {
                menuData[8] = null;
                iconData[8] = null;
            }
            if ( openrobertaProperties == null ) {
                loadOpenRobertaProperties();
            }
            menuData[0] = "Menu: " + openrobertaProperties.getProperty("menutype");
            menu.setItems(menuData, iconData);
            selection = getSelection(menu, selection);
            switch ( selection ) {
                case 0:
                    String menuType = openrobertaProperties.getProperty("menutype");
                    if ( menuType.equals("custom") ) {
                        openrobertaProperties.setProperty("menutype", "standard");
                        storeOpenRobertaProperties();
                    } else if ( menuType.equals("standard") ) {
                        openrobertaProperties.setProperty("menutype", "custom");
                        storeOpenRobertaProperties();
                    }
                    newScreen("System");
                    lcd.drawString("Menu type set to", 0, 2);
                    lcd.drawString("    >" + openrobertaProperties.getProperty("menutype") + "<", 0, 4);
                    Delay.msDelay(2500);
                    break;
                case 1:
                    if ( getYesNo("Delete all files?", false) == 1 ) {
                        File dir = new File(PROGRAMS_DIRECTORY);
                        for ( String fn : dir.list() ) {
                            File aFile = new File(dir, fn);
                            System.out.println("Deleting file " + aFile.getPath());
                            aFile.delete();
                        }
                        dir = new File(LIB_DIRECTORY);
                        for ( String fn : dir.list() ) {
                            File aFile = new File(dir, fn);
                            System.out.println("Deleting lib " + aFile.getPath());
                            aFile.delete();
                        }
                    }
                    break;
                case 2:
                    System.out.println("Timeout = " + this.timeout);
                    System.out.println("Max sleep time = " + maxSleepTime);
                    //timeout++;
                    if ( this.timeout > maxSleepTime ) {
                        this.timeout = 0;
                    }
                    Settings.setProperty(sleepTimeProperty, String.valueOf(this.timeout));
                    break;
                case 3:
                    systemAutoRun();
                    break;
                case 4:
                    String newName = Keyboard.getString();

                    if ( newName != null ) {
                        setName(newName);
                    }
                    break;
                case 5:
                    String host = Keyboard.getString();

                    if ( host != null ) {
                        Settings.setProperty(ntpProperty, host);
                    }
                    break;
                case 6:
                    suspend();
                    System.out.println("Menu suspended");
                    while ( true ) {
                        int b = Button.getButtons();
                        if ( b == 6 ) {
                            break;
                        }
                        Delay.msDelay(200);
                    }
                    resume();
                    System.out.println("Menu resumed");
                    break;
                case 7:
                    EV3IOPort.closeAll();
                    selection = 0;
                    break;
                case 8:
                    Settings.setProperty(defaultProgramProperty, "");
                    Settings.setProperty(defaultProgramAutoRunProperty, "");
                    selection = 0;
                    break;
            }
        } while ( selection >= 0 );
    }

    /**
     * Present details of the default program
     * Allow the user to specify run on system start etc.
     */
    private void systemAutoRun() {
        File f = getDefaultProgram();
        if ( f == null ) {
            msg("No default set");
            return;
        }

        newScreen("Auto Run");
        lcd.drawString("Default Program:", 0, 2);
        lcd.drawString(f.getName(), 1, 3);

        String current = Settings.getProperty(defaultProgramAutoRunProperty, "");
        int selection = getYesNo("Run at power up?", current.equals("ON"));
        if ( selection >= 0 ) {
            Settings.setProperty(defaultProgramAutoRunProperty, selection == 0 ? "OFF" : "ON");
        }
    }

    /**
     * Ask the user for confirmation of an action.
     *
     * @param prompt A description of the action about to be performed
     * @return 1=yes 0=no < 0 escape
     */
    private int getYesNo(String prompt, boolean yes) {
        //    	lcd.bitBlt(null, 178, 64, 0, 0, 0, 64, 178, 64, CommonLCD.ROP_CLEAR);
        GraphicMenu menu = new GraphicMenu(new String[] {
            "No",
            "Yes"
        }, new String[] {
            ICNo,
            ICYes
        }, 4, prompt, 3);
        return getSelection(menu, yes ? 1 : 0);
    }

    /**
     * Get the default program as a file
     */
    private static File getDefaultProgram() {
        String file = Settings.getProperty(defaultProgramProperty, "");
        if ( file != null && file.length() > 0 ) {
            File f = new File(file);
            if ( f.exists() ) {
                return f;
            }

            Settings.setProperty(defaultProgramProperty, "");
            Settings.setProperty(defaultProgramAutoRunProperty, "OFF");
        }
        return null;
    }

    /**
     * Display the sound menu.
     * Allow the user to change volume and key click volume.
     */
    private void soundMenu() {
        String[] soundMenuData = new String[] {
            "Volume:    ",
            "Key volume: ",
            "Key freq: ",
            "Key length: "
        };
        String[] soundMenuData2 = new String[soundMenuData.length];
        GraphicMenu menu = new GraphicMenu(soundMenuData2, new String[] {
            ICSound,
            ICSound,
            ICSound,
            ICSound
        }, 3);
        int[][] Volumes = {
            {
                Sound.getVolume() / 10,
                784,
                250,
                0
            }, {
                Button.getKeyClickVolume() / 10,
                Button.getKeyClickTone(1) / 200,
                Button.getKeyClickLength() / 10,
                0
            }, {
                Button.getKeyClickVolume() / 10,
                Button.getKeyClickTone(1) / 200,
                Button.getKeyClickLength() / 10,
                0
            }, {
                Button.getKeyClickVolume() / 10,
                Button.getKeyClickTone(1) / 200,
                Button.getKeyClickLength() / 10,
                0
            }
        };
        int selection = 0;
        // Make a note of starting values so we know if they change
        for ( int i = 0; i < Volumes.length; i++ ) {
            Volumes[i][3] = Volumes[i][(i == 0 ? 0 : i - 1)];
        }
        // remember and Turn off tone for the enter key
        int tone = Button.getKeyClickTone(Button.ID_ENTER);
        Button.setKeyClickTone(Button.ID_ENTER, 0);
        do {
            newScreen("Sound");
            for ( int i = 0; i < Volumes.length; i++ ) {
                soundMenuData2[i] = soundMenuData[i] + formatVol(Volumes[i][(i == 0 ? 0 : i - 1)]);
            }
            menu.setItems(soundMenuData2);
            selection = getSelection(menu, selection);
            if ( selection >= 0 ) {
                Volumes[selection][(selection == 0 ? 0 : selection - 1)]++;
                Volumes[selection][(selection == 0 ? 0 : selection - 1)] %= 11;
                if ( selection > 1 && Volumes[selection][(selection == 0 ? 0 : selection - 1)] == 0 ) {
                    Volumes[selection][(selection == 0 ? 0 : selection - 1)] = 1;
                }
                if ( selection == 0 ) {
                    Sound.setVolume(Volumes[0][0] * 10);
                    Sound.playTone(1000, Volumes[selection][1], Volumes[selection][2]);
                } else {
                    Sound.playTone(Volumes[selection][1] * 200, Volumes[selection][2] * 10, Volumes[selection][0] * 10);
                }
            }
        } while ( selection >= 0 );
        // Make sure key click is back on and has new volume
        Button.setKeyClickTone(Button.ID_ENTER, tone);
        Button.setKeyClickVolume(Volumes[1][0] * 10);
        // Save in settings
        if ( Volumes[0][0] != Volumes[0][3] ) {
            Settings.setProperty(Sound.VOL_SETTING, String.valueOf(Volumes[0][0] * 10));
        }
        if ( Volumes[1][0] != Volumes[1][3] ) {
            Settings.setProperty(Button.VOL_SETTING, String.valueOf(Volumes[1][0] * 10));
        }
        if ( Volumes[2][1] != Volumes[2][3] ) {
            Settings.setProperty(Button.FREQ_SETTING, String.valueOf(Volumes[2][1] * 200));
        }
        if ( Volumes[3][2] != Volumes[3][3] ) {
            Settings.setProperty(Button.LEN_SETTING, String.valueOf(Volumes[3][2] * 10));
        }
    }

    /**
     * Format a string for use when displaying the volume.
     *
     * @param vol Volume setting 0-10
     * @return String version.
     */
    private static String formatVol(int vol) {
        if ( vol == 0 ) {
            return "mute";
        }
        if ( vol == 10 ) {
            return "10";
        }
        return new StringBuilder().append(' ').append(vol).toString();
    }

    /**
     * Display system version information.
     */
    private void displayVersion() {
        if ( ORAhandler.isRestarted() == false ) {
            newScreen(OPENROBERTAHEAD);
            lcd.drawString("Firmware updated!", 0, 2);
            lcd.drawString(" Please restart", 0, 4);
            lcd.drawString("   the brick!", 0, 5);
            Delay.msDelay(3000);
        } else {
            newScreen("Version");
            lcd.drawString("Open Roberta:" + getORAmenuVersion(), 0, 2);
            lcd.drawString("leJOS:", 0, 4);
            lcd.drawString(version, 6, 4);
            lcd.drawString(menuProperties.getProperty("buildTimeStamp").split(" ")[0], 0, 6);
            lcd.drawString(menuProperties.getProperty("buildTimeStamp").split(" ")[1], 0, 7);
            getButtonPress();
        }
    }

    /**
     * Read a button press.
     * If the read timesout then exit the system.
     *
     * @return The bitcode of the button.
     */
    private int getButtonPress() {
        long timeoutCnt = (this.timeout == 0 ? Long.MAX_VALUE : (this.timeout * 60000) / 200);
        while ( timeoutCnt-- > 0 ) {
            int value = Button.waitForAnyPress(200);
            if ( value != 0 ) {
                return value;
            }
            if ( suspend ) {
                waitResume();
            }
        }
        shutdown();
        return 0;
    }

    /**
     * Present the menu for a single file.
     *
     * @param file
     */
    private void fileMenu(File file, int type) {
        String fileName = file.getName();
        String ext = Utils.getExtension(fileName);
        int selectionAdd;
        String[] items;
        String[] icons;
        if ( ext.equals("jar") ) {
            selectionAdd = 0;
            items = new String[] {
                "Execute program",
                "Debug program",
                "Set as Default",
                "Delete file"
            };
            icons = new String[] {
                ICProgram,
                ICDebug,
                ICDefault,
                ICDelete
            };
        } else if ( ext.equals("wav") ) {
            selectionAdd = 10;
            items = new String[] {
                "Play sample",
                "Delete file"
            };
            icons = new String[] {
                ICSound,
                ICDelete
            };
        } else {
            selectionAdd = 20;
            items = new String[] {
                "Delete file",
                "View File"
            };
            icons = new String[] {
                ICDelete,
                ICEV3
            };
        }
        newScreen();
        lcd.drawString("Size:", 0, 2);
        lcd.drawString(Long.toString(file.length()), 5, 2);
        GraphicMenu menu = new GraphicMenu(items, icons, 3, fileName, 1);
        int selection = getSelection(menu, 0);
        if ( selection >= 0 ) {
            String directory = null;

            switch ( type ) {
                case TYPE_PROGRAM:
                    directory = PROGRAMS_DIRECTORY;
                    break;
                case TYPE_SAMPLE:
                    directory = SAMPLES_DIRECTORY;
                    break;
                case TYPE_TOOL:
                    directory = TOOLS_DIRECTORY;
                    break;
            }
            switch ( selection + selectionAdd ) {
                case 0:
                    System.out.println("Running program: " + file.getPath());
                    if ( type == TYPE_TOOL ) {
                        execInThisJVM(file);
                    } else {
                        JarFile jar = null;
                        try {
                            jar = new JarFile(file);
                            String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
                            jar.close();
                            exec(file, JAVA_RUN_CP + file.getPath() + " lejos.internal.ev3.EV3Wrapper " + mainClass, directory);
                        } catch ( IOException e ) {
                            System.err.println("Exception running program");
                        }
                    }

                    break;
                case 1:
                    System.out.println("Debugging program: " + file.getPath());
                    JarFile jar = null;
                    try {
                        jar = new JarFile(file);
                        String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
                        jar.close();
                        exec(file, JAVA_DEBUG_CP + file.getPath() + " lejos.internal.ev3.EV3Wrapper " + mainClass, directory);
                    } catch ( IOException e ) {
                        System.err.println("Exception running program");
                    }
                    break;
                case 2:
                    Settings.setProperty(defaultProgramProperty, file.getPath());
                    break;
                case 10:
                    System.out.println("Playing " + file.getPath());
                    Sound.playSample(file);
                    break;
                case 3:
                case 11:
                case 20:
                    file.delete();
                    break;
                case 21:
                    try {
                        Viewer.view(file.getPath());
                    } catch ( IOException e ) {
                        System.err.println("Exception viewing file");
                    }
                    break;
            }
        }
    }

    /**
     * Present the menu for a menu tool.
     *
     * @param file
     */
    private void toolMenu(File file) {
        String fileName = file.getName();
        String ext = Utils.getExtension(fileName);
        if ( ext.equals("jar") ) {
            execInThisJVM(file);
        }
    }

    /**
     * Execute a program and display its output to System.out and error stream to System.err
     */
    private static void exec(File jar, String command, String directory) {
        menu.suspend();
        try {
            if ( jar != null ) {
                String jarName = jar.getName();
                programName = jarName.substring(0, jarName.length() - 4); // Remove .jar
            }

            drawLaunchScreen();

            program = new ProcessBuilder(command.split(" ")).directory(new File(directory)).start();
            BufferedReader input = new BufferedReader(new InputStreamReader(program.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(program.getErrorStream()));

            echoIn = new EchoThread(jar.getPath().replace(".jar", ".out"), input, System.out);
            echoErr = new EchoThread(jar.getPath().replace(".jar", ".err"), err, System.err);

            echoIn.start();
            echoErr.start();

            System.out.println("Executing " + command + " in " + directory);

            while ( true ) {
                int b = Button.getButtons();
                if ( b == 6 ) {
                    System.out.println("Killing the process");
                    program.destroy();
                    // reset motors after program is aborted
                    //resetMotors();
                    break;
                }
                if ( !echoIn.isAlive() && !echoErr.isAlive() ) {
                    break;
                }
                Delay.msDelay(200);
            }
            //System.out.println("Waiting for process to die");;
            program.waitFor();
            System.out.println("Program finished");
            // Turn the LED off, in case left on
        } catch ( Exception e ) {
            System.err.println("Failed to execute program: " + e);
        } finally {
            //resetMotors();
            Button.LEDPattern(0);
            program = null;
            menu.resume();
        }
    }

    /**
     * Execute a program and display its output to System.out and error stream to System.err
     */
    private static void startProgram(String command, File jar) {
        try {
            //System.out.println("Start sus " + GraphicStartup.suspend + " program null " + (program == null));
            if ( program != null ) {
                return;
            }
            String[] args = command.split(" ");
            File directory = jar.getParentFile();

            programName = jar.getName().replace(".jar", "");

            program = new ProcessBuilder(args).directory(directory).start();

            BufferedReader input = new BufferedReader(new InputStreamReader(program.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(program.getErrorStream()));

            echoIn = new EchoThread(jar.getPath().replace(".jar", ".out"), input, System.out);
            echoErr = new EchoThread(jar.getPath().replace(".jar", ".err"), err, System.err);

            echoIn.start();
            echoErr.start();
            menu.suspend();
            drawLaunchScreen();

            System.out.println("Executing " + command + " in " + directory);
        } catch ( Exception e ) {
            System.err.println("Failed to start program: " + e);
            menu.resume();
        }
    }

    @Override
    public void stopProgram() {
        try {
            //System.out.println("Stop sus " + GraphicStartup.suspend + " program null " + (program == null));
            if ( program == null ) {
                return;
            }

            program.destroy();

            //System.out.println("Waiting for process to die");;
            program.waitFor();
            System.out.println("Program finished");
        } catch ( Exception e ) {
            System.err.println("Failed to stop program: " + e);
        } finally {
            //resetMotors();
            // Turn the LED off, in case left on
            Button.LEDPattern(0);
            program = null;
            menu.resume();
        }
    }

    /**
     * Display the files in the file system.
     * Allow the user to choose a file for further operations.
     */
    private void filesMenu() {
        GraphicListMenu menu = new GraphicListMenu(null, null);
        System.out.println("Finding files ...");
        int selection = 0;
        do {
            File[] files = (new File(PROGRAMS_DIRECTORY)).listFiles();
            int len = 0;
            for ( int i = 0; i < files.length && files[i] != null; i++ ) {
                len++;
            }
            if ( len == 0 ) {
                msg("No files found");
                return;
            }
            newScreen("Files");
            String fileNames[] = new String[len];
            String[] icons = new String[len];
            for ( int i = 0; i < len; i++ ) {
                fileNames[i] = files[i].getName();
                String ext = Utils.getExtension(files[i].getName());
                if ( ext.equals("jar") ) {
                    icons[i] = ICMProgram;
                } else if ( ext.equals("wav") ) {
                    icons[i] = ICMSound;
                } else {
                    icons[i] = ICMFile;
                }
            }
            menu.setItems(fileNames, icons);
            selection = getSelection(menu, selection);
            if ( selection >= 0 ) {
                fileMenu(files[selection], TYPE_PROGRAM);
            }
        } while ( selection >= 0 );
    }

    /**
     * Display the tools from the tools directory.
     * Allow the user to choose a file for further operations.
     */
    private void toolsMenu() {
        GraphicListMenu menu = new GraphicListMenu(null, null);
        //System.out.println("Finding files ...");
        int selection = 0;
        do {
            File[] files = new File(TOOLS_DIRECTORY).listFiles();
            int len = 0;
            for ( int i = 0; i < files.length && files[i] != null; i++ ) {
                len++;
            }
            if ( len == 0 ) {
                msg("No tools found");
                return;
            }
            newScreen("Tools");
            String fileNames[] = new String[len];
            String[] icons = new String[len];
            for ( int i = 0; i < len; i++ ) {
                fileNames[i] = formatFileName(files[i].getName());
                String ext = Utils.getExtension(files[i].getName());
                if ( ext.equals("jar") ) {
                    icons[i] = ICMProgram;
                }
            }

            menu.setItems(fileNames, icons);
            selection = getSelection(menu, selection);
            if ( selection >= 0 ) {
                toolMenu(files[selection]);
            }
        } while ( selection >= 0 );
    }

    /**
     * Method to add spaces before capital letters and remove .jar extension.
     *
     * @param fileName
     * @return
     */
    static private String formatFileName(String fileName) {
        StringBuffer formattedName = new StringBuffer("" + fileName.charAt(0));
        for ( int i = 1; i < fileName.length(); i++ ) { //Skip the first letter-can't put space before first word
            if ( fileName.charAt(i) == '.' ) {
                break;
            }
            if ( Character.isUpperCase(fileName.charAt(i)) ) {
                formattedName.append(' ');
            }
            formattedName.append(fileName.charAt(i));

        }
        return formattedName.toString();
    }

    /**
     * Display the samples in the file system.
     * Allow the user to choose a file for further operations.
     */
    private void samplesMenu() {
        GraphicListMenu menu = new GraphicListMenu(null, null);
        //System.out.println("Finding files ...");
        int selection = 0;
        do {
            File[] files = (new File(SAMPLES_DIRECTORY)).listFiles();
            int len = 0;
            for ( int i = 0; i < files.length && files[i] != null; i++ ) {
                len++;
            }
            if ( len == 0 ) {
                msg("No samples found");
                return;
            }
            newScreen("Samples");
            String fileNames[] = new String[len];
            String[] icons = new String[len];
            for ( int i = 0; i < len; i++ ) {
                fileNames[i] = files[i].getName();
                String ext = Utils.getExtension(files[i].getName());
                if ( ext.equals("jar") ) {
                    icons[i] = ICMProgram;
                } else if ( ext.equals("wav") ) {
                    icons[i] = ICMSound;
                } else {
                    icons[i] = ICMFile;
                }
            }
            menu.setItems(fileNames, icons);
            selection = getSelection(menu, selection);
            if ( selection >= 0 ) {
                fileMenu(files[selection], TYPE_SAMPLE);
            }
        } while ( selection >= 0 );
    }

    /**
     * Start a new screen display using the current title.
     */
    private void newScreen() {
        lcd.clear();
        this.ind.updateNow();
    }

    /**
     * Start a new screen display.
     * Clear the screen and set the screen title.
     *
     * @param title
     */
    private void newScreen(String title) {
        this.indiBA.setTitle(title);
        newScreen();
    }

    /**
     * Display a status message
     *
     * @param msg
     */
    private void msg(String msg) {
        newScreen();
        lcd.drawString(msg, 0, 2);
        long start = System.currentTimeMillis();
        int button;
        int buttons = Button.readButtons();
        do {
            Thread.yield();

            int buttons2 = Button.readButtons();
            button = buttons2 & ~buttons;
        } while ( button != Button.ID_ESCAPE && System.currentTimeMillis() - start < 2000 );
    }

    /**
     * If the menu is suspended wait for it to be resumed. Handle any program exit
     * while we wait.
     */
    private void waitResume() {
        while ( suspend ) {
            if ( program != null && !echoIn.isAlive() && !echoErr.isAlive() ) {
                stopProgram();
                break;
            }
            int b = Button.getButtons();
            if ( b == 6 ) {
                if ( program != null ) {
                    stopProgram();
                } else {
                    // should we do this?
                    resume();
                }
                break;
            }
            Delay.msDelay(200);
        }
    }

    /**
     * Obtain a menu item selection
     * Allow the user to make a selection from the specified menu item. If a
     * power off timeout has been specified and no choice is made within this
     * time power off the NXT.
     *
     * @param menu Menu to display.
     * @param cur Initial item to select.
     * @return Selected item or < 0 for escape etc.
     */
    private int getSelection(GraphicMenu menu, int cur) {
        int selection;

        curMenu = menu;

        // If the menu is interrupted by another thread, redisplay
        do {
            selection = menu.select(cur, this.timeout * 60000);
            if ( suspend ) {
                waitResume();
            }
        } while ( selection == -2 );

        if ( selection == -3 ) {
            shutdown();
        }

        return selection;
    }

    @Override
    public String getExecutingProgramName() {
        if ( program == null ) {
            return null;
        }
        return programName;
    }

    /**
     * Shut down the EV3
     */
    @Override
    public void shutdown() {
        System.out.println("Shutting down the EV3");
        suspend();
        try {
            Runtime.getRuntime().exec("init 0");
        } catch ( IOException e ) {
            // Ignore
        }
        lcd.drawString("  Shutting down", 0, 6);
        lcd.refresh();
    }

    class PipeReader extends Thread {

        @Override
        public synchronized void run() {
            try {
                @SuppressWarnings("resource")
                InputStream is = new FileInputStream(MENU_DIRECTORY + "/menufifo");

                while ( true ) {
                    int c = is.read();
                    if ( c < 0 ) {
                        Delay.msDelay(200);
                    } else {
                        System.out.println("Read from fifo: " + c + " " + ((char) c));

                        if ( c == 's' ) {
                            GraphicStartup.this.suspend();
                            System.out.println("Menu suspended");
                        } else if ( c == 'r' ) {
                            GraphicStartup.this.resume();
                            System.out.println("Menu resumed");
                        }
                    }
                }

            } catch ( IOException e ) {
                System.err.println("Failed to read from fifo: " + e);
                return;
            }
        }
    }

    /**
     * Manage the top line of the display.
     * The top line of the display shows battery state, menu titles, and I/O
     * activity.
     */
    class IndicatorThread implements Runnable {
        int displayState = IND_NONE;
        int savedState = IND_NONE;
        Thread thread;

        public IndicatorThread() {
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
        }

        public void start() {
            this.thread.start();
        }

        @Override
        public synchronized void run() {
            try {
                int updateIPCountdown = 0;
                while ( true ) {
                    if ( this.displayState >= IND_NORMAL ) {
                        long time = System.currentTimeMillis();
                        if ( updateIPCountdown <= 0 ) {
                            if ( updateIPAddresses() ) {
                                System.out.println("Address changed");
                                startNetworkServices();
                            }
                            updateIPCountdown = Config.IP_UPDATE;
                        }
                        GraphicStartup.this.indiBA.setWifi(wlanAddress != null);
                        GraphicStartup.this.indiBA.draw(time);
                        if ( this.displayState >= IND_FULL ) {
                            lcd.clear(1);
                            lcd.clear(2);
                            int row = 1;
                            for ( String ip : ips ) {
                                lcd.drawString(ip, 8 - ip.length() / 2, row++);
                            }
                        }
                        lcd.refresh();
                        // wait until next tick
                        time = System.currentTimeMillis();
                        this.wait(Config.ANIM_DELAY - (time % Config.ANIM_DELAY));
                        updateIPCountdown -= Config.ANIM_DELAY;
                    } else {
                        this.wait();
                    }
                }
            } catch ( InterruptedException e ) {
                //just terminate
            }
        }

        /**
         * Update the indicators
         */
        public synchronized void updateNow() {
            this.notifyAll();
        }

        public void setDisplayState(int state) {
            if ( this.displayState != IND_SUSPEND ) {
                this.displayState = state;
                updateNow();
            } else {
                this.savedState = state;
            }
        }

        public void suspend() {
            this.savedState = this.displayState;
            this.displayState = IND_SUSPEND;
            updateNow();
        }

        public void resume() {
            this.displayState = this.savedState;
            updateNow();
        }
    }

    /**
     * Get all the IP addresses for the device, return true if either the wlan or
     * pan address has changed.
     */
    public synchronized boolean updateIPAddresses() {
        //System.out.println("Update IP addresses");
        List<String> result = new ArrayList<String>();
        Enumeration<NetworkInterface> interfaces;
        String oldWlan = wlanAddress;
        String oldPan = panAddress;
        wlanAddress = null;
        panAddress = null;
        ips.clear();
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch ( SocketException e ) {
            System.err.println("Failed to get network interfaces: " + e);
            return false;
        }
        while ( interfaces.hasMoreElements() ) {
            NetworkInterface current = interfaces.nextElement();
            try {
                if ( !current.isUp() || current.isLoopback() || current.isVirtual() ) {
                    continue;
                }
            } catch ( SocketException e ) {
                System.err.println("Failed to get network properties: " + e);
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while ( addresses.hasMoreElements() ) {
                InetAddress current_addr = addresses.nextElement();
                if ( current_addr.isLoopbackAddress() ) {
                    continue;
                }
                result.add(current_addr.getHostAddress());
                //System.out.println("Interface name " + current.getName());
                if ( current.getName().equals(WLAN_INTERFACE) ) {
                    wlanAddress = current_addr.getHostAddress();
                } else if ( current.getName().equals(PAN_INTERFACE) ) {
                    panAddress = current_addr.getHostAddress();
                }
            }
        }
        ips = result;
        // have any of the important addresses changed?
        return !(oldWlan == wlanAddress || (oldWlan != null && wlanAddress != null && wlanAddress.equals(oldWlan)))
            || !(oldPan == panAddress || (oldPan != null && panAddress != null && panAddress.equals(oldPan)));
    }

    public static void drawLaunchScreen() {
        GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
        g.setFont(Font.getDefaultFont());
        g.drawRegion(hourglass, 0, 0, hourglass.getWidth(), hourglass.getHeight(), GraphicsLCD.TRANS_NONE, 50, 65, GraphicsLCD.HCENTER | GraphicsLCD.VCENTER);
        int x = LCD.SCREEN_WIDTH / 2;
        g.drawString("Wait", x, 40, 0);
        g.drawString("a", x, 55, 0);
        g.drawString("second...", x, 70, 0);
        g.refresh(); // TODO: Needed?
    }

    @Override
    public void runProgram(String programName) {
        JarFile jar = null;
        String fullName = PROGRAMS_DIRECTORY + "/" + programName + ".jar";
        try {
            File jarFile = new File(fullName);
            jar = new JarFile(jarFile);
            String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
            jar.close();
            startProgram(JAVA_RUN_CP + fullName + " lejos.internal.ev3.EV3Wrapper " + mainClass, jarFile);
        } catch ( IOException e ) {
            System.err.println("Failed to run program");
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        File f = new File(fileName);
        return f.delete();
    }

    @Override
    public String[] getProgramNames() {
        File[] files = (new File(PROGRAMS_DIRECTORY)).listFiles();
        String[] fileNames = new String[files.length];
        for ( int i = 0; i < files.length; i++ ) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    @Override
    public void runSample(String programName) {
        JarFile jar = null;
        String fullName = SAMPLES_DIRECTORY + "/" + programName + ".jar";
        try {
            File jarFile = new File(fullName);
            jar = new JarFile(jarFile);
            String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
            jar.close();
            startProgram(JAVA_RUN_CP + fullName + " lejos.internal.ev3.EV3Wrapper " + mainClass, jarFile);
        } catch ( IOException e ) {
            System.err.println("Failed to run program");
        }
    }

    @Override
    public void debugProgram(String programName) {
        JarFile jar = null;
        String fullName = PROGRAMS_DIRECTORY + "/" + programName + ".jar";
        try {
            File jarFile = new File(fullName);
            jar = new JarFile(jarFile);
            String mainClass = jar.getManifest().getMainAttributes().getValue("Main-class");
            jar.close();
            startProgram(JAVA_DEBUG_CP + fullName + " lejos.internal.ev3.EV3Wrapper " + mainClass, jarFile);
        } catch ( IOException e ) {
            System.err.println("Failed to run program");
        }
    }

    @Override
    public String[] getSampleNames() {
        File[] files = (new File(SAMPLES_DIRECTORY)).listFiles();
        String[] fileNames = new String[files.length];
        for ( int i = 0; i < files.length; i++ ) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    @Override
    public long getFileSize(String filename) {
        return new File(filename).length();
    }

    @Override
    public boolean uploadFile(String fileName, byte[] contents) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(contents);
            out.close();
            return true;
        } catch ( IOException e ) {
            System.out.println("Failed to upload file: " + e);
            return false;
        }

    }

    private int startWlanInterface() {
        try {
            Process p = Runtime.getRuntime().exec("sh startwlan0.sh", null, new File("/home/roberta"));
            return p.waitFor();
        } catch ( IOException | InterruptedException e ) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void restartMenu() {
        try {
            Runtime.getRuntime().exec("sh restartmenu.sh", null, new File("/home/roberta"));
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void wifiMenu() {
        newScreen("WLAN");
        lcd.drawString("Searching...", 0, 2);
        while ( startWlanInterface() != 0 ) { // returns 1 if no dongle plugged in
            newScreen("WLAN");
            lcd.drawString("Please plug in a", 0, 2);
            lcd.drawString("USB WLAN adapter...", 0, 3);
            lcd.drawString("(ESCAPE to exit)", 0, 7);
            int id = Button.readButtons();
            if ( id == Button.ID_ESCAPE ) {
                lcd.drawString("Interrupted...!    ", 0, 7);
                Delay.msDelay(1000);
                return;
            }
            Delay.msDelay(100);

        }

        GraphicListMenu menu = new GraphicListMenu(null, null);
        System.out.println("Finding access points ...");
        LocalWifiDevice wifi = Wifi.getLocalDevice("wlan0");
        String[] names;
        try {
            names = wifi.getAccessPointNames();
        } catch ( Exception e ) {
            System.err.println("Exception getting access points" + e);
            names = new String[0];
        }
        int selection = 0;

        do {
            int len = names.length;
            if ( len == 0 ) {
                msg("No Access Points found");
                return;
            }
            newScreen("Access Pts");

            menu.setItems(names, null);
            selection = getSelection(menu, selection);
            if ( selection >= 0 ) {
                System.out.println("Access point is " + names[selection]);
                String pwd = Keyboard.getString();
                if ( pwd != null ) {
                    System.out.println("Password is " + pwd);
                    this.waitScreen.begin("Restart\nWiFi\nServices");
                    this.waitScreen.status("Save configuration");
                    WPASupplicant.writeConfiguration(WIFI_BASE, WIFI_CONFIG, names[selection], pwd);
                    startNetwork(START_WLAN, true);
                    this.waitScreen.end();
                }
                selection = -1;
            }
        } while ( selection >= 0 );
    }

    /**
     * Reset all motors to zero power and float state
     * and reset tacho counts
     */
    public static void resetMotors() {
        for ( String portName : new String[] {
            "A",
            "B",
            "C",
            "D"
        } ) {
            Port p = LocalEV3.get().getPort(portName);
            TachoMotorPort mp = p.open(TachoMotorPort.class);
            mp.controlMotor(0, BasicMotorPort.FLOAT);
            mp.resetTachoCount();
            mp.close();
        }
    }

    @Override
    public byte[] fetchFile(String fileName) {
        File f = new File(fileName);
        FileInputStream in;
        try {
            in = new FileInputStream(f);
            byte[] data = new byte[(int) f.length()];
            in.read(data);
            in.close();
            return data;
        } catch ( IOException e ) {
            System.err.println("Failed to fetch file: " + e);
            return null;
        }

    }

    @Override
    public String getSetting(String setting) {
        return Settings.getProperty(setting, null);
    }

    @Override
    public void setSetting(String setting, String value) {
        Settings.setProperty(setting, value);
    }

    @Override
    public void deleteAllPrograms() {
        File dir = new File(PROGRAMS_DIRECTORY);
        for ( String fn : dir.list() ) {
            File aFile = new File(dir, fn);
            System.out.println("Deleting " + aFile.getPath());
            aFile.delete();
        }
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getMenuVersion() {
        return Utils.versionToString(Config.VERSION);
    }

    @Override
    public String getName() {
        return hostname;
    }

    @Override
    public void setName(String name) {
        this.waitScreen.begin("Change\nSystem\nName");
        hostname = name;
        this.waitScreen.status("Save new name");
        // Write host to /etc/hostname
        try {
            PrintStream out = new PrintStream(new FileOutputStream("/etc/hostname"));
            out.println(name);
            out.close();
        } catch ( FileNotFoundException e ) {
            System.err.println("Failed to write to /etc/hostname: " + e);
        }

        try {
            Process p = Runtime.getRuntime().exec("hostname " + hostname);
            int status = p.waitFor();
            System.out.println("hostname returned " + status);
        } catch ( Exception e ) {
            System.err.println("Failed to execute hostname: " + e);
        }

        startNetwork(START_WLAN, false);
        startNetwork(START_PAN, true);
        this.waitScreen.end();
    }

    private void startNetworkServices() {
        // Start the RMI server
        System.out.println("Starting RMI");

        String rmiIP = (wlanAddress != null ? wlanAddress : (panAddress != null ? panAddress : "127.0.0.1"));
        System.out.println("Setting java.rmi.server.hostname to " + rmiIP);
        System.setProperty("java.rmi.server.hostname", rmiIP);

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch ( RemoteException e ) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        try {
            RMIRemoteEV3 ev3 = new RMIRemoteEV3();
            Naming.rebind("//localhost/RemoteEV3", ev3);
            RMIRemoteMenu remoteMenu = new RMIRemoteMenu(menu);
            Naming.rebind("//localhost/RemoteMenu", remoteMenu);
        } catch ( Exception e ) {
            System.err.println("RMI failed to start: " + e);
        }

        // Set the date
        //        try {
        //            String dt = SntpClient.getDate(Settings.getProperty(ntpProperty, "1.uk.pool.ntp.org"));
        //            System.out.println("Date and time is " + dt);
        //            Runtime.getRuntime().exec("date -s " + dt);
        //        } catch ( IOException e ) {
        //            System.err.println("Failed to get time from ntp: " + e);
        //        }
    }

    private void startNetwork(String startup, boolean startServices) {
        try {
            Process p = Runtime.getRuntime().exec(startup);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String statusMsg;
            while ( (statusMsg = input.readLine()) != null ) {
                this.waitScreen.status(statusMsg);
            }
            int status = p.waitFor();
            System.out.println("start returned " + status);
            updateIPAddresses();
            Delay.msDelay(2000);
            if ( startServices ) {
                this.waitScreen.status("Start services");
                startNetworkServices();
                Delay.msDelay(2000);
            }
        } catch ( Exception e ) {
            System.err.println("Failed to execute: " + startup + " : " + e);
            e.printStackTrace();

        }
    }

    private void execInThisJVM(File jar) {
        suspend();
        try {
            LCD.clearDisplay();
            JarMain jarMain = new JarMain(jar);
            jarMain.close();
        } catch ( Exception e ) {
            toolException(e);
            System.err.println("Exception in execution of tool: " + e);
            e.printStackTrace();
        } finally {
            resume();
        }
    }

    private void toolException(Throwable t) {
        Sound.buzz();
        TextLCD lcd = BrickFinder.getDefault().getTextLCD(Font.getSmallFont());
        int offset = 0;
        // Get rid of invocation exception
        if ( t.getCause() != null ) {
            t = t.getCause();
        }
        while ( true ) {
            lcd.clear();
            lcd.drawString("Tool exception:", offset, 1);
            lcd.drawString(t.getClass().getName(), offset, 3);
            if ( t.getMessage() != null ) {
                lcd.drawString(t.getMessage(), offset, 4);
            }

            if ( t.getCause() != null ) {
                lcd.drawString("Caused by:", offset, 5);
                lcd.drawString(t.getCause().toString(), offset, 6);
            }

            StackTraceElement[] trace = t.getStackTrace();
            for ( int i = 0; i < 7 && i < trace.length; i++ ) {
                lcd.drawString(trace[i].toString(), offset, 8 + i);
            }

            lcd.refresh();
            int id = Button.waitForAnyEvent();
            if ( id == Button.ID_ESCAPE ) {
                break;
            }
            if ( id == Button.ID_LEFT ) {
                offset += 5;
            }
            if ( id == Button.ID_RIGHT ) {
                offset -= 5;
            }
            if ( offset > 0 ) {
                offset = 0;
            }
        }
        lcd.clear();
    }

    @Override
    public void suspend() {
        suspend = true;
        this.ind.suspend();
        lcd.clear();
        LCD.setAutoRefresh(false);
        lcd.refresh();
        curMenu.quit();
    }

    @Override
    public void resume() {
        lcd.clear();
        this.ind.resume();
        lcd.refresh();
        LCD.setAutoRefresh(true);
        suspend = false;
    }

    static final Image hourglass = new Image(64, 64, new byte[] {
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xbe,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3c,
        (byte) 0xfe,
        (byte) 0x1f,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x7c,
        (byte) 0xe0,
        (byte) 0x81,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf8,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0x01,
        (byte) 0xe0,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x03,
        (byte) 0xf0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x0f,
        (byte) 0xf8,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0x1f,
        (byte) 0x7c,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x1f,
        (byte) 0x3e,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3e,
        (byte) 0x1e,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3c,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x38,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x38,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x38,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x38,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3c,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3e,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x3f,
        (byte) 0x3f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0x3f,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf8,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xfc,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xfc,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xfe,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0xff,
        (byte) 0x3f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0x1f,
        (byte) 0xfc,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0xf0,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0xff,
        (byte) 0x01,
        (byte) 0xe0,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0x7f,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0xff,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x80,
        (byte) 0x1f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xfe,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x0f,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf8,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xc0,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xe0,
        (byte) 0x01,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0xf0,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0xff,
        (byte) 0x07,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
    });

}
