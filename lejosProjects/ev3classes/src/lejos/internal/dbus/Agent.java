package lejos.internal.dbus;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;

@DBusInterfaceName("org.bluez.Agent")
public interface Agent extends DBusInterface {
    /**
     * This method gets called when the service daemon unregisters the agent. An
     * agent can use it to do cleanup tasks. There is no need to unregister the
     * agent, because when this method gets called it has already been
     * unregistered.
     */
    void Release();

    /**
     * This method gets called when the service daemon needs to get the passkey
     * for an authentication.
     * 
     * The return value should be a string of 1-16 characters length. The string
     * can be alphanumeric.
     */
    String RequestPinCode(Path device);

    /**
     * This method gets called when the service daemon needs to get the passkey
     * for an authentication.
     * 
     * The return value should be a numeric value between 0-999999.
     */
    UInt32 RequestPasskey(Path device);

    /**
     * This method gets called when the service daemon needs to display a
     * passkey for an authentication.
     * 
     * The entered parameter indicates the number of already typed keys on the
     * remote side.
     * 
     * An empty reply should be returned. When the passkey needs no longer to be
     * displayed, the Cancel method of the agent will be called.
     * 
     * During the pairing process this method might be called multiple times to
     * update the entered value.
     */
    void DisplayPasskey(Path device, UInt32 passkey, byte entered);

    /**
     * This method gets called when the service daemon needs to confirm a
     * passkey for an authentication.
     * 
     * To confirm the value it should return an empty reply or an error in case
     * the passkey is invalid.
     */
    void RequestConfirmation(Path device, UInt32 passkey);

    /**
     * This method gets called when the service daemon needs to authorize a
     * connection/service request.
     */
    void Authorize(Path device, String uuid);

    /**
     * This method gets called if a mode change is requested that needs to be
     * confirmed by the user. An example would be leaving flight mode.
     */
    void ConfirmModeChange(String mode);

    /**
     * This method gets called to indicate that the agent request failed before
     * a reply was returned.
     */
    void Cancel();
}
