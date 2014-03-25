package lejos.internal.io;

import java.io.IOError;
import com.sun.jna.Pointer;

/**
 * This class provides access from Java to Linux character devices. It is intended
 * to allow access from Java to the Lego kernel modules which provide access to
 * EV3 hardware features.
 * <p><p>
 * TODO: Find a better way to return memory mapped data for use by Java.
 * @author andy
 *
 */
public class NativeDevice extends NativeFile
{
    /**
     * Create a native device to provide access to the specified character device
     * @param dname name of the character device
     */
    public NativeDevice(String dname)
    {
        super();
        try {
            open(dname, O_RDWR, 0);
        } catch(Exception e)
        {
            throw new IOError(e);
        }
    }

    /**
     * Map a portion of the device into memory and return a pointer which can be
     * used to read/write the device.
     * @param len number of bytes to map
     * @return a pointer that can be used to access the device memory
     */
    public Pointer mmap(long len)
    {
        return super.mmap(len, PROT_READ | PROT_WRITE, MAP_SHARED, 0);
    }
}
