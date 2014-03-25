package lejos.internal.io;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import lejos.hardware.RemoteBTDevice;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

public class NativeHCI {
	
	public static final int PSCAN = 0x8;
	public static final int ISCAN = 0x10;
	public static final int PISCAN = 0x18;
	public static final int NOSCAN = 0;
	
	public static class DeviceInfo extends Structure implements Structure.ByReference  {  
        public short dev_id;
        public byte[] name = new byte[8];
        public byte[] bdaddr = new byte[6];
        public int flags;
        public byte  type;
        public byte[]features = new byte[8];
        public int pkt_type;
        public int link_policy;
        public int link_mode;
        public short acl_mtu;
        public short acl_pkts;
        public short sco_mtu;
        public short sco_pkts;
        public int[] stat = new int[10];
	}
	
    static class LibBlue {
        native public int hci_get_route(Pointer addr) throws LastErrorException;
        
        native public int hci_open_dev(int dev_id) throws LastErrorException;
        
        native public int hci_inquiry(int dev_id, int len, int num_rsp, Pointer lap, PointerByReference ii, long flags) throws LastErrorException;
        
        native public int hci_send_cmd(int sock, short ogf, short ocf, byte plen, Buffer param) throws LastErrorException;

        native public  int hci_read_remote_name(int dd, byte[] bdaddr, int len, Buffer name, int to) throws LastErrorException;
        
        native public int hci_devinfo(int dev_id, DeviceInfo di) throws LastErrorException;
        
        static {
            try {
                Native.register("bluetooth");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static LibBlue blue = new LibBlue();
    
	public static final int AF_UNIX = 1;
	public static final int SOCK_STREAM = 1;
	public static final int SOCK_RAW = 3;
	
	public static final int PROTOCOL = 0;
	
	public static final int AF_BLUETOOTH = 31;
	
	public static final int BTPROTO_RFCOMM = 3;
	public static final int BTPROTO_HCI = 1;
	
	public static final int SCAN_DISABLED = 0x00;
	public static final int SCAN_INQUIRY = 0x01;
	public static final int SCAN_PAGE = 0x02;
	
	public static final short OGF_HOST_CTL = 0x03;
	public static final short OCF_WRITE_SCAN_ENABLE = 0x001A;
	
	static final int INQUIRY_INFO_SIZE = 14;
	
	public static final int MAX_RSP = 32;
	
	private int deviceId;
	private int socket;
	private PointerByReference refii = new PointerByReference();
	
	private ArrayList<RemoteBTDevice> remoteDevices = new ArrayList<RemoteBTDevice>();
	
	private DeviceInfo deviceInfo = new DeviceInfo();
	
	public NativeHCI() {
		deviceId =  blue.hci_get_route(null);
		socket = blue.hci_open_dev(deviceId);
		blue.hci_devinfo(deviceId, deviceInfo);
	}

	public Collection<RemoteBTDevice> hciInquiry() throws LastErrorException {
		int numRsp = blue.hci_inquiry(deviceId, 8, MAX_RSP, null, refii, 0x0001);	
		Pointer ii = refii.getValue();
		remoteDevices.clear();
		
		for(int i=0;i<numRsp;i++) {			
			byte[] name = new byte[248];		
			byte[] bdaddr =  ii.getByteArray(i*INQUIRY_INFO_SIZE, 6);
			byte[] cod =  ii.getByteArray(i*INQUIRY_INFO_SIZE + 9, 3);
			StringBuilder nameBuilder = new StringBuilder();
			
			blue.hci_read_remote_name(socket, bdaddr, name.length, ByteBuffer.wrap(name), 0);
			
			for(int j=0;j<name.length && name[j] != 0;j++) {
				nameBuilder.append((char) name[j]);
			}
			
			remoteDevices.add(new RemoteBTDevice(nameBuilder.toString(),bdaddr, cod));
		}
		return remoteDevices;
	}
	
	public void hciSetVisible(boolean visible) throws LastErrorException {
		byte[] vis = new byte[1];
		vis[0] = (byte) (visible ? SCAN_PAGE | SCAN_INQUIRY : SCAN_DISABLED);
		
		blue.hci_send_cmd(socket, OGF_HOST_CTL, OCF_WRITE_SCAN_ENABLE, (byte) 1, ByteBuffer.wrap(vis,0,1));	
	}
	
	public DeviceInfo hciGetDeviceInfo() {
		return deviceInfo;
	}
	
	public boolean hcigetVisible() {
		blue.hci_devinfo(deviceId, deviceInfo);
		return (deviceInfo.flags & PISCAN) == PISCAN;
	}
	
	public String getRemoteName(String addr) {
		byte[] bdaddr = new byte[6];
		byte[] name = new byte[248];
		blue.hci_read_remote_name(socket, bdaddr, name.length, ByteBuffer.wrap(name), 0);
		StringBuilder nameBuilder = new StringBuilder();
		for(int j=0;j<name.length && name[j] != 0;j++) {
			nameBuilder.append((char) name[j]);
		}
		
		return nameBuilder.toString();
	}

}
