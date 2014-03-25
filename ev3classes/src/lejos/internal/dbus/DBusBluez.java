package lejos.internal.dbus;

import java.util.ArrayList;
import java.util.List;

import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;

public class DBusBluez {
	private Manager dbusManager;
	private DBusConnection dbusConn;
	private Path adapterPath;
	private Adapter adapter;
	
	public DBusBluez() throws DBusException {
		dbusConn = DBusConnection.getConnection(DBusConnection.SYSTEM);
		dbusManager = dbusConn.getRemoteObject("org.bluez", "/", Manager.class);
		selectAdapter(dbusManager.DefaultAdapter());
	}
	
    public void selectAdapter(Path adapterPath) throws DBusException {
        adapter = dbusConn.getRemoteObject("org.bluez", adapterPath.getPath(), Adapter.class);
        this.adapterPath = adapterPath;
    }
    
    public boolean authenticateRemoteDevice(String deviceAddress, final String passkey) throws DBusException {
    	 
        String agentPath = "/org/lejos/authenticate/" + getAdapterID() + "/" + deviceAddress.replace(':', '_');

        dbusConn.exportObject(agentPath, new PinAgent(passkey));

        //System.out.println("Calling CreatedPairedDevive on " + deviceAddress + " using agent: " + agentPath);
        try {
            adapter.CreatePairedDevice(deviceAddress, new Path(agentPath), "");
            return true;
        } finally {
            dbusConn.unExportObject(agentPath);
        }
    }
    
    public String getAdapterID() {
        return hciID(adapterPath.getPath());
    }

    private String hciID(String adapterPath) {
        final String bluezPath = "/org/bluez/";
        String path;
        if (adapterPath.startsWith(bluezPath)) {
            path = adapterPath.substring(bluezPath.length());
        } else {
            path = adapterPath;
        }
        int lastpart = path.lastIndexOf('/');
        if ((lastpart != -1) && (lastpart != path.length() -1)) {
            return path.substring(lastpart + 1);
        } else {
            return path;
        }
    }
    
    public List<String> listAdapters() {
        List<String> a = new ArrayList<String>();
        Path[] adapters = dbusManager.ListAdapters();
        if (adapters != null) {
            for (int i = 0; i < adapters.length; i++) {
                a.add(hciID(adapters[i].getPath()));
            }
        }
        return a;
    }
    
    public List<String> listDevices() {
    	List<String> a = new ArrayList<String>();
    	Path[] devices = adapter.ListDevices();
    	for(Path device: devices) {
    		String dev = device.getPath();
    		int ind = dev.indexOf("dev_");
    		if (ind >= 0) a.add(dev.substring(ind+4).replace('_', ':'));
    	}
    	return a;
    }
}
