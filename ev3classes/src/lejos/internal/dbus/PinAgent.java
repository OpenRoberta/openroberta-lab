package lejos.internal.dbus;

import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;

public class PinAgent implements Agent {
	
	private String pin;
	
	public PinAgent(String pin) {
		this.pin = pin;
	}

    public void Authorize(Path device, String uuid) {
    	System.out.println("Authorize called");
    }

    public void ConfirmModeChange(String mode)  {
    	System.out.println("ConfirmModeChange called");
    }

    public void DisplayPasskey(Path device, UInt32 passkey, byte entered) {
    }

    public void RequestConfirmation(Path device, UInt32 passkey) {
    }

    public UInt32 RequestPasskey(Path device) {
    	System.out.println("Request pass key called for " + device);
        return null;
    }

    public String RequestPinCode(Path device)  {
    	System.out.println("Request pin code called for " + device);
        return pin;
    }

    public void Cancel() {
    	System.out.println("Cancel called");
    }

    public void Release() {
    	System.out.println("Release called");
    }

    public boolean isRemote() {
        return false;
    }

}
