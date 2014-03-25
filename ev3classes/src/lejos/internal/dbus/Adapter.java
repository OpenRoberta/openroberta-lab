package lejos.internal.dbus;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.Path;

@DBusInterfaceName("org.bluez.Adapter")
public interface Adapter extends DBusInterface {
	
	Path CreatePairedDevice(String address, Path agent, String capability);

	/**
     * Returns list of device object paths.
     */
    Path[] ListDevices();
    
}