package lejos.internal.dbus;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.Path;

@DBusInterfaceName("org.bluez.Manager")
public interface Manager extends DBusInterface {
	
	public Path DefaultAdapter();
	
	Path[] ListAdapters();

}
